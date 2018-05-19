#PROCEDURE para mudar propriedades de uma pessoa
DROP PROCEDURE IF EXISTS change_person;
DELIMITER $$
CREATE PROCEDURE change_person
	(IN p_id 			 INT,
    IN novo_nome          VARCHAR(60),
    IN nova_password	     VARCHAR(60),
    IN novo_telemovel     INT,
    IN nova_morada        VARCHAR(60),
    IN nova_validade    VARCHAR(60),
    IN novo_tipo         VARCHAR(60))
	BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
        START TRANSACTION;
        
		UPDATE Pessoa
        SET Nome = novo_nome,
        Password = nova_password,
        Telemovel = novo_telemovel,
        Morada = nova_morada,
        Validade_Cc = nova_validade,
        Tipo = novo_tipo
        WHERE ID = p_id;
        
        COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para mudar propriedades de uma eleicao
DROP PROCEDURE IF EXISTS change_eleicao;
DELIMITER $$
CREATE PROCEDURE change_eleicao
    (IN p_eleicao VARCHAR(30), IN p_data_inicio VARCHAR(30), IN p_data_fim VARCHAR(30), 
    IN novo_tipo VARCHAR(60), IN novo_tipo_membros VARCHAR(60), IN nova_descricao_eleicao VARCHAR(30))
    BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
		START TRANSACTION;
    
		UPDATE ELEICAO
		SET DATA_INICIO = STR_TO_DATE(p_data_inicio, '%Y/%m/%d %H:%i:%s'),
		DATA_FIM = STR_TO_DATE(p_data_fim, '%Y/%m/%d %H:%i:%s'),
		TIPO = novo_tipo,
		TIPO_MEMBROS = novo_tipo_membros,
		DESCRICAO = nova_descricao_eleicao
		WHERE TITLO = p_eleicao;
       
		COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para mudar propriedades de um departamento
DROP PROCEDURE IF EXISTS change_departamento;
DELIMITER $$
CREATE PROCEDURE change_departamento
    (IN p_nome VARCHAR(60), IN novo_nome VARCHAR(60), IN novo_nome_faculdade VARCHAR(60))
    BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
        START TRANSACTION;
        
        UPDATE DEPARTAMENTO
        SET NOME = novo_nome,
        FACULDADE_NOME = novo_nome_faculdade
        WHERE NOME = p_nome;
        
        COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para mudar nome de uma faculdade
DROP PROCEDURE IF EXISTS change_faculdade_name;
DELIMITER $$
CREATE PROCEDURE change_faculdade_name
    (IN p_nome VARCHAR(60), IN novo_nome VARCHAR(60))
    BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
        START TRANSACTION;
        
        UPDATE FACULDADE
        SET NOME = novo_nome
        WHERE NOME = p_nome;
        
        COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para ir buscar a info do votador (local e data)
DROP PROCEDURE IF EXISTS get_info_votador;
DELIMITER $$
CREATE PROCEDURE get_info_votador
	(IN p_id INT(9), IN eleicao_titlo VARCHAR(60))
    BEGIN
		SELECT Departamento_Nome, Data_Voto
        FROM Pessoa_Eleicao, Mesa_de_Voto
        WHERE Pessoa_Id = p_id
        AND Mesa_Id = Id_Mesa;
	END $$
DELIMITER ;
#PROCEDURE para ir buscar os resultados de uma eleicao por mesa
DROP PROCEDURE IF EXISTS resultados_nulos_eleicao_por_mesa;
DELIMITER $$
CREATE PROCEDURE resultados_nulos_eleicao_por_mesa
	(IN eleicao VARCHAR(60))
    BEGIN
		SELECT Departamento_Nome, Nulos, Brancos
        FROM mesa_de_voto
        WHERE Eleicao_Titlo = eleicao;
    END $$
DELIMITER ;
#PROCEDURE para ir buscar os resultados de uma eleicao por mesa
DROP PROCEDURE IF EXISTS resultados_eleicao_por_mesa;
DELIMITER $$
CREATE PROCEDURE resultados_eleicao_por_mesa
	(IN eleicao VARCHAR(60))
    BEGIN
        SELECT Departamento_Nome, Lista_Nome, Votos
        FROM votos, mesa_de_voto
        WHERE Lista_Eleicao = eleicao
        AND Mesa_ID = Id_Mesa
        ORDER BY Departamento_Nome;
    END $$
DELIMITER ;
#PROCEDURE para obter os resultados nulos de uma eleicao
DROP PROCEDURE IF EXISTS resultados_nulos_eleicao;
DELIMITER $$
CREATE PROCEDURE resultados_nulos_eleicao
	(IN eleicao VARCHAR(60))
    BEGIN
		SELECT SUM(Nulos) AS Nulos, SUM(Brancos) AS Brancos
        FROM mesa_de_voto
        WHERE Eleicao_Titlo = eleicao;
    END $$
