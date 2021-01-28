package com.plastic305.web.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.OrderProcedure;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Promos305;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.models.entities.SuperOrder;
import com.plastic305.web.app.models.entities.SuperOrderStatus;
import com.plastic305.web.app.models.entities.VIPDoctorProcedure;
import com.plastic305.web.app.services.IClientService;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.IProductService;
import com.plastic305.web.app.services.IPromo305Service;
import com.plastic305.web.app.services.ISufferingService;
import com.plastic305.web.app.services.IVIPService;

@Secured("ROLE_USER")
@Controller
@SessionAttributes("client")
public class IndexController {
	private static final String changeLine = "\r\n" ;
	private static final String tittleR1 = "Questionnaire" ;
	private static final String questionnaireAnswerHeader = "Answers to the questionnaire" ;
	private static final String decideNO = "¡¡¡You decided not to have an surgery with any surgeon available!!!" ;
	private static final String p1 = "First procedure: " ;
	private static final String conditionQuestion = "Do you had or have any of these health conditions" ;
	private static final String bmiHeader = "Data for BMI calculation" ;
	private static final String bmiMetric = "Metric system" ;
	private static final String bmiEnglish = "English system" ;
	private static final String choiceH = "Answers to the questionnaire" ;
	private static final String decisionH = "Decision" ;
	private static final String noAcceptedByConditionH = "You suffer or suffered: " ;
	private static final String noAcceptedByConditionExp = ". Condition(s) that makes it impossible for you to apply the desired treatment" ;
	private static final String noAcceptedByBMIH = "Your BMI is: " ;
	private static final String noAcceptedByWeightH = "Yours weight is: " ;  
	private static final String weightLowerHPre = "You need to gain " ;  
	private static final String weightUpperHPre = "You need to lose " ;  
	private static final String weightHPost = " pounds to be able to have the surgery with the Dr. " ;  
	private static final String noAcceptedByWeighExp = ", therefore is greater than what is allowed" ;
	private static final String noAcceptedByBMIUpperExp = ", indicating your weight is greater than what is allowed" ;
	private static final String noAcceptedByBMILowerExp = ", indicating your weight is lower than what is allowed" ;
	private static final String acceptedMsg = "You have been accepted, select the procedure or surgeon you want" ;
	private static final String acceptedWithRemarkHeader = "Accepted with Remarks" ;
	private static final String notRemarkHeader = "Has not Remarks" ;
	private static final String acceptedHeader = "Accepted" ;
	private static final String hbgOutsideLimits = "Your hemoglobin level is outside the desired range" ;
	private static final String date4Surgery = "Choose the tentative date for your surgery, take into account the availability of the surgeon" ;
	private static final String date4SurgeryRecovery = "Choose the tentative date for your surgery, take into account the recovery time" ;
	private static final String needCellT = "Information needed to perform Lipo" ;
	private static final String aditionalProcedureH = "Select aditional procedures" ;
	
	private static final String observationCellByWeightLoss = "Added mandatory Cell Saver item for presenting a history of weight loss surgery. " ;
	private static final String observationCellByMoreOneLipo = "Added mandatory Cell saver due to having performed another similar surgery previously. " ;
	private static final String observationCellIncluded = "The Cell Saver is included in the price of the surgery" ;

