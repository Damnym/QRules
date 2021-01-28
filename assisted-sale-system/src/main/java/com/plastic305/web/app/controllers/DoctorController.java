package com.plastic305.web.app.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.plastic305.web.app.models.dao.IQuotaDAO;
import com.plastic305.web.app.models.dto.QuotaCalendar;
import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.models.entities.ProcByDoct;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Quota;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.services.IComboService;
import com.plastic305.web.app.services.IDoctorService;
import com.plastic305.web.app.services.IProcByDoctService;
import com.plastic305.web.app.services.IProcedureService;
import com.plastic305.web.app.services.ISufferingService;

@Controller
@RequestMapping("/doctor")
@SessionAttributes("doctor")
public class DoctorController {
	
	private static final String errorNotZero= "Id 0 don't exist!!!" ;
	
	private static final String tittleList= "Surgeons list" ;
	private static final String tittleNewDoctor = "New Surgeon" ;
	private static final String tittleUpdateProedure = "Update procedure-surgeon data" ;
	
	private static final String bNext = "Continue" ;
	private static final String bSave = "Save" ;
	private static final String bAddProcedure = "Add procedures" ;
	private static final String bAddCondition = "Add conditions" ;
	private static final String bAddCombo = "Add Combos" ;
	private static final String bUpdate = "Edit data" ;
	
	private static final String msgAddDoctorProcedure = "Form to add procedures to surgeon " ;
	private static final String msgUpdateDoctorProcedure = "Form to update procedures to surgeon " ;
	private static final String msgAddDoctorCondition = "Form to add conditions to surgeon " ;
	private static final String msgNewDoctor = "New Surgeon form" ;
	private static final String msgDoctorView = "Surgeon data" ;
	
	private static final String matchAll = "(.*)" ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired ISufferingService sufferingService ;
	@Autowired IDoctorService doctorService ;
	@Autowired IProcedureService procedureService ;
	@Autowired IComboService comboService ;
	@Autowired IProcByDoctService procByDoctService ;
	
	@Autowired IQuotaDAO quotaDAO ;

	// <<< IMPLEMENTATION >>>	
	
	@GetMapping(value = "/load-procedure/{idDoct}/{term}", produces = {"application/json"})
	public @ResponseBody List<Procedure> loadProcedure(@PathVariable(value = "idDoct") Long id, @PathVariable String term) {
		Doctor doctor = doctorService.findOne(id);
		if (doctor.getProcList()==null || doctor.getProcList().size()==0)
		{
			List <Procedure> l = procedureService.findProceduresByName(term);
			return l;
		}
		else
		{
			List <Procedure> l = procedureService.findProceduresByName(term);
//			for (Procedure procedure : l) {
//				logger.info(">>>> " + procedure.getName());
//			}
			return doctorService.findProceduresNotBelongToDoctorByName(id, term);
		}
	}

	@GetMapping(value = "/load-condition/{idDoct}/{term}", produces = {"application/json"})
	public @ResponseBody List<Suffering> loadCondition(@PathVariable(value = "idDoct") Long id, @PathVariable String term) {
		Doctor doctor = doctorService.findOne(id);
		if (doctor.getProcList()==null || doctor.getProcList().size()==0) {
			List <Suffering> s = sufferingService.findSufferingsByName(term);
			return s;
		}
		else
			return doctorService.findConditionNotBelongToDoctorByName(id, term);
	}
	
