package com.inmobiliaria.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase centralizada para obtener la conexión JDBC.
 * La cadena de conexión NO se repite en cada clase: se lee una sola vez
 * desde /db.properties (classpath), lo que la hace configurable sin
 * tener que recompilar el código (requisito del enunciado).
 */
public class ConexionBD {

    private static String URL;
    private static String USUARIO;
    private static String CLAVE;
    private static String DRIVER;

    static {
        try (InputStream input = ConexionBD.class.getResourceAsStream("/db.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo db.properties en el classpath");
            }
            prop.load(input);
            DRIVER  = prop.getProperty("db.driver");
            URL     = prop.getProperty("db.url");
            USUARIO = prop.getProperty("db.usuario");
            CLAVE   = prop.getProperty("db.clave");
            Class.forName(DRIVER);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la configuración de la base de datos", e);
        }
    }

    private ConexionBD() {
        // utilitaria: no se instancia
    }

    /**
     * Retorna una nueva conexión JDBC. El llamador es responsable de
     * cerrarla (se recomienda try-with-resources).
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
