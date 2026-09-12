package com.inmobiliaria.controller;

import com.inmobiliaria.dao.SolicitudDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/** Panel de la inmobiliaria: revisar la documentación radicada y aprobar/rechazar la solicitud. */
@WebServlet("/dashboard/inmobiliaria/solicitudes")
public class SolicitudInmobiliariaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idUsuario = (int) sesion.getAttribute("idUsuario");
        try {
            req.setAttribute("solicitudes", new SolicitudDAO().listarPorInmobiliaria(idUsuario));
            req.getRequestDispatcher("/dashboard/inmobiliaria/solicitudes.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar las solicitudes recibidas.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idUsuario = (int) sesion.getAttribute("idUsuario");
        try {
            int idSolicitud = Integer.parseInt(req.getParameter("idSolicitud"));
            String nuevoEstado = req.getParameter("estado"); // APROBADA, RECHAZADA
            boolean actualizado = new SolicitudDAO().cambiarEstado(idSolicitud, nuevoEstado, idUsuario);
            req.getSession().setAttribute(actualizado ? "exitoSolicitudes" : "errorSolicitudes",
                    actualizado ? "Estado de la solicitud actualizado." : "No se encontró la solicitud indicada.");
        } catch (SQLException | NumberFormatException e) {
            req.getSession().setAttribute("errorSolicitudes", "Ocurrió un error al actualizar la solicitud.");
        }
        resp.sendRedirect(req.getContextPath() + "/dashboard/inmobiliaria/solicitudes");
    }
}