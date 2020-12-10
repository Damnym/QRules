package com.plastic305.web.app.controllers;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.services.IClientService;

@Secured("ROLE_USER")
@Controller
@RequestMapping("/clients")
@SessionAttributes("client")
public class ClientController {
	private static final String tittleNewClient = "New patient" ;
	private static final String tittleListClient = "Patient list" ;
	private static final String tittleEditClient = "Edit Patient" ;
	private static final String msgNewClient = "New patient form" ;
	private static final String msgEditClient = "Edit patient form" ;
	private static final String addNewClientB = "Add new patient" ;
	private static final String addNewClientAndContB = "Add new patient and continue" ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired IClientService cService;
	
	@ModelAttribute("genderList")
	public List<String> genderList() {
		return Arrays.asList("Woman", "Man");
	}
	
	//   <<<<<<   IMPLEMENTATION    >>>>>>
	
	@GetMapping({"/form"})
	public String create(Model model) {
		Client cliente = new Client();
		model.addAttribute("tittle", tittleNewClient);
		model.addAttribute("msg", msgNewClient);
		model.addAttribute("buttonClient", addNewClientB);
		model.addAttribute("buttonClientContinue", addNewClientAndContB);
		
		model.addAttribute("client", cliente);
		return "/clients/form"; 
	}
	
	@PostMapping("/form")      // CREAR Y NO HACER MAS NADA
	public String guardar(@Valid Client cliente, BindingResult bResult, Model model, RedirectAttributes flash, SessionStatus st) {
		if (bResult.hasErrors()) {
			model.addAttribute("tittle", tittleNewClient);
			model.addAttribute("msg", msgNewClient);
			model.addAttribute("buttonClient", addNewClientB);
			model.addAttribute("buttonClientContinue", addNewClientAndContB);
			return "/clients/form";
		}
		String flashMsg = (cliente.getId() != null) ? "Patient \"" + cliente.getName() + "\" edit succesfully!!" : "Patient \"" + cliente.getName() + "\" add succesfully!!";
		cService.save(cliente);
		st.setComplete();
		flash.addFlashAttribute("success", flashMsg);
		return "redirect:/clients/client-list";
	}	
	
	@PostMapping("formcontinue")   // CREAR Y SEGUIR CON EL PROCESO   aqui!!!!!!!!!!!!!!!!!!
	public String guardarAndContinue(@Valid Client cliente, BindingResult bResult, Model model, RedirectAttributes flash, SessionStatus st) {
		if (bResult.hasErrors()) {
			model.addAttribute("tittle", tittleNewClient);
			model.addAttribute("msg", msgNewClient);
			model.addAttribute("buttonClient", addNewClientB);
			model.addAttribute("buttonClientContinue", addNewClientAndContB);
			return "/clients/form";
		}
		
		String flashMsg = (cliente.getId() != null) ? "Patient \"" + cliente.getName() + "\" edit succesfully!!" : "Patient \"" + cliente.getName() + "\" add succesfully!!";
		cService.save(cliente);
		st.setComplete();
		flash.addFlashAttribute("success", flashMsg);
		return "redirect:/r1/"+cliente.getId();  
	}	
	
	@GetMapping({"/client-list"})
	public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		List<Client> cListF = cService.findAll();

		model.addAttribute("tittle", tittleListClient);
		model.addAttribute("clients_list", cListF);

		return "/clients/client-list";
	}
		
	@GetMapping("form/{id}")       //ACTUALIZAR DATOS CLIENTE
	public String editClient(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Client client = null;
		if (id > 0) {
			client = cService.findOne(id);
			if (client == null) {
				flash.addFlashAttribute("error", "Don't exist Patient with this ID");
				return "/clients/client-list";
			}
		} else {
			flash.addFlashAttribute("error", "Cliente no puede <= 0");   // cambiar mensajes
			return "/clients/client-list";
		}
		model.addAttribute("tittle", tittleEditClient);
		model.addAttribute("msg", msgEditClient);
		model.addAttribute("buttonClient", "Update patient");
		model.addAttribute("buttonClientContinue", "Update patient and continue");
		model.addAttribute("client", client);
		return "/clients/form";
	}
		
	@GetMapping("delete/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		if (id > 0) {
			Client c = cService.findOne(id);
			cService.delete(id);
			flash.addFlashAttribute("success", "\"" + c.getName() + "\" patient has been removed from the system successfully");
		} else 
			flash.addFlashAttribute("error", "Cliente no puede <= 0");    // cambiar mensajes
		
		return "redirect:/clients/client-list";
	}
	
	@GetMapping("view/{idclient}")
	public String ver(@PathVariable(value = "idclient") Long idclient, Model model, RedirectAttributes flash) {
		Client client = null;
		if (idclient > 0) {
			client = cService.findOne(idclient);
			if (client == null) {
				flash.addFlashAttribute("error", "Patient no existe con ese Id"); // cambiar mensajes
				return "redirect:/clients/client-list";
			}
		} else {
			flash.addFlashAttribute("error", "Cliente no puede <= 0"); // cambiar mensajes
			return "redirect:/clients/client-list";
		}
		model.addAttribute("tittle", "Patient details");
		model.addAttribute("msg", "'" + client.getName() + "' patient details");
		model.addAttribute("client", client);
		return "/clients/view"; 
	}	

}
