package com.inmobiliaria.controller;

import com.inmobiliaria.dao.UsuarioAdminDAO;
import com.inmobiliaria.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Módulo de administración de usuarios y roles (Historia 4, retomada al
 * inicio del Sprint 2 según la retrospectiva del Sprint 1).
 * Ruta protegida: solo el rol ADMINISTRADOR (/dashboard/admin/*).
 */
@WebServlet("/dashboard/admin/usuarios")
public class UsuarioAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UsuarioAdminDAO dao = new UsuarioAdminDAO();
            req.setAttribute("usuarios", dao.listarUsuarios());
            req.setAttribute("rolesDisponibles", dao.listarNombresRoles());
            req.getRequestDispatcher("/dashboard/admin/usuarios.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar el listado de usuarios.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        UsuarioAdminDAO dao = new UsuarioAdminDAO();
        HttpSession sesion = req.getSession(false);
        Integer idAdmin = (Integer) sesion.getAttribute("idUsuario");

        try {
            int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));

            if ("cambiarEstado".equals(accion)) {
                String nuevoEstado = req.getParameter("estado");
                dao.cambiarEstado(idUsuario, nuevoEstado);
                new UsuarioDAO().registrarAuditoria(idAdmin,
                        "Cambio de estado del usuario #" + idUsuario + " a " + nuevoEstado, req.getRemoteAddr());

            } else if ("asignarRol".equals(accion)) {
                String rol = req.getParameter("rol");
                dao.asignarRol(idUsuario, rol);
                new UsuarioDAO().registrarAuditoria(idAdmin,
                        "Rol " + rol + " asignado al usuario #" + idUsuario, req.getRemoteAddr());

            } else if ("revocarRol".equals(accion)) {
                String rol = req.getParameter("rol");
                dao.revocarRol(idUsuario, rol);
                new UsuarioDAO().registrarAuditoria(idAdmin,
                        "Rol " + rol + " revocado al usuario #" + idUsuario, req.getRemoteAddr());
            }
            req.getSession().setAttribute("exitoUsuarios", "Cambios aplicados correctamente.");

        } catch (SQLException | NumberFormatException e) {
            req.getSession().setAttribute("errorUsuarios", "No fue posible aplicar el cambio solicitado.");
        }
        resp.sendRedirect(req.getContextPath() + "/dashboard/admin/usuarios");
    }
}