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
		List<String> docs = new ArrayList<String>();
		File docsFolder = new File("documents");
//		logger.info("cantidad: " + docsFolder.list().length);

		model.addAttribute("documents", docsFolder.list());
//		model.addAttribute("doc", docs.get(0));

		return "/index";
	}
	
	
}
