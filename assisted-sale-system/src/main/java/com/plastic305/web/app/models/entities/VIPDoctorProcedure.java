package com.plastic305.web.app.models.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "vip_doctor_procedure")
@PrimaryKeyJoinColumn(name="vip_doctor_procedure_id")
public class VIPDoctorProcedure extends Promotion
{  private static final long serialVersionUID = -1970526059190784323L;

	@ManyToOne(fetch = FetchType.LAZY) 
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "vip_doctor_procedure_id")
	private List<ProceduresForVIP> procList;
	
	//  <<<<< IMPLEMENTATION >>>>>
	
	public Doctor getDoctor() {	return doctor; }

	public void setDoctor(Doctor doctor) { this.doctor = doctor; }

	public List<ProceduresForVIP> getProcList() { return procList; }

	public void setProcList(List<ProceduresForVIP> procList) { this.procList = procList; }

	
}
