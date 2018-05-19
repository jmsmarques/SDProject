package admin.model;

import java.rmi.RemoteException;

import model.AdminBean;

public class TableBean extends AdminBean{

	private static final long serialVersionUID = 6766833119225889935L;
	private int ID;
	private String department;
	private String election;
	
	public TableBean() {
		super();
	}

	
	public String getTableCreation() {
		String result;
		try {
			result = server.addVotingTable(department, election);
		} catch (RemoteException e) {
			result = handleCreationException();
		}
		return result;
	}
	
	public String getTableRemoval() {
		String result;
		try {
			result = server.removeVotingTable(department, election);
		} catch (RemoteException e) {
			result = handleRemoveException();
		}
		return result;
	}
	
	public String getTableAddPerson() {
		String result;
		try {
			result = server.associatePersonToTable(ID, department, election);
		} catch (RemoteException e) {
			result = handleAddPersonException();
		}
		return result;
	}
	
	public String getTableRemovePerson() {
		String result;
		try {
			result = server.disassociatePersonToTable(ID);
		} catch (RemoteException e) {
			result = handleRemovePersonException();
		}
		return result;
	}
	
	public void setID(int iD) {
		ID = iD;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public void setElection(String election) {
		this.election = election;
	}
	
	private String handleCreationException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.addVotingTable(department, election);
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
				result = server.removeVotingTable(department, election);
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
	
	private String handleAddPersonException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.associatePersonToTable(ID, department, election);
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
	
	private String handleRemovePersonException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.disassociatePersonToTable(ID);
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
