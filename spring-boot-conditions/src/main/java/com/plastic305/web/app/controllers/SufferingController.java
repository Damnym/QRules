package com.plastic305.web.app.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.ISufferingService;

@Controller
@RequestMapping("/suffering")
@SessionAttributes("suffering")
public class SufferingController {
	private static final String tittleList= "Conditions list" ;
	private static final String errorNotZero= "Id 0 don't exist!!!" ;
	private static final String tittleNewCondition = "New Condition" ;
	private static final String bEditCondition = "Update Condition" ;
	private static final String bAddCondition = "Add Condition" ;
	private static final String msgNewCondition = "New Condition form" ;
	private static final String tittleEditCondition = "Edit Condition" ;
	private static final String msgEditCondition = "Edit Condition form" ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired ISufferingService sufferingService ;
	@Autowired IDoctorService doctorService ;
	
	@GetMapping({"/form"})
	public String create(Model model) {
		model.addAttribute("tittle", tittleNewCondition);
		model.addAttribute("msg", msgNewCondition);
		model.addAttribute("suffering", new Suffering());
		model.addAttribute("buttonaction", bAddCondition);
		return "/suffering/form"; 
	}
	
	
	@GetMapping("form/{idSuffering}")     
	public String editSuffering(@PathVariable(value = "idSuffering") Long id, Model model, RedirectAttributes flash) {
		Suffering suffering = sufferingService.findOne(id);
		
		model.addAttribute("tittle", tittleEditCondition);
		model.addAttribute("msg", msgEditCondition);
		model.addAttribute("suffering", suffering);
		model.addAttribute("buttonaction", bEditCondition); 
		return "/suffering/form";
	}
	
	
	@PostMapping("/form")      
	public String save(@Valid Suffering suffering, BindingResult bResult, Model model, RedirectAttributes flash, SessionStatus st) {
		if (bResult.hasErrors()) 
			return create(model);
		String flashMsg = (suffering.getId() != null) ? "Condition \"" + suffering.getName() + "\" edit succesfully!!" : "Condition \"" + suffering.getName() + "\" add succesfully!!";
		sufferingService.save(suffering);
		st.setComplete();
		flash.addFlashAttribute("success", flashMsg);
		return "redirect:/suffering/list";
	}
	
	@GetMapping({"/list"})
	public String list(Model model) {
		List<Suffering> sufferingList = sufferingService.findAll();
		TreeMap<Suffering, String> conditionDoctor = new TreeMap<>(new Comparator<Suffering>() {
			public int compare(Suffering s1, Suffering s2) {
 	    		return s1.getName().compareToIgnoreCase(s2.getName());
 	    	}
		});
 	   
		String doctors = null;
 	    for (Suffering s: sufferingList) {
 	    	doctors = " ";
 	    	List<Doctor> doctorList = doctorService.findAllbyCondition(s.getId());
 	    	for (Doctor d: doctorList) 
 	    		doctors = doctors.concat(d.getName()).concat(", ").concat("\n");
 	    	if (doctors.length()>2) {
 	    		doctors = doctors.substring(0, doctors.lastIndexOf(","));
 	    	logger.info(">>>>>>>>>>>>>>: " + doctors); }
 	    	conditionDoctor.put(s, doctors);
 	    }
		
		model.addAttribute("tittle", tittleList);
		model.addAttribute("conditionList", conditionDoctor);

		return "/suffering/list";
	}
	
	@GetMapping("delete/{idSuffering}")
	public String delete(@PathVariable(value = "idSuffering") Long id, Model model, RedirectAttributes flash) {
		if (id > 0) {
			Suffering s = sufferingService.findOne(id);
			sufferingService.delete(id);
			flash.addFlashAttribute("success", "Suffering: \"" + s.getName() + "\" delete successfully");
		} else 
			flash.addFlashAttribute("error", errorNotZero);
		
		return "redirect:/suffering/list";
	}
	
	
	
	
}
