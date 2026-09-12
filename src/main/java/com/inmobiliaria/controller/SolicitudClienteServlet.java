package com.inmobiliaria.controller;

import com.inmobiliaria.dao.SolicitudDAO;
import com.inmobiliaria.util.Validaciones;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Panel del cliente: radicar una solicitud de compra o arriendo sobre una
 * propiedad y cargar los documentos soporte (relación 1:N solicitud -> documento).
 */
@WebServlet("/dashboard/cliente/solicitudes")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024) // 10 MB por archivo
public class SolicitudClienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idCliente = (int) sesion.getAttribute("idUsuario");
        try {
            req.setAttribute("solicitudes", new SolicitudDAO().listarPorCliente(idCliente));
            req.getRequestDispatcher("/dashboard/cliente/solicitudes.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar tus solicitudes.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idCliente = (int) sesion.getAttribute("idUsuario");
        String accion = req.getParameter("accion");
        SolicitudDAO dao = new SolicitudDAO();

        try {
            if ("crear".equals(accion)) {
                Integer idPropiedad = Validaciones.parsearEntero(req.getParameter("idPropiedad"));
                String tipo = req.getParameter("tipo"); // COMPRA / ARRIENDO
                if (idPropiedad == null || !Validaciones.esCadenaObligatoriaValida(tipo)) {
                    req.getSession().setAttribute("errorSolicitudes", "Debes indicar la propiedad y el tipo de trámite.");
                } else {
                    int idSolicitud = dao.crear(idPropiedad, idCliente, tipo);
                    guardarDocumentoSiExiste(req, dao, idSolicitud);
                    req.getSession().setAttribute("exitoSolicitudes", "Solicitud radicada. Queda en revisión por la inmobiliaria.");
                }
            } else if ("adjuntar".equals(accion)) {
                int idSolicitud = Integer.parseInt(req.getParameter("idSolicitud"));
                if (dao.perteneceACliente(idSolicitud, idCliente)) {
                    if (guardarDocumentoSiExiste(req, dao, idSolicitud)) {
                        req.getSession().setAttribute("exitoSolicitudes", "Documento adjuntado correctamente.");
                    } else {
                        req.getSession().setAttribute("errorSolicitudes", "Selecciona un archivo antes de adjuntar.");
                    }
                } else {
                    resp.sendRedirect(req.getContextPath() + "/acceso-denegado.jsp");
                    return;
                }
            }
        } catch (SQLException | NumberFormatException | IOException e) {
            req.getSession().setAttribute("errorSolicitudes", "Ocurrió un error al procesar la solicitud.");
        }
        resp.sendRedirect(req.getContextPath() + "/dashboard/cliente/solicitudes");
    }

    /** Guarda el archivo adjunto (si el usuario seleccionó uno) en el directorio configurado y registra el documento. */
    private boolean guardarDocumentoSiExiste(HttpServletRequest req, SolicitudDAO dao, int idSolicitud)
            throws IOException, SQLException, ServletException {
        Part part = req.getPart("documento");
        if (part == null || part.getSize() == 0) return false;

        String nombreOriginal = obtenerNombreArchivo(part);
        String extension = nombreOriginal.contains(".") ? nombreOriginal.substring(nombreOriginal.lastIndexOf('.')) : "";
        String nombreAlmacenado = UUID.randomUUID() + extension;

        String directorioUploads = req.getServletContext().getInitParameter("upload.dir");
        if (directorioUploads == null || directorioUploads.isBlank()) {
            directorioUploads = System.getProperty("java.io.tmpdir") + File.separator + "sweethome-uploads";
        }
        Path carpeta = Path.of(directorioUploads);
        Files.createDirectories(carpeta);
        Path destino = carpeta.resolve(nombreAlmacenado);

        try (InputStream entrada = part.getInputStream()) {
            Files.copy(entrada, destino, StandardCopyOption.REPLACE_EXISTING);
        }

        dao.agregarDocumento(idSolicitud, nombreOriginal, "/uploads/" + nombreAlmacenado);
        return true;
    }

    private String obtenerNombreArchivo(Part part) {
        String header = part.getHeader("content-disposition");
        for (String contenido : header.split(";")) {
            if (contenido.trim().startsWith("filename")) {
                return contenido.substring(contenido.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "documento";
    }
}