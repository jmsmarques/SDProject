package admin.model;

import java.rmi.RemoteException;

import model.AdminBean;

public class ListBean extends AdminBean{

	private static final long serialVersionUID = 1572152065869960661L;
	private int ID;
	private String name;
	private String membersType;
	private String election;

	public ListBean() {
		super();
	}

	//creates list
	public String getListCreation() {
		String result;
		try {
			result = server.addList(name, membersType, election);
		} catch (RemoteException e) {
			result = handleCreationException();
		}
		return result;
	}

	//removes list
	public String getListRemoval() {
		String result;
		try {
			result = server.removeList(name, election);
		} catch (RemoteException e) {
			result = handleRemoveException();
		}
		return result;
	}

	//adds person to list
	public String getAddPersonResult() {
		String result;
		try {
			result = server.addPersonToList(ID, name, election);
		} catch (RemoteException e) {
			result = handleAddException();
		}
		return result;
	}

	//removes person from list
	public String getRemovePersonResult() {
		String result;
		try {
			result = server.removePersonFromList(ID, name, election);
		} catch (RemoteException e) {
			result = handleRemovePersonException();
		}
		return result;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMembersType(String membersType) {
		this.membersType = membersType;
	}

	public void setElection(String election) {
		this.election = election;
	}

	//failover protections
	private String handleCreationException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.addList(name, membersType, election);
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
				result = server.removeList(name, election);
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

	private String handleAddException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.addPersonToList(ID, name, election);
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
				result = server.removePersonFromList(ID, name, election);
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
