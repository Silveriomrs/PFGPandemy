/**  
* <p>Description: Clase general para la importación de tablas realizadas con
*  herramientas externas.</p>
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 11 may. 2022  
* @version 2.2 
*/  
package controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import modelo.DCVS;
import modelo.DCVSFactory;
import modelo.Labels;
import modelo.TypesFiles;
import modelo.Zona;

/**
 * <p>Description: Clase de donde heredarán los parses de modelos.</p>  
 * @author Silverio Manuel Rosales Santana
 * @date 11 may. 2022
 * @version versión 2.2
 */
public abstract class ParserModelo {

	protected DCVS dcvs;														//Conjunto de datos importados desde VenSim.
	protected DCVS mREL;														//Matriz de contactos (relaciones).
	protected DCVS mDEF;														//Módulo definición de la enfermedad (SIR)
	protected DCVS mPRJ;														//Módulo proyecto.
	protected DCVS mHST;														//Módulo histórico
	protected DCVS mMAP;
	protected DCVS mPAL;
	protected String[] IDs;														//Almacena los nombres de los grupos.
	protected HashMap<Integer,Zona> zonas;
	protected int NG;															//Número de grupos de población.
	protected int IT;															//Tiempo inicial de la simulación.
	protected int FT;															//Tiempo final de la simulación.
	protected int nLabels;														//Número de etiquetas totales del archivo de entrada.
	
	/**
	 * <p>Description: Inicializar el parser con los datos obtenidos por parámetro.</p>  
	 * @param prjV Conjunto de datos del proyecto VenSim
	 */
	public ParserModelo(DCVS prjV) {
		this.dcvs = prjV;
		this.zonas = new HashMap<Integer,Zona>();
		this.mREL = new DCVS();
		this.mDEF = DCVSFactory.newModule(TypesFiles.DEF);
		this.mPRJ = DCVSFactory.newModule(TypesFiles.PRJ);
		this.mMAP = DCVSFactory.newModule(TypesFiles.MAP);
		this.mPAL = DCVSFactory.newModule(TypesFiles.PAL);	
		
		this.NG = 0;
		IT = FT = 0;
		if(prjV != null) {
			setTypeAndName(mDEF,TypesFiles.DEF);
			setTypeAndName(mPAL,TypesFiles.PAL);
			setTypeAndName(mMAP,TypesFiles.MAP);
			setNLabels();
			importar();
		}
	}
	
	/**
	 * <p>Description: Importa los datos de un archivo generado con VenSim.</p>
	 * Esta opción elimina el resto de datos actuales de los módulos implicados.
	 */
	private void importar() {
		//Obtención de los nombres de cada grupo.
		this.IDs = getNombresGrupos();
		
		//Establecer módulo proyecto.
		crearMPRJ();
		
		//Obtención de los tiempos inicial y final.
	    readTimes();
	    
	    //Crea la tabla histórico (SIN DATOS).
	    crearMHST();
	    
		//Crear tantas Zonas como Grupos.
		crearZonas();
		
		//Crear tabla de grupos de población (MAP).
		crearMMAP();
		
		//Leer y crear tabla de relaciones.
		crearMREL();
		
		//Crear contenedor de definición de enfermedad (Parámetros SIR).
		crearMDEF();
		
		//Leer R,S,I
		readXs(Labels.R);
		readXs(Labels.S);
		readXs(Labels.I);
		
		//Leer tasas TC, TCONTAGIO
		readXs(Labels.TC);
		readXs(Labels.TCONTAGIO);
		
		//Leer casos de CC,CVS,CI
		readXs(Labels.CC);
		readXs(Labels.CVS);
		if(getPosOp(Labels.CI) > -1) {readXs(Labels.CI);}						//Añadir la serie localizada a la zona indicada	
		else {calcCI();	}														//En otro caso calcular las prevalencias.
		
		//Leer o calcular Prevalencia.
		//Debe realizarse después de haber obtenido R,S,I e CI.
		if(getPosOp(Labels.P) > -1) {readXs(Labels.P);}							//Añadir la serie localizada a la zona indicada	
		else {calcPs();}														//En otro caso calcular las prevalencias.
		
		//Leer o calcular el nivel de contagio C100K
		if(getPosOp(Labels.C100K) > -1) {readXs(Labels.C100K);}					//Añadir la serie localizada a la zona indicada	
		else {calcC100K();}														//En otro caso calcularos.
		
		//Leer casos de contactos de A con B y tasas de A con B
		readXenZ(Labels.TCS);
		readXenZ(Labels.CAB);		
	}
	
