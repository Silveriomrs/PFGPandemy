/**  
* <p>Title: Labels.java</p>  
* <p>Description: Diccionario de conversión del texto de las etiquetas de la
* aplicación al idioma español (España)</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 9 nov. 2021  
* @version 1.0  
*/  
package modelo;

import java.util.HashMap;


/**
 * <p>Title: Labels</p>  
 * Diccionario de conversión del texto de las etiquetas de la
 * aplicación al idioma español (España)</p> 
 * @author Silverio Manuel Rosales Santana
 * @date 9 nov. 2021
 * @version versión
 */
public class Labels {

	/* Etiquetas parámetros de la simulación */
	/** CC Casos de curación*/  
	public final static String CC = "CC";
	/** CVS Casos de vuelta a la suceptibilidad*/  
	public final static String CVS ="CVS";
	/** CI Casos de incidentes*/  
	public final static String CI ="CI";
	//Básicas
	/** S Susceptibles*/  
	public final static String S ="S";
	/** I Incidentes*/  
	public final static String I ="I";
	/** R Recuperados*/  
	public final static String R ="R";	
	//Tasas
	/** P Prevalencia*/  
	public final static String P ="P";
	/** TC Tasa de contactos*/  
	public final static String TC ="TC";
	/** TR Tasa Recuperados o curación*/  
	public final static String TR ="TR";
	/** TVS Tasa vuelta a la susceptibilidad*/  
	public final static String TVS ="TVS";
	/** TCONTAGIO Tasa de contagio*/  
	public final static String TCONTAGIO ="TCONTAGIO";
	//Contactos y tasas de X con Z.
	/** CAB Contactos de X con Z*/  
	public final static String CAB ="CAB";
	/** TCS Tasa de contactos de X con sintomáticos en Z*/  
	public final static String TCS ="TCS";		
	//Iniciales y constantes.
	/** S0 Sintomáticos iniciales*/  
	public final static String S0 ="S0";
	/** I0 Incidentes iniciales*/
	public final static String I0 ="I0";
	/** R0 Recuperados iniciales*/  
	public final static String R0 ="R0";
	/** PT0 Población inicial*/  
	public final static String PT0 ="PT0";
	/** C100K Casos por cada 100 mil habitantes*/  
	public final static String C100K ="C100K";
	/** FT Tiempo final*/  
	public final static String FT ="FT";
	/** IT Tiempo inicial*/  
	public final static String IT ="IT";
	//Parámetros de la enfermedad
	/** PTE Probabilidad de transmisión de la enfermedad*/  
	public final static String PTE ="PTE";
	/** DME Duración media de la enfermedad*/  
	public final static String DME ="DME";
	/** IP Inmunidad permanente*/  
	public final static String IP ="IP";
	/** DMIP Duración de la inmunidad*/  
	public final static String DMI ="DMI";
	/* Etiquetas propiedades proyecto */
	/** NAME Nombre del proyecto*/
	public final static String NAME = "NAME";
	/** AUTHOR Autor del proyecto*/  
	public final static String AUTHOR = "AUTHOR";
	/** DESCRIPTION Descripción del proyecto*/  
	public final static String DESCRIPTION = "DESCRIPTION";
	/** VERSION Versión del proyecto*/  
	public final static String VERSION = "VERSION";
	/** NG Número de grupos de población*/  
	public final static String NG = "NG";
	/** DATE0 Fecha de creación del proyecto*/  
	public final static String DATE0 = "DATE0";
	/** DATE1 Fecha de modificación del proyecto*/  
	public final static String DATE1 = "DATE1";
	/* Etiquetas campos de los grupos de población */

	/** ID Identificador de un grupo de población.*/  
	public final static String ID = "ID";
	/** PEOPLE Número de habitantes de una zona.*/  
	public final static String PEOPLE = "PEOPLE";
	/** AREA Superficie de una zona.*/  
	public final static String AREA = "AREA";
	
	private static HashMap<String,String> dic;
//	private HashMap<String,Integer> nparametros;
	private static final String language = "Español";
	
	/**
	 * <p>Title: Labels</p>  
	 * <p>Description: constructor</p> 
	 */
	public Labels(){
		dic = new HashMap<String,String>();
//		nparametros = new HashMap<String,Integer>();
		addLabels();
	}
	
