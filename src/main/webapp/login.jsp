<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/estilos.css">
</head>
<body>
<div class="container">
    <div class="card form-auth shadow-sm">
        <div class="card-body p-4">
            <h3 class="text-center mb-4 fw-bold" style="color: #0b3d66;">Iniciar sesión</h3>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getAttribute("exito") != null) { %>
                <div class="alert alert-success"><%= request.getAttribute("exito") %></div>
            <% } %>

            <form method="post" action="login">
                <div class="mb-3">
                    <label class="form-label">Correo electrónico</label>
                    <input type="email" name="correo" class="form-control" required autofocus>
                </div>
                <div class="mb-3">
                    <label class="form-label">Contraseña</label>
                    <input type="password" name="contrasena" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary w-100">Ingresar</button>
            </form>

            <p class="text-center mt-3 mb-0">
                ¿No tienes cuenta? <a href="registro">Regístrate</a>
            </p>
            <p class="text-center mt-1">
                <a href="<%= request.getContextPath() %>/" class="btn-volver">← Volver al inicio</a>
            </p>
        </div>
    </div>
</div>
</body>
</html>
