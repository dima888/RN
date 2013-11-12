package proxyServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/*
 * Verwaltet die Accounts
 */

class ServerAccountManagement  {

	//Verwalten aller Konten für fortgeschrittene (erstmall public, will es schnell fertig machen). Bei bedarf die KONTONAME aus der liste rausnehmen
	private Map<List<Object>, String> accountMap = new HashMap<>(); //String(0) => KONTONAME; List = {ip, port, KONTONAME, PASSWORD}
	private List<Object> infoList = new ArrayList<>();
	
	  private Path dirPath;
	  File f;
	
	//*******************Konstruktoren**************************
	public ServerAccountManagement() {
		
	}
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	
	//*****************************GETTER*******************************************
	public Path getDirPath() {
		return dirPath;
	}
	
	public String getIpFrom(String clientName) {
		String result = "NULL";
		for(Map.Entry<List<Object>, String> account : accountMap.entrySet()) {
			if(account.getValue().compareTo(clientName) == 0) {
				return (String) account.getKey().get(Information.IP);
			}
		}
		return result;
	}

	public int getServerPortFrom(String clientName) {
		int result = -1;
		for(Map.Entry<List<Object>, String> account : accountMap.entrySet()) {
			if(account.getValue().compareTo(clientName) == 0) {
				return (int) account.getKey().get(Information.PORT);
			}
		}
		return result;
	}

	public String getKONTONAMEFrom(String clientName) {
		String result = "NULL";
		for(Map.Entry<List<Object>, String> account : accountMap.entrySet()) {
			if(account.getValue().compareTo(clientName) == 0) {
				return (String) account.getKey().get(Information.KONTONAME);
			}
		}
		return result;
	}

	public String getPASSWORDFrom(String clientName) {
		String result = "NULL";
		for(Map.Entry<List<Object>, String> account : accountMap.entrySet()) {
			if(account.getValue().compareTo(clientName) == 0) {
				return (String) account.getKey().get(Information.PASSWORD);
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
	 * index(2) => KONTONAME
	 * index(3) => PASSWORD
	 * @param String clientName - Name des Clienten
	 * @param String ip - Die ip auf welchen Rechner wir connecten wollen
	 * @param Integer port - Die Anwendung, auf die wir zugreifen möchten
	 * @param String KONTONAME - eine email-Adresse
	 * @param String PASSWORD - zur email-Adresse das benötigte Passwort
	 */
	public void setAccount(String clientName, String ip, int port, String kontoname, String password, Path dirPath) {
		infoList = new ArrayList<>(Arrays.asList(ip, port, kontoname, password));
		accountMap.put(infoList, clientName);
		this.dirPath = dirPath;
		f = new File(this.dirPath.toString());
		System.out.println(accountMap);
		
		//Verzeichnnis erstellen
		try {
//			Files.createDirectory(server_commands.getDirPath());
			Files.createDirectory(this.dirPath);
		} catch (IOException e) {
//			System.err.println("Verzeichnis existiert bereits unter " + server_commands.getDirPath().toString() + "!");
			System.err.println("Verzeichnis existiert bereits unter " + this.dirPath + "!");
		}
	}

}
