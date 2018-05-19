package rmi;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminConsole extends UnicastRemoteObject implements AdminConsoleInterface{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5109182759911062977L;
	private Registry serverRegistry;
	private AdminRMIInterface server;
	private int timeout;
	private String rmiName;
	private Scanner keyboard;
	
	public AdminConsole(Registry sRegistry, AdminRMIInterface server, int timeout, String rmiName) throws RemoteException {
		super();
		
		serverRegistry = sRegistry;
		this.server = server;
		this.timeout = timeout;
		this.rmiName = rmiName;
		keyboard = new Scanner(System.in);
	}
	
	public boolean showMenu() { //shows menu options
		int choice = 0;
		int choice1 = 0;
		boolean go = true;
		String aux;
	
		//aux variables
		int ID;
		String election;
		String vote;
		
		System.out.println("1-Manage Person");
		System.out.println("2-Manage Colleges");
		System.out.println("3-Manage Departments");
		System.out.println("4-Manage an Election");
		System.out.println("5-Manage Lists");
		System.out.println("6-Manage Voting Tables");
		System.out.println("7-Vote In Advance");
		System.out.println("8-Get Number of votes in an election");
		System.out.println("0-Exit");
		System.out.print("Choice: ");
		
		aux = keyboard.nextLine();
		choice = Integer.parseInt(aux);
		
		switch(choice) {
			case 1:
				//clearScreen();
				System.out.println("1-Register Person");
				System.out.println("2-Remove Person");
				System.out.println("3-Change Person");
				System.out.println("4-Get Person Election Info");
				System.out.print("Choice: ");
				aux = keyboard.nextLine();
				choice1 = Integer.parseInt(aux);
				go = managePerson(choice1);
				break;
			case 3:
				//clearScreen();
				System.out.println("1-Add Department");
				System.out.println("2-Remove Department");
				System.out.println("3-Change Department");
				System.out.print("Choice: ");
				aux = keyboard.nextLine();
				choice1 = Integer.parseInt(aux);
				go = manageDepartment(choice1);
				break;
			case 2:
				//clearScreen();
				System.out.println("1-Add College");
				System.out.println("2-Remove College");
				System.out.println("3-Change College");
				System.out.print("Choice: ");
				aux = keyboard.nextLine();
				choice1 = Integer.parseInt(aux);
				go = manageCollege(choice1);
				break;
			case 4:
				//clearScreen();
				System.out.println("1-Create Election");
				System.out.println("2-Remove Election");
				System.out.println("3-Change Election");
				System.out.println("4-Get Election Info");
				System.out.print("Choice: ");
				aux = keyboard.nextLine();
				choice1 = Integer.parseInt(aux);
				go = manageElection(choice1);
				break;
			case 5:
				//clearScreen();
				System.out.println("1-Add List");
				System.out.println("2-Remove List");
				System.out.println("3-Add Person to List");
				System.out.println("4-Remove Person from List");
				System.out.print("Choice: ");
				aux = keyboard.nextLine();
				choice1 = Integer.parseInt(aux);
				go = manageList(choice1);
				break;
			case 6:
				//clearScreen();
				System.out.println("1-Add Voting Table");
				System.out.println("2-Remove Voting Table");
				System.out.println("3-Associate Person to Table");
				System.out.println("4-Disassociate Person from Table");
				System.out.print("Choice: ");
				aux = keyboard.nextLine();
				choice1 = Integer.parseInt(aux);
				go = manageVoterTable(choice1);
				break;
			case 7:
				//clearScreen();
				System.out.print("1-Person ID: ");
				aux = keyboard.nextLine();
				ID = Integer.parseInt(aux);
				System.out.print("2-Election Title: ");
				election = keyboard.nextLine();
				System.out.print("3-Vote(List Name): ");
				vote = keyboard.nextLine();
				try {
					System.out.println(server.voteInAdvance(ID, election, vote));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionVoteInAdvance(ID, election, vote);
				}
				break;
			case 8:
				//clearScreen();
				System.out.print("Election Title: ");
				election = keyboard.nextLine();
				try {
					System.out.println(server.getTableInfo(election));
				} catch (RemoteException e) {
					go = handleRemoteExceptionTableInfo(election);
				}
				break;
			case 0:
				go = false;
				break;
			default:
				System.out.println("Invalid Choice");	
		}
				
		return go;
	}
	
	private boolean managePerson(int choice) {
		//aux variables
		String aux;
		int ID;
		String name;
		String type;
		String password;
		int cellphone;
		String address;
		String validityCC;
		String election;
		//end of aux variables
		boolean go = true;
		
		switch(choice) {
			case 1:
				System.out.print("Person ID: ");
				aux = keyboard.nextLine();
				ID = Integer.parseInt(aux);
				System.out.print("Name: ");
				name = keyboard.nextLine();
				System.out.print("Type: ");
				type = keyboard.nextLine();
				System.out.print("Password: ");
				password = keyboard.nextLine();
				System.out.print("Cellphone:");
				aux = keyboard.nextLine();
				cellphone = Integer.parseInt(aux);
				System.out.print("Address: ");
				address = keyboard.nextLine();
				System.out.print("Validity CC(yyyy/mm/dd hh:mi:ss): ");
				validityCC = keyboard.nextLine();
				try {
					System.out.println(server.registerPerson(ID, name, password, type, cellphone, address, validityCC));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionRegisterVoter(ID, name, password, type, cellphone, address, validityCC);
				}  
				  break;
			case 2:
				System.out.print("Person ID: ");
				aux = keyboard.nextLine();
				ID = Integer.parseInt(aux);
				try {
					System.out.println(server.removePerson(ID));
				} catch(RemoteException e1) {
					go = handleRemoteExceptionRemoveVoter(ID);
				}
				break;
			case 3:
				System.out.print("Person ID: ");
				aux = keyboard.nextLine();
				ID = Integer.parseInt(aux);
				System.out.print("Name: ");
				name = keyboard.nextLine();
				System.out.print("Type: ");
				type = keyboard.nextLine();
				System.out.print("Password: ");
				password = keyboard.nextLine();
				System.out.print("Cellphone:");
				aux = keyboard.nextLine();
				cellphone = Integer.parseInt(aux);
				System.out.print("Address: ");
				address = keyboard.nextLine();
				System.out.print("Validity CC(yyyy/mm/dd hh:mi:ss): ");
				validityCC = keyboard.nextLine();
				try {
					System.out.println(server.changePerson(ID, name, password, cellphone, address, validityCC, type));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionChangeVoter(ID, name, password, cellphone, address, validityCC, type);
				}
				break;
			case 4:
				System.out.print("Person ID: ");
				aux = keyboard.nextLine();
				ID = Integer.parseInt(aux);
				System.out.print("Election: ");
				election = keyboard.nextLine();
				try { 
					System.out.println(server.getVoterInfo(ID, election));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionVoterInfo(ID, election);
				}
		}
		return go;
	}
	
	private boolean manageDepartment(int choice) {
		String name;
		String college;
		String newName;
		boolean go = true;
		
		//clearScreen();
		switch(choice) {
			case 1:
				System.out.print("Department name: ");
				name = keyboard.nextLine();
				System.out.print("College name: ");
				college = keyboard.nextLine();
				try {
					System.out.println(server.addDepartment(name, college));
				} catch (RemoteException e) {
					go = handleRemoteExceptionAddDepartment(name, college);
				}
				break;
			case 2:
				System.out.print("Department name: ");
				name = keyboard.nextLine();
				try {
					System.out.println(server.removeDepartment(name));
				} catch (RemoteException e) {
					go = handleRemoteExceptionRemoveDepartment(name);
				}
				break;
			case 3:
				System.out.print("Department name: ");
				name = keyboard.nextLine();
				System.out.print("New name: ");
				newName = keyboard.nextLine();
				System.out.print("New college: ");
				college = keyboard.nextLine();
				try {
					System.out.println(server.changeDepartment(name, newName, college));
				} catch (RemoteException e) {
					go = handleRemoteExceptionChangeDepartment(name, newName, college);
				}
				break;
			default: 
				System.out.println("Invalid Choice");
		}
		return go;
	}
	
	private boolean manageCollege(int choice) {
		String name;
		String newName;
		boolean go = true;
		
		//clearScreen();
		switch(choice) {
			case 1:
				System.out.print("College name: ");
				name = keyboard.nextLine();
				try {
					System.out.println(server.addCollege(name));
				} catch (RemoteException e) {
					go = handleRemoteExceptionAddCollege(name);
				}
				break;
			case 2:
				System.out.print("College name: ");
				name = keyboard.nextLine();
				try {
					System.out.println(server.removeCollege(name));
				} catch (RemoteException e) {
					go = handleRemoteExceptionRemoveCollege(name);
				}
				break;
			case 3:
				System.out.print("College name: ");
				name = keyboard.nextLine();
				System.out.print("New name: ");
				newName = keyboard.nextLine();
				try {
					System.out.println(server.changeCollegeName(name, newName));
				} catch (RemoteException e) {
					go = handleRemoteExceptionChangeCollege(name, newName);
				}
				break;
			default:
				System.out.println("Invalid Choice");
		}
		return go;
	}
	
	private boolean manageElection(int choice) {
		String title;
		String startDate;
		String endDate;
		String type;
		String membersType;
		String description;
		String election;
		boolean go = true;
		
		//clearScreen();
		switch(choice) {
			case 1:
				System.out.print("Election Title: ");
				title = keyboard.nextLine();
				System.out.print("Start Date: ");
				startDate = keyboard.nextLine();
				System.out.print("End Date: ");
				endDate = keyboard.nextLine();
				System.out.print("Election Type: ");
				type = keyboard.nextLine();
				System.out.print("Members Type: ");
				membersType = keyboard.nextLine();
				System.out.print("Election Description: ");
				description = keyboard.nextLine();
				try {
					System.out.println(server.createElection(title, startDate, endDate, type, membersType, description));
				} catch (RemoteException e) {
					go = handleRemoteExceptionCreateElection(title, startDate, endDate, type, membersType, description);
				}
				break;
			case 2:
				System.out.print("Election title: ");
				election = keyboard.nextLine();
				try {
					System.out.println(server.removeElection(election));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionRemoveElection(election);
				}
				break;
			case 3:
				System.out.print("Election Title: ");
				title = keyboard.nextLine();
				System.out.print("New Start Date: ");
				startDate = keyboard.nextLine();
				System.out.print("New End Date: ");
				endDate = keyboard.nextLine();
				System.out.print("New Election Type: ");
				type = keyboard.nextLine();
				System.out.print("New Members Type: ");
				membersType = keyboard.nextLine();
				System.out.print("New Election Description: ");
				description = keyboard.nextLine();
				try {
					System.out.println(server.changeElection(title, startDate, endDate, type, membersType, description));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionChangeElection(title, startDate, endDate, type, membersType, description);
				}
				break;
			case 4:
				ArrayList<String> auxA;
				System.out.print("Election Title: ");
				election = keyboard.nextLine();
				try { 
					auxA = server.getElectionInfo(election);
					for(int i = 0; i < auxA.size(); i++) {
						System.out.println(auxA.get(i));
					}
				} catch (RemoteException e1) {
					go = handleRemoteExceptionElectionInfo(election);
				}
				break;
			default:
				System.out.println("Invalid Choice");
		}
		return go;
	}

	private boolean manageList(int choice) {
		String name;
		String membersType;
		String election;
		String aux;
		int ID;
		boolean go = true;
		
		//clearScreen();
		switch(choice) {
			case 1:
				System.out.print("List Name : ");
				name = keyboard.nextLine();
				System.out.print("List Members Type: ");
				membersType = keyboard.nextLine();
				System.out.print("Election Title: ");
				election = keyboard.nextLine();
				try {
					System.out.println(server.addList(name, membersType, election));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionAddList(name, membersType, election);
				}
				break;
			case 2:
				System.out.print("List Name: ");
				name = keyboard.nextLine();
				System.out.print("Election Title: ");
				election = keyboard.nextLine();
				try {
					System.out.println(server.removeList(name, election));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionRemoveList(name, election);
				}
				break;
			case 3:
				System.out.print("Person ID: ");
				aux = keyboard.nextLine();
				ID = Integer.parseInt(aux);
				System.out.print("List Name: ");
				name = keyboard.nextLine();
				System.out.print("Election Title: ");
				election = keyboard.nextLine();
				try {
					System.out.println(server.addPersonToList(ID, name, election));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionAddPersonList(ID, name, election);
				}
				break;
			case 4:
				System.out.print("Person ID: ");
				aux = keyboard.nextLine();
				ID = Integer.parseInt(aux);
				System.out.print("List Name: ");
				name = keyboard.nextLine();
				System.out.print("Election Title: ");
				election = keyboard.nextLine();
				try {
					System.out.println(server.removePersonFromList(ID, name, election));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionRemovePersonList(ID, name, election);
				}
				break;
			default:
				System.out.println("Invalid Choice");
		}
		return go;
	}
	
	private boolean manageVoterTable(int choice) {
		String election;
		String department;
		String aux;
		int ID;
		boolean go = true;
		
		//clearScreen();
		switch(choice) {
			case 1:
				System.out.print("Election Title: ");
				election = keyboard.nextLine();
				System.out.print("Department Name: ");
				department = keyboard.nextLine();
				try {
					System.out.println(server.addVotingTable(department, election));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionAddVotingTable(department, election);
				}
				break;
			case 2:
				System.out.print("Election Title: ");
				election = keyboard.nextLine();
				System.out.print("Department Name: ");
				department = keyboard.nextLine();
				try {
					System.out.println(server.removeVotingTable(department, election));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionRemoveVotingTable(department, election);
				}
				break;
			case 3:
				System.out.print("Person ID: ");
				aux = keyboard.nextLine();
				ID = Integer.parseInt(aux);
				System.out.print("Election Title: ");
				election = keyboard.nextLine();
				System.out.print("Table Department Name: ");
				department = keyboard.nextLine();
				try {
					System.out.println(server.associatePersonToTable(ID, department, election));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionAssociatePersonToVotingTable(ID, department, election);
				}
				break;
			case 4:
				System.out.print("Person ID: ");
				aux = keyboard.nextLine();
				ID = Integer.parseInt(aux);
				try {
					System.out.println(server.disassociatePersonToTable(ID));
				} catch (RemoteException e1) {
					go = handleRemoteExceptionDisassociatePersonToVotingTable(ID);
				}
				break;
			default:
				System.out.println("Invalid Choice");
		}
		return go;
	}
	
	
	private void lookupRMI() throws RemoteException, NotBoundException {
		server = (AdminRMIInterface) serverRegistry.lookup(rmiName);
	}

	//functions to handle rmi failure
	private boolean handleRemoteExceptionTableInfo(String election) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.getTableInfo(election));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionElectionInfo(String election) {
		ArrayList<String> auxA;
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				auxA = server.getElectionInfo(election);
				for(int n = 0; n < auxA.size(); n++) {
					System.out.println(auxA.get(n));
				}
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionRegisterVoter(int ID, String name, String password, String type, int cellphone, String address, String validityCC) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.registerPerson(ID, name, password, type, cellphone, address, validityCC));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionChangeVoter(int ID, String name, String password, int cellphone, String address, String validityCC, String type) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.changePerson(ID, name, password, cellphone, address, validityCC, type));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionRemoveVoter(int ID) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.removePerson(ID));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	private boolean handleRemoteExceptionVoterInfo(int ID, String election) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.getVoterInfo(ID, election));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionAddVotingTable(String department, String election) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.addVotingTable(department, election));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionRemoveVotingTable(String department, String election) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.removeVotingTable(department, election));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionAssociatePersonToVotingTable(int ID, String department, String election) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.associatePersonToTable(ID, department, election));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionDisassociatePersonToVotingTable(int ID) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.disassociatePersonToTable(ID));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionAddList(String name, String membersType, String election) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.addList(name, membersType, election));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionRemoveList(String name, String election) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.removeList(name, election));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionAddPersonList(int ID, String list, String election) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.addPersonToList(ID, list, election));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionRemovePersonList(int ID, String list, String election) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.removePersonFromList(ID, list, election));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionCreateElection(String title, String startDate, String endDate, String type, String membersType, String description) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.createElection(title, startDate, endDate, type, membersType, description));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionRemoveElection(String election) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.removeElection(election));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionChangeElection(String election, String newDate, String newFinishDate, String newType, String newMembersType, String newDescription) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.changeElection(election, newDate, newFinishDate, newType, newMembersType, newDescription));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionAddCollege(String name) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.addCollege(name));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionRemoveCollege(String name) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.removeCollege(name));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionChangeCollege(String name, String newName) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.changeCollegeName(name, newName));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionAddDepartment(String name, String college) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.addDepartment(name, college));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionRemoveDepartment(String name) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.removeDepartment(name));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}	
		return false;
	}
	
	private boolean handleRemoteExceptionChangeDepartment(String name, String newName, String college) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.changeDepartment(name, newName, college));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}
	
	private boolean handleRemoteExceptionVoteInAdvance(int ID, String election, String vote) {
		for(int i = 0; true; i += 5000) {
			try {
				Thread.sleep(5000);
				System.out.print(".");
				lookupRMI();
				System.out.println(server.voteInAdvance(ID, election, vote));
				return true;
			} catch (InterruptedException e1) {
				System.out.println("Error on sleep: " + e1);
				break;
			} catch (RemoteException e1) {
				if(i >= timeout) {
					System.out.println("Lost RMI connection");
					break;
				}
			} catch (NotBoundException e) {
				System.out.println("Error on lookup: " + e);
				break;
			}
		}
		return false;
	}

	@Override
	public void notifyTable() throws RemoteException {
		System.out.println("Table Connected");
	}
	
	@Override
	public void notifyAdmin() throws RemoteException {
		System.out.println("Admin Table Connected");
	}
}