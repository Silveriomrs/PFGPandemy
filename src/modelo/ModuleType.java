/**  
* Contiene las etiquetas comunes a los diferentes tipos
*  de ficheros que maneja la aplicación.
*  Cada etiqueta tiene asociado su significado completo.
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 24 nov. 2021  
* @version 1.0  
*/  
package modelo;

/**
 * Definición de los tipos de módulos que maneja la aplicación. 
 * Su objetivo principal es brindar de una base que permita usar enumerados de manera
 *  que sea en todo momento una referencia para coordinar los valores. Además, sus valores
 *  permiten usar su nombre completo como opciones o parte de los mensajes y no únicamente
 *   las siglas.
 * @author Silverio Manuel Rosales Santana
 * @date 24 nov. 2021
 * @version versión 1.0
 */
public enum ModuleType {
	/** Archivos generales formato CSV*/ 
	CSV("General"),
	/** Archivo de proyecto que contiene el módelo PRJ*/  
	PRJ("Modelo"),
	/** Definición de los parámetros de la enfermedad DEF*/  
	DEF("Definición"),
	/** Archivo historico con la salida generada HST*/ 
	HST("Histórico"),
	/** Archivo configuración de la paleta de colores PAL*/ 
	PAL("Paleta"),
	/** Archivo definición gráfica de las zonas MAP*/ 
	MAP("Mapas"),
	/** Definición Relaciones entre zonas */
	REL("Relaciones"),
	/** Definición grupos (zonas) de estudio */
	GRP("Grupos"),
	/** Reproductor de histórico PLAYER*/  
	PLAYER("Player");

    private final String display;
    private ModuleType(String s) { display = s; }

    @Override
    public String toString() { return display; }
}