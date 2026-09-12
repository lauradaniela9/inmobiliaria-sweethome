package com.inmobiliaria.dao;

import com.inmobiliaria.model.Propiedad;
import com.inmobiliaria.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acceso a datos de la tabla favorito (PK compuesta cliente/propiedad). */
public class FavoritoDAO {

    public boolean esFavorito(int idCliente, int idPropiedad) throws SQLException {
        String sql = "SELECT 1 FROM favorito WHERE id_cliente = ? AND id_propiedad = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.setInt(2, idPropiedad);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Alterna el estado de favorito (si existe lo quita, si no existe lo agrega). Retorna el nuevo estado. */
    public boolean alternar(int idCliente, int idPropiedad) throws SQLException {
        if (esFavorito(idCliente, idPropiedad)) {
            String sql = "DELETE FROM favorito WHERE id_cliente = ? AND id_propiedad = ?";
            try (Connection con = ConexionBD.obtenerConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idCliente);
                ps.setInt(2, idPropiedad);
                ps.executeUpdate();
            }
            return false;
        } else {
            String sql = "INSERT INTO favorito (id_cliente, id_propiedad) VALUES (?, ?)";
            try (Connection con = ConexionBD.obtenerConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idCliente);
                ps.setInt(2, idPropiedad);
                ps.executeUpdate();
            }
            return true;
        }
    }

    public List<Propiedad> listarPorCliente(int idCliente) throws SQLException {
        String sql =
            "SELECT p.id_propiedad, p.titulo, p.precio, p.estado, c.nombre AS nombre_ciudad, " +
            "       tp.nombre AS nombre_tipo, f.fecha_marcado " +
            "FROM favorito f " +
            "INNER JOIN propiedad p ON p.id_propiedad = f.id_propiedad " +
            "INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad " +
            "INNER JOIN tipo_propiedad tp ON tp.id_tipo_propiedad = p.id_tipo_propiedad " +
            "WHERE f.id_cliente = ? " +
            "ORDER BY f.fecha_marcado DESC";
        List<Propiedad> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Propiedad p = new Propiedad();
                    p.setIdPropiedad(rs.getInt("id_propiedad"));
                    p.setTitulo(rs.getString("titulo"));
                    p.setPrecio(rs.getBigDecimal("precio"));
                    p.setEstado(rs.getString("estado"));
                    p.setNombreCiudad(rs.getString("nombre_ciudad"));
                    p.setNombreTipoPropiedad(rs.getString("nombre_tipo"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}