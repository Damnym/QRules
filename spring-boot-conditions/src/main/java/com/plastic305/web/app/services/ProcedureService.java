package com.plastic305.web.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IProcedureDAO;
import com.plastic305.web.app.models.entities.Procedure;

@Service
public class ProcedureService implements IProcedureService {
    @Autowired
    IProcedureDAO pDAO;
	
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

}
