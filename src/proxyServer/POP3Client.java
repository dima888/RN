package proxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import proxyServer.ServerAccountManagement.Info;

/********************************************* TODO *********************************************************************
 * 
 * Ich wäre dafür den Pfad zum Verzeichnis in der Klasse ServerAccountManagement zu verwalten, da wir diesen einmal in
 * dieser Klasse benötigen und einmal in der POP3Server Klasse. Somit haben wir Code duplizierung und müssen auch, wenn
 * wir ihn mal ändern müssen, dies an zwei Stellen tun.
 * 
 * Ich habe mir ein GMX Konto eingerichtet, was du natürlich auch verwenden kannst.
 * 
 * AUSLESEN DER MAIL erweitert. Jetzt erkennt er auch folgenden Text:
 * 
 * hallo.			Zeile 0
 * was geht.		Zeile 1
 * .				Zeile 2	
 * gut.				Zeile 3
 * freut mich.		Zeile 4
 * .				Zeile 5
 * 
 * Vorher hätte er bei Zeile 2 aufgehört die Mail weiter auszulesen, da ein einzelner Punkt in einer Reihe das Ende der
 * Mail bedeutet. Nun ist noch eine weitere Prüfung eingebaut, ob nach dem Punkt noch etwas kommt und damit wird bis
 * Zeile 5 ausgelesen.
 *************************************************************************************************************************/

class POP3Client extends Thread{
	
	//*********************** ATTRIBUTE *****************************
	
	private String clientName;
	//Liste mit den Kontoinformationen für das jeweilige Konto
	private List<Info> infos = new ArrayList<>();
	
	/* Server, der Verbindungsanfragen entgegennimmt */
	
	private DataOutputStream outToServer; //Ausgabestream zum Server 
	private BufferedReader inFromServer; // Eingabestream vom Server

	private boolean serviceRequested = true; // Client läuft solange true
	
	private POP3_Server_Commands server_commands;
	
	private static int emailID = 0;

	//********************** KONSTRUKTOR *****************************
	
	/**
	 * "AUTOMATISIERTER KONSTRUKTOR" --> Beim erstellen eines Objektes wird alles weitere automatisch ausgeführt
	 * @param clientName
	 */
	POP3Client(String clientName, POP3_Server_Commands server_commands) {
		this.clientName = clientName;
		
		getInfos(); //Zu dem übergebnen clientName die Konten heraus suchen
		
		//Verzeichnnis erstellen
		try {
			Files.createDirectory(server_commands.getDirPath());
		} catch (IOException e) {
			System.err.println("Verzeichnis existiert bereits unter " + server_commands.getDirPath().toString() + "!");
		}
		
		this.server_commands = server_commands;
	}
	
	//*********************** METHODEN *******************************
	
	/**
	 * Beschafft die Kontoinformationen zu dem im Konstruktor übergebenen clientName
	 */
	private void getInfos() {
		for(Map.Entry<Info, String> pair : ServerAccountManagement.getAbbildung().entrySet()) {
				if(pair.getValue().compareTo(clientName) == 0) {
					infos.add(pair.getKey());
			}
		}
		//Falls kein client mit dem übergebenen clientName übereinstimmt
		if(infos.isEmpty()) {
			throw new IllegalArgumentException("Das '" + clientName + "' ist nicht vorhanden!");
		}
	}
	
