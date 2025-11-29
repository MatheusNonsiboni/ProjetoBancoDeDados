CREATE SCHEMA SADE;
USE SADE;

DROP TABLE IF EXISTS Desempenho_Disciplina CASCADE;
DROP TABLE IF EXISTS Indicador_Educacional CASCADE;
DROP TABLE IF EXISTS Disciplina CASCADE;
DROP TABLE IF EXISTS Escola CASCADE;
DROP TABLE IF EXISTS Regiao CASCADE;
DROP TABLE IF EXISTS Gestor CASCADE;

CREATE TABLE Regiao (
    id_regiao INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    mesorregiao VARCHAR(100)
);

CREATE TABLE Escola (
    id_escola INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    codigo_mec VARCHAR(20),
    cidade VARCHAR(100),
    tipo_localizacao VARCHAR(10) CHECK (tipo_localizacao IN ('Urbana', 'Rural')),
    id_regiao INT NOT NULL,
    CONSTRAINT fk_escola_regiao FOREIGN KEY (id_regiao)
        REFERENCES Regiao (id_regiao)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE Disciplina (
    id_disciplina INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    area_conhecimento VARCHAR(50),
    id_escola INT NOT NULL, 
    
    CONSTRAINT fk_disciplina_escola FOREIGN KEY (id_escola)
        REFERENCES Escola (id_escola)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
CREATE TABLE Desempenho_Disciplina (
    id_desempenho INT AUTO_INCREMENT PRIMARY KEY,
    id_escola INT NOT NULL,
	ano_letivo INT NOT NULL,
    id_disciplina INT NULL,
    media_disciplina DECIMAL(5,2) CHECK (media_disciplina BETWEEN 0 AND 10),
    frequencia_media DECIMAL(5,2) CHECK (frequencia_media BETWEEN 0 AND 100),

    CONSTRAINT fk_desempenho_escola FOREIGN KEY (id_escola)
        REFERENCES Escola (id_escola)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_desempenho_disciplina FOREIGN KEY (id_disciplina)
        REFERENCES Disciplina (id_disciplina)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE TABLE Indicador_Educacional (
    id_indicador INT AUTO_INCREMENT PRIMARY KEY,
    id_escola INT NOT NULL,
    ano_letivo INT NOT NULL,
    ideb DECIMAL(4,2) CHECK (ideb BETWEEN 0 AND 10),
    taxa_evasao DECIMAL(5,2) CHECK (taxa_evasao BETWEEN 0 AND 100),

    CONSTRAINT fk_indicador_escola FOREIGN KEY (id_escola)
        REFERENCES Escola (id_escola)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Gestor (
    id_gestor INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    codigo_acesso VARCHAR(20) NOT NULL UNIQUE,
    id_escola INT NOT NULL,
    CONSTRAINT fk_gestor_escola FOREIGN KEY (id_escola)
        REFERENCES Escola(id_escola)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE INDEX idx_escola_regiao ON Escola (id_regiao);
CREATE INDEX idx_desempenho_disciplina ON Desempenho_Disciplina (id_escola, id_disciplina, ano_letivo);
CREATE INDEX idx_indicador_escola_ano ON Indicador_Educacional (id_escola, ano_letivo);
CREATE INDEX idx_gestor_escola ON Gestor (id_escola);

SELECT * FROM gestor;
SELECT * FROM disciplina;
SELECT * FROM escola;
SELECT * FROM regiao;
SELECT * FROM indicador_educacional;
