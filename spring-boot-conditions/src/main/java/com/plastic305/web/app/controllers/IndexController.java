package com.plastic305.web.app.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.services.IClientService;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.ISufferingService;

@Controller
@SessionAttributes("client")
public class IndexController {
	@Autowired ISufferingService sService;
	@Autowired IClientService cService;
	@Autowired IDoctorService dService;
	@Autowired IProcedureService pService;
	
// *******************
// *******EN USO******
	@GetMapping({"/r1", "/", "/index"})   
	public String form(Model model) {
		Client c =  new Client() ;
		
		List <Suffering> sList = sService.findAllRefusedOrWarning(); 
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
		if (cliente.getSuffering() !=null) {
			//Si tiene alguna de las enfermedades a analizar
			// 0: no aceptadas, 1: aceptadas, 2: depende doctor, 3: warning
			Suffering s = sService.findOne(cliente.getSuffering());
			switch (s.getAccepted()) {
				case 0: // Si no es aceptada
					return "redirect:/no_accepted";
				case 2: // depende doctor
					return "redirect:/somedoctors"; 	
				default: //  warning
					return "redirect:/doctor_or_procedure";
			}
		}
		else   // aceptadas
			return "redirect:/doctor_or_procedure";
	}
		
// *******************
// *******EN USO******	
	@GetMapping({"no_accepted"})     
	public String noAccepted(Client cliente, Model model, SessionStatus st) {
		model.addAttribute("eleccion", "Condition: " + sService.findOne(cliente.getSuffering()).getName());
		model.addAttribute("msg", "Decision");
		model.addAttribute("respuestas_e", "Answers to the questionnaire");
		model.addAttribute("respuesta_r", "You suffer or suffered \"" + sService.findOne(cliente.getSuffering()).getName() +
				                          "\", condition that makes it impossible for you to apply the desired treatment");
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
	@GetMapping({"doctor_or_procedure"})   
	public String doctorOrProcedure(Client cliente, Model model, SessionStatus s) {
		model.addAttribute("choice_h", "Answers to the questionnaire");
		if (cliente.getSuffering()==null) 
			model.addAttribute("msg", "Accepted");
		else {
			model.addAttribute("choice", "Condition: "  + sService.findOne(cliente.getSuffering()).getName());
			model.addAttribute("msg", "Accepted with Remarks");
		}
			
		model.addAttribute("explanationb", "You have been accepted, select the procedure or doctor you want"); 
		return "doctor_or_procedure";
	}
	
// *******************
// *******EN USO******
	@GetMapping({"choice_procedure_by_doctor"})   
	public String choiceProcedureByDoctor(Client cliente, Model model, SessionStatus s) {
		// Pasar, la condición, la lista de doctores
		model.addAttribute("choice_h", "Answers to the questionnaire");
		if (cliente.getSuffering()==null) 
			model.addAttribute("choices", "Condition: Other");
		else 
			model.addAttribute("choices", "Condition: " + sService.findOne(cliente.getSuffering()).getName());
		
		model.addAttribute("cardHeader", "Choose the doctor, then the procedure");
		model.addAttribute("doctorlisth", "Doctors list"); 
		if ((cliente.getSuffering()==null)||(sService.findOne(cliente.getSuffering()).getAccepted()!=2))
			model.addAttribute("doctorlist", dService.findAll()); 
		else 
			model.addAttribute("doctorlist", dService.findAllbyCondition(cliente.getSuffering())) ;
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
		List <String> choices ;
		if (cliente.getSuffering()==null) {
			choices = Arrays.asList("Condition: Other", "Doctor: " + dService.findOne(id).getName());
			model.addAttribute("choices", choices);
		}
		else {
			choices = Arrays.asList("Condition: " + sService.findOne(cliente.getSuffering()).getName(),
					                "Doctor: " + dService.findOne(id).getName());
			model.addAttribute("choices", choices);
		}
		
		model.addAttribute("cardHeader", "Choose the doctor, then the procedure");
		model.addAttribute("doctorlisth", "Doctors list");

		if ((cliente.getSuffering()==null)||(sService.findOne(cliente.getSuffering()).getAccepted()!=2))
			model.addAttribute("doctorlist", dService.findAll()); 
		else 
			model.addAttribute("doctorlist", dService.findAllbyCondition(cliente.getSuffering())) ;
		
		model.addAttribute("procedurelisth", "Procedures list of doctor " + dService.findOne(id).getName()); 
		model.addAttribute("procedurelist", dService.findAllProcedurebyDoctorId(id)); 
				
		return "choice_procedure_by_doctor";
	}		
	
// *******************
// *******EN USO******
	@GetMapping({"choice-combo-by-doctor-by-p1"})   
	public String choiceComboByDoctorByP1(Client cliente, Model model, SessionStatus st) {
		//Hasta aquí hay: suffering, Doctor y P1
		//Poner a null: P2, Combo
		cliente.setP2(null); 
		cliente.setCombo(null);
		
		model.addAttribute("choice_h", "Answers to the questionnaire");
		List <String> choices = new ArrayList<>() ;
		if (cliente.getSuffering()==null) 
			choices.add("Condition: Other");
		else 
			choices.add("Condition: " + sService.findOne(cliente.getSuffering()).getName());
		choices.add("Doctor: " + dService.findOne(cliente.getDoctor()).getName());	
		choices.add("First Procedure: " + pService.findOne(cliente.getP1()).getName());
		model.addAttribute("choices", choices);
		
		model.addAttribute("cardHeader", "Choose the procedure to make a Combo");
		model.addAttribute("doctorlisth", "Doctors list"); 
		model.addAttribute("procedurelisth", "Procedures list of doctor " + dService.findOne(cliente.getDoctor()).getName() + 
											 " that make combo with " + pService.findOne(cliente.getP1()).getName()); 
		
		if ((cliente.getSuffering()==null)||(sService.findOne(cliente.getSuffering()).getAccepted()!=2))
			model.addAttribute("doctorlist", dService.findAllbyProcedure(cliente.getP1())); 
		else  
			model.addAttribute("doctorlist", dService.findAllByConditionByProcedure(cliente.getSuffering(), cliente.getP1())) ;
			
		if (!(dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(), cliente.getP1()).isEmpty()))
			model.addAttribute("procedurelist", dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(), cliente.getP1()));
		else 
			model.addAttribute("procedurelistempty", "Sorry this doctor don't make Combo with " + pService.findOne(cliente.getP1()).getName());
	
