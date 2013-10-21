package Praktikumsaufgabe1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class POP3Client {
	
	/* 
	 * Man benötigt den ServerPort und die ServerIP um sich als Client mit einem Server zu verbinden
	 * ANALOGIE: Die ServerIP kann man sich wie eine Hausnummer vorstellen in einem Gebäude
	 * 			 und den ServerPort wie eine Zimmernummer in einem Gebäude
	 */
	public static final int SERVER_PORT = 11_000; //ZimmerNummer
	public static final String SERVER_IP = "192.168.178.61"; //HausNummer
	
	//Eine Socket ist ein Verbindungspunkt
	private Socket clientSocket; // TCP-Standard-Socketklasse
	
	/* 
	 * Ein Stream ist ein Puffer
	 * An einem Ende wird er mit Datenbefüllt, die Daten verweilen im Puffer, bis sie
	 * am anderen Ende wieder heraus geholt werden
	 * Streams sind immer "unidirektional", soll heißen, sie sind nur für eine Richtung ausgelegt
	 */
	private DataOutputStream outToServer; //Ausgabestream zum Server
	private BufferedReader inFromServer; // Eingabestream vom Server
	
	private boolean serviceRequested = true; // Client läuft solange true
	
	/**
	 * Startet den Clienten
	 */
	public void startJob() {
		//TODO
	}
	
	/**
	 * Ermöglicht das senden von Anfragen den Server
	 * @param String request - Die Anfrage, die an den Server gestellt werden soll
	 */
	public void writeToServer(String request) {
		//TODO
	}
	
	/**
	 * Ermöglicht das auslesen der Antwort vom Server
	 * @return String - die Antwort des Servers
	 */
	public String readFromServer() {
		//TODO
		return null;
	}
}
