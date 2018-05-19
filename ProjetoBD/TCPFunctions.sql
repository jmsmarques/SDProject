DROP PROCEDURE IF EXISTS person_unlock;
DELIMITER $$
CREATE PROCEDURE person_unlock
    (IN p_ID INT,
    OUT resultado VARCHAR(60)
    )
   
    BEGIN
		DECLARE aux INTEGER;
		DECLARE aux2 CHAR;
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
        START TRANSACTION;
        
        SELECT COUNT(*) INTO aux
        FROM PESSOA a
        WHERE a.ID = p_ID;
        IF aux > 0 THEN
            SELECT ATIVO INTO aux2
            FROM PESSOA
            WHERE p_ID = ID;
            IF aux2 = 'N' THEN
                UPDATE Pessoa e
                SET Ativo = 'Y'
                WHERE p_ID = e.ID;
                SET resultado = 'Operation success';
            ELSE
                SET resultado = 'User already active';
            END IF;
        ELSE
            SET resultado = 'No users with that id';
        END IF;
        
        COMMIT;
    END $$
 DELIMITER ;

DROP PROCEDURE IF EXISTS person_lock; 
DELIMITER $$
CREATE PROCEDURE person_lock
    (ID INTEGER) 
    BEGIN
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
        START TRANSACTION;
        
        UPDATE Pessoa e
		SET Ativo = 'N'
		WHERE ID = e.ID;
        
        COMMIT;
    END $$
DELIMITER ;
    
DROP PROCEDURE IF EXISTS vote;
DELIMITER $$
CREATE PROCEDURE vote
    (IN ID  INTEGER, 
    IN peleicao VARCHAR(30), 
    IN vote VARCHAR(30), 
    IN departamento_mesa VARCHAR(30),
    OUT resultado VARCHAR(30)
    )
    BEGIN
		DECLARE aux INTEGER;
		DECLARE mesa INTEGER;
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
        START TRANSACTION;
        
        SELECT COUNT(*) INTO aux
        FROM PESSOA_ELEICAO a
        WHERE Pessoa_Id = ID AND peleicao = Eleicao_Titlo;
        
        SELECT Mesa_Id INTO mesa
		FROM mesa_de_voto
		WHERE Eleicao_Titlo = peleicao
		AND Departamento_Nome = departamento_mesa; 
                
        
        IF aux > 0 THEN
            SET resultado = 'You already voted in this election';
        ELSE 
            IF vote = 'branco' THEN
                UPDATE Mesa_de_Voto
                    SET brancos = brancos + 1
                    WHERE departamento_nome = departamento_mesa AND eleicao_titlo = peleicao;
                    
            ELSEIF vote = 'nulo' THEN
                UPDATE Mesa_de_Voto
                    SET nulos = nulos + 1
                    WHERE departamento_nome = departamento_mesa AND eleicao_titlo = peleicao;
            ELSE
                UPDATE Votos
                    SET votos = votos + 1
                    WHERE lista_nome = vote AND lista_eleicao = peleicao AND Id_Mesa = mesa;
            END IF;
            
            INSERT pessoa_eleicao 
            (Pessoa_Id, Id_Mesa, Eleicao_Titlo)
            VALUES
            (ID, mesa, peleicao);
            
            SET resultado = 'Thank you for voting';
        END IF;
        
        COMMIT;
    END$$
DELIMITER ;
     

DROP PROCEDURE IF EXISTS authenticate;
DELIMITER $$
CREATE PROCEDURE authenticate
    (IN p_ID int, 
    IN p_password VARCHAR(30),
    OUT resultado VARCHAR(30)
    )     
    BEGIN
		DECLARE aux INTEGER;
		DECLARE aux2 VARCHAR(1);
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
        
        START TRANSACTION;
        
        SELECT COUNT(*) INTO aux
        FROM PESSOA a
        WHERE a.ID = p_ID AND p_password = a.password;
        IF aux > 0 THEN
            SELECT ATIVO INTO aux2
            FROM PESSOA
            WHERE p_ID = ID;
            IF aux2 = 'Y' THEN
                SET resultado = 'Welcome';
            ELSE
                SET resultado = 'Identify at voting table first';
            END IF;
        ELSE
             SET resultado = 'Invalid username or password';
        END IF;
        COMMIT;
    END$$
