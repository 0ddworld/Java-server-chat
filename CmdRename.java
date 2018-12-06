import java.io.IOException;

/**
 *  @description Commande USER permet de revenir en etat d'identification
 *  @author Lienard Tristan
 *  @date 2017
 */

public class CmdRename implements Commande{
	private Utilisateur l_utilisateur;
	private String le_login;
	
	public CmdRename(Utilisateur u){
		l_utilisateur = u;
	}
	
	public void analyse(String[] s)  throws ErreurDeSyntaxeException { //D�cris dans l'interface Commande
		if(s.length != 2){
			throw new ErreurDeSyntaxeException("Mauvais nombre de param�tres");
		}
		le_login = s[1];
	}
	public String execute()  throws ErreurExecutionException { //D�cris dans l'interface Commande
		String reponse = "-ERR RENAME " + le_login + " : erreur";
		String login;
		TableUtilisateurs tab_ut = l_utilisateur.getServeur().getTableUtilisateurs();
		try{
			if(le_login.length() < 6 || le_login.length() > 15){
				reponse = "-ERR RENAME " + le_login + " : taille incorrecte";
			}
			else if(tab_ut.loginExiste(le_login)){
				reponse = "-ERR RENAME " + le_login + " : déjà utilisé";
			}
			else if(l_utilisateur.getServeur().chercherCompte(le_login, null)){
				reponse = "-ERR RENAME " + le_login + " : compte enrigistré avec ce pseudo";
			}
			else{
				l_utilisateur = l_utilisateur.getServeur().getTableUtilisateurs().supprime(l_utilisateur.getLogin());
				login = l_utilisateur.setLogin(le_login);
				tab_ut.ajoute(l_utilisateur);
				l_utilisateur.getServeur().diffuse(le_login, Notification.RENAME," " + login + " to " + le_login);
				reponse = "+OK Rename " + le_login;
			}
		}catch(IOException e){
			System.out.println("Erreur lors de la lecture du fichier compte.txt");
		}
		return reponse;
	}

}
