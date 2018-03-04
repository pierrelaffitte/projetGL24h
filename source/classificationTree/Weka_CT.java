package classificationTree;

import java.util.Enumeration;

import org.netlib.lapack.Ssycon;

import interfaces.Implementation;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

/**
 * Algorithme d'arbres de classification en Weka
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class Weka_CT implements Implementation {

	@Override
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
	 * Retourne l'indice de position de la variable Y reçue en paramètre
	 * @param data chemin du fichier CSV
	 * @param y nom de la colonne Y 
	 * @return position de Y
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

	/**
	 * Renvoie "true" si la variable à expliquer Y est une variable qualitative
	 * @param data chemin du fichier CSV
	 * @param y nom de la colonne Y
	 * @return booleen
	 */
	public static boolean isString(Instances data, String y) {
		Weka_CT weka = new Weka_CT();
		Instance row = data.get(0);
		int pos = weka.posY(data, y);
		return row.attribute(pos).isString();
	}

	@Override
	public Object fit(String train, String y,String...args){
		J48 tree = null;
		try {
			Instances train1 = ((Instances) importer(train));
			if (!Weka_CT.isString(train1, y) ){
				train1 = this.convertNumToString(train1, y);
			}
			train1.setClassIndex(posY(train1,y));
			tree = new J48();
			tree.buildClassifier(train1);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return tree;
	}

	/**
	 * TODO Pierre : à finir/revoir
	 * @param data chemin du fichier CSV
	 * @param y nom de la variable à expliquer
	 * @return dataframe convertit
	 */
	public  Instances convertNumToString(Instances data, String y) {
		Instances res = null;
		NumericToNominal convert= new NumericToNominal();
		
		int pos = this.posY(data, y);
		String[] options= new String[2];
		options[0]="-R";
		options[1]= String.valueOf(pos+1);  //range of variables to make numeric

		try {
			convert.setOptions(options);
			convert.setInputFormat(data);			
			res = Filter.useFilter(data, convert);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	//TODO cette classe sert à quelque chose ??
	public void test(Instances data) throws Exception{
		NumericToNominal convert= new NumericToNominal();
		String[] options= new String[2];
		options[0]="-R";
		options[1]="11";  //range of variables to make numeric

		convert.setOptions(options);
		convert.setInputFormat(data);

		Instances newData=Filter.useFilter(data, convert);

		System.out.println("Before");
		System.out.println("Nominal? "+data.attribute(11).isNominal());
		System.out.println("After");
		System.out.println("Nominal? "+newData.attribute(11).isNominal());
	}
	
	@Override
	public Object evaluate(String train, String test, String y,String...args) {
		Evaluation eval;
		Object resultat = null;
		J48 tree = (J48) fit(train,y);
		Instances test1 = (Instances) importer(test);
		
		if (!isString(test1, y) ){
			test1 = this.convertNumToString(test1, y);
		}
		test1.setClassIndex(posY(test1,y));

		try {
			eval = new Evaluation(test1);
			eval.evaluateModel(tree, test1);
			resultat = eval.correct()*100.0/test1.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultat;
	}

	public static void main(String[] args) throws Exception {
		int fold = 10; // nombre k de groupes pour la cross validation
		int seed = 1; //graine pour la reproductibilité des résultats

		/*
		String file = "resources/iris.csv";
		Weka weka = new Weka();
		Instances data = weka.importer(file);
		data.setClassIndex(data.numAttributes()-1);

		// le choix du modele : J48 = C4.5
		// http://weka.sourceforge.net/doc.dev/weka/classifiers/trees/package-summary.html
		J48 tree = new J48();

		// on mélange notre jeu de données
		Random rand = new Random(seed);
		Instances randData = new Instances(data);
		randData.randomize(rand);
		if (randData.classAttribute().isNominal())
			randData.stratify(fold);
		double averagecorrect = 0;

		// split train and test
		System.out.println(randData.size());
		Instances train = randData.trainCV(5, 4);
		Instances test = randData.testCV(5, 4);		
		System.out.println(train);
		System.out.println("\n\n\n");
		System.out.println(test);
		System.out.println("\n\n\n");


		// CV
		for (int n=0;n<fold;n++){
			Evaluation eval = new Evaluation(randData);
			Instances app = train.trainCV(fold, n); // construit train sur data sauf nieme fold
			Instances val = train.testCV(fold, n); // construit test sur data avec nieme fold

			tree.buildClassifier(app);
			eval.evaluateModel(tree, val);
			double correct = eval.pctCorrect();
			averagecorrect = averagecorrect + correct;
			System.out.println("the "+n+"th cross validation:"+eval.toSummaryString());
		}
		System.out.println("the average correction rate of "+fold+" cross validation: "+averagecorrect/fold);
		 */

		Weka_CT weka = new Weka_CT();
		//J48 tree = (J48) weka.fit("resources/train_iris.csv","Species");
		// evaluate on test echantillon
		//weka.evaluate(tree, "resources/test_iris.csv","Species");
		/*
		Instances data = (Instances)weka.importer("resources/train_winequality.csv");
		Instance row = data.get(1);
		int pos = weka.posY(data, "quality");
		System.out.println(row.attribute(pos).isString());
		 */

		/*
		 * Instances data = (Instances)weka.importer("resources/train_winequality.csv");
		 */
		/*
		System.out.println(weka.posY(data, "quality"));
		System.out.println(data.get(11));
		Weka.isString(data, "quality");*/
		System.out.println(weka.evaluate("resources/train_winequality.csv","resources/test_winequality.csv","quality"));
	}

}
