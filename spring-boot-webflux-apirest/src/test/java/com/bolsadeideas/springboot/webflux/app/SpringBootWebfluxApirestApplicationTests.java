package com.bolsadeideas.springboot.webflux.app;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categorias;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;

import reactor.core.publisher.Mono;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SpringBootWebfluxApirestApplicationTests {
	
	@Autowired
	private WebTestClient client;
	
	@Value("${config.base.endpoint}")
	private String url;

	@Test
	void listarTest() {
		client.get()
		.uri("/api/v2/productos")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Producto.class)
		.consumeWith(response -> {
			List<Producto> list = response.getResponseBody();
			Assertions.assertThat(list.size()>0).isTrue();
		});
	}
	
	@Test
	void verTest() {
		client.get()
		.uri("/api/v2/productos")
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void crearTest() {
		
		Categorias electronico = new Categorias("Electronico");
		electronico.setId("1");
		
		Producto producto = new Producto("Mesa comedor", 100.00, electronico);
		
		client.post().uri("/api/v2/productos")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Mesa comedor")
		.jsonPath("$.categorias.nombre").isEqualTo("Electronico");
		/*otra solucion puede ser :
		.expectBody(Producto.class)
		.consumeWith(response -> {
			Producto pro = response.getResponseBody();
			Assertions.assertThat(pro.getId().isEmpty()).isFalse();
			Assertions.assertThat(pro.getNombre().equalsIgnoreCase(producto.getNombre())).isTrue();
		});*/
	}
	
	@Test
	public void eliminarTest() {
		client.delete()
		.uri(url + "/{id}", Collections.singletonMap("id", "1"))
		.exchange()
		.expectStatus().isNoContent()
		.expectBody()
		.isEmpty();
		
		client.get()
		.uri(url + "/{id}", Collections.singletonMap("id", "1"))
		.exchange()
		.expectStatus().isNotFound()
		.expectBody()
		.isEmpty();
	}

}
