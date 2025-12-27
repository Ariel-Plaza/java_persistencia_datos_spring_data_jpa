package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
//archivo para indicar que ocuparemos un CRUD
public interface SerieRepository extends JpaRepository<Serie,Long> {
}
