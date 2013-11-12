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

	//BenutzerDaten abspeichern --> Beispiel ACCOUNT
//	private ServerAccountManagement account1 = new ServerAccountManagement("foxhound", "pop.gmx.de", 110, "flah_ahmad@gmx.de", "RN2013Huebner");
	private ServerAccountManagement account2 = new ServerAccountManagement("foxhound", "pop.gmx.net", 110, "dima888@gmx.net", "12345678");
	private ServerAccountManagement serverAccountManagement = new ServerAccountManagement();
	
	//private POP3_Server_Commands server_commands = new POP3_Server_Commands(Paths.get("C:\\Users\\foxhound\\Desktop\\Emails"));
	private POP3_Server_Commands server_commands = new POP3_Server_Commands(Paths.get("C:\\Users\\abh840\\Desktop\\Emails"));
	
	private void starteClient() {
		POP3Client client = new POP3Client("foxhound", server_commands);
		client.start();
	}
	
	private void starteServer() {
		POP3Server server = new POP3Server(server_commands);
	}
	
	private void starteProxyServer() {
		this.starteClient();
		this.server_commands.aktualisieren();	
		this.server_commands.setServerAccountManagement(serverAccountManagement); //neu
		this.starteServer();
	}
	
	private void setAccounts() {
		serverAccountManagement.setAccount("foxhound", "pop.gmx.net", 110, "dima888@gmx.net", "12345678");
		serverAccountManagement.setAccount("foxhound", "pop.gmx.de", 110, "flah_ahmad@gmx.de", "RN2013Huebner"); //PW -> -_-
	}
	
	public static void main(String[] args) {					
		//ProxyServer erstellen
		ProxyServerStart proxyServer = new ProxyServerStart();
		proxyServer.setAccounts(); //Account zulassen
		proxyServer.starteProxyServer();
	}	
}
