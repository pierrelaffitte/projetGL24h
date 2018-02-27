package classificationTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TestType {

	public static HashMap<String, Double> convModalites(Set<String> modalites) {
		HashMap<String, Double> mesModaRecodes = new HashMap<String, Double>();
		ArrayList<String> mesModasOrdonnees = new ArrayList<String>();
		mesModasOrdonnees.addAll(modalites);
		Collections.sort(mesModasOrdonnees);
		System.out.println(mesModasOrdonnees);
		Double compteur = 0.0;
		for (String moda : mesModasOrdonnees) {
			mesModaRecodes.put(moda, compteur);
			compteur++;
		}
		return mesModaRecodes;
	}
	
	public static void main(String[] args) {
		HashSet<String> modalites = new HashSet<String>();
		modalites.add("flavien");
		modalites.add("charlene");
		modalites.add("pierre");
		modalites.add("laura");
		HashMap<String, Double> moda = new HashMap<String, Double>();
		System.out.println(TestType.convModalites(modalites));
		
	}
}
