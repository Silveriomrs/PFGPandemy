/**  
* <p>Title: ParserProyectoVS.java</p>  
* <p>Description: Parser para importar datos de un proyecto VenSim</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 11 nov. 2021  
* @version 1.0  
*/  
package modelo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>Title: ParserProyectoVS</p>  
 * <p>Description: Parser para importar datos de un proyecto VenSim </p> 
 * La clase obtiene mediante un sistema de etiquetas todos los datos necesarios
 * de un proyecto VenSim y configura los módulos correspondientes con esos datos.
 * Esta clase es usada para lectura de datos, no guarda datos.
 * <P>La fuente de datos tiene las etiquetas en la primera fila, es decir, 
 *  la información esta ordenada en columnas.</P>
 * @author Silverio Manuel Rosales Santana
 * @date 11 nov. 2021
 * @version versión 1.2
 */
public class ParserProyectoVS {
	
	private DCVS dcvs;															//Conjunto de datos importados de VenSim.
	private DCVS mREL;												//Matriz de contactos (relaciones).
	private DCVS definicionSIR;													//Módulo definición de la enfermedad (SIR)
	private DCVS historico;														//Matriz de contactos (relaciones).
	private DCVS proyecto;
	private String[] IDs;														//Almacena los nombres de los grupos.
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
	public ParserProyectoVS(DCVS prjV) {
		this.zonas = new HashMap<Integer,Zona>();
		this.mREL = new DCVS();
		this.definicionSIR = new DCVS();
		this.historico = new DCVS();
		this.proyecto = new DCVS();
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
		if(traza) {
			for(int i=0; i<NG;i++) System.out.println(IDs[i]);
		}
		
		//Obtención de los tiempos inicial y final.
	    readTimes();
		//Crear tantas Zonas como Grupos.
		crearZonas();
		
		//Configuración del historico. Debe hacerse después de obtener Nombres e IDs
		//después de crear las zonas y antes del cálculo de los niveles de contagio.
		setUpHistorico();

		//Leer tabla de relaciones.
		setUpMatriz();
		
		//Crear contenedor de definición de enfermedad (Parámetros SIR).
		//Depende de lectura previa de TIMES.
		setUpDefEnf();
		
		//Por último la configuración del proyecto o modelo, pues para realizar
		//Depende de que los otros módulos estén configurados.
		setUpProyecto();
		
		//Leer R,S,I y P
		readXs(Labels.R);
		readXs(Labels.S);
		readXs(Labels.I);
		readXs(Labels.P);
		
		//Leer casos de CC,CVS,CI
		readXs(Labels.CC);
		readXs(Labels.CVS);
		readXs(Labels.CI);
		
		//Leer tasas de contacto y contagio.
		readXs(Labels.TC);
		readXs(Labels.TCONTAGIO);
		
		//Leer casos de contactos de A con B y tasas de A con B
		readXenZ(Labels.TCS);
		readXenZ(Labels.CAB);
		
		//Cálcular niveles en base al número de incidentes por cada 100 mil habitantes.
		generateLevels();
	
	}
	
