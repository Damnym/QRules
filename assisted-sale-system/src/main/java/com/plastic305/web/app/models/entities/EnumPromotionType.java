package com.plastic305.web.app.models.entities;

public enum EnumPromotionType 
{  // 0-Nuevo precio a procedimientos, 1-Descuento precio, 2-Descuento %
	NEW_PROCEDURE_PRICE("Procedure price"),
	PRICE_DISCOUNT("Price discount"),
	PERCENT_DISCOUNT("% discount");

	private String description; 
	
	EnumPromotionType(String desc) {
		description = desc;
	}
	
	public String getDescription() {
		return description;
	}
	
}
