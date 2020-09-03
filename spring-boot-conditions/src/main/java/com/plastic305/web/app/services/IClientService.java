package com.plastic305.web.app.services;

import java.util.List;

import com.plastic305.web.app.models.entities.Client;

public interface IClientService {
	
	public void save(Client nClient);
	
	public List<Client> findAll();
	public Client findOne(Long id);
	
	public void delete(Long id);
	
	public List<String> getConditionsWithValue(Client client, int value); 
	public String getConditionsListCSV(Client client); 
	public String getRemarksListCSV(Client client); 
	public boolean haveRemark(Client client);

}
