# Sweet Home – Sistema Web de Inmobiliaria

# Sistema de Gestión Inmobiliaria

Aplicación web para la gestión de un negocio inmobiliario: publicación y búsqueda de propiedades, agendamiento de citas, radicación y aprobación de solicitudes de compra/arriendo, y paneles diferenciados por rol (Administrador, Inmobiliaria/Agente y Cliente).

Desarrollada con **Java EE (Servlets/Filters), JSP y JDBC**, sobre una base de datos **MySQL/MariaDB**.

---

## 1. Arquitectura del proyecto

El sistema está organizado en capas, siguiendo el patrón MVC clásico de Java EE:

```
Navegador
   │
   ▼
Vista (JSP + JSTL)                → páginas .jsp / fragmentos .jspf (header, topbar)
   │
   ▼
Controlador (Servlets + Filters)  → *Servlet.java (login, propiedades, citas, solicitudes...)
   │                                 Filter.java (control de acceso por sesión y rol)
   ▼
Acceso a datos (DAO)              → *DAO.java (UsuarioDAO, PropiedadDAO, CitaDAO, SolicitudDAO...)
   │
   ▼
Conexión (JDBC)                   → ConexionBD.java + db.properties
   │
   ▼
Base de datos (MySQL/MariaDB)     → db_inmobiliaria
```

**Puntos clave de diseño:**
- La conexión a la base de datos está centralizada en `ConexionBD.java`, que lee los parámetros desde `db.properties`. Ningún DAO abre su propia cadena de conexión.
- Las contraseñas nunca se guardan en texto plano: se cifran con BCrypt (`PasswordUtil.java`).
- El control de acceso por rol se hace con un `Filter` que revisa la sesión antes de dejar pasar a cualquier ruta privada.
- Toda acción de inicio de sesión queda registrada en la tabla `auditoria`, para trazabilidad.

### Roles del sistema

| Rol | Qué puede hacer |
|---|---|
| **Administrador** | Asignar/quitar roles, ver reportes (propiedades por ciudad y estado, solicitudes por inmobiliaria), desbloquear cuentas, ver auditoría. |
| **Inmobiliaria / Agente** | Publicar y editar propiedades (con imágenes y características), gestionar citas, aprobar o rechazar solicitudes de sus propiedades. |
| **Cliente** | Buscar y filtrar propiedades, marcar favoritos, agendar citas, radicar solicitudes de compra/arriendo y ver su estado. |

### Modelo de datos

La base de datos `db_inmobiliaria` está normalizada hasta 3FN e incluye 16 tablas agrupadas en:
- **Seguridad y usuarios:** `usuario`, `perfil`, `inmobiliaria`, `rol`, `usuario_rol`, `auditoria`.
- **Catálogos:** `ciudad`, `tipo_propiedad`, `caracteristica`.
- **Propiedades:** `propiedad`, `propiedad_caracteristica` (N:M), `imagen_propiedad` (1:N), `favorito` (N:M).
- **Proceso comercial:** `cita`, `solicitud`, `documento_solicitud` (1:N).

El detalle de cada tabla y columna está en el **Diccionario de Datos** (documento de diseño, PDF/Word), junto con el MER y el Modelo Relacional.

---

## 2. Cómo ejecutar el proyecto en local

**Requisitos:**
- JDK 8 o superior
- Apache Tomcat 9 (o compatible con Servlet API 3.1/4.0)
- MySQL o MariaDB
- Eclipse (o el IDE de preferencia) con soporte para proyectos Maven/Dynamic Web Project

**Pasos:**
1. Clonar el repositorio.
2. Crear la base de datos `db_inmobiliaria` y ejecutar en orden:
   - `sql/01_ddl.sql` (estructura: tablas, PK, FK, UNIQUE)
   - `sql/02_dml.sql` (datos de prueba, mínimo 10 registros por tabla principal)
3. Configurar `src/.../db.properties` con la URL, usuario y contraseña de tu MySQL local.
4. Importar el proyecto en Eclipse **apuntando directamente a la carpeta que contiene el `pom.xml`** (no a una carpeta contenedora), para evitar builds corruptos.
5. Desplegar en Apache Tomcat y acceder desde `http://localhost:8080/inmobiliaria/`.

**Despliegue en línea:** [completar con el enlace del servidor donde quede publicado, ej. Render, Railway, InfinityFree, etc.]

---

## 3. Metodología de trabajo

El proyecto se desarrolló bajo **Scrum**, en **3 sprints de 7 días cada uno**, gestionando un backlog de **14 historias de usuario** en un tablero Padlet (ver evidencia en la carpeta `/docs/scrum`). A continuación el detalle de cada sprint: planificación, revisión y retrospectiva.

---

## Sprint 1 – Cimientos y acceso
**Duración:** 26 de agosto – 1 de septiembre de 2026 (7 días)

### Sprint Planning

**Objetivo:** dejar lista la base de datos, la conexión centralizada y el login con roles.

