package com.plastic305.web.app.controllers;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.plastic305.web.app.util.paginator.PageRender;

@Controller
@RequestMapping("/clients")
@SessionAttributes("client")
public class ClientController {
	private static final String tittleNewClient = "New client" ;
	private static final String tittleListClient = "Client list" ;
	private static final String tittleEditClient = "Edit client" ;
	private static final String msgNewClient = "New client form" ;
	private static final String msgEditClient = "Edit client form" ;
	
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired IClientService cService;
	
	@ModelAttribute("genderList")
	public List<String> genderList() {
		return Arrays.asList("Woman", "Man");
	}
	
	//   <<<<<<   IMPLEMENTATION    >>>>>>
	
	
	@GetMapping({"/form", "/index"})
	public String create(Model model) {
		Client cliente = new Client();
		model.addAttribute("tittle", tittleNewClient);
		model.addAttribute("msg", msgNewClient);
		model.addAttribute("buttonClient", "Add new client");
		model.addAttribute("buttonClientContinue", "Add new client and continue");
		model.addAttribute("client", cliente);
		return "/clients/form"; 
	}
	
	@PostMapping("/form")      // CREAR Y NO HACER MAS NADA
	public String guardar(@Valid Client cliente, BindingResult bResult, Model model, RedirectAttributes flash, SessionStatus st) {
		if (bResult.hasErrors()) {
			model.addAttribute("tittle", tittleNewClient);
			model.addAttribute("msg", msgNewClient);
			model.addAttribute("buttonClient", "Add new client");
			model.addAttribute("buttonClientContinue", "Add new client and continue");
			return "/clients/form";
		}
		String flashMsg = (cliente.getId() != null) ? "Client \"" + cliente.getName() + "\" edit succesfully!!" : "Client \"" + cliente.getName() + "\" add succesfully!!";
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
			model.addAttribute("buttonClient", "Add new client");
			model.addAttribute("buttonClientContinue", "Add new client and continue");
			return "/clients/form";
		}
		
		String flashMsg = (cliente.getId() != null) ? "Client \"" + cliente.getName() + "\" edit succesfully!!" : "Client \"" + cliente.getName() + "\" add succesfully!!";
		cService.save(cliente);
		st.setComplete();
		flash.addFlashAttribute("success", flashMsg);
		return "redirect:/r1/"+cliente.getId();  
	}	
	
	@GetMapping({"/client-list"})
	public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		Pageable pageR = PageRequest.of(page, 15);
		Page<Client> cList = cService.findAll(pageR);
		PageRender<Client> pageRender = new PageRender<>("/clients/client-list", cList);

		model.addAttribute("tittle", tittleListClient);
		model.addAttribute("clients_list", cList);
		model.addAttribute("page", pageRender);

		return "/clients/client-list";
	}
		
	@GetMapping("form/{id}")       //ACTUALIZAR DATOS CLIENTE
	public String editClient(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Client client = null;
		if (id > 0) {
			client = cService.findOne(id);
			if (client == null) {
				flash.addFlashAttribute("error", "Don't exist Client with this ID");
				return "/clients/client-list";
			}
		} else {
			flash.addFlashAttribute("error", "Cliente no puede <= 0");   // cambiar mensajes
			return "/clients/client-list";
		}
		model.addAttribute("tittle", tittleEditClient);
		model.addAttribute("msg", msgEditClient);
		model.addAttribute("buttonClient", "Update client");
		model.addAttribute("buttonClientContinue", "Update client and continue");
		model.addAttribute("client", client);
		return "/clients/form";
	}
		
	@GetMapping("delete/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		if (id > 0) {
			Client c = cService.findOne(id);
			cService.delete(id);
			flash.addFlashAttribute("success", "\"" + c.getName() + "\" client has been removed from the system successfully");
		} else 
			flash.addFlashAttribute("error", "Cliente no puede <= 0");    // cambiar mensajes
		
		return "redirect:/clients/client-list";
	}
	
	@GetMapping("view/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Client customer = null;
		if (id > 0) {
			customer = cService.findOne(id);
			if (customer == null) {
				flash.addFlashAttribute("error", "Cliente no existe con ese Id"); // cambiar mensajes
				return "redirect:/clients/client-list";
			}
		} else {
			flash.addFlashAttribute("error", "Cliente no puede <= 0"); // cambiar mensajes
			return "redirect:/clients/client-list";
		}
		model.addAttribute("tittle", "Client details");
		model.addAttribute("msg", "'" + customer.getName() + "' client details");
		model.addAttribute("customer", customer);
		return "/clients/view"; 
	}	

}
