package com.inmobiliaria.dao;

import com.inmobiliaria.model.Usuario;
import com.inmobiliaria.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad usuario (y sus relaciones perfil / roles).
 * Toda excepción SQL se traduce a una excepción de negocio clara para
 * no romper la aplicación con un stacktrace crudo.
 */
public class UsuarioDAO {

    /** Minutos que dura el bloqueo por intentos fallidos antes de desbloquearse automáticamente. */
    private static final int MINUTOS_BLOQUEO = 30;

    /** Lanzada cuando el correo ya existe (violación de la restricción UNIQUE usuario.correo). */
    public static class CorreoDuplicadoException extends Exception {
        public CorreoDuplicadoException(String mensaje) { super(mensaje); }
    }

    /**
     * Registra un usuario nuevo con rol CLIENTE por defecto (transacción:
     * usuario + perfil + usuario_rol se insertan de forma atómica).
     */
    public void registrarCliente(String correo, String hashContrasena,
                                  String nombres, String apellidos,
                                  String documento, String telefono) throws CorreoDuplicadoException, SQLException {

        String sqlUsuario = "INSERT INTO usuario (correo, contrasena_hash) VALUES (?, ?)";
        String sqlPerfil  = "INSERT INTO perfil (id_usuario, nombres, apellidos, documento, telefono) VALUES (?, ?, ?, ?, ?)";
        String sqlRol     = "INSERT INTO usuario_rol (id_usuario, id_rol) " +
                            "SELECT ?, id_rol FROM rol WHERE nombre_rol = 'CLIENTE'";

        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false);
            try (PreparedStatement psUsuario = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                psUsuario.setString(1, correo);
                psUsuario.setString(2, hashContrasena);
                psUsuario.executeUpdate();

                int idUsuario;
                try (ResultSet rs = psUsuario.getGeneratedKeys()) {
                    rs.next();
                    idUsuario = rs.getInt(1);
                }

                try (PreparedStatement psPerfil = con.prepareStatement(sqlPerfil)) {
                    psPerfil.setInt(1, idUsuario);
                    psPerfil.setString(2, nombres);
                    psPerfil.setString(3, apellidos);
                    psPerfil.setString(4, documento);
                    psPerfil.setString(5, telefono);
                    psPerfil.executeUpdate();
                }

                try (PreparedStatement psRol = con.prepareStatement(sqlRol)) {
                    psRol.setInt(1, idUsuario);
                    psRol.executeUpdate();
                }

                con.commit();
            } catch (SQLIntegrityConstraintViolationException dup) {
                con.rollback();
                throw new CorreoDuplicadoException("El correo ya se encuentra registrado.");
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    /** Busca un usuario por correo, incluyendo sus roles. Retorna null si no existe. */
    public Usuario buscarPorCorreo(String correo) throws SQLException {
        String sql =
            "SELECT u.id_usuario, u.correo, u.contrasena_hash, u.estado, u.intentos_fallidos, u.fecha_bloqueo, " +
            "       p.nombres, p.apellidos, r.nombre_rol " +
            "FROM usuario u " +
            "LEFT JOIN perfil p ON p.id_usuario = u.id_usuario " +
            "LEFT JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario " +
            "LEFT JOIN rol r ON r.id_rol = ur.id_rol " +
            "WHERE u.correo = ?";

        Usuario usuario = null;
        List<String> roles = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (usuario == null) {
                        usuario = new Usuario();
                        usuario.setIdUsuario(rs.getInt("id_usuario"));
                        usuario.setCorreo(rs.getString("correo"));
                        usuario.setContrasenaHash(rs.getString("contrasena_hash"));
                        usuario.setEstado(rs.getString("estado"));
                        usuario.setIntentosFallidos(rs.getInt("intentos_fallidos"));
                        Timestamp fb = rs.getTimestamp("fecha_bloqueo");
                        if (fb != null) usuario.setFechaBloqueo(fb.toLocalDateTime());
                        usuario.setNombres(rs.getString("nombres"));
                        usuario.setApellidos(rs.getString("apellidos"));
                    }
                    String rol = rs.getString("nombre_rol");
                    if (rol != null) roles.add(rol);
                }
            }
        }
        if (usuario != null) usuario.setRoles(roles);
        return usuario;
    }

    /**
     * Registra un intento fallido de inicio de sesión. Al llegar a 5 intentos,
     * bloquea la cuenta y guarda la fecha/hora del bloqueo (para el desbloqueo
     * automático tras 30 minutos).
     */
    public void incrementarIntentosFallidos(int idUsuario) throws SQLException {
        // OJO: en un mismo UPDATE, MySQL evalúa las columnas de izquierda a derecha, así que
        // cuando el CASE lee "intentos_fallidos" aquí, ya está viendo el valor recién incrementado
        // de la línea anterior (no el original). Por eso NO se le vuelve a sumar 1 en el CASE.
        String sql = "UPDATE usuario SET intentos_fallidos = intentos_fallidos + 1, " +
                     "estado = CASE WHEN intentos_fallidos >= 5 THEN 'BLOQUEADO' ELSE estado END, " +
                     "fecha_bloqueo = CASE WHEN intentos_fallidos >= 5 THEN NOW() ELSE fecha_bloqueo END " +
                     "WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    /** Reinicia el contador de intentos fallidos tras un login exitoso. */
    public void reiniciarIntentosFallidos(int idUsuario) throws SQLException {
        String sql = "UPDATE usuario SET intentos_fallidos = 0, fecha_bloqueo = NULL WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    /**
     * Si la cuenta está BLOQUEADO y ya pasaron MINUTOS_BLOQUEO desde fecha_bloqueo,
     * la reactiva automáticamente (estado ACTIVO, contador en 0, fecha_bloqueo NULL)
     * y actualiza también el objeto en memoria para que el flujo de login continúe
     * sin necesidad de volver a consultar la base de datos.
     *
     * @return true si la cuenta sigue bloqueada (el tiempo aún no se cumple), false en cualquier otro caso.
     */
    public boolean sigueBloqueado(Usuario usuario) throws SQLException {
        if (!"BLOQUEADO".equals(usuario.getEstado()) || usuario.getFechaBloqueo() == null) {
            return "BLOQUEADO".equals(usuario.getEstado());
        }

        java.time.LocalDateTime limite = usuario.getFechaBloqueo().plusMinutes(MINUTOS_BLOQUEO);
        if (java.time.LocalDateTime.now().isBefore(limite)) {
            return true; // todavía no se cumplen los 30 minutos
        }

        // Ya se cumplió el tiempo: desbloquear en la base de datos y en el objeto en memoria
        String sql = "UPDATE usuario SET estado = 'ACTIVO', intentos_fallidos = 0, fecha_bloqueo = NULL WHERE id_usuario = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuario.getIdUsuario());
            ps.executeUpdate();
        }
        usuario.setEstado("ACTIVO");
        usuario.setIntentosFallidos(0);
        usuario.setFechaBloqueo(null);
        return false;
    }

    /** Registra un evento en la tabla de auditoría. */
    public void registrarAuditoria(Integer idUsuario, String accion, String ip) {
        String sql = "INSERT INTO auditoria (id_usuario, accion, ip_origen) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (idUsuario == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, idUsuario);
            ps.setString(2, accion);
            ps.setString(3, ip);
            ps.executeUpdate();
        } catch (SQLException e) {
            // La auditoría no debe tumbar el flujo principal; solo se registra en log.
            System.err.println("No se pudo registrar auditoría: " + e.getMessage());
        }
    }
}