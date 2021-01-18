package com.plastic305.web.app.models.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.plastic305.web.app.models.entities.Tips;


public class TipsChosenDTO implements Serializable { private static final long serialVersionUID = 6847497332244992632L;

	private List<Tips> chosen;
	
	
	public TipsChosenDTO() 
	{
		super();
		this.chosen = new ArrayList<Tips>();
	}


	public List<Tips> getChosen() {
		return chosen;
	}


	public void setChosen(List<Tips> chosen) {
		this.chosen = chosen;
	}
	
}
