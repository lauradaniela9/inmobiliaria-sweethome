# Sprint 2 – Retrospective

**Fecha:** [completar]

## ¿Qué salió bien?
- Resolver primero el CRUD de propiedades como una operación transaccional (propiedad + imágenes + características en una sola transacción con rollback) evitó dejar datos huérfanos ante un error a mitad de camino.
- Reutilizar `Validaciones` (extraída en el Sprint 1) en el formulario de propiedades confirmó que la decisión de aislarla en una clase propia fue acertada.
- Cerrar primero la deuda técnica de administración de usuarios/roles, tal como se acordó en la retrospectiva anterior, evitó que se acumulara para el Sprint 3.

## ¿Qué se puede mejorar?
- El formulario de propiedades creció bastante (imágenes dinámicas + características + catálogos); conviene dividirlo en componentes más pequeños si el proyecto creciera más allá del alcance académico.
- Aún no hay pruebas unitarias sobre los DAO (solo sobre `Validaciones`); se probó todo el flujo manualmente contra la base de datos local.
- El manejo de imágenes es solo por URL (no hay carga de archivos de imagen); se documenta como alcance del proyecto académico.

## Acciones para el Sprint 3
1. Implementar citas, solicitudes y documentos (con carga real de archivos vía `Part`/`multipart`).
2. Construir los reportes con `GROUP BY`/`HAVING` exigidos por el enunciado.
3. Dejar lista la guía de despliegue en línea y probar el flujo completo contra una base de datos en la nube.
