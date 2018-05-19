package websockets;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WebSocketInterface extends Remote{
	/**
	 * lists all active users
	 * @throws RemoteException
	 */
	public void list_users() throws RemoteException;
	/**
	 * lists all active and inactive tables
	 * @throws RemoteException
	 */
	public void list_tables() throws RemoteException;
	/**
	 * gets the number of users connected to an election
	 * @throws RemoteException
	 */
	public void users_nr() throws RemoteException;
}
