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
	private static final String tittleProduct = "Post-surgical items" ;
	private static final String orderClientHeader = "Client pre-order " ;
	private static final String orderDetailsHeader = "Pre-order details" ;
	private static final String recommendedItemsHeader = "Recommended items for: " ;
	private static final String mandatoryItemsHeader = "Mandatory items for Dr. " ;
	private static final String sumaryForItemsHeader = "***SUMARY** Based on choice --> Dr. " ;
	private static final String productText = "Post-surgical items exist to accomplish goals!\r\n" + 
											  "These items listed below are necessary in order to protect and enhance the outcome of your procedure, prevent the formation "
											  + "of scar tissue, and aide the recovery process. Prepare in advance for a successful recovery by purchasing the necessary "
											  + "post-surgical items in office at the time of pre-op: (See prices below and choose)" ;
	private static final String questionnaireAnswerHeader = "Answers to the questionnaire" ;
	private static final String personalDataHeader = "Personal information" ;
	private static final String HealthConditionHeader = "Health condition" ;
	private static final String opDecision = "Operation decision" ;

	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired ISufferingService sService;
	@Autowired IClientService cService;
	@Autowired IDoctorService dService;
	@Autowired IProcedureService pService;
	@Autowired IProductService prodService; 
	
	//   <<<<<<   IMPLEMENTATION    >>>>>>
	
// ************    UTIL	
	@GetMapping(value = "/load-products/{term}", produces = {"application/json"})
	public @ResponseBody List<Product> loadProducts(@PathVariable String term, Client cliente) { 
		// La anotación ResponseBody suprime cargar la vista y lo que hace es retornar, en este caso convertido a Json, y poblar dentro del Body de la respuesta
		return cService.findByName(term);										                 
		//logger.info("Llamando con: " + term + "/" + cliente.getP1() + "/" + cliente.getDoctor());
		//return cService.findNotMandatoryByName(term, cliente.getP1(), cliente.getDoctor());	
		// este último solo me da los que no son obligatorios
	}
