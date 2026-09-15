# Sprint 1 – Cimientos y acceso
**Duración:** 26 de agosto – 1 de septiembre de 2026 (7 días)

---

## 1. Sprint Planning

**Objetivo:** dejar lista la base de datos, la conexión centralizada y el login con roles.

**Historias que tomamos del backlog:**
- Como visitante quiero una landing page atractiva para conocer la inmobiliaria y buscar propiedades rápido. (Alta)
- Como usuario quiero registrarme con correo único, sin poder duplicar cuenta. (Alta)
- Como usuario quiero iniciar/cerrar sesión seguro y que me mande al panel según mi rol. (Alta)
- Como admin quiero poder asignar y quitar roles a los usuarios. (Alta)

**Estimación:** ~21 puntos.

**Qué había que hacer:**
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


## 2. Sprint Review

**Evidencia:**
- Landing page y catálogo público cargando bien.
- Login mostrando "Correo o contraseña incorrectos" cuando falla.
- Tabla `usuario` en phpMyAdmin con el UNIQUE en `correo`, y `auditoria` registrando los inicios de sesión.
- DDL exportado con los UNIQUE (`correo`, `matricula_inmobiliaria`, `perfil.id_usuario`, `usuario_rol`) y las FK con ON DELETE/ON UPDATE.

**Demo:** el login redirige solo al panel que corresponde según el rol, y si alguien intenta entrar a una ruta privada sin sesión, lo manda a acceso denegado.

## 3. Sprint Retrospective

**Qué salió bien:** el modelo de datos quedó bien pensado desde el principio (las tres relaciones 1:1, 1:N y N:M), y la conexión centralizada funcionó a la primera, no tuvimos que volver a tocarla en todo el proyecto.

**Qué salió mal:** el bloqueo de cuenta por intentos fallidos estaba mal ordenado en el `LoginServlet` — primero validaba la contraseña y después el estado de la cuenta. Entonces si alguien fallaba la contraseña en una cuenta ya bloqueada, el sistema seguía diciendo "correo o contraseña incorrectos" en vez de avisar que estaba bloqueada. Daba la sensación de que se podía seguir intentando sin límite.

**Qué mejorar:** revisar el estado de la cuenta ANTES de la contraseña, y probar los casos de error apenas se hace algo nuevo, no solo el camino mas facil.
