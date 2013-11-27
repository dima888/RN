package proxyServer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Proconditions, fuer die Korrekte Schreibweise von Client an den Server
 * @author foxhound
 */

class POP3_Server_Commands {

//	  final String ok = "+OK ";
//	  final String err = "-ERR ";
	  final String exception = "-ERR no such message";
	  
	  boolean userFlag = false; //user hat sich angemeldet bei true
	  boolean passFlag = false; // Passwort wurde akzeptiert bei true -> authenticationFlag = true
	  boolean authenticationFlag  = false; //Bei true ist man eingelogt
	  
	  String currentUser = ""; //Aktuell eingeloggte User 
	  private ServerAccountManagement serverAccountManagement;
	  	  
	  //static int emailNumber = 0;
	  private Map<File , Integer> emailMap = new HashMap<>();
	  private Map<File , Integer> deletedMails = new HashMap<>();
	  

	 //******************************** KONSTRUKTOR ******************************************	 	 
	 public POP3_Server_Commands() {
//		 aktualisieren();
	 }

	 //***************** GETTER ********************
	public Map<File , Integer> getEmailMap() {
		return emailMap;
	}

	
	//***************************SETTER***************************
	/**
	 * Mit ServerAccountManagment bekannt machen
	 * @param ServerAccountManagement serverAccountManagement - Ist eine Klasse
	 */
	public void setServerAccountManagement(ServerAccountManagement serverAccountManagement) {
		this.serverAccountManagement = serverAccountManagement;		
	}
	
	//************************* RFC 1939 METHODS *****************************
	
	/**
	 * Prueft den Befehl user 
	 * @param commad - Example: user Alf
	 * @param String userName - Ein username um auf eine bestimmte Email-Adresse zuzugreifen
	 * @return
	 */
	 String user(String secondPartCommand) {
		 if(authenticationFlag == true) {
			 return Information.err + "invalide command in this state";
		 }
		 for(Map.Entry<List<Object>, String> account : serverAccountManagement.getAccountMap().entrySet()) {
			 if(((String)account.getKey().get(Information.KONTONAME)).compareTo(secondPartCommand) == 0) {
				 currentUser = (String) account.getKey().get(Information.KONTONAME);
				 userFlag = true;
				 return Information.ok;
			 }
		 }		 		 
			return Information.err;
		}


	/**
	 * Prueft den Befehl pass
	 * @param command - Example: password Alf
	 * @param String userPassword - Ein Passwort um auf eine bestimmte Email-Adresse zuzugreifen
	 * @return String
	 */
	String password(String secondPartCommand) {
		 if(authenticationFlag == true) {
			 return Information.err + "invalide command in this state";
		 }
		if(userFlag == true) {
			for(Map.Entry<List<Object>, String> account : serverAccountManagement.getAccountMap().entrySet()) {
				 if(currentUser.compareTo(((String)account.getKey().get(Information.KONTONAME))) == 0) {
					 if(((String) account.getKey().get(Information.PASSWORD)).compareTo(secondPartCommand) == 0) {
						 authenticationFlag = true;						 
						 return Information.ok;
					 }
				 }
			 }
			userFlag = false;
		}
		return Information.err;
	}	
	 
	/**
	 * Macht die Markierungen durch den DELE Befehl rückgängig
	 * @return String
	 */
	String rset() {
		String info = "maildrop has " + deletedMails.size() + " messages (";
		int totalMessageSize = 0;
		if(authenticationFlag != true) {
			return Information.err + "invalide command in this state";
		}
		
		//Hier packen wir die markierten Mails wieder zurück in die mailMap
		for(Map.Entry<File, Integer> pair : deletedMails.entrySet()) {
			totalMessageSize += pair.getKey().length();
			emailMap.put(pair.getKey(), pair.getValue());	
		}
		
		//HashMap leeren
		deletedMails = new HashMap<>(); 
		 
		//Info zusammenbauen und returnen		S
		return Information.ok + info + totalMessageSize + " octets)";
	}
	 
	/**
	 * Holt die n-te E-Mail vom E-Mail-Server
	 * @param String SecondPartCommand - Das folgende Wort nach einem Befehl
	 * @return String
	 */
	String retr(String secondPartCommand) {
		if(authenticationFlag != true) {
			return Information.err + "invalide command in this state";
		}
		if(secondPartCommand.isEmpty()) {
			return Information.err;
		}
		boolean exceptionFlag = true;
		String result = Information.ok;
		try {
			int integer = Integer.parseInt(secondPartCommand);
			
			
			for (Map.Entry<File, Integer> pair : emailMap.entrySet()) {
				if (integer == pair.getValue()) {
					exceptionFlag = false;
					// Datei auslesen
//					thread.writeToClient("+OK " + pair.getKey().length());
					result += pair.getKey().length() + "\r\n";
					Scanner scanner = new Scanner(pair.getKey());
					
//					System.out.println(result);
					while (scanner.hasNextLine()) {
						
						String currentLine = scanner.nextLine();											
						
						//Nach einen Punkt am Anfang pruefen
						if(currentLine.startsWith(".")) {
							result += "." + currentLine;
						} else {
							result += currentLine;
						}
						
						result += "\r\n";
					}
					scanner.close();
					//Löscht die Mails vom "Server" --> vom Dateisystem
					pair.getKey().delete();
					emailMap.remove(pair.getKey());
				}
			}

		if (exceptionFlag) {
			return exception; // Postcondition
		}

		} catch (Exception e) {
			//return exception;
		}
		return result += ".";
	}

	/**
	 * 
	 * @return
	 */
	String noop() {
		if(authenticationFlag != true) {
			return Information.err + "invalide command in this state";
		}
		return Information.ok;
	}
	  

