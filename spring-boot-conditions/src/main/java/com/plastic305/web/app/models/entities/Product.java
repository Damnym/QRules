/**
 * 
 */
package com.plastic305.web.app.models.entities;

import java.io.Serializable;
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

/**
 * @author damny
 *
 */
@Entity
@Table(name = "products")
public class Product implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private double price;
	
	private boolean is_opcional;
	
	private String tips;  //Remark
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "id_product")
	private List<OfertByMount> ofertList;

	// IMPLEMENTATION
	//***************
	
	public Long getId() {
		return id;
	}


	public List<OfertByMount> getOfertList() {
		return ofertList;
	}


	public void setOfertList(List<OfertByMount> ofertList) {
		this.ofertList = ofertList;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public boolean isIs_opcional() {
		return is_opcional;
	}


	public void setIs_opcional(boolean is_opcional) {
		this.is_opcional = is_opcional;
	}


	public String getTips() {
		return tips;
	}


	public void setTips(String tips) {
		this.tips = tips;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -8784328357735351659L;
}
