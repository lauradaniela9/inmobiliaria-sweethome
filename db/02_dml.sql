-- =====================================================================
-- SCRIPT DML - Datos de prueba
-- Hash BCrypt 
-- (En producción cada hash se genera dinámicamente al registrar el usuario)
-- =====================================================================
USE db_inmobiliaria;

-- ROLES
INSERT INTO rol (nombre_rol) VALUES
('ADMINISTRADOR'), ('INMOBILIARIA'), ('CLIENTE');

-- CIUDADES
INSERT INTO ciudad (nombre, departamento) VALUES
('Bucaramanga','Santander'),
('Floridablanca','Santander'),
('Girón','Santander'),
('Piedecuesta','Santander'),
('Bogotá','Cundinamarca'),
('Medellín','Antioquia'),
('Cali','Valle del Cauca'),
('Barranquilla','Atlántico'),
('Cartagena','Bolívar'),
('Cúcuta','Norte de Santander');

-- TIPOS DE PROPIEDAD
INSERT INTO tipo_propiedad (nombre) VALUES
('Casa'),('Apartamento'),('Local'),('Oficina'),('Terreno');

-- CARACTERÍSTICAS
INSERT INTO caracteristica (nombre) VALUES
('Piscina'),('Parqueadero'),('Ascensor'),('Gimnasio'),('Zona verde'),
('Seguridad 24h'),('Balcón'),('Amoblado'),('Aire acondicionado'),('Depósito');

-- USUARIOS (10 usuarios: 1 admin, 3 inmobiliarias, 6 clientes)
INSERT INTO usuario (correo, contrasena_hash, estado) VALUES
('admin@inmobiliaria.com',   '$2b$12$Lt1m2xsrsz3FkFeJfJpGS.nEnrIHrBeGMZDytf0g7j2e9306FIl.e','ACTIVO'),
('agenteA@inmobiliaria.com', '$2b$12$Lt1m2xsrsz3FkFeJfJpGS.nEnrIHrBeGMZDytf0g7j2e9306FIl.e','ACTIVO'),
('agenteB@inmobiliaria.com', '$2b$12$Lt1m2xsrsz3FkFeJfJpGS.nEnrIHrBeGMZDytf0g7j2e9306FIl.e','ACTIVO'),
('agenteC@inmobiliaria.com', '$2b$12$Lt1m2xsrsz3FkFeJfJpGS.nEnrIHrBeGMZDytf0g7j2e9306FIl.e','ACTIVO'),
('cliente1@correo.com',      '$2b$12$Lt1m2xsrsz3FkFeJfJpGS.nEnrIHrBeGMZDytf0g7j2e9306FIl.e','ACTIVO'),
('cliente2@correo.com',      '$2b$12$Lt1m2xsrsz3FkFeJfJpGS.nEnrIHrBeGMZDytf0g7j2e9306FIl.e','ACTIVO'),
('cliente3@correo.com',      '$2b$12$Lt1m2xsrsz3FkFeJfJpGS.nEnrIHrBeGMZDytf0g7j2e9306FIl.e','ACTIVO'),
('cliente4@correo.com',      '$2b$12$Lt1m2xsrsz3FkFeJfJpGS.nEnrIHrBeGMZDytf0g7j2e9306FIl.e','ACTIVO'),
('cliente5@correo.com',      '$2b$12$Lt1m2xsrsz3FkFeJfJpGS.nEnrIHrBeGMZDytf0g7j2e9306FIl.e','ACTIVO'),
('cliente6@correo.com',      '$2b$12$Lt1m2xsrsz3FkFeJfJpGS.nEnrIHrBeGMZDytf0g7j2e9306FIl.e','INACTIVO');

-- PERFILES (1:1 con usuario)
INSERT INTO perfil (id_usuario, nombres, apellidos, documento, telefono, direccion) VALUES
(1,'Julian','Jaimes','1001','3001111111','Cra 1 # 1-01'),
(2,'Laura','Gómez','1002','3002222222','Cra 2 # 2-02'),
(3,'Carlos','Pérez','1003','3003333333','Cra 3 # 3-03'),
(4,'Marta','Rojas','1004','3004444444','Cra 4 # 4-04'),
(5,'Andrés','López','1005','3005555555','Cra 5 # 5-05'),
(6,'Sofía','Martínez','1006','3006666666','Cra 6 # 6-06'),
(7,'Diego','Hernández','1007','3007777777','Cra 7 # 7-07'),
(8,'Valentina','Torres','1008','3008888888','Cra 8 # 8-08'),
(9,'Camilo','Ramírez','1009','3009999999','Cra 9 # 9-09'),
(10,'Paula','Castro','1010','3000000000','Cra 10 # 10-10');

