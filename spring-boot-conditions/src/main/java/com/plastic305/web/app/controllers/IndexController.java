package com.plastic305.web.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.OrderItem;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.services.IClientService;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.IProductService;
import com.plastic305.web.app.services.ISufferingService;

@Controller
@SessionAttributes("client")
public class IndexController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private static final String tittleR1 = "Questionnaire" ;
	private static final String tittleProduct = "Post-surgical items" ;
	private static final String conditionHeader = "Diseases that you have or presented" ;
	private static final String productHeader = "See prices below and choose" ;
	private static final String conditionQuestion = "Please state if do you had or have any of these conditions" ;
	private static final String productText = "Post-surgical items exist to accomplish goals!\r\n" + 
											  "These items listed below are necessary in order to protect and enhance the outcome of your procedure, prevent the formation of scar tissue, and aide the recovery process.\r\n" + 
											  "Prepare in advance for a successful recovery by purchasing the necessary post-surgical items in office at the time of pre-op: (See prices below and choose)" ;
	private static final String bmiHeader = "Data for BMI calculation" ;
	private static final String bmiMetric = "Metric system" ;
	private static final String bmiEnglish = "English system" ;
	private static final String choiceH = "Answers to the questionnaire" ;
	private static final String decisionH = "Decision" ;
	private static final String noAcceptedByConditionH = "You suffer or suffered: " ;
	private static final String noAcceptedByConditionExp = ". Condition(s) that makes it impossible for you to apply the desired treatment" ;
	private static final String noAcceptedByBMIH = "Your BMI is: " ;
	private static final String noAcceptedByBMIUpperExp = ". Indicating your weight is greater than what is allowed" ;
	private static final String noAcceptedByBMILowerExp = ". Indicating your weight is lower than what is allowed" ;
	
	private static final Double bmiLowerLimit = Double.valueOf(18.5) ;
	private static final Double bmiUpperLimit = Double.valueOf(30) ;
	
	
	@Autowired ISufferingService sService;
	@Autowired IClientService cService;
	@Autowired IDoctorService dService;
	@Autowired IProcedureService pService;
	@Autowired IProductService prodService;  // NEW
	

	
// *******************      
// *******EN USO******    PRIMERA (ENGLISH FEET-INCHES-POUNDS)
	@GetMapping({"/r1", "/", "/index"})   
	public String form(Model model) {
		Client c =  new Client() ;
		List <Suffering> sList = sService.findAll(); 

		model.addAttribute("tittle", tittleR1);
 		model.addAttribute("conditionHeader", conditionHeader);
 		model.addAttribute("conditionQuestion", conditionQuestion) ;
 		model.addAttribute("bmiHeader", bmiHeader) ;   // NEW
 		model.addAttribute("bmiEnglish", bmiEnglish) ;   // NEW
 		model.addAttribute("bmiLink", bmiMetric) ;   // NEW
 		
 	    model.addAttribute("sList", sList);
 	    model.addAttribute("client", c);
		
 	    return "r1";
	}
		
// *******************				
// *******EN USO******     (METRIC Centimeters-Kilograms)
	@GetMapping({"/r1/{system}", "/", "/index/{system}"})  
	public String formMetric(@PathVariable(value = "system") String system, Model model) {
		Client c =  new Client() ;
		List <Suffering> sList = sService.findAll(); 

		model.addAttribute("tittle", tittleR1);
	 	model.addAttribute("conditionHeader", conditionHeader);
	 	model.addAttribute("conditionQuestion", conditionQuestion) ;
	 	model.addAttribute("bmiHeader", bmiHeader) ;   // NEW
 		model.addAttribute("bmiMetric", bmiMetric) ;   // NEW	
 		model.addAttribute("bmiLink", bmiEnglish) ;   // NEW
	 	
	 	model.addAttribute("sList", sList);
	 	model.addAttribute("client", c);
			
	 	return "r1";
	}

	