	/* Funciones para el manejo de datos básicos */
	
	/**
	 * <p>Description: Establece un nombre y un tipo de datos al módulo.</p>
	 * Usa los datos almacenados en el archivo de origen para establecer los
	 *  nombres a los módulos particulares. 
	 * @param modulo Módulo al que establecer los atributos de Tipo y Nombre.
	 * @param type Tipo de datos que contiene el módulo. Ver: \ref modelo#TypesFiles
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
	 * <p>Description: Función auxiliar que permite cambiar las etiquetas particulares X y Z de 
	 *   de una etiqueta con dos IDs, por sus correspondientes identificadores de grupos de población</p> 
	 * @param et Etiqueta completa con sus sub-etiquetas X y Z.
	 * @return Nueva etiqueta recompuesta con los identificadores personalizados.
	 */
	protected String changeXY(String et) {
		String label = et;
		label = label.replaceFirst("X", IDs[1]);								//Sustituir primera etiqueta.
		label = label.replaceFirst("Z", IDs[2]);								//Sustituir segunda etiqueta.
		return label;
	}
	
	/**
	 * <p>Description: Indica si la sintaxis de una cadena de comando y operadores
	 * contiene un operador o comando determiando.</p>
	 * Los comandos y operadores deben estar separados por espacios.
	 * @param op Operador buscado.
	 * @param txt Cadena de texto donde buscar el comando u operador.
	 * @return TRUE si la cadena contiene dicho operador, FALSE en otro caso.
	 */
	protected boolean hasOperator(String op, String txt) {
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
	 * <p>Description: Obtener el primer ID de una cadena de texto. </p> 
	 * @param parte Una cadena de texto parcial de la que extraer el primer ID.
	 * @return Primer ID encontrado.
	 */
	protected String getFirstID(String parte) {return parte.split(" ")[1];}
	
	/**
	 * <p>Description: Obtener el segundo ID de una cadena de texto. </p> 
	 * @param parte Una cadena de texto parcial de la que extraer el segundo ID.
	 * @return Segundo ID encontrado.
	 */
	protected String getSecondID(String parte) {return parte.split(" ")[2];}
	
	/**
	 * <p>Description: Devuelve el valor correspondiente a la etiqueta en el 
	 *  slot de tiempo indicado.</p>
	 * Esta función trabaja con la tabla mHST por tanto antes de usarse debería
	 *  asegurarse de que existe ya dicha etiqueta en la tabla. Además esta función
	 *   no trabaja con posiciones absolutas sino con tiempo concreto. 
	 * @param label Etiqueta de la que obtener el dato.
	 * @param slot Slot de tiempo del que obtener el dato.
	 * @return Valor en esa posición. 0.0 En otro caso.
	 */
	protected double getValueSlot(String label, int slot) {
		double v = 0.0;
		//búsqueda desde mHST no desde la tabla de entrada dcvs
		int index = mHST.getFilaItem(label); 
		if(index > -1) {
			String valor = ((String) mHST.getValueAt(index,slot + 1)).replace(",", ".");
			v = Double.parseDouble(valor);
		} else System.out.println("Error, ParserModelo > getValueSlot -> No encontrada etiqueta: " + label);
		return v;
	}
	
	/**
	 * <p>Description: Realiza una búsqueda de un operador por todas las filas
	 * contenedoras de las etiquetas de la primera columna.</p> 
	 * @param op Operador búscado, es decir, la etiqueta.
	 * @return Número de línea donde esta situado dicho operador. -1 Sino existe.
	 */
	protected int getPosOp(String op) {
		int pos = -1;
		int contador = 0;
		boolean encontrado = false;
		//Bucle de búsqueda.
		while(!encontrado && contador < nLabels) {
			if(hasOperator(op,getLabel(contador))) {
				encontrado = true;
				pos = contador;
			}else contador++;
		}
		return pos;
	}
	
	/* Funciones para la insercción de fechas en vez de unidades de tiempo*/
	
	/**
	 * Genera tiempos en formato fecha, añadiendolos a la cabecera del histórico.
	 * Esta función usa la fecha actual.
	 */
 	private void generateTimeStamps() {
		int contador = IT;
		//Establecer el día de hoy como día inicial.
		Date hoy = new Date();
		//crear array con la secuencia de fechas en incrementos de 1 día.
		while(contador < FT) {
			//Añadimos el tiempo en el formato deseado.
			String date =  new SimpleDateFormat("dd/MM/yyyy hh:mm").format(hoy);
			//Renombrar la columna con la fecha.
			mHST.setColumnName(contador + 1, date);
			//Incrementar un día.
			hoy = addDay(hoy);
			//Siguiente línea.
			contador++;
		}
	}
	
	/**
	 * <p>Añade un día a la fecha en curso.</p>
	 * Usada para dar un formato de fecha a las unidades tiempo. 
	 * @param dt Fecha a la que agregar un día.
	 * @return Fecha actualizada.
	 */
	private Date addDay(Date dt) {
		 Calendar c = Calendar.getInstance();
	        c.setTime(dt);
	        c.add(Calendar.DATE, 1);
	        return c.getTime();
	}
	
	/* Funciones para la creación de las tablas */
	
	/**
	 * <p>Description: Inicia y establece los valores iniciales para el módulo
	 *  del proyecto.</p>
	 *  Esta función debe ejecutarse después de haber obtenido el valor de NG.
	 */
	protected void crearMPRJ() {
		//Añadir tipo y nombre
		setTypeAndName(mPRJ,TypesFiles.PRJ);
		//Añadir etiquetas generales.
		mPRJ.setDataToLabel(Labels.NAME, dcvs.getNombre());
		mPRJ.setDataToLabel(Labels.NG,"" + getNG());
		mPRJ.setDataToLabel(Labels.DESCRIPTION,"Modelo de enfermedad de transmisión.");		
	}
	
	/**
	 * <p>Description: Crea la tabla con las etiquetas mínimas</p>
	 * No guarda datos. Los datos se añaden posteriormente desde las funciones
	 *  que a continuación se indican.
	 * @see #readXs(String)
	 * @see #readXenZ(String)
	 */
 	protected void crearMHST() {
		mHST = DCVSFactory.newHST(FT);
		setTypeAndName(mHST,TypesFiles.HST);
		generateTimeStamps();
	}
	
	/**
	 * <p>Description: Genera el módulo definición de la enfermedad.</p>
	 * Para ello genera una lista con las etiquetas que deben estár incluidas,
	 *  por tanto si se quiere incluir nuevas etiquetas, este es el lugar donde deberá
	 *   hacerser.
	 *  <p>Las etiquetas incluidas son: </p>
	 *  - PTE 
	 *  - DME 
	 *  - DMIP 
	 *  - IP 
	 *  - IT
	 *  - TI
	 *  <P>La Inmunidad Permanente IP, aunque no esté específicada de forma explícita, 
	 *   puede estar implicita en la presencía de las etiquetas CVS o TVS. Por
	 *    tanto, se realiza una búsqueda encadenada de las tres etiquetas.</P>
	 *  Para más información consultar \ref modelo#Labels .
	 */
	protected void crearMDEF(){
		//Lista de etiquetas a buscar de los que obtener sus valores.
		ArrayList<String> lista = new ArrayList<String>();
		lista.add(Labels.PTE);
		lista.add(Labels.DME);
		//Procesar la lista añadiendo dichos campos al módulo mDEF.
		for(int i = 0; i<lista.size(); i++) {
			String etiqueta = lista.get(i);
			String value = "0";													//En caso de no estar, se configurará a 0.
			Object v = getLabelValue(etiqueta);		
			if(v != null) {value = ((String) v).replace(",", ".");}
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
	}
	
	/**
	 * <p>Description: Crea las zonas o grupos de población con los parámetros
	 * almacenados en el conjunto de datos. Además añade el número de habitantes
	 * inicial cuando esté disponible, en otro caso el número de habitantes será
	 * cero.</p>
	 */
	protected void crearZonas() {
		for(int i = 0; i<NG; i++) {
			int superficie = 0;													//En este modelo VenSim no incluye superficie.	
			//Crear zonas con valores iniciales.
			zonas.put(i+1,new Zona(i+1, IDs[i], 0, superficie,0,0,0,0,0, null));
			//Obtener población inicial (si hay) PT0,R,S,I.
			setPeople(i);
		}
	}
	
	/**
	 * <p>Description: Genera la matriz de contactos desde las etiquetas del 
	 * conjunto de datos.</p>
	 */
	protected void crearMREL() {
		mREL = DCVSFactory.newREL(zonas);										//Obtiene la tabla configurada.
		setTypeAndName(mREL,TypesFiles.REL);									//Establece los atributos del nombre y tipo de módulo.
		//Búsqueda de la primera coincidencia con el operador "C".
		int index = getPosOp("C");
		//En caso de existir procesa.
		if(index > -1) {
			//Blucle para cada uno de los grupos de población
			for(int i = 0; i<NG;i++) {
				//Componer la etiqueta a buscar.
				String label = "C " + IDs[i] + " ";								
				for(int j = 0; j<NG;j++) {
					//Búsqueda de la etiqueta para el grupo de población en curso.
					Object v = getLabelValue(label + IDs[j]);
					//Si es valor válido escribirlo en la tabla.
					if(v != null) {	mREL.setValueAt(v,i,j+1);}
				}
			}
		}	
	}
	
	/**
	 * <p>Description: Genera el módulo de los grupos de población (MAP).</p>
	 * Después de realizar la lectura de las zonas y sus valores, se debe ejecutar
	 * este método para generar el modulo de mapa. (sin representaciones gráficas).
	 */
	protected void crearMMAP() {
		for(int i = 1; i <= NG; i++) {
			Zona z = zonas.get(i);
			String[] datos = z.toString().split(",");
			mMAP.addFila(datos);
		}
	}
	
	
	/* Funciones de lectura de datos no variables */
	
	/**
	 * <p>Description: Establece los tiempos inicial y final de la simulación
	 * en los campos correspondientes.</p>
	 */
	private void readTimes() {
		Object v = null;
		//Tiempo inicial.
		v = getLabelValue(Labels.IT);
		if(v != null) IT = Integer.parseInt((String) v);										//IT esta inicializado a 0. En caso de no estar declarado será su valor.
		else IT = 0;
		//Tiempo Final.
		v = getLabelValue(Labels.FT);
		if(v != null) FT = Integer.parseInt((String) v);
		else FT = getTimeSlots();												//Restar la columna de las estiquetas.
	}
	
	/**
	 * <p>Description: Indica si existe la Inmunidad Permanente o no.</p>
	 * Para ello, realiza una búsqueda muy específica de las etiquetas IP, CVS y
	 *  TVS, de existir alguna de ellas se entenderá que si existe dicha inmunidad.
	 *   La búsqueda se realiza en corte, búscando primero la propia etiqueta, de
	 *   existir, será la que indique su valor, en otro caso realiza una búsqueda de
	 *    otras etiquetas {TVS,CVS}, si encuentra alguna, se supondrá que existe IP.
	 * @return 1 Si existe IP, 0 en otro caso.
	 */
	private String hasIP() {
		String has = "0";
		Object v = null;
		//Realización al corte de la búsqueda, por eficiencia y por peso de etiqueta.
		v = getLabelValue(Labels.IP);
		if( v != null) {has = (String) getLabelValue(Labels.IP);}
		else if(getLabelValue(Labels.TVS) != null) has = "1";				//Se presumira que si existe esta tasa, hay IP.
		else if(getPosOp(Labels.CVS)> 0) has = "1";								//Se presume lo mismo.
		return has;
	}
	
	/**
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
		Object v = getLabelValue(Labels.DMI);
		if(v != null) { value = ((String) v).replace(",", ".");}
		else if((v = getLabelValue(Labels.TVS)) != null) {
			value = ((String) v).replace(",", ".");
			double d = Double.parseDouble(value);
			value = "" +  1/d;
		}	
		return value;
	}
	
	/**
	 * <p>Description: Establece el número de habitantes para la zona indicada</p> 
	 * Los habitantes que van a establecerse son los parámetros iniciales S,R,I,
	 * PT0 así como el nivel inicial de contagio C100K, en caso de no haber tales
	 *  etiquetas el valor inicial será el valor por defecto (0).
	 * @param ID Número identificador de la zona tal y como consta en la fuente de origen.
	 *  Se debe tener en cuenta que los indices de las zonas comienzan por 1, pero en los archivos
	 *   de entrada como el de este parser, comienzan por 0.
	 */
	private void setPeople(int ID) {
		Object v = null;
		int habitantes = 0;
		int s = 0;
		int i = 0;
		int r = 0;
		String name = IDs[ID];
		//Leer datos iniciales básicos. S0,R0,I0 y Población total (PT0).
		//S
		v = getLabelValue(Labels.S0 + " " + name);
		if(v != null) s = Integer.parseInt((String) v);
		else {
			//Obtener su valor por primer valor de su serie.
			v = getLabelValue(Labels.S + " " + name);
			s = Integer.parseInt((String) v);
		}
		//R
		v = getLabelValue(Labels.R0 + " " + name);
		if(v != null) r = Integer.parseInt((String) v);
		else {
			v = getLabelValue(Labels.R + " " + name);
			r = Integer.parseInt((String) v);
		}
		//I
		v = getLabelValue(Labels.I0 + " " + name);
		if(v != null) i = Integer.parseInt((String) v);
		else {
			//Obtener su valor por primer valor de su serie.
			v = getLabelValue(Labels.I + " " + name);
			i = Integer.parseInt((String) v);
		}
		//PT0
		v = getLabelValue(Labels.PT0 + " " + name);
		if(v != null) habitantes = Integer.parseInt((String) v);				//Si existe la etiqueta concreta se toma su valor.
		else habitantes = s + r + i;											//En otro caso realizar suma de S,R e I

		Zona z = zonas.get(ID + 1);												//Las zonas tienen un incremento de 1 en los indices.
		z.setR(r);
		z.setI(i);
		z.setS(s);
		z.setPoblacion(habitantes);
	}

	/**
	 * <p>Description: Obtener todos los IDs contenidos en el módulo y establecer
	 *  el número total de grupos de población.</p>
	 * Esta función realiza una búsqueda de todas las etiquetas que contienen el denominador
	 *  inicial "R" común a todos los grupos de población.
	 *   Son procesadas todas las coincidencias separando los identificadores de dichos grupos de población.
	 * @return Arreglo de cadenas de texto con los IDs de los grupos de población.
	 */
	private String[] getNombresGrupos() {
		String op = Labels.R;													//Operador de referencia.
		String parte;															//Registro temporal para las lecturas de etiquetas.
		ArrayList<String> grupos = new ArrayList<String>();
		//Buscar primera ocurrencia de la etiqueta.
		int pos0 = getPosOp(op);
		while(pos0 > 0) {
			// Obtener la ID de la etiqueta.
			parte = getLabel(pos0);
			//Comprobar si el Operador de esa posición es válido.
			if(hasOperator(op,parte)) {
				//Añadir el nombre a la lista.
				grupos.add(getFirstID(parte));	
				//Incrementar el contadores de grupos encontrados y siguiente posición.
				NG++;
				pos0++;
			}else pos0 = -1;													//En otro caso se termina la búsqueda de más grupos.
		}
		//Conversion de la lista en un arreglo.
		String[] nombres = new String[NG];
		for(int i = 0; i < NG; i++) nombres[i] = grupos.get(i);
		return nombres;
	}
	
	/* Funciones para el cálculo de datos complejos no incluidos en la tabla
	 *  de entrada. Estas funciones trabajan con la tabla generada mHST. */
	
	/**
	 * <p>Description: Realiza el cálculo de los valores de la prevalencia "P",
	 *   de cada grupo de población.</p>
	 *  Esta función es invocada cuando la fuente no cotiene la etiqueta de prevalencia
	 *   adecuada a tal efecto.
	 * <p>Usa de los valores previamente almacenados de R,S e I de cada zona.</p>
	 */
	protected void calcPs() {
		for(int i = 1; i<=NG; i++) {
			//obtener zona
			Zona z = zonas.get(i);
			String name = z.getName();
			//Crear arrays para contener los datos.
			String[] filaP = new String[FT + 1];
			//Añadir etiquetas.
			filaP[0] = Labels.P + " " + name;
			//Obtener datos
			//Proceso para todos los slots de tiempo.
			for(int j = IT; j < FT; j++) {
				//Obtener los valores S,R e I correspondientes al mismo tiempo.
				//valor Sj.
				double vs = getValueSlot(Labels.S + " " + name,j);
				//valor Ij.
				double vi = getValueSlot(Labels.I + " " + name,j);
				//valor Rj.
				double vr = getValueSlot(Labels.R + " " + name,j);

				//Cálculo de la prevalencia instantánea. P = I/(R+I+S)
				double suma = vs+vi+vr;											//Evitar división por cero.
				if(suma > 0) {
					double pj = vi/suma;
					//Guardado de la prevalencia.
					z.addNivel(Labels.P, j, pj);
					filaP[j+1] = "" + pj;
				}
				
			}
			
			//Añadir la nueva fila con los datos al módulo histórico.
			mHST.addFila(filaP);
		}
	}
	
	/**
	 * <p>Description: Cálcula los casos de incidentes usando la tasa de contagio y
	 *  el número de sintomáticos. CI = TCONTAGIOi*Si</p>
	 * Esta función es usada cuando no existe la etiqueta explicitamente.
	 */
	protected void calcCI() {
		for(int i = 1; i<=NG; i++) {
			//obtener zona
			Zona z = zonas.get(i);
			String name = z.getName();
			//Crear arrays para contener los datos.
			String[] filaCI = new String[FT + 1];
			//Añadir etiquetas.
			filaCI[0] = Labels.CI + " " + name;
			//Obtener datos
			//Proceso para todos los slots de tiempo.
			for(int j = IT; j < FT; j++) {
				//Obtener los valores S y TCONTAGIO correspondientes al mismo tiempo.
				//valor Sj.
				double vs = getValueSlot(Labels.S + " " + name,j);
				//valor TCONTAGIOj.
				double vTCONTAGIO = getValueSlot(Labels.TCONTAGIO + " " + name,j);
				// Calcular pues con CI = TCONTAGIOi*Si
				double vCI = vTCONTAGIO*vs;
				//Guardado del Nivel de contagio.
				z.addNivel(Labels.CI, j, vCI);
				filaCI[j+1] = "" + vCI;
			}
			//Añadir las nuevas filas con los datos al módulo histórico.
			mHST.addFila(filaCI);
		}
	}
	
	/**
	 * <p>Description: Cálcula los niveles de contagio de los grupos de población
	 *  usando los datos que intervienen en su cálculo.</p>
	 * Esta función es usada cuando no existe la etiqueta explicitamente.
	 */
	protected void calcC100K() {
		for(int i = 1; i<=NG; i++) {
			//obtener zona
			Zona z = zonas.get(i);
			String name = z.getName();
			//Crear arrays para contener los datos.
			String[] filaC100K = new String[FT + 1];
			//Añadir etiquetas.
			filaC100K[0] = Labels.C100K + " " + name;
			//Obtener datos
			//Proceso para todos los slots de tiempo.
			for(int j = IT; j < FT; j++) {
				//Obtener los valores S,R e I correspondientes al mismo tiempo.
				//valor Sj.
				double vs = getValueSlot(Labels.S + " " + name,j);
				//valor Ij.
				double vi = getValueSlot(Labels.I + " " + name,j);
				//valor Rj.
				double vr = getValueSlot(Labels.R + " " + name,j);
				//valor CIj.
				double vci = getValueSlot(Labels.CI + " " + name,j);
				//Cálculo casos incidentes por cada 100 mil habitantes = 
				// 100000*(CI/(S+I+R))
				double suma = vs+vi+vr;											//Evitar división por cero.
				double ci100K = 0.0;
				if(suma > 0) {ci100K = 100000*vci/suma;}
				z.addNivel(Labels.C100K, j, ci100K);							//Guardado del Nivel de contagio.
				filaC100K[j+1] = "" + ci100K;
				//Establecer estos valores iniciales en los grupos de población (zonas)
				if(j==0) {z.setNivel((int) ci100K);}
			}
			//Añadir las nuevas filas con los datos al módulo histórico.
			mHST.addFila(filaC100K);
		}
	}
	
	/* Funciones de lectura de series de datos de las tablas */
	
	/**
	 * <p>Description: Lee los datos referentes a las etiquetas que contienen
	 * una relación directa entre dos grupos de población.</p>
	 * Por ejemplo TCS y CAB. 
	 * @param et Etiqueta a leer.
	 */
	protected void readXenZ(String et) {
		//Primera ronda para el primer parámetro.
		for(int i = 1; i<=NG; i++) {
			Zona z = zonas.get(i);
			//Segunda ronda para el segundo parámetro.
			for(int j=0; j<NG; j++) {
				//Crear nombre columna.
				String nameS = et + " " + z.getName() + " " + IDs[j];
				int pos = getIndexLabel(nameS);
				//Añadir la serie si ha sido localizada a la zona indicada.
				if(pos > -1) {addSerieXs(nameS,pos,z);}
			}
		}
	}
	
	/**
	 * <p>Description: Realiza una lectura de la columna indicada por la etiqueta
	 * añadiendo los valores de dicha columna a la gráfica correspondiente a cada
	 * grupo de estudio (zona)</p> En caso de no existir dicha etiqueta en la fuente de
	 * datos, no se añadirán.
	 * @param et Etiqueta u operador del que obtener los datos. Ej: R,S,I,P,etc.
	 */
	protected void readXs(String et) {
		//Primero añadir las series básicas R,S e I a cada zona.
		for(int i = 1; i<=NG; i++) {
			Zona z = zonas.get(i);
			String sID = et + " " + z.getName();								//Compone la etiqueta particular a la zona.
			int pos = getIndexLabel(sID);									//Obtiene la posición de dicha etiqueta.
			if(pos > -1) addSerieXs(et,pos,z);									//Añadir la serie localizada a la zona indicada	
		}
	}	
	
	/* Funciones operacionales abstractas */
	
	/**
	 * <p>Description: Devuelve la etiqueta en la posición indicada.</p>
	 * Permite abstraer la búsqueda de etiquetas de la distribución de las etiquetas.
	 * @param index Posición de la etiqueta a obtener.
	 * @return El contenido de la etiqueta en la posición indicada.
	 */
	abstract protected String getLabel(int index);
	
	/**
	 * <p>Description: Establece el número de etiquetas totales que contiene el
	 *  documento.</p>
	 *  Esta función permite establecer el límite de búsqueda dentro de los módulos.
	 */
	abstract protected void setNLabels();
		
	/**
	 * <p>Description: Obtiene la posición dentro del espacio de etiquetas donde 
	 *  está localizada la coincidente con la búscada.</p>
	 *  Esta función busca en la tabla general fuente de la importación de datos.
	 * @param label Etiqueta a buscar.
	 * @return Posición de la etiqueta encontrada. -1 en otro caso.
	 */
	abstract protected int getIndexLabel(String label);
	
	/**
	 * <p>Description: Devuelve el valor asociado a una etiqueta dentro de un módulo</p>
	 * <p>Esta función desacopla la implementación por columnas o filas.</p>
	 *  Esta función busca en la tabla general fuente de la importación de datos.
	 * @param label Etiqueta a buscar.
	 * @return El valor asociado a la etiqueta, Null en otro caso.
	 */
	abstract protected Object getLabelValue(String label);
	
	/**
	 * <p>Description: Agrega la información de una serie de datos a la gráfica
	 * correspondiente a una zona.</p> 
	 * @param et Etiqueta u operador correspondiente de la columna.
	 * @param pos Posición en la que está situada dicha etiqueta.
	 * @param z Zona a la que añadir los valores correspondientes a la serie indicada en la etiqueta.
	 */
	abstract protected void addSerieXs(String et, int pos, Zona z);
	
	/**
	 * <p>Description: Cuenta el número de filas o columnas dedicados a los intervalos
	 *  de tiempo y devuelve el resultado.</p>
	 * Esta función no comprueba que el formato sea válido.
	 * @return El número de intervalos de tiempo contenidos en el histórico.
	 */
	abstract protected int getTimeSlots();
	
	/* Funciones para devolver los resultados de la conversión **/ 
		
	/**
	 * @return El INITIAL TIME o tiempo de inicio de la simulación.
	 */
	public int getIT() {return IT;}

	/**
	 * @return El FINAL TIME o tiempo final de la simulación.
	 */
	public int getFT() {return FT;}

	/**
	 * <p>Title: getNG</p>  
	 * <p>Description: Devuelve el número de grupos de población.</p> 
	 * @return Número de grupos de población.
	 */
	public int getNG() {return this.NG;}

	/**
	 * @return El/la zonas
	 */
	public HashMap<Integer, Zona> getZonas() {return zonas;}
	
	/**
	 * @return Matriz de contactos.
	 */
	public DCVS getREL() {return mREL;}

	/**
	 * @return Obtiene el módulo de grupos de población.
	 */
	public DCVS getMAP() {return mMAP;}

	/**
	 * @return Definición de la enfermedad, sus parámetros.
	 */
	public DCVS getDEF() {return this.mDEF;}
	
	/**
	 * <p>Title: getMPRJ</p>  
	 * <p>Description: Devuelve el módulo del proyecto configurado.</p> 
	 * @return Módulo Proyecto.
	 */
	public DCVS getPRJ() {return this.mPRJ;}
	
	/**
	 * <p>Title: getMHST</p>  
	 * <p>Description: Devuelve el módulo histórico con los valores obtenidos</p> 
	 * @return Módulo histórico, con los datos leídos y los calculados a partir de estos.
	 */
	public DCVS getHST() {return this.mHST;}
	
	/**
	 * <p>Title: getPAL</p>  
	 * <p>Description: Devuelve una paleta de colores (leyenda) por defecto.</p>
	 * El módulo almacena la configuración por defecto del resto del proyecto del parser.
	 * @return Módulo paleta de colores (leyenda).
	 */
	public DCVS getPAL() {return this.mPAL;}

}
