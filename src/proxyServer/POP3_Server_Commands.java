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
	 static File f = new File(ServerAccountManagement.getDirPath().toString());
	
	 
	 //******************************** PRIVATER KONSTRUKTOR ******************************************
	 
	 private POP3_Server_Commands() {
		 //Nicht m�glich Objekte von au�en zu erzeugen !
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
		 Mail_Service currentMail = new Mail_Service();
		 Map<File, Integer> emailMap = currentMail.getEmailMap();
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
		return ok;
	}

	 /**
	  * l�scht die n-te E-Mail am E-Mail-Server.
	  * @param secondPartCommand
	  * @return
	  */
	 static String dele(String secondPartCommand) {
		 	Mail_Service currentMail = new Mail_Service();
		 	Map<File, Integer> emailMap = currentMail.getEmailMap();
		 	boolean exceptionFlag = true;
			String exception = "-ERR invalid sequence number: " + secondPartCommand;
			String result = ok + "\n";		
			int count = 0;			

			try {			
				int integer = Integer.parseInt(secondPartCommand);
				
				for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
					if (integer == pair.getValue()) {
						//deleteFile(pair.getKey());
						pair.getKey().delete();
						emailMap.remove(pair);
						exceptionFlag = false;
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
	  * liefert die Anzahl und die Gr��e der (n-ten) E-Mails.
	  * @return
	  */
	 static String list() {
		 Mail_Service currentMail = new Mail_Service();
		 Map<File, Integer> emailMap = currentMail.getEmailMap();
		 

		 String result = ok + "\n";
		 
		for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
			result += pair.getValue() + " " + pair.getKey().length() + "\n"; 
		}
		
		 
		 return result + '.';
	}
	
	 /**
	  * liefert die Anzahl und die Gr��e der (n-ten) E-Mails.
	  * @param secondPartCommand - (n-ten) E-Mails
	  * @return
	  */
	static String list(String secondPartCommand) {
		Mail_Service currentMail = new Mail_Service();
		Map<File, Integer> emailMap = currentMail.getEmailMap();
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
	  * Liefert den Status der Mailbox, u.a. die Anzahl aller E-Mails im Postfach und deren Gesamtgr��e (in Byte).
	  * @return result - fertiger String mit den oben genannten infos
	  */
	 static String stat()  {	
		 Mail_Service currentMail = new Mail_Service();
		 Map<File, Integer> emailMap = currentMail.getEmailMap();
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
	
	//**************************** TEST *****************************
	public static void main(String[] args) {
		Mail_Service m = new Mail_Service();
		
		System.out.println(POP3_Server_Commands.retr("2"));
	}
}
