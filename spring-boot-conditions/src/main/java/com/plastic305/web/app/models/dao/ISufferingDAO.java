package com.plastic305.web.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.plastic305.web.app.models.entities.Suffering;

public interface ISufferingDAO extends CrudRepository<Suffering, Long> {
	
	@Query("SELECT s from Suffering s where (s.name=?1)")
	public Suffering findByName(String term);
	
	@Query("SELECT COUNT (s) FROM Doctor d "
			+ 	"join d.sufferingsList sl "
			+ 	"join sl.suffering s "
			+ "WHERE s.id=?1")
	public int getDoctorCountsByConditionsId(Long id);
	
	@Query("SELECT s FROM Suffering s WHERE (s.name like %?1%)")
	public List<Suffering> findSufferingsByName(String term);

}
