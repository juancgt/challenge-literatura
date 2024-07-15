package com.challengeliteratura.literatura;

import com.challengeliteratura.literatura.principal.Principal;
import com.challengeliteratura.literatura.repository.LibroRepository;
import com.challengeliteratura.literatura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(libroRepository, autorRepository);
		principal.muestraElMenu();
		// Mantener la aplicación en ejecución
		synchronized (LiteraturaApplication.class) {
			LiteraturaApplication.class.wait();
		}
	}
}
