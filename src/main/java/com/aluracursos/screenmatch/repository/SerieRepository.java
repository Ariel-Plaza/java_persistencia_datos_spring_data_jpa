package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//archivo para indicar que ocuparemos un CRUD
public interface SerieRepository extends JpaRepository<Serie,Long> {
    // USO Spring Data JPA
    //    busca titulo que contenga parte y ignora mayusculas
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);
    //query para top5
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    //busqueda por categoria
    List<Serie> findByGenero(Categoria categoria);

    //List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(int totalTemporadas, Double evaluacion);
    //USO Nativequerys en Spring Data JPA
//    @Query( value = "SELECT * FROM series WHERE series. total_temporadas <= 6 AND series. evaluacion >= 7.5", nativeQuery = true)
//    List<Serie> seriesPorTemporadaYEvaluacion();
    //USO JPQL es mas flexible y simple
    //s represetna la entidad que estoy trabajando, Serie nombre de la tabla, : especifica el valor pasado
    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemporadaYEvaluacion(int totalTemporadas, Double evaluacion);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5 ")
    List<Episodio> top5Episodios(Serie serie);
}
