package com.challengeliteratura.literatura.principal;

import com.challengeliteratura.literatura.model.*;
import com.challengeliteratura.literatura.repository.AutorRepository;
import com.challengeliteratura.literatura.repository.LibroRepository;
import com.challengeliteratura.literatura.service.ConsumoAPI;
import com.challengeliteratura.literatura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    //private final String URL_BASE = "http://gutendex.com/books";
    private final String URL_BASE = "http://gutendex.com/books/?search=dickens%20great";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepositorio;
    private AutorRepository autorRepositorio;
    public Principal(LibroRepository libroRepositorio, AutorRepository autorRepositorio) {
        this.libroRepositorio = libroRepositorio;
        this.autorRepositorio = autorRepositorio;
    }
    public void muestraElMenu() {

        String menu = """
                ********************* MENÚ **********************
                
                1- Buscar libro por titulo
                2- Listar libros registrados
                3- Listar autores registrados
                4- Listar autores vivos en un determinado año
                5- Listar libros por idiomas
                0- Salir
                *************************************************
                Seleccione una opción:
                """;
        int opcion=-1;
        while(opcion!=0){
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion){
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnAnio();
                    break;
                case 5:
                    listarLibrosPorIdiomas();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema");
                    break;
            }

        }
    }
    public void buscarLibroPorTitulo(){
        System.out.println("Ingrese el titulo del libro que desea buscar:");
        String titulo = teclado.nextLine();

        // Verificar si el libro ya está registrado
        List<Libro> libroExistente = libroRepositorio.findByTituloContainingIgnoreCase(titulo);

        if (!libroExistente.isEmpty()) {
            System.out.println("El libro con el título '" + titulo + "' ya está registrado.");
            return;
        }

        var json = consumoApi.obtenerDatos("http://gutendex.com/books/?search=%20"+titulo);
        //System.out.println(json);
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);
        Optional<DatosLibro> libroBuscado = datos.libros().stream()
                .findFirst();
        //System.out.println(libroBuscado);
        if(libroBuscado.isPresent()){
            System.out.println(libroBuscado.get());
            Libro libro = new Libro(libroBuscado.get());
            System.out.println(libro);
            libroRepositorio.save(libro);
        } else {
            System.out.println("No se encontró un libro con el título proporcionado.");
        }
    }
    public void listarLibrosRegistrados() {
        List<Libro> libros = libroRepositorio.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        int index = 1;
        for (Libro libro : libros) {
            System.out.println("Libro " + index);
            System.out.println("Titulo: " + libro.getTitulo());
            System.out.println("Autores: " + libro.getAutores().stream()
                    .map(Autor::getNombre)
                    .collect(Collectors.joining(", ")));
            System.out.println("Idiomas: " + libro.getIdioma());
            System.out.println("Numero de descargas: " + libro.getNumeroDescargas());
            System.out.println();
            index++;
        }
    }

    public void listarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
            return;
        }

        for (Autor autor : autores) {
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Fecha de nacimiento: " + autor.getAñoNacimiento());
            System.out.println("Fecha de fallecimiento: " + (autor.getAñofallecimiento() != null ? autor.getAñofallecimiento() : "N/A"));
            System.out.println("Libros: " + autor.getLibro().getTitulo());
            System.out.println();
        }
    }
    public void listarAutoresVivosEnAnio() {
        System.out.println("Ingrese el año para listar autores vivos:");
        int año = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores = autorRepositorio.findAll();
        List<Autor> autoresVivosEnAno = autores.stream()
                .filter(autor -> (autor.getAñoNacimiento() <= año &&
                        (autor.getAñofallecimiento() == null || autor.getAñofallecimiento() >= año)))
                .collect(Collectors.toList());

        if (autoresVivosEnAno.isEmpty()) {
            System.out.println("No hay autores vivos en el año " + año + ".");
            return;
        }

        for (Autor autor : autoresVivosEnAno) {
            System.out.println("Autor: " + autor.getNombre());
            System.out.println("Fecha de nacimiento: " + autor.getAñoNacimiento());
            System.out.println("Fecha de fallecimiento: " + (autor.getAñofallecimiento() != null ? autor.getAñofallecimiento() : "N/A"));
            System.out.println("Libros: " + autor.getLibro().getTitulo());
            System.out.println();
        }
    }
    public void listarLibrosPorIdiomas() {
        List<Libro> libros = libroRepositorio.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        // Obtener todos los idiomas disponibles en los libros registrados
        Set<Idioma> idiomasDisponibles = libros.stream()
                .map(Libro::getIdioma)
                .collect(Collectors.toSet());

        // Mostrar los idiomas disponibles al usuario
        System.out.println("Idiomas disponibles:");
        int index = 1;
        for (Idioma idioma : idiomasDisponibles) {
            System.out.println(index++ + ". " + idioma);
        }

        // Pedir al usuario que seleccione un idioma
        int opcion;
        Idioma idiomaSeleccionado = null;
        while (idiomaSeleccionado == null) {
            try {
                System.out.println("Seleccione el número del idioma que desea listar:");
                opcion = teclado.nextInt();
                teclado.nextLine(); // Consumir el salto de línea pendiente

                idiomaSeleccionado = idiomasDisponibles.stream()
                        .skip(opcion - 1)
                        .findFirst()
                        .orElse(null);

                if (idiomaSeleccionado == null) {
                    System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un número válido.");
                teclado.nextLine(); // Limpiar el buffer de entrada
            }
        }

        // Filtrar y mostrar los libros en el idioma seleccionado
        int i = 1;
        for (Libro libro : libros) {
            if (libro.getIdioma() == idiomaSeleccionado) {
                System.out.println("Libro " + i++);
                System.out.println("Titulo: " + libro.getTitulo());
                System.out.println("Autores: " + libro.getAutores().stream()
                        .map(Autor::getNombre)
                        .collect(Collectors.joining(", ")));
                System.out.println("Idioma: " + libro.getIdioma());
                System.out.println("Numero de descargas: " + libro.getNumeroDescargas());
                System.out.println();
            }
        }
    }
}