	private static final Double hbgFUpperLimit = Double.valueOf(16) ;
	private static final Double hbgFLowerLimit = Double.valueOf(12) ;
	private static final Double hbgMUpperLimit = Double.valueOf(18) ;
	private static final Double hbgMLowerLimit = Double.valueOf(14) ;
	private boolean remark;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired ISufferingService sService;
	@Autowired IClientService cService;
	@Autowired IDoctorService dService;
	@Autowired IProcedureService pService;
	@Autowired IProductService prodService; 
	@Autowired IPromo305Service promo305Service; 
	@Autowired IVIPService vipService; 
	
	
// *******************      
// *******EN USO - REVISADO 08/01/2021 ******    PRIMERA (ENGLISH FEET-INCHES-POUNDS)
	@GetMapping({"/r1/{clientId}"})   
	public String form(@PathVariable(value = "clientId") Long clientId, Model model) 
	{
		cService.UpdateFromInProcessToPending(clientId); // Se actualiza las superOrdenes (las EnProceso a Pendientes)

		Client cliente =  cService.findOne(clientId) ;
		cService.prepare(cliente);   // 30/10/20  este metodo cambiarlo pq tienen q ser mas procedimientos
		remark = false;
		
		List <Suffering> sList = sService.findAll(); 

		model.addAttribute("tittle", tittleR1);
 		model.addAttribute("conditionHeader", conditionQuestion);
 		model.addAttribute("conditionQuestion", conditionQuestion) ;
 		model.addAttribute("bmiHeader", bmiHeader) ;    
 		model.addAttribute("bmiEnglish", bmiEnglish) ;    
 		model.addAttribute("bmiLink", bmiMetric) ;    
 		
 	    model.addAttribute("sList", sList);
 	    model.addAttribute("client", cliente);
		
 	    return "r1";
	}
		
// *******************				
// *******EN USO******     (METRIC Centimeters-Kilograms)
	@GetMapping({"/r1-metric"})  
	public String formMetric(Client cliente, Model model) {
		List <Suffering> sList = sService.findAll(); 

		model.addAttribute("tittle", tittleR1);
	 	model.addAttribute("conditionHeader", conditionQuestion);
	 	model.addAttribute("conditionQuestion", conditionQuestion) ;
	 	model.addAttribute("bmiHeader", bmiHeader) ;    
 		model.addAttribute("bmiMetric", bmiMetric) ;    
 		model.addAttribute("bmiLink", bmiEnglish) ;    
	 	
	 	model.addAttribute("sList", sList);
	 	model.addAttribute("client", cliente);
			
	 	return "r1";
	}

// *******************				
// *******EN USO  - REVISADO 08/01/2021 ******     (ENGLISH FEET-INCHES-POUNDS)
	@GetMapping({"/r1-english"})  
	public String formEnglish(Client cliente, Model model) {
		List <Suffering> sList = sService.findAll(); 

		model.addAttribute("tittle", tittleR1);
 		model.addAttribute("conditionHeader", conditionQuestion);
 		model.addAttribute("conditionQuestion", conditionQuestion) ;
 		model.addAttribute("bmiHeader", bmiHeader) ;    
 		model.addAttribute("bmiEnglish", bmiEnglish) ;    
 		model.addAttribute("bmiLink", bmiMetric) ;    
	 	
	 	model.addAttribute("sList", sList);
	 	model.addAttribute("client", cliente);
	 	
	 	return "r1";
	}	
	
// *******************      
// *******EN USO  - REVISADO 08/01/2021 ******	
	@PostMapping({"/r1"})    
	public String formProcess(@Valid Client cliente, BindingResult bResult, Model model, SessionStatus st)   // Se asume que la cantidad de Dr nunca será 0!!!
	{
		if (bResult.hasErrors()) 
			return "redirect:/r1-english"; 
		
		String url = "redirect:/doctor_or_procedure" ;
		cliente.setAccepted(Long.valueOf(1));
		
		for (Suffering c: cliente.getConditionsList()) 
		{
			if (sService.getDoctorCountsByConditionsId(c.getId())!=0 && sService.getDoctorCountsByConditionsId(c.getId()) < dService.findAll().size())  
				cliente.setAccepted(Long.valueOf(2));
			  else if (sService.getDoctorCountsByConditionsId(c.getId())==0) 
			  	   {
				  	cliente.setAccepted(Long.valueOf(0));  
				
				  	cliente.setConditionsName(cService.getConditionsListCSV4Save(cliente));
				  	cService.save(cliente);
				
				  	return "redirect:/no_accepted";
			  	   }
			
			if (c.getWarning() !=null && !c.getWarning().isBlank())
				remark = true;
		}
		
		String cName = cService.getConditionsListCSV4Save(cliente);
		cliente.setConditionsName(cName); // condiciones como string
		cService.save(cliente);
		
		return url;
	}
		
// *******************
// *******EN USO******	 
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
			decision.add(noAcceptedByConditionH + cService.getConditionsWithValueNewAll(cliente, 0) + noAcceptedByConditionExp);
		
		// Pasando al modelo
		model.addAttribute("choice_h", choiceH);
		model.addAttribute("msg", decisionH);
		model.addAttribute("choices", choices);
		model.addAttribute("decision", decision);
		model.addAttribute("tittle", "Can not apply");

		st.setComplete();
		
		return "no_accepted";
	}
	
// *******************
// *******EN USO  - REVISADO 08/01/2021 ******	
	@PostMapping("no_accepted")
	public String noAcceptedP(Model model,SessionStatus st) {
		return "redirect:/r1";
	}
	
