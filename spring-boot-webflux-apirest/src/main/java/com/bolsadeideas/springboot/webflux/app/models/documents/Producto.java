package com.bolsadeideas.springboot.webflux.app.models.documents;



import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;



@Document(collection="productos")
public class Producto {
	
	@Id
	private String id;
	
	@NotEmpty
	private String nombre;
	
	@NotNull
	private Double precio;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createAt;
	
	@Valid
	private Categorias categorias;
	
	
	private String foto;
	
	
	public Producto() {
	}

	public Producto(String nombre, Double precio) {
		this.nombre = nombre;
		this.precio = precio;
	}
	

	public Producto( String nombre,  Double precio, Categorias categorias) {
		super();
		this.nombre = nombre;
		this.precio = precio;
		this.categorias = categorias;
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

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Categorias getCategorias() {
		return categorias;
	}

	public void setCategorias(Categorias categorias) {
		this.categorias = categorias;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	

}
