package pierre2;

import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.LabeledPoint;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scrawler a = new Scrawler();
		JavaRDD<String> linesData = a.load("resources/statsFSEVary.csv");
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
		
		JavaRDD<List<String>> data = a.splitCols(a.load("resources/train_statsFSEVary.csv"));
		data = a.removeHeader(data, header);
		Convertisseur c = new Convertisseur();
		JavaRDD<LabeledPoint> train = c.convert(cols, data, "sizePDF");
		
		System.out.println(train.collect().get(0));
		//System.out.println(train.collect().get(0));
		
	}

}
