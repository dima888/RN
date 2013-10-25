package proxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Timestamp;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import proxyServer.ServerAccountManagement.Info;

/********************************************* TODO *********************************************************************
 * Die Mails lassen sich schon abholen und speichern jedoch nicht so wie es sollte.
 * Worauf genau muss man achten beim holen und abspeichern ? Bisher ist es so,
 * dass auf nichts geachtet wird, sondern nur der Inhalt der Mail in einer Datei abgespeichert wird.
 * Bis auf diesen Punkt, funktioniert der rest. 
 * Das Auslesen der Mail ist bisher, sehr hässlich gelöst mit einer endlosschleife und einem 'break' drin.
 * Ich habe es anders versucht, doch dies endete in einer kompletten Endlosschleife
 * 
 * Fehlen tut noch, dass der Client nach der ausführung 30 Sekunden schlafen geht. Mit Thread.currentThread().sleep() hat 
 * es irgendwie nicht geklappt. Dies ist aber ein kleineres Übel.
 * 
 * Man könnte eventuell den Konstruktor um noch ein Parameter erweitern, falls man die Pfadangabe
 * dynamisch machen möchte zum Verzeichnis.
 * 
 * Ich wäre dafür den Pfad zum Verzeichnis in der Klasse ServerAccountManagement zu verwalten, da wir diesen einmal in
 * dieser Klasse benötigen und einmal in der POP3Server Klasse. Somit haben wir Code duplizierung und müssen auch, wenn
 * wir ihn mal ändern müssen, dies an zwei Stellen tun.
 * 
 * Ich habe mir ein GMX Konto eingerichtet, was du natürlich auch verwenden kannst.
 *************************************************************************************************************************/

class POP3Client {
	
	//********************* ATTRIBUTE *****************************
	
	private String clientName;
	//Liste mit den Kontoinformationen für das jeweilige Konto
	private List<Info> infos = new ArrayList<>();
	
	/* Server, der Verbindungsanfragen entgegennimmt */
	private Path dirPath = Paths.get("C:\\Users\\Flah\\Desktop\\Emails"); //Pfad des verzeichnisses festlegen unter diesem werden die datein abgespeichert
	
	Socket socket; // VerbindungsSocket mit Client
	
	private DataOutputStream outToServer; //Ausgabestream zum Server 
	private BufferedReader inFromServer; // Eingabestream vom Server

	private boolean serviceRequested = true; // Client läuft solange true

	//********************** KONSTRUKTOR ******************************
	
	POP3Client(String clientName) {
		this.clientName = clientName;
		getInfos(); //Zu dem übergebnen clientName die Konten heraus suchen
		
		//Verzeichnnis erstellen
		try {
			Files.createDirectory(dirPath);
		} catch (IOException e) {
			System.err.println("Verzeichnis existiert bereits unter " + dirPath.toString() + "!");
		}
		
		startePOP3_Client(); //Das abholen der Mails starten
	}
	
	//*********************** METHODEN *******************************
	
	//Beschafft die Kontoinformationen zu dem im Konstruktor übergebenen clientName
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
		
