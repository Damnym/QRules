package com.plastic305.web.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IProductsDAO;
import com.plastic305.web.app.models.entities.Product;

@Service
public class ProductService implements IProductService {
	@Autowired private IProductsDAO productDAO;
	
	@Override
	@Transactional 
	public void save(Product nProduct) {
		productDAO.save(nProduct) ;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> findAll() {
		return (List<Product>)productDAO.findAllByOrderByName();
	}

	@Override
	@Transactional(readOnly = true)
	public Product findOne(Long id) {
		return productDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional 
	public void delete(Long id) {
		productDAO.deleteById(id);
	}

}
