package com.inmobiliaria.dao;

import com.inmobiliaria.model.Caracteristica;
import com.inmobiliaria.model.ImagenPropiedad;
import com.inmobiliaria.model.Opcion;
import com.inmobiliaria.model.Propiedad;
import com.inmobiliaria.util.ConexionBD;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Acceso a datos de la entidad propiedad y sus relaciones:
 *  - imagen_propiedad (1:N)
 *  - propiedad_caracteristica (N:M)
 *
 * Contiene además las consultas de catálogo público (buscador con filtros)
 * y las consultas obligatorias con INNER JOIN de varias tablas.
 */
public class PropiedadDAO {

    /** Se lanza cuando se intenta duplicar la matrícula inmobiliaria (restricción UNIQUE). */
    public static class MatriculaDuplicadaException extends Exception {
        public MatriculaDuplicadaException(String mensaje) { super(mensaje); }
    }

    // ------------------------------------------------------------------
    // CATÁLOGOS (para combos de formularios y filtros)
    // ------------------------------------------------------------------

    public List<Opcion> listarCiudades() throws SQLException {
        return listarOpciones("SELECT id_ciudad AS id, CONCAT(nombre, ' - ', departamento) AS nombre FROM ciudad ORDER BY nombre");
    }

    public List<Opcion> listarTiposPropiedad() throws SQLException {
        return listarOpciones("SELECT id_tipo_propiedad AS id, nombre FROM tipo_propiedad ORDER BY nombre");
    }

