package com.plastic305.web.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.plastic305.web.app.models.entities.Client;
import com.plastic305.web.app.models.entities.SuperOrder;
import com.plastic305.web.app.models.entities.SuperOrderStatus;

public interface IClientDAO extends PagingAndSortingRepository<Client, Long> {
	
//	@Query("SELECT c FROM Client c left join fetch c.orderList i WHERE c.id=?1")
//	public Client fetchClientByIdWithOrder(Long id);
	
	@Query("SELECT c FROM Client c left join fetch c.superOrderList i WHERE c.id=?1")
	public Client fetchClientByIdWithOrder(Long id);  // Está con super orden
	
	@Query("SELECT c from Client c where c.name like %?1%")
	public List<Client> findByNameRegExp(String term);

	@Query("SELECT s "
		 + "FROM Client c "
		 +	 "join c.superOrderList s "
		 + "WHERE c.id=?1 AND s.status=?2")
	public SuperOrder getSuperOrderWithSpecificStatusByClient(Long idC, SuperOrderStatus st);

}
