package com.inmobiliaria.model;

import java.time.LocalDateTime;
import java.util.List;

public class Usuario {
    private int idUsuario;
    private String correo;
    private String contrasenaHash;
    private String estado;
    private int intentosFallidos;
    private LocalDateTime fechaBloqueo; // fecha/hora en que la cuenta pasó a BLOQUEADO (para el desbloqueo automático)
    private List<String> roles; // ADMINISTRADOR, INMOBILIARIA, CLIENTE
    private String nombres;
    private String apellidos;

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public LocalDateTime getFechaBloqueo() { return fechaBloqueo; }
    public void setFechaBloqueo(LocalDateTime fechaBloqueo) { this.fechaBloqueo = fechaBloqueo; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    /** Rol "principal" usado para decidir a qué dashboard redirigir tras el login. */
    public String getRolPrincipal() {
        if (roles == null || roles.isEmpty()) return null;
        if (roles.contains("ADMINISTRADOR")) return "ADMINISTRADOR";
        if (roles.contains("INMOBILIARIA")) return "INMOBILIARIA";
        return "CLIENTE";
    }
}