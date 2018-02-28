package pierre;
 
import java.io.Serializable;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

public class Convertisseur  implements Serializable{
	
	private MesVars cols;
	
	public MesVars getCols() {
		return cols;
	}


	public void cols(MesVars mesColonnes) {
		this.cols = mesColonnes;
	}
	
	public JavaRDD<LabeledPoint> convert(JavaRDD<List<String>> dataSplit){
		JavaRDD<LabeledPoint> data = dataSplit.map(new Function<List<String>, LabeledPoint>(){
			@Override
			public LabeledPoint call(List<String> ligne) {
				double[] vec = new double[cols.howManyVars()]; //a.nbX];
				int pos = 0;
				double monY = 0.0;
				for (int i = 0; i < ligne.size(); i++) {
					if (cols.getMesVars().get(i).getMonType() == MonType.Boolean) {
						if (ligne.get(i).equals("true")) {
							vec[i] = 1.0;
						}else {
							vec[i] = 0.0;
						}
					}
					if (cols.getMesVars().get(i).getMonType() == MonType.Double) {
						vec[i] = Double.parseDouble(ligne.get(i));
					}
					if (cols.getMesVars().get(i).getMonType() == MonType.Y) {
						monY = a.modasY.get(ligne.get(i));
					}
				}
				return new LabeledPoint(monY, Vectors.dense(vec));
			}
		});
		return data;
	}
}
