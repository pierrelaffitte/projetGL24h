
package utilitaire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	public void changeAlgo(Algorithme nouveauChoixAlgo) {
		this.algo = nouveauChoixAlgo;
	}

	public static void main(String[] args) throws IOException {
		
		String action = args[0];
		if (action.equals("importer")) {
			String nom_CSV = args[1];
			String path = args[2];
			Client c = new Client(new ClassificationTree());
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
				Client c = new Client(new RandomForest());
				c.compareLibrairies(nom_CSV, ',', y, otherArgs);
			}
			if (method.equals("DecisionTree")) {
				Client c = new Client(new ClassificationTree());
				c.compareLibrairies(nom_CSV, ',', y, otherArgs);
			}
			
		}
		/*
		Client c = new Client(new ClassificationTree());
		System.out.print("What do you want to do ?:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String action = br.readLine();
		System.out.println(action);
		if (action.equals("importer")) {
			System.out.println("Insert the name file (you don't need to precise .csv): ");
			String nom_CSV = br.readLine();
			System.out.println("Insert the path file : ");
			String path = br.readLine();
			c.importerData(nom_CSV, path);
		}

		if (action.equals("run")) {
			String nom_CSV;
			String y;
			String nbTrees = "10";
			String tmp  = "10";
			System.out.println("Insert the name file (you don't need to precise .csv) : ");
			nom_CSV = br.readLine();
			System.out.println("Insert the target variable : ");
			y = br.readLine();
			System.out.println("Select the method (1 : Decision Tree, 2 : RandomForest)  : ");
			String method = br.readLine();
			if (method.equals("2")) {
				System.out.println("Do you want to select the number of trees (Y/N) : ");
				String opt = br.readLine();
				if (opt.equals("Y")) {
					System.out.println("Select the number of trees (Y/N) : ");
					tmp = br.readLine();
				}
				c.changeAlgo(new RandomForest());
			}
			String[] otherArgs = {tmp};

			c.compareLibrairies(nom_CSV, ',', y, otherArgs);
		}	
*/


/*
		// Choix de l'algo
		ClassificationTree ct = new ClassificationTree();
		RandomForest rf = new RandomForest();
		// Création du client 
		Client c = new Client(ct);

		// Importation du fichier
		//c.importerData("iris", "/home/charlene/Téléchargements/");

		// Fichier 1 : iris
		//String nom_CSV = "iris";
		// Paramètres de comparaisons
		char delimiter = ',';
		//String y = "Species";
		String tmp = "10";
		if (args.length == 4) {
			tmp = args[3];
		}
		String[] otherArgs = {tmp};
		// Arbre de classification
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
		// Random Forest
		c.changeAlgo(rf);
		c.compareLibrairies(nom_CSV, delimiter, y, otherArgs);
*/
		/*
		// Fichier 2 : statsFSEVary
		nom_CSV = "statsFSEVary";
		y = "sizePDF";

		// Fichier 3 : winequality
		nom_CSV = "winequality";
		y = "quality";
		// Fichier 4 : mushrooms
		nom_CSV = "mushrooms";
		y = "class";
		 */
	}
}