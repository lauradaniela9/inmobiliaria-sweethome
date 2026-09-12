package com.inmobiliaria.controller;

import com.inmobiliaria.dao.PropiedadDAO;
import com.inmobiliaria.model.Propiedad;
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
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Formulario de creación y edición de propiedades (CRUD), con imágenes (1:N)
 * y características (N:M). Ruta protegida: solo el rol INMOBILIARIA.
 *
 * Cada imagen puede llegar como URL externa (pegada por el usuario) o como
 * archivo subido desde el computador; @MultipartConfig habilita ambos casos
 * en la misma petición.
 */
@WebServlet("/dashboard/inmobiliaria/propiedad-form")
@MultipartConfig(
        maxFileSize = 5 * 1024 * 1024,       // 5 MB por imagen
        maxRequestSize = 25 * 1024 * 1024    // 25 MB por petición completa
)
public class PropiedadFormServlet extends HttpServlet {

    /** Carpeta (relativa al contexto web) donde se guardan las imágenes subidas como archivo. */
    private static final String CARPETA_IMAGENES = "/img/propiedades";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PropiedadDAO dao = new PropiedadDAO();
        try {
            req.setAttribute("ciudades", dao.listarCiudades());
            req.setAttribute("tipos", dao.listarTiposPropiedad());
            req.setAttribute("caracteristicas", dao.listarCaracteristicas());

            Integer id = Validaciones.parsearEntero(req.getParameter("id"));
            if (id != null) {
                HttpSession sesion = req.getSession(false);
                int idUsuario = (int) sesion.getAttribute("idUsuario");
                if (!dao.perteneceAInmobiliaria(id, idUsuario)) {
                    resp.sendRedirect(req.getContextPath() + "/acceso-denegado.jsp");
                    return;
                }
                req.setAttribute("propiedad", dao.buscarPorId(id));
            }
            req.getRequestDispatcher("/dashboard/inmobiliaria/propiedad-form.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "No fue posible cargar el formulario de la propiedad.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        int idUsuarioInmobiliaria = (int) sesion.getAttribute("idUsuario");
        PropiedadDAO dao = new PropiedadDAO();

        Integer id = Validaciones.parsearEntero(req.getParameter("idPropiedad"));
        String matricula = req.getParameter("matriculaInmobiliaria");
        String titulo = req.getParameter("titulo");
        String descripcion = req.getParameter("descripcion");
        BigDecimal precio = Validaciones.parsearDecimal(req.getParameter("precio"));
        String direccion = req.getParameter("direccion");
        BigDecimal area = Validaciones.parsearDecimal(req.getParameter("areaM2"));
        Integer idCiudad = Validaciones.parsearEntero(req.getParameter("idCiudad"));
        Integer idTipo = Validaciones.parsearEntero(req.getParameter("idTipoPropiedad"));
        String estado = req.getParameter("estado");
        String[] caracteristicasSeleccionadas = req.getParameterValues("caracteristica");

        // Validaciones de campos obligatorios y formato (requisito del enunciado)
        if (!Validaciones.esCadenaObligatoriaValida(matricula) || !Validaciones.esCadenaObligatoriaValida(titulo)
                || !Validaciones.esCadenaObligatoriaValida(direccion) || idCiudad == null || idTipo == null) {
            req.setAttribute("error", "Todos los campos obligatorios deben diligenciarse.");
            reenviarFormulario(req, resp, dao);
            return;
        }
        if (!Validaciones.esPrecioValido(precio)) {
            req.setAttribute("error", "El precio debe ser un valor numérico mayor a cero.");
            reenviarFormulario(req, resp, dao);
            return;
        }

        Propiedad p = new Propiedad();
        p.setIdUsuarioInmobiliaria(idUsuarioInmobiliaria);
        p.setIdCiudad(idCiudad);
        p.setIdTipoPropiedad(idTipo);
        p.setMatriculaInmobiliaria(matricula.trim());
        p.setTitulo(titulo.trim());
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setDireccion(direccion.trim());
        p.setAreaM2(area);
        p.setEstado(estado != null ? estado : "DISPONIBLE");

        List<String> imagenes;
        try {
            imagenes = procesarImagenes(req);
        } catch (IOException e) {
            req.setAttribute("error", "Ocurrió un error al procesar las imágenes. Intenta nuevamente.");
            reenviarFormulario(req, resp, dao);
            return;
        }

        List<Integer> caracteristicas = new ArrayList<>();
        if (caracteristicasSeleccionadas != null) {
            for (String c : caracteristicasSeleccionadas) {
                Integer valor = Validaciones.parsearEntero(c);
                if (valor != null) caracteristicas.add(valor);
            }
        }

        try {
            if (id == null) {
                dao.crear(p, imagenes, caracteristicas);
                req.getSession().setAttribute("exitoPropiedad", "Propiedad publicada correctamente.");
            } else {
                if (!dao.perteneceAInmobiliaria(id, idUsuarioInmobiliaria)) {
                    resp.sendRedirect(req.getContextPath() + "/acceso-denegado.jsp");
                    return;
                }
                p.setIdPropiedad(id);
                dao.actualizar(p, imagenes, caracteristicas);
                req.getSession().setAttribute("exitoPropiedad", "Propiedad actualizada correctamente.");
            }
            resp.sendRedirect(req.getContextPath() + "/dashboard/inmobiliaria/propiedades");

        } catch (PropiedadDAO.MatriculaDuplicadaException dup) {
            // Mensaje claro al usuario en lugar de una excepción cruda (restricción UNIQUE)
            req.setAttribute("error", dup.getMessage());
            req.setAttribute("propiedad", p);
            reenviarFormulario(req, resp, dao);
        } catch (SQLException e) {
            req.setAttribute("error", "Ocurrió un error al guardar la propiedad. Intente nuevamente.");
            req.setAttribute("propiedad", p);
            reenviarFormulario(req, resp, dao);
        }
    }

    /**
     * Recorre las filas de imagen enviadas por el formulario (tipoImagen_0, tipoImagen_1, ...)
     * y arma la lista final en el mismo orden en que aparecen en el formulario, ya que la
     * primera imagen no vacía queda como portada (ver PropiedadDAO.insertarImagenes).
     * Cada fila puede ser una URL pegada por el usuario o un archivo subido desde su equipo.
     */
    private List<String> procesarImagenes(HttpServletRequest req) throws IOException, ServletException {
        List<String> imagenes = new ArrayList<>();
        int i = 0;
        while (true) {
            String tipo = req.getParameter("tipoImagen_" + i);
            if (tipo == null) break; // no hay más filas de imagen en el formulario

            if ("archivo".equals(tipo)) {
                Part parte = req.getPart("archivoImagen_" + i);
                if (parte != null && parte.getSize() > 0) {
                    String rutaGuardada = guardarArchivoImagen(parte);
                    if (rutaGuardada != null) imagenes.add(rutaGuardada);
                }
            } else {
                String url = req.getParameter("urlImagen_" + i);
                if (url != null && !url.trim().isEmpty()) imagenes.add(url.trim());
            }
            i++;
        }
        return imagenes;
    }

    /**
     * Guarda el archivo subido dentro de /img/propiedades con un nombre único,
     * y devuelve la ruta relativa que se almacena en imagen_propiedad.url_imagen
     * (mismo formato que las imágenes ya existentes, ej. "/img/propiedades/5_a.jpg").
     */
    private String guardarArchivoImagen(Part parte) throws IOException {
        String nombreOriginal = parte.getSubmittedFileName();
        String extension = "";
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            extension = nombreOriginal.substring(nombreOriginal.lastIndexOf('.'));
        }
        String nombreArchivo = UUID.randomUUID() + extension;

        String directorioReal = getServletContext().getRealPath(CARPETA_IMAGENES);
        if (directorioReal == null) return null; // despliegue no explota a disco (WAR empaquetado); no se puede guardar

        File directorio = new File(directorioReal);
        if (!directorio.exists()) directorio.mkdirs();

        File destino = new File(directorio, nombreArchivo);
        try (InputStream entrada = parte.getInputStream()) {
            Files.copy(entrada, destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return CARPETA_IMAGENES + "/" + nombreArchivo;
    }

    private void reenviarFormulario(HttpServletRequest req, HttpServletResponse resp, PropiedadDAO dao)
            throws ServletException, IOException {
        try {
            req.setAttribute("ciudades", dao.listarCiudades());
            req.setAttribute("tipos", dao.listarTiposPropiedad());
            req.setAttribute("caracteristicas", dao.listarCaracteristicas());
        } catch (SQLException ignored) {
            // Si fallan los catálogos, el formulario igual se muestra con el mensaje de error principal.
        }
        req.getRequestDispatcher("/dashboard/inmobiliaria/propiedad-form.jsp").forward(req, resp);
    }
}