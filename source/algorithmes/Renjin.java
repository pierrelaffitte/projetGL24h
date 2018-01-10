package algorithmes;

import javax.script.*;

import org.renjin.script.*;

public class Renjin implements algoInterface {

	private static RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	private static ScriptEngine engine = factory.getScriptEngine();	
	
	public static void main(String[] args) throws Exception {
		// code R    
		//engine.eval(new java.io.FileReader("resources/progR.R"));
		
		// Essai méthodes
		Renjin rj = new Renjin();
		Object modCart = rj.fit("resources/train_iris.csv");
		rj.evaluate(modCart, "resources/test_iris.csv");
		
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

	public Object fit(String train) {
		Object trainCSV = importer(train);
		Object modCart = null;
		
		String code = "library(rpart)\n" +
					  "rpart(Species~.,data_train, method=\"class\",parms=list( split='gini'))";
		
		try {
			engine.put("data_train", trainCSV);
			modCart = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
				
		return modCart;
	}

	public void evaluate(Object model, String test) {
		Object testCSV = importer(test);
		String code = "modpredCART=predict(mod,data_test,type=\"class\")\n" +  
				"modmatCART=table(data_test$Species,modpredCART)\n" + 
				"modtaux_err_CART= sum(modpredCART!= data_test$Species)/nrow(data_test)\n" + 
				"print(modtaux_err_CART)";
		try {
			engine.put("data_test", testCSV);
			engine.put("mod", model);
			engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		
	}
}