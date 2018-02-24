package randomForest;

import javax.script.*;

import org.renjin.script.*;

import interfaces.algoInterface;

// TODO 1er argument => ntree

public class Renjin_randomForest implements algoInterface {

	private static RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	private static ScriptEngine engine = factory.getScriptEngine();	
	
	public static void main(String[] args) throws Exception {
		// code R    
		//engine.eval(new java.io.FileReader("resources/progR.R"));
		
		// Essai méthodes d'arbres de classification
		Renjin_randomForest rj = new Renjin_randomForest();
		
		System.out.println("Fichier 1 : iris ---------------------------------------");
		//Object modRF = rj.fit("resources/train_iris.csv","Species");
		Object accuracy = rj.evaluate("resources/train_iris.csv", "resources/test_iris.csv","Species","2");
		System.out.println(accuracy);
		
		System.out.println("Fichier 2 : statsFSEVary ---------------------------------------");
		//Object modRF2 = rj.fit("resources/train_statsFSEVary.csv","nbPages");
		accuracy = rj.evaluate("resources/train_statsFSEVary.csv", "resources/test_statsFSEVary.csv","nbPages","1");
		System.out.println(accuracy);
		
		System.out.println("Fichier 3 : winequality ---------------------------------------");
		//Object modRF3 = rj.fit("resources/train_winequality.csv","quality");
		accuracy = rj.evaluate("resources/train_winequality.csv", "resources/test_winequality.csv","quality","1");
		System.out.println(accuracy);
	}

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
	
	// TODO paramètre de l'arbre à intégrer --> nombre d'arbres (ntree)
	public Object fit(String train, String y,String... otherArgs) {
		Object trainCSV = importer(train);
		Object modCart = null;
		
		String code = "library(randomForest)\n" +
				      "col <- which(colnames(data) ==\""+ y +"\")\n" +
					  "randomForest(y ~ ., data = data_train[c(-col)], ntree = nb_tree, mtry = 2, na.action = na.omit)";
		
		try {
			engine.put("nb_tree", Double.valueOf(otherArgs[0]));
			engine.put("data_train", trainCSV);
			engine.put("y", returnVar(trainCSV,y));
			modCart = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
				
		return modCart;
	}

	public Object evaluate(String train, String test, String y,String... otherArgs) {
		Object testCSV = importer(test);
		Object model=fit(train, y,otherArgs);
		Object accuracy=null;
		String code = "modpredRF=predict(mod,data_test,type=\"class\")\n" +  
				"modmatRF=table(y,modpredRF)\n" + 
				"modtaux_err_RF= sum(modpredRF != y)/nrow(data_test)\n" + 
				//"print(modtaux_err_RF)";
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
}