package com.plastic305.web.app.controllers;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.EnumPromoTo;
import com.plastic305.web.app.models.entities.EnumPromotionType;
import com.plastic305.web.app.models.entities.Promos305;
import com.plastic305.web.app.models.entities.Promotion;
import com.plastic305.web.app.services.IPromo305Service;

@Controller
@RequestMapping("/promo")
@SessionAttributes("promo305")
public class PromotionsController {
	private static final String tittleList= "Deals list" ;
	private static final String tittleNew = "New deals" ;
	private static final String tittleAddDetails = "Add deals details" ;
	private static final String msgAddDetails = "Add deals details form" ;
	private static final String bAdd = "Add deals" ;
	private static final String bAddDetails = "Add deals details" ;
	private static final String msgNew = "New deals form" ;
	
	private static final String errorNotZero= "Id 0 don't exist!!!" ;
	private static final String bEditProduct = "Update item" ;
	private static final String tittleEditProduct = "Edit item" ;
	private static final String msgEditProduct = "Edit item form" ;
	private static final String msgViewProduct = " item details" ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired IPromo305Service promo305Service;
	
	
	private String getNow()
	{
		Calendar a = new GregorianCalendar();
		int year = a.get(Calendar.YEAR);
		int month = a.get(Calendar.MONTH);
		int day = a.get(Calendar.DAY_OF_MONTH);
		
		return year +"-"+ (month+1) +"-"+ day ;
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping({"/form"})
	public String create(Model model) 
	{
		model.addAttribute("tittle", tittleNew);
		model.addAttribute("msg", msgNew);
		model.addAttribute("promo305", new Promos305());
		model.addAttribute("datemin", getNow());
		model.addAttribute("buttonaction", bAdd);
		model.addAttribute("buttonactionContinue", bAddDetails);
		
		return "/promo/form"; 
	}
	
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/form")      
	public String guardar(@Valid Promos305 promo305, BindingResult bResult, Model model, RedirectAttributes flash, SessionStatus st) 
	{
		if (bResult.hasErrors()) 
		{
			if (promo305.getId() != null)  // ya existe es update
			{
				logger.info("Error"); 
				model.addAttribute("tittle", tittleEditProduct);
				model.addAttribute("msg", msgEditProduct);
				model.addAttribute("buttonaction", bEditProduct); 
			} 
			else   // ya existe es update no existe todavia es error en nueva
			{
				model.addAttribute("tittle", tittleNew);
				model.addAttribute("msg", msgNew);
				model.addAttribute("datemin", getNow());
				model.addAttribute("buttonaction", bAdd);
				model.addAttribute("buttonactionContinue", bAddDetails);
			}
			return "/promo/form";
		}
		
		if (promo305.getMaxDateValid().before(promo305.getMinDateValid()))
		{
			Calendar a = new GregorianCalendar();
			a.setTime(promo305.getMinDateValid());
			String fromDate = a.get(Calendar.YEAR) + "-" + (a.get(Calendar.MONTH)+1) + "-" + a.get(Calendar.DAY_OF_MONTH);  
			a.setTime(promo305.getMaxDateValid());
			String untilDate = a.get(Calendar.YEAR) + "-" + (a.get(Calendar.MONTH)+1) + "-" + a.get(Calendar.DAY_OF_MONTH);  
			
			Promos305 promos305E = new Promos305(); 
			promos305E.setMinDateValid(promo305.getMinDateValid());
			promos305E.setMaxDateValid(promo305.getMaxDateValid());
			
			model.addAttribute("tittle", tittleNew);
			model.addAttribute("msg", msgNew);
			model.addAttribute("datemin", getNow());
			model.addAttribute("buttonaction", bAdd);
			model.addAttribute("buttonactionContinue", bAddDetails);
			
			model.addAttribute("promo305", promos305E);
			model.addAttribute("fromDate", fromDate);
			model.addAttribute("afterDate", untilDate);
			model.addAttribute("errorDate", "errorDate");
			
			return "/promo/form";
		}
		
		promo305Service.save(promo305);
		
		return "redirect:/promo/list";
	}	
	
	
	@Secured("ROLE_ADMIN")
	@GetMapping({"/add-details"})
	public String addDetails(Model model) 
	{
		model.addAttribute("tittle", tittleAddDetails);
		model.addAttribute("msg", msgAddDetails);
		model.addAttribute("promotion", new Promotion());
		model.addAttribute("buttonaction", bAdd);
		model.addAttribute("buttonactionContinue", bAddDetails);
		
		model.addAttribute("typeDeals", EnumPromotionType.values());
		model.addAttribute("applyTo", EnumPromoTo.values());
		
		return "/promo/add-details"; 
	}
	
	
	
//	
//	@Secured("ROLE_ADMIN")
//	@GetMapping("form/{idProduct}")     
//	public String editProduct(@PathVariable(value = "idProduct") Long id, Model model, RedirectAttributes flash) {
//		Product product = productService.findOne(id);
//		logger.info(">>>>>>>> Estoy entrando al update: " + product.getName());
//		
//		model.addAttribute("tittle", tittleEditProduct);
//		model.addAttribute("msg", msgEditProduct);
//		model.addAttribute("product", product);
//		model.addAttribute("buttonaction", bEditProduct); 
//		
//		return "/product/form";
//	}
//	
	@Secured("ROLE_USER")
	@GetMapping({"/list"})
	public String list(Model model) {
		model.addAttribute("tittle", tittleList);
		model.addAttribute("promos", promo305Service.findAll());

		return "/promo/list";
	}
//	
//	@Secured("ROLE_ADMIN")
//	@GetMapping("delete/{id-product}")
//	public String eliminar(@PathVariable(value = "id-product") Long id, Model model, RedirectAttributes flash) {
//		if (id > 0) {
//			Product d = productService.findOne(id);
//			productService.delete(id);
//			flash.addFlashAttribute("success", "Item: \"" + d.getName() + "\" delete successfully");
//		} else 
//			flash.addFlashAttribute("error", errorNotZero);
//		
//		return "redirect:/product/list-product";
//	}
//	
//	
//	@Secured("ROLE_USER")
//	@GetMapping({"/view/{idProduct}"})
//	public String view(@PathVariable(value = "idProduct") Long id,Model model) {
//		Product product = productService.findOne(id);
//		
//		model.addAttribute("tittle", tittleViewItem);
//		model.addAttribute("msg", product.getName() + msgViewProduct);
//		model.addAttribute("product", product);
//		
//		return "/product/view";
//	}
	

}
