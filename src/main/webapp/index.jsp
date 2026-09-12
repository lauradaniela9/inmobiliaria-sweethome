<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.inmobiliaria.dao.PropiedadDAO" %>
<%@ page import="com.inmobiliaria.model.Opcion" %>
<%@ page import="com.inmobiliaria.model.Propiedad" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    // Landing page dinámica: los combos del buscador rápido usan los IDs reales
    // de la base de datos y las destacadas son las 3 publicaciones más recientes.
    List<Opcion> ciudades;
    List<Opcion> tipos;
    List<Propiedad> destacadas;
    try {
        PropiedadDAO dao = new PropiedadDAO();
        ciudades = dao.listarCiudades();
        tipos = dao.listarTiposPropiedad();
        List<Propiedad> disponibles = dao.buscarConFiltros(null, null, null, null, null);
        destacadas = disponibles.subList(0, Math.min(3, disponibles.size()));
    } catch (Exception e) {
        ciudades = Collections.emptyList();
        tipos = Collections.emptyList();
        destacadas = Collections.emptyList();
    }
    pageContext.setAttribute("ciudades", ciudades);
    pageContext.setAttribute("tipos", tipos);
    pageContext.setAttribute("destacadas", destacadas);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sweet Home | Inicio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/estilos.css">
</head>
<body>

<%@ include file="/WEB-INF/topbar-publico.jspf" %>

<header class="hero text-white text-center d-flex align-items-center">
    <div class="container">
        <h1 class="display-5 fw-bold">Encuentra el inmueble ideal en Sweet Home</h1>
        <p class="lead">Casas, apartamentos, locales, oficinas y terrenos, todo en un solo lugar.</p>

        <form id="buscador" class="row g-2 justify-content-center bg-light p-3 rounded shadow-sm mt-4"
              method="get" action="<%= request.getContextPath() %>/catalogo">
            <div class="col-md-3">
                <select class="form-select" name="ciudad">
                    <option value="">Ciudad</option>
                    <c:forEach var="c" items="${ciudades}">
                        <option value="${c.id}">${c.nombre}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <select class="form-select" name="tipo">
                    <option value="">Tipo de propiedad</option>
                    <c:forEach var="t" items="${tipos}">
                        <option value="${t.id}">${t.nombre}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <input type="number" class="form-control" name="precioMax" placeholder="Precio máximo">
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-dark w-100">Buscar</button>
            </div>
        </form>
    </div>
</header>

<section id="destacadas" class="container py-5">
    <h2 class="text-center mb-4">Propiedades destacadas</h2>
    <c:choose>
        <c:when test="${empty destacadas}">
            <p class="text-center text-muted">Aún no hay propiedades publicadas en el catálogo.</p>
        </c:when>
        <c:otherwise>
            <div class="row g-4">
                <c:forEach var="p" items="${destacadas}">
                    <div class="col-md-4">
                        <a href="<%= request.getContextPath() %>/propiedad?id=${p.idPropiedad}" class="text-decoration-none text-dark">
                            <div class="card h-100 shadow-sm">
                                <img src="<%= request.getContextPath() %>${p.urlPortada}" class="card-img-top" style="height:180px;object-fit:cover" alt="${p.titulo}">
                                <div class="card-body">
                                    <h5 class="card-title">${p.titulo}</h5>
                                    <p class="card-text text-muted">${p.nombreCiudad} · ${p.nombreTipoPropiedad}</p>
                                    <p class="fw-bold">
                                        <fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                    </p>
                                </div>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
    <p class="text-center mt-4"><a href="<%= request.getContextPath() %>/catalogo" class="btn btn-outline-dark">Ver catálogo completo</a></p>
</section>

<footer class="navbar-brand-custom text-white text-center py-3">
    <p class="mb-0">&copy; 2026 Sweet Home - Proyecto académico UTS</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
