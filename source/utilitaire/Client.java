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
		// Paramètres de comparaisons
		String nom_CSV = "iris";
		char delimiter = ',';
		String y = "Species";
		String[] otherArgs = {"1"};
		// Comparaison
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
		// Nouvel algo : RF
		c.changeAlgo(rf);
		// Nouvelle comparaison
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
	}
}