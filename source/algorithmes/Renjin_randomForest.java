package algorithmes;

import javax.script.*;

import org.renjin.script.*;

public class Renjin_randomForest implements algoInterface {

	private static RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	@SuppressWarnings("restriction")
	private static ScriptEngine engine = factory.getScriptEngine();	
	
	public static void main(String[] args) throws Exception {
		// code R    
		//engine.eval(new java.io.FileReader("resources/progR.R"));
		
		// Essai méthodes d'arbres de classification
		Renjin_randomForest rj = new Renjin_randomForest();
		
		System.out.println("Fichier 1 : iris ---------------------------------------");
		Object modRF = rj.fit("resources/train_iris.csv","Species");
		rj.evaluate(modRF, "resources/test_iris.csv","Species");
		
		System.out.println("Fichier 2 : statsFSEVary ---------------------------------------");
		Object modRF2 = rj.fit("resources/train_statsFSEVary.csv","nbPages");
		rj.evaluate(modRF2, "resources/test_statsFSEVary.csv","nbPages");
		
		System.out.println("Fichier 3 : winequality ---------------------------------------");
		Object modRF3 = rj.fit("resources/train_winequality.csv","quality");
		rj.evaluate(modRF3, "resources/test_winequality.csv","quality");
		
	}

	@SuppressWarnings("restriction")
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

	@SuppressWarnings("restriction")
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
	@SuppressWarnings("restriction")
	public Object fit(String train, String y) {
		Object trainCSV = importer(train);
		Object modCart = null;
		
		String code = "library(randomForest)\n" +
					  "randomForest(y ~ ., data = data_train, ntree = 5, mtry = 2, na.action = na.omit)";
		
		try {
			engine.put("data_train", trainCSV);
			engine.put("y", returnVar(trainCSV,y));
			modCart = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
				
		return modCart;
	}

	@SuppressWarnings("restriction")
	public void evaluate(Object model, String test, String y) {
		Object testCSV = importer(test);
		String code = "modpredRF=predict(mod,data_test,type=\"class\")\n" +  
				//"modmatRF=table(y,modpredRF)\n" + 
				"modtaux_err_RF= sum(modpredRF != y)/nrow(data_test)\n" + 
				"print(modtaux_err_RF)";
		try {
			engine.put("data_test", testCSV);
			engine.put("mod", model);
			engine.put("y", returnVar(testCSV, y));
			engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
	}
}