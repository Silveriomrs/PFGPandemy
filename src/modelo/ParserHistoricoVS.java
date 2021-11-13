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
public class ParserHistoricoVS {
	
	private DCVS dcvs;															//Conjunto de datos importados desde VenSim.
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
	public ParserHistoricoVS(DCVS prjV) {
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
		System.out.println();
		
		//Obtención de los nombres de cada grupo.
		this.IDs = getNombresGrupos();
		
		//Obtención del número de grupos.
		this.NG = IDs.length;
		String nombres = "Número de Grupos: " + NG + " >";
		for(int i=0; i<NG;i++) nombres +=  " " + IDs[i];
		System.out.println(nombres);
		
		//Obtención de los tiempos inicial y final.
	    readTimes();
	    System.out.println("IT: " + IT + " FT: " + FT);
	    
		//Crear tantas Zonas como Grupos.
		crearZonas();
		System.out.println("Creadas " + zonas.size() + " zonas");
		//Leer tabla de relaciones.
		crearMatriz();
		
		//Leer R,S,I
		readXs("R");
		readXs("S");
		readXs("I");
		
		//Leer casos de CC,CVS,CI
		readXs("CC");
		readXs("CVS");
		readXs("CI");
		
		//Leer o calcular Prevalencia.
		if(getPosOp("P") > -1) {readXs("P");}									//Añadir la serie localizada a la zona indicada	
		else {getPs();}															//En otro caso calcular las prevalencias.
		
		//Leer o calcular nivel de contagio por cada 100 mil habitantes			//Equivale al nivel de contagio.
//		if(getPosOp("P") > -1) {readXs("P");}									//Añadir la serie localizada a la zona indicada	
//		else {getPs();}															//En otro caso calcular las prevalencias.
		
		//Leer tasas TC, TCONTAGIO
		readXs("TC");
		readXs("TCONTAGIO");
		
		//Leer casos de contactos de A con B y tasas de A con B
		readXenZ("TCS");
		readXenZ("CAB");	
	}
	
