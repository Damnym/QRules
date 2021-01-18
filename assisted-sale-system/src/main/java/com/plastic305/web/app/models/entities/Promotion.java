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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "promotions")
@Inheritance(strategy=InheritanceType.JOINED)
public class Promotion implements Serializable 
{ private static final long serialVersionUID = -5957353347768735253L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private EnumPromotionType type ;    //0-Nuevo precio a procedimientos, 1-Descuento precio, 2-Descuento %

	@NotNull
	private EnumPromoTo promoProcOrItems ;    //0-Procedure, 1-Items
	
	@NotNull
	private Double discount ;    

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "promotion_id")
	private List<ItemsAmountForVIP> freeItemsList;

	
	//  <<<<< IMPLEMENTATION >>>>>
	
	
	public Promotion() {
		super();
		freeItemsList = new ArrayList<ItemsAmountForVIP>();
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setType(EnumPromotionType type) {
		this.type = type;
	}

	public EnumPromotionType getType() {
		return type;
	}

	public EnumPromoTo getPromoProcOrItems() {
		return promoProcOrItems;
	}

	public void setPromoProcOrItems(EnumPromoTo promoProcOrItems) {
		this.promoProcOrItems = promoProcOrItems;
	}

	public List<ItemsAmountForVIP> getFreeItemsList() {
		return freeItemsList;
	}

	public void setFreeItemsList(List<ItemsAmountForVIP> freeItemsList) {
		this.freeItemsList = freeItemsList;
	}

	
}
