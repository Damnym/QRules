package com.plastic305.web.app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.ProductByDoctAndProc;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;

public interface IClientService {
	
//GENERAL
//*******
	public List<Client> findAll();
	public Page<Client> findAll(Pageable page);
	public Client findOne(Long id);

	public void save(Client nClient);
	public void delete(Long id);

//COMERCIAL
//*********
	public void saveOrder(Order order);
	public void deleteOrder(Long id);
	
	public Client fetchClientByIdWithOrder(Long id);

	public List<Product> findByName(String term);
	public List<Product> findNotMandatoryByName(String term, Long idD, Long idP);
	
	public List<Product> findProductsMandatoryByDoctorByProcedure(Long idD, Long idP, int loss);
	public List<Product> findProductsMandatoryAndIncludedByDoctorByProcedure(Long idD, Long idP);
	public List<ProductRecommendedByProcedure> findProductsRecommendedByProcedure(Long idP, Long idD);
	public List<Product> findProductsNotMandatoryAndNotRecommended(Long idP, Long idD); 
	public Product findProductById(Long id);

	public Order findOrderById(Long id);
	public Order findOrderByClientId(Long id);
	public Order fetchOrderByIdWithClientWithOrderItemWithProduct(Long id);
	

//ASISTENCIAL
//***********
	public List<String> getConditionsWithValue(Client client, int value); 
	public String getConditionsListCSV(Client client); 
	public String getRemarksListCSV(Client client); 
	public boolean haveRemark(Client client);
	
	public Double getBMI(Client client);

	
//NO SE USAN
//***********	
	
	public List<Product> findOldProductsRecommendedByProcedure(Long idP);//   NOOOOOOOOOOOOO se usa
	public List<ProductByDoctAndProc> findProductsByProcRecommendedByProcedure(Long idP); //   NOOOOOOOOOOOOO se usa
	
}
