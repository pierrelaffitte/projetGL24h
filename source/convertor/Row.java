package convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.io.Serializable;

/**
 * Classe intermédiare lors de la construction du Header
 * Elle contient les informations d'une seule ligne du jeu de données
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class Row implements Serializable{
	
	private List<Set<String>> vecteur = new ArrayList<Set<String>>();

	public List<Set<String>> get(){
		return vecteur;	
	}
	
	/**
	 * ajoute la valeur prise par la modalité 
	 * @param moda 
	 */
	public void add(Set<String> moda ) {
		vecteur.add(moda);
	}
	
	public String toString() {
		return vecteur.toString();
	}
}
