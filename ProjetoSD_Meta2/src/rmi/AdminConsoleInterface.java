package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * interface to use for callbacks on admin console
 * @author Jorge
 *
 */
public interface AdminConsoleInterface extends Remote{
	/**
	 * notifies admin console that a tcp console connected to the rmi server
	 * @throws RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public void notifyTable() throws RemoteException;
	/**
	 * notifies admin console that another admin console connected to the rmi server
	 * @throws RemoteException common superclass for a number of communication-related exceptions that may occur during the execution of a remote method call (mandatory)
	 */
	public void notifyAdmin() throws RemoteException;
}
