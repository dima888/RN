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
	
	// PFad  zum Verzeichnis, in dem wir die Emails speichern möchten --> erstellt für uns ein Verzeichnis
	//private static Path dirPath = Paths.get("C:\\Users\\Flah\\Desktop\\Emails");
	private static Path dirPath = Paths.get("C:\\Users\\dima\\Desktop\\Emails");
//	private static Path dirPath2 = Paths.get("C:\\Users\\foxhound\\Desktop\\Emails");
	private static Path dirPath2 = Paths.get("C:\\Users\\abg688\\Desktop\\Emails");
	private static Path dirPathLab = Paths.get("C:\\Users\\Flah\\Desktop\\Emails");
	
	
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
	//*************** GETTER *******************
	public static Path getDirPath() {
		return dirPath;
	}
	
	public static Path getDirPath2() {
		return dirPath2;
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
