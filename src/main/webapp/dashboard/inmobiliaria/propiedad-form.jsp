<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty propiedad ? 'Publicar propiedad' : 'Editar propiedad'} | Sweet Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body class="body-dashboard">
<%@ include file="/WEB-INF/header.jspf" %>
<div class="container py-4" style="max-width:760px">
    <h2 class="mb-4 titulo-seccion">${empty propiedad ? 'Publicar nueva propiedad' : 'Editar propiedad'}</h2>

    <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>

    <form method="post" action="<%= request.getContextPath() %>/dashboard/inmobiliaria/propiedad-form"
          enctype="multipart/form-data" class="card card-body form-propiedad mt-3">
        <c:if test="${not empty propiedad}">
            <input type="hidden" name="idPropiedad" value="${propiedad.idPropiedad}">
        </c:if>

        <div class="row g-3">
            <div class="col-md-6">
                <label class="form-label">Matrícula inmobiliaria (única)</label>
                <input type="text" name="matriculaInmobiliaria" class="form-control" value="${propiedad.matriculaInmobiliaria}" required>
            </div>
            <div class="col-md-6">
                <label class="form-label">Título</label>
                <input type="text" name="titulo" class="form-control" value="${propiedad.titulo}" required>
            </div>

            <div class="col-12">
                <label class="form-label">Descripción</label>
                <textarea name="descripcion" class="form-control" rows="3">${propiedad.descripcion}</textarea>
            </div>

            <div class="col-md-4">
                <label class="form-label">Ciudad</label>
                <select name="idCiudad" class="form-select" required>
                    <option value="">Selecciona...</option>
                    <c:forEach var="c" items="${ciudades}">
                        <option value="${c.id}" ${propiedad.idCiudad == c.id ? 'selected' : ''}>${c.nombre}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Tipo de propiedad</label>
                <select name="idTipoPropiedad" class="form-select" required>
                    <option value="">Selecciona...</option>
                    <c:forEach var="t" items="${tipos}">
                        <option value="${t.id}" ${propiedad.idTipoPropiedad == t.id ? 'selected' : ''}>${t.nombre}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Estado</label>
                <select name="estado" class="form-select">
                    <option value="DISPONIBLE" ${propiedad.estado == 'DISPONIBLE' || empty propiedad ? 'selected' : ''}>DISPONIBLE</option>
                    <option value="RESERVADA" ${propiedad.estado == 'RESERVADA' ? 'selected' : ''}>RESERVADA</option>
                    <option value="VENDIDA" ${propiedad.estado == 'VENDIDA' ? 'selected' : ''}>VENDIDA</option>
                    <option value="ARRENDADA" ${propiedad.estado == 'ARRENDADA' ? 'selected' : ''}>ARRENDADA</option>
                    <option value="INACTIVA" ${propiedad.estado == 'INACTIVA' ? 'selected' : ''}>INACTIVA</option>
                </select>
            </div>

            <div class="col-md-4">
                <label class="form-label">Precio (COP)</label>
                <input type="number" step="0.01" name="precio" class="form-control" value="${propiedad.precio}" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Área (m²)</label>
                <input type="number" step="0.01" name="areaM2" class="form-control" value="${propiedad.areaM2}">
            </div>
            <div class="col-md-4">
                <label class="form-label">Dirección</label>
                <input type="text" name="direccion" class="form-control" value="${propiedad.direccion}" required>
            </div>

            <div class="col-12">
                <hr>
                <label class="form-label fw-bold">Imágenes — la primera queda como portada</label>
                <p class="text-muted small mb-2">Por cada imagen, elige si vas a pegar una URL o subir un archivo desde tu computador.</p>

                <div id="contenedorImagenes">
                    <c:choose>
                        <c:when test="${not empty propiedad.imagenes}">
                            <c:forEach var="img" items="${propiedad.imagenes}" varStatus="st">
                                <div class="imagen-row border rounded p-2 mb-2">
                                    <div class="btn-group btn-group-sm mb-2" role="group">
                                        <button type="button" id="btnUrl_${st.index}" class="btn btn-outline-secondary active"
                                                onclick="toggleTipoImagen(${st.index}, 'url')">URL</button>
                                        <button type="button" id="btnArchivo_${st.index}" class="btn btn-outline-secondary"
                                                onclick="toggleTipoImagen(${st.index}, 'archivo')">Subir archivo</button>
                                    </div>
                                    <input type="hidden" name="tipoImagen_${st.index}" id="tipoImagen_${st.index}" value="url">
                                    <input type="url" name="urlImagen_${st.index}" id="urlImagen_${st.index}"
                                           class="form-control mb-1" value="${img.urlImagen}" placeholder="https://...">
                                    <input type="file" name="archivoImagen_${st.index}" id="archivoImagen_${st.index}"
                                           class="form-control mb-1" accept="image/*" style="display:none">
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="imagen-row border rounded p-2 mb-2">
                                <div class="btn-group btn-group-sm mb-2" role="group">
                                    <button type="button" id="btnUrl_0" class="btn btn-outline-secondary active"
                                            onclick="toggleTipoImagen(0, 'url')">URL</button>
                                    <button type="button" id="btnArchivo_0" class="btn btn-outline-secondary"
                                            onclick="toggleTipoImagen(0, 'archivo')">Subir archivo</button>
                                </div>
                                <input type="hidden" name="tipoImagen_0" id="tipoImagen_0" value="url">
                                <input type="url" name="urlImagen_0" id="urlImagen_0" class="form-control mb-1" placeholder="https://...">
                                <input type="file" name="archivoImagen_0" id="archivoImagen_0" class="form-control mb-1" accept="image/*" style="display:none">
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <button type="button" class="btn btn-sm btn-outline-secondary" onclick="agregarCampoImagen()">+ Agregar otra imagen</button>
            </div>

            <div class="col-12">
                <hr>
                <label class="form-label fw-bold">Características </label>
                <div class="row">
                    <c:forEach var="carac" items="${caracteristicas}">
                        <div class="col-md-4 form-check">
                            <input class="form-check-input" type="checkbox" name="caracteristica" value="${carac.idCaracteristica}"
                                   id="carac${carac.idCaracteristica}"
                                   <c:forEach var="sel" items="${propiedad.caracteristicas}">${sel.idCaracteristica == carac.idCaracteristica ? 'checked' : ''}</c:forEach>>
                            <label class="form-check-label" for="carac${carac.idCaracteristica}">${carac.nombre}</label>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <button type="submit" class="btn btn-personalizado mt-4">${empty propiedad ? 'Publicar' : 'Guardar cambios'}</button>
        <a href="<%= request.getContextPath() %>/dashboard/inmobiliaria/propiedades" class="btn btn-outline-secondary mt-4">Cancelar</a>
    </form>
