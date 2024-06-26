/**  
* Contiene las etiquetas referentes a los diferentes tipos de ficheros 
*  que puede manejar la aplicación.  
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 17 nov. 2021  
* @version 1.0  
*/  
package modelo;

import java.util.HashMap;

/**
 * Clase coordinadora de los tipos de archivos, módulos, 
 * extensiones y cualquie otra vicisitud que sea requerida por el sistema.
 * Usa etiquetas para la relación con extensiones y archivos.
 * @author Silverio Manuel Rosales Santana
 * @date 17 nov. 2021
 * @version versión 1.0
 */
public class TypesFiles {
	
	private static HashMap<String,String> tipos;
    
    /** Archivos generales formato CSV*/  
    public final static String CSV = "csv";
    /** Archivo de proyecto PRJ*/  
    public final static String PRJ = "prj";
    /** Archivo de proyecto PRJ*/  
    public final static String DEF = "def";
    /** Archivo historico con la salida generada HST*/  
    public final static String HST = "hst";
    /** Archivo configuración de la paleta de colores PAL*/  
    public final static String PAL = "pal";
    /** Archivo definición gráfica de las zonas ZON*/  
    public final static String MAP = "map";
    /** Archivo definición Relaciones entre zonas */
    public final static String REL = "rel";
    /** Archivo definición grupos (zonas) de estudio */
    public final static String GRP = "grp";
    /** Archivo de Imagenes PNG */
    public final static String PNG = "png";
    /** Archivo de Imagenes JPG */
    public final static String JPG = "jpg";
    /** Archivo de Imagenes JPEG */
    public final static String JPEG = "jpeg";
    /** Archivo de Imagenes GIF */
    public final static String GIF= "gif";
    /** Archivo de Imagenes PNG, JPG p GIF */
    public final static String IMG = "imagen";
    /** Archivo de lectura PDF*/
	public final static String PDF = "pdf";
	/** Tipo de archivo Modelo A*/
	public final static String MODEL_A = "A";
	/** Tipo de archivo Modelo B*/
	public final static String MODEL_B = "B";
	/**
	 * Inicializa los valores aceptables para los tipos de archivos soportados por el sistema. 
	 */
	public TypesFiles() {configurar();}
	
    private static void configurar() {
    	tipos = new HashMap<String,String>();
    	tipos.put(PAL, Labels_GUI.PAL);
		tipos.put(DEF, Labels_GUI.DEF);
		tipos.put(CSV, Labels_GUI.CSV);
		tipos.put(HST, Labels_GUI.HST);
		tipos.put(MAP, Labels_GUI.MAP);
		tipos.put(GRP, Labels_GUI.GRP);
		tipos.put(PRJ, Labels_GUI.PRJ);
		tipos.put(REL, Labels_GUI.REL);
		tipos.put(PNG, Labels_GUI.PNG);
		tipos.put(JPG, Labels_GUI.JPG);
		tipos.put(JPEG, Labels_GUI.JPEG);
		tipos.put(GIF, Labels_GUI.GIF);
		tipos.put(IMG, Labels_GUI.IMG);
		tipos.put(PDF, Labels_GUI.PDF);
		tipos.put(MODEL_A, Labels_GUI.MODEL_A);
		tipos.put(MODEL_B, Labels_GUI.MODEL_B);
    }
   
   /**
	 * Devuelve el texto asociado a una extensión. 
	 * @param t Tipo de datos o etiqueta.
	 * @return Texto asociado a dicha etiqueta. Null en otro caso.
	 */
	public static String get(String t) {
		if(tipos.isEmpty())  configurar();
		String txt = null;
		if(hasType(t)) txt = tipos.get(t);
		else System.out.println("TypesFiles > No existe tipo: " + t);
		return txt;
		
	}
   
	/**
	 * Indica si existe dicha extensión o etiqueta. 
	 * @param t Tipo de extensión o etiqueta.
	 * @return TRUE si esta contenida, FALSE en otro caso.
	 */
	public static boolean hasType(String t) { return tipos.containsKey(t); }

}
