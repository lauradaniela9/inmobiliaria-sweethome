<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi perfil | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>

<div class="panel-header">
    <div class="container">
        <span class="panel-eyebrow">MI CUENTA</span>
        <h2>Mi perfil</h2>
    </div>
</div>

<div class="container" style="max-width:640px">

    <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
    <c:if test="${not empty exito}"><div class="alert alert-success">${exito}</div></c:if>

    <form method="post" action="<%= request.getContextPath() %>/dashboard/perfil" class="card card-body shadow-sm">
        <p class="text-muted">Estos son tus datos personales.</p>
        <div class="mb-3">
            <label class="form-label">Nombres</label>
            <input type="text" name="nombres" class="form-control" value="${perfil.nombres}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Apellidos</label>
            <input type="text" name="apellidos" class="form-control" value="${perfil.apellidos}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Documento de identidad</label>
            <input type="text" name="documento" class="form-control" value="${perfil.documento}" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Teléfono</label>
            <input type="tel" name="telefono" class="form-control" value="${perfil.telefono}" pattern="[0-9]{7,10}">
        </div>
        <div class="mb-3">
            <label class="form-label">Dirección</label>
            <input type="text" name="direccion" class="form-control" value="${perfil.direccion}">
        </div>
        <div class="mb-3">
            <label class="form-label">URL de foto (opcional)</label>
            <input type="text" name="foto" class="form-control" value="${perfil.foto}" placeholder="https://...">
        </div>
        <button type="submit" class="btn btn-personalizado">Guardar cambios</button>
    </form>
</div>
</body>
</html>
