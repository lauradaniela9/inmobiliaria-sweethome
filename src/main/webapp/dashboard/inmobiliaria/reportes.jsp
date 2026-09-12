<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reportes | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>

<div class="container py-4">
    <div class="mb-4">
        <h2 class="titulo-seccion">Reporte de ventas y arriendos concretados</h2>
        <p class="text-muted mt-2">Resumen estadístico y detalle general de propiedades con estado <strong>VENDIDA</strong> o <strong>ARRENDADA</strong>.</p>
    </div>

    <!-- 1. Tarjetas de Agregación (GROUP BY) -->
    <c:choose>
        <c:when test="${empty ventasArriendos}">
            <div class="alert alert-info shadow-sm">Aún no tienes propiedades vendidas o arrendadas para generar estadísticas.</div>
        </c:when>
        <c:otherwise>
            <div class="row g-3 mb-5">
                <c:forEach var="fila" items="${ventasArriendos}">
                    <div class="col-md-4">
                        <div class="stat-venta">
                            <div class="stat-venta-label">${fila[0]}</div>
                            <div class="stat-venta-numero">${fila[1]}</div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- 2. Tabla Detallada -->
    <div class="tabla-admin-wrapper">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead>
                    <tr>
                        <th>Matrícula</th>
                        <th>Inmueble / Título</th>
                        <th>Ubicación (Dirección / Ciudad)</th>
                        <th>Tipo</th>
                        <th class="text-end">Precio Final</th>
                        <th class="text-center">Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty listaDetalladaVendidas}">
                            <c:forEach var="p" items="${listaDetalladaVendidas}">
                                <tr>
                                    <td class="fw-semibold">#${p.matriculaInmobiliaria}</td>
                                    <td>
                                        <div class="fw-bold text-dark">${p.titulo}</div>
                                        <small class="text-muted">Área: ${p.areaM2} m²</small>
                                    </td>
                                    <td><i class="bi bi-geo-alt-fill text-danger"></i> ${p.direccion}</td>
                                    <td><span class="badge badge-rol">${p.nombreTipoPropiedad}</span></td>
                                    <td class="text-end fw-bold" style="color: var(--brand-primary);">
                                        <fmt:formatNumber value="${p.precio}" type="currency" currencySymbol="$" maxFractionDigits="0"/>
                                    </td>
                                    <td class="text-center"><span class="badge badge-confirmada px-3 py-2">${p.estado}</span></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="text-center text-muted py-5">
                                    <i class="bi bi-folder2-open fs-2 d-block mb-2"></i>
                                    No hay registros detallados disponibles en este momento.
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>