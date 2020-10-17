package com.plastic305.web.app.models.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "sufferings")
public class Suffering implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2251354311478813324L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String name;
	
//	@NotEmpty
	private int accepted;
	
	private String warning;
	
	
	//  <<<<< IMPLEMENTATION >>>>>	
	
	
	/**
	 * @return the warning
	 */
	public String getWarning() {
		return warning;
	}
	/**
	 * @param warning the warning to set
	 */
	public void setWarning(String warning) {
		this.warning = warning;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the accepted
	 */
	public int getAccepted() {
		return accepted;
	}
	/**
	 * @param accepted the accepted to set
	 */
	public void setAccepted(int accepted) {
		this.accepted = accepted;
	}

}