// *******************
// *******EN USO  - REVISADO 08/01/2021 ******           
	@GetMapping({"doctor_or_procedure"})   // Si llega aquí es pq es candidato 
	public String doctorOrProcedure(Client cliente, Model model, SessionStatus s) {
		model.addAttribute("choice_h", questionnaireAnswerHeader);
		if (!remark)
			model.addAttribute("msg", acceptedHeader);
		else 
			model.addAttribute("msg", acceptedWithRemarkHeader);
		
		List <String> choices = Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
	              							  "Remarks: " + cService.getRemarksListCSV(cliente));
		
		model.addAttribute("choice", choices);
		model.addAttribute("explanationb", acceptedMsg);
		
		if ((cliente.getGender().equals("Woman") && (cliente.getHbg()>hbgFUpperLimit || cliente.getHbg()<hbgFLowerLimit)) ||  // Mujer con hemoglobina fuera del rango		
		    (cliente.getGender().equals("Man")   && (cliente.getHbg()>hbgMUpperLimit || cliente.getHbg()<hbgMLowerLimit)))    // Hombre con la hemoglobina fuera del rango
			model.addAttribute("hbgOutsideRange", hbgOutsideLimits);
		
		model.addAttribute("tittle", "Surgeon or procedure first?");
		return "doctor_or_procedure";
	}
	
	
	// *******************
	// *******EN USO******           
	@GetMapping({"doctor_or_procedure/{idsorder}"})  
	public String doctorOrProcedureAdd(@PathVariable(value = "idsorder") Long idSOrder, Model model, SessionStatus s) 
	{ // logger.info("Index>>>>>>: " + cliente.getName());
		SuperOrder superOrder = cService.getSuperOrderById(idSOrder);
		Client cliente = superOrder.getClient();
		cService.reOrder(cliente);
		
		model.addAttribute("client", cliente);
		model.addAttribute("choice_h", questionnaireAnswerHeader);
		if (!remark)
			model.addAttribute("msg", acceptedHeader);
		else 
			model.addAttribute("msg", acceptedWithRemarkHeader);
			
		List <String> choices = Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
											  "Remarks: " + cService.getRemarksListCSV(cliente));
			
		model.addAttribute("choice", choices);
		model.addAttribute("explanationb", acceptedMsg);
			
		if ((cliente.getGender().equals("Woman") && (cliente.getHbg()>hbgFUpperLimit || cliente.getHbg()<hbgFLowerLimit)) ||  // Mujer con hemoglobina fuera del rango		
		    (cliente.getGender().equals("Man")   && (cliente.getHbg()>hbgMUpperLimit || cliente.getHbg()<hbgMLowerLimit)))    // Hombre con la hemoglobina fuera del rango
			model.addAttribute("hbgOutsideRange", hbgOutsideLimits);
			
		model.addAttribute("tittle", "Surgeon or procedure first?");
		return "doctor_or_procedure";
	}	
	
	
	// *******************
	// *******EN USO******   
		@GetMapping({"choice_doctor_by_procedure"})   
		public String choiceDoctorByProcedure(Client cliente, Model model, SessionStatus st) {
			if (cService.getConditionsWithValueNewAll(cliente, 2).isEmpty())
				model.addAttribute("procedurelist", pService.findAllOrder()); 
			else 
				model.addAttribute("procedurelist", dService.findAllProcedureOfAllDoctorsByConditions(cliente.getConditionsList())) ;
			
			model.addAttribute("choice_h", "Answers to the questionnaire");
			model.addAttribute("choices", "Condition: " + cService.getConditionsListCSV(cliente));
			model.addAttribute("cardHeader", "Choose the surgeon, then the procedure");
			model.addAttribute("doctorlisth", "Surgeon list"); 
			model.addAttribute("doctorlistempty", "Choose the procedure to see the surgeon who practice them"); 
			model.addAttribute("procedurelisth", "Procedures list"); 
			model.addAttribute("tittle", "Procedure, then surgeon");

			return "choice_doctor_by_procedure";
		}
			
	// *******************
	// *******EN USO******   
		@GetMapping({"choice_doctor_by_procedure/{idproc}"})   
		public String choiceDoctorByProcedureSelectProc(@PathVariable(value = "idproc") Long idproc, Client cliente, Model model, SessionStatus st) {
			cliente.setDoctor(null);
			cliente.setP1(idproc);
			
			if (cService.getConditionsWithValueNewAll(cliente, 2).isEmpty()) {
				model.addAttribute("procedurelist", pService.findAllOrder()); 
				model.addAttribute("doctorlist", dService.findAllbyProcedure(idproc)); 
			}
			else {
				model.addAttribute("procedurelist", dService.findAllProcedureOfAllDoctorsByConditions(cliente.getConditionsList()));   
				model.addAttribute("doctorlist", dService.findAllByConditionsByProcedure(cliente.getConditionsList(), idproc)); 
			}
			model.addAttribute("choice_h", "Answers to the questionnaire");
			model.addAttribute("choices", Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
													    "Procedure: " + pService.findOne(cliente.getP1()).getName()));
			model.addAttribute("cardHeader", "Choose the procedure, then the surgeon");
			model.addAttribute("procedurelisth", "Procedures list"); 
			model.addAttribute("doctorlisth", "List of surgeon who practice the " + pService.findOne(idproc).getName() + " procedure"); 

			return "choice_doctor_by_procedure";
		}		
		
	// *******************
	// *******EN USO******      
		@GetMapping({"choice-combo-by-p1-by-doctor"}) 
		public String choiceComboByP1ByDoctor(Client cliente, Model model, SessionStatus st) {
			cliente.setP2(null); 

			List<Procedure> listProcedure = dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(), cliente.getP1());
			if (!listProcedure.isEmpty())
				model.addAttribute("procedurelist", listProcedure); 
			else
				model.addAttribute("procedurelistempty", "Sorry this surgeon don't make Combo with " + pService.findOne(cliente.getP1()).getName());
			
			if (cService.getConditionsWithValueNewAll(cliente, 2).isEmpty())
				model.addAttribute("doctorlist", dService.findAllbyProcedure(cliente.getP1())); 
			else
				model.addAttribute("doctorlist", dService.findAllByConditionsByProcedure(cliente.getConditionsList(), cliente.getP1())); 

			model.addAttribute("choice_h", "Answers to the questionnaire");
			model.addAttribute("choices", Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
														"Surgeon: " + dService.findOne(cliente.getDoctor()).getName(),
														"First Procedure: " + pService.findOne(cliente.getP1()).getName()));
			model.addAttribute("cardHeader", "Choose the procedure to make a Combo");
			model.addAttribute("procedurelisth", "Procedures list that make combo with " + pService.findOne(cliente.getP1()).getName() + 
					                             " for the surgeon " + dService.findOne(cliente.getDoctor()).getName());
			model.addAttribute("doctorlisth", "List of surgeon who practice the " + pService.findOne(cliente.getP1()).getName() + " procedure");
			model.addAttribute("tittle", "Choose other Procedure for Combo");

			return "choice-combo-by-p1-by-doctor"; 
		}	
		
	// *******************
	// *******EN USO******	
		@GetMapping({"choice-combo-by-p1-by-doctor/{iddoct}"}) 
		public String choiceComboByP1ByDoctorchange(@PathVariable(value = "iddoct") Long iddoct, Client cliente, Model model, SessionStatus st) {
			// Hay que cambiar el Doctor
			cliente.setDoctor(iddoct);
			
			return this.choiceComboByP1ByDoctor(cliente, model, st);
		}	
	
	
	
	
