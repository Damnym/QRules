package com.plastic305.web.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plastic305.web.app.models.dao.ITipsDAO;
import com.plastic305.web.app.models.entities.Tips;

@Service
public class TipsService implements ITipsService 
{
    @Autowired ITipsDAO tipsDAO;
	
	@Override
	@Transactional
	public void save(Tips tips)
	{
		tipsDAO.save(tips);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tips> findAll() 
	{
		return (List<Tips>) tipsDAO.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Tips findOne(Long id) 
	{
		return tipsDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) 
	{
		tipsDAO.deleteById(id);
	}

}
