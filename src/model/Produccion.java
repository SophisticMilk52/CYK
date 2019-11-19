package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Produccion {
	
	
	private String izq;
	private ArrayList<String> der;
	
	public Produccion(String izq, String[] der) {
		this.izq = izq;
		this.der = new ArrayList<String>(Arrays.asList(der));
	}
	
	public boolean produce(String in) {
		if(der.contains(in)) {
			return true;
		}
		return false;
	}
	
	public boolean produceAlgun(ArrayList<String> in) {
		for(int i=0; i< in.size(); i++) {
			if (produce(in.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public String izq() {
		return izq;
	}
}
