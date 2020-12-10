package com.plastic305.web.app.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@Secured("ROLE_USER")
	@GetMapping({"/", "/index"})
	public String home() {
    	return "forward:/clients/client-list";
	}
}
