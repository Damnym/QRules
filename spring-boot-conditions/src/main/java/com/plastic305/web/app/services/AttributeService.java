package com.plastic305.web.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.plastic305.web.app.models.entities.Attribute;

@Service
public class AttributeService implements IAttributeService {
     
	List<Attribute> atts = new ArrayList<Attribute>() ;
	
	public AttributeService() {
		Attribute a = new Attribute("Cirugia_tiempo", //Nombre
					      /* ask */ "¿Ha sido sometido a alguna cirugía en los últimos 2 años?", 
                	  /* because */ "Cirugías con tiempo menor a dos años suelen estar comprometidas, por lo que en este caso, al no ser de vital importancia se posponen hasta alcanzar el tiempo recomendado.");
				  a.addDomain("Si");
				  a.addDomain("No");
				  a.setResultMsg("Tiempo desde la última cirugía mayor que 2 años:  ");
		atts.add(a);
		
		a = new Attribute("Estado_salud", 
                		  "¿Su salud en qué estado se encuentra?", 
                		  "Los pacientes en estado saludable o enfermedad leve no presentan peligro para la vida en la CA.");
		a.addDomain("Saludable o Enfermedad leve"); 
		a.addDomain("Enfermedad sistemica grave");
		a.setResultMsg("Su estado de salud se encuentra: ");
		atts.add(a);

		a = new Attribute("Cardiaco", 
                		  "¿Presenta problemas cardiacos?", 
                		  "Algunos problemas cardiacos o estadíos de los mismos pueden comprometer el resultado de la operación");
		a.addDomain("Si");
		a.addDomain("No");
		a.setResultMsg("Presenta problemas cardiacos: ");
		atts.add(a);

		a = new Attribute("Respiratorio", 
                          "¿Presenta problemas respiratorios?", 
				          "Presentar algunos problemas respiratorios, o unidos a otros padecimientos puede comprometer el resultado de la operación");
		a.addDomain("Si");
		a.addDomain("No");
		a.setResultMsg("Presenta problemas respiratorios: ");
		atts.add(a);

		a = new Attribute("Insuficiencia_cardiaca", 
                          "¿Su problema cardiaco es Insuficiencia Cardiaca?", 
						  "La Insuficiencia Cardiaca puede comprometer el resultado de la operación");
		a.addDomain("Si");
		a.addDomain("No");
		a.setResultMsg("Presenta Insuficiencia cardiaca: ");
		atts.add(a);

		a = new Attribute("Infarto", 
                          "¿Ha sufrido algún infarto?", 
                       	  "Haber sufrido de infartos puede comprometer el resultado de la operación");
		a.addDomain("Si");
		a.addDomain("No");
		a.setResultMsg("Ha sufrido infarto: ");
		atts.add(a);

		a = new Attribute("Tiempo_infarto", 
                		  "¿El tiempo transcurrido desde su infarto es menor a los dos años?", 
                		  "El tiempo de su último infarto influye en su estado de salud");
		a.addDomain("Si");
		a.addDomain("No");
		a.setResultMsg("El tiempo transcurrido desde su infarto es menor a los dos años: ");
		atts.add(a);

		a = new Attribute("Tipo_respiratorio", 
                	      "¿Su afectación respiratoria es severa o moderada?", 
                		  "La magnitud de su afectación respiratoria compromete el resultado de la CA");
		a.addDomain("Severa");
		a.addDomain("Moderada");
		a.setResultMsg("Su afectación respiratoria es: ");
		atts.add(a);

		a = new Attribute("Diabetes", 
                		  "¿Padece de diabetes, en qué estado se encuentra?", 
                		  "Un nivel avanzado de diabetes puede comprometer la salud en la CA");
		a.addDomain("No");
		a.addDomain("Leve");
		a.addDomain("Descompensada con afectación de órganos");
		a.setResultMsg("Presenta problema de diabetes: ");
		atts.add(a);
	}

	@Override
	public List<Attribute> getAttributeList() {
		return atts;
	}

	@Override
	public void save(Attribute nAttribute) {
		int i = 0 ;
		for (Attribute a: atts){
			if (a.getName().equals(nAttribute.getName()))
				atts.set(i, nAttribute);
			i++ ;
	        }
	}
	
	@Override
	public Attribute getAttributeByName(String aName) {
			for (Attribute a: atts){
				if (a.getName().equals(aName))
					return a;
		        }
			return null;
		}

	@Override
	public void resetAttributes() {
		for (Attribute a: atts)
			a.setValue(null);
	}

	@Override
	public List<String> getRespuestas() {
		List <String> respuestas = new ArrayList<>();
		for (Attribute a: atts)
			if (a.getValue()!=null)
				respuestas.add(a.getResultMsg()+a.getValue());
		return respuestas;
	}
	
}
