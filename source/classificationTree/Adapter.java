package classificationTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;

import breeze.linalg.Vector;
import scala.Tuple2;


// TODO : à généraliser à n'importe quel CSV
public class Adapter {

	public static Adapter sparkML = new Adapter();
	public static SparkConf conf = new SparkConf().setAppName("Workshop").setMaster("local[*]");
	public static JavaSparkContext sc = new JavaSparkContext(conf);
	
	public Object fit(JavaRDD<LabeledPoint> train, String y,String... args) {
		int numClasses = 3;
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
		String impurity = "gini";
		int maxDepth = 5;
		int maxBins = 32;	

		DecisionTreeModel model = DecisionTree.trainClassifier(train, numClasses,
				categoricalFeaturesInfo, impurity, maxDepth, maxBins);
		return model;
	}
	/*
	public Object evaluate(JavaRDD train, String test, String y,String... args) {
		
		DecisionTreeModel model = (DecisionTreeModel) fit(train, y);
		JavaPairRDD<Double, Double> predictionAndLabel =
				test2.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));
		double testErr =
				predictionAndLabel.filter(pl -> !pl._1().equals(pl._2())).count() / (double) test2.count();

		//System.out.println("Test Error: " + (1-testErr));
		//System.out.println("Learned classification tree model:\n" + model.toDebugString());
		return 1-testErr;
	}
	*/
	public static void main(String[] args) {
		Adapter a = new Adapter();
		JavaRDD<String> linesData = sc.textFile("resources/train_iris.csv");
				
		// on retire la première ligne (le header) 
		String header = linesData.first();
		JavaRDD<String> col2 = (JavaRDD) linesData.filter(new Function<String, Boolean>(){
			@Override
			public Boolean call(String s) {
				return !s.equals(header);
			}
		});
		System.out.println(col2.collect().get(0));
		
		JavaRDD<List<String>> col = col2.map(new Function<String, List<String>>(){
			@Override
			public List<String> call(String s){
				return Arrays.asList(s.split("\\s*,\\s*"));
			}
		});
		System.out.println(col.collect().get(0));
		
		JavaRDD<ArrayList<ArrayList<Boolean>>> col1 = col.map(new Function<List<String>, ArrayList<ArrayList<Boolean>>>(){
			@Override
			public ArrayList<ArrayList<Boolean>> call(List<String> s) {
				ArrayList<ArrayList<Boolean>> res = new ArrayList<ArrayList<Boolean>>();
				for (int col = 0; col < s.size(); col ++) {
					res.add(new ArrayList<Boolean>());
					try {
						Double.parseDouble(s.get(col));
						res.get(col).add(true);
					} catch (Exception e){
						res.get(col).add(false);
					}
					boolean b = Boolean.parseBoolean(s.get(col));
					res.get(col).add(b);
					try {
						Integer.parseInt(s.get(col));
						res.get(col).add(true);
					} catch (Exception e){
						res.get(col).add(false);
					}					
				}
				return res;
			}
		});
		System.out.println(col1.collect().get(0));
		
		System.out.println(col1.collect().get(1));
		System.out.println(col1.collect().get(2));
		HashMap<String, List<Integer>> prop = new HashMap<String, List<Integer>>();
		prop.put("Double", new ArrayList<Integer>());
		prop.put("Boolean", new ArrayList<Integer>());
		prop.put("Integer", new ArrayList<Integer>());
		prop.put("String", new ArrayList<Integer>());
		System.out.println("nb de var : "+col.collect().get(0).size());
		System.out.println("nb de type"+col.collect().size());
		for (int var = 1; var < col.collect().get(0).size(); var++) {
			int mavar = var;
			boolean find = false;
			for (int type = 0; type < 3; type++) {
				int montype = type;
				Boolean temp = col1.map(new Function<ArrayList<ArrayList<Boolean>>, Boolean>(){
					@Override
					public Boolean call(ArrayList<ArrayList<Boolean>> ligne) {
						return ligne.get(mavar).get(montype);
					}
				}).reduce(new Function2<Boolean, Boolean, Boolean>(){
					@Override
					public Boolean call(Boolean b1, Boolean b2) {
						return b1 & b2;
					}
				});
				System.out.println(mavar+ " "+montype+ "  "+ temp);
				if (temp) {
					find = true;
					if (montype == 0) {
						prop.get("Double").add(mavar);
					}
					if (montype == 1) {
						prop.get("Boolean").add(mavar);
					}
					if (montype == 2) {
						prop.get("Integer").add(mavar);
					}
				}
			
			if (!find)
				prop.get("String").add(mavar);				
			}
			
				
		}
		System.out.println(prop);
		
		ArrayList<String> index = new ArrayList<String>();
		for (String var: col.first()) {
			index.add(" ");
		}
		
		for (String key : prop.keySet()) {
			for ( int i = 0 ; i < prop.get(key).size(); i++) {
				index.set(prop.get(key).get(i), key);
			}
		}
		
		
		System.out.println(index);
		ArrayList<Set<String>> modalites = new ArrayList<Set<String>>();
		for (int i =0; i < index.size(); i++) {
			int colonne = i;
			if (index.get(i).equals("String")) {
				Set<String> mesModa = col.map(new Function<List<String>, Set<String>>(){
					@Override
					public Set<String> call(List<String> s) {
						Set<String> res = new HashSet<String>();
						res.add(s.get(colonne));
						return res;
					}
				}).reduce(new Function2<Set<String>, Set<String>, Set<String>>(){
					@Override
					public Set<String> call(Set<String> set1, Set<String> set2){
						Iterator<String> it = set2.iterator();
						while(it.hasNext()) {
							set1.add(it.next());
						}
						return set1;
					}
				});
				modalites.add(mesModa);
			}
		}
		System.out.println(modalites);
		
		ArrayList<HashMap<String, Double>> mesModaRecodes = new ArrayList<HashMap<String, Double>>();
		for (Set<String> mesModas : modalites) {
			HashMap<String, Double> tmp = new HashMap<String, Double>();
			Iterator<String> it = mesModas.iterator();
			double compteur = 0;
			while(it.hasNext()) {
				tmp.put(it.next(), compteur);
				compteur++;
			}
			mesModaRecodes.add(tmp);
		}
		System.out.println(mesModaRecodes);
		
		
		// création du jeu de données
		JavaRDD<Double[]> vecteur = col.map(new Function<List<String>, Double[]>(){
			@Override
			public Double[] call(List<String> s) {
				Double[] ligne = new Double[s.size()-1];
				int j = 0;
				for (int i=1; i < s.size(); i++) {
					if (index.get(i).equals("Double")) {
						ligne[i-1] = Double.parseDouble(s.get(i));
					}
					if (index.get(i).equals("Boolean")) {
						if (s.get(i).equals("true")){
							ligne[i-1] = 1.0;
						}else {
							ligne[i-1] = 0.0;
						}
					}
					if (index.get(i).equals("String")) {
						ligne[i-1] = mesModaRecodes.get(j).get(s.get(i));
						j++;
					}
				}
				return ligne;
			}
		});
		Double[] maLigne = vecteur.collect().get(0);
		for (Double val : maLigne) {
			System.out.println(val);
		}
		
		// création des LabeledPoint
		int varY = 4;
		
		JavaRDD<LabeledPoint> res = vecteur.map(new Function<Double[], LabeledPoint>(){
			@Override
			public LabeledPoint call(Double[] monvec) {
				double[] x = new double[monvec.length];
				int j = 0;
				for (int i = 0; i< monvec.length; i++) {
					if (i != varY) {
						x[j] = monvec[i];
						j++;
					}
				}
				return new LabeledPoint(monvec[varY], Vectors.dense(x));
			}
		});
		System.out.println(res.collect().get(0));
		
		a.fit(res, "Species");
		

		sc.stop();

	}


}



