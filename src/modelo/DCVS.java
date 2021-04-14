/**
 * Clase de almacenamiento de los datos en formato CVS,
 * Permite el manejo de la tabla a nivel lectura, edición y escritura de nuevos
 * datos.
 */
package modelo;

import java.util.ArrayList;

/**
 * @author Silverio Manuel Rosales Santana
 * @date 2021.04.12
 * @version 1.2
 *
 */
public class DCVS {
	private ArrayList<String> fila;
	private ArrayList<ArrayList<String>> tabla; 
	
	private int filas,columnas;

	/**
	 * Genera una tabla de n Filas por m Columnas, cabecera inclusive.
	 */
	public DCVS() {
		super();
		tabla = new ArrayList<ArrayList<String>>();
		fila = new ArrayList<String>();
		filas = 0;
	}

	/**
	 * Devuelve el número de filas de la tabla
	 * @return devuelve filas
	 */
	public int getFilas() {return tabla.size();}

	/**
	 * @param f 
	 * @return devuelve columnas
	 */
	public int getColumnas(int f) {return tabla.get(f).size();}

	/**
	 * @return devuelve cabecera
	 */
	public ArrayList<ArrayList<String>> getTabla() {
		return tabla;
	}

	/**
	 * @param f fila de la tabla
	 * @param c columna de la tabla
	 * @param numero el dato a establecer
	 */
	public void setDato(int f, int c, float numero) {tabla.get(f).set(c,"" + numero);}

	/**
	 * @param f fila de la tabla
	 * @param c columna de la tabla
	 * @return String devuelve dato en formato texto.
	 */
	public String getDato(int f, int c) {return tabla.get(f).get(c);}

	/**
	 * @param f fila de la tabla
	 * @param c columna de la tabla
	 * @param dato el dato a establecer
	 */
	public void setDato(int f, int c, String dato) {tabla.get(f).set(c,dato);}

	/**
	 * @param f fila a leer.
	 * @return devuelve fila
	 */
	public ArrayList<String> getFila(int f) {return tabla.get(f);}

	/**
	 * Añade una fila a la tabla.
	 * @param fila Fila a añadir
	 */
	public void addFila(ArrayList<String> fila) {
		tabla.add(fila);
		filas++;	
	}
	
	@Override
	public String toString() {
		String texto = "";
		for(int i=0; i<filas;i++) {
			ArrayList<String> f = getFila(i);
			for(int k=0; k<getColumnas(i) ;k++) {
				texto += f.get(k);
				if(k < getColumnas(i) -1) {texto += ",";}
				else {texto += "\n";}
			}		
		}
		return texto;
	}
	
}
