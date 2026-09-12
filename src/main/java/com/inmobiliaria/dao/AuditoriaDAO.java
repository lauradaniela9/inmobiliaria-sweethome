package com.inmobiliaria.dao;

import com.inmobiliaria.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Consulta de los eventos registrados en la tabla auditoria, para el panel del administrador. */
public class AuditoriaDAO {

    public static class EventoAuditoria {
        private String correo;
        private String accion;
        private String fecha;
        private String ip;

        public String getCorreo() { return correo; }
        public String getAccion() { return accion; }
        public String getFecha() { return fecha; }
        public String getIp() { return ip; }
    }

    /** Últimos eventos de auditoría (LEFT JOIN: algunos eventos, como registros fallidos, pueden no tener usuario). */
    public List<EventoAuditoria> listarUltimos(int limite) throws SQLException {
        String sql =
            "SELECT u.correo, a.accion, a.fecha_evento, a.ip_origen " +
            "FROM auditoria a " +
            "LEFT JOIN usuario u ON u.id_usuario = a.id_usuario " +
            "ORDER BY a.fecha_evento DESC " +
            "LIMIT ?";
        List<EventoAuditoria> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EventoAuditoria e = new EventoAuditoria();
                    e.correo = rs.getString("correo") != null ? rs.getString("correo") : "(usuario eliminado)";
                    e.accion = rs.getString("accion");
                    Timestamp fecha = rs.getTimestamp("fecha_evento");
                    e.fecha = fecha != null ? fecha.toString() : "";
                    e.ip = rs.getString("ip_origen");
                    lista.add(e);
                }
            }
        }
        return lista;
    }
}