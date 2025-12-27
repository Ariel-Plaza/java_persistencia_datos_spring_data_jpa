package com.aluracursos.screenmatch;

import com.aluracursos.screenmatch.principal.Principal;
import com.aluracursos.screenmatch.repository.SerieRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
    //Indica a spring que tiene que hacer una inyeccion de dependencias
    @Autowired
    private SerieRepository repository;

	public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")  // Busca en la raíz del proyecto
                .ignoreIfMissing()
                .load();

        // Imprime para verificar que se cargó (quita después)
        System.out.println("DB_HOST cargado: " + dotenv.get("DB_HOST"));

        // Cargar como propiedades del sistema
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//        indico que inyecte repository
		Principal principal = new Principal(repository);
		principal.muestraElMenu();
	}
}
