# Sprint 1 – Cimientos y acceso

**Duración:** 7 días
**Product Owner:** Docente (Julian Barney Jaimes Rincón)
**Scrum Master / Development Team:** [tu nombre / equipo]
**Fecha de inicio:** [completar]
**Fecha de cierre:** [completar]

## Objetivo del Sprint
Dejar listos los cimientos técnicos del sistema: el modelo de datos normalizado
y su base de datos funcional, la conexión JDBC centralizada, la landing page
pública y el módulo de autenticación completo (registro, login, logout) con
contraseñas cifradas y control de acceso por rol validado en el servidor.

## Historias de usuario seleccionadas (Sprint Backlog)

| # | Historia | Prioridad | Estimación | Estado |
|---|----------|-----------|------------|--------|
| 1 | Como visitante, quiero una página de aterrizaje atractiva para conocer la inmobiliaria y buscar propiedades rápidamente. | Alta | 5 pts | Hecho |
| 2 | Como usuario, quiero registrarme con un correo único y validado para crear mi cuenta sin duplicados en el sistema. | Alta | 5 pts | Hecho |
| 3 | Como usuario registrado, quiero iniciar y cerrar sesión de forma segura para que el sistema me lleve al panel que corresponde a mi rol. | Alta | 8 pts | Hecho |
| 4 (parcial) | Como administrador, quiero asignar y revocar roles a los usuarios para controlar los permisos de la aplicación. | Alta | 3 pts (modelo N:M listo; CRUD de roles se completa en Sprint 2) | En progreso |

**Total estimado:** 21 puntos de historia.

## Tareas técnicas del Sprint 1
- [x] Diseñar el MER completo (16 entidades, relaciones 1:1, 1:N y N:M).
- [x] Normalizar el modelo relacional hasta 3FN.
- [x] Elaborar diccionario de datos.
- [x] Script DDL con llaves primarias, foráneas, ON DELETE/ON UPDATE y UNIQUE.
- [x] Script DML con datos de prueba (mínimo 10 registros por tabla principal).
- [x] Clase `ConexionBD` con cadena de conexión centralizada y configurable (`db.properties`).
- [x] Landing page responsiva (Bootstrap) con buscador rápido y destacadas.
- [x] Registro de usuarios con validación de campos, formato de correo y captura del error de correo duplicado (UNIQUE).
- [x] Cifrado de contraseñas con BCrypt (`PasswordUtil`).
- [x] Login con `HttpSession`, bloqueo tras intentos fallidos y redirección automática por rol.
- [x] `AuthFilter` (Servlet Filter) que protege `/dashboard/*` según autenticación y rol.
- [x] Página de acceso denegado.
- [x] Tres dashboards mínimos diferenciados (Administrador, Inmobiliaria, Cliente).
- [x] Registro de eventos clave en tabla `auditoria`.

## Criterios de Cierre:
- El script DDL se ejecuta sin errores y crea las 16 tablas con sus restricciones.
- El script DML inserta los datos de prueba sin violar ninguna restricción.
- Un visitante puede registrarse, la contraseña queda cifrada en la base de datos (nunca en texto plano).
- Un usuario registrado puede iniciar sesión y es redirigido automáticamente al panel de su rol.
- Si un usuario intenta acceder por URL a un panel que no le corresponde (o sin iniciar sesión), es redirigido a "acceso denegado".
- El intento de registrar un correo repetido muestra un mensaje claro, no una excepción de Java.
