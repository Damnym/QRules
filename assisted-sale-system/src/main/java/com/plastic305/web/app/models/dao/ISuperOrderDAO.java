package com.plastic305.web.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.SuperOrder;
import com.plastic305.web.app.models.entities.SuperOrderStatus;

public interface ISuperOrderDAO extends CrudRepository<SuperOrder, Long> 
{
	
//	UPDATE table_name
//	SET column1 = value1, column2 = value2, ...
//	WHERE condition;

	@Query("UPDATE SuperOrder so " 
	+      "SET so.status=?1 "
	+      "WHERE so.status=?2")
	public void UpdateFromInProcessToPending(SuperOrderStatus pto, SuperOrderStatus pfrom);

	@Query("SELECT so "
	+	   "FROM SuperOrder so "
	+      		"join so.client c "
	+      "WHERE so.status=?1 and c.id=?2")
	public SuperOrder SelectInProcess(SuperOrderStatus status, Long client);
	
//	@Query("SELECT o FROM Order o "
//			+ "join fetch o.client c "
//			+ "join fetch o.itemList il "
//			+ "join fetch il.product "
//			+ "WHERE o.id=?1")
//	public Order fetchOrderByIdWithClientWithOrderItemWithProduct(Long id);
//	
	

}
