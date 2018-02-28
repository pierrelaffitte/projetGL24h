package pierre;

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;
public class MaVar implements Serializable{

	private MonType type;
	private int pos;
	private String name;
	private Set<String> mesModas;
	
	public MaVar(MonType type, int pos,String name) {
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
		String res = type.toString() + Integer.toString(pos) + name + mesModas ;
		
		return res;
	}
	
}
