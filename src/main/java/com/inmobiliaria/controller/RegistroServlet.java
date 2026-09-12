package com.inmobiliaria.controller;

import com.inmobiliaria.dao.UsuarioDAO;
import com.inmobiliaria.util.PasswordUtil;
import com.inmobiliaria.util.Validaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/registro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String correo      = req.getParameter("correo") != null ? req.getParameter("correo").trim() : "";
        String contrasena  = req.getParameter("contrasena");
        String confirmar   = req.getParameter("confirmar");
        String nombres     = req.getParameter("nombres");
        String apellidos   = req.getParameter("apellidos");
        String documento   = req.getParameter("documento");
        String telefono    = req.getParameter("telefono");

        // ---- Validaciones de campos obligatorios y formato ----
        if (correo.isEmpty() || contrasena == null || nombres == null || apellidos == null || documento == null) {
            req.setAttribute("error", "Todos los campos obligatorios deben diligenciarse.");
            req.getRequestDispatcher("/registro.jsp").forward(req, resp);
            return;
        }
        if (!Validaciones.esCorreoValido(correo)) {
            req.setAttribute("error", "El formato del correo electrónico no es válido.");
            req.getRequestDispatcher("/registro.jsp").forward(req, resp);
            return;
        }
        if (!Validaciones.esContrasenaValida(contrasena) || !Validaciones.coinciden(contrasena, confirmar)) {
            req.setAttribute("error", "La contraseña debe tener mínimo 8 caracteres y coincidir con la confirmación.");
            req.getRequestDispatcher("/registro.jsp").forward(req, resp);
            return;
        }
        if (!Validaciones.esTelefonoValido(telefono)) {
            req.setAttribute("error", "El teléfono debe tener entre 7 y 10 dígitos numéricos.");
            req.getRequestDispatcher("/registro.jsp").forward(req, resp);
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        try {
            String hash = PasswordUtil.cifrar(contrasena);
            dao.registrarCliente(correo, hash, nombres, apellidos, documento, telefono);
            dao.registrarAuditoria(null, "Registro de nuevo usuario: " + correo, req.getRemoteAddr());

            req.setAttribute("exito", "Cuenta creada correctamente. Ya puedes iniciar sesión.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);

        } catch (UsuarioDAO.CorreoDuplicadoException dup) {
            // Mensaje claro al usuario en lugar de una excepción cruda de Java
            req.setAttribute("error", dup.getMessage());
            req.getRequestDispatcher("/registro.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Ocurrió un error al registrar el usuario. Intente nuevamente.");
            req.getRequestDispatcher("/registro.jsp").forward(req, resp);
        }
    }
}