// *******************      NUEVO PARA AGREGAR EL BMI
// *******EN USO******	
	@PostMapping({"/r1", "/", "/index"})    
	public String formProcess(Client cliente, Model model) {
		String url = "redirect:/doctor_or_procedure" ;
		for (Suffering c: cliente.getConditionsList()) {
			if (c.getAccepted() == 0) { 
				cliente.setAccepted(Long.valueOf(0));   
				return "redirect:/no_accepted";
			}
			else if (c.getAccepted() == 2) 
					url = "redirect:/somedoctors";
		}
		// CALCULANDO EL BMI
		if (cService.getBMI(cliente) < bmiLowerLimit  || cService.getBMI(cliente) > bmiUpperLimit) {  
			
			return "redirect:/no_accepted";
		}
		return url;
	}
		
// *******************
// *******EN USO******	NUEVO PARA AGREGAR EL BMI
	@GetMapping({"no_accepted"})       
	public String noAccepted(Client cliente, Model model, SessionStatus st) {
		// Locales necesarias
		List <String> decision = new ArrayList<>();
		List<String> choices;
		
		// LLenando los datos
		if (cliente.getHeightInches()!=null)  // americano
			choices = Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
					                "Weigth: " + cliente.getWeight() + " Pounds", 
					                "Height: " + cliente.getHeightFeetOrCentimeters() + "'  " + cliente.getHeightInches() + "\"");
		else 
			choices = Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
									"Weigth: " + cliente.getWeight() + " Kilograms", 
									"Height: " + cliente.getHeightFeetOrCentimeters() + " Centimeters");
		
		if (cliente.getAccepted() != null && cliente.getAccepted()== 0)
			decision.add(noAcceptedByConditionH + cService.getConditionsWithValue(cliente, 0) + noAcceptedByConditionExp);
		double bmi = cService.getBMI(cliente) ; 
		logger.info("<<<<<<    BMI: " + String.format("%.2f", bmi) + "    >>>>>>");
		if (bmi < bmiLowerLimit)
			decision.add(noAcceptedByBMIH + String.format("%.2f", bmi) + noAcceptedByBMILowerExp);
		else if (bmi > bmiUpperLimit)
			decision.add(noAcceptedByBMIH + String.format("%.2f", bmi) + noAcceptedByBMIUpperExp);
		
		// Pasando al modelo
		model.addAttribute("choice_h", choiceH);
		model.addAttribute("msg", decisionH);
		model.addAttribute("choices", choices);
		model.addAttribute("decision", decision);
		
		st.setComplete();
		return "no_accepted";
	}
	
// *******************
// *******EN USO******	
	@PostMapping("no_accepted")
	public String noAcceptedP(Model model,SessionStatus st) {
		return "redirect:/r1";
	}
	
// *******************
// *******EN USO******           
	@GetMapping({"doctor_or_procedure"})   // Si llega aquí es pq es candidato 
	public String doctorOrProcedure(Client cliente, Model model, SessionStatus s) {
		model.addAttribute("choice_h", "Answers to the questionnaire");
		if (!cService.haveRemark(cliente))  
			model.addAttribute("msg", "Accepted");
		else 
			model.addAttribute("msg", "Accepted with Remarks");
		model.addAttribute("choice", "Condition: " + cService.getConditionsListCSV(cliente));
		model.addAttribute("explanationb", "You have been accepted, select the procedure or doctor you want");
		return "doctor_or_procedure";
	}
	
// *******************
// *******EN USO******            
	@GetMapping({"choice_procedure_by_doctor"})   
	public String choiceProcedureByDoctor(Client cliente, Model model, SessionStatus s) {
		// Pasar, la condición, la lista de doctores
		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("choices", "Condition: " + cService.getConditionsListCSV(cliente));
		
		model.addAttribute("cardHeader", "Choose the doctor, then the procedure");
		model.addAttribute("doctorlisth", "Doctors list"); 
		
		if (cService.getConditionsWithValue(cliente, 2).isEmpty())  // Que no tiene condición especial
			model.addAttribute("doctorlist", dService.findAll()); 
		else  
			model.addAttribute("doctorlist", dService.findAllbyConditions(cliente.getConditionsList())) ;
		model.addAttribute("procedurelisth", "Procedures list"); 
		model.addAttribute("procedurelistempty", "Choose the doctor to see the procedures that his make"); 

		return "choice_procedure_by_doctor";
	}
	
