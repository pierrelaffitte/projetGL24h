package pierre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import java.io.Serializable;

public class MesVars implements Serializable{

	private List<MaVar> mesVars = new ArrayList<MaVar>();
	private HashMap<String, Integer> modasY;

	public List<MaVar> getMesVars() {
		return mesVars;
	}

	public void setMesVars(List<MaVar> mesVars) {
		this.mesVars = mesVars;
	}

	public void add(MaVar t) {
		mesVars.add(t);
	}

	public static void main(String[] args) throws Exception {
		MesVars a = new MesVars();
		MaVar t = new MaVar(MonType.Y,1,"y");
		t.add("setosa");
		System.out.println(t.getMesModas());
		a.add(t);
		System.out.println(a.getMesVars().get(0).getMesModas());
	}

	public String toString() {
		String res = "";
		for (int i = 0; i < mesVars.size(); i++) {
			res = res + mesVars.get(i).toString()+"\n";
		}
		return res;
	}

	public int varY() {
		int id = 0;
		int compteur = 0;
		while (compteur < mesVars.size()) {
			if (mesVars.get(compteur).getMonType() == MonType.Id) {
				id = compteur;
			}
		}
		return getY() - id;
	}

	public int getY() {
		int res = 0;
		int compteur = 0;
		while (compteur < mesVars.size()) {
			if (mesVars.get(compteur).getMonType() == MonType.Y) {
				res = compteur;
				compteur = mesVars.size();
			}
			compteur++;
		}
		return res;
	}

	public int howManyVars() {
		int compteur = 0;
		for (MaVar variable : mesVars) {
			if (variable.getMonType() == MonType.Boolean | variable.getMonType() == MonType.Double) {
				compteur++;
			}
		}
		return compteur;
	}

	public int knowNumbersOfClasses(MesVars cols) {
		return mesVars.get(getY()).getMesModas().size();
	}

	public void knowModalitesOfY() {
		modasY = new HashMap<String, Integer>();
		Iterator<String> it= mesVars.get(getY()).getMesModas().iterator();
		List<String> myList = Lists.newArrayList(it);
		Collections.sort(myList);
		int compteur = 0;
		for (String moda : myList) {
			modasY.put(moda, compteur);
			compteur++;
		}
	}


}
