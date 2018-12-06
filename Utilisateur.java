import java.io.IOException;
import java.net.Socket;

/**
 *  @description Un utilisateur, c'est � dire un client identifi�.
 *  			Cette classe correspond � un client qui s'est connect� (TCP) 
 *  			et qui s'est identifi�
 *  @author Lienard Tristan
 *  @date 2017
 */

public class Utilisateur extends Client{
	private String le_login; //Le login de l'utilisateur
	
	public Utilisateur(String l, Serveur s, Socket so) throws IOException { //Cr�e un nouvel utilisateur
		super(s, so);
		le_login = l;
	}
	public String getLogin(){ //Renvoie le login de l'utilisateur
		return le_login;
	}

	public boolean traiter(){ //Traite une commande de l'utilisateur s'il y en a une de disponible sur le flux d'entr�e. 
		String ligne;	      //les commandes utilisables sont : MSGTO envoi d'un message.
		String[] tab_cmd;	  								// : LIST envoie la liste des utilisateurs connect�
		Commande cmd;										// : QUIT Deconnecte l'utilisateur proprement
		String reponse;
		Boolean fin_client = true;
		try {
			if(l_entree.ready()){
				ligne = l_entree.readLine();
				if(ligne == null){
					System.out.println("D�connexion sauvage de " + getSocket());
					getServeur().desenregistrerUtilisateur(le_login);
					return false;
				}
				ligne = ligne.trim();
				tab_cmd = decoupe(ligne);
				if(tab_cmd.length > 0){
					if(tab_cmd[0].toUpperCase().equals(Commande.MSGTO)){
						cmd = new CmdMsgTo(this);
					}
					else if(tab_cmd[0].toUpperCase().equals(Commande.LIST)){
						cmd = new CmdList(this);
					}
					else if(tab_cmd[0].toUpperCase().equals(Commande.QUIT)){
						cmd = new CmdQuit(this);
						fin_client = false;
					}
					else if(tab_cmd[0].toUpperCase().equals(Commande.DISCONNECT)){
						cmd = new CmdDisconnect(this);
						fin_client = false;
					}
					else if(tab_cmd[0].toUpperCase().equals(Commande.RENAME)){
						cmd = new CmdRename(this);
					}
					else if(tab_cmd[0].toUpperCase().equals(Commande.HELP)){
						cmd = new CmdHelp();
					}
					else if(tab_cmd[0].toUpperCase().equals(Commande.INSCRIPTION)){
						cmd = new CmdInscription(this);
					}
					else{
						throw new ErreurDeSyntaxeException(tab_cmd[0] + " : commande inconnue");
					}
					cmd.analyse(tab_cmd);
					reponse = cmd.execute();
					if(!tab_cmd[0].toUpperCase().equals(Commande.QUIT)){
						envoyer(reponse);
					}
				} 
			}
			else if(!isConnected()){
				System.out.println("Le client " + le_login + " s'est déconnecté " + getIp());
				getServeur().desenregistrerUtilisateur(le_login);
				try{
					getSocket().close();
				}
				catch(IOException e){
					System.out.println("probleme a la fermeture du socket");
				}
				return false;
			}
		}catch(IOException e){
				System.out.println("Erreur d'E/S lors de la lecture ou l'�criture du socket");
				stopper("Connexion ferm�e par le serveur");
				getServeur().desenregistrerUtilisateur(le_login);
				return false;
			}
			catch(ErreurDeSyntaxeException e){
				envoyer("-ERR " + e.getMessage());
				return true;
			}
			catch(ErreurExecutionException e){
				envoyer("-ERR " + e.getMessage());
				return true;
			}
		return fin_client;
	}
	/**
	 * Teste l'�galit� de cet Utilisateur avec un objet pass� en param�tre.
	 * 
	 * @param anObject
	 *            l'objet avec lequel tester l'�galit�.
	 * @return {@code true} si l'objet est un Utilisateur avec le m�me login,
	 *         {@code false} dans le cas contraire.
	 * @see java.lang.String#equals(java.lang.Object)
	 */
	public boolean equals(Object anObject) {
	    if (anObject instanceof Utilisateur) {
	        return le_login.equals(((Utilisateur)anObject).le_login);
	    }
	    return false;
	}
	public String setLogin(String l){
		String reponse = le_login;
		le_login = l;
		return reponse;
	}
}
