package convertor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import java.io.Serializable;

/**
 * Classe qui contient la structure des données
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public class Header implements Serializable{

	private List<Variable> variables = new ArrayList<Variable>();
	
	public List<Variable> get() {
		return variables;
	}
	
	/**
	 * ajoute la variable t dans le Header
	 * @param t la variable à ajouter
	 */
	public void add(Variable t) {
		variables.add(t);
	}
	
	/**
	 * renvoie la liste des noms des colonnes
	 * @return la liste des noms de colonne
	 */
	public List<String> colnames(){
		List<String> names = new ArrayList<String>();
		for (Variable var : variables) {
			names.add(var.getName().replaceAll("\"", ""));
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
	
	/**
	 * renvoie le numéro de la colonne de la variable reçue
	 * @param name le nom de la variable
	 * @return le numéro de la colonne
	 */
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
	
	/**
	 * compte le nombre de variables explicatives en écartant la variable d'intérêt
	 * @param n numéro de la colonne de la variable d'intêret
	 * @return le nombre de variables explicatives
	 */
	public int compteVarExplicatives(int n) {
		int compteur = 0;
		for (Variable var : variables) {
			if (var.getMonType() != MonType.Id & var.getPos() != n) {
				compteur++;
			}
		}
		return compteur;
	}
	
	/**
	 * vérifie pour chaque variable si son type correspond bien à celui déterminer de manière automatique
	 */
	public void check() {
		for (Variable var : variables) {
			var.checkBool();
			Set<String> mesModasCorrigees = new HashSet<String>();
			for (String moda : var.getMesModas()) {
				mesModasCorrigees.add(moda.replaceAll("\"", ""));
			}
			var.setMesModas(mesModasCorrigees);
			var.fillMesModasRecodees();
		}
	}
}
