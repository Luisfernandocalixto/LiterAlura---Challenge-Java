package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultadoBusqueda {
    private int count;
    private String next;
    private String previuos;
    private List<DatosLibro> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPreviuos() {
        return previuos;
    }

    public void setPreviuos(String previuos) {
        this.previuos = previuos;
    }

    public List<DatosLibro> getResults() {
        return results;
    }

    public void setResults(List<DatosLibro> results) {
        this.results = results;
    }
}
