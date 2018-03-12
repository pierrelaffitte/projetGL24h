package convertor;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

public class ConvertIntoLabeledPoint  implements Serializable{

	public static JavaRDD<LabeledPoint> convert(Header header, JavaRDD<List<String>> dataToConvert, String y) {
		int varY = header.getVar(y);
		int nb =header.compteVarExplicatives(varY);
		System.out.println("colY : "+varY+", nb var explicatives : "+nb);
		return dataToConvert.map(new Function<List<String>, LabeledPoint>(){
			@Override
			public LabeledPoint call(List<String> row) {

				double valY = 0.0;
				//header.get().get(varY).fillMesModasRecodees();
				double[] varExplicatives = new double[nb];
				int pos = 0; 
				for (int colonne = 0; colonne < row.size(); colonne ++) {
					if (colonne == varY) {
						try {
							valY = ConvertIntoLabeledPoint.convertValue(row.get(colonne), header.get().get(colonne), varY);	
						}catch(Exception e) {
							System.out.println(header.get().get(colonne).getMesModasRecodees());
							System.out.println("varY : "+varY+",variable : "+row.get(colonne)+", colonne :"+colonne);
						}
					}else {
						if(colonne != 0){
							try {
								varExplicatives[pos] = ConvertIntoLabeledPoint.convertValue(row.get(colonne), header.get().get(colonne), varY);;
								pos++;
							}catch(Exception e) {
								System.out.println("varY : "+varY+",variable : "+row.get(colonne)+", pos : "+pos+", colonne :"+colonne);
							}
						}
					}

				}
				return new LabeledPoint(valY, Vectors.dense(varExplicatives));

			}
		});
	}

	public static Double convertValue(String value, Variable var, int varY) {
		if (varY == var.getPos()) {
			return var.getMesModasRecodees().get(value);
		}else {
			if (var.getMonType() == MonType.Boolean) {
				if (value.equals("true")) {
					return 1.0;
				}else {
					return 0.0;
				}
			}
			if (var.getMonType() == MonType.Double) {
				return Double.parseDouble(value);
			}
			if (var.getMonType() == MonType.Qualitative) {
				return var.getMesModasRecodees().get(value);
			} 
		}
		return 0.0;

	}

}