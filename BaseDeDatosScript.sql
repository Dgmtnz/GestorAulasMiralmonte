-- Tabla AULA
use gestoraulas;
CREATE TABLE AULA (
    idaula BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(200),
    capacidad DECIMAL(3),
    proyector TINYINT(1),
    smarttv TINYINT(1),
    hdmi TINYINT(1),
    appletv TINYINT(1),
    aireacondicionado TINYINT(1),
    nenchufes DECIMAL(2),
    activo TINYINT(1),
    foto VARCHAR(255),
    TIPOAULAidtipo BIGINT,
    PLANTAidplanta BIGINT,
    PRIMARY KEY (idaula)
);

-- Tabla CURSO
CREATE TABLE CURSO (
    idcurso BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    fechainicio DATE,
    fechafin DATE,
    activo TINYINT(1),
    FAMCURSOidfamcurso BIGINT NOT NULL,
    PRIMARY KEY (idcurso)
);

-- Tabla EDIFICIO
CREATE TABLE EDIFICIO (
    idedificio BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    npuertasacceso DECIMAL(2),
    ubicacion VARCHAR(200),
    activo TINYINT(1),
    foto VARCHAR(255),
    nplantas INT DEFAULT 0,
    PRIMARY KEY (idedificio)
);

-- Tabla FAMCURSO
CREATE TABLE FAMCURSO (
    idfamcurso BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    activo TINYINT(1),
    SUBUSOAULAidsubuso BIGINT NOT NULL,
    PRIMARY KEY (idfamcurso)
);

-- Tabla PLANTA
CREATE TABLE PLANTA (
    idplanta BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200),
    naulasdocentes DECIMAL(3),
    naulaauxiliares DECIMAL(2),
    activo TINYINT(1),
    EDIFICIOidedificio BIGINT NOT NULL,
    naulas INT DEFAULT 0,
    PRIMARY KEY (idplanta)
);

-- Tabla RESERVA
CREATE TABLE RESERVA (
    idreserva BIGINT NOT NULL AUTO_INCREMENT,
    fechadesde DATE,
    fechahasta DATE,
    horadesde TIME,
    horahasta TIME,
    validar TINYINT(1),
    activo TINYINT(1),
    AULAidaula BIGINT,
    USUARIOidusuario BIGINT,
    CURSOidcurso BIGINT,
    PRIMARY KEY (idreserva)
);

-- Tabla SUBUSOAULA
CREATE TABLE SUBUSOAULA (
    idsubuso BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200),
    activo TINYINT(1),
    USOAULAiduso BIGINT NOT NULL,
    PRIMARY KEY (idsubuso)
);

-- Tabla TIPOAULA
CREATE TABLE TIPOAULA (
    idtipo BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(200),
    activo TINYINT(1),
    PRIMARY KEY (idtipo)
);

-- Tabla TIPOUSUARIO
CREATE TABLE TIPOUSUARIO (
    idtipousuario BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(200),
    activo TINYINT(1),
    PRIMARY KEY (idtipousuario)
);

-- Tabla USOAULA
CREATE TABLE USOAULA (
    iduso BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200),
    activo TINYINT(1),
    PRIMARY KEY (iduso)
);

-- Tabla USUARIO
CREATE TABLE USUARIO (
    idusuario BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30),
    apellido VARCHAR(150),
    fechaactivacion DATE,
    fechadesactivacion DATE,
    dni VARCHAR(9),
    email VARCHAR(200),
    contrasenya VARCHAR(60),
    activo TINYINT(1),
    telefono VARCHAR(9),
    foto VARCHAR(255),
    TIPOUSUARIOidtipousuario BIGINT,
    PRIMARY KEY (idusuario)
);

-- Nueva tabla para relación N:N entre USUARIO y CURSO
CREATE TABLE USUARIO_CURSO (
    USUARIOidusuario BIGINT,
    CURSOidcurso BIGINT,
    fechaasignacion DATE NOT NULL,
    activo TINYINT(1),
    PRIMARY KEY (USUARIOidusuario, CURSOidcurso)
);

---------------------------------------------------------------------
-- Claves foráneas (FOREIGN KEYS)
---------------------------------------------------------------------

