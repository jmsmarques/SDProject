Abrir rmiregistry [port]
	(ex. rmiregistry 7000)

Abrir RMIServer
	Escolher ficheiro de configuracao (ex.RMIConfig.txt)
	
Abrir AdminConsole
	Escolher opcao pretendida consuante as opcoes mostradas na Consola




Abrir SERVIDOR TCP

Começar por abrir o servidor, verificando se ha conexao ao servidor rmi.

Esperar pelas ligacoes dos Clientes 

DESBLOQUEIO DE TERMINAL Clientes:
	Selecionar o numero do terminal em inteiro   (ex. 0)
	Selecionar o ID de eleitor (ex. 123123123)


Abrir CLINTE TCP
	Caso o terminal esteja bloqueado nenhum input será respondido.
	Depois do desbloqueio:
	LOGIN: escrever: type|login;id|user_id;password|user_password(ex.type|login;id|123123123;password|jorge)
	onde user_id = numero de identificacao do user e user_password= password de autenticacao do utilizador
	Depois do Login:
	LOGOUT: escrever :type|logout
	VISUALIZAR ELEICOES disponíveis: escrever type|eleicoes_list;request|asd
	VISUALIZAR LISTAS DE UMA ELEICAO: escrever type|listas_list;eleicao|eleicao_escolhida
	onde eleicao_escolhida= eleicao a listar
	VOTAR: escrever type|vote;eleicao|eleicao_escolhida;vote|lista_escolhida
	onde eleicao_escolhida= nome da eleicao escolhida e lista_escolhida= lista a qual o voto vai ser a favor 
							


