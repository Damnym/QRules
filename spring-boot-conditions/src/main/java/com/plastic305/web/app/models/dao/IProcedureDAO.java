package com.plastic305.web.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Procedure;

public interface IProcedureDAO extends CrudRepository<Procedure, Long> {
	public List<Procedure> findAllByOrderByName();
	
	@Query("SELECT proc FROM Procedure proc WHERE (proc.name like %?1%)")
	public List<Procedure> findProceduresByName(String term);
	
	@Query("SELECT proc FROM Procedure proc WHERE (proc.aditional = false)")
	public List<Procedure> findPrincipalProcedures();

	
}