ALTER TABLE AULA 
    ADD CONSTRAINT AULAPLANTAFK 
    FOREIGN KEY (PLANTAidplanta) REFERENCES PLANTA(idplanta) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE AULA 
    ADD CONSTRAINT AULATIPOAULAFK 
    FOREIGN KEY (TIPOAULAidtipo) REFERENCES TIPOAULA(idtipo) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE CURSO 
    ADD CONSTRAINT CURSOFAMCURSOFK 
    FOREIGN KEY (FAMCURSOidfamcurso) REFERENCES FAMCURSO(idfamcurso) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE FAMCURSO 
    ADD CONSTRAINT FAMCURSOSUBUSOAULAFK 
    FOREIGN KEY (SUBUSOAULAidsubuso) REFERENCES SUBUSOAULA(idsubuso) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE PLANTA 
    ADD CONSTRAINT PLANTAEDIFICIOFK 
    FOREIGN KEY (EDIFICIOidedificio) REFERENCES EDIFICIO(idedificio) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE RESERVA 
    ADD CONSTRAINT RESERVAAULAFK 
    FOREIGN KEY (AULAidaula) REFERENCES AULA(idaula) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE RESERVA 
    ADD CONSTRAINT RESERVACURSOFK 
    FOREIGN KEY (CURSOidcurso) REFERENCES CURSO(idcurso) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE RESERVA 
    ADD CONSTRAINT RESERVAUSUARIOFK 
    FOREIGN KEY (USUARIOidusuario) REFERENCES USUARIO(idusuario) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE SUBUSOAULA 
    ADD CONSTRAINT SUBUSOAULAUSOAULAFK 
    FOREIGN KEY (USOAULAiduso) REFERENCES USOAULA(iduso) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE USUARIO 
    ADD CONSTRAINT USUARIOTIPOUSUARIOFK 
    FOREIGN KEY (TIPOUSUARIOidtipousuario) REFERENCES TIPOUSUARIO(idtipousuario) 
    ON DELETE RESTRICT ON UPDATE CASCADE;

-- Triggers para actualización automática
DELIMITER //
CREATE TRIGGER after_planta_insert
AFTER INSERT ON PLANTA
FOR EACH ROW
BEGIN
    UPDATE EDIFICIO 
    SET nplantas = nplantas + 1 
    WHERE idedificio = NEW.EDIFICIOidedificio;
END;//

CREATE TRIGGER after_planta_delete
AFTER DELETE ON PLANTA
FOR EACH ROW
BEGIN
    UPDATE EDIFICIO 
    SET nplantas = nplantas - 1 
    WHERE idedificio = OLD.EDIFICIOidedificio;
END;//

CREATE TRIGGER after_aula_insert
AFTER INSERT ON AULA
FOR EACH ROW
BEGIN
    UPDATE PLANTA 
    SET naulas = naulas + 1 
    WHERE idplanta = NEW.PLANTAidplanta;
END;//

CREATE TRIGGER after_aula_delete
AFTER DELETE ON AULA
FOR EACH ROW
BEGIN
    UPDATE PLANTA 
    SET naulas = naulas - 1 
    WHERE idplanta = OLD.PLANTAidplanta;
END;//
DELIMITER ;

-- Tipos de Usuario
INSERT INTO TIPOUSUARIO (nombre, descripcion, activo) VALUES
('ADMIN', 'Administrador del sistema', true),
('USER', 'Usuario regular', true);

-- Tipos de Aula
INSERT INTO TIPOAULA (nombre, descripcion, activo) VALUES
('Aula Ordinaria', 'Aula de uso general para clases regulares', true),
('Aula Específica', 'Aula equipada para usos específicos como laboratorios o talleres', true),
('Otros', 'Otros tipos de espacios educativos', true);

-- Uso de Aula
INSERT INTO USOAULA (nombre, descripcion, activo) VALUES
('Docencia', 'Uso para actividades docentes', true),
('Administrativo', 'Uso para actividades administrativas', true),
('Otros', 'Otros usos', true);

-- SubUso de Aula
INSERT INTO SUBUSOAULA (nombre, descripcion, activo, USOAULAiduso) VALUES
('Clases Teóricas', 'Para impartir clases teóricas', true, 1),
('Laboratorios', 'Para prácticas y experimentos', true, 1),
('Talleres', 'Para actividades prácticas', true, 1),
('Oficinas', 'Para trabajo administrativo', true, 2);

-- Familia de Cursos
INSERT INTO FAMCURSO (nombre, descripcion, activo, SUBUSOAULAidsubuso) VALUES
('Bachillerato', 'Cursos de bachillerato', true, 1),
('Formación Profesional', 'Ciclos formativos de FP', true, 1),
('ESO', 'Educación Secundaria Obligatoria', true, 1),
('Otros', 'Otros tipos de formación', true, 1);