// *******************
// *******EN USO******            
	@GetMapping({"choice_procedure_by_doctor"})   
	public String choiceProcedureByDoctor(Client cliente, Model model, SessionStatus s) 
	{
		// Pasar, la condición, la lista de doctores
		if (cService.getConditionsWithValueNewAll(cliente, 2).isEmpty())  // Que no tiene condición especial
			model.addAttribute("doctorlist", dService.findAll()); 
		else  
			model.addAttribute("doctorlist", dService.findAllbyConditions(cliente.getConditionsList())) ;
		
		model.addAttribute("procedurelisth", "Procedures list"); 
		model.addAttribute("procedurelistempty", "Choose the surgeon to see the procedures that his make"); 
		model.addAttribute("tittle", "First surgeon, then procedure");
		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("cardHeader", "Choose the surgeon, then the procedure");
		model.addAttribute("doctorlisth", "Surgeon list"); 

		model.addAttribute("choices", "Condition: " + cService.getConditionsListCSV(cliente));
		
		return "choice_procedure_by_doctor";
	}
	
	
// *******************
// *******INVENTO******            
	@GetMapping({"procedures-images/{pname}"})   
	public String viewImages(@PathVariable(value = "pname") String pname, Client cliente, Model model, SessionStatus s) {
		model.addAttribute("procedure", pService.getProcedureByName(pname));
				
		return "procedures-images";
	}
	
