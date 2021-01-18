package com.plastic305.web.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Calendar;
import java.util.Date;
//import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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

import com.plastic305.web.app.models.dto.ProductRecommendedByProcedureDTO;
import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.EnumPayMethods;
import com.plastic305.web.app.models.entities.EnumPromoTo;
//import com.plastic305.web.app.models.entities.EnumPromotionType;
import com.plastic305.web.app.models.entities.ItemsAmountForVIP;
import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.OrderItem;
import com.plastic305.web.app.models.entities.OrderProcedure;
import com.plastic305.web.app.models.entities.ProcedureCart;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;
import com.plastic305.web.app.models.entities.SuperOrder;
import com.plastic305.web.app.models.entities.SuperOrderStatus;
import com.plastic305.web.app.models.entities.VIPDoctorProcedure;
import com.plastic305.web.app.services.IClientService;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.IProductService;
import com.plastic305.web.app.services.IPromo305Service;
import com.plastic305.web.app.services.ISufferingService;

@Secured("ROLE_USER")
@Controller
@RequestMapping("/orders")
@SessionAttributes("superOrder")
public class OrderController 
{
	private static final String changeLine = "\r\n" ;
	private static final String orderClientHeader = "Patient pre-order " ;
	private static final String patientHeader = "Patient name: " ;
	private static final String orderDetailsHeader = "Pre-order details" ;
	private static final String summaryHeader = "Surgery's and items summary" ;
	private static final String itemsheader = "Items summary" ;
	private static final String recommendedItemsHeader = "Recommended items for: " ;
	private static final String surgeriesHeader = "Choosed surgeries" ;
//	private static final String mandatoryItemsHeader = "Mandatory items for Dr. " ;
	private static final String productTextH = "Post-surgical items" ; 
	private static final String productText = "Post-surgical items exist to accomplish goals!\r\n" + 
											  "These items listed below are necessary in order to protect and enhance the outcome of your procedure, prevent the formation "
											  + "of scar tissue, and aide the recovery process. Prepare in advance for a successful recovery by purchasing the necessary "
											  + "post-surgical items in office at the time of pre-op: (See prices below and choose)" ;
	private static final String personalDataHeader = "Personal information" ;
	private static final String HealthConditionHeader = "Health condition" ;
	private static final String opDecision = "Surgery decision" ;
//	private static final String observationCellByWeightLoss = "Added mandatory Cell Saver item for presenting a history of weight loss surgery. " ;
//	private static final String observationCellByMoreOneLipo = "Added mandatory Cell saver due to having performed another similar surgery previously. " ;
	private static final String drain = " procedure need drain. " ;
	private static final String timeRecoveryB = "You must stay " ;
	private static final String timeRecoveryE = " days in Miami for recovery." ;
	private static final String recoveryH = " Take this into account for time to book at recovery house." ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired ISufferingService sService;
	@Autowired IClientService cService;
	@Autowired IDoctorService dService;
	@Autowired IProcedureService pService;
	@Autowired IProductService prodService; 
	@Autowired IPromo305Service promoService; 
	
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
	public @ResponseBody List<Product> loadProducts(@PathVariable String term, Client cliente) 
	{ 
		return cService.findByName(term);
	}

	
	@GetMapping("/order-form/{sorderid}")  // 
	public String create(@PathVariable(value = "sorderid") Long sOrderId, Model model, RedirectAttributes flash, SessionStatus st)
	{   //logger.info("ORDER-CREATE>>>> items count: " + order.getItemList().size());
		
		//Obtengo la orden actual
		SuperOrder superOrder = cService.getSuperOrderById(sOrderId);
		Order order = superOrder.getOrderList().get(superOrder.getOrderList().size()-1);
		
		String proceduresNames = " ";
		String observationStr = order.getObservation()!=null? order.getObservation(): " ";;
		Integer minRecoveryTime = 0 ;
		Integer maxRecoveryTime = 0 ;
		Integer promoSize = 0 ;
		
		List<ProcedureCart> proceduresData = new ArrayList<>();

		Double procedureFinancedPrice = Double.valueOf(0) ;
		Double singleProcedureFinancedPrice = Double.valueOf(0) ;
		Double procedureCashPrice = Double.valueOf(0) ;
		Double singleProcedureCashPrice = Double.valueOf(0) ;
		Double procedureVIPPrice = Double.valueOf(0) ;
		Double procedureVIPTotalPrice = Double.valueOf(0) ;
		
		boolean hasPromo = false ;
		List<VIPDoctorProcedure> promosDoctorNProc = new ArrayList<VIPDoctorProcedure>();
		VIPDoctorProcedure singlePromoDoctorNProc = null;
		
		// por cada procedimiento de la orden
		for (OrderProcedure procedure: order.getProcedureList()) 
		{ 
//			Obtengo el nombre
			proceduresNames += procedure.getProcedure().getName() + ", " ;
			logger.info("proceduresNames: " + proceduresNames);
			// calculo precio total teniendo en cuenta si está incluido
			if (!procedure.getIncludedInPrice()) 
			{
				procedureCashPrice += dService.getProcedurePrice(superOrder.getClient().getDoctor(), procedure.getProcedure().getId(), false) ;
				procedureFinancedPrice += dService.getProcedurePrice(superOrder.getClient().getDoctor(), procedure.getProcedure().getId(), true) ;
			}
			
			// observaciones de drenaje y tiempo de recuperacion
			observationStr += (dService.getProcByDoct(superOrder.getClient().getDoctor(), procedure.getProcedure().getId()).getHasDrains())? 
								procedure.getProcedure().getName() + drain + changeLine:"";
			minRecoveryTime = (dService.getProcByDoct(superOrder.getClient().getDoctor(), procedure.getProcedure().getId()).getPostSurgeryMinStayTime() > minRecoveryTime)? 
								   dService.getProcByDoct(superOrder.getClient().getDoctor(), procedure.getProcedure().getId()).getPostSurgeryMinStayTime(): minRecoveryTime;
			maxRecoveryTime = (dService.getProcByDoct(superOrder.getClient().getDoctor(), procedure.getProcedure().getId()).getPostSurgeryMaxStayTime() > maxRecoveryTime)? 
								   dService.getProcByDoct(superOrder.getClient().getDoctor(), procedure.getProcedure().getId()).getPostSurgeryMaxStayTime(): maxRecoveryTime;
			
			// veo si hay promos 
			promosDoctorNProc = promoService.findActivePromosForDoctorAndProcedure(new Date(), superOrder.getClient().getDoctor(), procedure.getProcedure().getId());
			
			// precio para promos si hay
			if (promosDoctorNProc.size()>0) 
			{
				hasPromo = true ;
				singlePromoDoctorNProc = promosDoctorNProc.get(0) ;
				switch (singlePromoDoctorNProc.getType()) 
				{
				case NEW_PROCEDURE_PRICE:
					procedureVIPPrice = singlePromoDoctorNProc.getDiscount() ;
					break;
				case PRICE_DISCOUNT:
					procedureVIPPrice = dService.getProcedurePrice(superOrder.getClient().getDoctor(), procedure.getProcedure().getId(), false) -
											singlePromoDoctorNProc.getDiscount() ;
					break;
				case PERCENT_DISCOUNT:
					procedureVIPPrice = dService.getProcedurePrice(superOrder.getClient().getDoctor(), procedure.getProcedure().getId(), false) * 
											(1-singlePromoDoctorNProc.getDiscount()/100) ;
					break;
				}
			}
			// sino precio cash
			else  
				procedureVIPPrice = procedureCashPrice;
			procedureVIPTotalPrice += procedureVIPPrice ;
			
			// adicionar el proc al carrito
			if (!procedure.getIncludedInPrice())
			{
				singleProcedureCashPrice = dService.getProcedurePrice(superOrder.getClient().getDoctor(), procedure.getProcedure().getId(), false) ;
				singleProcedureFinancedPrice= dService.getProcedurePrice(superOrder.getClient().getDoctor(), procedure.getProcedure().getId(), true) ;
			}
			else
			{
				singleProcedureCashPrice = Double.valueOf(0) ;
				singleProcedureFinancedPrice= Double.valueOf(0) ;
			}
			promoSize = promoService.countPromoTypeForDoctorAndProcedure(new Date(), superOrder.getClient().getDoctor(), procedure.getProcedure().getId()).size();
			proceduresData.add(new ProcedureCart(procedure.getProcedure().getName(), singleProcedureCashPrice, singleProcedureFinancedPrice, procedureVIPPrice, 
												 procedure.getProcedure().getId(), promoSize));
		}
		proceduresNames = proceduresNames.substring(0, proceduresNames.length()-2);
		
		String recommendedItemStrHeader = recommendedItemsHeader + " for surgery" ; //proceduresNames + " procedure(s)";
		observationStr += timeRecoveryB + minRecoveryTime + "-" + maxRecoveryTime + timeRecoveryE + recoveryH + changeLine ;
		
		order.setObservation(observationStr);
		
		order.setPayMethod(EnumPayMethods.CASH);
		
		model.addAttribute("tittle", orderClientHeader);
		model.addAttribute("productText", productText); 
		model.addAttribute("productTexth", productTextH); 
		model.addAttribute("procedureHeader", surgeriesHeader);
		model.addAttribute("freeItemHeader", "Free items");
		model.addAttribute("recommendedItemStrHeader", recommendedItemStrHeader);
		model.addAttribute("totalProcedureCashPrice", procedureCashPrice);  
		model.addAttribute("totalProcedureVIPTotalPrice", procedureVIPTotalPrice);  
		model.addAttribute("totalProcedureFinancedPrice", procedureFinancedPrice);  
		model.addAttribute("proceduresData", proceduresData);  

		model.addAttribute("recommendedItemList", cService.findProductsRecommendedByProcedure(superOrder.getClient().getDoctor(), order.getProcedureList()));  
		model.addAttribute("restItemList", cService.findProductsNotMandatoryAndNotRecommended(order.getProcedureList(), superOrder.getClient().getDoctor(), 0));
		
		List <ProductRecommendedByProcedureDTO> itemIncludedList = new ArrayList<ProductRecommendedByProcedureDTO>();
		if (order.getVip() !=null && order.getVip().getItemsFree())
			for (ProductRecommendedByProcedure itemRecommended : cService.findProductsRecommendedByProcedure(superOrder.getClient().getDoctor(), order.getProcedureList())) 
				itemIncludedList.add(new ProductRecommendedByProcedureDTO(itemRecommended.getProduct().getId(), 
						  												  itemRecommended.getProduct().getName(), 
						  												  itemRecommended.getAmountRecommended())) ;
		// aqui tengo q ver si entra siempre o solo cuando hay promo
		for (ItemsAmountForVIP itemIfVIP : promoService.findFreeItemByDoctorNProcedures(new Date(), superOrder.getClient().getDoctor(),order.getProcedureList())) 
			itemIncludedList.add(new ProductRecommendedByProcedureDTO(itemIfVIP.getItem().getId(), 
																	  itemIfVIP.getItem().getName(), 
																	  itemIfVIP.getAmount().longValue())) ;
		model.addAttribute("freeItemList", itemIncludedList);
		
		model.addAttribute("superOrder", superOrder);
		model.addAttribute("order", order);
		
		model.addAttribute("hasPromo", hasPromo); // Aqui tengo que preguntar si este doctor tiene promociones sino solo cash y financiado
		if (order.getVip()==null)
			model.addAttribute("payMethods", EnumPayMethods.values()); // Aqui tengo que preguntar si este doctor tiene promociones sino solo cash y financiado
		else
		{
			model.addAttribute("vipObj", order.getVip()); // Aqui tengo que preguntar si este doctor tiene promociones para agregarlas
			model.addAttribute("payMethods", EnumPayMethods.CASH); // Aqui tengo que preguntar si este doctor tiene promociones para agregarlas
		}
			
		model.addAttribute("promoTo", EnumPromoTo.values());
			
		return "/orders/order-form"; 
	}

