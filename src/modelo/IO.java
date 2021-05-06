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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

//import com.opencsv.CSVReader;



/**
 * Clase de Entrada y salida de datos.
 * @author Silverio Manuel Rosales Santana
 * @date 
 * @version 2.0
 *
 */
public class IO{
    private final String SEPARADOR =",";
//    private CSVReader lector;
    
    /**
     * Constructor de la clase IO
     */
    public IO()
    {    

    }
    

	/**
	 * Metodo que lee un archivo del disco. En caso de que no exista lanza un mensaje y el error de 
	 * excepcion correspondiente. El metodo llevara el control de los parametros minimos y maximos permitidos.
	 * Unicamente una 'R', unicamente una 'S' y en la periferia, al menos un caracter 'O'. Todo caracter en mayuscula.
	 * No admite numeros negativos. Almacena el resultado en el campo lectura.
	 * @param ext extensión del archivo.
	 * @return tabla de datos en formato ArrayList de ArrayList (ColumnasxFilas)
	 */
	public DCVS abrirArchivo(String ext) {
		File f;
		Scanner scn = null;
		JFileChooser sf = new JFileChooser(".");	
		FileNameExtensionFilter filtro = new FileNameExtensionFilter(ext.toLowerCase(),ext.toUpperCase());
		DCVS bd = new DCVS();
		int resultado;		
		
		sf.setFileFilter(filtro);
		sf.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		resultado = sf.showOpenDialog(null);									// indica cual fue la accion de usuario sobre el jfilechooser
		
		if(resultado == JFileChooser.APPROVE_OPTION) {
			f = sf.getSelectedFile();											// obtiene el archivo seleccionado
			// muestra error si es inválido
			if ((f == null) || (f.getName().equals("")) || !f.isFile()) {
				JOptionPane.showMessageDialog(null, "Nombre de archivo inválido", "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
			}else {			
				try {
					scn = new Scanner(f);
					while (scn.hasNextLine()) {
						String l = scn.nextLine();
						bd.addFila(trataLinea(l));
					}
					scn.close();
				}
				catch (FileNotFoundException e1) {e1.printStackTrace();}
			}
		}
		return bd;
	}
	    
	/**
	 * Metodo para almacenar en un archivo una cadena de texto.
	 * @param nombreArchivo , nombre del archivo donde se almacenara el texto.
	 * @param bd los datos a guardar.
	 * @param ext Extensión del archivo a guardar.
	 * @return TRUE en caso de operación realizada, false en otro caso.
	 */
	public boolean grabarArchivo(String bd, String ext) {
		boolean ok = false;
		JFileChooser sf=new JFileChooser(".");									//Creamos el objeto JFileChooser
		FileNameExtensionFilter filtro = new FileNameExtensionFilter(ext.toLowerCase(),ext.toUpperCase());
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
	
	/**
	 * Función que convierte una línea leída del archivo en disco
	 * en un array de cadenas (campos) en formato texto. Elimina los separadores
	 * y también elimina los espacios en blanco innecesarios.
	 * @param linea a formatear.
	 * @return la lista de valores.
	 */
	private Object[] trataLinea(String linea) {							
		String[] campos = linea.trim().split(SEPARADOR);						//Limpieza de los campos.
		int nc = campos.length;													//Obtención del número de datos.
		Object[] lista = new Object[nc];										//Lista donde almacenar los datos de la línea de entrada
		for(int i=0; i<nc; i++) {lista[i] = campos[i];}							//Creación de la lista (fila) con los datos obtenidos.
		return lista;
	}
}