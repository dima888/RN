package proxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class POP3Client {
	
	/* 
	 * Man ben�tigt den ServerPort und die ServerIP um sich als Client mit einem Server zu verbinden
	 * ANALOGIE: Die ServerIP kann man sich wie eine Hausnummer vorstellen in einem Geb�ude
	 * 			 und den ServerPort wie eine Zimmernummer in einem Geb�ude
	 */
	public static final int SERVER_PORT = 11_000; //ZimmerNummer
	public static final String SERVER_IP = "192.168.178.61"; //HausNummer
	
	//Eine Socket ist ein Verbindungspunkt
	private Socket clientSocket; // TCP-Standard-Socketklasse
	
	/* 
	 * Ein Stream ist ein Puffer
	 * An einem Ende wird er mit Datenbef�llt, die Daten verweilen im Puffer, bis sie
	 * am anderen Ende wieder heraus geholt werden
	 * Streams sind immer "unidirektional", soll hei�en, sie sind nur f�r eine Richtung ausgelegt
	 */
	private DataOutputStream outToServer; //Ausgabestream zum Server
	private BufferedReader inFromServer; // Eingabestream vom Server
	
	private boolean serviceRequested = true; // Client l�uft solange true
	
	
}
