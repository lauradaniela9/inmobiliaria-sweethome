<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    request.setAttribute("errorPropiedad", session.getAttribute("errorPropiedad"));
    request.setAttribute("exitoPropiedad", session.getAttribute("exitoPropiedad"));
    session.removeAttribute("errorPropiedad");
    session.removeAttribute("exitoPropiedad");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis propiedades | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0 titulo-seccion">Mis propiedades publicadas</h2>
        <a href="<%= request.getContextPath() %>/dashboard/inmobiliaria/propiedad-form" class="btn btn-personalizado">+ Publicar propiedad</a>
    </div>

    <c:if test="${not empty errorPropiedad}"><div class="alert alert-danger">${errorPropiedad}</div></c:if>
    <c:if test="${not empty exitoPropiedad}"><div class="alert alert-success">${exitoPropiedad}</div></c:if>

    <c:choose>
        <c:when test="${empty propiedades}">
            <div class="alert alert-info">Aún no has publicado propiedades.</div>
        </c:when>
        <c:otherwise>
            <div class="tabla-admin-wrapper">
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead>
                            <tr>
                                <th>Matrícula</th><th>Título</th><th>Ciudad</th><th>Tipo</th>
                                <th>Precio</th><th>Estado</th><th>Citas</th><th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${propiedades}">
                                <tr>
                                    <td>${p.matriculaInmobiliaria}</td>
                                    <td>${p.titulo}</td>
                                    <td>${p.nombreCiudad}</td>
                                    <td>${p.nombreTipoPropiedad}</td>
                                    <td><fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="$" maxFractionDigits="0"/></td>
                                    <td>
                                        <span class="badge ${p.estado == 'DISPONIBLE' ? 'badge-disponible' : p.estado == 'INACTIVA' ? 'badge-inactiva' : 'badge-otro-estado'}">${p.estado}</span>
                                    </td>
                                    <td>${p.totalCitas}</td>
                                    <td class="text-nowrap">
                                        <a href="<%= request.getContextPath() %>/propiedad?id=${p.idPropiedad}" class="btn btn-sm btn-outline-secondary" target="_blank">Ver</a>
                                        <a href="<%= request.getContextPath() %>/dashboard/inmobiliaria/propiedad-form?id=${p.idPropiedad}" class="btn btn-sm btn-outline-dark">Editar</a>
                                        <c:if test="${p.estado != 'INACTIVA'}">
                                            <form method="post" action="<%= request.getContextPath() %>/dashboard/inmobiliaria/propiedad-eliminar"
                                                  class="d-inline" onsubmit="return confirm('¿Dar de baja esta propiedad del catálogo? (baja lógica, no se elimina el registro)');">
                                                <input type="hidden" name="id" value="${p.idPropiedad}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">Dar de baja</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>