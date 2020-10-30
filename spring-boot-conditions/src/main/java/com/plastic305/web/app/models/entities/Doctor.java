package com.plastic305.web.app.models.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "doctors")
public class Doctor implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String surname;
	
	@Column(name = "required_cell_saver")
	private boolean requiredCellSaver;
	
	@Column(name = "use_bmi")
	private boolean useBMI;
	
	@Column(name = "min_weight")
	private Double minWeight;
	
	@Column(name = "max_weight")
	private Double maxWeight;
	
	@Column(name = "min_bmi")
	private Double minBMI;
	
	@Column(name = "max_bmi")
	private Double maxBMI;
	
	
	
	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "doctor_id")
	private List<SufferingByDoctor> sufferingsList;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "doctor_id")
	private List<ComboByDoctor> comboList;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "doctor_id")
	private List<ProcByDoct> procList;
	
	//  <<<<< IMPLEMENTATION >>>>>
	
	
	public Doctor() {
		sufferingsList = new ArrayList<SufferingByDoctor>(); 
//		comboList = new ArrayList<ComboByDoctor>(); 
//		procList = new ArrayList<ProcByDoct>(); 
	}

	public Double getMinWeight() {
		return minWeight;
	}

	public void setMinWeight(Double minWeight) {
		this.minWeight = minWeight;
	}

	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Double getMinBMI() {
		return minBMI;
	}

	public void setMinBMI(Double minBMI) {
		this.minBMI = minBMI;
	}

	public Double getMaxBMI() {
		return maxBMI;
	}

	public void setMaxBMI(Double maxBMI) {
		this.maxBMI = maxBMI;
	}

	public boolean isUseBMI() {
		return useBMI;
	}

	public void setUseBMI(boolean useBMI) {
		this.useBMI = useBMI;
	}

	public String getShortDate() {
		Calendar c = new GregorianCalendar(); 
		c.setTime(date);

		String month = Integer.toString(c.get(Calendar.MONTH)+1);
		String year = Integer.toString(c.get(Calendar.YEAR)); ;
		
		return " (" + year + "/" + month + ")";
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
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
	 * @return the sufferingsList
	 */
	public List<SufferingByDoctor> getSufferingsList() {
		return sufferingsList;
	}
	/**
	 * @param sufferingsList the sufferingsList to set
	 */
	public void setSufferingsList(List<SufferingByDoctor> sufferingsList) {
		this.sufferingsList = sufferingsList;
	}
	
	public void addSuffering(Suffering s ) {
		SufferingByDoctor sByD = new SufferingByDoctor();
		sByD.setSuffering(s);
		sufferingsList.add(sByD);
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
	 * @return the comboList
	 */
	public List<ComboByDoctor> getComboList() {
		return comboList;
	}
	/**
	 * @param comboList the comboList to set
	 */
	public void setComboList(List<ComboByDoctor> comboList) {
		this.comboList = comboList;
	}
	/**
	 * @return the procList
	 */
	public List<ProcByDoct> getProcList() {
		return procList;
	}
	/**
	 * @param procList the procList to set
	 */
	public void setProcList(List<ProcByDoct> procList) {
		this.procList = procList;
	}



	public boolean isRequiredCellSaver() {
		return requiredCellSaver;
	}
	public void setRequiredCellSaver(boolean requiredCellSaver) {
		this.requiredCellSaver = requiredCellSaver;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 6439305829659849320L;

}
