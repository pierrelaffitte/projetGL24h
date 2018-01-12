package algorithmes;

import java.util.HashMap;
import java.util.Map;

import scala.Tuple2;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.util.MLUtils;
import org.apache.spark.SparkConf;

public class SparkML implements algoInterface{
	
	static SparkConf sparkConf = new SparkConf().setAppName("JavaDecisionTree").setMaster("local");
	static JavaSparkContext sc = new JavaSparkContext(sparkConf);
	
	public static void main(String[] args) {
		// Load and parse the data file.
		//String datapath = "data/mllib/sample_libsvm_data.txt";
		//JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(sc.sc(), datapath).toJavaRDD();
		// Split the data into training and test sets (30% held out for testing)
		//JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3});
		
		JavaRDD<LabeledPoint> trainingData = MLUtils.loadLibSVMFile(sc.sc(), "resources/train_iris").toJavaRDD();
		JavaRDD<LabeledPoint> testData = MLUtils.loadLibSVMFile(sc.sc(), "resources/test_iris").toJavaRDD();

		// Set parameters.
		//  Empty categoricalFeaturesInfo indicates all features are continuous.
		Integer numClasses = 2;
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
		String impurity = "gini";
		Integer maxDepth = 5;
		Integer maxBins = 32;

		// Train a DecisionTree model for classification.
		final DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses,
		  categoricalFeaturesInfo, impurity, maxDepth, maxBins);

		// Evaluate model on test instances and compute test error
		JavaPairRDD<Double, Double> predictionAndLabel =
		  testData.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
		    public Tuple2<Double, Double> call(LabeledPoint p) {
		      return new Tuple2<Double, Double>(model.predict(p.features()), p.label());
		    }
		  });
		Double testErr =
		  1.0 * predictionAndLabel.filter(new Function<Tuple2<Double, Double>, Boolean>() {
		    public Boolean call(Tuple2<Double, Double> pl) {
		      return !pl._1().equals(pl._2());
		    }
		  }).count() / testData.count();
		
		System.out.println("Test Error: " + testErr);
		System.out.println("Learned classification tree model:\n" + model.toDebugString());

	}

	public Object importer(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object fit(String train) {
		// TODO Auto-generated method stub
		return null;
	}

	public void evaluate(Object model, String test) {
		// TODO Auto-generated method stub
		
	}

	public Object fit(String train, String y) {
		// TODO Auto-generated method stub
		return null;
	}

	public void evaluate(Object model, String test, String y) {
		// TODO Auto-generated method stub
		
	}

}
