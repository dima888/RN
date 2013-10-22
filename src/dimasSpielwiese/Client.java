package dimasSpielwiese;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public final int SERVER_PORT = 7000; //Warum public?
	public static final String SERVER_IP = "192.168.0.104"; //Warum public?
	
	private Socket clientSocket; //TCP standart Socketklasse
	
	private DataOutputStream outToServer; //Ausgabenstream zum Server
	private BufferedReader inFromServer; //Eingabestream vom Server
	
	private boolean serverRequested = true; //Client läuft so lange true 
	
	public void letsGo() {
		Scanner inputFromUser; //Zum auslesen was der user eingibt
		String sentence; //Von User übergebener String
		String modifiedSentence; //Vom Server modifizierter String
		
		try {
			clientSocket = new Socket(SERVER_IP, SERVER_PORT); //Socket erzeugt sowie die verbindungaufbau initiiert
			
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			
		} catch (IOException e) {
			System.out.println("Connection aborted by server!");
		}
	}
	
	private void writeToServer(String request) throws IOException {
		outToServer.writeBytes(request + "\n"); //Senden einer Zeile zum Server
		System.out.println("TCP client has send the message: " + request);		
	}
	
	private String readFromServer() throws IOException {
		String reply = inFromServer.readLine();
		System.out.println("TCP client got from server: " + reply);
		return reply;
	}
	
	public static void main(String[] args) {
		Client client = new Client();
		client.letsGo();
	}
	
}
