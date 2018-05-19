 package tcp;
    import java.net.*;
    import java.io.*;
    import java.rmi.*;
    import java.rmi.NotBoundException;
    import java.rmi.registry.LocateRegistry;
    import java.util.*;

    import rmi.VoterRMIInterface;

public class TCPServer{
	private static String hostname = "localhost"; //variable for hostname of the host who hostes the server
    private static int serverPort = 6000; //variable for port number of tcp server with default value
	private static int rmiPort = 7000; //variable for port number of rmi server with default value
    private static int timeout = 7500; //variable to keep the time it takes for secondary server to try to replace the primary after stoping receiving pings
	private static String rmiName = "porto"; //variable for reference name of rmi server
	private static String fileName = "TCPConfig.txt"; //variable with the name of the file with the server configs
	
    public static void main(String args[]){
        fileConfig(); //Loads the servers configurations from config file
        int number=0; //variable for number of client connected to this tcp server with defautl value
        Utilizadores users= new Utilizadores();
        @SuppressWarnings("resource")
		Scanner input=new Scanner(System.in);
        System.out.println("Departamento: ");
        String dep=input.nextLine();
        users.setDep(dep);
        System.out.println("Eleicao: ");
        String eleicao=input.nextLine();
        users.setElection(eleicao);

        @SuppressWarnings("unused")
		boolean online=false;





        try{
            //Setup RMI
            VoterRMIInterface server = (VoterRMIInterface) LocateRegistry.getRegistry(hostname, rmiPort).lookup(rmiName);
            users.setServer(server);
            new findRMI(users,hostname,rmiName,rmiPort);
            //Fim Setup RMI
            while(true){
                System.out.println("----------------LOGIN----------------");
                System.out.print("ID: ");
                String aux=input.nextLine();
                int id=Integer.parseInt(aux);
                users.setIdTable(id);
                System.out.println("ID ->>>"+users.getIdTable());
                System.out.print("Password: ");
                String password=input.nextLine();
                String resp;
                try {
                    if ((resp = users.getServer().authenticateVoterInTable(id, password,users.getElection(),users.getDep())) != null) {     /////////////////////////    FALTA ALTERAR PARA VOTERTABLE

                        //RMI PARA VALIDAR
                        //System.out.println(no.getId()+" ------ "+no.getPassword());
                        System.out.println(resp);
                        ///
                    }
                } catch (RemoteException e1) {
                    resp = handleRemoteExceptionAutenticateVoterTable(id,password,users);

                }
                if (resp.equals("Welcome")) {
                    //FEZ LOGIN
                    // no.setPassword(message2[1]);

                    break;


                } else if (resp.equals("operation fail")) {


                    System.out.println("msg|Autenticacao Incorreta;");
                } else {
                    System.out.println("Impossivel ligar ao RMI");
                }

            }

            System.out.println("A Escuta no Porto " + serverPort);
            @SuppressWarnings("resource")
			ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("LISTEN SOCKET="+listenSocket);
            new ConnectionManager(users,timeout);
            while(true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                users.listAdd(clientSocket);
                System.out.println("CLIENT_SOCKET (created at accept())="+clientSocket);
                number ++;

                new Connection(clientSocket, number,server,users,timeout);
            }
        }catch(IOException e) {
            System.out.println("Listen:" + e.getMessage());
        }catch(NotBoundException e) { //execao para rmi
            System.out.println("RMI:" + e.getMessage());
        }


    }
    private static String handleRemoteExceptionAutenticateVoterTable(int id, String password,Utilizadores users) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                //Adicinonar string
                String str;
                str = users.getServer().authenticateVoterInTable(id, password, users.getElection(),users.getDep());
                return str;
            } catch (InterruptedException e1) {
                System.out.println("Error on sleep: " + e1);
                break;
            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection, retrying to connect");

                }
            }
        }
        return "fail";
    }
    
    /**
	 * Loads the servers configurations from config file
	 */
    private static void fileConfig() {
    	//Start reading from config file
    	try {
    		BufferedReader fileRead = new BufferedReader(new FileReader(fileName));
    		String parts[]; //variable used to split the string read from the config file
    		String read; //variable to store the string read from the config file
    		//read file and load values
    		while((read = fileRead.readLine()) != null) {
    			parts = read.split("="); //split the string read on the = sign
    			if(parts[0].equals("rmiName")) {
    				rmiName = parts[1];
    			}
    			else if(parts[0].equals("rmiPort")) {
    				rmiPort = Integer.parseInt(parts[1]);
    			}
    			else if(parts[0].equals("serverPort")) {
    				serverPort = Integer.parseInt(parts[1]);
    			}
    			else if(parts[0].equals("hostname")) {
					hostname = parts[1];
				}
                else if(parts[0].equals("timeout")) {
                    timeout = Integer.parseInt(parts[1]);
                }

    			else {
    				System.out.println("File is not correctly written.\nCreating a new file with default values");
    				fileRead.close();
    				throw new FileNotFoundException();
    			}
    		}
    		fileRead.close();

    	} catch (FileNotFoundException e) {//if file doesnt exist or cant be opened
    		System.out.println("Configuration file non existant.\nNew one created with default values");
    		try {
    			//create new file and fill it with default values
    			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
    			writer.println("hostname=localhost");
    			writer.println("serverPort=6000");
    			writer.println("rmiPort=7000");
    			writer.println("rmiName=porto");
                writer.println("timeout=7500");
    			writer.close();
    		} catch (Exception e1) {
    			System.out.println("Could not create new file, default values still apply");
    		}
    	} catch (IOException e) {
    		System.out.println("Error reading from file, setting values to default and continuing");
    	}
    	//End of reading from config file
    }
}

