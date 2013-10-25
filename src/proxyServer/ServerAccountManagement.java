package proxyServer;

import java.util.HashMap;
import java.util.Map;

/*
 * Verwaltet die Accounts
 */
class ServerAccountManagement {
	
	private static Map<Info, String> abbildung = new HashMap<>();
	private Info information; 
	
	public ServerAccountManagement(String clientName, String ip, int serverPort, String kontoName, String password) {
		information = new Info(ip, serverPort, kontoName, password);
		abbildung.put(information, clientName);
	}
	
	/*
	 * Gibt uns die Konteninformationen
	 */
	public static Map<Info, String> getAbbildung() {
		return abbildung;
	}
	
	// Datenbehälter
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

		/**
		 * @return the ip
		 */
		public String getIp() {
			return ip;
		}

		/**
		 * @return the serverPort
		 */
		public int getServerPort() {
			return serverPort;
		}

		/**
		 * @return the kontoName
		 */
		public String getKontoName() {
			return kontoName;
		}

		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
		}
	}
}
