<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.inmobiliaria.util.ConexionBD" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Inmobiliaria | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>

<%
    Integer idUsuarioInmobiliaria = (Integer) session.getAttribute("idUsuario");
    int totalMisPropiedades = 0, totalCitasPendientes = 0, totalSolicitudesPendientes = 0;
    try (Connection con = ConexionBD.obtenerConexion()) {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM propiedad WHERE id_usuario_inmobiliaria = ? AND estado <> 'INACTIVA'")) {
            ps.setInt(1, idUsuarioInmobiliaria);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) totalMisPropiedades = rs.getInt(1); }
        }
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM cita ci INNER JOIN propiedad p ON p.id_propiedad = ci.id_propiedad " +
                "WHERE p.id_usuario_inmobiliaria = ? AND ci.estado = 'PENDIENTE'")) {
            ps.setInt(1, idUsuarioInmobiliaria);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) totalCitasPendientes = rs.getInt(1); }
        }
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COUNT(*) FROM solicitud s INNER JOIN propiedad p ON p.id_propiedad = s.id_propiedad " +
                "WHERE p.id_usuario_inmobiliaria = ? AND s.estado = 'EN_REVISION'")) {
            ps.setInt(1, idUsuarioInmobiliaria);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) totalSolicitudesPendientes = rs.getInt(1); }
        }
    } catch (Exception e) {
        System.err.println("Advertencia al calcular estadísticas del panel inmobiliaria: " + e.getMessage());
    }
%>

<div class="panel-header">
    <div class="container">
        <span class="panel-eyebrow">INMOBILIARIA</span>
        <h2>Panel de Inmobiliaria</h2>
    </div>
</div>

<div class="container">
    <div class="row g-3 mb-4">
        <div class="col-md-4">
            <div class="stat-card">
                <div class="stat-numero"><%= totalMisPropiedades %></div>
                <div class="stat-label">Mis propiedades activas</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-card">
                <div class="stat-numero"><%= totalCitasPendientes %></div>
                <div class="stat-label">Citas pendientes</div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="stat-card">
                <div class="stat-numero"><%= totalSolicitudesPendientes %></div>
                <div class="stat-label">Solicitudes pendientes</div>
            </div>
        </div>
    </div>

    <div class="row g-3">
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/inmobiliaria/propiedades">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">🏠</span>
                    <span class="titulo-panel">Mis propiedades</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/inmobiliaria/propiedad-form">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">➕</span>
                    <span class="titulo-panel">Publicar propiedad</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/inmobiliaria/citas">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">📅</span>
                    <span class="titulo-panel">Solicitudes de citas</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/inmobiliaria/solicitudes">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">📄</span>
                    <span class="titulo-panel">Solicitudes de compra/arriendo</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/inmobiliaria/reportes">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">📊</span>
                    <span class="titulo-panel">Reportes de ventas/arriendos</span>
                </div>
            </a>
        </div>
    </div>
</div>

</body>
</html>
