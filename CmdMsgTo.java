import java.util.ArrayList;

/**
 *  @description Commande MSGTO permet d'envoy� un message � un autre utilisateur
 *  @author Lienard Tristan
 *  @date 2017
 */

public class CmdMsgTo implements Commande{
	private Utilisateur l_utilisateur; //L'objet repr�sentant l'utilisateur
	private String[] les_destinataires; //La liste des utilisateurs
	private String le_msg; //Le message � envoy�
	
	public CmdMsgTo(Utilisateur u){
		l_utilisateur = u;
	}
	/**
	 * @see iut.tchatche.Commande#analyse(java.lang.String[])
	 */
	@Override
	public void analyse(String[] s) throws ErreurDeSyntaxeException { //D�cris dans l'interface Commande
	    // a minima : "MSGTO [ destinataire ] Ugh" (5 lex�mes)
	    ArrayList<String> dst = new ArrayList<String>();
	    if (s.length < 5) {
	        throw new ErreurDeSyntaxeException("Mauvais nombre de param�tres.");
	    }
	    if (! s[1].equals("[")) {
	        throw new ErreurDeSyntaxeException("Destinataires mal formul�s.");
	    }
	    int i;
	    for (i = 2 ; i < s.length && ! s[i].equals("]") ; i++) {
	        dst.add(s[i]);
	    }
	    if (i == s.length) {
	        throw new ErreurDeSyntaxeException("Destinataires mal formul�s.");                      
	    }
	    les_destinataires = dst.toArray(new String [dst.size()]);
	    // accumuler le message
	    StringBuilder buf = new StringBuilder("");
	    for (i++ ; i < s.length ; i++) {
	        if (! buf.equals("")) {
	            buf.append(" ");
	        }
	        buf.append(s[i]);
	    }
	    le_msg = buf.toString();
	}
	public String execute()  throws ErreurExecutionException { //D�cris dans l'interface Commande
		Serveur srv = l_utilisateur.getServeur();
		TableUtilisateurs tab_ut = srv.getTableUtilisateurs();
		String reponse;
		if(les_destinataires.length == 1 && les_destinataires[0].toUpperCase().equals("ALL")){
			srv.diffuse(l_utilisateur.getLogin(), Notification.MSGFROM, le_msg);
		}
		else{
			for(int i = 0; i < les_destinataires.length; i++){
				if(tab_ut.getUtilisateur(les_destinataires[i]) == null){
					throw new ErreurExecutionException("utilisateur " + les_destinataires[i] + " inconnu");
				}
			}
			srv.diffuse(l_utilisateur.getLogin(),Notification.MSGFROM, le_msg,les_destinataires);
		}
		reponse = "+OK MSGTO";
		return reponse;
	}
}
