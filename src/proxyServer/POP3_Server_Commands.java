package proxyServer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Proconditions, fuer die Korrekte Schreibweise von Client an den Server
 * @author foxhound
 *
 */

class POP3_Server_Commands {

	  final String ok = "+OK";
	  final String err = "-ERR";
	  
	  //static int emailNumber = 0;
	  private Map<File , Integer> emailMap = new HashMap<>();
	  private Map<File , Integer> deletedMails = new HashMap<>();
	  
	  // PFad zum Verzeichnis, in dem wir die Emails speichern möchten -->
	  // erstellt für uns ein Verzeichnis
	  private Path dirPath;

	  File f;
	 
	 //******************************** PRIVATER KONSTRUKTOR ******************************************
	 
	 public POP3_Server_Commands(Path dirPath) {
		 this.dirPath = dirPath;
		 f = new File(dirPath.toString());
	 }
	 
	 //***************** GETTER ********************
	public Map<File , Integer> getEmailMap() {
		return emailMap;
	}
	
	public Path getDirPath() {
		return dirPath;
	}
	
	//************************* RFC 1939 METHODS *****************************
	
	/**
	 * Prueft den Befehl user 
	 * @param commad - Example: user Alf
	 * @param userName - Example: POP3Server.USER
	 * @return
	 */
	   String user(String secondPartCommand) {
			if (POP3Server.getUser().compareTo(secondPartCommand) == 0) {
				System.out.println("USER akzeptiert");
				return ok;
			}
			return err;
		}


	/**
	 * Prueft den Befehl pass
	 * @param command - Example: password Alf
	 * @param userPassword - Example: POP3Server.PASS
	 * @return
	 */
	   String password(String secondPartCommand) {
		if(POP3Server.getPassword().compareTo(secondPartCommand) == 0) {
			System.out.println("PASSWORT akzeptiert");
			return ok;
		}
		return err;
	}	
	 
	String rset() {
		
		for(Map.Entry<File, Integer> pair : deletedMails.entrySet()) {
			emailMap.put(pair.getKey(), pair.getValue());
			deletedMails.remove(pair.getKey());
		}

		return ok;
	}
	 
	/**
	 * TODO: DAT MACHT AUCH DIMA; SCHON HIER ANGFANGEN: Hier noch auf die
	 * Punkte(. || .. || .) achten Holt die n-te E-Mail vom E-Mail-Server
	 * 
	 * @param secondPartCommand
	 * @return
	 */
	String retr(String secondPartCommand) {
		boolean exceptionFlag = true;
		String exception = "-ERR invalid sequence number: " + secondPartCommand;
		int count = 0;
		String result = ok + "\n";
		try {
			int integer = Integer.parseInt(secondPartCommand);

			for (Map.Entry<File, Integer> pair : emailMap.entrySet()) {
				if (integer == pair.getValue()) {
					exceptionFlag = false;
					// Datei auslesen
					Scanner scanner = new Scanner(pair.getKey());
					while (scanner.hasNextLine()) {
						result += scanner.nextLine() + "\n";
					}
					scanner.close();
				}
			}

			if (exceptionFlag) {
				return exception; // Postcondition
			}

		} catch (Exception e) {
			return exception;
		}

		return result;
	}

	String noop() {
		return ok;
	}
	  

	/**
	 * markiert die n-te E-Mail am E-Mail-Server.
	 * @param secondPartCommand
	 * @return
	 */
	String dele(String secondPartCommand) {
		boolean exceptionFlag = true;
		String exception = "-ERR invalid sequence number: " + secondPartCommand;
		String result = ok + "\n";

		try {

			int integer = Integer.parseInt(secondPartCommand);
			for (Map.Entry<File, Integer> pair : emailMap.entrySet()) {
				if (integer == pair.getValue()) {
					exceptionFlag = false;
					emailMap.remove(pair);
					deletedMails.put(pair.getKey(), pair.getValue());
				}
			}

		} catch (Exception e) {
			return exception;
		}

		if (exceptionFlag) {
			return exception; // Postcondition
		}

		return result;
	}

	 /**
	  * liefert die Anzahl und die Größe der (n-ten) E-Mails.
	  * @return
	  */
	  String list() {
		 String result = ok + "\n";
		 
		for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
			result += pair.getValue() + " " + pair.getKey().length() + "\n"; 
		}
		
		 
		 return result + '.';
	}
	
	 /**
	  * liefert die Anzahl und die Größe der (n-ten) E-Mails.
	  * @param secondPartCommand - (n-ten) E-Mails
	  * @return
	  */
	 String list(String secondPartCommand) {
		String exception = "-ERR invalid sequence number: " + secondPartCommand;
		String result = ok + "\n";		
		boolean exceptionFlag = true;

		try {			
			int integer = Integer.parseInt(secondPartCommand);

				
			for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
				if(integer == pair.getValue()) {
					result += pair.getValue() + " " + pair.getKey().length() + "\n"; 
					exceptionFlag = false;
				}
			}
			
			 if(exceptionFlag)  {
				 return exception; //Postcondition
			 }

		} catch (Exception e) {
			return exception;
		}

		return result + '.';
	}

	 /**
	  * Liefert den Status der Mailbox, u.a. die Anzahl aller E-Mails im Postfach und deren Gesamtgröße (in Byte).
	  * @return result - fertiger String mit den oben genannten infos
	  */
	  String stat()  {	 
		 String result = ok + " ";
		 int count = 0;
		 int completeLengh = 0;

		for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
			completeLengh += pair.getKey().length(); 
			count ++;
		}
		
		Integer c = new Integer(count);
		String emailCount = c.toString();
		
		result += emailCount += " ";		
		result += completeLengh;
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
		return ok + " POP SERVER SIGNING OFF";
	}
	
	  
	  //******************************* HILFS METHODEN ******************************
		public Map<File, Integer> getDeletedMails() {
			return deletedMails;
		}
		
		public void aktualisieren() {
			for (File i : f.listFiles()) {
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
		void deleteMail(File mail) {
			for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
				if(pair.getKey() == mail) {
					emailMap.remove(pair);
				}
			}
		}
		
	  
	//**************************** TEST *****************************
	public static void main(String[] args) {
		
//		POP3_Server_Commands server_commands = new POP3_Server_Commands(Paths.get("C:\\Users\\abg688\\Desktop\\Emails"));
//		System.out.println(server_commands.dele("2"));
//		System.out.println(server_commands.quit(null));
		
	}
}
