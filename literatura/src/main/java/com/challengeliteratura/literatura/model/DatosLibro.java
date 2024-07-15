package com.challengeliteratura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("id") Long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Integer numeroDeDescargas) {
}


//@JsonAlias("title") String titulo,
//@JsonAlias("authors") List<DadosAutor> autores,
//@JsonAlias("languages") List<String> idiomas,
//@JsonAlias("copyright") String copyright,