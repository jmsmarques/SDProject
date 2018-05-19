package rmi;
import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import auxclass.CloseElections;
import auxclass.UDPConnection;
import websockets.WebSocketInterface;

public class RMIServer extends UnicastRemoteObject implements AdminRMIInterface, VoterRMIInterface{
	private static final long serialVersionUID = 1L;
	private static String hostname = "localhost"; //variable to keep the hostname of the registry
	private static int port = 7000; //variable for port number of rmi server with default value
	private static String name = "porto"; //variable for reference name of rmi server with default value
	private static int timeout = 7500; //variable to keep the time it takes for secondary server to try to replace the primary after stoping receiving pings
	private static String fileName = "RMIConfig.txt"; //variable with the name of the file with the server configs
	private static int udpPort = 6500; //variable for port number of udp socket with default value
	private static int udpTargetPort = 6501; //variable for port number of udp socket which we want to send the ping with default value
	private static String udpHostname = "localhost"; //variable for the hostname of the other udp socket of the other rmi server with defautl value
	private static String dbUsername = "bdProj"; //variable with the username to connect to the database
	private static String dbPassword = "bdProj"; //variable with the password to connect to the database
	private static String dbHostname = "localhost"; //variable with the hostname of the database
	private static Connection con = null; //variable for connection object for database connection
	private static ArrayList <AdminConsoleInterface> adminClients; //array to keep the connect clients
	private static final Set<WebSocketInterface> webSockets = new CopyOnWriteArraySet<>();
	
	public RMIServer() throws RemoteException {
		super();
	}

