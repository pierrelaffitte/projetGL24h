package algorithmes;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;

import javax.script.*;
import org.renjin.script.*;
import org.apache.commons.csv.*;

// ... add additional imports here ...

public class Renjin {
	
  public static void main(String[] args) throws Exception {
    // create a script engine manager:
    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
    // create a Renjin engine:
    ScriptEngine engine = factory.getScriptEngine();

    // lire un fichier csv
    Reader in = null;
	try {
		in = new FileReader("resources/iris.csv");
	} catch (FileNotFoundException e) {
		System.out.println("Fichier pas trouvé");
	}
	CSVParser in2 = CSVFormat.DEFAULT.withHeader().parse(in);
	Map<String, Integer> header = in2.getHeaderMap();
	System.out.println(header);	
   // in2.getRecords()
    
    //engine.eval("df <- data.frame(x=1:10, y=(1:10)+rnorm(n=10))");
   // engine.eval("print(df)");
    //engine.eval("print(lm(y ~ x, df))");
  }
}