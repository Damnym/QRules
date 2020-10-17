package com.plastic305.web.app.models.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_procedures")
public class OrderProcedure implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6708753856536301907L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "procedure_id")
	private Procedure procedure;

	private Double subTotal;
	
	//  <<<<< IMPLEMENTATION >>>>>
	
	public Procedure getProcedure() {
		return procedure;
	}

	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subtotal) {
		this.subTotal =  subtotal;
	}
	

}
