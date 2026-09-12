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
    <title>Mis solicitudes | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>

<div class="panel-header">
    <div class="container">
        <span class="panel-eyebrow">CLIENTE</span>
        <h2>Mis solicitudes de compra / arriendo</h2>
    </div>
</div>

<div class="container">

    <c:if test="${not empty errorSolicitudes}"><div class="alert alert-danger">${errorSolicitudes}</div></c:if>
    <c:if test="${not empty exitoSolicitudes}"><div class="alert alert-success">${exitoSolicitudes}</div></c:if>

    <div class="cta-login mb-4 d-block w-100">
        <span style="font-size: 1.5rem;">💡</span>
        <span>Para radicar una nueva solicitud, entra a la ficha de la propiedad desde el
            <a href="<%= request.getContextPath() %>/catalogo">catálogo</a> y usa el botón "Solicitar compra o arriendo".</span>
    </div>

    <c:choose>
        <c:when test="${empty solicitudes}">
            <div class="alert alert-info">Aún no has radicado solicitudes.</div>
        </c:when>
        <c:otherwise>
            <div class="row g-3">
                <c:forEach var="s" items="${solicitudes}">
                    <div class="col-md-6">
                        <div class="card card-solicitud">
                            <div class="card-body">
                                <h5 class="card-title">${s.tituloPropiedad}</h5>
                                <p class="mb-1">Trámite: <strong>${s.tipo}</strong></p>
                                <p class="mb-1">Fecha: ${s.fechaSolicitud}</p>
                                <p class="mb-2">
                                    Estado:
                                    <span class="badge
                                        ${s.estado == 'APROBADA' ? 'badge-aprobada' :
                                          s.estado == 'RECHAZADA' ? 'badge-rechazada' : 'badge-en-revision'}">
                                        ${s.estado}
                                    </span>
                                </p>

                                <h6 class="small text-muted">Documentos adjuntos</h6>
                                <c:choose>
                                    <c:when test="${empty s.documentos}">
                                        <p class="small text-muted">Sin documentos adjuntos.</p>
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
                                    <form method="post" action="<%= request.getContextPath() %>/dashboard/cliente/solicitudes"
                                          enctype="multipart/form-data" class="d-flex gap-2 mt-2">
                                        <input type="hidden" name="accion" value="adjuntar">
                                        <input type="hidden" name="idSolicitud" value="${s.idSolicitud}">
                                        <input type="file" name="documento" class="form-control form-control-sm" required>
                                        <button type="submit" class="btn btn-sm btn-personalizado">Adjuntar</button>
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