// *******************
// *******EN USO******
	@GetMapping({"choice_procedure_by_doctor/{iddoct}"})       /// ///    // 30/10/20    EMPEZAR A VER EL TRABAJO CON LOS COMBOS 
	public String choiceProcedureByDoctorSelect(@PathVariable(value = "iddoct") Long iddoct, Client cliente, Model model, SessionStatus s) 
	{
		cliente.setDoctor(iddoct);
		cliente.setP1(null);

		List <String> choices = Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
								              "Surgeon: " + dService.findOne(iddoct).getName());
		
		if (cService.getConditionsWithValueNewAll(cliente, 2).isEmpty())  // Que no tiene condición especial
			model.addAttribute("doctorlist", dService.findAll()); 
		else 
			model.addAttribute("doctorlist", dService.findAllbyConditions(cliente.getConditionsList())) ;
		
		// TRABAJO CON LAS PROMOCIONES ( POR AHORA SOLO LAS Q ESTÁN)
		//**********************************************************
		List<Promos305> activesPromos = promo305Service.findActivePromosForDoctor(new Date(), iddoct);  // para obtener las fechas si algo solo coger la fechas
		List<Date> untilDates = new ArrayList<Date>();
		for (Promos305 promos305 : activesPromos) 
			untilDates.add(promos305.getMaxDateValid());
			// estamos pensando q solo sea una...sino hacer un metodo q en vez de pasar la fecha pase el id de cada activa
		List<VIPDoctorProcedure> promo = null ;
		promo = promo305Service.findActivePromosXForDoctor(new Date(), iddoct); // activas para este doctor
		
		model.addAttribute("untildates", untilDates);
		if (promo.size()>0) 
		{
		model.addAttribute("promo", promo);
		model.addAttribute("doctorId", iddoct);
		model.addAttribute("doctorName", dService.findOne(iddoct).getName());
		}
		
		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("choices", choices);
		model.addAttribute("cardHeader", "Choose the surgeon, then the procedure");
		model.addAttribute("doctorlisth", "Surgeon list");
		model.addAttribute("procedurelisth", "Procedures list of surgeon " + dService.findOne(iddoct).getName()); 
		model.addAttribute("procedurelist", dService.findAllPrincipalProceduresbyDoctorId(iddoct));   // AQUI se sustituyó findAllProcedurebyDoctorId
		model.addAttribute("tittle", "First surgeon, then procedure");
		
		return "choice_procedure_by_doctor";
	}		
	
	
	
	@GetMapping({"view-active-promo/{iddoct}"})       /// ///    // 30/10/20    EMPEZAR A VER EL TRABAJO CON LOS COMBOS  O SEA SI dos proc que estan en promos
	public String viewActivePromoDoct(@PathVariable(value = "iddoct") Long iddoct, Model model) 
	{
		// TRABAJO CON LAS PROMOCIONES ( POR AHORA SOLO LAS Q ESTÁN)
		//**********************************************************
		List<Promos305> activesPromos = promo305Service.findActivePromos(new Date());  // para obtener las fechas si algo solo coger la fechas
		List<Date> untilDates = new ArrayList<Date>();
		for (Promos305 promos305 : activesPromos) 
			untilDates.add(promos305.getMaxDateValid());
		
		// estamos pensando q solo sea una...sino hacer un metodo q en vez de pasar la fecha pase el id de cada activa
		List<VIPDoctorProcedure> promo ;
		promo = promo305Service.findActivePromosXForDoctor(new Date(), iddoct); // activas para este doctor
		logger.info("Cantidad de promos:" + promo.size());
		
		model.addAttribute("untildates", untilDates);
		if (promo.size()>0) 
		{
			model.addAttribute("promo", promo);
			model.addAttribute("doctorName", dService.findOne(iddoct).getName());
		}
		model.addAttribute("tittle", "Specials of doctor " + dService.findOne(iddoct).getName());
		
		
		
		return "/promo/view-doctor-promo-sales" ;
	}
	
	
	
	
// *******************
// *******EN USO******    
	@GetMapping({"choice-combo-by-doctor-by-p1"})    ///    // 30/10/20    EMPEZAR A VER EL TRABAJO CON LOS COMBOS 
	public String choiceComboByDoctorByP1(Client cliente, Model model, SessionStatus st) {
		//Hasta aquí hay: Conditions, Doctor y P1 
		cliente.setP2(null);     

		if (cService.getConditionsWithValueNewAll(cliente, 2).isEmpty())  // Que no tiene condición especial
			model.addAttribute("doctorlist", dService.findAllbyProcedure(cliente.getP1())); 
		else  
			model.addAttribute("doctorlist", dService.findAllByConditionsByProcedure(cliente.getConditionsList(), cliente.getP1())) ;
		
		// Aquí se sustituyó findAllProcedurebyDoctorIdbyFirstProcedure
		List<Procedure> listProcedure = dService.findAllPrincipalProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(), cliente.getP1());  
		if (!listProcedure.isEmpty())
			model.addAttribute("procedurelist", listProcedure);
		else 
			model.addAttribute("procedurelistempty", "Sorry this surgeon don't make Combo with " + pService.findOne(cliente.getP1()).getName());

		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("choices", Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
													"Surgeon: " + dService.findOne(cliente.getDoctor()).getName(),
													"First Procedure: " + pService.findOne(cliente.getP1()).getName()));
		model.addAttribute("cardHeader", "Choose the procedure to make a Combo");
		model.addAttribute("doctorlisth", "Surgeon list"); 
		model.addAttribute("procedurelisth", "Procedures list of surgeon " + dService.findOne(cliente.getDoctor()).getName() + 
				 		   " that make combo with " + pService.findOne(cliente.getP1()).getName());
		model.addAttribute("tittle", "Choose other procedure for combo");

		return "choice-combo-by-doctor-by-p1";
	}			
	
// *******************
// *******EN USO******    
	@GetMapping({"choice-combo-by-doctor-by-p1/{iddoct}"})   
	public String choiceComboByDoctorByP1SelectDoct(@PathVariable(value = "iddoct") Long iddoct, Client cliente, Model model, SessionStatus st) {
		cliente.setDoctor(iddoct);
		cliente.setP2(null); 
		
		return this.choiceComboByDoctorByP1(cliente, model, st);
	}			
	

