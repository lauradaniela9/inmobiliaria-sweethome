package com.inmobiliaria.dao;

import com.inmobiliaria.model.Perfil;
import com.inmobiliaria.util.ConexionBD;

import java.sql.*;

/** Acceso a datos de la tabla perfil (relación 1:1 con usuario). */
public class PerfilDAO {

    /** Se lanza cuando el documento de identidad ya pertenece a otro usuario (restricción UNIQUE). */
    public static class DocumentoDuplicadoException extends Exception {
        public DocumentoDuplicadoException(String mensaje) { super(mensaje); }
    }

    public Perfil buscarPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT id_usuario, nombres, apellidos, documento, telefono, direccion, foto " +
                     "FROM perfil WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Perfil perfil = new Perfil();
                    perfil.setIdUsuario(rs.getInt("id_usuario"));
                    perfil.setNombres(rs.getString("nombres"));
                    perfil.setApellidos(rs.getString("apellidos"));
                    perfil.setDocumento(rs.getString("documento"));
                    perfil.setTelefono(rs.getString("telefono"));
                    perfil.setDireccion(rs.getString("direccion"));
                    perfil.setFoto(rs.getString("foto"));
                    return perfil;
                }
            }
        }
        return null;
    }

    public void actualizar(Perfil perfil) throws DocumentoDuplicadoException, SQLException {
        String sql = "UPDATE perfil SET nombres=?, apellidos=?, documento=?, telefono=?, direccion=?, foto=? " +
                     "WHERE id_usuario=?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, perfil.getNombres());
            ps.setString(2, perfil.getApellidos());
            ps.setString(3, perfil.getDocumento());
            ps.setString(4, perfil.getTelefono());
            ps.setString(5, perfil.getDireccion());
            ps.setString(6, perfil.getFoto());
            ps.setInt(7, perfil.getIdUsuario());
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException dup) {
            throw new DocumentoDuplicadoException("El número de documento ya está registrado para otro usuario.");
        }
    }
}