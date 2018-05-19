/* Entrar com,
   Login: system
   Pwd:   <passord>
   e correr os seguintes comandos.
*/

/* Cria utilizador */

CREATE USER bdProj IDENTIFIED BY bdProj;

/* Atribuição de previlégio */
GRANT "CONNECT" TO bdProj;
GRANT "RESOURCE" TO bdProj;
GRANT UNLIMITED TABLESPACE TO bdProj;
GRANT ALTER ANY CLUSTER TO bdProj;
GRANT ALTER SESSION TO bdProj;
GRANT CREATE PROCEDURE TO bdProj;
GRANT CREATE SEQUENCE TO bdProj;
GRANT CREATE TABLE TO bdProj;
GRANT CREATE TRIGGER TO bdProj;
GRANT CREATE VIEW TO bdProj;
ALTER USER bdProj DEFAULT ROLE ALL;

COMMIT;


/* Agora devem sair da conta, e 
   entrar como,
   Login: bd
   Pwd:   bd
   e correr os outros dois scripts: 
   createTables.sql e insertData.sql.
*/
