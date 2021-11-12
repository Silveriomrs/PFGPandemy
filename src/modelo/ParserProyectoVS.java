/**  
* <p>Title: ParserProyectoVS.java</p>  
* <p>Description: Parser para importar datos de un proyecto VenSim</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 11 nov. 2021  
* @version 1.0  
*/  
package modelo;

import java.util.HashMap;

import vista.GraficasChart;

/**
 * <p>Title: ParserProyectoVS</p>  
 * <p>Description: Parser para importar datos de un proyecto VenSim </p> 
 * La clase obtiene mediante un sistema de etiquetas todos los datos necesarios
 * de un proyecto VenSim y configura los módulos correspondientes con esos datos.
 * Esta clase es usada para lectura de datos, no guarda datos.
 * @author Silverio Manuel Rosales Santana
 * @date 11 nov. 2021
 * @version versión 1.0
 */
public class ParserProyectoVS {
	
	private DCVS dcvs;															//Conjunto de datos importados de VenSim.
	private DCVS mContactos;													//Matriz de contactos (relaciones).
	private String[] IDs;														//Almacena los nombres de los grupos.
	private Labels labels;
	private HashMap<Integer,Zona> zonas;
	private int NG;																//Número de grupos de población.
	private int IT;																//Tiempo inicial de la simulación.
	private int FT;																//Tiempo final de la simulación.

	/**
	 * <p>Title: Constructor del parser</p>  
	 * <p>Description: Inicializar el parser con los datos obtenidos por parámetro.</p>  
	 * @param prjV Conjunto de datos del proyecto VenSim
	 */
	public ParserProyectoVS(DCVS prjV) {
		this.zonas = new HashMap<Integer,Zona>();
		this.mContactos = new DCVS();
		this.labels = new Labels();
		this.NG = 0;
		IT = FT = 0;
		if(prjV != null) importarVensim(prjV);
	}


	/**
	 * <p>Title: importarVensim</p>  
	 * <p>Description: Importa los datos de un archivo generado con VenSim.</p>
	 * Esta opción elimina el resto de datos actuales de los módulos implicados.
	 * @param prjV Conjunto de datos del archivo de salida Vensim.
	 */
	public void importarVensim(DCVS prjV) {
		if (prjV != null) this.dcvs = prjV;
		
		//Obtención de los nombres de cada grupo.
		this.IDs = getNombresGrupos();
		//Obtención del número de grupos.
		this.NG = IDs.length;
		for(int i=0; i<NG;i++) System.out.println(IDs[i]);
		//Obtención de los tiempos inicial y final.
	    readTimes();
		//Crear tantas Zonas como Grupos.
		crearZonas();
		
		//Leer tabla de relaciones.
		crearMatriz();
		
		//Leer R,S,I y P
		readXs("R");
		readXs("S");
		readXs("I");
		readXs("P");
		
		//Leer casos de CC,CVS,CI
		readXs("CC");
		readXs("CVS");
		readXs("CI");
		
		//Leer tasas TC, TCONTAGIO
		readXs("TC");
		readXs("TCONTAGIO");
		
		//Leer casos de contactos de A con B y tasas de A con B
		readXenZ("TCS");
		readXenZ("CAB");
		
	}
	
	private void readXenZ(String et) {
		//Primera ronda para el primer parámetro.
		for(int i = 1; i<=NG; i++) {
			Zona z = zonas.get(i);
			//Segunda ronda para el segundo parámetro.
			for(int j=0; j<NG; j++) {
				//Crear nombre columna.
				String nameS = et + " " + z.getName() + " " + IDs[j];
				int col = dcvs.getColItem(nameS);
				if(col > -1) {addSerieXs(nameS,col,z);}							//Añadir la serie localizada a la zona indicada.
			}
			
			GraficasChart gc = z.getGrafica();
			gc.setVisible(true);
		}
		
	}
	
	
	@SuppressWarnings("unused")
	private String changeXY(String et, int id1, int id2) {
		String label = et;
		label = label.replaceFirst("X", IDs[1]);
		label = label.replaceFirst("Z", IDs[2]);
		return label;
	}
	
