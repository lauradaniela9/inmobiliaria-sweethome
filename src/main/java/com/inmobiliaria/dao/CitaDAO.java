package com.inmobiliaria.dao;

import com.inmobiliaria.model.Cita;
import com.inmobiliaria.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acceso a datos de la tabla cita (agendamiento de visitas). */
public class CitaDAO {

    /** Se lanza cuando ya existe una cita para esa propiedad en ese horario (restricción UNIQUE). */
    public static class HorarioOcupadoException extends Exception {
        public HorarioOcupadoException(String mensaje) { super(mensaje); }
    }

    public void agendar(int idPropiedad, int idCliente, String fechaHora, String observacion)
            throws HorarioOcupadoException, SQLException {
        String sql = "INSERT INTO cita (id_propiedad, id_cliente, fecha_hora, observacion) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropiedad);
            ps.setInt(2, idCliente);
            ps.setString(3, fechaHora.replace('T', ' ') + (fechaHora.length() == 16 ? ":00" : ""));
            ps.setString(4, observacion);
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException dup) {
            throw new HorarioOcupadoException(
                    "Ya existe una cita agendada para esta propiedad en el horario seleccionado. Elige otro horario.");
        }
    }

    public List<Cita> listarPorCliente(int idCliente) throws SQLException {
        String sql =
            "SELECT c.id_cita, c.id_propiedad, c.fecha_hora, c.estado, c.observacion, p.titulo " +
            "FROM cita c INNER JOIN propiedad p ON p.id_propiedad = c.id_propiedad " +
            "WHERE c.id_cliente = ? ORDER BY c.fecha_hora DESC";
        List<Cita> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs, true));
            }
        }
        return lista;
    }

    /** Citas de todas las propiedades administradas por una inmobiliaria (para aprobar/rechazar). */
    public List<Cita> listarPorInmobiliaria(int idUsuarioInmobiliaria) throws SQLException {
        String sql =
            "SELECT c.id_cita, c.id_propiedad, c.fecha_hora, c.estado, c.observacion, p.titulo, " +
            "       pf.nombres, pf.apellidos, u.correo " +
            "FROM cita c " +
            "INNER JOIN propiedad p ON p.id_propiedad = c.id_propiedad " +
            "INNER JOIN usuario u ON u.id_usuario = c.id_cliente " +
            "LEFT JOIN perfil pf ON pf.id_usuario = c.id_cliente " +
            "WHERE p.id_usuario_inmobiliaria = ? " +
            "ORDER BY c.fecha_hora DESC";
        List<Cita> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioInmobiliaria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita c = mapear(rs, true);
                    String nombres = rs.getString("nombres");
                    String apellidos = rs.getString("apellidos");
                    c.setNombreClienteCompleto((nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : ""));
                    c.setCorreoCliente(rs.getString("correo"));
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    private Cita mapear(ResultSet rs, boolean incluirPropiedad) throws SQLException {
        Cita c = new Cita();
        c.setIdCita(rs.getInt("id_cita"));
        c.setIdPropiedad(rs.getInt("id_propiedad"));
        c.setEstado(rs.getString("estado"));
        c.setObservacion(rs.getString("observacion"));
        Timestamp fh = rs.getTimestamp("fecha_hora");
        if (fh != null) c.setFechaHora(fh.toString());
        if (incluirPropiedad) c.setTituloPropiedad(rs.getString("titulo"));
        return c;
    }

    /** Cambia el estado de una cita, validando que pertenezca a una propiedad de esa inmobiliaria. */
    public boolean cambiarEstado(int idCita, String nuevoEstado, int idUsuarioInmobiliaria) throws SQLException {
        String sql =
            "UPDATE cita c INNER JOIN propiedad p ON p.id_propiedad = c.id_propiedad " +
            "SET c.estado = ? WHERE c.id_cita = ? AND p.id_usuario_inmobiliaria = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);
            ps.setInt(3, idUsuarioInmobiliaria);
            return ps.executeUpdate() > 0;
        }
    }

    /** Cliente cancela su propia cita. */
    public boolean cancelar(int idCita, int idCliente) throws SQLException {
        String sql = "UPDATE cita SET estado = 'CANCELADA' WHERE id_cita = ? AND id_cliente = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCita);
            ps.setInt(2, idCliente);
            return ps.executeUpdate() > 0;
        }
    }

    /** Consulta obligatoria de agregación: total de citas agrupadas por estado (alimenta un reporte). */
    public List<Object[]> totalPorEstado() throws SQLException {
        String sql = "SELECT estado, COUNT(*) AS total FROM cita GROUP BY estado ORDER BY total DESC";
        List<Object[]> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Object[]{ rs.getString("estado"), rs.getLong("total") });
        }
        return lista;
    }
}