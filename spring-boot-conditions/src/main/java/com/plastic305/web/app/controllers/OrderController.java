package com.plastic305.web.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.OrderItem;
import com.plastic305.web.app.models.entities.OrderProcedure;
import com.plastic305.web.app.models.entities.ProcedureCart;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.services.IClientService;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.IProductService;
import com.plastic305.web.app.services.ISufferingService;

@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {
	private static final String changeLine = "\r\n" ;
	private static final String orderClientHeader = "Client pre-order " ;
	private static final String orderDetailsHeader = "Pre-order details" ;
	private static final String recommendedItemsHeader = "Recommended items for: " ;
	private static final String surgeriesHeader = "Choosed surgeries" ;
	private static final String mandatoryItemsHeader = "Mandatory items for Dr. " ;
	private static final String productTextH = "Post-surgical items" ; 
	private static final String productText = "Post-surgical items exist to accomplish goals!\r\n" + 
											  "These items listed below are necessary in order to protect and enhance the outcome of your procedure, prevent the formation "
											  + "of scar tissue, and aide the recovery process. Prepare in advance for a successful recovery by purchasing the necessary "
											  + "post-surgical items in office at the time of pre-op: (See prices below and choose)" ;
	private static final String questionnaireAnswerHeader = "Answers to the questionnaire" ;
	private static final String personalDataHeader = "Personal information" ;
	private static final String HealthConditionHeader = "Health condition" ;
	private static final String opDecision = "Operation decision" ;
	private static final String observationCellByWeightLoss = "Added mandatory Cell Saver item for presenting a history of weight loss surgery. " ;
	private static final String observationCellByMoreOneLipo = "Added mandatory Cell saver due to having performed another similar surgery previously. " ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired ISufferingService sService;
	@Autowired IClientService cService;
	@Autowired IDoctorService dService;
	@Autowired IProcedureService pService;
	@Autowired IProductService prodService; 
	
	//   <<<<<<   IMPLEMENTATION    >>>>>>
	
// ************    UTIL
	/**
	 * 
	 * La anotación ResponseBody suprime cargar la vista y lo que hace es retornar, en este caso convertido a Json, y poblar dentro del Body de la respuesta
	 * logger.info("Llamando con: " + term + "/" + cliente.getP1() + "/" + cliente.getDoctor());
     * return cService.findNotMandatoryByName(term, cliente.getP1(), cliente.getDoctor());	
	 * este último solo me da los que no son obligatorios
	 *  
	 */
	@GetMapping(value = "/load-products/{term}", produces = {"application/json"})
	public @ResponseBody List<Product> loadProducts(@PathVariable String term, Client cliente) { 
		return cService.findByName(term);
	}
// *************** UTIL     
	
	@GetMapping("/order-form/{orderid}")  // 
	public String create(@PathVariable(value = "orderid") Long orderid, Model model, RedirectAttributes flash, SessionStatus st)
	{
		Order order = cService.findOrderById(orderid);
		logger.info("ORDER-CREATE>>>> items count: " + order.getItemList().size());
				
		int cell = 0 ; // no es obligatorio el Cell, si se cambia a 1 si
		String proceduresNames = " ";
		Double procedureFinancedPrice = Double.valueOf(0) ;
		Double procedureCashPrice = Double.valueOf(0) ;
		List<ProcedureCart> proceduresData = new ArrayList<>();
		for (OrderProcedure procedure: order.getProcedureList()) 
		{ 
			proceduresNames += procedure.getProcedure().getName() + ", " ;
			procedureCashPrice += dService.getProcedurePrice(order.getClient().getDoctor(), procedure.getProcedure().getId(), false) ;
			procedureFinancedPrice += dService.getProcedurePrice(order.getClient().getDoctor(), procedure.getProcedure().getId(), true) ;
			
			proceduresData.add(new ProcedureCart(procedure.getProcedure().getName(), 
												 dService.getProcedurePrice(order.getClient().getDoctor(), procedure.getProcedure().getId(), false), 
												 dService.getProcedurePrice(order.getClient().getDoctor(), procedure.getProcedure().getId(), true)));
			
			if (cell == 0 &&   // Si todavía no requiere CellSaver
				procedure.getProcedure().isRequiredCellSaver() &&  // y el procedimiento la requiere 
				(order.getClient().isHasWeightLoss() || order.getClient().isMoreOneLipo()) &&  // y el cliente ha presentado esto 
				!dService.findOne(order.getClient().getDoctor()).isRequiredCellSaver())  // y el doctor no la pide
				cell = 1; 
		}
		proceduresNames = proceduresNames.substring(0, proceduresNames.length()-2);
		
		String mandatoryItemHeader = mandatoryItemsHeader + order.getDoctorName() + " and " + proceduresNames + " procedure(s)";
		String recommendedItemStrHeader = recommendedItemsHeader + proceduresNames + " procedure(s)";
			
		String observationStr = "";
		if (cell == 1 && order.getClient().isHasWeightLoss())
			observationStr = observationCellByWeightLoss + changeLine ;
		if (cell == 1 && order.getClient().isMoreOneLipo())
			observationStr = observationStr  + observationCellByMoreOneLipo ;
		order.setObservation(observationStr);
					
		model.addAttribute("tittle", orderClientHeader);
		model.addAttribute("productText", productText); 
		model.addAttribute("productTexth", productTextH); 
		model.addAttribute("mandatoryItemHeader", mandatoryItemHeader);
		model.addAttribute("procedureHeader", surgeriesHeader);
		model.addAttribute("recommendedItemStrHeader", recommendedItemStrHeader);
		model.addAttribute("totalProcedureCashPrice", procedureCashPrice);  
		model.addAttribute("totalProcedureFinancedPrice", procedureFinancedPrice);  
		model.addAttribute("proceduresData", proceduresData);  

		model.addAttribute("mandatoryItemList", cService.findProductsMandatoryByDoctorByProcedure(order.getClient().getDoctor(), order.getProcedureList(), cell));  
		model.addAttribute("mandatoryAndIncludedItemList", cService.findProductsMandatoryAndIncludedByDoctorByProcedure(order.getClient().getDoctor(), order.getProcedureList()));
		model.addAttribute("recommendedItemList", cService.findProductsRecommendedByProcedure(order.getClient().getDoctor(), order.getProcedureList()));  
		model.addAttribute("restItemList", cService.findProductsNotMandatoryAndNotRecommended(order.getProcedureList(), order.getClient().getDoctor()));
		
		model.addAttribute("order", order);
			
		return "/orders/order-form"; 
	}
	
	/*******************
	// *******DESARROLLANDO******/ 	
		@PostMapping("/order-form")
		public String save(@Valid Order order, BindingResult bR, Model model, 
						   @RequestParam(name = "item_id_m[]", required = false) Long itemIdM[],  
						   @RequestParam(name = "amount_m[]", required = false) Integer amountM[], 
						   @RequestParam(name = "item_id_mi[]", required = false) Long itemIdMI[],
						   @RequestParam(name = "item_id_c[]", required = false) Long itemIdC[],
						   @RequestParam(name = "amount_c[]", required = false) Integer amountC[],
						   RedirectAttributes flash, SessionStatus st) 
		{
			logger.info("<<<<ORDER-UPDATE>>>> order id: " + order.getId());
			logger.info("<<<<ORDER-UPDATE>>>> doctor name: " + order.getDoctorName());
			logger.info("<<<<ORDER-UPDATE>>>> client name: " + order.getClient().getName());
			logger.info("<<<<ORDER-UPDATE>>>> procedures count: " + order.getProcedureList().size());
			logger.info("<<<<ORDER-UPDATE>>>> items count: " + order.getItemList().size());
			
			for (OrderProcedure procedureChoosed: order.getProcedureList()) 
				procedureChoosed.setSubTotal(dService.getProcedurePrice(cService.findOne(order.getClient().getId()).getDoctor(),
						procedureChoosed.getProcedure().getId(), 
						order.isFinanced()));
			logger.info("ORDER<<<< Se tragó los procedimientos");

			OrderItem lineI = null ;
			Product item = null ;
//			
			// Recorrer cada lista de items (Mandatories, includies, Chosen)
			if (itemIdM!=null) // Mandatories
				for (int i = 0; i < itemIdM.length; i++) {			
					item = prodService.findOne(itemIdM[i]);
					lineI = new OrderItem();
					lineI.setAmount(amountM[i]);
					lineI.setProduct(item);
					lineI.setSubTotal();
					order.addItem(lineI);
					logger.info("Mandatory Add --> ID: " + itemIdM[i].toString() + ", Cantidad: " + amountM[i].toString());
				}
			logger.info("ORDER<<<< Se tragó los obligatorios");
			
			if (itemIdMI!=null) ///			Mandatories, includies,
				for (int i = 0; i < itemIdMI.length; i++) {		
					item = prodService.findOne(itemIdMI[i]);
					lineI = new OrderItem();
					lineI.setAmount(0);
					lineI.setProduct(item);
					lineI.setSubTotal();
					order.addItem(lineI);
					logger.info("Included Add --> ID: " + itemIdMI[i].toString());
				}
			logger.info("ORDER<<<< Se tragó los incluidos");
			
			if (itemIdC!=null)  //Chosen
				for (int i = 0; i < itemIdC.length; i++) {     
					item = prodService.findOne(itemIdC[i]);
					lineI = new OrderItem();
					lineI.setAmount(amountC[i]);
					lineI.setProduct(item);
					lineI.setSubTotal();
					order.addItem(lineI);
					logger.info("Choosen Add -->: " + itemIdC[i].toString() + ", Cantidad: " + amountC[i].toString());
				}
			logger.info("ORDER<<<< Se tragó los elegidos");

			cService.saveOrder(order);

			String flashMsg = "Order for the client: \"" + order.getClient().getName() + "\" created successfully!!!";
			flash.addFlashAttribute("success", flashMsg);
			
			st.setComplete();
//			
			return "redirect:/orders/view-order/" + order.getId(); // order.getClient().getId() + "/" +
		}
		
		
		@GetMapping(value = "/view-order/{orderid}") // {clientid}/
		public String view(@PathVariable(value = "orderid") Long orderid, Model model, RedirectAttributes flash, SessionStatus st) { // @PathVariable(value = "clientid") Long clientid, 
			
			logger.info("ORDER<<<< " + orderid);
			
			Order order = null ;
			Client cliente = null;
			
			if (orderid > 0) {
				order = cService.findOrderById(orderid);
				logger.info("ORDER\\view<<<< " + order.getDoctorName() + "  " + order.getClient().getId());
//				if (order == null) {
//					flash.addFlashAttribute("error", "Don't exits order with this Id in the system");
//					return "redirect:/clients/client-list";    // otra cosa tiene q ser
//				}
//				else
					cliente = cService.findOne(order.getClient().getId());
			}
			else {
					flash.addFlashAttribute("error", "Order id must to be <= 0"); 
					return "redirect:/clients/client-list";
				}
			
			//Esto leerlo de un fichero
			List <String> included = Arrays.asList("Drug test", "Nicotine test", "Pregnancy test", "Anesthesia Fees", "Operating Room Fees", "Surgeon's Fees", 
												   "Compression Socks", "All Pre Op Appointments", "All Post Op Appointments", "Prescriptions for medications");
			
			
			model.addAttribute("tittle", orderDetailsHeader);
			model.addAttribute("orderClientHeader", orderClientHeader + cliente.getName());
			model.addAttribute("questionnaireAnswerHeader", questionnaireAnswerHeader);
			model.addAttribute("personalDataHeader", personalDataHeader);
			model.addAttribute("healthConditionHeader", HealthConditionHeader);
			model.addAttribute("opDecision", opDecision);
			
			model.addAttribute("client", cliente);
			model.addAttribute("order", order);
			
			model.addAttribute("conditionList", cliente.getConditionsName());  
			// los incluidos de arriba
			model.addAttribute("included", included);
			
			if (cliente.getDoctor()!=null)  // si es null es pq es una orden ya hecha
				model.addAttribute("doctor", dService.findOne(cliente.getDoctor()).getName());
			else
				model.addAttribute("doctor", order.getDoctorName());
			
			if (cliente.getP1()!=null) // si es null es pq es una orden ya hecha
				model.addAttribute("p1", pService.findOne(cliente.getP1()).getName());
			else
				model.addAttribute("p1", order.getProcedureList().get(0).getProcedure().getName());
			
			if (cliente.getP2() != null) // si es null es pq es una orden ya hecha o pq no hay P2
				model.addAttribute("combo", pService.findOne(cliente.getP2()).getName());
			else if (order.getProcedureList().size()>1)
				model.addAttribute("combo", order.getProcedureList().get(1).getProcedure().getName());
			else
				model.addAttribute("combo", null);

			// para lo del portapapeles
			String text = order.getDoctorName() + ", ";
			for (OrderProcedure procedureChoosed: order.getProcedureList()) 
				text = text.concat(procedureChoosed.getProcedure().getName()).concat(" + ");
			text = text.substring(0, text.length()-2);
			text = text.concat("$").concat(order.getTotalOrder().toString());
			text = text.concat(changeLine);
			text = text.concat("Descripción de lo que se hace en las cirugías");
			text = text.concat(changeLine);
			//Items
			for (OrderItem item: order.getItemList()) 
				text = text.concat(item.getProduct().getName()).concat(changeLine);
			for (String other: included)
				text = text.concat(other).concat(changeLine);
			text = text.concat("Puede fijar este precio con un pequeño depósito de $ 250.00");
			model.addAttribute("text", text);
			
			return "/orders/view-order";
		}
		
}

/*   	PARA LOS ERRORES DE CREAR LA ORDER
 * //			if (bR.hasErrors()) {
//				model.addAttribute("tittle", tittle);
//				model.addAttribute("msg", msg);
//				return "invoice/form";
//			}
//			if (itemId==null || itemId.length==0) {
//				model.addAttribute("tittle", tittle);
//				model.addAttribute("msg", msg);
//				model.addAttribute("error", msgENotLine);
//				return "invoice/form";
//			}
 * 
 * 
 * */
