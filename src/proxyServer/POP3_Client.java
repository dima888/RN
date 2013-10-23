package proxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.Scanner;

class POP3_Client {
	
	/* Server, der Verbindungsanfragen entgegennimmt */
	private final String IP;
	private final int SERVER_PORT; // Auf diesen Port wird "gelauscht"
	private Path path;
	private static int count = 0;
	
	Socket socket; // VerbindungsSocket mit Client
	
	private DataOutputStream outToServer; //Ausgabestream zum Server 
	private BufferedReader inFromServer; // Eingabestream vom Server

	private boolean serviceRequested = true; // Client läuft solange true
	
	POP3_Client(final String IP, final int SERVER_PORT) {
		this.IP = IP;
		this.SERVER_PORT = SERVER_PORT;
	}
	
	// TODO
	void startePOP3_Client() {		
		/* Client starten. Ende, wenn quit eingegeben wurde */
		Scanner inFromUser; //Zum Auslesen der Benutzereingaben
		String sentence; // vom User übergebener String
		String modifiedSentence; // vom Server modifizierter String
		
		try {
			socket = new Socket(IP, SERVER_PORT); //Verbindung aufbauen
			
			/* Socket-Basisstreams durch spezielle Streams filtern */
			outToServer = new DataOutputStream(socket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			/* Konsolenstream (Standardeingabe) initialisieren */
			inFromUser = new Scanner(System.in);
			
			while (serviceRequested) {
				System.out.println("ENTER TCP-DATA: ");
				/* String vom Benutzer (Konsoleneingabe) holen */
				sentence = inFromUser.nextLine();

				/* String an den Server senden */
				writeToServer(sentence);

				/* Modifizierten String vom Server empfangen */
				modifiedSentence = readFromServer();

				/* Test, ob Client beendet werden soll */
				if (modifiedSentence.indexOf("QUIT") > -1) {
					serviceRequested = false;
				}
			}
			
		} catch (IOException e) {
			System.out.println("TCP Verbindung konnte zum Server nicht hergestellt werden");
		}
	}
	
	void writeToServer(String request) throws IOException {
		outToServer.writeBytes(request + '\n');
		System.out.println("TCP Client write: " + request);
		
	}
	
	String readFromServer() {
		String reply = "";
		
		
		return reply;
	}
}
