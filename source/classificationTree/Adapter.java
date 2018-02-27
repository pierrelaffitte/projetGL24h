package classificationTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public class Adapter implements Serializable{

	public static Adapter sparkML = new Adapter();
	public static SparkConf conf = new SparkConf().setAppName("Workshop").setMaster("local[*]");
	public static JavaSparkContext sc = new JavaSparkContext(conf);

	public int chooseY(String y, String header, String sep, boolean id) {
		String[] colnames = header.split(sep);
		int res = 0;
		for (int i =0; i < colnames.length; i++) {
			if (y.equals(colnames[i])) {
				res = i;
				if (id) {
					res--;
				}
			}
		}
		return res;
	}

	public String getHeader(JavaRDD<String> data) {
		return data.first();
	}

	public JavaRDD<String> removeHeader(JavaRDD<String> data, String header){
		JavaRDD<String> res = (JavaRDD) data.filter(new Function<String, Boolean>(){
			@Override
			public Boolean call(String s) {
				return !s.equals(header);
			}
		});
		return res;
	}

	public JavaRDD<List<String>> splitData(JavaRDD<String> data){
		JavaRDD<List<String>> res = data.map(new Function<String, List<String>>(){
			@Override
			public List<String> call(String s){
				return Arrays.asList(s.split("\\s*,\\s*"));
			}
		});
		return res;
	}

	public JavaRDD<ArrayList<ArrayList<Boolean>>> typeOfDataByIndividu(JavaRDD<List<String>> data) {
		JavaRDD<ArrayList<ArrayList<Boolean>>> res = data.map(new Function<List<String>, ArrayList<ArrayList<Boolean>>>(){
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
		return res;
	}

	public HashMap<String, List<Integer>> typeOfVar(JavaRDD<List<String>> data, JavaRDD<ArrayList<ArrayList<Boolean>>> typesOfInd, int varY) {
		HashMap<String, List<Integer>> prop = new HashMap<String, List<Integer>>();
		prop.put("Double", new ArrayList<Integer>());
		prop.put("Boolean", new ArrayList<Integer>());
		prop.put("Integer", new ArrayList<Integer>());
		prop.put("String", new ArrayList<Integer>());
		//System.out.println("nb de var : "+col.collect().get(0).size());
		//System.out.println("nb de type"+col.collect().size());
		for (int var = 1; var < data.collect().get(0).size(); var++) {
			if (var == varY) {
				prop.get("String").add(var);	
			}else {
				int mavar = var;
				boolean find = false;
				for (int type = 0; type < 3; type++) {
					int montype = type;
					Boolean temp = typesOfInd.map(new Function<ArrayList<ArrayList<Boolean>>, Boolean>(){
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



		}
		return prop;
	}

	public ArrayList<String> knowTypes(JavaRDD<List<String>> col,HashMap<String, List<Integer>> prop) {

		ArrayList<String> index = new ArrayList<String>();
		for (String var: col.first()) {
			index.add(" ");
		}

		for (String key : prop.keySet()) {
			for ( int i = 0 ; i < prop.get(key).size(); i++) {
				index.set(prop.get(key).get(i), key);
			}
		}
		return index;
	}

	public ArrayList<Set<String>> knowModalites(ArrayList<String> index,JavaRDD<List<String>> col ){
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
		return modalites;
	}


	public ArrayList<HashMap<String, Double>> convModalites(ArrayList<Set<String>> modalites) {
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
		return mesModaRecodes;
	}

	public ArrayList<HashMap<String, Double>> convModalites2(ArrayList<Set<String>> modalites) {
		ArrayList<HashMap<String, Double>> mesModaRecodes = new ArrayList<HashMap<String, Double>>();
		for (Set<String> mesModas : modalites) {
			mesModaRecodes.add(convModa(mesModas));
		}
		return mesModaRecodes;
	}
	
	public HashMap<String, Double> convModa(Set<String> modalites) {
		HashMap<String, Double> mesModaRecodes = new HashMap<String, Double>();
		ArrayList<String> mesModasOrdonnees = new ArrayList<String>();
		mesModasOrdonnees.addAll(modalites);
		Collections.sort(mesModasOrdonnees);
		System.out.println(mesModasOrdonnees);
		Double compteur = 0.0;
		for (String moda : mesModasOrdonnees) {
			mesModaRecodes.put(moda, compteur);
			compteur++;
		}
		return mesModaRecodes;
	}	

	public JavaRDD<Double[]> recodage(ArrayList<String> index, JavaRDD<List<String>> col, ArrayList<HashMap<String, Double>> mesModaRecodes){
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
		return vecteur;
	}

	public JavaRDD<LabeledPoint> convertIntoLabeledPoint(JavaRDD<Double[]> vecteur, int varY){

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
		return res;
	}


	public Set<String> varYModalites(int varY,JavaRDD<List<String>> col ){
		Set<String> mesModa = col.map(new Function<List<String>, Set<String>>(){
			@Override
			public Set<String> call(List<String> s) {
				Set<String> res = new HashSet<String>();
				res.add(s.get(varY).toString());
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
		return mesModa;
	}

	public JavaRDD<LabeledPoint> convert(JavaRDD<String> linesData, String y, String sep){
		// on retire la première ligne (le header) 
		String header = getHeader(linesData);
		int varY = chooseY(y, header, sep, true);
		System.out.println(varY);
		
		JavaRDD<String> dataWithoutHeader = removeHeader(linesData, header);
		JavaRDD<List<String>> data = splitData(dataWithoutHeader);
		System.out.println(data.collect().get(0));
		JavaRDD<ArrayList<ArrayList<Boolean>>> hypo = typeOfDataByIndividu(data);
		System.out.println(hypo.collect().get(0));
		
		
		HashMap<String, List<Integer>> prop = typeOfVar(data, hypo,varY+1);
		System.out.println(prop);
		
		ArrayList<String> monVecteurDeType = knowTypes(data, prop);
		System.out.println(monVecteurDeType);
		ArrayList<Set<String>> mesModas = knowModalites(monVecteurDeType, data);
		System.out.println(mesModas);
		ArrayList<HashMap<String, Double>> mesModasRecodes= convModalites2(mesModas);
		System.out.println(mesModasRecodes);
		
		
		
		JavaRDD<Double[]> myDataAlmostClean= recodage(monVecteurDeType, data, mesModasRecodes);
		Double[] maLigne = myDataAlmostClean.collect().get(0);
		for (Double val : maLigne) {
			System.out.println(val);
		}
		JavaRDD<LabeledPoint> train = convertIntoLabeledPoint(myDataAlmostClean, varY);
		return train;
	}
	


	public Object fit(JavaRDD<LabeledPoint> train, String y,String... args) {
		int numClasses = 4;
		Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
		String impurity = "gini";
		int maxDepth = 5;
		int maxBins = 32;	

		DecisionTreeModel model = DecisionTree.trainClassifier(train, numClasses,
				categoricalFeaturesInfo, impurity, maxDepth, maxBins);
		return model;
	}
	
	public JavaRDD<LabeledPoint> importer(String name, String y, String sep){
		JavaRDD<String> linesData = sc.textFile(name);
		return convert(linesData, y, sep);
	}
	
	public int comptNbClassesY() {
		return 0;
	}

	public static void main(String[] args) {
		/*
		Adapter a = new Adapter();
		JavaRDD<String> linesData = sc.textFile("resources/train_iris.csv");

		// on retire la première ligne (le header) 
		String header = linesData.first();
		System.out.println(a.chooseY("Species", header, ",", true));





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

		// choix du Y
		int varY = 4;


		// création des LabeledPoint


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
		 */

		Adapter a = new Adapter();
		JavaRDD<String> linesData = sc.textFile("resources/train_statsFSEVary.csv");
		//JavaRDD<LabeledPoint> train = a.convert(linesData);
		
		/*
		// on retire la première ligne (le header) 
		String header = a.getHeader(linesData);
		int varY = a.chooseY("sizePDF", header, ",", true);
		System.out.println(varY);
		
		JavaRDD<String> dataWithoutHeader = a.removeHeader(linesData, header);
		JavaRDD<List<String>> data = a.splitData(dataWithoutHeader);
		System.out.println(data.collect().get(0));
		JavaRDD<ArrayList<ArrayList<Boolean>>> hypo = a.typeOfDataByIndividu(data);
		System.out.println(hypo.collect().get(0));
		
		
		HashMap<String, List<Integer>> prop = a.typeOfVar(data, hypo,varY+1);
		System.out.println(prop);
		
		ArrayList<String> monVecteurDeType = a.knowTypes(data, prop);
		System.out.println(monVecteurDeType);
		ArrayList<Set<String>> mesModas = a.knowModalites(monVecteurDeType, data);
		System.out.println(mesModas);
		ArrayList<HashMap<String, Double>> mesModasRecodes= a.convModalites2(mesModas);
		System.out.println(mesModasRecodes);
		
		
		
		JavaRDD<Double[]> myDataAlmostClean= a.recodage(monVecteurDeType, data, mesModasRecodes);
		Double[] maLigne = myDataAlmostClean.collect().get(0);
		for (Double val : maLigne) {
			System.out.println(val);
		}
		JavaRDD<LabeledPoint> train = a.convertIntoLabeledPoint(myDataAlmostClean, varY);
		System.out.println(train.collect().get(0));
		System.out.println(train.collect().get(0).label());
		*/
		//a.fit(train, "sizePDF"); 
		sc.stop();

	}


}



