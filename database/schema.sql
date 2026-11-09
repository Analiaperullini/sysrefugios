CREATE DATABASE IF NOT EXISTS sysrefugios_db CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE sysrefugios_db;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS asignacion_castracion;
DROP TABLE IF EXISTS campania_castracion;
DROP TABLE IF EXISTS seguimiento;
DROP TABLE IF EXISTS adopcion;
DROP TABLE IF EXISTS adoptante;
DROP TABLE IF EXISTS castracion;
DROP TABLE IF EXISTS vacunacion;
DROP TABLE IF EXISTS derivacion;
DROP TABLE IF EXISTS transito;
DROP TABLE IF EXISTS espacio_transito;
DROP TABLE IF EXISTS rescate;
DROP TABLE IF EXISTS animal;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS organizacion;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE organizacion (
    id_organizacion BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT NULL,
    telefono VARCHAR(255) NULL,
    direccion VARCHAR(255) NULL,
    email VARCHAR(150) NULL,
    etapa_vida_atendida VARCHAR(20) NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_organizacion)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE usuario (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    id_organizacion BIGINT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',
    intentos_fallidos INT NOT NULL DEFAULT 0,
    cambio_obligatorio BIT(1) NOT NULL DEFAULT b'1',
    fecha_cambio_contrasena DATETIME(6) NULL,
    PRIMARY KEY (id_usuario),
    CONSTRAINT uk_usuario_email UNIQUE (email),
    CONSTRAINT fk_usuario_organizacion FOREIGN KEY (id_organizacion) REFERENCES organizacion (id_organizacion)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE animal (
    id_animal BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    especie VARCHAR(255) NOT NULL,
    sexo VARCHAR(255) NULL,
    fecha_nacimiento DATE NULL,
    color VARCHAR(50) NULL,
    etapa_vida VARCHAR(20) NOT NULL,
    tamanio VARCHAR(20) NULL,
    estado VARCHAR(30) NOT NULL,
    descripcion TEXT NULL,
    id_organizacion BIGINT NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_animal),
    CONSTRAINT fk_animal_organizacion FOREIGN KEY (id_organizacion) REFERENCES organizacion (id_organizacion)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE rescate (
    id_rescate BIGINT NOT NULL AUTO_INCREMENT,
    id_animal BIGINT NOT NULL,
    fecha_rescate DATE NOT NULL,
    lugar VARCHAR(255) NULL,
    descripcion VARCHAR(1000) NULL,
    observaciones TEXT NULL,
    id_organizacion BIGINT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    id_usuario BIGINT NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_rescate),
    CONSTRAINT fk_rescate_animal FOREIGN KEY (id_animal) REFERENCES animal (id_animal),
    CONSTRAINT fk_rescate_organizacion FOREIGN KEY (id_organizacion) REFERENCES organizacion (id_organizacion),
    CONSTRAINT fk_rescate_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE espacio_transito (
    id_espacio BIGINT NOT NULL AUTO_INCREMENT,
    id_organizacion BIGINT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(1000) NULL,
    capacidad INT NOT NULL,
    estado VARCHAR(30) NOT NULL,
    observaciones VARCHAR(1000) NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_espacio),
    CONSTRAINT ck_espacio_transito_capacidad CHECK (capacidad >= 1),
    CONSTRAINT fk_espacio_transito_organizacion FOREIGN KEY (id_organizacion) REFERENCES organizacion (id_organizacion)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE transito (
    id_transito BIGINT NOT NULL AUTO_INCREMENT,
    id_animal BIGINT NOT NULL,
    id_espacio BIGINT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'EN_CURSO',
    observaciones VARCHAR(1000) NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_transito),
    CONSTRAINT fk_transito_animal FOREIGN KEY (id_animal) REFERENCES animal (id_animal),
    CONSTRAINT fk_transito_espacio FOREIGN KEY (id_espacio) REFERENCES espacio_transito (id_espacio)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE derivacion (
    id_derivacion BIGINT NOT NULL AUTO_INCREMENT,
    id_animal BIGINT NOT NULL,
    id_organizacion_origen BIGINT NOT NULL,
    id_organizacion_destino BIGINT NOT NULL,
    fecha_derivacion DATE NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    observaciones VARCHAR(1000) NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_derivacion),
    CONSTRAINT fk_derivacion_animal FOREIGN KEY (id_animal) REFERENCES animal (id_animal),
    CONSTRAINT fk_derivacion_origen FOREIGN KEY (id_organizacion_origen) REFERENCES organizacion (id_organizacion),
    CONSTRAINT fk_derivacion_destino FOREIGN KEY (id_organizacion_destino) REFERENCES organizacion (id_organizacion)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE vacunacion (
    id_vacunacion BIGINT NOT NULL AUTO_INCREMENT,
    id_animal BIGINT NOT NULL,
    fecha DATE NOT NULL,
    vacuna VARCHAR(100) NOT NULL,
    dosis VARCHAR(50) NOT NULL,
    observaciones VARCHAR(1000) NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_vacunacion),
    CONSTRAINT fk_vacunacion_animal FOREIGN KEY (id_animal) REFERENCES animal (id_animal)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE castracion (
    id_castracion BIGINT NOT NULL AUTO_INCREMENT,
    id_animal BIGINT NOT NULL,
    fecha DATE NOT NULL,
    veterinario VARCHAR(150) NOT NULL,
    observaciones VARCHAR(1000) NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_castracion),
    CONSTRAINT fk_castracion_animal FOREIGN KEY (id_animal) REFERENCES animal (id_animal)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE adoptante (
    id_adoptante BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    documento VARCHAR(30) NOT NULL,
    telefono VARCHAR(255) NULL,
    email VARCHAR(150) NULL,
    direccion VARCHAR(255) NULL,
    observaciones TEXT NULL,
    id_organizacion BIGINT NOT NULL,
    consentimiento_datos BIT(1) NOT NULL,
    fecha_consentimiento DATE NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_adoptante),
    CONSTRAINT uk_adoptante_documento_organizacion UNIQUE (documento, id_organizacion),
    CONSTRAINT fk_adoptante_organizacion FOREIGN KEY (id_organizacion) REFERENCES organizacion (id_organizacion)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE adopcion (
    id_adopcion BIGINT NOT NULL AUTO_INCREMENT,
    id_animal BIGINT NOT NULL,
    id_adoptante BIGINT NOT NULL,
    id_organizacion BIGINT NOT NULL,
    fecha_adopcion DATE NOT NULL,
    cumple_mayor_edad BIT(1) NOT NULL,
    cumple_responsabilidad BIT(1) NOT NULL,
    cumple_espacio_cerrado BIT(1) NOT NULL,
    compromiso_vacunacion BIT(1) NOT NULL,
    compromiso_castracion BIT(1) NOT NULL,
    condiciones TEXT NULL,
    observaciones TEXT NULL,
    fecha_limite_castracion DATE NOT NULL,
    alerta_castracion_vencida BIT(1) NOT NULL DEFAULT b'0',
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_adopcion),
    CONSTRAINT fk_adopcion_animal FOREIGN KEY (id_animal) REFERENCES animal (id_animal),
    CONSTRAINT fk_adopcion_adoptante FOREIGN KEY (id_adoptante) REFERENCES adoptante (id_adoptante),
    CONSTRAINT fk_adopcion_organizacion FOREIGN KEY (id_organizacion) REFERENCES organizacion (id_organizacion)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE seguimiento (
    id_seguimiento BIGINT NOT NULL AUTO_INCREMENT,
    id_adopcion BIGINT NOT NULL,
    fecha_seguimiento DATE NOT NULL,
    estado_animal VARCHAR(50) NOT NULL,
    observaciones VARCHAR(1000) NULL,
    referencia_multimedia VARCHAR(255) NULL,
    informacion_contacto TEXT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_seguimiento),
    CONSTRAINT fk_seguimiento_adopcion FOREIGN KEY (id_adopcion) REFERENCES adopcion (id_adopcion)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE campania_castracion (
    id_campania BIGINT NOT NULL AUTO_INCREMENT,
    id_organizacion BIGINT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    fecha DATE NOT NULL,
    lugar VARCHAR(255) NULL,
    cantidad_cupos INT NOT NULL,
    descripcion VARCHAR(1000) NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'PROGRAMADA',
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id_campania),
    CONSTRAINT ck_campania_castracion_cupos CHECK (cantidad_cupos >= 1),
    CONSTRAINT fk_campania_castracion_organizacion FOREIGN KEY (id_organizacion) REFERENCES organizacion (id_organizacion)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE asignacion_castracion (
    id_asignacion BIGINT NOT NULL AUTO_INCREMENT,
    id_campania BIGINT NOT NULL,
    id_animal BIGINT NOT NULL,
    fecha_asignacion DATE NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'ASIGNADA',
    observaciones VARCHAR(1000) NULL,
    PRIMARY KEY (id_asignacion),
    CONSTRAINT uk_asignacion_castracion_campania_animal UNIQUE (id_campania, id_animal),
    CONSTRAINT fk_asignacion_castracion_campania FOREIGN KEY (id_campania) REFERENCES campania_castracion (id_campania),
    CONSTRAINT fk_asignacion_castracion_animal FOREIGN KEY (id_animal) REFERENCES animal (id_animal)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO organizacion (id_organizacion, nombre, descripcion, telefono, direccion, email, etapa_vida_atendida) VALUES
    (1, 'Narices Frías', 'Organización de rescate de cachorros de San Salvador de Jujuy', NULL, NULL, 'contacto@naricesfrias.local', 'CACHORRO'),
    (2, 'Hogar San Roque', 'Hogar para perros y gatos adultos', NULL, NULL, 'contacto@hogarsanroque.local', 'ADULTO');

INSERT INTO usuario (id_usuario, nombre, apellido, email, contrasena, id_organizacion, estado, intentos_fallidos, cambio_obligatorio, fecha_cambio_contrasena) VALUES
    (1, 'Usuario Demo', 'Narices Frías', 'demo.narices@sysrefugios.local', '$2a$10$o4r7a9uvi9JF9q/RcQrKheeBqwaoTkPgBsUzCHbAQ8k8VW1n0Q1vu', 1, 'ACTIVO', 0, b'0', NOW()),
    (2, 'Usuario Demo', 'Hogar San Roque', 'demo.sanroque@sysrefugios.local', '$2a$10$o4r7a9uvi9JF9q/RcQrKheeBqwaoTkPgBsUzCHbAQ8k8VW1n0Q1vu', 2, 'ACTIVO', 0, b'0', NOW());

INSERT INTO espacio_transito (id_espacio, id_organizacion, nombre, descripcion, capacidad, estado, observaciones) VALUES
    (1, 1, 'Espacio de tránsito Narices Frías', 'Espacio para cachorros', 50, 'ACTIVO', NULL),
    (2, 2, 'Espacio de tránsito Hogar San Roque', 'Predio del hogar para animales adultos', 400, 'ACTIVO', 'Capacidad aproximada');

INSERT INTO animal (id_animal, nombre, especie, sexo, fecha_nacimiento, color, etapa_vida, tamanio, estado, descripcion, id_organizacion) VALUES
    (1, 'Luna', 'Perro', 'Hembra', DATE_SUB(CURDATE(), INTERVAL 11 MONTH), 'Negra con manchas blancas', 'CACHORRO', 'PEQUENO', 'ADOPTADO', 'Mestiza, muy sociable', 1),
    (2, 'Toto', 'Perro', 'Macho', NULL, 'Marrón', 'ADULTO', 'GRANDE', 'EN_TRANSITO', 'Mestizo. Ingresó por Narices Frías y se derivó por ser adulto', 2),
    (3, 'Milo', 'Gato', 'Macho', DATE_SUB(CURDATE(), INTERVAL 4 MONTH), 'Atigrado gris', 'CACHORRO', 'PEQUENO', 'ADOPTADO', 'Común europeo', 1),
    (4, 'Negra', 'Perro', 'Hembra', NULL, 'Negro', 'ADULTO', 'MEDIANO', 'EN_TRANSITO', 'Mestiza, llegó desnutrida', 2),
    (5, 'Pelusa', 'Gato', 'Hembra', DATE_SUB(CURDATE(), INTERVAL 2 YEAR), 'Crema', 'ADULTO', 'PEQUENO', 'EN_ADOPCION', 'Siamés. Castrada y vacunada, lista para adopción', 2),
    (6, 'Chispa', 'Perro', 'Macho', DATE_SUB(CURDATE(), INTERVAL 3 MONTH), 'Blanco y canela', 'CACHORRO', 'MEDIANO', 'EN_TRANSITO', 'Mestizo, muy activo', 1);

INSERT INTO rescate (id_rescate, id_animal, fecha_rescate, lugar, descripcion, observaciones, id_organizacion, estado, id_usuario) VALUES
    (1, 1, DATE_SUB(CURDATE(), INTERVAL 280 DAY), 'Barrio Alto Comedero', 'Cachorra encontrada sola en la vereda', 'Ya comía alimento balanceado', 1, 'FINALIZADO', 1),
    (2, 2, DATE_SUB(CURDATE(), INTERVAL 60 DAY), 'Ruta 9, km 12', 'Perro adulto atropellado, con una pata lastimada', 'Se lo llevó al veterinario el mismo día', 1, 'DERIVADO', 1),
    (3, 3, DATE_SUB(CURDATE(), INTERVAL 70 DAY), 'Barrio Centro', 'Gatito dentro de una caja', NULL, 1, 'FINALIZADO', 1),
    (4, 4, DATE_SUB(CURDATE(), INTERVAL 25 DAY), 'Palpalá', 'Perra adulta desnutrida', 'Dieta especial durante el primer mes', 2, 'EN_CURSO', 2),
    (5, 5, DATE_SUB(CURDATE(), INTERVAL 90 DAY), 'Barrio Gorriti', 'Gata adulta abandonada en un patio', NULL, 2, 'FINALIZADO', 2),
    (6, 6, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 'Barrio Los Huaicos', 'Cachorro encontrado en la plaza', 'Ya come solo', 1, 'EN_CURSO', 1);

INSERT INTO transito (id_transito, id_animal, id_espacio, fecha_inicio, fecha_fin, estado, observaciones) VALUES
    (1, 1, 1, DATE_SUB(CURDATE(), INTERVAL 279 DAY), DATE_SUB(CURDATE(), INTERVAL 250 DAY), 'FINALIZADO', NULL),
    (2, 2, 1, DATE_SUB(CURDATE(), INTERVAL 59 DAY), DATE_SUB(CURDATE(), INTERVAL 55 DAY), 'FINALIZADO', 'Estadía breve hasta la derivación'),
    (3, 2, 2, DATE_SUB(CURDATE(), INTERVAL 55 DAY), NULL, 'EN_CURSO', 'En recuperación de la pata'),
    (4, 3, 1, DATE_SUB(CURDATE(), INTERVAL 69 DAY), DATE_SUB(CURDATE(), INTERVAL 35 DAY), 'FINALIZADO', NULL),
    (5, 4, 2, DATE_SUB(CURDATE(), INTERVAL 24 DAY), NULL, 'EN_CURSO', NULL),
    (6, 5, 2, DATE_SUB(CURDATE(), INTERVAL 89 DAY), DATE_SUB(CURDATE(), INTERVAL 30 DAY), 'FINALIZADO', NULL),
    (7, 6, 1, DATE_SUB(CURDATE(), INTERVAL 9 DAY), NULL, 'EN_CURSO', NULL);

INSERT INTO derivacion (id_derivacion, id_animal, id_organizacion_origen, id_organizacion_destino, fecha_derivacion, motivo, observaciones) VALUES
    (1, 2, 1, 2, DATE_SUB(CURDATE(), INTERVAL 55 DAY), 'Animal adulto: corresponde al Hogar San Roque', 'Se coordinó el traslado por teléfono');

INSERT INTO vacunacion (id_vacunacion, id_animal, fecha, vacuna, dosis, observaciones) VALUES
    (1, 1, DATE_SUB(CURDATE(), INTERVAL 265 DAY), 'Séxtuple', 'Primera dosis', NULL),
    (2, 3, DATE_SUB(CURDATE(), INTERVAL 50 DAY), 'Triple felina', 'Primera dosis', NULL),
    (3, 5, DATE_SUB(CURDATE(), INTERVAL 80 DAY), 'Triple felina', 'Refuerzo anual', NULL),
    (4, 2, DATE_SUB(CURDATE(), INTERVAL 50 DAY), 'Antirrábica', 'Dosis única', NULL);

INSERT INTO castracion (id_castracion, id_animal, fecha, veterinario, observaciones) VALUES
    (1, 5, DATE_SUB(CURDATE(), INTERVAL 60 DAY), 'Dra. Paula Quispe', 'Sin complicaciones');

INSERT INTO adoptante (id_adoptante, nombre, apellido, documento, telefono, email, direccion, observaciones, id_organizacion, consentimiento_datos, fecha_consentimiento) VALUES
    (1, 'María', 'González', '28444555', '388-4111222', 'maria.gonzalez@ejemplo.local', 'Barrio Ciudad de Nieva', 'Tiene patio cerrado', 1, b'1', DATE_SUB(CURDATE(), INTERVAL 252 DAY)),
    (2, 'Carlos', 'Ruiz', '33222111', '388-4333444', NULL, 'Barrio Cuyaya', NULL, 1, b'1', DATE_SUB(CURDATE(), INTERVAL 37 DAY)),
    (3, 'Laura', 'Fernández', '30999888', '388-4555666', 'laura.fernandez@ejemplo.local', 'Palpalá', NULL, 2, b'1', DATE_SUB(CURDATE(), INTERVAL 20 DAY));

INSERT INTO adopcion (id_adopcion, id_animal, id_adoptante, id_organizacion, fecha_adopcion, cumple_mayor_edad, cumple_responsabilidad, cumple_espacio_cerrado, compromiso_vacunacion, compromiso_castracion, condiciones, observaciones, fecha_limite_castracion, alerta_castracion_vencida) VALUES
    (1, 1, 1, 1, DATE_SUB(CURDATE(), INTERVAL 250 DAY), b'1', b'1', b'1', b'1', b'1', 'Completar el calendario de vacunas y castrar al cumplir la edad indicada', NULL,
        DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 250 DAY), INTERVAL 6 MONTH), b'1'),
    (2, 3, 2, 1, DATE_SUB(CURDATE(), INTERVAL 35 DAY), b'1', b'1', b'1', b'1', b'1', 'Enviar fotos mensuales', 'Primera adopción del adoptante',
        DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 35 DAY), INTERVAL 6 MONTH), b'0');

