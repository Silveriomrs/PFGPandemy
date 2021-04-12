/**
 * Clase de almacenamiento de los datos en formato CVS,
 * Permite el manejo de la tabla a nivel lectura, edición y escritura de nuevos
 * datos.
 */
package modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Silverio Manuel Rosales Santana
 * @date 2021.04.12
 * @version 1.0
 *
 */
public class DCVS {

	private List<List<String>> tabla = new ArrayList<List<String>>();
	private List<String> fila = new ArrayList<String>();

	/**
	 * Genera una tabla de n Filas por m Columnas, cabecera inclusive.
	 */
	public DCVS() {super();}

	/**
	 * Devuelve el número de filas de la tabla
	 * @return devuelve filas
	 */
	public int getFilas() {return tabla.get(0).size();}

	/**
	 * @return devuelve columnas
	 */
	public int getColumnas() {return tabla.size();}

	/**
	 * @return devuelve cabecera
	 */
	public List<List<String>> getTabla() {
		System.out.println(Arrays.deepToString(tabla.toArray()));
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
	public List<String> getFila(int f) {return tabla.get(f);}

	/**
	 * Añade una fila a la tabla.
	 * @param fila Fila a añadir
	 */
	public void addFila(List<String> fila) {tabla.add(fila);}
	
}
