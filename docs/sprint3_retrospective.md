# Sprint 3 – Retrospective

**Fecha:** [completar]

## ¿Qué salió bien?
- Definir desde el Sprint 1 la restricción UNIQUE `cita(id_propiedad, fecha_hora)` evitó tener que agregar validaciones de conflicto de horario en la aplicación desde cero: bastó con capturar la excepción de integridad y traducirla a un mensaje claro.
- Centralizar la ruta de subida de archivos en el `context-param upload.dir` (en vez de hardcodearla en cada servlet) permitió cambiarla fácilmente entre el entorno local y el de despliegue.
- Escribir el documento de consultas SQL obligatorias al final, citando directamente el DAO y método donde vive cada una, hizo mucho más rápida la preparación de la sustentación.

## ¿Qué se puede mejorar?
- Los documentos radicados en `db/02_dml.sql` apuntan a rutas de ejemplo (`/docs/...`) que no existen físicamente en disco; solo los documentos cargados a través de la aplicación (bajo `/uploads/...`) son descargables. Se documenta como una limitación conocida de los datos de prueba.
- No se automatizaron pruebas de integración contra la base de datos (solo pruebas unitarias de `Validaciones` y verificación manual del resto de los flujos).
- El despliegue en línea de la aplicación (WAR + Tomcat en un proveedor gratuito) depende de servicios externos cuya disponibilidad puede cambiar; se documentaron varias alternativas para no depender de una sola.

## Cierre del proyecto
Las tres historias de valor agregado sugeridas por el enunciado (bloqueo temporal de cuenta,
recuperación de contraseña por correo, auditoría) se cubrieron parcialmente: el bloqueo tras
intentos fallidos y la auditoría quedaron implementados; la recuperación de contraseña por
correo se deja fuera de alcance por requerir un servidor SMTP, y se documenta como
extensión futura para no comprometer la entrega a tiempo.