		return "choice-combo-by-doctor-by-p1";
	}			
	
// *******************
// *******EN USO******
	@GetMapping({"choice-combo-by-doctor-by-p1/{id}"})   
	public String choiceComboByDoctorByP1SelectDoct(@PathVariable(value = "id") Long id,Client cliente, Model model, SessionStatus st) {
		//Hasta aquí hay: suffering, Doctor y P1
		//Poner a null: P2, Combo
		cliente.setDoctor(id);
		cliente.setP2(null); 
		cliente.setCombo(null);
			
		model.addAttribute("choice_h", "Answers to the questionnaire");
		List <String> choices = new ArrayList<>() ;
		if (cliente.getSuffering()==null) 
			choices.add("Condition: Other");
		else 
			choices.add("Condition: " + sService.findOne(cliente.getSuffering()).getName());
		choices.add("Doctor: " + dService.findOne(cliente.getDoctor()).getName());	
		choices.add("First Procedure: " + pService.findOne(cliente.getP1()).getName());
		model.addAttribute("choices", choices);
		
		model.addAttribute("cardHeader", "Choose the procedure to make a Combo");
		model.addAttribute("doctorlisth", "Doctors list"); 
		model.addAttribute("procedurelisth", "Procedures list of doctor " + dService.findOne(cliente.getDoctor()).getName() + 
											 " that make combo with " + pService.findOne(cliente.getP1()).getName()); 
			
		if ((cliente.getSuffering()==null)||(sService.findOne(cliente.getSuffering()).getAccepted()!=2))
			model.addAttribute("doctorlist", dService.findAllbyProcedure(cliente.getP1())); 
		else  
			model.addAttribute("doctorlist", dService.findAllByConditionByProcedure(cliente.getSuffering(), cliente.getP1())) ;
			
		if (!(dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(), cliente.getP1()).isEmpty()))
			model.addAttribute("procedurelist", dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(), cliente.getP1()));
		else 
			model.addAttribute("procedurelistempty", "Sorry this doctor don't make Combo with " + pService.findOne(cliente.getP1()).getName());
	
		return "choice-combo-by-doctor-by-p1";
	}			
	