	/**
	 * <p>Title: readXs</p>  
	 * <p>Description: Realiza una lectura de la columna indicada por la etiqueta
	 * añadiendo los valores de dicha columna a la gráfica correspondiente a cada
	 * grupo de estudio (zona)</p> En caso de no existir dicha etiqueta en la fuente de
	 * datos, no se añadirán.
	 * @param et Etiqueta u operador del que obtener los datos. Ej: R,S,I,P,etc.
	 */
	private void readXs(String et) {
		//Primero añadir las series básicas R,S e I a cada zona.
		for(int i = 1; i<=NG; i++) {
			Zona z = zonas.get(i);
			String sID = et + " " + z.getName();
			int col = dcvs.getColItem(sID);		
			if(col > -1) addSerieXs(et,col,z);									//Añadir la serie localizada a la zona indicada	
		}
	}
	
	/**
	 * <p>Title: addXs</p>  
	 * <p>Description: </p> 
	 * @param et Etiqueta u operador correspondiente de la columna.
	 * @param col Número de columna en la que está situada dicha etiqueta.
	 * @param z Zona a la que añadir los valores correspondientes a la serie indicada en la etiqueta.
	 */
	private void addSerieXs(String et, int col, Zona z) {
		double valor;															//Almacena cada valor de la serie en lectura.
		boolean correcto = true;												//Finaliza la lectura si hay un valor no válido.
		int contador = IT;
		//Se recorre para cada tipo de dato todos los registros.
		while(correcto && (contador < FT)) {
			String s = (String) dcvs.getValueAt(contador, col);
			String op = et.split(" ")[0];
			
			if(s != null && !s.equals("") && op != null) {
				valor =  Double.parseDouble(s);
				String etiqueta = labels.getWord(op);							//Obtener el valor de la etiqueta.	
				//Caso especial para etiquetas con dos operandos.
				if( op.equals("CAB") || op.equals("TCS")) {
					System.out.println(et + " > col: " + col + " fila: " + contador + " - "+ etiqueta + ", Valor: " + valor);
					etiqueta = etiqueta.replaceFirst("Z",getSecondID(et));
				}
				//Si etiqueta es nulo usar el pasado por parámetro.
				if(etiqueta == null ) etiqueta = et;							
				if(s != null && !s.equals("")) z.addNivel(etiqueta, contador,valor);
			}else { correcto = false; }											//Si un valor leído no es válido no continuar con la lectura.
			//Siguiente línea.
			contador++;
		}
	}
	
	/**
	 * <p>Title: readTimes</p>  
	 * <p>Description: Establece los tiempos inicial y final de la simulación
	 * en los campos correspondientes.</p>
	 */
	private void readTimes() {
		this.IT = Integer.parseInt((String) dcvs.getValueAt(0,dcvs.getColItem("INITIAL TIME")));
		this.FT = Integer.parseInt((String) dcvs.getValueAt(0,dcvs.getColItem("FINAL TIME")));
	}
	
	
	/**
	 * <p>Title: crearMatriz</p>  
	 * <p>Description: Genera la matriz de contactos desde las etiquetas del 
	 * conjunto de datos.</p>
	 */
	private void crearMatriz() {
		String[] fila = new String[NG+1];
		String[] cabecera = new String[NG+1];
		int contadorCol = 1;
		//Crear cabeceras.
		cabecera[0] = "Grupos";													//Primera columna reservada.
		//Crear resto columnas con nombres de grupos.
		for(int i=1; i<=NG;i++) {cabecera[i] = IDs[i-1];}
		mContactos.addCabecera(cabecera);
		
		//Añadir filas
		for(int i = 0; i<NG; i++) {
			fila[0] = IDs[i];													//Dar nombre a la fila 0 del grupo que representa.
			for(int j = 1; j<=NG; j++) {										//Saltamos la columna de indentificadores verticales.
				int contadorCeros = i;											//Indica los ceros que hay que dejar al comienzo de cada fila.
				if(contadorCeros > (j-1))  fila[j] = "0";
				else {
					fila[j] = (String) dcvs.getValueAt(0, contadorCol);
					contadorCol++;
				}
				contadorCeros++;		
			}
			mContactos.addFila(fila);
		}
		
		System.out.println( mContactos.toString() );
	}
	
	
	/**
	 * <p>Title: crearZonas</p>  
	 * <p>Description: Crea las zonas o grupos de población con los parámetros
	 * almacenados en el conjunto de datos. Además añade el número de habitantes
	 * inicial cuando esté disponible, en otro caso el número de habitantes será
	 * cero.</p>
	 */
	private void crearZonas() {
		for(int i = 0; i<NG; i++) {
			int superficie = 0;													//En este modelo VenSim no incluye superficie.	
			int habitantes = getPeople(i);										//Obtener población inicial (si hay).
			zonas.put(i+1,new Zona(i+1, IDs[i], habitantes, superficie, null));
		}	
	}
	
