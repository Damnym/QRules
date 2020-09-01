package com.plastic305.web.app.models.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.plastic305.web.app.models.entities.Suffering;

public interface ISufferingDAOPagNSortRepository extends PagingAndSortingRepository<Suffering, Long> {

}
