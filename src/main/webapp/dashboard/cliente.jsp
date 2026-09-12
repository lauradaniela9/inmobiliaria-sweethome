<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Cliente | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>

<div class="panel-header">
    <div class="container">
        <span class="panel-eyebrow">CLIENTE</span>
        <h2>Panel de Cliente</h2>
    </div>
</div>

<div class="container">
    <div class="row g-3">
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/catalogo">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">🔍</span>
                    <span class="titulo-panel">Buscar propiedades</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/cliente/favoritos">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">❤️</span>
                    <span class="titulo-panel">Mis favoritos</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/cliente/citas">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">📅</span>
                    <span class="titulo-panel">Mis citas</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/cliente/solicitudes">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">📄</span>
                    <span class="titulo-panel">Mis solicitudes</span>
                </div>
            </a>
        </div>
        <div class="col-md-3">
            <a class="text-decoration-none" href="<%= request.getContextPath() %>/dashboard/perfil">
                <div class="card p-3 h-100 shadow-sm tarjeta-panel">
                    <span class="icono-panel">👤</span>
                    <span class="titulo-panel">Mi perfil</span>
                </div>
            </a>
        </div>
    </div>
</div>

</body>
</html>