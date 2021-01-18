package com.plastic305.web.app.models.dao;


import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.SufferingByDoctor;

public interface IS_DDAO extends CrudRepository<SufferingByDoctor, Long> {
//	public List<SufferingByDoctor> findByDoctor_id(Long id); 

}
