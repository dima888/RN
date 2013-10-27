package proxyServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Proconditions, fuer die Korrekte Schreibweise von Client an den Server
 * @author foxhound
 *
 */

class POP3_Server_Commands {

	 //final  int MAX_COMMAND_SIZE = 5; //Funf, da noch das Leerzeichen wir dazu zaehlen
	 String ok = "+OK";
	 String err = "-ERR";
	 String secondPartCommand = "";
	 Map<String, Object> mapCommands = new HashMap() {       { put("user", user()) ; put("pass", password()) ;  }       };	
	
	//************************* SUPER METHODE; PRUEFT OB BEFEHL LAUT RFC 1939 DEFINIERT IST ***********************
	 Object checkAllCommand(String clientCommand) {
		Scanner scanner = new Scanner(clientCommand);
		String firstPartCommand = scanner.next();
		
		this.secondPartCommand = scanner.next();
		System.out.println("Lokal: " + this.secondPartCommand); 
		System.out.println("Forschleife: " + getSecondPartCommand(clientCommand));
		if(scanner.hasNext()) {
			//secondPartCommand = scanner.next();
			
			System.out.println("Forschleife" + getSecondPartCommand(clientCommand));
		}
		scanner.close();
		
		
//		String firstPartCommand = getFirstPartCommand(clientCommand);
//		secondPartCommand = getSecondPartCommand(clientCommand);
		
		for(Map.Entry<String, Object> pair : mapCommands.entrySet()) {
			if(pair.getKey().compareTo(firstPartCommand.toLowerCase()) == 0) {
				if(secondPartCommand.isEmpty()) {
					return  pair.getValue();
				} 				
			}
		}		
		return err;		
	}
	
	
	
	//************************* RFC 1939 METHODS *****************************
	
	/**
	 * Prueft den Befehl user 
	 * @param commad - Example: user Alf
	 * @param userName - Example: POP3Server.USER
	 * @return
	 */
	 String user() {		
		System.out.println("Global: " + this.secondPartCommand);
		if(POP3Server.getUser().compareTo(secondPartCommand) == 0) {
			System.out.println("USER erkannt");
			return ok;
		}
//		return err;
		
		return user("AAA");
	}
	
	 String user(String a) {
		return "";
	}


	/**
	 * Prueft den Befehl pass
	 * @param command - Example: password Alf
	 * @param userPassword - Example: POP3Server.PASS
	 * @return
	 */
	 String password() {
		if(POP3Server.getPassword().compareTo(secondPartCommand) == 0) {
			return ok;
		}
		return err;
	}	
	
	
	//********************** PRIVATE METHODS FOR USER && PASSWORD ***************************	
	/*
	 * Final Befehle = {user, pass, quit, start, list, retr, dele, noop, rset}
	 */
	
	/**
	 * Precondition Methode: 
	 * Macht fuer uns eine Pruefung, ob die Protokoll Syntax korrekt ist
	 * @param commandName (Final Befehle) - Example: {user, pass, quit, start, list, dele ... }
	 * @param command - Example: user Aligator1337
	 * @return
	 */
//	private  boolean checkCommand(String commandName, String command, String secondPathOfCommand) {			
//		if (command.length() < 6) { // Sechs, weil der BenutzerName mindestens ein Zeichen bei uns Haben muss
//			return true;
//		}
//		
//		int l = command.length() - MAX_COMMAND_SIZE; //Ermittle die laenge des wortes nach den final Befehl
//		if(l != secondPathOfCommand.length()) {
//			return true;
//		}
//		
//		for (int i = 0; i < MAX_COMMAND_SIZE; i++) {
//
//			if (commandName.toCharArray()[i] != command.toLowerCase().toCharArray()[i]) {
//				return true;
//			}
//		}		
//		return false;
//	}
	
	/**
	 * Prueft ob der zweite Teils des Befehls, zu unseren vordefinierten Werten kompatibel ist. 
	 * @param command
	 * @param userName
	 * @return
	 */
//	private  boolean checkSecondPathOfCommand(String command,	String secondPathOfCommand) {
//		//Hier wird der Username aus den Befehl gezoggen
//		for(int i = 5, j = 0; i < command.length(); i++, j++) {
//			if(secondPathOfCommand.toCharArray()[j] != command.toCharArray()[i]) {				
//				return true;
//			}
//		}
//		return false;
//	}
	
	private  String getSecondPartCommand(String command) {
		if (command.length() < 6) { // Sechs, weil der BenutzerName mindestens ein Zeichen bei uns Haben muss
			return err;
		}
		
		String result = "";
		for(int i = 5; i < command.length(); i++) {
			result += command.toCharArray()[i];
		}
		return result;
	}	
	
	//**************************** TEST *****************************
	public static void main(String[] args) {
		POP3_Server_Commands a = new POP3_Server_Commands();
		System.out.println(a.checkAllCommand("user dima888@gmx.net"));
		//System.out.println(password("pass Aa", "Aa"));		
	}
}
