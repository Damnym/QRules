package com.plastic305.web.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.plastic305.web.app.models.entities.Product;
import com.plastic305.web.app.models.entities.ProductByDoctAndProc;
import com.plastic305.web.app.models.entities.ProductRecommendedByProcedure;


public interface IProductsDAO extends PagingAndSortingRepository<Product, Long> {
	
	@Query("SELECT p from Product p where p.name like %?1%")
	public List<Product> findByName(String term);
	
	@Query("SELECT p from Product p where p.name like %?1%")
	public Product findOneByName(String name);
	
	@Query("SELECT p "
		 + "FROM Product p "
		 + "WHERE p.name like %?1% "
		 +   "AND p NOT IN ("  
		 +                  "SELECT prodm "  
		 + 					"FROM Doctor d "                           /* No es mandatory */
		 + 			 	   	    "join d.procList procl "  
		 + 					    "join procl.procedure proc "
		 + 						"join procl.productList prodl " 
		 + 						"join prodl.product prodm " 
		 + 					"WHERE (d.id=?2) AND (proc.id=?3))")
	public List<Product> findNotMandatoryByName(String term, Long idD, Long idP);
	
	public List<Product> findByNameLikeIgnoreCase(String term);
	
	
	@Query("SELECT prod "
		 + "FROM Doctor d "
		 + 		"join d.procList procl "
		 + 		"join procl.procedure proc "
		 + 		"join procl.productList prodl "
		 + 		"join prodl.product prod "
		 + "WHERE (d.id=?1) AND (proc.id=?2) AND (prodl.isIncluded=0)")
	public List<Product> findProductsMandatoryByDoctorByProcedure(Long idD, Long idP); 
	
	@Query("SELECT DISTINCT prod "
		 + "FROM Doctor d "
		 + 		"join d.procList procl "
		 + 		"join procl.procedure proc "
		 + 		"join procl.productList prodl "
		 + 		"join prodl.product prod "
		 + "WHERE (d.id=?1) AND ((proc.id=?2) OR (proc.id=?3)) AND (prodl.isIncluded=0)")
	public List<Product> findProductsMandatoryByDoctorByCombo(Long idD, Long idP1, Long idP2); 
	
	@Query("SELECT prod "
			+ "FROM Doctor d "
			+ 		"join d.procList procl "
			+ 		"join procl.procedure proc "
			+ 		"join procl.productList prodl "
			+ 		"join prodl.product prod "
			+ "WHERE d.id=?1 AND proc.id=?2 AND prodl.isIncluded=1")
	public List<Product> findProductsMandatoryAndIncludedByDoctorByProcedure(Long idD, Long idP);   
	
	@Query("SELECT prod "
			+ "FROM Doctor d "
			+ 		"join d.procList procl "
			+ 		"join procl.procedure proc "
			+ 		"join procl.productList prodl "
			+ 		"join prodl.product prod "
			+ "WHERE d.id=?1 AND ((proc.id=?2) OR (proc.id=?3)) AND prodl.isIncluded=1")
	public List<Product> findProductsMandatoryAndIncludedByDoctorByCombo(Long idD, Long idP1, Long idP2);   
	
	@Query("SELECT prodrlist "
			+ "FROM Procedure p "
			+		"join p.productRecommendedList prodrlist "
			+ 	 	"join prodrlist.product prod "
			+ "WHERE (p.id=?1) AND prod NOT IN("
			+ 								   "SELECT prodm "
			+ 								   "FROM Doctor d "
			+ 										"join d.procList procl "
			+ 										"join procl.procedure proc "
			+ 										"join procl.productList prodl "
			+ 										"join prodl.product prodm "
			+ 								   "WHERE d.id=?2 AND proc.id=?1)")
	public List<ProductRecommendedByProcedure> findProductsRecommendedByProcedure(Long idP, Long idD); 
	
	@Query("SELECT DISTINCT prodrlist "
			+ "FROM Procedure p "
			+		"join p.productRecommendedList prodrlist "
			+ 	 	"join prodrlist.product prod "
			+ "WHERE (((p.id=?1) OR (p.id=?3)) AND prod NOT IN("
			+ 									 	      "SELECT prodm "
			+ 									          "FROM Doctor d "
			+ 										          "join d.procList procl "
			+ 									 	          "join procl.procedure proc "
			+ 										          "join procl.productList prodl "
			+ 										          "join prodl.product prodm "
			+ 									          "WHERE d.id=?2 AND (proc.id=?1 OR proc.id=?3)))")
	public List<ProductRecommendedByProcedure> findProductsRecommendedByCombo(Long idP1, Long idD, Long idP2); 
	
	@Query("SELECT prod "
			+ "FROM Product prod "
			+ "WHERE (prod NOT IN ("
			+                      "SELECT prodm "
			+ 						"FROM Doctor d "                           /* No es mandatory */
			+ 							"join d.procList procl "
			+ 							"join procl.procedure proc "
			+ 							"join procl.productList prodl "
			+ 							"join prodl.product prodm "
			+ 						"WHERE (d.id=?2) AND (proc.id=?1))) "
			+   "AND (prod NOT IN (" 											/* No es Recommended */
			+ 						"SELECT prod " 
			+ 						"FROM Procedure p "
			+    						"join p.productRecommendedList prodl "
			+    						"join prodl.product prod "
			+ 						"WHERE (p.id=?1)))")
	public List<Product> findProductsNotMandatoryAndNotRecommended(Long idP, Long idD);
	
	@Query("SELECT prod "
			+ "FROM Product prod "
			+ "WHERE (prod NOT IN ("
			+                      "SELECT prodm "
			+ 						"FROM Doctor d "                           /* No es mandatory */
			+ 							"join d.procList procl "
			+ 							"join procl.procedure proc "
			+ 							"join procl.productList prodl "
			+ 							"join prodl.product prodm "
			+ 						"WHERE (d.id=?2) AND (proc.id=?1 OR proc.id=?3))) "
			+   "AND (prod NOT IN (" 											/* No es Recommended */
			+ 						"SELECT prod " 
			+ 						"FROM Procedure p "
			+    						"join p.productRecommendedList prodl "
			+    						"join prodl.product prod "
			+ 						"WHERE ( (p.id=?1) OR (p.id=?3) )))")
	public List<Product> findProductsNotMandatoryAndNotRecommended(Long idP1, Long idD, Long IdP2);
	

	
	//  <<<<<<<<<<<<<<< NOOOOOOOOOOOOO se usan	 >>>>>>>>>>>>>>>
	
	
	
	
	@Query("SELECT prod FROM Doctor d "
			+ "join d.procList procl "
			+ "join procl.procedure proc "
			+ "join procl.productList prodl "
			+ "join prodl.product prod "
			+ "WHERE (proc.id=?1)")
	public List<Product> findOldProductsRecommendedByProcedure(Long idP); // Este no da la cantidad recomendada      AND (prodl.isMandatory=0) Eliminado el mandatory
	
	@Query("SELECT prodl FROM Doctor d "
			+ "join d.procList procl "
			+ "join procl.procedure proc "
			+ "join procl.productList prodl "
			+ "WHERE (proc.id=?1)")
	public List<ProductByDoctAndProc> findProductsByProcRecommendedByProcedure(Long idP); //   NOOOOOOOOOOOOO se usa Eliminado el mandatory  AND (prodl.isMandatory=0)
}