	/**
	 * <p>Title: addLabels</p>  
	 * <p>Description: Añade las etiquetas y sus correspondientes palabras en
	 * el idioma destino. </p>
	 */
	private static void addLabels(){
		//Casos de.
		dic.put(CC, "Casos de curación");
		dic.put(CVS, "Casos de vuelta a la suceptibilidad");
		dic.put(CI, "Casos incidentes");
		dic.put(C100K, "Nivel de contagio");
		//Básicas
		dic.put(S, "Susceptibles");
		dic.put(I, "Incidencias");
		dic.put(R, "Recuperados");	
		//Tasas
		dic.put(P, "Prevalencia");
		dic.put(TC, "Tasa de contactos");
		dic.put(TR, "Tasa de curación");
		dic.put(TVS, "Tasa vuelta a la susceptibilidad");
		dic.put(TCONTAGIO, "Tasa de contagio");
		//Contactos y tasas de X con Z.
		dic.put(CAB, "Contactos con Z");										//X y Z serán sustituidos por el ID o el nombre de los grupos correspondientes.
		dic.put(TCS, "Tasa de contactos con sintomáticos en Z");		
		//Iniciales y constantes.
		dic.put(S0, "Susceptibles iniciales");
		dic.put(I0, "Incidentes iniciales");
		dic.put(R0, "Recuperados iniciales");
		dic.put(PT0, "Población inicial");
		dic.put(FT, "Tiempo final");
		dic.put(IT, "Tiempo inicial");
		//Parámetros de la enfermedad
		dic.put(PTE,"Probabilidad de transmisión de la enfermedad");
		dic.put(DME,"Duración media de la enfermedad");
		dic.put(IP,"Inmunidad permanente");
		dic.put(DMI,"Duración media de la inmunidad");
		//Etiquetas de propiedades del proyecto.
		dic.put(NAME,"Nombre del proyecto" );
		dic.put(AUTHOR,"Autor del proyecto");
		dic.put(DESCRIPTION,"Descripción del proyecto" );
		dic.put(VERSION,"Versión del proyecto" );
		dic.put(NG,"Número de grupos de población" );
		dic.put(DATE0,"Fecha creación del proyecto");
		dic.put(DATE1,"Fecha modificación del proyecto");
		//Etiquetas particulares de las zonas.
		dic.put(ID, "Indentificador");
		dic.put(PEOPLE, "Número de habitantes");
		dic.put(AREA, "Superficie de la zona");
		
		///

		
	}
	
	/**
	 * <p>Title: existLabel</p>  
	 * <p>Description: </p> 
	 * @param label Etiqueta a buscar dentro del diccionario.
	 * @return TRUE si esta contenida (es válida). FALSE en otro caso.
	 */
	public static boolean existLabel(String label) {return dic.containsKey(label);}

	/**
	 * Devuelve el lenguaje del módulo.
	 * @return El language de la aplicación
	 */
	public static String getLanguage() {return language;}
	
	/**
	 * <p>Title: getWord</p>  
	 * <p>Description: Comprueba si existe una etiqueta dentro del diccionario
	 * en caso de que exista devuelve el texto en el idioma del diccionario</p> 
	 * @param label Etiqueta a buscar.
	 * @return Cadena de texto con la frase en el idioma, null en otro caso.
	 */
	public static String getWord(String label) {
		String word = null;
		if(dic.isEmpty()) addLabels();
		if(existLabel(label)) word = dic.get(label);
		else if(label != null) System.out.println("No está agregada al diccionario: " + label);
		return word;
	}

	
	/**
	 * <p>Title: getKey</p>  
	 * <p>Description: Devuelve la clave para un valor en el diccionario.</p>
	 * Realiza una búsqueda comparando cada valor con la entrada proporcionada.
	 *  Cuando encuentra la primera coincidencia, devuelve la clave asociada. 
	 * @param word Texto a buscar.
	 * @return La clave asociada a dicho texto. NULL en otro caso.
	 */
	public String getKey(String word) {
		String label = null;
		for (String clave:dic.keySet()) {
			String valor = getWord(clave);
			if(valor.equals(word)) {return "" + clave;}
		}
		return label;
	}
	
	
}
