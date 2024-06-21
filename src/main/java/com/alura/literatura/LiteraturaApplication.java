package com.alura.literatura;

import com.alura.literatura.model.Autor;
import com.alura.literatura.principal.Principal;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

    @Autowired
    private LibroRepository repository;
    @Autowired
    private AutorRepository autorRepository;

    public static void main(String[] args) {
        SpringApplication.run(LiteraturaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(repository, autorRepository);
        principal.muestraElMenu();
    }


}
