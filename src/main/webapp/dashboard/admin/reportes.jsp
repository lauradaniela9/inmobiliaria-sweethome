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
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>
<div class="container py-4">
    <h2 class="mb-4 titulo-seccion">Reportes consolidados del sistema</h2>

    <div class="row g-4 mt-1">
        <div class="col-md-6">
            <div class="card card-reporte h-100">
                <div class="card-header">📍 Propiedades por ciudad y estado</div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty propiedadesPorCiudad}">
                            <p class="text-muted">Sin datos.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table table-sm">
                                <thead><tr><th>Ciudad</th><th>Estado</th><th>Total</th></tr></thead>
                                <tbody>
                                <c:forEach var="fila" items="${propiedadesPorCiudad}">
                                    <tr><td>${fila[0]}</td><td>${fila[1]}</td><td>${fila[2]}</td></tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card card-reporte h-100">
                <div class="card-header">💰 Precio promedio por tipo de propiedad</div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty valorPorTipo}">
                            <p class="text-muted">Sin datos.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table table-sm">
                                <thead><tr><th>Tipo</th><th>Cantidad</th><th>Precio promedio</th></tr></thead>
                                <tbody>
                                <c:forEach var="fila" items="${valorPorTipo}">
                                    <tr>
                                        <td>${fila[0]}</td>
                                        <td>${fila[1]}</td>
                                        <td><fmt:formatNumber value="${fila[2]}" type="currency" currencySymbol="$" maxFractionDigits="0"/></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card card-reporte h-100">
                <div class="card-header">📅 Citas por estado</div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty citasPorEstado}">
                            <p class="text-muted">Sin datos.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table table-sm">
                                <thead><tr><th>Estado</th><th>Total</th></tr></thead>
                                <tbody>
                                <c:forEach var="fila" items="${citasPorEstado}">
                                    <tr><td>${fila[0]}</td><td>${fila[1]}</td></tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="card card-reporte h-100">
                <div class="card-header">🏢 Solicitudes por inmobiliaria</div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty solicitudesPorInmobiliaria}">
                            <p class="text-muted">Sin datos.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="table table-sm">
                                <thead><tr><th>Inmobiliaria</th><th>Total solicitudes</th></tr></thead>
                                <tbody>
                                <c:forEach var="fila" items="${solicitudesPorInmobiliaria}">
                                    <tr><td>${fila[0]}</td><td>${fila[1]}</td></tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
