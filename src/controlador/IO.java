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
package controlador;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;


/**
 * Clase de Entrada y salida de datos.
 * @author Silverio Manuel Rosales Santana
 * @date 
 * @version 2.0
 *
 */
public class IO{
    private Ayuda ayuda;
    private final String SEPARADOR =",";
    private CSVReader lector = null;
    
    /**
     * Constructor de la clase IO
     */
    public IO()
    {    
        ayuda = new Ayuda();
    }
    

	/**
	 * Metodo que lee un archivo del disco. En caso de que no exista lanza un mensaje y el error de 
	 * excepcion correspondiente. El metodo llevara el control de los parametros minimos y maximos permitidos.
	 * Unicamente una 'R', unicamente una 'S' y en la periferia, al menos un caracter 'O'. Todo caracter en mayuscula.
	 * No admite numeros negativos. Almacena el resultado en el campo lectura.
	 * @param ext extensión del archivo.
	 */
	public void abrirArchivo(String ext) {
		File f;
		Scanner scn = null;
		JFileChooser sf = new JFileChooser(".");	
		FileNameExtensionFilter filtro = new FileNameExtensionFilter(ext,"CVS");
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
						 //jtaContenido.insert(scn.nextLine() + "\n", jtaContenido.getText().length());
						String l = scn.nextLine();
						System.out.println(l);
						String[] campos = l.split(SEPARADOR); 
						System.out.println(Arrays.toString(campos));
					}
					scn.close();
				}
				catch (FileNotFoundException e1) {e1.printStackTrace();}
			}
		}
	}
	    
	/**
	 * Metodo para almacenar en un archivo una cadena de texto.
	 * @param nombreArchivo , nombre del archivo donde se almacenara el texto.
	 * @param datos los datos a guardar.
	 */
	public void grabarArchivo(String nombreArchivo, String datos) { 
		//Creamos el objeto JFileChooser
		JFileChooser sf=new JFileChooser();
		 
		//Abrimos la ventana, guardamos la opcion seleccionada por el usuario
		int seleccion=sf.showSaveDialog(null);
		 
		//Si el usuario, pincha en aceptar
		if(seleccion==JFileChooser.APPROVE_OPTION){		 
		    //Seleccionamos el fichero
		    File fichero=sf.getSelectedFile();
		 
		    try(FileWriter fw=new FileWriter(fichero)){
		 
		        //Escribimos el texto en el fichero
		   //     fw.write(textArea.getText());
		    	fw.close();
		 
		    } catch (IOException e1) {
		        e1.printStackTrace();
		    }
		 
		}
	}
	    
}