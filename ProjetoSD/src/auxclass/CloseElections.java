package auxclass;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class CloseElections extends Thread{ //thread to check from time to time if an election need closing
	private CallableStatement stmt;
	
	public CloseElections(Connection conn) {
		try {
			stmt = conn.prepareCall("{call close_election}");
			this.start();
		} catch (SQLException e) {
			System.out.println("Error setting sql statement: " + e);
		}
	}
	
	public void run() {
		try {
			while(true) {
				stmt.executeUpdate();
				Thread.sleep(5000);
			}
		} catch (InterruptedException e) {
			System.out.println("Error on sleep: " + e);
		} catch (SQLException e) {
			System.out.println("Error on executing query to close elections: " + e);
		}
	}
}
