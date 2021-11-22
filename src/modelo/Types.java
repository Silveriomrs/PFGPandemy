/**  
* <p>Title: Types.java</p>  
* <p>Description: </p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 17 nov. 2021  
* @version 1.0  
*/  
package modelo;

import java.util.HashMap;

/**
 * <p>Title: Types</p>  
 * <p>Description: Clase coordinadora de los tipos de archivos, módulos, 
 * extensiones y cualquie otra vicisitud que sea requerida por el sistema.</p>
 * Usa etiquetas para la relación con extensiones y archivos.
 * @author Silverio Manuel Rosales Santana
 * @date 17 nov. 2021
 * @version versión 1.0
 */
public class Types {
	
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
	

	/**
	 * <p>Title: </p>  
	 * <p>Description: Inicializa los valores aceptables para los tipos de
	 * archivos soportados por el sistema.</p>  
	 */
	public Types() {configurar();}
	
   private static void configurar() {
    	tipos = new HashMap<String,String>();
    	tipos.put(PAL,"Paleta de colores");
    	tipos.put(DEF,"Archivo definición de enfermedad");
    	tipos.put(CSV, "Archivo general");
    	tipos.put(HST, "Archivo historico");
    	tipos.put(MAP, "Archivo de mapa");
    	tipos.put(GRP, "Archivo definición de grupos");
    	tipos.put(PRJ, "Archivo de proyecto");
    	tipos.put(REL, "Archivo de relaciones");
    	tipos.put(PNG, "Archivos de imagen PNG");
    	tipos.put(JPG, "Archivos de imagen JPG");
    	tipos.put(JPEG,"Archivos de imagen JPEG");
    	tipos.put(GIF, "Archivos de imagen GIF");
    	tipos.put(IMG, "Archivos de imagen PNG, JPG, JPEG o GIF");
   }
   
	   /**
	 * <p>Title: get</p>  
	 * <p>Description: Devuelve el texto asociado a una extensión.</p> 
	 * @param t Tipo de datos o etiqueta.
	 * @return Texto asociado a dicha etiqueta. Null en otro caso.
	 */
	public static String get(String t) {
		String txt = null;
		if(hasType(t)) txt = tipos.get(txt);
		else System.out.println("Types > No existe tipo: " + t);
		return txt;
		
	}
   
	/**
	 * <p>Title: hasType</p>  
	 * <p>Description: Indica si existe dicha extensión o etiqueta.</p> 
	 * @param t Tipo de extensión o etiqueta.
	 * @return TRUE si esta contenida, FALSE en otro caso.
	 */
	public static boolean hasType(String t) { return tipos.containsKey(t); }

}
