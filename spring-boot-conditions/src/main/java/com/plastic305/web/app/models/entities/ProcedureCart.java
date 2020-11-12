package com.plastic305.web.app.models.entities;

public class ProcedureCart {
	private String name;
	private Double priceCash;
	private Double priceFinanced;
	
	
	public ProcedureCart(String name, Double priceCash, Double priceFinanced) {
		super();
		this.name = name;
		this.priceCash = priceCash;
		this.priceFinanced = priceFinanced;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPriceCash() {
		return priceCash;
	}
	public void setPriceCash(Double priceCash) {
		this.priceCash = priceCash;
	}
	public Double getPriceFinanced() {
		return priceFinanced;
	}
	public void setPriceFinanced(Double priceFinanced) {
		this.priceFinanced = priceFinanced;
	}
	
	

}
