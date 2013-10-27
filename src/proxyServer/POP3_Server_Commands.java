package proxyServer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
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
		 int count = 0;
		 String result = ok + "\n";
		 try {
			 int integer = Integer.parseInt(secondPartCommand);
			 
			 for(File i : f.listFiles()) {
				 if(++count == integer) {
					 
					 //Datei auslesen
					 Scanner scanner = new Scanner(i);
					 while(scanner.hasNext()) {
						 result += scanner.next() + " ";
					 }					 
				 }
			 }
			 
		} catch (Exception e) {
			return "-ERR invalid sequence number: " + secondPartCommand;
		}
		 		 		
		 
		 
		// TODO Auto-generated method stub
		return result;
	}

	 static String noop() {
		// TODO Auto-generated method stub
		return null;
	}

	 static String dele(String secondPartCommand) {
		// TODO Auto-generated method stub
		return null;
	}


	 static String list() {
		// TODO Auto-generated method stub
		return null;
	}
	
	 static String list(String secondPartCommand) {
		// TODO Auto-generated method stub
		return null;
	}

	 /**
	  * Liefert den Status der Mailbox, u.a. die Anzahl aller E-Mails im Postfach und deren Gesamtgröße (in Byte).
	  * @return result - fertiger String mit den oben genannten infos
	  */
	 static String stat()  {	
		 String result = "";
		 int count = 0;
		 int completeLengh = 0;
		 
		
		for(File i : f.listFiles()) {
			completeLengh += i.length();
			count ++;
		}
		
		Integer c = new Integer(count);
		String emailCount = c.toString();
		
		result += ok += " ";
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
		System.out.println(retr("$"));
		System.out.println(retr("1"));
//		System.out.println(stat());
	}
}
