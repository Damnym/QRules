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
@Table(name = "super_orders")
public class SuperOrder implements Serializable 
{ private static final long serialVersionUID = -3804446708679469551L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String observation;
	
	private SuperOrderStatus status;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Client client;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "super_order_id")
	private List<Order> orderList;

	//  <<<<< IMPLEMENTATION >>>>>
	
	public SuperOrder() {
		orderList = new ArrayList<Order>();
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

	@PrePersist
	public void dateGenerate() {
		this.date = new Date();
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Order> orderList) {
		this.orderList = orderList;
	}

	public Double getTotalOrder() 
	{
		Double totalSuperOrder = 0.0 ;
		for (Order order: orderList) 
			totalSuperOrder += order.getTotalOrderWithDiscount();
		
		return totalSuperOrder;
	}

	public Double getTotalSOrder() 
	{
		Double totalSuperOrder = 0.0 ;
		for (Order order: orderList) 
			totalSuperOrder += order.getTotalOrderWithDiscount();
		
		return totalSuperOrder;
	}
	
	public Order getLastOrder()
	{
		return orderList.get(orderList.size()-1);
	}

	public SuperOrderStatus getStatus() {
		return status;
	}

	public void setStatus(SuperOrderStatus status) {
		this.status = status;
	}
	
	public Integer getSuperOrderRecoveryTime() {
		Integer result = 0 ;
		for (Order o : orderList)
			result = (o.getOrderRecoveryTime() > result)? o.getOrderRecoveryTime(): result ;

		return result ;
	}

}
