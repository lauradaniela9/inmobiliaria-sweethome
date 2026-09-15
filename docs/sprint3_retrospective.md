# Sprint 3 – Retrospective

## ¿Qué salió bien?
- Definir desde el Sprint 1 la restricción UNIQUE `cita(id_propiedad, fecha_hora)` evitó tener que agregar validaciones de conflicto de horario en la aplicación desde cero: bastó con capturar la excepción de integridad y traducirla a un mensaje claro.
- Centralizar la ruta de subida de archivos en el `context-param upload.dir` (en vez de hardcodearla en cada servlet) permitió cambiarla fácilmente entre el entorno local y el de despliegue.
- Escribir el documento de consultas SQL obligatorias al final, citando directamente el DAO y método donde vive cada una, hizo mucho más rápida la preparación de la sustentación.
- El bloqueo de cuenta quedó bien completo — se bloquea a los 5 intentos, se desbloquea solo a los 30 minutos, y el admin igual puede desbloquearla antes si quiere.

## ¿Qué se puede mejorar?
- Los documentos radicados en `db/02_dml.sql` apuntan a rutas de ejemplo (`/docs/...`) que no existen físicamente en disco; solo los documentos cargados a través de la aplicación (bajo `/uploads/...`) son descargables. Se documenta como una limitación conocida de los datos de prueba.
- El despliegue en línea de la aplicación (WAR + Tomcat en un proveedor gratuito) depende de servicios externos cuya disponibilidad puede cambiar; se documentaron varias alternativas para no depender de una sola.
- Encontramos un error de "uno de más" en el SQL que sube el contador de intentos fallidos: como MySQL usa el valor ya actualizado dentro del mismo UPDATE, la cuenta se estaba bloqueando en el intento 4 en vez del 5. Se arregló quitando el +1 que sobraba en el CASE.
- Un contador del panel ("Solicitudes pendientes") siempre marcaba 0 porque comparaba contra `'PENDIENTE'`, un valor que ni existe en el estado de `solicitud` (que usa `EN_REVISION`, `APROBADA`, `RECHAZADA`). Se corrigió el valor de la comparación.
- Casi al final, el proyecto dejó de arrancar en Tomcat por un `ClassNotFoundException`. Resultó que en Eclipse había quedado un proyecto duplicado (una carpeta contenedora vieja del .zip original, mal configurada como Maven) generando cientos de errores falsos. De paso, ese mismo lío hizo que al subir a GitHub por primera vez se subiera esa carpeta vieja en vez del proyecto real.

## Cierre del proyecto
Las tres historias de valor agregado sugeridas por el enunciado (bloqueo temporal de cuenta,
recuperación de contraseña por correo, auditoría) se cubrieron parcialmente: el bloqueo tras
intentos fallidos y la auditoría quedaron implementados; la recuperación de contraseña por
correo se deja fuera de alcance por requerir un servidor SMTP, y se reviso bien, antes de importar en Eclipse o de inicializar Git, que se está apuntando justo a la carpeta que tiene el `pom.xml` y no a una carpeta contenedora — nos habría ahorrado como una hora de vueltas al final.
