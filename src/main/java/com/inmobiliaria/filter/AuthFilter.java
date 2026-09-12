package com.inmobiliaria.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Filtro que protege las rutas privadas del sistema.
 *
 * Reglas (obligatorias según el enunciado):
 *  - Si el usuario NO está autenticado y pide una ruta privada -> acceso-denegado.jsp
 *  - Si está autenticado pero NO tiene el rol requerido        -> acceso-denegado.jsp
 *  - Ocultar un botón/menú en el JSP NO es control de acceso: la validación
 *    real siempre ocurre aquí, en el servidor.
 */
@WebFilter("/dashboard/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI().substring(req.getContextPath().length());
        HttpSession sesion = req.getSession(false);

        // 1) ¿Está autenticado?
        if (sesion == null || sesion.getAttribute("idUsuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/acceso-denegado.jsp");
            return;
        }

        // 2) ¿Tiene el rol requerido para la ruta solicitada?
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) sesion.getAttribute("roles");

        String rolRequerido = obtenerRolRequeridoParaRuta(uri);
        if (rolRequerido != null && (roles == null || !roles.contains(rolRequerido))) {
            resp.sendRedirect(req.getContextPath() + "/acceso-denegado.jsp");
            return;
        }

        // Autorizado: continúa la cadena de filtros / llega al recurso
        chain.doFilter(request, response);
    }

    /** Mapea cada subruta del panel al rol exigido. */
    private String obtenerRolRequeridoParaRuta(String uri) {
        if (uri.startsWith("/dashboard/admin")) return "ADMINISTRADOR";
        if (uri.startsWith("/dashboard/inmobiliaria")) return "INMOBILIARIA";
        if (uri.startsWith("/dashboard/cliente")) return "CLIENTE";
        return null; // otras subrutas del panel: solo exige estar autenticado
    }
}
