package admin.model;

import java.rmi.RemoteException;

import model.AdminBean;

public class DepartmentBean extends AdminBean{

	private static final long serialVersionUID = 6766833119225889935L;
	private String name;
	private String college;
	private String newName;
	
	public DepartmentBean() {
		super();
	}

	
	public String getDepartmentCreation() {
		String result;
		try {
			result = server.addDepartment(name, college);
		} catch (RemoteException e) {
			result = handleCreationException();
		}
		return result;
	}
	
	public String getDepartmentRemoval() {
		String result;
		try {
			result = server.removeDepartment(name);
		} catch (RemoteException e) {
			result = handleRemoveException();
		}
		return result;
	}
	
	public String getDepartmentChange() {
		String result;
		try {
			result = server.changeDepartment(name, newName, college);
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

	public void setCollege(String college) {
		this.college = college;
	}
	
	private String handleCreationException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.addDepartment(name, college);
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
				result = server.removeDepartment(name);
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
				result = server.changeDepartment(name, newName, college);
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