// *******************
// *******EN USO******
	@GetMapping({"choice_doctor_by_procedure"})   
	public String choiceDoctorByProcedure(Client cliente, Model model, SessionStatus st) {
			// Pasar, la condición, la lista de doctores
		model.addAttribute("choice_h", "Answers to the questionnaire");
		if (cliente.getSuffering()==null) model.addAttribute("choices", "Condition: Other");
		else model.addAttribute("choices", "Condition: " + sService.findOne(cliente.getSuffering()).getName());
		
		model.addAttribute("cardHeader", "Choose the doctor, then the procedure");
		
		model.addAttribute("doctorlisth", "Doctors list"); 
		model.addAttribute("doctorlistempty", "Choose the procedure to see the doctors who practice them"); 
		
		model.addAttribute("procedurelisth", "Procedures list"); 
		if ((cliente.getSuffering()==null)||(sService.findOne(cliente.getSuffering()).getAccepted()!=2))
			model.addAttribute("procedurelist", pService.findAllOrder()); 
		else 
			model.addAttribute("procedurelist", 
  		    dService.findAllProcedureOfAllDoctorsByCondition(cliente.getSuffering())) ;
			
		return "choice_doctor_by_procedure";
	}
		
// *******************
// *******EN USO******
	@GetMapping({"choice_doctor_by_procedure/{id}"})   
	public String choiceDoctorByProcedureSelectProc(@PathVariable(value = "id") Long id, Client cliente, Model model, 
													SessionStatus st) {
		cliente.setDoctor(null);
		cliente.setP1(id);
		
		model.addAttribute("choice_h", "Answers to the questionnaire");
		List <String> choices ;
		if (cliente.getSuffering()==null) 
			choices = Arrays.asList("Condition: Other", "Procedure: " + pService.findOne(cliente.getP1()).getName());
		else 
			choices = Arrays.asList("Condition: " + sService.findOne(cliente.getSuffering()).getName(),
					                "Procedure: " + pService.findOne(cliente.getP1()).getName());
		model.addAttribute("choices", choices);
		
		model.addAttribute("cardHeader", "Choose the procedure, then the doctor");
		model.addAttribute("procedurelisth", "Procedures list"); 
		model.addAttribute("doctorlisth", "List of doctors who practice the " + pService.findOne(id).getName() + " procedure"); 

		List<Procedure> procedureList = null;
		List<Doctor> doctorList = null;
		if ((cliente.getSuffering()==null)||(sService.findOne(cliente.getSuffering()).getAccepted()!=2)) {
			procedureList = pService.findAllOrder();
			doctorList = dService.findAllbyProcedure(id);
		}
		else {
			procedureList = dService.findAllProcedureOfAllDoctorsByCondition(cliente.getSuffering());
			doctorList = dService.findAllByConditionByProcedure(cliente.getSuffering(), id);
		}
		model.addAttribute("doctorlist", doctorList);
		model.addAttribute("procedurelist", procedureList);
		
		return "choice_doctor_by_procedure";
	}		
	
