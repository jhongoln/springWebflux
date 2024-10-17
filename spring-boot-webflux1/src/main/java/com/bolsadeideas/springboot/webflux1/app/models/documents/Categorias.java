package com.bolsadeideas.springboot.webflux1.app.models.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;

@Document(collection = "categorias")
public class Categorias {
	
	@Id
	@NotEmpty
	private String id;
	
	private String nombre;
	

	public Categorias() {
	}

	public Categorias(String nombre) {
		super();
		this.nombre = nombre;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
