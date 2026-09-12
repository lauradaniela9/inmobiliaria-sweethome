package com.inmobiliaria.dao;

import com.inmobiliaria.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Acceso a datos para el módulo de administración de usuarios y roles
 * (Historia de usuario 4: "Como administrador, quiero asignar y revocar
 * roles a los usuarios para controlar los permisos de la aplicación").
 */
public class UsuarioAdminDAO {

    public static class VistaUsuario {
        private int idUsuario;
        private String correo;
        private String estado;
        private String nombres;
        private String apellidos;
        private List<String> roles = new ArrayList<>();

        public int getIdUsuario() { return idUsuario; }
        public String getCorreo() { return correo; }
        public String getEstado() { return estado; }
        public String getNombres() { return nombres; }
        public String getApellidos() { return apellidos; }
        public List<String> getRoles() { return roles; }
    }

    /** Listado de todos los usuarios con sus roles (INNER/LEFT JOIN usuario + perfil + rol). */
    public List<VistaUsuario> listarUsuarios() throws SQLException {
        String sql =
            "SELECT u.id_usuario, u.correo, u.estado, p.nombres, p.apellidos, r.nombre_rol " +
            "FROM usuario u " +
            "LEFT JOIN perfil p ON p.id_usuario = u.id_usuario " +
            "LEFT JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario " +
            "LEFT JOIN rol r ON r.id_rol = ur.id_rol " +
            "ORDER BY u.id_usuario";

        Map<Integer, VistaUsuario> mapa = new LinkedHashMap<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                VistaUsuario vu = mapa.get(id);
                if (vu == null) {
                    vu = new VistaUsuario();
                    vu.idUsuario = id;
                    vu.correo = rs.getString("correo");
                    vu.estado = rs.getString("estado");
                    vu.nombres = rs.getString("nombres");
                    vu.apellidos = rs.getString("apellidos");
                    mapa.put(id, vu);
                }
                String rol = rs.getString("nombre_rol");
                if (rol != null) vu.roles.add(rol);
            }
        }
        return new ArrayList<>(mapa.values());
    }

    /** Cambia el estado de la cuenta (ACTIVO / INACTIVO / BLOQUEADO). */
    public void cambiarEstado(int idUsuario, String nuevoEstado) throws SQLException {
        String sql = "UPDATE usuario SET estado = ?, intentos_fallidos = 0 WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }

    /** Asigna un rol a un usuario (idempotente: ignora si ya lo tiene, gracias a la PK compuesta). */
    public void asignarRol(int idUsuario, String nombreRol) throws SQLException {
        String sql = "INSERT IGNORE INTO usuario_rol (id_usuario, id_rol) " +
                     "SELECT ?, id_rol FROM rol WHERE nombre_rol = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, nombreRol);
            ps.executeUpdate();
        }
    }

    /** Revoca un rol de un usuario. */
    public void revocarRol(int idUsuario, String nombreRol) throws SQLException {
        String sql = "DELETE ur FROM usuario_rol ur " +
                     "INNER JOIN rol r ON r.id_rol = ur.id_rol " +
                     "WHERE ur.id_usuario = ? AND r.nombre_rol = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, nombreRol);
            ps.executeUpdate();
        }
    }

    public List<String> listarNombresRoles() throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre_rol FROM rol ORDER BY nombre_rol";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(rs.getString(1));
        }
        return lista;
    }
}