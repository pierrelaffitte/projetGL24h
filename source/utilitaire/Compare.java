package utilitaire;

import java.io.IOException;

import algorithmes.Renjin_classificationTree;
import algorithmes.SparkML;
import algorithmes.Weka;

public class Compare {

	public static void main(String[] args) {
		Compare c = new Compare();
		try {
			c.afficheRes("", "iris", ',', "Species");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void afficheRes(String method, String nom_CSV, 
			char delimiter, String y) throws IOException {
		
		// Split du csv
		FichierCSV f = new FichierCSV(nom_CSV);
		f.splitCSV(delimiter);
		
		// Def du train et test
		String train = "resources/train_"+ nom_CSV + ".csv";
		String test = "resources/test_" + nom_CSV + ".csv";
		
		// les 3 méthodes
		Renjin_classificationTree rj = new Renjin_classificationTree();
		Weka wk = new Weka();
		SparkML sp = new SparkML();
		
		// evaluate 
		rj.evaluate(train, test, y);
		wk.evaluate(train, test, y);
	}

}
