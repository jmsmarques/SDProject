package login.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import model.VoterBean;
import rmi.VoterRMIInterface;

public class FacebookLoginBean extends  VoterBean {





	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	private int num;
	private String resp;
	private String username;
	private String id;



	public FacebookLoginBean(String username) {
		super();
		setUsername(username);
		setId(getId(username));

	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getResp() {
		return resp;
	}
	public void setResp(String resp) {
		this.resp = resp;
	}
	public String getId(String username) {

		String result=null;
		try {

			result = server.faceLog(username);
			System.out.println(result);

		} catch (RemoteException e) {
			result = handleFaceLogException();
		}
		return result;
		
	}
	private String handleFaceLogException() {

		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				serverRegistry = (Registry) LocateRegistry.getRegistry("localhost", 7000);
				server = (VoterRMIInterface)serverRegistry.lookup("porto");
				String result = server.faceLog(getUsername());
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







	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
