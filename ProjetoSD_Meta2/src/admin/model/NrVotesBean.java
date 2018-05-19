package admin.model;

import java.rmi.RemoteException;

import model.AdminBean;

public class NrVotesBean extends AdminBean{
	
	private static final long serialVersionUID = -9189266458132732446L;
	private String election;
	
	public NrVotesBean() {
		super();
	}
	
	public String getNrVotes() {
		String result;
		try {
			result = server.getTableInfo(election);
		} catch (RemoteException e) {
			result = handleNrVoteException();
		}
		return result;
	}

	public void setElection(String election) {
		this.election = election;
	}
	
	//failover protections
	private String handleNrVoteException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				result = server.getTableInfo(election);
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
