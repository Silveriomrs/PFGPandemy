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
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;



/**
 * Clase de Entrada y salida de datos.
 * @author Silverio Manuel Rosales Santana
 * @date 
 * @version 2.2
 *
 */
public class IO{
    private final String SEPARADOR =",";
    private final FileNameExtensionFilter filtro = new FileNameExtensionFilter("cvs","CVS");
    private CSVReader lector;
    
    /**
     * Constructor de la clase IO
     */
    public IO(){ }
    

	/**
	 * Metodo que lee un archivo del disco. En caso de que no exista lanza un mensaje y el error de 
	 * excepcion correspondiente. El metodo llevara el control de los parametros minimos y maximos permitidos.
	 * Unicamente una 'R', unicamente una 'S' y en la periferia, al menos un caracter 'O'. Todo caracter en mayuscula.
	 * No admite numeros negativos. Almacena el resultado en el campo lectura.
	 * @param ext extensión del archivo.
	 * @return tabla de datos en formato ArrayList de ArrayList (ColumnasxFilas)
	 */
	public DCVS abrirArchivo() {
		File f;
		Scanner scn = null;
		JFileChooser sf = new JFileChooser(".");	
		DCVS bd = null;
		int resultado;
		ArrayList<Object[]> listado = new ArrayList<Object[]>();
		
		sf.setFileFilter(filtro);
		sf.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		resultado = sf.showOpenDialog(null);									// indica cual fue la accion de usuario sobre el jfilechooser
/* APLICAR lector DE LA CLASE CVS Reader */
		
		if(resultado == JFileChooser.APPROVE_OPTION) {
			f = sf.getSelectedFile();											// obtiene el archivo seleccionado	
			if ((f == null) || (f.getName().equals("")) || !f.isFile()) {		// muestra error si es inválido
				JOptionPane.showMessageDialog(null, "Nombre de archivo inválido", "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
			}else {			
				try {
					scn = new Scanner(f);
					String l = scn.nextLine();
					bd = new DCVS();
					bd.addCabecera(trataLinea(l));
					while (scn.hasNextLine()) {
						l = scn.nextLine();
						listado.add(trataLinea(l));
					}
					scn.close();												//Cierre del Scanner.
					bd.setDatos(formarDatos(listado));							//Asignamos los datos en el formato adecuado.
					bd.setModelo(null);											//Accionamos un trigger dentro de BD para conformar el modelo.
				}
				catch (FileNotFoundException e1) {e1.printStackTrace();}
			}
		}
		return bd;
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
		for(int i=0; i<nc; i++) {
			if(campos[i] == null) {	lista[i] = "a";}							//Si el valor es null escribe ""
			else{lista[i] = campos[i];}											//Valor leído.
		}
		return lista;
	}
	
	/**
	 * Función auxiliar para conformar la matriz de datos a devolver desde
	 * un ArrayList de objetos de nColumnas x nFilas.
	 * @param listado Filas de datos que conforman cada línea.
	 * @return array bidimensional de objetos conformados.
	 */
	private Object[][] formarDatos(ArrayList<Object[]> listado){
		int ncols = listado.size();
		int nfils = listado.get(0).length;
		Object[][] datos = new Object[nfils][ncols];
		//Bucle para recorrer los datos y dar la nueva asignación.
		for(int i = 0; i<nfils; i++) {
			for(int j = 0; j<ncols; j++) {
				datos[i][j] = listado.get(i)[j];
			}
		}		
		return datos;
	}
}