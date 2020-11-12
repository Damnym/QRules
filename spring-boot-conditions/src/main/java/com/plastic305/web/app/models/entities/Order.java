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
	
	private String observation;
	
	private String doctorName ;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateSurgery ;

	@ManyToOne(fetch = FetchType.LAZY)
	private Client client;
	
	private boolean financed;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	private List<OrderItem> itemList;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "order_id")
	private List<OrderProcedure> procedureList;
	
	//  <<<<< IMPLEMENTATION >>>>>
	
	public Order() {
		itemList  = new ArrayList<OrderItem>(); 
		procedureList  = new ArrayList<OrderProcedure>(); 
	}

	public boolean isFinanced() {
		return financed;
	}

	public void setFinanced(boolean financed) {
		this.financed = financed;
	}

	public Date getDateSurgery() {
		return dateSurgery;
	}

	public void setDateSurgery(Date dateSurgery) {
		this.dateSurgery = dateSurgery;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public List<OrderProcedure> getProcedureList() {
		return procedureList;
	}

	public void setProcedureList(List<OrderProcedure> procedureList) {
		this.procedureList = procedureList;
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
		for (int i = 0; i < procedureList.size(); i++) {
			totalOrder+=procedureList.get(i).getSubTotal();
		}
		return totalOrder;
	}
	
	public void addItem(OrderItem item) {
		itemList.add(item);
	}

	public void addProcedure(OrderProcedure procedure) {
		procedureList.add(procedure);
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
