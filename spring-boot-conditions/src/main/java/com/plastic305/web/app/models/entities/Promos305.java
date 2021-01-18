package com.plastic305.web.app.models.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
@Table(name = "promos_305")
public class Promos305 implements Serializable 
{ private static final long serialVersionUID = 224392491007560924L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String description;
	
	@Column(name = "min_valid")
	@Temporal(TemporalType.DATE)       // Min fecha validez
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date minDateValid;
	
	@Column(name = "max_valid")
	@Temporal(TemporalType.DATE)       // Max fecha validez
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date maxDateValid;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "promos305_id")
	private List<Promotion> promotionsList;
	
	
//  <<<<< IMPLEMENTATION >>>>>

	public Promos305() 
	{
		super();
		this.minDateValid = new Date();
		promotionsList = new ArrayList<Promotion>(); 
				
	}
	
	public Date getMinDateValid() {
		return minDateValid;
	}

	public void setMinDateValid(Date minDateValid) {
		this.minDateValid = minDateValid;
	}

	public Date getMaxDateValid() {
		return maxDateValid;
	}

	public void setMaxDateValid(Date maxDateValid) {
		this.maxDateValid = maxDateValid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Promotion> getPromotionsList() {
		return promotionsList;
	}

	public void setPromotionsList(List<Promotion> promotionsList) {
		this.promotionsList = promotionsList;
	}
	
}
