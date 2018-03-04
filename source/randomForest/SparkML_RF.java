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

import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import interfaces.Implementation;

/**
 * Algorithme de forêts aléatoires en SparkML
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class SparkML_RF implements Implementation{

	public static SparkML_RF sparkML = new SparkML_RF();
	public static SparkConf conf = new SparkConf().setAppName("Workshop").setMaster("local[*]");
	public static JavaSparkContext sc = new JavaSparkContext(conf);
	
	@Override
	public Object importer(String path) {
		JavaRDD<String> linesData = sc.textFile(path);
		JavaRDD<LabeledPoint> data = sc.parallelize((ArrayList)sparkML.convert(linesData));
		return data;
	}
	
	@Override
	public Object fit(String train, String y,String... args) {
		SparkML_RF sp = new SparkML_RF();
		JavaRDD<LabeledPoint> train2 = (JavaRDD<LabeledPoint>) sp.importer(train);
		
		int numClasses = 3;
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
		String impurity = "gini";
		int maxDepth = 5;
		int maxBins = 32;
		int numTrees = 1;
		String featureSubsetStrategy = "auto";
		Integer seed = 1;

		RandomForestModel model = RandomForest.trainClassifier(train2, numClasses,
				categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins,seed);
		return model;
	}

	@Override
	public Object evaluate(String train, String test, String y,String... args) {
		SparkML_RF sp = new SparkML_RF();
		JavaRDD<LabeledPoint> test2 = (JavaRDD<LabeledPoint>) sp.importer(test);
		
		RandomForestModel model = (RandomForestModel) fit(train, y);
		JavaPairRDD<Double, Double> predictionAndLabel =
				test2.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
		double testErr =
				predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) test2.count();

		return 1-testErr;
	}

	/**
	 * TODO a completer
	 * @param lines TODO
	 * @return TODO
	 */
	public List<LabeledPoint> convert(JavaRDD<String> lines){
		ArrayList<LabeledPoint> temp = new ArrayList<LabeledPoint>();
		for (int i=1; i < lines.count(); i++) {
			String[] parts = lines.collect().get(i).split(",");
			Double varY = null;
			switch (parts[5]) {
			case "setosa" : 
				varY = 0.0;
				break;
			case "versicolor" :
				varY = 1.0;
				break;
			case "virginica" :
				varY = 2.0;
				break;
			}
			//System.out.println(varY);
			temp.add(new LabeledPoint(varY, Vectors.dense(Double.parseDouble(parts[1]),Double.parseDouble(parts[2]),Double.parseDouble(parts[3]),Double.parseDouble(parts[4]))));
		}
		return temp;
	}

	public static void main(String[] args) {
		/*
		SparkML sparkML = new SparkML();
		SparkConf conf = new SparkConf().setAppName("Workshop").setMaster("local[*]");
		JavaSparkContext sc = new JavaSparkContext(conf);

		//Path path = Paths.get(TentativePierreSparkML.class.getResource("resources/train_iris.csv").getPath());
		JavaRDD<String> linesTrain = sc.textFile("resources/train_iris.csv");
		JavaRDD<String> linesTest = sc.textFile("resources/test_iris.csv");
		*/
		SparkML_RF sp = new SparkML_RF();
		System.out.println(sp.evaluate("resources/train_iris.csv","resources/test_iris.csv", "Species"));
		/*
		System.out.println("Lines count: " + lines.count());
		//colnames
		System.out.println(lines.first());
		System.out.println(lines.collect().get(1));
		System.out.println(lines.collect().get(2));
		 */
		//recodage
		// exemple de labeledpoint
		/*
		JavaRDD<LabeledPoint> train = sc.parallelize((ArrayList)sparkML.convert(linesTrain));
		System.out.println(train.collect().get(1));
		JavaRDD<LabeledPoint> test = sc.parallelize((ArrayList)sparkML.convert(linesTest));

		int numClasses = 3;
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
		String impurity = "gini";
		int maxDepth = 5;
		int maxBins = 32;	

		DecisionTreeModel model = DecisionTree.trainClassifier(train, numClasses,
				categoricalFeaturesInfo, impurity, maxDepth, maxBins);
		JavaPairRDD<Double, Double> predictionAndLabel =
				test.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
		double testErr =
				predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) test.count();

		System.out.println("Test Error: " + (1-testErr));
		System.out.println("Learned classification tree model:\n" + model.toDebugString());
		
		*/
		//ArrayList<String> var = (ArrayList<String>) lines.collect();
		/*
		SparkSession spark = SparkSession.builder().appName("JavaModelSelectionViaCrossValidation").getOrCreate();
		Dataset<Row> training = spark.read().format("libsvm").load("resources/train.csv");
		Dataset<Row> test= spark.read().format("libsvm").load("resources/test.csv");
		 */
		/*
		LinearRegression lr = new LinearRegression();

		// We use a ParamGridBuilder to construct a grid of parameters to search over.
		// TrainValidationSplit will try all combinations of values and determine best model using
		// the evaluator.
		ParamMap[] paramGrid = new ParamGridBuilder()
				.addGrid(lr.regParam(), new double[] {0.1, 0.01})
				.addGrid(lr.fitIntercept())
				.addGrid(lr.elasticNetParam(), new double[] {0.0, 0.5, 1.0})
				.build();

		// In this case the estimator is simply the linear regression.
		// A TrainValidationSplit requires an Estimator, a set of Estimator ParamMaps, and an Evaluator.
		TrainValidationSplit trainValidationSplit = new TrainValidationSplit()
				.setEstimator(lr)
				.setEvaluator(new RegressionEvaluator())
				.setEstimatorParamMaps(paramGrid)
				.setTrainRatio(0.8); // 80% for training and the remaining 20% for validation
		//.setParallelism(2);  // Evaluate up to 2 parameter settings in parallel

		// Run train validation split, and choose the best set of parameters.
		TrainValidationSplitModel model = trainValidationSplit.fit(training);

		// Make predictions on test data. model is the model with combination of parameters
		// that performed best.
		model.transform(test)
		.select("features", "label", "prediction")
		.show();
		// $example off$






		/*CrossValidator cv = new CrossValidator();
			CrossValidatorModel cvModel = cv.fit(training);




		 */
		sc.stop();

	}


}

