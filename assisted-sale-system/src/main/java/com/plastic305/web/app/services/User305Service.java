package com.plastic305.web.app.services;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IUserDAO305;
import com.plastic305.web.app.models.entities.User305;

@Service
public class User305Service implements IUser305Service {
	@Autowired private IUserDAO305 user305DAO;
	@Autowired private BCryptPasswordEncoder passwordEncoder ;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	 /*<<<<< IMPLEMENTATION >>>>>*/

	@Override @Transactional(readOnly = true)
	public List<User305> findAll() 
	{
		return ( List<User305>) user305DAO.findAll();
	}

	@Override @Transactional(readOnly = true)
	public User305 findOne(Long id) 
	{
		return user305DAO.findById(id).orElse(null);
	}

	@Override @Transactional
	public void save(User305 user) 
	{	
		if (user.getAdmin())
		{	
			user.addAdminRole();
			user.addPowerRole();
		}
		else if (user.getPower())
				user.addPowerRole();
		
		user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
		user305DAO.save(user);
	}

	@Override @Transactional
	public void delete(Long id) 
	{
		user305DAO.deleteById(id);		
	}

	@Override
	public Boolean exists(String username) {
		return user305DAO.findByUsername(username) != null;
	}
	@Override
	public User305 findByUsername(String username) {
		return user305DAO.findByUsername(username);
	}
	

}
