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
	// PFad  zum Verzeichnis, in dem wir die Emails speichern möchten
	private static Path dirPath = Paths.get("C:\\Users\\Flah\\Desktop\\Emails");
	
	public ServerAccountManagement(String clientName, String ip, int serverPort, String kontoName, String password) {
		Info information = new Info(ip, serverPort, kontoName, password);
		abbildung.put(information, clientName);
	}
	
	public static Map<Info, String> getAbbildung() {
		return abbildung;
	}
	
	public static Path getDirPath() {
		return dirPath;
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
