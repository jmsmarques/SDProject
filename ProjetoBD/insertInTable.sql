SET autocommit = 1;
#PROCEDURE to Insert data  pessoa table
DROP PROCEDURE IF EXISTS Insert_In_Pessoa;
DELIMITER $$
CREATE PROCEDURE Insert_In_Pessoa (
    IN Id 			 INT,
    IN Nome          VARCHAR(60),
    IN Password	     VARCHAR(60),
    IN Telemovel     INT,
    IN Morada        VARCHAR(60),
    IN Validadecc    VARCHAR(60),
    IN Tipop         VARCHAR(60)
)
BEGIN
	DECLARE EXIT handler
    FOR sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
        RESIGNAL;
	END;
	START TRANSACTION;
    
    INSERT INTO Pessoa (
        Id,
        Nome,
        Password,
        Telemovel,
        Morada,
        Validade_cc,
        Tipo
    ) VALUES (
        Id,
        Nome,
        Password,
        Telemovel,
        Morada,
        STR_TO_DATE(Validadecc, '%Y/%m/%d %H:%i:%s'),
        Tipop
    );
    
    COMMIT;
END $$
DELIMITER ;

#PROCEDURE to insert data in Faculdade table
DROP PROCEDURE IF EXISTS Insert_In_Faculdade;
DELIMITER $$
CREATE PROCEDURE Insert_In_Faculdade (IN Nome VARCHAR(30))
BEGIN
	DECLARE EXIT handler
    FOR sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
        RESIGNAL;
	END;
    
	START TRANSACTION;
    
    INSERT INTO Faculdade VALUES (Nome);
    
    COMMIT;
END $$
DELIMITER ;
   
#PROCEDURE to insert data in Departamento table
DROP PROCEDURE IF EXISTS Insert_In_Departamento;
DELIMITER $$
CREATE PROCEDURE Insert_In_Departamento (
    IN Nome         VARCHAR(60),
    IN Faculdade    VARCHAR(60)
)
    
BEGIN
	DECLARE EXIT handler
    FOR sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
        RESIGNAL;
	END;
    
	START TRANSACTION;
    
    INSERT INTO Departamento 
    VALUES ( Nome, Faculdade );
    
    COMMIT;
END $$
DELIMITER ;
#PROCEDURE to insert data in Lista table
DROP PROCEDURE IF EXISTS Insert_In_Lista;
DELIMITER $$
CREATE PROCEDURE Insert_In_Lista (
    IN p_Nome          VARCHAR(60),
    IN Tipo_Membros    VARCHAR(60),
    IN Eleicao         VARCHAR(60)
)
    
BEGIN
	DECLARE EXIT handler
    FOR sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
        RESIGNAL;
	END;
	START TRANSACTION;
    
    INSERT INTO Lista VALUES (
        p_Nome,
        Tipo_Membros,
        Eleicao
    );
    
    INSERT INTO Votos (
        Id_Mesa,
        Lista_Nome,
        Lista_Eleicao
    ) SELECT Mesa_Id, p_Nome, Eleicao
    FROM Mesa_de_Voto
    WHERE Eleicao_Titlo = Eleicao;
    
    INSERT INTO Votos (
		Id_Mesa,
        Lista_Nome,
        Lista_Eleicao)
	SELECT Mesa_Id, p_Nome, Eleicao
    FROM mesa_de_voto
    WHERE Departamento_Nome = 'Consola Administracao';
    
    INSERT INTO Votos (
		Id_Mesa,
        Lista_Nome,
        Lista_Eleicao)
	SELECT Mesa_Id, p_Nome, Eleicao
    FROM mesa_de_voto
    WHERE Departamento_Nome = 'Web';
    COMMIT;
END $$
DELIMITER ;
#PROCEDURE to insert data in Eleicao table
DROP PROCEDURE IF EXISTS Insert_In_Eleicao;
DELIMITER $$
CREATE PROCEDURE Insert_In_Eleicao (
    IN Titlop          VARCHAR(60),
    IN Data_Inicio     VARCHAR(60),
    IN Data_Fim        VARCHAR(60),
    IN Tipo            VARCHAR(60),
    IN Tipo_Membros    VARCHAR(60),
    IN Descricao       VARCHAR(60)
)
    
BEGIN
	DECLARE EXIT handler
    FOR sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
        RESIGNAL;
	END;
	START TRANSACTION;
    
    INSERT INTO Eleicao (
        Titlo,
        Data_Inicio,
        Data_Fim,
        Tipo,
        Tipo_Membros,
        Descricao
    ) VALUES (
        Titlop,
        STR_TO_DATE(Data_Inicio, '%Y/%m/%d %H:%i:%s'),
        STR_TO_DATE(Data_Fim, '%Y/%m/%d %H:%i:%s'),
        Tipo,
        Tipo_Membros,
        Descricao
    );
	COMMIT;
END $$
DELIMITER ;
#PROCEDURE to insert data in Mesa de Voto table
DROP PROCEDURE IF EXISTS Insert_In_Mesadevoto;
DELIMITER $$
CREATE PROCEDURE Insert_In_Mesadevoto (
    IN Departamento    VARCHAR(60),
    IN P_Eleicao       VARCHAR(60)
)
    
