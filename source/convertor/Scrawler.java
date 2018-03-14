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

/**
 * Parcours un jeu de données pour en ressortir la structure
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
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

	/**
	 * récupère le nom des colonnes du fichier csv reçu en paramètre
	 * @param header la chaine de caractère qui contient la première ligne du fichier csv
	 * @param sep le séparateur
	 */
	public void createColnames(String header, String sep) {
		colnames  = new ArrayList<String>();
		String[] names = header.split(sep);
		for (int i = 0 ; i < names.length; i++) {
			colnames.add(names[i]);
		}
	}

	/**
	 * charge le fichier csv reçu en paramètre
	 * @param name le chemin du fichier
	 * @return le fichier
	 */
	public JavaRDD<String> load(String name){
		return sc.textFile(name);
	}

	/**
	 * renvoie l'entête du jeu de données reçu en paramètre
	 * @param data le jeu de données
	 * @return la première ligne, soit l'entête
	 */
	public List<String> getHeader(JavaRDD<List<String>> data) {
		return data.first();
	}

	/**
	 * renvoie le jeu de données sans l'entête 
	 * @param data le jeu de données
	 * @param header l'entête
	 * @return le jeu de données mis au propre
	 */
	public JavaRDD<List<String>> removeHeader(JavaRDD<List<String>> data, List<String> header){
		JavaRDD<List<String>> res = (JavaRDD) data.filter(new Function<List<String>, Boolean>(){
			@Override
			public Boolean call(List<String> s) {
				return !s.equals(header);
			}
		});
		return res;
	}
	
	/**
	 * transforme le jeu de données en séparant les colonnes avec le séparateur déjà initialisé
	 * @param data le jeu de données à transformer
	 * @return le jeu de données mis au propre
	 */
	public JavaRDD<List<String>> splitCols(JavaRDD<String> data){
		JavaRDD<List<String>> res = data.map(new Function<String, List<String>>(){
			@Override
			public List<String> call(String s){
				return Arrays.asList(s.split("\\s*"+sep+"\\s*"));
			}
		});
		return res;
	}
	
	/**
	 * renvoie les informations sur les individus du jeu de données reçu en paramètre
	 * @param data le jeu de données
	 * @return les informations
	 */
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

	/**
	 * agrège l'information de l'individu à la variable. Pour chaque variable, on a l'ensemble des valeurs prises
	 * @param data le jeu de données 
	 * @return la liste des variables avec l'ensemble des valeurs prises
	 */
	public Row reduceInfosFromData(JavaRDD<Row> data) {
		Row res = data.reduce(new Function2<Row, Row, Row>(){
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
		return res;
	}

	/**
	 * renvoie le Header associé au Row
	 * @param data le row contenant
	 * @param colnames le nom des colonnes
	 * @return le header
	 */
	public Header knowtypes(Row data, List<String> colnames) {
		Header cols = new Header();
		for (int i = 0; i < data.get().size(); i++) {
			Iterator<String> it = data.get().get(i).iterator();
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
						montype = MonType.Qualitative;
					}
				}
			}
			
			Variable colI = new Variable(montype, i, colnames.get(i));
			colI.setMesModas(data.get().get(i));
			cols.add(colI);
		}
		return cols;
	}
}