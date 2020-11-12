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
@Table(name = "combos_by_doctors")
public class ComboByDoctor implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "combo_id")
	private Combo combo;
	
	
	
	

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
	 * @return the combo
	 */
	public Combo getCombo() {
		return combo;
	}
	/**
	 * @param combo the combo to set
	 */
	public void setCombo(Combo combo) {
		this.combo = combo;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1132986380831755358L;

}
