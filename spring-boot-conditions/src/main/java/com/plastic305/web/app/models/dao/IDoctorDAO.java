package com.plastic305.web.app.models.dao;


import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Doctor;

public interface IDoctorDAO extends CrudRepository<Doctor, Long> {
	//	public List<Suffering> findSufferingsListById(Long id);
	

}
