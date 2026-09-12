package com.inmobiliaria.model;

/** Relación 1:N -> una propiedad tiene muchas imágenes. */
public class ImagenPropiedad {

    private int idImagen;
    private int idPropiedad;
    private String urlImagen;
    private boolean esPortada;

    public int getIdImagen() { return idImagen; }
    public void setIdImagen(int idImagen) { this.idImagen = idImagen; }

    public int getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(int idPropiedad) { this.idPropiedad = idPropiedad; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }

    public boolean isEsPortada() { return esPortada; }
    public void setEsPortada(boolean esPortada) { this.esPortada = esPortada; }
}