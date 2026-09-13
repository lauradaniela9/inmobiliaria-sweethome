# Sprint 2 – Núcleo del negocio
**Duración:** 2 – 8 de septiembre de 2026 (7 días)

---

## 1. Sprint Planning

**Objetivo:** CRUD de propiedades con imágenes (1:N) y características (N:M), buscador con filtros, y los paneles por rol.

**Historias:**
- Como agente quiero registrar y editar propiedades con fotos, características y precio. (Alta)
- Como cliente quiero buscar/filtrar propiedades por ciudad, tipo, precio y características. (Alta)
- Como cliente quiero completar mi perfil (documento, teléfono, dirección). (Media)
- Como cliente quiero marcar propiedades como favoritas. (Media)

**Estimación:** ~26 puntos.

**Qué había que hacer:**
- `PropiedadDAO` con las consultas obligatorias (INNER JOIN propiedad+ciudad+tipo+inmobiliaria, y LEFT JOIN de propiedades sin citas).
- Formulario de publicar/editar propiedad, con imágenes que se pueden pegar por URL o subir como archivo (cada imagen se elige aparte).
- Que el catálogo y la ficha de detalle muestren bien tanto imágenes locales como externas.
- Dashboards de admin, inmobiliaria y cliente con sus contadores.
- Módulo de favoritos.

## 2. Sprint Review

**Evidencia:**
- Panel del agente publicando una propiedad con una imagen por URL y otra subida como archivo.
- Ficha de detalle con el carrusel de imágenes y las características.
- Catálogo mostrando imágenes de Unsplash al lado de las locales, sin que se rompa ninguna.
- Panel de inmobiliaria con el contador de solicitudes pendientes.

**Demo:** cada rol ve un panel distinto, y una propiedad con imagen externa se ve bien tanto en el catálogo como en su detalle.

## 3. Sprint Retrospective

**Qué salió bien:** el formulario mixto de imágenes (URL o archivo) quedó bastante cómodo de usar, y usar `@MultipartConfig` en el servlet nos dejó mezclar campos de texto y archivos en la misma petición sin mucho lío.

**Qué salió mal:** dos bugs que costó encontrar:
1. Las imágenes con URL externa se rompían en el catálogo y en la ficha de detalle porque el código siempre le agregaba el `contextPath` adelante, incluso cuando la URL ya era completa (quedaba algo como `localhost:8080/inmobiliaria/https://...`). Tocó revisar si la URL empieza con `http` antes de decidir si le pega el contextPath o no.
2. Al agregar imágenes dinámicamente con JS, usamos `${variable}` dentro de un template string, pero JSP interpreta ese símbolo como su propio lenguaje en cualquier parte del archivo (hasta dentro de `<script>`). Nos dejaba las variables vacías y el botón de subir archivo no hacía nada en las filas nuevas. Se arregló armando el HTML con concatenación (`+`) en vez de template strings.

**Qué mejorar:** probar cada funcionalidad con casos "raros" (como una URL externa) apenas se termina de programar, no solo con el caso más simple — así se detectan antes estas cosas.
