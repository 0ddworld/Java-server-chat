import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * Gère une table des utilisateurs.
 * 
 * @author wurbel
 *
 */
class TableUtilisateurs {
	/**
	 * La table.
	 * @y.exclude
	 */
	private HashMap<String,Utilisateur> la_table; 

	/**
	 * Crée une nouvelle table d'utilisateurs.
	 */
	TableUtilisateurs() {
		la_table = new HashMap<String,Utilisateur>();
	}
	
	/**
	 * Ajoute un nouvel utilisateur dans la table.
	 * 
	 * @param utilisateur
	 */
	public void ajoute(Utilisateur utilisateur) {
		la_table.put(utilisateur.getLogin(), utilisateur);
	}
	
	/**
	 * Supprime l'utilisateur correspondant au login fourni.
	 * 
	 * @param login
	 *            le login
	 * @return l'objet Utilisateur supprimé si l'utilisateur a été trouvé dans
	 *         la table et supprimé, {@code null} si l'utilisateur n'a pas été
	 *         supprimé.
	 */
	public Utilisateur supprime(String login) {
		return la_table.remove(login);
	}
	
	/**
	 * Renvoie la liste des utilisateurs sauf celui correspondant au login
	 * spécifié.
	 * 
	 * @param login
	 *            le login a ne pas inclure dans la liste.
	 * @return la liste des objets {@linkplain iut.tchatche.Utilisateur} à
	 *         l'exclusion de celui passé en paramètre.
	 */
	public Utilisateur[] getLoginsSauf(String login) {
		ArrayList<Utilisateur> resultat = new ArrayList<Utilisateur>();
		
		for (Utilisateur ut : la_table.values()) {
			if (! ut.getLogin().equals(login)) {
				resultat.add(ut);
			}
		}
		return resultat.toArray(new Utilisateur [resultat.size()]);
	}

	/**
	 * Renvoie l'utilisateur correspondant au login.
	 * 
	 * @param login
	 *            le login.
	 * @return l'objet {@link iut.tchatche.Utilisateur} correspondant au login,
	 *         ou {@code null} si cet utilisateur n'existe pas.
	 */
	public Utilisateur  getUtilisateur(String login) {
		return la_table.get(login);
	}
	
	

	/**
	 * Vérifie si le login spécifié existe dans la table des utilisateurs.
	 * 
	 * @param l
	 *            le login à vérifier.
	 * @return {@code true} si le login existe, {@code false} sinon.
	 */
	public boolean loginExiste(String l) {
		return la_table.containsKey(l);
	}


	/**
	 * Renvoie le nombre d'utilisateurs connectés.
	 * 
	 * @return le nombre d'utilisateurs connectés.
	 */
	public int nbUtilisateurs() {
		return la_table.size();
	}

	/**
	 * Renvoie la liste de tous les logins.
	 * 
	 * @return la liste de tous les logins.
	 */
	public String[] getAllLogins() {
		Set<String> ensemble = la_table.keySet();
		return ensemble.toArray(new String [ensemble.size()]);
	}
	
	public Utilisateur [] getAllUtilisateurs() {
		Collection<Utilisateur> ensemble = la_table.values();
		return ensemble.toArray(new Utilisateur [ensemble.size()]);
	}
}