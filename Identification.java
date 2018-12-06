import java.io.IOException;
import java.net.Socket;

/**
 *  @description Un client en phase d'identification. 
 *  			Cette classe correspond � un client ayant ouvert une connexion 
 *  			TCP sur le serveur mais ne s'�tant pas encore identifi�.
 *  @author Lienard Tristan
 *  @date 2017
 */

public class Identification extends Client{
	
	public Identification(Serveur s, Socket so) throws IOException { //Construit une nouvelle t�che d'identification
		super(s, so);
	}

	public boolean traiter() { //Traite une commande soumise � la t�che d'identification
		String ligne;		//les commandes utilisables sont : USER connection avec un login
		String[] tab_cmd;
		CmdUser cmd;
		String reponse = "-";
		try{
			if(l_entree.ready()){
				ligne = l_entree.readLine();
				if(ligne == null){
					System.out.println("D�connexion sauvage de " + getIp());
					return false;
				}
				ligne = ligne.trim();
				tab_cmd = decoupe(ligne);
				if(tab_cmd.length > 0){
					if(tab_cmd[0].toUpperCase().equals(Commande.USER)){
						cmd = new CmdUser(this);
						cmd.analyse(tab_cmd);
						reponse = cmd.execute();
						System.out.println("Reponse : " + reponse);
					}
					else{
						throw new ErreurDeSyntaxeException(tab_cmd[0] + " : commande inconnue");
					}
					envoyer(reponse);
				}
			}
			else if(!isConnected()){ //Detecte la deconnexion sauvage d'un client
				System.out.println("Le client s'est déconnecté " + getIp());
				try{
					getSocket().close();
				}
				catch(IOException e){
					System.out.println("probleme a la fermeture du socket");
				}
				return false;
			}
		} catch(IOException e){
			System.out.println("Erreur d'E/S lors de la lecture ou l'�criture du socket");
			stopper("Connexion ferm�e par le serveur");
			return false;
		}
		catch(ErreurDeSyntaxeException e){
			envoyer("-ERR " + e.getMessage());
			return true;
		}
		catch(ErreurExecutionException e){
			System.out.println("Erreur d'E/S lors de la lecture ou l'�criture du socket");
			stopper("Connexion ferm�e par le serveur");
			return false;
		}
		if(reponse.charAt(0) == '+'){
			envoyer("La commande Help est à votre disposition si vous avez un doute");
			return false;
		}
		return true;
	}
}
