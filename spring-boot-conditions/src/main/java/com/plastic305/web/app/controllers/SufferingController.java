package com.plastic305.web.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.util.paginator.PageRender;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.services.ISufferingService;

@Controller
@RequestMapping("/suffering")
@SessionAttributes("suffering")
public class SufferingController {

	private static final String tittleList= "Suffering list" ;
	private static final String errorNotZero= "Id 0 don't exist!!!" ;
	
//	private static final String tittleNew= "Nuevo producto" ;
//	private static final String tittleEdit= "Editar producto" ;
//	private static final String msgNew= "Formulario para nuevo producto" ;
//	private static final String msgEdit= "Formulario para editar producto" ;
//	private static final String errorNotExist= "No existe producto con ese Id!!!" ;

	@Autowired
	ISufferingService sufferingService ;
	
	@GetMapping({"/list"})
	public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageR = PageRequest.of(page, 15);
		Page<Suffering> sList = sufferingService.findAll(pageR);
		PageRender<Suffering> pageRender = new PageRender<>("/suffering/list", sList);

		model.addAttribute("tittle", tittleList);
		model.addAttribute("suffering_list", sList);
		model.addAttribute("page", pageRender);

		return "/suffering/list";
	}
	
	@GetMapping("delete/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, Model model, 
						   RedirectAttributes flash) {
		if (id > 0) {
			Suffering s = sufferingService.findOne(id);
			sufferingService.delete(id);
			flash.addFlashAttribute("success", "Suffering: \"" + s.getName() + 
									"\" delete successfully");
		} else 
			flash.addFlashAttribute("error", errorNotZero);
		
		return "redirect:/suffering/list";
	}
	
	
}
