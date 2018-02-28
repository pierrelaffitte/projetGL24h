package pierre2;

import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.LabeledPoint;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String file = "iris";
		String y = "\"Species\"";
		Scrawler a = new Scrawler();
		JavaRDD<String> linesData = a.load("resources/"+file+".csv");
		JavaRDD<List<String>> dataSplit = a.splitCols(linesData);
		
		System.out.println(dataSplit.collect().get(0));
		List<String> header = a.getHeader(dataSplit);
		System.out.println(header);
		
		JavaRDD<List<String>> dataWithoutHeader = a.removeHeader(dataSplit, a.getHeader(dataSplit));
		System.out.println(dataWithoutHeader.collect().get(0));
		
		JavaRDD<Row> temp = a.getInfosFromData(dataWithoutHeader);
		Row ind = temp.collect().get(0);
		System.out.println(ind);

		Row temp2 = a.reduceInfosFromData(temp);
		System.out.println(temp2);

		Header cols = a.knowtypes(temp2, header);
		cols.check();
		System.out.println("cols = \n "+cols);
		for (Variable var : cols.get()){
			System.out.println(var.getMesModasRecodees());
		}
		
		
		JavaRDD<List<String>> data = a.splitCols(a.load("resources/train_"+file+".csv"));
		List<String> headerTrain = a.getHeader(data);
		data = a.removeHeader2(data);
		System.out.println("premiere ligne sans l'entete : "+data.collect().get(0));
		Convertisseur c = new Convertisseur();
		//System.out.println(cols.colnames().get(1));
		JavaRDD<LabeledPoint> train = c.convert(cols, data, y);
		
		System.out.println(train.collect().get(0));
		//System.out.println(train.collect().get(0));
		
	}

}
