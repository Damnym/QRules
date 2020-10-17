package com.plastic305.web.app.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;
import com.plastic305.web.app.services.IClientService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.IProductService;

@Controller
@RequestMapping("/procedure")
@SessionAttributes("procedure")
public class ProcedureController {
	private static final String tittleList= "Procedure list" ;
	private static final String errorNotZero= "Id 0 don't exist!!!" ;
	private static final String tittleNewProcedure = "New procedure" ;
	private static final String bEditProcedure = "Update procedure" ;
	private static final String bAddProcedure = "Add procedure" ;
	private static final String msgNewProcedure = "New procedure form" ;
	private static final String tittleEditProcedure = "Edit procedure" ;
	private static final String msgEditProcedure = "Edit procedure form" ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired IProcedureService procedureService;
	@Autowired IClientService cService;
	@Autowired IProductService prodService; 
	
	@GetMapping(value = "/load-products/{term}", produces = {"application/json"})
	public @ResponseBody List<Product> loadProducts(@PathVariable String term) { 
		return cService.findByName(term);
	}
	
	@GetMapping({"/form"})
	public String create(Model model) {
		model.addAttribute("tittle", tittleNewProcedure);
		model.addAttribute("msg", msgNewProcedure);
		model.addAttribute("procedure", new Procedure());
		model.addAttribute("buttonaction", bAddProcedure);
		return "/procedure/form"; 
	}
	
	@PostMapping("/form")      
	public String guardar(@Valid Procedure procedure, BindingResult bResult, Model model, RedirectAttributes flash, SessionStatus st, 
						  @RequestParam(name = "item_id[]", required = false) Long itemId[], @RequestParam(name = "amount[]", required = false) Integer amount[]) {
		if (bResult.hasErrors()) 
			return create(model);
		
		ProductRecommendedByProcedure lineI = null ;
		
		if (itemId!=null)
			for (int i = 0; i < itemId.length; i++) { 
				lineI = new ProductRecommendedByProcedure();
				lineI.setProduct(prodService.findOne(itemId[i]));
				lineI.setAmountRecommended(Long.valueOf(amount[i]));
				procedure.addItem(lineI);
				logger.info("Choosen Add -->: " + itemId[i].toString() + ", Cantidad: " + amount[i].toString());
		}
		
		String flashMsg = (procedure.getId() != null) ? "Procedure \"" + procedure.getName() + "\" edit succesfully!!" : "Item \"" + procedure.getName() + "\" add succesfully!!";
		
		procedureService.save(procedure);
		st.setComplete();
		flash.addFlashAttribute("success", flashMsg);
		return "redirect:/procedure/list-procedure";
	}	
	
	@GetMapping("form/{idProcedure}")     
	public String editProduct(@PathVariable(value = "idProcedure") Long id, Model model, RedirectAttributes flash) {
		Procedure procedure = null;
		if (id > 0) {
			procedure = procedureService.findOne(id);
			if (procedure == null) {
				flash.addFlashAttribute("error", "Don't exist Procedure with this ID");
				return "/procedure/list-procedure";
			}
		} else {
			flash.addFlashAttribute("error", "Procedure id can't not <= 0");   // cambiar mensajes
			return "/procedure/list-procedure";
		}
		model.addAttribute("tittle", tittleEditProcedure);
		model.addAttribute("msg", msgEditProcedure);
		logger.info(">>>> dsdssdsa: " + procedure.getProductRecommendedList().get(0).getProduct().getName());
		model.addAttribute("procedure", procedure);
		model.addAttribute("buttonaction", bEditProcedure); 
		return "/procedure/form";
	}
	
	@GetMapping({"/list-procedure"})
	public String list(Model model) {
		model.addAttribute("tittle", tittleList);
		model.addAttribute("procedureList", procedureService.findAll());

		return "/procedure/list-procedure";
	}
	
	@GetMapping("/list-procedure/{idProcedure}")
	public String listWithItem(@PathVariable(value = "idProcedure") Long id, Model model) {
		model.addAttribute("tittle", tittleList);
		model.addAttribute("procedureList", procedureService.findAll());
		model.addAttribute("prodsRecomended", procedureService.findProductsRecommended(id));
		model.addAttribute("procedureName", procedureService.findOne(id).getName());

		return "/procedure/list-procedure";
	}
	
	@GetMapping("delete/{idProcedure}")
	public String eliminar(@PathVariable(value = "idProcedure") Long id, Model model, RedirectAttributes flash) {
		if (id > 0) {
			Procedure d = procedureService.findOne(id);
			procedureService.delete(id);
			flash.addFlashAttribute("success", "Procedure: \"" + d.getName() + "\" delete successfully");
		} else 
			flash.addFlashAttribute("error", errorNotZero);
		
		return "redirect:/procedure/list-procedure";
	}

}
