package com.plastic305.web.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.plastic305.web.app.models.entities.Product;

public interface IProductsDAO extends PagingAndSortingRepository<Product, Long> {
	
	@Query("select p from Product p where p.name like %?1%")
	public List<Product> findByName(String term);
	
	public List<Product> findByNameLikeIgnoreCase(String term);
}
