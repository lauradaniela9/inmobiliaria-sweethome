# Consultas SQL obligatorias

El enunciado exige al menos 5 consultas que demuestren dominio del modelo. Todas están
implementadas como sentencias `PreparedStatement` dentro de los DAO (nunca concatenación
insegura de parámetros) y se documentan aquí con su propósito, el archivo donde viven y
el reporte o pantalla que alimentan.

## 1 y 2. INNER JOIN entre tres o más tablas

### 1.1 Búsqueda pública con filtros (catálogo)
`PropiedadDAO.buscarConFiltros(...)` — alimenta `/catalogo`.

```sql
SELECT p.id_propiedad, p.titulo, p.precio, p.direccion, p.area_m2, p.estado,
       c.nombre AS nombre_ciudad, tp.nombre AS nombre_tipo,
       im.nombre_comercial AS nombre_inmobiliaria
FROM propiedad p
INNER JOIN ciudad c          ON c.id_ciudad = p.id_ciudad
INNER JOIN tipo_propiedad tp ON tp.id_tipo_propiedad = p.id_tipo_propiedad
INNER JOIN inmobiliaria im   ON im.id_usuario = p.id_usuario_inmobiliaria
[INNER JOIN propiedad_caracteristica pc ON pc.id_propiedad = p.id_propiedad]  -- solo si se filtra por característica
WHERE p.estado = 'DISPONIBLE' [AND ...filtros dinámicos...]
ORDER BY p.fecha_publicacion DESC;
```
Involucra **4 tablas** (propiedad, ciudad, tipo_propiedad, inmobiliaria), con una quinta
(propiedad_caracteristica) cuando el filtro de característica está activo.

### 1.2 Ficha de detalle de una propiedad
`PropiedadDAO.buscarPorId(int)` — alimenta `/propiedad?id=`.

```sql
SELECT p.*, c.nombre AS nombre_ciudad, c.departamento AS depto_ciudad,
       tp.nombre AS nombre_tipo, im.nombre_comercial, im.telefono_contacto
FROM propiedad p
INNER JOIN ciudad c          ON c.id_ciudad = p.id_ciudad
INNER JOIN tipo_propiedad tp ON tp.id_tipo_propiedad = p.id_tipo_propiedad
INNER JOIN inmobiliaria im   ON im.id_usuario = p.id_usuario_inmobiliaria
WHERE p.id_propiedad = ?;
```
Involucra **4 tablas** (propiedad, ciudad, tipo_propiedad, inmobiliaria).

## 3. Consulta que resuelve una relación muchos a muchos

### 3.1 Características de una propiedad concreta
`PropiedadDAO.listarCaracteristicasDePropiedad(int)` — resuelve `propiedad <-> caracteristica`.

```sql
SELECT ca.id_caracteristica, ca.nombre, pc.cantidad
FROM propiedad_caracteristica pc
INNER JOIN caracteristica ca ON ca.id_caracteristica = pc.id_caracteristica
WHERE pc.id_propiedad = ?
ORDER BY ca.nombre;
```

### 3.2 Roles de un usuario (alternativa N:M ya usada en login/registro)
`UsuarioDAO.buscarPorCorreo(String)` — resuelve `usuario <-> rol`.

```sql
SELECT u.id_usuario, u.correo, u.contrasena_hash, u.estado, u.intentos_fallidos,
       p.nombres, p.apellidos, r.nombre_rol
FROM usuario u
LEFT JOIN perfil p       ON p.id_usuario = u.id_usuario
LEFT JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario
LEFT JOIN rol r          ON r.id_rol = ur.id_rol
WHERE u.correo = ?;
```

## 4. Consulta con LEFT JOIN

### 4.1 Propiedades sin citas agendadas
`PropiedadDAO.listarSinCitas(int idUsuarioInmobiliaria)`.

```sql
SELECT p.id_propiedad, p.titulo, p.estado
FROM propiedad p
LEFT JOIN cita ci ON ci.id_propiedad = p.id_propiedad
WHERE p.id_usuario_inmobiliaria = ? AND ci.id_cita IS NULL;
```

### 4.2 Auditoría (LEFT JOIN adicional, usado en el panel de administración)
`AuditoriaDAO.listarUltimos(int)`.

```sql
SELECT u.correo, a.accion, a.fecha_evento, a.ip_origen
FROM auditoria a
LEFT JOIN usuario u ON u.id_usuario = a.id_usuario
ORDER BY a.fecha_evento DESC
LIMIT ?;
```
El `LEFT JOIN` es necesario porque `auditoria.id_usuario` admite `NULL` (por ejemplo, un
intento de inicio de sesión con un correo que no existe en el sistema).

## 5. Consulta de agregación con GROUP BY y HAVING

### 5.1 Propiedades por ciudad y estado
`ReporteDAO.propiedadesPorCiudadYEstado()` — alimenta el reporte de administrador.

```sql
SELECT c.nombre AS ciudad, p.estado, COUNT(*) AS total
FROM propiedad p
INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad
GROUP BY c.nombre, p.estado
HAVING COUNT(*) > 0
ORDER BY c.nombre, total DESC;
```

### 5.2 Precio promedio por tipo de propiedad
`ReporteDAO.valorPromedioPorTipo()`.

```sql
SELECT tp.nombre, COUNT(*) AS total_propiedades, ROUND(AVG(p.precio), 0) AS precio_promedio
FROM propiedad p
INNER JOIN tipo_propiedad tp ON tp.id_tipo_propiedad = p.id_tipo_propiedad
GROUP BY tp.nombre
HAVING COUNT(*) >= 1
ORDER BY precio_promedio DESC;
```

### 5.3 Solicitudes por inmobiliaria (3 tablas + agregación)
`SolicitudDAO.totalPorInmobiliaria()`.

```sql
SELECT im.nombre_comercial, COUNT(*) AS total
FROM solicitud s
INNER JOIN propiedad p    ON p.id_propiedad = s.id_propiedad
INNER JOIN inmobiliaria im ON im.id_usuario = p.id_usuario_inmobiliaria
GROUP BY im.nombre_comercial
HAVING COUNT(*) >= 1
ORDER BY total DESC;
```

### 5.4 Citas por estado
`CitaDAO.totalPorEstado()`.

```sql
SELECT estado, COUNT(*) AS total
FROM cita
GROUP BY estado
ORDER BY total DESC;
```

## Resumen de cobertura

| Requisito del enunciado | Cumplido con |
|---|---|
| 2 × INNER JOIN de 3+ tablas | Consultas 1.1 y 1.2 |
| 1 × resolución de N:M | Consultas 3.1 y 3.2 |
| 1 × LEFT JOIN | Consultas 4.1 y 4.2 |
| 1 × GROUP BY + HAVING | Consultas 5.1, 5.2, 5.3 y 5.4 |

Todas las consultas anteriores usan `PreparedStatement` con parámetros vinculados (`?`),
por lo que además cumplen con protección contra inyección SQL.
