package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=9da7ed5e";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Serie> series;
    Optional<Serie> serieBuscada;

    //Creacion de una lista en base a DatosSerie
    private List<DatosSerie> datosSeries = new ArrayList<>();
    // tipo de dato
    private SerieRepository repositorio;

    //    creo el constructor
    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por titulo
                    5 - TOP 5 mejores series
                    6 - Bucar serie por categoria
                    7 - filtrar series por temporadas y evaluación
                    8 - Buscar episodios por titulo
                    9 - Top 5 episodios por Serie
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
                case   4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriePorCategoria();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
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
//        DatosSerie datosSerie = getDatosSerie();
//        busca las series en la BD
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieresver los episodios");
        var nombreSerie = teclado.nextLine();
//      se ocupa optional, ya que puede existir o no una serie.
        Optional<Serie> serie = series.stream()
//                filtra solo las series que contengan parte del nombre
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
//        si la serie existe
        if (serie.isPresent()) {
//            se modifica variable en base a la BD
//            retorna la serie encontrada
            var serieEncontrada = serie.get();
//            Crea una lista en base a los DatosTemporadas
            List<DatosTemporadas> temporadas = new ArrayList<>();
//          realiza un bucle en base a la cantidad total de temporadas
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
//            para obtener la informacion completa de las temporadas
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
//            convierte de json a objeto java en base a DatosTemporadas
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
//            agrega la informacion de la temporada a la lista temporadas
                temporadas.add(datosTemporada);
            }
//        imprime la informacion de cada lista
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
//                    convertir cada dato de temporada a stream
                    .flatMap(d ->d.episodios().stream()
//                            convierte de datostemporada a episodio por el numero del episodio
                            .map(e->new Episodio(d.numero(), e)))
//                    crea una nueva lista
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }
    }
        private void buscarSerieWeb () {
            //obtiene los datos de la serie
            DatosSerie datos = getDatosSerie();
//        agrega los datos a la lista de series
//        datosSeries.add(datos);
//        Guardar en la BD, creamos instancia de serie
            Serie serie = new Serie(datos);
            //agrego los datos a la BD.
            repositorio.save(serie);
            System.out.println(datos);
        }

        private void mostrarSeriesBuscadas () {
//        muestra la lista de las series
//        datosSeries.forEach(System.out::println);
//       USO DE BD
//        List<Serie> series = repositorio.findAll();
//      Conversion a tipo Global
            series = repositorio.findAll();

//      Creacion de lista en base al dato Serie
//        List<Serie> series =  new ArrayList<>();
////        agrega datosSeries al array lo convierte en stream
//        series = datosSeries.stream()
////                transforma cada elemento DatoSerie en un objeto Serie(funcion lambda)
//                .map(d -> new Serie(d))
////                los agrupa en una lista
//                .collect(Collectors.toList());
//        toma  la lista y lo convierte en stream
            series.stream()
//                ordena por genero de serie
                    .sorted(Comparator.comparing(Serie::getGenero))
//                imprime las series
                    .forEach(System.out::println);
        }
    private void buscarSeriesPorTitulo(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
         serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if(serieBuscada.isPresent()){
            System.out.println("La serie buscada es: " + serieBuscada.get());
        }else{
            System.out.println("Serie no encontrada.");
        }

    };

    private void buscarTop5Series(){
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + "Evaluacion: " + s.getEvaluacion()));
    };

    private void  buscarSeriePorCategoria(){
        System.out.println("Escriba el genero/categoria de la serie que desea buscar");
        var genero = teclado.nextLine();
//        ttransformar genero en una categoria del Enum
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoria " + genero);
        seriePorCategoria.forEach(System.out::println);
    };

    public void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Con evaluación a partir de cuál valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }

    private void  buscarEpisodiosPorTitulo(){
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie: %s Temporada %s Episodio %s Evaluación %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));

    }

    private void buscarTop5Episodios() {
        buscarSeriesPorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Serie: %s - Temporada %s - Episodio %s - Evaluación %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));
        }
    }
}