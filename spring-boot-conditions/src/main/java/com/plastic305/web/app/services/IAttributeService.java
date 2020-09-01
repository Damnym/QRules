package com.plastic305.web.app.services;

import java.util.List;

import com.plastic305.web.app.models.entities.Attribute;

public interface IAttributeService {
	
	public  List<Attribute> getAttributeList();

	public  Attribute getAttributeByName(String aName);
	public void save(Attribute nAttribute);
    public void resetAttributes();
    public List<String> getRespuestas();
}
