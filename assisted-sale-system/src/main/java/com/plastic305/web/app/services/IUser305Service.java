package com.plastic305.web.app.services;

import java.util.List;

import com.plastic305.web.app.models.entities.User305;

public interface IUser305Service {
	
//GENERAL
//*******
	public List<User305> findAll();
	public User305 findOne(Long id);
	public Boolean exists(String username);

	public void save(User305 user);
	public void delete(Long id);
	
	
}
