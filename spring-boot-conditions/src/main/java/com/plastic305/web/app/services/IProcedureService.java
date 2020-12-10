package com.plastic305.web.app.services;

import java.util.List;

import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;

public interface IProcedureService {
	
    public Procedure getProcedureByName(String pName);
	public void save(Procedure nProcedure);
	
	public List<Procedure> findAll();
	public List<Procedure> findAllPrincipal();
	public List<Procedure> findAllOrder();
	public List<Procedure> findProceduresByName(String term);
	public Procedure findOne(Long id);
	
	List<ProductRecommendedByProcedure> findProductsRecommended(Long idP); 
	
	public void delete(Long id);

}
