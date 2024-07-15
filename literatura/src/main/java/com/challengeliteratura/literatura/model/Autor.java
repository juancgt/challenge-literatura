package com.challengeliteratura.literatura.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String nombre;

    private Integer añoDeNacimiento;
    private Integer añoDeFallecimiento;

    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    public Autor(){}

    public Autor(DatosAutor datosAutor){
        this.nombre= datosAutor.nombre();
        this.añoDeNacimiento = datosAutor.añoDeNacimiento();
        this.añoDeFallecimiento = datosAutor.añoDeFallecimiento();
        /*try{
            this.añoDeNacimiento = LocalDate.parse(datosAutor.añoDeNacimiento());
        }catch (DateTimeParseException e){
            this.añoDeNacimiento = null;
        }
        try {
            this.añoDeFallecimiento = LocalDate.parse(datosAutor.añoDeFallecimiento());
        }catch (DateTimeParseException e){
            this.añoDeFallecimiento = null;
        }*/

    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAñoNacimiento() {
        return añoDeNacimiento;
    }

    public void setAñoNacimiento(Integer añoNacimiento) {
        this.añoDeNacimiento = añoNacimiento;
    }

    public Integer getAñofallecimiento() {
        return añoDeFallecimiento;
    }

    public void setAñofallecimiento(Integer añofallecimiento) {
        this.añoDeFallecimiento = añofallecimiento;
    }

    public Libro getLibro() {
        return libro;
    }



    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    @Override
    public String toString() {
        return "Autor{" +
                "Id=" + Id +
                ", nombre='" + nombre + '\'' +
                ", añoDeNacimiento=" + añoDeNacimiento +
                ", añoDeFallecimiento=" + añoDeFallecimiento +
                '}';
    }
}
