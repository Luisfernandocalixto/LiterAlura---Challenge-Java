package com.alura.literatura.dto;

public class AutorLibroDTO {
    private String nombre;
    private int anioNacimiento;
    private int anioFallecimiento;
    private String titulo;
    private String idioma;
    private int descargas;

    // Constructores, getters y setters
    public AutorLibroDTO(String nombre, int anioNacimiento, int anioFallecimiento, String titulo, String idioma, int descargas) {
        this.nombre = nombre;
        this.anioNacimiento = anioNacimiento;
        this.anioFallecimiento = anioFallecimiento;
        this.titulo = titulo;
        this.idioma = idioma;
        this.descargas = descargas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(int anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public int getAnioFallecimiento() {
        return anioFallecimiento;
    }

    public void setAnioFallecimiento(int anioFallecimiento) {
        this.anioFallecimiento = anioFallecimiento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public int getDescargas() {
        return descargas;
    }

    public void setDescargas(int descargas) {
        this.descargas = descargas;
    }
}