	// =========================================================
	public static void main(String args[]) {
		adminClients = new ArrayList <AdminConsoleInterface>(); 
		boolean failover; //indicates if server is failover or not
		UDPConnection udp = null;
		Scanner keyboard = new Scanner(System.in);
		boolean go = true; //variable to know if there were errors binding udp socket

		
		System.out.print("Config file name: "); //scan file name
		fileName = keyboard.nextLine();
		fileConfig(); //Loads the servers configurations from config file
		keyboard.close();
		
		try {
			udp = new UDPConnection(udpPort, udpHostname,udpTargetPort, timeout);
		} catch (SocketException e) {
			System.out.println("Error on socket binding: " + e);
			go = false;
		} catch (UnknownHostException e) {
			System.out.println("Error on hostname: " + e);
			go = false;
		}
		RMIServer h;
		//aux variable to call a function from a rmi server to know if there's a rmi server already bound
		AdminRMIInterface server;
		//checking if there's already one rmi server up
		try {
			server = (AdminRMIInterface)LocateRegistry.getRegistry(hostname, port).lookup(name);
			server.sayHello();
			failover = true;
		} catch (RemoteException | NotBoundException e) {
			failover = false;
			System.out.println("No RMI server is bound\nBinding this one");
		}
		//end of checking other rmi server
		try {
			while(go) {
				if(!failover) { //if its not the failover server
					h = new RMIServer();
					//Database connection
					Class.forName("com.mysql.jdbc.Driver");
					
					con = (Connection) DriverManager.getConnection("jdbc:mysql://" + dbHostname + ":8070/sd?autoReconnect=true&useSSL=false",dbUsername, dbPassword);  
					con.setAutoCommit(true);
					//end database connection
					
					new CloseElections(con); //thread to check if an election is active or not
					Naming.rebind("rmi://localhost:" + port + "/" + name, h);
					System.out.println("RMI Server ready.");

					while(true) { //sends ping to the other rmi server
						Thread.sleep(5000);
						udp.pingSend();
					}
				}
				else { //failover server
					System.out.println("Failover server setting up");
					try {
						while(true) {
							udp.pingReceive(); //receives ping from other rmi server
						}
					} catch (SocketTimeoutException e1) {
						System.out.println("Timeout reached, activating failover measures");
						failover = false;
					} catch (IOException e1) {
						System.out.println("IO exception in receiving ping: " + e1);
					}
				}
			}

		} catch (IOException e) {
			System.out.println("IO exception: " + e);
		} catch (InterruptedException e) {
			System.out.print("Error on wait: " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("Error connecting to database: " + e);
		} catch (SQLException e) {
			System.out.println("Error connecting to database: " + e);
		} finally {
			try {
				con.close();
				udp.closeSocket();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
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
				if(parts[0].equals("name")) {
					name = parts[1];
				}
				else if(parts[0].equals("port")) {
					port = Integer.parseInt(parts[1]);
				}
				else if(parts[0].equals("UDP port")) {
					udpPort = Integer.parseInt(parts[1]);
				}
				else if(parts[0].equals("UDP target port")) {
					udpTargetPort = Integer.parseInt(parts[1]);
				}
				else if(parts[0].equals("UDP hostname")) {
					udpHostname = parts[1];
				}
				else if(parts[0].equals("DB username")) {
					dbUsername = parts[1];
				}
				else if(parts[0].equals("DB password")) {
					dbPassword = parts[1];
				}
				else if(parts[0].equals("DB hostname")) {
					dbHostname = parts[1];
				}
				else if(parts[0].equals("RMI Registry Hostname")) {
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
				writer.println("RMI Registry Hostname=localhost");
				writer.println("port=7000");
				writer.println("name=porto");
				writer.println("timeout=7500");
				writer.println("UDP port=6500");
				writer.println("UDP target port=6501");
				writer.println("UDP hostname=localhost");
				writer.println("DB username=bdProj");
				writer.println("DB password=bdProj");
				writer.println("DB hostname=localhost");
				writer.close();
			} catch (Exception e1) {
				System.out.println("Could not create new file, default values still apply");
			}
		} catch (IOException e) {
			System.out.println("Error reading from file, setting values to default and continuing");
		}
		//End of reading from config file
	}
	
	//=====================================================================================================
	@Override
	public synchronized void webRegisterForCallback(WebSocketInterface websocket) throws RemoteException {
		System.out.println("websocket registou");
		webSockets.add(websocket);
	}
	@Override
	public synchronized void  webUnregisterForCallback(WebSocketInterface websocket) throws RemoteException{
		if(webSockets.remove(websocket)) {
			System.out.println("websocket deresgistou");
		}
		else {
			System.out.println("Web Socket that was not registered tried to unregister");
		}
	}
	
	public synchronized void registerForCallback(AdminConsoleInterface adminClient) throws java.rmi.RemoteException{
		if(!adminClients.contains(adminClient)) {
			adminClients.add(adminClient);
			System.out.println("New admin console connected");
			for(int i = 0; i < adminClients.size(); i++) {
				try {
					adminClients.get(i).notifyAdmin();
				} catch (RemoteException e) {
					unregisterForCallback(adminClients.get(i));
					System.out.println("Removed admin console that crashed");
				}
			}
		}
	}
	
	public synchronized void  unregisterForCallback(AdminConsoleInterface adminClient) throws java.rmi.RemoteException{
		if(adminClients.remove(adminClient)) {
			System.out.println("Admin console disconnected");
		}
		else {
			System.out.println("Admin console that was not registered tried to unregister");
		}
	}
	//admin rmi sayHello function to check if a rmi is alive
	public void sayHello() throws java.rmi.RemoteException{
		System.out.println("Hello from the other RMI Server");
	}
	public synchronized void sayHellos() throws java.rmi.RemoteException{
		for(int i = 0; i < adminClients.size(); i++) {
			try {
				adminClients.get(i).notifyTable();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	//======================================================================================================
	//tcp admin functions
	public String identifyVoter(int ID) throws java.rmi.RemoteException {
		String sql = "{call person_unlock(?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		String res = "Operation fail"; //variable with the result of the operation
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.registerOutParameter(2, Types.VARCHAR);
			
			stmt.execute();
			
			res = stmt.getString(2);
			
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return res;
		}
		return res;
	}
	
	public String authenticateVoter(int ID, String password) throws java.rmi.RemoteException {
		String sql = "{call authenticate(?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		String res = "Operation fail"; //variable with the result of the operation
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, password);
			stmt.registerOutParameter(3, Types.VARCHAR);

			stmt.execute();
			
			res = stmt.getString(3);
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return res;
		}
		sendToWebsocketUsers();
		return res;
	}
	
	public String authenticateVoterInTable(int ID, String password,String eleicao, String dep) throws java.rmi.RemoteException {
        String sql = "{call authenticateTable(?, ?, ?, ?, ?)}"; //variable with the name of the procedure to call
        CallableStatement stmt; //variable for statement object for database connection
        String res = "Operation fail"; //variable with the result of the operation
        try {
            stmt = con.prepareCall(sql);
            
            stmt.setInt(1, ID);
            stmt.setString(2, password);
            stmt.setString(3, eleicao);
            stmt.setString(4, dep);
            stmt.registerOutParameter(5, Types.VARCHAR);
            
            stmt.execute();
            
            res = stmt.getString(5);
        } catch (SQLException e) {
            System.out.println("Invalid SQL call authenticateVoterInTable: " + e);
            return res;
        }
        try {
        	sendToWebsocketTables();
        }catch (RemoteException e) {
        	System.out.println("nao deu");
        }
        return res;
    }
    
    public String deauthenticateVoterInTable(int ID,String eleicao, String dep) throws java.rmi.RemoteException {
        String sql = "{call deauthenticateTable(?, ?, ?, ?)}"; //variable with the name of the procedure to call
        CallableStatement stmt; //variable for statement object for database connection
        String res = "Operation fail"; //variable with the result of the operation
        try {
            stmt = con.prepareCall(sql);
            
            stmt.setInt(1, ID);
            stmt.setString(2, eleicao);
            stmt.setString(3, dep);
            stmt.registerOutParameter(4, Types.VARCHAR);
            
            stmt.execute();
            
            res = stmt.getString(4);
        } catch (SQLException e) {
            System.out.println("Invalid SQL call: " + e);
            return res;
        }
        sendToWebsocketTables();
        return res;
    }
	
	public String vote(int ID, String election, String vote, String departamento) throws java.rmi.RemoteException {
		String sql = "{call vote(?, ?, ?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		String res = "Operation fail"; //variable with the result of the operation
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, election);
			stmt.setString(3, vote);
			stmt.setString(4, departamento);
			stmt.registerOutParameter(5, Types.VARCHAR);
			
			stmt.execute();
			
			res = stmt.getString(5);
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return res;
		}
		sendToWebsocketNr();
		return res;
	}
	
