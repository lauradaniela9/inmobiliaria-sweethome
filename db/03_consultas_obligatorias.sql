-- CONSULTAS OBLIGATORIAS - Sweet Home
USE db_inmobiliaria;

-- 1. INNER JOIN de 3+ tablas: propiedades con ciudad, tipo e inmobiliaria
SELECT p.id_propiedad, p.titulo, c.nombre AS ciudad, tp.nombre AS tipo,
       i.nombre_comercial AS inmobiliaria
FROM propiedad p
INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad
INNER JOIN tipo_propiedad tp ON tp.id_tipo_propiedad = p.id_tipo_propiedad
INNER JOIN inmobiliaria i ON i.id_usuario = p.id_usuario_inmobiliaria
ORDER BY p.fecha_publicacion DESC;

-- 2. INNER JOIN de 3+ tablas: solicitudes con cliente, propiedad e inmobiliaria
SELECT s.id_solicitud, s.tipo, s.estado, p.titulo,
       CONCAT(pr.nombres, ' ', pr.apellidos) AS cliente,
       i.nombre_comercial AS inmobiliaria
FROM solicitud s
INNER JOIN propiedad p ON p.id_propiedad = s.id_propiedad
INNER JOIN perfil pr ON pr.id_usuario = s.id_cliente
INNER JOIN inmobiliaria i ON i.id_usuario = p.id_usuario_inmobiliaria
ORDER BY s.fecha_solicitud DESC;

-- 3. N:M: características asociadas a cada propiedad
SELECT p.titulo, c.nombre AS caracteristica, pc.cantidad
FROM propiedad p
INNER JOIN propiedad_caracteristica pc ON pc.id_propiedad = p.id_propiedad
INNER JOIN caracteristica c ON c.id_caracteristica = pc.id_caracteristica
ORDER BY p.titulo, c.nombre;

-- 4. LEFT JOIN: propiedades que todavía no tienen citas
SELECT p.id_propiedad, p.titulo, p.estado
FROM propiedad p
LEFT JOIN cita c ON c.id_propiedad = p.id_propiedad
WHERE c.id_cita IS NULL;

-- 5. GROUP BY + HAVING: propiedades por ciudad y estado (mismo criterio que usa
--    ReporteDAO.propiedadesPorCiudadYEstado(), que alimenta el reporte del administrador)
SELECT c.nombre AS ciudad, p.estado, COUNT(*) AS total_propiedades
FROM propiedad p
INNER JOIN ciudad c ON c.id_ciudad = p.id_ciudad
GROUP BY c.id_ciudad, c.nombre, p.estado
HAVING COUNT(*) >= 1
ORDER BY c.nombre, total_propiedades DESC;
