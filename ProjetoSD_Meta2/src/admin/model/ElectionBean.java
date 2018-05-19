package admin.model;

import java.rmi.RemoteException;
import java.util.ArrayList;

import model.AdminBean;

public class ElectionBean extends AdminBean{

	private static final long serialVersionUID = -7604531547629189812L;
	String title;
	String startDate;
	String endDate;
	String type;
	String membersType;
	String description;

	public ElectionBean() {
		super();
	}

	//creates election
	public String getElectionCreation() {
		String result;
		try {
			result = server.createElection(title, startDate, endDate, type, membersType, description);
		} catch (RemoteException e) {
			result = handleCreationException();
		}
		return result;
	}

	//removes election
	public String getElectionRemoval() {
		String result;
		try {
			result = server.removeElection(title);
		} catch (RemoteException e) {
			result = handleRemoveException();
		}
		return result;
	}

	//changes election
	public String getElectionChange() {
		String result;
		try {
			result = server.changeElection(title, startDate, endDate, type, membersType, description);
		} catch (RemoteException e) {
			result = handleChangeException();
		}
		return result;
	}

	//gets election info
	public ArrayList<String> getElectionInfo() {
		ArrayList<String> result = new ArrayList<String>();
		try {
			result = server.getElectionInfo(title);
		} catch (RemoteException e) {
			result = handleInfoException();
		}
		return result;
	}
	
	//gets all elections
	public ArrayList<String> getAllElections() {
		ArrayList<String> result = new ArrayList<String>();
		try {
			result = server.electionDetails(title);
		} catch (RemoteException e) {
			result = handleDetailsException();
		}
		return result;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setMembersType(String membersType) {
		this.membersType = membersType;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//failover protections
	private String handleCreationException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.createElection(title, startDate, endDate, type, membersType, description);
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
				result = server.removeElection(title);
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
				result = server.changeElection(title, startDate, endDate, type, membersType, description);
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

	private ArrayList<String> handleInfoException() {
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.getElectionInfo(title);
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
		result.add("Operation fail");
		return result;
	}
	
	private ArrayList<String> handleDetailsException() {
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.electionDetails(title);
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
		result.add("Operation fail");
		return result;
	}

	public String getTitle() {
		return title;
	}
}
