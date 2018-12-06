
/**
 *  @description Commande Commande LIST permet de connaitre la liste des utilisateurs
 *  @author Lienard Tristan
 *  @date 2017
 */

public class CmdList implements Commande{
	private Utilisateur l_utilisateur; //L'objet repr�sentant l'utilisateur
	
	public CmdList(Utilisateur u){
		l_utilisateur = u;
	}
	public void analyse(String[] s)  throws ErreurDeSyntaxeException { //D�cris dans l'interface Commande
		if(s.length != 1){
			throw new ErreurDeSyntaxeException("nombre de param�tres incorrect");
		}
	}
	public String execute()  throws ErreurExecutionException { //D�cris dans l'interface Commande
		Serveur srv = l_utilisateur.getServeur();
		TableUtilisateurs tab_ut = srv.getTableUtilisateurs();
		String[] logins = tab_ut.getAllLogins();
		String reponse = logins[0]+ "\n";
		for(int i = 1; i < logins.length; i++){
			reponse = reponse+logins[i]+"\n";
		}
		reponse = reponse +"+OK LIST END";
		return reponse;
	}
}