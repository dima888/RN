package TCP_Code_Beispiel;

/*
 * TCPClient.java
 *
 * Version 2.0
 * Autor: M. H�bner HAW Hamburg (nach Kurose/Ross)
 * Zweck: TCP-Client Beispielcode:
 *        TCP-Verbindung zum Server aufbauen, einen vom Benutzer eingegebenen
 *        String senden, den String in Gro�buchstaben empfangen und ausgeben
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {
	
	/* Man ben�tigt den ServerPort und die ServerIP um sich als Client mit einem Server zu verbinden
	 * ANALOGIE: Die ServerIP kann man sich wie eine Hausnummer vorstellen in einem Geb�ude
	 * 			 und den ServerPort wie eine Zimmernummer in einem Geb�ude
	 */
	public static final int SERVER_PORT = 6789;
	public static final String SERVER_IP = "192.168.178.61";

	//Eine Socket ist ein Verbindungspunkt
	private Socket clientSocket; // TCP-Standard-Socketklasse

	/* Ein Stream ist ein Puffer
	 * An einem Ende wird er mit Datenbef�llt, die Daten verweilen im Puffer, bis sie
	 * am anderen Ende wieder heraus geholt werden
	 * Streams sind immer "unidirektional", soll hei�en, sie sind nur f�r eine Richtung ausgelegt
	 */
	private DataOutputStream outToServer; //Ausgabestream zum Server 
	private BufferedReader inFromServer; // Eingabestream vom Server

	private boolean serviceRequested = true; // Client l�uft solange true

	public void startJob() {
		/* Client starten. Ende, wenn quit eingegeben wurde */
		Scanner inFromUser; //Zum Auslesen der Benutzereingaben
		String sentence; // vom User �bergebener String
		String modifiedSentence; // vom Server modifizierter String

		/* Ab Java 7: try-with-resources mit automat. close benutzen! */
		try {
			/* Socket erzeugen --> Verbindungsaufbau mit dem Server */
			clientSocket = new Socket(SERVER_IP, SERVER_PORT);

			/* Socket-Basisstreams durch spezielle Streams filtern */
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			/* Konsolenstream (Standardeingabe) initialisieren */
			inFromUser = new Scanner(System.in);

			while (serviceRequested) {
				System.out.println("ENTER TCP-DATA: ");
				/* String vom Benutzer (Konsoleneingabe) holen */
				sentence = inFromUser.nextLine();

				/* String an den Server senden */
				writeToServer(sentence);

				/* Modifizierten String vom Server empfangen */
				modifiedSentence = readFromServer();

				/* Test, ob Client beendet werden soll */
				if (modifiedSentence.indexOf("QUIT") > -1) {
					serviceRequested = false;
				}
			}

			/* Socket-Streams schlie�en --> Verbindungsabbau */
			clientSocket.close();
		} catch (IOException e) {
			System.err.println("Connection aborted by server!");
		}

		System.out.println("TCP Client stopped!");
	}

	private void writeToServer(String request) throws IOException {
		/* Sende eine Zeile zum Server */
		outToServer.writeBytes(request + '\n');
		System.out.println("TCP Client has sent the message: " + request);
	}

	private String readFromServer() throws IOException {
		/* Lies die Antwort (reply) vom Server */
		String reply = inFromServer.readLine();
		System.out.println("TCP Client got from Server: " + reply);
		return reply;
	}

	public static void main(String[] args) {
		TCPClient myClient = new TCPClient();
		myClient.startJob();
	}
}
