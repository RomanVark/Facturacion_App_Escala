-- Ejecutar conectado a tienda_javafx. No elimina datos existentes.
BEGIN;
CREATE TABLE IF NOT EXISTS categoria (
 id SERIAL PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL CHECK (btrim(nombre) <> ''),
 activa BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE UNIQUE INDEX IF NOT EXISTS uq_categoria_nombre_normalizado ON categoria (LOWER(BTRIM(nombre)));
CREATE TABLE IF NOT EXISTS producto (
 id SERIAL PRIMARY KEY,
 codigo VARCHAR(50) NOT NULL UNIQUE CHECK (btrim(codigo) <> ''),
 nombre VARCHAR(150) NOT NULL CHECK (btrim(nombre) <> ''),
 categoria_id INTEGER NOT NULL,
 precio_venta NUMERIC(12,2) NOT NULL CHECK (precio_venta > 0),
 existencia INTEGER NOT NULL DEFAULT 0 CHECK (existencia >= 0),
 ruta_imagen VARCHAR(500),
 activo BOOLEAN NOT NULL DEFAULT TRUE,
 CONSTRAINT fk_producto_categoria FOREIGN KEY(categoria_id) REFERENCES categoria(id) ON DELETE RESTRICT
);
CREATE INDEX IF NOT EXISTS idx_producto_categoria ON producto(categoria_id);
CREATE TABLE IF NOT EXISTS cliente (
 id SERIAL PRIMARY KEY,
 nombre VARCHAR(150) NOT NULL CHECK (btrim(nombre) <> ''),
 documento VARCHAR(50) NOT NULL UNIQUE CHECK (btrim(documento) <> ''),
 telefono VARCHAR(30), correo VARCHAR(150), direccion VARCHAR(300),
 activo BOOLEAN NOT NULL DEFAULT TRUE
);
COMMIT;
