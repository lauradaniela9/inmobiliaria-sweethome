<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.inmobiliaria.util.ConexionBD" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Administrador | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>

<%
    int totalPropiedades = 0, totalUsuarios = 0, totalCitas = 0, totalSolicitudes = 0;
    try (Connection con = ConexionBD.obtenerConexion()) {
        try (Statement st = con.createStatement()) {
            ResultSet rs;
            rs = st.executeQuery("SELECT COUNT(*) FROM propiedad WHERE estado = 'DISPONIBLE'");
            if (rs.next()) totalPropiedades = rs.getInt(1);

            rs = st.executeQuery("SELECT COUNT(*) FROM usuario");
            if (rs.next()) totalUsuarios = rs.getInt(1);

            rs = st.executeQuery("SELECT COUNT(*) FROM cita");
            if (rs.next()) totalCitas = rs.getInt(1);

            rs = st.executeQuery("SELECT COUNT(*) FROM solicitud");
            if (rs.next()) totalSolicitudes = rs.getInt(1);
        }
    } catch (Exception e) {
        // Si alguna tabla/consulta falla, se deja el contador en 0 en vez de romper la página
        System.err.println("Advertencia al calcular estadísticas del panel: " + e.getMessage());
    }
%>

<div class="panel-header">
    <div class="container">
        <span class="panel-eyebrow">ADMINISTRADOR</span>
        <h2>Panel de Administrador</h2>
    </div>
</div>

<div class="container">
    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <div class="stat-card">
                <div class="stat-numero"><%= totalPropiedades %></div>
                <div class="stat-label">Propiedades disponibles</div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <div class="stat-numero"><%= totalUsuarios %></div>
                <div class="stat-label">Usuarios registrados</div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <div class="stat-numero"><%= totalCitas %></div>
                <div class="stat-label">Citas agendadas</div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card">
                <div class="stat-numero"><%= totalSolicitudes %></div>
                <div class="stat-label">Solicitudes registradas</div>
            </div>
        </div>
    </div>

    <div class="row g-3">
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/admin/usuarios">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">👥</span>
                    <span class="titulo-panel">Gestión de usuarios y roles</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/admin/reportes">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">📊</span>
                    <span class="titulo-panel">Reportes consolidados</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/admin/auditoria">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">🔍</span>
                    <span class="titulo-panel">Auditoría de accesos</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/catalogo">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">🏠</span>
                    <span class="titulo-panel">Catálogo público de propiedades</span>
                </div>
            </a>
        </div>
    </div>
</div>

</body>
</html>
