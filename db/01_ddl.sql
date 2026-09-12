-- =====================================================================
-- PROYECTO: Sistema Web Inmobiliaria
-- SCRIPT: DDL - Creación de base de datos y tablas (3FN)
-- =====================================================================

DROP DATABASE IF EXISTS db_inmobiliaria;
CREATE DATABASE db_inmobiliaria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_inmobiliaria;

-- ---------------------------------------------------------------------
-- CATÁLOGOS
-- ---------------------------------------------------------------------
CREATE TABLE rol (
    id_rol      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol  VARCHAR(30) NOT NULL UNIQUE   -- ADMINISTRADOR, INMOBILIARIA, CLIENTE
);

CREATE TABLE ciudad (
    id_ciudad   INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(60) NOT NULL,
    departamento VARCHAR(60) NOT NULL
);

CREATE TABLE tipo_propiedad (
    id_tipo_propiedad INT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(40) NOT NULL UNIQUE   -- Casa, Apartamento, Local, Oficina, Terreno
);

CREATE TABLE caracteristica (
    id_caracteristica INT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(60) NOT NULL UNIQUE   -- Piscina, Parqueadero, Ascensor, Gimnasio...
);

-- ---------------------------------------------------------------------
-- USUARIOS, PERFIL (1:1) Y ROLES (N:M)
-- ---------------------------------------------------------------------
CREATE TABLE usuario (
    id_usuario      INT AUTO_INCREMENT PRIMARY KEY,
    correo          VARCHAR(120) NOT NULL UNIQUE,        -- UNIQUE obligatoria (credencial de ingreso)
    contrasena_hash VARCHAR(255) NOT NULL,                -- se guarda cifrada (BCrypt)
    estado          ENUM('ACTIVO','INACTIVO','BLOQUEADO') NOT NULL DEFAULT 'ACTIVO',
    intentos_fallidos INT NOT NULL DEFAULT 0,
    fecha_registro  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Relación 1:1 -> perfil.id_usuario es a la vez PK y FK UNIQUE
CREATE TABLE perfil (
    id_usuario  INT PRIMARY KEY,
    nombres     VARCHAR(80) NOT NULL,
    apellidos   VARCHAR(80) NOT NULL,
    documento   VARCHAR(30) NOT NULL UNIQUE,
    telefono    VARCHAR(20),
    direccion   VARCHAR(150),
    foto        VARCHAR(255),
    CONSTRAINT fk_perfil_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Relación N:M -> un usuario puede tener varios roles y viceversa
CREATE TABLE usuario_rol (
    id_usuario  INT NOT NULL,
    id_rol      INT NOT NULL,
    fecha_asignacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario, id_rol),
    CONSTRAINT fk_usuariorol_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_usuariorol_rol
        FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ---------------------------------------------------------------------
-- INMOBILIARIA (agente) -> 1:1 con usuario
-- ---------------------------------------------------------------------
CREATE TABLE inmobiliaria (
    id_usuario       INT PRIMARY KEY,
    nombre_comercial VARCHAR(120) NOT NULL,
    nit              VARCHAR(30) NOT NULL UNIQUE,
    telefono_contacto VARCHAR(20),
    CONSTRAINT fk_inmobiliaria_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ---------------------------------------------------------------------
-- PROPIEDAD (1:N con inmobiliaria y ciudad) + imágenes (1:N) + características (N:M)
-- ---------------------------------------------------------------------
CREATE TABLE propiedad (
    id_propiedad          INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario_inmobiliaria INT NOT NULL,
    id_ciudad             INT NOT NULL,
    id_tipo_propiedad     INT NOT NULL,
    matricula_inmobiliaria VARCHAR(40) NOT NULL UNIQUE,   -- UNIQUE: evita publicaciones duplicadas
    titulo                VARCHAR(150) NOT NULL,
    descripcion           TEXT,
    precio                DECIMAL(14,2) NOT NULL,
    direccion             VARCHAR(150) NOT NULL,
    area_m2               DECIMAL(8,2),
    estado                ENUM('DISPONIBLE','RESERVADA','VENDIDA','ARRENDADA','INACTIVA') NOT NULL DEFAULT 'DISPONIBLE',
    fecha_publicacion     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_propiedad_inmobiliaria
        FOREIGN KEY (id_usuario_inmobiliaria) REFERENCES inmobiliaria(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_propiedad_ciudad
        FOREIGN KEY (id_ciudad) REFERENCES ciudad(id_ciudad)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_propiedad_tipo
        FOREIGN KEY (id_tipo_propiedad) REFERENCES tipo_propiedad(id_tipo_propiedad)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE imagen_propiedad (
    id_imagen    INT AUTO_INCREMENT PRIMARY KEY,
    id_propiedad INT NOT NULL,
    url_imagen   VARCHAR(255) NOT NULL,
    es_portada   BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_imagen_propiedad
        FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Relación N:M -> propiedad <-> característica
CREATE TABLE propiedad_caracteristica (
    id_propiedad      INT NOT NULL,
    id_caracteristica INT NOT NULL,
    cantidad          INT DEFAULT 1,
    PRIMARY KEY (id_propiedad, id_caracteristica),
    CONSTRAINT fk_propcarac_propiedad
        FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_propcarac_caracteristica
        FOREIGN KEY (id_caracteristica) REFERENCES caracteristica(id_caracteristica)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ---------------------------------------------------------------------
-- CITAS, SOLICITUDES, DOCUMENTOS, FAVORITOS, AUDITORÍA
-- ---------------------------------------------------------------------
CREATE TABLE cita (
    id_cita      INT AUTO_INCREMENT PRIMARY KEY,
    id_propiedad INT NOT NULL,
    id_cliente   INT NOT NULL,
    fecha_hora   DATETIME NOT NULL,
    estado       ENUM('PENDIENTE','CONFIRMADA','RECHAZADA','REALIZADA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
    observacion  VARCHAR(255),
    CONSTRAINT uq_cita_propiedad_horario UNIQUE (id_propiedad, fecha_hora), -- evita cruce de agendas
    CONSTRAINT fk_cita_propiedad
        FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_cita_cliente
        FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE solicitud (
    id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
    id_propiedad INT NOT NULL,
    id_cliente   INT NOT NULL,
    tipo         ENUM('COMPRA','ARRIENDO') NOT NULL,
    estado       ENUM('EN_REVISION','APROBADA','RECHAZADA') NOT NULL DEFAULT 'EN_REVISION',
    fecha_solicitud DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_solicitud_propiedad
        FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_solicitud_cliente
        FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE documento_solicitud (
    id_documento  INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud  INT NOT NULL,
    nombre_archivo VARCHAR(150) NOT NULL,
    url_archivo   VARCHAR(255) NOT NULL,
    fecha_carga   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_documento_solicitud
        FOREIGN KEY (id_solicitud) REFERENCES solicitud(id_solicitud)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE favorito (
    id_cliente   INT NOT NULL,
    id_propiedad INT NOT NULL,
    fecha_marcado DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_cliente, id_propiedad),
    CONSTRAINT fk_favorito_cliente
        FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_favorito_propiedad
        FOREIGN KEY (id_propiedad) REFERENCES propiedad(id_propiedad)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE auditoria (
    id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario   INT NULL,
    accion       VARCHAR(150) NOT NULL,
    fecha_evento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_origen    VARCHAR(45),
    CONSTRAINT fk_auditoria_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE SET NULL ON UPDATE CASCADE
);
