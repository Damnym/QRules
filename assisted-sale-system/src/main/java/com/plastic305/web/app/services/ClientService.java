package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IClientDAO;
import com.plastic305.web.app.models.dao.IOrderDAO;
import com.plastic305.web.app.models.dao.IProductsDAO;
import com.plastic305.web.app.models.dao.ISufferingDAO;
import com.plastic305.web.app.models.dao.ISuperOrderDAO;
import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.OrderProcedure;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.ProductByDoctAndProc;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.models.entities.SuperOrder;
import com.plastic305.web.app.models.entities.SuperOrderStatus;

@Service
public class ClientService implements IClientService {
	@Autowired IClientDAO cDAO;
	@Autowired IOrderDAO iODAO;
	@Autowired ISuperOrderDAO sOrderDAO;
	@Autowired IProductsDAO iPDAO;
	@Autowired ISufferingDAO sDAO;
	@Autowired IDoctorService dService;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
  /*<<<<< IMPLEMENTATION >>>>>*/
	
	
//GENERAL
//*******
	@Override @Transactional(readOnly = true)
	public List<Client> findAll() { 
		return (List<Client>) cDAO.findAll(); 
	}
	
	@Override @Transactional(readOnly = true)
	public Page<Client> findAll(Pageable page) {
		return cDAO.findAll(page);
	}
	
	@Override @Transactional(readOnly = true) 
	public Client findOne(Long id) { 
		return cDAO.findById(id).orElse(null); 
	}
	
	@Override @Transactional 
	public void save(Client nClient) { 
		cDAO.save(nClient); 
	}
	
	@Override @Transactional
	public void delete(Long id) { 
		cDAO.deleteById(id); 
	}
	