	/**
	 * <p>Title: getPs</p>  
	 * <p>Description: Calcula las prevalencias instantaneas para cada una de
	 * las zonas o grupos de población</p>
	 * Para ello hace uso de los valores previamente almacenados de R,S e I de cada zona.
	 */
	private void getPs() {
		for(int i = 1; i<=NG; i++) {
			Zona z = zonas.get(i);
			String ns = labels.getWord("P");									//Nombre de la serie.
			//Obtener los valores S,R e I correspondientes al mismo tiempo.
			for(int j = IT; j < FT; j++) {
				String name = z.getName();
				double vs = z.getGrafica().getYValue(labels.getWord("S"), j);	//valor Sj.
				double vr = z.getGrafica().getYValue(labels.getWord("R"), j);	//valor Rj.
				double vi = z.getGrafica().getYValue(labels.getWord("I"), j);	//valor Ij.

				//Cálculo de la prevalencia instantánea. P = I/(R+I+S)
				double pj = vi/(vs+vi+vr);
				//Guardado de la prevalencia.
				z.addNivel(ns, j, pj);
				
				//Calculo casos incidentes por cada 100 mil habitantes = 100000*(CI/(S+I+R))
				double vci = z.getGrafica().getYValue(labels.getWord("CI"), j);	//valor Ij.
				double ci10K = 100000*vci/(vs+vi+vr);							//Cálculo
				z.addNivel("Nivel", j, ci10K);									//Guardado del Nivel de contagio.
				
				if(j==0) {
					System.out.println(ns + " inicial " + name + " : " + pj);
					System.out.println("Casos Incidentes iniciales por cada 100 mil habitantes" + " : " + ci10K);
				}
			}
		}
	}
	
	
	/**
	 * <p>Title: readXenZ</p>  
	 * <p>Description: Lee los datos referentes a las etiquetas que contienen
	 * una relación directa entre dos grupos de población.</p>
	 * Por ejemplo TCS y CAB. 
	 * @param et Etiqueta a leer.
	 */
	private void readXenZ(String et) {
		//Primera ronda para el primer parámetro.
		for(int i = 1; i<=NG; i++) {
			Zona z = zonas.get(i);
			//Segunda ronda para el segundo parámetro.
			for(int j=0; j<NG; j++) {
				//Crear nombre columna.
				String nameS = et + " " + z.getName() + " " + IDs[j];
				int row = dcvs.getFilaItem(nameS);
				if(row > -1) {addSerieXs(nameS,row,z);}							//Añadir la serie localizada a la zona indicada.
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
			int row = dcvs.getFilaItem(sID);		
			if(row > -1) addSerieXs(et,row,z);									//Añadir la serie localizada a la zona indicada	
		}
	}
	
	/**
	 * <p>Title: addXs</p>  
	 * <p>Description: Agrega la información de una serie de datos a la gráfica
	 * correspondiente a una zona.</p> 
	 * @param et Etiqueta u operador correspondiente de la columna.
	 * @param pos Número de fila en la que está situada dicha etiqueta.
	 * @param z Zona a la que añadir los valores correspondientes a la serie indicada en la etiqueta.
	 */
	private void addSerieXs(String et, int pos, Zona z) {
		double valor;															//Almacena cada valor de la serie en lectura.
		boolean correcto = true;												//Finaliza la lectura si hay un valor no válido.
		int contador = IT;
		//Se recorre para cada tipo de dato todos los registros.
		while(correcto && (contador < FT)) {
			String s = (String) dcvs.getValueAt(pos, contador +1);
			String op = et.split(" ")[0];
			
			if(s != null && !s.equals("") && op != null) {
				valor =  Double.parseDouble(s.replace(",", "."));
				String etiqueta = labels.getWord(op);							//Obtener el valor de la etiqueta.	
				//Caso especial para etiquetas con dos operandos.
				if( op.equals("CAB") || op.equals("TCS")) {
					etiqueta = etiqueta.replaceFirst("Z",getSecondID(et));
				}
				//Si etiqueta es nulo usar el pasado por parámetro.
				if(etiqueta == null ) etiqueta = et;
				//Guardar el valor en la gráfica de la zona.
				z.addNivel(etiqueta, contador,valor);
				
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
		int fila = 0;
		//Tiempo inicial.
		fila = dcvs.getFilaItem("INITIAL TIME");
		if(fila > -1) IT = fila;
		else IT = 0;
		//Tiempo Final.
		fila = dcvs.getFilaItem("FINAL TIME");
		if(fila > -1) FT = fila;
		else FT = dcvs.getColumnCount() - 1;									//Restar la columna de las estiquetas.
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
		//Detectar si tiene etiqueta de Matriz de contactos.
		boolean hasMC = getPosOp("C") > -1;
		//Añadir filas
		for(int i = 0; i<NG; i++) {
			fila[0] = IDs[i];													//Dar nombre a la fila 0 del grupo que representa.
			for(int j = 1; j<=NG; j++) {										//Saltamos la columna de indentificadores verticales.
				int contadorCeros = i;											//Indica los ceros que hay que dejar al comienzo de cada fila.
				if(contadorCeros > (j-1))  fila[j] = "0";
				else {
					if(hasMC) fila[j] = (String) dcvs.getValueAt(0, contadorCol);
					else fila[j] = "0";											//Sino tiene Matriz de contactos indicar valor 0.
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
		String name = IDs[ID];
		int row = dcvs.getFilaItem("PT0 " + name);
		//Si existe la etiqueta concreta se toma su valor.
		if(row>-1) habitantes = Integer.parseInt((String) dcvs.getValueAt(row, 0));
		//En otro caso se realiza una suma de los valores de las etiquetas R,S,I.
		else {
			int posS = dcvs.getFilaItem("S " + name);
			if(posS > -1) habitantes += Integer.parseInt((String) dcvs.getValueAt( posS,1));
			int posR = dcvs.getFilaItem("R " + name);
			if(posR > -1) habitantes += Integer.parseInt((String) dcvs.getValueAt( posR,1));
			int posI = dcvs.getFilaItem("I " + name);
			if(posI > -1) habitantes += Integer.parseInt((String) dcvs.getValueAt( posI,1));
		}
		System.out.println("Población Inicial " + name + ": " + habitantes);
		return habitantes;	
	}
	
	
	private String[] getNombresGrupos() {
		String op = "C";		
		int row0 = getPosOp("C");
		//En caso de no contener dicha etiqueta buscará la básica S.
		if(row0 < 0) {
			op = "S";
			row0 = getPosOp("S");
		}
		
		
		String parte;
		// Encontrar el número de grupos total.
		int NG = getNumberGroups();
		// Proceso de búsqueda de todas las IDs
		String[] nombres = new String[NG];
		if(NG > 0) {
			// Obtener la primera ID:
			parte = (String) dcvs.getValueAt(row0,0);
			nombres[0] = getFirstID(parte);
			
			//Encontrar el resto
			for(int i=1; i<NG ;i++) {
				String IDAux = null;
				parte = (String) dcvs.getValueAt(row0 + i,0);
				if(op.equals("C")) {IDAux = getSecondID(parte);	}
				else {IDAux = getFirstID(parte);}
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
		String OP = "R";
		int nRows = dcvs.getRowCount();
		//Primero encontramos la posición de la primera etiqueta C perteneciente a la matriz de contactos.
		int row = getPosOp(OP);
		//Si se ha encontrado (col1 != -1)
		if(row > -1) {
			//Encontrada primera coincidencia, se incrementa el contador.
			contador++;
			//obtenemos el primer ID
			String ID1 = getFirstID( (String) dcvs.getValueAt(row, 0));
			//Repetición de la operación mientras la siguiente columna contenga "C + ID" igual.	
			while(!fin && row < nRows) {		
				row++;
				String s = (String) dcvs.getValueAt(row, 0);
				String IDAux = getFirstID(s);	
				fin = !hasOperator(OP,s);
//				System.out.println(fin + " | Parser > getNumberGroups s: " + s + " ID: " + IDAux);
				if(!fin && !IDAux.equals(ID1)) contador++;
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
	
	/**
	 * <p>Title: getPosOp</p>  
	 * <p>Description: Realiza una búsqueda de un operador por todas las filas
	 * contenedoras de las etiquetas de la primera columna.</p> 
	 * @param op Operador búscado, es decir, la etiqueta.
	 * @return Número de línea donde esta situado dicho operador. -1 Sino existe.
	 */
	private int getPosOp(String op) {
		int row = -1;
		int contador = 0;
		boolean encontrado = false;
		int nRows = dcvs.getRowCount();
		
		while(!encontrado && contador < nRows) {
			if(hasOperator(op,(String) dcvs.getValueAt(contador, 0))) {
				encontrado = true;
				row = contador;
			}else contador++;
		}
		
		return row;
	}
	
	/**
	 * <p>Title: hasOperator</p>  
	 * <p>Description: Indica si la sintaxis de una cadena de comando y operadores
	 * contiene un operador o comando determiando.</p> 
	 * @param op Operador buscado.
	 * @param txt Cadena de texto donde buscar el comando u operador.
	 * @return TRUE si la cadena contiene dicho operador, FALSE en otro caso.
	 */
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
