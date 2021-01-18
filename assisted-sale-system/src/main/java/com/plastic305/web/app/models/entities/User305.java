package com.plastic305.web.app.models.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User305 implements Serializable
{ 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(unique = true)
	private String username;

	@NotEmpty
//	@Column(length = 60)
	private String password;
	
	@NotNull
	private Boolean enabled;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String lastname;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "user_id")
	private List<Role> roleList;
	
	@Transient
	private Boolean admin;
	
	@Transient
	private Boolean power;
	
	//  <<<<< IMPLEMENTATION >>>>>

	public User305() {
		roleList = new ArrayList<Role>();
		
		Role role = new Role() ;
		role.setAuthority("ROLE_USER");
		roleList.add(role);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	private static final long serialVersionUID = 1L;

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	
	public void addAdminRole()
	{
		Role role = new Role() ;
		role.setAuthority("ROLE_ADMIN");
		roleList.add(role);
	}
	
	public Boolean isAdmin() 
	{
		for (Role role : roleList) 
			if (role.getAuthority().equals("ROLE_ADMIN"))
				return true;
		return false;
	}

	public Boolean getPower() {
		return power;
	}

	public void setPower(Boolean power) {
		this.power = power;
	}
	
	public void addPowerRole()
	{
		Role role = new Role() ;
		role.setAuthority("ROLE_POWER");
		roleList.add(role);
	}
	
	public Boolean isPower() 
	{
		for (Role role : roleList) 
			if (role.getAuthority().equals("ROLE_POWER"))
				return true;
		return false;
	}

}
