package com.plastic305.web.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.models.entities.Procedure;

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
	public List<Procedure> fetchProceduresByDoctorId(Long id);

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
	
	
	@Query("SELECT p FROM Doctor d "
			+ "join d.sufferingsList sl "
			+ "join sl.suffering s "
			+ "join d.procList pl "
			+ "join pl.procedure p "
			+ "WHERE s.id=?1")
	public List<Procedure> fetchProceduresByAllDoctorBySufferingId(Long id);

}
