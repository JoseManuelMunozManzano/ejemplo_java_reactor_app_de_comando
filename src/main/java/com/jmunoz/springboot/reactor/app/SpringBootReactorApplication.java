package com.jmunoz.springboot.reactor.app;

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
		Flux<String> nombres = Flux.just("José M", "Adriana", "María", "Tania", "Ferney")
				.map(nombre -> {return nombre.toUpperCase();})
				// Emulamos algún tipo de error
				.doOnNext(e -> {
					if (e.isEmpty()) {
						throw new RuntimeException("Nombres no pueden ser vacíos");
					}
					System.out.println(e);
				})
		// INMUTABLE
		// Todas las operaciones que se realizan con los operadores NO MODIFICAN EL FLUJO ORIGINAL, sino que retornan
		// una nueva instancia con los datos transformados.
				.map(nombre -> {return nombre.toLowerCase();});


		// Nos suscribimos al Observable
		// En el subscriber también se pueden realizar tareas usando funciones anónimas al consumir la información.
		// También se puede manejar cualquier tipo de error que pueda ocurrir en el segundo parámetro.
		//
		// Tercer parámetro
		// Hay otra forma del método subscribe que nos permite hacer una tarea cuando se completa
		// la subscripción en el onComplete, que se implementa con un Runnable utilizando hilos.
		// Es decir, cuando el Observable termina CORRECTAMENTE de emitir el último elemento.
		nombres.subscribe(log::info,
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
