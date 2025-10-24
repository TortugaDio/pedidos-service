-- Tabla de Clientes
CREATE TABLE IF NOT EXISTS clientes (
    cliente_id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla de Zonas
CREATE TABLE IF NOT EXISTS zonas (
    codigo_zona VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    soporte_refrigeracion BOOLEAN NOT NULL DEFAULT FALSE,
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla de Pedidos
CREATE TABLE IF NOT EXISTS pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_pedido VARCHAR(50) NOT NULL UNIQUE,
    cliente_id VARCHAR(50) NOT NULL,
    fecha_entrega DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    zona_entrega VARCHAR(50) NOT NULL,
    requiere_refrigeracion BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(cliente_id),
    CONSTRAINT fk_pedido_zona FOREIGN KEY (zona_entrega) REFERENCES zonas(codigo_zona)
);

-- Índices para optimizar consultas
CREATE INDEX IF NOT EXISTS idx_numero_pedido ON pedidos(numero_pedido);
CREATE INDEX IF NOT EXISTS idx_cliente_id ON pedidos(cliente_id);
CREATE INDEX IF NOT EXISTS idx_zona_entrega ON pedidos(zona_entrega);
CREATE INDEX IF NOT EXISTS idx_fecha_entrega ON pedidos(fecha_entrega);

-- Datos de prueba
INSERT INTO clientes (cliente_id, nombre, email, activo) VALUES
('CLI-123', 'Juan Pérez', 'juan.perez@example.com', TRUE),
('CLI-456', 'María García', 'maria.garcia@example.com', TRUE),
('CLI-789', 'Carlos López', 'carlos.lopez@example.com', TRUE);

INSERT INTO zonas (codigo_zona, nombre, soporte_refrigeracion, activa) VALUES
('ZONA1', 'Zona Norte', TRUE, TRUE),
('ZONA2', 'Zona Sur', TRUE, TRUE),
('ZONA3', 'Zona Centro', FALSE, TRUE),
('ZONA4', 'Zona Este', TRUE, TRUE),
('ZONA5', 'Zona Oeste', FALSE, TRUE);