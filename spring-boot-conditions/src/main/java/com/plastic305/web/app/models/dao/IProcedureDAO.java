package com.plastic305.web.app.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Procedure;

public interface IProcedureDAO extends CrudRepository<Procedure, Long> {
	public List<Procedure> findAllByOrderByName();
}
