package com.plastic305.web.app.services;

import java.util.List;

import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.Procedure;

public interface IComboService {
	
//    public Combo getComboByName(String pName);
	public void save(Combo combo);
	
	public List<Combo> findAll();
	public List<Combo> findAllWithOutAditional();
//	public List<Combo> findAllOrder();
	public Combo findOne(Long id);
	
	public List<Procedure> findP2ThatComboWithP1(Long idP1); 
	public List<Procedure> findP2ThatNotComboWithP1(Long idP1); 
	
//	List<ProductRecommendedByProcedure> findProductsRecommended(Long idP); 
	public void delete(Long id);
	
	
	
	public Boolean needAdd(Procedure p1, Procedure p2);
	public Boolean needRemove(Procedure p1C,  Procedure p2C, List<Procedure> chosseds, Procedure p1);

}