    public List<Caracteristica> listarCaracteristicas() throws SQLException {
        List<Caracteristica> lista = new ArrayList<>();
        String sql = "SELECT id_caracteristica, nombre FROM caracteristica ORDER BY nombre";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Caracteristica(rs.getInt("id_caracteristica"), rs.getString("nombre")));
            }
        }
        return lista;
    }

    private List<Opcion> listarOpciones(String sql) throws SQLException {
        List<Opcion> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Opcion(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return lista;
    }

    // ------------------------------------------------------------------
    // BÚSQUEDA PÚBLICA CON FILTROS (Consulta obligatoria: INNER JOIN 1)
    // propiedad + ciudad + tipo_propiedad + inmobiliaria, con filtros dinámicos
    // ------------------------------------------------------------------

    public List<Propiedad> buscarConFiltros(Integer idCiudad, Integer idTipo, BigDecimal precioMin,
                                             BigDecimal precioMax, Integer idCaracteristica) throws SQLException {

        StringBuilder sql = new StringBuilder(
            "SELECT p.id_propiedad, p.titulo, p.precio, p.direccion, p.area_m2, p.estado, " +
            "       c.nombre AS nombre_ciudad, tp.nombre AS nombre_tipo, " +
            "       im.nombre_comercial AS nombre_inmobiliaria " +
            "FROM propiedad p " +
            "INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad " +
            "INNER JOIN tipo_propiedad tp ON tp.id_tipo_propiedad = p.id_tipo_propiedad " +
            "INNER JOIN inmobiliaria im ON im.id_usuario = p.id_usuario_inmobiliaria ");

        if (idCaracteristica != null) {
            sql.append("INNER JOIN propiedad_caracteristica pc ON pc.id_propiedad = p.id_propiedad ");
        }

        sql.append("WHERE p.estado = 'DISPONIBLE' ");
        List<Object> parametros = new ArrayList<>();

        if (idCiudad != null) { sql.append("AND p.id_ciudad = ? "); parametros.add(idCiudad); }
        if (idTipo != null) { sql.append("AND p.id_tipo_propiedad = ? "); parametros.add(idTipo); }
        if (precioMin != null) { sql.append("AND p.precio >= ? "); parametros.add(precioMin); }
        if (precioMax != null) { sql.append("AND p.precio <= ? "); parametros.add(precioMax); }
        if (idCaracteristica != null) { sql.append("AND pc.id_caracteristica = ? "); parametros.add(idCaracteristica); }

        sql.append("ORDER BY p.fecha_publicacion DESC");

        List<Propiedad> resultado = new ArrayList<>();
        Map<Integer, Propiedad> mapaTemp = new LinkedHashMap<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_propiedad");
                    if (mapaTemp.containsKey(id)) continue; // evita duplicados por el JOIN con características
                    Propiedad p = mapearFilaBasica(rs);
                    mapaTemp.put(id, p);
                    resultado.add(p);
                }
            }
        }

        // Cargar portada de cada propiedad en una segunda pasada (evita duplicar filas por el JOIN de imágenes)
        for (Propiedad p : resultado) {
            p.setImagenes(listarImagenes(p.getIdPropiedad()));
        }
        return resultado;
    }

    private Propiedad mapearFilaBasica(ResultSet rs) throws SQLException {
        Propiedad p = new Propiedad();
        p.setIdPropiedad(rs.getInt("id_propiedad"));
        p.setTitulo(rs.getString("titulo"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setDireccion(rs.getString("direccion"));
        p.setAreaM2(rs.getBigDecimal("area_m2"));
        p.setEstado(rs.getString("estado"));
        p.setNombreCiudad(rs.getString("nombre_ciudad"));
        p.setNombreTipoPropiedad(rs.getString("nombre_tipo"));
        p.setNombreComercialInmobiliaria(rs.getString("nombre_inmobiliaria"));
        return p;
    }

    // ------------------------------------------------------------------
    // FICHA DE DETALLE (Consulta obligatoria: INNER JOIN 2, con LEFT JOIN de citas)
    // ------------------------------------------------------------------

    /** Ficha completa de una propiedad: datos, ciudad, tipo, inmobiliaria, imágenes y características. */
    public Propiedad buscarPorId(int idPropiedad) throws SQLException {
        String sql =
            "SELECT p.*, c.nombre AS nombre_ciudad, c.departamento AS depto_ciudad, " +
            "       tp.nombre AS nombre_tipo, im.nombre_comercial, im.telefono_contacto " +
            "FROM propiedad p " +
            "INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad " +
            "INNER JOIN tipo_propiedad tp ON tp.id_tipo_propiedad = p.id_tipo_propiedad " +
            "INNER JOIN inmobiliaria im ON im.id_usuario = p.id_usuario_inmobiliaria " +
            "WHERE p.id_propiedad = ?";

        Propiedad p = null;
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropiedad);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Propiedad();
                    p.setIdPropiedad(rs.getInt("id_propiedad"));
                    p.setIdUsuarioInmobiliaria(rs.getInt("id_usuario_inmobiliaria"));
                    p.setIdCiudad(rs.getInt("id_ciudad"));
                    p.setIdTipoPropiedad(rs.getInt("id_tipo_propiedad"));
                    p.setMatriculaInmobiliaria(rs.getString("matricula_inmobiliaria"));
                    p.setTitulo(rs.getString("titulo"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio(rs.getBigDecimal("precio"));
                    p.setDireccion(rs.getString("direccion"));
                    p.setAreaM2(rs.getBigDecimal("area_m2"));
                    p.setEstado(rs.getString("estado"));
                    p.setAlcobas((Integer) rs.getObject("alcobas"));
                    p.setBanos(rs.getString("banos"));
                    p.setAnioConstruccion((Integer) rs.getObject("anio_construccion"));
                    p.setAreaTerreno(rs.getBigDecimal("area_terreno"));
                    p.setAreaPrivada(rs.getBigDecimal("area_privada"));
                    p.setEstrato((Integer) rs.getObject("estrato"));
                    p.setValorAdministracion(rs.getBigDecimal("valor_administracion"));
                    p.setTipoNegocio(rs.getString("tipo_negocio"));
                    p.setZonaBarrio(rs.getString("zona_barrio"));
                    p.setCodigo(rs.getString("codigo"));
                    p.setCondicion(rs.getString("condicion"));
                    Timestamp fecha = rs.getTimestamp("fecha_publicacion");
                    if (fecha != null) p.setFechaPublicacion(fecha.toString());
                    p.setNombreCiudad(rs.getString("nombre_ciudad"));
                    p.setDepartamentoCiudad(rs.getString("depto_ciudad"));
                    p.setNombreTipoPropiedad(rs.getString("nombre_tipo"));
                    p.setNombreComercialInmobiliaria(rs.getString("nombre_comercial"));
                    p.setTelefonoContactoInmobiliaria(rs.getString("telefono_contacto"));
                }
            }
        }
        if (p != null) {
            p.setImagenes(listarImagenes(idPropiedad));
            p.setCaracteristicas(listarCaracteristicasDePropiedad(idPropiedad));
        }
        return p;
    }

    /** Consulta obligatoria N:M: características asociadas a una propiedad concreta. */
    public List<Caracteristica> listarCaracteristicasDePropiedad(int idPropiedad) throws SQLException {
        String sql =
            "SELECT ca.id_caracteristica, ca.nombre, pc.cantidad " +
            "FROM propiedad_caracteristica pc " +
            "INNER JOIN caracteristica ca ON ca.id_caracteristica = pc.id_caracteristica " +
            "WHERE pc.id_propiedad = ? ORDER BY ca.nombre";
        List<Caracteristica> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropiedad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Caracteristica c = new Caracteristica(rs.getInt("id_caracteristica"), rs.getString("nombre"));
                    c.setCantidad(rs.getInt("cantidad"));
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    public List<ImagenPropiedad> listarImagenes(int idPropiedad) throws SQLException {
        String sql = "SELECT id_imagen, id_propiedad, url_imagen, es_portada FROM imagen_propiedad " +
                     "WHERE id_propiedad = ? ORDER BY es_portada DESC, id_imagen ASC";
        List<ImagenPropiedad> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropiedad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ImagenPropiedad img = new ImagenPropiedad();
                    img.setIdImagen(rs.getInt("id_imagen"));
                    img.setIdPropiedad(rs.getInt("id_propiedad"));
                    img.setUrlImagen(rs.getString("url_imagen"));
                    img.setEsPortada(rs.getBoolean("es_portada"));
                    lista.add(img);
                }
            }
        }
        return lista;
    }

    // ------------------------------------------------------------------
    // PANEL DE LA INMOBILIARIA: listado propio, crear, editar, baja lógica
    // ------------------------------------------------------------------

    public List<Propiedad> listarPorInmobiliaria(int idUsuarioInmobiliaria) throws SQLException {
        String sql =
            "SELECT p.id_propiedad, p.titulo, p.precio, p.estado, p.matricula_inmobiliaria, " +
            "       c.nombre AS nombre_ciudad, tp.nombre AS nombre_tipo, " +
            "       (SELECT COUNT(*) FROM cita ci WHERE ci.id_propiedad = p.id_propiedad) AS total_citas " +
            "FROM propiedad p " +
            "INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad " +
            "INNER JOIN tipo_propiedad tp ON tp.id_tipo_propiedad = p.id_tipo_propiedad " +
            "WHERE p.id_usuario_inmobiliaria = ? " +
            "ORDER BY p.fecha_publicacion DESC";
        List<Propiedad> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioInmobiliaria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Propiedad p = new Propiedad();
                    p.setIdPropiedad(rs.getInt("id_propiedad"));
                    p.setTitulo(rs.getString("titulo"));
                    p.setPrecio(rs.getBigDecimal("precio"));
                    p.setEstado(rs.getString("estado"));
                    p.setMatriculaInmobiliaria(rs.getString("matricula_inmobiliaria"));
                    p.setNombreCiudad(rs.getString("nombre_ciudad"));
                    p.setNombreTipoPropiedad(rs.getString("nombre_tipo"));
                    p.setTotalCitas(rs.getLong("total_citas"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    /** Crea una propiedad junto con sus imágenes y características, de forma transaccional. */
    public int crear(Propiedad p, List<String> urlsImagenes, List<Integer> idsCaracteristicas)
            throws MatriculaDuplicadaException, SQLException {

        String sqlProp =
            "INSERT INTO propiedad (id_usuario_inmobiliaria, id_ciudad, id_tipo_propiedad, " +
            "matricula_inmobiliaria, titulo, descripcion, precio, direccion, area_m2, estado) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                int idPropiedad;
                try (PreparedStatement ps = con.prepareStatement(sqlProp, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, p.getIdUsuarioInmobiliaria());
                    ps.setInt(2, p.getIdCiudad());
                    ps.setInt(3, p.getIdTipoPropiedad());
                    ps.setString(4, p.getMatriculaInmobiliaria());
                    ps.setString(5, p.getTitulo());
                    ps.setString(6, p.getDescripcion());
                    ps.setBigDecimal(7, p.getPrecio());
                    ps.setString(8, p.getDireccion());
                    ps.setBigDecimal(9, p.getAreaM2());
                    ps.setString(10, p.getEstado() != null ? p.getEstado() : "DISPONIBLE");
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        idPropiedad = rs.getInt(1);
                    }
                }
                insertarImagenes(con, idPropiedad, urlsImagenes);
                insertarCaracteristicas(con, idPropiedad, idsCaracteristicas);
                con.commit();
                return idPropiedad;
            } catch (SQLIntegrityConstraintViolationException dup) {
                con.rollback();
                throw new MatriculaDuplicadaException(
                        "La matrícula inmobiliaria ya se encuentra registrada para otro inmueble.");
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    /** Actualiza los datos de una propiedad y reemplaza sus imágenes/características. */
    public void actualizar(Propiedad p, List<String> urlsImagenes, List<Integer> idsCaracteristicas)
            throws MatriculaDuplicadaException, SQLException {

        String sql =
            "UPDATE propiedad SET id_ciudad=?, id_tipo_propiedad=?, matricula_inmobiliaria=?, " +
            "titulo=?, descripcion=?, precio=?, direccion=?, area_m2=?, estado=? " +
            "WHERE id_propiedad=? AND id_usuario_inmobiliaria=?";

        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, p.getIdCiudad());
                    ps.setInt(2, p.getIdTipoPropiedad());
                    ps.setString(3, p.getMatriculaInmobiliaria());
                    ps.setString(4, p.getTitulo());
                    ps.setString(5, p.getDescripcion());
                    ps.setBigDecimal(6, p.getPrecio());
                    ps.setString(7, p.getDireccion());
                    ps.setBigDecimal(8, p.getAreaM2());
                    ps.setString(9, p.getEstado());
                    ps.setInt(10, p.getIdPropiedad());
                    ps.setInt(11, p.getIdUsuarioInmobiliaria());
                    ps.executeUpdate();
                }
                if (urlsImagenes != null) {
                    try (PreparedStatement del = con.prepareStatement(
                            "DELETE FROM imagen_propiedad WHERE id_propiedad = ?")) {
                        del.setInt(1, p.getIdPropiedad());
                        del.executeUpdate();
                    }
                    insertarImagenes(con, p.getIdPropiedad(), urlsImagenes);
                }
                if (idsCaracteristicas != null) {
                    try (PreparedStatement del = con.prepareStatement(
                            "DELETE FROM propiedad_caracteristica WHERE id_propiedad = ?")) {
                        del.setInt(1, p.getIdPropiedad());
                        del.executeUpdate();
                    }
                    insertarCaracteristicas(con, p.getIdPropiedad(), idsCaracteristicas);
                }
                con.commit();
            } catch (SQLIntegrityConstraintViolationException dup) {
                con.rollback();
                throw new MatriculaDuplicadaException(
                        "La matrícula inmobiliaria ya se encuentra registrada para otro inmueble.");
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    private void insertarImagenes(Connection con, int idPropiedad, List<String> urls) throws SQLException {
        if (urls == null || urls.isEmpty()) return;
        String sql = "INSERT INTO imagen_propiedad (id_propiedad, url_imagen, es_portada) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < urls.size(); i++) {
                String url = urls.get(i).trim();
                if (url.isEmpty()) continue;
                ps.setInt(1, idPropiedad);
                ps.setString(2, url);
                ps.setBoolean(3, i == 0); // la primera imagen cargada queda como portada
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertarCaracteristicas(Connection con, int idPropiedad, List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) return;
        String sql = "INSERT INTO propiedad_caracteristica (id_propiedad, id_caracteristica, cantidad) VALUES (?, ?, 1)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Integer idCaracteristica : ids) {
                ps.setInt(1, idPropiedad);
                ps.setInt(2, idCaracteristica);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    /** Baja lógica: nunca se borra físicamente el inmueble (se conserva el historial de citas/solicitudes). */
    public void darDeBaja(int idPropiedad, int idUsuarioInmobiliaria) throws SQLException {
        String sql = "UPDATE propiedad SET estado = 'INACTIVA' WHERE id_propiedad = ? AND id_usuario_inmobiliaria = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropiedad);
            ps.setInt(2, idUsuarioInmobiliaria);
            ps.executeUpdate();
        }
    }

    /** Verifica que la propiedad pertenezca a la inmobiliaria autenticada (control de acceso a nivel de datos). */
    public boolean perteneceAInmobiliaria(int idPropiedad, int idUsuarioInmobiliaria) throws SQLException {
        String sql = "SELECT 1 FROM propiedad WHERE id_propiedad = ? AND id_usuario_inmobiliaria = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropiedad);
            ps.setInt(2, idUsuarioInmobiliaria);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Consulta obligatoria LEFT JOIN: propiedades de una inmobiliaria que aún no tienen ninguna cita agendada. */
    public List<Propiedad> listarSinCitas(int idUsuarioInmobiliaria) throws SQLException {
        String sql =
            "SELECT p.id_propiedad, p.titulo, p.estado " +
            "FROM propiedad p " +
            "LEFT JOIN cita ci ON ci.id_propiedad = p.id_propiedad " +
            "WHERE p.id_usuario_inmobiliaria = ? AND ci.id_cita IS NULL";
        List<Propiedad> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuarioInmobiliaria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Propiedad p = new Propiedad();
                    p.setIdPropiedad(rs.getInt("id_propiedad"));
                    p.setTitulo(rs.getString("titulo"));
                    p.setEstado(rs.getString("estado"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}