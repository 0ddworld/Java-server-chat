
/**
 *  @description Commande USER permet de revenir en etat d'identification
 *  @author Lienard Tristan
 *  @date 2017
 */

public class CmdDisconnect implements Commande{
	private Utilisateur l_utilisateur; //L'objet repr�sentant l'utilisateur
	
	public CmdDisconnect(Utilisateur u){
		l_utilisateur = u;
	}
	public void analyse(String[] s) throws ErreurDeSyntaxeException { //Decris dans l'interface Commande
		if(s.length != 1){
			throw new ErreurDeSyntaxeException("nombre de param�tres incorrect");
		}
	}
	public String execute() throws ErreurExecutionException { //Decris dans l'interface Commande
		String reponse = "+OK Disconnect";
		Serveur srv = l_utilisateur.getServeur();
			srv.creerIdentification(l_utilisateur.getSocket());
			srv.desenregistrerUtilisateur(l_utilisateur.getLogin());
		return reponse;
	}
}