-- USUARIO_ROL (N:M)
INSERT INTO usuario_rol (id_usuario, id_rol) VALUES
(1,1),                 -- admin
(2,2),(3,2),(4,2),     -- inmobiliarias
(5,3),(6,3),(7,3),(8,3),(9,3),(10,3); -- clientes

-- INMOBILIARIAS (1:1 con usuario, rol INMOBILIARIA)
INSERT INTO inmobiliaria (id_usuario, nombre_comercial, nit, telefono_contacto) VALUES
(2,'Vivienda Norte S.A.S.','900111222-1','6076001111'),
(3,'Hábitat Andino Ltda.','900222333-2','6076002222'),
(4,'Raíces Inmobiliaria','900333444-3','6076003333');

-- PROPIEDADES (mínimo 10)
INSERT INTO propiedad (id_usuario_inmobiliaria, id_ciudad, id_tipo_propiedad, matricula_inmobiliaria, titulo, descripcion, precio, direccion, area_m2, estado) VALUES
(2,1,2,'MI-0001','Apartamento moderno centro','Excelente ubicación, remodelado',320000000,'Cl 30 #10-20',85,'DISPONIBLE'),
(2,2,1,'MI-0002','Casa campestre Floridablanca','Amplio jardín, dos pisos',650000000,'Vereda La Cumbre',220,'DISPONIBLE'),
(2,1,3,'MI-0003','Local comercial Cabecera','Ideal para negocio de comida',180000000,'Cra 33 #45-10',60,'DISPONIBLE'),
(3,1,2,'MI-0004','Apartaestudio Cabecera','Perfecto para estudiantes',210000000,'Cl 48 #27-15',45,'DISPONIBLE'),
(3,3,1,'MI-0005','Casa esquinera Girón','Cerca al centro histórico',480000000,'Cl 28 #25-30',180,'RESERVADA'),
(3,5,4,'MI-0006','Oficina Chapinero','Edificio empresarial con recepción',390000000,'Cra 13 #55-40',70,'DISPONIBLE'),
(4,4,5,'MI-0007','Lote Piedecuesta','Apto para construcción',150000000,'Km 2 vía Pescadero',500,'DISPONIBLE'),
(4,1,2,'MI-0008','Penthouse Cabecera','Vista panorámica, terraza privada',890000000,'Cra 36 #52-10',150,'VENDIDA'),
(4,6,2,'MI-0009','Apartamento Poblado Medellín','Zona exclusiva, amoblado',520000000,'Cl 10 #35-20',95,'DISPONIBLE'),
(2,7,1,'MI-0010','Casa campestre Cali','Piscina privada, amplio garaje',720000000,'Km 5 vía Jamundí',300,'DISPONIBLE'),
(3,2,3,'MI-0011','Local Floridablanca','Alto flujo peatonal',95000000,'Cl 5 #8-12',40,'ARRENDADA'),
(2,1,2,'MI-0012','Apartamento Cañaveral','Conjunto cerrado con zonas comunes',410000000,'Cra 30 #100-50',90,'DISPONIBLE');

-- IMÁGENES (1:N)
INSERT INTO imagen_propiedad (id_propiedad, url_imagen, es_portada) VALUES
(1,'/img/propiedades/1_a.jpg',TRUE),(1,'/img/propiedades/1_b.jpg',FALSE),
(2,'/img/propiedades/2_a.jpg',TRUE),(2,'/img/propiedades/2_b.jpg',FALSE),
(3,'/img/propiedades/3_a.jpg',TRUE),
(4,'/img/propiedades/4_a.jpg',TRUE),
(5,'/img/propiedades/5_a.jpg',TRUE),
(6,'/img/propiedades/6_a.jpg',TRUE),
(7,'/img/propiedades/7_a.jpg',TRUE),
(8,'/img/propiedades/8_a.jpg',TRUE),
(9,'/img/propiedades/9_a.jpg',TRUE),
(10,'/img/propiedades/10_a.jpg',TRUE);