// *******************
// *******EN USO******
	@GetMapping({"choice_procedure_by_doctor/{id}"})   
	public String choiceProcedureByDoctorSelect(@PathVariable(value = "id") Long id, Client cliente, Model model, SessionStatus st) {
		cliente.setDoctor(id);
		cliente.setP1(null);
		
		model.addAttribute("choice_h", "Answers to the questionnaire");
		List <String> choices = Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
								              "Doctor: " + dService.findOne(id).getName());
		model.addAttribute("choices", choices);
		
		model.addAttribute("cardHeader", "Choose the doctor, then the procedure");
		
		model.addAttribute("doctorlisth", "Doctors list");
		if (cService.getConditionsWithValue(cliente, 2).isEmpty())  // Que no tiene condición especial
			model.addAttribute("doctorlist", dService.findAll()); 
		else 
			model.addAttribute("doctorlist", dService.findAllbyConditions(cliente.getConditionsList())) ;
		
		model.addAttribute("procedurelisth", "Procedures list of doctor " + dService.findOne(id).getName()); 
		model.addAttribute("procedurelist", dService.findAllProcedurebyDoctorId(id)); 
				
		return "choice_procedure_by_doctor";
	}		
	
// *******************
// *******EN USO******    
	@GetMapping({"choice-combo-by-doctor-by-p1"})   
	public String choiceComboByDoctorByP1(Client cliente, Model model, SessionStatus st) {
		//Hasta aquí hay: Conditions, Doctor y P1
		cliente.setP2(null); 
		
		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("choices", Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
  			  										"Doctor: " + dService.findOne(cliente.getDoctor()).getName(),
  			  										"First Procedure: " + pService.findOne(cliente.getP1()).getName()));
		
		model.addAttribute("cardHeader", "Choose the procedure to make a Combo");
		
		model.addAttribute("doctorlisth", "Doctors list"); 
		if (cService.getConditionsWithValue(cliente, 2).isEmpty())  // Que no tiene condición especial
			model.addAttribute("doctorlist", dService.findAllbyProcedure(cliente.getP1())); 
		else  
			model.addAttribute("doctorlist", dService.findAllByConditionsByProcedure(cliente.getConditionsList(), cliente.getP1())) ;
		
		model.addAttribute("procedurelisth", "Procedures list of doctor " + dService.findOne(cliente.getDoctor()).getName() + 
				 		   " that make combo with " + pService.findOne(cliente.getP1()).getName()); 
		List<Procedure> listProcedure = dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(), cliente.getP1());	
		if (!listProcedure.isEmpty())
			model.addAttribute("procedurelist", listProcedure);
		else 
			model.addAttribute("procedurelistempty", "Sorry this doctor don't make Combo with " + pService.findOne(cliente.getP1()).getName());
	
		return "choice-combo-by-doctor-by-p1";
	}			
	
// *******************
// *******EN USO******    
	@GetMapping({"choice-combo-by-doctor-by-p1/{id}"})   
	public String choiceComboByDoctorByP1SelectDoct(@PathVariable(value = "id") Long id, Client cliente, Model model, SessionStatus st) {
		cliente.setDoctor(id);
		cliente.setP2(null); 
		
		return this.choiceComboByDoctorByP1(cliente, model, st);
	}			
	
// *******************
// *******EN USO******   
	@GetMapping({"choice_doctor_by_procedure"})   
	public String choiceDoctorByProcedure(Client cliente, Model model, SessionStatus st) {
		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("choices", "Condition: " + cService.getConditionsListCSV(cliente));
		
		model.addAttribute("cardHeader", "Choose the doctor, then the procedure");
		
		model.addAttribute("doctorlisth", "Doctors list"); 
		model.addAttribute("doctorlistempty", "Choose the procedure to see the doctors who practice them"); 
		
		model.addAttribute("procedurelisth", "Procedures list"); 
		if (cService.getConditionsWithValue(cliente, 2).isEmpty())
			model.addAttribute("procedurelist", pService.findAllOrder()); 
		else 
			model.addAttribute("procedurelist", dService.findAllProcedureOfAllDoctorsByConditions(cliente.getConditionsList())) ;
			
		return "choice_doctor_by_procedure";
	}
		
