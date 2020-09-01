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
public class Rule4 implements IRules{
   @Override
   public String evaluate( IAttributeService attService) {
	   if (attService.getAttributeByName("Cirugia_tiempo").getValue().equals("No") && 
	       attService.getAttributeByName("Estado_salud").getValue().equals("Enfermedad sistemica grave") &&
		   attService.getAttributeByName("Cardiaco").getValue().equals("Si")) {
	        if (attService.getAttributeByName("Insuficiencia_cardiaca").getValue() == null)
	        	return "_Insuficiencia_cardiaca";
	        else if (attService.getAttributeByName("Insuficiencia_cardiaca").getValue().equals("Si"))
	       	 		return "T";
	        }
	        return "F";
	    }

   @Override
    public String getAction() {
        return "No apto para la CA, pues la insuficiencia cardiaca severa compromete su salud, por lo que debe"
        		+ " valorarse otro tipo de cirugía";
    }
}

    
    

