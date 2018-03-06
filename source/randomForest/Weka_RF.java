package randomForest;

import java.util.Enumeration;

import org.netlib.lapack.Ssycon;

import interfaces.algoInterface;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class Weka_RF implements algoInterface {

	public Object importer(String file) {
		Instances data = null;
		try {
			DataSource source = new DataSource(file);
			data = source.getDataSet();
		}catch (Exception e) {
			System.out.println("Le fichier "+file+" n'existe pas. \n");
		}
		return data;
	}

	/**
	 * retourne l'indice de position de la variable Y reçue en paramètre
	 * @param data le chemin du fichier CSV
	 * @param y le nom de la colonne Y 
	 * @return pos
	 */
	public int posY(Instances data, String y) {
		Enumeration<Attribute> liste= data.enumerateAttributes();
		int compteur = 0;
		int pos = data.numAttributes();
		while (liste.hasMoreElements() & compteur < data.numAttributes()) {
			if(liste.nextElement().name().equals(y)) {
				pos = compteur;
				compteur = data.numAttributes();
			}
			compteur++;
		}
		return pos;
	}

	public static boolean isString(Instances data, String y) {
		Weka_RF weka = new Weka_RF();
		Instance row = data.get(0);
		//Instance row = data.instance(0);
		int pos = weka.posY(data, y);
		//System.out.println(row.attribute(pos).isNumeric());
		return row.attribute(pos).isString();
	}

	public Object fit(String train, String y,String...args){
		RandomForest rf = null;
		int nbtrees = Integer.valueOf(args[0]);
		//System.out.println(nbtrees);
		try {
			Instances train1 = ((Instances) importer(train));
			if (!Weka_RF.isString(train1, y) ){
				System.err.println("do conversion");
				train1 = this.convertNumToString(train1, y);
			}
			train1.setClassIndex(posY(train1,y));
			//train1.setClassIndex(train1.numAttributes()-1);
			rf = new RandomForest();
			rf.setNumIterations(100);
			//rf.setNumTrees(4);
			rf.buildClassifier(train1);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return rf;
	}

	public  Instances convertNumToString(Instances data, String y) {
		/* change la variable y de quantitative à qualitative*/
		Instances res = null;
		int pos = this.posY(data, y);
		System.err.println(pos);
		NumericToNominal convert= new NumericToNominal();
		String[] options= new String[2];
		options[0]="-R";
		options[1]= String.valueOf(pos+1);  //range of variables to make numeric
		//System.out.println(options[1]);
		try {
			//this.test(data);
			convert.setOptions(options);
			convert.setInputFormat(data);			
			res = Filter.useFilter(data, convert);

			// on s'assure que la conversion est bien faite
			Instance row = res.get(1);
			//Instance row = res.instance(1);


			//System.out.println(row.attribute(pos).isString());
		}catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public void test(Instances data) throws Exception{
		NumericToNominal convert= new NumericToNominal();
		String[] options= new String[2];
		options[0]="-R";
		options[1]="11";  //range of variables to make numeric

		convert.setOptions(options);
		convert.setInputFormat(data);

		Instances newData=Filter.useFilter(data, convert);

		//System.out.println("Before");
		//System.out.println("Nominal? "+data.attribute(11).isNominal());
		//System.out.println("After");
		//System.out.println("Nominal? "+newData.attribute(11).isNominal());
	}


	public Object evaluate(String train, String test, String y,String...args) {
		//Object model=fit(train,y);
		Object resultat = null;
		RandomForest rf = (RandomForest) fit(train,y, args);
		//rf.setNumIterations(105);
		//rf.setNumTrees(4);
		
		Instances test1 = (Instances) importer(test);
		if (!this.isString(test1, y) ){
			test1 = this.convertNumToString(test1, y);
		}
		test1.setClassIndex(posY(test1,y));
		//test1.setClassIndex(test1.numAttributes()-1); // indique le y => inclure le string y
		Evaluation eval;
		try {
			eval = new Evaluation(test1);
			eval.evaluateModel(rf, test1);
			resultat = eval.correct()*100.0/test1.size();
			//resultat = eval.correct()*100.0/test1.numInstances();
			//System.out.println(eval.correct()*100.0/test1.size());
			//System.out.println(eval.toSummaryString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultat;
	}

	public static void main(String[] args) throws Exception {
		int fold = 10; // nombre k de groupes pour la cross validation
		int seed = 1; //graine pour la reproductibilité des résultats
		Weka_RF weka = new Weka_RF();
		int y = weka.posY((Instances)weka.importer("resources/train_iris.csv"), "Species");
		RandomForest rf = new RandomForest();
		RandomForest.main(new String[] {"-h"});
		RandomForest.main(new String[] {"-I", "1000", // Number of trees to build
                "-t", "resources/train_iris.csv", "-T", "resources/test_iris.csv",
                "-c", Integer.toString(y+1) 
                });
		System.out.println(weka.evaluate("resources/train_iris.csv","resources/test_iris.csv","Species", "50"));
	}

}
