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
@Table(name = "vip")
public class VIP implements Serializable {
	private static final long serialVersionUID = 4130308986379080362L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String description;

	private Boolean active;

	@Column(name = "early_date")
	private Boolean earlyDate;

	@Column(name = "items_free")
	private Boolean itemsFree;

	@Column(name = "lipo_areas_count")
	private Integer lipoAreasCount;

	@Column(name = "price_for_vip")
	private Double priceForVIP;

//  <<<<< IMPLEMENTATION >>>>>

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getEarlyDate() {
		return earlyDate;
	}

	public void setEarlyDate(Boolean earlyDate) {
		this.earlyDate = earlyDate;
	}

	public Boolean getItemsFree() {
		return itemsFree;
	}

	public void setItemsFree(Boolean itemsFree) {
		this.itemsFree = itemsFree;
	}

	public Integer getLipoAreasCount() {
		return lipoAreasCount;
	}

	public void setLipoAreasCount(Integer lipoAreasCount) {
		this.lipoAreasCount = lipoAreasCount;
	}

	public Double getPriceForVIP() {
		return priceForVIP;
	}

	public void setPriceForVIP(Double priceForVIP) {
		this.priceForVIP = priceForVIP;
	}

	@Override
	public String toString() 
	{
		String vip = "Price $" + priceForVIP + ". Contain: ";
		vip = earlyDate? vip + " (Early date)": vip ;
		vip = itemsFree? vip + " (Items free)": vip ;
		vip = lipoAreasCount>0? vip + " (" + lipoAreasCount + " Lipo areas)": vip ;
		
		return  vip;
	}

}
