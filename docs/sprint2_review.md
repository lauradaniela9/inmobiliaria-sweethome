# Sprint 2 – Review

## Evidencia
1. Catálogo público con filtros reales (`/catalogo`) y ficha de detalle (`/propiedad?id=`) con galería de imágenes y características.
2. Catálogo mostrando imágenes de Unsplash al lado de las locales, sin que se rompa ninguna.
3. CRUD completo de propiedades para el rol INMOBILIARIA, incluyendo imágenes (1:N) y características (N:M) en un único formulario transaccional.
4. Baja lógica de propiedades (el registro nunca se elimina físicamente; se conserva el historial de citas/solicitudes).
5. Módulo de administración de usuarios y roles para el rol ADMINISTRADOR, con auditoría de cada cambio.
6. Edición del perfil personal (relación 1:1) con validación de formato y de duplicados.
7. Landing page dinámica: el buscador rápido y las propiedades destacadas ahora se leen de la base de datos.

**Demo:** cada rol ve un panel distinto, y una propiedad con imagen externa se ve bien tanto en el catálogo como en su detalle.

