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
@Table(name = "products_by_docts_and_procs")
public class ProductByDoctAndProc implements Serializable { private static final long serialVersionUID = 2548212282710887828L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	private Boolean isMandatory;
	private Boolean isIncluded;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	//  <<<<< IMPLEMENTATION >>>>>		
	
	public Boolean getIsIncluded() {
		return isIncluded;
	}

	public void setIsIncluded(Boolean isIncluded) {
		this.isIncluded = isIncluded;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public Boolean getIsMandatory() {
//		return isMandatory;
//	}
//
//	public void setIsMandatory(Boolean isMandatory) {
//		this.isMandatory = isMandatory;
//	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


}
