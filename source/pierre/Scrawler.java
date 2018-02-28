package pierre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.apache.spark.mllib.stat.correlation.CorrelationNames;

import com.google.common.collect.Lists;

import java.io.Serializable;

public class Scrawler implements Serializable {

	public static Scrawler sparkML = new Scrawler();
	public static SparkConf conf = new SparkConf().setAppName("Workshop").setMaster("local[*]");
	public static JavaSparkContext sc = new JavaSparkContext(conf);
	public ArrayList<String> colnames;
	public HashMap<String, Integer> modasY;
	public int nbX;

	public void createColnames(String header, String sep) {
		colnames  = new ArrayList<String>();
		String[] names = header.split(sep);
		for (int i = 0 ; i < names.length; i++) {
			colnames.add(names[i]);
		}
	}

	public JavaRDD<String> importer(String name){
		return sc.textFile(name);
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

	public JavaRDD<List<String>> splitCols(JavaRDD<String> data){
		JavaRDD<List<String>> res = data.map(new Function<String, List<String>>(){
			@Override
			public List<String> call(String s){
				return Arrays.asList(s.split("\\s*,\\s*"));
			}
		});
		return res;
	}

	public JavaRDD<Individu2> monIter2(JavaRDD<List<String>> dataSplit){
		JavaRDD<Individu2> temp = dataSplit.map(new Function<List<String>, Individu2>(){
			@Override
			public Individu2 call(List<String> ligne) {
				Individu2 res = new Individu2();
				for (int i = 0; i < ligne.size(); i++ ) {
					Set<String> tmp = new HashSet<String>();
					tmp.add(ligne.get(i));
					res.add(tmp);
				}
				return res;
			}
		});
		return temp;
	}

	public Individu2 individu2(JavaRDD<Individu2> temp) {
		Individu2 temp2 = temp.reduce(new Function2<Individu2, Individu2, Individu2>(){
			@Override
			public Individu2 call(Individu2 ind1, Individu2 ind2) {
				for (int i = 0; i < ind1.getVecteur().size(); i++) {
					Iterator<String> it = ind2.getVecteur().get(i).iterator();
					while (it.hasNext()) {
						ind1.getVecteur().get(i).add(it.next());
					}
				}
				return ind1;
			}
		});
		return temp2;
	}

	public MesVars knowtypes(Individu2 temp, List<String> colnames) {
		MesVars cols = new MesVars();
		for (int i = 0; i < temp.getVecteur().size(); i++) {
			Iterator<String> it = temp.getVecteur().get(i).iterator();
			boolean isDouble = true;
			boolean isBoolean = true;
			while (it.hasNext()) {
				String moda =it.next();
				try {
					Double.parseDouble(moda);
				} catch (Exception e){
					isDouble = false;
				}
				isBoolean = Boolean.parseBoolean(moda);
			}
			MonType montype = MonType.Double; 
			if (isDouble){
				montype = MonType.Double;
			}
			if (isBoolean) {
				montype = MonType.Boolean;
			}
			if (i == 0) {
				montype = MonType.Id;
			}
			if (i == 5) {
				montype = MonType.Y;
			}

			MaVar colI = new MaVar(montype, i, colnames.get(i));
			if (i == 5) {
				colI.setMesModas(temp.getVecteur().get(i));
			}
			cols.add(colI);
		}
		return cols;
	}

	public void createModalites(MesVars colnames) {
		modasY = new HashMap<String, Integer>();
		for (MaVar variable : colnames.getMesVars()) {
			if (variable.getMonType() == MonType.Y) {
				Iterator<String> it = variable.getMesModas().iterator();
				List<String> myList = Lists.newArrayList(it);
				Collections.sort(myList);
				int compteur = 0;
				for (String moda : myList) {
					modasY.put(moda, compteur);
					compteur++;
				}
			}
		}
	}

	public void knowNumbersOfClasses(MesVars cols) {
		nbX = 0;

		for (int i = 0; i < cols.getMesVars().size(); i++) {
			if(cols.getMesVars().get(i).getMonType() != MonType.Id & cols.getMesVars().get(i).getMonType() != MonType.Id) {
				nbX++;
			}
		}
	}

	public JavaRDD<LabeledPoint> convert(JavaRDD<List<String>> dataSplit, MesVars cols, Scrawler a){
		JavaRDD<LabeledPoint> data = dataSplit.map(new Function<List<String>, LabeledPoint>(){
			@Override
			public LabeledPoint call(List<String> ligne) {
				double[] vec = new double[a.nbX];
				int pos = 0;
				double monY = 0.0;
				for (int i = 0; i < ligne.size(); i++) {
					if (cols.getMesVars().get(i).getMonType() == MonType.Boolean) {
						if (ligne.get(i).equals("true")) {
							vec[i] = 1.0;
						}else {
							vec[i] = 0.0;
						}
					}
					if (cols.getMesVars().get(i).getMonType() == MonType.Double) {
						vec[i] = Double.parseDouble(ligne.get(i));
					}
					if (cols.getMesVars().get(i).getMonType() == MonType.Y) {
						monY = a.modasY.get(ligne.get(i));
					}
				}
				return new LabeledPoint(monY, Vectors.dense(vec));
			}
		});
		return data;
	}


	public static void main(String[] args) {
		Scrawler a = new Scrawler();
		JavaRDD<String> linesData = a.importer("resources/train_iris.csv");
		String header = a.getHeader(linesData);
		System.out.println();
		JavaRDD<String> dataWithoutHeader = a.removeHeader(linesData, a.getHeader(linesData));
		JavaRDD<List<String>> dataSplit = a.splitCols(dataWithoutHeader);
		System.out.println(dataSplit.collect().get(0));
		a.createColnames(header, ",");
		System.out.println(a.colnames);

		JavaRDD<Individu2> temp = a.monIter2(dataSplit);
		Individu2 ind = temp.collect().get(0);
		System.out.println(ind);


		Individu2 temp2 = a.individu2(temp);
		System.out.println(temp2);


		MesVars cols = a.knowtypes(temp2, a.colnames);
		System.out.println("cols = "+cols);

		a.createModalites(cols); 
		System.out.println(a.modasY);

		a.knowNumbersOfClasses(cols);
		JavaRDD<LabeledPoint> data = a.convert(dataSplit, cols, a);
		System.out.println(data.collect().get(0));
		sc.stop();
	}

}