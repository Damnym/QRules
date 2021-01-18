package com.plastic305.web.app.services;

import java.util.List;

import com.plastic305.web.app.models.entities.Product;

public interface IProductService {
	
	public void save(Product nProduct);
	
	public List<Product> findAll();
	public Product findOne(Long id);
	
	public void delete(Long id);
	

//	public String getConditionsListCSV(Client client); 
//	public String getRemarksListCSV(Client client); 
//	public boolean haveRemark(Client client);

}
