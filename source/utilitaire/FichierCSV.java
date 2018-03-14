package utilitaire;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Gestion des fichiers CSV
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class FichierCSV {
	
	private String nameFile;
	
	public FichierCSV(String name) {
		nameFile = name;
	}
	
	/**
	 * Getter du nom 
	 * @return nom
	 */
	public String getNameFile() {
		return nameFile;
	}

	/**
	 * Création du fichier CSV
	 * @param file nom du fichier CSV à créer
	 * @param headers liste des noms des variables (entête du fichier)
	 * @param records lignes du fichier CSV
	 * @throws IOException exception de lecture/écriture
	 */
	private void writeCSV(String file, List<String> headers, List<CSVRecord> records) 
			throws IOException {
		FileWriter out = new FileWriter(file);
		CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
		
		for (String s : headers) {
	    	printer.print(s);
	    }
		
		printer.println();
		
		for (CSVRecord c : records) {
	    	printer.printRecord(c);
	    }
		printer.close();
	}
	
	/**
	 * Génère l'échantillon test qui est de taille égale à 20% de la taille du jeu de données
	 * @param taille taille du jeu de données
	 * @return liste d'entiers pour l'échantillon test
	 */
	private List<Integer> generateNbAlea(int taille){
		List<Integer> nbAlea = new ArrayList<Integer>();
		
		// Graine
		Random generator = new Random(1);
				
		// Détermine la taille de l'échantillon test
		int nbLigneTest = (int) (0.2 * taille);
				
		// Génération des nombres aléatoires
		while(nbAlea.size() < nbLigneTest) {
			int alea = (int) (generator.nextInt(taille+1));
			if(!nbAlea.contains(alea)) {
				nbAlea.add(alea);
			}
		}
		return nbAlea;
	}
	
	/**
	 * Division du fichier CSV en 2 parties : train et test (80% / 20%)
	 * @param delimiter delimiteur du fichier CSV
	 * @throws IOException exception de lecture
	 * @see #generateNbAlea(int)
	 */
	public void splitCSV(char delimiter) throws IOException  {
		
		List<CSVRecord> fichierTest = new ArrayList<CSVRecord>();
		List<CSVRecord> fichierApp = new ArrayList<CSVRecord>();

		// Lecture du CSV
		Reader in = new FileReader("resources/" + nameFile + ".csv");
		CSVParser in2 = CSVFormat.DEFAULT.withHeader().withDelimiter(delimiter)
				.parse(in);
		List<CSVRecord> records = in2.getRecords();
		int taille = records.size();
		
		//TODO : Suppression de la premiere colonne : PENSER A LE FAIRE
		
		// Génération des nombres aléa
		List<Integer> nbAlea = generateNbAlea(taille);
		
		// Découpage du fichier csv en 2 listes de CSV record
		for(int i=0; i<records.size(); i++) {
			if(!nbAlea.contains(i+1)) {
				fichierApp.add(records.get(i));
			}else {
				fichierTest.add(records.get(i));
			}
		}
		
		// Headers
		List<String> headers = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry : in2.getHeaderMap().entrySet()){
		    headers.add(entry.getKey());
		}
		
		// Ecriture des CSV
		writeCSV("resources/test_" + nameFile +".csv",headers,fichierTest);
		writeCSV("resources/train_" + nameFile +".csv",headers,fichierApp);
	}
	
	/**
	 * Importe un fichier dans le projet à partir du chemin et du nom du fichier
	 * @param path chemin du fichier
	 * @throws IOException si fichier pas trouvé
	 */
	public void importCSV(String path) throws IOException {
		// Lecture du fichier
		File file = new File(path + nameFile + ".csv");
		FileReader fr = new FileReader(file);
		CSVReader cv = new CSVReader(fr,',');
		
		// Ecriture du fichier
		FileWriter out = new FileWriter("resources/" + nameFile + ".csv");
		CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
		
		List<String[]> records = cv.readAll();
		for (String[] tabS : records ) {
			for (String s :  tabS) {
		    	printer.print(s);
			}
			printer.println();
	    }
		printer.close();
	}
	
	public static void main(String[] args) throws Exception {
		// Split des fichiers
		//f.splitCSV("resources/iris.csv", ',', "iris");
		//f.splitCSV("resources/statsFSEVary.csv", ',', "statsFSEVary");
		
		FichierCSV f = new FichierCSV("iris");
		f.importCSV("/home/charlene/Téléchargements/");
	}
}
