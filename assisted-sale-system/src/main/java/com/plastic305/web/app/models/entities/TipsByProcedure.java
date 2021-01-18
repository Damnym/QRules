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
@Table(name = "tips_by_procedures")
public class TipsByProcedure implements Serializable { private static final long serialVersionUID = -1132986380831755358L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tips_id")
	private Tips tips;


	//<<<IMPLEMENTATION>>>

	
	public TipsByProcedure(Tips tips) {
		super();
		this.tips = tips;
	}
	
	public TipsByProcedure() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Tips getTips() {
		return tips;
	}

	public void setTips(Tips tips) {
		this.tips = tips;
	}

}
