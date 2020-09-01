package com.plastic305.web.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IClientDAO;
import com.plastic305.web.app.models.entities.Client;

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

}
