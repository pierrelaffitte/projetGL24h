package utilitaire;

import java.io.IOException;
import classificationTree.ClassificationTree;
import interfaces.Algorithme;
import randomForest.RandomForest;

/**
 * Modélisation des fonctionnalités du client
 * @author Laura Dupuis, Flavien Lévêque, Pierre Laffitte, Charlène Noé
 *
 */
public class Client {

	private Algorithme algo;
	
	public Client(Algorithme choixAlgo) {
		this.algo = choixAlgo;
	}
	
	/**
	 * Affiche les différents taux d'erreur selon l'algo choisi
	 * @param nom_CSV nom du fichier CSV à comparer
	 * @param delimiter delimiteur du fichier CSV
	 * @param y nom de la variable à expliquer
	 * @param otherArgs autres arguments relatifs à l'algo choisi
	 */
	public void compareLibrairies(String nom_CSV, char delimiter, String y, String...otherArgs) {
		try {
			algo.getAccuracy(nom_CSV, delimiter, y,otherArgs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Setter de l'algorithme choisi
	 * @param nouveauChoixAlgo nouvel algo
	 */
	public void changeAlgo(Algorithme nouveauChoixAlgo) {
		this.algo = nouveauChoixAlgo;
	}
	
	public static void main(String[] args) {
		// Choix de l'algo
		ClassificationTree ct = new ClassificationTree();
		RandomForest rf = new RandomForest();
		// Création du client 
		Client c = new Client(ct);
		
		// Fichier 1 : iris
		String nom_CSV = "iris";
		char delimiter = ',';
		String y = "Species";
		String[] otherArgs = {"1"};
		System.out.println("Fichier 1 : iris ---------------");
		// Arbre de classification
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
		// Random Forest
		c.changeAlgo(rf);
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
		
		// Fichier 2 : statsFSEVary
		nom_CSV = "statsFSEVary";
		delimiter = ',';
		y = "sizePDF";
		System.out.println("Fichier 2 : statsFSEVary ---------------");
		// Arbre de classification
		c.changeAlgo(ct);
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
		// Random Forest
		c.changeAlgo(rf);
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
		
		// Fichier 3 : winequality
		nom_CSV = "winequality";
		delimiter = ',';
		y = "quality";
		System.out.println("Fichier 3 : winequality ---------------");
		// Arbre de classification
		c.changeAlgo(ct);
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
		// Random Forest
		c.changeAlgo(rf);
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
		
		// Fichier 4 : mushrooms
		nom_CSV = "winequality";
		delimiter = ',';
		y = "class";
		System.out.println("Fichier 4 : mushrooms ---------------");
		// Arbre de classification
		c.changeAlgo(ct);
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
		// Random Forest
		c.changeAlgo(rf);
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
	}
}
