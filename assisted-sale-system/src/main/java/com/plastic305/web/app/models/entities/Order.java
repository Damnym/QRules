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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "orders")
																				
public class Order implements Serializable 
{ private static final long serialVersionUID = -3804446708679469551L; 	// VALORAR QUE TENGA UNA LISTA DE PROCEDIMIENTOS COMO PARTE DE LA ORDEN O LOS DOS Q SON
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String observation;
	
	private String doctorName ;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateSurgery ;

//	@ManyToOne(fetch = FetchType.LAZY)
//	private Client client;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private SuperOrder superOrder;

//	private boolean financed;
	
	private EnumPayMethods payMethod;
	
	private Double discount ;
	
	@OneToOne
	@JoinColumn(name = "vip_id")
	private VIP vip;
	
	@Transient
	private EnumPromoTo promoTo ;  // si se hace en el procedureORder quitar de aqui
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
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

	public boolean isFinanced() 
	{
		return payMethod == EnumPayMethods.FINANCED ;
	}

//	public void setFinanced(boolean financed) {
//		this.financed = financed;
//	}

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

	public Double getTotalOrder() {
		Double totalOrder = 0.0 ;
		for (int i = 0; i < itemList.size(); i++) 
		{
			if (itemList.get(i)!= null)
				totalOrder+=itemList.get(i).getSubTotal();
		}
		for (int i = 0; i < procedureList.size(); i++)
		{
			if (procedureList.get(i)!= null)
			totalOrder+=procedureList.get(i).getSubTotal();
		}
		if (this.vip != null)
			totalOrder+=vip.getPriceForVIP();
		
		return totalOrder;
	}

	public Double getTotalOrderWithDiscount() {
		if (this.discount != null)
			return this.getTotalOrder() - this.discount;   // por las promociones
		else
			return this.getTotalOrder();   // por las promociones
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

	public List<OrderItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<OrderItem> itemList) {
		this.itemList = itemList;
	}

	public SuperOrder getSuperOrder() {
		return superOrder;
	}

	public void setSuperOrder(SuperOrder superOrder) {
		this.superOrder = superOrder;
	}
	
	public Integer getOrderRecoveryTime() {
		Integer result = 0 ;
		for (OrderProcedure op : procedureList)
			result = (op.getProcedure().getRecoveryTime() > result)? op.getProcedure().getRecoveryTime(): result ;

		return result ;
	}

	public EnumPayMethods getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(EnumPayMethods payMethod) {
		this.payMethod = payMethod;
	}

	public EnumPromoTo getPromoTo() {
		return promoTo;
	}

	public void setPromoTo(EnumPromoTo promoTo) {
		this.promoTo = promoTo;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	public int getItemPos(Long id) // me deveulve la posicion de un item dado el id o -1 si no está
	{
		for (OrderItem orderItem : itemList) 
			if (orderItem.getProduct().getId() == id)
				return itemList.indexOf(orderItem);
		return -1 ;
	}
	
	public void updateItemAmount(int pos, int amount)
	{
		itemList.get(pos).setAmount(amount);
	}

	public VIP getVip() {
		return vip;
	}

	public void setVip(VIP vip) {
		this.vip = vip;
	}

}
