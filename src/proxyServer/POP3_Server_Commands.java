package proxyServer;

import java.util.HashMap;
import java.util.Map;


/**
 * Proconditions, fuer die Korrekte Schreibweise von Client an den Server
 * @author foxhound
 *
 */

class POP3_Server_Commands {

	final static int MAX_COMMAND_SIZE = 5; //Funf, da noch das Leerzeichen wir dazu zaehlen
	
	//************************* RFC 1939 METHODS *****************************
	
	/**
	 * Prueft den Befehl user 
	 * @param commad - Example: user Alf
	 * @param userName - Example: POP3Server.USER
	 * @return
	 */
	static String user(String command, String userName) {				
		String ok = "+OK";
		String err = "-ERR";
		String user = "user "; //Mit Leerzeichen!!!
		
		
		if(checkCommand(user, command, userName)) {
			return err;
		}
		
		if(checkSecondPathOfCommand(command, userName)) {
			return err;
		}
		
		//Preconditon: Hier wird geprueft ob der zu uns verbindente Client uns Bekannt ist
//		for (Map.Entry<Info, String> map : ServerAccountManagement.getAbbildung().entrySet()) {
//			if(map.getValue().compareTo(userName) == 0) {
//			} else{
//				return ok;
//			}
//		}

		return ok; //TODO: Hier muss auf err gewechselt werden und die Precondition von oben einkommentieren, wenn es fertig ist
	}

	/**
	 * Prueft den Befehl pass
	 * @param command - Example: password Alf
	 * @param userPassword - Example: POP3Server.PASS
	 * @return
	 */
	static String password(String command, String userPassword) {
		String ok = "+OK";
		String err = "-ERR";
		String pass = "pass "; //Mit Leerzeichen!!!
		
		
		if(checkCommand(pass, command, userPassword)) {
			return err;
		}
		
		if(checkSecondPathOfCommand(command, userPassword)) {
			return err;
		}
		
		//Preconditon: Hier wird geprueft ob der zu uns verbindente Client von uns Bekannt ist
//		for (Map.Entry<Info, String> map : ServerAccountManagement.getAbbildung().entrySet()) {
//			if(map.getValue().compareTo(userPassword) == 0) {
//			} else{
//				return ok;
//			}
//		}

		return ok; //TODO: Hier muss auf err gewechselt werden und die Precondition von oben einkommentieren, wenn es fertig ist
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
	private static boolean checkCommand(String commandName, String command, String secondPathOfCommand) {			
		if (command.length() < 6) { // Sechs, weil der BenutzerName mindestens ein Zeichen bei uns Haben muss
			return true;
		}
		
		int l = command.length() - MAX_COMMAND_SIZE; //Ermittle die laenge des wortes nach den final Befehl
		if(l != secondPathOfCommand.length()) {
			return true;
		}
		
		for (int i = 0; i < MAX_COMMAND_SIZE; i++) {

			if (commandName.toCharArray()[i] != command.toLowerCase().toCharArray()[i]) {
				return true;
			}
		}		
		return false;
	}
	
	/**
	 * Prueft ob der zweite Teils des Befehls, zu unseren vordefinierten Werten kompatibel ist. 
	 * @param command
	 * @param userName
	 * @return
	 */
	private static boolean checkSecondPathOfCommand(String command,	String secondPathOfCommand) {
		//Hier wird der Username aus den Befehl gezoggen
		for(int i = 5, j = 0; i < command.length(); i++, j++) {
			if(secondPathOfCommand.toCharArray()[j] != command.toCharArray()[i]) {				
				return true;
			}
		}
		return false;
	}
	
	//**************************** TEST *****************************
	public static void main(String[] args) {
		System.out.println(user("user FinaL", "FinaL"));
		System.out.println(password("pass Aa", "Aa"));
	}
}
