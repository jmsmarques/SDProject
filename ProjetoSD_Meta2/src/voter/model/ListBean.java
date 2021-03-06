package voter.model;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import model.VoterBean;
import rmi.VoterRMIInterface;

public class ListBean extends  VoterBean {
	private static final long serialVersionUID = 4L;
	private int num;
	private String election;
	private ArrayList<String> lista;
	
	
	
	public ListBean(String election,String username) {
		super();
		setElection(election,username);
	}
	
	public String getElection() {
		return election;
		
	}
	
	

	public void setElection(String election,String username) {
		String result;
		String aux2[];
		ArrayList<String> list = new ArrayList<>();
		
		try {
			result = server.listLists(election,Integer.parseInt(username));
			String aux[]=result.split(";");
			num=Integer.parseInt(aux[0]);
			if(num==0) {
				setLista(null);
			}else {
				
			
			for(int i=1;i<aux.length;i++) {
				aux2=aux[i].split("\\|");
				list.add(aux2[1]);
				System.out.println(aux2[1]+" ADDED!");
				this.setLista(list);
				
			}
			}
		} catch (RemoteException e) {
			result = handleElectionsListException();
		}
		this.election=result;	
		
	}
	private String handleElectionsListException() {
		String result;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				serverRegistry = (Registry) LocateRegistry.getRegistry("localhost", 7000);
				server = (VoterRMIInterface)serverRegistry.lookup("porto");
				result = server.listLists("eleicao1",123123123);
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

