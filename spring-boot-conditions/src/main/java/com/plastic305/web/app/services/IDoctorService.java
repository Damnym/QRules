package com.plastic305.web.app.services;

import java.util.List;

import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.models.entities.ProcByDoct;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Suffering;

public interface IDoctorService {
	/**
	 * @param filter: to specify if all or some specific group of accepted or not are returned
	 */
	public Doctor findOne(Long id);
	public List<Doctor> findAll();
	public List<Doctor> findAllbyCondition(Long idC);
	public List<Doctor> findAllbyConditions(List<Suffering> conditions);
	public List<Doctor> findAllbyProcedure(Long idP);
	public List<Doctor> findAllByConditionByProcedure(Long idC, Long idP1); // Para Combo
	public List<Doctor> findAllByConditionsByProcedure(List<Suffering> conditions, Long idP1);
	
	public List<Doctor> fetchDoctorsBySufferingId(Long id);
	
	public void save(Doctor nDoctor);
	public void delete(Long id);
	public void unlinkProcedure(Long id);
	public void unlinkCombo(Long id);
	public void unlinkCondition(Long id);
	
	                       
	public List<Procedure> findAllPrincipalProceduresbyDoctorId(Long idD) ;   // new por los de los adicionales
	public List<Procedure> findAllAditionalProceduresbyDoctorId(Long idD) ;   // new por los de los adicionales
	public List<Procedure> findAllProcedurebyDoctorId(Long idD) ;
	public List<Procedure> findProceduresNotBelongToDoctor(Long idD) ;
	public List<Procedure> findProceduresNotBelongToDoctorByName(Long id, String term);
	public List<Suffering> findConditionNotBelongToDoctorByName(Long id, String term);
	public List<Suffering> findConditionNotBelongToDoctor(Long id);
	public List<Procedure> findAllProcedureOfAllDoctorsByCondition(Long idC) ;
	public List<Procedure> findAllProcedureOfAllDoctorsByConditions(List<Suffering> conditions) ;
	public List<Procedure> findAllPrincipalProcedurebyDoctorIdbyFirstProcedure(Long idD, Long idP) ;// Para Combo // new por los de los adicionales
	public List<Procedure> findAllProcedurebyDoctorIdbyFirstProcedure(Long idD, Long idP) ;// Para Combo
	public ProcByDoct getProcByDoct(Long idD, Long idP) ;
	public List<String> findAllProcedureNamebyDoctorIdbyFirstProcedure(Long idD, Long idP) ;// Para Combo
	
//	Creado para Admin Combo
	public List<Combo> findUnlinkedCombo(Long idD);
	public List<Combo> findUnlinkedComboWithP1(Long idD, Long idP);
	public List<Combo> findLinkedCombo(Long idD); 
	public List<Procedure> findAllP1OfUnlinkedCombos(Long idD);
	public List<Procedure> findProceduresBelongToDoctorByName(Long id, String term);
	public Long findComboByProcedures(Long idD, Long idP1, Long idP2);
	
	public Double getProcedurePrice(Long idD, Long idP, boolean isFinanced);
	
	//NEW  por los de los adicionales
	List<Procedure> findAllAditionalProcedurebyDoctorIdbyCombo(Long idD, Long idP1, Long idP2);

}
