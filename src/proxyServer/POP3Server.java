package proxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import proxyServer.ServerAccountManagement.Info;

class POP3Server {
	
	//*********************** ATTRIBUTE *****************************
	
	/* Server, der Verbindungsanfragen entgegennimmt */
	private static final int SERVER_PORT = 11000; // Auf diesen Port wird "gelauscht"
	
	//Anmeldedaten für genau einen Clienten in beispielsweise Thunderbird
//	private final String USER = "flah";
//	private final String PASS = "123";
	
	private static final String USER = "dima888@gmx.net"; //foxhound
	private static final String PASS = "12345678";
	
	POP3_Server_Commands server_commands;	
	
	// *********************GETTER**********************
	
	public static String getUser() {
		return USER;
	}
	
	public static String getPassword() {
		return PASS;
	}
	
	//********************** KONSTRUKTOR *****************************	
	
	POP3Server(POP3_Server_Commands server_commands) {
		this.server_commands = server_commands;
		startePOP3_Server();
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
				(new POP3_Server_Thread(connectionSocket, server_commands.getDirPath())).start();
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
		
		//************* KONSTRUKTOR *********************
		POP3_Server_Thread(Socket socket, Path path) {
			this.socket = socket;
			this.path = path;
			this.threadNumber ++;
		}		
		
		//************************* SUPER METHODE; PRUEFT OB BEFEHL LAUT RFC 1939 DEFINIERT IST ***********************		 
		  String checkAllCommand(String clientCommand) {
			 
			 Scanner scanner = new Scanner(clientCommand);
			 String firstPartCommand = scanner.next();
			 String secondPartCommand = "";
			 
			 if(scanner.hasNext()) {
				 secondPartCommand = scanner.next();
			 }
			 
			 
			 switch(firstPartCommand.toLowerCase()) {
			 //{user, pass, quit, stat, list, retr, dele, noop, rset}
			 case "user" : return server_commands.user(secondPartCommand); 
			 case "pass" : return server_commands.password(secondPartCommand); 
			 case "quit" : return server_commands.quit(socket);
			 case "stat" : return server_commands.stat(); 
			 case "list" : if(secondPartCommand.isEmpty()) {return server_commands.list();} else {return server_commands.list(secondPartCommand);}
			 case "retr" : return server_commands.retr(secondPartCommand); 
			 case "dele" : return server_commands.dele(secondPartCommand); 
			 case "noop" : return server_commands.noop(); 
			 case "rset" : return server_commands.rset(); 
			 default: return server_commands.err;
			 
			 }		
		}
		
//		@Override
		public void run() {
			System.out.println("Der Client hat sich zu uns verbunden");
//			USER, PASS, QUIT, START, LIST, RETR, DELE, NOOP, RSET
			String capa = "+OK Server ready\nUSER\nPASS\nSTAT\nLIST\nRETR\n";							
			
			//Erst muss eingeloggt werde, bevor request gestartet werden koennen 
			try {

				/* Socket-Basisstreams durch spezielle Streams filtern */
				inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outToClient = new DataOutputStream(socket.getOutputStream());
								

//				while (serviceRequested) {
				
				writeToClient("+OK Welcome");
				
				String request = readFromClient();
				System.out.println("Anfrage von CLient" + request);	
				
				
				//writeToClient(err);
				
//				request = readFromClient();
//				System.out.println(request);
				
					while(authentication()) {
						
					}
					
					System.out.println("Authentication erfolgreich");			
					
					request = readFromClient();
					System.out.println(request);
					
					checkAllCommand(request);
					
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
			System.out.println("VOR EINLESEN");
			request = inFromClient.readLine() + "\n" ;
			System.out.println("NACH EINLESEN");
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
			System.out.println(readFromClient());
			System.out.println("authentication Method betretten");
			if(checkAllCommand(readFromClient()).compareTo(ok) == 0) {
				System.out.println("authentication Username");
				writeToClient(ok + " Username accepted, password please");
				if(checkAllCommand(readFromClient()).compareTo(ok) == 0) {
					System.out.println("authentication Password");
					writeToClient(ok + " Password accept");
					return false;
				}
			}
		System.out.println("Verlasse authentication Method erfolglos");
			return true;
		}
	}
	
	//********************** TEST *********************
	public static void main(String[] args) {
		//POP3Server server = new POP3Server(dirPath);
//		server.startePOP3_Server();
	}
}