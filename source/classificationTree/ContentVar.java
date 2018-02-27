package classificationTree;

import java.util.HashSet;

public class ContentVar {

	private String name;
	HashSet<String> modalites = new HashSet<String>();
	private boolean d;
	private boolean b;
	private boolean isY;
	
	public void setDouble(boolean b) {
		this.d = b;
	}	
	public boolean isDouble() {
		return d;
	}	
	public void setBoolean(boolean b) {
		this.b = b;
	}	 
	public boolean isBoolean() {
		return b;
	}	
	public boolean isY() {
		return isY;
	}
	public void setY(boolean isY) {
		this.isY = isY;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void addModa(String value) {
		modalites.add(value);
	}
}
