/**
 * Clase de almacenamiento de los datos en formato CVS,
 * Permite el manejo de la tabla a nivel lectura, edición y escritura de nuevos
 * datos.
 */
package modelo;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @author Silverio Manuel Rosales Santana
 * @date 2021.04.12
 * @version 2.1
 *
 */
public class DCVS implements TableModel{
	private DefaultTableModel modelo;
	private Class[] clases;
	private Object[] nombresColumnas;
	private Object[] columaTipos;
	private Object datos[][];

	/**
	 * Genera una tabla de n Filas por m Columnas, cabecera inclusive.
	 */
	public DCVS() {modelo = new DefaultTableModel();}

	/**
	 * Devuelve la fila referenciada.
	 * @param f fila a leer.
	 * @return devuelve un array Object[] con los datos de una fila
	 */
	public Object[] getFila(int f) {
		int columnas = getColumnCount();
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
	 * @param tableModel 
	 * @return True en caso de realizar la conversión, False en otro caso
	 */
	public boolean setModelo(TableModel tableModel) {
		this.modelo = (DefaultTableModel) tableModel;
		return true;
	}
	
	private DefaultTableModel crearModelo() {
		DefaultTableModel m;
		int columnas = getColumnCount();
		int filas = getRowCount();
		
		Object[][] datos = new Object[columnas][columnas];
		//portar datos	
		 if(columnas > 0) {
		     for(int i=0; i< filas; i++) {
		         for(int j = 0; j < columnas; j++) {
		             datos[i][j] = getValueAt(i, j);
		         }
		     }
		 }
		 this.datos = datos;
		 //Falta la cabecera.
		 
		 //Falta el tipo de datos.
		 
		 
		/**Refinar **/ 
		m = new DefaultTableModel(datos,nombresColumnas) {
			private static final long serialVersionUID = 5615251971828569240L;
			Class[] columnTypes = new Class[] {Object.class, Object.class, Object.class, Object.class, Object.class};
			public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}
		};
		
		return m;
	}
	
	/**
	 * Método sobreescrito, da el formato correcto a la información contenida
	 * en la tabla.
	 */
	@Override
	public String toString() {
		int filas = getRowCount();
		int columnas = getColumnCount();
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

	@Override
	public void addTableModelListener(TableModelListener arg0) {modelo.addTableModelListener(arg0);}

	@Override
	public Class<?> getColumnClass(int arg0) {return modelo.getColumnClass(arg0);}

	@Override
	public int getColumnCount() {return modelo.getColumnCount();}

	@Override
	public String getColumnName(int arg0) {return modelo.getColumnName(arg0);}

	@Override
	public int getRowCount() {return modelo.getRowCount();}

	@Override
	public Object getValueAt(int arg0, int arg1) {return modelo.getValueAt(arg0, arg1);}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {return modelo.isCellEditable(arg0, arg1);}

	@Override
	public void removeTableModelListener(TableModelListener arg0) { modelo.removeTableModelListener(arg0);}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {this.modelo.setValueAt(arg0, arg1, arg2);}
	
}
