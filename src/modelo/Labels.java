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
	/** I Incidentes*/  
	public final static String I ="I";
	/** S Susceptibles*/  
	public final static String S ="S";
	/** R Recuperados*/  
	public final static String R ="R";	
	//Tasas
	/** P Prevalencia*/  
	public final static String P ="P";
	/** TC Tasa de contactos*/  
	public final static String TC ="TC";
	/** TR Tasa Recuperados*/  
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
	/** I0 Incidentes iniciales*/  
	public final static String I0 ="I0";
	/** S0 Sintomáticos iniciales*/  
	public final static String S0 ="S0";
	/** PT0 Población inicial*/  
	public final static String PT0 ="PT0";
	/** FT Tiempo final*/  
	public final static String FT ="FINAL TIME";
	/** IT Tiempo inicial*/  
	public final static String IT ="INITIAL TIME";
	//Parámetros de la enfermedad
	/** PTE Probabilidad de transmisión de la enfermedad*/  
	public final static String PTE ="PTE";
	/** DME Duración media de la enfermedad*/  
	public final static String DME ="DME";
	/** IP Inmunidad permanente*/  
	public final static String IP ="IP";
	/** DMIP Duración de la inmunidad permanente*/  
	public final static String DMIP ="DMIP";
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
	/** DATE Fecha de creación o modificación del proyecto*/  
	public final static String DATE = "DATE";
	
	private HashMap<String,String> dic;
//	private HashMap<String,Integer> nparametros;
	private final String language = "Español";
	
	/**
	 * <p>Title: Labels</p>  
	 * <p>Description: constructor</p> 
	 */
	public Labels(){
		dic = new HashMap<String,String>();
//		nparametros = new HashMap<String,Integer>();
		addLabelss();
	}
	
	/**
	 * <p>Title: addLabelss</p>  
	 * <p>Description: Añade las etiquetas y sus correspondientes palabras en
	 * el idioma destino. </p>
	 */
	private void addLabelss(){
		//Casos de.
		dic.put(CC, "Casos de curación");
		dic.put(CVS, "Casos de vuelta a la suceptibilidad");
		dic.put(CI, "Casos incidentes");
		//Básicas
		dic.put(I, "Incidencias");
		dic.put(S, "Susceptibles");
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
		dic.put(I0, "Incidentes iniciales");
		dic.put(S0, "Susceptibles iniciales");
		dic.put(PT0, "Población inicial");
		dic.put("FINAL TIME", "Tiempo final");
		dic.put("INITIAL TIME", "Tiempo inicial");
		//Parámetros de la enfermedad
		dic.put(PTE,"Probabilidad de transmisión de la enfermedad");
		dic.put(DME,"Duración media de la enfermedad (en días)");
		dic.put(IP,"Inmunidad permanente");
		dic.put(DMIP,"Duración media de la inmunidad permanente (en días)");
		//Etiquetas de propiedades del proyecto.
		dic.put(NAME,"Nombre del proyecto" );
		dic.put(AUTHOR,"Autor del proyecto");
		dic.put(DESCRIPTION,"Descripción del proyecto" );
		dic.put(VERSION,"Versión del proyecto" );
		dic.put(NG,"Número de grupos de población" );
		dic.put(DATE,"Fecha del proyecto");
	}
	
	/**
	 * <p>Title: existLabel</p>  
	 * <p>Description: </p> 
	 * @param label Etiqueta a buscar dentro del diccionario.
	 * @return TRUE si esta contenida (es válida). FALSE en otro caso.
	 */
	public boolean existLabel(String label) {return dic.containsKey(label);}

	/**
	 * Devuelve el lenguaje del módulo.
	 * @return El language de la aplicación
	 */
	public String getLanguage() {return language;}
	
	/**
	 * <p>Title: getWord</p>  
	 * <p>Description: Comprueba si existe una etiqueta dentro del diccionario
	 * en caso de que exista devuelve el texto en el idioma del diccionario</p> 
	 * @param label Etiqueta a buscar.
	 * @return Cadena de texto con la frase en el idioma, null en otro caso.
	 */
	public String getWord(String label) {
		String word = null;
		if(existLabel(label)) word = dic.get(label);
		else System.out.println("No está agregada al diccionario: " + label);
		return word;
	}
	
	
}