// *******************
// *******EN USO******   
	@GetMapping({"choice_doctor_by_procedure/{id}"})   
	public String choiceDoctorByProcedureSelectProc(@PathVariable(value = "id") Long id, Client cliente, Model model, SessionStatus st) {
		cliente.setDoctor(null);
		cliente.setP1(id);
		
		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("choices", Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
												    "Procedure: " + pService.findOne(cliente.getP1()).getName()));
		
		model.addAttribute("cardHeader", "Choose the procedure, then the doctor");
		
		model.addAttribute("procedurelisth", "Procedures list"); 
		model.addAttribute("doctorlisth", "List of doctors who practice the " + pService.findOne(id).getName() + " procedure"); 

		if (cService.getConditionsWithValue(cliente, 2).isEmpty()) {
			model.addAttribute("procedurelist", pService.findAllOrder()); 
			model.addAttribute("doctorlist", dService.findAllbyProcedure(id)); 
		}
		else {
			model.addAttribute("procedurelist", dService.findAllProcedureOfAllDoctorsByConditions(cliente.getConditionsList()));   
			model.addAttribute("doctorlist", dService.findAllByConditionsByProcedure(cliente.getConditionsList(), id)); 
		}
		
		return "choice_doctor_by_procedure";
	}		
	
// *******************
// *******EN USO******      
	@GetMapping({"choice-combo-by-p1-by-doctor"}) 
	public String choiceComboByP1ByDoctor(Client cliente, Model model, SessionStatus st) {
		cliente.setP2(null); 

		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("choices", Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
													"Doctor: " + dService.findOne(cliente.getDoctor()).getName(),
													"First Procedure: " + pService.findOne(cliente.getP1()).getName()));
		
		model.addAttribute("cardHeader", "Choose the procedure to make a Combo");
		
		model.addAttribute("procedurelisth", "Procedures list that make combo with " + pService.findOne(cliente.getP1()).getName() + 
				                             " for the doctor " + dService.findOne(cliente.getDoctor()).getName());
		List<Procedure> listProcedure = dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(), cliente.getP1());
		if (!listProcedure.isEmpty())
			model.addAttribute("procedurelist", listProcedure); 
		else
			model.addAttribute("procedurelistempty", "Sorry this doctor don't make Combo with " + 
													  pService.findOne(cliente.getP1()).getName());
		
		model.addAttribute("doctorlisth", "List of doctors who practice the " + pService.findOne(cliente.getP1()).getName() +
										  " procedure");
		
		if (cService.getConditionsWithValue(cliente, 2).isEmpty())
			model.addAttribute("doctorlist", dService.findAllbyProcedure(cliente.getP1())); 
		else
			model.addAttribute("doctorlist", dService.findAllByConditionsByProcedure(cliente.getConditionsList(), cliente.getP1())); 

		return "choice-combo-by-p1-by-doctor"; 
	}	
	
// *******************
// *******EN USO******	
	@GetMapping({"choice-combo-by-p1-by-doctor/{id}"}) 
	public String choiceComboByP1ByDoctorchange(@PathVariable(value = "id") Long id, Client cliente, Model model, SessionStatus st) {
		// Hay que cambiar el Doctor
		cliente.setDoctor(id);
		
		return this.choiceComboByP1ByDoctor(cliente, model, st);
	}	

// *******************
// *******EN USO******	
	@GetMapping({"somedoctors"})
	public String somedoctor(Client cliente, Model model, SessionStatus st) {
		
		model.addAttribute("choice", "Condition: " + cService.getConditionsListCSV(cliente));
		model.addAttribute("msg", "Observation");
		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("explanationb", "Only the doctors: "); 
		model.addAttribute("explanationc", "perform the procedures on patients who have or are suffering " 
				                         + cService.getConditionsWithValue(cliente, 2)
 										 + " condition(s). Do you agree to be treated by any of these?");
		model.addAttribute("tips", "Do you want choice the doctor now or see first the procedures?");
		model.addAttribute("doctors", dService.findAllbyConditions(cliente.getConditionsList()));
		return "somedoctors";
	}
	
