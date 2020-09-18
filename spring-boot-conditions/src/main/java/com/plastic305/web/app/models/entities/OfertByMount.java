package com.plastic305.web.app.models.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "mount_oferts")
public class OfertByMount implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private int mount;
	
	@NotEmpty
	private double total_price; 

	
	// IMPLEMENTATION
	//***************

	/**
	 * 
	 */
	private static final long serialVersionUID = 5055848989999982548L;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public int getMount() {
		return mount;
	}


	public void setMount(int mount) {
		this.mount = mount;
	}


	public double getTotal_price() {
		return total_price;
	}


	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

}
