package voter.model;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import model.VoterBean;
import rmi.VoterRMIInterface;




public class FacebookBean extends  VoterBean {


	private static final long serialVersionUID = 4L;
	private int num;
	private String resp;
	private String username;
	private String id;
	
	
	
	public FacebookBean(String username,String id) {
		super();
		setUsername(username);
		setId(id);
		setFacebookUser( username,id);
	}
	
	
	
	

	public void setFacebookUser(String username,String id) {
		String result=null;
		try {
			
			result = server.faceAss(username,Integer.parseInt(id));
			
			
		} catch (RemoteException e) {
			result = handleFaceAssException();
		}
		if(result.equals("success")) {
			setResp("success");
		}else {
			setResp("fail");
		}
		
		
	}
	private String handleFaceAssException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				serverRegistry = (Registry) LocateRegistry.getRegistry("localhost", 7000);
				server = (VoterRMIInterface)serverRegistry.lookup("porto");
				result = server.faceAss(getUsername(),Integer.parseInt(getId()));
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





	public String getUsername() {
		return username;
	}





	public void setUsername(String username) {
		this.username = username;
	}





	public String getId() {
		return id;
	}





	public void setId(String id) {
		this.id = id;
	}

	



}
