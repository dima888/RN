package proxyServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

class POP3_Client {
	
	/* Server, der Verbindungsanfragen entgegennimmt */
	private final String IP;
	private final int SERVER_PORT; // Auf diesen Port wird "gelauscht"
	private Path path;
	private static int count = 0;
	
	POP3_Client(final String IP, final int SERVER_PORT) {
		this.IP = IP;
		this.SERVER_PORT = SERVER_PORT;
	}
	
	// TODO
	void startePOP3_Client() {
		Socket connectionSocket; // VerbindungsSocket mit Client
	}
}
