package com.plastic305.web.app.models.entities;

public enum EnumPromoTo 
{  // Aplica a procedimiento o a item?
	PROCEDURE("Procedures"),
	ITEM("Items");

	private String applyTo; 
	
	EnumPromoTo(String msg) {
		applyTo = msg;
	}
	
	public String getApplyTo() {
		return applyTo;
	}
	
	
	
}
