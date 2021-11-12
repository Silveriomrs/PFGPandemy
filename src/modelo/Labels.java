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
		addWords();
	}
	
	/**
	 * <p>Title: addWords</p>  
	 * <p>Description: Añade las etiquetas y sus correspondientes palabras en
	 * el idioma destino. </p>
	 */
	private void addWords(){
		//Casos de.
		dic.put("CC", "Casos de curación");
		dic.put("CVS", "Casos de vuelta a la suceptibilidad");
		dic.put("CI", "Casos incidentes");
		//Básicas
		dic.put("I", "Incidencias");
		dic.put("S", "Susceptibles");
		dic.put("R", "Recuperados");
		dic.put("P", "Prevalecia");
		//Tasas
		dic.put("TC", "Tasa de contactos");
		dic.put("TCONTAGIO", "Tasa de contagio");
		//Contactos y tasas de X con Z.
		dic.put("CAB", "Contactos con Z");										//X y Z serán sustituidos por el ID o el nombre de los grupos correspondientes.
		dic.put("TCS", "Tasa de contactos con sintomáticos en Z");		
		//Iniciales
		dic.put("I0", "Incidencias iniciales");
		dic.put("S0", "Susceptibles iniciales");
		dic.put("PT0", "Población inicial");
		dic.put("FINAL TIME", "Tiempo final");
		dic.put("INITIAL TIME", "Tiempo inicial");
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
		return word;
	}
	
	
}
