# Sprint 1 – Retrospective

**Fecha:** [completar]

## ¿Qué salió bien?
- Diseñar el modelo de datos completo antes de escribir código evitó retrabajo en las tablas de autenticación.
- Centralizar la cadena de conexión en `db.properties` facilitó probar en local sin tocar clases Java.
- Usar BCrypt desde el día 1 evitó tener que migrar contraseñas en texto plano más adelante.

## ¿Qué se puede mejorar?
- La estimación de la historia 4 (roles) fue optimista: se subestimó el trabajo de administración de roles vía interfaz.
- Falta automatizar las pruebas del filtro de acceso (por ahora se probaron manualmente cambiando roles en la sesión).
- El tablero de seguimiento se actualizó al final del sprint y no día a día.

## Acciones para el Sprint 2
1. Mover al inicio del Sprint 2 el CRUD de administración de usuarios/roles pendiente.
2. Actualizar el tablero (Trello/GitHub Projects) con cada commit relevante, no al cierre del sprint.
3. Escribir al menos 3 pruebas unitarias sobre `PasswordUtil` y `UsuarioDAO` antes de avanzar con el CRUD de propiedades.
