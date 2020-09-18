package com.plastic305.web.app.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Product;

public interface IProductsDAO extends CrudRepository<Product, Long> {
	
	 public List<Product> findAllByOrderByName();

}
