# Sweet Home – Sistema Web de Inmobiliaria

Proyecto académico UTS – Tecnología en Desarrollo de Sistemas Informáticos.
Entrega completa: **Sprint 1 (Cimientos y acceso) + Sprint 2 (Núcleo del negocio)
+ Sprint 3 (Operación y cierre)**.

Java EE (JSP) + JDBC + MySQL, arquitectura MVC con Servlets como controladores,
Bootstrap 5 para el frontend responsivo y Apache Tomcat como servidor de aplicaciones.

## Contenido de esta entrega
- `db/01_ddl.sql` — Script de creación de la base de datos (16 tablas, 3FN, relaciones 1:1, 1:N, N:M, restricciones UNIQUE).
- `db/02_dml.sql` — Datos de prueba (mínimo 10 registros por tabla principal).
- `src/main/java/...` — Modelos, DAO, servlets (controladores) y filtro de control de acceso por rol.
- `src/main/webapp/...` — Landing page, catálogo público, ficha de detalle, formularios de registro/login,
  página de acceso denegado y los tres paneles diferenciados con todos sus módulos.
- `src/test/java/...` — Pruebas unitarias JUnit 5 (`Validaciones`).
- `docs/` — Documentación Scrum de los 3 sprints (Planning/Review/Retrospective), diccionario de datos
  completo, documento de las consultas SQL obligatorias y guía de despliegue en línea.

## Módulos funcionales por rol

**Visitante (sin sesión):** landing page, catálogo público con filtros, ficha de detalle de cada
propiedad (sin datos completos de contacto), registro e inicio de sesión.

**Cliente:** todo lo del visitante + favoritos, agendamiento y cancelación de citas, radicación de
solicitudes de compra/arriendo con carga de documentos, edición de perfil (1:1).

**Inmobiliaria (agente):** CRUD de propiedades con imágenes (1:N) y características (N:M), baja
lógica, gestión de citas recibidas (confirmar/rechazar/marcar realizada), gestión de solicitudes
recibidas (revisar documentos, aprobar/rechazar), reporte propio de ventas/arriendos.

**Administrador:** gestión de usuarios y asignación/revocación de roles, reportes consolidados del
sistema completo (propiedades por ciudad/estado, precio promedio por tipo, citas por estado,
solicitudes por inmobiliaria) y auditoría de accesos y cambios.

## Cómo ejecutar en local

### 1. Base de datos
```bash
mysql -u root -p < db/01_ddl.sql
mysql -u root -p db_inmobiliaria < db/02_dml.sql
```
Ajusta usuario/contraseña de MySQL en `src/main/resources/db.properties` si es necesario.
La cadena de conexión está centralizada ahí (clase `ConexionBD`); no se repite en ninguna otra clase.

### 2. Compilar y desplegar
Proyecto **Maven** estándar (`packaging: war`). Con Eclipse/IntelliJ + Tomcat:
1. Importar como proyecto Maven existente.
2. Verificar que Maven descargue las dependencias (`jakarta.servlet-api`, `mysql-connector-java`, `jbcrypt`, `junit-jupiter`).
3. Agregar el servidor Apache Tomcat (10.x, Jakarta EE 9+) al IDE.
4. Ejecutar como "Run on Server".
5. Abrir `http://localhost:8080/inmobiliaria/`.

O por línea de comandos:
```bash
mvn clean package
# copiar target/inmobiliaria.war a la carpeta webapps de Tomcat
```

### 3. Ejecutar las pruebas unitarias
```bash
mvn test
```

## Usuarios de prueba (ver `db/02_dml.sql`)

| Correo | Rol |
|---|---|
| admin@inmobiliaria.com | ADMINISTRADOR |
| agenteA@inmobiliaria.com / agenteB@inmobiliaria.com / agenteC@inmobiliaria.com | INMOBILIARIA |
| cliente1@correo.com … cliente6@correo.com | CLIENTE (cliente6 está INACTIVO, para probar ese caso) |

## Despliegue en línea
Ver `docs/guia_despliegue.md` para el paso a paso (base de datos gestionada + Docker/Tomcat en un
proveedor con capa gratuita).

## Cobertura del enunciado

- [x] Página de aterrizaje pública, responsiva y dinámica (destacadas y buscador contra la BD real).
- [x] Registro con correo único (UNIQUE + mensaje claro, no excepción cruda).
- [x] Contraseñas cifradas con BCrypt (nunca texto plano).
- [x] Login con `HttpSession`, bloqueo temporal tras 5 intentos fallidos y redirección automática por rol.
- [x] `AuthFilter` (Servlet Filter) que protege `/dashboard/*` en el servidor, no solo ocultando botones en la vista.
- [x] Página de acceso denegado.
- [x] Modelo de datos completo: 16 tablas, 3FN, las 3 relaciones exigidas (1:1, 1:N, N:M) y 5 campos/llaves UNIQUE.
- [x] Script DDL y DML con datos de prueba (mínimo 10 registros por tabla principal).
- [x] CRUD completo de propiedades con imágenes y características, baja lógica.
- [x] Buscador con filtros reales contra la base de datos (ciudad, tipo, precio, característica).
- [x] Perfil de usuario (1:1) editable con validaciones.
- [x] Favoritos, citas (con control de conflicto de horario) y solicitudes con carga de documentos.
- [x] Reportes con `INNER JOIN` (2), resolución de N:M, `LEFT JOIN` y `GROUP BY`/`HAVING` — documentados en `docs/consultas_sql_obligatorias.md`.
- [x] Pruebas unitarias JUnit 5 sobre `Validaciones`.
- [x] Documentación Scrum de los 3 sprints (Planning/Review/Retrospective).
- [x] Diccionario de datos completo de las 16 tablas.
- [x] Guía de despliegue en línea.

- Carga de imágenes de propiedad como archivo binario (se maneja por URL); los documentos de
  solicitud sí se cargan como archivo real vía `multipart/form-data`.
