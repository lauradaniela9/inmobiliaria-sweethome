<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    request.setAttribute("errorFavoritos", session.getAttribute("errorFavoritos"));
    session.removeAttribute("errorFavoritos");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis favoritos | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>

<div class="panel-header">
    <div class="container">
        <span class="panel-eyebrow">CLIENTE</span>
        <h2>Mis propiedades favoritas</h2>
    </div>
</div>

<div class="container">

    <c:if test="${not empty errorFavoritos}"><div class="alert alert-danger">${errorFavoritos}</div></c:if>

    <c:choose>
        <c:when test="${empty favoritos}">
            <div class="alert alert-info">Aún no has marcado propiedades como favoritas. Explora el
                <a href="<%= request.getContextPath() %>/catalogo">catálogo</a> y usa el ícono de corazón en la ficha del inmueble.</div>
        </c:when>
        <c:otherwise>
            <div class="row g-3">
                <c:forEach var="p" items="${favoritos}">
                    <div class="col-md-4">
                        <div class="card h-100 shadow-sm">
                            <div class="card-body d-flex flex-column">
                                <span class="badge bg-secondary align-self-start mb-2">${p.nombreTipoPropiedad}</span>
                                <h5 class="card-title">${p.titulo}</h5>
                                <p class="text-muted mb-1">${p.nombreCiudad}</p>
                                <p class="fw-bold">
                                    <fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                </p>
                                <div class="mt-auto d-flex gap-2">
                                    <a href="<%= request.getContextPath() %>/propiedad?id=${p.idPropiedad}" class="btn btn-outline-dark btn-sm w-100">Ver detalle</a>
                                    <form method="post" action="<%= request.getContextPath() %>/dashboard/cliente/favoritos">
                                        <input type="hidden" name="idPropiedad" value="${p.idPropiedad}">
                                        <button type="submit" class="btn btn-outline-danger btn-sm">Quitar</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>