</div>

<script>
let contadorImagenes = ${empty propiedad.imagenes ? 1 : fn:length(propiedad.imagenes)};

function toggleTipoImagen(indice, tipo) {
    document.getElementById('tipoImagen_' + indice).value = tipo;
    document.getElementById('urlImagen_' + indice).style.display = (tipo === 'url') ? 'block' : 'none';
    document.getElementById('archivoImagen_' + indice).style.display = (tipo === 'archivo') ? 'block' : 'none';
    document.getElementById('btnUrl_' + indice).classList.toggle('active', tipo === 'url');
    document.getElementById('btnArchivo_' + indice).classList.toggle('active', tipo === 'archivo');

    // Limpia el campo que quedó oculto para no enviar datos residuales
    if (tipo === 'url') {
        document.getElementById('archivoImagen_' + indice).value = '';
    } else {
        document.getElementById('urlImagen_' + indice).value = '';
    }
}

function agregarCampoImagen() {
    const indice = contadorImagenes++;
    const contenedor = document.getElementById('contenedorImagenes');
    const div = document.createElement('div');
    div.className = 'imagen-row border rounded p-2 mb-2';
    div.innerHTML =
        '<div class="btn-group btn-group-sm mb-2" role="group">' +
            '<button type="button" id="btnUrl_' + indice + '" class="btn btn-outline-secondary active" ' +
                'onclick="toggleTipoImagen(' + indice + ', \'url\')">URL</button>' +
            '<button type="button" id="btnArchivo_' + indice + '" class="btn btn-outline-secondary" ' +
                'onclick="toggleTipoImagen(' + indice + ', \'archivo\')">Subir archivo</button>' +
        '</div>' +
        '<input type="hidden" name="tipoImagen_' + indice + '" id="tipoImagen_' + indice + '" value="url">' +
        '<input type="url" name="urlImagen_' + indice + '" id="urlImagen_' + indice + '" class="form-control mb-1" placeholder="https://...">' +
        '<input type="file" name="archivoImagen_' + indice + '" id="archivoImagen_' + indice + '" class="form-control mb-1" accept="image/*" style="display:none">';
    contenedor.appendChild(div);
}
</script>
</body>
</html>
