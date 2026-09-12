<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    request.setAttribute("errorSolicitudes", session.getAttribute("errorSolicitudes"));
    request.setAttribute("exitoSolicitudes", session.getAttribute("exitoSolicitudes"));
    session.removeAttribute("errorSolicitudes");
    session.removeAttribute("exitoSolicitudes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitudes recibidas | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>
<div class="container py-4">
    <h2 class="mb-4 titulo-seccion">Solicitudes de compra / arriendo recibidas</h2>

    <c:if test="${not empty errorSolicitudes}"><div class="alert alert-danger">${errorSolicitudes}</div></c:if>
    <c:if test="${not empty exitoSolicitudes}"><div class="alert alert-success">${exitoSolicitudes}</div></c:if>

    <c:choose>
        <c:when test="${empty solicitudes}">
            <div class="alert alert-info mt-3">No has recibido solicitudes sobre tus propiedades.</div>
        </c:when>
        <c:otherwise>
            <div class="row g-3 mt-1">
                <c:forEach var="s" items="${solicitudes}">
                    <div class="col-md-6">
                        <div class="card card-solicitud">
                            <div class="card-body">
                                <h5 class="card-title">${s.tituloPropiedad}</h5>
                                <p class="mb-1">Cliente: ${s.nombreClienteCompleto} <small class="text-muted">(${s.correoCliente})</small></p>
                                <p class="mb-1">Trámite: <strong>${s.tipo}</strong> · Radicada: ${s.fechaSolicitud}</p>
                                <p class="mb-2">
                                    Estado:
                                    <span class="badge
                                        ${s.estado == 'APROBADA' ? 'badge-aprobada' :
                                          s.estado == 'RECHAZADA' ? 'badge-rechazada' : 'badge-en-revision'}">
                                        ${s.estado}
                                    </span>
                                </p>

                                <h6 class="small text-muted">Documentos radicados</h6>
                                <c:choose>
                                    <c:when test="${empty s.documentos}">
                                        <p class="small text-muted">El cliente aún no ha adjuntado documentos.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <ul class="list-unstyled small">
                                            <c:forEach var="d" items="${s.documentos}">
                                                <li>&#128206; <a href="<%= request.getContextPath() %>${d.urlArchivo}" target="_blank">${d.nombreArchivo}</a></li>
                                            </c:forEach>
                                        </ul>
                                    </c:otherwise>
                                </c:choose>

                                <c:if test="${s.estado == 'EN_REVISION'}">
                                    <form method="post" action="<%= request.getContextPath() %>/dashboard/inmobiliaria/solicitudes" class="d-inline">
                                        <input type="hidden" name="idSolicitud" value="${s.idSolicitud}">
                                        <input type="hidden" name="estado" value="APROBADA">
                                        <button type="submit" class="btn btn-sm btn-success">Aprobar</button>
                                    </form>
                                    <form method="post" action="<%= request.getContextPath() %>/dashboard/inmobiliaria/solicitudes" class="d-inline">
                                        <input type="hidden" name="idSolicitud" value="${s.idSolicitud}">
                                        <input type="hidden" name="estado" value="RECHAZADA">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">Rechazar</button>
                                    </form>
                                </c:if>
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