	/**
	 * <p>Title: getPeople</p>  
	 * <p>Description: Obtiene el número de habitantes para la zona indicada</p> 
	 * @param ID Número identificador de la zona.
	 * @return Número de habitantes de la zona. 0 en otro caso.
	 */
	private int getPeople(int ID) {
		int habitantes = 0;
		int col = dcvs.getColItem("PT0 " + IDs[ID]);
		if(col>-1) habitantes = Integer.parseInt((String) dcvs.getValueAt(0, col));
		System.out.println("Parser VenSim > getPeople: " + habitantes);
		return habitantes;	
	}
	
	
	private String[] getNombresGrupos() {	
		int col0 = getColOp("C");
		String parte;
		// Encontrar el número de grupos total.
		int NG = getNumberGroups();
		// Proceso de búsqueda de todas las IDs
		String[] nombres = new String[NG];
		if(NG > 0) {
			// Obtener la primera ID:
			parte = dcvs.getColumnName(col0);
			nombres[0] = getFirstID(parte);
			
			//Encontrar el resto
			for(int i=1; i<NG ;i++) {
				parte = dcvs.getColumnName(col0 + i);
				String IDAux = getSecondID(parte);
				nombres[i] = IDAux;
			}
		}
		
		return nombres;
	}
	
	
	/**
	 * <p>Title: getNumberGroups</p>  
	 * <p>Description: Obtiene el número de grupos de población en un archivo
	 * con formato VenSim CSV </p> 
	 * @return Número de elementos identificados como grupos de población.
	 */
	private int getNumberGroups() {
		int contador = 0;
		boolean fin = false;
		String OP = "C";
		int ncols = dcvs.getColumnCount();
		//Primero encontramos la posición de la primera etiqueta C perteneciente a la matriz de contactos.
		int col = getColOp(OP);		
		//Si se ha encontrado (col1 != -1)
		if(col > -1) {
			//Encontrada primera coincidencia, se incrementa el contador.
			contador++;
			//obtenemos el primer ID
			String ID1 = getFirstID(dcvs.getColumnName(col));
			//While mientras la siguiente columna tenga C + ID igual.	
			while(!fin && col < ncols) {		
				col++;
				String IDAux = getFirstID(dcvs.getColumnName(col));
				fin = !hasOperator(OP,dcvs.getColumnName(col));
				
				if(!fin && IDAux.equals(ID1)) contador++;
			}
		}
		return contador;														//Devolver número de coincidencias == número de grupos.
	}
	
	
	/**
	 * @return El INITIAL TIME o tiempo de inicio de la simulación.
	 */
	public int getIT() {return IT;}	

	/**
	 * @return El FINAL TIME o tiempo final de la simulación.
	 */
	public int getFT() {return FT;}


	private String getFirstID(String parte) {return parte.split(" ")[1];}
	
	
	private String getSecondID(String parte) {return parte.split(" ")[2];}
	
	
	private int getColOp(String op) {
		int col = -1;
		int contador = 0;
		boolean encontrado = false;
		int ncols = dcvs.getColumnCount();
		
		while(!encontrado && contador < ncols) {
			if(hasOperator(op,dcvs.getColumnName(contador))) {
				encontrado = true;
				col = contador;
			}else contador++;
		}
		
		return col;
	}
	
	
	private boolean hasOperator(String op, String txt) {
		boolean encontrado = false;
		int contador = 0;
		String[] partes = txt.split(" ");
		int longitud = partes.length;
		
		while(!encontrado && contador < longitud) {
			if(partes[0].equals(op)) encontrado = true;
			else contador++;		
		}
	
		return encontrado;
	}


	
	/**
	 * @return Matriz de contactos.
	 */
	public DCVS getRelaciones() {return mContactos;}
	
}
