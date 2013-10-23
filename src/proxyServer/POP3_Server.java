package proxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

class POP3_Server {
	
	/* Server, der Verbindungsanfragen entgegennimmt */
	private static final int SERVER_PORT = 11_000; // Auf diesen Port wird "gelauscht"
	private Path path;
	private static int count = 0;
	
	POP3_Server(Path path) {
		this.path = path;
	}
	
	void startePOP3_Server() {
		ServerSocket welcomeSocket; // ServerSocket zum lauschen
		Socket connectionSocket; // VerbindungsSocket mit Client
		
		int counter = 0; // Zähler für die verbundenen Clienten
		
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
				(new POP3_Server_Thread(connectionSocket, path)).start();
			}
			
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
	private class POP3_Server_Thread extends Thread{
		
		private int name;
		private RFC_1939_Parser parser = new RFC_1939_Parser();
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