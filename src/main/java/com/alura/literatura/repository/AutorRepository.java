package com.alura.literatura.repository;

import com.alura.literatura.dto.AutorLibroDTO;
import com.alura.literatura.model.Autor;
import com.alura.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    @Query("SELECT new com.alura.literatura.dto.AutorLibroDTO(a.nombre, a.anioNacimiento, a.anioFallecimiento, l.titulo, l.idioma, l.descargas) " +
            "FROM Libro l INNER JOIN l.autor a")
    List<AutorLibroDTO> autoresConLibro();

    @Query("SELECT l FROM Libro l JOIN FETCH l.autor")
    List<Libro> findAllWithAutor();

//    @Query("SELECT l FROM Libro l JOIN l.autor a WHERE a.anioNacimiento =:anioNacimiento")
//    List<Libro> autorPorDeterminadaFecha(int anioNacimiento);

    @Query("SELECT l FROM Libro l JOIN l.autor a WHERE a.anioNacimiento BETWEEN :anioNacimiento - 100 AND " +
            ":anioNacimiento + 100 ORDER BY a.anioNacimiento DESC")
    List<Libro> autorPorDeterminadaFecha(int anioNacimiento);


}
