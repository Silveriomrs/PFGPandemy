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


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
    private final FileNameExtensionFilter filtro = new FileNameExtensionFilter("cvs","CVS");
    
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
	public List<String[]> abrirArchivo() {
		File f;
		JFileChooser sf = new JFileChooser(".");	
		List<String[]> datos = null;
		sf.setFileFilter(filtro);
		sf.setFileSelectionMode(JFileChooser.FILES_ONLY);
		CSVReader lectorCSV;
		
		if(sf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			f = sf.getSelectedFile();											// obtiene el archivo seleccionado	
			if ((f != null) && f.exists() && f.isFile() ) {						// muestra error si es inválido			
				try {
					lectorCSV = new CSVReader(new FileReader(f));					//Abrir el archivo.
					datos = lectorCSV.readAll();
					lectorCSV.close();
				}
				catch (IOException e) {e.printStackTrace();}
				catch (CsvException e) {e.printStackTrace();}
			}
		}
		return datos;
	}
	    
	/**
	 * Metodo para almacenar en un archivo una cadena de texto.
	 * @param bd los datos a guardar.
	 * @return TRUE en caso de operación realizada, false en otro caso.
	 */
	public boolean grabarArchivo(String bd) {
		boolean ok = false;
		JFileChooser sf=new JFileChooser(".");									//Creamos el objeto JFileChooser
		sf.setFileSelectionMode(JFileChooser.FILES_ONLY);
		sf.setFileFilter(filtro);
		int seleccion=sf.showSaveDialog(null);									//Abrimos la ventana, guardamos la opcion seleccionada por el usuario		
		if(seleccion==JFileChooser.APPROVE_OPTION){		 						//Si el usuario, selecciona aceptar
		    File fichero=sf.getSelectedFile();									//Seleccionamos el fichero							
		    try(FileWriter fw=new FileWriter(fichero)){	        
		    	fw.write(bd);													//Escribimos el texto en el fichero.
		    	fw.close();	 													//Cierre del escritor de fichero.
		    	ok = true;
		    } catch (IOException e1) {e1.printStackTrace();return ok;}
		}
		return ok;
	}
}