package classificationTree;

import java.util.Enumeration;

import interfaces.Implementation;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

/**
 * Algorithme d'arbres de classification en Weka
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class Weka_CT implements Implementation {

	@Override
	public Object importer(String file) {
		Instances data = null;
		try {
			DataSource source = new DataSource(file);
			data = source.getDataSet();
		}catch (Exception e) {
			System.out.println("Le fichier "+file+" n'existe pas. \n");
		}
		return data;
	}

	/**
	 * Retourne l'indice de position de la variable Y reçue en paramètre
	 * @param data chemin du fichier CSV
	 * @param y nom de la colonne Y 
	 * @return position de Y
	 */
	public int posY(Instances data, String y) {
		Enumeration<Attribute> liste= data.enumerateAttributes();
		int compteur = 0;
		int pos = data.numAttributes();
		while (liste.hasMoreElements() & compteur < data.numAttributes()) {
			if(liste.nextElement().name().equals(y)) {
				pos = compteur;
				compteur = data.numAttributes();
			}
			compteur++;
		}
		return pos;
	}

	/**
	 * Renvoie "true" si la variable à expliquer Y est une variable qualitative
	 * @param data chemin du fichier CSV
	 * @param y nom de la colonne Y
	 * @return booleen
	 */
	public static boolean isString(Instances data, String y) {
		Weka_CT weka = new Weka_CT();
		Instance row = data.get(0);
		int pos = weka.posY(data, y);
		return row.attribute(pos).isString();
	}

	@Override
	public Object fit(String train, String y,String...args){
		J48 tree = null;
		try {
			Instances train1 = ((Instances) importer(train));
			if (!Weka_CT.isString(train1, y) ){
				train1 = this.convertNumToString(train1, y);
			}
			train1.setClassIndex(posY(train1,y));
			tree = new J48();
			tree.buildClassifier(train1);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return tree;
	}

	/**
	 *  convertit la variable y de numeric en String
	 * @param data chemin du fichier CSV
	 * @param y nom de la variable à expliquer
	 * @return dataframe converti
	 */
	public  Instances convertNumToString(Instances data, String y) {
		Instances res = null;
		NumericToNominal convert= new NumericToNominal();
		
		int pos = this.posY(data, y);
		String[] options= new String[2];
		options[0]="-R";
		options[1]= String.valueOf(pos+1);  //range of variables to make numeric

		try {
			convert.setOptions(options);
			convert.setInputFormat(data);			
			res = Filter.useFilter(data, convert);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}
	
	@Override
	public Object evaluate(String train, String test, String y,String...args) {
		Evaluation eval;
		Object resultat = null;
		J48 tree = (J48) fit(train,y);
		Instances test1 = (Instances) importer(test);
		
		if (!isString(test1, y) ){
			test1 = this.convertNumToString(test1, y);
		}
		test1.setClassIndex(posY(test1,y));

		try {
			eval = new Evaluation(test1);
			eval.evaluateModel(tree, test1);
			resultat = eval.correct()*1.0/test1.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultat;
	}
}
