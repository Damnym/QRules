package com.plastic305.web.app.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.User305;
import com.plastic305.web.app.services.IUser305Service;

@Secured("ROLE_POWER")
@Controller
@RequestMapping("/users305")
//@SessionAttributes("client")
public class User305Controller {
	private static final String tittleNewUser = "New user" ;
//	private static final String tittleView = "User details" ;
	private static final String tittleListUser = "User list" ;
	private static final String tittleEditUser = "Edit user" ;
	private static final String msgNewUser = "New user form" ;
	private static final String addNewUserB = "Add new user" ;
	private static final String msgEditUser = "Edit user form" ;
	private static final String updateNewUserB = "Update user" ;

	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired IUser305Service user305Service;
	
	@ModelAttribute("authoritiesList")
	public List<String> authoritiesList() {
		return Arrays.asList("ROLE_ADMIN", "ROLE_POWER", "ROLE_USER");
	}
	
	//   <<<<<<   IMPLEMENTATION    >>>>>>
	
	@GetMapping({"/form"})
	public String create(Model model) 
	{
		User305 user305 = new User305();
		
		model.addAttribute("tittle", tittleNewUser);
		model.addAttribute("msg", msgNewUser);
		model.addAttribute("buttonUser", addNewUserB);
		model.addAttribute("user305", user305);
		
		return "/users305/form"; 
	}
	
	
	@PostMapping("/form")     
	public String guardar(User305 user305, BindingResult bResult, Model model, RedirectAttributes flash) 
	{
		if (bResult.hasErrors()) 
		{
			for (ObjectError oe: bResult.getAllErrors())
			{
				logger.info("USER>>1>>>> " + oe.getDefaultMessage());
				logger.info("USER>>2>>>> " + oe.getObjectName());
			}
			model.addAttribute("tittle", tittleNewUser);
			model.addAttribute("msg", msgNewUser);
			model.addAttribute("buttonUser", addNewUserB);
			return "/users305/form";
		}
		
		if (!user305Service.exists(user305.getUsername())) // si tiene el mismo nombre y el id es diferente son dos usuarios..error pensar bien
		{
			if (user305.getAdmin() == null)
				user305.setAdmin(false);
			if (user305.getId()!=null)
				user305Service.delete(user305.getId());
			user305Service.save(user305);
		}
		else
		{
			String flashMsg = (user305.getUsername() + " username already exists!!, please check the username in use.");
			flash.addFlashAttribute("error", flashMsg);
		}
		
		return "redirect:/users305/user-list";
	}	
	
	
	@GetMapping({"/user-list"})
	public String list(Model model) 
	{
		List<User305> uList = user305Service.findAll();
		model.addAttribute("tittle", tittleListUser);
		model.addAttribute("update", new Date());
		model.addAttribute("user_list", uList);

		return "/users305/user-list";
	}
		
	@GetMapping("form/{id}") 
	public String editClient(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash)
	{
		User305 user305 = null;
		if (id > 0) {
			user305 = user305Service.findOne(id);
			if (user305 == null) {
				flash.addFlashAttribute("error", "Don't exist User with this ID");
				return "/users305/user-list";
			}
		} else {
			flash.addFlashAttribute("error", "Cliente no puede <= 0");   // cambiar mensajes
			return "/users305/user-list";
		}
		
		model.addAttribute("tittle", tittleEditUser);
		model.addAttribute("msg", msgEditUser);
		model.addAttribute("buttonUser", updateNewUserB);
		model.addAttribute("user305", user305);
		
		return "/users305/form";
	}
		
	
	@GetMapping("delete/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) 
	{
		if (id > 0)
//			User305 user305 = user305Service.findOne(id);
			user305Service.delete(id);
		
		return "redirect:/users305/user-list";
	}
	
//	@GetMapping("view/{iduser}")
//	public String ver(@PathVariable(value = "iduser") Long id, Model model, RedirectAttributes flash) 
//	{
//		User305 user305 = null;
//		if (id > 0) {
//			user305 = user305Service.findOne(id);
//			if (user305 == null) {
//				flash.addFlashAttribute("error", "User no existe con ese Id"); // cambiar mensajes
//				return "redirect:/users305/user-list";
//			}
//		} else {
//			flash.addFlashAttribute("error", "User no puede <= 0"); // cambiar mensajes
//			return "redirect:/users305/user-list";
//		}
//		model.addAttribute("tittle", "Patient details");
//		model.addAttribute("msg", "'" + client.getName() + "' patient details");
//		model.addAttribute("client", client);
//		return "/clients/view"; 
//	}	

}
