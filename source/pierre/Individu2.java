package pierre;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.io.Serializable;
public class Individu2 implements Serializable{
	private List<Set<String>> vecteur = new ArrayList<Set<String>>();

	public void add(Set<String> i ) {
		vecteur.add(i);
	}

	public List<Set<String>> getVecteur(){
		return vecteur;	
	}
	
	public void setVecteur(List<Set<String>> l) {
		vecteur = l;
	}

	public String toString() {
		return vecteur.toString();
	}
}
