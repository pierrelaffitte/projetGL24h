package algorithmes;

import java.util.Random;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.*;

public class Weka implements algoInterface {

	public Instances importer(String file) {
		Instances data = null;
		try {
			DataSource source = new DataSource(file);
			data = source.getDataSet();
		}catch (Exception e) {
			System.out.println("Le fichier "+file+" n'existe pas. \n");
		}
		return data;
	}
	
	public static void main(String[] args) throws Exception {
		int fold = 10; // nombre k de groupes pour la cross validation
		int seed = 1; //graine pour la reproductibilité des résultats
		
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

		tree.buildClassifier(train);
		// evaluate on test echantillon
		Evaluation eval = new Evaluation(test);
		eval.evaluateModel(tree, test);
		System.out.println(eval.toSummaryString());
	}

	public Object fit(Instances train) throws Exception {
		// TODO Auto-generated method stub
		J48 tree = new J48();
		tree.buildClassifier(train);
		return null;
	}

	public Object[] split(Instances data) {
		// TODO Auto-generated method stub
		Instances[] res = new Instances[2];
		res[0] = data.trainCV(5, 4);
		res[1] = data.testCV(5, 4);
		return res;		
	}

	public void evaluate(Object tree, Instances test) {
		// TODO Auto-generated method stub
		Evaluation eval = new Evaluation(test);
		eval.evaluateModel(tree, test);
		System.out.println(eval.toSummaryString());
	}

}
