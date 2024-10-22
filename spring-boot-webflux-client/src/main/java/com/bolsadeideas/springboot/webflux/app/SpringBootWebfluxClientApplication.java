package com.bolsadeideas.springboot.webflux.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bolsadeideas.springboot.webflux.app.models.Producto;
import com.bolsadeideas.springboot.webflux.app.services.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootWebfluxClientApplication implements CommandLineRunner{
	
	@Autowired
	private ProductoService service;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		/*Flux<Producto> pro = service.findAll();
		
		Mono<Boolean> tieneElementos = pro.hasElements();  // Retorna un Mono<Boolean>

		tieneElementos.subscribe(
		    tiene -> {
		        if (tiene) {
		            System.out.println("El Flux no está vacío.");
		        } else {
		            System.out.println("El Flux está vacío.");
		        }
		    }
		);
		
		pro.subscribe(System.out::println);*/
		
	}
	
	

}
