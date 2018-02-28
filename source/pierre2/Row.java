package pierre2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.io.Serializable;
public class Row implements Serializable{
	
	private List<Set<String>> vecteur = new ArrayList<Set<String>>();

	public List<Set<String>> get(){
		return vecteur;	
	}
	
	public void add(Set<String> i ) {
		vecteur.add(i);
	}
	
	public String toString() {
		return vecteur.toString();
	}
}
