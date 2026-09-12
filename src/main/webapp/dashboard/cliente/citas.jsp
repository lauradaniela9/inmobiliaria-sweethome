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
    <title>Mis citas | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>

<div class="panel-header">
    <div class="container">
        <span class="panel-eyebrow">CLIENTE</span>
        <h2>Mis citas agendadas</h2>
    </div>
</div>

<div class="container">

    <c:if test="${not empty errorCitas}"><div class="alert alert-danger">${errorCitas}</div></c:if>
    <c:if test="${not empty exitoCitas}"><div class="alert alert-success">${exitoCitas}</div></c:if>

    <div class="cta-login mb-4 d-block w-100">
    <span style="font-size: 1.5rem;"></span>
    <span>Para agendar una nueva visita, entra a la ficha de la propiedad desde el
        <a href="<%= request.getContextPath() %>/catalogo">catálogo</a> y usa el botón "Agendar visita".</span>

    <c:choose>
        <c:when test="${empty citas}">
            <div class="alert alert-info">Aún no tienes citas agendadas.</div>
        </c:when>
        <c:otherwise>
            <div class="tabla-admin-wrapper">
                <div class="table-responsive">
                    <table class="table table-striped mb-0">
                        <thead>
                            <tr><th>Propiedad</th><th>Fecha y hora</th><th>Estado</th><th>Observación</th><th></th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${citas}">
                                <tr>
                                    <td>${c.tituloPropiedad}</td>
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
                                    <td>
                                        <c:if test="${c.estado == 'PENDIENTE' || c.estado == 'CONFIRMADA'}">
                                            <form method="post" action="<%= request.getContextPath() %>/dashboard/cliente/citas"
                                                  onsubmit="return confirm('¿Cancelar esta cita?');">
                                                <input type="hidden" name="accion" value="cancelar">
                                                <input type="hidden" name="idCita" value="${c.idCita}">
                                                <button type="submit" class="btn btn-sm btn-outline-danger">Cancelar</button>
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