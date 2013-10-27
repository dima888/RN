package proxyServer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

import javax.swing.text.html.parser.Parser;

/**
 * Proconditions, fuer die Korrekte Schreibweise von Client an den Server
 * @author foxhound
 *
 */

class POP3_Server_Commands {

	 static String ok = "+OK";
	 static String err = "-ERR";
	 
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
	 
	 static String retr(String secondPartCommand) {
		// TODO Auto-generated method stub
		return null;
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
	  * @return
	  * @throws IOException 
	  */
	 static String stat()  {	
		 int count = 0;
		 ServerAccountManagement.getDirPath2();
		 File f = new File(ServerAccountManagement.getDirPath2().toString());
		 
		 
		System.out.println(f.listFiles());
		for(File i : f.listFiles()) {
			count ++;
		}
		
		Integer c = new Integer(count);
		String emailCount = c.toString();
		System.out.println(emailCount);
		 
		// TODO Auto-generated method stub
		return null;
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
		stat();
	}
}
