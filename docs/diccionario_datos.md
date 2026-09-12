# Diccionario de Datos – Sistema Inmobiliaria (16 tablas, 3FN)

Motor: MySQL 8.x · Script fuente: `db/01_ddl.sql` · Datos de prueba: `db/02_dml.sql`

## Catálogos

### Tabla: rol
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_rol | INT | PK, AUTO_INCREMENT | Identificador del rol |
| nombre_rol | VARCHAR(30) | UNIQUE, NOT NULL | ADMINISTRADOR / INMOBILIARIA / CLIENTE |

### Tabla: ciudad
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_ciudad | INT | PK, AUTO_INCREMENT | Identificador de la ciudad |
| nombre | VARCHAR(60) | NOT NULL | Nombre de la ciudad |
| departamento | VARCHAR(60) | NOT NULL | Departamento al que pertenece |

### Tabla: tipo_propiedad
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_tipo_propiedad | INT | PK, AUTO_INCREMENT | Identificador del tipo |
| nombre | VARCHAR(40) | UNIQUE, NOT NULL | Casa, Apartamento, Local, Oficina, Terreno |

### Tabla: caracteristica
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_caracteristica | INT | PK, AUTO_INCREMENT | Identificador de la característica |
| nombre | VARCHAR(60) | UNIQUE, NOT NULL | Piscina, Parqueadero, Ascensor, Gimnasio, etc. |

## Usuarios, perfil (1:1) y roles (N:M)

### Tabla: usuario
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_usuario | INT | PK, AUTO_INCREMENT | Identificador único del usuario |
| correo | VARCHAR(120) | UNIQUE, NOT NULL | Credencial de ingreso; no admite duplicados |
| contrasena_hash | VARCHAR(255) | NOT NULL | Hash BCrypt de la contraseña (nunca texto plano) |
| estado | ENUM | NOT NULL, DEFAULT 'ACTIVO' | ACTIVO / INACTIVO / BLOQUEADO |
| intentos_fallidos | INT | DEFAULT 0 | Contador para el bloqueo temporal |
| fecha_registro | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de creación de la cuenta |

### Tabla: perfil (relación 1:1 con usuario)
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_usuario | INT | PK, FK -> usuario.id_usuario (UNIQUE por ser PK) | Comparte llave con usuario para forzar 1:1 |
| nombres | VARCHAR(80) | NOT NULL | Nombres del usuario |
| apellidos | VARCHAR(80) | NOT NULL | Apellidos del usuario |
| documento | VARCHAR(30) | UNIQUE, NOT NULL | Documento de identidad |
| telefono | VARCHAR(20) | -- | Teléfono de contacto |
| direccion | VARCHAR(150) | -- | Dirección de residencia |
| foto | VARCHAR(255) | -- | URL de la foto de perfil |

### Tabla: usuario_rol (relación N:M entre usuario y rol)
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_usuario | INT | PK compuesta, FK -> usuario, ON DELETE CASCADE | Usuario asignado |
| id_rol | INT | PK compuesta, FK -> rol, ON DELETE RESTRICT | Rol asignado |
| fecha_asignacion | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha en que se asignó el rol |

### Tabla: inmobiliaria (relación 1:1 con usuario, rol INMOBILIARIA)
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_usuario | INT | PK, FK -> usuario, ON DELETE CASCADE | Usuario que representa a la inmobiliaria |
| nombre_comercial | VARCHAR(120) | NOT NULL | Nombre comercial |
| nit | VARCHAR(30) | UNIQUE, NOT NULL | Identificación tributaria |
| telefono_contacto | VARCHAR(20) | -- | Teléfono de contacto comercial |

## Propiedades e imágenes (1:N) y características (N:M)

