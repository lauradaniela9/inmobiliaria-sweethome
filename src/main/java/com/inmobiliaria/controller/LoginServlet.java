package com.inmobiliaria.controller;

import com.inmobiliaria.dao.UsuarioDAO;
import com.inmobiliaria.model.Usuario;
import com.inmobiliaria.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final int MAX_INTENTOS = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String correo     = req.getParameter("correo");
        String contrasena = req.getParameter("contrasena");
        UsuarioDAO dao = new UsuarioDAO();

        try {
            Usuario usuario = dao.buscarPorCorreo(correo);

            // Si el correo no existe, no hay nada más que revisar: credenciales inválidas.
            if (usuario == null) {
                dao.registrarAuditoria(null, "Intento de inicio de sesión fallido: " + correo, req.getRemoteAddr());
                req.setAttribute("error", "Correo o contraseña incorrectos.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            // El estado de la cuenta se revisa ANTES de validar la contraseña, para que
            // el mensaje de bloqueo/inactividad aparezca sin importar qué contraseña se escriba.
            // dao.sigueBloqueado() de paso desbloquea automáticamente la cuenta en la base de
            // datos (y en este objeto en memoria) si ya pasaron los 30 minutos de espera.
            if (dao.sigueBloqueado(usuario)) {
                dao.registrarAuditoria(usuario.getIdUsuario(),
                        "Intento de inicio de sesión en cuenta bloqueada: " + correo, req.getRemoteAddr());
                req.setAttribute("error", "Tu cuenta está bloqueada temporalmente por múltiples intentos fallidos. Intenta de nuevo en unos minutos o contacta al administrador.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }
            if ("INACTIVO".equals(usuario.getEstado())) {
                req.setAttribute("error", "Tu cuenta se encuentra inactiva. Contacta al administrador.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            // Solo si la cuenta está en un estado válido (ACTIVO, o recién auto-desbloqueada) se evalúa la contraseña.
            if (!PasswordUtil.verificar(contrasena, usuario.getContrasenaHash())) {
                dao.incrementarIntentosFallidos(usuario.getIdUsuario());
                dao.registrarAuditoria(usuario.getIdUsuario(),
                        "Intento de inicio de sesión fallido: " + correo, req.getRemoteAddr());
                req.setAttribute("error", "Correo o contraseña incorrectos.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            // Login exitoso: reiniciar contador y crear sesión
            dao.reiniciarIntentosFallidos(usuario.getIdUsuario());
            dao.registrarAuditoria(usuario.getIdUsuario(), "Inicio de sesión exitoso", req.getRemoteAddr());

            HttpSession sesion = req.getSession(true);
            sesion.setAttribute("idUsuario", usuario.getIdUsuario());
            sesion.setAttribute("correo", usuario.getCorreo());
            sesion.setAttribute("nombres", usuario.getNombres());
            sesion.setAttribute("roles", usuario.getRoles());       // lista completa de roles
            sesion.setAttribute("rolPrincipal", usuario.getRolPrincipal());
            sesion.setMaxInactiveInterval(30 * 60); // 30 minutos

            // Redirección automática según el rol (requisito obligatorio)
            String destino;
            switch (usuario.getRolPrincipal()) {
                case "ADMINISTRADOR": destino = "/dashboard/admin.jsp"; break;
                case "INMOBILIARIA":  destino = "/dashboard/inmobiliaria.jsp"; break;
                default:              destino = "/dashboard/cliente.jsp";
            }
            resp.sendRedirect(req.getContextPath() + destino);

        } catch (SQLException e) {
            req.setAttribute("error", "Ocurrió un error al validar las credenciales. Intente nuevamente.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}