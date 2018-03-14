package convertor;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

/**
 * Classe qui prépare le jeu de données pour le convertir en JavaRDD LabeledPoint
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class Convertisseur {
	public Scrawler scrawler = new Scrawler();
	public Header header;
	private Boolean headerOfData = false;
		
	/**
	 * regarde si le header a déjà été chargé 
	 * @return vrai ou faux
	 */
	public boolean isLoad() {
		return headerOfData;
	}

	/**
	 * initialise le header à partir du jeu de données reçu en paramètre
	 * @param train le chemin du fichier
	 */
	public void setHeader(String train) {
		header = prepareHeader(readDatas(train));
		headerOfData = true;
	}

	/**
	 * renvoie le jeu de données reçu en paramètre
	 * @param path le chemin du fichier csv
	 * @return le jeu de données à transformer
	 */
	public JavaRDD<List<String>> readDatas(String path){
		JavaRDD<String> linesData = scrawler.load(path);
		JavaRDD<List<String>> dataSplit = scrawler.splitCols(linesData);
		return dataSplit;
	}
	
	/**
	 * transforme le jeu de données en JavaRDD LabeledPoint
	 * @param data le jeu de données à transformer
	 * @param y la variable d'intérêt
	 * @return le jeu de données mis au propre
	 */
	public JavaRDD<LabeledPoint> prepareData(JavaRDD<List<String>> data, String y) {
		ConvertIntoLabeledPoint c = new ConvertIntoLabeledPoint();
		JavaRDD<LabeledPoint> train = c.convert(header, data, y);
		return train;
	}
	
	/**
	 * construit le header à partir du jeu de données reçu en paramètre
	 * @param data le jeu de données
	 * @return le header
	 */
	public Header prepareHeader(JavaRDD<List<String>> data) {
		List<String> header = scrawler.getHeader(data);
		JavaRDD<List<String>> dataWithoutHeader = scrawler.removeHeader(data, header);
		JavaRDD<Row> temp = scrawler.getInfosFromData(dataWithoutHeader);
		Row ind = temp.collect().get(0);
		Row temp2 = scrawler.reduceInfosFromData(temp);
		Header cols = scrawler.knowtypes(temp2, header);
		cols.check();
		return cols;
	}
	


}
