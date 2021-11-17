/**
 * lectura de un fichero, linea a linea.
 * Contiene control de excepciones y el tratamiendo de las mismas cuando es posible.
 * Comprueba que el archivo de entrada tiene un formato correcto y en caso de realizar 
 * la operacion correctamente, almacena el resultado en un arreglo, donde los Obstaculos
 * tendran el valor -2, el nodo inicial el valor 0, el nodo final valor -1.
 * 
 * @author (Silverio Manuel Rosales Santana) 
 * @version (2017.10.15.0212)
 */
package modelo;


import java.awt.Image;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;



/**
 * Clase de Entrada y salida de datos.
 * @author Silverio Manuel Rosales Santana
 * @date 2021.04.12
 * @version 2.8
 *
 */
public class IO{
	
	private static HashMap<String,String> tipos;
    
    /** Archivos generales formato CSV*/  
    public final static String CSV = "csv";
    /** Archivo de proyecto PRJ*/  
    public final static String PRJ = "prj";
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
     * Constructor de la clase IO
     */
    public IO(){
    	configurar();
    }
    
    private void configurar() {
    	tipos = new HashMap<String,String>();
    	tipos.put(PAL,"Paleta de colores");
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
	 * Metodo que lee un archivo del disco en formato CVS con separación de comas
	 * ',' almacenandolos en un Objeto del tipo DCVS.
	 * En caso de error lanza un mensaje y el error de excepcion. 
	 * @param ruta Ruta completa con el nombre del archivo a cargar. Null si se
	 * desea que muestre dialogo de selección.
	 * @param ext extensión del archivo.
	 * @return tabla de datos en formato ArrayList de ArrayList (ColumnasxFilas),
	 *  null en otro caso.
	 */
	public DCVS abrirArchivo(String ruta, String ext) {
		DCVS dcvs = null;
		String ruta2 = ruta;
		File f = getFile(1,ruta,ext);	
		if ((f != null) && f.exists() && f.isFile() ) {							// muestra error si es inválido			
			try {
				CSVReader lectorCSV =  new CSVReader(new FileReader(f));		//Abrir el archivo.
				List<String[]> datos = lectorCSV.readAll();
				lectorCSV.close();
				if(ruta == null) ruta2 = f.getPath();
				dcvs = new DCVS();
				dcvs.crearModelo(datos);
				dcvs.setRuta(ruta2);
				dcvs.setTipo(ext);
				dcvs.setDate(new Date( f.lastModified() ));
				
				System.out.println(dcvs.getRuta() + " - "	+ dcvs.getTipo());
			}
			catch (IOException e) {e.printStackTrace();}
			catch (CsvException e) {e.printStackTrace();}
		}
		return dcvs;
	}
	    
	/**
	 * Metodo para almacenar en un archivo un texto.
	 * Este método en caso de no tener asignado el nombre completo del fichero
	 * de destino con una extensión, le añade la extensión pasada por parámetro.
	 * En caso de no coincidir la extensión de la ruta completa con la extensión
	 * deseada, sustituye la primera por la segunda.
	 * @param bd los datos a guardar.
	 * @param ruta uta completa con el nombre del fichero. Null si se
	 * desea que muestre dialogo de selección.
	 * @param ext extensión del archivo.
	 * @return Ruta en caso de operación realizada, Null en otro caso.
	 */
	public String grabarArchivo(String bd, String ruta, String ext) {
		String ruta2 = ruta;
		String ext2 = ext;
		//Comprobación de extensión coincida con extensión de la ruta.
		if(ruta != null && !checkExt(ruta,ext)) {												//Si son diferentes
			ext2 = ruta.substring(ruta.length() -3).toLowerCase();
			//Comprobación de que es una extensión registrada
			if (!tipos.containsKey(ext2)) {										//Sino esta registrada, tomar como no añadida.
				ruta2 = ruta + ext;												//Añade la extensión.
			}else {																//En otro caso remover la que tiene por la nueva.
				ruta2 = ruta.substring(0, ruta.length() -3);					//Eliminar 3 últimos carácteres.
				ruta2 = ruta2 + ext;
			}
			System.out.println("Módulo IO - cambio de extensión: " + ruta + " -> " + ruta2);
		}
		
		
		File f = getFile(2,ruta2,ext2);
		if(f != null){
		    try(FileWriter fw = new FileWriter(f)){	 
		    	ruta2 = f.getPath();
		    	fw.write(bd);													//Escribimos el texto en el fichero.
		    	fw.close();	 													//Cierre del escritor de fichero.
		    } catch (IOException e1) {e1.printStackTrace();}
		}
		return ruta2;
	}
	
	
	private File getFile(int sel, String path,String ext) {
		File f = null;
		String ruta = path;
		if(ruta == null || ruta == "") {ruta = selFile(sel,ext);}				// Obtención del archivo.	
		if(ruta != null) f = new File(ruta);
		return f;
	}
	
	
	/**
	 * Selecciona un archivo del disco, o establece su nombre. Puede recibir un
	 * por parámetro un filtro para aplicar a los ficheros de extensión válidos
	 * para selección.
	 * @param sel Selecciona el tipo de dialogo 1: Leer, 2: Grabar.
	 * @param ext Extensión del archivo.
	 * @return Ruta del archivo seleccionado, null en otro caso.
	 */
	public static String selFile(int sel, String ext) {
		String ruta  = null;
		FileNameExtensionFilter filtro = null;
		//Comprobación de filtro para imagenes soportadas u otros tipos.
		if(!ext.equals(IMG)) {
			filtro = new FileNameExtensionFilter(tipos.get(ext),ext);
		}else {
			filtro = new FileNameExtensionFilter(tipos.get(ext), PNG, JPG, JPEG, GIF);
		}
		
		JFileChooser sf = new JFileChooser(".");								//Directorio local.
		sf.setFileSelectionMode(JFileChooser.FILES_ONLY);						//Selección de ficheros unicamente.
		sf.setFileFilter(filtro);												//Establecimiento del filtro
		int seleccion;
		//Apertura del dialogo correspondiente a la selección indicada.
		if(sel == 1) seleccion = sf.showOpenDialog(null);
		else seleccion = sf.showSaveDialog(null);		
		//Elección del archivo.
	    if(seleccion == JFileChooser.APPROVE_OPTION) {							//En caso de haber elegido archivo.
	        File f = sf.getSelectedFile();										//Obtenemos el archivo.
			ruta = f.getPath();													//Obtención de la ruta del archivo.
			//Comprobación de elección de archivo correcta.
			if(!checkExt(ruta,ext)) {
				ruta = ruta + "." + ext;										//En caso de omisión de la extensión o discordancia, se le añade la indicada.
				System.out.println("Añadida extensión: " + ext + " al archivo.");
			}					
	    }
		return ruta;
	}
	
	/**
	 * <p>Title: checkExt</p>  
	 * <p>Descripción: Comprobación de la extensión sea de los tipos aceptados
	 * y además para los tipos de archivos imagen, comprobar los tres formatos
	 * admitidos.</p> 
	 * @param ruta Nombre del archivo con su ruta completa.
	 * @param ext Extensión con la que comparar.
	 * @return True si las extensiones coinciden, False en otro caso.
	 */
	public static boolean checkExt(String ruta, String ext) {
		boolean ok = true;
		String ext2 = ruta.substring(ruta.length() -3).toLowerCase();
		// Comprobar que la extensión pueda ser JPG, JPEG, PNG o GIF
		if(ext.equals(IMG) && !(ext2.equals(JPG) || ext2.equals(PNG) || ext2.equals(JPEG) || ext2.equals(GIF))) {
			System.out.println("Tipo de imagen incorrecto: " + ext2);
			ok = false;
		}else if(!ext.equals(IMG) && !ext.equals(ext2)) {												//Comprobación de elección de archivo correcta.
			System.out.println("Tipo de fichero sin extensión conocida: " + ext2);
			ok = false;
		}
		return ok;
	}
	
	/**
	 * Carga una imagen desde un dispositivo físico (disco duro, etc) y realiza
	 * un escalado si se desea.
	 * @param ruta Ruta completa con el nombre de la imagen.
	 * @param escalado True realiza un escalado de la imagen. False en otro caso.
	 * @param w Ancho del escalado de la imagen. Ignorado si escalado es false.
	 * @param h Alto del escalado de la imagen. Ignorado si escalado es false.
	 * @return imagen leído desde el dispostivo. Null en otro caso.
	 */
	public static Image getImagen(String ruta, boolean escalado, int w, int h) {
		//System.out.println(ruta);
		Image img = null;
		try {
			img = new ImageIcon(IO.class.getResource(ruta)).getImage();
			if(escalado) img = img.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Imagen no cargada");
			return img;
		}
		return img;
	}
	
	/**
	 * <p>Title: getIcon</p>  
	 * <p>Description: Devuelve un icono escalado a las medidas obtenidas
	 * por parámetros, de la imagen fuente..</p> 
	 * @param ruta Ruta del archivo de imagen.
	 * @param w Ancho del escalado de la imagen.
	 * @param h Alto del escalado de la imagen.
	 * @return Icono escalado de la imagen. Null en otro caso,
	 */
	public static ImageIcon getIcon(String ruta, int w, int h) {
		ImageIcon icon = null;
		Image img = getImagen(ruta,false,w,h);
		if(img != null) {
			icon = new ImageIcon(img);
			icon.setImage(icon.getImage().getScaledInstance(w,h, java.awt.Image.SCALE_DEFAULT));
		}
		
		return icon;
	}
	
	
}