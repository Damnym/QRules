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
public class Rule6 implements IRules{
   @Override
   public String evaluate(IAttributeService attService) {
	   if (attService.getAttributeByName("Cirugia_tiempo").getValue().equals("No") && 
		       attService.getAttributeByName("Estado_salud").getValue().equals("Enfermedad sistemica grave") &&
			   attService.getAttributeByName("Cardiaco").getValue().equals("Si")) 
		       	 		return "T";
		        return "F";
		    }

   @Override
    public String getAction() {
        return "Apto para la CA. Asistir a la consulta especializada previa";
    }
}

    
    

