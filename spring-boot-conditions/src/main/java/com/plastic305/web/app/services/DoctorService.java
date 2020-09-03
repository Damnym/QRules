package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IDoctorDAO;
import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.ComboByDoctor;
//import com.plastic305.web.app.models.dao.ISufferingDAOPagNSortRepository;
import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.models.entities.ProcByCombo;
import com.plastic305.web.app.models.entities.ProcByDoct;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Suffering;
//import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.models.entities.SufferingByDoctor;

@Service
public class DoctorService implements IDoctorService{
	@Autowired private IDoctorDAO doctorDAO;
	
	@Override
	@Transactional
	public void save(Doctor nDoctor) {
		doctorDAO.save(nDoctor);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Doctor> findAll() {
		return (List<Doctor>) doctorDAO.findAll() ;
	}

	@Override
	@Transactional(readOnly = true)
	public Doctor findOne(Long id) {
		return doctorDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		doctorDAO.deleteById(id);		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Doctor> findAllbyCondition(Long idC) {
		ArrayList<Doctor> doctorList = new ArrayList<Doctor>();
		for (Doctor d: doctorDAO.findAll()) {
			Iterator<SufferingByDoctor> it = d.getSufferingsList().iterator();
			while(it.hasNext()) {
				if( ((SufferingByDoctor)it.next()).getSuffering().getId()==idC)
					doctorList.add(d);
			}
		}
		return doctorList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Procedure> findAllProcedurebyDoctorId(Long idD) {
		ArrayList<Procedure> procedureList = new ArrayList<Procedure>();
		for (ProcByDoct pByd: findOne(idD).getProcList()) 
			procedureList.add(pByd.getProcedure());
		return procedureList;
	}

	@Override
	@Transactional(readOnly = true) //Tratar hacerlo con JPA
	public List<Procedure> findAllProcedurebyDoctorIdbyFirstProcedure(Long idD, Long idP) {
		ArrayList<Procedure> procedureList = new ArrayList<Procedure>();
		
		//Lista de combos de este doctor
		List<ComboByDoctor> ComboList = findOne(idD).getComboList();
		List<Combo> comboListWithParamProcedure = new ArrayList<Combo>();;
		for (ComboByDoctor oneComboByDoct: ComboList) {
			//Tener lista de combos con el procedimiento Parametro
			for (ProcByCombo oneProcedureByCombo: oneComboByDoct.getCombo().getProcedureList()) {
				if (oneProcedureByCombo.getProcedure().getId()==idP)
					comboListWithParamProcedure.add(oneComboByDoct.getCombo());
			}
		}
		for (Combo combo: comboListWithParamProcedure) {
			for (ProcByCombo oneProcedureByCombo: combo.getProcedureList()) {
				if (oneProcedureByCombo.getProcedure().getId()!=idP)
					procedureList.add(oneProcedureByCombo.getProcedure());
			}
		}
		return procedureList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Doctor> findAllbyProcedure(Long idP) {
		ArrayList<Doctor> doctorList = new ArrayList<Doctor>();
		for (Doctor d: doctorDAO.findAll()) {
			Iterator<ProcByDoct> it = d.getProcList().iterator();
			while(it.hasNext()) {
				if( ((ProcByDoct)it.next()).getProcedure().getId()==idP)
					doctorList.add(d);
			}
		}
		return doctorList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Procedure> findAllProcedureOfAllDoctorsByCondition(Long idC) {
		List<Procedure> procedureList = new ArrayList<Procedure>();
		for (Doctor doctor: this.findAllbyCondition(idC)) {
			List<Procedure> tempList = this.findAllProcedurebyDoctorId(doctor.getId()) ; 
			for(Procedure p: tempList) {
				procedureList.remove(p);
				procedureList.add(p);
			}
		}
		return procedureList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Doctor> findAllByConditionByProcedure(Long idC, Long idP1) {
		List<Doctor> docList = new ArrayList<Doctor>();
		for (Doctor doctor: this.findAllbyProcedure(idP1)) 
			for(SufferingByDoctor sByD: doctor.getSufferingsList()) 
				if (idC.equals(sByD.getSuffering().getId())) 
					docList.add(doctor);
		return docList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Doctor> findAllbyConditions(List<Suffering> conditions) {
		HashSet<Doctor> doctorSet = new HashSet<>();
		for (Suffering s: conditions) 
			for (Doctor d: doctorDAO.findAll()) {
				Iterator<SufferingByDoctor> it = d.getSufferingsList().iterator();
				while(it.hasNext()) 
					if( ((SufferingByDoctor)it.next()).getSuffering().getId()==s.getId())
						doctorSet.add(d);
			}
		
		List<Doctor> doctorList = new ArrayList<Doctor>(doctorSet);
		return doctorList;
	}

	@Override
	public List<Doctor> findAllByConditionsByProcedure(List<Suffering> conditions, Long idP1) {
		HashSet<Doctor> doctorSet = new HashSet<>();
		for (Suffering s: conditions) 
			for (Doctor d: findAllbyProcedure(idP1)) {
				Iterator<SufferingByDoctor> it = d.getSufferingsList().iterator();
				while(it.hasNext()) 
					if( ((SufferingByDoctor)it.next()).getSuffering().getId()==s.getId())
						doctorSet.add(d);
			}
		
		List<Doctor> doctorList = new ArrayList<Doctor>(doctorSet);
		return doctorList;
	}

	@Override
	public List<Procedure> findAllProcedureOfAllDoctorsByConditions(List<Suffering> conditions) {
		HashSet<Procedure> proceduresSet = new HashSet<>();
		for (Suffering s: conditions) //  Por cada condición
			for (Doctor doctor: this.findAllbyCondition(s.getId()))    // Por cada doctor que trata esa condición
				proceduresSet.addAll(this.findAllProcedurebyDoctorId(doctor.getId())) ; 
		List<Procedure> procedureList = new ArrayList<>(proceduresSet) ;
		return procedureList;
	}

}
