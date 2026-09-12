package com.inmobiliaria.model;

/** Par id/nombre genérico para poblar combos de catálogos (ciudad, tipo_propiedad). */
public class Opcion {

    private int id;
    private String nombre;

    public Opcion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
}