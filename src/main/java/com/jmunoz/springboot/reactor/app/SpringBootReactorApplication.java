package com.jmunoz.springboot.reactor.app;

import com.jmunoz.springboot.reactor.app.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;


// Para que la app sea de consola hay que implementar CommandLineRunner

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// CREANDO FLUJOS A PARTIR DE OTROS OBJETOS (no Flux.just)
		// Por ejemplo, de una lista o un set que herede de Iterable se puede crear un flux.
		// Esto es muy útil para usar por ejemplo con JPA al hacer una consulta a BD relacionales.
		// Va a retornar una lista de objetos que la podemos convertir a un flujo (stream reactivo) y hacerle
		// operaciones y nos podemos subscribir.
		//
		// Para BD no SQL como MongoDB o Cassandra, y ya con soporte reactivo, en vez de retornar un list,
		// usando la API reactiva de Mongo va a retornar un flujo, Flux si son varios elementos o Mono si es uno.
		// Ejemplo: https://docs.spring.io/spring-data/mongodb/docs/3.4.2/reference/html/#repositories
		// y buscar flatMap
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("José Muñoz");
		usuariosList.add("Adriana López");
		usuariosList.add("María Pérez");
		usuariosList.add("Tania Fernández");
		usuariosList.add("Ferney Rodríguez");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");

		// Se usa el método fromIterable de la clase Flux y se le pasa un Iterable.
		// Hay otros métodos como fromArray o fromStream (un stream normal no reactivo lo pasamos a reactivo)
		Flux<String> nombres = Flux.fromIterable(usuariosList);

		Flux<Usuario> usuarios =  nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().equalsIgnoreCase("bruce"))
				.doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacíos");
					}
					System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
				})
				.map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				});

		usuarios.subscribe(e -> log.info(e.toString()),
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
