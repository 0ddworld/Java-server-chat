import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.time.LocalTime;

/**
 *  @description Un client repr�sente un processus associ� � un socket connect� au serveur.
 *  @author Lienard Tristan
 *  @date 2017
 */

public abstract class Client {
	private Serveur le_serveur; //Le serveur
	private Socket le_socket; //Le socket correspondant � la connexion du client
	protected BufferedReader l_entree; // Flux d'entr�e du socket
	protected BufferedWriter la_sortie; // Flux de sortie du socket
	private LocalTime le_last_ping; //Le temps
	
	public Client(Serveur s, Socket so) throws IOException{ //Constructeur unique
		le_serveur = s;
		le_socket = so;
		l_entree = new BufferedReader(new InputStreamReader(le_socket.getInputStream(), "UTF-8"));
		la_sortie = new BufferedWriter(new OutputStreamWriter(le_socket.getOutputStream(),"UTF-8"));
		le_last_ping = LocalTime.now();
	}
	
	public abstract boolean traiter(); //Traite un message envoy� par le client
	
	public Socket getSocket(){ //Renvoie le socket de ce client
		return le_socket;
	}
	public Serveur getServeur(){ //Renvoie le serveur quel ce client est connect�
		return le_serveur;
	}
	public void envoyer(String msg){ //envoyer un message sur la sortie
		try{
			la_sortie.write(msg);
			la_sortie.newLine();
			la_sortie.flush();
			System.out.println("Envoie d'un message à : " +getIp());
		}
		catch(IOException e){
			System.out.println("Erreur d'E/S lors de l'envoi d'un message");
		}
	}
	public String getIp(){ //Renvoie l'adresse IP de ce client
		return le_socket.getInetAddress().getHostAddress();
	}
	public boolean isConnected(){ //Verifie si le client est toujours present grâce à un ping
		if(LocalTime.now().minusSeconds(30).isAfter(le_last_ping)){
			try{
				le_socket.getOutputStream().write(0);
				le_last_ping = LocalTime.now();
			}catch(IOException e){
				return false;
			}
		}
		return true;
	}
	public void stopper(String msg){ //Stop la connexion � ce client
		if(msg != null){
			envoyer(msg);
			try{
				le_socket.close();
			}
			catch(IOException e){
				System.out.println("probleme a la fermeture du socket");
			}
		}
	}
	public String[] decoupe(String s) { //Decoupe la cha�ne pass�e en param�tre
	    Scanner sc = new Scanner(s);
	    ArrayList<String> tab = new ArrayList<String>();
	    String tab_ret [];
	    Pattern p = Pattern.compile(
					"\\[" +
					"|\\]" +
					"|\"[^\"]*\"" +
					"|[\\p{IsLatin}\\p{Graph}&&[^\\]\\[]]+");
	    String token;
	    
	    while ((token = sc.findInLine(p)) != null) {
		tab.add(token);
	    }
	    tab_ret = new String [tab.size()];
	    sc.close();
	    return tab.toArray(tab_ret);
	}

}
