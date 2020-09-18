package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IClientDAO;
import com.plastic305.web.app.models.dao.IOrderDAO;
import com.plastic305.web.app.models.dao.IProductsDAO;
import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.Suffering;

@Service
public class ClientService implements IClientService {
	@Autowired IClientDAO iCDAO;
	@Autowired IOrderDAO iODAO;
	@Autowired IProductsDAO iPDAO;
	
  /*<<<<< IMPLEMENTATION >>>>>*/
	
	
//GENERAL
//*******
	@Override @Transactional(readOnly = true)
	public List<Client> findAll() { 
		return (List<Client>) iCDAO.findAll(); 
	}
	
	@Override @Transactional(readOnly = true) 
	public Client findOne(Long id) { 
		return iCDAO.findById(id).orElse(null); 
	}
	
	@Override @Transactional 
	public void save(Client nClient) { 
		iCDAO.save(nClient); 
	}
	
	@Override @Transactional
	public void delete(Long id) { 
		iCDAO.deleteById(id); 
	}

	
//COMERCIAL
//*********
	@Override @Transactional
	public void saveOrder(Order order) { 
		iODAO.save(order); 
	}

	@Override @Transactional
	public void deleteOrder(Long id) { 
		iODAO.deleteById(id); 
	}

	@Override @Transactional(readOnly = true)
	public Client fetchClientByIdWithOrder(Long id) { 
		return iCDAO.fetchClientByIdWithOrder(id); 
	}

	@Override
	public List<Product> findByName(String term) { 
		return iPDAO.findByName(term);
	}

	@Override
	public Product findProductById(Long id) {
		return iPDAO.findById(id).orElse(null);
	}

	@Override
	public Order findOrderById(Long id) {
		return iODAO.findById(id).orElse(null);
	}

	@Override
	public Order fetchOrderByIdWithClientWithOrderItemWithProduct(Long id) {
		return iODAO.fetchOrderByIdWithClientWithOrderItemWithProduct(id);
	}
	

//ASISTENCIAL
//***********	
	@Override
	public List<String> getConditionsWithValue(Client client, int value) {
		List<String> conditions = new ArrayList<String>();
		for (Suffering c: client.getConditionsList()) 
			if (c.getAccepted() == value) 
				conditions.add(c.getName());// TODO Auto-generated method stub
		return conditions;
	}

	@Override
	public String getConditionsListCSV(Client client) {
		String conditions = "";
		for (Suffering c: client.getConditionsList()) 
			conditions += (c.getName() + ", ");
		if (conditions.equals(""))
			return "No health condition";
		else
			return conditions.substring(0, conditions.lastIndexOf(", "));
	}

	@Override
	public boolean haveRemark(Client client) {
		List<String> conditions = this.getConditionsWithValue(client, 3);
		return conditions!=null;
	}

	@Override
	public String getRemarksListCSV(Client client) {
		HashSet<String> remarks = new HashSet<>();
		for (Suffering c: client.getConditionsList()) 
			remarks.add(c.getWarning());
		if (remarks.isEmpty())
			return "No remarks";
		else
			return remarks.toString();
	}

	@Override
	public Double getBMI(Client client) {
		Double bmi = (client.getHeightInches()!=null)?client.getWeight()/Math.pow(client.getHeightFeetOrCentimeters()*12+client.getHeightInches(), 2)*703:
													  client.getWeight()/Math.pow(client.getHeightFeetOrCentimeters()/100, 2);
		return bmi;
	}

	


}