### Tabla: propiedad
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_propiedad | INT | PK, AUTO_INCREMENT | Identificador del inmueble |
| id_usuario_inmobiliaria | INT | FK -> inmobiliaria, ON DELETE CASCADE | Inmobiliaria propietaria de la publicación |
| id_ciudad | INT | FK -> ciudad, ON DELETE RESTRICT | Ciudad donde se ubica |
| id_tipo_propiedad | INT | FK -> tipo_propiedad, ON DELETE RESTRICT | Tipo de inmueble |
| matricula_inmobiliaria | VARCHAR(40) | UNIQUE, NOT NULL | Evita publicaciones duplicadas del mismo inmueble |
| titulo | VARCHAR(150) | NOT NULL | Título de la publicación |
| descripcion | TEXT | -- | Descripción libre |
| precio | DECIMAL(14,2) | NOT NULL | Precio de venta/arriendo |
| direccion | VARCHAR(150) | NOT NULL | Dirección del inmueble |
| area_m2 | DECIMAL(8,2) | -- | Área en metros cuadrados |
| estado | ENUM | DEFAULT 'DISPONIBLE' | DISPONIBLE / RESERVADA / VENDIDA / ARRENDADA / INACTIVA (baja lógica) |
| fecha_publicacion | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de publicación |

### Tabla: imagen_propiedad (relación 1:N con propiedad)
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_imagen | INT | PK, AUTO_INCREMENT | Identificador de la imagen |
| id_propiedad | INT | FK -> propiedad, ON DELETE CASCADE | Propiedad a la que pertenece |
| url_imagen | VARCHAR(255) | NOT NULL | URL de la imagen |
| es_portada | BOOLEAN | DEFAULT FALSE | Indica si es la imagen de portada |

### Tabla: propiedad_caracteristica (relación N:M entre propiedad y característica)
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_propiedad | INT | PK compuesta, FK -> propiedad, ON DELETE CASCADE | Propiedad |
| id_caracteristica | INT | PK compuesta, FK -> caracteristica, ON DELETE CASCADE | Característica asociada |
| cantidad | INT | DEFAULT 1 | Atributo propio de la relación (ej. número de parqueaderos) |

## Citas, solicitudes, documentos, favoritos y auditoría

### Tabla: cita
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_cita | INT | PK, AUTO_INCREMENT | Identificador de la cita |
| id_propiedad | INT | FK -> propiedad, ON DELETE CASCADE | Propiedad a visitar |
| id_cliente | INT | FK -> usuario, ON DELETE CASCADE | Cliente que agenda |
| fecha_hora | DATETIME | NOT NULL | Fecha y hora de la visita |
| estado | ENUM | DEFAULT 'PENDIENTE' | PENDIENTE / CONFIRMADA / RECHAZADA / REALIZADA / CANCELADA |
| observacion | VARCHAR(255) | -- | Observación del cliente |
| -- | -- | UNIQUE (id_propiedad, fecha_hora) | Evita dos citas en el mismo horario para la misma propiedad |

### Tabla: solicitud
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_solicitud | INT | PK, AUTO_INCREMENT | Identificador de la solicitud |
| id_propiedad | INT | FK -> propiedad, ON DELETE CASCADE | Propiedad solicitada |
| id_cliente | INT | FK -> usuario, ON DELETE CASCADE | Cliente que radica |
| tipo | ENUM | NOT NULL | COMPRA / ARRIENDO |
| estado | ENUM | DEFAULT 'EN_REVISION' | EN_REVISION / APROBADA / RECHAZADA |
| fecha_solicitud | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de radicación |

### Tabla: documento_solicitud (relación 1:N con solicitud)
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_documento | INT | PK, AUTO_INCREMENT | Identificador del documento |
| id_solicitud | INT | FK -> solicitud, ON DELETE CASCADE | Solicitud a la que pertenece |
| nombre_archivo | VARCHAR(150) | NOT NULL | Nombre original del archivo cargado |
| url_archivo | VARCHAR(255) | NOT NULL | Ruta de almacenamiento (/uploads/{uuid}.ext) |
| fecha_carga | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha de carga |

