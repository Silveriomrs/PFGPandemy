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
 * @version 2.8
 *
 */
public class DCVS implements TableModel{
	private DefaultTableModel modelo;
	private String ruta;
	private String directorio;
	private String nombre;
	private String tipo;
	private String date;
	private boolean editLabels;
	private Object[] cabecera;
	private Object datos[][];
	
	/**
	 * <p>Title: Constructor básico</p>  
	 * <p>Description: crea un nuevo modelo sin filas ni columnas</p>
	 * Su ruta es null y su tipo de datos vacio.  
	 */
	public DCVS() {
		super();
		setEditLabels(true);
		ruta = null;
		tipo = "";
		nuevoModelo();
	}

	
	/**
	 * Elimina los datos de la fila referenciada.
	 * @param f fila a leer.
	 */
	public void clearRow(int f) {
		int columnas = getColumnCount();
		for(int i = 0; i<columnas;i++) {
			modelo.setValueAt(null,f, i);
		}
	}
	
	/**
	 * Devuelve la fila referenciada.
	 * @param f fila a leer.
	 * @return devuelve un array Object[] con los datos de una fila
	 */
	public String[] getRow(int f) {
		int columnas = getColumnCount();
		String[] fila = new String[columnas];
		for(int i = 0; i<columnas;i++) {
			fila[i] = (String) modelo.getValueAt(f, i);
		}
		return fila;
	}

