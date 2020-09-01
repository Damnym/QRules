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
public class Rule1 implements IRules {
	
    @Override
    public String evaluate(IAttributeService attService) {
    	if (attService.getAttributeByName("Cirugia_tiempo").getValue() == null)
    		return "_Cirugia_tiempo";
    	else if (attService.getAttributeByName("Cirugia_tiempo").getValue().equals("Si")) {
        	 	return "T";
        		}
        return "F";	
    }

   @Override
    public String getAction() {
        return "No apto para la CA por no alcanzar el tiempo necesario posterior a la última cirugía,"
        		+ " por favor recurra a su médico al cumplir los 2 años de su última cirugía";
    }

}
