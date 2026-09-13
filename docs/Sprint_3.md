# Sprint 3 – Operación y cierre
**Duración:** 9 – 15 de septiembre de 2026 (7 días)

---

## 1. Sprint Planning

**Objetivo:** cerrar el flujo de negocio (citas, solicitudes, reportes), reforzar el login, completar los datos de prueba y dejar todo listo para entregar.

**Historias:**
- Como cliente quiero agendar cita en un horario disponible sin que se cruce con otra. (Media)
- Como cliente quiero radicar documentos de compra/arriendo y ver el estado de mi solicitud. (Media)
- Como agente quiero aprobar o rechazar solicitudes y su documentación. (Media)
- Como admin quiero un reporte de propiedades por ciudad y estado. (Media)
- Extra: bloqueo temporal de cuenta con desbloqueo automático por tiempo, y auditoría.

**Estimación:** ~24 puntos.

**Qué había que hacer:**
- Tabla `cita` con el UNIQUE `(id_propiedad, fecha_hora)` para que no se crucen horarios.
- Radicación de `solicitud` + `documento_solicitud`, y su aprobación/rechazo desde el panel de inmobiliaria.
- Reporte con `GROUP BY` + `HAVING` (solicitudes por inmobiliaria).
- Mejorar el login: agregar `fecha_bloqueo` para que la cuenta se desbloquee sola a los 30 minutos, sin depender siempre de que un admin la desbloquee a mano.
- Revisar que todas las tablas principales tuvieran al menos 10 registros de prueba (la tabla `inmobiliaria` se había quedado con solo 1 registro).
- Exportar el DDL y DML finales, y subir todo a un repo público en GitHub.

## 2. Sprint Review

**Evidencia:**
- Agendamiento de cita bloqueando el cruce de horario por la restricción UNIQUE.
- Panel de inmobiliaria mostrando las solicitudes en `EN_REVISION`, con botones de aprobar/rechazar.
- Mensaje de "cuenta bloqueada temporalmente" al intentar entrar con una cuenta bloqueada, y esa misma cuenta desbloqueándose sola pasados los 30 minutos.
- Tabla `inmobiliaria` ya con los 10 registros.
- Repositorio en GitHub con el código publicado.

**Demo:** se mostró el flujo completo — el cliente agenda y radica, el agente aprueba, y el cambio se refleja en ambos lados.

## 3. Sprint Retrospective

**Qué salió bien:** el bloqueo de cuenta quedó bien completo — se bloquea a los 5 intentos, se desbloquea solo a los 30 minutos, y el admin igual puede desbloquearla antes si quiere.

**Qué salió mal:**
- Encontramos un error de "uno de más" en el SQL que sube el contador de intentos fallidos: como MySQL usa el valor ya actualizado dentro del mismo UPDATE, la cuenta se estaba bloqueando en el intento 4 en vez del 5. Se arregló quitando el +1 que sobraba en el CASE.
- Un contador del panel ("Solicitudes pendientes") siempre marcaba 0 porque comparaba contra `'PENDIENTE'`, un valor que ni existe en el estado de `solicitud` (que usa `EN_REVISION`, `APROBADA`, `RECHAZADA`). Se corrigió el valor de la comparación.
- Casi al final, el proyecto dejó de arrancar en Tomcat por un `ClassNotFoundException`. Resultó que en Eclipse había quedado un proyecto duplicado (una carpeta contenedora vieja del .zip original, mal configurada como Maven) generando cientos de errores falsos. De paso, ese mismo lío hizo que al subir a GitHub por primera vez se subiera esa carpeta vieja en vez del proyecto real.

**Qué mejorar:** revisar bien, antes de importar en Eclipse o de inicializar Git, que se está apuntando justo a la carpeta que tiene el `pom.xml` y no a una carpeta contenedora — nos habría ahorrado como una hora de vueltas al final.
