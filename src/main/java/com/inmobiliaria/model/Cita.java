package com.inmobiliaria.model;

public class Cita {

    private int idCita;
    private int idPropiedad;
    private int idCliente;
    private String fechaHora; // formato 'yyyy-MM-dd''T''HH:mm' (input datetime-local)
    private String estado;    // PENDIENTE, CONFIRMADA, RECHAZADA, REALIZADA, CANCELADA
    private String observacion;

    // Campos derivados para listados (JOIN con propiedad / usuario)
    private String tituloPropiedad;
    private String nombreClienteCompleto;
    private String correoCliente;

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public int getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(int idPropiedad) { this.idPropiedad = idPropiedad; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getTituloPropiedad() { return tituloPropiedad; }
    public void setTituloPropiedad(String tituloPropiedad) { this.tituloPropiedad = tituloPropiedad; }

    public String getNombreClienteCompleto() { return nombreClienteCompleto; }
    public void setNombreClienteCompleto(String nombreClienteCompleto) { this.nombreClienteCompleto = nombreClienteCompleto; }

    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
}