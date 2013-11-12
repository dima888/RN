package proxyServer;

import java.nio.file.Paths;

//TODO: Wenn wir zwei verschiedene user zulassen wollen, dann müssen wir leider zwei serverAccountManagment erzeugen.

/* IP: lab30.cpt.haw-hamburg.de
 * PORT: 11_000
 * Username: bai4rnpX
 * 
 * IP: lab31.cpt.haw-hamburg.de
 * PORT: 11_000
 * Username: bai4rnpX
 */

public class ProxyServerFaktory {
	
	
	
	String username = "foxhound";
	String username2 = "flah";


	private ServerAccountManagement serverAccountManagement = new ServerAccountManagement();
//	private ServerAccountManagement serverAccountManagement2 = new ServerAccountManagement();
	
	private POP3_Server_Commands server_commands = new POP3_Server_Commands();
	
	private void starteClient() {
		POP3Client client = new POP3Client(username, server_commands);
		client.setServerAccountManagement(serverAccountManagement);
		client.start();
		
//		POP3Client client2 = new POP3Client(username2, server_commands);
//		client2.setServerAccountManagement(serverAccountManagement2);
//		client2.start();
	}
	
	private void starteServer() {
		//POP3Server server = new POP3Server(server_commands);
		POP3Server server = new POP3Server();
		server.setServerAccountManagement(serverAccountManagement); //neu
		server.setPOP3_Server_Commands(server_commands);
	}
	
	private void starteProxyServer() {
		this.server_commands.setServerAccountManagement(serverAccountManagement); //neu
		this.server_commands.aktualisieren();	

		this.starteClient();
		this.starteServer();		
	}
	
	private void setAccounts() {
		serverAccountManagement.setAccount(username, "pop.gmx.net", 110, "dima888@gmx.net", "12345678", Information.aiLab11DimExtra);
		serverAccountManagement.setAccount(username, "pop.gmx.de", 110, "flah_ahmad@gmx.de", "RN2013Huebner", Information.aiLab11DimExtra); 
	}
	
//	public void initialize() {
//		
//	}
	

	
	public void startTheProgramm() {
		// ProxyServer erstellen
		ProxyServerFaktory proxyServer = new ProxyServerFaktory();
		proxyServer.setAccounts(); // Account zulassen
		proxyServer.starteProxyServer();
	}
	
}
