package com.plastic305.web.app.models.entities;

public enum EnumPayMethods 
{  
	CASH("Cash"),
	FINANCED("Financed"),
	VIP("VIP");

	private String method; 
	
	EnumPayMethods(String msg) {
		method = msg;
	}
	
	public String getMethod() {
		return method;
	}
	
}
