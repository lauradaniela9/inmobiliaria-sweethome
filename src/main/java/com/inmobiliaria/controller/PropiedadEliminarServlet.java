package com.inmobiliaria.controller;

import com.inmobiliaria.dao.PropiedadDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/** Baja lógica de una propiedad (nunca se elimina físicamente el registro). */
@WebServlet("/dashboard/inmobiliaria/propiedad-eliminar")
public class PropiedadEliminarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idUsuario = (int) sesion.getAttribute("idUsuario");
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            new PropiedadDAO().darDeBaja(id, idUsuario);
            req.getSession().setAttribute("exitoPropiedad", "La propiedad fue dada de baja del catálogo.");
        } catch (SQLException | NumberFormatException e) {
            req.getSession().setAttribute("errorPropiedad", "No fue posible dar de baja la propiedad.");
        }
        resp.sendRedirect(req.getContextPath() + "/dashboard/inmobiliaria/propiedades");
    }
}