package com.plastic305.web.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Client;

public interface IClientDAO extends CrudRepository<Client, Long> {
	
	@Query("SELECT c FROM Client c left join fetch c.orderList i WHERE c.id=?1")
	public Client fetchClientByIdWithOrder(Long id);

}
