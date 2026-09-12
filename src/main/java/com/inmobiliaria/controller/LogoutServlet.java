package com.inmobiliaria.controller;

import com.inmobiliaria.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        if (sesion != null) {
            Object idUsuario = sesion.getAttribute("idUsuario");
            new UsuarioDAO().registrarAuditoria(
                    idUsuario != null ? (Integer) idUsuario : null,
                    "Cierre de sesión", req.getRemoteAddr());
            sesion.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
