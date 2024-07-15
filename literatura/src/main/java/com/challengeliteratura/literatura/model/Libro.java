package com.challengeliteratura.literatura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    private Long Id;
    @Column(nullable = false, unique = true)
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private Integer numeroDescargas;
    //@ManyToOne
    //private Autor autores;
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;
    public Libro(){}
    public Libro(DatosLibro datosLibro){
        this.Id=datosLibro.id();
        this.titulo = datosLibro.titulo();
        this.idioma = Idioma.fromString(datosLibro.idiomas().stream()
                .limit(1).collect(Collectors.joining()));
        this.numeroDescargas = datosLibro.numeroDeDescargas();
        this.autores = datosLibro.autores().stream()
                .map(datosAutor -> {
                    Autor autor = new Autor(datosAutor);
                    autor.setLibro(this); // Establece la relación bidireccional
                    return autor;
                })
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Libro{" +
                "Id=" + Id +
                ", titulo='" + titulo + '\'' +
                ", idioma=" + idioma +
                ", numeroDescargas=" + numeroDescargas +
                ", autores=" + autores +
                '}';
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }
}
