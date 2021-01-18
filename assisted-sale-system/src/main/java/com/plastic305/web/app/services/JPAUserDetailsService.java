package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.User;

import com.plastic305.web.app.models.dao.IUserDAO305;
import com.plastic305.web.app.models.entities.Role;
import com.plastic305.web.app.models.entities.User305;

@Service("jpaUserDetailsService")
public class JPAUserDetailsService implements UserDetailsService
{
	@Autowired 
	private IUserDAO305 userDAO;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		User305 user305 = userDAO.findByUsername(username);
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Role role: user305.getRoleList())
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		
		return new User(username, user305.getPassword(), user305.getEnabled(), true, true, true, authorities);
	}
	
}