class ConnectionManager extends Thread{
    Utilizadores users;
    int timeout;
    Scanner input=new Scanner(System.in);
    public ConnectionManager(Utilizadores users,int timeout) {
        this.timeout=timeout;
        this.users = users;
        this.start();
    }
    public void run(){
        int n;
        String id;


        //THREAD MANAGER
        while(true) {
            String resp="";

                System.out.println("------>Treads actuais:");
                users.listPrint();
                System.out.print("------>Thread a desbloquear: (99 para sair) \n------>");
                n = input.nextInt();
                if(n==99){
                    System.out.println(users.getIdTable()+"\n"+users.getElection()+"\n"+users.getDep());
                    System.out.println("LOGOFF TABLE");
                    try {
                        resp = users.getServer().deauthenticateVoterInTable(users.getIdTable(),users.getElection(),users.getDep());

                    }catch(RemoteException e){
                        resp= handleRemoteExceptiondeauthenticateVoterInTable(users);
                    }
                    System.out.println(resp);
                    System.exit(0);

                }

                System.out.println("------> ID de Vontante: ");
                id = input.next();
                resp="";

            try {
                resp = users.getServer().identifyVoter(Integer.parseInt(id));
            }catch(RemoteException e){
                resp= handleRemoteExceptionidentifiyVoter(Integer.parseInt(id));
            }
                System.out.println(resp);
            if(resp.equals("Operation success")) {
                users.lista.get(n).stateUp();

                users.lista.get(n).setId(Integer.parseInt(id));
                System.out.println(users.lista.get(n).getId());

                System.out.println("=======>Thread " + n + " desbloqueada!");
            }else if (resp.equals("operation fail")){
                System.out.println("=======>Impossivel desbloquear id " + id + "!");
            }else {
                System.out.println("=======>Impossivel ligar ao RMI");
            }


        }
    }
    private String handleRemoteExceptionidentifiyVoter(int id){
        for(int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                //Adicinonar string
                String resp;
                resp = users.getServer().identifyVoter(id);
                return resp;
            } catch (InterruptedException e1) {
                System.out.println("Error on sleep: " + e1);
                break;
            } catch (RemoteException e1) {
                if(i >= timeout) {
                    System.out.println("Lost RMI connection, retrying to connect");

                }
            }
        }
        return "fail";
    }
    private static String handleRemoteExceptiondeauthenticateVoterInTable(Utilizadores users) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                //Adicinonar string
                String str;
                str = users.getServer().deauthenticateVoterInTable(users.getIdTable(), users.getElection(),users.getDep());
                return str;
            } catch (InterruptedException e1) {
                System.out.println("Error on sleep: " + e1);
                break;
            } catch (RemoteException e1) {
                if (i >= 7500) {
                    System.out.println("Lost RMI connection, retrying to connect");

                }
            }
        }
        return "fail";
    }
}










