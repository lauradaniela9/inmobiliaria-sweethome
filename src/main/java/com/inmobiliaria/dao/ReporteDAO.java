package com.inmobiliaria.dao;

import com.inmobiliaria.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Consultas consolidadas para el módulo de reportes. Concentra las
 * consultas obligatorias de agregación (GROUP BY / HAVING) exigidas
 * por el enunciado, además de reutilizar las de CitaDAO/SolicitudDAO.
 */
public class ReporteDAO {

    /** Reporte: propiedades disponibles agrupadas por ciudad y estado (GROUP BY + HAVING). */
    public List<Object[]> propiedadesPorCiudadYEstado() throws SQLException {
        String sql =
            "SELECT c.nombre AS ciudad, p.estado, COUNT(*) AS total " +
            "FROM propiedad p " +
            "INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad " +
            "GROUP BY c.nombre, p.estado " +
            "HAVING COUNT(*) > 0 " +
            "ORDER BY c.nombre, total DESC";
        return ejecutarTresColumnas(sql);
    }

    /** Reporte: valor total publicado por tipo de propiedad, solo tipos con más de una propiedad. */
    public List<Object[]> valorPromedioPorTipo() throws SQLException {
        String sql =
            "SELECT tp.nombre, COUNT(*) AS total_propiedades, ROUND(AVG(p.precio), 0) AS precio_promedio " +
            "FROM propiedad p " +
            "INNER JOIN tipo_propiedad tp ON tp.id_tipo_propiedad = p.id_tipo_propiedad " +
            "GROUP BY tp.nombre " +
            "HAVING COUNT(*) >= 1 " +
            "ORDER BY precio_promedio DESC";
        return ejecutarTresColumnas(sql);
    }

    /** Reporte por inmobiliaria: propiedades vendidas o arrendadas (ventas concretadas). */
    public List<Object[]> ventasArriendosPorInmobiliaria(int idUsuarioInmobiliaria) throws SQLException {
        String sql =
            "SELECT p.estado, COUNT(*) AS total " +
            "FROM propiedad p " +
            "WHERE p.id_usuario_inmobiliaria = ? AND p.estado IN ('VENDIDA','ARRENDADA') " +
            "GROUP BY p.estado";
        List<Object[]> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioInmobiliaria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(new Object[]{ rs.getString("estado"), rs.getLong("total") });
            }
        }
        return lista;
    }

    private List<Object[]> ejecutarTresColumnas(String sql) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{ rs.getObject(1), rs.getObject(2), rs.getObject(3) });
            }
        }
        return lista;
    }
}