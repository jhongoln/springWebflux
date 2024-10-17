package com.bolsadeideas.springboot.webflux1.app.services;

import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.webflux1.app.models.documents.Categorias;
import com.bolsadeideas.springboot.webflux1.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ProductoService {
	
	public Flux<Producto> findAll();
	
	public Flux<Producto> returnAllWhitUppercaseInFieldNombre();
	
	public Flux<Producto> returnAllWhitRepeat();
	
    public Mono<Producto> findById(String id);
	
	public Mono<Producto> save(Producto producto);
	
	public Mono<Void> delete(Producto producto);
	
	public Flux<Categorias> findAllCategoria();
	
	public Mono<Categorias> findByIdCategorias(String id);
	
	public Mono<Categorias> saveCategoria(Categorias categorias);
	
}