//= Thread para tratar de cada canal de comunicacao com um cliente
class Connection extends Thread {
    BufferedReader in;
    PrintWriter out;
    Socket clientSocket;
    int thread_number;
    VoterRMIInterface server;
    Utilizadores users;
    int timeout;


    public Connection(Socket aClientSocket, int numero, VoterRMIInterface server, Utilizadores users, int timeout) {
        this.server = server;
        this.users = users;
        this.timeout = timeout;
        thread_number = numero;
        try {
            clientSocket = aClientSocket;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            clientSocket.setSoTimeout(120000);
            this.start();
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    //=============================
    public void run() {
        @SuppressWarnings("unused")
		String resposta = "";
        String data;
        String[] message, message1, message2;
        String[] raw;
        String passw = "";
        Node no = null;
        for (Node node : users.lista) {
            if (node.socket == clientSocket) {
                no = node;
                System.out.println("Client node ENCONTRADO");


            }
        }
        while (true) {
            try {
                while (true) {
                    System.out.print("");

                    //no.getState();
                    if (no.getState()) {


                        out.println("type|login;id|" + no.getId());
                        System.out.println("type|login;id|" + no.getId() + "  ENVIADO");
                        data = in.readLine();
                        System.out.println("T[" + thread_number + "] Recebeu: " + data);
                        raw = data.split(";");
                        message = raw[0].split("\\|");
                        switch (message[0]) {
                            case "type":
                                switch (message[1]) {
                                    case "login":
                                        //split
                                        message1 = raw[1].split("\\|");
                                        switch (message1[0]) {
                                            case "id":
                                                no.setId(Integer.parseInt(message1[1]));
                                                message2 = raw[2].split("\\|");
                                                switch (message2[0]) {
                                                    case "password":

                                                        String resp;
                                                        try {
                                                            if ((resp = users.getServer().authenticateVoter(no.getId(), message2[1])) != null) {

                                                                //RMI PARA VALIDAR
                                                                //System.out.println(no.getId()+" ------ "+no.getPassword());
                                                                System.out.println(resp);
                                                                ///
                                                            }
                                                        } catch (RemoteException e1) {
                                                            resp = handleRemoteExceptionAutenticateVoter(no);

                                                        }
                                                        if (resp.equals("Welcome")) {
                                                            //FEZ LOGIN
                                                            no.setPassword(message2[1]);

                                                            out.println("type|status;logged|on;msg|Ja esta activo");


                                                        } else if (resp.equals("operation fail")) {


                                                            out.println("msg|Autenticacao Incorreta;");
                                                        } else {
                                                            System.out.println("Impossivel ligar ao RMI");
                                                        }

                                                        break;
                                                    default:
                                                        out.println("msg|Autenticacao Incorreta;");
                                                        break;
                                                }
                                                break;
                                            default:

                                                break;
                                        }
                                        break;
                                    default:

                                        break;
                                }
                                break;
                            default:

                                break;
                        }


                        while (no.getId() != 0 && no.getPassword() != null) {
                            System.out.println("Entrou no login");
                            data = in.readLine();
                            System.out.println("Terminal " + thread_number + " --->" + data);
                            raw = data.split(";");
                            message = raw[0].split("\\|");
                            String rs = null;
                            String resp="";
                            @SuppressWarnings("unused")
							int i = 0;

                            switch (message[0]) {
                                case "type":
                                    switch (message[1]) {
                                        case "login":
                                            //split
                                            message1 = raw[1].split("\\|");

                                            switch (message1[0]) {
                                                case "password":
                                                    passw = message1[1];
                                                    System.out.println("|" + passw + "|");
                                                    System.out.println(no.getId() + " - " + passw);


                                                    out.println("type|msg;msg|Ja esta activo;");


                                                    break;
                                            }

                                            break;


                          
                                            
                                        case "listas_list":
                                            //split
                                            rs = null;
                                            message1 = raw[1].split("\\|");
                                            switch (message1[0]) {

                                                case "eleicao":

                                                    //BUSCAR LISTAS CANDIDATAS
                                                  
                                                    if (!rs.equals("0;")) {


                                                        //TEM ELEICOES DISP
                                                        System.out.println(rs);

                                                        out.println("type|listas_list;eleicao|" + message1[1] + ";list_count|" + rs);


                                                    } else if (rs.equals("0;")) {


                                                        out.println("type|msg;msg|Nao existe nenhuma lista candidata de momento;");
                                                    } else {
                                                        System.out.println("Impossivel ligar ao RMI");
                                                    }


                                                    break;
                                            }
                                            break;
                                        case "vote":
                                            message1 = raw[1].split("\\|");
                                            switch (message1[0]) {

                                                case "vote":
                                                    //VOTO PADA BD
                                                    resp = "";
                                                    try {
                                                        if ((resp = users.getServer().vote(no.getId(), users.getElection(), message1[1], users.getDep())) != null) {

                                                            //RMI PARA VALIDAR
                                                            System.out.println(resp);
                                                            ///
                                                        }
                                                    } catch (RemoteException e1) {
                                                        resp = handleRemoteExceptionVote(no, users.getElection(), message1[1], users.getDep());

                                                    }
                                                    if (resp.equals("operation success")) {
                                                        //FEZ LOGIN

                                                        out.println("type|msg;msg|Voto efetuado! Obrigado");
                                                        System.out.println("VOTO EFECTUADO: Eleicao: " + users.getElection() + " VOTO:" + message1[1]);
                                                        String log = null;
                                                        try {

                                                            if ((log = users.getServer().lockVoter(no.getId())) != null) {

                                                                //RMI PARA VALIDAR
                                                                System.out.println(log);
                                                                ///
                                                            }
                                                        } catch (RemoteException e1) {
                                                            log = handleRemoteExceptionLockVoter(no);

                                                        }
                                                        if (resp.equals("operation success")) {


                                                            out.println("type|msg;msg|LOGOUT");
                                                            System.out.println(log);
                                                            no.setId(0);
                                                            no.setPassword(null);
                                                            no.stateDown();
                                                            users.listPrint();


                                                        } else if (log.equals("operation fail")) {


                                                            out.println("type|msg;msg|Nao foi possivel fazer logout;");
                                                        } else {
                                                            System.out.println("Impossivel ligar ao RMI");
                                                        }


                                                    } else if (resp.equals("fail vote")) {


                                                        out.println("msg|Autenticacao Incorreta;");
                                                    } else {
                                                        System.out.println("Impossivel ligar ao RMI");
                                                    }


                                                    break;

                                            }
                                            break;
                                        case "logout":
                                            String log=null;
                                            try {

                                                if ((log = users.getServer().lockVoter(no.getId())) != null) {

                                                    //RMI PARA VALIDAR
                                                    System.out.println(log);
                                                    ///
                                                }
                                            } catch (RemoteException e1) {
                                                log = handleRemoteExceptionLockVoter(no);

                                            }
                                            if (log.equals("operation success")) {

                                                out.println("type|msg;msg|LOGOUT");

                                                System.out.println(log);
                                                no.setId(0);
                                                no.setPassword(null);
                                                no.stateDown();
                                                users.listPrint();




                                            } else if (log.equals("operation fail")) {


                                                out.println("type|msg;msg|Nao foi possivel fazer logout;");
                                            } else {
                                                System.out.println("Impossivel ligar ao RMI");
                                            }

                                            break;
                                    }

                                    break;
                                default:

                                    break;

                            }


                        }
                        System.out.println();
                    }

                }
            } catch (EOFException e) {
                System.out.println("EOF:" + e);
            } catch (SocketTimeoutException e) {


                System.out.println("Terminal " + thread_number + " bloquado");
                out.println("msg|Terminal Bloqueado");
                String log=null;
                try {


                    if ((log = users.getServer().lockVoter(no.getId())) != null) {

                        //RMI PARA VALIDAR
                        System.out.println(log);
                        ///
                    }
                } catch (RemoteException e1) {
                    log = handleRemoteExceptionLockVoter(no);

                }
                if (log.equals("operation success")) {

                    out.println("type|msg;msg|LOGOUT");

                    System.out.println(log);
                    no.setId(0);
                    no.setPassword(null);
                    no.stateDown();
                    users.listPrint();




                } else if (log.equals("operation fail")) {


                    out.println("type|msg;msg|Nao foi possivel fazer logout;");
                } else {
                    System.out.println("Impossivel ligar ao RMI");
                }





            } catch (IOException e) {
                System.out.println("IO:" + e);
            }
        }


    }


    private String handleRemoteExceptionAutenticateVoter(Node no) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                //Adicinonar string
                String str;
                str = users.getServer().authenticateVoter(no.getId(), no.getPassword());
                return str;
            } catch (InterruptedException e1) {
                System.out.println("Error on sleep: " + e1);
                break;
            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection, retrying to connect");

                }
            }
        }
        return "fail";
    }

    //VOTE
    private String handleRemoteExceptionVote(Node no, String eleicao, String lista, String dep) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                //Adicinonar string
                String str;
                str = users.getServer().vote(no.getId(), eleicao, lista, dep);
                return str;
            } catch (InterruptedException e1) {
                System.out.println("Error on sleep: " + e1);
                break;
            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection, retrying to connect");

                }
            }
        }
        return "fail";
    }
    private String handleRemoteExceptionLockVoter(Node no) {
        for (int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                //Adicinonar string
                String str;
                str = users.getServer().lockVoter(no.getId());
                return str;
            } catch (InterruptedException e1) {
                System.out.println("Error on sleep: " + e1);
                break;
            } catch (RemoteException e1) {
                if (i >= timeout) {
                    System.out.println("Lost RMI connection, retrying to connect");

                }
            }
        }
        return "fail";
    }
}
/*
    private String handleRemoteExceptionAutenticateVoter(){
        for(int i = 0; true; i += 5000) {
            try {
                Thread.sleep(5000);
                //Adicinonar string
                String str;
                str=users.getServer().authenticateVoter(no.getId(), no.getPassword()));
                return str;
            } catch (InterruptedException e1) {
                System.out.println("Error on sleep: " + e1);
                break;
            } catch (RemoteException e1) {
                if(i >= timeout) {
                    System.out.println("Lost RMI connection, retrying to connect");

                }
            }
        }
        return "fail";
        }
}
*/

