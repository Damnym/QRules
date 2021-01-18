package com.plastic305.web.app.services;

import java.util.List;

import com.plastic305.web.app.models.entities.ProcByDoct;

public interface IProcByDoctService 
{
	public void save(ProcByDoct procByDoct);
	
	public List<ProcByDoct> findAll();
	public ProcByDoct findOne(Long id);
	public ProcByDoct findProcByDoctByProcIdNDoctId(Long idP, Long idD);
	
	public void delete(Long id);
}
