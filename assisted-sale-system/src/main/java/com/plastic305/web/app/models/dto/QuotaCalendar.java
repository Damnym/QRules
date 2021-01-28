package com.plastic305.web.app.models.dto;

import java.io.Serializable;

public class QuotaCalendar implements Serializable { private static final long serialVersionUID = 6847497332244992632L;
	
	private String id;
	private String name;
	private String badge; // Insignia Opcional
	private String date;
	private String type; // Type of event (event, holiday, birthday)
	private String color; // Event custom color (optional)
	private Boolean everyYear;
	
	
	public QuotaCalendar(String id, String name, String badge, String date, String type, String color, Boolean everyYear)
	{
		super();
		this.id = id;
		this.name = name;
		this.badge = badge;
		this.date = date;
		this.type = type;
		this.color = color;
		this.everyYear = everyYear;
	}
	
	public QuotaCalendar() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBadge() {
		return badge;
	}
	public void setBadge(String badge) {
		this.badge = badge;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Boolean getEveryYear() {
		return everyYear;
	}
	public void setEveryYear(Boolean everyYear) {
		this.everyYear = everyYear;
	}

	
}