//****************
// *******EN USO******
	@GetMapping({"result"})
	public String result(Client cliente, Model model, SessionStatus st) {
		List <String> choices = new ArrayList<>() ;
		String observation = "Has not Remarks" ;
		
		model.addAttribute("choice_h", "Answers to the questionnaire");
		choices.add("Condition: " + cService.getConditionsListCSV(cliente));
		observation = cService.getRemarksListCSV(cliente);
		
		if (cliente.getDoctor()!=null) {
			choices.add("Doctor: " + dService.findOne(cliente.getDoctor()).getName());
			if (cliente.getP1()!=null) {
				choices.add("First procedure: " + pService.findOne(cliente.getP1()).getName()); 
				model.addAttribute("observation", observation);
				if (cliente.getP2()!=null)
					choices.add("Combo with: " + pService.findOne(cliente.getP2()).getName());
			}
			else  
				choices.add("¡¡¡Do not decide to have surgery with any doctor available!!!");
		}
		else {
			if (cliente.getP1()!=null) 
				 choices.add("First procedure: " + pService.findOne(cliente.getP1()).getName()); 
			choices.add("¡¡¡Do not decide to have surgery with any doctor available!!!");
				
		}
		model.addAttribute("choices", choices);
	//	st.setComplete();
		return "result";
	}
	
// *******************      
// *******DESARROLLANDO******    
		@GetMapping({"/post-surgical"})   
		public String postSurgical(Client cliente, Model model, SessionStatus st) {
//			List <OrderItem> pSubTotalList = new ArrayList<>();
//			List <Product> pList = prodService.findAll();
//			
////			for (Product product: pList)
////				pSubTotalList.add(new OrderItem(product.getId(),product, 6, 10.0));
////			cliente.setListProd(pSubTotalList);
//
//			model.addAttribute("tittle", tittleProduct);
//	 		model.addAttribute("productText", productText) ;
//	 		
//	 //	    model.addAttribute("products_list", pList);
//	 	    model.addAttribute("sub_total_list", pSubTotalList);
////	 	    model.addAttribute("client", c);
			
	 	    return "post-surgical";
		}	
	
// *******************      
// *******DESARROLLANDO******    
	@GetMapping({"/recalculate"})   // id del producto
	public String calculateTotal(Client cliente, Model model, SessionStatus st) {
//		for (OrderItem product: cliente.getListProd()) {
//			logger.info(product.getAmount());
//		} 
					
		return "redirect:/r1";
	}		
	//*********************************************************************************************
// *******************      
// *******DESARROLLANDO******    
	@GetMapping("/orders/order-form")  // en algún momento pasar el id del cliente
	public String create(Client cliente, Model model, RedirectAttributes flash, SessionStatus st) {
		Client clientObj = new Client();
		clientObj.setName("Lolo");
		clientObj.setId(Long.valueOf(1));
		//Todo esto de arriba se va cuando le pase el cliente real
/*	Esto se descomenta cuando se le pase el cliente de arriba o se va	
		if (clientObj == null) {
			flash.addFlashAttribute("error", "Cliente no existe con ese Id");
			return "redirect:/listar";
		}
*/		
		List <Product> pList = prodService.findAll();
		Order orderObj = new Order();
		orderObj.setClient(clientObj);
		model.addAttribute("tittle", tittleProduct);  //("tittle", tittle);
		model.addAttribute("productText", productText);   //("msg", msg);
		model.addAttribute("pList", pList);   //("msg", msg);
		model.addAttribute("order", orderObj);

		return "/orders/order-form";
	}
	
	@GetMapping(value = "/orders/load-products/{term}", produces = {"application/json"})
	public @ResponseBody List<Product> loadProducts(@PathVariable String term) {
		/*
		 * La anotación ResponseBody suprime cargar la vista y lo que hace es retornar,
		 * en este caso convertido a Json, y poblar dentro del Body de la respuesta
		 */
		return cService.findByName(term);
	}
	
	
	
	
	
	
	
}