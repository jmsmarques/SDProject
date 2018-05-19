package admin.model;

import java.rmi.RemoteException;

import model.AdminBean;

public class UserBean extends AdminBean{
	private static final long serialVersionUID = 4995728292147055452L;

	private int ID;
	private String name;
	private String password;
	private String type;
	private int cellphone;
	private String address;
	private String validityCC;
	private String election;
	
	public UserBean() {
		super();
	}

	//registers user
	public String getUserRegistration() {
		String result;
		try {
			result = server.registerPerson(ID, name, password, type, cellphone, address, validityCC);
		} catch (RemoteException e) {
			result = handleRegisterException();
		}
		return result;
	}

	//removes user
	public String getUserRemoval() {
		String result;
		try {
			result = server.removePerson(ID);
		} catch (RemoteException e) {
			result = handleRemoveException();
		}
		return result;
	}

	//changes user
	public String getUserChange() {
		String result;
		try {
			result = server.changePerson(ID, name, password, cellphone, address, validityCC, type);
		} catch (RemoteException e) {
			result = handleChangeException();
		}
		return result;
	}
	
	//gets user election info
	public String getUserElectionInfo() {
		String result;
		try {
			result = server.getVoterInfo(ID, election);
		} catch (RemoteException e) {
			result = handleInfoException();
		}
		return result;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCellphone(int cellphone) {
		this.cellphone = cellphone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setValidityCC(String validityCC) {
		this.validityCC = validityCC;
	}
	
	public void setElection(String election) {
		this.election = election;
	}
	public int getID() {
		return ID;
	}
	//failover protections
	private String handleRegisterException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.registerPerson(ID, name, password, type, cellphone, address, validityCC);
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
				result = server.removePerson(ID);
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
				result = server.changePerson(ID, name, password, cellphone, address, validityCC, type);
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
	
	private String handleInfoException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.getVoterInfo(ID, election);
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
