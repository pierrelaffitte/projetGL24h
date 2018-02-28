package pierre;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
public class MesVars implements Serializable{

	private List<MaVar> mesVars = new ArrayList<MaVar>();
	
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
		int res = 0;
		int compteur = 0;
		while (res < mesVars.size()) {
			if (mesVars.get(compteur).getMonType() == MonType.Id) {
				id --;
			}
			if (mesVars.get(compteur).getMonType() == MonType.Y) {
				res = compteur;
			}
		}
		return res - id;
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
}
