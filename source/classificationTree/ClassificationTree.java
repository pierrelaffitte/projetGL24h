package classificationTree;

import java.io.IOException;

import interfaces.Algorithme;
import utilitaire.FichierCSV;

/**
 * Algorithme d'arbres de classification
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class ClassificationTree implements Algorithme {

	private SparkML_CT sp = new SparkML_CT();
	private Weka_CT w = new Weka_CT();
	private Renjin_CT rj = new Renjin_CT();
	
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
		System.out.println("Résultats Classification Tree -------------");
		System.out.println("Spark ML : " + accuracySparkML + "\n" + "Weka : " + accuracyWeka + "\n" + "Renjin : " + accuracyRenjin);
	}
	
	
}