INSERT INTO seguimiento (id_seguimiento, id_adopcion, fecha_seguimiento, estado_animal, observaciones, referencia_multimedia, informacion_contacto) VALUES
    (1, 1, DATE_SUB(CURDATE(), INTERVAL 220 DAY), 'Bueno', 'Se adaptó bien.', 'Fotos enviadas por WhatsApp', 'WhatsApp de la adoptante'),
    (2, 1, DATE_SUB(CURDATE(), INTERVAL 150 DAY), 'Bueno', 'Se le recordó la castración pendiente.', NULL, 'Llamada telefónica'),
    (3, 2, DATE_SUB(CURDATE(), INTERVAL 20 DAY), 'Muy bueno', 'Primera visita: todo bien.', NULL, 'Visita al domicilio');

INSERT INTO campania_castracion (id_campania, id_organizacion, nombre, fecha, lugar, cantidad_cupos, descripcion, estado) VALUES
    (1, 1, 'Campaña de castración Alto Comedero', DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'Centro vecinal Alto Comedero', 2, 'Castración gratuita de cachorros a cargo de la organización', 'PROGRAMADA'),
    (2, 2, 'Jornada de castración Palpalá', DATE_ADD(CURDATE(), INTERVAL 20 DAY), 'Salón municipal de Palpalá', 3, 'Jornada para animales adultos del hogar', 'PROGRAMADA');

INSERT INTO asignacion_castracion (id_asignacion, id_campania, id_animal, fecha_asignacion, estado, observaciones) VALUES
    (1, 1, 6, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'ASIGNADA', NULL),
    (2, 2, 2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'ASIGNADA', 'Una vez recuperado de la pata'),
    (3, 2, 4, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'ASIGNADA', NULL);
