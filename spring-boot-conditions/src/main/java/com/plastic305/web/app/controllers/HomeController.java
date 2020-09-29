package com.plastic305.web.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {
	//	return "redirect:/study/index";
	//	return "redirect:https://www.google.com";
    	return "forward:/clients/client-list";
	}
}
