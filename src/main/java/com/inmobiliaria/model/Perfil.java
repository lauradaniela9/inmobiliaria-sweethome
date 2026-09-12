package com.inmobiliaria.model;

/** Relación 1:1 con usuario: datos personales que NO son credenciales de ingreso. */
public class Perfil {

    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String documento;
    private String telefono;
    private String direccion;
    private String foto;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}