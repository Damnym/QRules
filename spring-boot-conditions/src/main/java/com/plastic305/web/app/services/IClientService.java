package com.plastic305.web.app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.OrderProcedure;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.ProductByDoctAndProc;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;
import com.plastic305.web.app.models.entities.SuperOrder;
import com.plastic305.web.app.models.entities.SuperOrderStatus;

public interface IClientService {
	
//GENERAL
//*******
	public List<Client> findAll();
	public List<Client> findByNameRegExp(String term);
	public Page<Client> findAll(Pageable page);
	public Client findOne(Long id);

	public void save(Client nClient);
	public void delete(Long id);
	
	public void prepare(Client client);
	public void reOrder(Client client);

//COMERCIAL
//*********
	//Orden
	//******
	public void saveOrder(Order order);
	public void deleteOrder(Long id);
	
	public Client fetchClientByIdWithOrder(Long id);
	
	public Order findOrderById(Long id);
	public Order findOrderByClientId(Long id);
	public Order fetchOrderByIdWithClientWithOrderItemWithProduct(Long id);
	
	//Super-Orden
	//***********
	public void saveSuperOrder(SuperOrder sOrder);
	public void deleteSuperOrder(Long id);
	public SuperOrder getSuperOrderById(Long id);
	public SuperOrder getSuperOrderWithSpecificStatusByClient(Long idC, SuperOrderStatus st);
	
	
	//Productos
	//*********
	public List<Product> findByName(String term);
	public Product findProductById(Long id);
	public List<Product> findNotMandatoryByName(String term, Long idD, Long idP);
	
	public List<Product> findProductsMandatoryByDoctorByProcedure(Long idD, Long idP1, Long idP2, int loss);  //
	public List<Product> findProductsMandatoryByDoctorByProcedure(Long idD, List<OrderProcedure> procedureList, int loss);  //
	public List<Product> findProductsMandatoryAndIncludedByDoctorByProcedure(Long idD, Long idP1, Long idP2);  //
	public List<Product> findProductsMandatoryAndIncludedByDoctorByProcedure(Long idD, List<OrderProcedure> procedureList);
	public List<ProductRecommendedByProcedure> findProductsRecommendedByProcedure(Long idP1, Long idP2, Long idD);  //
	public List<ProductRecommendedByProcedure> findProductsRecommendedByProcedure(Long idD, List<OrderProcedure> procedureList);  //
	public List<Product> findProductsNotMandatoryAndNotRecommended(Long idP1, Long idP2, Long idD); //
	public List<Product> findProductsNotMandatoryAndNotRecommended(List<OrderProcedure> procedureList, Long idD, int loss); //
	
	public List<Product> mandatoryScrubber(List<Product> l1, List<Product> l2);

//ASISTENCIAL
//***********
	public List<String> getConditionsWithValue(Client client, int value); 
	public List<String> getConditionsWithValueNewAll(Client client, int value);
	
	public String getConditionsListCSV(Client client); 
	public String getConditionsListCSV4Save(Client client);
	public String getRemarksListCSV(Client client); 
	public boolean haveRemark(Client client);
	
	public Double getBMI(Client client);
	
//NO SE USAN
//***********	
	public List<Product> findOldProductsRecommendedByProcedure(Long idP);//   NOOOOOOOOOOOOO se usa
	public List<ProductByDoctAndProc> findProductsByProcRecommendedByProcedure(Long idP); //   NOOOOOOOOOOOOO se usa
	
}