DELIMITER ;




DROP PROCEDURE IF EXISTS authenticateTable;
DELIMITER $$
CREATE PROCEDURE authenticateTable
    (IN p_ID  int,
    IN p_password VARCHAR(30),
    IN m_eleicao VARCHAR(30),
    IN m_departamento Varchar(30),
    OUT resultado VARCHAR(30)
    ) 
  
	BEGIN
		DECLARE aux integer ;
		DECLARE aux2 VARCHAR(30);
        DECLARE mesa INT;
        DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
        START TRANSACTION;
        
		SELECT COUNT(*) INTO aux
		FROM PESSOA_MESA_DE_VOTO m, PESSOA p
		WHERE p.Id = p_ID AND p.Password = p_password AND m.Pessoa_Id=p.Id ;
        
        SELECT Mesa_Id INTO mesa
        FROM mesa_de_voto
        WHERE Eleicao_Titlo = m_eleicao
        AND Departamento_Nome = m_departamento;
        
        UPDATE PESSOA_MESA_DE_VOTO  SET Ativo=1
        WHERE Pessoa_Id = p_ID AND Id_Mesa = mesa;
        
        
		IF aux > 0 THEN
				UPDATE MESA_DE_VOTO SET Ativo=1
				WHERE Mesa_ID = mesa;
	
				SET resultado = 'Welcome';
           
		ELSE
				SET resultado = 'Invalid username, password or table access';
		END IF;
        COMMIT;
    END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS deauthenticateTable;
DELIMITER $$
CREATE PROCEDURE deauthenticateTable
    (IN p_ID  int,
    IN m_eleicao VARCHAR(30),
    IN m_departamento Varchar(30),
    OUT resultado VARCHAR(30)
    ) 
	BEGIN
		DECLARE aux integer ;
		DECLARE mesa INT;
		DECLARE EXIT handler
		FOR sqlexception, sqlwarning
		BEGIN
			ROLLBACK;
			RESIGNAL;
		END;
        START TRANSACTION;
        
		SELECT Mesa_Id INTO mesa
        FROM mesa_de_voto
        WHERE Eleicao_Titlo = m_eleicao
        AND Departamento_Nome = m_departamento;
        
        UPDATE PESSOA_MESA_DE_VOTO SET ATIVO=0
        WHERE Pessoa_Id = p_ID AND Id_Mesa = mesa;
        
        
        SELECT COUNT(*) INTO aux
		FROM PESSOA_MESA_DE_VOTO 
        WHERE Id_Mesa = mesa AND ATIVO=1; 
        
       
        
		IF aux < 1 THEN
				UPDATE MESA_DE_VOTO SET ATIVO=0
				WHERE Mesa_ID = mesa;
	
				SET resultado = 'Mesa nao ativa';
           
		ELSE
				SET resultado = 'Mesa ainda ativa';
		END IF;
        
        COMMIT;
    END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS listLists;
DELIMITER $$
CREATE PROCEDURE listLists (
IN idt INT,
IN election VARCHAR(30)
)
BEGIN
	SELECT NOME 
	FROM LISTA 
	WHERE ELEICAO_TITLO = election 
	AND TIPO_MEMBROS = (SELECT TIPO FROM PESSOA WHERE ID = idt);
   
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS faceAss;
DELIMITER $$
CREATE PROCEDURE faceAss (
IN idt INT,
IN token VARCHAR(300),
OUT resultado VARCHAR(30)
)
BEGIN
IF((SELECT COUNT(*) FROM pessoa Where facetoken= token )=0)Then
	update pessoa
    set facetoken= token
    where id= idt;
   set resultado= "success";
    else
   set resultado ="fail";
   end if;
END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS faceLog;
DELIMITER $$
CREATE PROCEDURE faceLog (

IN token VARCHAR(300),
 OUT resultado VARCHAR(30)
)
BEGIN

	select id from pessoa
   where facetoken= token;
   SET resultado=(select id from pessoa
   where facetoken= token);
   
   
END $$
DELIMITER ;