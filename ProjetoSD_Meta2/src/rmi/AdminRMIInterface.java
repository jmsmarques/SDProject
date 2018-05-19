package rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import websockets.WebSocketInterface;

public interface AdminRMIInterface extends Remote{
	/**
	 * gets all the active tables and inactive
	 * @return all active and inactive tables
	 * @throws RemoteException
	 */
	public String tables() throws RemoteException;
	/**
	 * gets all active users
	 * @return all active users
	 * @throws RemoteException
	 */
	public String onlineUsers() throws RemoteException;
	/**
	 * registers a websocket in rmi
	 * @param webSocket webSocket object we want to remove
	 * @throws java.rmi.RemoteException
	 */
	public void webRegisterForCallback(WebSocketInterface websocket) throws RemoteException;
	/**
	 * unregister a websocket from rmi
	 * @param webSocket webSocket object we want to remove
	 * @throws java.rmi.RemoteException
	 */
	void webUnregisterForCallback(WebSocketInterface websocket) throws RemoteException;
	/**
	 * function to check if a server is up, to use for failover
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public void sayHello() throws java.rmi.RemoteException;
	/**
	 * adds a client to the connect clients list for callbacks
	 * @param adminClient client to be added 
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public void registerForCallback(AdminConsoleInterface adminClient) throws java.rmi.RemoteException;
	/**
	 * removes a client from the connect client list
	 * @param adminClient client to be removed
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)@throws java.rmi.RemoteException
	 */
	public void unregisterForCallback(AdminConsoleInterface adminClient) throws java.rmi.RemoteException;
	/**
	 * registers a person in the database
	 * @param ID cartao de cidadao identification number
	 * @param name name of the person to be registered
	 * @param password string which the person needs to use to authenticate himself
	 * @param type 3 types estudantes/docentes/funcionarios
	 * @param cellphone cellphone number of the person
	 * @param address address of the person
	 * @param validityCC expiration date of cartao de cidadao
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String registerPerson(int ID, String name, String password, String type, int cellphone, String address, String validityCC) throws java.rmi.RemoteException;
	/**
	 * removes a person from the database
	 * @param ID ID of the person we want to remove
	 * @return success or not
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String removePerson(int ID) throws java.rmi.RemoteException;
	/**
	 * change person properties
	 * @param ID of the person we want to change
	 * @param name new name of the person
	 * @param password new password of the person
	 * @param cellphone new cellphone of the person
	 * @param address new address of the person
	 * @param validityCC new expiration date of CC of the person
	 * @param type new type of the person
	 * @return success or not
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String changePerson(int ID, String name, String password, int cellphone, String address, String validityCC, String type) throws java.rmi.RemoteException;
	/**
	 * adds a department to the database
	 * @param name name of the department being added
	 * @param college name of the college which the department belongs
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String addDepartment(String name, String college) throws java.rmi.RemoteException;
	/**
	 * adds a college to the database
	 * @param name name of the college being added
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String addCollege(String name) throws java.rmi.RemoteException;
	/**
	 * removes a department from the database
	 * @param name name of the department to be removed
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String removeDepartment(String name) throws java.rmi.RemoteException;
	/**
	 * removes a college from the database
	 * @param name name of the college to be removed
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String removeCollege(String name) throws java.rmi.RemoteException;
	/**
	 * changes a department name in the database
	 * @param name name of the department which name will be changed
	 * @param newName new name of the department
	 * @param college new college to which the department will belong
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String changeDepartment(String name, String newName, String college) throws java.rmi.RemoteException;
	/**
	 * changes a college name in the database
	 * @param name name of the college which name will be changed
	 * @param newName new name of the college
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String changeCollegeName(String name, String newName) throws java.rmi.RemoteException;
	/**
	 * creates a new election in the database
	 * @param title title of the election(there cannot be 2 equal titles)
	 * @param startDate date which the election will start (dd-mm-yy hh:mm)
	 * @param endDate date which the election will end (dd-mm-yy hh:mm)
	 * @param type type election type there's 2 different types (conselho geral and nucleos)
	 * @param membersType if the election is for conselho geral there's 3 members type possible (estudantes/docentes/funcionarios) if the election is for nucleos type can only be estudantes
	 * @param description brief description of the election
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String createElection(String title, String startDate, String endDate, String type, String membersType,String description) throws java.rmi.RemoteException;
	/**
	 * removes an election
	 * @param title election title to remove
	 * @return success or not
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String removeElection(String title) throws java.rmi.RemoteException;
	/**
	 * adds a list to an election in the database
	 * @param name name of the list to be added (there can't be 2 lists with the same name)
	 * @param membersType type of constituting members of the list, only valid for conselho geral elections, else it's always estudantes
	 * @param election name of the election this list will run for
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String addList(String name, String membersType, String election) throws java.rmi.RemoteException;
	/**
	 * removes a list from the database
	 * @param name name of the list to remove
	 * @param election election to which the list to remove belongs
	 * @return success or not
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String removeList(String name, String election) throws java.rmi.RemoteException;
	/**
	 * adds a person to a list in the database
	 * @param ID identification number of cartao de cidadao of the person to be added to the list
	 * @param list name of the list which the person will be added to
	 * @param election name of the election in which the list is running
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String addPersonToList(int ID, String list, String election) throws java.rmi.RemoteException;
	/**
	 * removes a person from a list from the database
	 * @param ID identification number of cartao de cidadao of the person to be removed from the list
	 * @param list name of the list which the person will be removed from
	 * @param election title of the election to which the list belongs to
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String removePersonFromList(int ID, String list, String election) throws java.rmi.RemoteException;
	/**
	 * adds a voting table to a department for an election from the database
	 * @param department name of the department where the table will be located (only 1 table allowed per department)
	 * @param election election name to which the table belongs
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String addVotingTable(String department, String election) throws java.rmi.RemoteException;
	/**
	 * removes a voting table from a department for an election from the database
	 * @param department department name where the table we want to remove is
	 * @param election election title of the table we want to remove
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String removeVotingTable(String department, String election) throws java.rmi.RemoteException;
	/**
	 * changes an election properties from the database
	 * @param election title of the election we want to change the date
	 * @param newDate new date in which we want the election to start
	 * @param newFinishDate new date in which we want the election to finish
	 * @param newType new type of the election
	 * @param newMembersType new members type of the election
	 * @param description new description for the election
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String changeElection(String election, String newDate, String newFinishDate, String newType, String newMembersType, String description) throws java.rmi.RemoteException;
	/**
	 * gets the time and the table where a voter voted for a certain election
	 * @param ID identification number of cartao de cidadao of the person who wants to vote
	 * @param election title of the election which we want to know at what time the voter voted
	 * @return string with the date in which the voter voted for an election
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String getVoterInfo(int ID, String election) throws java.rmi.RemoteException;
	/**
	 * sees what table and voters are connected at the present time
	 * @param election election title we want to know the info about
	 * @return String with the current tables online
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String getTableInfo(String election) throws java.rmi.RemoteException;
	/**
	 * gets all the info (number votes/where/percentage) from a certain election
	 * @param election title of the election we want to get the info
	 * @return array with the result of the election
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public ArrayList<String> getElectionInfo(String election) throws java.rmi.RemoteException;
	/**
	 * disassociates a person to a table (max 3)
	 * @param ID ID of the person we want to associate
	 * @param department department name of the table
	 * @param election election title of the table election
	 * @return success or not
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String associatePersonToTable(int ID, String department, String election) throws java.rmi.RemoteException;
	/**
	 * disassociates a person to a table (max 3)
	 * @param ID ID of the person we want to disassociate
	 * @return success or not
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String disassociatePersonToTable(int ID) throws java.rmi.RemoteException;
	/**
	 * allows voters to vote in advance for a certain election
	 * @param ID ID of the voter who wants to vote in advance
	 * @param election election title in which the voter wants to vote
	 * @param vote list name we want to vote
	 * @return success or not
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String voteInAdvance(int ID, String election, String vote) throws java.rmi.RemoteException;
	/**
	 * allows voters to vote in advance for a certain election in the web
	 * @param ID ID of the voter who wants to vote in advance
	 * @param election election title in which the voter wants to vote
	 * @param vote list name we want to vote
	 * @return success or not
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String voteInAdvanceWeb(int ID, String election, String vote) throws java.rmi.RemoteException;
	/**
	 * gets all the possible list for a voter to vote
	 * @param ID ID of the voter who wants to vote
	 * @param election election in which the voter wants to vote
	 * @return arraylist with the name of the lists
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public ArrayList<String> listInAdvanceLists(int ID, String election) throws java.rmi.RemoteException;
	/**
	 * gets all elections where we can vote before time
	 * @param ID ID of the person who want to vote
	 * @return arraylist with the name of the elections
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public ArrayList<String> listInAdvanceElections(int ID) throws java.rmi.RemoteException;
	/**
	 * lists all elections
	 * @param election election we want to know the details about
	 * @return all the elections
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public ArrayList<String> electionDetails(String election) throws java.rmi.RemoteException;
	
}