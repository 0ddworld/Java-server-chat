
/**
 *  @description Une commande repr�sente une demande particuli�re du client
 *  @author Lienard Tristan
 *  @date 2017
 */

public interface Commande {
	public static final String USER = "USER"; //Commande USER
	public static final String MSGTO = "MSGTO"; //Commande MSGTO
	public static final String LIST = "LIST"; //Commande LIST
	public static final String QUIT = "QUIT"; //Commande QUIT
	public static final String HELP = "HELP"; //Commande HELP
	public static final String DISCONNECT = "DISCONNECT"; //Commande DISCONNECT
	public static final String RENAME = "RENAME"; //Commande RENAME
	public static final String INSCRIPTION = "INSCRIPTION"; //Commande INSCRIPTION
	
	public void analyse(String[] commande) throws ErreurDeSyntaxeException;
	//Analyse le tableau en entr�e pour y extraire les arguments de la commande
	
	public String execute() throws ErreurExecutionException;
	//Ex�cute la commande
}
