package com.plastic305.web.app.controllers;

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
import com.plastic305.web.app.models.entities.Procedure;
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
	
	
	// *******DESARROLLANDO******    
		@GetMapping("/order-form/{clientid}")  // {cell} es 1 si lleva cellsaver obligatorio 0 sino
		public String create(@PathVariable(value = "clientid") Long clientid, Model model, RedirectAttributes flash, SessionStatus st) {
			String mandatoryItemHeader = "";
			String recommendedItemStrHeader ="";
			int cell = 0 ; // no es obligatorio el Cell, si se cambia a 1 si
			String observationStr = "";
			
			Double p1PC = null, p2PC = null, p1PF = null, p2PF = null;
			String p1N = null, p2N = null;
																	
			Client cliente = cService.findOne(clientid);
			Order order = new Order();
			order.setClient(cliente);
			order.setDateSurgery(cliente.getDate());
	
			if (cliente.getP2() != null) { 
				mandatoryItemHeader = mandatoryItemsHeader + dService.findOne(cliente.getDoctor()).getName() + " and " + pService.findOne(cliente.getP1()).getName() + ", " 
			                                               + pService.findOne(cliente.getP2()).getName() + " procedures";
				recommendedItemStrHeader = recommendedItemsHeader + pService.findOne(cliente.getP1()).getName() + " and " 
			                                                      + pService.findOne(cliente.getP2()).getName()  + " procedures";
				p2N = pService.findOne(cliente.getP2()).getName();                                                  
				p2PC = dService.getProcedurePrice(cliente.getDoctor(), cliente.getP2(), false) ;                                                  
				p2PF = dService.getProcedurePrice(cliente.getDoctor(), cliente.getP2(), true) ;
				
				if (  !(pService.findOne(cliente.getP2()).getName().equals("BBL") || pService.findOne(cliente.getP1()).getName().equals("BBL"))    // No es un BBL
				    && (pService.findOne(cliente.getP2()).isRequiredCellSaver() || (pService.findOne(cliente.getP1()).isRequiredCellSaver())       //   y el procedimiento requiere Cell, es Lipo
				    && (cliente.isHasWeightLoss() || cliente.isMoreOneLipo()))         //   y el cliente ha perdido peso o es mas de una Lipo
				    &&  !dService.findOne(cliente.getDoctor()).isRequiredCellSaver()) // y ya el doctor no lo pide
					cell = 1; 														// es obligatorio el Cell
			}
			else {
				mandatoryItemHeader = mandatoryItemsHeader + dService.findOne(cliente.getDoctor()).getName() + " and " + pService.findOne(cliente.getP1()).getName() + " procedure" ;
				recommendedItemStrHeader = recommendedItemsHeader + pService.findOne(cliente.getP1()).getName() + " procedure";
				if (   !pService.findOne(cliente.getP1()).getName().equals("BBL")    // No es un BBL
					&&  pService.findOne(cliente.getP1()).isRequiredCellSaver()      //   y el procedimiento requiere Cell, es Lipo
					&&	(cliente.isHasWeightLoss() || cliente.isMoreOneLipo())         //   y el cliente ha perdido peso o es mas de una Lipo
				    &&  !dService.findOne(cliente.getDoctor()).isRequiredCellSaver()) // y ya el doctor no lo pide
					cell = 1; 														 // es obligatorio el Cell
			}
			
			p1N = pService.findOne(cliente.getP1()).getName();                                                  
			p1PC = dService.getProcedurePrice(cliente.getDoctor(), cliente.getP1(), false) ;                                                  
			p1PF = dService.getProcedurePrice(cliente.getDoctor(), cliente.getP1(), true) ;
			
			if (cell == 1 && cliente.isHasWeightLoss())
				observationStr = observationCellByWeightLoss + changeLine ;
			if (cell == 1 && cliente.isMoreOneLipo())
				observationStr = observationStr  + observationCellByMoreOneLipo ;
			order.setObservation(observationStr);
						
			model.addAttribute("productText", productText); 
			model.addAttribute("productTexth", productTextH); 
			model.addAttribute("mandatoryItemHeader", mandatoryItemHeader);
			model.addAttribute("procedureHeader", surgeriesHeader);
			model.addAttribute("recommendedItemStrHeader", recommendedItemStrHeader);
			
			model.addAttribute("p1N", p1N);  
			model.addAttribute("p1PC", p1PC);  
			model.addAttribute("p1PF", p1PF);  
			model.addAttribute("p2N", p2N);  
			model.addAttribute("p2PC", p2PC);  
			model.addAttribute("p2PF", p2PF);  
			
			model.addAttribute("mandatoryItemList", cService.findProductsMandatoryByDoctorByProcedure(cliente.getDoctor(), cliente.getP1(), cliente.getP2(), cell));  
//			logger.info("#####" + cService.findProductsMandatoryByDoctorByProcedure(cliente.getDoctor(), cliente.getP1(), cliente.getP2(), cell).get(0).getName());
			
			model.addAttribute("mandatoryAndIncludedItemList", 
								cService.findProductsMandatoryAndIncludedByDoctorByProcedure(cliente.getDoctor(), cliente.getP1(), cliente.getP2()));
//			logger.info("#####" + cService.findProductsMandatoryAndIncludedByDoctorByProcedure(cliente.getDoctor(), cliente.getP1(), cliente.getP2()).get(0).getName());
			model.addAttribute("recommendedItemList", cService.findProductsRecommendedByProcedure(cliente.getP1(), cliente.getP2(), cliente.getDoctor()));  //
			model.addAttribute("restItemList", cService.findProductsNotMandatoryAndNotRecommended(cliente.getP1(), cliente.getP2(), cliente.getDoctor()));//
			model.addAttribute("order", order);
			model.addAttribute("tittle", orderClientHeader);
			
			return "/orders/order-form"; 
		}
		
		
		
	/*******************
	// *******DESARROLLANDO******/ 	
		@PostMapping("/order-form")
		public String save(@Valid Order order, BindingResult bR, Model model, 
						   @RequestParam(name = "item_id_m[]", required = false) Long itemIdM[],  @RequestParam(name = "item_id_mi[]", required = false) Long itemIdMI[],
						   @RequestParam(name = "item_id_c[]", required = false) Long itemIdC[],
						   @RequestParam(name = "amount_m[]", required = false) Integer amountM[], @RequestParam(name = "amount_c[]", required = false) Integer amountC[],
						   RedirectAttributes flash, SessionStatus st) {
			OrderItem lineI = null ;
			OrderProcedure lineP = null ;
			Product item = null ;
			Procedure procedure = null ;
			
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
			
			lineP = new OrderProcedure();
			procedure = pService.findOne(cService.findOne(order.getClient().getId()).getP1());
			lineP.setProcedure(procedure);
			lineP.setSubTotal(dService.getProcedurePrice(cService.findOne(order.getClient().getId()).getDoctor(),
														 cService.findOne(order.getClient().getId()).getP1(), 
														 order.isFinanced()));
			order.addProcedure(lineP);
			if (cService.findOne(order.getClient().getId()).getP2()!=null) {
				lineP = new OrderProcedure();
				procedure = pService.findOne(cService.findOne(order.getClient().getId()).getP2());
				lineP.setProcedure(procedure);
				lineP.setSubTotal(dService.getProcedurePrice(cService.findOne(order.getClient().getId()).getDoctor(),
								  cService.findOne(order.getClient().getId()).getP2(), 
								  order.isFinanced()));
				order.addProcedure(lineP);
			}
			//el doctor
			order.setDoctorName(dService.findOne(cService.findOne(order.getClient().getId()).getDoctor()).getName());
			
			cService.saveOrder(order);

			String flashMsg = "Order for the client: \"" + order.getClient().getName() + "\" created successfully!!!";
			flash.addFlashAttribute("success", flashMsg);
//			st.setComplete();
			
			return  "redirect:/orders/view-order/" +  order.getId(); // order.getClient().getId() + "/" +  
		}
		
		
		@GetMapping(value = "/view-order/{orderid}") // {clientid}/
		public String view(@PathVariable(value = "orderid") Long orderid, Model model, RedirectAttributes flash) { // @PathVariable(value = "clientid") Long clientid, 
			
			Order order = null ;
			Client cliente = null;
			
			if (orderid > 0) {
				order = cService.findOrderById(orderid);
				if (order == null) {
					flash.addFlashAttribute("error", "Don't exits order with this Id in the system");
					return "redirect:/clients/client-list";    // otra cosa tiene q ser
				}
				else
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
