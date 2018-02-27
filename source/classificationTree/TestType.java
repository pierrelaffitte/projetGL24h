package classificationTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TestType {

	public HashMap<String, Double> convModalites(Set<String> modalites) {
		ArrayList<HashMap<String, Double>> mesModaRecodes = new ArrayList<HashMap<String, Double>>();
		for (String mesModas : modalites) {
			HashMap<String, Double> tmp = new HashMap<String, Double>();
			
			Iterator<String> it = mesModas.iterator();
			double compteur = 0;
			while(it.hasNext()) {
				tmp.put(it.next(), compteur);
				compteur++;
			}
			mesModaRecodes.add(tmp);
		}
		return mesModaRecodes;
	}
	
	public static void main(String[] args) {
		HashSet<String> modalites = new HashSet<String>();
		HashMap<String, Double> moda = new HashMap<String, Double>();
		
	}
}
