package com.inmobiliaria.controller;

import com.inmobiliaria.dao.PropiedadDAO;
import com.inmobiliaria.util.Validaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * Catálogo público de propiedades disponibles, con filtros por ciudad,
 * tipo, rango de precio y característica. Accesible sin autenticación
 * (rol Visitante) y también usado por el Cliente autenticado.
 */
@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PropiedadDAO dao = new PropiedadDAO();

        Integer idCiudad = Validaciones.parsearEntero(req.getParameter("ciudad"));
        Integer idTipo = Validaciones.parsearEntero(req.getParameter("tipo"));
        Integer idCaracteristica = Validaciones.parsearEntero(req.getParameter("caracteristica"));
        BigDecimal precioMin = Validaciones.parsearDecimal(req.getParameter("precioMin"));
        BigDecimal precioMax = Validaciones.parsearDecimal(req.getParameter("precioMax"));

        try {
            req.setAttribute("propiedades", dao.buscarConFiltros(idCiudad, idTipo, precioMin, precioMax, idCaracteristica));
            req.setAttribute("ciudades", dao.listarCiudades());
            req.setAttribute("tipos", dao.listarTiposPropiedad());
            req.setAttribute("caracteristicas", dao.listarCaracteristicas());

            req.setAttribute("filtroCiudad", idCiudad);
            req.setAttribute("filtroTipo", idTipo);
            req.setAttribute("filtroCaracteristica", idCaracteristica);
            req.setAttribute("filtroPrecioMin", precioMin);
            req.setAttribute("filtroPrecioMax", precioMax);

            req.getRequestDispatcher("/catalogo.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar el catálogo de propiedades en este momento.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}