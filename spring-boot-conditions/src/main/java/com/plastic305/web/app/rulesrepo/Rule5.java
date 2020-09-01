/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plastic305.web.app.rulesrepo;

import com.plastic305.web.app.services.IAttributeService;

/**
 *
 * @author damny
 */
public class Rule5 implements IRules{
   @Override
   public String evaluate(IAttributeService attService ) {
	   if (attService.getAttributeByName("Cirugia_tiempo").getValue().equals("No") && 
		       attService.getAttributeByName("Estado_salud").getValue().equals("Enfermedad sistemica grave") &&
			   attService.getAttributeByName("Cardiaco").getValue().equals("Si")) {
		        if (attService.getAttributeByName("Infarto").getValue() == null)
		        	return "_Infarto";
		        if (attService.getAttributeByName("Infarto").getValue().equals("Si")) {
		        	if (attService.getAttributeByName("Tiempo_infarto").getValue() == null)
		        		return "_Tiempo_infarto";
		        	if (attService.getAttributeByName("Tiempo_infarto").getValue().equals("Si"))
		       	 		return "T";
		        }
	   }
	   return "F";
   }

   @Override
    public String getAction() {
        return "No apto para la CA, pues el tiempo transcurrido desde su infarto es muy corto, por lo que "
        		+ "compromete su salud, debe valorar otro tipo de cirugía";
    }
}

    
    

