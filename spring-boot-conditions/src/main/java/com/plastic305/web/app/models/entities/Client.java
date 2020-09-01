package com.plastic305.web.app.models.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "clients")
public class Client implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String surname;
	
	private Long suffering, combo, p1, p2, doctor, accepted;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "client_id")
	private List<Suffering> conditionsList;
	
	/**
	 * @return the accepted
	 */
	public Long getAccepted() {
		return accepted;
	}

	/**
	 * @param accepted the accepted to set
	 */
	public void setAccepted(Long accepted) {
		this.accepted = accepted;
	}

	/**
	 * @return the p1
	 */
	public Long getP1() {
		return p1;
	}

	/**
	 * @param p1 the p1 to set
	 */
	public void setP1(Long p1) {
		this.p1 = p1;
	}
	/**
	 * @return the p2
	 */
	public Long getP2() {
		return p2;
	}
	/**
	 * @param p2 the p2 to set
	 */
	public void setP2(Long p2) {
		this.p2 = p2;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * @return the suffering
	 */
	public Long getSuffering() {
		return suffering;
	}
	/**
	 * @param suffering the suffering to set
	 */
	public void setSuffering(Long suffering) {
		this.suffering = suffering;
	}
	/**
	 * @return the combo
	 */
	public Long getCombo() {
		return combo;
	}
	/**
	 * @param combo the combo to set
	 */
	public void setCombo(Long combo) {
		this.combo = combo;
	}
	/**
	 * @return the doctor
	 */
	public Long getDoctor() {
		return doctor;
	}
	/**
	 * @param doctor the doctor to set
	 */
	public void setDoctor(Long doctor) {
		this.doctor = doctor;
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
	public void setId(Long id) {
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
	/**
	 * @return the surName
	 */
	public String getSurName() {
		return surname;
	}
	/**
	 * @param surName the surName to set
	 */
	public void setSurName(String surName) {
		this.surname = surName;
	}
	
	/**
	 * @return the conditionsList
	 */
	public List<Suffering> getConditionsList() {
		return conditionsList;
	}

	/**
	 * @param conditionsList the conditionsList to set
	 */
	public void setConditionsList(List<Suffering> conditionsList) {
		this.conditionsList = conditionsList;
	}

	public Client() {
		conditionsList = new ArrayList<Suffering>(); 
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 6439305829659849320L;

}