DELIMITER ;
#PROCEDURE para obter os resultados de uma eleicao
DROP PROCEDURE IF EXISTS resultados_eleicao;
DELIMITER $$
CREATE PROCEDURE resultados_eleicao
	(IN eleicao VARCHAR(60))
    BEGIN
        SELECT Lista_Nome, SUM(Votos) AS Votos
        FROM votos
        WHERE Lista_eleicao = eleicao
        GROUP BY Lista_Nome;
    END $$
DELIMITER ;
#PROCEDURE para obter o nr de eleitores que votaram em cada mesa
DROP PROCEDURE IF EXISTS nr_votos_por_mesa;
DELIMITER $$
CREATE PROCEDURE nr_votos_por_mesa
	(IN eleicao VARCHAR(60))
	BEGIN
		SELECT Departamento_Nome, SUM(votos) + SUM(nulos) + SUM(brancos) AS votos
        FROM votos, Mesa_De_Voto
        WHERE Mesa_ID = Id_Mesa
        AND Lista_Eleicao = eleicao 
        GROUP BY Departamento_Nome;
	END $$
DELIMITER ;
#PROCEDURE para fechar uma eleicao
DROP PROCEDURE IF EXISTS close_election;
DELIMITER $$
CREATE PROCEDURE close_election()
    BEGIN
		DECLARE exit HANDLER
		FOR SQLEXCEPTION, SQLWARNING
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
		START TRANSACTION;
        
        UPDATE ELEICAO
        SET ATIVO = 'N'
        WHERE CURRENT_TIMESTAMP > DATA_FIM OR CURRENT_TIMESTAMP < DATA_INICIO;
        
        UPDATE ELEICAO
        SET ATIVO = 'Y'
        WHERE CURRENT_TIMESTAMP <= DATA_FIM AND CURRENT_TIMESTAMP >= DATA_INICIO;
        
        COMMIT;
    END $$
DELIMITER ;
#PROCEDURE para voter antes do tempo
DROP PROCEDURE IF EXISTS vote_in_advance;
DELIMITER $$
CREATE PROCEDURE vote_in_advance
	(IN ID_Pessoa INT, 
    IN P_Eleicao VARCHAR(60), 
    IN voto VARCHAR(60),
    OUT resultado VARCHAR(60)
    )
	BEGIN
		DECLARE mesa INT;
		DECLARE aux INT;
		DECLARE exit HANDLER
		FOR SQLEXCEPTION, SQLWARNING
			BEGIN
				ROLLBACK;
				RESIGNAL;
			END;
		START TRANSACTION;
        
		SELECT Mesa_Id INTO mesa
		FROM mesa_de_voto
		WHERE Departamento_Nome = 'Consola Administracao';
		
		SELECT COUNT(*) INTO aux
		FROM PESSOA_ELEICAO a
		WHERE Pessoa_Id = ID_pessoa AND p_eleicao = Eleicao_Titlo;
			
		IF aux > 0 THEN
			SET resultado = 'You already voted in this election';
		ELSE 
			IF voto = 'branco' THEN
				UPDATE Mesa_de_Voto
				SET brancos = brancos + 1
				WHERE departamento_nome = 'Consola Administracao' AND eleicao_titlo = p_eleicao;
					
			ELSEIF voto = 'nulo' THEN
				UPDATE Mesa_de_Voto
				SET nulos = nulos + 1
				WHERE departamento_nome = 'Consola Administracao' AND eleicao_titlo = p_eleicao;
			ELSE
				UPDATE Votos
				SET votos = votos + 1
				WHERE lista_nome = voto AND lista_eleicao = p_eleicao AND Id_Mesa = mesa;
			END IF;
            
            INSERT pessoa_eleicao 
            (Pessoa_Id, Id_Mesa, Eleicao_Titlo)
            VALUES
            (ID_Pessoa, mesa, p_eleicao);
            
            SET resultado = 'Thank you for voting';
		END IF;
        
        COMMIT;
	END $$
DELIMITER ;

#PROCEDURE para listar as listas conforme a eleicao pedida
DROP PROCEDURE IF EXISTS list_lists
DELIMITER $$
CREATE PROCEDURE list_lists
	(IN ID_Pessoa INT,
    IN eleicao VARCHAR(60)
    )
    BEGIN
		SELECT Nome
        FROM lista
        WHERE Eleicao_Titlo = eleicao
        AND Tipo_Membros = (SELECT Tipo
							FROM pessoa	
                            WHERE ID_Pessoa = Id);
    END $$
DELIMITER ;

