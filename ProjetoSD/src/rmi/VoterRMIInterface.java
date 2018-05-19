package rmi;

import java.rmi.Remote;

public interface VoterRMIInterface extends Remote{
	/**
	 * function to inform rmi server that a tcp table connected
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public void sayHellos() throws java.rmi.RemoteException;
	/**
	 * identifies a voter to unlock a voting terminal
	 * @param ID identification number of cartao de cidadao of the person who wants to vote
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String identifyVoter(int ID) throws java.rmi.RemoteException;
	/**
	 * voter authenticates himself in the unlocked terminal to vote
	 * @param ID identification number of cartao de cidadao of the person who wants to vote
	 * @param password password of the voter
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String authenticateVoter(int ID, String password) throws java.rmi.RemoteException;
	/**
     * authenticates person to operate table
     * @param ID identification number of person who wants to authenticate
     * @param eleicao name of the election to authenticate
     * @param dep name of the departement to authenticate
     * @return success or not
     * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
     */
    public String deauthenticateVoterInTable(int ID,String eleicao,String dep) throws java.rmi.RemoteException;   ///V
    /**
     * authenticates person to operate table
     * @param ID identification number of person who wants to authenticate
     * @param password password of the person who wants to authenticate
     * @param eleicao name of the election to authenticate
     * @param dep name of the departement to authenticate
     * @return success or not
     * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
     */
    public String authenticateVoterInTable(int ID, String password,String eleicao,String dep) throws java.rmi.RemoteException;   ///V
	/**
	 * vote is cast in favor of a list or null
	 * @param ID identification number of the person who voted
	 * @param election title of the election where the voter wants to vote
	 * @param list name of the list which the voter wants to give his vote to (can be null)
	 * @param departamento department name where the table where the vote was cast is located
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String vote(int ID, String election, String list, String departamento) throws java.rmi.RemoteException;
	/**
	 * identifies a voter to lock a voting terminal
	 * @param ID identification number of cartao de cidadao of the person who timeout
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String lockVoter(int ID) throws java.rmi.RemoteException;
	/**
	 * shows all available lists to vote for a certain election from a certain type
	 * @param election election which we want to see the candidate list
	 * @param ID identification number of cartao de cidadao of the person who wants to see the lists
	 * @return string with the result of the query (number of rows + ; + info + | + info + | ...
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String listLists(String election, int ID) throws java.rmi.RemoteException;
	/**
	 * shows all available lists to vote for a certain election from a certain type
	 * @param election election which we want to see the candidate list
	 * @param ID identification number of cartao de cidadao of the person who wants to see the lists
	 * @return string with the result of the query (number of rows + ; + info + | + info + | ...
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String listElections( int ID) throws java.rmi.RemoteException;
	/**
	 * identifies a voter to lock a voting terminal
	 
	 * @param token identification facebook token
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String faceLog(String token) throws java.rmi.RemoteException;
	/**
	 * associates a voter to facebook
	 * @param ID identification number of cartao de cidadao of the person who timeout
	 * @param token identification facebook token
	 * @return result(success or not)
	 * @throws java.rmi.RemoteException  common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String faceAss(String token,int ID) throws java.rmi.RemoteException;
	/**
	 * allows voters to vote in advance for a certain election in the web
	 * @param ID ID of the voter who wants to vote in advance
	 * @param election election title in which the voter wants to vote
	 * @param vote list name we want to vote
	 * @return success or not
	 * @throws java.rmi.RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public String voteInAdvanceWeb(int ID, String election, String vote) throws java.rmi.RemoteException;
	
}
