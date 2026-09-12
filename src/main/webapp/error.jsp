<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="d-flex align-items-center justify-content-center vh-100 bg-light">
<div class="text-center">
    <h1 class="display-3 text-danger">¡Ups!</h1>
    <p>Ocurrió un error inesperado. Por favor intenta nuevamente.</p>
    <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-primary">Volver al inicio</a>
</div>
</body>
</html>
