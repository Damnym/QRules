package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IClientDAO;
import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Suffering;

@Service
public class ClientService implements IClientService {
	@Autowired
	IClientDAO iCDAO;

	@Override
	@Transactional
	public void save(Client nClient) {
		iCDAO.save(nClient);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return (List<Client>) iCDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Client findOne(Long id) {
		return iCDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		iCDAO.deleteById(id);

	}

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
		String conditions = " ";
		for (Suffering c: client.getConditionsList()) 
			conditions += (c.getName() + ", ");
		return conditions.substring(0, conditions.lastIndexOf(", "));
	}

	@Override
	public boolean haveRemark(Client client) {
		List<String> conditions = this.getConditionsWithValue(client, 3);
		return conditions!=null;
	}


}
