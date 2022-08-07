package com.jmunoz.springboot.reactor.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

// Para que la app sea de consola hay que implementar CommandLineRunner

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Trabajando con Flux para realizar nuestro primer Observable
		Flux<String> nombres = Flux.just("José M", "Adriana", "Tania", "Ferney")
		// Para implementar una tarea o proceso cada vez que el observador notifica al subscriber que se ha recibido
		// un elemento se usa un elemento del ciclo de vida del observable llamado doOnNext() que es un método de
		// evento.
				.doOnNext(System.out::println);

		// Nos suscribimos al Observable
		nombres.subscribe();
	}
}
