package com.inmobiliaria.model;

import java.util.List;

public class Solicitud {

    private int idSolicitud;
    private int idPropiedad;
    private int idCliente;
    private String tipo;   // COMPRA, ARRIENDO
    private String estado; // EN_REVISION, APROBADA, RECHAZADA
    private String fechaSolicitud;

    // Derivados
    private String tituloPropiedad;
    private String nombreClienteCompleto;
    private String correoCliente;
    private List<DocumentoSolicitud> documentos;

    public int getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(int idSolicitud) { this.idSolicitud = idSolicitud; }

    public int getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(int idPropiedad) { this.idPropiedad = idPropiedad; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(String fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public String getTituloPropiedad() { return tituloPropiedad; }
    public void setTituloPropiedad(String tituloPropiedad) { this.tituloPropiedad = tituloPropiedad; }

    public String getNombreClienteCompleto() { return nombreClienteCompleto; }
    public void setNombreClienteCompleto(String nombreClienteCompleto) { this.nombreClienteCompleto = nombreClienteCompleto; }

    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }

    public List<DocumentoSolicitud> getDocumentos() { return documentos; }
    public void setDocumentos(List<DocumentoSolicitud> documentos) { this.documentos = documentos; }
}