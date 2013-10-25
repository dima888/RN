package proxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

class POP3Server {
	
	/* Server, der Verbindungsanfragen entgegennimmt */
	private static final int SERVER_PORT = 11_000; // Auf diesen Port wird "gelauscht"
	private Path dirPath = Paths.get("C:\\Users\\Flah\\Desktop\\Emails"); // angabe des Pfades zum Verzeichnis, in welchem die Datein liegen
	private static int count = 0; // soll für uns die Clientanzahl zählen, die sich mit diesem Server verbunden hat
	
	POP3Server(Path dirPath) {
		this.dirPath = dirPath;
	}
	
	void startePOP3_Server() {
		ServerSocket welcomeSocket; // ServerSocket zum lauschen
		Socket connectionSocket; // VerbindungsSocket mit Client
		
		try {
			welcomeSocket = new ServerSocket(SERVER_PORT);
			
			while(true) {
				System.out.println("TCP Server: Lauschen auf Port: " + SERVER_PORT);
				
				/*
				 * Blockiert MainThread, wartet auf Verbindungsanfrage --> nach
				 * Verbindungsaufbau Standard-Socket erzeugen und
				 * connectionSocket zuweisen
				 */
				connectionSocket = welcomeSocket.accept();
				
				// Neuen Arbeits-Thread erzeugen und starten mit übergebenen Socket
				(new POP3_Server_Thread(connectionSocket, dirPath)).start();
			}
			
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
	private class POP3_Server_Thread extends Thread{
		
		private int name;	
		private Path path;
		
		private Socket socket;
		
		private BufferedReader inFromClient; //Liest Daten von Client ein 
		private DataOutputStream outToClient; //Daten senden zum Client
		
		boolean serviceRequested = true; //Server läuft so lange true ist
		
		POP3_Server_Thread(Socket socket, Path path) {
			this.socket = socket;
			this.path = path;
			this.name = count;
			count++;
		}
		
		@Override
		public void run() {
			
		}
	}
}