	/**
	 * markiert die n-te E-Mail am E-Mail-Server.
	 * @param String secondPartCommand - Das folgende Wort nach einem Befehl
	 * @return
	 */
	String dele(String secondPartCommand) {
		if(authenticationFlag != true) {
			return Information.err + "invalide command in this state";
			
		}
		boolean exceptionFlag = true;
		String result = Information.ok + "message" + secondPartCommand + " deleted";
		
		try {
			System.out.println("INHALT VON PARAMETER:" + secondPartCommand);
			int integer = Integer.parseInt(secondPartCommand);
			for (Map.Entry<File, Integer> pair : emailMap.entrySet()) {
				if (integer == pair.getValue()) {
					exceptionFlag = false;
					deletedMails.put(pair.getKey(), pair.getValue());
					emailMap.remove(pair.getKey());					//*
				}
			}

		} catch (Exception e) {
			//return exception;
		}

		if (exceptionFlag) {
			return "-ERR message " +  secondPartCommand + " already deleted"; // Postcondition
		}

		return result;
	}

	 /**
	  * liefert die Anzahl und die Größe der (n-ten) E-Mails.
	  * @return String
	  */
	  String list() {
		  if(authenticationFlag != true) {
				return Information.err + "invalide command in this state";
			}
		  
		 String result = "\r\n";
		 String head = "";
		 String body = "\r\n";
		 int messageLength = 0;
		 
		for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
			body += pair.getValue() + " " + pair.getKey().length() + "\r\n"; 
			messageLength += pair.getKey().length();			
		}
		
		//Head zusammenbauen
		head += Information.ok + " " + emailMap.size() + " messages (" + messageLength + " octets)";
		
		//Head und Body verbinden und ausgeben
		return result = head + body + "\n.";
		
	}
	
	 /**
	  * liefert die Anzahl und die Größe der (n-ten) E-Mails.
	  * @param secondPartCommand - (n-ten) E-Mails
	  * @return String
	  */
	 String list(String secondPartCommand) {
			if(authenticationFlag != true) {
				return Information.err + "invalide command in this state";
			}
		String result = Information.ok;		
		boolean exceptionFlag = true;

		try {			
			int integer = Integer.parseInt(secondPartCommand);

				
			for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
				if(integer == pair.getValue()) {
					result += pair.getValue() + " " + pair.getKey().length() + "\r\n"; 
					exceptionFlag = false;
				}
			}
			
			 if(exceptionFlag)  {
				 return exception + ", only " + emailMap.size() + " messages in maildrop"; //Postcondition
			 }

		} catch (Exception e) {
			//return exception + ", only " + emailMap.size() + " messages in maildrop";
			return exception;
		}
		return result;
	}

	 /**
	  * Liefert den Status der Mailbox, u.a. die Anzahl aller E-Mails im Postfach und deren Gesamtgröße (in Byte).
	  * @return result - fertiger String mit den oben genannten infos
	  */
	  String stat()  {
			if(authenticationFlag != true) {
				return Information.err + "invalide command in this state";
			}
		 String result = Information.ok + " ";
		 int count = 0;
		 int completeLengh = 0;

		for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
			completeLengh += pair.getKey().length(); 
			count ++;
		}
		
		return result += count + " " + completeLengh;
	}
	  
	  /**
	   * Liefert die eindeutigen ID's der Mails zurück
	   * @return String
	   */
	  String uidl() {
			String result = Information.ok + "\n";		
			
			for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
				result += pair.getValue() + "\n";
			}
			return result + ".";
	  }
	  
	  /**
	   * Liefert die eindeutigen ID's der Mails zurück
	   * @param String SecondPartCommand - Das folgende Wort nach einem Befehl
	   */
	  String uidl(String secondPartCommand) {
			if(authenticationFlag != true) {
				return Information.err; //nicht autorisiert
			}
			String result = Information.ok + " ";
			boolean exceptionFlag = true;
			
			int integer = Integer.parseInt(secondPartCommand);
			
			for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
				if(integer == pair.getValue()) {
					result += pair.getValue() + "\n";
					exceptionFlag = false;
				}
			}
			
			if(exceptionFlag) {
				return exception;
			}			
			return result;			
	  }

	 /**
	  * Beenden die Verbindung mit den Aktuellen Clienten
	  * @param socket
	  * @return
	  */
	  String quit(Socket socket) {
		  
		for (Map.Entry<File, Integer> pair : deletedMails.entrySet()) {
			pair.getKey().delete();
			deletedMails.remove(pair);
		}
		  
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Socket can not be closed!");
		}
		authenticationFlag = false;
		return Information.ok + " dewey POP3 server signing off";
	}
	
	  
	  //******************************* HILFS METHODEN ******************************
//		public Map<File, Integer> getDeletedMails() {
//			return deletedMails;
//		}
		
	  /**
	   * Hier wird der Name der Email zusammen gebaut
	   */
		public void aktualisieren() {			
			for (File i : serverAccountManagement.f.listFiles()) {
				char[] puffer = i.getName().toCharArray();
				String result = "";
				for (int j = 6; j < puffer.length; j++) {
					if (puffer[j] == '.') {
						break;
					} else {
						result += puffer[j];
					}
				}
				emailMap.put(i, Integer.parseInt(result));
			}
		}
		
		/**
		 * Löscht die Mails vom Server, wenn der Quit Befehl eingegeben wird
		 * @param mail
		 */
//		void deleteMail(File mail) {
//			for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
//				if(pair.getKey() == mail) {
//					emailMap.remove(pair);
//				}
//			}
//		}
}
