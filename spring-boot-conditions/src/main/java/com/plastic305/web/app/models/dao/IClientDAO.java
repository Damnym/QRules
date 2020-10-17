package com.plastic305.web.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.plastic305.web.app.models.entities.Client;

public interface IClientDAO extends PagingAndSortingRepository<Client, Long> {
	
	@Query("SELECT c FROM Client c left join fetch c.orderList i WHERE c.id=?1")
	public Client fetchClientByIdWithOrder(Long id);
	
	
	@Query("SELECT c from Client c where c.name like %?1%")
	public List<Client> findByNameRegExp(String term);
	

}
