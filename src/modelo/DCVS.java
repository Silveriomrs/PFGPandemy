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
	private Object[] cabecera;
	private Object datos[][];

	/**
	 * Constructor de la clase.
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
	 * @param c
	 */
	public void addCabecera(Object[] c) {this.cabecera = c;}
	
	/**
	 * Devuelve la el modelo da la JTable
	 * @return  DefaultTableModel modelo con los datos de la tabla.
	 */
	public DefaultTableModel getModelo() {return modelo;}
	
	/**
	 * @param datos
	 */
	public void setDatos(Object[][] datos) {this.datos = datos;}
	
	/**
	 * @param tableModel 
	 */
	public void setModelo(TableModel tableModel) {
		if(tableModel != null) {
			this.modelo = (DefaultTableModel) tableModel;
			int ncols = modelo.getColumnCount();
			this.cabecera = new Object[ncols];
			for(int i=0;i<ncols;i++) {
				this.cabecera[i] = modelo.getColumnName(i);
			}	
		}
		else {this.modelo = crearModelo();}

	}
	
	/**
	 * Función especial cuyo propósito es crear y devolver un modelo básico del tipo
	 * DefaultTableModel para ser usado por un JTable.
	 * @return instancia de DefaultTableModel básica con los datos importantes.
	 */
	private DefaultTableModel crearModelo() {
		this.modelo = new DefaultTableModel(datos,cabecera) {
			private static final long serialVersionUID = 5615251971828569240L;
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {Object.class, Object.class, Object.class, Object.class, Object.class};
			public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}
		};
		
		return modelo;
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
		//lectura cabecera
		for(int k=0; k<columnas ;k++) {
			texto += cabecera[k];
			if(k < columnas -1) {texto += ",";}
		}
		texto +="\n";
		//Lectura filas.
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
