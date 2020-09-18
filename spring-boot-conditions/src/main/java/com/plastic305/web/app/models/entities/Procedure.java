package com.plastic305.web.app.models.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "procedures")
public class Procedure implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String name;
	
	@Column(name = "required_cell_saver")
	private boolean requiredCellSaver;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2251354311478813324L;

	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
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
	
	public boolean isRequiredCellSaver() {
		return requiredCellSaver;
	}
	
	public void setRequiredCellSaver(boolean cellSaver) {
		this.requiredCellSaver = cellSaver;
	}

}
