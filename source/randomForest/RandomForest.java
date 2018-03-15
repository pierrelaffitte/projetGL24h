package randomForest;

import java.io.IOException;
import java.util.ArrayList;

import interfaces.Algorithme;
import utilitaire.FichierCSV;

/**
 * Algorithme de forêts aléatoires 
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class RandomForest implements Algorithme {

	//SparkML_RF sp = new SparkML_RF();
	SparkML_RF sp = new SparkML_RF();
	Weka_RF w = new Weka_RF();
	Renjin_RF rj = new Renjin_RF();
	
	@Override
	public void getAccuracy(String nom_CSV, char delimiter, String y, String...otherArgs) throws IOException {
	
		// Split du csv
		FichierCSV f = new FichierCSV(nom_CSV);
		f.splitCSV(delimiter);
				
		// Def du train et test
		String train = "resources/train_"+ nom_CSV + ".csv";
		String test = "resources/test_" + nom_CSV + ".csv";
		
		// Récup les accuracy
		Object accuracySparkML = sp.evaluate(train, test, y, otherArgs);
		Object accuracyWeka = w.evaluate(train, test, y, otherArgs);
		Object accuracyRenjin = rj.evaluate(train, test, y, otherArgs);
		
		// Affichage des accuracy
		System.out.println("Résultats Random Forest -------------");
		System.out.println("Spark ML : " + accuracySparkML + "\n" + 
				"Weka : " + accuracyWeka + "\n" + 
				"Renjin : " + accuracyRenjin);
	}
	
	@Override
	public ArrayList<Object> run(String nom_CSV, char delimiter, String y, String...otherArgs) throws IOException{
		ArrayList<Object> res = new ArrayList<Object>();
		FichierCSV f = new FichierCSV(nom_CSV);
		f.splitCSV(delimiter);
		// Def du train et test
		String train = "resources/train_"+ nom_CSV + ".csv";
		String test = "resources/test_" + nom_CSV + ".csv";
		
		// Récup les accuracy
		res.add(sp.evaluate(train, test, y, otherArgs));
		res.add(w.evaluate(train, test, y, otherArgs));
		res.add(rj.evaluate(train, test, y, otherArgs));
		return res;		
	}
}
