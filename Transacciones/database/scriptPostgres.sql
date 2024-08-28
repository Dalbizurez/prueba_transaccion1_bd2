-- Crear la base de datos transacciones
CREATE DATABASE transacciones;

-- Conectar a la base de datos transacciones
\c transacciones;

-- Crear la tabla cliente
CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    direccion VARCHAR(255)
);

-- Crear la tabla telefono
CREATE TABLE telefono (
    id SERIAL PRIMARY KEY,
    number VARCHAR(20) NOT NULL,
    cliente_id INT REFERENCES cliente(id) ON DELETE CASCADE
);


-- psql -U postgres -h localhost -d postgres
