package com.plastic305.web.app.models.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.ProcByDoct;

public interface IProcByDoctDAO extends CrudRepository<ProcByDoct, Long> 
{
	
	@Query("SELECT pbyd FROM Doctor d "
			+ "join d.procList pbyd "
			+ "join pbyd.procedure p "
			+ "WHERE p.id=?1 AND d.id=?2")
	public ProcByDoct findProcByDoctByProcIdNDoctId(Long idP, Long idD);

	

}