	/**
	 * Programm start
	 */
	@Override
	public void run() {	
		String answerFromServer; // Antwort vom Server
		
		// ENDLOSSCHLEIFE --> Der client soll mails holen, bis wir nicht mehr möchten
		while (serviceRequested) {
			// für alle Konten des clienten, also für alle 'INFO' Objekte eine
			// Verbindung aufbauen und die Emails holen
			for (Info i : infos) {
				try {
					//****************************** VERBINDUNGSAUFBAU **************************************************
					
					Socket socket = verbindungAufbauen(i.getIp(), i.getServerPort());
					
					System.out.println("TCP-Client hat sich verbunden!");
					System.out.println("TCP-CLient beschafft Email von Emailadresse: "	+ i.getKontoName());

					//*********************************** AUTHENTIFIZIERUNG **********************************************
					
					boolean a = authentifizierung(i.getKontoName(), i.getPassword());
					
					if(! a) {
						System.err.println("Authentifizierung fehlgeschlagen, Benutzername oder Passwort falsch bei Konto: " + i.getKontoName());
						continue;
					}
					
					System.out.println("TCP-Client hat sich erfolgreich eingeloggt bei " + i.getKontoName());

					//*********************************** EMAIL BESCHAFFUNG **********************************************
					
					//Gibt Auskunft über das Konto an --> +OK mailbox ... has anzahl nachrichten
					answerFromServer = readFromServer();
					
					//Nachschauen wieviele ungelesenen Emails vorhanden sind
					writeToServer("STAT"); //Befehl gibt uns die Anzahl an Emails und Anzahlder Zeichen aus
					
					answerFromServer = readFromServer(); //+OK Anzahl und Zeichen
					
					//Extrahiert die Anzahl der Emails aus dem String
					int anzahlDerEmails = gibMirAnzahlDerMails(answerFromServer);
					
					//Falls keine Mails da sind, dann ist das nächste Konto dran
					if(anzahlDerEmails == 0) {
						//stat 0 0 --> Anzahl 0 und anzahl enthaltener Zeichen 0
						continue; //Wir gehen einen Durchlauf weiter, zum nächsten
					}
					
					//Ist die Anzahl != 0, dann wollen wir die Emails holen und abspeichern
					//Schleife um alle Mails auszulesen und abzuspeichern
					for(int j = 1; j <= anzahlDerEmails; j++) {
						//Befehl zum erhalten der Mail an den Server schicken
						writeToServer("RETR " + j);
						
						//Puffer für den Text
						List<String> pufferListe = new ArrayList<>();
						
						boolean flag = true;
						
						//komplette Mail auslesen
						//TODO: Klappt noch nicht ganz
						//while(! (readFromServer().contains("\r\n"))) --> ENDLOSSCHLEIFE !
						while(flag) {							
							String answer = readFromServer();
							
							//deleteDoubleDots(answer);
							
							pufferListe.add(answer);
							
							//WENN wir eine ZEILE mit nur einem PUNKT bekommen, sind wir durch
							//SIEHE RN FOLIE 2 ab SEITE 26 BEISPIELE
							if(answer.startsWith(".")) {
								if(checkIfLastDot(answer)) {
									flag = false;
								}
							}
						}
						//Email im Dateisystem abspeichern
						speicherDieEmail(pufferListe, ++emailID);
						
						//markiert die zu löschende Mail
						writeToServer("DELE " + j);
					}
					
					//beim quiten werden die durch DELE markierten Emails gelöscht
					//writeToServer("QUIT");
					
					//notify -> server_commands (Emailverzeichnis aktualisieren)
					server_commands.aktualisieren();
					
					//Verbindung wieder schließen
					socket.close();
					
				} catch (IOException e) {
					System.err.println("TCP Verbindung konnte zum Server nicht hergestellt werden");
				}
			}
		
			try {
				System.out.println("Abhol Service wartet 30 Sekunden mit dem erneuten abholen der Mails !");
				Thread.currentThread().sleep(30_000);
				System.out.println("Mails werden wieder abgeholt !\n");
			} catch (InterruptedException e) {
				System.err.println("Ich wurde beim schlafen aufgeweckt");
			}
		}
	}
	
	/**
	 * Baut eine Verbindung per Socket mit einem HOST auf
	 * @param ip
	 * @param port
	 * @return
	 * @throws IOException
	 */
	private Socket verbindungAufbauen(String ip, int port) throws IOException {
		Socket socket = new Socket(ip, port);
		
		/* Socket-Basisstreams durch spezielle Streams filtern */
		outToServer = new DataOutputStream(socket.getOutputStream());
		inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		return socket;
	}
	
