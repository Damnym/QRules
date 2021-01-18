package com.plastic305.web.app.services;

import java.util.List;

import com.plastic305.web.app.models.entities.Tips;

public interface ITipsService 
{
	public void save(Tips tips);
	
	public List<Tips> findAll();
	public Tips findOne(Long id);
	
	public void delete(Long id);
}
