package classificationTree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;

import convertor.ConvertIntoLabeledPoint;
import convertor.Header;
import convertor.Row;
import convertor.Scrawler;
//import classificationTree.SparkML_CT;
import scala.Tuple2;

public class SparkML_CT {
	
	private Scrawler scrawler = new Scrawler();
	private Header header;
	
	public void setHeader(String train) {
		header = prepareHeader(readDatas(train));
		System.out.println(header.colnames());
	}
	
	
	private JavaRDD<List<String>> readDatas(String path){
		JavaRDD<String> linesData = scrawler.load(path);
		JavaRDD<List<String>> dataSplit = scrawler.splitCols(linesData);
		return dataSplit;
	}
	
	public JavaRDD<List<String>> importer(String path){
		JavaRDD<List<String>> dataSplit = readDatas(path);
		List<String> header = scrawler.getHeader(dataSplit);		
		return scrawler.removeHeader(dataSplit, header);
	}
	
	public JavaRDD<LabeledPoint> prepareData(JavaRDD<List<String>> data, String y) {
		return ConvertIntoLabeledPoint.convert(header, data, y);
	}
	
	public Header prepareHeader(JavaRDD<List<String>> data) {
		List<String> header = scrawler.getHeader(data);
		JavaRDD<List<String>> dataWithoutHeader = scrawler.removeHeader(data, header);
		JavaRDD<Row> temp = scrawler.getInfosFromData(dataWithoutHeader);
		Row temp2 = scrawler.reduceInfosFromData(temp);
		System.out.println(temp2);
		Header cols = scrawler.knowtypes(temp2, header);
		cols.check();
		return cols;
	}
		
	public DecisionTreeModel fit(String train, String y,String... args) {
		JavaRDD<LabeledPoint> train2 = prepareData(importer(train),y);
		int vary = header.getVar(y);
		System.out.println(y+ " : "+ vary);
		int numClasses = header.get().get(vary).getMesModas().size();
		System.out.println(numClasses);
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
		String impurity = "gini";
		int maxDepth = 5;
		int maxBins = 32;	

		DecisionTreeModel model = DecisionTree.trainClassifier(train2, numClasses,
				categoricalFeaturesInfo, impurity, maxDepth, maxBins);
		return model;
		
	}

	
	public Object evaluate(String train, String test, String y,String... args) {
		String entete = args[0];
		setHeader(entete);
		System.out.println(header);
		JavaRDD<LabeledPoint> test2 = prepareData(importer(test),y);	
		DecisionTreeModel model = (DecisionTreeModel) fit(train, y);
		
		JavaPairRDD<Double, Double> predictionAndLabel =
				test2.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
		double testErr =
				predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) test2.count();
		return 1-testErr;
		
		//return model;
	}

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String file = "iris";
		String y = "Species";
		SparkML_CT m = new SparkML_CT();
		System.out.println(m.evaluate("resources/train_"+file+".csv", "resources/test_"+file+".csv", y, "resources/"+file+".csv"));
		
	}

}
