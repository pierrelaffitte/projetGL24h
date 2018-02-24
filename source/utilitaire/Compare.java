package utilitaire;

import java.io.IOException;

import classificationTree.Renjin_CT;
import classificationTree.SparkML_CT;
import classificationTree.Weka_CT;
import interfaces.algoInterface;
import java_cup.sym;

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
				sp = new SparkML_CT();
				break;
			case randomForest:
				break;
			default:
				break;
		}
		
		// Evaluate 
		Object rj_eval = rj.evaluate(train, test, y);
		Object wk_eval = wk.evaluate(train, test, y);
		Object sp_eval = sp.evaluate(train, test, y);
		
		// Affichage
		System.out.println("Renjin : " + rj_eval);
		System.out.println("Weka : " + wk_eval);
		System.out.println("Spark ML : " + sp_eval);
	}

}