	@PostMapping("/order-form")
	// *******DESARROLLANDO******/ 	
	/*******************
	 * @param superOrder
	 * @param bR
	 * @param model
	 * @param itemIdM
	 * @param amountM
	 * @param itemIdMI
	 * @param itemIdC
	 * @param amountC
	 * @param payM
	 * @param promoTo
	 * @param flash
	 * @param st
	 * @return
	 */
//	logger.info("<<<<ORDER-UPDATE>>>> order id: " + order.getId());
//	promosDoctorNProc = promoService.findActivePromosForDoctorAndProcedure(new Date(), 
//																		   superOrder.getClient().getDoctor(), 
//																		   procedureChoosed.getProcedure().getId());
//List<VIPDoctorProcedure> promosDoctorNProc = new ArrayList<VIPDoctorProcedure>();
	// Recorrer cada lista de items (Mandatories, includies, Chosen)
//	if (itemIdM!=null) // Mandatories
//		for (int i = 0; i < itemIdM.length; i++) {			
//			item = prodService.findOne(itemIdM[i]);
//			lineI = new OrderItem();
//			lineI.setAmount(amountM[i]);
//			lineI.setProduct(item);
//			lineI.setSubTotal();
//			order.addItem(lineI);
//		}
//	if (itemIdMI!=null) ///			Mandatories, includies,
//		for (int i = 0; i < itemIdMI.length; i++) {		
//			item = prodService.findOne(itemIdMI[i]);
//			lineI = new OrderItem();
//			lineI.setAmount(0);
//			lineI.setProduct(item);
//			lineI.setSubTotal();
//			order.addItem(lineI);
//		}
	public String save(@Valid SuperOrder superOrder, BindingResult bR, Model model, 
					   @RequestParam(name = "item_id_m[]", required = false) Long itemIdM[],  
					   @RequestParam(name = "amount_m[]", required = false) Integer amountM[], 
					   @RequestParam(name = "item_id_mi[]", required = false) Long itemIdMI[],
					   @RequestParam(name = "item_id_c[]", required = false) Long itemIdC[],
					   @RequestParam(name = "amount_c[]", required = false) Integer amountC[],
					   @RequestParam(name = "payM", required = false) EnumPayMethods payM,
					   @RequestParam(name = "promoTo", required = false) Integer promoTo,
					   RedirectAttributes flash, SessionStatus st) 
		{ 
			// Tengo la orden actual y le especifico el metodo de pago 
		Order order = superOrder.getOrderList().get(superOrder.getOrderList().size()-1); // getLastOrder()
		order.setPayMethod(payM);
			
		Double procedureSubtotal = 0.0 ; 
		Double itemSubTotal = 0.0;
		Double orderPreDiscount = 0.0;

		String observation = order.getObservation()!=null? order.getObservation(): " ";
		
		OrderItem lineI = null ;
		List<OrderItem> oi = new ArrayList<OrderItem>();

		Product item = null ;
		order.setDiscount(0.0);
		order.setItemList(oi);

		// Si existen items escogidos adicionarlos a la orden
		if (itemIdC!=null)  
			for (int i = 0; i < itemIdC.length; i++) 
			{     
				item = prodService.findOne(itemIdC[i]);
				lineI = new OrderItem();
				lineI.setAmount(amountC[i]);
				lineI.setProduct(item);
				lineI.setSubTotal();
				itemSubTotal += lineI.getSubTotal();	
				order.addItem(lineI);
				if (item.getObservations()!=null)
					observation = observation.concat(item.getObservations()).concat(changeLine) ;
			}
		
		// ahora adicionar los procedimientos escogidos
		VIPDoctorProcedure singlePromoDoctorNProc = null;
		for (OrderProcedure procedureChoosed: order.getProcedureList())
		{	
			if (promoTo != null) //PARCHE!!!
				switch (promoTo) 
				{
					case 0:
						procedureChoosed.setPromoTo(EnumPromoTo.PROCEDURE);
						break;
					case 1:
						procedureChoosed.setPromoTo(EnumPromoTo.ITEM);
						break;
				}//PARCHE!!!
			
			
			procedureSubtotal = !procedureChoosed.getIncludedInPrice()? dService.getProcedurePrice(cService.findOne(superOrder.getClient().getId()).getDoctor(),
	                   													procedureChoosed.getProcedure().getId(), order.isFinanced()): Double.valueOf(0);
			
			//Ya tengo si es Financiado o Cash, ahora a ver si hay ofertas y aplica a los procedimientos 
			if (order.getPayMethod() == EnumPayMethods.VIP && promoService.findActivePromosForDoctorAndProcedure(new Date(), 
																												 superOrder.getClient().getDoctor(), 
																												 procedureChoosed.getProcedure().getId()).size()>0)
			{
				singlePromoDoctorNProc = promoService.findActivePromosForDoctorAndProcedure(new Date(), 
																							superOrder.getClient().getDoctor(), 
																							procedureChoosed.getProcedure().getId()).get(0) ;
				switch (procedureChoosed.getPromoTo())  // el promoTo es por proc no por orden y por tanto hay q sumar...ver todo lo q hay q sumar
				{
					case PROCEDURE:
						switch (singlePromoDoctorNProc.getType()) 
						{
								case NEW_PROCEDURE_PRICE:
									procedureSubtotal = singlePromoDoctorNProc.getDiscount() ;
									break;
								case PRICE_DISCOUNT:
									procedureSubtotal = Math.min(0, procedureSubtotal-singlePromoDoctorNProc.getDiscount()) ;
									break;
								case PERCENT_DISCOUNT:
									procedureSubtotal *= (1-singlePromoDoctorNProc.getDiscount()/100) ;
									break;
							}
							observation= observation.concat("VIP " + procedureChoosed.getProcedure().getName() + " price: $" + procedureSubtotal + ". ").concat(changeLine) ;
							break;
						case ITEM:
							orderPreDiscount += singlePromoDoctorNProc.getDiscount() ;
							break;
					}
				}
				procedureChoosed.setSubTotal(procedureSubtotal);
			}
			
			order.setDiscount(Math.min(orderPreDiscount, itemSubTotal));
			if (order.getDiscount()>0)
				observation= observation.concat("Discount $" + order.getDiscount() + " in item for VIP pay method. " ).concat(changeLine) ;
			 
			//Adicionar items precargados en las promociones
			for (ItemsAmountForVIP itemsForVIP : promoService.findFreeItemByDoctorNProcedures(new Date(), superOrder.getClient().getDoctor(), order.getProcedureList()))
			{
				int pos = order.getItemPos(itemsForVIP.getItem().getId()) ;
				if (pos == -1) // No está en la lista
				{
					item = prodService.findOne(itemsForVIP.getItem().getId());
					lineI = new OrderItem();
					lineI.setAmount(itemsForVIP.getAmount());
					lineI.setProduct(item);
					lineI.setSubTotal(0.0);
					order.addItem(lineI);
					if (item.getObservations()!=null)
						order.setObservation((order.getObservation() != null)? 
												order.getObservation() + item.getObservations() + changeLine: 
													item.getObservations() + changeLine) ;
				}
				else // está en la lista
					order.updateItemAmount(pos, order.getItemList().get(pos).getAmount() + itemsForVIP.getAmount());

				observation = observation.concat(itemsForVIP.getAmount() + " " + itemsForVIP.getItem().getName() + " free for VIP pay method. ").concat(changeLine) ;
			}
			
			
			// ver si la orden es VIP y adicionar los itemsVIP
			if (order.getVip() !=null && order.getVip().getItemsFree())
				for (ProductRecommendedByProcedure itemRecommended: cService.findProductsRecommendedByProcedure(superOrder.getClient().getDoctor(), order.getProcedureList()))
				{
					int pos = order.getItemPos(itemRecommended.getProduct().getId()) ;
					if (pos == -1) // No está en la lista
					{
						item = prodService.findOne(itemRecommended.getProduct().getId());
						lineI = new OrderItem();
						lineI.setAmount(itemRecommended.getAmountRecommended().intValue());
						lineI.setProduct(item);
						lineI.setSubTotal(0.0);
						order.addItem(lineI);
						if (item.getObservations()!=null)
							order.setObservation((order.getObservation() != null)? 
													order.getObservation() + item.getObservations() + changeLine: 
														item.getObservations() + changeLine) ;
					}
					else // está en la lista
						order.updateItemAmount(pos, order.getItemList().get(pos).getAmount() + itemRecommended.getAmountRecommended().intValue());

					observation = observation.concat(itemRecommended.getAmountRecommended().intValue() + " " + itemRecommended.getProduct().getName() + 
														" free for VIP pay method. ").concat(changeLine) ;
				}
			
			
			order.setObservation(observation);
				
			cService.saveSuperOrder(superOrder);
			st.setComplete();
			
			return "redirect:/orders/summary/" + superOrder.getId(); // order.getClient().getId() + "/" + AQUI MOSTRAR SOLO ESTA ORDEN EN EL SUMMARY
		}
	
		
	@GetMapping(value = "/summary/{sorderid}")
	public String summary(@PathVariable(value = "sorderid") Long sOrderId, Model model, RedirectAttributes flash, SessionStatus st) 
	{  
		Order order = null ;
		Client cliente = null;
		SuperOrder superOrder = null ;
			
		if (sOrderId > 0) {
			superOrder = cService.getSuperOrderById(sOrderId);
			order = superOrder.getOrderList().get(superOrder.getOrderList().size()-1);
			cliente = cService.findOne(superOrder.getClient().getId());
		}

		model.addAttribute("tittle", summaryHeader);
		model.addAttribute("itemsheader", itemsheader);
		model.addAttribute("orderClientHeader", patientHeader + cliente.getName());
		model.addAttribute("opDecision", opDecision);
			
		model.addAttribute("client", cliente);
		model.addAttribute("order", order);
		model.addAttribute("sOrderId", sOrderId);

		return "/orders/summary";
	}
		