// *******************
// *******EN USO******
	@GetMapping({"choice-combo-by-p1-by-doctor"}) 
	public String choiceComboByP1ByDoctor(Client cliente, Model model, SessionStatus st) {
		//Hasta aquí hay: suffering, Doctor y P1
		//Poner a null: P2, Combo
		cliente.setP2(null); 
		cliente.setCombo(null);
		
		model.addAttribute("choice_h", "Answers to the questionnaire");
		List <String> choices = new ArrayList<>() ;
		if (cliente.getSuffering()==null) 
			choices.add("Condition: Other");
		else 
			choices.add("Condition: " + sService.findOne(cliente.getSuffering()).getName());
		choices.add("Doctor: " + dService.findOne(cliente.getDoctor()).getName());	
		choices.add("First Procedure: " + pService.findOne(cliente.getP1()).getName());
		model.addAttribute("choices", choices);
		
		model.addAttribute("cardHeader", "Choose the procedure to make a Combo");
		
		model.addAttribute("procedurelisth", "Procedures list that make combo with " + pService.findOne(cliente.getP1()).getName() + 
				                             " for the doctor " + dService.findOne(cliente.getDoctor()).getName());
		if (!dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(), cliente.getP1()).isEmpty())
			model.addAttribute("procedurelist", dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(), 
																									cliente.getP1())); 
		else
			model.addAttribute("procedurelistempty", "Sorry this doctor don't make Combo with " + 
													  pService.findOne(cliente.getP1()).getName());
		
		model.addAttribute("doctorlisth", "List of doctors who practice the " + pService.findOne(cliente.getP1()).getName() + " procedure");
		
		if ((cliente.getSuffering()==null)||(sService.findOne(cliente.getSuffering()).getAccepted()!=2))
			model.addAttribute("doctorlist", dService.findAllbyProcedure(cliente.getP1())); 
		else
			model.addAttribute("doctorlist", dService.findAllByConditionByProcedure(cliente.getSuffering(), cliente.getP1())); 

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
		model.addAttribute("choice", "Condition: " + sService.findOne(cliente.getSuffering()).getName());
		model.addAttribute("msg", "Observation");
		model.addAttribute("choice_h", "Answers to the questionnaire");
		model.addAttribute("explanationb", "Only the doctors: "); 
		model.addAttribute("explanationc", "perform the procedures on patients who have or are suffering \"" 
				                         + sService.findOne(cliente.getSuffering()).getName()
 										 + "\" condition. Do you agree to be treated by any of these?");
		model.addAttribute("tips", "Do you want choice the doctor now or see first the procedures?");
		model.addAttribute("doctors", dService.findAllbyCondition(cliente.getSuffering()));
		return "somedoctors";
	}
	
// PROGRAMANDO
//****************
	@GetMapping({"result"})
	public String result(Client cliente, Model model, SessionStatus st) {
		model.addAttribute("choice_h", "Answers to the questionnaire");
		List <String> choices = new ArrayList<>() ;
		String observation = "Has not Remarks" ;
		if (cliente.getSuffering()!=null) {
			choices.add("Condition: " + sService.findOne(cliente.getSuffering()).getName());
			observation = sService.findOne(cliente.getSuffering()).getWarning();
		}
		if (cliente.getDoctor()!=null) {
			choices.add("Doctor: " + dService.findOne(cliente.getDoctor()).getName());
			if (cliente.getP1()!=null) {
				choices.add("First procedure: " + pService.findOne(cliente.getP1()).getName()); 
				model.addAttribute("observation", observation);
				if (cliente.getP2()!=null)
					choices.add("Combo with: " + pService.findOne(cliente.getP2()).getName());
			}
			else {
				choices.add("¡¡¡Do not decide to have surgery with any doctor available!!!");
				}
			
		}
		else {
			if (cliente.getP1()!=null) 
				 choices.add("First procedure: " + pService.findOne(cliente.getP1()).getName()); 
			choices.add("¡¡¡Do not decide to have surgery with any doctor available!!!");
				
		}
		model.addAttribute("choices", choices);
		return "result";
	}
	
	
	
