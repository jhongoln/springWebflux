package com.bolsadeideas.springboot.webflux.app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.webflux.app.models.dao.CategoriaDao;
import com.bolsadeideas.springboot.webflux.app.models.dao.ProductoDao;
import com.bolsadeideas.springboot.webflux.app.models.documents.Categorias;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService{
	
	@Autowired(required = true)
	private ProductoDao dao;
	
	@Autowired
	private CategoriaDao daoCat;

	@Override
	public Flux<Producto> findAll() {
		return dao.findAll();
	}

	@Override
	public Mono<Producto> findById(String id) {
		return dao.findById(id);
	}

	@Override
	public Mono<Producto> save(Producto producto) {
		return dao.save(producto);
	}

	@Override
	public Mono<Void> delete(Producto producto) {
		return dao.delete(producto);
	}

	@Override
	public Flux<Producto> returnAllWhitUppercaseInFieldNombre() {
		return dao.findAll().map(producto -> {
			producto.setNombre(producto.getNombre().toUpperCase());
			
			return producto;
			
		});
	}

	@Override
	public Flux<Producto> returnAllWhitRepeat() {
		return returnAllWhitUppercaseInFieldNombre().repeat(5000);
	}

	@Override
	public Flux<Categorias> findAllCategoria() {
		return daoCat.findAll();
	}

	@Override
	public Mono<Categorias> findByIdCategorias(String id) {
		return daoCat.findById(id);
	}

	@Override
	public Mono<Categorias> saveCategoria(Categorias categorias) {
		return daoCat.save(categorias);
	}
	
	

	

}
