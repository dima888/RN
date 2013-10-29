package proxyServer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/*
 * Verwaltet die Accounts
 */
class ServerAccountManagement {
	
	//Verwaltet alle Konten für einen Benutzer
	private static Map<Info, String> abbildung = new HashMap<>();
	
	public ServerAccountManagement(String clientName, String ip, int serverPort, String kontoName, String password) {
		Info information = new Info(ip, serverPort, kontoName, password);
		abbildung.put(information, clientName);
	}
	
	/**
	 * Hier werden die Information aller User in einer Map verwaltet. Info -> (ip, serverport, kontoName, password); User -> "Whatever"
	 * @return Map - abbildung
	 */
	public static Map<Info, String> getAbbildung() {
		return abbildung;
	}
	
	// Datenbehälter für Kontoinformationen
	class Info {
		private String ip;
		private int serverPort;
		private String kontoName;
		private String password;
		
		Info(String ip, int serverPort, String kontoName, String password) {
			this.ip = ip;
			this.serverPort = serverPort;
			this.kontoName = kontoName;
			this.password = password;
		}

		public String getIp() {
			return ip;
		}

		public int getServerPort() {
			return serverPort;
		}

		public String getKontoName() {
			return kontoName;
		}

		public String getPassword() {
			return password;
		}
	}
}
