package algorithmes;

import javax.script.*;

import org.renjin.script.*;

public class Renjin implements algoInterface {

	private static RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	private static ScriptEngine engine = factory.getScriptEngine();	
	
	public static void main(String[] args) throws Exception {
		// code R    
		//engine.eval(new java.io.FileReader("resources/progR.R"));
		
		// Essai méthodes d'arbres de classification
		Renjin rj = new Renjin();
		
		System.out.println("Fichier 1 : iris ---------------------------------------");
		Object modCart = rj.fit("resources/train_iris.csv","Species");
		rj.evaluate(modCart, "resources/test_iris.csv","Species");
		
		System.out.println("Fichier 2 : statsFSEVary ---------------------------------------");
		Object modCart2 = rj.fit("resources/train_statsFSEVary.csv","ACK");
		rj.evaluate(modCart2, "resources/test_statsFSEVary.csv","ACK");
		
		//Class objectType = modCart.getClass();
		//System.out.println("Java class of 'res' is: " + objectType.getName());
		//System.out.println("In R, typeof(res) would give '" + modCart.getTypeName() + "'");
		//System.out.println(modCart);
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
	
	public Object fit(String train, String y) {
		Object trainCSV = importer(train);
		Object modCart = null;
		
		String code = "library(rpart)\n" +
					  "rpart(y~.,data_train, method=\"class\",parms=list( split='gini'))";
		
		try {
			engine.put("data_train", trainCSV);
			engine.put("y", returnVar(trainCSV,y));
			modCart = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
				
		return modCart;
	}

	public void evaluate(Object model, String test, String y) {
		Object testCSV = importer(test);
		String code = "modpredCART=predict(mod,data_test,type=\"class\")\n" +  
				"modmatCART=table(y,modpredCART)\n" + 
				"modtaux_err_CART= sum(modpredCART!= y)/nrow(data_test)\n" + 
				"print(modtaux_err_CART)";
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