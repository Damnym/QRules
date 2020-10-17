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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.services.IClientService;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.IProductService;
import com.plastic305.web.app.services.ISufferingService;

@Controller
@SessionAttributes("client")
public class IndexController {
	private static final String tittleR1 = "Questionnaire" ;
	private static final String questionnaireAnswerHeader = "Answers to the questionnaire" ;
	private static final String decideNO = "¡¡¡You decided not to have an surgery with any doctor available!!!" ;
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
	private static final String noAcceptedByWeighExp = ", therefore is greater than what is allowed" ;
	private static final String noAcceptedByBMIUpperExp = ", Indicating your weight is greater than what is allowed" ;
	private static final String acceptedMsg = "You have been accepted, select the procedure or doctor you want" ;
	private static final String acceptedWithRemarkHeader = "Accepted with Remarks" ;
	private static final String notRemarkHeader = "Has not Remarks" ;
	private static final String acceptedHeader = "Accepted" ;
	private static final String hbgOutsideLimits = "Your hemoglobin level is outside the desired range" ;
	private static final String date4Surgery = "Choose the tentative date for your surgery, take into account the availability of the surgeon" ;
	private static final String date4SurgeryT = "Summary - Surgery date - Need cellsaver" ;
	private static final String needCellT = "Information needed to perform Lipo" ;
	
