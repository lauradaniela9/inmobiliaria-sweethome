# Guía de despliegue en línea

El enunciado otorga puntos adicionales por desplegar tanto la base de datos como la
aplicación en línea. Esta guía cubre una ruta gratuita y reproducible.

## 1. Base de datos en línea (MySQL gestionado)

Cualquiera de estas opciones ofrece un plan gratuito/de prueba suficiente para el proyecto:

| Proveedor | Notas |
|---|---|
| **Railway** (railway.app) | Plantilla "MySQL" con un clic; expone host, puerto, usuario, clave y nombre de BD. |
| **Aiven** (aiven.io) | Plan gratuito de MySQL; requiere SSL (agregar `?useSSL=true&requireSSL=true` a la URL JDBC). |
| **Clever Cloud** (clever-cloud.com) | Add-on de MySQL con capa gratuita pequeña, ideal para un proyecto académico. |
| **FreeSQLDatabase / db4free** | Alternativas ligeras si solo se necesita algo simple para sustentar. |

Pasos generales:
1. Crear la instancia y anotar: host, puerto, nombre de la base de datos, usuario y contraseña.
2. Conectarse con un cliente MySQL (MySQL Workbench, DBeaver o la CLI) y ejecutar, en orden:
   ```bash
   mysql -h <host> -P <puerto> -u <usuario> -p < db/01_ddl.sql
   mysql -h <host> -P <puerto> -u <usuario> -p db_inmobiliaria < db/02_dml.sql
   ```
3. Verificar con `SHOW TABLES;` que las 16 tablas quedaron creadas.

## 2. Configurar la aplicación para apuntar a la base en línea

La cadena de conexión está centralizada en `src/main/resources/db.properties`
(leída una sola vez por `ConexionBD`), por lo que **no hay que tocar ninguna clase Java**:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://<host>:<puerto>/db_inmobiliaria?useSSL=true&serverTimezone=America/Bogota
db.usuario=<usuario>
db.clave=<clave>
```

Para no exponer credenciales en el repositorio Git público, se recomienda:
- Mantener un `db.properties` de ejemplo con valores ficticios en el repositorio.
- Sobrescribir el archivo real solo en el servidor de despliegue (o inyectarlo como variable
  de entorno leída por un script de arranque), y agregar el archivo real a `.gitignore`.

## 3. Aplicación (WAR) en un servidor con soporte Tomcat/Jakarta EE

Opciones gratuitas o de bajo costo compatibles con WAR + Tomcat 10 (Jakarta EE):

| Proveedor | Notas |
|---|---|
| **Render** (render.com) | Servicio "Web Service" con Docker: usar una imagen `tomcat:10-jdk21` y copiar el WAR a `webapps/`. |
| **Railway** | Igual que Render: se puede desplegar un `Dockerfile` con Tomcat 10 + el WAR generado. |
| **Un VPS gratuito/de prueba** (Oracle Cloud Free Tier, etc.) con Tomcat 10 instalado manualmente. |

### Dockerfile sugerido (colocarlo en la raíz del proyecto)
```dockerfile
FROM tomcat:10.1-jdk21
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY target/inmobiliaria.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
```

Pasos:
1. Generar el WAR: `mvn clean package`.
2. Construir la imagen: `docker build -t inmosantander .`
3. Probar localmente: `docker run -p 8080:8080 inmosantander` y abrir `http://localhost:8080/`.
4. Subir la imagen al proveedor elegido (Render/Railway detectan el `Dockerfile` automáticamente
   si se conecta el repositorio de GitHub).
5. Configurar en el panel del proveedor las variables de entorno o el archivo `db.properties`
   con los datos de la base de datos en línea del paso 1.

## 4. Verificación final antes de sustentar

- [ ] La URL pública carga `index.jsp` con las propiedades destacadas reales (confirma que la
      app sí está leyendo de la base de datos en línea).
- [ ] Un registro nuevo (`/registro`) crea el usuario en la base de datos en línea.
- [ ] Un login con los usuarios de prueba de `db/02_dml.sql` redirige al panel correcto según el rol.
- [ ] Entrar directamente por URL a una ruta de otro rol (por ejemplo, un cliente autenticado
      escribiendo `/dashboard/admin/usuarios`) muestra `acceso-denegado.jsp` (prueba del `AuthFilter`).
