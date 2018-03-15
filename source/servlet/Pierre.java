package servlet;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Pierre {
	
	public static void main(String[] args) throws IOException {
		Scan s = new Scan();
		s.read("/home/pierre/Téléchargements/", "iris22", ',');
	}

}
