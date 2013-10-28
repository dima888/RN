package proxyServer;

import java.nio.file.Paths;

/* IP: lab30.cpt.haw-hamburg.de
 * PORT: 11_000
 * Username: bai4rnpX
 * 
 * IP: lab31.cpt.haw-hamburg.de
 * PORT: 11_000
 * Username: bai4rnpX
 */

public class ProxyServerStart {
	
	/**
	 * Client initiiert eine Verbindung mit einem Server
	 * @param IP - IP des Servers
	 * @param SERVER_PORT - Port des Servers
	 */
	public void starteClient(String clientName) {
		POP3Client client = new POP3Client(clientName);
	}
	
	public void starteServer() {
		POP3Server server = new POP3Server(Paths.get("")); //PFAD angeben
	}
	
	public static void main(String[] args) {
		//*************************************************************************************************************************************************
		//BenutzerDaten abspeichern --> Beispiel ACCOUNT
		ServerAccountManagement account1 = new ServerAccountManagement("flah", "pop.gmx.de", 110, "flah_ahmad@gmx.de", "RN2013Huebner"); //Bitte Passwort nicht löschen, sonst vergesse ich es !
		ServerAccountManagement account2 = new ServerAccountManagement("foxhound", "pop.gmx.net", 110, "dima888@gmx.net", "12345678");
		ServerAccountManagement account3;
		
		//*************************************************************************************************************************************************
		//ProxyServer erstellen
		ProxyServerStart proxyServer = new ProxyServerStart();
		
		//ProxyServer starten
		//proxyServer.starteServer();
		proxyServer.starteClient("foxhound");
	}	
}
