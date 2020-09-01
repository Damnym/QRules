package com.plastic305.web.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.ISufferingService;

@Controller
@RequestMapping("/doctor")
@SessionAttributes("doctor")
public class DoctorController {
	
	private static final String tittleList= "Doctor list" ;
	private static final String errorNotZero= "Id 0 don't exist!!!" ;
//	private static final String msg= "Formulario para nueva factura del cliente: " ;
//	private static final String msgENotLine= "La factura debe tener artículos!!!" ;
	
	@Autowired
	ISufferingService sufferingService ;
	
	@Autowired
	IDoctorService doctorService ;

	@GetMapping({"/list"})
	public String list(Model model) {
		List<Doctor> dList = doctorService.findAll();
		

		model.addAttribute("tittle", tittleList);
		model.addAttribute("doctor_list", dList);
	//	model.addAttribute("suffname", sufferingService.findByDoctorID(idD, idS))

		return "/doctor/list";
	}
	
	@GetMapping("delete/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, Model model, 
						   RedirectAttributes flash) {
		if (id > 0) {
			Doctor d = doctorService.findOne(id);
			doctorService.delete(id);
			flash.addFlashAttribute("success", "Doctor: \"" + d.getName() + 
									"\" delete successfully");
		} else 
			flash.addFlashAttribute("error", errorNotZero);
		
		return "redirect:/doctor/list";
	}
}
