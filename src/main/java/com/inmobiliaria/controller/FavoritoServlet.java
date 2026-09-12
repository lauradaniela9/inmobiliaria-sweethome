package com.inmobiliaria.controller;

import com.inmobiliaria.dao.FavoritoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/** Panel del cliente: listado de propiedades favoritas y acción de marcar/desmarcar. */
@WebServlet("/dashboard/cliente/favoritos")
public class FavoritoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idCliente = (int) sesion.getAttribute("idUsuario");
        try {
            req.setAttribute("favoritos", new FavoritoDAO().listarPorCliente(idCliente));
            req.getRequestDispatcher("/dashboard/cliente/favoritos.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar tus favoritos.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idCliente = (int) sesion.getAttribute("idUsuario");
        try {
            int idPropiedad = Integer.parseInt(req.getParameter("idPropiedad"));
            new FavoritoDAO().alternar(idCliente, idPropiedad);

            // Si la acción vino desde la ficha de detalle, regresa allí; si no, al listado de favoritos.
            String volverA = req.getParameter("volverA");
            if ("detalle".equals(volverA)) {
                resp.sendRedirect(req.getContextPath() + "/propiedad?id=" + idPropiedad);
                return;
            }
        } catch (SQLException | NumberFormatException e) {
            req.getSession().setAttribute("errorFavoritos", "No fue posible actualizar tus favoritos.");
        }
        resp.sendRedirect(req.getContextPath() + "/dashboard/cliente/favoritos");
    }
}