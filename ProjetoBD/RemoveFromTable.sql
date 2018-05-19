#PROCEDURE para remover eleicao
DROP PROCEDURE IF EXISTS removeEleicao;
DELIMITER $$
CREATE PROCEDURE removeEleicao
	(IN eleicao_titlo VARCHAR(30))
    BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
		START TRANSACTION;
        
		DELETE FROM Eleicao
        WHERE Titlo = eleicao_titlo;
        
        COMMIT;
	END $$
DELIMITER ;

#PROCEDURE para remover pessoa
DROP PROCEDURE IF EXISTS removePerson;
DELIMITER $$
CREATE PROCEDURE removePerson
	(IN P_ID INT(9))
    BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
		START TRANSACTION;
		
		DELETE FROM pessoa
        WHERE ID = P_ID;
        
        COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para remover da tabela departamento
DROP PROCEDURE IF EXISTS removeDepartamento;
DELIMITER $$
CREATE PROCEDURE removeDepartamento
    (IN nome_departamento VARCHAR(60))
    BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
		START TRANSACTION;
		
        DELETE FROM departamento
        WHERE nome = nome_departamento;
        
        COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para remover da tabela faculdade
DROP PROCEDURE IF EXISTS removeFaculdade;
DELIMITER $$
CREATE PROCEDURE removeFaculdade
    (IN nome_faculdade VARCHAR(60))
    BEGIN  
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
		START TRANSACTION;
        
        DELETE FROM faculdade
        WHERE nome = nome_faculdade;
        
        COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para remover da tabela lista
DROP PROCEDURE IF EXISTS removeLista;
DELIMITER $$
CREATE PROCEDURE removeLista
    (IN nome_lista VARCHAR(60), IN titlo_eleicao VARCHAR(30))
    BEGIN  
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
		START TRANSACTION;
        
        DELETE FROM Lista
        WHERE Nome = nome_lista
        AND Eleicao_Titlo =  titlo_eleicao;
        
        COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para remover pessoa da lista
DROP PROCEDURE IF EXISTS removePessoaDaLista;
DELIMITER $$
CREATE PROCEDURE removePessoaDaLista
    (IN p_ID INT(9), IN nome_lista VARCHAR(60), IN eleicao_lista VARCHAR(60))
    BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
		START TRANSACTION;
        
        DELETE FROM PESSOA_LISTA
        WHERE Pessoa_ID = p_ID 
        AND LISTA_Nome = nome_lista
        AND Lista_Eleicao = eleicao_lista;
        
        COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para remover da tabela mesa de voto
DROP PROCEDURE IF EXISTS removeMesaVoto;
DELIMITER $$
CREATE PROCEDURE removeMesaVoto
    (IN nome_departamento VARCHAR(60), IN titlo_eleicao VARCHAR(30))
    BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
		START TRANSACTION;
        
        DELETE FROM MESA_DE_VOTO
        WHERE Departamento_Nome = nome_departamento
        AND Eleicao_Titlo = titlo_eleicao;
        
        COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para remover da tabela pessoa_mesa_de_voto
DROP PROCEDURE IF EXISTS removePessoaMesaDeVoto;
DELIMITER $$
CREATE PROCEDURE removePessoaMesaDeVoto
	(IN p_id INT)
    BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
		START TRANSACTION;
		
		DELETE FROM Pessoa_Mesa_De_Voto
        WHERE Pessoa_Id = p_id;
        
        COMMIT;
	END $$
DELIMITER ;