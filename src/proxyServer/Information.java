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
	
	//Account informations
	public static final int IP = 0;
	public static final int PORT = 1;
	public static final int KONTONAME= 2;
	public static final int PASSWORD = 3;
	
	//Server informations
	public static final int SERVER_PORT = 11000;
	
	//Computer path informations
	public static Path aiLab11DimExtra = Paths.get("C:\\Users\\abg688.INFORMATIK\\Desktop\\EmailOrdner\\foxMail"); //Keine Ahnung warum so
	public static Path aiLab11Dim = Paths.get("C:\\Users\\abg688\\Desktop\\EmailOrdner\\foxMail");
	public static Path aiLab11Flah = Paths.get("C:\\Users\\abh840\\Desktop\\EmailOrdner\\foxMail");

	
}


