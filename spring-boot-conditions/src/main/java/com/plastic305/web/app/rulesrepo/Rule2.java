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
public class Rule2 implements IRules{
   @Override
   public String evaluate(IAttributeService attService) {
        if (attService.getAttributeByName("Cirugia_tiempo").getValue().equals("No")) { 
        	if (attService.getAttributeByName("Estado_salud").getValue() == null)
       	 		return "_Estado_salud";    
        	else if (attService.getAttributeByName("Estado_salud").getValue().equals("Saludable o Enfermedad leve"))
       	 		return "T";
        }
        return "F";
    }

   @Override
    public String getAction() {
        return "Totalmente apto para la CA pues su condición de salud no es comprometida en la CA";
    }
}

    
    

