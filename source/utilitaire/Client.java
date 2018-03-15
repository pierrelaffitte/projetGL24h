
package utilitaire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
	
	public ArrayList<Object> run(String nom_CSV, char delimiter, String y, String...otherArgs) {
		try {
			return algo.run(nom_CSV, delimiter, y,otherArgs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Importe un fichier .csv dans le projet
	 * @param name nom du fichier
	 * @param path emplacement du fichier
	 */
	public void importerData(String name, String path) {
		FichierCSV f = new FichierCSV(name);
		try {
			f.importCSV(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Setter de l'algorithme choisi
	 * @param nouveauChoixAlgo nouvel algo
	 */
	public void setAlgo(Algorithme nouveauChoixAlgo) {
		this.algo = nouveauChoixAlgo;
	}

	public static void main(String[] args) throws IOException {
		Client c = new Client();
		String action = args[0];
		if (action.equals("importer")) {
			String nom_CSV = args[1];
			String path = args[2];
			c.importerData(nom_CSV, path);
		}
		if (action.equals("run")) {
			String nom_CSV = args[1];
			String y = args[2];
			String method = args[3];
			String opt  = "10";
			if (args.length == 5) {
				opt = args[4];
			}
			String[] otherArgs = {opt};
			if (method.equals("RandomForest")) {
				c.setAlgo(new RandomForest());
				c.compareLibrairies(nom_CSV, ',', y, otherArgs);
			}
			if (method.equals("DecisionTree")) {
				c.setAlgo(new ClassificationTree());
				c.compareLibrairies(nom_CSV, ',', y, otherArgs);
			}	
		}
	}
}