	@GetMapping(value = "/view-order/{sorderid}")
	public String view(@PathVariable(value = "sorderid") Long sorderid, Model model, RedirectAttributes flash, SessionStatus st) 
	{
		SuperOrder superOrder = cService.getSuperOrderById(sorderid);
		superOrder.setStatus(SuperOrderStatus.READY); // Si llegó hasta aquí es pq ya está "Ready"
		cService.saveSuperOrder(superOrder);
	
		List <String> included = Arrays.asList("Drug test", "Nicotine test", "Pregnancy test", "Anesthesia Fees", "Operating Room Fees", "Surgeon's Fees", 
				   								"Compression Socks", "All Pre Op Appointments", "All Post Op Appointments", "Prescriptions for medications");
//		 para lo del portapapeles
		String text = "";
		int i = 1 ;
		for (Order order : superOrder.getOrderList()) 
		{
			text = text.concat("SubOrder" + i++).concat(changeLine);
			text = text.concat("***************").concat(changeLine);
			text = text.concat(order.getDoctorName() + ", ");
			//Procedures
			for (OrderProcedure procedureChoosed: order.getProcedureList()) 
				text = text.concat(procedureChoosed.getProcedure().getName()).concat(" + ");
			text = text.substring(0, text.length()-2);
			text = text.concat("$").concat(order.getTotalOrder().toString());
			text = text.concat(changeLine);
			text = text.concat("Descripción de lo que se hace en las cirugías");
			text = text.concat(changeLine);
			//Items
			if (order.getItemList() != null)
				for (OrderItem item: order.getItemList()) 
					text = text.concat(item.getProduct().getName()).concat(changeLine);
			text = text.concat(changeLine);
		}
		for (String other: included)
			text = text.concat(other).concat(changeLine);
		text = text.concat("Total order: $" + superOrder.getTotalOrder()).concat(changeLine); 
		text = text.concat("You can set this price with a small deposit of $ 250.00");
		
		model.addAttribute("text", text);
		model.addAttribute("tittle", orderDetailsHeader);
		model.addAttribute("orderClientHeader", orderClientHeader + superOrder.getClient().getName());
		model.addAttribute("personalDataHeader", personalDataHeader);
		model.addAttribute("healthConditionHeader", HealthConditionHeader);
		model.addAttribute("opDecision", opDecision);
		model.addAttribute("client", superOrder.getClient());
		model.addAttribute("superOrder", superOrder);
		if (superOrder.getClient().getConditionsName() != null)
			model.addAttribute("conditionList", superOrder.getClient().getConditionsName());  
		model.addAttribute("included", included);
		
		return "/orders/view-order";
	}
	
}