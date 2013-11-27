package proxyServer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author abg688
 * Account definieren
 * index(0) => ip
 * index(1) => port
 * index(2) => kontoname
 * index(3) => password
 *
 */

public class Information {
	
	//Return Values
	final public static String ok = "+OK ";
	final public static String err = "-ERR ";
	
	//Account informations
	public static final int IP = 0;
	public static final int PORT = 1;
	public static final int KONTONAME = 2;
	public static final int PASSWORD = 3;
	
	//Server informations
	public static final int SERVER_PORT = 11000;
	
	//Computer path informations
	public static Path aiLab11DimExtra = Paths.get("C:\\Users\\abg688.INFORMATIK\\Desktop\\EmailOrdner\\foxMail"); //Keine Ahnung warum so
	public static Path laptopDim = Paths.get("C:\\Users\\dima\\Desktop\\EmailOrdner"); //Keine Ahnung warum so
	public static Path aiLab11Dim = Paths.get("C:\\Users\\abg688\\Desktop\\foxMail");
	public static Path dimHome = Paths.get("C:\\Users\\foxhound\\Desktop\\EmailOrdner");
	public static Path aiLab11Flah = Paths.get("C:\\Users\\abh840\\Desktop\\EmailOrdner\\foxMail");	
	public static Path lab30 = Paths.get("C:\\Users\\abh840\\Desktop\\EmailOrdner\\foxMail");
	
	//Username 
	final public static String username = "foxhound"; 
	
	//First Accounts Informationen
	public static String account1Username = "flah_ahmad@gmx.de"; 
	public static String account1Password = "RN2013Huebner";
	public static String account1MailServerIP = "pop.gmx.de";
	public static int account1MailServerPort = 110;
	
	//Second Account Informationen
	public static String account2Username = "foxhound.1@gmx.de"; 
	public static String account2Password = "12345678";
	public static String account2MailServerIP = "pop.gmx.de";
	public static int account2MailServerPort = 110;
	
}


