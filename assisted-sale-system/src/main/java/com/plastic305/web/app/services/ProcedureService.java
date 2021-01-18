package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IComboDAO;
import com.plastic305.web.app.models.dao.IProcedureDAO;
import com.plastic305.web.app.models.dao.IProductsDAO;
import com.plastic305.web.app.models.dto.TipsChosenDTO;
import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;
import com.plastic305.web.app.models.entities.Tips;

@Service
public class ProcedureService implements IProcedureService {
    @Autowired IProcedureDAO pDAO;
    @Autowired IProductsDAO iDAO;
    @Autowired IComboDAO comboDAO;
	
	@Override
	@Transactional(readOnly = true)
	public Procedure getProcedureByName(String pName) {
		for (Procedure p: pDAO.findAll()) {
			if (p.getName().equals(pName))
				return p;
		}
		return null;
	}

	@Override
	@Transactional
	public void save(Procedure nProcedure) {
		pDAO.save(nProcedure);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Procedure> findAll() {
		return (List<Procedure>) pDAO.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Procedure findOne(Long id) {
		return pDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		pDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Procedure> findAllOrder() {
		return (List<Procedure>) pDAO.findAllByOrderByName();
	}

	@Override
	public List<ProductRecommendedByProcedure> findProductsRecommended(Long idP) {
		return iDAO.findProductsRecommendedByProcedure(idP);
	}

	@Override
	public List<Procedure> findProceduresByName(String term) {
		return pDAO.findProceduresByName(term);
	}

	@Override
	public List<Procedure> findAllPrincipal() {
		return pDAO.findPrincipalProcedures();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Procedure> findByTips(TipsChosenDTO tipschosen) 
	{
		HashSet<Procedure> procedureSetResult = new HashSet<>();
		HashSet<Procedure> procedureSetTemp = new HashSet<>();
		
		for (Tips t: tipschosen.getChosen())
		{
			procedureSetTemp.addAll(pDAO.findProcedureByTipsId(t.getId()));
			if (!procedureSetResult.isEmpty())
				procedureSetResult.retainAll(procedureSetTemp);
			else
				procedureSetResult.addAll(procedureSetTemp);
			procedureSetTemp.clear();
		}
		
		return new ArrayList<Procedure>(procedureSetResult);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Combo> findComboByTips(TipsChosenDTO tipschosen) 
	{
		HashSet<Procedure> procedureSetTemp = new HashSet<>();
		HashSet<Combo> comboSet = new HashSet<>();
		
		for (Tips t: tipschosen.getChosen())
			procedureSetTemp.addAll(pDAO.findPrincipalProcedureByTipsId(t.getId()));  // tengo todos los proc principales
		List<Combo> combos = comboDAO.findAllWithOutAditional();
		for (Combo combo : combos) 
			if (procedureSetTemp.contains(combo.getProcedureList().get(0).getProcedure()) && procedureSetTemp.contains(combo.getProcedureList().get(1).getProcedure()))
				comboSet.add(combo);
		    
		return new ArrayList<Combo>(comboSet);
	}
	

}
