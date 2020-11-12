package com.plastic305.web.app.models.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "procedures")
public class Procedure implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String name;

	@Column(name = "required_cell_saver")
	private boolean requiredCellSaver;

	private Boolean aditional;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "procedure_id")
	private List<ProductRecommendedByProcedure> productRecommendedList;

//  <<<<< IMPLEMENTATION >>>>>

	public Procedure() {
		productRecommendedList = new ArrayList<ProductRecommendedByProcedure>();
	}

	public List<ProductRecommendedByProcedure> getProductRecommendedList() {
		return productRecommendedList;
	}

	public Boolean getAditional() {
		return aditional;
	}

	public void setAditional(Boolean aditional) {
		this.aditional = aditional;
	}

	public void setProductRecommendedList(List<ProductRecommendedByProcedure> productRecommendedList) {
		this.productRecommendedList = productRecommendedList;
	}

	public void addItem(ProductRecommendedByProcedure item) {
		productRecommendedList.add(item);
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequiredCellSaver() {
		return requiredCellSaver;
	}

	public void setRequiredCellSaver(boolean cellSaver) {
		this.requiredCellSaver = cellSaver;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2251354311478813324L;

}
