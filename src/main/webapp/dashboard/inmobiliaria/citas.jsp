<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    request.setAttribute("errorCitas", session.getAttribute("errorCitas"));
    request.setAttribute("exitoCitas", session.getAttribute("exitoCitas"));
    session.removeAttribute("errorCitas");
    session.removeAttribute("exitoCitas");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Citas recibidas | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>
<div class="container py-4">
    <h2 class="mb-4 titulo-seccion">Citas agendadas sobre mis propiedades</h2>

    <c:if test="${not empty errorCitas}"><div class="alert alert-danger">${errorCitas}</div></c:if>
    <c:if test="${not empty exitoCitas}"><div class="alert alert-success">${exitoCitas}</div></c:if>

    <c:choose>
        <c:when test="${empty citas}">
            <div class="alert alert-info mt-3">No hay citas agendadas sobre tus propiedades.</div>
        </c:when>
        <c:otherwise>
            <div class="tabla-admin-wrapper mt-3">
                <div class="table-responsive">
                    <table class="table align-middle mb-0">
                        <thead>
                            <tr><th>Propiedad</th><th>Cliente</th><th>Fecha y hora</th><th>Estado</th><th>Observación</th><th></th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${citas}">
                                <tr>
                                    <td>${c.tituloPropiedad}</td>
                                    <td>${c.nombreClienteCompleto}<br><small class="text-muted">${c.correoCliente}</small></td>
                                    <td>${c.fechaHora}</td>
                                    <td>
                                        <span class="badge
                                            ${c.estado == 'CONFIRMADA' ? 'badge-confirmada' :
                                              c.estado == 'PENDIENTE' ? 'badge-pendiente' :
                                              c.estado == 'REALIZADA' ? 'badge-realizada' : 'badge-rechazada'}">
                                            ${c.estado}
                                        </span>
                                    </td>
                                    <td>${c.observacion}</td>
                                    <td class="text-nowrap">
                                        <c:if test="${c.estado == 'PENDIENTE'}">
                                            <form method="post" action="<%= request.getContextPath() %>/dashboard/inmobiliaria/citas" class="d-inline">
                                                <input type="hidden" name="idCita" value="${c.idCita}">
                                                <input type="hidden" name="estado" value="CONFIRMADA">
                                                <button type="submit" class="btn btn-sm btn-success">Confirmar</button>
                                            </form>
                                            <form method="post" action="<%= request.getContextPath() %>/dashboard/inmobiliaria/citas" class="d-inline">
                                                <input type="hidden" name="idCita" value="${c.idCita}">
                                                <input type="hidden" name="estado" value="RECHAZADA">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">Rechazar</button>
                                            </form>
                                        </c:if>
                                        <c:if test="${c.estado == 'CONFIRMADA'}">
                                            <form method="post" action="<%= request.getContextPath() %>/dashboard/inmobiliaria/citas" class="d-inline">
                                                <input type="hidden" name="idCita" value="${c.idCita}">
                                                <input type="hidden" name="estado" value="REALIZADA">
                                                <button type="submit" class="btn btn-sm btn-outline-primary">Marcar realizada</button>
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