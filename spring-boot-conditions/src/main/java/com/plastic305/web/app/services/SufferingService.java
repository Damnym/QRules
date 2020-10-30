package com.plastic305.web.app.services;

//import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.ISufferingDAOPagNSortRepository;
import com.plastic305.web.app.models.entities.Suffering;

@Service
public class SufferingService implements ISufferingService {
	@Autowired
	private ISufferingDAOPagNSortRepository sufferingDAO;
	
	
	@Override
	@Transactional(readOnly = true)
	public Page<Suffering> findAll(Pageable page) {
		return sufferingDAO.findAll(page);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> getSufferingList(int filter) {
//		List<String> sufferingList = new ArrayList<String>();
//		for (Suffering s: this.findAll())
//			if (s.getAccepted()==filter)  // 0 no aceptadas, 1 aceptadas, 2 depende doctor
//				sufferingList.add(s.getName()); 
		return null ; //sufferingList;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Suffering> findAll() {
		return (List<Suffering>) sufferingDAO.findAll() ;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Suffering> findAllRefusedOrWarning() {
//		List<Suffering> sufferingList = new ArrayList<Suffering>();
//		for (Suffering s: this.findAll())
//			// 0 no aceptadas, 1 aceptadas, 2 depende doctor, 3 Warning
//			if (s.getAccepted()!=1)  
//				sufferingList.add(s); 
		return null ;//sufferingList;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Suffering getSufferingByName(String aName) {
		for (Suffering s: this.findAll())
			if (s.getName().equals(aName))
				return s;
		return null;
	}

	@Override
	@Transactional
	public void save(Suffering nSuffering) {
		sufferingDAO.save(nSuffering);
	}

	@Override
	@Transactional(readOnly = true)
	public Suffering findOne(Long id) {
		return sufferingDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		sufferingDAO.deleteById(id);		
	}

	@Override
	public int getDoctorCountsByConditionsId(Long id) {
		return sufferingDAO.getDoctorCountsByConditionsId(id);
	}

	

/*	@Override
	public List<String> findByDoctorID(Long idD, Long idS) {
		List<SufferingByDoctor> list = s_dDAO.findByDoctor_id(idD); // lista de enfermedades del doctor
		List<String> listSN = new ArrayList<String>();
		
		for (SufferingByDoctor s: list)
			listSN.add(s.getSuffering().getName());
		return listSN;
	}
*/

}
