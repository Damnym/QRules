package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IDoctorDAO;
import com.plastic305.web.app.models.entities.Combo;
import com.plastic305.web.app.models.entities.ComboByDoctor;
import com.plastic305.web.app.models.entities.Doctor;
import com.plastic305.web.app.models.entities.ProcByCombo;
import com.plastic305.web.app.models.entities.Procedure;
import com.plastic305.web.app.models.entities.Suffering;
import com.plastic305.web.app.models.entities.SufferingByDoctor;

@Service
public class DoctorService implements IDoctorService{ protected final Log logger = LogFactory.getLog(this.getClass());
	@Autowired private IDoctorDAO doctorDAO;
	
	@Override
	@Transactional
	public void save(Doctor nDoctor) {
		doctorDAO.save(nDoctor);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Doctor> findAll() {
		return (List<Doctor>) doctorDAO.findAllByOrderByName() ;
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
	
	private ArrayList<Procedure> compareProcedures(ArrayList<Procedure> list){
		Collections.sort(list, new Comparator<Procedure>() {
		    @Override
			public int compare(Procedure p1, Procedure p2) {
				return p1.getName().compareTo(p2.getName());
			}
		});
		return list;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Doctor> findAllbyConditions(List<Suffering> conditions) {
		HashSet<Doctor> doctorSet = new HashSet<>();
		for (Suffering s: conditions)
			doctorSet.addAll(doctorDAO.fetchDoctorsBySufferingId(s.getId()));
		
		return new ArrayList<Doctor>(doctorSet);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Doctor> findAllbyProcedure(Long idP) {
		return doctorDAO.fetchDoctorsByProcedureId(idP);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Doctor> findAllByConditionsByProcedure(List<Suffering> conditions, Long idP1) {
		HashSet<Doctor> doctorSet = new HashSet<>();
		for (Suffering s: conditions) 
			doctorSet.addAll(doctorDAO.fetchDoctorsBySufferingIdByProcedureId(s.getId(), idP1));
		return new ArrayList<Doctor>(doctorSet);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Procedure> findAllProcedurebyDoctorId(Long idD) {
		return doctorDAO.fetchProceduresByDoctorId(idD);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Procedure> findAllPrincipalProceduresbyDoctorId(Long idD) {
		return doctorDAO.findPProceduresByDoctorId(idD);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Procedure> findAllAditionalProceduresbyDoctorId(Long idD) {
		return doctorDAO.findAProceduresByDoctorId(idD);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Procedure> findProceduresNotBelongToDoctor(Long idD) {
		return doctorDAO.findProceduresNotBelongToDoctor(idD);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Procedure> findProceduresNotBelongToDoctorByName(Long idD, String term){
		return doctorDAO.findProceduresNotBelongToDoctorByName(idD, term);
	}

	@Override  
	@Transactional(readOnly = true)  // como es conjunto el orden me mata...hay q arreglar esto
	public List<Procedure> findAllProcedureOfAllDoctorsByConditions(List<Suffering> conditions) {
		HashSet<Procedure> proceduresSet = new HashSet<>();
		for (Suffering s: conditions) //  Por cada condición
			proceduresSet.addAll(doctorDAO.fetchProceduresByAllDoctorBySufferingId(s.getId()));
		
		return compareProcedures(new ArrayList<>(proceduresSet)) ;
	}

	
	////       AQUI

	@Override
	@Transactional(readOnly = true) //Tratar hacerlo con JPA
	public List<Procedure> findAllProcedurebyDoctorIdbyFirstProcedure(Long idD, Long idP) {
		ArrayList<Procedure> procedureList = new ArrayList<Procedure>();
		
		//Lista de combos de este doctor
		List<ComboByDoctor> ComboList = findOne(idD).getComboList();
		List<Combo> comboListWithParamProcedure = new ArrayList<Combo>();
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
		procedureList.sort(new Comparator<Procedure>()
		{
			public int compare(Procedure p1, Procedure p2) {
 	    		return p1.getName().compareToIgnoreCase(p2.getName());
 	    	}
		});
		return procedureList;
	}
	
	@Override
	@Transactional(readOnly = true) 
	public List<Procedure> findAllPrincipalProcedurebyDoctorIdbyFirstProcedure(Long idD, Long idP) {
		ArrayList<Procedure> procedureList = new ArrayList<Procedure>();
		
		//Lista de combos de este doctor
		List<ComboByDoctor> ComboList = findOne(idD).getComboList();
		List<Combo> comboListWithParamProcedure = new ArrayList<Combo>();
		for (ComboByDoctor oneComboByDoct: ComboList) {
			//Tener lista de combos con el procedimiento Parametro
			for (ProcByCombo oneProcedureByCombo: oneComboByDoct.getCombo().getProcedureList()) {
				if (oneProcedureByCombo.getProcedure().getId()==idP)
					comboListWithParamProcedure.add(oneComboByDoct.getCombo());
			}
		}
		for (Combo combo: comboListWithParamProcedure) {
			for (ProcByCombo oneProcedureByCombo: combo.getProcedureList()) {
				if (oneProcedureByCombo.getProcedure().getId()!=idP && !oneProcedureByCombo.getProcedure().getAditional())
					procedureList.add(oneProcedureByCombo.getProcedure());
			}
		}
		procedureList.sort(new Comparator<Procedure>()
		{
			public int compare(Procedure p1, Procedure p2) {
				return p1.getName().compareToIgnoreCase(p2.getName());
			}
		});
		return procedureList;
	}
	
	////////////////////
	@Override
	@Transactional(readOnly = true) 
	public List<Procedure> findAllAditionalProcedurebyDoctorIdbyCombo(Long idD, Long idP1, Long idP2) 
	{
		ArrayList<Procedure> procedureList = new ArrayList<Procedure>();
		
		for (Procedure procedure: doctorDAO.findAProceduresByDoctorId(idD)) 
			if (this.findAllProcedurebyDoctorIdbyFirstProcedure(idD, idP1).contains(procedure) && 
				(idP2==null || this.findAllProcedurebyDoctorIdbyFirstProcedure(idD, idP2).contains(procedure)) )
				procedureList.add(procedure);

		return procedureList;
	}
	////////////////////
	
	@Override
	@Transactional(readOnly = true) //Tratar hacerlo con JPA
	public List<String> findAllProcedureNamebyDoctorIdbyFirstProcedure(Long idD, Long idP) {
		ArrayList<String> procedureList = new ArrayList<String>();
		
		//Lista de combos de este doctor
		List<ComboByDoctor> ComboList = findOne(idD).getComboList();
		List<Combo> comboListWithParamProcedure = new ArrayList<Combo>();
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
					procedureList.add(oneProcedureByCombo.getProcedure().getName());
			}
		}
		return procedureList;
	}

	////////////////////    OLD OR DEPRECATED ///////////////////

	@Override
	@Transactional(readOnly = true)
	public List<Doctor> fetchDoctorsBySufferingId(Long id) {
		return doctorDAO.fetchDoctorsBySufferingId(id);
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
	public Double getProcedurePrice(Long idD, Long idP, boolean isFinanced) {
		if (isFinanced)
			return doctorDAO.getPriceFinanced(idD, idP);
		else 
			return doctorDAO.getPriceCash(idD, idP);
	}
	
}
