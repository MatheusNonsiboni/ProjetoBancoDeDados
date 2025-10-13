CREATE SCHEMA SADE;
USE SADE;

DROP TABLE IF EXISTS Desempenho_Disciplina CASCADE;
DROP TABLE IF EXISTS Indicador_Educacional CASCADE;
DROP TABLE IF EXISTS Avaliacao CASCADE;
DROP TABLE IF EXISTS Disciplina CASCADE;
DROP TABLE IF EXISTS Escola CASCADE;
DROP TABLE IF EXISTS Regiao CASCADE;

CREATE TABLE Regiao (
    id_regiao SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    mesorregiao VARCHAR(100)
);

CREATE TABLE Escola (
    id_escola SERIAL PRIMARY KEY,
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
    id_disciplina SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    area_conhecimento VARCHAR(50)
);

CREATE TABLE Avaliacao (
    id_avaliacao SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_avaliacao DATE NOT NULL,
    etapa VARCHAR(50),
    descricao TEXT
);

CREATE TABLE Desempenho_Disciplina (
    id_desempenho SERIAL PRIMARY KEY,
    id_escola INT NOT NULL,
    id_disciplina INT NOT NULL,
    id_avaliacao INT NOT NULL,
    media_disciplina DECIMAL(5,2) CHECK (media_disciplina BETWEEN 0 AND 10),

    CONSTRAINT fk_desempenho_escola FOREIGN KEY (id_escola)
        REFERENCES Escola (id_escola)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_desempenho_disciplina FOREIGN KEY (id_disciplina)
        REFERENCES Disciplina (id_disciplina)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_desempenho_avaliacao FOREIGN KEY (id_avaliacao)
        REFERENCES Avaliacao (id_avaliacao)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Indicador_Educacional (
    id_indicador SERIAL PRIMARY KEY,
    id_escola INT NOT NULL,
    ano INT NOT NULL,
    ideb DECIMAL(4,2) CHECK (ideb BETWEEN 0 AND 10),
    taxa_frequencia DECIMAL(5,2) CHECK (taxa_frequencia BETWEEN 0 AND 100),
    taxa_evasao DECIMAL(5,2) CHECK (taxa_evasao BETWEEN 0 AND 100),

    CONSTRAINT fk_indicador_escola FOREIGN KEY (id_escola)
        REFERENCES Escola (id_escola)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE INDEX idx_escola_regiao ON Escola (id_regiao);
CREATE INDEX idx_desempenho_disciplina ON Desempenho_Disciplina (id_escola, id_disciplina, id_avaliacao);
CREATE INDEX idx_indicador_escola_ano ON Indicador_Educacional (id_escola, ano);