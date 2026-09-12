package com.inmobiliaria.controller;

import com.inmobiliaria.dao.PerfilDAO;
import com.inmobiliaria.model.Perfil;
import com.inmobiliaria.util.Validaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Edición del perfil personal (datos 1:1 con usuario). Ruta /dashboard/perfil:
 * al no empezar por /admin, /inmobiliaria ni /cliente, el AuthFilter solo
 * exige que exista una sesión activa, sin importar el rol.
 */
@WebServlet("/dashboard/perfil")
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idUsuario = (int) sesion.getAttribute("idUsuario");
        try {
            req.setAttribute("perfil", new PerfilDAO().buscarPorUsuario(idUsuario));
            req.getRequestDispatcher("/dashboard/perfil.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar tu perfil.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idUsuario = (int) sesion.getAttribute("idUsuario");

        String nombres = req.getParameter("nombres");
        String apellidos = req.getParameter("apellidos");
        String documento = req.getParameter("documento");
        String telefono = req.getParameter("telefono");
        String direccion = req.getParameter("direccion");
        String foto = req.getParameter("foto");

        if (!Validaciones.esCadenaObligatoriaValida(nombres) || !Validaciones.esCadenaObligatoriaValida(apellidos)
                || !Validaciones.esCadenaObligatoriaValida(documento)) {
            req.setAttribute("error", "Nombres, apellidos y documento son obligatorios.");
            reenviarConDatos(req, resp, idUsuario, nombres, apellidos, documento, telefono, direccion, foto);
            return;
        }
        if (!Validaciones.esTelefonoValido(telefono)) {
            req.setAttribute("error", "El teléfono debe tener entre 7 y 10 dígitos numéricos.");
            reenviarConDatos(req, resp, idUsuario, nombres, apellidos, documento, telefono, direccion, foto);
            return;
        }

        Perfil perfil = new Perfil();
        perfil.setIdUsuario(idUsuario);
        perfil.setNombres(nombres.trim());
        perfil.setApellidos(apellidos.trim());
        perfil.setDocumento(documento.trim());
        perfil.setTelefono(telefono);
        perfil.setDireccion(direccion);
        perfil.setFoto(foto);

        try {
            new PerfilDAO().actualizar(perfil);
            sesion.setAttribute("nombres", perfil.getNombres()); // refresca el saludo del header
            req.setAttribute("exito", "Perfil actualizado correctamente.");
            req.setAttribute("perfil", perfil);
            req.getRequestDispatcher("/dashboard/perfil.jsp").forward(req, resp);
        } catch (PerfilDAO.DocumentoDuplicadoException dup) {
            req.setAttribute("error", dup.getMessage());
            req.setAttribute("perfil", perfil);
            req.getRequestDispatcher("/dashboard/perfil.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Ocurrió un error al actualizar el perfil. Intente nuevamente.");
            req.setAttribute("perfil", perfil);
            req.getRequestDispatcher("/dashboard/perfil.jsp").forward(req, resp);
        }
    }

    private void reenviarConDatos(HttpServletRequest req, HttpServletResponse resp, int idUsuario, String nombres,
                                   String apellidos, String documento, String telefono, String direccion, String foto)
            throws ServletException, IOException {
        Perfil perfil = new Perfil();
        perfil.setIdUsuario(idUsuario);
        perfil.setNombres(nombres);
        perfil.setApellidos(apellidos);
        perfil.setDocumento(documento);
        perfil.setTelefono(telefono);
        perfil.setDireccion(direccion);
        perfil.setFoto(foto);
        req.setAttribute("perfil", perfil);
        req.getRequestDispatcher("/dashboard/perfil.jsp").forward(req, resp);
    }
}