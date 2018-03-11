package randomForest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import scala.Tuple2;

import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import convertor.Convertisseur;
import interfaces.Implementation;

/**
 * Algorithme de forêts aléatoires en SparkML
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class SparkML_RF implements Implementation{

	/*
	public static Main_RF sparkML = new Main_RF();
	public static SparkConf conf = new SparkConf().setAppName("Workshop").setMaster("local[*]");
	public static JavaSparkContext sc = new JavaSparkContext(conf);
	*/
	private Convertisseur main = new Convertisseur(); 


	@Override
	public Object importer(String path) {
		JavaRDD<List<String>> dataSplit = main.readDatas(path);
		List<String> header = main.scrawler.getHeader(dataSplit);		
		return main.scrawler.removeHeader(dataSplit, header);
	}

	@Override
	public Object fit(String train, String y,String... args) {
		if (!main.isLoad()) {
			main.setHeader(args[0]);
		}
		JavaRDD<LabeledPoint> train2 = main.prepareData((JavaRDD<List<String>>)importer(train),y);
		int vary = main.header.getVar(y);
		System.out.println(y+ " : "+ vary);
		int numClasses = main.header.get().get(vary).getMesModas().size();
		System.out.println(numClasses);
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
		String impurity = "gini";
		int maxDepth = 5;
		int maxBins = 32;	

		int numTrees = Integer.valueOf(args[0]);
		String featureSubsetStrategy = "auto";
		Integer seed = 1;

		RandomForestModel model = RandomForest.trainClassifier(train2, numClasses,
				categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins,seed);
		return model;
	}

	@Override
	public Object evaluate(String train, String test, String y,String... args) {
		if (!main.isLoad()) {
			main.setHeader(train.replaceAll("train_", ""));
		}
		System.out.println(main.header);
		JavaRDD<LabeledPoint> test2 = main.prepareData((JavaRDD<List<String>>)importer(test),y);	
		RandomForestModel model = (RandomForestModel) fit(train, y, args[0]);
		JavaPairRDD<Double, Double> predictionAndLabel =
				test2.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
		double testErr =
				predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) test2.count();

		return 1-testErr;
	}

	public static void main(String[] args) {
		String file = "statsFSEVary";
		String y = "sizePDF";
		SparkML_RF m = new SparkML_RF();
		System.out.println(m.evaluate("resources/train_"+file+".csv", "resources/test_"+file+".csv", y, "resources/"+file+".csv"));
		//sc.stop();
	}


}

