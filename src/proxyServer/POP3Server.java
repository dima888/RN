package proxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import proxyServer.ServerAccountManagement.Info;

class POP3Server {
	
	//*********************** ATTRIBUTE *****************************
	
	/* Server, der Verbindungsanfragen entgegennimmt */
	private static final int SERVER_PORT = 11_003; // Auf diesen Port wird "gelauscht"
	private static Path dirPath = ServerAccountManagement.getDirPath(); // angabe des Pfades zum Verzeichnis, in welchem die Datein liegen
	private static int count = 0; // soll für uns die Clientanzahl zählen, die sich mit diesem Server verbunden hat
	
	//Anmeldedaten für genau einen Clienten in beispielsweise Thunderbird
//	private final String USER = "flah";
//	private final String PASS = "123";
	
	private final String USER = "foxhound"; //dima888@gmx.net
	private final String PASS = "12345678";
	
	//********************** KONSTRUKTOR *****************************	
	POP3Server(Path dirPath) {
		this.dirPath = dirPath;
	}
	
	//*********************** METHODEN *******************************	
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
	
	
	
	private class POP3_Server_Thread extends Thread {
		
		private int threadNumber = 0;	
		private Path path;
		
		private Socket socket;
		
		private BufferedReader inFromClient; //Liest Daten von Client ein 
		private DataOutputStream outToClient; //Daten senden zum Client
		
		boolean serviceRequested = true; //Server läuft so lange true ist
		
		private String ok = "+OK";
		private String err = "-ERR";
		
		POP3_Server_Thread(Socket socket, Path path) {
			this.socket = socket;
			this.path = path;
			this.threadNumber ++;
			
		}		
		
//		@Override
		public void run() {
			System.out.println("Der Client hat sich zu uns verbunden");
			

			
			//Erst muss eingeloggt werde, bevor request gestartet werden koennen 
			try {

				/* Socket-Basisstreams durch spezielle Streams filtern */
				inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outToClient = new DataOutputStream(socket.getOutputStream());

//				while (serviceRequested) {
				
				//writeToClient("+OK POP");
				
				String request = readFromClient();
				System.out.println(request);	
				
				
				request = readFromClient();
				System.out.println(request);
				
					while(authentication()) {
						
					}
					
					System.out.println("Authentication erfolgreich");			
					
					request = readFromClient();
					System.out.println(request);
					
					
				    request = readFromClient();
					System.out.println(request + "Request");
//				}

			} catch (IOException e) {
				System.out.println(e.toString() + "AAA");
			}

		}
					
		
		/**
		 * Liest die Eingabe von Client ein
		 * @return
		 * @throws IOException
		 */
		private String readFromClient() throws IOException {
			String request = "";
			request = inFromClient.readLine() + "\n" ;
			System.out.println("TCP Server sending to Thread: " + threadNumber  + " :" + request);
			return request;
		}
		
		/**
		 * Sendet eine Antwort an den Client
		 * @param reply
		 * @throws IOException
		 */
		private void writeToClient(String reply) throws IOException {
			outToClient.writeBytes(reply);
			System.out.println("TCP Server Thread " + threadNumber	+ " has written the message: " + reply);
		}		
		
		private boolean authentication() throws IOException {
			System.out.println("authentication Method");
			if(POP3_Server_Commands.user(readFromClient(), USER).compareTo("+OK") == 0) {
				System.out.println("authentication Username");
				writeToClient(ok + " Username accepted, password please");
				if(POP3_Server_Commands.password(readFromClient(), PASS).compareTo("+OK") == 0) {
					System.out.println("authentication Password");
					writeToClient(ok + " Password accept");
					return false;
				}
			}
		System.out.println();
			return true;
		}
	}
	
	//********************** TEST *********************
	public static void main(String[] args) {
		POP3Server server = new POP3Server(dirPath);
		server.startePOP3_Server();
	}
}