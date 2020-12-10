package com.plastic305.web.app.models.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "sufferings_by_doctors")
public class SufferingByDoctor implements Serializable { private static final long serialVersionUID = -1132986380831755358L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "suffering_id")
	private Suffering suffering;


	//<<<IMPLEMENTATION>>>

	
	public SufferingByDoctor(Suffering suffering) {
		super();
		this.suffering = suffering;
	}
	
	
	public SufferingByDoctor() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Suffering getSuffering() {
		return suffering;
	}

	public void setSuffering(Suffering suffering) {
		this.suffering = suffering;
	}

}
