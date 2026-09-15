# Sprint 2 – Retrospective

## ¿Qué salió bien?
- Resolver primero el CRUD de propiedades como una operación transaccional (propiedad + imágenes + características en una sola transacción con rollback) evitó dejar datos huérfanos ante un error a mitad de camino.
- El formulario mixto de imágenes (URL o archivo) quedó bastante cómodo de usar, y usar `@MultipartConfig` en el servlet nos dejó mezclar campos de texto y archivos en la misma petición sin mucho lío.
- Reutilizar `Validaciones` (extraída en el Sprint 1) en el formulario de propiedades confirmó que la decisión de aislarla en una clase propia fue acertada.
- Cerrar primero la deuda técnica de administración de usuarios/roles, tal como se acordó en la retrospectiva anterior, evitó que se acumulara para el Sprint 3.

## ¿Qué se puede mejorar?  dos bugs que costó encontrar:
1. Las imágenes con URL externa se rompían en el catálogo y en la ficha de detalle porque el código siempre le agregaba el `contextPath` adelante, incluso cuando la URL ya era completa (quedaba algo como `localhost:8080/inmobiliaria/https://...`). Tocó revisar si la URL empieza con `http` antes de decidir si le pega el contextPath o no.
2. Al agregar imágenes dinámicamente con JS, usamos `${variable}` dentro de un template string, pero JSP interpreta ese símbolo como su propio lenguaje en cualquier parte del archivo (hasta dentro de `<script>`). Nos dejaba las variables vacías y el botón de subir archivo no hacía nada en las filas nuevas. Se arregló armando el HTML con concatenación (`+`) en vez de template strings.

**Qué mejorar:** probar cada funcionalidad con casos "raros" (como una URL externa) apenas se termina de programar, no solo con el caso más simple — así se detectan antes estas cosas.
