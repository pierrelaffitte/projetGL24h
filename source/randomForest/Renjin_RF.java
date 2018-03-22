package randomForest;

import javax.script.*;

import org.renjin.script.*;

import interfaces.Implementation;

/**
 * Algorithme de forêts aléatoires en Renjin
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class Renjin_RF implements Implementation {

	private static RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	private static ScriptEngine engine = factory.getScriptEngine();	
	
	@Override
	public Object importer(String file) {	
		Object data = null;
		String code = "data <- read.csv(\""+ file +"\")";
		try {
			data = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Renvoie les données d'une variable à partir de son appellation 
	 * @param data data frame contenant les données
	 * @param var nom de la variable
	 * @return données de la variable
	 */
	public Object returnVar(Object data, String var) {
		Object variable = null;
		
		String code = "col <- which(colnames(data) == y)\n" + 
		              "var <- data[,col]";
	
		try {
			engine.put("data", data);
			engine.put("y", var);
			variable = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return variable;
	}
	
	@Override
	public Object fit(String train, String y,String... otherArgs) {
		Object trainCSV = importer(train);
		Object modCart = null;
		
		String code = "library(randomForest)\n" +
				      "col <- which(colnames(data) ==\""+ y +"\")\n" +
					  "randomForest(y ~ ., data = data_train[c(-col)], ntree = nb_tree)";
		
		try {
			engine.put("nb_tree", Integer.valueOf(otherArgs[0]));
			engine.put("data_train", trainCSV);
			engine.put("y", returnVar(trainCSV,y));
			modCart = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
				
		return modCart;
	}

	@Override
	public Object evaluate(String train, String test, String y,String... otherArgs) {
		Object testCSV = importer(test);
		Object model=fit(train, y,otherArgs);
		Object accuracy=null;
		String code = "set.seed(1)\n"+
				"modpredRF=predict(mod,data_test,type=\"class\")\n" +  
				"modmatRF=table(y,modpredRF)\n" + 
				"modtaux_err_RF= sum(modpredRF != y)/nrow(data_test)\n" + 
				"accuracy <- 1-modtaux_err_RF\n";
		try {
			engine.put("data_test", testCSV);
			engine.put("mod", model);
			engine.put("y", returnVar(testCSV, y));
			accuracy = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return accuracy;	
	}
	
	public static void main(String[] args) throws Exception {
		// Essai méthodes d'arbres de classification
		Renjin_RF rj = new Renjin_RF();
		
		System.out.println("Fichier 1 : iris ---------------------------------------");
		//Object modRF = rj.fit("resources/train_iris.csv","Species");
		Object accuracy = rj.evaluate("resources/train_iris.csv", "resources/test_iris.csv","Species","20");
		System.out.println(accuracy);
		
		System.out.println("Fichier 2 : statsFSEVary ---------------------------------------");
		//Object modRF2 = rj.fit("resources/train_statsFSEVary.csv","nbPages");
		accuracy = rj.evaluate("resources/train_statsFSEVary.csv", "resources/test_statsFSEVary.csv","nbPages","20");
		System.out.println(accuracy);
		
		System.out.println("Fichier 3 : winequality ---------------------------------------");
		//Object modRF3 = rj.fit("resources/train_winequality.csv","quality");
		accuracy = rj.evaluate("resources/train_winequality.csv", "resources/test_winequality.csv","quality","20");
		System.out.println(accuracy);
		
		System.out.println("Fichier 4 : mushrooms ---------------------------------------");
		accuracy = rj.evaluate("resources/train_mushrooms.csv", "resources/test_mushrooms.csv","class","20");
		System.out.println(accuracy);
	}
}