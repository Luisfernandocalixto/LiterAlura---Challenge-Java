package com.alura.literatura.principal;

import com.alura.literatura.model.*;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import com.alura.literatura.service.ConsumoApi;
import com.alura.literatura.service.ConvierteDatos;


import java.util.*;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repositorio;
    private AutorRepository autorRepository;


    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.repositorio = repository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;

        while (opcion != 0) {
            var menu = """
                    Elija ingresando el número de opción:
                    1- Buscar libro por título
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    O- Salir de la aplicación
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Cerrando aplicación");
                    break;
                case 1:
                    System.out.println("Escribe el nombre del libro que desea buscar:");
                    var nombreLibro = teclado.nextLine();
                    buscarLibroWeb(nombreLibro);
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresRegistrados();
                    break;
                case 4:
                    mostrarAutoresPorFecha();
                    break;
                case 5:
                    mostrarLibrosPorIdioma();
                    break;
                default:
                    System.out.println("Ups! Opción no válida");
            }
        }
    }

    private ResultadoBusqueda getDatosLibro(String nombreLibro) {
//        Buscar los datos generales de los libros
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        var resultadoBusqueda = conversor.obtenerDatos(json, ResultadoBusqueda.class);
        return resultadoBusqueda;
    }

    private void buscarLibroWeb(String tituloBuscado) {
        ResultadoBusqueda resultadoBusqueda = getDatosLibro(tituloBuscado);
        List<DatosLibro> datosLibros = resultadoBusqueda.getResults();

        boolean libroEncontrado = false;

        for (DatosLibro datosLibro : datosLibros) {
            String titulo = datosLibro.titulo();
            if (titulo.equalsIgnoreCase(tituloBuscado)) {
                libroEncontrado = true;

                DatosAutor datosAutor = datosLibro.autores().get(0);
                String nombreAutor = datosAutor.nombre();
                int anioNacimiento = datosAutor.anioNacimiento();
                int anioFallecimiento = datosAutor.anioFallecimiento();
                String idioma = datosLibro.idiomas().get(0);

                Optional<Autor> autorExistente = autorRepository.findByNombre(nombreAutor);
                Autor autorLibro;
                if (autorExistente.isPresent()) {
                    autorLibro = autorExistente.get();
                } else {
                    Autor nuevoAutor = new Autor();
                    nuevoAutor.setNombre(nombreAutor);
                    nuevoAutor.setAnioNacimiento(anioNacimiento);
                    nuevoAutor.setAnioFallecimiento(anioFallecimiento);
                    autorLibro = autorRepository.save(nuevoAutor);
                }

                Optional<Libro> libroExistente = repositorio.findByTituloAndAutorAndIdioma(titulo, autorLibro, idioma);
                if (libroExistente.isPresent()) {
                    System.out.println("\nEl libro: " + titulo + " ya existe!");
                    break;
                }

                Libro nuevoLibro = new Libro();
                nuevoLibro.setTitulo(titulo);
                nuevoLibro.setIdioma(idioma);
                nuevoLibro.setDescargas(datosLibro.descargas());
                nuevoLibro.setAutor(autorLibro);

                autorLibro.getLibros().add(nuevoLibro);

                repositorio.save(nuevoLibro);
                System.out.println("*** Libro ***" + "\n"
                        + "Título: " + nuevoLibro.getTitulo() + "\n"
                        + "Autor: " + nuevoLibro.getAutor().getNombre() + "\n"
                        + "Idioma: " + nuevoLibro.getIdioma() + "\n"
                        + "Número de descargas: " + nuevoLibro.getDescargas() + "\n"
                );
            }
        }

        if (!libroEncontrado) {
            System.out.println("\nUps! No se encontro el libro: " + tituloBuscado + ".");
        }
    }

    private void mostrarLibrosBuscados() {
        List<Libro> libros = repositorio.findAll();
        System.out.println("\n*** Lista de Libros ***");
        libros.stream()
                .forEach(l ->
                        System.out.println(
                                "\nTítulo: " + l.getTitulo() + "\n" +
                                        "Autor: " + l.getAutor().getNombre() + "\n" +
                                        "Idioma: " + l.getIdioma() + "\n" +
                                        "Número de descargas: " + l.getDescargas() + "\n"
                        )
                );
        System.out.println("***********************");

    }

    private void mostrarAutoresRegistrados() {
        List<Libro> filtroAutores = autorRepository.findAllWithAutor();
        System.out.println("\n*** Autores registrados ***");
        filtroAutores.stream()
                .forEach(s ->
                        System.out.println("\nAutor: " + s.getAutor().getNombre() + "\n"
                                + " Fecha de Nacimiento: " + s.getAutor().getAnioNacimiento() + "\n"
                                + " Fecha de Fallecimiento: " + s.getAutor().getAnioFallecimiento() + "\n"
                                + " Libros: " + s.getTitulo() + "\n"
                                + " Idioma: " + s.getIdioma() + "\n"
                                + " Número de descargas: " + s.getDescargas() + "\n"
                        ));
        System.out.println("***********************");


    }

    private void mostrarAutoresPorFecha() {
        System.out.println("Escribe el año de fecha de autor que desea buscar");
        var aniofecha = teclado.nextInt();
        List<Libro> filtraPorFecha = autorRepository.autorPorDeterminadaFecha(aniofecha);
        if (filtraPorFecha.isEmpty()) {
            System.out.println("Parece que no hay autor con el año proporcionado: " + aniofecha);
        } else {
            System.out.println("\n*** Autor(es) por el año proporcionado ***");
            filtraPorFecha.forEach(a ->
                    System.out.println("\nAutor: " + a.getAutor().getNombre() + "\n"
                            + " Fecha de Nacimiento: " + a.getAutor().getAnioNacimiento() + "\n"
                            + " Fecha de Fallecimiento: " + a.getAutor().getAnioFallecimiento() + "\n"
                            + " Libros: " + a.getTitulo() + "\n"
                    ));
            System.out.println("*******************************************");
        }

    }

    private void mostrarLibrosPorIdioma() {
        System.out.println("\nEscribe el sufijo del idioma deseado para mostrar los libros que desea buscar");
        var menuDeIdiomas = """
                es- español
                en- inglés
                fr- francés
                pt- portugués
                """;
        System.out.println(menuDeIdiomas);
        var idioma = teclado.nextLine();
        List<Libro> filtrarLibrosPorIdioma = repositorio.obtenerLibrosPorIdioma(idioma);
        if (filtrarLibrosPorIdioma.isEmpty()) {
            System.out.println("Parace que no hay libros con ese idioma por el momento!");
        } else {
            System.out.println("\n*** Lista de Libros Por Idioma ***");
            filtrarLibrosPorIdioma.forEach(l ->
                    System.out.println(
                            "\nTítulo: " + l.getTitulo() + "\n" +
                                    "Autor: " + l.getAutor().getNombre() + "\n" +
                                    "Idioma: " + l.getIdioma() + "\n" +
                                    "Número de descargas: " + l.getDescargas() + "\n"
                    )
            );
            System.out.println("*******************************************");

        }
    }


}
