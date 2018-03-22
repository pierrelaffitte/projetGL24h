package servlet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Scan des fichiers CSV
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class Scan {

	/**
	 * Lecture et stockage des en-têtes des fichiers dans une liste
	 * @param path chemin du fichier
	 * @param name nom du fichier
	 * @param delimiter delimiteur du fichier
	 * @return liste des variables
	 * @throws IOException exception de lecture
	 */
	public ArrayList<String> read(String path, String name, char delimiter) throws IOException{
		File file = new File(path + name + ".csv");
		FileReader fr = new FileReader(file);
		CSVReader cv = new CSVReader(fr,',');
		String[] f = cv.readNext();
		ArrayList<String> res = new ArrayList<String>();
		for (String var: f) {
			res.add(var);
		}
		System.out.println(res);
		return res;
	}
}
