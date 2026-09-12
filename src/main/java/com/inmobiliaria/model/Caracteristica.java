package com.inmobiliaria.model;

/** Catálogo de características (Piscina, Parqueadero, Ascensor...). Relación N:M con propiedad. */
public class Caracteristica {

    private int idCaracteristica;
    private String nombre;
    private int cantidad; // solo se usa cuando viene asociada a una propiedad concreta
    private boolean seleccionada; // usado en formularios (checkbox marcado o no)

    public Caracteristica() { }

    public Caracteristica(int idCaracteristica, String nombre) {
        this.idCaracteristica = idCaracteristica;
        this.nombre = nombre;
    }

    public int getIdCaracteristica() { return idCaracteristica; }
    public void setIdCaracteristica(int idCaracteristica) { this.idCaracteristica = idCaracteristica; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public boolean isSeleccionada() { return seleccionada; }
    public void setSeleccionada(boolean seleccionada) { this.seleccionada = seleccionada; }
}