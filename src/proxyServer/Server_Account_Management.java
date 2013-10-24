package proxyServer;

import java.util.HashMap;
import java.util.Map;

/*
 * Verwaltet die Accounts
 */
class Server_Account_Management {
	
	private static Map<Info, String> abbildung = new HashMap<>();
	private Info information; 
	
	public Server_Account_Management(String clientName, String ip, int serverPort, String kontoName, String password) {
		information = new Info(ip, serverPort, kontoName, password);
		abbildung.put(information, clientName);
	}
	
//	/**
//	 * @return the ip
//	 */
//	public String getIp(String clientName) {
//		return information.ip;
//	}
//
//	/**
//	 * @return the serverPort
//	 */
//	public int getServerPort(String clientName) {
//		return information.serverPort;
//	}
//
//	/**
//	 * @return the kontoName
//	 */
//	public String getKontoName(String clientName) {
//		return information.kontoName;
//	}
//
//	/**
//	 * @return the password
//	 */
//	public String getPassword(String clientName) {
//		return information.password;
//	}
//	
	/*
	 * Gibt uns die Konten zum User
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
	
	/*
	 * Hier werden die Kontoinformationen eingetragen
	 */
	public static void main(String[] args) {
//		Server_Account_Management account = new Server_Account_Management();
//		Server_Account_Management account2 = new Server_Account_Management();
	}
}
