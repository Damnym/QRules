package com.plastic305.web.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IProcByDoctDAO;
import com.plastic305.web.app.models.entities.ProcByDoct;

@Service
public class ProcByDoctService implements IProcByDoctService {
	@Autowired private IProcByDoctDAO procByDoctDAO;

	@Override @Transactional
	public void save(ProcByDoct procByDoct) 
	{
		procByDoctDAO.save(procByDoct);
	}

	@Override @Transactional(readOnly = true)
	public List<ProcByDoct> findAll() 
	{
		return (List<ProcByDoct>)procByDoctDAO.findAll();
	}
 
	@Override @Transactional(readOnly = true)
	public ProcByDoct findOne(Long id) 
	{
		return procByDoctDAO.findById(id).orElse(null);
	}

	@Override @Transactional
	public void delete(Long id) 
	{
		procByDoctDAO.deleteById(id);
	}
	

}
