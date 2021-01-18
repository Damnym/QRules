package com.plastic305.web.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Order;

public interface IOrderDAO extends CrudRepository<Order, Long> {

//	@Query("SELECT o FROM Order o "
//			+ "join fetch o.client c "
//			+ "join fetch o.itemList il "
//			+ "join fetch il.product "
//			+ "WHERE o.id=?1")
//	public Order fetchOrderByIdWithClientWithOrderItemWithProduct(Long id);
	
	@Query("SELECT o FROM Order o "
			+ "WHERE o.id=?1")
	public Order fetchOrderByIdWithClientWithOrderItemWithProduct(Long id); // borrando todo lo comentado arriba para q no de error, es que no se está usando

//	@Query("SELECT o FROM Client c "
//			+ "join c.orderList o "
//			+ "WHERE c.id=?1")
//	public Order findOrderByClientId(Long id);
	
	@Query("SELECT o FROM Order o "
			+ "WHERE o.id=?1")
	public Order findOrderByClientId(Long id); // borrando todo lo comentado arriba para q no de error, es que no se está usando
	

}
