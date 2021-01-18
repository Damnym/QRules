package com.plastic305.web.app.models.entities;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.Table;


@Entity
@Table(name = "procs_by_doctors")
public class ProcByDoct implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double priceCash ;
	private Double priceFinanced ;
	private Boolean cellIncludedInPrice;   // para lo del cell 23-12
	
	private Boolean hasDrains ; 
	
	private Integer postSurgeryMinStayTime;
	private Integer postSurgeryMaxStayTime;
	
	private String remark;
	
	
	@ManyToOne(fetch = FetchType.LAZY) // , cascade = CascadeType.ALL
	@JoinColumn(name = "procedure_id")
	private Procedure procedure;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "proc_by_doct_id")
	private List<ProductByDoctAndProc> productList;
	
	//  <<<<< IMPLEMENTATION >>>>>	
		
	public ProcByDoct() {
		productList = new ArrayList<>();
	}


	public List<ProductByDoctAndProc> getProductList() {
		return productList;
	}


	public void setProductList(List<ProductByDoctAndProc> productList) {
		this.productList = productList;
	}


	public Double getPriceCash() {
		return priceCash;
	}


	public void setPriceCash(Double priceCash) {
		this.priceCash = priceCash;
	}


	public Double getPriceFinanced() {
		return priceFinanced;
	}


	public void setPriceFinanced(Double priceFinanced) {
		this.priceFinanced = priceFinanced;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the procedure
	 */
	public Procedure getProcedure() {
		return procedure;
	}


	/**
	 * @param procedure the procedure to set
	 */
	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -2570548228895528159L;

	public Boolean getHasDrains() {
		return hasDrains;
	}


	public void setHasDrains(Boolean hasDrains) {
		this.hasDrains = hasDrains;
	}


	public Integer getPostSurgeryMinStayTime() {
		return postSurgeryMinStayTime;
	}


	public void setPostSurgeryMinStayTime(Integer postSurgeryMinStayTime) {
		this.postSurgeryMinStayTime = postSurgeryMinStayTime;
	}


	public Integer getPostSurgeryMaxStayTime() {
		return postSurgeryMaxStayTime;
	}


	public void setPostSurgeryMaxStayTime(Integer postSurgeryMaxStayTime) {
		this.postSurgeryMaxStayTime = postSurgeryMaxStayTime;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Boolean getCellIncludedInPrice() {
		return cellIncludedInPrice;
	}


	public void setCellIncludedInPrice(Boolean cellIncludedInPrice) {
		this.cellIncludedInPrice = cellIncludedInPrice;
	}




}
