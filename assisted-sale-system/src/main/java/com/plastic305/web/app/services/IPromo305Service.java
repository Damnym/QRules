package com.plastic305.web.app.services;

import java.util.Date;
import java.util.List;

import com.plastic305.web.app.models.entities.EnumPromotionType;
import com.plastic305.web.app.models.entities.ItemsAmountForVIP;
import com.plastic305.web.app.models.entities.OrderProcedure;
import com.plastic305.web.app.models.entities.Promos305;
import com.plastic305.web.app.models.entities.VIPDoctorProcedure;

public interface IPromo305Service 
{
	public void save(Promos305 nPromo);
	
	public List<Promos305> findAll();
	public List<Promos305> findActivePromos(Date now);
	public List<Promos305> findActivePromosForDoctor(Date now, Long idD);
	
	public List<VIPDoctorProcedure> findActivePromosXForDoctor(Date now, Long idD);
	public List<VIPDoctorProcedure> findActivePromosForDoctorAndProcedure(Date now, Long idD, Long idP);
	
	public List<Object> countPromoTypeForDoctorAndProcedure(Date now, Long idD, Long idP);
	
	public List<ItemsAmountForVIP> findFreeItemByDoctorNProcedures(Date now, Long idD,  List<OrderProcedure> procedureList);
	
	
	public Promos305 findOne(Long id);
	
	public void delete(Long id);

}
