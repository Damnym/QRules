package com.plastic305.web.app.models.dto;

import java.io.Serializable;


public class ProductRecommendedByProcedureDTO implements Serializable { private static final long serialVersionUID = 6847497332244992632L;

	private Long productId;
	private String productName;
	private Long amountRecommended ;
	
	
	public ProductRecommendedByProcedureDTO(Long productId, String productName, Long amountRecommended) 
	{
		super();
		this.productId = productId;
		this.productName = productName;
		this.amountRecommended = amountRecommended;
	}
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getAmountRecommended() {
		return amountRecommended;
	}
	public void setAmountRecommended(Long amountRecommended) {
		this.amountRecommended = amountRecommended;
	}
	
	
	
	
}
