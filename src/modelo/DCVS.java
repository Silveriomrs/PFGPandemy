/**
 * Clase de almacenamiento de los datos en formato CVS,
 * Permite el manejo de la tabla a nivel lectura, edición y escritura de nuevos
 * datos.
 */
package modelo;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @author Silverio Manuel Rosales Santana
 * @date 2021.04.12
 * @version 2.7
 *
 */
public class DCVS implements TableModel{
	private DefaultTableModel modelo;
	private Object[] cabecera;
	private Object datos[][];

	/**
	 * Constructor de la clase. Recepciona un objeto de la clase DefaultTable Model
	 * y lo establece como su modelo por defecto. En caso de recepcionar 'null'
	 * establece este como modelo.
	 * @param modelo Objeto DefaultTableModel a establecer o null.
	 */
	public DCVS(DefaultTableModel modelo) {this.modelo = modelo;}

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
	 * @param f Fila a añadir al modelo.
	 * @return TRUE si ha tenido exito la operación, FALSE en otro caso.
	 */
	public boolean addFila(Object[] f) {
		boolean ok = false;
		if(modelo != null && f != null) {
			modelo.addRow(f);
			ok = true;
		}
		return ok;
	}
	
	/**
	 * Añade una columna nueva al modelo. Tanto al array de la cabecera
	 * como al modelo.
	 * @param nombre nombre de la columna.
	 */
	public void addColumna(String nombre) {
		//Hay que añadirlo al array de la cabecera.
		int n = cabecera.length;												//Obtenemos la longitud actual.
		Object[] c = new Object[n+1];											//Instancia para el nuevo tamaño.
		for(int i=0;i<n;i++) { c[i] = cabecera[i];}								//Copia de los datos.
		cabecera[n+1] = nombre;													//Añade el nuevo nombre al array cabecera.
		modelo.addColumn(nombre);												//Añade la nueva columna al modelo.
		cabecera = c;															//Actualiza la nueva cabecera.
	}
	
	/**
	 * Añade una nueva cabecera completa.
	 * @param c array de objetos que van a sustituir a la cabecera actual.
	 */
	public void addCabecera(Object[] c) {this.cabecera = c;}
	
	/**
	 * Devuelve la el modelo da la JTable
	 * @return  DefaultTableModel modelo con los datos de la tabla.
	 */
	public DefaultTableModel getModelo() {return modelo;}
	
	/**
	 * Establece una matriz de objetos como la matriz de datos de la instancia
	 * DCVS. Recibirá un array bidimensional de objetos.
	 * @param datos Array Bidimensional de datos.
	 */
	public void setDatos(Object[][] datos) {this.datos = datos;}
	
	/**
	 * Establece el modelo del TableModel.
	 * @param tableModel modelo de la tabla a establecer en el TableModel.
	 */
	public void setModelo(TableModel tableModel) {
		if(tableModel != null) {this.modelo = (DefaultTableModel) tableModel;}
	}
	
	/**
	 * Función de propósito,crear y devolver un modelo básico del tipo
	 * DefaultTableModel para ser usado por un JTable con los datos obtenidos
	 * mediante parámetro.
	 * @param lista Una lista de arrays de tipo String, List<String[]>.
	 * @return instancia de DefaultTableModel básica con los datos importantes.
	 * 		null en otro caso.
	 */
	public DefaultTableModel crearModelo(List<String[]> lista) {	
		if(lista != null) {
			int nfilas = lista.size();
			cabecera = lista.get(0);			
			datos = new Object[nfilas-1][cabecera.length];						//obtener número columnas.
			for(int i = 1; i<nfilas; i++) {	datos[i-1] = lista.get(i);}			//Componer tabla de datos.
			//Conformar modelo
			this.modelo = new DefaultTableModel(datos,cabecera) {		
				private static final long serialVersionUID = 5615251971828569240L;
				//será añadido cuando se concrete los tipos de datos de cada columna de la tabla
		//		@SuppressWarnings("rawtypes")
		//		Class[] columnTypes = new Class[] {Object.class, Object.class, Object.class, Object.class, Object.class};
		//		public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}
			};
		}
		return modelo;
	}
	
	/**
	 * Convierte los datos almenceados en el modelo al formato CVS, de esta
	 * forma estará lista para almacenar en un archivo en disco o imprimir.
	 */
	@Override
	public String toString() {
		int filas = getRowCount();
		int columnas = getColumnCount();
		String texto = "";
		//lectura cabecera
		for(int k=0; k<columnas ;k++) {
			//texto += cabecera[k];
			texto += modelo.getColumnName(k);
			if(k < columnas -1) {texto += ",";}
		}
		texto +="\n";
		//Lectura filas.
		for(int i=0; i<filas;i++) {
			for(int k=0; k<columnas ;k++) {
				texto += modelo.getValueAt(i,k);
				if(k < columnas -1) {texto += ",";}
				else if(i<(filas-1)){texto += "\n";}
			}
		}
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
