SET SQL_SAFE_UPDATES = 0;
DELETE FROM Votos;
DELETE FROM Pessoa_Eleicao;
DELETE FROM Lista;
DELETE FROM Mesa_de_Voto;
DELETE FROM Departamento;
DELETE FROM Faculdade;
DELETE FROM Eleicao;
DELETE FROM Pessoa;

CALL insert_In_Pessoa(123123123, 'Jorge', 'jorge', 966666666, 'rua falsa', '2018/06/01 12:00:00', 'Estudante');
CALL insert_In_Pessoa(123123124, 'Quinaz', 'jorge', 966666666, 'rua falsa', '2018/06/01 12:00:00', 'Estudante');
CALL insert_In_Pessoa(123123125, 'Francisco', 'jorge', 966666666, 'rua falsa', '2018/06/01 12:00:00', 'Estudante');
CALL insert_In_Eleicao('eleicao1', '2018/06/01 12:00:00', '2018/08/01 12:00:00', 'Conselho Geral', 'Estudante', 'ola2');
CALL insert_In_Eleicao('eleicao2', '2018/06/01 12:00:00', '2018/08/01 12:00:00', 'Conselho Geral', 'Estudante', 'ola1');
CALL insert_In_Lista('lista1', 'Estudante', 'eleicao1');
CALL insert_In_Lista('lista2', 'Estudante', 'eleicao1');
CALL insert_In_Lista('lista3', 'Estudante', 'eleicao1');
CALL insert_In_Lista('lista4', 'Estudante', 'eleicao2');
CALL insert_In_Lista('lista5', 'Docentes', 'eleicao2');
CALL insert_In_Faculdade('FCTUC');
CALL insert_In_Faculdade('Consola Administracao');
CALL insert_In_Faculdade('Web');
CALL insert_In_Departamento('Consola Administracao', 'Consola Administracao');
CALL insert_In_Departamento('Web', 'Web');
CALL insert_In_Departamento('DEI', 'FCTUC');
CALL insert_In_Departamento('DEEC', 'FCTUC');
CALL cria_mesa_especial('Consola Administracao');
CALL cria_mesa_especial('Web');
CALL insert_In_MesaDeVoto('DEI', 'eleicao1');
CALL insert_In_MesaDeVoto('DEEC', 'eleicao1');

UPDATE mesa_de_voto
SET Ativo = 1
WHERE Departamento_Nome = 'Consola Administracao'
OR Departamento_Nome = 'Web';