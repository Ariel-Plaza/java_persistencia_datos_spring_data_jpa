package com.aluracursos.screenmatch.model;
import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;
//Indico que esta clase se utilizara como tabla en a BD
@Entity
//puedo definir el nombre de la tabla
@Table(name = "series")
public class Serie {
//    defino el id,
    @Id
//    forma en como se gestiona el id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
//    que campo no se repita
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double evaluacion;
    private String poster;
//    Indicamos que valor es un enumerated
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String actores;
    private String sinopsis;
    private String pais;
//no realiza relacion
//    @Transient
//Relacion
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios;

//    Constructor predeterminado requerido por JPA
    public Serie(){}
//Constructor de Serie
//    se basa en DatosSerie
    public Serie(DatosSerie datosSerie){
        this.titulo = datosSerie.titulo();
        this.totalTemporadas = datosSerie.totalTemporadas();
//        transformacion de la evaluacion de datosSerie
        //tiene la opcion de si no tiene valor envia por defecto 0
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse ( 0);
        this.poster = datosSerie.poster();
//      Corta la cantidad de categoria en la , y deja solo una
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0]);
        this.actores = datosSerie.actores();
        this.sinopsis = datosSerie.sinopsis();
        this.pais = datosSerie.pais();
    }

    @Override
    public String toString() {
        return
                "genero=" + genero +
                " titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", evaluacion=" + evaluacion +
                ", poster='" + poster + '\'' +
                ", actores='" + actores + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                ", pais='" + pais + '\''+
                ", episodios='" + episodios + '\'' ;
    }

    public Long getId() {
        return Id;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        //para cada uno de los episodios setiar el valor de la misma serie
        episodios.forEach(e ->e.setSerie(this));
        this.episodios = episodios;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }
}