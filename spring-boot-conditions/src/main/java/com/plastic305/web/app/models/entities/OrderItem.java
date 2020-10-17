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
@Table(name = "order_items")
public class OrderItem implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private int amount ;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	private Double subTotal;
	
	private Boolean isOfert;
	
	
	//  <<<<< IMPLEMENTATION >>>>>
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal() {
		this.subTotal = this.amount * this.product.getPrice();   // ESTE getPrice() DE PRODUCTO TIENE Q ESTAR CON LA OFERTA YA...SINO CAMBIAR
	}

	public Boolean getIsOfert() {
		return isOfert;
	}

	public void setIsOfert(Boolean isOfert) {
		this.isOfert = isOfert;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4897593327975032543L;
}
