package login.model;

import model.VoterBean;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class LoginBean extends VoterBean{

	private static final long serialVersionUID = -1667642418145969554L;
	private String username; // username and password supplied by the user
	private String password;

	public LoginBean() {
		super();
	}

	public boolean getAdminPassword() {
		if(username.equals("admin") && password.equals("admin"))
			return true;
		else
			return false;
	}
	
	public boolean getAuthentication() {
		int ID;
		String result;
		if(!username.matches("\\d+")) { //username is not an int
			return false;
		}
		ID = Integer.parseInt(username);
		try {
			server.identifyVoter(ID); //identifies voter
			result = server.authenticateVoter(ID, password); //authenticastes voter
		} catch (RemoteException e) {
			result = handleConnectionException(ID);
		}
		
		if(result.equals("Welcome")) {
			return true;
		}
		else 
			return false;
	}
	
	public void getLogout() {
		int ID;
		if(!username.matches("\\d+")) { //username is not an int
			return;
		}
		ID = Integer.parseInt(username);
		try {
			server.lockVoter(ID); 
		} catch (RemoteException e) {
			handleLogoutException(ID);
		}
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	private String handleConnectionException(int ID) {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				server.identifyVoter(ID); //identifies voter
				result = server.authenticateVoter(ID, password); //authenticastes voter
				return result;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= 30000) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return "Operation fail";
	}
	
	private void handleLogoutException(int ID) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				server.lockVoter(ID); //authenticastes voter
				return;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= 30000) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
	}

	
}
