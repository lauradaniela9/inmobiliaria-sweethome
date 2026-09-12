package com.inmobiliaria.controller;

import com.inmobiliaria.dao.CitaDAO;
import com.inmobiliaria.util.Validaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/** Panel del cliente: agendar una cita para una propiedad y consultar/cancelar las propias. */
@WebServlet("/dashboard/cliente/citas")
public class CitaClienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idCliente = (int) sesion.getAttribute("idUsuario");
        try {
            req.setAttribute("citas", new CitaDAO().listarPorCliente(idCliente));
            req.getRequestDispatcher("/dashboard/cliente/citas.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar tus citas.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idCliente = (int) sesion.getAttribute("idUsuario");
        String accion = req.getParameter("accion");
        CitaDAO dao = new CitaDAO();

        try {
            if ("agendar".equals(accion)) {
                Integer idPropiedad = Validaciones.parsearEntero(req.getParameter("idPropiedad"));
                String fechaHora = req.getParameter("fechaHora");
                String observacion = req.getParameter("observacion");

                if (idPropiedad == null || !Validaciones.esCadenaObligatoriaValida(fechaHora)) {
                    req.getSession().setAttribute("errorCitas", "Debes indicar la propiedad y el horario de la visita.");
                } else {
                    dao.agendar(idPropiedad, idCliente, fechaHora, observacion);
                    req.getSession().setAttribute("exitoCitas", "Cita agendada. Queda pendiente de confirmación por la inmobiliaria.");
                }
            } else if ("cancelar".equals(accion)) {
                int idCita = Integer.parseInt(req.getParameter("idCita"));
                dao.cancelar(idCita, idCliente);
                req.getSession().setAttribute("exitoCitas", "Cita cancelada correctamente.");
            }
        } catch (CitaDAO.HorarioOcupadoException conflicto) {
            req.getSession().setAttribute("errorCitas", conflicto.getMessage());
        } catch (SQLException | NumberFormatException e) {
            req.getSession().setAttribute("errorCitas", "Ocurrió un error al procesar la solicitud de cita.");
        }
        resp.sendRedirect(req.getContextPath() + "/dashboard/cliente/citas");
    }
}