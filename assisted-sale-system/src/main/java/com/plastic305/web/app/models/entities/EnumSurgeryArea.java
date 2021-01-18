package com.plastic305.web.app.models.entities;

public enum EnumSurgeryArea 
{  // 0-FACE, 1-BREAST, 2-LIPO
	FACE("Face area"),
	BREAST("Breast area"),
	LIPO("Lipos");

	private String description; 
	
	EnumSurgeryArea(String desc) {
		description = desc;
	}
	
	public String getDescription() {
		return description;
	}
	
}
