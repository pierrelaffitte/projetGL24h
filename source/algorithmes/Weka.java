package algorithmes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Weka {

	 public static void main(String[] args) throws Exception {
		DataSource source = new DataSource("resources/iris.csv");
		Instances data = source.getDataSet();
		for (int i =0; i < data.size(); i++ ) {
			System.out.println(data.get(i));
		}
	}

}
