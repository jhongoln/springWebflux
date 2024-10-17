package com.bolsadeideas.springboot.reactor1.app;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bolsadeideas.springboot.reactor1.app.models.Comentarios;
import com.bolsadeideas.springboot.reactor1.app.models.Usuario;
import com.bolsadeideas.springboot.reactor1.app.models.UsuarioComentarios;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactor1Application implements CommandLineRunner{
	
	private static final Logger log = LoggerFactory.getLogger(SpringBootReactor1Application.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactor1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		
	}
	
	public void ejemploContraPresion() {
		Flux.range(1, 10)
		.log()
		.limitRate(5)//forma automatica 
		.subscribe(
				//Forma manual
				/*new Subscriber<Integer>() {
			
			private Subscription s;
			private Integer limite = 2;
			private Integer consumido = 2;
			

			@Override
			public void onSubscribe(Subscription s) {
				this.s = s;
				s.request(limite);
			}

			@Override
			public void onNext(Integer t) {
				log.info(t.toString());
				consumido++;
				if(consumido ==limite) {
					consumido = 0 ;
					s.request(limite);
				}
				
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				
			}
		}*/);
		
	}
	
	public void ejemploIntervalDesdeCeroCreate() {
		Flux.create(emitter -> {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				
				private Integer contador = 0;
				
				@Override
				public void run() {
					//se le asigna un valor al flux en cada interacción
					emitter.next(++contador);
					if(contador == 10) {
						timer.cancel();
						emitter.complete();
					}
					
				}
			}, 1000, 1000);
		}).doOnNext(item -> log.info(item.toString()))
		.doOnComplete(() -> {log.info("");})
		.subscribe();
		
	}
	
	public void ejemploDelayElements() {
		Flux<Integer> rango = Flux.range(1, 12)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i -> log.info(i.toString()))
				.limitRate(6);
		
		rango.blockLast();
	}
	
    public void ejemploInterval() {
		
		Flux<Integer> rango = Flux.range(1, 12);
		Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));
		
		// se combina rango con retraso, se ejecuta 12 veces 
		rango.zipWith(retraso, (ra, re) -> ra)
		.doOnNext(i -> log.info(i.toString()))
		.blockLast();
		
		//blockLast() sirve para bloquear el flujo despues de cada ejecucion
		
	}
	
	
	public void ejemploUsuarioComentariosFlatMap() {
		Mono<Usuario> usuarios = Mono.just(new Usuario("jhon", "Doe"));
		Mono<Comentarios> comentariosUsuario = Mono.fromCallable(() -> {
			Comentarios comentarios = new Comentarios();
			comentarios.addComentarios("comentario1");
			comentarios.addComentarios("comentario2");
			comentarios.addComentarios("comentario3");
			comentarios.addComentarios("comentario4");
			return comentarios;
		});  
		
		usuarios.flatMap(u -> comentariosUsuario.map(c ->  new UsuarioComentarios(u,c)))
		.subscribe(uc -> log.info(uc.toString()));
		
	}
	
	//Combinamos el objeto Mono<Usuario> usuarios con el objeto  Mono<Comentarios>
	// a traves del zipwith 
	// el parametro (u,c) es una tupla dentro del zipwith
	public void ejemploUsuarioComentariosZipWith() {
		Mono<Usuario> usuarios = Mono.just(new Usuario("jhon", "Doe"));
		Mono<Comentarios> comentario = Mono.fromCallable(() -> {
			Comentarios comentarios = new Comentarios();
			comentarios.addComentarios("comentario1");
			comentarios.addComentarios("comentario2");
			comentarios.addComentarios("comentario3");
			comentarios.addComentarios("comentario4");
			return comentarios;
		});  
		
		Mono<UsuarioComentarios> usuarioConComentarios =  usuarios.zipWith(comentario, (u,c)-> new UsuarioComentarios(u, c));
		
		usuarioConComentarios.subscribe(uc -> log.info(uc.toString()));
		
	}
	
   public void ejemploZipWithRangos() {
		Flux<Integer> rangos = Flux.range(0, 4);
		Flux.just(1,2,3,4)
		.map(i -> (i*2))
		.zipWith(rangos, (fluxNum, rango) -> String.format("primer Flux: %d, Segundo Flux: %d", fluxNum, rango))
		.subscribe(texto -> log.info(texto));
		
	}
	
   public void ejemploFlatMap() throws Exception {
		
		List<String> usuarioList = new ArrayList<>();
		
		usuarioList.add("Andres fulano");
		usuarioList.add("Pedro fulano");
		usuarioList.add("juan fulano");
		usuarioList.add("Bruce lee");
		usuarioList.add("Bruce willis");
		
		Flux.fromIterable(usuarioList).map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {
					if(usuario.getNombre().equalsIgnoreCase("bruce")) {
						return Mono.just(usuario);
					}else {
						 return Mono.empty();
					}
				})	
				.map(usuario -> { 
						String nombre = usuario.getNombre().toLowerCase();
						usuario.setNombre(nombre);
						return usuario;
					}).subscribe(item -> log.info(item.toString()));
		
	}
   
	
	
	
    public void ejemploIterable() throws Exception {
		
		List<String> usuarioList = new ArrayList<>();
		
		usuarioList.add("Andres fulano");
		usuarioList.add("Pedro fulano");
		usuarioList.add("juan fulano");
		usuarioList.add("Bruce lee");
		usuarioList.add("Bruce willis");
		
		Flux<String> nombres = Flux.fromIterable(usuarioList);
		
		
		Flux<Usuario> usuarios = nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter( usuario -> usuario.getNombre().equalsIgnoreCase("bruce"))
				.doOnNext(usuario -> {
					if(usuario == null) {
						throw new RuntimeException("Nombre no puede ser vacios");
					}
					System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
					})
				.map(usuario -> {
						String nombre = usuario.getNombre().toLowerCase();
						usuario.setNombre(nombre);
						return usuario;
					});
		
		usuarios.subscribe(item -> log.info(item.toString()), 
				error -> log.error(error.getMessage()),
				new Runnable() {
					
					@Override
					public void run() {
						log.info("finalizo la ejecucion del observable con éxito!");
						
					}
				});
	}

}