### Tabla: favorito (relación N:M entre usuario/cliente y propiedad)
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_cliente | INT | PK compuesta, FK -> usuario, ON DELETE CASCADE | Cliente |
| id_propiedad | INT | PK compuesta, FK -> propiedad, ON DELETE CASCADE | Propiedad marcada |
| fecha_marcado | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha en que se marcó como favorita |

### Tabla: auditoria
| Campo | Tipo | Restricción | Descripción |
|---|---|---|---|
| id_auditoria | INT | PK, AUTO_INCREMENT | Identificador del evento |
| id_usuario | INT | FK -> usuario, ON DELETE SET NULL, NULL permitido | Usuario relacionado (puede ser NULL: ej. intento fallido con correo inexistente) |
| accion | VARCHAR(150) | NOT NULL | Descripción de la acción registrada |
| fecha_evento | DATETIME | DEFAULT CURRENT_TIMESTAMP | Fecha y hora del evento |
| ip_origen | VARCHAR(45) | -- | Dirección IP de origen |

## Relaciones exigidas y dónde se materializan

| Tipo de relación | Tablas involucradas | Cómo se garantiza |
|---|---|---|
| 1:1 | usuario <-> perfil | perfil.id_usuario es PK y FK a la vez (comparte llave) |
| 1:1 | usuario <-> inmobiliaria | Mismo patrón: inmobiliaria.id_usuario PK+FK |
| 1:N | inmobiliaria -> propiedad | FK id_usuario_inmobiliaria en el lado "muchos" (propiedad) |
| 1:N | propiedad -> imagen_propiedad | FK id_propiedad en imagen_propiedad, ON DELETE CASCADE |
| 1:N | usuario(cliente) -> cita | FK id_cliente en cita |
| 1:N | usuario(cliente) -> solicitud | FK id_cliente en solicitud |
| 1:N | solicitud -> documento_solicitud | FK id_solicitud en documento_solicitud, ON DELETE CASCADE |
| N:M | usuario <-> rol | Tabla intermedia usuario_rol, PK compuesta, atributo propio fecha_asignacion |
| N:M | propiedad <-> caracteristica | Tabla intermedia propiedad_caracteristica, PK compuesta, atributo propio cantidad |
| N:M | usuario(cliente) <-> propiedad | Tabla intermedia favorito, PK compuesta, atributo propio fecha_marcado |

## Campos con restricción UNIQUE (mínimo 3 exigidos, aquí hay 5)

1. usuario.correo — credencial de ingreso, evita cuentas duplicadas.
2. perfil.documento — evita que dos usuarios compartan el mismo documento de identidad.
3. propiedad.matricula_inmobiliaria — evita publicaciones duplicadas del mismo inmueble.
4. inmobiliaria.nit — evita registrar dos veces la misma inmobiliaria.
5. cita (id_propiedad, fecha_hora) — evita agendar dos citas para la misma propiedad en el mismo horario.

Todas estas restricciones son capturadas por la aplicación (SQLIntegrityConstraintViolationException)
y traducidas a un mensaje claro para el usuario final (ver UsuarioDAO.CorreoDuplicadoException,
PerfilDAO.DocumentoDuplicadoException, PropiedadDAO.MatriculaDuplicadaException y
CitaDAO.HorarioOcupadoException) — nunca se muestra una excepción cruda de Java.

## Normalización (3FN)

- 1FN: todos los atributos son atómicos (no hay listas ni grupos repetidos dentro de una fila;
  por eso imágenes, características, roles y documentos viven en tablas separadas).
- 2FN: en las tablas con llave compuesta (usuario_rol, propiedad_caracteristica, favorito)
  no hay atributos que dependan solo de una parte de la llave; los atributos propios
  (fecha_asignacion, cantidad, fecha_marcado) dependen de la combinación completa.
- 3FN: no hay dependencias transitivas. Por ejemplo, ciudad.departamento no se repite dentro de
  propiedad (se normaliza en la tabla ciudad); el nombre comercial de la inmobiliaria no se repite
  en cada fila de propiedad (se normaliza en inmobiliaria).
