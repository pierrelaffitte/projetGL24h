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
	
	public List<Variable> get() {
		return variables;
	}

	public void add(Variable t) {
		variables.add(t);
	}
	
	public List<String> colnames(){
		List<String> names = new ArrayList<String>();
		for (Variable var : variables) {
			names.add(var.getName());
		}
		return names;
	}
	
	public String toString() {
		String res = "";
		for (int i = 0; i < variables.size(); i++) {
			res = res + variables.get(i).toString()+"\n";
		}
		return res;
	}
	
	public int getVar(String name) {
		int res = 0;
		for (String nameVar : colnames()) {
			if (nameVar.equals(name)) {
				return res;
			}
			res++;
		}
		return res;
	}
	
	public int compteVarExplicatives(int n) {
		int compteur = 0;
		for (Variable var : variables) {
			if (var.getMonType() != MonType.Id & var.getPos() != n) {
				compteur++;
			}
		}
		return compteur;
	}
	
	public void check() {
		for (Variable var : variables) {
			var.fillMesModasRecodees();
			var.checkBool();
		}
	}
}
