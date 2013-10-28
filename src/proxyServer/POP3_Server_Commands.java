package proxyServer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.html.parser.Parser;

/**
 * Proconditions, fuer die Korrekte Schreibweise von Client an den Server
 * @author foxhound
 *
 */

class POP3_Server_Commands {

	 static String ok = "+OK";
	 static String err = "-ERR";
	 static File f = new File(ServerAccountManagement.getDirPath2().toString());
	 static int emailNumber = 0;
	 static Map<File , Integer> emailMap = new HashMap<>();
	 
	 //******************************** PRIVATER KONSTRUKTOR ******************************************
	 
	 private POP3_Server_Commands() {
		 //Nicht möglich Objekte von außen zu erzeugen !
	 }
		
	
	//************************* RFC 1939 METHODS *****************************
	
	/**
	 * Prueft den Befehl user 
	 * @param commad - Example: user Alf
	 * @param userName - Example: POP3Server.USER
	 * @return
	 */
	  static String user(String secondPartCommand) {
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
	  static String password(String secondPartCommand) {
		if(POP3Server.getPassword().compareTo(secondPartCommand) == 0) {
			System.out.println("PASSWORT akzeptiert");
			return ok;
		}
		return err;
	}	
	 
	  static String rset() {
		// TODO Auto-generated method stub
		return null;
	}
	 
	  /** TODO: DAT MACHT AUCH DIMA; SCHON HIER ANGFANGEN: Hier noch auf die Punkte(. || .. || .) achten
	   * Holt die n-te E-Mail vom E-Mail-Server
	   * @param secondPartCommand
	   * @return
	   */
	 static String retr(String secondPartCommand) {
		 boolean exceptionFlag = true;
		 String exception = "-ERR invalid sequence number: " + secondPartCommand;
		 int count = 0;
		 String result = ok + "\n";
		 try {
			 int integer = Integer.parseInt(secondPartCommand);

				for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
					if(integer == pair.getValue()) {
						 exceptionFlag = false;
						 //Datei auslesen
						 Scanner scanner = new Scanner(pair.getKey());
						 while(scanner.hasNextLine()) {
							 result += scanner.nextLine() + "\n";
						 }
						 scanner.close();
					}
				}
			 
			 if(exceptionFlag)  {
				 return exception; //Postcondition
			 }
			 
		} catch (Exception e) {
			return exception;
		}
		 
		return result;
	}

	 static String noop() {
		// TODO Auto-generated method stub
		return null;
	}

	 /**
	  * löscht die n-te E-Mail am E-Mail-Server.
	  * @param secondPartCommand
	  * @return
	  */
	 static String dele(String secondPartCommand) {
		 	boolean exceptionFlag = true;
			String exception = "-ERR invalid sequence number: " + secondPartCommand;
			String result = ok + "\n";		
			int count = 0;			

			try {			
				int integer = Integer.parseInt(secondPartCommand);
				
				for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
					if (integer == pair.getValue()) {
						deleteFile(pair.getKey());					
					}
				}
				
				 if(exceptionFlag)  {
					 return exception; //Postcondition
				 }

			} catch (Exception e) {
				return exception;
			}

			return result;
	}

	 /**
	  * liefert die Anzahl und die Größe der (n-ten) E-Mails.
	  * @return
	  */
	 static String list() {
		 int count = 0;
		 String result = ok + "\n";
		 for(File i : f.listFiles()) {
			 count++;			  
			 result += count + " " + i.length() + "\n";
		 }
		 
		 return result;
	}
	
	 /**
	  * liefert die Anzahl und die Größe der (n-ten) E-Mails.
	  * @param secondPartCommand - (n-ten) E-Mails
	  * @return
	  */
	static String list(String secondPartCommand) {
		String exception = "-ERR invalid sequence number: " + secondPartCommand;
		String result = ok + "\n";		
		int count = 0;

		try {			
			int integer = Integer.parseInt(secondPartCommand);
			for(File i : f.listFiles()) {
				if (integer == ++count) {
					result += integer + " " + i.length() + "\n";
				}
			}
			
			 if(integer > count)  {
				 return exception; //Postcondition
			 }

		} catch (Exception e) {
			return exception;
		}

		return result;
	}

	 /**
	  * Liefert den Status der Mailbox, u.a. die Anzahl aller E-Mails im Postfach und deren Gesamtgröße (in Byte).
	  * @return result - fertiger String mit den oben genannten infos
	  */
	 static String stat()  {	
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
	 static String quit(Socket socket) {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Socket can not be closed!");
		}
		return ok + " POP SERVER SIGNING OFF";
	}
	
	 static void initializeEmailMap() {
		 for(File i : f.listFiles()) {
			 emailMap.put(i, ++emailNumber);
		 }
	 }
	 
	 static void deleteFile(File file) {
		 emailMap.remove(file);
		 file.delete();
	 }
	
	//**************************** TEST *****************************
	public static void main(String[] args) {
		initializeEmailMap(); //TODO: Verlagern
		
		System.out.println(list());
		System.out.println(emailMap);
		System.out.println(dele("2"));
		
		System.out.println(emailMap);
	}
}
