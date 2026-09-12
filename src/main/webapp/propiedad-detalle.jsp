<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ page import="java.util.List"%>
<%
@SuppressWarnings("unchecked")
List<String> rolesSesion = (session != null) ? (List<String>) session.getAttribute("roles") : null;
pageContext.setAttribute("esCliente", rolesSesion != null && rolesSesion.contains("CLIENTE"));
%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${propiedad.titulo}|Sweet Home</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body>

	<%@ include file="/WEB-INF/topbar-publico.jspf"%>

	<div class="container py-4">
		<a href="<%=request.getContextPath()%>/catalogo" class="btn-volver">&larr;
			Volver al catálogo</a>

		<div class="row mt-3 g-4">
			<div class="col-md-7">
				<div id="carruselGaleria"
					class="carousel slide shadow-sm rounded overflow-hidden"
					data-bs-ride="carousel">
					<div class="carousel-inner bg-dark">
						<c:forEach var="img" items="${propiedad.imagenes}" varStatus="st">
							<div class="carousel-item ${st.first ? 'active' : ''}">
								<c:choose>
									<c:when test="${fn:startsWith(img.urlImagen, 'http://') or fn:startsWith(img.urlImagen, 'https://')}">
										<%-- URL externa (ej. Unsplash): se usa tal cual, sin anteponer el contextPath --%>
										<img src="${img.urlImagen}"
											class="d-block w-100" style="height: 420px; object-fit: cover"
											alt="Imagen de ${propiedad.titulo}">
									</c:when>
									<c:otherwise>
										<%-- Ruta local dentro del proyecto (ej. /img/propiedades/...): se antepone el contextPath --%>
										<img src="<%= request.getContextPath() %>${img.urlImagen}"
											class="d-block w-100" style="height: 420px; object-fit: cover"
											alt="Imagen de ${propiedad.titulo}">
									</c:otherwise>
								</c:choose>
							</div>
						</c:forEach>
					</div>
					<c:if test="${fn:length(propiedad.imagenes) > 1}">
						<button class="carousel-control-prev" type="button"
							data-bs-target="#carruselGaleria" data-bs-slide="prev">
							<span class="carousel-control-prev-icon"></span>
						</button>
						<button class="carousel-control-next" type="button"
							data-bs-target="#carruselGaleria" data-bs-slide="next">
							<span class="carousel-control-next-icon"></span>
						</button>
					</c:if>
				</div>
			</div>

			<div class="col-md-5">
				<span class="badge bg-secondary mb-2">${propiedad.nombreTipoPropiedad}</span>
				<span class="badge bg-info text-dark mb-2">${propiedad.estado}</span>
				<h2>${propiedad.titulo}</h2>

				<div class="precio-header d-flex flex-column align-items-start">
					<p class="precio-valor d-block w-100 mb-2">
						<fmt:formatNumber value="${propiedad.precio}" type="currency"
							currencySymbol="$" maxFractionDigits="0" />
					</p>
					<div class="iconos-rapidos d-flex flex-wrap gap-3">
						<c:if test="${not empty propiedad.alcobas}">
							<div class="icono-item">
								<span class="emoji">🛏️</span>${propiedad.alcobas} habitaciones
							</div>
						</c:if>
						<c:if test="${not empty propiedad.banos}">
							<div class="icono-item">
								<span class="emoji">🚿</span>${propiedad.banos} baños
							</div>
						</c:if>
						<c:if test="${not empty propiedad.areaM2}">
							<div class="icono-item">
								<span class="emoji">📐</span>${propiedad.areaM2} m²
							</div>
						</c:if>
					</div>
				</div>
				<c:if
					test="${not empty propiedad.valorAdministracion and propiedad.valorAdministracion > 0}">
					<div class="admin-bar">
						Administración:
						<fmt:formatNumber value="${propiedad.valorAdministracion}"
							type="currency" currencySymbol="$" maxFractionDigits="0" />
					</div>
				</c:if>

				<p class="ubicacion-pin">📍 ${propiedad.zonaBarrio},
					${propiedad.nombreCiudad}, ${propiedad.departamentoCiudad}</p>

				<div class="detalle-grid">
					<div class="detalle-item">
						<strong>Tipo de vivienda</strong> ${propiedad.nombreTipoPropiedad}
					</div>
					<div class="detalle-item">
						<strong>Estrato</strong> ${propiedad.estrato}
					</div>
					<div class="detalle-item">
						<strong>Tipo de operación</strong> ${propiedad.tipoNegocio}
					</div>
					<div class="detalle-item">
						<strong>Año de construcción</strong> ${propiedad.anioConstruccion}
					</div>
					<div class="detalle-item">
						<strong>Código</strong> ${propiedad.codigo}
					</div>
					<div class="detalle-item">
						<strong>Área construida</strong> ${propiedad.areaM2} m²
					</div>
				</div>

				<p class="fecha-publicado">Publicado por
					${propiedad.nombreComercialInmobiliaria}</p>

				<hr>
				<h5>Descripción</h5>
				<p style="white-space: pre-line;">${propiedad.descripcion}</p>
				<hr>
				<h5>Características de la propiedad</h5>
				<c:choose>
					<c:when test="${empty propiedad.caracteristicas}">
						<p class="text-muted small">Sin características registradas.</p>
					</c:when>
					<c:otherwise>
						<ul class="caracteristicas-lista">
							<c:forEach var="carac" items="${propiedad.caracteristicas}">
								<li>${carac.nombre}<c:if test="${carac.cantidad > 1}"> (${carac.cantidad})</c:if></li>
							</c:forEach>
						</ul>
					</c:otherwise>
				</c:choose>

				<hr>
				<h5>Inmobiliaria a cargo</h5>
				<div class="inmobiliaria-cargo">
					<p class="mb-1 fw-semibold">${propiedad.nombreComercialInmobiliaria}</p>
					<c:choose>
						<c:when test="${not empty sessionScope.idUsuario}">
							<p class="text-muted mb-0">Tel. contacto:
								${propiedad.telefonoContactoInmobiliaria}</p>
						</c:when>
						<c:otherwise>
							<p class="text-muted small mb-0">Inicia sesión para ver los
								datos completos de contacto.</p>
						</c:otherwise>
					</c:choose>
				</div>

				<c:if test="${esCliente}">
					<hr>
					<form method="post"
						action="<%=request.getContextPath()%>/dashboard/cliente/favoritos"
						class="d-inline">
						<input type="hidden" name="idPropiedad"
							value="${propiedad.idPropiedad}"> <input type="hidden"
							name="volverA" value="detalle">
						<button type="submit"
							class="btn ${esFavorito ? 'btn-danger' : 'btn-outline-danger'} w-100 mb-2">
							<c:choose>
								<c:when test="${esFavorito}">&#9829; Quitar de favoritos</c:when>
								<c:otherwise>&#9825; Marcar como favorito</c:otherwise>
							</c:choose>
						</button>
					</form>

					<button class="btn btn-dark w-100 mb-2" type="button"
						data-bs-toggle="collapse" data-bs-target="#formCita">Agendar
						visita</button>
					<div class="collapse" id="formCita">
						<form method="post"
							action="<%=request.getContextPath()%>/dashboard/cliente/citas"
							class="card card-body mb-2">
							<input type="hidden" name="accion" value="agendar"> <input
								type="hidden" name="idPropiedad"
								value="${propiedad.idPropiedad}"> <label
								class="form-label small">Fecha y hora</label> <input
								type="datetime-local" name="fechaHora" class="form-control mb-2"
								required> <label class="form-label small">Observación
								(opcional)</label> <input type="text" name="observacion"
								class="form-control mb-2" maxlength="255">
							<button type="submit" class="btn btn-personalizado">Confirmar
								solicitud de cita</button>
						</form>
					</div>

					<button class="btn btn-outline-dark w-100" type="button"
						data-bs-toggle="collapse" data-bs-target="#formSolicitud">Solicitar
						compra o arriendo</button>
					<div class="collapse" id="formSolicitud">
						<form method="post"
							action="<%=request.getContextPath()%>/dashboard/cliente/solicitudes"
							enctype="multipart/form-data" class="card card-body mt-2">
							<input type="hidden" name="accion" value="crear"> <input
								type="hidden" name="idPropiedad"
								value="${propiedad.idPropiedad}"> <label
								class="form-label small">Tipo de trámite</label> <select
								name="tipo" class="form-select mb-2" required>
								<option value="COMPRA">Compra</option>
								<option value="ARRIENDO">Arriendo</option>
							</select> <label class="form-label small">Documento soporte
								(opcional)</label> <input type="file" name="documento"
								class="form-control mb-2">
							<button type="submit" class="btn btn-personalizado">Radicar
								solicitud</button>
						</form>
					</div>
				</c:if>
				<c:if test="${empty sessionScope.idUsuario}">
					<hr>
					<div class="cta-login">
						<span style="font-size: 1.5rem;">🔑</span>
						<span>
							<a href="<%=request.getContextPath()%>/login">Inicia sesión</a> como
							cliente para agendar visitas, marcar favoritos o radicar una
							solicitud de compra/arriendo.
						</span>
					</div>
				</c:if>
			</div>
		</div>
	</div>

	<footer class="navbar-brand-custom text-white text-center py-3 mt-5">
		<p class="mb-0">&copy; 2026 Sweet Home - Proyecto académico UTS</p>
	</footer>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
