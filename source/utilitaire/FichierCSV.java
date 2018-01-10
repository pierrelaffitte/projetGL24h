package utilitaire;

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


public class FichierCSV {
	
	/*public void importCSV(String file,char delimiter) throws IOException  {
		Reader in = null;
		in = new FileReader(file);
		CSVParser in2 = CSVFormat.DEFAULT.withHeader().withDelimiter(delimiter)
				.parse(in);
		List<CSVRecord> records = in2.getRecords();
		for(CSVRecord record : records) {
			System.out.println(record);
		}
	}*/
	
	public void splitCSV(String file,char delimiter) throws IOException  {
		
		List<CSVRecord> fichierTest = new ArrayList<CSVRecord>();
		List<CSVRecord> fichierApp = new ArrayList<CSVRecord>();

		// Lecture du CSV
		Reader in = new FileReader(file);
		CSVParser in2 = CSVFormat.DEFAULT.withHeader().withDelimiter(delimiter)
				.parse(in);
		List<CSVRecord> records = in2.getRecords();
		int taille = records.size();
		
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
		writeCSV("resources/test_iris.csv",headers,fichierTest);
		writeCSV("resources/train_iris.csv",headers,fichierApp);
	}
	
	public List<Integer> generateNbAlea(int taille){
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
	
	public void writeCSV(String file, List<String> headers, List<CSVRecord> records) 
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
	
	public static void main(String[] args) throws Exception {
		FichierCSV f = new FichierCSV();
		// Split du fichier "iris"
		f.splitCSV("resources/iris.csv", ',');
	}
}