	void startePOP3_Client() {		
		String requestFromClient; // Anfrage des Clienten
		String answerFromServer; // Antwort vom Server
		
		// ENDLOSSCHLEIFE --> Der client soll mails holen, bis wir nicht mehr
		// möchten
		//while (serviceRequested) {
			// für alle Konten des clienten, also für alle 'INFO' Objekte eine
			// Verbindung aufbauen und die Emails holen
			for (Info i : infos) {
				try {
					//****************************** VERBINDUNGSAUFBAU **************************************************
					
					socket = new Socket(i.getIp(), i.getServerPort());
					System.out.println("TCP-Client hat sich verbunden!");

					/* Socket-Basisstreams durch spezielle Streams filtern */
					outToServer = new DataOutputStream(socket.getOutputStream());
					inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					System.out.println("TCP-CLient beschafft Email von Emailadresse: "	+ i.getKontoName());

					//*********************************** AUTHENTIFIZIERUNG **********************************************
					
					// Schreibe USER + name zum Server
					writeToServer("USER " + i.getKontoName());

					// Hole Antwort vom Server
					answerFromServer = readFromServer(); //NACHRICHT --> POP server ready...
					answerFromServer = readFromServer(); //Antwort auf --> USER + kontoname

					// Prüfe ob Server Kontoname akzeptiert hat
					pruefeObServerKontoNameAkzeptiertHat(answerFromServer, i);
					
					// Schreibe PASS + passwort zum Server
					writeToServer("PASS " + i.getPassword());

					// Hole Antwort vom Server
					answerFromServer = readFromServer();
					
					// Prüfe ob Server Passwort akzeptiert hat
					pruefeObServerPasswortAkzeptiertHat(answerFromServer, i);
					
					System.out.println("TCP-Client hat sich erfolgreich eingeloggt bei + " + i.getKontoName());

					//*********************************** EMAIL BESCHAFFUNG **********************************************
					
					//Nachschauen wieviele ungelesenen Emails vorhanden sind
					writeToServer("STAT"); //Befehl gibt uns die Anzahl an Emails und Anzahlder Zeichen aus
					
					answerFromServer = readFromServer(); //+OK Anzahl und Zeichen
					
					//Anzahl der ungelesenen Mails filtern
					Scanner scanner = new Scanner(answerFromServer);
					scanner.next(); //Zum überspringen des --> +OK
					//Beispiel: EINGABE --> LIST	AUSGABE DES SERVERS --> +OK 3 4070
					String zahl = scanner.next();
					int anzahl = Integer.parseInt(zahl);
					
					//Falls keine Mails da sind, dann ist das nächste Konto dran
					if(anzahl == 0) {
						continue; //Wir gehen einen Durchlauf weiter, zum nächsten
					}
					
					//Ist die Anzahl != 0, dann wollen wir die Emails holen und abspeichern
					//Schleife um alle Mails auszulesen und abzuspeichern
					for(int j = 1; j <= anzahl; j++) {
						
						//Befehl zum erhalten der Mail an den Server schicken
						writeToServer("RETR " + j);
						//System.out.println(readFromServer());
						
						//VerzeichnisPfad holen
						String path = dirPath.toFile().getAbsolutePath();
						path += "\\";
						
						//DateiPfad erzeugen
						path += "Email " + j + ".txt"; //TODO VIELLEICHT NOCH ZEIT DRANHÄNGEN
						
						//Datei erzeugen und den Pfad angeben
						File f = new File(path);
						
						//Zum schreiben in die Datei
						FileWriter fw = new FileWriter(f);
						
						//komplette Mail auslesen
						//TODO: Klappt noch nicht ganz
						//while(! (readFromServer().contains("\r\n"))) --> ENDLOSSCHLEIFE !
						while(true) {
							String answer = readFromServer();
							
							//Kommt keine Antwort mehr vom Server, haben wir alles erhalten
							if(answer.isEmpty()) {
								break;
							}
							
							fw.write(answer);
						}
						fw.close();
					}

					//Verbindung wieder schließen
					socket.close();
					
				} catch (IOException e) {
					System.out.println("TCP Verbindung konnte zum Server nicht hergestellt werden");
				} 
			}
		//}
	}
	
	private boolean pruefeObServerKontoNameAkzeptiertHat(String answerFromServer, Info i) {
		Scanner scanner = new Scanner(answerFromServer);
		if(scanner.next().compareTo("+OK") == 0) {
			scanner.close();
			System.out.println("MailServer hat Kontoname: " + i.getKontoName()	+ " akzeptiert");
			return true;
		} else {
			scanner.close();
			System.err.println("MailServer hat Kontoname: " + i.getKontoName()	+ " nicht akzeptiert");
			return false;
		}		
	}
	
	private boolean pruefeObServerPasswortAkzeptiertHat(String answerFromServer, Info i) {
		Scanner scanner = new Scanner(answerFromServer);
		if(scanner.next().compareTo("+OK") == 0) {
			scanner.close();
			System.out.println("MailServer hat Passwort: " + i.getPassword() + " akzeptiert");
			return true;
		} else {
			scanner.close();
			System.err.println("MailServer hat Passwort: " + i.getPassword() + " nicht akzeptiert");
			return false;
		}		
	}
	
	private void writeToServer(String request) throws IOException {
		outToServer.writeBytes(request + '\n'); // Ausgabe an den Server
	}
	
	private String readFromServer() throws IOException {
		return inFromServer.readLine();
	}
	
	public static void main(String[] args) {
		
	}
}
