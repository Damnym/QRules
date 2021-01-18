package com.plastic305.web.app.models.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.Procedure;

public interface IComboDAO extends CrudRepository<Combo, Long> 
{
	@Query("SELECT combo FROM Combo combo "
		+ 	  "join combo.procedureList plist "
		+ 	  "join plist.procedure proc "
		+  "WHERE proc.aditional = false")
	public List<Combo> findAllWithOutAditional();


	
}
