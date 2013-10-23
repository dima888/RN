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

public class Proxy_Server_Start {
	
	/**
	 * Client initiiert eine Verbindung mit einem Server
	 * @param IP - IP des Servers
	 * @param SERVER_PORT - Port des Servers
	 */
	public void starteClient(final String IP, final int SERVER_PORT) {
		POP3_Client client = new POP3_Client(IP, SERVER_PORT);
	}
	
	public void starteServer() {
		POP3_Server server = new POP3_Server(Paths.get("")); //PFAD angeben
	}
	
	public static void main(String[] args) {
		Proxy_Server_Start proxyServer = new Proxy_Server_Start();
		proxyServer.starteServer();
		proxyServer.starteClient("", 123); //für lab30
		proxyServer.starteClient("", 123); //für lab31
		
	}	
}
