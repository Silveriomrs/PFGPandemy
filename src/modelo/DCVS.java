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
	 * Función que elimina las filas indicadas en un arreglo de números indice.
	 * @param filas arreglo de los indeces de filas a eliminar.
	 * @return done TRUE si se ha realizado con exito la operación,
	 * false en otro caso.
	 */
	public boolean delFilas(int[] filas) {
		int nfilas = filas.length;
		boolean done = false;
		if (nfilas > 0) {
			for(int i = (nfilas-1); i >= 0; i--) {
				modelo.removeRow(filas[i]);
			}
			done = true;
		}
		return done;
	}
	
	/**
	 * Elimina las columnas indicadas en un arreglo con los indices de las
	 * columnas a eliminar del modelo.
	 * @param cols arreglo de los indices de comlumnas a eliminar.
	 * @return modelo resultante de eliminar las columnas indicadas.
	 */
	public DefaultTableModel delColumnas(int[] cols) {
		int ncols = cols.length;												//Número de columnas a borrar.
		int ncols2 = modelo.getColumnCount();									//Número de columnas en origen.
		int nfilas = getRowCount();
		Object datos[][] = new Object[nfilas][ncols2 - ncols];
		Object cabecera[] = new Object[ncols2 - ncols];
		int contCols = 0;
		if (ncols > 0) {	
			for(int j = 0; j<ncols2; j++) {
				if(!isListed(cols,j)) {
					//Copiar cabecera.
					cabecera[contCols] = modelo.getColumnName(j);
					//Copiar los datos.
					for(int i=0; i<nfilas;i++) {
						datos[i][contCols] = modelo.getValueAt(i,j);	
					}
					contCols++;
				}
			}
			this.cabecera = cabecera;
			this.datos = datos;
			modelo = new DefaultTableModel(datos,cabecera);
		}
		return modelo;
	}
	
	/**
	 * Busca un número dado dentro de un arreglo de números tipo int. Devolviendo
	 * el resultado de la operación de búsqueda como un boolean.
	 * @param lista Arreglo de los datos.
	 * @param num Número que debe ser encontrado dentro del arreglo.
	 * @return True en caso de contener el número, false en otro caso.
	 */
	private boolean isListed(int[] lista, int num) {
		boolean encontrado = false;
		int cont = 0;
		while(!encontrado && cont<lista.length) {
			if(lista[cont] == num) {encontrado = true;}
			else {cont++;}
		}
		return encontrado;
	}
	
	
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
			for(int i = 1; i<nfilas; i++) {datos[i-1] = lista.get(i);}			//Componer tabla de datos.	
			this.modelo = new DefaultTableModel(datos,cabecera);				//Conformar modelo
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
				String valor = "" + modelo.getValueAt(i,k);						//Obtención del valor convertido a texto
				if(!valor.equalsIgnoreCase("null")) {texto += valor;}			//Comparación si el texto almacenado es null.
				else {valor = "";}												//En tal caso sustituye por cadena vacia.
				if(k < columnas -1) {texto += ",";}								//Separador de campos.
				else if(i<(filas-1)){texto += "\n";}							//Salta a la siguiente fila de datos.
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
