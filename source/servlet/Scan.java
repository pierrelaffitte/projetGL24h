package servlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

public class Scan {
;
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