	@GetMapping(value = "/load-procedure1/{idDoct}/{term}", produces = {"application/json"})
	public @ResponseBody List<Procedure> loadP1(@PathVariable(value = "idDoct") Long id, @PathVariable String term) 
	{
		Doctor doctor = doctorService.findOne(id);
		if (doctor.getComboList()==null || doctor.getComboList().size()==0) 
			return doctorService.findProceduresBelongToDoctorByName(id, term); 
		else
		{
			List <Procedure> procedures = new ArrayList<Procedure>();
			List <Procedure> proceduresAll = doctorService.findAllP1OfUnlinkedCombos(id);
			
			for (Procedure procedure : proceduresAll) 
				if (procedure.getName().toUpperCase().matches(matchAll + term.toUpperCase() + matchAll))
					procedures.add(procedure);
			
			return procedures;
		}
	}
	
	
	@Secured("ROLE_ADMIN")
	@GetMapping({"/form"}) //NEW
	public String create(Model model) {
		Doctor doctor = new Doctor();
//		pero en la vista poner la lista de condiciones y de procedimiento pero como dato
		
		model.addAttribute("doctor", doctor);
		model.addAttribute("tittle", tittleNewDoctor);
		model.addAttribute("msg", msgNewDoctor);
		model.addAttribute("bSave", bSave);
		
		return "/doctor/form"; 
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping({"/edit/{id-doctor}"}) //NEW
	public String Edit(@PathVariable(value = "id-doctor") Long id, Model model) {
		Doctor doctor = doctorService.findOne(id);
//		pero en la vista poner la lista de condiciones y de procedimiento pero como dato
		
		model.addAttribute("doctor", doctor);
		model.addAttribute("tittle", tittleNewDoctor);
		model.addAttribute("msg", msgNewDoctor);
		model.addAttribute("bSave", bSave);
		
		return "/doctor/form"; 
	}
		
	@Secured("ROLE_ADMIN") 
	@PostMapping("/form")      
	public String save(Doctor doctor, BindingResult bResult, Model model, SessionStatus st) 
	{
		doctorService.save(doctor);
		
		return "redirect:/doctor/view/" + doctor.getId();
	}	
	
	@Secured("ROLE_ADMIN")
	@GetMapping({"/view/{id-doctor}"}) //NEW
	public String viewDoctor(@PathVariable(value = "id-doctor") Long id, Model model) 
	{
		model.addAttribute("doctor", doctorService.findOne(id));
		
		model.addAttribute("msg", msgDoctorView);
		model.addAttribute("bContinue", bNext);
		model.addAttribute("bAddProcedure", bAddProcedure);
		model.addAttribute("bAddCondition", bAddCondition); 
		model.addAttribute("bupdateData", bUpdate);
		
		return "/doctor/view";
	}
	
	
	@Secured("ROLE_ADMIN")   // este para la cuotas
	@GetMapping("/add-procedure/{id-doctor}")
	public String addProcedure(@PathVariable(value = "id-doctor") Long id, Model model) 
	{
		model.addAttribute("doctor", doctorService.findOne(id));
		model.addAttribute("procedureByDoct", new ProcByDoct());
		
		model.addAttribute("tittle", bAddProcedure);
		model.addAttribute("msg", msgAddDoctorProcedure);
		model.addAttribute("bContinue", bNext);
		model.addAttribute("bSave", bSave);
		model.addAttribute("bAddCondition", bAddCondition);
		model.addAttribute("bAddCombo", bAddCombo);

		return "/doctor/add-procedure"; 
	}

	@Secured("ROLE_ADMIN")   // este para la cuotas
	@GetMapping("/admin-quotas/{id-doctor}")
	public String adminQuotas(@PathVariable(value = "id-doctor") Long id, Model model) 
	{
		model.addAttribute("doctor", doctorService.findOne(id));
		model.addAttribute("quota", new Quota());
		
		model.addAttribute("tittle", "Admin quotas");
		model.addAttribute("msg", "Form to add quotas of surgeon ");
		model.addAttribute("bContinue", bNext);
		model.addAttribute("bSave", bSave);
		
		return "/doctor/admin-quotas"; 
	}
	
	@Secured("ROLE_ADMIN")   // este para la cuotas
	@PostMapping("/admin-quotas")
	public String adminQuotasSave(Quota quota, Doctor doctor, Model model) 
	{
		doctor.getQuotaList().add(quota);
		doctorService.save(doctor);
		return "redirect:/doctor/admin-quotas/" + doctor.getId() ;
	}
	
	@Secured("ROLE_ADMIN")   // este para la cuotas
	@GetMapping("/add-quotas/{idq}")
	public String incQuotas(@PathVariable(value = "idq") Long idQ, Doctor doctor, Model model) 
	{
		Quota q = quotaDAO.findById(idQ).orElse(null);
		q.setQuotaAmount(q.getQuotaAmount()+1) ;
		quotaDAO.save(q);
		
		return "redirect:/doctor/admin-quotas/" + doctor.getId() ;
	}

	@Secured("ROLE_ADMIN")   // este para la cuotas
	@GetMapping("/dec-quotas/{idq}")
	public String decQuotas(@PathVariable(value = "idq") Long idQ, Doctor doctor, Model model) 
	{
		Quota q = quotaDAO.findById(idQ).orElse(null);
		q.setQuotaAmount(q.getQuotaAmount()-1) ;
		if (q.getQuotaAmount()==0)
			quotaDAO.deleteById(idQ);
		else
			quotaDAO.save(q);
		
		return "redirect:/doctor/admin-quotas/" + doctor.getId() ;
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("delete-quotas/{idq}")
	public String eliminarQuotas(@PathVariable(value = "idq") Long idQ, Doctor doctor, Model model)
	{
		quotaDAO.deleteById(idQ);
		
		return "redirect:/doctor/admin-quotas/" + doctor.getId() ;
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("update-procbydoct/{idprocbydoct}/{iddoct}")
	public String updateProcByDoct(@PathVariable(value = "idprocbydoct") Long idProcByDoct,
								   @PathVariable(value = "iddoct") Long iddoct,
								   Model model) 
	{
		model.addAttribute("doctor", doctorService.findOne(iddoct));
		model.addAttribute("procedureByDoct", procByDoctService.findOne(idProcByDoct));
		
		model.addAttribute("tittle", tittleUpdateProedure);
		model.addAttribute("msg", msgUpdateDoctorProcedure);
		model.addAttribute("bContinue", bNext);
		model.addAttribute("bSave", bSave);
		model.addAttribute("bAddCondition", bAddCondition);
		model.addAttribute("bAddCombo", bAddCombo);

		return "/doctor/add-procedure"; 
	}
	
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/add-procedure")      
	public String addProcedureSave(ProcByDoct procedureByDoct, 
								   Doctor doctor, 
								   @RequestParam(name = "procID", required = false) Long procID,  
								   BindingResult bResult, Model model, SessionStatus st) 
	{
		if (doctor.indexOfProc(procID) != -1)
		{
			doctor.getProcList().get(doctor.indexOfProc(procID)).setPostSurgeryMaxStayTime(procedureByDoct.getPostSurgeryMaxStayTime());
			doctor.getProcList().get(doctor.indexOfProc(procID)).setPostSurgeryMinStayTime(procedureByDoct.getPostSurgeryMinStayTime());
			doctor.getProcList().get(doctor.indexOfProc(procID)).setPriceCash(procedureByDoct.getPriceCash());
			doctor.getProcList().get(doctor.indexOfProc(procID)).setPriceFinanced(procedureByDoct.getPriceFinanced());
			doctor.getProcList().get(doctor.indexOfProc(procID)).setHasDrains(procedureByDoct.getHasDrains());
			doctorService.save(doctor);
			
			return "redirect:/doctor/admin-procedure/"  + doctor.getId();
		}
		else
		{
			procedureByDoct.setProcedure(procedureService.findOne(procID));
			doctor.addProcedure(procedureByDoct);
			doctorService.save(doctor);
			
			return "redirect:/doctor/add-procedure/" + doctor.getId();
		}
	}
	
//	@Secured("ROLE_ADMIN")
	@GetMapping("/change-date/{id-doctor}")
	public String changeDate(@PathVariable(value = "id-doctor") Long id, Model model) 
	{
		model.addAttribute("doctor", doctorService.findOne(id));
		
		model.addAttribute("tittle", "Change availables date");
		model.addAttribute("msg", "Change available dates of surgeon ");
		model.addAttribute("bAccept", "Accept");

		return "/doctor/change-date"; 
	}
	
//	@Secured("ROLE_ADMIN")
	@PostMapping("/change-date")      
	public String changeDateSave(Doctor doctor, BindingResult bResult, Model model, SessionStatus st) 
	{
		doctorService.save(doctor);

		return "redirect:/doctor/list";
	}
	
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/add-condition/{id-doctor}")
	public String addCondition(@PathVariable(value = "id-doctor") Long id, Model model) 
	{
//		SufferingByDoctor sufferingByDoct = new SufferingByDoctor() ;
//		model.addAttribute("sufferingByDoct", sufferingByDoct);
		
// doctorService.findConditionNotBelongToDoctorByName(id, term);
		                                              
		model.addAttribute("suffering", doctorService.findConditionNotBelongToDoctorByName(id, ""));   // NEW!!!
		model.addAttribute("doctor", doctorService.findOne(id));
		
		model.addAttribute("tittle", bAddCondition);
		model.addAttribute("msg", msgAddDoctorCondition);
		model.addAttribute("bContinue", bNext);
		model.addAttribute("bSave", bSave);
		model.addAttribute("bAddProcedure", bAddProcedure);
		model.addAttribute("bAddCombo", bAddCombo);

		return "/doctor/add-condition"; 
	}
	
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/add-condition")      
	public String addConditionSave(Doctor doctor, 
								 //  SufferingByDoctor sufferingByDoct, ESTO SOBRA SIEMPRE
								  // @RequestParam(name = "condID", required = false) Long condID,  
								   BindingResult bResult, Model model, SessionStatus st) 
	{
		for (Suffering s: doctor.getSufferingsListPre())
			doctor.addSuffering(s);
		
		doctor.getSufferingsListPre().clear();
		doctorService.save(doctor);
		
		
//		doctor.addSuffering(sufferingService.findOne(condID));  ORIGINAL ESTO
//		doctorService.save(doctor);

		return "redirect:/doctor/add-condition/" + doctor.getId();
	}
	
	
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/add-combo/{id-doctor}")
	public String addCombo(@PathVariable(value = "id-doctor") Long id, Model model) 
	{
		List<Combo> linkedCombos = doctorService.findLinkedCombo(id);
		Doctor doctor = doctorService.findOne(id) ;
		doctor.getComboListPre().clear();
		
		model.addAttribute("doctor", doctor);
		model.addAttribute("linkedCombos", linkedCombos);
		
		model.addAttribute("tittle", "Add combos");
		model.addAttribute("msg", "Form to add combos to surgeon ");
		model.addAttribute("bContinue", bNext);
		model.addAttribute("bSave", bSave);
		model.addAttribute("bAddCondition", bAddCondition);
		model.addAttribute("bChoiseP1Msg", "Select first procedure and continue");
		model.addAttribute("bAddProcedure", bAddProcedure);
		// Falta adicionar procedimientos
		return "/doctor/add-combo";
	}
	
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/add-combo/{id-doctor}/{id-p1}")
	public String addComboWithP1(@PathVariable(value = "id-doctor") Long id, 
								 @PathVariable(value = "id-p1") Long idP1,
								 Model model) 
	{
		Doctor doctor = doctorService.findOne(id) ;
		doctor.getComboListPre().clear();
		
		List<Combo> linkedCombos = doctorService.findLinkedCombo(id);
		
		List<Combo> unlinkedCombosWithP1 = doctorService.findUnlinkedComboWithP1(id, idP1);
		
		model.addAttribute("doctor", doctor);
		model.addAttribute("linkedCombos", linkedCombos);
		model.addAttribute("unlinkedCombos", unlinkedCombosWithP1);
		
		
		model.addAttribute("p1", procedureService.findOne(idP1).getName());
		model.addAttribute("bChoiseP1Msg", "View availables combos for ");
		model.addAttribute("tittle", "Add combos");
		model.addAttribute("msg", "Form to add combos to surgeon ");
		model.addAttribute("bContinue", bNext);
		model.addAttribute("bSave", bSave);
		model.addAttribute("bAddCondition", bAddCondition);
		model.addAttribute("bAddProcedure", bAddProcedure);
		// Falta adicionar boton  procedimientos
		return "/doctor/add-combo";
	}
	
	
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/add-combo")      
	public String addComboSave(Doctor doctor, BindingResult bResult, Model model, SessionStatus st) 
	{
		for (Combo combo: doctor.getComboListPre())
			doctor.addCombo(combo);
		
		doctor.getComboListPre().clear();
		doctorService.save(doctor);

		return "redirect:/doctor/add-combo/" + doctor.getId();
	}
	

//	@Secured("ROLE_ADMIN")
	@GetMapping("/admin-procedure/{id-doctor}")
	public String adminProcedure(@PathVariable(value = "id-doctor") Long id, Model model)
	{
		Doctor doctor = doctorService.findOne(id);
		
		model.addAttribute("doctor", doctor);
		
		model.addAttribute("tittle", "Manage procedures of surgeon ");
		
		return "/doctor/admin-procedure";
	}

//	@Secured("ROLE_ADMIN")
	@GetMapping("/admin-condition/{id-doctor}")
	public String adminCondition(@PathVariable(value = "id-doctor") Long id, Model model)
	{
		Doctor doctor = doctorService.findOne(id);
		
		model.addAttribute("doctor", doctor);
		
		model.addAttribute("tittle", "Manage conditions of surgeon ");
		
		return "/doctor/admin-condition";
	}
	
	
//	@Secured("ROLE_ADMIN")
	@GetMapping("/admin-combo/{id-doctor}")
	public String adminCombos(@PathVariable(value = "id-doctor") Long id, Model model)
	{
		Doctor doctor = doctorService.findOne(id);
		List<Procedure> procedures = doctorService.findAllPrincipalProceduresbyDoctorId(id);
		String [][] combos = new String[procedures.size()][procedures.size()+1];
		
		for(int i=0;i<combos.length;i++)
			combos[i][0] = "("+ procedures.get(i).getName() + ")";
		
		for(int i=0;i<combos.length;i++)
			for(int j=1;j<combos[i].length;j++)
				if (i+1==j)
					combos[i][j] = "x";
				else
					combos[i][j] = "n";
				
		for(int i=0;i<combos.length;i++)
		{
			List<Procedure> p2 = doctorService.findAllPrincipalProcedurebyDoctorIdbyFirstProcedure(id, procedures.get(i).getId());
			for (Procedure procedure : p2) 
				combos[i][procedures.indexOf(procedure)+1] = doctorService.findComboByProcedures(id, procedures.get(i).getId(), procedure.getId()).toString();
		}
		
		
		model.addAttribute("procedures", procedures);
		model.addAttribute("combos", combos);
		model.addAttribute("doctor", doctor);
		
		model.addAttribute("tittle", "Manage combos of surgeon ");
		
		return "/doctor/admin-combo";
	}
	

	@Secured("ROLE_USER")
	@GetMapping({"/list"})
	public String list(Model model) {
		List<Doctor> dList = doctorService.findAll();
		model.addAttribute("tittle", tittleList);
		model.addAttribute("doctor_list", dList);

		return "/doctor/list";
	}
	
//	@Secured("ROLE_USER")
//	@GetMapping("/list/{id-doctor}")
//	public String listWithProcedures(@PathVariable(value = "id-doctor") Long id, Model model) {
//		List<Procedure> procedures = doctorService.findAllProcedurebyDoctorId(id);
// 	    TreeMap<Procedure, List<Procedure>> proceduresCombos = new TreeMap<>(new Comparator<Procedure>() {
// 	    	public int compare(Procedure p1, Procedure p2) {
// 	    		return p1.getName().compareToIgnoreCase(p2.getName());
// 	    	}
//		});
// 	    
// 	    for (Procedure p: procedures) 
//			proceduresCombos.put(p, doctorService.findAllProcedurebyDoctorIdbyFirstProcedure(id, p.getId()));
//		
//		model.addAttribute("tittle", tittleList);
//		model.addAttribute("doctor_list", doctorService.findAll());
//		model.addAttribute("proceduresCombos", proceduresCombos);
//		model.addAttribute("doctor", doctorService.findOne(id));
//
//		return "/doctor/list";
//	}

	@Secured("ROLE_ADMIN")
	@GetMapping("delete/{id-doctor}")
	public String eliminar(@PathVariable(value = "id-doctor") Long id, Model model, RedirectAttributes flash) {
		if (id > 0) {
			doctorService.delete(id);
		} else 
			flash.addFlashAttribute("error", errorNotZero);
		
		return "redirect:/doctor/list";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("delete-procedure/{idproc}/{id-doctor}")
	public String unlinkProcedure(@PathVariable(value = "idproc") Long idProcByDoct,
								  @PathVariable(value = "id-doctor") Long id,
			                      Model model, RedirectAttributes flash) 
	{
		doctorService.unlinkProcedure(idProcByDoct);
		
		return "redirect:/doctor/admin-procedure/" + id;
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("delete-combo/{idcombo}/{id-doctor}")
	public String unlinkCombo(@PathVariable(value = "idcombo") Long idProcByDoct,
							  @PathVariable(value = "id-doctor") Long id,
							  Model model, RedirectAttributes flash) 
	{
		doctorService.unlinkCombo(idProcByDoct);
		
		return "redirect:/doctor/admin-combo/" + id;
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("delete-condition/{idcond}/{id-doctor}")
	public String unlinkCondition(@PathVariable(value = "idcond") Long idCondByDoct,
							  @PathVariable(value = "id-doctor") Long id,
							  Model model, RedirectAttributes flash) 
	{
		doctorService.unlinkCondition(idCondByDoct);
		return "redirect:/doctor/admin-condition/" + id;
	}
	
	@Secured("ROLE_USER")
	@GetMapping({"/calendar"})
	public String showCalendar(Model model) 
	{
//		model.addAttribute("tittle", "VIP calendar for all surgeon");
//		model.addAttribute("doctor_list", doctorService.findAll());
		
		List<Doctor> doctors = doctorService.findAll();
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(doctors.get(0).getDate());
		Integer month = cal.get(Calendar.MONTH) ;
		String date = cal.get(Calendar.YEAR) + "," + ++month + "," + cal.get(Calendar.DAY_OF_MONTH) ;
		model.addAttribute("regularDate", date);

		cal.setTime(doctors.get(0).getEarlyDate());
		month = cal.get(Calendar.MONTH) ;
		date = cal.get(Calendar.YEAR) + "," + ++month + "," + cal.get(Calendar.DAY_OF_MONTH) ;
		model.addAttribute("VIPDate", date);
        
		List<Quota> qList = doctors.get(0).getQuotaList() ;
		if (qList != null && qList.size()>0)
		{
			cal.setTime(qList.get(0).getDay());
			month = cal.get(Calendar.MONTH) ;
			date = cal.get(Calendar.YEAR) + "," + ++month + "," + cal.get(Calendar.DAY_OF_MONTH) ;
			model.addAttribute("quotaDate", date);
		}
		
		model.addAttribute("doctor", doctors.get(0));
		model.addAttribute("doctors", doctors);

		return "/doctor/calendar2";
	}

	@Secured("ROLE_USER")
	@GetMapping({"/calendar/{iddoct}"})
	public String showCalendarByDoctor(@PathVariable(value = "iddoct") Long idDoct, Model model) 
	{
		Calendar cal = GregorianCalendar.getInstance();
		
		cal.setTime(doctorService.findOne(idDoct).getDate());
		Integer month = cal.get(Calendar.MONTH) ;
		String date = cal.get(Calendar.YEAR) + "," + ++month + "," + cal.get(Calendar.DAY_OF_MONTH) ;
		model.addAttribute("regularDate", date);
		
		cal.setTime(doctorService.findOne(idDoct).getEarlyDate());
		month = cal.get(Calendar.MONTH) ;
		date = cal.get(Calendar.YEAR) + "," + ++month + "," + cal.get(Calendar.DAY_OF_MONTH) ;
		model.addAttribute("VIPDate", date);
		
		List<Quota> qList = doctorService.findOne(idDoct).getQuotaList() ; 
		if (qList != null && qList.size()>0)
		{
			cal.setTime(qList.get(0).getDay());
			month = cal.get(Calendar.MONTH) ;
			date = cal.get(Calendar.YEAR) + "," + ++month + "," + cal.get(Calendar.DAY_OF_MONTH) ;
			model.addAttribute("quotaDate", date);
		}
		
		model.addAttribute("doctor", doctorService.findOne(idDoct));
		model.addAttribute("doctors", doctorService.findAll());
		
		return "/doctor/calendar2";
	}
	
	@GetMapping(value = "/load-quotas/{idDoct}", produces = {"application/json"})
	public @ResponseBody List<QuotaCalendar> getQuotas(@PathVariable(value = "idDoct") Long id) 
	{
		List<Quota> quotas = doctorService.getDoctorQuotas(id);
		List<QuotaCalendar> quotasCalendar = new ArrayList<QuotaCalendar>();
		
		Calendar cal = GregorianCalendar.getInstance();
		String date ;
		Integer month ;
		
		for (Quota quota : quotas) 
		{
			cal.setTime(quota.getDay());
			month = cal.get(Calendar.MONTH) ;
			date = cal.get(Calendar.YEAR) + "," + ++month + "," + cal.get(Calendar.DAY_OF_MONTH) ;
			String text = "Quotas amount: " + quota.getQuotaAmount();
			logger.info(date);
			logger.info(text);
			
			quotasCalendar.add(new QuotaCalendar(quota.getId().toString(), text, "", date, "event", "#28A745", false));
		}
		return quotasCalendar ;
	}
	
	
}
