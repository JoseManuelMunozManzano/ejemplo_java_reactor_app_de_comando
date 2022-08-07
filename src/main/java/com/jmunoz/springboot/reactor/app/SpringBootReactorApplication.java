package com.jmunoz.springboot.reactor.app;

import com.jmunoz.springboot.reactor.app.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;


// Para que la app sea de consola hay que implementar CommandLineRunner

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Trabajando con Flux para realizar nuestro primer Observable
		Flux<Usuario> nombres = Flux.just("José M", "Adriana", "María", "Tania", "Ferney")
				// Usando map retornamos otro tipo de dato diferente. Ver la declaración Flux<Usuario>
				.map(nombre -> new Usuario(nombre.toUpperCase(), null))
				// Ahora tenemos que trabajar con tipo de datos Usuario
				.doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacíos");
					}
					System.out.println(usuario.getNombre());
				})
				.map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				});

		// Tenemos usuarios
		nombres.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado la ejecución del observable con éxito!");
					}
				}
		);
	}
}
