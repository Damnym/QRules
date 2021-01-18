package com.plastic305.web.app.models.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "clients")
public class Client implements Serializable 
{ private static final long serialVersionUID = 6439305829659849320L;
// DB
//****
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

//	@NotEmpty
	private String conditionsName;
	
	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SuperOrder> superOrderList;
//	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Order> orderList;
	

// GENERAL
//********
	@NotEmpty
	private String name;
	
	//@NotEmpty
	private String dni;
	
	@NotEmpty
	private String surname;
	
	@NotEmpty
	@Email
	private String email;
	
	@NotEmpty
	private String mobile;
	
	@NotNull 
	@Min(5)
	@Max(120)
	private Integer age;
	
	@NotEmpty
	private String gender;
	
	@Temporal(TemporalType.DATE)       // Esta fecha es la de suscripción
	@Column(name = "create_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createAt;
	
	
// SYSTEM LOGIC
//*************	
	@Transient                               
	private List<Suffering> conditionsList;
	
	@Transient  
	private List<Procedure> aditionalProcedures;
	
	@Transient  
	private List<Procedure> vipAditionalProcedures;

	@Transient  
	private VIP vip ;
	
	private boolean moreOneLipo;
	
	private boolean hasWeightLoss;

	private Long p1, p2, doctor, accepted;  // esto valorar no ponerlo en la BD agregarlo como Order
	                                       // 0: No aceptada por enfermedad
	private Double weight; 
	
	private Double hbg;
	
	private Double heightFeetOrCentimeters;
	private Double heightInches ;
	
	@Column(name = "make_cell_saver")   // Esto valorar dejarlo como producto y quitarlo como atributo aparte
	private boolean makeCellSaver;  
	
	@Temporal(TemporalType.DATE)            // Esta fecha es la que elige como tentativa para la operacion
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	
	//  <<<<< IMPLEMENTATION >>>>>
	

	public Client() 
	{
		conditionsList = new ArrayList<Suffering>(); 
		superOrderList  = new ArrayList<SuperOrder>();  // aqui era order 
		aditionalProcedures = new ArrayList<Procedure>();
		vipAditionalProcedures = new ArrayList<Procedure>();
	}
	
	public boolean isMoreOneLipo() {
		return moreOneLipo;
	}

	public List<Procedure> getAditionalProcedures() {
		return aditionalProcedures;
	}

	public void setAditionalProcedures(List<Procedure> aditionalProcedures) {
		this.aditionalProcedures = aditionalProcedures;
	}

	public void setMoreOneLipo(boolean moreOneLipo) {
		this.moreOneLipo = moreOneLipo;
	}

	public boolean isHasWeightLoss() {
		return hasWeightLoss;
	}

	public void setHasWeightLoss(boolean hasWeightLoss) {
		this.hasWeightLoss = hasWeightLoss;
	}

	public String getConditionsName() {
		return conditionsName;
	}

	public void setConditionsName(String conditionsName) {
		this.conditionsName = conditionsName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Double getHbg() {
		return hbg;
	}

	public void setHbg(Double hbg) {
		this.hbg = hbg;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getShortDate() {
		Calendar c = new GregorianCalendar(); 
		c.setTime(date);

		String month = Integer.toString(c.get(Calendar.MONTH));
		String year = Integer.toString(c.get(Calendar.YEAR)); ;
		
		return " (" + year + "/" + month + ")";
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeightFeetOrCentimeters() {
		return heightFeetOrCentimeters;
	}

	public void setHeightFeetOrCentimeters(Double heightFeetOrCentimeters) {
		this.heightFeetOrCentimeters = heightFeetOrCentimeters;
	}

	public Double getHeightInches() {
		return heightInches;
	}

	public void setHeightInches(Double heightInches) {
		this.heightInches = heightInches;
	}
	
	/**
	 * @return the accepted
	 */
	public Long getAccepted() {
		return accepted;
	}

	/**
	 * @param accepted the accepted to set
	 */
	public void setAccepted(Long accepted) {
		this.accepted = accepted;
	}

	/**
	 * @return the p1
	 */
	public Long getP1() {
		return p1;
	}

	/**
	 * @param p1 the p1 to set
	 */
	public void setP1(Long p1) {
		this.p1 = p1;
	}

	/**
	
 * @return the p2
	 */
	public Long getP2() {
		return p2;
	}

	/**
	 * @param p2 the p2 to set
	 */
	public void setP2(Long p2) {
		this.p2 = p2;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the doctor
	 */
	public Long getDoctor() {
		return doctor;
	}

	/**
	 * @param doctor the doctor to set
	 */
	public void setDoctor(Long doctor) {
		this.doctor = doctor;
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

	/**
	 * @return the conditionsList
	 */
	public List<Suffering> getConditionsList() {
		return conditionsList;
	}

	/**
	 * @param conditionsList the conditionsList to set
	 */
	public void setConditionsList(List<Suffering> conditionsList) {
		this.conditionsList = conditionsList;
	}

	public boolean isMakeCellSaver() {
		return makeCellSaver;
	}

	public void setMakeCellSaver(boolean makeCellSaver) {
		this.makeCellSaver = makeCellSaver;
	}

	public List<SuperOrder> getSuperOrderList() {
		return superOrderList;
	}

	public void setSuperOrderList(List<SuperOrder> superOrderList) {
		this.superOrderList = superOrderList;
	}

	public List<Procedure> getVipAditionalProcedures() {
		return vipAditionalProcedures;
	}

	public void setVipAditionalProcedures(List<Procedure> vipAditionalProcedures) {
		this.vipAditionalProcedures = vipAditionalProcedures;
	}

	public VIP getVip() {
		return vip;
	}

	public void setVip(VIP vip) {
		this.vip = vip;
	}

//	public List<Order> getOrderList() {
//		return orderList;
//	}
//	
//	public void setOrderList(List<Order> orderList) {
//		this.orderList = orderList;
//	}

}
