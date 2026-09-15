# Sprint 1 – Retrospective

## ¿Qué salió bien?
- Diseñar el modelo de datos completo antes de escribir código evitó retrabajo en las tablas de autenticación.
- Centralizar la cadena de conexión en `db.properties` facilitó probar en local sin tocar clases Java.
- Usar BCrypt desde el día 1 evitó tener que migrar contraseñas en texto plano más adelante.

## ¿Qué se puede mejorar?
- Falta automatizar las pruebas del filtro de acceso (por ahora se probaron manualmente cambiando roles en la sesión).
- El tablero de seguimiento se actualizó al final del sprint y no día a día.
- El bloqueo de cuenta por intentos fallidos estaba mal ordenado en el `LoginServlet` — primero validaba la contraseña y después el estado de la cuenta. Entonces si alguien fallaba la contraseña en una cuenta ya bloqueada, el sistema seguía diciendo "correo o contraseña incorrectos" en vez de avisar que estaba bloqueada. Daba la sensación de que se podía seguir intentando sin límite.

**Qué mejorar:** revisar el estado de la cuenta ANTES de la contraseña, y probar los casos de error apenas se hace algo nuevo, no solo el camino mas facil.
