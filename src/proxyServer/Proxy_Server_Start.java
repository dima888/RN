package proxyServer;

import java.nio.file.Paths;

public class Proxy_Server_Start {
	
	public void starteClient() {
		POP3_Client client = new POP3_Client();
		client.startJob();
	}
	
	public void starteServer() {
		POP3_Server server = new POP3_Server(Paths.get("")); //PFAD angeben
	}
	
	public static void main(String[] args) {
		Proxy_Server_Start proxyServer = new Proxy_Server_Start();
		proxyServer.starteServer();
		proxyServer.starteClient();
	}
	
}
