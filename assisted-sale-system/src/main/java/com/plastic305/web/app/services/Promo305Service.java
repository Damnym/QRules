package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IPromo305DAO;
import com.plastic305.web.app.models.entities.EnumPromotionType;
import com.plastic305.web.app.models.entities.ItemsAmountForVIP;
import com.plastic305.web.app.models.entities.OrderProcedure;
import com.plastic305.web.app.models.entities.Promos305;
import com.plastic305.web.app.models.entities.VIPDoctorProcedure;

@Service
public class Promo305Service implements IPromo305Service 
{
    @Autowired IPromo305DAO promoDAO;

	@Override
	@Transactional
	public void save(Promos305 nPromo) 
	{
		promoDAO.save(nPromo) ;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Promos305> findAll() 
	{
		return (List<Promos305>) promoDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Promos305> findActivePromos(Date now) 
	{
		return promoDAO.findActivePromos(now);
	}

	@Override
	@Transactional(readOnly = true)
	public Promos305 findOne(Long id) 
	{
		return promoDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) 
	{
		promoDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Promos305> findActivePromosForDoctor(Date now, Long idD) 
	{
		return promoDAO.findActivePromosForDoctor(now, idD);
	}

	@Override
	@Transactional(readOnly = true)
	public List<VIPDoctorProcedure> findActivePromosXForDoctor(Date now, Long idD) 
	{
		return promoDAO.findActivePromosXForDoctor(now, idD);
	}

	@Override
	@Transactional
	public List<VIPDoctorProcedure> findActivePromosForDoctorAndProcedure(Date now, Long idD, Long idP) 
	{
		return promoDAO.findActivePromosForDoctorAndProcedure(now, idD, idP);
	}

	@Override
	@Transactional
	public List<Object> countPromoTypeForDoctorAndProcedure(Date now, Long idD, Long idP) 
	{
		return  promoDAO.countPromoTypeForDoctorAndProcedure(now, idD, idP);
	}

	private int ItemPos(List<ItemsAmountForVIP> list, Long id)
	{
		for (ItemsAmountForVIP itemsAmountForVIP : list) 
			if (itemsAmountForVIP.getId() == id)
				return list.indexOf(itemsAmountForVIP) ;
		
		return -1 ;
	}
	
	@Override
	@Transactional
	public List<ItemsAmountForVIP> findFreeItemByDoctorNProcedures(Date now, Long idD, List<OrderProcedure> procedureList) 
	{
		List<ItemsAmountForVIP> result = new ArrayList<ItemsAmountForVIP>();
		HashSet<Long> itemFreeId = new HashSet<Long>();
		int posx ;
		
		for (OrderProcedure procedure: procedureList)
			for (ItemsAmountForVIP itemAmountForVIP : promoDAO.findItemFreeInActivePromoByDoctorNProcedure(now, idD, procedure.getProcedure().getId())) 
				if (!itemFreeId.contains(itemAmountForVIP.getItem().getId()))
				{
					result.add(itemAmountForVIP);
					itemFreeId.add(itemAmountForVIP.getItem().getId());
				}
				else
				{
					posx = ItemPos(result, itemAmountForVIP.getItem().getId()) ;
					result.get(posx).setAmount(result.get(posx).getAmount() + itemAmountForVIP.getAmount());
				}
		
		return result;
	}
	
}
