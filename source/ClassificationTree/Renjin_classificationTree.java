package ClassificationTree;

import javax.script.*;

import org.renjin.script.*;

import interfaces.algoInterface;

public class Renjin_classificationTree implements algoInterface {

	private static RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	private static ScriptEngine engine = factory.getScriptEngine();	
	
	public static void main(String[] args) throws Exception {
		// code R    
		//engine.eval(new java.io.FileReader("resources/progR.R"));
		
		// Essai méthodes d'arbres de classification
		Renjin_classificationTree rj = new Renjin_classificationTree();
		
		/*List<String> x = new ArrayList<String>();
		x.add("Sepal.Length");
		x.add("Sepal.Width");
		x.add("Petal.Length");
		x.add("Petal.Width");
		*/
		
		System.out.println("Fichier 1 : iris ---------------------------------------");
		rj.evaluate("resources/train_iris.csv", "resources/test_iris.csv","Species");
		System.out.println("");
		
		System.out.println("Fichier 2 : statsFSEVary ---------------------------------------");
		rj.evaluate("resources/train_statsFSEVary.csv", "resources/test_statsFSEVary.csv","nbPages");
		System.out.println("");
		
		
		System.out.println("Fichier 3 : winequality ---------------------------------------");
		rj.evaluate("resources/train_winequality.csv", "resources/test_winequality.csv","quality");
		System.out.println("");
		
		System.out.println("Fichier 4 : mushrooms ---------------------------------------");
		rj.evaluate("resources/train_mushrooms.csv", "resources/test_mushrooms.csv","class");
		System.out.println("");
		
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
	
		//SEXP va = (SEXP) variable;
		//System.out.println(va);
		
		return variable;
	}
	
	//  TODO : variables explicatives, poids, split=deviance
	public Object fit(String train, String y,String... otherArgs) {
		Object trainCSV = importer(train);
		Object modCart = null;
		
		String code = "library(rpart)\n" +
					  "col <- which(colnames(data) ==\""+ y +"\")\n" +
					  "rpart(y~.,data_train[c(-col)], method=\"class\",parms=list( split='gini'))";
		
		try {
			engine.put("data_train", trainCSV);
			engine.put("y", returnVar(trainCSV,y));
			modCart = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
				
		return modCart;
	}

	public Object evaluate(String train, String test, String y,String... otherArgs) {
		Object model=fit(train, y);
		Object testCSV = importer(test);
		Object accuracy= null;
		String code = "modpredCART=predict(mod,data_test,type=\"class\")\n" +  
				"modmatCART=table(y,modpredCART)\n"+
				"modtaux_err_CART= sum(modpredCART!= y)/nrow(data_test)\n" +
				//"cat(\"Taux de mal classés :\",modtaux_err_CART)";
				//"accuracy <- sum(diag(modmatCART))/sum(modmatCART)\n" + 
				"accuracy <- 1-modtaux_err_CART\n";
		try {
			engine.put("data_test", testCSV);
			engine.put("mod", model);
			engine.put("y", returnVar(testCSV, y));
			accuracy= engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return accuracy;
	}

}