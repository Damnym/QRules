package com.plastic305.web.app.services;

import java.util.List;

import com.plastic305.web.app.models.entities.VIP;

public interface IVIPService 
{
	public void save(VIP vip);
	
	public List<VIP> findAll();
	public VIP findOne(Long id);
	
	public void delete(Long id);
}
