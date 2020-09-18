package com.plastic305.web.app.models.entities;

public class ProdSubTotal {
	private Long id;
	private Product product;
	private int amount ;
	private Double subTotal;
	
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	
	public ProdSubTotal(Long id, Product product, int amount, Double subTotal) {
		super();
		this.id=id;
		this.product = product;
		this.amount = amount;
		this.subTotal = subTotal;
	}
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
	
	

}