**Historias tomadas del backlog:**
- Como visitante quiero una landing page atractiva para conocer la inmobiliaria y buscar propiedades rápido. (Alta)
- Como usuario quiero registrarme con correo único, sin poder duplicar cuenta. (Alta)
- Como usuario quiero iniciar/cerrar sesión seguro y que me mande al panel según mi rol. (Alta)
- Como admin quiero poder asignar y quitar roles a los usuarios. (Alta)
- Como admin quiero que el sistema registre automáticamente los inicios de sesión en una bitácora de auditoría, para tener trazabilidad de lo que pasa en el sistema. (Media)

**Estimación:** ~24 puntos.

**Qué se construyó:**
- MER y modelo relacional en 3FN.
- Script DDL (tablas, PK, FK con ON DELETE/ON UPDATE, UNIQUE) y DML con datos de prueba.
- Conexión JDBC centralizada en `ConexionBD.java` + `db.properties`, para no repetir la cadena de conexión en cada DAO.
- Contraseñas cifradas con BCrypt (`PasswordUtil.java`), nada en texto plano.
- `LoginServlet`: validar credenciales, revisar estado de la cuenta, redirigir según rol.
- `Filter` que bloquea rutas privadas si no hay sesión o el rol no corresponde.
- `header.jspf` y `topbar-publico.jspf` para no repetir el menú en cada JSP.
- Registro automático en la tabla `auditoria` cada vez que un usuario inicia sesión.

### Sprint Review

**Evidencia:**
- Landing page y catálogo público cargando correctamente.
- Login mostrando "Correo o contraseña incorrectos" cuando falla.
- Tabla `usuario` con el UNIQUE en `correo`, y `auditoria` registrando los inicios de sesión.
- DDL exportado con los UNIQUE (`correo`, `matricula_inmobiliaria`, `perfil.id_usuario`, `usuario_rol`) y las FK con ON DELETE/ON UPDATE.

**Demo:** el login redirige solo al panel que corresponde según el rol, y si alguien intenta entrar a una ruta privada sin sesión, lo manda a acceso denegado.

### Sprint Retrospective

**Qué salió bien:** el modelo de datos quedó bien pensado desde el principio (las tres relaciones 1:1, 1:N y N:M), y la conexión centralizada funcionó a la primera; no tuvimos que volver a tocarla en todo el proyecto.

**Qué salió mal:** el bloqueo de cuenta por intentos fallidos estaba mal ordenado en el `LoginServlet` — primero se validaba la contraseña y después el estado de la cuenta. Si alguien fallaba la contraseña en una cuenta ya bloqueada, el sistema seguía diciendo "correo o contraseña incorrectos" en vez de avisar que estaba bloqueada, dando la sensación de que se podía seguir intentando sin límite.

**Qué mejorar:** revisar el estado de la cuenta ANTES que la contraseña, y probar los casos de error apenas se construye algo nuevo, no solo el camino más fácil.

---

## Sprint 2 – Núcleo del negocio
**Duración:** 2 – 8 de septiembre de 2026 (7 días)

### Sprint Planning

**Objetivo:** CRUD de propiedades con imágenes (1:N) y características (N:M), buscador con filtros, y paneles por rol.

**Historias tomadas del backlog:**
- Como agente quiero registrar y editar propiedades con fotos, características y precio. (Alta)
- Como cliente quiero buscar/filtrar propiedades por ciudad, tipo, precio y características. (Alta)
- Como cliente quiero completar mi perfil (documento, teléfono, dirección). (Media)
- Como cliente quiero marcar propiedades como favoritas. (Media)

**Estimación:** ~26 puntos.

**Qué se construyó:**
- `PropiedadDAO` con las consultas obligatorias (INNER JOIN propiedad+ciudad+tipo+inmobiliaria, y LEFT JOIN de propiedades sin citas).
- Formulario de publicar/editar propiedad, con imágenes que se pueden pegar por URL o subir como archivo (cada imagen se elige aparte).
- Renderizado correcto tanto de imágenes locales como externas en el catálogo y la ficha de detalle.
- Dashboards de admin, inmobiliaria y cliente con sus contadores.
- Módulo de favoritos.

### Sprint Review

**Evidencia:**
- Panel del agente publicando una propiedad con una imagen por URL y otra subida como archivo.
- Ficha de detalle con el carrusel de imágenes y las características.
- Catálogo mostrando imágenes externas (Unsplash) al lado de las locales, sin que se rompa ninguna.
- Panel de inmobiliaria con el contador de solicitudes pendientes.

**Demo:** cada rol ve un panel distinto, y una propiedad con imagen externa se ve bien tanto en el catálogo como en su detalle.

### Sprint Retrospective

**Qué salió bien:** el formulario mixto de imágenes (URL o archivo) quedó cómodo de usar, y `@MultipartConfig` en el servlet permitió mezclar campos de texto y archivos en la misma petición sin mayor problema.

