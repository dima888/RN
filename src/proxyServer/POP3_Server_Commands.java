package proxyServer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


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

	 static String stat() {
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
//		System.out.println(checkAllCommand("user dima888@gmx.net"));
//		System.out.println(checkAllCommand("pass 12345678"));
		//System.out.println(password("pass Aa", "Aa"));	
	}
}
