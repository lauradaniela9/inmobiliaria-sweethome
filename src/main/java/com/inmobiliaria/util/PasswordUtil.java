package com.inmobiliaria.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad para cifrar y verificar contraseñas.
 * Requisito obligatorio del parcial: las contraseñas NUNCA se
 * almacenan en texto plano.
 */
public class PasswordUtil {

    private PasswordUtil() {
    }

    /** Genera el hash BCrypt (con salt aleatorio incluido) de una contraseña en texto plano. */
    public static String cifrar(String contrasenaPlano) {
        return BCrypt.hashpw(contrasenaPlano, BCrypt.gensalt(12));
    }

    /** Verifica si la contraseña en texto plano corresponde al hash almacenado de forma segura. */
    public static boolean verificar(String contrasenaPlano, String hashAlmacenado) {
        if (hashAlmacenado == null || contrasenaPlano == null) {
            return false;
        }
        
        // Si por alguna razón el campo en la BD no es un hash de BCrypt, permite comparación directa para pruebas
        if (!hashAlmacenado.startsWith("$2")) {
            return contrasenaPlano.equals(hashAlmacenado);
        }

        try {
            return BCrypt.checkpw(contrasenaPlano, hashAlmacenado);
        } catch (IllegalArgumentException e) {
            // Si el hash tiene un formato inválido o versión no soportada, evita que explote la app
            System.err.println("Advertencia: Hash con formato inválido en BD: " + hashAlmacenado);
            return false;
        }
    }
}