	public String lockVoter(int ID) throws java.rmi.RemoteException {
		String sql = "{call person_lock(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		sendToWebsocketUsers();
		return "Operation success";
	}
	
	public String faceAss(String token,int ID) throws java.rmi.RemoteException {
		String sql = "{call faceass(?,?,?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		String res;
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, token);
			stmt.registerOutParameter(3, Types.VARCHAR);
			
			stmt.execute();
			 res = stmt.getString(3);
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		return res;
	}
	public String faceLog(String token) throws java.rmi.RemoteException {
		String sql = "{call faceLog(?,?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		String res=null;
		try {
			stmt = con.prepareCall(sql);
			
			
			stmt.setString(1, token);
			stmt.registerOutParameter(2, Types.VARCHAR);
			
			
			stmt.execute();
			res = stmt.getString(2);
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		return res;
	}
	
	
	
	
	public String listLists(String election, int ID) throws java.rmi.RemoteException {
		String sql = "SELECT NOME FROM LISTA WHERE ELEICAO_titlo = ? AND TIPO_MEMBROS = (SELECT TIPO FROM PESSOA WHERE ID = ?)";
		ResultSet rs;
		int rows = 0; //aux variable to know how many rows we got from the query
		String result = ""; //string to return;
		
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			
			stmt.setString(1, election);
			stmt.setInt(2, ID);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				result =  result + "lista_nome" + "|" + rs.getString("NOME") + ";";
				rows++;
			}
			
			result = rows + ";" + result;
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
		}
	
		return result;
	}
	
	public String listElections(int ID) throws java.rmi.RemoteException {
		String sql = "SELECT TITLO FROM ELEICAO WHERE TIPO_MEMBROS = (SELECT TIPO FROM PESSOA WHERE ID = ?) AND ATIVO = 'Y'";
		ResultSet rs;
		int rows = 0; //aux variable to know how many rows we got from the query
		String result = ""; //string to return;
		
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			
			stmt.setInt(1, ID);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				result =  result + "eleicao_nome" + "|" + rs.getString("TITLO") + ";";
				rows++;
			}
			
			result = rows + ";" + result;
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
	
		return result;
	}
	
	//end of tcp admin functions
	
	//admin rmi functions
	public String registerPerson(int ID, String name, String password, String type, int cellphone, String address, String validityCC) throws java.rmi.RemoteException {
		String sql = "{call Insert_In_Pessoa(?, ?, ?, ?, ?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, name);
			stmt.setString(3, password);
			stmt.setInt(4, cellphone);
			stmt.setString(5, address);
			stmt.setString(6, validityCC);
			stmt.setString(7, type);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String removePerson(int ID) throws RemoteException {
		String sql = "{call removePerson(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
			try {
				stmt = con.prepareCall(sql);
				
				stmt.setInt(1, ID);
				
				stmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				return "Operation fail";
			}
		return "Operation success";
	}

	public String changePerson(int ID, String name, String password, int cellphone, String address, String validityCC, String type) throws RemoteException {
		String sql = "{call change_person(?, ?, ?, ?, ?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, name);
			stmt.setString(3, password);
			stmt.setInt(4, cellphone);
			stmt.setString(5, address);
			stmt.setString(6, validityCC);
			stmt.setString(7, type);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String addDepartment(String name, String college) throws java.rmi.RemoteException {
		String sql = "{call Insert_In_Departamento(?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, name);
			stmt.setString(2, college);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		return "Operation success";
	}
	
	public String addCollege(String name) throws java.rmi.RemoteException {
		String sql = "{call Insert_In_Faculdade(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, name);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String removeDepartment(String name) throws java.rmi.RemoteException {
		String sql = "{call removeDepartamento(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, name);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String removeCollege(String name) throws java.rmi.RemoteException {
		String sql = "{call removeFaculdade(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, name);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String changeDepartment(String name, String newName, String college) throws java.rmi.RemoteException {
		String sql = "{call change_departamento(?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, name);
			stmt.setString(2, newName);
			stmt.setString(3, college);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String changeCollegeName(String name, String newName) throws java.rmi.RemoteException {
		String sql = "{call change_faculdade_name(?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, name);
			stmt.setString(2, newName);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		return "Operation success";
	}
	
	public String createElection(String title, String startDate, String endDate, String type, String membersType, String description) throws java.rmi.RemoteException {
		String sql = "{call Insert_In_Eleicao(?, ?, ?, ?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, title);
			stmt.setString(2, startDate);
			stmt.setString(3, endDate);
			stmt.setString(4, type);
			stmt.setString(5, membersType);
			stmt.setString(6, description);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		return "Operation success";
	}
	
	public String removeElection(String title) throws java.rmi.RemoteException {
		String sql = "{call removeEleicao(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, title);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		return "Operation success";
	}
	
	public String addList(String name, String membersType, String election) throws java.rmi.RemoteException {
		String sql = "{call Insert_In_Lista(?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, name);
			stmt.setString(2, membersType);
			stmt.setString(3, election);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String removeList(String name, String election) throws java.rmi.RemoteException {
		String sql = "{call removeLista(?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
	
			stmt.setString(1, name);
			stmt.setString(2, election);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String addPersonToList(int ID, String list, String election) throws java.rmi.RemoteException {
		String sql = "{call Insert_In_PessoaLista(?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, list);
			stmt.setString(3, election);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String removePersonFromList(int ID, String list, String election) throws java.rmi.RemoteException {
		String sql = "{call removePessoaDaLista(?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, list);
			stmt.setString(3, election);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String addVotingTable(String department, String election) throws java.rmi.RemoteException {
		String sql = "{call insert_In_MesaDeVoto(?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, department);
			stmt.setString(2, election);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String removeVotingTable(String department, String election) throws java.rmi.RemoteException {
		String sql = "{call removeMesaVoto(?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, department);
			stmt.setString(2, election);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String changeElection(String election, String newDate, String newFinishDate, String newType, String newMembersType, String description) throws java.rmi.RemoteException {
		String sql = "{call change_eleicao(?, ?, ?, ?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, election);
			stmt.setString(2, newDate);
			stmt.setString(3, newFinishDate);
			stmt.setString(4, newType);
			stmt.setString(5, newMembersType);
			stmt.setString(6, description);
			
			stmt.execute();
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String getVoterInfo(int ID, String election) throws java.rmi.RemoteException {
		String sql = "{call get_info_votador(?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		ResultSet rs;
		String result = "";
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, election);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				result =  result + "Table: " + rs.getString("Departamento_Nome") + " " + rs.getString("Data_Voto") + "\n";
			}
			
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return result;
	}
	
	public String getTableInfo(String election) throws java.rmi.RemoteException {
		String sql = "{call nr_votos_por_mesa(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		ResultSet rs = null;
		String result = "";
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setString(1, election);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				result =  result + rs.getString("Departamento_Nome") + ": " + rs.getInt("votos") + "\n\n";;
			}
			
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		if(result.equals("")) {
			result = "This elections has no votes yet";
		}
		return result;
	}
	
	public ArrayList<String> getElectionInfo(String election) throws java.rmi.RemoteException {
		String sql1 = "{call resultados_nulos_eleicao(?)}"; //variable with the name of the procedure to call
		String sql2 = "{call resultados_eleicao(?)}"; //variable with the name of the procedure to call
		String sql3 = "{call resultados_nulos_eleicao_por_mesa(?)}"; //variable with the name of the procedure to call
		String sql4 = "{call resultados_eleicao_por_mesa(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt3; //variable for statement object for database connection
		CallableStatement stmt4; //variable for statement object for database connection
		CallableStatement stmt1; //variable for statement object for database connection
		CallableStatement stmt2; //variable for statement object for database connection
		ResultSet rs = null;
		ArrayList<String> result = new ArrayList<String>();
		String aux;
		
		try {
			stmt1 = con.prepareCall(sql1);
			stmt2 = con.prepareCall(sql2);
			stmt3 = con.prepareCall(sql3);
			stmt4 = con.prepareCall(sql4);
			
			stmt1.setString(1, election);
			stmt2.setString(1, election);
			stmt3.setString(1, election);
			stmt4.setString(1, election);
			
			rs = stmt1.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				aux = "Brancos: " + rs.getInt("BRANCOS") + "\nNulos: " + rs.getInt("NULOS");
				result.add(aux);
			}
			
			rs = stmt2.executeQuery();
			while(rs.next()) {//reads the result of the query
				aux = rs.getString("LISTA_NOME") + ": " + rs.getInt("VOTOS");
				result.add(aux);
			}
			
			result.add("");
			rs = stmt3.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				aux = rs.getString("DEPARTAMENTO_Nome") + " Nulos: " + rs.getInt("Nulos") + " Brancos: " + rs.getInt("Brancos");
				result.add(aux);
			}
			
			rs = stmt4.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				aux = rs.getString("Departamento_Nome") + " " + rs.getString("LISTA_NOME") + ": " + rs.getInt("VOTOS");
				result.add(aux);
			}
			
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			result.add("Operation fail");
			return result;
		}

		return result;
	}
	
	public String associatePersonToTable(int ID, String department, String election) throws java.rmi.RemoteException {
		String sql = "{call insert_in_pessoa_mesa(?, ?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		String result = "";
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, department);
			stmt.setString(3, election);
			stmt.registerOutParameter(4, java.sql.Types.VARCHAR);
			
			stmt.executeQuery();
			
			result = stmt.getString(4);
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return result;
	}
	
	public String disassociatePersonToTable(int ID) throws java.rmi.RemoteException {
		String sql = "{call removePessoaMesaDeVoto(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);;
			
			stmt.executeQuery();
			
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return "Operation fail";
		}
		
		return "Operation success";
	}
	
	public String voteInAdvance(int ID, String election, String vote) throws java.rmi.RemoteException {
		String sql = "{call vote_in_advance(?, ?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		String res = "Operation fail"; //variable with the result of the operation
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, election);
			stmt.setString(3, vote);
			stmt.registerOutParameter(4, Types.VARCHAR);
			
			stmt.execute();
			
			res = stmt.getString(4);
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return res;
		}
		sendToWebsocketNr();
		return res;
	}
	
	public String voteInAdvanceWeb(int ID, String election, String vote) throws java.rmi.RemoteException {
		String sql = "{call vote_in_advance_web(?, ?, ?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		String res = "Operation fail"; //variable with the result of the operation
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, election);
			stmt.setString(3, vote);
			stmt.registerOutParameter(4, Types.VARCHAR);
			
			stmt.execute();
			
			res = stmt.getString(4);
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return res;
		}
		sendToWebsocketNr();
		return res;
	}
	
	public ArrayList<String> listInAdvanceLists(int ID, String election) throws java.rmi.RemoteException {
		String sql = "{call list_lists(?, ?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		ResultSet rs; //variable with the result of the operation
		ArrayList<String> lists = new ArrayList<String>(); //value to return
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			stmt.setString(2, election);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				lists.add(rs.getString("Nome"));
			}
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return null;
		}
		return lists;
	}
	public ArrayList<String> listInAdvanceElections(int ID) throws java.rmi.RemoteException {
		String sql = "{call list_eleicoes(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		ResultSet rs; //variable with the result of the operation
		ArrayList<String> lists = new ArrayList<String>(); //value to return
		try {
			stmt = con.prepareCall(sql);
			
			stmt.setInt(1, ID);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				lists.add(rs.getString("Titlo"));
			}
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			return null;
		}
		return lists;
	}
	
	public ArrayList<String> electionDetails(String election) throws java.rmi.RemoteException {
		String sql = "{call detalhes_eleicoes(?)}"; //variable with the name of the procedure to call
		String sql1 = "{call detalhes_eleicoes_listas(?)}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		CallableStatement stmt1; //variable for statement object for database connection
		ResultSet rs; //variable with the result of the operation
		ArrayList<String> result = new ArrayList<String>(); //value to return
		String aux;
		try {
			stmt = con.prepareCall(sql);
			stmt1 = con.prepareCall(sql1);
			
			stmt.setString(1, election);
			stmt1.setString(1, election);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				aux = rs.getString("Titlo") + " Data Inicio: " + rs.getString("Data_Inicio") + " Data Fim: " + rs.getString("Data_Fim")
				+ " Tipo: " + rs.getString("Tipo") + " Tipo de Membros: " + rs.getString("Tipo_Membros") + " Descricao: " 
				+ rs.getString("Descricao") + " Ativa: " + rs.getString("Ativo");
				
				result.add(aux);
			}
			
			rs = stmt1.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				aux = rs.getString("Lista_Nome") + " " + rs.getInt("Pessoa_Id");
				
				result.add(aux);
			}
			
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			result.add("Operation fail");
			return result;
		}
		return result;
	}
	//end of admin rmi functions
	public String tables() throws RemoteException {
		String sql = "{call mesas_ativas()}"; //variable with the name of the procedure to call
		String sql1 = "{call mesas_inativas()}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		CallableStatement stmt1; //variable for statement object for database connection
		ResultSet rs; //variable with the result of the operation
		String result = "";
		
		try {
			stmt = con.prepareCall(sql);
			stmt1 = con.prepareCall(sql1);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				result += "Departamento: " + rs.getString("Departamento_Nome") + "\nEleicao: " + rs.getString("Eleicao_titlo") + " Ativa||\n";
			}
			result += "\n";
			rs = stmt1.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				result += "Departamento: " + rs.getString("Departamento_Nome") + "\nEleicao: " + rs.getString("Eleicao_titlo") + " Inativa||\n";
			}
			
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			result = "Operation fail";
			return result;
		}
		return result;
	}
	
	public String onlineUsers() throws RemoteException{
		String sql = "{call utilizadores_online()}"; //variable with the name of the procedure to call
		CallableStatement stmt; //variable for statement object for database connection
		ResultSet rs; //variable with the result of the operation
		String result = "";
		
		try {
			stmt = con.prepareCall(sql);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {//reads the result of the query
				result += "Id: " + rs.getString("Id") + " Nome: " + rs.getString("Nome") + " Tipo: " + rs.getString("tipo") + "||\n";
			}
			
		} catch (SQLException e) {
			System.out.println("Invalid SQL call: " + e);
			result = "Operation fail";
			return result;
		}
		return result;
	}
	
	private void sendToWebsocketUsers() throws RemoteException{
		webSockets.forEach((s) -> {
			try {
				s.list_users();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});	
	}
	
	private void sendToWebsocketTables() throws RemoteException{
		webSockets.forEach((s) -> {
			try {
				s.list_tables();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});	
	}
	
	private void sendToWebsocketNr() throws RemoteException{
		webSockets.forEach((s) -> {
			try {
				s.users_nr();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});	
	}
	
}
