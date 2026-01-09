-- =========================================
-- SEED COMPLETO DA BASE DE DADOS — ESTG
-- (MySQL / MariaDB)
-- Compatível com Spring Security + JWT
-- =========================================

-- 0) Desativar safe updates (Workbench)
SET SQL_SAFE_UPDATES = 0;

-- 1) Limpar tabelas (ordem correta por FKs)
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM resolucao WHERE id IS NOT NULL;
DELETE FROM exercicio WHERE id IS NOT NULL;
DELETE FROM unidade_curricular WHERE id IS NOT NULL;

DELETE FROM aluno WHERE id IS NOT NULL;
DELETE FROM docente WHERE id IS NOT NULL;
DELETE FROM professor WHERE id IS NOT NULL;

DELETE FROM user_roles WHERE user_id IS NOT NULL;
DELETE FROM user WHERE id IS NOT NULL;
DELETE FROM role WHERE id IS NOT NULL;

SET FOREIGN_KEY_CHECKS = 1;

-- 2) ROLES
INSERT INTO role (id, name) VALUES
                                (1, 'ALUNO'),
                                (2, 'DOCENTE');

-- 3) USERS (AUTENTICAÇÃO)
-- ⚠️ Passwords com {noop} (obrigatório)
INSERT INTO user (id, email, nome, password) VALUES
                                                 (1, 'docente@estg.pt',  'Prof. Carlos Silva', '{noop}1234'),
                                                 (2, 'docente2@estg.pt', 'Prof. Maria Rocha',  '{noop}1234'),
                                                 (3, 'aluno1@estg.pt',   'Ricardo Pereira',    '{noop}1234'),
                                                 (4, 'aluno2@estg.pt',   'Ana Martins',        '{noop}1234'),
                                                 (5, 'aluno3@estg.pt',   'João Costa',          '{noop}1234'),
                                                 (6, 'aluno4@estg.pt',   'Inês Ferreira',       '{noop}1234');

-- 4) USER_ROLES
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 2),
                                              (2, 2),
                                              (3, 1),
                                              (4, 1),
                                              (5, 1),
                                              (6, 1);

-- 5) DOCENTES
INSERT INTO docente (id, email, nome) VALUES
                                          (1, 'docente@estg.pt',  'Prof. Carlos Silva'),
                                          (2, 'docente2@estg.pt', 'Prof. Maria Rocha');

-- 6) PROFESSOR (usar só se existir no teu esquema)
INSERT INTO professor (id, email, nome) VALUES
                                            (1, 'docente@estg.pt',  'Prof. Carlos Silva'),
                                            (2, 'docente2@estg.pt', 'Prof. Maria Rocha');

-- 7) ALUNOS
INSERT INTO aluno (id, email, nome, password) VALUES
                                                  (1, 'aluno1@estg.pt', 'Ricardo Pereira', '{noop}1234'),
                                                  (2, 'aluno2@estg.pt', 'Ana Martins',     '{noop}1234'),
                                                  (3, 'aluno3@estg.pt', 'João Costa',       '{noop}1234'),
                                                  (4, 'aluno4@estg.pt', 'Inês Ferreira',    '{noop}1234');

-- 8) UNIDADES CURRICULARES
INSERT INTO unidade_curricular (id, nome, docente_id) VALUES
                                                          (1, 'Engenharia de Software', 1),
                                                          (2, 'Programação Web',        1),
                                                          (3, 'Bases de Dados',         2);

-- 9) EXERCÍCIOS
INSERT INTO exercicio (id, enunciado, total_etapas, unidade_curricular_id) VALUES
                                                                               (1, 'Checkpoint 1 – UML e Requisitos',   4, 1),
                                                                               (2, 'Checkpoint 2 – Arquitetura Spring', 5, 1),
                                                                               (3, 'Checkpoint 3 – Testes e Qualidade', 4, 1),
                                                                               (4, 'React – Dashboard do Aluno',        3, 2),
                                                                               (5, 'React – Dashboard do Docente',      4, 2),
                                                                               (6, 'SQL – Modelo Relacional',           4, 3),
                                                                               (7, 'SQL – Queries Avançadas',           5, 3);

-- 10) RESOLUÇÕES (estado realista para testes)
INSERT INTO resolucao
(id, data_inicio, nota, solicitar_ajuda, terminado, ultima_etapa_concluida, aluno_id, exercicio_id)
VALUES
-- Ricardo
(1, NOW(), NULL, 0, 0, 1, 1, 1),
(2, NOW(), NULL, 1, 0, 2, 1, 2),
(3, NOW(),  15.5,0, 1, 3, 1, 4),
-- Ana
(4, NOW(), NULL, 0, 0, 3, 2, 1),
(5, NOW(), NULL, 1, 0, 1, 2, 5),
(6, NOW(),  17.0,0, 1, 4, 2, 6),
-- João
(7, NOW(), NULL, 0, 0, 0, 3, 3),
(8, NOW(), NULL, 0, 0, 2, 3, 7),
(9, NOW(), NULL, 1, 0, 1, 3, 2),
-- Inês
(10, NOW(), NULL, 0, 0, 2, 4, 4),
(11, NOW(),  13.0,0, 1, 4, 4, 5);

-- 11) Reativar safe updates (opcional)
SET SQL_SAFE_UPDATES = 1;
