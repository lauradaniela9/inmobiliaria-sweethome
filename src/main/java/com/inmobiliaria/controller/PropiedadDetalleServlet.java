package com.inmobiliaria.controller;

import com.inmobiliaria.dao.FavoritoDAO;
import com.inmobiliaria.dao.PropiedadDAO;
import com.inmobiliaria.model.Propiedad;
import com.inmobiliaria.util.Validaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/** Ficha pública de detalle de un inmueble: galería, características e inmobiliaria a cargo. */
@WebServlet("/propiedad")
public class PropiedadDetalleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Validaciones.parsearEntero(req.getParameter("id"));
        if (id == null) {
            resp.sendRedirect(req.getContextPath() + "/catalogo");
            return;
        }
        try {
            Propiedad propiedad = new PropiedadDAO().buscarPorId(id);
            if (propiedad == null) {
                req.setAttribute("error", "La propiedad solicitada no existe o fue retirada del catálogo.");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("propiedad", propiedad);

            HttpSession sesion = req.getSession(false);
            if (sesion != null && sesion.getAttribute("idUsuario") != null) {
                int idUsuario = (int) sesion.getAttribute("idUsuario");
                req.setAttribute("esFavorito", new FavoritoDAO().esFavorito(idUsuario, id));
            }
            req.getRequestDispatcher("/propiedad-detalle.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar el detalle de la propiedad.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}