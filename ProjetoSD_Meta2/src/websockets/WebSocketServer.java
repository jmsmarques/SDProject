package websockets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.websocket.server.ServerEndpoint;

import rmi.AdminRMIInterface;

import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnError;
import javax.websocket.Session;

@ServerEndpoint(value = "/ws")
public class WebSocketServer extends UnicastRemoteObject implements WebSocketInterface {

	private static final long serialVersionUID = -1235136832572599455L;
	private Session session;
	private String type;
	private static Registry serverRegistry;
	private static AdminRMIInterface server;
	private static String name;
	private static String host;
	private static int port;
	private String election;
	//private static final Set<WebSocketServer> users = new CopyOnWriteArraySet<>();

	public WebSocketServer() throws RemoteException {
		super();
		fileConfig();
		election = "";
	}

	@OnOpen
	public void start(Session session) throws RemoteException{
		this.session = session;
		//users.add(this);

		if(server == null) {
			connectRMI();
		}

		try {
			server.webRegisterForCallback((WebSocketInterface)this);
		} catch (RemoteException e) {
			handleRegistrationException();
		}
		System.out.println("WebSocket endpoint startup finished");
	}

	@OnClose
	public void end() throws RemoteException{
		//users.remove(this);
		try {
			server.webUnregisterForCallback((WebSocketInterface)this);
		} catch (RemoteException e1) {
			handleUnregistrationException();
		}
		try {
			this.session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// clean up once the WebSocket connection is closed
	}

	@OnMessage
	public void receiveMessage(String message) throws RemoteException{
		String [] parts = message.split(":");
		System.out.println(parts[0]);
		String nr = "";
		ArrayList <String> aux;
		if(parts[0].equals("tables")) {
			type = "tables";
			nr = server.tables();
			sendMessage(nr);	
		} else if(parts[0].equals("election")) {
			type = "election";
			election = parts[1];
			aux = server.getElectionInfo(election);
			for(int i = 0; i < aux.size(); i++) {
				nr += aux.get(i);
				nr += "||\n";
			}
			sendMessage(nr);
		} else if(parts[0].equals("users")) {
			type = "users";
			nr = server.onlineUsers();
			sendMessage(nr);
		}
	}

	@OnError
	public void handleError(Throwable t) throws RemoteException{
		t.printStackTrace();
	}

	private void sendMessage(String text) throws RemoteException{

		try {
			this.session.getBasicRemote().sendText(text);
		} catch (IOException e) {
			// clean up once the WebSocket connection is closed
			try {
				this.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void list_users() throws RemoteException {
		if(type.equals("users")) {
			String nr;
			nr = server.onlineUsers();
			sendMessage(nr);
		}
	}

	@Override
	public void list_tables() throws RemoteException {
		if(type.equals("tables")) {
			String nr;
			nr = server.tables();
			sendMessage(nr);	
		}
	}

	@Override
	public void users_nr() throws RemoteException {
		if(type.equals("election")) {
			String nr ="";
			ArrayList <String> aux;
			aux = server.getElectionInfo(election);
			for(int i = 0; i < aux.size(); i++) {
				nr += aux.get(i);
				nr += "||\n";
			}
			sendMessage(nr);
		}
	} 

	private void connectRMI() throws RemoteException{
		try {
			serverRegistry = (Registry) LocateRegistry.getRegistry(host, port);
			server = (AdminRMIInterface)serverRegistry.lookup(name);
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void lookupRMI() throws RemoteException {
		try {
			server = (AdminRMIInterface)serverRegistry.lookup(name);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	//aux functions
	private void handleRegistrationException() {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				server.webRegisterForCallback(this);
				break;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= 30000) {
					System.out.println("Lost RMI connection");
					break;
				}
			}
		}
	}

	private void handleUnregistrationException() {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				server.webUnregisterForCallback(this);
				break;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= 30000) {
					System.out.println("Lost RMI connection");
					break;
				}
			}
		}
	}

	//config file for rmi location
	private void fileConfig() throws RemoteException{
		//Start reading from config file
		try {
			BufferedReader fileRead = new BufferedReader(new FileReader("beanConfig.txt"));
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
				else if(parts[0].equals("host")) {
					host = parts[1];
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
				PrintWriter writer = new PrintWriter("beanConfig.txt", "UTF-8");
				writer.println("host=localhost");
				writer.println("port=7000");
				writer.println("name=porto");
				writer.close();
			} catch (Exception e1) {
				System.out.println("Could not create new file, default values still apply");
			}
		} catch (IOException e) {
			System.out.println("Error reading from file, setting values to default and continuing");
		}
	}
	//End of reading from config file	
}
