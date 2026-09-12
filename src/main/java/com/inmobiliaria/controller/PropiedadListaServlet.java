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

/**
 * Panel de la inmobiliaria: listado de sus propiedades publicadas.
 * Ruta bajo /dashboard/inmobiliaria/* -> protegida por AuthFilter (rol INMOBILIARIA).
 */
@WebServlet("/dashboard/inmobiliaria/propiedades")
public class PropiedadListaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idUsuario = (int) sesion.getAttribute("idUsuario");
        try {
            req.setAttribute("propiedades", new PropiedadDAO().listarPorInmobiliaria(idUsuario));
            req.getRequestDispatcher("/dashboard/inmobiliaria/propiedades.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar tus propiedades.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}