//****************
// *******EN USO******
	@GetMapping({"result"})
	public String result(Client cliente, Model model, SessionStatus st) 
	{  
		List <String> choices = new ArrayList<>() ;
		String observation = notRemarkHeader ;
		Boolean needAddCell = false ;
		
		choices.add("Condition: " + cService.getConditionsListCSV(cliente));
		observation = cService.getRemarksListCSV(cliente);
		
		if (cliente.getDoctor()!=null) 
		{
			choices.add("Surgeon: " + dService.findOne(cliente.getDoctor()).getName());
			choices.add("Availability date: " + dService.findOne(cliente.getDoctor()).getDate());
			choices.add("Availability early date: " + dService.findOne(cliente.getDoctor()).getEarlyDate());
			
			if (cliente.getP1()!=null) {
				choices.add(p1 + pService.findOne(cliente.getP1()).getName()); 
				model.addAttribute("observation", observation);
				needAddCell = (pService.findOne(cliente.getP1()).isRequiredCellSaver());
				if (cliente.getP2()!=null) {
					choices.add("Combo with: " + pService.findOne(cliente.getP2()).getName());
					needAddCell |= (pService.findOne(cliente.getP2()).isRequiredCellSaver());
				}
			}
			else  
				choices.add(decideNO);
			
			// CALCULANDO EL BMI y el Peso     
			Double weightA = (cliente.getHeightInches() == null)? cliente.getWeight()*1000/460: cliente.getWeight();  // Por si era en KG, ya están en libras

			if (dService.findOne(cliente.getDoctor()).isUseBMI()) { // Si el doctor se fija en el BMI
				if (cService.getBMI(cliente) > dService.findOne(cliente.getDoctor()).getMaxBMI()) {
					// si el bmi es mayor q el max
					model.addAttribute("weightProblem", noAcceptedByBMIH  + String.format("%.2f",cService.getBMI(cliente)) + noAcceptedByBMIUpperExp);
					model.addAttribute("weightCalculate", weightUpperHPre  + 
							             String.format("%.2f", weightA * (cService.getBMI(cliente) - dService.findOne(cliente.getDoctor()).getMaxBMI()) 
							            		                       / cService.getBMI(cliente)) + weightHPost + dService.findOne(cliente.getDoctor()).getName());
				}
				else if (cService.getBMI(cliente) < dService.findOne(cliente.getDoctor()).getMinBMI()) {
					model.addAttribute("weightProblem", noAcceptedByBMIH  + String.format("%.2f",cService.getBMI(cliente)) + noAcceptedByBMILowerExp);
					model.addAttribute("weightCalculate", weightLowerHPre  + 
							             String.format("%.2f", weightA * (dService.findOne(cliente.getDoctor()).getMinBMI() - cService.getBMI(cliente)) 
							            		 					   / cService.getBMI(cliente)) + weightHPost + dService.findOne(cliente.getDoctor()).getName());
				}
			}
			else { // Si el doctor se fija solo en el peso
				if (weightA > dService.findOne(cliente.getDoctor()).getMaxWeight()) {
					model.addAttribute("weightProblem", noAcceptedByWeightH  + weightA + noAcceptedByWeighExp);
					model.addAttribute("weightCalculate", weightUpperHPre + String.format("%.0f", weightA- dService.findOne(cliente.getDoctor()).getMaxWeight()) + 
														  weightHPost + dService.findOne(cliente.getDoctor()).getName());
				}
				else if (weightA < dService.findOne(cliente.getDoctor()).getMinWeight()) {
					model.addAttribute("weightProblem", noAcceptedByWeightH  + weightA + noAcceptedByBMILowerExp);
					model.addAttribute("weightCalculate", weightLowerHPre + String.format("%.0f", dService.findOne(cliente.getDoctor()).getMinWeight() - weightA) +
														  weightHPost + dService.findOne(cliente.getDoctor()).getName());
				}
			}
			// CALCULANDO EL BMI y el Peso    
			
			model.addAttribute("aditionalProcedureH", aditionalProcedureH);
			List<Procedure> aditionalProcedures = dService.findAllAditionalProcedurebyDoctorIdbyCombo(cliente.getDoctor(), cliente.getP1(), cliente.getP2());
			
			if (((dService.getProcByDoct(cliente.getDoctor(), cliente.getP1())).getCellIncludedInPrice())  // Si el Cell está incluido en el precio de P1 o de P2
				|| ((cliente.getP2() != null) && (dService.getProcByDoct(cliente.getDoctor(), cliente.getP2())).getCellIncludedInPrice())) 
			{
		//		cliente.setMakeCellSaver(true);   // el cliente se hace el cell
				aditionalProcedures.remove(pService.findProceduresByName("Cell").get(0)); // se quita de los opcionales
			}
			model.addAttribute("aditionalProcedures", aditionalProcedures);
			
		}
		else {
			if (cliente.getP1()!=null) 
				 choices.add(p1 + pService.findOne(cliente.getP1()).getName()); 
			choices.add(decideNO);
		}
		
		String header4Calendar = date4Surgery ;
		Date minDate = null ;
		if (cliente.getVip() == null)
			minDate = dService.findOne(cliente.getDoctor()).getDate() ;
		else
			minDate = dService.findOne(cliente.getDoctor()).getEarlyDate() ;
			

		// Veo si hay una superorden activa
		SuperOrder sOrder = cService.getSuperOrderWithSpecificStatusByClient(cliente.getId(), SuperOrderStatus.IN_PROCESS);
		if (sOrder != null)
		{
			Integer recoveryTime = sOrder.getSuperOrderRecoveryTime();  // Mayor tiempo de recuperación
			Calendar datewithRecovery = new GregorianCalendar();
			
			datewithRecovery.setTime(sOrder.getOrderList().get(sOrder.getOrderList().size()-1).getDateSurgery()); // tiempo elegido de la cirugía previa
			choices.add("Previus surgery date: " + datewithRecovery.get(Calendar.YEAR) + "-" + 
												   Integer.toString(datewithRecovery.get(Calendar.MONTH)+1) + "-" +  
												   datewithRecovery.get(Calendar.DATE));  // paso la fecha previa para la traza
			choices.add("Previus surgery recovery time: " + recoveryTime + " months");  // paso la fecha previa para la traza
			datewithRecovery.add(Calendar.MONTH, recoveryTime); // adiciono el tiempo de recuperacion

			if (cliente.getVip() == null && datewithRecovery.getTime().after(dService.findOne(cliente.getDoctor()).getDate()) || 
			   (cliente.getVip() != null && datewithRecovery.getTime().after(dService.findOne(cliente.getDoctor()).getEarlyDate())))
			{
				minDate = datewithRecovery.getTime() ;
				header4Calendar = date4SurgeryRecovery ;
			}
		}
		cliente.setDate(minDate);
		
		model.addAttribute("needAddCell", needAddCell && !dService.findOne(cliente.getDoctor()).isRequiredCellSaver()); 
		model.addAttribute("choices", choices);
		model.addAttribute("choice_h", questionnaireAnswerHeader);
		model.addAttribute("minDate", minDate);
		model.addAttribute("date4Surgery", header4Calendar);
		model.addAttribute("tittle", "Summary");
		model.addAttribute("needCellT", needCellT);
		
		
		model.addAttribute("vip", vipService.findAll());
		model.addAttribute("vipProcedureH", aditionalProcedureH + " included in VIP");
		List<Procedure> vipProcedures = dService.findAllAditionalProcedurebyDoctorIdbyCombo(cliente.getDoctor(), cliente.getP1(), cliente.getP2());
		vipProcedures.remove(pService.findProceduresByName("Cell").get(0)); // se quita de los opcionales
		model.addAttribute("vipProcedures", vipProcedures);
		
		return "result";
	}
	
	
	@PostMapping("vip-result")
	public String vipResult(Client cliente, Model model, SessionStatus st)
	{
		logger.info(cliente.getVip().getLipoAreasCount());
		return "redirect:/result" ;
	}
	
	
