# Sprint 2 – Núcleo del negocio

**Duración:** 7 días
**Product Owner:** Docente (Julian Barney Jaimes Rincón)
**Scrum Master / Equipo de Desarrollo:**  [Julieth León y Laura Galvis]
**Fecha de inicio:** [2 de septiembre]
**Fecha de cierre:** [8 de septiembre]

## Objetivo del Sprint
Completar el CRUD de propiedades con imágenes (1:N) y características (N:M),
el buscador público con filtros reales contra la base de datos, el perfil
personal (1:1) y los tres paneles diferenciados con funcionalidad real

## Historias de usuario seleccionadas (Sprint Backlog)

| # | Historia | Prioridad | Estimación | Estado |
|---|----------|-----------|------------|--------|
| 4 (cierre) | Como administrador, quiero asignar y revocar roles a los usuarios para controlar los permisos de la aplicación. | Alta | 5 pts | Hecho |
| 5 | Como cliente, quiero completar mi perfil con documento, teléfono y dirección asociados a mi cuenta para agilizar mis trámites. | Media | 3 pts | Hecho |
| 6 | Como agente de la inmobiliaria, quiero registrar y editar propiedades con fotos, características y precio para mantener el catálogo actualizado. | Alta | 8 pts | Hecho |
| 7 | Como cliente, quiero buscar y filtrar propiedades por ciudad, tipo, precio y características para encontrar las opciones que se ajusten a mis necesidades. | Alta | 5 pts | Hecho |

**Total estimado:** 21 puntos de historia.

## Tareas técnicas del Sprint 2
- [x] `UsuarioAdminDAO` + `UsuarioAdminServlet` + `usuarios.jsp`: listado de usuarios con sus roles (JOIN usuario/perfil/rol), cambio de estado (ACTIVO/INACTIVO/BLOQUEADO) y asignación/revocación de roles con registro en auditoría.
- [x] `PerfilDAO` + `PerfilServlet` + `perfil.jsp`: edición de los datos 1:1 con validación de campos y captura del error de documento duplicado (UNIQUE).
- [x] `PropiedadDAO` ampliado: `crear`/`actualizar` transaccionales (propiedad + imágenes + características en una sola transacción, con rollback si falla), `darDeBaja` (baja lógica, nunca DELETE físico), `perteneceAInmobiliaria` (control de acceso a nivel de datos).
- [x] `PropiedadFormServlet` + `propiedad-form.jsp`: formulario único de creación/edición con campos de imagen dinámicos y checkboxes de características, captura del error de matrícula duplicada (UNIQUE).
- [x] `PropiedadListaServlet` + `propiedades.jsp`: listado de propiedades propias de la inmobiliaria con acciones de editar/dar de baja.
- [x] `CatalogoServlet` + `catalogo.jsp`: buscador público con filtros dinámicos (ciudad, tipo, rango de precio, característica) contra la base de datos.
- [x] `PropiedadDetalleServlet` + `propiedad-detalle.jsp`: ficha pública con galería, características e inmobiliaria a cargo; contacto completo solo visible con sesión activa.
- [x] Landing page (`index.jsp`) conectada a la base de datos real: combos del buscador rápido con los IDs reales y destacadas dinámicas.
- [x] Los tres dashboards (`admin.jsp`, `cliente.jsp`, `inmobiliaria.jsp`) enlazados a los módulos funcionales reales en lugar de tarjetas estáticas.

## Criterios de Cierre
- Un agente puede publicar una propiedad con varias imágenes y varias características desde una sola pantalla, y verla reflejada de inmediato en el catálogo público.
- Publicar dos propiedades con la misma matrícula muestra un mensaje claro, no una excepción de Java.
- Un cliente puede filtrar el catálogo por ciudad, tipo, rango de precio y característica, combinando varios filtros a la vez.
- Un administrador puede asignar o revocar un rol a cualquier usuario y el cambio se refleja de inmediato en su próximo inicio de sesión.
- Editar el perfil con un documento ya usado por otro usuario muestra un mensaje claro (UNIQUE `perfil.documento`).
