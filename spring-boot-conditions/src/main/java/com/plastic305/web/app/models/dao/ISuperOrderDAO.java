package com.plastic305.web.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Order;
import com.plastic305.web.app.models.entities.SuperOrder;

public interface ISuperOrderDAO extends CrudRepository<SuperOrder, Long> {

//	@Query("SELECT o FROM Order o "
//			+ "join fetch o.client c "
//			+ "join fetch o.itemList il "
//			+ "join fetch il.product "
//			+ "WHERE o.id=?1")
//	public Order fetchOrderByIdWithClientWithOrderItemWithProduct(Long id);
//	
//	@Query("SELECT o FROM Client c "
//			+ "join c.orderList o "
//			+ "WHERE c.id=?1")
//	public Order findOrderByClientId(Long id);
	

}