	/**
	 * Añade una nueva fila a la tabla. Si la función recibe un array de datos
	 *  este será usado para añadir a la nueva fila, en otro caso crea una fila vacia
	 *   con referencias null en cada columna de la fila.
	 * @param f Array de objetos de la fila a añadir al modelo. NULL para añadir una fila
	 *  vacia.
	 * @return TRUE si ha tenido exito la operación, FALSE en otro caso.
	 */
	public boolean addFila(Object[] f) {
		boolean ok = false;
		if(modelo != null) {
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
		modelo.addColumn(nombre);												//Añade la nueva columna al modelo.
		cabecera = c;															//Actualiza la nueva cabecera.
	}
	
	/**
	 * <p>Title: setColumnName</p>  
	 * <p>Description: Establece un nuevo nombre a una posición de la cebcera</p>
	 * La función en si misma realiza una copia de la antigua cabecera y sustituye
	 *  el valor de la posición indicada por el nuevo valor.
	 * @param index Posición de la columna cuyo nombre se quiere cambiar.
	 * @param name Nuevo nombre a establecer en la columna.
	 */
	public void setColumnName(int index, String name) {
		//Modificación del valor de la columna especificado.
		cabecera[index] = name;				
		addCabecera(cabecera);													//Añade la nueva columna al modelo.
	}
	
	/**
	 * Añade una nueva cabecera completa.
	 * @param c array de objetos que van a sustituir a la cabecera actual.
	 */
	public void addCabecera(Object[] c) {
		this.cabecera = c;
		modelo.setColumnIdentifiers(c);
	}
	
	/**
	 * Función que elimina una fila de la tabla. Para ello se le debe pasar
	 * el índice de la fila a eliminar.
	 * @param fila Indice de la fila a eliminar.
	 * @return done TRUE si se ha realizado con exito la operación,
	 * false en otro caso.
	 */
	public boolean delFila(int fila) {
		boolean done = false;
		if (fila > -1 && fila < modelo.getRowCount()) {
			modelo.removeRow(fila);
			done = true;
		}
		return done;
	}
	
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
		int ncols2 = getColumnCount();									//Número de columnas en origen.
		int nfilas = getRowCount();
		Object datos[][] = new Object[nfilas][ncols2 - ncols];
		Object cabecera[] = new Object[ncols2 - ncols];
		int contCols = 0;
		if (ncols > 0) {	
			for(int j = 0; j<ncols2; j++) {
				if(!isListed(cols,j)) {
					//Copiar cabecera.
					cabecera[contCols] = getColumnName(j);
					//Copiar los datos.
					for(int i=0; i<nfilas;i++) {
						datos[i][contCols] = getValueAt(i,j);	
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
	 * <p>Title: crearModelo</p>  
	 * <p>Description: Crea un modelo con los datos almacenados en la instancia</p> 
	 * @return Modelo conformado.
	 */
	public DefaultTableModel crearModelo() {
		this.modelo = new DefaultTableModel(datos,cabecera);				//Conformar modelo
		return modelo;
	}
	
	/**
	 * <p>Title: nuevoModelo</p>  
	 * <p>Description: Crea una nueva tabla/modelo vacia.</p> 
	 */
	public void nuevoModelo() {
		//Cuerpo de datos
		datos = new Object[][]{};	
		//Cabecera de datos.		
		cabecera = new String[] {};
		this.modelo = new DefaultTableModel(datos,cabecera){
			/** serialVersionUID*/  
			private static final long serialVersionUID = -2383558100131841835L;
		};
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
	
	/**
	 * <p>Title: setDataToLabel</p>  
	 * <p>Description: Asigna un valor a una etiqueta.</p>
	 * Realiza una búsqueda por las filas buscando una etiqueta (que están situadas
	 *  en la primera fila (indice 0) hasta encontrarla, el valor de dicha etiqueta
	 *   esta almacenado en el segundo campo, y este dato será sobre escrito por
	 *    el nuevo valor. En otro caso no realiza acción alguna. 
	 * @param label Etiqueta a buscar.
	 * @param data Dato a insertar en la tabla.
	 * @return TRUE si la operación se ha realizado correctamente, FALSE en otro caso.
	 */
	public boolean setDataToLabel(String label, String data) {
		boolean done = true;
		int index = getFilaItem(label);
		if( index >= 0) setValueAt(data,index,1);
		else {done = false;}
		return done;
	}
	
	/**
	 * <p>Title: getDataFromRowLabel</p>  
	 * <p>Description: Devuelve el valor asignado a una etiqueta horizontal.</p>
	 * Las estiquetas en los módulos pueden estar orientadas de forma vertical
	 *  (una columna) u horizontal (en filas). Este método realiza el indexado
	 *   en modo horizontal, cuando encuentra dicha etiqueta devuelve el valor
	 *    asociado. En caso de no existir dicha etiqueta, devolverá NULL.
	 * @param label Etiqueta a buscar.
	 * @return El valor asociado a la etiqueta, NULL en otro caso.
	 */
	public Object getDataFromRowLabel(String label) {
		Object data = null;
		int index = getFilaItem(label);
		if(index > -1) data = getValueAt(index,1);
		return data;
	}
	
	
	/**
	 * <p>Title: getFilaItem</p>  
	 * <p>Description: Busca un elemento/valor en todas las primeras celdas
	 * de la tabla.</p> 
	 * @param v Valor a encontrar.
	 * @return Número de la fila que contiene dicho valor. -1 En otro caso.
	 */
	public int getFilaItem(String v) {
		int filas = getRowCount();
		int fila = -1;
		int indexF = 0;
		boolean encontrado = false;
		while(!encontrado && indexF < filas ) {
			String dato = (String) getValueAt(indexF,0);
			if(dato.equals(v)) {
				fila = indexF;
				encontrado = true;
			}
			indexF++;
		}
		return fila;
	}

	/**
	 * <p>Title: getColItem</p>  
	 * <p>Description: Busca un elemento/valor en todas las primeras celdas
	 * de cada columna en la tabla. Es decir sus nombres.</p> 
	 * @param v Valor a encontrar.
	 * @return Número de la columna que contiene dicho valor (nombre). -1 En otro caso.
	 */
	public int getColItem(String v) {
		int cols = getColumnCount();
		int col = -1;
		int indexC = 0;
		boolean encontrado = false;
		while(!encontrado && cols > indexC) {
			String dato = getColumnName(indexC);
			if(dato.equals(v)) {
				col = indexC;
				encontrado = true;
			}
			indexC++;
		}
		return col;
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

	/**
	 * Obtiene el valor en la posición indicada (No se tiene en cuenta la cabecera).
	 * @param arg0 Número de fila.
	 * @param arg1 Número de columna.
	 * @return Dato almacenado en dicha posición.
	 */
	@Override
	public Object getValueAt(int arg0, int arg1) {return modelo.getValueAt(arg0, arg1);}

	/**
	 * Permite conocer si una celda es editable o no lo es.
	 * En caso de que el módulo tenga desactivado la función de editar las etiquetas
	 *  la primera columna no será editable.
	 *  @see #setEditLabels
	 */
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		boolean is = modelo.isCellEditable(arg0, arg1);
		if(arg1 == 0 && !editLabels) is = false;
		return is;		
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) { modelo.removeTableModelListener(arg0);}

	
	/**
	 * Establece el dato en la posición indicada dentro de la tabla.
	 *  Si no existe la columna o la fila, mostrará el mensaje de aviso correspondiente
	 *   y no realizará acción alguna.
	 * @param arg0 Objeto o dato a establecer.
	 * @param arg1 Número de fila.
	 * @param arg2 Número de columna.
	 */
	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		if(arg1 > this.getRowCount()) {
			System.out.println("DCVS > setValueAt > Valor de número de fila incorrecto: " + arg1 + "/" + this.getRowCount());
		}else if (arg2 > this.getColumnCount() ) {
			System.out.println("DCVS > setValueAt > Valor de número de columna incorrecto: " + arg2 + "/" + this.getColumnCount());
		}else {this.modelo.setValueAt(arg0, arg1, arg2);}
		
	}
	

	/**
	 * @return La ruta en el dispositivo, de la que procede la fuente de los datos.
	 */
	public String getRuta() {return ruta;}

	/**
	 * @param ruta La nueva ruta en el dispositivo de almacenamiento donde guardar
	 * los datos.
	 */
	public void setRuta(String ruta) {	this.ruta = ruta;}

	/**
	 * @return Devuelve el tipo de datos almacenados en esta instancia.
	 */
	public String getTipo() {return tipo;}
	

	/**
	 * @param tipo El tipo de datos a establecer para esta instancia.
	 */
	public void setTipo(String tipo) {	this.tipo = tipo;}
	
	/**
	 * <p>Title: getNombre</p>  
	 * <p>Description: Devuelve el nombre del archivo, con extensión inclusive</p> 
	 * @return Nombre del archivo.
	 */
	public String getNombre() {return this.nombre;}


	/**
	 * <p>Title: setName</p>  
	 * <p>Description: </p> 
	 * @param name Nombre del archivo.
	 */
	public void setName(String name) {this.nombre = name;}
	
	
	/**
	 * <p>Title: getDirectorio</p>  
	 * <p>Description: Devuelve el directorio del que ha sido cargado este módulo</p>
	 * No incluye nombre del fichero.
	 * @return Directorio del módulo.
	 */
	public String getDirectorio() { return this.directorio;}
	
	/**
	 * <p>Title: setDirectorio</p>  
	 * <p>Description: Establece su directorio de trabajo </p> 
	 * @param ruta Ruta desde el raíz hasta el directorio padre.
	 */
	public void setDirectorio(String ruta) {this.directorio = ruta;}


	/**
	 * @return La fecha del archivo en disco.
	 */
	public String getDate() {return date;}

	/**
	 * @param date La fecha a establecer del archivo.
	 */
	public void setDate(String date) {this.date = date;}


	/**
	 *  <P> Activa o desactiva la edición de la primera columna </P>
	 * La primera columna es la columna donde van identificadas las filas por etiquetas. En general,
	 *  al editar un archivo CSV es deseable permitir edición, no así cuando
	 *   se editan los módulos del proyecto a fin de mantener la consistencia de datos.
	 * @param editLabels TRUE si se desea permitir edición para la primera columna. FALSE en otro caso.
	 */
	public void setEditLabels(boolean editLabels) {this.editLabels = editLabels;}
	
}
