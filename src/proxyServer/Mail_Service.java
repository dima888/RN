package proxyServer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Mail_Service {

	 static int emailNumber = 0;
	 private Map<File , Integer> emailMap = new HashMap<>();
	 
	 
	 //************** KONSTRUKTOR **************
	 
	 public Mail_Service() {
		getMails();
	}
	 
	 //***************** GETTER ********************
	public Map<File , Integer> getEmailMap() {
		return emailMap;
	}
	
	private void getMails() {
		for (File i : POP3_Server_Commands.f.listFiles()) {
			char[] puffer = i.getName().toCharArray();
			String result = "";
			for (int j = 6; j < puffer.length; j++) {
				if (puffer[j] == '.') {
					break;
				} else {
					result += puffer[j];
				}
			}
			emailMap.put(i, Integer.parseInt(result));
		}
	}
	
	void deleteMail(File mail) {
		for(Map.Entry<File, Integer> pair : emailMap.entrySet()) {
			if(pair.getKey() == mail) {
				emailMap.remove(pair);
			}
		}
	}
}
