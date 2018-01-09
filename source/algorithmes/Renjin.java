package algorithmes;

import javax.script.*;

import org.renjin.script.*;

public class Renjin {
	
  public static void main(String[] args) throws Exception {
    // create a script engine manager:
    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
    // create a Renjin engine:
    ScriptEngine engine = factory.getScriptEngine();	
	
   // in2.getRecords()
    //engine.eval("library(\"rpart\")");
	engine.eval("data <- read.csv(\"resources/iris.csv\")");
	engine.eval("str(data)");
	engine.eval("adm_data<-as.data.frame(data)");
	engine.eval("tree <- rpart(Species ~ adm_data$Sepal.Length + adm_data$Sepal.Width+ adm_data$Petal.Length + adm_data$Sepal.Width,\n" + 
			" + data = adm_data,\n" + 
			" + method = \"class\")");
	engine.eval("plot(tree)");
	
	
  }
}