package com.plastic305.web.app.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InfoController 
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@GetMapping({"/index"})
	public String crear(Model model) 
	{
		return "/index";
	}

	@GetMapping({"/docs/documentationEN"})
	public String docs(Model model) 
	{
		model.addAttribute("tittle", "Payment solution");
		return "/docs/documentationEN"; 
	}
	
	@GetMapping({"/docs/documentationES"})
	public String docsES(Model model) 
	{
		
		model.addAttribute("tittle", "Solución de pago");
		return "/docs/documentationES"; 
	}
	
	
}
