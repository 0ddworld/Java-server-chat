import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @description Classe principale du serveur de tchatche
 *  @author Lienard Tristan
 *  @date 2017
 */
/*
 * Bug a corriger :
 * 
 * 1- Du au passage Windows-Linux, certains message d'erreur contiennent des point d'interrogations. EXTREMEMENT PEU IMPORTANT
 * 
 * 
 * Bug traité :
 * 
 * 2- Lors d'une commande QUIT, la commande s'effectue parfaitement mais une erreur d'E/S se produit. PEU IMPORTANT
 * 
 * 3- Lors de l'envoie d'un message à un utilisateurs inexistant, une deconnection se produit. IMPORTANT
 * 
 * 4- Sans explication, le serveur ne répond plus a l'utilisateur après un commande. EXTREMEMENT IMPORTANT
 * 
 * 5- De temps en temps, le serveur ne reconnais pas une commande sans raison particuliere. TRES IMPORTANT
 * 
 * 6- Rarement, l'utilisateurs reviens à l'état d'identification sans raison particuliere. TRES IMPORTANT
 * 		Remarque : après la commande QUIT de l'utilisateur lambda, ce bug peut se produire.
 * 
 * 7- Detection d'une deconnexion sauvage. IMPORTANT
 */

public class Serveur{
private ServerSocket le_socket_srv; //le socket serveur
private Queue<Client> la_file; //File de traitement des connexions en cours
private TableUtilisateurs les_utilisateurs; //La table des utilisateurs connect�s

public Serveur() throws IOException { //Constructeur par d�fault
	le_socket_srv = new ServerSocket(3333);
	le_socket_srv.setSoTimeout(50);
	la_file = new ArrayDeque<Client>(50);
	les_utilisateurs = new TableUtilisateurs();
}

public void lancer() throws IOException{ //T�che principale du serveur
	Socket client;
	while(true){
		client = null;
		try{
			client = le_socket_srv.accept();
		}
		catch(SocketTimeoutException e){
			
		}
		if(client!=null){
			System.out.println("Connection entrante");
			creerIdentification(client);
		}
		traiterClientSuivant();
	}
}
public TableUtilisateurs getTableUtilisateurs(){ //Renvoie la table utilisateur
	return les_utilisateurs;
}
public void diffuse(String login, Notification notif, String msg){
//Diffuse une notification aux utilisateurs connect�s
	Utilisateur[] u = les_utilisateurs.getLoginsSauf(login);
	if(notif.equals(Notification.MSGFROM)){
		for(int i = 0; i < u.length; i++){
			u[i].envoyer(notif + " [" + login + "] " + msg);
		}
	}
	else{
		for(int i = 0; i< u.length; i++){
			u[i].envoyer(notif +" "+ msg);
		}
	}
}
public void diffuse(String login, Notification notif, String msg, String[] destinataires){
//Diffuse une notification aux utilisateurs sp�cifi�s
	Utilisateur[] u = new Utilisateur[destinataires.length];
	for(int i = 0; i < u.length; i++){
			u[i] = les_utilisateurs.getUtilisateur(destinataires[i]);
	}
	if(notif.equals(Notification.MSGFROM)){
		for(int i = 0; i < u.length; i++){
			u[i].envoyer(notif + " [" + login + "] " + msg);
		}
	}
	else{
		for(int i = 0; i< u.length; i++){
			u[i].envoyer(notif + msg);
		}
	}
}
public void enregistreUtilisateur(String login, Socket so) { //Cr�er et ajoute l'utilisateur dans les listes du serveur
	Utilisateur u = null;
	try {
		u = new Utilisateur(login, this, so);
	}catch (IOException e) {
		System.out.println("Erreur lors de la cr�ation de Utilisateur");
	}
	les_utilisateurs.ajoute(u);
	try{
		la_file.offer(u);
	}catch (RuntimeException e){
		System.out.println("Erreur lors de l'ajout de \"Utilisateur\" dans la file");
	}
	diffuse(login, Notification.CONNECT, login);
}
public void desenregistrerUtilisateur(String login){ //Retire l'utilisateur des listes du serveur
	Utilisateur u =les_utilisateurs.supprime(login);
	if(u != null){
		Client c = la_file.poll();
		if(c != null){
			diffuse(login, Notification.DISCONNECT, login);
			la_file.offer(c);
		}
	}
}

public void creerIdentification(Socket client){ //Cr�er un nouvel objet d'identification
	Client c = null;
	try{
		c = new Identification(this, client);
	}
	catch(IOException e){
		System.out.println("Erreur lors de la cr�ation de identification");
	}
	c.envoyer("Bienvenue sur le tchatcheur de tristan lienard !\nUtilisez la commande \"user <<Votre_pseudo>>\" pour vous connecter !");
	try{
		la_file.offer(c);
	}catch (RuntimeException e){
		System.out.println("Erreur lors de l'ajout de \"Identification\" dans la file");
	}
}
public boolean chercherCompte(String nom,String mdp) throws IOException{
	BufferedReader br;
	String line;
	String[] mots;
	boolean check = false;
	boolean retour = false;
	try {
		br = new BufferedReader(new InputStreamReader(new FileInputStream("compte.txt")));
	}catch (FileNotFoundException e) {
			FileOutputStream fos;
			fos = new FileOutputStream("compte.txt");
			fos.close();
			br = new BufferedReader(new InputStreamReader(new FileInputStream("compte.txt")));
	}
		while((line = br.readLine()) != null && check != true){
			mots = line.split(":");
			if(mots[0].equals(nom)){
				check = true;
				if(mdp != null){
					if(mots[1].equals(mdp)){
						retour = true;
					}else
						retour = false;
				}else
					retour = true;
			}
		}
	br.close();
	return retour;
}
public void ecrireCompte(String nom, String mdp) throws IOException{
	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("compte.txt", true)));
	bw.write(nom + ":" + mdp);
	bw.newLine();
	bw.close();
}

private void traiterClientSuivant() throws NullPointerException{ //Traiter le client suivant dans la file
	Client c;
	if(!la_file.isEmpty()){
		c = la_file.poll();
		if(c.traiter()){
			la_file.offer(c);
		}
	}
}





public static void main(String [] args) throws IOException{ //Programme principal
		Serveur srv = new Serveur();
		srv.lancer();
}}