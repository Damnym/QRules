package com.plastic305.web.app.controllers;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.ProcByCombo;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.SuperOrder;
import com.plastic305.web.app.services.IComboService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.IProductService;

@Controller
@RequestMapping("/combo")
@SessionAttributes("combo")
public class ComboController {
	private static final String tittleList= "Combos" ;
//	private static final String errorNotZero= "Id 0 don't exist!!!" ;
//	private static final String tittleNewItem = "New item" ;
//	private static final String tittleViewItem = "View item" ;
	private static final String msgNotCombosP1 = "Procedures that not do combo with " ;
	private static final String msgCombosP1 = "Procedures that do combo with " ;
	private static final String msgCombos = "Procedures that do combo with the chosen" ;
//	private static final String tittleEditProduct = "Edit item" ;
//	private static final String msgEditProduct = "Edit item form" ;
//	private static final String msgViewProduct = " item details" ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired IComboService comboService;
	@Autowired IProcedureService procedureService;
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/list-combo")      
	public String guardar(Procedure procedure, Model model, RedirectAttributes flash, SessionStatus st) 
	{
		for (Combo combo: comboService.findAll()) 
			if (comboService.needRemove(combo.getProcedureList().get(0).getProcedure(), combo.getProcedureList().get(1).getProcedure(),
				   procedure.getComboList(), procedure))
				comboService.delete(combo.getId());
			
		for (Procedure proc : procedure.getComboList()) 
			if (comboService.needAdd(procedure, proc))
			{
				Combo combo = new Combo();
				
				ProcByCombo procbyCombo = new ProcByCombo();
				procbyCombo.setProcedure(procedure);
				combo.getProcedureList().add(procbyCombo);
				
				procbyCombo = new ProcByCombo();
				procbyCombo.setProcedure(proc);
				combo.getProcedureList().add(procbyCombo);
				
				comboService.save(combo);
			}
		
		return "redirect:/combo/list-combo";
	}	
	
	
	
	@Secured("ROLE_USER")
	@GetMapping({"/list-combo"})
	public String list(Model model) 
	{
		model.addAttribute("tittle", tittleList);
		model.addAttribute("msg", msgCombos);
		
		model.addAttribute("proceduresAll", procedureService.findAllPrincipal());
		
		return "/combo/list-combo";
	}
	
	

	@Secured("ROLE_USER")
	@GetMapping({"/list-combo/{id-procedure}"})
	public String list(@PathVariable(value = "id-procedure") Long id, Model model) 
	{
		Procedure procedure = procedureService.findOne(id);
		procedure.setComboList(comboService.findP2ThatComboWithP1(id));

		List<Procedure> procedures = procedureService.findAllPrincipal() ;
		procedures.remove(procedure);
		
		model.addAttribute("tittle", tittleList);
		model.addAttribute("msg", msgCombosP1);
		model.addAttribute("msgNot", msgNotCombosP1);
		
		model.addAttribute("procedure", procedure);
		model.addAttribute("procedures", procedures);
		model.addAttribute("proceduresAll", procedureService.findAllPrincipal());
		
		return "/combo/list-combo";
	}
	

}