	@Override
	public List<Client> findByNameRegExp(String term) {
		return cDAO.findByNameRegExp(term);
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
		return cDAO.fetchClientByIdWithOrder(id); 
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
	
	// ESTA VERSIÓN ES RECIBIENDO EL CLIENTE...SE CAMBIÓ POR LA DE ABAJO PARA RECIBIR PARTE DE LA ORDEN
		//***************************************************************************************************
	@Override @Transactional(readOnly = true)
	public List<Product> findProductsMandatoryByDoctorByProcedure(Long idD, Long idP1, Long idP2, int loss) {  // Llamar solo al ByProcedure
		List<Product> mandatoryItemsList = new ArrayList<Product>();
		if (idP2!=null)
			mandatoryItemsList = iPDAO.findProductsMandatoryByDoctorByCombo(idD, idP1, idP2);
		else
			mandatoryItemsList = iPDAO.findProductsMandatoryByDoctorByProcedure(idD, idP1);
		
		for (int i=0; i<mandatoryItemsList.size(); i++)
			for (int j=i+1; j<mandatoryItemsList.size(); j++) 
				if (mandatoryItemsList.get(i).getId().equals(mandatoryItemsList.get(j).getId())) {
					mandatoryItemsList.remove(j);
					break;
				}
		
		if (loss==1) {
			Product p = iPDAO.findOneByName("cell");
			mandatoryItemsList.add(p);
		}
		
		return mandatoryItemsList;
	}

	@Override @Transactional(readOnly = true)
	public List<Product> findProductsMandatoryByDoctorByProcedure(Long idD, List<OrderProcedure> procedureList, int loss)  // Llamar solo al ByProcedure
	{ 
		List<Product> mandatoryItemsList = new ArrayList<Product>();
		
		for (OrderProcedure procedure: procedureList) 
			mandatoryItemsList.addAll(iPDAO.findProductsMandatoryByDoctorByProcedure(idD, procedure.getProcedure().getId()));
		
		Product cellSaver = iPDAO.findOneByName("cell");
		if (loss==1) 
			mandatoryItemsList.add(cellSaver);
		else
			for (int i=0; i<mandatoryItemsList.size(); i++)
				if (mandatoryItemsList.get(i).getId() == cellSaver.getId())
					mandatoryItemsList.remove(i);
		
		for (int i=0; i<mandatoryItemsList.size(); i++)
		{
			int j = i+1 ;
			while (j<mandatoryItemsList.size())
				if (mandatoryItemsList.get(i).getId().equals(mandatoryItemsList.get(j).getId())) 
					mandatoryItemsList.remove(j);
				else
					j++;
		}
		
		return mandatoryItemsList;
	}
	
	@Override @Transactional(readOnly = true)
	public List<Product> findProductsMandatoryAndIncludedByDoctorByProcedure(Long idD, List<OrderProcedure> procedureList) 
	{//		logger.info("AQUI!!!!!! <<<<>>>>>> ");

		List<Product> mandatoryAndIncludedItemsList = new ArrayList<Product>();
		for (OrderProcedure procedure: procedureList) 
			mandatoryAndIncludedItemsList.addAll(iPDAO.findProductsMandatoryAndIncludedByDoctorByProcedure(idD, procedure.getProcedure().getId()));
		
		for (int i=0; i<mandatoryAndIncludedItemsList.size(); i++)
		{
			int j = i+1 ;
			while (j<mandatoryAndIncludedItemsList.size())
			{
				if (mandatoryAndIncludedItemsList.get(i).getId().equals(mandatoryAndIncludedItemsList.get(j).getId())) 
					mandatoryAndIncludedItemsList.remove(j);
				else
					j++;
			}
		}
		return mandatoryAndIncludedItemsList;
	}
	
	
	@Override @Transactional(readOnly = true)
	public List<ProductRecommendedByProcedure> findProductsRecommendedByProcedure(Long idD, List<OrderProcedure> procedureList) 
	{
		List<ProductRecommendedByProcedure> recommendedItemsList = new ArrayList<ProductRecommendedByProcedure>();
		for (OrderProcedure procedure: procedureList) 
			recommendedItemsList.addAll(iPDAO.findProductsRecommendedByProcedure(procedure.getProcedure().getId(), idD));
		
		for (int i=0; i<recommendedItemsList.size(); i++)
		{
			int j = i+1 ;
			while (j<recommendedItemsList.size())
			{
				if (recommendedItemsList.get(i).getProduct().getId().equals(recommendedItemsList.get(j).getProduct().getId())) 
					recommendedItemsList.remove(j);
				else
					j++;
			}
		}
		return recommendedItemsList;
	}
	
	
	@Override @Transactional(readOnly = true)
	public List<Product> findProductsNotMandatoryAndNotRecommended(List<OrderProcedure> procedureList, Long idD, int loss) 
	{ //logger.info("CService Producto >>>>> " + product1.getName() + "(" + product1.getId() + ")");
		List<Product> productsNotMandatoryAndNotRecommended = (List<Product>) iPDAO.findAll();
		
		List<Product> prevProducts= this.findProductsMandatoryByDoctorByProcedure(idD, procedureList, loss);
		prevProducts.addAll(this.findProductsMandatoryAndIncludedByDoctorByProcedure(idD, procedureList));
		for(ProductRecommendedByProcedure pRecomended:  this.findProductsRecommendedByProcedure(idD, procedureList))
			prevProducts.add(pRecomended.getProduct());
		
		for (int i=0; i<prevProducts.size(); i++)
		{
			int j = 0 ;
			while (j<productsNotMandatoryAndNotRecommended.size() && !productsNotMandatoryAndNotRecommended.get(j).getId().equals(prevProducts.get(i).getId()))
				j++;
			if(j<productsNotMandatoryAndNotRecommended.size())
				productsNotMandatoryAndNotRecommended.remove(j);
		}
		
		return productsNotMandatoryAndNotRecommended;
	}
	
	
	@Override @Transactional(readOnly = true)
	public List<Product> findProductsMandatoryAndIncludedByDoctorByProcedure(Long idD, Long idP1, Long idP2) {
		List<Product> mandatoryAndIncludedItemsList = new ArrayList<Product>();
		if (idP2!=null)
			mandatoryAndIncludedItemsList = iPDAO.findProductsMandatoryAndIncludedByDoctorByCombo(idD, idP1, idP2);
		else
			mandatoryAndIncludedItemsList = iPDAO.findProductsMandatoryAndIncludedByDoctorByProcedure(idD, idP1);
		
		for (int i=0; i<mandatoryAndIncludedItemsList.size(); i++)
			for (int j=i+1; j<mandatoryAndIncludedItemsList.size(); j++) 
				if (mandatoryAndIncludedItemsList.get(i).getId().equals(mandatoryAndIncludedItemsList.get(j).getId())) {
					mandatoryAndIncludedItemsList.remove(j);
					break;
				}
		return mandatoryAndIncludedItemsList;
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
	public List<ProductRecommendedByProcedure> findProductsRecommendedByProcedure(Long idP1, Long idP2, Long idD) {
		List<ProductRecommendedByProcedure> recommendedItemsList = new ArrayList<ProductRecommendedByProcedure>();
		if (idP2!=null)
			recommendedItemsList = iPDAO.findProductsRecommendedByCombo(idP1, idD, idP2);
		else
			recommendedItemsList = iPDAO.findProductsRecommendedByProcedure(idP1, idD);
		
		for (int i=0; i<recommendedItemsList.size(); i++)
			for (int j=i+1; j<recommendedItemsList.size(); j++) 
				if (recommendedItemsList.get(i).getProduct().getId().equals(recommendedItemsList.get(j).getProduct().getId())) {
					recommendedItemsList.remove(j);
					break;
				}
		return recommendedItemsList;
	}
	
	@Override @Transactional(readOnly = true)
	public List<Product> findProductsNotMandatoryAndNotRecommended(Long idP1, Long idP2, Long idD) {
		if (idP2!=null)
			return iPDAO.findProductsNotMandatoryAndNotRecommended(idP1, idD, idP2);
		else
			return iPDAO.findProductsNotMandatoryAndNotRecommended(idP1, idD);
	}
	

//ASISTENCIAL
//***********	
	@Override @Transactional(readOnly = true)
	public List<String> getConditionsWithValue(Client client, int value) {
/*		List<String> conditions = new ArrayList<String>();
		for (Suffering c: client.getConditionsList()) 
			if (c.getAccepted() == value) 
				conditions.add(c.getName()); */
		return null;
	}
	
	
	@Override @Transactional(readOnly = true)
	public List<String> getConditionsWithValueNewAll(Client client, int value) {
		List<String> conditions = new ArrayList<String>();
		int valueLocal = 0 ;
		for (Suffering c: client.getConditionsList()) 
		{
			if (sDAO.getDoctorCountsByConditionsId(c.getId()) == 0) 
				valueLocal = 0 ;
			else if (sDAO.getDoctorCountsByConditionsId(c.getId()) < dService.findAll().size()) 
					valueLocal = 2 ;
				else if ((sDAO.getDoctorCountsByConditionsId(c.getId()) == dService.findAll().size()) && (c.getWarning() !=null && !c.getWarning().isBlank())) 
						valueLocal = 3 ;
					else 
						valueLocal = 1;
			if (valueLocal == value) 
				conditions.add(c.getName());
		}
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

	@Override 
	public String getConditionsListCSV4Save(Client client) {
		String conditions = "";
		for (Suffering c: client.getConditionsList()) 
			conditions += (c.getName()+ ",");   // estaba Name() idem al de arriba
 		if (conditions.equals(""))
			return "No health condition";
		else
			return conditions.substring(0, conditions.lastIndexOf(","));
	}
	
	
	@Override @Transactional(readOnly = true)
	public boolean haveRemark(Client client) {
		List<String> conditions = this.getConditionsWithValueNewAll(client, 3);  // otra forma de verlo
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
	
	
	private List <Suffering> conditionToStr(Client cliente)
	{
		List <Suffering> sList = new ArrayList<Suffering>();
		if (cliente.getConditionsName()!=null && !cliente.getConditionsName().isBlank()) {
			String[] conditions = cliente.getConditionsName().split(",");
			for (String condition: conditions) 
				sList.add(sDAO.findByName(condition));
		}
		return sList;
	}

	@Override
	public void prepare(Client cliente) {  
		cliente.setAccepted(null);   // es aceptado?
		cliente.setDate(null);       // fecha elegida
		cliente.setDoctor(null);     
		cliente.setHbg(null);
		cliente.setP1(null);
		cliente.setP2(null);
		cliente.setWeight(null);
		
		cliente.setConditionsList(conditionToStr(cliente));
		cliente.getAditionalProcedures().clear();
	}
	
	@Override
	public void reOrder(Client cliente) 
	{  
		cliente.setDoctor(null);     
		cliente.setP1(null);
		cliente.setP2(null);
		
		cliente.setConditionsList(conditionToStr(cliente));
		cliente.getAditionalProcedures().clear();
	}

	@Override
	public List<Product> mandatoryScrubber(List<Product> l1, List<Product> l2) 
	{
		for (int i=0; i<l2.size(); i++)
			if ( l1.indexOf(l2.get(i))!=-1 )
				l1.add(l2.get(i));
		return l1 ;
	}

	@Override @Transactional
	public void saveSuperOrder(SuperOrder sOrder) {
		sOrderDAO.save(sOrder);
	}

	@Override @Transactional
	public void deleteSuperOrder(Long id) {
		sOrderDAO.deleteById(id);
	}

	@Override @Transactional(readOnly = true)
	public SuperOrder getSuperOrderById(Long id) {
		return sOrderDAO.findById(id).orElse(null);
	}

	@Override @Transactional(readOnly = true)
	public SuperOrder getSuperOrderWithSpecificStatusByClient(Long idC, SuperOrderStatus st) {
		return cDAO.getSuperOrderWithSpecificStatusByClient(idC, st);
	}

	@Override
	public void UpdateFromInProcessToPending(Long cId) 
	{
		SuperOrder so = sOrderDAO.SelectInProcess(SuperOrderStatus.IN_PROCESS, cId);
		if (so != null)
		{
			so.setStatus(SuperOrderStatus.PENDING);
			this.saveSuperOrder(so);
		}
	}

}