/**  
* <p>Title: ParserProyectoVS.java</p>  
* <p>Description: Parser para importar datos de un proyecto VenSim</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 11 nov. 2021  
* @version 1.0  
*/  
package modelo;

import java.util.ArrayList;
import java.util.HashMap;

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
	private DCVS mREL;															//Matriz de contactos (relaciones).
	private DCVS mDEF;															//Módulo definición de la enfermedad (SIR)
	private DCVS mPRJ;															//Módulo proyecto.
	private DCVS mHST;															//Módulo histórico
	private DCVS mMAP;
	private String[] IDs;														//Almacena los nombres de los grupos.
	private Labels labels;
	private HashMap<Integer,Zona> zonas;
	private int NG;																//Número de grupos de población.
	private int IT;																//Tiempo inicial de la simulación.
	private int FT;																//Tiempo final de la simulación.
	private boolean traza = true;

	/**
	 * <p>Title: Constructor del parser</p>  
	 * <p>Description: Inicializar el parser con los datos obtenidos por parámetro.</p>  
	 * @param prjV Conjunto de datos del proyecto VenSim
	 */
	public ParserHistoricoVS(DCVS prjV) {
		this.zonas = new HashMap<Integer,Zona>();
		this.mREL = new DCVS();
		this.mDEF = DCVSFactory.newModule(TypesFiles.DEF);
		this.mPRJ = DCVSFactory.newModule(TypesFiles.PRJ);
		this.mMAP = DCVSFactory.newModule(TypesFiles.MAP);
			
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
		if(traza) System.out.println(nombres);
		
		//Establecer módulo proyecto.
		setUpProyecto();
		
		//Obtención de los tiempos inicial y final.
	    readTimes();
	    if(traza) System.out.println("IT: " + IT + " FT: " + FT);
	    
	    //Crea la tabla histórico (SIN DATOS).
	    crearHST();
	    
		//Crear tantas Zonas como Grupos.
		crearZonas();
		if(traza) System.out.println("Creadas " + zonas.size() + " zonas");
		
		//Crear tabla de grupos de población (MAP).
		crearMMAP();
		
		//Leer y crear tabla de relaciones.
		crearMREL();
		
		//Crear contenedor de definición de enfermedad (Parámetros SIR).
		crearMDEF();
		
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
//		else {getPs();}															//Sino están, calcular las prevalencias.
		
		//Leer tasas TC, TCONTAGIO
		readXs("TC");
		readXs("TCONTAGIO");
		
		//Leer casos de contactos de A con B y tasas de A con B
		readXenZ("TCS");
		readXenZ("CAB");
		
		
	}
	
	/**
	 * <p>Title: setTypeAndName</p>  
	 * <p>Description: Establece un nombre y un tipo de datos al módulo.</p>
	 * Usa los datos almacenados en el archivo de origen para establecer los
	 *  nombres a los módulos particulares. 
	 * @param modulo Módulo al que establecer los atributos de Tipo y Nombre.
	 * @param type Tipo de datos que contiene el módulo. Ver: {@link TypesFiles Tipos de datos}.
	 */
	private void setTypeAndName(DCVS modulo, String type){
		//Establecer atributos propios del módulo.
		modulo.setTipo(type);
		//Crear nombre con extensión DEF a partir del nombre del archivo original.
		String newName = dcvs.getNombre();
		//Quitamos la extensión.
		newName = newName.substring(0, newName.length() -3);
		//Guardar dato con nueva extensión.
		modulo.setName(newName + type);
	}
	
	
	private void setUpProyecto() {
		//Añadir tipo y nombre
		setTypeAndName(mPRJ,TypesFiles.PRJ);

		//Añadir etiquetas generales.
		mPRJ.setDataToLabel(Labels.NAME, dcvs.getNombre());
		mPRJ.setDataToLabel(Labels.NG,"" + getNG());
		mPRJ.setDataToLabel(Labels.DESCRIPTION,"Modelo obtenido de una fuente externa.");		
		System.out.println("\nProyecto: \n" + mPRJ.toString());
	}

	/**
	 * <p>Title: crearHST</p>  
	 * <p>Description: Crea la tabla con las etiquetas mínimas</p>
	 * No guarda datos. Los datos se añaden posteriormente desde las funciones
	 *  que a continuación se indican.
	 * @see #readXs(String)
	 * @see #readXenZ(String)
	 */
	private void crearHST() {
		mHST = DCVSFactory.newHST(FT);
		setTypeAndName(mHST,TypesFiles.HST);
	}
	
	
	/**
	 * <p>Title: crearMDEF</p>  
	 * <p>Description: Genera el módulo definición de la enfermedad.</p>
	 * Para ello genera una lista con las etiquetas que deben estár incluidas,
	 *  por tanto si se quiere incluir nuevas etiquetas, este es el lugar donde deberá
	 *   hacerser.
	 *  <p>Las etiquetas incluidas son: </> PTE,DME,DMIP,IP, IT e TI. para más 
	 *   información {@link Labels Clase Etiquetas.}
	 *  <P>La Inmunidad Permanente IP, aunque no esté específicada de forma explícita, 
	 *   puede estar implicita en la presencía de las etiquetas CVS o TVS. Por
	 *    tanto, se realiza una búsqueda encadenada de las tres etiquetas.</P>
	 */
	private void crearMDEF(){
		setTypeAndName(mDEF,TypesFiles.DEF);	
		//Añadir etiquetas requeridas a la lista.
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(Labels.PTE);
		lista.add(Labels.DME);
		
		//Procesar la lista añadiendo dichos campos al módulo mDEF.
		for(int i = 0; i<lista.size(); i++) {
			String etiqueta = lista.get(i);
			String value = "0";													//En caso de no estar, se configurará a 0.
			int pos = dcvs.getFilaItem(etiqueta);		
			if(pos > -1) {value = (String) dcvs.getValueAt(pos, 1);}
			mDEF.setDataToLabel(etiqueta, value);
		}
		
		//IT Leído previamente
		mDEF.setDataToLabel(Labels.IT,"" + IT);
		//FT Leído previamente
		mDEF.setDataToLabel(Labels.FT,"" + FT);
		//Búsqueda de la IP específica.
		mDEF.setDataToLabel(Labels.IP,hasIP());
		//Búsqueda de DMIP o su inversa = 1/TVS
		mDEF.setDataToLabel(Labels.DMI,getDMI());
		
		if(traza) System.out.println( mDEF.toString() );		
	}
	
	/**
	 * <p>Title: crearMMAP</p>  
	 * <p>Description: Genera el módulo de los grupos de población (MAP).</p>
	 * Después de realizar la lectura de las zonas y sus valores, se debe ejecutar
	 * este método para generar el modulo de mapa. (sin representaciones gráficas).
	 */
	private void crearMMAP() {
		setTypeAndName(mMAP,TypesFiles.MAP);
		for(int i = 1; i <= NG; i++) {
			Zona z = zonas.get(i);
			String[] datos = z.toString().split(",");
			mMAP.addFila(datos);
		}
	}

	
	/**
	 * <p>Title: crearMREL</p>  
	 * <p>Description: Genera la matriz de contactos desde las etiquetas del 
	 * conjunto de datos.</p>
	 */
	private void crearMREL() {
		mREL = DCVSFactory.newREL(zonas);
		setTypeAndName(mREL,TypesFiles.REL);
		int index = getPosOp("C");
		boolean hasMC = index > -1;
		
		if(hasMC) {
			for(int i = 0; i<NG;i++) {
				String label = "C " + IDs[i] + " ";
				for(int j = 0; j<NG;j++) {
					index = dcvs.getFilaItem(label + IDs[j]);
					hasMC = index > -1;
					if(hasMC) {
						//Leer valor.
						String valor = (String) dcvs.getValueAt(index, 1);
						//escribir valor en tabla
						mREL.setValueAt(valor,i,j+1);
					}
				}
			}
		}	
		if(traza) System.out.println( mREL.toString() );
	}
	
	
	/**
	 * <p>Title: getDMI</p>  
	 * <p>Description: Devuelve la Duración Media de la Inmunidad.</p>
	 * Para ello, realiza una búsqueda primero de la propia etiqueta DMIP, si esta
	 * incluida, devolverá dicho valor. En otro caso, proseguiría con la búsqueda 
	 *  de la etiqueta TVS (Tasa de vuelta a la Susceptibilidad) pues esta es su
	 *   inversa. En caso de no encontrar ninguna de las dos, devolverá el valor 0.
	 * @return El valor de la etiqueta DMIP o TVS en su ausencia, 0 en otro caso.
	 */
	private String getDMI() {
		String value = "0";
		//Realización al corte de la búsqueda, por eficiencia y por peso de etiqueta.
		int pos = dcvs.getFilaItem(Labels.DMI);
		if( pos > -1) {	value = (String) dcvs.getValueAt(pos,1);	}
		else if(getPosOp(Labels.TVS) > 0) {
			//En este caso hay que realizar la operación matemática inversa a su valor.
			pos =  getPosOp(Labels.TVS);
			value = (String) dcvs.getValueAt(pos,1);
			double d = Double.parseDouble(value);
			value = "" +  1/d;
		}	
		return value;
	}
	
	/**
	 * <p>Title: hasIP</p>  
	 * <p>Description: Indica si existe la Inmunidad Permanente o no.</p>
	 * Para ello, realiza una búsqueda muy específica de las etiquetas IP, CVS y
	 *  TVS, de existir alguna de ellas se entenderá que si existe dicha inmunidad.
	 *   La búsqueda se realiza en corte, búscando primero la propia etiqueta, de
	 *   existir, será la que indique su valor, en otro caso realiza una búsqueda de
	 *    las otras etiquetas, si encuentra alguna, se supondrá que existe IP.
	 * @return 1 Si existe IP, 0 en otro caso.
	 */
	private String hasIP() {
		String has = "0";
		//Realización al corte de la búsqueda, por eficiencia y por peso de etiqueta.
		int pos = dcvs.getFilaItem(Labels.IP);
		if( pos > -1) {	has = (String) dcvs.getValueAt(pos, 1);	}
		return has;
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
			String[] filaP = new String[FT + 1];
			filaP[0] = "P " + z.getID();
			String[] filaC100K = new String[FT + 1];
			filaC100K[0] = "C100K " + z.getID();
			
			String ns = labels.getWord("P");									//Nombre de la serie.
			//Obtener los valores S,R e I correspondientes al mismo tiempo.
			for(int j = IT; j < FT; j++) {
				String name = z.getName();
				double vs = z.getGrafica().getYValue(labels.getWord("S"), j+1);	//valor Sj.
				double vr = z.getGrafica().getYValue(labels.getWord("R"), j+1);	//valor Rj.
				double vi = z.getGrafica().getYValue(labels.getWord("I"), j+1);	//valor Ij.

				//Cálculo de la prevalencia instantánea. P = I/(R+I+S)
				double pj = vi/(vs+vi+vr);
				//Guardado de la prevalencia.
				z.addNivel(ns, j, pj);
				filaP[j+1] = "" + pj;
				
				//Calculo casos incidentes por cada 100 mil habitantes = 100000*(CI/(S+I+R))
				double vci = z.getGrafica().getYValue(labels.getWord("CI"), j);	//valor Ij.
				double ci100K = 100000*vci/(vs+vi+vr);							//Cálculo
				z.addNivel(labels.getWord(Labels.C100K), j, ci100K);									//Guardado del Nivel de contagio.
				filaC100K[j+1] = "" + ci100K;
				
				if(j==0) {
					if(traza) System.out.println(ns + " inicial " + name + " : " + pj);
					if(traza) System.out.println("Casos Incidentes iniciales por cada 100 mil habitantes" + " : " + ci100K);
				}
			}
			//Añadir las nuevas filas con los datos al módulo histórico.
			mHST.addFila(filaC100K);
			mHST.addFila(filaP);
			
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
		String[] fila = new String[FT + 1];
		fila[0] = et + " " + z.getName();
		// OP + " " + NombreZ  == et
		System.out.println(fila[0]);
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
				fila[contador +1] = "" + valor;
			}else { correcto = false; }											//Si un valor leído no es válido no continuar con la lectura.
			//Siguiente línea.
			contador++;
		}
		
		mHST.addFila(fila);

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
			zonas.put(i+1,new Zona(i+1, IDs[i], habitantes, superficie,0,0,0,0,0, null));
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
		if(traza) System.out.println("Población Inicial " + name + ": " + habitantes);
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
		int NG = readNumberGroups();
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
	 * <p>Title: redNumberGroups</p>  
	 * <p>Description: Obtiene el número de grupos de población en un archivo
	 * con formato VenSim CSV </p>
	 * Para realizar su tarea realiza una cuenta de uno de los operadores indespensables
	 * en el archivo de entrada, en este caso la etiqueta 'R'. 
	 * @return Número de elementos identificados como grupos de población.
	 */
	private int readNumberGroups() {
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
	public DCVS getMContactos() {return mREL;}


	/**
	 * @return El/la zonas
	 */
	public HashMap<Integer, Zona> getZonas() {return zonas;	}


	/**
	 * @return Obtiene el módulo de grupos de población.
	 */
	public DCVS getMAP() {return mMAP;}
	

	/**
	 * <p>Title: getNG</p>  
	 * <p>Description: Devuelve el número de grupos de población.</p> 
	 * @return Número de grupos de población.
	 */
	public int getNG() {return this.NG;}

	/**
	 * @return Definición de la enfermedad, sus parámetros.
	 */
	public DCVS getmDefENF() {return mDEF;}	
	
	/**
	 * <p>Title: getMPRJ</p>  
	 * <p>Description: Devuelve el módulo del proyecto configurado.</p> 
	 * @return Módulo Proyecto.
	 */
	public DCVS getMPRJ() {return mPRJ;}
	
	/**
	 * <p>Title: getMHST</p>  
	 * <p>Description: Devuelve el módulo histórico con los valores obtenidos</p> 
	 * @return Módulo histórico, con los datos leídos y los calculados a partir de estos.
	 */
	public DCVS getMHST() {return mHST;}
}
