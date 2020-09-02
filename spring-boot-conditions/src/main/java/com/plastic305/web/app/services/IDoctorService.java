package com.plastic305.web.app.services;

import java.util.List;


import com.plastic305.web.app.models.entities.Doctor;
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
	public void save(Doctor nDoctor);
	public void delete(Long id);
	
	public List<Procedure> findAllProcedurebyDoctorId(Long idD) ;
	public List<Procedure> findAllProcedureOfAllDoctorsByCondition(Long idC) ;
	public List<Procedure> findAllProcedurebyDoctorIdbyFirstProcedure(Long idD, Long idP) ;// Para Combo
	
	//public List<String> getSufferingListbyDoctor(Long id);

}
