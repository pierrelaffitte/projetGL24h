package classificationTree;

import javax.script.*;

import org.renjin.script.*;

import interfaces.Implementation;

/**
 * Algorithme d'arbres de classification en Renjin
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class Renjin_CT implements Implementation {

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

	@Override
	public Object evaluate(String train, String test, String y,String... otherArgs) {
		Object model=fit(train, y);
		Object testCSV = importer(test);
		Object accuracy= null;
		String code = "modpredCART=predict(mod,data_test,type=\"class\")\n" +  
				"modmatCART=table(y,modpredCART)\n"+
				"modtaux_err_CART= sum(modpredCART!= y)/nrow(data_test)\n" + 
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