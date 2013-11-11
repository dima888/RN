package proxyServer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.RowFilter.Entry;

/*
 * Verwaltet die Accounts
 */

//TODO: Clientname ist redundant, meinens empfinden gerade, es ist 23Uhr -_-
class ServerAccountManagement {
	
	private String ip;
	private int serverPort;
	private String kontoName;
	private String password;
	
	//Verwaltet alle Konten für einen Benutzer
	private static Map<Info, String> abbildung = new HashMap<>(); //old version
	
	//Verwalten aller Konten für fortgeschrittene (erstmall public, will es schnell fertig machen). Bei bedarf die kontoname aus der liste rausnehmen
	private Map<String , List<Object>> accountMap = new HashMap<>(); //String(0) => kontoname; List = {ip, port, kontoname, password}
	private List<Object> infoList = new ArrayList<>();
	
	//*******************Konstruktoren**************************
	public ServerAccountManagement(String clientName, String ip, int serverPort, String kontoName, String password) {
		Info information = new Info(ip, serverPort, kontoName, password);
		abbildung.put(information, clientName);
	}
	
	public ServerAccountManagement() {
		
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	//*****************************GETTER*******************************************
	public String getIpFrom(String kontoname) {
		String result = "NULL";
		for(Map.Entry<String, List<Object>> account : accountMap.entrySet()) {
			if(account.getKey().compareTo(kontoname) == 0) {
				return (String) account.getValue().get(0);
			}
		}
		return result;
	}

	public int getServerPortFrom(String kontoname) {
		int result = -1;
		for(Map.Entry<String, List<Object>> account : accountMap.entrySet()) {
			if(account.getKey().compareTo(kontoname) == 0) {
				return (int) account.getValue().get(1);
			}
		}
		return result;
	}

	public String getKontoNameFrom(String kontoname) {
		String result = "NULL";
		for(Map.Entry<String, List<Object>> account : accountMap.entrySet()) {
			if(account.getKey().compareTo(kontoname) == 0) {
				return (String) account.getValue().get(2);
			}
		}
		return result;
	}

	public String getPasswordFrom(String kontoname) {
		String result = "NULL";
		for(Map.Entry<String, List<Object>> account : accountMap.entrySet()) {
			if(account.getKey().compareTo(kontoname) == 0) {
				return (String) account.getValue().get(3);
			}
		}
		return result;
	}
	
	public Map<String, List<Object>> getAccountMap() {
		return accountMap;
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	/**
	 * Account definieren
	 * index(0) => ip
	 * index(1) => port
	 * index(2) => kontoname
	 * index(3) => password
	 */
	public void setAccount(String clientName, String ip, int port, String kontoname, String password) {
		infoList = new ArrayList<>(Arrays.asList(ip, port, kontoname, password));
		accountMap.put(kontoname, infoList);
		System.out.println(accountMap);
	}
	
	/**
	 * Hier werden die Information aller User in einer Map verwaltet. Info -> (ip, serverport, kontoName, password); User -> "Whatever"
	 * @return Map - abbildung
	 */
	public static Map<Info, String> getAbbildung() {
		return abbildung;
	}
	
	
	
	
	

	
	
	
	//TODO: Info, finde diese Klasse echt überflussig und unnötig kompliziert bei händeln von daten
	
	
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
