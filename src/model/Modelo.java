package model;

import java.util.ArrayList;

public class Modelo {

	private Produccion inicial;
	private ArrayList<Produccion> producciones;

	public Produccion getInicial() {
		return inicial;
	}

	public void setInicial(Produccion inicial) {
		this.inicial = inicial;
	}

	public ArrayList<Produccion> getProducciones() {
		return producciones;
	}

	public void setProducciones(ArrayList<Produccion> producciones) {
		this.producciones = producciones;
	}

	public Modelo() {
		inicial = null;
		producciones = new ArrayList<Produccion>();
	}

	/**
	 * Metodo que utiliza el algoritmo CYK para determinar si con la gramatica
	 * actual se puede generar una cadena. [ESTE METODO PROBABLEMENTE PUEDE
	 * OPTIMIZARSE, PERO FUNCIONA]
	 * 
	 * @param input - Cadena que se desea saber si la gramatica G genera.
	 * @return boolean que indica si la cadena input puede generarse con la
	 *         gramatica dada.
	 */
	public boolean CYK(String input) {
		// Paso 1: Se inicializa la matriz de conjuntos de terminales.
		int n = input.length();
		ArrayList<String>[][] matriz = new ArrayList[n][n];
		for (int a = 0; a < n; a++) {
			for (int b = 0; b < n; b++) {
				matriz[a][b] = new ArrayList<String>();
			}
		}

		// Para las posiciones de la matriz X_i.0, se adicionan los no terminales que
		// producen el valor i de la cadena.
		for (int i = 0; i < n; i++) {
			ArrayList<String> entry = new ArrayList<String>();
			for (int p = 0; p < producciones.size(); p++) {
				Produccion actual = producciones.get(p);
				if (actual.produce(input.charAt(i) + "")) {
					entry.add(actual.izq());
				}
			}
			matriz[i][0] = entry;
		}

		// Paso 3. BLABLA 3 FORS (Y se puede optimizar porque en este momento hace las
		// concatenaciones y las revisa cada una, incluso si eso significa revisar algo
		// dos veces y agregar la misma no terminal mas de una vez.
		for (int j = 1; j < n; j++) {
			for (int i = 0; i < n - j; i++) {
				for (int k = 0; k < j; k++) {
					ArrayList<String> x = concat(matriz[i][k], matriz[i + k + 1][j - k - 1]);
					if (!x.isEmpty()) {
						for (int p = 0; p < producciones.size(); p++) {
							Produccion actual = producciones.get(p);
							if (actual.produceAlgun(x)) {
								matriz[i][j].add(actual.izq());
							}
						}
					}
				}
			}
		}

		// Paso 4: Si el ultimo conjunto contiene al no terminal inicial (S), entonces
		// la gramatica puede generar la cadena. De lo contrario, no puede generarla.
		if (matriz[0][n - 1].contains(inicial.izq())) {
			return true;
		}

		return false;
	}

	public ArrayList<String> concat(ArrayList<String> a, ArrayList<String> b) {
		ArrayList<String> out = new ArrayList<String>();
		if (!a.isEmpty() && !b.isEmpty()) {
			for (int i = 0; i < a.size(); i++) {
				for (int j = 0; j < b.size(); j++) {
					String o = a.get(i) + b.get(j);
					if (!out.contains(o)) {
						out.add(o);
					}
				}
			}
		}
		return out;
	}

	public void addProduccion(String izq, String[] der) {
		Produccion p = new Produccion(izq, der);
		if (inicial == null) {
			inicial = p;
		}
		producciones.add(p);
	}

	public void reset() {
		inicial = null;
		producciones = new ArrayList<Produccion>();
	}

}
