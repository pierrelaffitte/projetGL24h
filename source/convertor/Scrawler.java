package convertor;

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

import java.io.Serializable;

public class Scrawler implements Serializable {

	public static Scrawler sparkML = new Scrawler();
	public static SparkConf conf = new SparkConf().setAppName("Workshop").setMaster("local[*]");
	public static JavaSparkContext sc = new JavaSparkContext(conf);
	public ArrayList<String> colnames;
	public HashMap<String, Integer> modasY;
	public int nbX;
	public String sep = ",";
	
	public void setSep(String sep) {
		this.sep = sep;
	}
	
	public String getSep() {
		return sep;
	}

	public void createColnames(String header, String sep) {
		colnames  = new ArrayList<String>();
		String[] names = header.split(sep);
		for (int i = 0 ; i < names.length; i++) {
			colnames.add(names[i]);
		}
	}

	public JavaRDD<String> load(String name){
		return sc.textFile(name);
	}

	public List<String> getHeader(JavaRDD<List<String>> data) {
		return data.first();
	}

	public JavaRDD<List<String>> removeHeader(JavaRDD<List<String>> data, List<String> header){
		JavaRDD<List<String>> res = (JavaRDD) data.filter(new Function<List<String>, Boolean>(){
			@Override
			public Boolean call(List<String> s) {
				return !s.equals(header);
			}
		});
		return res;
	}

	public JavaRDD<List<String>> removeHeader2(JavaRDD<List<String>> data){
		List<String> header = data.first();
		JavaRDD<List<String>> res = (JavaRDD) data.filter(new Function<List<String>, Boolean>(){
			@Override
			public Boolean call(List<String> s) {
				return !s.equals(header);
			}
		});
		return res;
	}
	
	public JavaRDD<List<String>> splitCols(JavaRDD<String> data){
		JavaRDD<List<String>> res = data.map(new Function<String, List<String>>(){
			@Override
			public List<String> call(String s){
				return Arrays.asList(s.split("\\s*"+sep+"\\s*"));
			}
		});
		return res;
	}
	
	public JavaRDD<Row> getInfosFromData(JavaRDD<List<String>> data){
		JavaRDD<Row> temp = data.map(new Function<List<String>, Row>(){
			@Override
			public Row call(List<String> ligne) {
				Row res = new Row();
				for (int colonne = 0; colonne < ligne.size(); colonne++ ) {
					Set<String> tmp = new HashSet<String>();
					tmp.add(ligne.get(colonne));
					res.add(tmp);
				}
				return res;
			}
		});
		return temp;
	}

	public Row reduceInfosFromData(JavaRDD<Row> temp) {
		Row temp2 = temp.reduce(new Function2<Row, Row, Row>(){
			@Override
			public Row call(Row ind1, Row ind2) {
				for (int i = 0; i < ind1.get().size(); i++) {
					Iterator<String> it = ind2.get().get(i).iterator();
					while (it.hasNext()) {
						ind1.get().get(i).add(it.next());
					}
				}
				return ind1;
			}
		});
		return temp2;
	}

	public Header knowtypes(Row temp, List<String> colnames) {
		Header cols = new Header();
		for (int i = 0; i < temp.get().size(); i++) {
			Iterator<String> it = temp.get().get(i).iterator();
			boolean isDouble = true;
			boolean isBoolean = true;
			while (it.hasNext()) {
				String moda =it.next();
				try {
					Double.parseDouble(moda);
				} catch (Exception e){
					isDouble = false;
				}
				if (moda.equals("true") | moda.equals("false")) {
					isBoolean = true;
				}else {
					isBoolean = false;
				}
				
			}
			MonType montype = MonType.Double; 
			if (i == 0) {
				montype = MonType.Id;
			}else {
				if (isDouble){
					montype = MonType.Double;
				}else {
					if (isBoolean) {
						montype = MonType.Boolean;
					}else {
						montype = MonType.Y;
					}
				}
			}
			
			Variable colI = new Variable(montype, i, colnames.get(i));
			colI.setMesModas(temp.get().get(i));
			/*
			if (colI.getMonType()== MonType.Y) {
				colI.setMesModas(temp.get().get(i));
			}*/
			cols.add(colI);
		}
		return cols;
	}

	public static void main(String[] args) {
		Scrawler a = new Scrawler();
		JavaRDD<String> linesData = a.load("resources/train_statsFSEVary.csv");
		JavaRDD<List<String>> dataSplit = a.splitCols(linesData);
		
		System.out.println(dataSplit.collect().get(0));
		List<String> header = a.getHeader(dataSplit);
		System.out.println(header);
		
		JavaRDD<List<String>> dataWithoutHeader = a.removeHeader(dataSplit, a.getHeader(dataSplit));
		System.out.println(dataWithoutHeader.collect().get(0));
		
		JavaRDD<Row> temp = a.getInfosFromData(dataWithoutHeader);
		Row ind = temp.collect().get(0);
		System.out.println(ind);

		Row temp2 = a.reduceInfosFromData(temp);
		System.out.println(temp2);

		Header cols = a.knowtypes(temp2, header);
		System.out.println("cols = \n "+cols);
		sc.stop();
	}

}