class findRMI extends Thread {
    Utilizadores users;
    VoterRMIInterface server=null;
    String rmiName, hostname;
    int rmiPort;



    public findRMI(Utilizadores users,String hostname,String rmiName,int rmiPort) {
        this.hostname=hostname;
        this.rmiName=rmiName;
        this.users = users;
        this.rmiPort=rmiPort;
        this.start();
    }

    public void run() {
        try {
            while (true) {
                Thread.sleep(5000);
                this.server = (VoterRMIInterface) LocateRegistry.getRegistry(hostname, rmiPort).lookup(rmiName);
                users.setServer(server);
            }
        }catch(NotBoundException e){
            System.out.println("NotBound: " + e);

        } catch (IOException e) {
            System.out.println("IO exception in sending ping: " + e);

        } catch (InterruptedException e) {
            System.out.print("Error on wait: " + e);

        }
    }
}

class Utilizadores {
    protected List<Node> lista = new ArrayList<Node>();
    private VoterRMIInterface server = null;
    private String dep="";
    private String election="";
    private int idTable;




    public synchronized void setDep(String dep){
        this.dep=dep;
    }
    public synchronized String getDep(){
        return dep;
    }

    public synchronized void setElection(String election){
        this.election=election;
    }
    public synchronized String getElection(){
        return election;
    }

