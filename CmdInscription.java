import java.io.IOException;

public class CmdInscription implements Commande {
	private Utilisateur l_utilisateur;
	String le_mdp;
	
	public CmdInscription(Utilisateur u){
		l_utilisateur = u;
	}
	public void analyse(String[] s) throws ErreurDeSyntaxeException {
		if(s.length != 2){
			throw new ErreurDeSyntaxeException("nombre de parametre incorrect");
		}
		else
			le_mdp = s[1];
	}
	public String execute() throws ErreurExecutionException{
		String reponse;
		if(le_mdp.length() < 3 || le_mdp.length() > 10){
			reponse = "-ERR INSCRIPTION " + l_utilisateur.getLogin() + " : taille de mot de passe incorrect";
		}
		else if(!checkMdp(le_mdp)){
			reponse = "-ERR INSCRIPTION " + l_utilisateur.getLogin() + " : caractère \":\" interdit dans le mot de passe";
		}
		else{
			try {
				if(!l_utilisateur.getServeur().chercherCompte(l_utilisateur.getLogin(), null)){
					l_utilisateur.getServeur().ecrireCompte(l_utilisateur.getLogin(), le_mdp);
					reponse = "+OK INSCRIPTION " + l_utilisateur.getLogin();
				}
				else
					reponse = "-ERR INSCRIPTION " + l_utilisateur.getLogin() + " : utilisateur déjà inscris";
				
				
			} catch (IOException e) {
				System.out.println("Erreur lors de la lecture ou l'écriture dans le fichier des comptes");
				reponse = "-ERR INSCRIPTION " + l_utilisateur.getLogin() + " : erreur serveur";
			}
		}
		return reponse;
	}
	private boolean checkMdp(String mdp){
		boolean check = true;
		for(int i = 0; i < mdp.length();i++){
			if(mdp.charAt(i) == ':')
				check = false;
		}
		return check;
	}

}
