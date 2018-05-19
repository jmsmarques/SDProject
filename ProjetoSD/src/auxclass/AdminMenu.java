package auxclass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import rmi.AdminConsole;
import rmi.AdminRMIInterface;

public class AdminMenu {
	private static String hostname = "localhost"; //variable for hostname of the host who hostes the server
	private static int port = 7000; //variable for port number of rmi server with default value
	private static String name = "porto"; //variable for reference name of rmi server with default value
	private static int timeout = 30000; //variable to keep the time it takes before the client knows rmi is not working
	private static String fileName = "AdminConfig.txt"; //variable with the name of the file with the admin console configs
	
	public static void main(String args[]) {
		AdminConsole admin;
		Registry serverRegistry;
		Scanner keyboard = new Scanner(System.in);
		
		System.out.print("Config file name: "); //scan config file name
		fileName = keyboard.nextLine();
		fileConfig(); //Loads the servers configurations from config file
		
		try {
			//Setup of rmi connection
			serverRegistry = LocateRegistry.getRegistry(hostname, port);
			AdminRMIInterface server = (AdminRMIInterface) serverRegistry.lookup(name);
			//End of setup			
			
			//seting admin console object to use for callbacks
			admin = new AdminConsole(serverRegistry, server, timeout, name);

			//register for callback
			server.registerForCallback(admin);
			
			while(admin.showMenu());
			
			server.unregisterForCallback(admin);
			System.out.println("Bye Bye");
			System.exit(0);
		}catch(NotBoundException e) {
	        System.out.println("Not Bound:" + e.getMessage());
		} catch (RemoteException e) {
			System.out.println("RMI:" + e.getMessage());
		} finally {
			keyboard.close();
			System.out.println("Bye Bye");
			System.exit(0);
		}
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
				else if(parts[0].equals("hostname")) {
					hostname = parts[1];
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
				writer.println("port=7000");
				writer.println("name=porto");
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
