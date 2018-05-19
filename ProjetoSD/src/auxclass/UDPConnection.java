package auxclass;
import java.net.*;
import java.io.*;

public class UDPConnection {
	private DatagramSocket aSocket;
	private InetAddress targetHost;
	private String toSend;
	private byte[] bufferSend;
	private byte[] bufferReceive;
	private DatagramPacket ping; //datagram to send ping
	private DatagramPacket pingReceive; //datagram to receive ping
	
	public UDPConnection(int port, String hostname, int targetPort, int timeout) throws SocketException, UnknownHostException { //constructor
		aSocket = new DatagramSocket(port);
		System.out.println("Socket Datagram a escuta no porto " + port);
		toSend = "i"; //ping to send
		
		bufferSend = new byte[2];
		bufferSend = toSend.getBytes();
		bufferReceive = new byte[2];
		targetHost = InetAddress.getByName(hostname);
		ping = new DatagramPacket(bufferSend, bufferSend.length, targetHost, targetPort); //sets datagram to send ping
		pingReceive = new DatagramPacket(bufferReceive, bufferReceive.length); //sets datagram to receive ping
		
		aSocket.setSoTimeout(timeout); //sets the time the receive will block before assuming the other server went down
	}
	
	public void pingReceive() throws IOException, SocketTimeoutException { //receives a ping
		aSocket.receive(pingReceive);
		System.out.println("Ping received");	
	}
	
	public void pingSend() throws IOException { //sends a ping to an UDP socket
		aSocket.send(ping);
		System.out.println("Ping sent");
	}
	
	public void closeSocket() { //closes socket
		aSocket.close();
	}
}
