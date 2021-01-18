package com.plastic305.web.app.models.entities;

public class ProcedureCart {
	private Long pId;
	private String name;
	private Double priceCash;
	private Double priceFinanced;
	private Double priceVIP;
	private Integer promoCount;
	
	
	public ProcedureCart(String name, Double priceCash, Double priceFinanced, Double priceVIP, Long pId, Integer promoCount) 
	{
		super();
		this.name = name;
		this.priceCash = priceCash;
		this.priceFinanced = priceFinanced;
		this.priceVIP = priceVIP;
		this.promoCount = promoCount ;
		this.pId = pId ;
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
	public Double getPriceVIP() {
		return priceVIP;
	}
	public void setPriceVIP(Double priceVIP) {
		this.priceVIP = priceVIP;
	}


	public Integer getPromoCount() {
		return promoCount;
	}


	public void setPromoCount(Integer promoCount) {
		this.promoCount = promoCount;
	}


	public Long getpId() {
		return pId;
	}


	public void setpId(Long pId) {
		this.pId = pId;
	}
	
	

}
