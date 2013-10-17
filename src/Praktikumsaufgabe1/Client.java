package Praktikumsaufgabe1;
 import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

	private final static int SERVER_PORT = 25;
	private Socket clientSocket;
	private boolean serviceRequested = true; // Bei true ist der Client beendet
	
	private DataOutputStream outToServer; // Ausgabestream zum Server
	private BufferedReader inFromServer; // Eingabestream vom Server
	
	public void connectToServer(String serverIp, int port) throws IOException {
			
		clientSocket = new Socket("localhost", SERVER_PORT);
		
		
	}
	
	 
}
