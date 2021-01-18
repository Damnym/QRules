package com.plastic305.web.app.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
//import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.dto.TipsChosenDTO;
import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.models.entities.EnumSurgeryArea;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.models.entities.Tips;
import com.plastic305.web.app.services.IClientService;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.IProductService;
import com.plastic305.web.app.services.ITipsService;

@Controller
@RequestMapping("/procedure")
@SessionAttributes("procedure")
public class ProcedureController {
	private static final String tittleList= "Procedure list" ;
	private static final String tittleNewProcedure = "New procedure" ;
	private static final String tittleEditProcedure = "Edit procedure" ;
	private static final String tittleViewProcedure= "View procedure" ;
	private static final String errorNotZero= "Id 0 don't exist!!!" ;
	private static final String bEditProcedure = "Update procedure" ;
	private static final String bAddProcedure = "Add procedure" ;
	private static final String msgNewProcedure = "New procedure form" ;
	private static final String msgEditProcedure = "Edit procedure form" ;
	private static final String msgViewProcedure = " procedure  details" ;
	private static final String changeLine = "\r\n" ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired IProcedureService procedureService;
	@Autowired IClientService cService;
	@Autowired IProductService prodService; 
	@Autowired ITipsService tipsService; 
	@Autowired IDoctorService doctorService; 
	
