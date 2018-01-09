package algorithmes;

import javax.script.*;

import org.renjin.script.*;

public class Renjin {
	
  public static void main(String[] args) throws Exception {
    // create a script engine manager:
    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
    // create a Renjin engine:
    ScriptEngine engine = factory.getScriptEngine();	
	
    // code R    
	engine.eval(new java.io.FileReader("resources/progR.R"));	
  }
}