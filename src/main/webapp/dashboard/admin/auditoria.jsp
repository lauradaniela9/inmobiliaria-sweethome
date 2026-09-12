<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Auditoría | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>
<div class="container py-4">
    <h2 class="mb-4 titulo-seccion">Auditoría de accesos y cambios</h2>

    <c:choose>
        <c:when test="${empty eventos}">
            <div class="alert alert-info mt-3">No hay eventos de auditoría registrados.</div>
        </c:when>
        <c:otherwise>
            <div class="tabla-admin-wrapper tabla-auditoria mt-3">
                <div class="table-responsive">
                    <table class="table table-sm align-middle mb-0">
                        <thead><tr><th>Fecha</th><th>Usuario</th><th>Acción</th><th>IP</th></tr></thead>
                        <tbody>
                            <c:forEach var="ev" items="${eventos}">
                                <tr>
                                    <td>${ev.fecha}</td>
                                    <td>${empty ev.correo ? 'Desconocido' : ev.correo}</td>
                                    <td class="${fn:contains(ev.accion, 'fallido') ? 'accion-fallido' : fn:contains(ev.accion, 'exitoso') ? 'accion-login' : ''}">${ev.accion}</td>
                                    <td>${ev.ip}</td>
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
