<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    request.setAttribute("errorUsuarios", session.getAttribute("errorUsuarios"));
    request.setAttribute("exitoUsuarios", session.getAttribute("exitoUsuarios"));
    session.removeAttribute("errorUsuarios");
    session.removeAttribute("exitoUsuarios");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Usuarios y roles | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>
<div class="container py-4">
    <h2 class="mb-4 titulo-seccion">Gestión de usuarios y roles</h2>

    <c:if test="${not empty errorUsuarios}"><div class="alert alert-danger">${errorUsuarios}</div></c:if>
    <c:if test="${not empty exitoUsuarios}"><div class="alert alert-success">${exitoUsuarios}</div></c:if>

    <div class="tabla-admin-wrapper mt-3">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead>
                    <tr><th>#</th><th>Nombre</th><th>Correo</th><th>Estado</th><th>Roles</th><th>Asignar / revocar rol</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${usuarios}">
                        <tr>
                            <td>${u.idUsuario}</td>
                            <td>${u.nombres} ${u.apellidos}</td>
                            <td>${u.correo}</td>
                            <td>
                                <form method="post" action="<%= request.getContextPath() %>/dashboard/admin/usuarios" class="d-flex gap-1">
                                    <input type="hidden" name="accion" value="cambiarEstado">
                                    <input type="hidden" name="idUsuario" value="${u.idUsuario}">
                                    <select name="estado" class="form-select form-select-sm select-estado ${u.estado == 'ACTIVO' ? 'estado-activo' : u.estado == 'INACTIVO' ? 'estado-inactivo' : 'estado-bloqueado'}" style="width:auto" onchange="this.form.submit()">
                                        <option value="ACTIVO" ${u.estado == 'ACTIVO' ? 'selected' : ''}>ACTIVO</option>
                                        <option value="INACTIVO" ${u.estado == 'INACTIVO' ? 'selected' : ''}>INACTIVO</option>
                                        <option value="BLOQUEADO" ${u.estado == 'BLOQUEADO' ? 'selected' : ''}>BLOQUEADO</option>
                                    </select>
                                </form>
                            </td>
                            <td>
                                <c:forEach var="r" items="${u.roles}">
                                    <span class="badge badge-rol me-1">${r}</span>
                                </c:forEach>
                                <c:if test="${empty u.roles}"><span class="text-muted small">Sin roles</span></c:if>
                            </td>
                            <td class="text-nowrap">
                                <form method="post" action="<%= request.getContextPath() %>/dashboard/admin/usuarios" class="d-inline-flex gap-1">
                                    <input type="hidden" name="idUsuario" value="${u.idUsuario}">
                                    <select name="rol" class="form-select form-select-sm" style="width:auto">
                                        <c:forEach var="r" items="${rolesDisponibles}">
                                            <option value="${r}">${r}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" name="accion" value="asignarRol" class="btn btn-sm btn-outline-success">Asignar</button>
                                    <button type="submit" name="accion" value="revocarRol" class="btn btn-sm btn-outline-danger">Revocar</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>