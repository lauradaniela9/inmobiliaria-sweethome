package com.inmobiliaria.controller;

import com.inmobiliaria.dao.CitaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/** Panel de la inmobiliaria: revisar y confirmar/rechazar las citas agendadas sobre sus propiedades. */
@WebServlet("/dashboard/inmobiliaria/citas")
public class CitaInmobiliariaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idUsuario = (int) sesion.getAttribute("idUsuario");
        try {
            req.setAttribute("citas", new CitaDAO().listarPorInmobiliaria(idUsuario));
            req.getRequestDispatcher("/dashboard/inmobiliaria/citas.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar las citas de tus propiedades.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idUsuario = (int) sesion.getAttribute("idUsuario");
        try {
            int idCita = Integer.parseInt(req.getParameter("idCita"));
            String nuevoEstado = req.getParameter("estado"); // CONFIRMADA, RECHAZADA, REALIZADA
            boolean actualizado = new CitaDAO().cambiarEstado(idCita, nuevoEstado, idUsuario);
            req.getSession().setAttribute(actualizado ? "exitoCitas" : "errorCitas",
                    actualizado ? "Estado de la cita actualizado." : "No se encontró la cita indicada.");
        } catch (SQLException | NumberFormatException e) {
            req.getSession().setAttribute("errorCitas", "Ocurrió un error al actualizar la cita.");
        }
        resp.sendRedirect(req.getContextPath() + "/dashboard/inmobiliaria/citas");
    }
}