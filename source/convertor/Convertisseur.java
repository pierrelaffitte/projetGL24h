package convertor;

import java.util.ArrayList;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

public class Convertisseur {
	public Scrawler scrawler = new Scrawler();
	public Header header;
	private Boolean headerOfData = false;
		
	public boolean isLoad() {
		return headerOfData;
	}

	public void setHeader(String train) {
		header = prepareHeader(readDatas(train));
		System.out.println(header.colnames());
		headerOfData = true;
	}


	public JavaRDD<List<String>> readDatas(String path){
		JavaRDD<String> linesData = scrawler.load(path);
		JavaRDD<List<String>> dataSplit = scrawler.splitCols(linesData);
		return dataSplit;
	}
	
	public JavaRDD<LabeledPoint> prepareData(JavaRDD<List<String>> data, String y) {
		ConvertIntoLabeledPoint c = new ConvertIntoLabeledPoint();
		JavaRDD<LabeledPoint> train = c.convert(header, data, y);
		return train;
	}
	
	public Header prepareHeader(JavaRDD<List<String>> data) {
		List<String> header = scrawler.getHeader(data);
		System.out.println(header);
		JavaRDD<List<String>> dataWithoutHeader = scrawler.removeHeader(data, header);
		JavaRDD<Row> temp = scrawler.getInfosFromData(dataWithoutHeader);
		Row ind = temp.collect().get(0);
		System.out.println(ind);
		Row temp2 = scrawler.reduceInfosFromData(temp);
		System.out.println(temp2);
		Header cols = scrawler.knowtypes(temp2, header);
		cols.check();
		return cols;
	}
	


}
