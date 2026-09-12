<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catálogo de propiedades | InmoSantander</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/estilos.css">
</head>
<body>

<%@ include file="/WEB-INF/topbar-publico.jspf" %>

<div class="container py-4">
    <h2 class="mb-4 titulo-seccion">Catálogo de propiedades</h2>
    <form method="get" action="<%= request.getContextPath() %>/catalogo" class="row g-2 bg-white p-3 rounded shadow-sm mb-4">
        <div class="col-md-2">
            <label class="form-label small text-muted">Ciudad</label>
            <select class="form-select" name="ciudad">
                <option value="">Todas</option>
                <c:forEach var="c" items="${ciudades}">
                    <option value="${c.id}" ${filtroCiudad == c.id ? 'selected' : ''}>${c.nombre}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2">
            <label class="form-label small text-muted">Tipo</label>
            <select class="form-select" name="tipo">
                <option value="">Todos</option>
                <c:forEach var="t" items="${tipos}">
                    <option value="${t.id}" ${filtroTipo == t.id ? 'selected' : ''}>${t.nombre}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2">
            <label class="form-label small text-muted">Característica</label>
            <select class="form-select" name="caracteristica">
                <option value="">Cualquiera</option>
                <c:forEach var="carac" items="${caracteristicas}">
                    <option value="${carac.idCaracteristica}" ${filtroCaracteristica == carac.idCaracteristica ? 'selected' : ''}>${carac.nombre}</option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2">
           <label class="form-label small text-muted">Precio mínimo</label>
           <select class="form-select" name="precioMin">
              <option value="" ${empty filtroPrecioMin ? 'selected' : ''}>Sin mínimo</option>
              <option value="500000" ${filtroPrecioMin == 500000 ? 'selected' : ''}>$500.000</option>
              <option value="600000" ${filtroPrecioMin == 600000 ? 'selected' : ''}>$600.000</option>
              <option value="700000" ${filtroPrecioMin == 700000 ? 'selected' : ''}>$700.000</option>
              <option value="800000" ${filtroPrecioMin == 800000 ? 'selected' : ''}>$80.000</option>
              <option value="900000" ${filtroPrecioMin == 900000 ? 'selected' : ''}>$900.000</option>
              <option value="1000000" ${filtroPrecioMin == 1000000 ? 'selected' : ''}>$1.000.000</option>
              <option value="5000000" ${filtroPrecioMin == 5000000 ? 'selected' : ''}>$5.000.000</option>
              <option value="10000000" ${filtroPrecioMin == 10000000 ? 'selected' : ''}>$10.000.000</option>
              <option value="20000000" ${filtroPrecioMin == 20000000 ? 'selected' : ''}>$20.000.000</option>
          </select>
        </div>
        <div class="col-md-2">
           <label class="form-label small text-muted">Precio máximo</label>
           <select class="form-select" name="precioMax">
              <option value="" ${empty filtroPrecioMax ? 'selected' : ''}>Sin máximo</option>
              <option value="3000000" ${filtroPrecioMax == 3000000 ? 'selected' : ''}>$3.000.000</option>
              <option value="10000000" ${filtroPrecioMax == 10000000 ? 'selected' : ''}>$10.000.000</option>
              <option value="20000000" ${filtroPrecioMax == 20000000 ? 'selected' : ''}>$20.000.000</option>
              <option value="100000000" ${filtroPrecioMax == 100000000 ? 'selected' : ''}>$100.000.000</option>
              <option value="200000000" ${filtroPrecioMax == 200000000 ? 'selected' : ''}>$200.000.000</option>
              <option value="300000000" ${filtroPrecioMax == 300000000 ? 'selected' : ''}>$300.000.000</option>
              <option value="500000000" ${filtroPrecioMax == 500000000 ? 'selected' : ''}>$500.000.000</option>
              <option value="700000000" ${filtroPrecioMax == 700000000 ? 'selected' : ''}>$700.000.000</option>
              <option value="1000000000" ${filtroPrecioMax == 1000000000 ? 'selected' : ''}>$1.000.000.000</option>
              <option value="1500000000" ${filtroPrecioMax == 1500000000 ? 'selected' : ''}>$1.500.000.000</option>
          </select>
        </div>
        <div class="col-md-2 d-flex align-items-end">
            <button type="submit" class="btn btn-personalizado w-100">Filtrar</button>
        </div>
    </form>

    <c:choose>
        <c:when test="${empty propiedades}">
            <div class="alert alert-info">No se encontraron propiedades disponibles con los filtros seleccionados.</div>
        </c:when>
        <c:otherwise>
            <p class="text-muted">${fn:length(propiedades)} propiedad(es) encontrada(s).</p>
            <div class="row g-4">
                <c:forEach var="p" items="${propiedades}">
                    <div class="col-md-4">
                        <div class="card h-100 shadow-sm">
                            <c:choose>
                                <c:when test="${fn:startsWith(p.urlPortada, 'http://') or fn:startsWith(p.urlPortada, 'https://')}">
                                    <%-- URL externa (ej. Unsplash): se usa tal cual, sin anteponer el contextPath --%>
                                    <img src="${p.urlPortada}" class="card-img-top" style="height:180px;object-fit:cover" alt="${p.titulo}">
                                </c:when>
                                <c:otherwise>
                                    <%-- Ruta local dentro del proyecto (ej. /img/propiedades/...): se antepone el contextPath --%>
                                    <img src="<%= request.getContextPath() %>${p.urlPortada}" class="card-img-top" style="height:180px;object-fit:cover" alt="${p.titulo}">
                                </c:otherwise>
                            </c:choose>
                            <div class="card-body d-flex flex-column">
                                <span class="badge bg-secondary align-self-start mb-2">${p.nombreTipoPropiedad}</span>
                                <h5 class="card-title">${p.titulo}</h5>
                                <p class="card-text text-muted mb-1">${p.nombreCiudad}<c:if test="${not empty p.areaM2}"> · ${p.areaM2} m²</c:if></p>
                                <p class="card-text text-muted small">Inmobiliaria: ${p.nombreComercialInmobiliaria}</p>
                                <p class="fw-bold fs-5 mt-auto">
                                    <fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                </p>
                                <a href="<%= request.getContextPath() %>/propiedad?id=${p.idPropiedad}" class="btn btn-outline-dark w-100">Ver detalle</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<footer style="background-color: #0b3d66;" class="text-white text-center py-3 mt-5">
    <p class="mb-0">&copy; 2026 Sweet Home - Proyecto académico UTS</p>
</footer>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

