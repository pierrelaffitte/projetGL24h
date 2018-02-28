package pierre2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import java.io.Serializable;

public class Header implements Serializable{

	private List<Variable> variables = new ArrayList<Variable>();
	private HashMap<String, Integer> modasY;
	private List<String> colnames;
	
	public List<Variable> get() {
		return variables;
	}

	public void add(Variable t) {
		variables.add(t);
	}

	public static void main(String[] args) throws Exception {
		Header a = new Header();
		Variable t = new Variable(MonType.Y,1,"y");
		t.add("setosa");
		System.out.println(t.getMesModas());
		a.add(t);
		System.out.println(a.get().get(0).getMesModas());
	}

	public String toString() {
		String res = "";
		for (int i = 0; i < variables.size(); i++) {
			res = res + variables.get(i).toString()+"\n";
		}
		return res;
	}

	public int varY() {
		int id = 0;
		int compteur = 0;
		while (compteur < variables.size()) {
			if (variables.get(compteur).getMonType() == MonType.Id) {
				id = compteur;
			}
		}
		return getY() - id;
	}

	public int getY() {
		int res = 0;
		int compteur = 0;
		while (compteur < variables.size()) {
			if (variables.get(compteur).getMonType() == MonType.Y) {
				res = compteur;
				compteur = variables.size();
			}
			compteur++;
		}
		return res;
	}

	public int howManyVars() {
		int compteur = 0;
		for (Variable variable : variables) {
			if (variable.getMonType() == MonType.Boolean | variable.getMonType() == MonType.Double) {
				compteur++;
			}
		}
		return compteur;
	}

	public int knowNumbersOfClasses(Header cols) {
		return variables.get(getY()).getMesModas().size();
	}

	public void knowModalitesOfY() {
		modasY = new HashMap<String, Integer>();
		Iterator<String> it= variables.get(getY()).getMesModas().iterator();
		List<String> myList = Lists.newArrayList(it);
		Collections.sort(myList);
		int compteur = 0;
		for (String moda : myList) {
			modasY.put(moda, compteur);
			compteur++;
		}
	}

	public List<String> getColnames() {
		return colnames;
	}

	public void setColnames() {
		this.colnames = new ArrayList<String>();
		for (Variable var : variables){
			colnames.add(var.getName());
		}
	}


}
