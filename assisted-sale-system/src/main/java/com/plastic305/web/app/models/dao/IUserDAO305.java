package com.plastic305.web.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.User305;

public interface IUserDAO305 extends CrudRepository<User305, Long> 
{
	public User305 findByUsername(String username);
	
	
}
