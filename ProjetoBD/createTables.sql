DROP TABLE IF EXISTS Pessoa_Mesa_De_Voto;

DROP TABLE IF EXISTS Votos;

DROP TABLE IF EXISTS Pessoa_Lista;

DROP TABLE IF EXISTS Pessoa_Eleicao;

DROP TABLE IF EXISTS Mesa_De_Voto;

DROP TABLE IF EXISTS Departamento;

DROP TABLE IF EXISTS Faculdade;

DROP TABLE IF EXISTS Lista;

DROP TABLE IF EXISTS Eleicao;

DROP TABLE IF EXISTS Pessoa;

CREATE TABLE Pessoa (
    Id INT(9) NOT NULL PRIMARY KEY,
    Nome VARCHAR(60) NOT NULL,
    Password VARCHAR(20) NOT NULL,
    Telemovel INT(9) NOT NULL,
    Morada VARCHAR(30) NOT NULL,
    Validade_Cc TIMESTAMP NOT NULL,
    Tipo VARCHAR(15) NOT NULL,
    Ativo CHAR(1) DEFAULT 'N' NOT NULL,
	FaceToken VARCHAR(350)
);

CREATE TABLE Faculdade (
    Nome VARCHAR(60) NOT NULL PRIMARY KEY
);

CREATE TABLE Departamento (
    Nome VARCHAR(60) NOT NULL PRIMARY KEY,
    Faculdade_Nome VARCHAR(60) NOT NULL,
    CONSTRAINT Fk_Dfaculdade FOREIGN KEY (Faculdade_Nome)
        REFERENCES Faculdade (Nome)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Eleicao (
    Titlo VARCHAR(30) NOT NULL PRIMARY KEY,
    Data_Inicio TIMESTAMP NOT NULL,
    Data_Fim TIMESTAMP NOT NULL,
    Tipo VARCHAR(60) NOT NULL,
    Tipo_Membros VARCHAR(60) NOT NULL,
    Descricao VARCHAR(100) NOT NULL,
    Ativo CHAR(1) DEFAULT 'N' NOT NULL
);

CREATE TABLE Mesa_De_Voto (
	Mesa_ID INT NOT NULL AUTO_INCREMENT,
    Nulos INT DEFAULT 0 NOT NULL,
    Brancos INT DEFAULT 0 NOT NULL,
    Departamento_Nome VARCHAR(60) NOT NULL,
    Eleicao_Titlo VARCHAR(30),
    Ativo TINYINT DEFAULT 0 NOT NULL,
    CONSTRAINT Pk_Mesa_De_Voto PRIMARY KEY (Mesa_ID),
    CONSTRAINT Fk_Mdepartamento FOREIGN KEY (Departamento_Nome)
        REFERENCES Departamento (Nome)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT Fk_Meleicao FOREIGN KEY (Eleicao_Titlo)
        REFERENCES Eleicao (Titlo)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
	CONSTRAINT Uq_Mesa_Departamento UNIQUE(Departamento_Nome, Eleicao_Titlo)
);

CREATE TABLE Lista (
    Nome VARCHAR(60) NOT NULL,
    Tipo_Membros VARCHAR(60) NOT NULL,
    Eleicao_Titlo VARCHAR(30) NOT NULL,
    CONSTRAINT Pk_L_Nome_Eleicao PRIMARY KEY (Nome , Eleicao_Titlo),
    CONSTRAINT Fk_Leleicao FOREIGN KEY (Eleicao_Titlo)
        REFERENCES Eleicao (Titlo)
        
);

CREATE TABLE Votos (
    Votos INT DEFAULT 0 NOT NULL,
    Id_Mesa INT NOT NULL,
    Lista_Nome VARCHAR(30) NOT NULL,
    Lista_Eleicao VARCHAR(30) NOT NULL,
    CONSTRAINT Pk_V_Lista_Mesa PRIMARY KEY (Id_Mesa , Lista_Eleicao , Lista_Nome),
    CONSTRAINT Fk_Vmesa FOREIGN KEY (Id_Mesa)
        REFERENCES Mesa_De_Voto (Mesa_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT Fk_Vlista FOREIGN KEY (Lista_Nome , Lista_Eleicao)
        REFERENCES Lista (Nome , Eleicao_Titlo)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Pessoa_Lista (
    Pessoa_Id INT NOT NULL,
    Lista_Nome VARCHAR(30) NOT NULL,
    Lista_Eleicao VARCHAR(30) NOT NULL,
    CONSTRAINT Pk_Pl_Pessoa_Lista PRIMARY KEY (Pessoa_Id , Lista_Nome),
    CONSTRAINT Fk_Plpessoa FOREIGN KEY (Pessoa_Id)
        REFERENCES Pessoa (Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT Fk_Pl_Lista_Eleicao FOREIGN KEY (Lista_Nome , Lista_Eleicao)
        REFERENCES Lista (Nome , Eleicao_Titlo)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Pessoa_Eleicao (
    Pessoa_Id INT NOT NULL,
    Id_Mesa INT NOT NULL,
    Eleicao_Titlo VARCHAR(60) NOT NULL,
    Data_Voto TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT Pk_Pessoa_Eleicao PRIMARY KEY (Pessoa_Id , Eleicao_Titlo),
    CONSTRAINT Fk_Pepessoa FOREIGN KEY (Pessoa_Id)
        REFERENCES Pessoa (Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT Fk_Pemesa FOREIGN KEY (Id_Mesa)
        REFERENCES Mesa_De_Voto (Mesa_ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
	CONSTRAINT Fk_Peeleicao FOREIGN KEY (Eleicao_Titlo)
        REFERENCES Eleicao (Titlo)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE Pessoa_Mesa_De_Voto (
    Pessoa_Id INT NOT NULL PRIMARY KEY,
    Id_Mesa INT NOT NULL,
    ATIVO TINYINT DEFAULT 0 NOT NULL,
    CONSTRAINT Fk_Pessoa_Na_Mesa_De_Voto FOREIGN KEY (Pessoa_Id)
        REFERENCES Pessoa (Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT Fk_Mesa_Na_Pessoa_Mesa_De_Voto FOREIGN KEY (Id_Mesa)
        REFERENCES Mesa_De_Voto (Mesa_Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);