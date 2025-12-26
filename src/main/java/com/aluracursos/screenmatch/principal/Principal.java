package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=9da7ed5e";
    private ConvierteDatos conversor = new ConvierteDatos();

    //Creacion de una lista en base a DatosSerie
    private List<DatosSerie> datosSeries = new ArrayList<>();

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }



    private DatosSerie getDatosSerie() {
//        muestra informacion por pantalla
        System.out.println("Escribe el nombre de la serie que deseas buscar");
//        guarda nombre de la sere ingresada por usuario
        var nombreSerie = teclado.nextLine();
//        crea variable para guardar el consumo dela API en base a el nombre de la serie
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
//        Imprime datos obtenidos
        System.out.println(json);
//        convierte el json a un objeto java en base a los datosSerie
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
//        obtiene los datos de la serie a travez de la funcion y los guarda como un tipo de dato
//        DatosSerie
        DatosSerie datosSerie = getDatosSerie();
//        Crea una lista en base a los DatosTemporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();
//      realiza un bucle en base a la cantidad total de temporadas
        for (int i = 1; i <= datosSerie.totalTemporadas(); i++) {
//            para obtener la informacion completa de las temporadas
            var json = consumoApi.obtenerDatos(URL_BASE + datosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
//            convierte de json a objeto java en base a DatosTemporadas
            DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
//            agrega la informacion de la temporada a la lista temporadas
            temporadas.add(datosTemporada);
        }
//        imprime la informacion de cada lista
        temporadas.forEach(System.out::println);
    }
    private void buscarSerieWeb() {
    //obtiene los datos de la serie
        DatosSerie datos = getDatosSerie();
//        agrega los datos a la lista de series
        datosSeries.add(datos);
        System.out.println(datos);

    }

    private void mostrarSeriesBuscadas() {
//        muestra la lista de las series
//        datosSeries.forEach(System.out::println);
//    Creacion de lista en base al dato Serie
        List<Serie> series =  new ArrayList<>();
//        agrega datosSeries al array lo convierte en stream
        series = datosSeries.stream()
//                transforma cada elemento DatoSerie en un objeto Serie(funcion lambda)
                .map(d -> new Serie(d))
//                los agrupa en una lista
                .collect(Collectors.toList());
//        toma  la lista y lo convierte en stream
        series.stream()
//                ordena por genero de serie
                .sorted(Comparator.comparing(Serie::getGenero))
//                imprime las series
                .forEach(System.out::println);

    }

}

