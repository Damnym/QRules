package com.plastic305.web.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController 
{
	private static final String loginFirst= "Login";
	private static final String loginError= "Incorrect username or password, please try again\r\n" ;
//	private static final String logoutSuccess= "You have successfully logged out" ;
	private static final String alreadyLogged= "You have previously logged in" ;
	
	@GetMapping({"/login"})
	public String crear(@RequestParam(name = "error", required = false) String error,
			            @RequestParam(name = "logout", required = false) String logout, Model model, Principal principal, RedirectAttributes flash) 
	{
		if (principal != null) {
			flash.addFlashAttribute("info", alreadyLogged);
			return "redirect:/";
		}
		if (error != null)
			model.addAttribute("error", loginError); 
//		if (logout != null)
//			model.addAttribute("success", logoutSuccess); 
		
		model.addAttribute("msg", loginFirst); 
		
		return "login";
	}
}
