package algorithmes;

import javax.script.*;

import org.renjin.script.*;

public class Renjin implements algoInterface {

	public static void main(String[] args) throws Exception {
		// create a script engine manager:
		RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
		// create a Renjin engine:
		ScriptEngine engine = factory.getScriptEngine();	

		// code R    
		//engine.eval(new java.io.FileReader("resources/progR.R"));
		
		// Essai méthodes
		Renjin rj = new Renjin();
		Object data = rj.importer("resources/iris.csv");
		System.out.println(data.toString());
	}

	public Object importer(String file) {
		RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
		ScriptEngine engine = factory.getScriptEngine();	
		Object data = null;
		String code = "data <- read.csv(\""+ file +"\")";
		try {
			data = engine.eval(code);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return data;
	}

	public Object fit(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] split(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	public void evaluate(Object model, Object test) {
		// TODO Auto-generated method stub

	}
}