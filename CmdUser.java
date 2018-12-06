import java.io.IOException;

/**
 *  @description Commande USER permet de s'identifier avec un login
 *  @author Lienard Tristan
 *  @date 2017
 */

public class CmdUser implements Commande{
	private Identification l_identification; //L'objet repr�sentant la t�che d'identification
	private String le_login; //Le login demand�
	private String le_mdp; //mot de passe utilisateur
	
	public CmdUser(Identification i){ //Constructeur unique
		l_identification = i;
	}
	
	public void analyse(String[] s)  throws ErreurDeSyntaxeException { //D�cris dans l'interface Commande
		if(s.length < 2 || s.length > 3){
			throw new ErreurDeSyntaxeException("Mauvais nombre de param�tres");
		}
		le_login = s[1];
		if(s.length == 3){
			le_mdp = s[2];
		}
		else{
			le_mdp = null;
		}
	}
	public String execute()  throws ErreurExecutionException { //D�cris dans l'interface Commande
		TableUtilisateurs tab_ut;
		String reponse = "-ERR USER " + le_login + " : erreur";
		tab_ut = l_identification.getServeur().getTableUtilisateurs();
		boolean check = false;
		try {
			if(le_mdp == null){
				if(l_identification.getServeur().chercherCompte(le_login, le_mdp)){
					reponse = "-ERR USER " + le_login + " : déjà enregisitrer, veuillez utilisé votre mot de passe";
				}
				else
					check = true;
			}
			else if(le_mdp != null){ 
				if(l_identification.getServeur().chercherCompte(le_login, le_mdp)){
					check = true;
				}
				else
					reponse = "-ERR USER " + le_login + " : erreur login ou mot de passe";
			}
		}catch(IOException e){
			System.out.println("Erreur lors de la lecture ou l'écriture du fichier des comptes");
			check = false;
		}
		if(le_login.length() < 6 || le_login.length() > 15){
			reponse = "-ERR USER " + le_login + " : taille incorrecte";
		}
		else if(tab_ut.loginExiste(le_login)){
			reponse = "-ERR USER " + le_login + " : déjà utilisé";
		}
		else if(check){
			l_identification.getServeur().enregistreUtilisateur(le_login,l_identification.getSocket());
			reponse = "+OK USER ";
		}
		return reponse;
	}
}