// ***************************************************************************************** 
// *******ANTIGUO******
/*	
	@GetMapping("combo_by_doct_first_proc")
	public String comboListByDoctorFirstProc(Client cliente, Model model, SessionStatus st) {
		model.addAttribute("choice_h", "Choices");
		List <String> choices =
			Arrays.asList("Condition: " + sService.findOne(cliente.getSuffering()).getName(), 
					      "Doctor: " + dService.findOne(cliente.getDoctor()).getName(),
					      "First procedure: " + pService.findOne(cliente.getP1()).getName());
		model.addAttribute("choices", choices);
		model.addAttribute("cardHeader", "Select some Procedure from list");
		model.addAttribute("listHeader", "Procedures list");
		model.addAttribute("explanationb", "Do you want to choose some of these procedures"
					  	 + " for make a Combo? or you only make the first choice procedure?"); 
		model.addAttribute("procedures", 
					dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(),
					cliente.getP1()));
					
		return "combo_by_doct_first_proc"; 
	}	
	
// ********************
// *******ANTIGUO******	
	@GetMapping("combobydoctfirstdoctor")
	public String comboListByDoctorFirstDoctor(Client cliente, Model model, SessionStatus st) {
		model.addAttribute("choice_h", "Choices");
			List <String> choices =
					Arrays.asList("Condition: " + sService.findOne(cliente.getSuffering()).getName(), 
							      "Doctor: " + dService.findOne(cliente.getDoctor()).getName(),
							      "First procedure: " + pService.findOne(cliente.getP1()).getName());
			model.addAttribute("choices", choices);
			model.addAttribute("cardHeader", "Select some Procedure from list");
			model.addAttribute("listHeader", "Procedures list");
			model.addAttribute("explanationb", "Do you want to choose some of these procedures for make a Combo? "
											 + "or you only make the first choice procedure?"); 
			model.addAttribute("procedures", 
						        dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(),
						                                                            cliente.getP1()));
				
			return "combobydoctfirstdoctor";
		}	
	
// ********************
// *******ANTIGUO******	
	@GetMapping("pbydoct/{id}")
	public String p1ListByDoctor(@PathVariable(value = "id") Long id, Client cliente, Model model,
								SessionStatus st) {
		//Cliente elige este doctor
		cliente.setDoctor(id);
		cliente.setP1(null);
		// necesito lista de procedimientos de este doctor
		model.addAttribute("choice_h", "Choices");
		List <String> choices = Arrays.asList("Condition: " + sService.findOne(cliente.getSuffering()).getName(), 
									          "Doctor: " + dService.findOne(cliente.getDoctor()).getName());
		model.addAttribute("choices", choices);
		model.addAttribute("cardHeader", "Select some Procedure from list");
		model.addAttribute("listHeader", "Procedures list");
		model.addAttribute("explanationb", "Do you want to choose some of these procedures? "
										 + "In this case, do you want some combo? Or do you "
										 + "want to view other possible procedures of other doctor?"); 
		model.addAttribute("procedures", dService.findAllProcedurebyDoctorId(id));
		
		return "pbydoct";
	}
	
// *******************
// *******EN USO******
	@PostMapping("combobydoct")
	public String comboListByDoctor(Client cliente, Model model, SessionStatus st) {
		// necesito lista de procedimientos de este doctor que hacen combo con el elegido
		// findAllProcedurebyDoctorIdbyFirstProcedure
		model.addAttribute("choice_h", "Choices");
		List <String> choices = Arrays.asList("Condition: " + sService.findOne(cliente.getSuffering()).getName(), 
									          "Doctor: " + dService.findOne(cliente.getDoctor()).getName(),
									          "First procedure: " + pService.findOne(cliente.getP1()).getName());
		model.addAttribute("choices", choices);
		model.addAttribute("cardHeader", "Select some Procedure from list");
		model.addAttribute("listHeader", "Procedures list");
		model.addAttribute("explanationb", "Do you want to choose some of these procedures for make a Combo? "
										 + "or you only make the first choice procedure?"); 
		model.addAttribute("procedures", 
				           dService.findAllProcedurebyDoctorIdbyFirstProcedure(cliente.getDoctor(),
				        		                                               cliente.getP1()));
		
		return "combobydoct";
	}
	
	
	@GetMapping({"respuesta"})
	public String respuesta(Model model,SessionStatus st) {
 		st.setComplete();
 		List <String> elecciones = attService.getRespuestas();
		model.addAttribute("elecciones", elecciones);
		model.addAttribute("respuesta_r", respuestaR);
		model.addAttribute("msg", "Decisión");
		model.addAttribute("respuestas_e", "Respuestas al cuestionario");
		switch (respuestaR.charAt(0)) {
			case 'N':
				model.addAttribute("isapto", "N");
				break;
			case 'A':
				model.addAttribute("isapto", "A");
				break;
			default:
				model.addAttribute("isapto", "T");
				break;
		} 
 		attService.resetAttributes();
		return "respuesta";
	}
	
// ********************
// *******ANTIGUO******		
	@PostMapping("respuesta")
	public String respuestap(Model model,SessionStatus st) {
		return "redirect:/r1";
	}

// ********************
// *******ANTIGUO******	
	@PostMapping("programacion")
	public String programacion(Model model,SessionStatus st) {
		return "programacion";
	}
	//DESARROLLO	
		@GetMapping({"choice-doctor-by-procedure-by-condition"})   
		public String choiceDoctorByProcedureByCondition(Client cliente, Model model, 
														 SessionStatus st) {
			cliente.setP1(null);
			model.addAttribute("choice_h", "Answers to the questionnaire");
			model.addAttribute("choices", "Condition: " + 
								           sService.findOne(cliente.getSuffering()).getName());
			model.addAttribute("cardHeader", "Choose the procedure, then the doctor");
			model.addAttribute("doctorlisth", "Doctors list"); 
			model.addAttribute("doctorlistempty", "Choose the procedure to see the doctors who practice them"); 
			model.addAttribute("procedurelisth", "Procedures list"); 
			model.addAttribute("procedurelist", 
					 dService.findAllProcedureOfAllDoctorsByCondition(cliente.getSuffering())) ;
				
			return "choice-doctor-by-procedure-by-condition";
		}
		
		@GetMapping({"choice-doctor-by-procedure-by-condition/{id}"})   
		public String choiceDoctorByProcedureByConditionSelectP1(@PathVariable(value = "id") Long id, 
																 Client cliente, Model model, 
														         SessionStatus st) {
			cliente.setDoctor(null);
			cliente.setP1(id);
			model.addAttribute("choice_h", "Answers to the questionnaire");
			List <String> choices = Arrays.asList("Condition: " + 
			           						 sService.findOne(cliente.getSuffering()).getName(), 
			                                      "Procedure: " + pService.findOne(id).getName());
			model.addAttribute("choices", choices);
			model.addAttribute("cardHeader", "Choose the procedure, then the doctor");
			model.addAttribute("doctorlisth", "Doctors list"); 
			model.addAttribute("doctorlist", 
						     dService.findAllByConditionByProcedure(cliente.getSuffering(), id)); 
			model.addAttribute("procedurelisth", "Procedures list"); 
			model.addAttribute("procedurelist", 
					 dService.findAllProcedureOfAllDoctorsByCondition(cliente.getSuffering())) ;
				
			return "choice-doctor-by-procedure-by-condition";
		}
	*/
}