    public synchronized void setIdTable(int id){
        this.idTable=id;
    }
    public synchronized int getIdTable(){
        return idTable;
    }

    public synchronized VoterRMIInterface getServer() {
        return this.server;
    }

    public synchronized void setServer(VoterRMIInterface server) {
        this.server = server;
       // System.out.println("Server RMI alterado: " + server);
    }


    public synchronized void listAdd(Socket socket) {

        lista.add(new Node(socket));
    }

    public synchronized void listPrint() {
        int i = 0;
        for (Node node : lista) {

            System.out.println("------> " + i + "--->" + node.socket + "    ACTIVO: " + node.getState());
            i++;
        }
    }

}

class Node {
    Socket socket;
    private int id =0;
    private String password = null;
    private boolean state;

    public Node(Socket socketa) {
        this.socket = socketa;
        state = false;

    }

    public void stateUp() {
        System.out.println("STATE UP");
        this.state = true;
    }

    public void stateDown() {
        this.state = false;
    }

    public boolean getState() {
        return this.state;

    }

    public synchronized int getId() {
        return this.id;
    }

    public synchronized void setId(int id) {
        this.id = id;
        System.out.println("ID SET TO: " + id);
    }

    public synchronized String getPassword() {
        return this.password;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
        System.out.println("PASSWORD SET TO: " + password);
    }


}

