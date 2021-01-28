package com.plastic305.web.app.models.entities;

public enum EnumPayMethods 
{  
	CASH("Cash"),
	FINANCED("Financed"),
	SPECIAL("Special");

	private String method; 
	
	EnumPayMethods(String msg) {
		method = msg;
	}
	
	public String getMethod() {
		return method;
	}
	
}
