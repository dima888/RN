package proxyServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/*
 * Verwaltet die Accounts
 */

//TODO: Clientname ist redundant, meinens empfinden gerade, es ist 23Uhr -_-
class ServerAccountManagement {
	
	//Verwalten aller Konten für fortgeschrittene (erstmall public, will es schnell fertig machen). Bei bedarf die kontoname aus der liste rausnehmen
	private Map<List<Object>, String> accountMap = new HashMap<>(); //String(0) => kontoname; List = {ip, port, kontoname, password}
	private List<Object> infoList = new ArrayList<>();
	
	//*******************Konstruktoren**************************
	public ServerAccountManagement(String clientName, String ip, int serverPort, String kontoName, String password) {
//		Info information = new Info(ip, serverPort, kontoName, password);
//		abbildung.put(information, clientName);
	}
	
	public ServerAccountManagement() {
		
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	
	//*****************************GETTER*******************************************
	public String getIpFrom(String clientName) {
		String result = "NULL";
		for(Map.Entry<List<Object>, String> account : accountMap.entrySet()) {
			if(account.getValue().compareTo(clientName) == 0) {
				return (String) account.getKey().get(0);
			}
		}
		return result;
	}

	public int getServerPortFrom(String clientName) {
		int result = -1;
		for(Map.Entry<List<Object>, String> account : accountMap.entrySet()) {
			if(account.getValue().compareTo(clientName) == 0) {
				return (int) account.getKey().get(1);
			}
		}
		return result;
	}

	public String getKontoNameFrom(String clientName) {
		String result = "NULL";
		for(Map.Entry<List<Object>, String> account : accountMap.entrySet()) {
			if(account.getValue().compareTo(clientName) == 0) {
				return (String) account.getKey().get(2);
			}
		}
		return result;
	}

	public String getPasswordFrom(String clientName) {
		String result = "NULL";
		for(Map.Entry<List<Object>, String> account : accountMap.entrySet()) {
			if(account.getValue().compareTo(clientName) == 0) {
				return (String) account.getKey().get(3);
			}
		}
		return result;
	}
	
	public Map<List<Object>, String> getAccountMap() {
		return accountMap;
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	/**
	 * Account definieren
	 * index(0) => ip
	 * index(1) => port
	 * index(2) => kontoname
	 * index(3) => password
	 * @param clientName
	 * @param ip
	 * @param port
	 * @param kontoname
	 * @param password
	 */
	public void setAccount(String clientName, String ip, int port, String kontoname, String password) {
		infoList = new ArrayList<>(Arrays.asList(ip, port, kontoname, password));
		accountMap.put(infoList, clientName);
		System.out.println(accountMap);
	}

}