	private static final Double bmiUpperLimit = Double.valueOf(35) ;
	private static final Double poundstUpperLimit = Double.valueOf(190) ;
	private static final Double hbgFUpperLimit = Double.valueOf(16) ;
	private static final Double hbgFLowerLimit = Double.valueOf(12) ;
	private static final Double hbgMUpperLimit = Double.valueOf(18) ;
	private static final Double hbgMLowerLimit = Double.valueOf(14) ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired ISufferingService sService;
	@Autowired IClientService cService;
	@Autowired IDoctorService dService;
	@Autowired IProcedureService pService;
	@Autowired IProductService prodService; 
	
	
// *******************      
// *******EN USO******    PRIMERA (ENGLISH FEET-INCHES-POUNDS)
	@GetMapping({"/r1/{clientId}"})   
	public String form(@PathVariable(value = "clientId") Long clientId, Model model) {
		Client cliente =  cService.findOne(clientId) ;
		cService.prepare(cliente);
		
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
// *******EN USO******     (ENGLISH FEET-INCHES-POUNDS)
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
	
// *******************      NUEVO PARA AGREGAR EL BMI
// *******EN USO******	
	@PostMapping({"/r1"})    
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
		// CALCULANDO EL BMI y el Peso   AGREGAR lo de la hemoglobina
		Double weight = cliente.getWeight() ;  
		if (cliente.getHeightInches() == null) weight = weight * 1000 / 460 ; // Era en KG
		
		if (weight > poundstUpperLimit  ||   // Peso por encima de lo permitido
			cService.getBMI(cliente) > bmiUpperLimit) {     // BMI por encima de lo permitido
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
		
		Double weight = cliente.getWeight() ;  
		if (cliente.getHeightInches() == null) weight = weight * 1000 / 460 ; // Era en KG
		if (weight > poundstUpperLimit)
			decision.add(noAcceptedByWeightH + String.format("%.2f", cliente.getWeight()) + noAcceptedByWeighExp);

		double bmi = cService.getBMI(cliente) ; 
		if (bmi > bmiUpperLimit)
			decision.add(noAcceptedByBMIH + String.format("%.2f", bmi) + noAcceptedByBMIUpperExp);
		
		// Pasando al modelo
		model.addAttribute("choice_h", choiceH);
		model.addAttribute("msg", decisionH);
		model.addAttribute("choices", choices);
		model.addAttribute("decision", decision);
		model.addAttribute("tittle", "Can not apply");

		cliente.setConditionsName(cService.getConditionsListCSV4Save(cliente)); // condiciones como string
		
		cService.save(cliente);  // Salvado el cliente como quedó......
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
		model.addAttribute("choice_h", questionnaireAnswerHeader);
		if (!cService.haveRemark(cliente))  
			model.addAttribute("msg", acceptedHeader);
		else 
			model.addAttribute("msg", acceptedWithRemarkHeader);
		model.addAttribute("choice", "Condition: " + cService.getConditionsListCSV(cliente));
		model.addAttribute("explanationb", acceptedMsg);
		
		if ((cliente.getGender().equals("Woman") && (cliente.getHbg()>hbgFUpperLimit || cliente.getHbg()<hbgFLowerLimit)) ||  // Mujer con hemoglobina fuera del rango		
		    (cliente.getGender().equals("Man")   && (cliente.getHbg()>hbgMUpperLimit || cliente.getHbg()<hbgMLowerLimit)))    // Hombre con la hemoglobina fuera del rango
			model.addAttribute("hbgOutsideRange", hbgOutsideLimits);
		
		model.addAttribute("tittle", "Doctor or procedure first?");
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
		model.addAttribute("tittle", "First doctor, then procedure");
		
		return "choice_procedure_by_doctor";
	}
	
// *******************
// *******EN USO******
	@GetMapping({"choice_procedure_by_doctor/{iddoct}"})   
	public String choiceProcedureByDoctorSelect(@PathVariable(value = "iddoct") Long iddoct, Client cliente, Model model, SessionStatus s) {
		cliente.setDoctor(iddoct);
		cliente.setP1(null);
		
		model.addAttribute("choice_h", "Answers to the questionnaire");
		List <String> choices = Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
								              "Doctor: " + dService.findOne(iddoct).getName());
		model.addAttribute("choices", choices);
		
		model.addAttribute("cardHeader", "Choose the doctor, then the procedure");
		
		model.addAttribute("doctorlisth", "Doctors list");
		if (cService.getConditionsWithValue(cliente, 2).isEmpty())  // Que no tiene condición especial
			model.addAttribute("doctorlist", dService.findAll()); 
		else 
			model.addAttribute("doctorlist", dService.findAllbyConditions(cliente.getConditionsList())) ;
		
		model.addAttribute("procedurelisth", "Procedures list of doctor " + dService.findOne(iddoct).getName()); 
		model.addAttribute("procedurelist", dService.findAllProcedurebyDoctorId(iddoct)); 
		model.addAttribute("tittle", "First doctor, then procedure");		
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
		
		model.addAttribute("tittle", "Procedure, then Doctor");
		return "choice_doctor_by_procedure";
	}
		
// *******************
// *******EN USO******   
	@GetMapping({"choice_doctor_by_procedure/{idproc}"})   
	public String choiceDoctorByProcedureSelectProc(@PathVariable(value = "idproc") Long idproc, Client cliente, Model model, SessionStatus st) {
		cliente.setDoctor(null);
		cliente.setP1(idproc);
		
		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("choices", Arrays.asList("Condition: " + cService.getConditionsListCSV(cliente), 
												    "Procedure: " + pService.findOne(cliente.getP1()).getName()));
		
		model.addAttribute("cardHeader", "Choose the procedure, then the doctor");
		
		model.addAttribute("procedurelisth", "Procedures list"); 
		model.addAttribute("doctorlisth", "List of doctors who practice the " + pService.findOne(idproc).getName() + " procedure"); 

		if (cService.getConditionsWithValue(cliente, 2).isEmpty()) {
			model.addAttribute("procedurelist", pService.findAllOrder()); 
			model.addAttribute("doctorlist", dService.findAllbyProcedure(idproc)); 
		}
		else {
			model.addAttribute("procedurelist", dService.findAllProcedureOfAllDoctorsByConditions(cliente.getConditionsList()));   
			model.addAttribute("doctorlist", dService.findAllByConditionsByProcedure(cliente.getConditionsList(), idproc)); 
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
	@GetMapping({"somedoctors"})
	public String somedoctor(Client cliente, Model model, SessionStatus st) {
		
		model.addAttribute("choice", "Condition: " + cService.getConditionsListCSV(cliente));
		model.addAttribute("msg", "Observation");
		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("explanationb", "Only the doctors: "); 
		model.addAttribute("explanationc", "perform the procedures on patients who have or are suffering " 
				                         + cService.getConditionsWithValue(cliente, 2)
 										 + " condition(s). Do you agree to be treated by any of these? \r\n Do you want choice the doctor now or see first the procedures?");
		model.addAttribute("tips", "Do you want choice the doctor now or see first the procedures?");
		model.addAttribute("doctors", dService.findAllbyConditions(cliente.getConditionsList()));
		model.addAttribute("tittle", "Some Doctor for theese Condition");
		return "somedoctors";
	}
	
//****************
// *******EN USO******
	@GetMapping({"result"})
	public String result(Client cliente, Model model, SessionStatus st) {
		List <String> choices = new ArrayList<>() ;
		String observation = notRemarkHeader ;
		
		model.addAttribute("choice_h", questionnaireAnswerHeader);
		choices.add("Condition: " + cService.getConditionsListCSV(cliente));
		observation = cService.getConditionsListCSV4Save(cliente);
		
		if (cliente.getDoctor()!=null) {
			choices.add("Doctor: " + dService.findOne(cliente.getDoctor()).getName());
			choices.add("Availability date: " + dService.findOne(cliente.getDoctor()).getDate());
			if (cliente.getP1()!=null) {
				choices.add(p1 + pService.findOne(cliente.getP1()).getName()); 
				model.addAttribute("observation", observation);
				if (pService.findOne(cliente.getP1()).isRequiredCellSaver())
					model.addAttribute("needAddCell", "true");
				if (cliente.getP2()!=null) {
					choices.add("Combo with: " + pService.findOne(cliente.getP2()).getName());
					if (pService.findOne(cliente.getP2()).isRequiredCellSaver())
						model.addAttribute("needAddCell", "true");
				}
			}
			else  
				choices.add(decideNO);
		}
		else {
			if (cliente.getP1()!=null) 
				 choices.add(p1 + pService.findOne(cliente.getP1()).getName()); 
			choices.add(decideNO);
		}
		
		model.addAttribute("choices", choices);
		cliente.setDate(dService.findOne(cliente.getDoctor()).getDate());
		model.addAttribute("minDate", dService.findOne(cliente.getDoctor()).getDate());
		model.addAttribute("date4Surgery", date4Surgery);
		model.addAttribute("tittle", "Summary");
		model.addAttribute("needCellT", needCellT);
		
		return "result";
	}
	
//****************
// *******desarrollo******	
	@PostMapping("result")
	public String noAcceptedP(Client cliente, Model model, SessionStatus st) {
		String cName = cService.getConditionsListCSV4Save(cliente);
		cliente.setConditionsName(cName); // condiciones como string
		cService.save(cliente);
		return "redirect:/orders/order-form/" + cliente.getId();
	}
			
	//*********************************************************************************************
	
}