// *************** UTIL     
	
	
	// *******DESARROLLANDO******    
		@GetMapping("/order-form/{cell}/{clientid}")  // en algún momento pasar el id del cliente
		public String create(@PathVariable(value = "cell") int cell, @PathVariable(value = "clientid") Long clientid, Model model, RedirectAttributes flash, SessionStatus st) {
			String mandatoryItemHeader = "";
			String summaryheader = "";
			String recommendedItemStrHeader ="";
			
			Client cliente = cService.findOne(clientid);
			Order order = new Order();
			order.setClient(cliente);
			
			if (cliente.getP2() != null) { 
				mandatoryItemHeader = mandatoryItemsHeader + dService.findOne(cliente.getDoctor()).getName() + " and " + pService.findOne(cliente.getP1()).getName() + ", " 
			                                               + pService.findOne(cliente.getP2()).getName() + " procedures";
				summaryheader = sumaryForItemsHeader + dService.findOne(cliente.getDoctor()).getName() + " and " + pService.findOne(cliente.getP1()).getName() + ", " 
			                                         + pService.findOne(cliente.getP2()).getName()  + " procedures";
				recommendedItemStrHeader = recommendedItemsHeader + pService.findOne(cliente.getP1()).getName() + " and " + pService.findOne(cliente.getP2()).getName()  + " procedures";
			}
			else {
				mandatoryItemHeader = mandatoryItemsHeader + dService.findOne(cliente.getDoctor()).getName() + " and " + pService.findOne(cliente.getP1()).getName() + " procedure" ;
				summaryheader = sumaryForItemsHeader + dService.findOne(cliente.getDoctor()).getName() + " and " + pService.findOne(cliente.getP1()).getName() + " procedure" ;
				recommendedItemStrHeader = recommendedItemsHeader + pService.findOne(cliente.getP1()).getName() + " procedure";
			}
			model.addAttribute("tittle", tittleProduct);  
			model.addAttribute("productText", productText); 
			model.addAttribute("mandatoryItemHeader", mandatoryItemHeader);
			model.addAttribute("summaryheader", summaryheader);
			model.addAttribute("recommendedItemStrHeader", recommendedItemStrHeader);
			
			model.addAttribute("mandatoryItemList", cService.findProductsMandatoryByDoctorByProcedure(cliente.getDoctor(), cliente.getP1(), cliente.getP2(), cell));  
			model.addAttribute("mandatoryAndIncludedItemList", cService.findProductsMandatoryAndIncludedByDoctorByProcedure(cliente.getDoctor(), cliente.getP1(), cliente.getP2()));
			model.addAttribute("recommendedItemList", cService.findProductsRecommendedByProcedure(cliente.getP1(), cliente.getP2(), cliente.getDoctor()));  //
			model.addAttribute("restItemList", cService.findProductsNotMandatoryAndNotRecommended(cliente.getP1(), cliente.getP2(), cliente.getDoctor()));//
			model.addAttribute("order", order);
			
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
//			if (bR.hasErrors()) {
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
			// Recorrer cada lista de items (Mandatories, includies, Chosen)
			if (itemIdM!=null) 
				for (int i = 0; i < itemIdM.length; i++) {			// Mandatories
					Product p = prodService.findOne(itemIdM[i]);
					OrderItem line = new OrderItem();
					line.setAmount(amountM[i]);
					line.setProduct(p);
					order.addItem(line);
					logger.info("Mandatory Add --> ID: " + itemIdM[i].toString() + ", Cantidad: " + amountM[i].toString());
				}
			if (itemIdMI!=null) 
				for (int i = 0; i < itemIdMI.length; i++) {		///			Mandatories, includies,
					Product p = prodService.findOne(itemIdMI[i]);
					OrderItem line = new OrderItem();
					line.setAmount(0);
					line.setProduct(p);
					order.addItem(line);
					logger.info("Included Add --> ID: " + itemIdMI[i].toString());
				}
			if (itemIdC!=null) 
				for (int i = 0; i < itemIdC.length; i++) {      //Chosen
					Product p = prodService.findOne(itemIdC[i]);
					OrderItem line = new OrderItem();
					line.setAmount(amountC[i]);
					line.setProduct(p);
					order.addItem(line);
					logger.info("Choosen Add -->: " + itemIdC[i].toString() + ", Cantidad: " + amountC[i].toString());
				}
			
			// LA FECHA TIENE Q TENERLA LA ORDEN  NO EL CLIENTE
			cService.saveOrder(order);
			
			String flashMsg = "Order for the client: \"" + order.getClient().getName() + "\" created successfully!!!";
			flash.addFlashAttribute("success", flashMsg);
//			st.setComplete();
			
			return  "redirect:/orders/view-order/" + order.getClient().getId() + "/" + order.getId();  
		}
		
		
		@GetMapping(value = "/view-order/{clientid}/{orderid}")
		public String view(@PathVariable(value = "clientid") Long clientid, @PathVariable(value = "orderid") Long orderid, Model model, RedirectAttributes flash) {
			Order order = null ;
			if (clientid > 0) {
				order = cService.findOrderById(orderid);
				if (order == null) {
					flash.addFlashAttribute("error", "Don't exits order with this Id in the system");
					return "redirect:/clients/client-list";    // otra cosa tiene q ser
				}
			} else {
				flash.addFlashAttribute("error", "Order id must to be <= 0"); 
				return "redirect:/clients/client-list";
			}
			//Esto leerlo de un fichero
			List <String> included = Arrays.asList("Drug test", "Nicotine test", "Pregnancy test", "Anesthesia Fees", "Operating Room Fees", "Surgeon's Fees", 
												   "Compression Socks", "All Pre Op Appointments", "All Post Op Appointments", "Prescriptions for medications");
			
			model.addAttribute("tittle", orderDetailsHeader);
			model.addAttribute("orderClientHeader", orderClientHeader + cService.findOne(clientid).getName());
			model.addAttribute("questionnaireAnswerHeader", questionnaireAnswerHeader);
			model.addAttribute("personalDataHeader", personalDataHeader);
			model.addAttribute("healthConditionHeader", HealthConditionHeader);
			model.addAttribute("opDecision", opDecision);
			model.addAttribute("client", cService.findOne(clientid));
			model.addAttribute("order", cService.findOrderById(orderid));
			model.addAttribute("doctor", dService.findOne(cService.findOne(clientid).getDoctor()).getName());
			model.addAttribute("p1", pService.findOne(cService.findOne(clientid).getP1()).getName());
			if (cService.findOne(clientid).getP2() != null)
				model.addAttribute("combo", pService.findOne(cService.findOne(clientid).getP2()).getName());
			model.addAttribute("conditionList", cService.getConditionsListCSV(cService.findOne(clientid)));  // esto se cambia cuando haga la consulta con JPA
			model.addAttribute("included", included);

			return "/orders/view-order";
		}
		


}
