package com.plastic305.web.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.IVipDAO;
import com.plastic305.web.app.models.entities.VIP;

@Service
public class VIPService implements IVIPService 
{
    @Autowired IVipDAO vipDAO;
	
	@Override
	@Transactional
	public void save(VIP vip)
	{
		vipDAO.save(vip);
	}

	@Override
	@Transactional(readOnly = true)
	public List<VIP> findAll() 
	{
		return (List<VIP>) vipDAO.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public VIP findOne(Long id) 
	{
		return vipDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) 
	{
		vipDAO.deleteById(id);
	}

}
