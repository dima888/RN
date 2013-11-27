package proxyServer;

import java.nio.file.Path;

/* IP: lab30.cpt.haw-hamburg.de
 * PORT: 11_000
 * Username: bai4rnpX
 * 
 * IP: lab31.cpt.haw-hamburg.de
 * PORT: 11_000
 * Username: bai4rnpX
 */

public class ProxyServerFaktory {
	
	private ServerAccountManagement serverAccountManagement = new ServerAccountManagement();
	
	private POP3_Server_Commands server_commands = new POP3_Server_Commands();
	
	private void starteClient() {
		POP3Client client = new POP3Client(Information.username, server_commands); //Hier evtl user wechseln
		client.setServerAccountManagement(serverAccountManagement);
		client.start();
	}
	
	private void starteServer() {
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
	
	public void setMailKonto(String username, String mailServerIP, int mailServerPort, String mailAccountName, String mailAccountPassword, Path mailPath) {
		serverAccountManagement.setAccount(username, mailServerIP, mailServerPort, mailAccountName, mailAccountPassword, mailPath);	
	}


	
	public void startTheProgramm() {
		this.starteProxyServer();
	}
	
}
