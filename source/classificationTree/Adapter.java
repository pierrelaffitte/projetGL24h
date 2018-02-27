package classificationTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import breeze.linalg.Vector;


// TODO : à généraliser à n'importe quel CSV
public class Adapter {

	public static Adapter sparkML = new Adapter();
	public static SparkConf conf = new SparkConf().setAppName("Workshop").setMaster("local[*]");
	public static JavaSparkContext sc = new JavaSparkContext(conf);
	
	public Object importer(String path) {
		JavaRDD<String> linesData = sc.textFile(path);
		JavaRDD<LabeledPoint> data = sc.parallelize((ArrayList)sparkML.convert(linesData));
		return data;
	}
	
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
		//JavaRDD<String>col3 = sc.parallelize(col2);
		
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
		
		/*
		ArrayList<ArrayList<Boolean>> res = new ArrayList<ArrayList<Boolean>>();
		ArrayList<Boolean> inter = new ArrayList<Boolean>();
		inter.add(false);
		inter.add(false);
		inter.add(false);
		res.add(inter);
		res.add(inter);
		res.add(inter);
		res.add(inter);
		res.add(inter);
		System.out.println("nb de var : "+col.collect().get(0).size());

		
		for (int ind = 0; ind < col1.collect().size(); ind++) {
			for (int var = 1; var < col1.collect().get(0).size(); var++) {
				for (int type = 0; type <3 ; type ++) {
					Boolean b = (res.get(var-1).get(type) & col1.collect().get(ind).get(var).get(type));
					res.get(var-1).set(type, b);
				}
			}
		}
		System.out.println(res);
		
		*/
		
		
		
		
		
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
		
		ArrayList<HashMap<String, Integer>> mesModaRecodes = new ArrayList<HashMap<String, Integer>>();
		for (Set<String> mesModas : modalites) {
			HashMap<String, Integer> tmp = new HashMap<String, Integer>();
			Iterator<String> it = mesModas.iterator();
			int compteur = 1;
			while(it.hasNext()) {
				tmp.put(it.next(), compteur);
				compteur++;
			}
			mesModaRecodes.add(tmp);
		}
		System.out.println(mesModaRecodes);
		
		
		// création du jeu de données
		col.map(new Function<List<String>, E>(){
			@Override
			public E call(List<String> s) {
				double[] ligne = new double[s.size()-1];
				for (int i=1; i < s.size(); i++) {
					if (index.get(i).equals("Double")) {
						ligne[i] = Double.parseDouble(s.get(i));
					}
					if (index.get(i).equals("Boolean")) {
						
					}
				}
			}
		});
		
		/*
		
		int compteurD = 0;
		for (int type =0 ; type <index.size(); type++) {
			if (index.get(type).equals("Double")) {
				compteurD++;
			}
			if (index.get(type).equals("Integer")) {
				compteurD++;
			}
		}
		double[] valDou = new double[compteurD];
		*/
		
		/*
		ArrayList<LabeledPoint> temp = new ArrayList<LabeledPoint>();
		col.map(new Function<List<String>, LabeledPoint>(){
			@Override
			public LabeledPoint call(List<String> row) {
				double[] valDou = new double[compteurD];
				int cD = 0;
				for (int type =0 ; type <index.size(); type++) {
					if (index.get(type).equals("Double") | index.get(type).equals("Integer")) {
						valDou[cD] = Integer.parseInt(row.get(type));7
						cD++;
					}
				}
				
				return new LabeledPoint(varY, Vectors.dense(valDou));
			}
		});
		*/
		
		
		//JavaRDD<LabeledPoint> data = (JavaRDD<LabeledPoint>) a.importer("resources/train_iris.csv");
		//System.out.println(data);
		sc.stop();

	}


}



