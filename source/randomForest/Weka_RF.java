package randomForest;

import java.util.Enumeration;

import interfaces.Implementation;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;

public class Weka_RF implements Implementation {

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
	 * retourne l'indice de position de la variable Y reçue en paramètre
	 * @param data le chemin du fichier CSV
	 * @param y le nom de la colonne Y 
	 * @return pos
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
	 * vérifie si la variable y est bien du type String, pour pouvoir faire des arbres de classement
	 * @param data le jeu de données
	 * @param y le nom de la variable y
	 * @return vrai si la variable y est du type String, faux sinon	
	 */
	public static boolean isString(Instances data, String y) {
		Weka_RF weka = new Weka_RF();
		Instance row = data.get(0);
		int pos = weka.posY(data, y);
		return row.attribute(pos).isString();
	}

	@Override
	public Object fit(String train, String y,String...args){
		RandomForest rf = null;
		int nbtrees = Integer.valueOf(args[0]);
		try {
			Instances train1 = ((Instances) importer(train));
			if (!Weka_RF.isString(train1, y) ){
				train1 = this.convertNumToString(train1, y);
			}
			train1.setClassIndex(posY(train1,y));
			rf = new RandomForest();
			rf.setNumIterations(nbtrees);
			rf.buildClassifier(train1);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return rf;
	}

	/**
	 * convertit la variable y de numeric en String
	 * @param data l'instance de la colonne y
	 * @param y le nom de la colonne
	 * @return l'instance de la variable y transformée
	 */
	public  Instances convertNumToString(Instances data, String y) {
		/* change la variable y de quantitative à qualitative*/
		Instances res = null;
		int pos = this.posY(data, y);
		NumericToNominal convert= new NumericToNominal();
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
		Object resultat = null;
		RandomForest rf = (RandomForest) fit(train,y, args);
		Instances test1 = (Instances) importer(test);
		if (!Weka_RF.isString(test1, y) ){
			test1 = this.convertNumToString(test1, y);
		}
		test1.setClassIndex(posY(test1,y));
		Evaluation eval;
		try {
			eval = new Evaluation(test1);
			eval.evaluateModel(rf, test1);
			resultat = eval.correct()*100.0/test1.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultat;
	}
}
