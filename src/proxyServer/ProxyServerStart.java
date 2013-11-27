package proxyServer;

public class ProxyServerStart {

	public static void main(String[] args) {
		ProxyServerFaktory f = new ProxyServerFaktory();
		f.setMailKonto(Information.username, Information.account1MailServerIP, Information.account1MailServerPort, Information.account1Username, Information.account1Password, Information.aiLab11Dim);
		//f.setMailKonto(Information.username, Information.account1MailServerIP, Information.account1MailServerPort, Information.account2Username, Information.account2Password, Information.aiLab11Dim);
		f.startTheProgramm();
	}
}