	/**
	 * Führt die Authentifizierung eines Clienten mit dem Server durch
	 * @param benutzername
	 * @param passwort
	 * @return
	 * @throws IOException
	 */
	private boolean authentifizierung(String benutzername, String passwort) throws IOException {
		String answerFromServer;
		
		// Schreibe USER + name zum Server
		writeToServer("USER " + benutzername);
		
		//Antwort überprüfen
		answerFromServer = readFromServer(); //Antwort auf --> USER + kontoname
		
		if(answerFromServer.startsWith("-ERR")) {
			return false;
		}
		
		// Schreibe PASS + passwort zum Server
		writeToServer("PASS " + passwort);
		
		//Antwort überprüfen
		answerFromServer = readFromServer(); //Antwort auf --> PASS + passwort
		
		if(answerFromServer.startsWith("-ERR")) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gibt uns die Anzahl der vorhandenen EMAILS --> notwenig für die Schleifendurchläufe
	 * @param answerFromServer
	 * @return
	 */
	private int gibMirAnzahlDerMails(String answerFromServer) {
		int result = 0;
		
		//Anzahl der ungelesenen Mails filtern
		Scanner scanner = new Scanner(answerFromServer);
		
		scanner.next(); //Zum überspringen des --> +OK
		
		String zahl = "";
		
		//Beispiel: EINGABE --> LIST	AUSGABE DES SERVERS --> +OK 3 4070
		if(scanner.hasNext()) {
			zahl = scanner.next();
		}
		
		if(zahl.isEmpty()) {
			return 0;
		}
		
		result = Integer.parseInt(zahl);
		
		return result;
	}
	
	/**
	 * Speichert die email unter dem angegebenen Pfad in der Klasse ServerAccountManagement, mit dynamischen Dateinamen
	 * @param email
	 * @param emailNummer
	 * @throws IOException
	 */
	private void speicherDieEmail(List<String> email, int emailNummer) throws IOException {
		//VerzeichnisPfad holen und Dateipfad dynamisch erzeugen
		String path = server_commands.getDirPath().toFile().getAbsolutePath();
		path += "\\";
		
		//DateiPfad erzeugen
		path += "Email " + emailNummer + ".txt"; //TODO VIELLEICHT NOCH ZEIT DRANHÄNGEN
		
		//Datei erzeugen und den Pfad angeben
		File f = new File(path);
		
		//Zum schreiben in die Datei
		FileWriter fw = new FileWriter(f);

		//Wir fangen bei erster Stelle an, da wir das +OK ueberspringen wollen
		for(int i = 1; i < email.size(); i++) {
			fw.write(email.get(i));
		}
		
		//Den Writer schließen
		fw.close();
	}
	
	private boolean checkIfLastDot(String answer) {
		Scanner scanner = new Scanner(answer);
		scanner.nextLine(); //Auch der Punkt
		
		//überprüfen, ob nach dem Punkt noch etwas kommt
		try {
			scanner.next(); //Löst eine Exception aus, falls kein weiteres Element existiert
		} catch(NoSuchElementException e) {
			//System.out.println("SCANNER HAT NACH DEM PUNKT NICHTS MEHR GEFUNDEN!");									
			return true; //Schleife beenden, da Email komplett ausgelesen
		}
		return false;
	}
	
	private void deleteDoubleDots(String answer) {
		String puffer = "";
		
		//Punkte suchen und verdoppeln, wenn es nicht der letzte ist
		//Beispiel --> Mein Name. ist Flah. = Mein Name.. ist Flah.
		int count = 0;
		for(char c : answer.toCharArray()) {
			if(count == 0) {
				if(! (c == '.')) {
					puffer += c;
				} else {
					count++;
				}
			} else {
				if(! (c == '.')) {
					puffer += c;
				} else {
					puffer += c;
				}
			}
		}
	}
	
	//Leitet unsere Anfragen an den Server weiter
	private void writeToServer(String request) throws IOException {
		outToServer.writeBytes(request + '\n'); // Ausgabe an den Server
	}
	
	//Holt uns die Antwort des Servers auf eine Anfrage
	private String readFromServer() throws IOException {
		return inFromServer.readLine() + '\n';
	}
	
}