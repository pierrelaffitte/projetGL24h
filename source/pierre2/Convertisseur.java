package pierre2;
 
import java.io.Serializable;

public class Convertisseur  implements Serializable{
	
	private Header cols;
	
	public Header getCols() {
		return cols;
	}

	public void cols(Header mesColonnes) {
		this.cols = mesColonnes;
	}
	
}
