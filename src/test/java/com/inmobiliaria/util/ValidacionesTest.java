package com.inmobiliaria.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias de la clase Validaciones, extraída en el Sprint 1 precisamente
 * para poder cubrirla con JUnit sin depender de un Servlet ni de la base de datos
 * (acción pendiente registrada en la retrospectiva del Sprint 1).
 */
class ValidacionesTest {

    // ---- correo ----

    @Test
    void correoValidoConFormatoCorrecto() {
        assertTrue(Validaciones.esCorreoValido("cliente1@correo.com"));
        assertTrue(Validaciones.esCorreoValido("nombre.apellido@inmobiliaria.co"));
    }

    @Test
    void correoInvalidoSinArrobaOSinDominio() {
        assertFalse(Validaciones.esCorreoValido("correo-sin-arroba.com"));
        assertFalse(Validaciones.esCorreoValido("correo@sindominio"));
        assertFalse(Validaciones.esCorreoValido(null));
    }

    // ---- teléfono ----

    @Test
    void telefonoValidoEntreSieteYDiezDigitos() {
        assertTrue(Validaciones.esTelefonoValido("3001234567"));
        assertTrue(Validaciones.esTelefonoValido("1234567"));
    }

    @Test
    void telefonoOpcionalEsValidoSiVieneVacio() {
        assertTrue(Validaciones.esTelefonoValido(null));
        assertTrue(Validaciones.esTelefonoValido(""));
        assertTrue(Validaciones.esTelefonoValido("   "));
    }

    @Test
    void telefonoInvalidoConLetrasOLongitudIncorrecta() {
        assertFalse(Validaciones.esTelefonoValido("300abc4567"));
        assertFalse(Validaciones.esTelefonoValido("123")); // menos de 7 dígitos
        assertFalse(Validaciones.esTelefonoValido("12345678901")); // más de 10 dígitos
    }

    // ---- contraseña ----

    @Test
    void contrasenaValidaConMinimoOchoCaracteres() {
        assertTrue(Validaciones.esContrasenaValida("Password123"));
    }

    @Test
    void contrasenaInvalidaConMenosDeOchoCaracteres() {
        assertFalse(Validaciones.esContrasenaValida("abc123"));
        assertFalse(Validaciones.esContrasenaValida(null));
    }

    @Test
    void confirmacionDeContrasenaDebeCoincidir() {
        assertTrue(Validaciones.coinciden("Password123", "Password123"));
        assertFalse(Validaciones.coinciden("Password123", "OtraClave123"));
        assertFalse(Validaciones.coinciden(null, "Password123"));
    }

    // ---- precio ----

    @Test
    void precioValidoMayorACero() {
        assertTrue(Validaciones.esPrecioValido(new BigDecimal("150000000")));
    }

    @Test
    void precioInvalidoSiEsCeroNegativoONulo() {
        assertFalse(Validaciones.esPrecioValido(BigDecimal.ZERO));
        assertFalse(Validaciones.esPrecioValido(new BigDecimal("-100")));
        assertFalse(Validaciones.esPrecioValido(null));
    }

    // ---- cadena obligatoria ----

    @Test
    void cadenaObligatoriaInvalidaSiEsNulaOVacia() {
        assertFalse(Validaciones.esCadenaObligatoriaValida(null));
        assertFalse(Validaciones.esCadenaObligatoriaValida(""));
        assertFalse(Validaciones.esCadenaObligatoriaValida("   "));
        assertTrue(Validaciones.esCadenaObligatoriaValida("Casa campestre"));
    }

    // ---- parseo seguro ----

    @Test
    void parsearDecimalDevuelveNullSiNoEsNumerico() {
        assertNull(Validaciones.parsearDecimal("no-es-un-numero"));
        assertNull(Validaciones.parsearDecimal(null));
        assertEquals(new BigDecimal("320000000"), Validaciones.parsearDecimal("320000000"));
    }

    @Test
    void parsearEnteroDevuelveNullSiNoEsNumerico() {
        assertNull(Validaciones.parsearEntero("abc"));
        assertNull(Validaciones.parsearEntero(""));
        assertEquals(Integer.valueOf(5), Validaciones.parsearEntero("5"));
    }
}
