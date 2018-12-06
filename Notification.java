
/**
 * Types d'�v�nements pouvant faire l'objet de diffusion de messages du serveur
 * vers les clients.
 * 
 * @author wurbel
 *
 */
public enum Notification {
	
	/**
	 * un utilisateur s'est connect�.
	 */
	CONNECT,
	
	/**
	 * Un message est envoy� par un utilisateur.
	 */
	MSGFROM,
	
	/**
	 * Un utilisateur s'est d�connect�.
	 */
	DISCONNECT,
	
	/**
	 * un utilisateur s'est renomé
	 */
	RENAME,
}