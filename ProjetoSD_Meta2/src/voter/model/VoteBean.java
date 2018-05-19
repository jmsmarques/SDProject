package voter.model;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import model.VoterBean;
import rmi.VoterRMIInterface;


public class VoteBean extends  VoterBean{
	
	
		private static final long serialVersionUID = 4L;
	
		private String resp;
		private ArrayList<String> lista;
		
		
		
		public VoteBean(String election,String username,String list) {
			super();
			setVote(election,username,list);
		}
		
		public String getResp() {
			return resp;
			
		}
		
		

		public void setVote(String election,String username,String list) {
			String result="fail";
		
			//ArrayList<String> list = new ArrayList<>();
			
			try {
				System.out.println(username + " " + election + " " + list);
				result = server.voteInAdvanceWeb(Integer.parseInt(username),election,	list);
				
			
			} catch (RemoteException e) {
				result = handleElectionVoteException(election, username, list);
			}
			System.out.println("RESULT:" +result);
			setResp(result);
			
		}
		private void setResp(String result) {
			this.resp=result;
			
		}

		private String handleElectionVoteException(String election,String username,String list) {
			String result;
			for(int i = 0; true; i += 5000) {
				try {
					Thread.sleep(5000);
					System.out.print(".");
					serverRegistry = (Registry) LocateRegistry.getRegistry("localhost", 7000);
					server = (VoterRMIInterface)serverRegistry.lookup("porto");
					result = server.vote(Integer.parseInt(username),election,	list,"Web");
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

		public ArrayList<String> getLista() {
			return lista;
		}

		public void setLista(ArrayList<String> lista) {
			this.lista = lista;
		}


	
}
