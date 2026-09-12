package com.inmobiliaria.model;

public class DocumentoSolicitud {

    private int idDocumento;
    private int idSolicitud;
    private String nombreArchivo;
    private String urlArchivo;
    private String fechaCarga;

    public int getIdDocumento() { return idDocumento; }
    public void setIdDocumento(int idDocumento) { this.idDocumento = idDocumento; }

    public int getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(int idSolicitud) { this.idSolicitud = idSolicitud; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }

    public String getFechaCarga() { return fechaCarga; }
    public void setFechaCarga(String fechaCarga) { this.fechaCarga = fechaCarga; }
}