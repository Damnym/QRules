package com.plastic305.web.app.models.dao;


import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.VIP;

public interface IVipDAO extends CrudRepository<VIP, Long> 
{
	
}
