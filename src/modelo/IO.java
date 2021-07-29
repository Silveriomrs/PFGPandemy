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
    /** filtro Instancia del filtro*/  
    private FileNameExtensionFilter filtro;
    
    /**
     * Constructor de la clase IO
     */
    public IO(){ }
    

	/**
	 * Metodo que lee un archivo del disco en formato CVS con separación de comas
	 * ',' almacenandolos en un Objeto del tipo DCVS.
	 * En caso de error lanza un mensaje y el error de excepcion. 
	 * @param ext extensión del archivo.
	 * @return tabla de datos en formato ArrayList de ArrayList (ColumnasxFilas),
	 *  null en otro caso.
	 */
	public List<String[]> abrirArchivo(String ext) {
		List<String[]> datos = null;
		CSVReader lectorCSV;
		File f = selFile(1,ext);													// Obtención del archivo.							
		if ((f != null) && f.exists() && f.isFile() ) {							// muestra error si es inválido			
			try {
				lectorCSV =  new CSVReader(new FileReader(f));					//Abrir el archivo.
				datos = lectorCSV.readAll();
				lectorCSV.close();
				
			}
			catch (IOException e) {e.printStackTrace();}
			catch (CsvException e) {e.printStackTrace();}
		}
		return datos;
	}
	    
	/**
	 * Metodo para almacenar en un archivo una cadena de texto.
	 * @param bd los datos a guardar.
	 * @param ext extensión del archivo.
	 * @return TRUE en caso de operación realizada, false en otro caso.
	 */
	public boolean grabarArchivo(String bd, String ext) {
		boolean ok = false;
		File fichero = selFile(2,ext);	
		if(fichero != null){		 												    																
		    try(FileWriter fw=new FileWriter(fichero)){	        
		    	fw.write(bd);													//Escribimos el texto en el fichero.
		    	fw.close();	 													//Cierre del escritor de fichero.
		    	ok = true;
		    } catch (IOException e1) {e1.printStackTrace();return ok;}
		}
		return ok;
	}
	
	
	/**
	 * Selecciona un archivo del disco, o establece su nombre. Puede recibir un
	 * por parámetro un filtro para aplicar a los ficheros de extensión válidos
	 * para selección.
	 * @param sel Selecciona el tipo de dialogo 1: Leer, 2: Grabar.
	 * @param ext extensión del archivo a modo de filtro.
	 * @return File del archivo seleccionado, null en otro caso.
	 */
	private File selFile(int sel, String ext) {
		File f = null;
		filtro = new FileNameExtensionFilter(ext.toLowerCase(),ext.toUpperCase());
		JFileChooser sf=new JFileChooser(".");									//Directorio local.
		sf.setFileSelectionMode(JFileChooser.FILES_ONLY);						//Selección de ficheros unicamente.
		sf.setFileFilter(filtro);												//Establecimiento del filtro
		int seleccion;
		//Apertura del dialogo de selección.
		if(sel==1) seleccion = sf.showOpenDialog(null);
		else seleccion=sf.showSaveDialog(null);								
	    if(seleccion == JFileChooser.APPROVE_OPTION) {							//En caso de haber elegido archivo.
	        f = sf.getSelectedFile();											//Obtenemos el archivo.
	    }
		return f;
	}
	
	
	/**
	 * Carga una imagen desde un dispositivo físico (disco duro, etc).
	 * @param nombre ruta y nombre de la imagen.
	 * @return imagen leído desde el dispostivo. Null en otro caso
	 */
	public Image abrirImagen(String nombre) {
		BufferedImage image = null;
		try {                
	         image = ImageIO.read(new File(nombre));
	       } catch (IOException ex) {
	    	    ex.printStackTrace();
	       }
		return image;
	}
}