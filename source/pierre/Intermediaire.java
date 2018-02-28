package pierre;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import java.io.Serializable;

public class Intermediaire implements Serializable {

	private boolean d;
	private boolean b;
	private Set<String> values = new HashSet<String>();
	
	public Intermediaire(boolean b1, boolean b2, String value) {
		d = b1;
		b = b2;
		add(value);
	}
	
	public boolean isDouble() {
		return d;
	}
	
	public boolean isBoolean() {
		return b;
	}
	
	public void setBoolean(boolean b1) {
		b =b1;
	}
	public void setDouble(boolean b2) {
		d = b2;
	}
	
	public void add(String value) {
		values.add(value);
	}
	
	public Set<String> getValues() {
		return values;
	}
	
	public String toString() {
		String res = "isDouble : "+b+", isBoolean : "+b+", value : ";
		Iterator<String> it = values.iterator();
		while (it.hasNext()) {
			
			res = res + it.next()+",";
		}
		return res;
	}
}