#PROCEDURE para listar todas as eleicoes
DROP PROCEDURE IF EXISTS list_eleicoes
DELIMITER $$
CREATE PROCEDURE list_eleicoes
	(IN ID_Pessoa INT)
    BEGIN
		SELECT Titlo
        FROM eleicao
        WHERE current_timestamp() < data_inicio
        AND Tipo_Membros = (SELECT Tipo
							FROM pessoa	
                            WHERE ID_Pessoa = Id);
    END $$
DELIMITER ;

#PROCEDURE para mostrar detalhes de uma eleicao
DROP PROCEDURE IF EXISTS detalhes_eleicoes;
DELIMITER $$
CREATE PROCEDURE detalhes_eleicoes
	(IN eleicao_titlo VARCHAR(60))
	BEGIN
		SELECT Titlo, Data_Inicio, Data_Fim, Tipo, Tipo_Membros, Descricao, Ativo
        FROM eleicao 
        WHERE Titlo = eleicao_titlo;
    END $$
DELIMITER ;

#PROCEDURE para mostrar detalhes de uma eleicao
DROP PROCEDURE IF EXISTS detalhes_eleicoes_listas;
DELIMITER $$
CREATE PROCEDURE detalhes_eleicoes_listas
	(IN eleicao VARCHAR(60))
	BEGIN
		SELECT Lista_Nome, Pessoa_Id
        FROM pessoa_lista
        WHERE Lista_Eleicao = eleicao
        ORDER BY Lista_Nome;
    END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS vote_in_advance_web;
DELIMITER $$
CREATE PROCEDURE vote_in_advance_web
	(IN ID_Pessoa INT, 
    IN P_Eleicao VARCHAR(60), 
    IN voto VARCHAR(60),
    OUT resultado VARCHAR(60)
    )
	BEGIN
		DECLARE mesa INT;
		DECLARE aux INT;
		DECLARE exit HANDLER
		FOR SQLEXCEPTION, SQLWARNING
			BEGIN
				ROLLBACK;
				RESIGNAL;
			END;
		START TRANSACTION;
        
		SELECT Mesa_Id INTO mesa
		FROM mesa_de_voto
		WHERE Departamento_Nome = 'Web';
		
		SELECT COUNT(*) INTO aux
		FROM PESSOA_ELEICAO a
		WHERE Pessoa_Id = ID_pessoa AND p_eleicao = Eleicao_Titlo;
			
		IF aux > 0 THEN
			SET resultado = 'You already voted in this election';
		ELSE 
			IF voto = 'branco' THEN
				UPDATE Mesa_de_Voto
				SET brancos = brancos + 1
				WHERE departamento_nome = 'Consola Administracao' AND eleicao_titlo = p_eleicao;
					
			ELSEIF voto = 'nulo' THEN
				UPDATE Mesa_de_Voto
				SET nulos = nulos + 1
				WHERE departamento_nome = 'Consola Administracao' AND eleicao_titlo = p_eleicao;
			ELSE
				UPDATE Votos
				SET votos = votos + 1
				WHERE lista_nome = voto AND lista_eleicao = p_eleicao AND Id_Mesa = mesa;
			END IF;
            
            INSERT pessoa_eleicao 
            (Pessoa_Id, Id_Mesa, Eleicao_Titlo)
            VALUES
            (ID_Pessoa, mesa, p_eleicao);
            
            SET resultado = 'Thank you for voting';
		END IF;
        
        COMMIT;
	END $$
DELIMITER ;

#PROCEDURE para ir buscar nr de eleitores que votaram numa eleicao
DROP PROCEDURE IF EXISTS nr_votos_eleicao;
DELIMITER $$
CREATE PROCEDURE nr_votos_eleicao
	(IN eleicao VARCHAR(60))
    BEGIN
		SELECT COUNT(*) AS votos
        FROM pessoa_eleicao
        WHERE Eleicao_titlo = eleicao;
    END $$
DELIMITER ;

#PROCEDURE para ir buscar mesas ativas
DROP PROCEDURE IF EXISTS mesas_ativas;
DELIMITER $$
CREATE PROCEDURE mesas_ativas()
	BEGIN 
		SELECT Departamento_Nome, Eleicao_titlo
        FROM mesa_de_voto
        WHERE Ativo = 1;
	END $$
DELIMITER ;

#PROCEDURE para ir buscar mesas inativas
DROP PROCEDURE IF EXISTS mesas_inativas;
DELIMITER $$
CREATE PROCEDURE mesas_inativas()
	BEGIN 
		SELECT Departamento_Nome, Eleicao_titlo
        FROM mesa_de_voto
        WHERE Ativo = 0;
	END $$
DELIMITER ;

#PROCEDURE para ir buscar mesas inativas
DROP PROCEDURE IF EXISTS utilizadores_online;
DELIMITER $$
CREATE PROCEDURE utilizadores_online()
	BEGIN 
		SELECT Id, Nome, Tipo
        FROM pessoa
        WHERE Ativo = 'Y';
	END $$
DELIMITER ;