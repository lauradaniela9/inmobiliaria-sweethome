<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acceso denegado</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex align-items-center justify-content-center vh-100 bg-light">
<div class="text-center">
    <h1 class="display-1 text-danger">403</h1>
    <h3>Acceso denegado</h3>
    <p class="text-muted">No tienes permisos para ver este recurso, o tu sesión ha expirado.</p>
    <a href="<%= request.getContextPath() %>/login" class="btn btn-primary me-2">Iniciar sesión</a>
    <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-outline-secondary">Volver al inicio</a>
</div>
</body>
</html>