//****************
//	String cName = cService.getConditionsListCSV4Save(cliente);
//	cliente.setConditionsName(cName); // condiciones como string
	//logger.info("INDEX\\Result(POST)\\conditions >>>> " + cliente.getName() + "  <<<>>> " +cName);
// *******desarrollo******	
	@PostMapping("result")
	public String resultProcess(Client cliente, Model model, SessionStatus st) 
	{  
		Boolean cellIncluded = false ;
		//Actualizo la condicion del paciente
		cService.save(cliente);
		
		//Creo la nueva Sub-Orden
		Order order = new Order();
		order.setVip(cliente.getVip());
		
		
		// Agrego proc principales
		OrderProcedure lineP = new OrderProcedure();
		
		lineP.setProcedure(pService.findOne(cliente.getP1()));
		lineP.setIncludedInPrice(false);
		order.addProcedure(lineP);
		cellIncluded = dService.getProcByDoct(cliente.getDoctor(), cliente.getP1()).getCellIncludedInPrice() ;
		
		if (cliente.getP2() != null) 
		{
			lineP = new OrderProcedure();
			lineP.setProcedure(pService.findOne(cliente.getP2()));
			lineP.setIncludedInPrice(false);
			order.addProcedure(lineP);
			cellIncluded |= dService.getProcByDoct(cliente.getDoctor(), cliente.getP2()).getCellIncludedInPrice() ;
		}
		
		//Agrego los adicionales si VIP
		for (Procedure aditionalsVIP: cliente.getVipAditionalProcedures()) 
		{
			lineP = new OrderProcedure();
			lineP.setProcedure(aditionalsVIP);
			lineP.setIncludedInPrice(true);
			order.addProcedure(lineP);
		}
		
		// Agrego los adicionales
		Boolean cell = false ;
		for (Procedure aditionals: cliente.getAditionalProcedures()) 
		{
			lineP = new OrderProcedure();
			lineP.setProcedure(aditionals);
			lineP.setIncludedInPrice(false);
			order.addProcedure(lineP);
			if (pService.findProceduresByName("Cell").get(0).getName().equals(aditionals.getName())) // pregunto si ya está el cell seleccionado
				cell = true ;
		}
		         // sino está seleccionado y es necesario lo agrego           // o si está incluido en el precio
		if ( (!cell && (cliente.isHasWeightLoss() || cliente.isMoreOneLipo())) || cellIncluded )
		{
			lineP = new OrderProcedure();
			lineP.setProcedure(pService.findProceduresByName("Cell").get(0));
			
			if (cellIncluded)
			{
				lineP.setIncludedInPrice(true);
				order.setObservation(observationCellIncluded + changeLine);
			}
			else 
			{
				lineP.setIncludedInPrice(false);
				if (cliente.isHasWeightLoss())
					order.setObservation(observationCellByWeightLoss + changeLine);
				else
					order.setObservation(observationCellByMoreOneLipo + changeLine);
			}
			order.addProcedure(lineP);
		}
		
		String remark="" ;
		if (cService.getRemarksListCSV(cliente) != null && !cService.getRemarksListCSV(cliente).isBlank())
		{
			if (order.getObservation()!= null && !order.getObservation().isBlank())
				remark = cService.getRemarksListCSV(cliente) + order.getObservation();
			else
				remark = cService.getRemarksListCSV(cliente) ;
			order.setObservation(remark) ;
		}
		
		// Doctor y Fecha de operacion
		order.setDoctorName(dService.findOne(cliente.getDoctor()).getName());
		order.setDateSurgery(cliente.getDate());

		// Agrego la Sub-Orden a la SuperOrden "En Proceso" o creo una nueva SuperOrden y la pongo "En Proceso"
		SuperOrder sOrder = cService.getSuperOrderWithSpecificStatusByClient(cliente.getId(), SuperOrderStatus.IN_PROCESS);
		if (sOrder == null)
		{
			sOrder = new SuperOrder();
			sOrder.setStatus(SuperOrderStatus.IN_PROCESS);
			sOrder.setClient(cliente);
			sOrder.setDate(new Date());
		}
		else
		{ 
			String observation = sOrder.getObservation();
			if (observation == null)
				observation = " ";
			String procedures = sOrder.getOrderList().get(sOrder.getOrderList().size()-1).getProcedureList().get(0).getProcedure().getName();
			procedures += (sOrder.getOrderList().get(sOrder.getOrderList().size()-1).getProcedureList().size()>1)? 
							"+" + sOrder.getOrderList().get(sOrder.getOrderList().size()-1).getProcedureList().get(1).getProcedure().getName(): "";
			observation = observation.concat("Need to wait " + 
											 sOrder.getOrderList().get(sOrder.getOrderList().size()-1).getOrderRecoveryTime() + " months " + 
											 "of previus " + procedures + " surgery. " + changeLine);
			sOrder.setObservation(observation);
		}
		
		
		sOrder.getOrderList().add(order);
		cService.saveSuperOrder(sOrder);
		
		st.setComplete();
		
		return "redirect:/orders/order-form/" + sOrder.getId();  // aqui pasar ahora el seuperorder
	} 
	
	// *******************
	// *******SE QUITÓ******	
		@GetMapping({"somedoctors"})
		public String somedoctor(Client cliente, Model model, SessionStatus st)
		{
			List <String> choices = Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
					                              "Remarks: " + cService.getRemarksListCSV(cliente));
			model.addAttribute("choices", choices);
			
			model.addAttribute("msg", "Remark");
			model.addAttribute("choice_h", "Answers to the questionnaire");
			model.addAttribute("explanationb", "Only the surgeon: "); 
			model.addAttribute("explanationc", "perform the procedures on patients who have or are suffering " 
					                        + cService.getConditionsWithValueNewAll(cliente, 2)
	 									    + " condition(s). Do you agree to be treated by any of these? \r\n Do you want choice the surgeon now or see first the procedures?");
			model.addAttribute("tips", "Do you want choice the surgeon now or see first the procedures?");
			model.addAttribute("doctors", dService.findAllbyConditions(cliente.getConditionsList()));
			model.addAttribute("tittle", "Some Surgeon for theese Condition");
			
			if ((cliente.getGender().equals("Woman") && (cliente.getHbg()>hbgFUpperLimit || cliente.getHbg()<hbgFLowerLimit)) ||  // Mujer con hemoglobina fuera del rango		
				    (cliente.getGender().equals("Man")   && (cliente.getHbg()>hbgMUpperLimit || cliente.getHbg()<hbgMLowerLimit)))  // Hombre con la hemoglobina fuera del rango
					model.addAttribute("hbgOutsideRange", hbgOutsideLimits);
			
			return "somedoctors";
		}
	
}