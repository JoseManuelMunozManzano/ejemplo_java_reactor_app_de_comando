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
		Flux<String> nombres = Flux.just("José M", "Adriana", "", "Tania", "Ferney")
				// Emulamos algún tipo de error
				.doOnNext(e -> {
					if (e.isEmpty()) {
						throw new RuntimeException("Nombres no pueden ser vacíos");
					}
					System.out.println(e);
				});

		// Nos suscribimos al Observable
		// En el subscriber también se pueden realizar tareas usando funciones anónimas al consumir la información.
		// También se puede manejar cualquier tipo de error que pueda ocurrir en el segundo parámetro.
		nombres.subscribe(log::info, error -> log.error(error.getMessage()));
	}
}
