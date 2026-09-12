package com.inmobiliaria.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Sirve los archivos que los clientes radican como soporte de sus solicitudes
 * (documento_solicitud.url_archivo apunta a /uploads/{nombre}). Requiere sesión
 * activa: el detalle de la ruta no expone documentos a un visitante anónimo.
 */
@WebServlet("/uploads/*")
public class DescargaDocumentoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        if (sesion == null || sesion.getAttribute("idUsuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/acceso-denegado.jsp");
            return;
        }

        String nombreArchivo = req.getPathInfo(); // "/{uuid}.ext"
        if (nombreArchivo == null || nombreArchivo.contains("..")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String directorio = getServletContext().getInitParameter("upload.dir");
        Path archivo = Path.of(directorio, nombreArchivo.substring(1));

        if (!Files.exists(archivo)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.setContentType(getServletContext().getMimeType(archivo.toString()));
        try (InputStream in = Files.newInputStream(archivo)) {
            in.transferTo(resp.getOutputStream());
        }
    }
}