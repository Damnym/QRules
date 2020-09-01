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
public class Rule9 implements IRules{
	
   @Override
   public String evaluate( IAttributeService attService) {
	   if (attService.getAttributeByName("Cirugia_tiempo").getValue().equals("No") && 
		       attService.getAttributeByName("Estado_salud").getValue().equals("Enfermedad sistemica grave")) {
		        if (attService.getAttributeByName("Diabetes").getValue() == null)
		        	return "_Diabetes";
		        else if (attService.getAttributeByName("Diabetes").getValue().equals("Descompensada con afectación de órganos"))
		       	 		return "T";
		        }
		        return "F";
		    }

   @Override
    public String getAction() {
        return "No apto para la CA, pues su estado de diabetes compromete su salud, por lo que debe valorarse "
        		+ "otro tipo de cirugía";
    }
}

    
    

