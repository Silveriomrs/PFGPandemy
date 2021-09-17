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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
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
	
	private HashMap<String,String> tipos;
    
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
    	tipos.put(PRJ, "Archivo de proyecto");
    	tipos.put(REL, "Archivo de relaciones");
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
		File f = getFile(1,ruta,ext);
		
		if ((f != null) && f.exists() && f.isFile() ) {							// muestra error si es inválido			
			try {
				CSVReader lectorCSV =  new CSVReader(new FileReader(f));		//Abrir el archivo.
				List<String[]> datos = lectorCSV.readAll();
				lectorCSV.close();
				if(ruta == null) ruta = f.getPath();
				dcvs = new DCVS();
				dcvs.crearModelo(datos);
				dcvs.setRuta(ruta);
				dcvs.setTipo(ext);
			}
			catch (IOException e) {e.printStackTrace();}
			catch (CsvException e) {e.printStackTrace();}
		}
		return dcvs;
	}
	    
	/**
	 * Metodo para almacenar en un archivo una cadena de texto.
	 * @param bd los datos a guardar.
	 * @param ruta uta completa con el nombre del fichero. Null si se
	 * desea que muestre dialogo de selección.
	 * @param ext extensión del archivo.
	 * @return TRUE en caso de operación realizada, false en otro caso.
	 */
	public String grabarArchivo(String bd, String ruta, String ext) {
		File f = getFile(2,ruta,ext);
		
		if(f != null){
		    try(FileWriter fw=new FileWriter(f)){	 
		    	ruta = f.getPath();
		    	fw.write(bd);													//Escribimos el texto en el fichero.
		    	fw.close();	 													//Cierre del escritor de fichero.
		    } catch (IOException e1) {e1.printStackTrace();}
		}
		return ruta;
	}
	
	
	private File getFile(int sel, String path,String ext) {
		File f = null;
		String ruta = path;
		if(ruta == null || ruta == "") {ruta = selFile(1,ext);	}				// Obtención del archivo.	
		if(ruta != null) f= new File(ruta);
		return f;
	}
	
	
	/**
	 * Selecciona un archivo del disco, o establece su nombre. Puede recibir un
	 * por parámetro un filtro para aplicar a los ficheros de extensión válidos
	 * para selección.
	 * @param sel Selecciona el tipo de dialogo 1: Leer, 2: Grabar.
	 * desea que muestre dialogo de selección.
	 * @param ext extensión del archivo a modo de filtro.
	 * @return Ruta del archivo seleccionado, null en otro caso.
	 */
	public String selFile(int sel, String ext) {
		String ruta  = null;
		FileNameExtensionFilter filtro = new FileNameExtensionFilter(tipos.get(ext),ext);
		JFileChooser sf=new JFileChooser(".");									//Directorio local.
		sf.setFileSelectionMode(JFileChooser.FILES_ONLY);						//Selección de ficheros unicamente.
		sf.setFileFilter(filtro);												//Establecimiento del filtro
		int seleccion;
		//Apertura del dialogo de selección.
		if(sel==1) seleccion = sf.showOpenDialog(null);
		else seleccion=sf.showSaveDialog(null);		
		//Elección del archivo.
	    if(seleccion == JFileChooser.APPROVE_OPTION) {							//En caso de haber elegido archivo.
	        File f = sf.getSelectedFile();											//Obtenemos el archivo.
			ruta = f.getPath();													//Obtención de la ruta del archivo.
			String ext2 = ruta.substring(ruta.length() -3).toLowerCase();
			if(!ext.equals(ext2)) {												//Comprobación de elección de archivo correcta.
				System.out.println("Tipo de fichero incorrecto");
			}
	    }
		return ruta;
	}
	
	
	/**
	 * Carga una imagen desde un dispositivo físico (disco duro, etc).
	 * @param nombre ruta y nombre de la imagen.
	 * @return imagen leído desde el dispostivo. Null en otro caso
	 */
	public Image abrirImagen(String nombre) {
		BufferedImage image = null;
		try { image = ImageIO.read(new File(nombre)); }
		catch (IOException ex) { ex.printStackTrace(); }
		return image;
	}
}