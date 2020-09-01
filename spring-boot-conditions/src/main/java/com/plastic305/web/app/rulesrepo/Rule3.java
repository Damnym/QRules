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
public class Rule3 implements IRules {
    @Override
    public String evaluate(IAttributeService attService ) {
        if (attService.getAttributeByName("Cirugia_tiempo").getValue().equals("No") && 
            attService.getAttributeByName("Estado_salud").getValue().equals("Enfermedad sistemica grave")) {
        	if (attService.getAttributeByName("Cardiaco").getValue() == null)
       	 		return "_Cardiaco";
        	if (attService.getAttributeByName("Respiratorio").getValue() == null)
        		return "_Respiratorio";
        	if (attService.getAttributeByName("Cardiaco").getValue().equals("Si") &&
        		attService.getAttributeByName("Respiratorio").getValue().equals("Si")) 
       	 		return "T";
        }
        return "F";
}

    @Override
    public String getAction() {
        return "No apto para la CA, pues tener problemas cardiacos unidos a problemas respiratorios compromete "
        		+ "su salud, debe valorar otro tipo de cirugía.";
    }
}
