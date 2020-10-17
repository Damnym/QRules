package com.plastic305.web.app.controllers;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.services.IProductService;

@Controller
@RequestMapping("/product")
@SessionAttributes("product")
public class ProductController {
	private static final String tittleList= "Items list" ;
	private static final String errorNotZero= "Id 0 don't exist!!!" ;
	private static final String tittleNewItem = "New item" ;
	private static final String bEditProduct = "Update item" ;
	private static final String bAddProduct = "Add item" ;
	private static final String msgNewItem = "New item form" ;
	private static final String tittleEditProduct = "Edit item" ;
	private static final String msgEditProduct = "Edit item form" ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired IProductService productService;
	
	@GetMapping({"/form"})
	public String create(Model model) {
		logger.info(">>>>>>>>>>>>>>>>> pasaré por aquí?????????????");
		model.addAttribute("tittle", tittleNewItem);
		model.addAttribute("msg", msgNewItem);
		model.addAttribute("product", new Product());
		model.addAttribute("buttonaction", bAddProduct);
		return "/product/form"; 
	}
	
	@PostMapping("/form")      
	public String guardar(@Valid Product product, BindingResult bResult, Model model, RedirectAttributes flash, SessionStatus st) {
		if (bResult.hasErrors()) 
			return create(model);
		logger.info(">>>>>>>>>>>: " + product.getId());
		String flashMsg = (product.getId() != null) ? "Item \"" + product.getName() + "\" edit succesfully!!" : "Item \"" + product.getName() + "\" add succesfully!!";
		productService.save(product);
		st.setComplete();
		flash.addFlashAttribute("success", flashMsg);
		return "redirect:/product/list-product";
	}	
	
	@GetMapping("form/{idProduct}")     
	public String editProduct(@PathVariable(value = "idProduct") Long id, Model model, RedirectAttributes flash) {
		Product product = productService.findOne(id);
		logger.info(">>>>>>>> Estoy entrando al update: " + product.getName());
		
		model.addAttribute("tittle", tittleEditProduct);
		model.addAttribute("msg", msgEditProduct);
		model.addAttribute("product", product);
		model.addAttribute("buttonaction", bEditProduct); 
		return "/product/form";
	}
	
	@GetMapping({"/list-product"})
	public String list(Model model) {
		model.addAttribute("tittle", tittleList);
		model.addAttribute("product_list", productService.findAll());

		return "/product/list-product";
	}
	
	@GetMapping("delete/{id-product}")
	public String eliminar(@PathVariable(value = "id-product") Long id, Model model, RedirectAttributes flash) {
		if (id > 0) {
			Product d = productService.findOne(id);
			productService.delete(id);
			flash.addFlashAttribute("success", "Item: \"" + d.getName() + "\" delete successfully");
		} else 
			flash.addFlashAttribute("error", errorNotZero);
		
		return "redirect:/product/list-product";
	}

}
