package com.plastic305.web.app.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

//import javax.validation.Valid;

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
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.models.entities.Procedure;
//import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.models.entities.SufferingByDoctor;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.ISufferingService;

@Controller
@RequestMapping("/doctor")
@SessionAttributes("doctor")
public class DoctorController {
	
	private static final String tittleList= "Doctor list" ;
	private static final String errorNotZero= "Id 0 don't exist!!!" ;
	private static final String tittleNewDoctor = "New Doctor" ;
//	private static final String bEditDoctor = "Update Doctor" ;
	private static final String bAddCDoctor = "Add Doctor" ;
	private static final String msgNewDoctor = "New Doctor form" ;
//	private static final String tittleEditDoctor = "Edit Doctor" ;
//	private static final String msgEditDoctor = "Edit Doctor form" ;
	private static final String msgAddProcedureToDoctor = "Add procedure to Dr. " ;
	private static final String msgAvailableProcedures = "List of available procedures" ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired ISufferingService sufferingService ;
	@Autowired IDoctorService doctorService ;
	
	
	@GetMapping(value = "/load-procedure/{idDoct}/{term}", produces = {"application/json"})
	public @ResponseBody List<Procedure> loadProducts(@PathVariable(value = "idDoct") Long id, @PathVariable String term) { 
		return doctorService.findProceduresNotBelongToDoctorByName(id, term);
	}
	
	@GetMapping({"/form"}) //NEW
	public String create(Model model) {
		Doctor doctor = new Doctor();
		List <Suffering> sList = sufferingService.findAll();
		List <SufferingByDoctor> sbydL = new ArrayList<SufferingByDoctor>();
		
		Long i = Long.valueOf(1);
		for (Suffering s: sList) {
			SufferingByDoctor sbyd = new SufferingByDoctor();
			sbyd.setSuffering(s);
			sbyd.setId(i++);
			sbydL.add(sbyd);
		}
		doctor.setSufferingsList(sbydL);
		
		model.addAttribute("tittle", tittleNewDoctor);
		model.addAttribute("msg", msgNewDoctor);
		
		model.addAttribute("doctor", doctor);
		model.addAttribute("sbydList", sbydL);
		
		model.addAttribute("buttonaction", bAddCDoctor);
		return "/doctor/form"; 
	}
	
	
	@PostMapping("/form")      
	public String guardar(Doctor doctor, BindingResult bResult, Model model, RedirectAttributes flash, SessionStatus st) {
		for (SufferingByDoctor a : doctor.getSufferingsList()) {
			logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>: " + a.getSuffering().getName());
		}
		
		return "/doctor/list";
	}	

	@GetMapping("/add-procedure/{id-doctor}")
	public String addProcedure(@PathVariable(value = "id-doctor") Long id, Model model) {
		model.addAttribute("msg", msgAddProcedureToDoctor);
		model.addAttribute("procedureHeader", msgAvailableProcedures);
		model.addAttribute("availableProcedures", doctorService.findProceduresNotBelongToDoctor(id));
		//model.addAttribute("doctor", doctorService.findOne(id));
		
		for(Procedure p: doctorService.findProceduresNotBelongToDoctor(id))
			logger.info(">>>>>>>>>>>>>>>> DESDE EL ADD-PROCEDURE DE DOCTOR-CONTROLLER" + p.getName());
		return "/doctor/add-procedure"; 
	}

	@GetMapping({"/list"})
	public String list(Model model) {
		List<Doctor> dList = doctorService.findAll();
		model.addAttribute("tittle", tittleList);
		model.addAttribute("doctor_list", dList);

		return "/doctor/list";
	}
	
	@GetMapping("/list/{id-doctor}")
	public String listWithProcedures(@PathVariable(value = "id-doctor") Long id, Model model) {
		List<Procedure> procedures = doctorService.findAllProcedurebyDoctorId(id);
 	    TreeMap<Procedure, List<Procedure>> proceduresCombos = new TreeMap<>(new Comparator<Procedure>() {
 	    	public int compare(Procedure p1, Procedure p2) {
 	    		return p1.getName().compareToIgnoreCase(p2.getName());
 	    	}
		});
 	    
 	    for (Procedure p: procedures) 
			proceduresCombos.put(p, doctorService.findAllProcedurebyDoctorIdbyFirstProcedure(id, p.getId()));
		
		model.addAttribute("tittle", tittleList);
		model.addAttribute("doctor_list", doctorService.findAll());
		model.addAttribute("proceduresCombos", proceduresCombos);
		model.addAttribute("doctor", doctorService.findOne(id));

		return "/doctor/list";
	}

	@GetMapping("delete/{id-doctor}")
	public String eliminar(@PathVariable(value = "id-doctor") Long id, Model model, RedirectAttributes flash) {
		if (id > 0) {
			Doctor d = doctorService.findOne(id);
			doctorService.delete(id);
			flash.addFlashAttribute("success", "Doctor: \"" + d.getName() + "\" delete successfully");
		} else 
			flash.addFlashAttribute("error", errorNotZero);
		
		return "redirect:/doctor/list";
	}
	
	
	
}
