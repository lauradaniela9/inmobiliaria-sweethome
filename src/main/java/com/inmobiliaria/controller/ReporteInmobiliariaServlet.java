package com.inmobiliaria.controller;

import com.inmobiliaria.dao.PropiedadDAO;
import com.inmobiliaria.dao.ReporteDAO;
import com.inmobiliaria.model.Propiedad;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/** Reporte propio de ventas y arriendos concretados por la inmobiliaria autenticada. */
@WebServlet("/dashboard/inmobiliaria/reportes")
public class ReporteInmobiliariaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idUsuario = (int) sesion.getAttribute("idUsuario");
        try {
            req.setAttribute("ventasArriendos", new ReporteDAO().ventasArriendosPorInmobiliaria(idUsuario));

            // Detalle: de todas las propiedades de esta inmobiliaria, solo las VENDIDA o ARRENDADA
            List<Propiedad> todas = new PropiedadDAO().listarPorInmobiliaria(idUsuario);
            List<Propiedad> vendidasOArrendadas = todas.stream()
                    .filter(p -> "VENDIDA".equals(p.getEstado()) || "ARRENDADA".equals(p.getEstado()))
                    .collect(Collectors.toList());
            req.setAttribute("listaDetalladaVendidas", vendidasOArrendadas);

            req.getRequestDispatcher("/dashboard/inmobiliaria/reportes.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible generar tu reporte de ventas y arriendos.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}