package com.plastic305.web.app.models.entities;

import java.util.ArrayList;
import java.util.List;

public class Attribute  {
	private String id ;      
    private String name ;   // Nombre de atributo
    private String ask ;    // Pregunta
    private String because;   // Explicación de la pregunta
    private String value ;		// Valor elegido por el usuario
    private String resultMsg;  //Texto para dar la respuesta dadas a las preguntas
    private ArrayList <String> domain;   //Lista de posibles valores

    public Attribute(String name, String ask, String because) {
        this.name = name;
        this.ask = ask;
        this.because = because;
        domain = new ArrayList<String>();
    }
    
    public void addDomain(String nDomain){
        domain.add(nDomain);
    }
    
    public List<String> getDomain (){
        return domain;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getBecause() {
        return because;
    }

    public void setBecause(String because) {
        this.because = because;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

	public Attribute() {
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

}
