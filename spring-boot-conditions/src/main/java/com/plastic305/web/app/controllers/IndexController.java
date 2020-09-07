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
import com.plastic305.web.app.services.IAttributeService;
import com.plastic305.web.app.services.IClientService;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.ISufferingService;

@Controller
@SessionAttributes("client")
public class IndexController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired IAttributeService attService;
	@Autowired ISufferingService sService;
	@Autowired IClientService cService;
	@Autowired IDoctorService dService;
	@Autowired IProcedureService pService;

	
// *******************
// *******EN USO******
	@GetMapping({"/r1", "/", "/index"})   
	public String form(Model model) {
		Client c =  new Client() ;
		 
		List <Suffering> sList = sService.findAll(); 
 		model.addAttribute("tittle", "Questionnaire");
 		model.addAttribute("msg", "Diseases that you have or presented");
 		model.addAttribute("text2client", "Please state if do you had or have any of these conditions") ;
 	    model.addAttribute("sList", sList);
 	    model.addAttribute("client", c);
		return "r1";
	}
		
// *******************
// *******EN USO******	
	@PostMapping({"/r1", "/", "/index"})    
	public String formProcess(Client cliente, Model model) {
		String url = "redirect:/doctor_or_procedure" ;
		for (Suffering c: cliente.getConditionsList()) {
			if (c.getAccepted() == 0) 
				return "redirect:/no_accepted";
			else if (c.getAccepted() == 2) 
					url = "redirect:/somedoctors";
		}
		return url;
	}
		
// *******************
// *******EN USO******	
	@GetMapping({"no_accepted"})     
	public String noAccepted(Client cliente, Model model, SessionStatus st) {
		model.addAttribute("respuestas_e", "Answers to the questionnaire");
		model.addAttribute("eleccion", "Condition: " + cService.getConditionsListCSV(cliente));
		model.addAttribute("msg", "Decision");
		model.addAttribute("respuesta_h", "You suffer or suffered:");
		model.addAttribute("respuesta_b", cService.getConditionsWithValue(cliente, 0));
		model.addAttribute("respuesta_f", "Condition(s) that makes it impossible for you to apply the desired treatment");
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
// *******EN USO******            CAMBIANDO
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
		cliente.setCombo(null);
		
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
		cliente.setCombo(null);
		
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
		cliente.setCombo(null);

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
		st.setComplete();
		return "result";
	}
	
}