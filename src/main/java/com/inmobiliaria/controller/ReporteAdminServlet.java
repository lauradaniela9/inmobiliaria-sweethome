package com.inmobiliaria.controller;

import com.inmobiliaria.dao.CitaDAO;
import com.inmobiliaria.dao.ReporteDAO;
import com.inmobiliaria.dao.SolicitudDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Reportes consolidados del sistema completo (rol ADMINISTRADOR):
 * propiedades por ciudad/estado, valor promedio por tipo, citas por
 * estado y solicitudes por inmobiliaria. Todas construidas con SQL
 * que involucra varias tablas (JOIN + GROUP BY/HAVING).
 */
@WebServlet("/dashboard/admin/reportes")
public class ReporteAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ReporteDAO reporteDAO = new ReporteDAO();
            req.setAttribute("propiedadesPorCiudad", reporteDAO.propiedadesPorCiudadYEstado());
            req.setAttribute("valorPorTipo", reporteDAO.valorPromedioPorTipo());
            req.setAttribute("citasPorEstado", new CitaDAO().totalPorEstado());
            req.setAttribute("solicitudesPorInmobiliaria", new SolicitudDAO().totalPorInmobiliaria());
            req.getRequestDispatcher("/dashboard/admin/reportes.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible generar los reportes en este momento.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}