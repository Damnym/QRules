package com.plastic305.web.app.models.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "conditions_by_clients")
public class ConditionByClient implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "suffering_id")
	private Suffering suffering;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the suffering
	 */
	public Suffering getSuffering() {
		return suffering;
	}

	/**
	 * @param suffering the suffering to set
	 */
	public void setSuffering(Suffering suffering) {
		this.suffering = suffering;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = -746622531018349040L;

}
