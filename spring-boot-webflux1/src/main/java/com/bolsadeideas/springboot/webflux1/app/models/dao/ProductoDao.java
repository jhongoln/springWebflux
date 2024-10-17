package com.bolsadeideas.springboot.webflux1.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.webflux1.app.models.documents.Producto;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String>{
	

}
