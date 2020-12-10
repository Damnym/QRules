package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IComboDAO;
import com.plastic305.web.app.models.dao.IProcedureDAO;
import com.plastic305.web.app.models.dao.IProductsDAO;
import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;

@Service
public class ComboService implements IComboService {
    @Autowired IComboDAO comboDAO;
    @Autowired IProcedureDAO procedureDAO;
	
	@Override
	@Transactional
	public void save(Combo combo) {
		comboDAO.save(combo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Combo> findAll() {
		return (List<Combo>) comboDAO.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Combo findOne(Long id) {
		return comboDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		comboDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Combo> findAllWithOutAditional() 
	{
		List<Combo> result = new ArrayList<Combo>();
		
		for (Combo combo: this.findAll()) {
			int i = 0 ;
			while (i<combo.getProcedureList().size() && !combo.getProcedureList().get(i).getProcedure().getAditional())
				i++;
			if (i==combo.getProcedureList().size())
				result.add(combo);
		}
		
		return result;
	}

	@Override @Transactional(readOnly = true)
	public List<Procedure> findP2ThatComboWithP1(Long idP1) 
	{
		List<Procedure> procedures = new ArrayList<Procedure>();
		
		for (Combo combo : comboDAO.findAllWithOutAditional()) 
		{
			if (combo.getProcedureList().get(0).getProcedure().getId() == idP1)
				procedures.add(combo.getProcedureList().get(1).getProcedure());
			if (combo.getProcedureList().get(1).getProcedure().getId() == idP1)
				procedures.add(combo.getProcedureList().get(0).getProcedure());
		}
		for (Procedure procedure : procedureDAO.findPrincipalProcedures()) // ideal con conjuntos
			procedures.remove(procedure);
		
		return procedures;
	}

	@Override @Transactional(readOnly = true)
	public List<Procedure> findP2ThatNotComboWithP1(Long idP1) 
	{
		List<Procedure> procedures = procedureDAO.findPrincipalProcedures();
		
		for (Combo combo : comboDAO.findAllWithOutAditional()) 
		{
			if (combo.getProcedureList().get(0).getProcedure().getId() == idP1)
				procedures.remove(combo.getProcedureList().get(1).getProcedure());
			if (combo.getProcedureList().get(1).getProcedure().getId() == idP1)
				procedures.remove(combo.getProcedureList().get(0).getProcedure());
		}
		procedures.remove(procedureDAO.findById(idP1).orElse(null));
		
		return procedures;
	}

	@Override
	public Boolean needAdd(Procedure p1, Procedure p2) {
		List<Combo> combos = comboDAO.findAllWithOutAditional();
		for (Combo combo : combos) 
			if ( (combo.getProcedureList().get(0).getProcedure().getId() == p1.getId() || combo.getProcedureList().get(1).getProcedure().getId() == p1.getId()) &&
			    combo.getProcedureList().get(0).getProcedure().getId() == p2.getId() || combo.getProcedureList().get(1).getProcedure().getId() == p2.getId() )
				return false ;
		return true;
	}

	@Override
	public Boolean needRemove(Procedure p1C,  Procedure p2C, List<Procedure> chosseds, Procedure p1)
	{ // de los combos q existen
		for (Procedure proc: chosseds) 
				if ( (proc.getId() == p1C.getId() || proc.getId() == p2C.getId()) &&
					  p1.getId() == p1C.getId() || p1.getId() == p2C.getId() )
					return false ;
		return true;
	}

//	@Override
//	@Transactional(readOnly = true)
//	public List<Procedure> findAllOrder() {
//		return (List<Procedure>) pDAO.findAllByOrderByName();
//	}

//	@Override
//	public List<ProductRecommendedByProcedure> findProductsRecommended(Long idP) {
//		return iDAO.findProductsRecommendedByProcedure(idP);
//	}
	
	

}
