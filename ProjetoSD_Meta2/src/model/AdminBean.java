package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.AdminRMIInterface;

public class AdminBean implements Serializable{ //father bean for bean for the admin operations

	private static final long serialVersionUID = -6824365862497133239L;
	protected AdminRMIInterface server;
	protected Registry serverRegistry;
	protected int port;
	protected String host, name;
	
	public AdminBean() {
		fileConfig();
		try {
			serverRegistry = (Registry) LocateRegistry.getRegistry(host, port);
			server = (AdminRMIInterface)serverRegistry.lookup(name);
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
	}
	
	protected final void lookupRMI() {
		try {
			server = (AdminRMIInterface)serverRegistry.lookup(name);
		} catch (RemoteException | NotBoundException e) {	
			System.out.println("Error looking up rmi " + e);;
		}
	}
	
	private void fileConfig() {
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