-- PROPIEDAD_CARACTERISTICA (N:M)
INSERT INTO propiedad_caracteristica (id_propiedad, id_caracteristica, cantidad) VALUES
(1,2,1),(1,3,1),(1,9,1),
(2,1,1),(2,2,2),(2,5,1),
(3,6,1),
(4,2,1),(4,9,1),
(5,2,1),(5,5,1),
(6,3,1),(6,6,1),
(8,1,1),(8,3,1),(8,7,1),
(9,4,1),(9,6,1),(9,8,1),
(10,1,1),(10,2,3),(10,5,1);

-- CITAS
INSERT INTO cita (id_propiedad, id_cliente, fecha_hora, estado) VALUES
(1,5,'2026-09-05 10:00:00','PENDIENTE'),
(2,6,'2026-09-05 14:00:00','CONFIRMADA'),
(3,7,'2026-09-06 09:00:00','PENDIENTE'),
(4,8,'2026-09-06 11:00:00','RECHAZADA'),
(6,9,'2026-09-07 15:00:00','CONFIRMADA'),
(9,5,'2026-09-08 16:00:00','PENDIENTE'),
(10,6,'2026-09-08 10:30:00','REALIZADA'),
(1,7,'2026-09-09 09:30:00','PENDIENTE'),
(2,8,'2026-09-10 13:00:00','CANCELADA'),
(9,9,'2026-09-11 15:30:00','PENDIENTE');

-- SOLICITUDES
INSERT INTO solicitud (id_propiedad, id_cliente, tipo, estado) VALUES
(5,5,'COMPRA','EN_REVISION'),
(8,6,'COMPRA','APROBADA'),
(11,7,'ARRIENDO','APROBADA'),
(2,8,'COMPRA','EN_REVISION'),
(9,9,'COMPRA','RECHAZADA'),
(4,5,'ARRIENDO','APROBADA'),
(3,6,'ARRIENDO','EN_REVISION'),
(10,7,'COMPRA','EN_REVISION'),
(1,8,'ARRIENDO','APROBADA'),
(6,9,'ARRIENDO','EN_REVISION');

-- DOCUMENTOS DE SOLICITUD
INSERT INTO documento_solicitud (id_solicitud, nombre_archivo, url_archivo) VALUES
(1,'cedula_cliente1.pdf','/docs/sol1_cedula.pdf'),
(2,'certificado_laboral.pdf','/docs/sol2_laboral.pdf'),
(3,'contrato_arriendo.pdf','/docs/sol3_contrato.pdf'),
(4,'cedula_cliente4.pdf','/docs/sol4_cedula.pdf'),
(5,'certificado_ingresos.pdf','/docs/sol5_ingresos.pdf'),
(6,'referencia_bancaria.pdf','/docs/sol6_banco.pdf'),
(7,'cedula_cliente6.pdf','/docs/sol7_cedula.pdf'),
(8,'extracto_bancario.pdf','/docs/sol8_extracto.pdf'),
(9,'cedula_cliente8.pdf','/docs/sol9_cedula.pdf'),
(10,'carta_laboral.pdf','/docs/sol10_laboral.pdf');

-- FAVORITOS
INSERT INTO favorito (id_cliente, id_propiedad) VALUES
(5,1),(5,9),(6,2),(6,10),(7,3),(7,4),(8,2),(8,8),(9,6),(9,9);

-- AUDITORÍA
INSERT INTO auditoria (id_usuario, accion, ip_origen) VALUES
(1,'Inicio de sesión exitoso','127.0.0.1'),
(2,'Publicó propiedad MI-0001','192.168.1.10'),
(5,'Registro de usuario nuevo','192.168.1.20'),
(6,'Inicio de sesión exitoso','192.168.1.21'),
(NULL,'Intento de acceso a /admin sin autenticación','192.168.1.99'),
(7,'Solicitud de cita creada','192.168.1.22'),
(3,'Editó propiedad MI-0004','192.168.1.11'),
(8,'Radicó documento de solicitud','192.168.1.23'),
(1,'Asignó rol INMOBILIARIA a usuario 4','127.0.0.1'),
(9,'Cierre de sesión','192.168.1.24');