	/**
	 * <p>Title: crearMDefEnf</p>  
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
	private void setUpDefEnf(){
		setTypeAndName(definicionSIR,TypesFiles.DEF);
		//Crear cabecera		
		String[] cabecera = {"tipo","dato"};
		//Añadir cabecera.
		definicionSIR.addCabecera(cabecera);
		
		//Añadir etiquetas requeridas a la lista.
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(Labels.PTE);
		lista.add(Labels.DME);
		
		//Procesar la lista añadiendo dichos campos al módulo definicionSIR.
		for(int i = 0; i<lista.size(); i++) {
			String etiqueta = lista.get(i);
			String value = "0";													//En caso de no estar, se configurará a 0.
			int pos = dcvs.getColItem(etiqueta);		
			if(pos > -1) {
				value = (String) dcvs.getValueAt(0, pos);
				definicionSIR.addFila(new String[]{etiqueta,value});
			}
			else definicionSIR.addFila(new String[]{etiqueta,value});
		}
		
		//IT Leído previamente
		definicionSIR.addFila(new String[]{Labels.IT,"" + IT});
		//FT Leído previamente
		definicionSIR.addFila(new String[]{Labels.FT,"" + FT});
		//Búsqueda de la IP específica.
		definicionSIR.addFila(new String[]{Labels.IP,hasIP()});
		//Búsqueda de DMIP o su inversa = 1/TVS
		definicionSIR.addFila(new String[]{Labels.DMI,getDMIP()});
		
		if(traza) System.out.println( definicionSIR.toString() );		
	}
	
	/**
	 * <p>Title: getDMIP</p>  
	 * <p>Description: Devuelve la Duración Media de la Inmunidad Permanente.</p>
	 * Para ello, realiza una búsqueda primero de la propia etiqueta DMIP, si esta
	 * incluida, devolverá dicho valor. En otro caso, proseguiría con la búsqueda 
	 *  de la etiqueta TVS (Tasa de vuelta a la Susceptibilidad) pues esta es su
	 *   inversa. En caso de no encontrar ninguna de las dos, devolverá el valor 0.
	 * @return El valor de la etiqueta DMIP o TVS en su ausencia, 0 en otro caso.
	 */
	private String getDMIP() {
		String value = "0";
		//Realización al corte de la búsqueda, por eficiencia y por peso de etiqueta.
		int pos = dcvs.getColItem(Labels.DMI);
		if( pos > -1) {	value = (String) dcvs.getValueAt(0, pos);	}
		else if(getPosOp(Labels.TVS) > 0) {
			//En este caso hay que realizar la operación matemática inversa a su valor.
			pos =  getPosOp(Labels.TVS);
			value = (String) dcvs.getValueAt(0, pos);
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
		int pos = dcvs.getColItem(Labels.IP);
		if( pos > -1) {	has = (String) dcvs.getValueAt(0, pos);	}
		else if(dcvs.getColItem(Labels.TVS) > -1) has = "1";						//Se presumira que si existe esta tasa, hay IP.
		else if(getPosOp(Labels.CVS)> 0) has = "1";							//Se presume lo mismo.
		return has;
	}
	
	private void setUpProyecto() {
		//Añadir tipo y nombre
		setTypeAndName(proyecto,TypesFiles.PRJ);
		//Crear cabecera		
		String[] cabecera = {"Tipo","Dato"};
		//Añadir cabecera.
		proyecto.addCabecera(cabecera);
		//Añadir etiquetas generales.
		proyecto.addFila(new String[]{Labels.NAME, dcvs.getNombre()});
		proyecto.addFila(new String[]{Labels.NG,"" + getNG()});
//		definicionSIR.addFila(new String[]{Labels.DATE,dcvs.getDate()});
		proyecto.addFila(new String[]{Labels.DESCRIPTION,"Modelo obtenido de una fuente externa."});
		
		//Añadir ahora la ruta del resto de módulos.
//		definicionSIR.addFila(new String[]{TypesFiles.HST,historico.getNombre()});
//		definicionSIR.addFila(new String[]{TypesFiles.REL,mREL.getNombre()});
//		definicionSIR.addFila(new String[]{TypesFiles.DEF,definicionSIR.getNombre()});
		System.out.println("\nProyecto: \n" + proyecto.toString());

	}
		
	/**
	 * <p>Title: setUpHistorico</p>  
	 * <p>Description: Genera la cabecera y filas legibles por la aplicación.</p>
	 * Usando los datos de cada grupo de población (ID y Nombre) genera cada
	 * una de las columnas que requiere el histórico para ser reproducido en el
	 * player.
	 * <p>Añade también una columna para indicar la posición del tiempo<p>
	 */
	private void setUpHistorico() {
		//Añadir tipo y nombre
		setTypeAndName(historico,TypesFiles.HST);
		
		String[] cabecera = new String[NG + 1];									//Una columna por cada zona y una columna para el tiempo.
		//Generar cabecera.
		cabecera[0] = "fecha";													//Fecha se situa en la primera columna (posición 0).
		for(int i = 1; i<=NG; i++) {
			Zona z = zonas.get(i);
			cabecera[i] = z.getID() + " " + z.getName();
		}
		//Añadir la cabecera.
		historico.addCabecera(cabecera);
		
		//Añadir tantas filas como lecturas de tiempo hay.	
		for(int i = IT; i<FT ;i++) {
			String[] fila = new String[NG+1];									//Cada fila tiene NG+1 Slots (columnas).
			historico.addFila(fila);
		}
	}
	
	/* Funciones internas necesarias para el proceso ya depuradas */

	
	/**
	 * <p>Title: generateLevels</p>  
	 * <p>Description: Cálcula los niveles de contagio.</p>
	 * Los niveles de contagio se calcula por cada 100 mil habitantes y responde
	 * a la formula general 100000*(ci/(s+i+r)), siendo CI el número de casos 
	 * incidentes, S el número de sintómaticos, I el de infectados o incidentes 
	 * y R el de recuperados.
	 * <p> Esta función debe ser llamada después de haber calculado u obtenido
	 * los valores para CI, S, R e I.</p>
	 */
	private void generateLevels() {
		boolean correcto = true;							//Finaliza la lectura si hay un valor no válido.
		int contador = IT;
		Date hoy = new Date();     							//Establece el día de hoy.
		
		for(int col = 1; col<=NG; col++) {
			Zona z = zonas.get(col);
			//Se recorre para cada tipo de dato todos los registros.
			while(correcto && (contador < FT)) {
				double ci = z.getGrafica().getYValue(Labels.getWord(Labels.CI), contador);
				double s = z.getGrafica().getYValue(Labels.getWord(Labels.S), contador);
				double i = z.getGrafica().getYValue(Labels.getWord(Labels.I), contador);
				double r = z.getGrafica().getYValue(Labels.getWord(Labels.R), contador);
				if(ci > -1 && s > -1 && i > -1 && r > -1 ) {
					double nivel = 100000*(ci/(s+i+r));
					//z.addNivel("Nivel", contador,nivel);
					//Añadir fecha solo 1 vez, usar col = 1.
					if(col == 1) {
						//Añadimos valor al histórico.
						historico.setValueAt("" + nivel, contador, col);
						//Añadimos el tiempo.
						String date =  new SimpleDateFormat("dd/MM/yyyy hh:mm").format(hoy);
						historico.setValueAt(date, contador, 0);
						hoy = addDay(hoy);    									//Incrementar un día.
					}				
					contador++;													//Siguiente línea.
				} else correcto = false;		
			}
		}
	}
	
	private Date addDay(Date dt) {
		 Calendar c = Calendar.getInstance();
	        c.setTime(dt);
	        c.add(Calendar.DATE, 1);
	        return c.getTime();
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
			
			if(traza) {
//				z.getGrafica().setVisible(true);
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
				String etiqueta = Labels.getWord(op);							//Obtener el valor de la etiqueta.	
				//Caso especial para etiquetas con dos operandos.
				if( op.equals("CAB") || op.equals("TCS")) {
//					if(traza) System.out.println("ParserProyecto > addSerieXs: " + et + " > col: " + col +	" fila: " + contador + " - " + etiqueta + ", Valor: " + valor);
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
	
	/**
	 * <p>Title: crearMatriz</p>  
	 * <p>Description: Genera la matriz de contactos desde las etiquetas del 
	 * conjunto de datos.</p>
	 */
	private void setUpMatriz() {
		mREL = DCVSFactory.newREL(zonas);

		int index = getPosOp("C");
		boolean hasMC = index > -1;
		if(hasMC) {
			for(int i = 0; i<NG;i++) {
				String label = "C " + IDs[i] + " ";
				for(int j = 0; j<NG;j++) {
					index = dcvs.getColItem(label + IDs[j]);
					hasMC = index > -1;
					if(hasMC) {
						//Leer valor.
						String valor = (String) dcvs.getValueAt(0, index);
						//escribir valor en tabla
						mREL.setValueAt(valor,i,j+1);
					}
				}
			}
		}
		if(traza) System.out.println( mREL.toString() );
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
		int col = dcvs.getColItem("PT0 " + IDs[ID]);
		if(col>-1) habitantes = Integer.parseInt((String) dcvs.getValueAt(0, col));
		if(traza) System.out.println("Parser VenSim > getPeople: " + habitantes);
		return habitantes;	
	}
	
	
	private String[] getNombresGrupos() {	
		int col0 = getPosOp("C");
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
		int col = getPosOp(OP);		
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
	

	private String getFirstID(String parte) {return parte.split(" ")[1];}
	
	
	private String getSecondID(String parte) {return parte.split(" ")[2];}
	
	
	private int getPosOp(String op) {
		int pos = -1;
		int contador = 0;
		boolean encontrado = false;
		int nslots = dcvs.getColumnCount();
		
		while(!encontrado && contador < nslots) {
			if(hasOperator(op,dcvs.getColumnName(contador))) {
				encontrado = true;
				pos = contador;
			}else contador++;
		}
		
		return pos;
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


	/* Funciones para extraer la información recolectada y calculada */
	
	
	/**
	 * @return El INITIAL TIME o tiempo de inicio de la simulación.
	 */
	public int getIT() {return IT;}	

	/**
	 * @return El FINAL TIME o tiempo final de la simulación.
	 */
	public int getFT() {return FT;}

	/**
	 * @return El número de grupos de población.
	 */
	public int getNG() {return NG;}
	
	/**
	 * @return Matriz de contactos.
	 */
	public DCVS getMContactos() {return this.mREL;}
	
	/**
	 * @return Definición de la enfermedad, sus parámetros.
	 */
	public DCVS getmDefENF() {return definicionSIR;}	
	
	/**
	 * @return Descripción del modelo (proyecto), sus parámetros.
	 */
	public DCVS getmProyecto() {return proyecto;}	

	
	/**
	 * <p>Title: getZonas</p>  
	 * <p>Description: </p> 
	 * @return Los grupos de población o zonas obtenidas.
	 */
	public HashMap<Integer, Zona> getZonas() {return zonas;	}


	/**
	 * @return El historico con la representación de nivel de contagio.
	 */
	public DCVS getHistorico() {
//		System.out.println(historico.toString());
		return historico;}
	
}