**Qué salió mal:** dos bugs que costó encontrar:
1. Las imágenes con URL externa se rompían en el catálogo y en la ficha de detalle porque el código siempre le agregaba el `contextPath` adelante, incluso cuando la URL ya era completa (quedaba algo como `localhost:8080/inmobiliaria/https://...`). Se corrigió revisando si la URL empieza con `http` antes de decidir si se le antepone el contextPath.
2. Al agregar imágenes dinámicamente con JS, se usó `${variable}` dentro de un template string, pero JSP interpreta ese símbolo como su propio lenguaje en cualquier parte del archivo (incluso dentro de `<script>`), dejando las variables vacías y el botón de subir archivo sin funcionar en las filas nuevas. Se solucionó armando el HTML con concatenación (`+`) en vez de template strings.

**Qué mejorar:** probar cada funcionalidad con casos "raros" (como una URL externa) apenas se termina de programar, no solo con el caso más simple, para detectar antes este tipo de problemas.

---

## Sprint 3 – Operación y cierre
**Duración:** 9 – 15 de septiembre de 2026 (7 días)

### Sprint Planning

**Objetivo:** cerrar el flujo de negocio (citas, solicitudes, reportes), reforzar el login, completar los datos de prueba y dejar todo listo para entregar.

**Historias tomadas del backlog:**
- Como cliente quiero agendar cita en un horario disponible sin que se cruce con otra. (Media)
- Como cliente quiero radicar documentos de compra/arriendo y ver el estado de mi solicitud. (Media)
- Como agente quiero aprobar o rechazar solicitudes y su documentación. (Media)
- Como admin quiero un reporte de propiedades por ciudad y estado. (Media)
- Como admin quiero que las cuentas bloqueadas por intentos fallidos se desbloqueen automáticamente pasado un tiempo, para no depender siempre de una acción manual. (Media)

**Estimación:** ~27 puntos.

**Qué se construyó:**
- Tabla `cita` con el UNIQUE `(id_propiedad, fecha_hora)` para que no se crucen horarios.
- Radicación de `solicitud` + `documento_solicitud`, y su aprobación/rechazo desde el panel de inmobiliaria.
- Reporte con `GROUP BY` + `HAVING` (solicitudes por inmobiliaria).
- Mejora del login: campo `fecha_bloqueo` para que la cuenta se desbloquee sola a los 30 minutos, sin depender siempre de que un admin la desbloquee a mano.
- Revisión de que todas las tablas principales tuvieran al menos 10 registros de prueba (la tabla `inmobiliaria` se había quedado con solo 1 registro).
- Exportación del DDL y DML finales, y publicación del repositorio en GitHub.

### Sprint Review

**Evidencia:**
- Agendamiento de cita bloqueando el cruce de horario por la restricción UNIQUE.
- Panel de inmobiliaria mostrando las solicitudes en `EN_REVISION`, con botones de aprobar/rechazar.
- Mensaje de "cuenta bloqueada temporalmente" al intentar entrar con una cuenta bloqueada, y esa misma cuenta desbloqueándose sola pasados los 30 minutos.
- Tabla `inmobiliaria` ya con los 10 registros.
- Repositorio en GitHub con el código publicado.

**Demo:** se mostró el flujo completo — el cliente agenda y radica, el agente aprueba, y el cambio se refleja en ambos lados.

### Sprint Retrospective

**Qué salió bien:** el bloqueo de cuenta quedó completo — se bloquea a los 5 intentos, se desbloquea solo a los 30 minutos, y el admin igual puede desbloquearla antes si quiere.

**Qué salió mal:**
- Se encontró un error de "uno de más" en el SQL que aumenta el contador de intentos fallidos: como MySQL usa el valor ya actualizado dentro del mismo UPDATE, la cuenta se estaba bloqueando en el intento 4 en vez del 5. Se corrigió quitando el `+1` que sobraba en el `CASE`.
- Un contador del panel ("Solicitudes pendientes") siempre marcaba 0 porque comparaba contra `'PENDIENTE'`, un valor que no existe en el estado de `solicitud` (que usa `EN_REVISION`, `APROBADA`, `RECHAZADA`). Se corrigió el valor de comparación.
- Casi al final, el proyecto dejó de arrancar en Tomcat por un `ClassNotFoundException`. Resultó que en Eclipse había quedado un proyecto duplicado (una carpeta contenedora vieja del .zip original, mal configurada como Maven), generando errores falsos. Ese mismo problema hizo que, al subir a GitHub por primera vez, se subiera esa carpeta vieja en vez del proyecto real.

**Qué mejorar:** revisar bien, antes de importar en Eclipse o de inicializar Git, que se está apuntando justo a la carpeta que tiene el `pom.xml` y no a una carpeta contenedora — habría ahorrado tiempo de vueltas al final del proyecto.

---

## 4. Estructura del repositorio

```
/src                 → código fuente Java (Servlets, Filters, DAO, utilidades)
/WebContent (o /webapp)  → JSP, JS, CSS, fragmentos .jspf
/sql
  01_ddl.sql         → script de creación de tablas, PK, FK, UNIQUE
  02_dml.sql         → datos de prueba (mínimo 10 registros por tabla principal)
/docs
  Diccionario_de_Datos.docx  → MER, modelo relacional y diccionario de datos
  Sprint_1.md, Sprint_2.md, Sprint_3.md → detalle de cada sprint (contenido también integrado en este README)
  scrum/             → evidencia del tablero (capturas de Padlet)
README.md
```
