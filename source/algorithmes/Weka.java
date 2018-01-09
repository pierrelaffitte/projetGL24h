package algorithmes;

import java.util.Random;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;

public class Weka {

	public static Instances importer(String file) throws Exception {
		DataSource source = new DataSource("resources/iris.csv");
		Instances data = source.getDataSet();
		if (!file.equals("resources/iris.csv")) {
			try {
				source = new DataSource(file);
				data = source.getDataSet();
			}catch (Exception e) {
				System.out.println("Le fichier "+file+" n'existe pas. \n"+
			"le fichier iris.csv a été chargé à la place.");
			}
		}
		return data;
	}
	
	public static void main(String[] args) throws Exception {
		int fold = 10; // nombre k de groupes pour la cross validation
		int seed = 1; //graine pour la reproductibilité des résultats
		
		String file = "resources/iris.csv";
		Instances data = Weka.importer(file);
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

}
