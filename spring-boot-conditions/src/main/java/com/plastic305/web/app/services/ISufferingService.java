package com.plastic305.web.app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.plastic305.web.app.models.entities.Suffering;

public interface ISufferingService {
	/**
	 * @param filter: to specify if all or some specific group of accepted or not are returned
	 */
	public List<String> getSufferingList(int filter);

	public Suffering getSufferingByName(String aName);
	public void save(Suffering nSuffering);
	
	public List<Suffering> findAll();
	public Page<Suffering> findAll(Pageable page);
	public Suffering findOne(Long id);
	
	public List<Suffering> findAllRefusedOrWarning();
	
	public int getDoctorCountsByConditionsId(Long id);
	
//	public List<String> findByDoctorID(Long idD, Long idS);
	
	public void delete(Long id);

}
