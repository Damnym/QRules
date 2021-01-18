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
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "procedures")
public class Procedure implements Serializable { private static final long serialVersionUID = -2251354311478813324L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String name;

	@Column(name = "required_cell_saver")
	private boolean requiredCellSaver;

	private Boolean aditional;
	
	private Integer recoveryTime; // En meses

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "procedure_id")
	private List<ProductRecommendedByProcedure> productRecommendedList;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "procedure_id")
	private List<ProcedureImage> imagesList;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "procedure_id")
	private List<TipsByProcedure> tipsList;
	
	
	@Transient
	private List<Procedure> notComboList ;
	@Transient
	private List<Procedure> comboList ;
	

//  <<<<< IMPLEMENTATION >>>>>

	public Procedure() {
		productRecommendedList = new ArrayList<ProductRecommendedByProcedure>();
		imagesList = new ArrayList<ProcedureImage>(4);
		
		notComboList = new ArrayList<Procedure>();
		comboList = new ArrayList<Procedure>();
		tipsList = new ArrayList<TipsByProcedure>();
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequiredCellSaver() {
		return requiredCellSaver;
	}

	public void setRequiredCellSaver(boolean cellSaver) {
		this.requiredCellSaver = cellSaver;
	}

	public Integer getRecoveryTime() {
		return recoveryTime;
	}

	public void setRecoveryTime(Integer recoveryTime) {
		this.recoveryTime = recoveryTime;
	}

	public List<ProcedureImage> getImagesList() {
		return imagesList;
	}

	public void setImagesList(List<ProcedureImage> imagesList) {
		this.imagesList = imagesList;
	}
	
	public void addImage(String imgPath, int pos) // Esta hecho para 4 imágenes
	{
		if (imagesList.size()>=pos)
		{
			if (imagesList.get(pos-1) != null) 
				imagesList.get(pos-1).setPath(imgPath);
		}
		else
		{
			ProcedureImage img = new ProcedureImage();
			img.setPath(imgPath);
			imagesList.add(img);
		}
	}
	
	public ProductRecommendedByProcedure getProductRecommended(int pos) {
		return productRecommendedList.get(pos-1);
	}

	public List<Procedure> getNotComboList() {
		return notComboList;
	}

	public void setNotComboList(List<Procedure> notComboList) {
		this.notComboList = notComboList;
	}

	public List<Procedure> getComboList() {
		return comboList;
	}

	public void setComboList(List<Procedure> comboList) {
		this.comboList = comboList;
	}

	public List<TipsByProcedure> getTipsList() {
		return tipsList;
	}

	public void setTipsList(List<TipsByProcedure> tipsList) {
		this.tipsList = tipsList;
	}

}