	@GetMapping(value = "/load-products/{term}", produces = {"application/json"})
	public @ResponseBody List<Product> loadProducts(@PathVariable String term) { 
		return cService.findByName(term);
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping({"/form"})
	public String create(Model model) {
		Procedure procedure = new Procedure();
		
		procedure.addImage("man.png", 1);
		procedure.addImage("woman.png", 2);
		procedure.addImage("man.png", 3);
		procedure.addImage("woman.png",  4);
		
		model.addAttribute("tittle", tittleNewProcedure);
		model.addAttribute("msg", msgNewProcedure);
		model.addAttribute("procedure", procedure);
		model.addAttribute("buttonaction", bAddProcedure);
		return "/procedure/form"; 
	}
	
	
	///// 20-11
	@Secured("ROLE_ADMIN")
	private Procedure uploadPicture(MultipartFile picture, Procedure procedure, RedirectAttributes flash, int pos)
	{
		if (!picture.isEmpty())
		{
			String uniqueFilename = UUID.randomUUID().toString() + "_" + picture.getOriginalFilename();
			Path rootPath = Paths.get("uploads").resolve(uniqueFilename);
			Path rootAbsolutPath = rootPath.toAbsolutePath();
			try 
			{
				Files.copy(picture.getInputStream(), rootAbsolutPath);
//				flash.addFlashAttribute("info", "'" + uniqueFilename + "'" + "Upload correctly");
				procedure.addImage(uniqueFilename, pos);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return procedure;
	}
	
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/form")      
	public String guardar(@Valid Procedure procedure, BindingResult bResult, Model model, RedirectAttributes flash, SessionStatus st,
						  @RequestParam(name = "file_1", required = false) MultipartFile picture1, 
						  @RequestParam(name = "file_2", required = false) MultipartFile picture2, 
						  @RequestParam(name = "file_3", required = false) MultipartFile picture3,
						  @RequestParam(name = "file_4", required = false) MultipartFile picture4,
						  
						  @RequestParam(name = "item_id[]", required = false) Long itemId[], 
						  @RequestParam(name = "amount[]", required = false) Integer amount[],
						  
						  @RequestParam(name = "itemPrev_id[]", required = false) Long itemPrevId[], 
						  @RequestParam(name = "amountPrev[]", required = false) Integer amountPrev[]) 
	{
		if (bResult.hasErrors()) 
			return create(model);
		
		ProductRecommendedByProcedure lineI = null ;
		
		//Borrar todos los items y llenarlos de nuevo
		procedure.getProductRecommendedList().clear();
		
		if (itemPrevId!=null)
			for (int i = 0; i < itemPrevId.length; i++) { 
				lineI = new ProductRecommendedByProcedure();
				lineI.setProduct(prodService.findOne(itemPrevId[i]));
				lineI.setAmountRecommended(Long.valueOf(amountPrev[i]));
				procedure.addItem(lineI);
				logger.info("Choosen Update -->: " + itemPrevId[i].toString() + ", Cantidad: " + amountPrev[i].toString());
			}

		if (itemId!=null)
			for (int i = 0; i < itemId.length; i++) { 
				lineI = new ProductRecommendedByProcedure();
				lineI.setProduct(prodService.findOne(itemId[i]));
				lineI.setAmountRecommended(Long.valueOf(amount[i]));
				procedure.addItem(lineI);
				logger.info("Choosen Add -->: " + itemId[i].toString() + ", Cantidad: " + amount[i].toString());
			}
		if (picture1 != null)
			procedure = this.uploadPicture(picture1, procedure, flash, 1);
		if (picture2 != null)
			procedure = this.uploadPicture(picture2, procedure, flash, 2);
		if (picture3 != null)
			procedure = this.uploadPicture(picture3, procedure, flash, 3);
		if (picture4 != null)
			procedure = this.uploadPicture(picture4, procedure, flash, 4);

//		String flashMsg = (procedure.getId() != null) ? "Procedure \"" + procedure.getName() + "\" edit succesfully!!" : "Item \"" + procedure.getName() + "\" add succesfully!!";
		procedureService.save(procedure);
		st.setComplete();
//		flash.addFlashAttribute("success", flashMsg);
		
		return "redirect:/procedure/list-procedure";
	}	
	
	
	@Secured("ROLE_ADMIN")
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
		model.addAttribute("procedure", procedure);
		model.addAttribute("buttonaction", bEditProcedure); 
		return "/procedure/form";
	}
	
	
	@Secured("ROLE_USER")
	@GetMapping({"/list-procedure"})
	public String list(Model model) 
	{
		model.addAttribute("tittle", tittleList);
		model.addAttribute("procedureList", procedureService.findAll());
		model.addAttribute("update", new Date());

		return "/procedure/list-procedure";
	}
	
	@Secured("ROLE_USER")
	@GetMapping("/list-procedure/{idProcedure}")
	public String listWithItem(@PathVariable(value = "idProcedure") Long id, Model model) {
		model.addAttribute("tittle", tittleList);
		model.addAttribute("procedureList", procedureService.findAll());
		model.addAttribute("prodsRecomended", procedureService.findProductsRecommended(id));
		model.addAttribute("procedureName", procedureService.findOne(id).getName());
		model.addAttribute("update", new Date());

		return "/procedure/list-procedure";
	}
	
	@Secured("ROLE_ADMIN")
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
	
	
	
	@Secured("ROLE_USER")
	@GetMapping({"/view/{idProcedure}"})
	public String view(@PathVariable(value = "idProcedure") Long id,Model model) {
		Procedure procedure = procedureService.findOne(id);
		
		model.addAttribute("tittle", tittleViewProcedure);
		model.addAttribute("msg", procedure.getName() + msgViewProcedure);
		model.addAttribute("procedure", procedure);
		
		return "/procedure/view";
	}

	
	@Secured("ROLE_USER")
	@GetMapping({"/procedure-decide"})
	public String procedureDecide(Model model) 
	{
		model.addAttribute("tittle", "Help to decide for procedures");
		model.addAttribute("tips", tipsService.findAll());
		model.addAttribute("tipschosen", new TipsChosenDTO());
		return "/procedure/procedure-decide";
	}
	
	
	@Secured("ROLE_ADMIN")
	@GetMapping({"/tips-form"})
	public String createTips(Model model) {
		Tips tips = new Tips();
		
		model.addAttribute("tips", tips);
		model.addAttribute("tittle", "New Tip");
		model.addAttribute("msg", "New tip form");
		model.addAttribute("area", EnumSurgeryArea.values()); 

		model.addAttribute("bSave", "Save");
		
		return "/procedure/tips-form"; 
	}
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/tips-form")      
	public String save(@Valid Tips tips, BindingResult bResult, Model model) 
	{
		if (bResult.hasErrors()) 
		{
			if (tips.getId() != null)
			{
				model.addAttribute("tittle", "Edit Tip");
				model.addAttribute("msg", "Edit tip form");
			}
			else
			{
				model.addAttribute("tittle", "New Tip");
				model.addAttribute("msg", "New tip form");
			}
			model.addAttribute("buttonaction", "Save"); 
			
			return "/procedure/tips-form";
		}
		tipsService.save(tips);
		
		return "redirect:/procedure/list-procedure";
	}
	
	
	@Secured("ROLE_USER")
	@PostMapping({"/procedure-decide"})
	public String procedureDecideAnswer(TipsChosenDTO tipsChosen, Model model) 
	{
		model.addAttribute("tittle", "Help to decide for procedures");
		model.addAttribute("tips", tipsService.findAll());
		model.addAttribute("tipschosen", tipsChosen);
		if (tipsChosen.getChosen().size()>0)
		{
			TreeMap<Procedure, String> procedureDoctor = new TreeMap<>(new Comparator<Procedure>() {
				public int compare(Procedure p1, Procedure p2) {
	 	    		return p1.getName().compareToIgnoreCase(p2.getName());
	 	    	}
			});
		
			String doctors;
			List<Procedure> procedures = procedureService.findByTips(tipsChosen) ;
	 	    for (Procedure p: procedures) 
	 	    {
	 	    	doctors = "";
	 	    	List<Doctor> doctorList = doctorService.findAllbyProcedure(p.getId());
	 	    	if (!doctorList.isEmpty())
	 	    	{
		 	    	for (Doctor d: doctorList) 
		 	    		doctors += d.getName() + " ($" + doctorService.getProcedurePrice(d.getId(),p.getId(), false) + 
		 	    		                         " / $"  +  doctorService.getProcedurePrice(d.getId(),p.getId(), true) + "), " + changeLine;
		 	    	if (doctors.length()>2) 
		 	    		doctors = doctors.substring(0, doctors.lastIndexOf(","));
		 	    	procedureDoctor.put(p, doctors);
	 	    	}
	 	    }
	 	    model.addAttribute("procedures", procedureDoctor);
	 	    
	 	    List<Combo> combos = new ArrayList<Combo>() ;
	 	    for (Combo combo : procedureService.findComboByTips(tipsChosen)) 
	 	    {
				if (!doctorService.findAllbyProcedure(combo.getProcedureList().get(0).getProcedure().getId()).isEmpty() && 
					!doctorService.findAllbyProcedure(combo.getProcedureList().get(1).getProcedure().getId()).isEmpty())   // no hacen al menos 1 de los dos proc
				{
					combos.add(combo);
				}
			}
	 	    model.addAttribute("combos", combos);
	 	    
		}
		
		return "procedure/procedure-decide";
	}

}
