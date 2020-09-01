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
public class Rule11 implements IRules{
   @Override
   public String evaluate(IAttributeService attService ) {
	   if (attService.getAttributeByName("Cirugia_tiempo").getValue().equals("No") && 
		       attService.getAttributeByName("Estado_salud").getValue().equals("Enfermedad sistemica grave") &&
			   attService.getAttributeByName("Respiratorio").getValue().equals("No") && 
			   attService.getAttributeByName("Cardiaco").getValue().equals("No") &&
			   (attService.getAttributeByName("Diabetes").getValue().equals("No") ||
					    attService.getAttributeByName("Diabetes").getValue().equals("Leve")))
		       	 	return "T";
	   return "F";
		    }

   @Override
    public String getAction() {
        return "Apto para la CA, Asistir a la consulta especializada previa";
    }
}
    
    

