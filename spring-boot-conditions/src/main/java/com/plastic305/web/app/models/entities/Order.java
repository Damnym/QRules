package com.plastic305.web.app.models.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "orders")
public class Order implements Serializable { 	// VALORAR QUE TENGA UNA LISTA DE PROCEDIMIENTOS COMO PARTE DE LA ORDEN O LOS DOS Q SON
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@NotEmpty
	private String observation;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Client client;
	
//	private Double totalOrder;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	private List<OrderItem> itemList;
	
	//  <<<<< IMPLEMENTATION >>>>>

	public Order() {
		itemList  = new ArrayList<OrderItem>(); 
	}
	
	@PrePersist
	public void dateGenerate() {
		this.date = new Date();
	}
	
	public Double getTotalOrder() {
		Double totalOrder = 0.0 ;
		for (int i = 0; i < itemList.size(); i++) {
			totalOrder+=itemList.get(i).getSubTotal();
		}
		return totalOrder;
	}
	
	public void addItem(OrderItem item) {
		itemList.add(item);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<OrderItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrderItem> itemList) {
		this.itemList = itemList;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3804446708679469551L;

}
