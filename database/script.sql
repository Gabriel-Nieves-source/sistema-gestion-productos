-- =========================================================
-- Sistema de Gestion de Productos
-- Script de creacion de la base de datos (MySQL 8.x)
-- =========================================================

-- 1. Crear la base de datos (si no existe)
CREATE DATABASE IF NOT EXISTS sistema_productos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 2. Seleccionar la base de datos
USE sistema_productos;

-- 3. Crear la tabla de productos
CREATE TABLE IF NOT EXISTS productos (
    id          INT            NOT NULL AUTO_INCREMENT,
    codigo      VARCHAR(20)    NOT NULL,
    nombre      VARCHAR(100)   NOT NULL,
    descripcion VARCHAR(255)   NULL,
    precio      DECIMAL(10,2)  NOT NULL,
    cantidad    INT            NOT NULL,
    CONSTRAINT pk_productos PRIMARY KEY (id),
    CONSTRAINT uq_productos_codigo UNIQUE (codigo),
    CONSTRAINT ck_productos_precio CHECK (precio >= 0),
    CONSTRAINT ck_productos_cantidad CHECK (cantidad >= 0)
);

-- 4. Consulta para verificar el contenido de la tabla
SELECT * FROM productos;
