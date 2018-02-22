package utilitaire;

import java.io.IOException;

import ClassificationTree.Renjin_CT;
import ClassificationTree.SparkML_CT;
import ClassificationTree.Weka_CT;
import interfaces.algoInterface;

public class Compare {

	public static void main(String[] args) {
		Compare c = new Compare();
		try {
			c.afficheRes(EnumAlgo.classificationTree, "iris", ',', "Species");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void afficheRes(EnumAlgo method, String nom_CSV, 
			char delimiter, String y) throws IOException {
		
		// Split du csv
		FichierCSV f = new FichierCSV(nom_CSV);
		f.splitCSV(delimiter);
		
		// Def du train et test
		String train = "resources/train_"+ nom_CSV + ".csv";
		String test = "resources/test_" + nom_CSV + ".csv";
		
		// Méthodes
		algoInterface rj = null;
		algoInterface wk = null;
		algoInterface sp = null;
	
		switch(method) {
			case classificationTree:
				rj = new Renjin_CT();
				wk = new Weka_CT();
				//sp = new SparkML();
				break;
			case randomForest:
				break;
			default:
				break;
		}
		
		// Evaluate 
		rj.evaluate(train, test, y);
		wk.evaluate(train, test, y);
	}

}
