package com.inmobiliaria.dao;

import com.inmobiliaria.model.DocumentoSolicitud;
import com.inmobiliaria.model.Solicitud;
import com.inmobiliaria.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de solicitud y documento_solicitud (radicación de
 * compra/arriendo).
 */
public class SolicitudDAO {

	public int crear(int idPropiedad, int idCliente, String tipo) throws SQLException {
		String sql = "INSERT INTO solicitud (id_propiedad, id_cliente, tipo) VALUES (?, ?, ?)";
		try (Connection con = ConexionBD.obtenerConexion();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, idPropiedad);
			ps.setInt(2, idCliente);
			ps.setString(3, tipo);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				rs.next();
				return rs.getInt(1);
			}
		}
	}

	public void agregarDocumento(int idSolicitud, String nombreArchivo, String urlArchivo) throws SQLException {
		String sql = "INSERT INTO documento_solicitud (id_solicitud, nombre_archivo, url_archivo) VALUES (?, ?, ?)";
		try (Connection con = ConexionBD.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, idSolicitud);
			ps.setString(2, nombreArchivo);
			ps.setString(3, urlArchivo);
			ps.executeUpdate();
		}
	}

	public List<Solicitud> listarPorCliente(int idCliente) throws SQLException {
		String sql = "SELECT s.id_solicitud, s.id_propiedad, s.tipo, s.estado, s.fecha_solicitud, p.titulo "
				+ "FROM solicitud s INNER JOIN propiedad p ON p.id_propiedad = s.id_propiedad "
				+ "WHERE s.id_cliente = ? ORDER BY s.fecha_solicitud DESC";
		List<Solicitud> lista = new ArrayList<>();
		try (Connection con = ConexionBD.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, idCliente);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					lista.add(mapear(rs));
			}
		}
		for (Solicitud s : lista)
			s.setDocumentos(listarDocumentos(s.getIdSolicitud()));
		return lista;
	}

	/**
	 * Solicitudes recibidas por una inmobiliaria sobre cualquiera de sus
	 * propiedades.
	 */
	public List<Solicitud> listarPorInmobiliaria(int idUsuarioInmobiliaria) throws SQLException {
		String sql = "SELECT s.id_solicitud, s.id_propiedad, s.tipo, s.estado, s.fecha_solicitud, p.titulo, "
				+ "       pf.nombres, pf.apellidos, u.correo " + "FROM solicitud s "
				+ "INNER JOIN propiedad p ON p.id_propiedad = s.id_propiedad "
				+ "INNER JOIN usuario u ON u.id_usuario = s.id_cliente "
				+ "LEFT JOIN perfil pf ON pf.id_usuario = s.id_cliente " + "WHERE p.id_usuario_inmobiliaria = ? "
				+ "ORDER BY s.fecha_solicitud DESC";
		List<Solicitud> lista = new ArrayList<>();
		try (Connection con = ConexionBD.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, idUsuarioInmobiliaria);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Solicitud s = mapear(rs);
					String nombres = rs.getString("nombres");
					String apellidos = rs.getString("apellidos");
					s.setNombreClienteCompleto(
							(nombres != null ? nombres : "") + " " + (apellidos != null ? apellidos : ""));
					s.setCorreoCliente(rs.getString("correo"));
					lista.add(s);
				}
			}
		}
		for (Solicitud s : lista)
			s.setDocumentos(listarDocumentos(s.getIdSolicitud()));
		return lista;
	}

	public List<DocumentoSolicitud> listarDocumentos(int idSolicitud) throws SQLException {
		String sql = "SELECT id_documento, id_solicitud, nombre_archivo, url_archivo, fecha_carga "
				+ "FROM documento_solicitud WHERE id_solicitud = ? ORDER BY fecha_carga";
		List<DocumentoSolicitud> lista = new ArrayList<>();
		try (Connection con = ConexionBD.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, idSolicitud);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					DocumentoSolicitud d = new DocumentoSolicitud();
					d.setIdDocumento(rs.getInt("id_documento"));
					d.setIdSolicitud(rs.getInt("id_solicitud"));
					d.setNombreArchivo(rs.getString("nombre_archivo"));
					d.setUrlArchivo(rs.getString("url_archivo"));
					Timestamp fc = rs.getTimestamp("fecha_carga");
					if (fc != null)
						d.setFechaCarga(fc.toString());
					lista.add(d);
				}
			}
		}
		return lista;
	}

	/**
	 * La inmobiliaria aprueba o rechaza, validando que la solicitud sea sobre una
	 * propiedad propia.
	 */
	public boolean cambiarEstado(int idSolicitud, String nuevoEstado, int idUsuarioInmobiliaria) throws SQLException {
		String sql = "UPDATE solicitud s INNER JOIN propiedad p ON p.id_propiedad = s.id_propiedad "
				+ "SET s.estado = ? WHERE s.id_solicitud = ? AND p.id_usuario_inmobiliaria = ?";
		try (Connection con = ConexionBD.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nuevoEstado);
			ps.setInt(2, idSolicitud);
			ps.setInt(3, idUsuarioInmobiliaria);
			return ps.executeUpdate() > 0;
		}
	}

	/**
	 * Verifica que la solicitud pertenezca al cliente autenticado (para permitirle
	 * subir documentos).
	 */
	public boolean perteneceACliente(int idSolicitud, int idCliente) throws SQLException {
		String sql = "SELECT 1 FROM solicitud WHERE id_solicitud = ? AND id_cliente = ?";
		try (Connection con = ConexionBD.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, idSolicitud);
			ps.setInt(2, idCliente);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	private Solicitud mapear(ResultSet rs) throws SQLException {
		Solicitud s = new Solicitud();
		s.setIdSolicitud(rs.getInt("id_solicitud"));
		s.setIdPropiedad(rs.getInt("id_propiedad"));
		s.setTipo(rs.getString("tipo"));
		s.setEstado(rs.getString("estado"));
		Timestamp fecha = rs.getTimestamp("fecha_solicitud");
		if (fecha != null)
			s.setFechaSolicitud(fecha.toString());
		s.setTituloPropiedad(rs.getString("titulo"));
		return s;
	}

	/**
	 * Consulta obligatoria de agregación: solicitudes por inmobiliaria, con HAVING
	 * para destacar las más activas.
	 */
	public List<Object[]> totalPorInmobiliaria() throws SQLException {
		String sql = "SELECT im.nombre_comercial, COUNT(*) AS total " + "FROM solicitud s "
				+ "INNER JOIN propiedad p ON p.id_propiedad = s.id_propiedad "
				+ "INNER JOIN inmobiliaria im ON im.id_usuario = p.id_usuario_inmobiliaria "
				+ "GROUP BY im.nombre_comercial " + "HAVING COUNT(*) >= 1 " + "ORDER BY total DESC";
		List<Object[]> lista = new ArrayList<>();
		try (Connection con = ConexionBD.obtenerConexion();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				lista.add(new Object[] { rs.getString("nombre_comercial"), rs.getLong("total") });
		}
		return lista;
	}
}