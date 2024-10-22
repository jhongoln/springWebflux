package com.bolsadeideas.springboot.webflux.app.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import static org.springframework.web.reactive.function.BodyInserters.*;

import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.app.models.documents.Categorias;
import com.bolsadeideas.springboot.webflux.app.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.app.services.ProductoService;

import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {
	
	@Autowired
	private ProductoService productoService;
	
	@Value("${config.path.upload}")
	private String path;
	
	public Mono<ServerResponse> listar(ServerRequest request){
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(productoService.findAll(), Producto.class);
	}
	
	public Mono<ServerResponse> ver(ServerRequest request){
		String id = request.pathVariable("id");
		return productoService.findById(id).flatMap(p -> ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(fromValue(p)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> crear(ServerRequest request){
		Mono<Producto> producto = request.bodyToMono(Producto.class);
		
		return producto.flatMap(p-> {
			if(p.getCreateAt()==null) {
				p.setCreateAt(new Date());
			}
			return productoService.save(p);
		}).flatMap(p -> ServerResponse.created(URI.create("/api/v2/productos/".concat(p.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.body(fromValue(p))
			);
	}
	
	public Mono<ServerResponse> editar(ServerRequest request){
		Mono<Producto> producto = request.bodyToMono(Producto.class);
		
		String id = request.pathVariable("id");
		
		
		
		Mono<Producto> productoDb = productoService.findById(id);
		
		return productoDb.zipWith(producto, (db, req) ->{
			db.setNombre(req.getNombre());
			db.setPrecio(req.getPrecio());
			db.setCategorias(req.getCategorias());
			return db;
		}).flatMap(p -> ServerResponse.created(URI.create("/api/v2/productos/update/".concat(p.getId())))
				.contentType(MediaType.APPLICATION_JSON)
				.body(productoService.save(p), Producto.class))
		.switchIfEmpty(ServerResponse.notFound().build());
		
		
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest request){
		String id = request.pathVariable("id");
		
		Mono<Producto> productoDb = productoService.findById(id);
		
		return productoDb.flatMap(p-> productoService.delete(p).then(ServerResponse.noContent().build()))
				.switchIfEmpty(ServerResponse.notFound().build());
		
	}
	
	
	public Mono<ServerResponse> crearConFoto(ServerRequest request){

        Mono<Producto> producto = request.multipartData().map(multipart -> {
        	FormFieldPart nombre = (FormFieldPart) multipart.toSingleValueMap().get("nombre");
        	FormFieldPart precio = (FormFieldPart) multipart.toSingleValueMap().get("precio");
        	FormFieldPart categoriaId = (FormFieldPart) multipart.toSingleValueMap().get("categoria.id");
        	FormFieldPart categoriaNombre = (FormFieldPart) multipart.toSingleValueMap().get("categoria.nombre");
        	
        	Categorias categoria = new Categorias(categoriaNombre.value());
        	categoria.setId(categoriaId.value());
        	return new Producto(nombre.value(), Double.parseDouble(precio.value()), categoria);
        });
		
		return request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file"))
				.cast(FilePart.class)
				.flatMap(file -> producto
						.flatMap(p -> {
							
					p.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
					.replace(" ", "-")
					.replace(":", "")
					.replace("\\", ""));
					
					p.setCreateAt(new Date());
					
					return file.transferTo(new File(path + p.getFoto())).then(productoService.save(p));
				})).flatMap(p -> ServerResponse.created(URI.create("/api/v2/productos/".concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p)));
	}
	
	public Mono<ServerResponse> upload(ServerRequest request){
		String id = request.pathVariable("id");
		return request.multipartData().map(multipart -> multipart.toSingleValueMap().get("file"))
				.cast(FilePart.class)
				.flatMap(file -> productoService.findById(id)
						.flatMap(p -> {
							
					p.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
					.replace(" ", "-")
					.replace(":", "")
					.replace("\\", ""));
					return file.transferTo(new File(path + p.getFoto())).then(productoService.save(p));
				})).flatMap(p -> ServerResponse.created(URI.create("/api/v2/productos/".concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(p)))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	
	

}
