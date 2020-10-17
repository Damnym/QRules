package com.plastic305.web.app.models.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.plastic305.web.app.models.entities.Suffering;

public interface ISufferingDAOPagNSortRepository extends PagingAndSortingRepository<Suffering, Long> {
	
	@Query("SELECT s from Suffering s where (s.name=?1)")
	public Suffering findByName(String term);

}
