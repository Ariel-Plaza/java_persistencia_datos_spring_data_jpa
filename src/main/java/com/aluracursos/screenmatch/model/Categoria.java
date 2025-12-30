package com.aluracursos.screenmatch.model;
//Es un tipo de dato especial que representa un conjunto fijo y
// limitado de valores constantes relacionados.
// Es como una lista cerrada de opciones posibles.

public enum Categoria {
//    categoria equivale a categoria de la API
    ACCION("Action", "Acción"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    DRAMA("Drama", "Drama"),
    CRIMEN("Crime", "Crimen"),
    DESCONOCIDO("Unknown", "Desconocido");


//tipo de dato
    private String categoriaOmdb;


    private String categoriaEspanol;
    Categoria (String categoriaOmdb, String categoriaEspanol) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEspanol = categoriaEspanol;
    }
//Constructor categoria
    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }

//convierte un String en un valor del enum Categoira
    public static Categoria fromString(String text) {

        //      itera sobre todos los valores posibles del enum Categoria,devuelve un array
//        con todos los valores del enum
        for (Categoria categoria : Categoria.values()) {
//            compara categoriaOmdb de cada enum, con el texto ignora , y Mayus
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
//                si encuentra coincidencia lo devuelve
                return categoria;
            }
        }
//        Si no coincide envia mensaje
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

    public static Categoria fromEspanol(String text) {
        //      itera sobre todos los valores posibles del enum Categoria,devuelve un array
    //        con todos los valores del enum
        for (Categoria categoria : Categoria.values()) {
    //            compara categoriaOmdb de cada enum, con el texto ignora , y Mayus
            if (categoria.categoriaEspanol.equalsIgnoreCase(text)) {
    //                si encuentra coincidencia lo devuelve
                return categoria;
            }
        }
    //        Si no coincide envia mensaje
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}