BEGIN
	DECLARE mesa INT;
	DECLARE EXIT handler
    FOR sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
        RESIGNAL;
	END;
    
	START TRANSACTION;
    INSERT INTO Mesa_De_Voto ( Departamento_Nome, Eleicao_Titlo ) 
    VALUES ( Departamento, P_Eleicao );
	
    SELECT Mesa_Id INTO mesa
    FROM mesa_de_voto
    WHERE P_Eleicao = Eleicao_Titlo
    AND Departamento = Departamento_Nome;
    
    INSERT INTO Votos (
        Id_Mesa,
        Lista_Nome,
        Lista_Eleicao
    ) SELECT mesa, Nome, Eleicao_Titlo
    FROM Lista
    WHERE P_Eleicao = Eleicao_Titlo;
    COMMIT;
END $$
DELIMITER ; 
#PROCEDURE to insert data in Pessoa Eleicao table
DROP PROCEDURE IF EXISTS Insert_In_Pessoaeleicao;
DELIMITER $$
CREATE PROCEDURE Insert_In_Pessoaeleicao (
    IN Pessoa    INT,
    IN Departamento_Mesa VARCHAR(60),
    IN Eleicao_Mesa VARCHAR(60)
)
    
BEGIN
	DECLARE EXIT handler
    FOR sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
        RESIGNAL;
	END;
	START TRANSACTION;
     
    INSERT INTO Pessoa_Eleicao 
    ( Pessoa_Id, Id_Mesa ) 
    SELECT Pessoa, Mesa_Id
    FROM Mesa_de_Voto
    WHERE Departamento_Nome = Departamento_Mesa 
    AND Eleicao_Titlo = Eleicao_Mesa;
    
	COMMIT;
END $$
DELIMITER ;
#PROCEDURE to insert data in Pessoa Lista table
DROP PROCEDURE IF EXISTS Insert_In_PessoaLista;
DELIMITER $$
CREATE PROCEDURE Insert_In_PessoaLista (
    IN Pessoa    INT,
    IN Lista     VARCHAR(60),
    IN Eleicao_Lista  VARCHAR(60)
)
    
BEGIN
	DECLARE EXIT handler
    FOR sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
        RESIGNAL;
	END;
	START TRANSACTION;
    
    INSERT INTO Pessoa_Lista 
    VALUES ( Pessoa, Lista, Eleicao_Lista );
    
    COMMIT;
END $$
DELIMITER ;

#PROCEDURE to insert date in Pessoa_Mesa_De_Voto
DROP PROCEDURE IF EXISTS Insert_In_Pessoa_Mesa;
DELIMITER $$
CREATE PROCEDURE Insert_In_Pessoa_Mesa (
	IN ID INT,
    IN P_Mesa_Departamento VARCHAR(60),
    IN P_Mesa_Eleicao VARCHAR(60),
    OUT resultado VARCHAR(60)
) 
BEGIN
	DECLARE nr_pessoa INT;
    DECLARE pessoas_na_mesa INT;
    DECLARE mesa INT;
    DECLARE EXIT handler
    FOR sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
        RESIGNAL;
	END;
    START TRANSACTION;
    
    SELECT mesa_ID INTO mesa
	FROM mesa_de_voto
    WHERE Departamento_nome = P_Mesa_Departamento
    AND Eleicao_Titlo = P_Mesa_Eleicao;
    
    SELECT COUNT(*) INTO nr_pessoa
    FROM pessoa_mesa_de_voto
    WHERE PESSOA_ID = ID;
    
    IF nr_pessoa < 1 THEN
		SELECT COUNT(*) INTO pessoas_na_mesa
        FROM pessoa_mesa_de_voto
        WHERE mesa = Id_Mesa; 
		IF pessoas_na_mesa < 4 THEN
			INSERT INTO Pessoa_Mesa_De_Voto 
            (Pessoa_Id, Id_Mesa)
            SELECT ID, Mesa_Id
			FROM Mesa_de_voto
			WHERE Departamento_Nome = P_Mesa_Departamento
            AND Eleicao_Titlo = P_Mesa_Eleicao;
            SET resultado = 'Operation success';
		ELSE
			SET resultado = 'Table already has 3 people';
		END IF;
	ELSE
		SET resultado = 'Person already has a table';
	END IF;
    
    COMMIT;
END $$
DELIMITER ;
#PROCEDURE para inserir consola de administraçao ou web nas mesas
DROP PROCEDURE IF EXISTS cria_mesa_especial;
DELIMITER $$
CREATE PROCEDURE cria_mesa_especial
	(IN nome_departamento VARCHAR(60))
BEGIN
	DECLARE mesa INT;
    DECLARE EXIT handler
    FOR sqlexception, sqlwarning
    BEGIN
		ROLLBACK;
        RESIGNAL;
	END;
	START TRANSACTION;
    
    INSERT INTO Mesa_De_Voto ( Departamento_Nome, Eleicao_Titlo ) 
    VALUES ( nome_departamento, NULL);
	
    SELECT Mesa_Id INTO mesa
    FROM mesa_de_voto
    WHERE nome_departamento = Departamento_Nome;
    
    INSERT INTO Votos (
        Id_Mesa,
        Lista_Nome,
        Lista_Eleicao
    ) SELECT mesa, Nome, Eleicao_Titlo
    FROM Lista;
	COMMIT;
END $$
DELIMITER ; 