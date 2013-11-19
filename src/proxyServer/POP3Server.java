package proxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Scanner;

//import proxyServer.ServerAccountManagement.Info;

class POP3Server {
	
	//*********************** ATTRIBUTE *****************************
	
	/* Server, der Verbindungsanfragen entgegennimmt */
	 // Auf diesen Port wird "gelauscht"
	
	POP3_Server_Commands server_commands;	
	ServerAccountManagement serverAccountManagement;
	
	// *********************GETTER**********************
	
	//**********************SETTER**********************
	/*
	 * Mit ServerAccountManagment bekannt machen
	 * @param ServerAccountManagement serverAccountManagement - Ist eine Klasse
	 */
	public void setServerAccountManagement(ServerAccountManagement serverAccountManagement) {
		this.serverAccountManagement = serverAccountManagement;		
	}
	
	public void setPOP3_Server_Commands(POP3_Server_Commands server_commands) {
		this.server_commands = server_commands;
		startePOP3_Server();
	}
	
	
	//********************** KONSTRUKTOR *****************************	
	
	POP3Server(POP3_Server_Commands server_commands) {
		this.server_commands = server_commands;
		startePOP3_Server();
	}
	
	POP3Server() {
	}
	
	
	
	//*********************** METHODEN *******************************	
	
	void startePOP3_Server() {
		ServerSocket welcomeSocket; // ServerSocket zum lauschen
		Socket connectionSocket; // VerbindungsSocket mit Client
		
		try {
			welcomeSocket = new ServerSocket(Information.SERVER_PORT); 						
			
			while(true) {
				System.out.println("TCP Server: Lauschen auf Port: " + Information.SERVER_PORT);
				
				/*
				 * Blockiert MainThread, wartet auf Verbindungsanfrage --> nach
				 * Verbindungsaufbau Standard-Socket erzeugen und
				 * connectionSocket zuweisen
				 */
				connectionSocket = welcomeSocket.accept();
				
				// Neuen Arbeits-Thread erzeugen und starten mit übergebenen Socket
				(new POP3_Server_Thread(connectionSocket, serverAccountManagement.getDirPath())).start();
			}
			
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
	class POP3_Server_Thread extends Thread {
		
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
		
		@Override
		public void run() {
			
			
			System.out.println("Der Client hat sich zu uns verbunden");							
			
			//Erst muss eingeloggt werde, bevor request gestartet werden koennen 
			try {

				/* Socket-Basisstreams durch spezielle Streams filtern */
				inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outToClient = new DataOutputStream(socket.getOutputStream());
				
				writeToClient("+OK POP3 server ready");				
											
					//Hier ist der login Mechanismus 
					while(authentification()) {}
					
					//Hier werden die Befehle von Client interpretiert; Server laeuft noch unendlich
					while(true) {
						String answer = checkAllCommand(readFromClient());
						writeToClient(answer);
					}

			} catch (IOException e) {
				return;
			}

		}
					
		
		/**
		 * Liest die Eingabe von Client ein
		 * @return
		 * @throws IOException
		 */
		private String readFromClient() throws IOException {
			String request = "";
			request = inFromClient.readLine() + "\n";
			System.out.println("Client senden to Thread" + threadNumber  + " " + request);
			return request;
		}
		
		/**
		 * Sendet eine Antwort an den Client
		 * @param reply
		 * @throws IOException
		 */
		 private void writeToClient(String reply) throws IOException {
			outToClient.writeBytes(reply + "\r\n");
			System.out.println("TCP Server Thread " + threadNumber	+ " has written the message to client: " + reply);
		}		
		
		/**
		 * Methode verlangt nach username und password, sofern die defeniert sind
		 * @return boolean
		 * @throws IOException
		 */
		private boolean authentification() throws IOException {
			String clientRequest = readFromClient();
			
			//CAPA abfangen
			if(clientRequest.compareTo("CAPA\n") == 0) {
				writeToClient("-ERR");
				return true;
			}
			
			if (checkAllCommand(clientRequest).compareTo(ok) == 0) {
				writeToClient(ok + " Username accepted, password please\r\n");
				String clientRequestTwo = readFromClient();
				
				if (checkAllCommand(clientRequestTwo).compareTo(ok) == 0) {
					writeToClient(ok + " Password accepted\r\n");
					return false;
				}
			}
			writeToClient("authentification failed\r\n");
			return true;
		}		
		
		/**
		 * Methode Prueft ob die einkommenden Befehle von Client nach den RFC 1939 standarts sind
		 * @param String clientCommand - Ein Befehl von Client den wir hier entgegen nehmen
		 * @return String
		 */
		  private String checkAllCommand(String clientCommand) {
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
			 case "uidl" : if(secondPartCommand.isEmpty()) {return server_commands.uidl();} else {return server_commands.uidl(secondPartCommand);}
			 default: return server_commands.err + " unknown command";
			 
			 }		
		}
	}
}

