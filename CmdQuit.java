
/**
 *  @description Commande QUIT permet de se d�connect� proprement
 *  @author Lienard Tristan
 *  @date 2017
 */

public class CmdQuit implements Commande{
	private Utilisateur l_utilisateur; //L'objet repr�sentant l'utilisateur
	
	public CmdQuit(Utilisateur u){
		l_utilisateur = u;
	}
	public void analyse(String[] s)  throws ErreurDeSyntaxeException { //D�cris dans l'interface Commande
		if(s.length != 1){
			throw new ErreurDeSyntaxeException("nombre de param�tres incorrect");
		}
	}
	public String execute()  throws ErreurExecutionException { //D�cris dans l'interface Commande
		String reponse = "+OK QUIT";
		Serveur srv = l_utilisateur.getServeur();
		l_utilisateur.stopper(reponse);
		srv.desenregistrerUtilisateur(l_utilisateur.getLogin());
		return reponse;
	}
}