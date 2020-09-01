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
public class Rule7 implements IRules{
   @Override
   public String evaluate(IAttributeService attService ) {
	   if (attService.getAttributeByName("Cirugia_tiempo").getValue().equals("No") && 
		       attService.getAttributeByName("Estado_salud").getValue().equals("Enfermedad sistemica grave") &&
			   attService.getAttributeByName("Respiratorio").getValue().equals("Si")) {
		        if (attService.getAttributeByName("Tipo_respiratorio").getValue() == null)
		        	return "_Tipo_respiratorio";
		        else if (attService.getAttributeByName("Tipo_respiratorio").getValue().equals("Severa"))
		       	 		return "T";
		        }
		        return "F";
		    }

   @Override
    public String getAction() {
        return "No apto para la CA, pues su afección respiratoria es Severa, por lo que compromete su salud, debe "
        		+ "valorar otro tipo de cirugía";
    }
}

    
    

