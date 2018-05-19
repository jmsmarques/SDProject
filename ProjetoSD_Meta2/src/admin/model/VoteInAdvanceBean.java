package admin.model;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.List;

import model.AdminBean;

public class VoteInAdvanceBean extends AdminBean{
	
	private static final long serialVersionUID = 3392280745255718689L;
	private int ID;
	private String election;
	private String vote;
	private List <String> lists;
	private List <String> elections;
	
	public VoteInAdvanceBean() {
		super();
	}

	public String getVoteResult() {
		String result;
		try {
			result = server.voteInAdvanceWeb(ID, election, vote);
		} catch (RemoteException e) {
			result = handleVoteException();
		}
		return result;
	}
	
	public List<String> getListsList() {
		ArrayList <String>result = new ArrayList<String>();
		try {
			result = server.listInAdvanceLists(ID, election);
		} catch (RemoteException e) {
			result = handleListListsException();
		}
		return result;
	}
	
	public List<String> getElectionsList() {
		ArrayList <String>result = new ArrayList<String>();
		try {
			result = server.listInAdvanceElections(ID);
		} catch (RemoteException e) {
			result = handleListElectionsException();
		}
		return result;
	}
	
	public void setID(int iD) {
		ID = iD;
	}

	public void setElection(String election) {
		this.election = election;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}
	
	//failover protections
	private String handleVoteException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.voteInAdvanceWeb(ID, election, vote);
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
	
	private ArrayList<String> handleListListsException() {
		ArrayList <String> result = new ArrayList<String>();
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.listInAdvanceLists(ID, election);
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
		return null;
	}
	
	private ArrayList<String> handleListElectionsException() {
		ArrayList <String> result = new ArrayList<String>();
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.listInAdvanceElections(ID);
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
		return null;
	}

	public void setLists(List<String> lists) {
		this.lists = lists;
	}

	public List<String> getLists() {
		return lists;
	}

	public List<String> getElections() {
		return elections;
	}

	public void setElections(List<String> elections) {
		this.elections = elections;
	}
}
