package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IClientDAO;
import com.plastic305.web.app.models.dao.IOrderDAO;
import com.plastic305.web.app.models.dao.IProductsDAO;
import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.ProductByDoctAndProc;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;
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
	public Page<Client> findAll(Pageable page) {
		return iCDAO.findAll(page);
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

	@Override @Transactional(readOnly = true)
	public List<Product> findByName(String term) { 
		return iPDAO.findByName(term);
	}
	
	@Override @Transactional(readOnly = true)
	public List<Product> findNotMandatoryByName(String term, Long idD, Long idP) {
		return iPDAO.findNotMandatoryByName(term, idD, idP);
	}

	@Override  @Transactional(readOnly = true)
	public Product findProductById(Long id) {
		return iPDAO.findById(id).orElse(null);
	}

	@Override  @Transactional(readOnly = true)
	public Order findOrderById(Long id) {
		return iODAO.findById(id).orElse(null);
	}
	
	@Override  @Transactional(readOnly = true)
	public Order findOrderByClientId(Long id) {    // mal...un cliente tiene una pila de ordenes
		return iODAO.findOrderByClientId(id);
	}

	@Override  @Transactional(readOnly = true)
	public Order fetchOrderByIdWithClientWithOrderItemWithProduct(Long id) {
		return iODAO.fetchOrderByIdWithClientWithOrderItemWithProduct(id);
	}
	
	@Override @Transactional(readOnly = true)
	public List<Product> findProductsMandatoryByDoctorByProcedure(Long idD, Long idP, int loss) {
		/// PROBANDO
		HashSet<Product> productSet = new HashSet<>();
		List<Product> lll = iPDAO.findProductsMandatoryByDoctorByProcedure(idD, idP); 
		productSet.addAll(lll);
		if (loss==1) {
			Product p = iPDAO.findOneByName("cell");
			productSet.add(p);
		}
		List<Product> ttt= new ArrayList<>() ;
		ttt.addAll(productSet);
		return ttt ;
	}

	@Override @Transactional(readOnly = true)
	public List<Product> findProductsMandatoryAndIncludedByDoctorByProcedure(Long idD, Long idP) {
		return iPDAO.findProductsMandatoryAndIncludedByDoctorByProcedure(idD, idP);
	}
	
	@Override @Transactional(readOnly = true)
	public List<Product> findOldProductsRecommendedByProcedure(Long idP) {
		return iPDAO.findOldProductsRecommendedByProcedure(idP);
	}

	@Override @Transactional(readOnly = true)
	public List<ProductByDoctAndProc> findProductsByProcRecommendedByProcedure(Long idP) {
		List<ProductByDoctAndProc> productByDoctAndProcList= new ArrayList<>() ;
		HashSet<Product> productSet = new HashSet<>();
		for (ProductByDoctAndProc prodByDoctAndProc: iPDAO.findProductsByProcRecommendedByProcedure(idP)) 
			if (!productSet.contains(prodByDoctAndProc.getProduct())) {
				productSet.add(prodByDoctAndProc.getProduct());
				productByDoctAndProcList.add(prodByDoctAndProc);
			}
		return productByDoctAndProcList;
	}
	
	@Override @Transactional(readOnly = true)
	public List<ProductRecommendedByProcedure> findProductsRecommendedByProcedure(Long idP, Long idD) {
		return  iPDAO.findProductsRecommendedByProcedure(idP, idD) ;
	}

	@Override @Transactional(readOnly = true)
	public List<Product> findProductsNotMandatoryAndNotRecommended(Long idP, Long idD) {
		return iPDAO.findProductsNotMandatoryAndNotRecommended(idP, idD);
	}
	

//ASISTENCIAL
//***********	
	@Override @Transactional(readOnly = true)
	public List<String> getConditionsWithValue(Client client, int value) {
		List<String> conditions = new ArrayList<String>();
		for (Suffering c: client.getConditionsList()) 
			if (c.getAccepted() == value) 
				conditions.add(c.getName());// TODO Auto-generated method stub
		return conditions;
	}

	@Override @Transactional(readOnly = true)   // esto cambiar  a JPA
	public String getConditionsListCSV(Client client) {
		String conditions = "";
		for (Suffering c: client.getConditionsList()) 
			conditions += (c.getName() + ", ");
		if (conditions.equals(""))
			return "No health condition";
		else
			return conditions.substring(0, conditions.lastIndexOf(", "));
	}

	@Override @Transactional(readOnly = true)
	public boolean haveRemark(Client client) {
		List<String> conditions = this.getConditionsWithValue(client, 3);
		return conditions!=null;
	}

	@Override @Transactional(readOnly = true)
	public String getRemarksListCSV(Client client) {
		HashSet<String> remarks = new HashSet<>();
		for (Suffering c: client.getConditionsList()) 
			remarks.add(c.getWarning());
		if (remarks.isEmpty())
			return "No remarks";
		else
			return remarks.toString();
	}

	@Override @Transactional(readOnly = true)
	public Double getBMI(Client client) {
		Double bmi = (client.getHeightInches()!=null)?client.getWeight()/Math.pow(client.getHeightFeetOrCentimeters()*12+client.getHeightInches(), 2)*703:
													  client.getWeight()/Math.pow(client.getHeightFeetOrCentimeters()/100, 2);
		return bmi;
	}




}
