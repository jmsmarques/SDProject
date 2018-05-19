package admin.model;

import java.rmi.RemoteException;

import model.AdminBean;

public class CollegeBean extends AdminBean{

	private static final long serialVersionUID = 426347190825111262L;
	private String name;
	private String newName;
	
	public CollegeBean() {
		super();
	}

	
	public String getCollegeCreation() {
		String result;
		try {
			result = server.addCollege(name);
		} catch (RemoteException e) {
			result = handleCreationException();
		}
		return result;
	}
	
	public String getCollegeRemoval() {
		String result;
		try {
			result = server.removeCollege(name);
		} catch (RemoteException e) {
			result = handleRemoveException();
		}
		return result;
	}
	
	public String getCollegeChange() {
		String result;
		try {
			result = server.changeCollegeName(name, newName);
		} catch (RemoteException e) {
			result = handleChangeException();
		}
		return result;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	private String handleCreationException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.addCollege(name);
				return result;
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
		return "Operation fail";
	}
	
	private String handleRemoveException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.removeCollege(name);
				return result;
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
		return "Operation fail";
	}
	
	private String handleChangeException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.changeCollegeName(name, newName);
				return result;
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
		return "Operation fail";
	}
}
