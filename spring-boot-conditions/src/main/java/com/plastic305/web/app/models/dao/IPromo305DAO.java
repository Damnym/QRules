package com.plastic305.web.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.EnumPromoTo;
import com.plastic305.web.app.models.entities.EnumPromotionType;
import com.plastic305.web.app.models.entities.ItemsAmountForVIP;
import com.plastic305.web.app.models.entities.Promos305;
import com.plastic305.web.app.models.entities.VIPDoctorProcedure;


public interface IPromo305DAO extends CrudRepository<Promos305, Long> 
{
	@Query("SELECT p "
	+ 	   "FROM Promos305 p "
	+      "WHERE p.minDateValid <=?1 AND p.maxDateValid>=?1")
	public List<Promos305> findActivePromos(Date now);

	@Query("SELECT p "
	+ 	   "FROM Promos305 p "
	+		     "join p.promotionsList promol "  
	+		     "join promol.doctor d "
	+      "WHERE p.minDateValid <=?1 AND p.maxDateValid>=?1 AND d.id=?2")
	public List<Promos305> findActivePromosForDoctor(Date now, Long idD);
	
	@Query("SELECT promol "
	+ 	   "FROM Promos305 p "
	+		     "join p.promotionsList promol "  
	+		     "join promol.doctor d "
	+      "WHERE p.minDateValid <=?1 AND p.maxDateValid>=?1 AND d.id=?2")
	public List<VIPDoctorProcedure> findActivePromosXForDoctor(Date now, Long idD);
	
	@Query("SELECT promol "
	+ 	   "FROM Promos305 p "
	+		     "join p.promotionsList promol "  
	+		     "join promol.doctor d "
	+		     "join promol.procList procl "
	+		     "join procl.procedure proc "
	+      "WHERE p.minDateValid <=?1 AND p.maxDateValid>=?1 AND d.id=?2 AND proc.id=?3")
	public List<VIPDoctorProcedure> findActivePromosForDoctorAndProcedure(Date now, Long idD, Long idP);

	
	@Query("SELECT promo_l.promoProcOrItems "    // apply to
	+ 	   "FROM Promos305 p "
	+		     "join p.promotionsList promo_l "   
	+		     "join promo_l.doctor d "
	+		     "join promo_l.procList proc_l "
	+		     "join proc_l.procedure proc "
	+      "WHERE p.minDateValid <=?1 AND p.maxDateValid>=?1 AND d.id=?2 AND proc.id=?3")
	public List<Object> countPromoTypeForDoctorAndProcedure(Date now, Long idD, Long idP);

	@Query("SELECT freeI "
	+ 	   "FROM Promos305 p "
	+		     "join p.promotionsList promol "  
	+		     "join promol.doctor d "
	+		     "join promol.freeItemsList freeI "
	+		     "join promol.procList procl "
	+		     "join procl.procedure proc "
	+      "WHERE p.minDateValid <=?1 AND p.maxDateValid>=?1 AND d.id=?2 AND proc.id=?3")
	public List<ItemsAmountForVIP> findItemFreeInActivePromoByDoctorNProcedure(Date now, Long idD, Long idP);
	

	
}
