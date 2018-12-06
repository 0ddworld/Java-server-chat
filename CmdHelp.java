
/**
 *  @description Commande Commande help permet de connaitre la liste des commandes et leurs effets
 *  @author Lienard Tristan
 *  @date 2017
 */

public class CmdHelp implements Commande{
	
	public CmdHelp(){
	}
	public void analyse(String[] s)  throws ErreurDeSyntaxeException { //D�cris dans l'interface Commande
		if(s.length != 1){
			throw new ErreurDeSyntaxeException("nombre de param�tres incorrect");
		}
	}
	public String execute()  throws ErreurExecutionException { //D�cris dans l'interface Commande
		String reponse = "LIST : permet de connaitre la liste des utilisateurs connecté\n"
				+ "MSGTO [ u ] message : permet d'envoyer un message à un utilisateur\n"
				+ "QUIT : permet de deconnecter proprement\n"
				+ "DISCONNECT : permet de revenir a l'etat d'identification\n"
				+ "RENAME : permet de changer de pseudo (n'affecte pas le compte inscris, il est recommande de s'inscrire a nouveau\n"
				+"INSCRIPTION : permet de s'inscrire avec un mdp (inscription definitive)\n"
				+ "+OK HELP END";
		return reponse;
	}
}
