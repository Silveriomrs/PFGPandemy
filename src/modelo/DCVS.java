/**
 * Clase de almacenamiento de los datos en formato CVS,
 * Permite el manejo de la tabla a nivel lectura, edición y escritura de nuevos
 * datos.
 */
package modelo;

import javax.swing.table.DefaultTableModel;

/**
 * @author Silverio Manuel Rosales Santana
 * @date 2021.04.12
 * @version 2.1
 *
 */
public class DCVS {
	private DefaultTableModel modelo;

	/**
	 * Genera una tabla de n Filas por m Columnas, cabecera inclusive.
	 */
	public DCVS() {
		modelo = new DefaultTableModel();
	}
	
	//public DCVS() {}

	/**
	 * Devuelve el número de filas de la tabla.
	 * @return devuelve filas
	 */
	public int getNFilas() {return modelo.getRowCount();}

	/**
	 * @return devuelve el número de columnas del modelo
	 */
	public int getNColumnas() {return modelo.getColumnCount();}

	/**
	 * Función para obtener el dato de una celda.
	 * @param f fila de la tabla
	 * @param c columna de la tabla
	 * @return Object devuelve dato en formato texto.
	 */
	public Object getDato(int f, int c) {return modelo.getValueAt(f, c);}

	/**
	 * Establece el valor de una celda por un nuevo valor tipo String.
	 * @param f fila de la tabla
	 * @param c columna de la tabla
	 * @param dato el dato a establecer
	 */
	public void setDato(int f, int c, String dato) {this.modelo.setValueAt(dato, f, c);}

	/**
	 * Devuelve la fila referenciada.
	 * @param f fila a leer.
	 * @return devuelve un array Object[] con los datos de una fila
	 */
	public Object[] getFila(int f) {
		int columnas = getNColumnas();
		Object[] fila = new Object[columnas];
		for(int i = 0; i<columnas;i++) {
			fila[i] = modelo.getValueAt(f, i);
		}
		return fila;	
	}

	/**
	 * Añade una nueva fila a la tabla.
	 * Añade una fila a la tabla.
	 * @param f Fila a añadir
	 */
	public void addFila(Object[] f) {modelo.addRow(f);}
	
	/**
	 * Devuelve la el modelo da la JTable
	 * @return  DefaultTableModel modelo con los datos de la tabla.
	 */
	public DefaultTableModel getModelo() {return modelo;}
	
	/**
	 * @return True en caso de realizar la conversión, False en otro caso
	 */
	public boolean setModelo() {
		this.modelo = convToModelo();
		return true;
	}
	
	private DefaultTableModel convToModelo() {
		DefaultTableModel m = new DefaultTableModel();
		int columnas = getNColumnas();
		int filas = getNFilas();
		Object[] fila = new Object[columnas];
		//portar datos	
		 if(columnas > 0) {
		     for(int i=0; i< filas; i++) {
		         for(int j = 0; j < columnas; j++) {
		             fila[j] = getDato(i, j);
		         }
		         m.addRow(fila);												//Agrega la fila al TableModel de la tabla de destino
		     }
		 }
		return m;
	}
	
	/**
	 * Método sobreescrito, da el formato correcto a la información contenida
	 * en la tabla.
	 */
	@Override
	public String toString() {
		int filas = getNFilas();
		int columnas = getNColumnas();
		String texto = "";
		
		for(int i=0; i<filas;i++) {
			for(int k=0; k<columnas ;k++) {
				texto += modelo.getValueAt(i,k);
				if(k < columnas -1) {texto += ",";}
				else {texto += "\n";}
			}
		}
		System.out.println(texto);
		return texto;
	}
	
}
