package com.plastic305.web.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.ComboByDoctor;
import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.models.entities.ProcByDoct;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Suffering;

public interface IDoctorDAO extends CrudRepository<Doctor, Long> {
	
	public List<Doctor> findAllByOrderByName();
	
	@Query("SELECT d FROM Doctor d "
			+ "join fetch d.sufferingsList sl "
			+ "join fetch sl.suffering s "
			+ "WHERE s.id=?1")
	public List<Doctor> fetchDoctorsBySufferingId(Long id);
	
	@Query("SELECT pl.priceCash FROM Doctor d "
			+ "join d.procList pl "
			+ "join pl.procedure p "
			+ "WHERE d.id=?1 AND p.id=?2")
	public Double getPriceCash(Long idD, Long idP);
	
	@Query("SELECT pl.priceFinanced FROM Doctor d "
			+ "join d.procList pl "
			+ "join pl.procedure p "
			+ "WHERE d.id=?1 AND p.id=?2")
	public Double getPriceFinanced(Long idD, Long idP);
	
	@Query("SELECT d FROM Doctor d "
			+ "join fetch d.procList pl "
			+ "join fetch pl.procedure p "
			+ "WHERE p.id=?1")
	public List<Doctor> fetchDoctorsByProcedureId(Long id);
	
	@Query("SELECT d FROM Doctor d "
			+ "join d.sufferingsList sl "
			+ "join sl.suffering s "
			+ "join d.procList pl "
			+ "join pl.procedure p "
			+ "WHERE s.id=?1 AND p.id=?2")
	public List<Doctor> fetchDoctorsBySufferingIdByProcedureId(Long idC, Long idP);

	@Query("SELECT p FROM Doctor d "
			+ "join d.procList pl "
			+ "join pl.procedure p "
			+ "WHERE d.id=?1 "
			+ "ORDER BY p.name")
	public List<Procedure> fetchProceduresByDoctorId(Long id); // todos los procedimienos por doctor
	
	@Query("SELECT p FROM Doctor d "
			+ "join d.procList pl "
			+ "join pl.procedure p "
			+ "WHERE d.id=?1 AND p.aditional = false "
			+ "ORDER BY p.name")
	public List<Procedure> findPProceduresByDoctorId(Long id);    // procedimienos PRINCIPALES por doctor

	@Query("SELECT p FROM Doctor d "
			+ "join d.procList pl "
			+ "join pl.procedure p "
			+ "WHERE d.id=?1 AND p.aditional = true "
			+ "ORDER BY p.name")
	public List<Procedure> findAProceduresByDoctorId(Long id);    // procedimienos ADICIONALES por doctor
	
	
	@Query("SELECT proc FROM Procedure proc "
		    + "WHERE proc NOT IN (SELECT p FROM Doctor d "
			+ 				 	    "join d.procList pl "
			+ 				 	    "join pl.procedure p "
			+ 					  "WHERE d.id=?1) "
			+ "ORDER BY proc.name")
	public List<Procedure> findProceduresNotBelongToDoctor(Long id);
	
	
	@Query("SELECT proc FROM Procedure proc "
			+ "WHERE (proc.name like %?2%) "
			+ "   AND proc NOT IN (SELECT p FROM Doctor d "
			+ 				 	     "join d.procList pl "
			+ 				 	     "join pl.procedure p "
			+ 					   "WHERE d.id=?1) "
			+ "ORDER BY proc.name")
	public List<Procedure> findProceduresNotBelongToDoctorByName(Long id, String term);
	
	
	@Query("SELECT s FROM Suffering s "
			+ "WHERE (s.name like %?2%) "
			+ "   AND s NOT IN (SELECT c FROM Doctor d "
			+ 				 	     "join d.sufferingsList sl "
			+ 				 	     "join sl.suffering c "
			+ 				   "WHERE d.id=?1) "
			+ "ORDER BY s.name")
	public List<Suffering> findConditionNotBelongToDoctorByName(Long id, String term);
	
	
//	+ "WHERE s NOT IN (SELECT c FROM Doctor d "
//	+ 				 	 "join d.sufferingsList sl "
//	+ 				 	 "join sl.suffering c "
//	+ 				   "WHERE d.id=?1) "

	@Query("SELECT s FROM Suffering s "
			
			+ "ORDER BY s.name")
	public List<Suffering> findConditionNotBelongToDoctor(Long id);
	
	
	@Query("SELECT p FROM Doctor d "
			+ "join d.sufferingsList sl "
			+ "join sl.suffering s "
			+ "join d.procList pl "
			+ "join pl.procedure p "
			+ "WHERE s.id=?1")
	public List<Procedure> fetchProceduresByAllDoctorBySufferingId(Long id);
	
	@Query("SELECT pbyd FROM Doctor d "
			+ "join d.procList pbyd "
			+ "join pbyd.procedure p "
			+ "WHERE d.id=?1 and p.id=?2")
	public ProcByDoct findProcByDoct(Long idD, Long idP);
	
	@Query("DELETE FROM ProcByDoct p "
			+ "WHERE p.id=?1")
	public void deleteProcByDoct(Long idPbD);
	
	
	@Query("SELECT combo FROM Combo combo "
		    + "WHERE combo NOT IN (SELECT c FROM Doctor d "
			+ 				 	    "join d.comboList cl "
			+ 				 	    "join cl.combo c "
			+ 					  "WHERE d.id=?1)")
	public List<Combo> findCombosNotBelongToDoctor(Long id);

	
	@Query("SELECT combo FROM Combo combo "
		    + "WHERE combo IN (SELECT c FROM Doctor d "
			+ 				 	    "join d.comboList cl "
			+ 				 	    "join cl.combo c "
			+ 				   "WHERE d.id=?1)")
	public List<Combo> findCombosBelongToDoctor(Long id);
	
	
	@Query("SELECT proc FROM Procedure proc "
			+ "WHERE (proc.name like %?2%) "
			+ "   AND proc IN (SELECT p FROM Doctor d "
			+ 				 	     "join d.procList pl "
			+ 				 	     "join pl.procedure p "
			+ 					   "WHERE d.id=?1) "
			+ "ORDER BY proc.name")
	public List<Procedure> findProceduresBelongToDoctorByName(Long id, String term);

//	
//	@Query("SELECT combos FROM Doctor d "
//		+ 			        "join d.comboList cl "
//		+ "WHERE d.id=?1")
//	public List<ComboByDoctor> findCombosByDoctorId(Long idD);
	
}
