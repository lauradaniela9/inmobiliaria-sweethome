package com.inmobiliaria.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Validaciones de formato reutilizadas por varios servlets (registro,
 * perfil, propiedades, citas). Se extrajeron a una clase independiente
 * de Servlet/JSP para poder cubrirlas con pruebas unitarias JUnit
 * (acción pendiente de la retrospectiva del Sprint 1).
 */
public final class Validaciones {

    private static final Pattern PATRON_CORREO =
        Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PATRON_TELEFONO = Pattern.compile("^[0-9]{7,10}$");

    private Validaciones() { }

    public static boolean esCorreoValido(String correo) {
        return correo != null && PATRON_CORREO.matcher(correo).matches();
    }

    public static boolean esTelefonoValido(String telefono) {
        // El teléfono es opcional en varios formularios; solo se valida si viene diligenciado.
        return telefono == null || telefono.isBlank() || PATRON_TELEFONO.matcher(telefono).matches();
    }

    public static boolean esContrasenaValida(String contrasena) {
        return contrasena != null && contrasena.length() >= 8;
    }

    public static boolean coinciden(String contrasena, String confirmacion) {
        return contrasena != null && contrasena.equals(confirmacion);
    }

    public static boolean esPrecioValido(BigDecimal precio) {
        return precio != null && precio.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean esCadenaObligatoriaValida(String valor) {
        return valor != null && !valor.trim().isEmpty();
    }

    /** Convierte un texto a BigDecimal de forma segura; retorna null si no es un número válido. */
    public static BigDecimal parsearDecimal(String texto) {
        if (texto == null || texto.isBlank()) return null;
        try {
            return new BigDecimal(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Convierte un texto a Integer de forma segura; retorna null si no es un entero válido. */
    public static Integer parsearEntero(String texto) {
        if (texto == null || texto.isBlank()) return null;
        try {
            return Integer.valueOf(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}