package com.bolsadeideas.springboot.webflux.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categorias;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.services.ProductoService;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApirestApplication implements CommandLineRunner{
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApirestApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApirestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("productos").subscribe();
		mongoTemplate.dropCollection("categorias").subscribe(); 
		
		Categorias electronico = new Categorias("Electronico");
		electronico.setId("1");
		Categorias Deporte = new Categorias("Deporte");
		Categorias Computacion = new Categorias("Computacion");
		Categorias Muebles = new Categorias("Muebles");
		
		Flux.just(electronico, Deporte, Computacion, Muebles)
		.flatMap(productoService::saveCategoria)
		.doOnNext(c -> {
			log.info("Categoria");
		}).thenMany(
		//Nota: no utilizamos map porque el map devuelve propiedades de tipo Mono<Producto>
		//pero el flat map aplana por debajo y devuelve propiedades de tipo Producto
		/*Flux.just(new Producto("TV sony", 20.3),
				new Producto("Sony camara", 35.6)
				).map(producto -> dao.save(producto)
						).subscribe(producto -> log.info(producto.getClass()));*/
		
		Flux.just(new Producto("TV sony", 20.3, electronico),
				new Producto("Sony camara", 35.6, electronico),
				new Producto("Apple iPod", 85.24, Computacion),
				new Producto("Sony Notebook", 78.65, Computacion),
				new Producto("Hewlett Packard", 98.54, Computacion),
				new Producto("Biciclete", 74.12,Deporte),
				new Producto("HP Computador", 220.54,Computacion),
				new Producto("Cajones", 988.2,Muebles),
				new Producto("Teclado", 789.25,Computacion )
				).flatMap(producto -> {
					if(producto.getNombre().equalsIgnoreCase("TV sony")) producto.setId("1");
					producto.setCreateAt(new Date());
					return productoService.save(producto);
					})
		).subscribe(producto -> log.info("insert: "+ producto.getId()+" "+ producto.getNombre()));
		
		
	}

}
