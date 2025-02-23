-- Limpiar datos existentes manteniendo la estructura
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE RESERVA;
TRUNCATE TABLE AULA;
TRUNCATE TABLE PLANTA;
TRUNCATE TABLE EDIFICIO;
TRUNCATE TABLE CURSO;
TRUNCATE TABLE FAMCURSO;
TRUNCATE TABLE SUBUSOAULA;
TRUNCATE TABLE USOAULA;
SET FOREIGN_KEY_CHECKS = 1;

-- Insertar edificios
INSERT INTO EDIFICIO (nombre, descripcion, npuertasacceso, ubicacion, activo, foto) VALUES
('Edificio Principal', 'Edificio principal del centro', 4, 'Entrada principal del campus', true, 'edificio-principal.jpg'),
('Edificio Tecnológico', 'Edificio dedicado a tecnología', 2, 'Zona norte del campus', true, 'edificio-tecnologico.jpg'),
('Edificio de Ciencias', 'Edificio de laboratorios', 3, 'Zona este del campus', true, 'edificio-ciencias.jpg');

-- Insertar plantas para cada edificio
INSERT INTO PLANTA (nombre, descripcion, naulasdocentes, naulaauxiliares, activo, EDIFICIOidedificio) VALUES
-- Edificio Principal
('Planta Baja Principal', 'Planta baja con recepción', 5, 2, true, 1),
('Primera Planta Principal', 'Primera planta con aulas', 8, 1, true, 1),
('Segunda Planta Principal', 'Segunda planta con despachos', 6, 2, true, 1),

-- Edificio Tecnológico
('Planta Baja Tecnológica', 'Planta con laboratorios informáticos', 4, 1, true, 2),
('Primera Planta Tecnológica', 'Planta con aulas multimedia', 6, 2, true, 2),

-- Edificio de Ciencias
('Planta Baja Ciencias', 'Planta con laboratorios', 5, 2, true, 3),
('Primera Planta Ciencias', 'Planta con aulas teóricas', 7, 1, true, 3);

-- Insertar tipos de aula si no existen
INSERT INTO TIPOAULA (nombre, descripcion, activo) VALUES
('Aula Teórica', 'Para clases teóricas', true),
('Laboratorio Informática', 'Con ordenadores', true),
('Laboratorio Ciencias', 'Con equipamiento científico', true),
('Aula Multimedia', 'Con equipamiento audiovisual', true);

-- Insertar aulas
INSERT INTO AULA (nombre, descripcion, capacidad, proyector, smarttv, hdmi, appletv, aireacondicionado, nenchufes, activo, foto, TIPOAULAidtipo, PLANTAidplanta) VALUES
-- Aulas Edificio Principal - Planta Baja
('A001', 'Aula teórica grande', 40, true, true, true, false, true, 20, true, 'aula-001.jpg', 1, 1),
('A002', 'Aula teórica mediana', 30, true, false, true, false, true, 15, true, 'aula-002.jpg', 1, 1),

-- Aulas Edificio Principal - Primera Planta
('A101', 'Aula multimedia', 35, true, true, true, true, true, 35, true, 'aula-101.jpg', 4, 2),
('A102', 'Laboratorio informática', 25, true, true, true, false, true, 30, true, 'aula-102.jpg', 2, 2),

-- Aulas Edificio Tecnológico - Planta Baja
('T001', 'Laboratorio informática avanzado', 30, true, true, true, true, true, 40, true, 'aula-t001.jpg', 2, 4),
('T002', 'Aula multimedia especializada', 25, true, true, true, true, true, 30, true, 'aula-t002.jpg', 4, 4),

-- Aulas Edificio Ciencias - Planta Baja
('C001', 'Laboratorio química', 20, true, false, true, false, true, 15, true, 'aula-c001.jpg', 3, 6),
('C002', 'Laboratorio física', 20, true, false, true, false, true, 15, true, 'aula-c002.jpg', 3, 6);

-- Insertar usos de aula
INSERT INTO USOAULA (nombre, descripcion, activo) VALUES
('Docencia Regular', 'Clases regulares', true),
('Prácticas', 'Sesiones prácticas', true),
('Eventos', 'Eventos especiales', true);

-- Insertar subusos de aula
INSERT INTO SUBUSOAULA (nombre, descripcion, activo, USOAULAiduso) VALUES
('Clases Teóricas', 'Para teoría', true, 1),
('Laboratorio', 'Para prácticas', true, 2),
('Seminarios', 'Para grupos pequeños', true, 1),
('Conferencias', 'Para eventos', true, 3);

-- Insertar familias de cursos
INSERT INTO FAMCURSO (nombre, descripcion, activo, SUBUSOAULAidsubuso) VALUES
('Informática', 'Cursos de informática', true, 2),
('Ciencias', 'Cursos de ciencias', true, 2),
('Administración', 'Cursos de administración', true, 1);

-- Insertar cursos
INSERT INTO CURSO (nombre, descripcion, fechainicio, fechafin, activo, FAMCURSOidfamcurso) VALUES
('DAW', 'Desarrollo de Aplicaciones Web', '2024-09-15', '2025-06-20', true, 1),
('DAM', 'Desarrollo de Aplicaciones Multiplataforma', '2024-09-15', '2025-06-20', true, 1),
('Administración de Sistemas', 'Administración de Sistemas Informáticos', '2024-09-15', '2025-06-20', true, 1),
('Química Industrial', 'Técnico en Química Industrial', '2024-09-15', '2025-06-20', true, 2),
('Gestión Administrativa', 'Técnico en Gestión Administrativa', '2024-09-15', '2025-06-20', true, 3); 