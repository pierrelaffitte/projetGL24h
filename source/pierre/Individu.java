package pierre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Individu implements Serializable {

	public List<Intermediaire> vecteur = new ArrayList<Intermediaire>();
	
	public void add(Intermediaire i ) {
		vecteur.add(i);
	}
	
	public List<Intermediaire> getVecteur(){
		return vecteur;	
	}
	
	public String toString() {
		return vecteur.toString();
	}
}
