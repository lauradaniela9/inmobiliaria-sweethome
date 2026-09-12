package com.inmobiliaria.controller;

import com.inmobiliaria.dao.AuditoriaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/** Historial de auditoría de accesos y cambios (Historia 13, rol ADMINISTRADOR). */
@WebServlet("/dashboard/admin/auditoria")
public class AuditoriaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("eventos", new AuditoriaDAO().listarUltimos(100));
            req.getRequestDispatcher("/dashboard/admin/auditoria.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar la auditoría del sistema.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}