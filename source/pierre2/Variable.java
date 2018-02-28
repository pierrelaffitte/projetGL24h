package pierre2;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import java.io.Serializable;
public class Variable implements Serializable{

	private MonType type;
	private int pos;
	private String name;
	private Set<String> mesModas;
	private HashMap<String, Integer> mesModasRecodees;
	private int nbModas;
	
	public Variable(MonType type, int pos,String name) {
		this.name = name;
		this.pos = pos;
		this.type = type;
		mesModas = new HashSet<String>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public MonType getMonType() {
		return type;
	}
	public void setMonType(MonType monType) {
		this.type = monType;
	}
	public Set<String> getMesModas() {
		return mesModas;
	}
	public void setMesModas(Set<String> mesModas) {
		this.mesModas = mesModas;
	}
	public void add(String moda) {
		this.mesModas.add(moda);
	}
	
	public String toString() {
		String res = name+" : "+ type.toString() +", " + Integer.toString(pos);
		if(type == MonType.Y) {
			res = res + ", "+ mesModas ;
		}
		return res;
	}

	public HashMap<String, Integer> getMesModasRecodees() {
		return mesModasRecodees;
	}

	public void fillMesModasRecodees() {
		mesModasRecodees = new HashMap<String, Integer>();
		Iterator<String> it= getMesModas().iterator();
		List<String> myList = Lists.newArrayList(it);
		Collections.sort(myList);
		int compteur = 0;
		for (String moda : myList) {
			mesModasRecodees.put(moda, compteur);
			compteur++;
		}
	}

	public int getNbModas() {
		return nbModas;
	}

	public void setNbModas() {
		this.nbModas = mesModas.size();
	}
	
}
