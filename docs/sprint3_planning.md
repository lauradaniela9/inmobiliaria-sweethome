# Sprint 3 – Operación y cierre

**Duración:** 7 días
**Product Owner:** Docente (Julian Barney Jaimes Rincón)
**Scrum Master / Development Team:** [tu nombre / equipo]
**Fecha de inicio:** [completar]
**Fecha de cierre:** [completar]

## Objetivo del Sprint
Completar el ciclo operativo del negocio (citas, solicitudes, documentos y favoritos),
construir los reportes consolidados exigidos por el enunciado, dejar la documentación
final (diccionario de datos completo, consultas SQL obligatorias documentadas, guía de
despliegue) y validar el sistema de punta a punta antes de la sustentación.

## Historias de usuario seleccionadas (Sprint Backlog)

| # | Historia | Prioridad | Estimación | Estado |
|---|----------|-----------|------------|--------|
| 8 | Como cliente, quiero marcar propiedades como favoritas para consultarlas más adelante sin tener que buscarlas de nuevo. | Media | 2 pts | Hecho |
| 9 | Como cliente, quiero solicitar una cita en un horario disponible para visitar el inmueble sin que se crucen las agendas. | Media | 5 pts | Hecho |
| 10 | Como cliente, quiero radicar los documentos de compra o arriendo y consultar el estado de mi solicitud. | Media | 5 pts | Hecho |
| 11 | Como agente de la inmobiliaria, quiero aprobar o rechazar las solicitudes y sus documentos para dar trámite a la negociación. | Media | 3 pts | Hecho |
| 12 | Como administrador, quiero un reporte de propiedades por ciudad y estado, generado con consultas de agregación, para tomar decisiones. | Media | 3 pts | Hecho |
| 13 | Como administrador, quiero consultar la auditoría de accesos y cambios para hacer seguimiento a la operación del sistema. | Baja | 2 pts | Hecho |

**Total estimado:** 20 puntos de historia.

## Tareas técnicas del Sprint 3
- [x] `FavoritoDAO` + `FavoritoServlet` + `favoritos.jsp`: marcar/desmarcar desde la ficha de detalle y desde el listado propio.
- [x] `CitaDAO` + `CitaClienteServlet`/`CitaInmobiliariaServlet` + JSP correspondientes: agendamiento con captura del conflicto de horario (UNIQUE `cita(id_propiedad, fecha_hora)`), confirmación/rechazo por la inmobiliaria y cancelación por el cliente.
- [x] `SolicitudDAO` + `SolicitudClienteServlet`/`SolicitudInmobiliariaServlet` + JSP correspondientes: radicación de compra/arriendo con carga real de documentos (`multipart/form-data`, `Part`), adjuntar documentos adicionales y aprobación/rechazo por la inmobiliaria.
- [x] `DescargaDocumentoServlet`: sirve los documentos cargados solo a usuarios autenticados (no expone archivos a un visitante anónimo).
- [x] `ReporteDAO` + `ReporteAdminServlet`/`ReporteInmobiliariaServlet` + JSP correspondientes: propiedades por ciudad/estado, precio promedio por tipo, solicitudes por inmobiliaria (todas con `GROUP BY`/`HAVING`) y ventas/arriendos propios de cada inmobiliaria.
- [x] `AuditoriaDAO` + `AuditoriaServlet` + `auditoria.jsp`: últimos 100 eventos con `LEFT JOIN` a usuario.
- [x] Documentación final: diccionario de datos completo (16 tablas), documento de consultas SQL obligatorias, guía de despliegue en línea y actualización del `README.md`.

## Definition of Done (DoD) del Sprint
- Un cliente puede agendar una cita y, si otro cliente ya la agendó en el mismo horario para la misma propiedad, recibe un mensaje claro (no una excepción de Java).
- Un cliente puede radicar una solicitud, adjuntar un documento y consultar el estado de su trámite; la inmobiliaria puede revisar el documento y aprobar/rechazar.
- El panel de administrador muestra los 4 reportes exigidos (propiedades por ciudad/estado, valor por tipo, citas por estado, solicitudes por inmobiliaria) y la auditoría de los últimos eventos.
- Toda la documentación exigida por el enunciado (MER, relacional 3FN, diccionario, DDL/DML, consultas documentadas, Scrum de los 3 sprints) está en el repositorio.
