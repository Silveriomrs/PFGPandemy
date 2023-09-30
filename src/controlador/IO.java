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


import java.awt.Image;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import modelo.DCVS;
import modelo.TypesFiles;



/**
 * Clase de Entrada y salida de datos.
 * @author Silverio Manuel Rosales Santana
 * @date 2021.04.12
 * @version 2.8
 *
 */
public class IO{
	/** WorkingDirectory Directorio de trabajo del proyecto actual*/  
	public static String WorkingDirectory = null;
	
    /**
     * Constructor de la clase IO
     */
    public IO(){
    }
    
	/**
	 * Metodo que lee un archivo del disco en formato CVS con separación de comas
	 * ',' almacenandolos en un Objeto del tipo DCVS.
	 * En caso de error lanza un mensaje y el error de excepcion. 
	 * @param ruta Ruta completa con el nombre del archivo (con extensión) a cargar.
	 * @param ext extensión del archivo.
	 * @return DCVS módulo con sus atributos de fichero configurados y los datos contenidos. Null en otro caso.
	 * @throws IOException  Ecepción en caso de que el fichero no sea legible desde el dispositivo.
	 */
	public DCVS abrirArchivo(String ruta, String ext) throws IOException {
		DCVS dcvs = null;
		CSVReader lectorCSV = null;

		//Búsqueda del fichero. getFile() comprueba también que la ruta no sea Null.
		File f = getFile(1,ruta,ext);
		//Ahora en el caso de que el fichero sea correcto y no sea Null => procede.
		if ((f != null) && f.exists() && f.isFile() ) {							// muestra error si es inválido			
			try {
				lectorCSV =  new CSVReader(new FileReader(f));					// Abrir el archivo.
				List<String[]> datos = lectorCSV.readAll();
				dcvs = new DCVS();
				dcvs.crearModelo(datos);
				dcvs.setPath(ruta);
				dcvs.setDirectorio(f.getParent());
				dcvs.setName(extractName(f.getName()));
				if(ext.equals(TypesFiles.PRJ))  WorkingDirectory = f.getParent();
				dcvs.setType(ext);
				dcvs.setDate("" + f.lastModified());
			}
			catch (IOException e) {System.out.println("Error trying to open a file.");}
			catch (CsvException e) {System.out.println("Error trying to open a CSV file.");}
			finally {if(lectorCSV != null) lectorCSV.close();}
		}
		return dcvs;
	}

	/**
	 * Metodo para almacenar en un archivo un texto.
	 * Este método en caso de no tener asignado el nombre completo del fichero
	 * de destino con una extensión, le añade la extensión pasada por parámetro.
	 * En caso de no coincidir la extensión de la ruta completa con la extensión
	 * deseada, sustituye la primera por la segunda.
	 * @param modulo Módulo para almacenar en disco.
	 * @param ruta ruta completa con el nombre del fichero. Null si se
	 * desea que muestre dialogo de selección.
	 * @return ok, True en caso de operación realizada, False en otro caso.
	 */
	public boolean saveModule(DCVS modulo) {
		boolean ok = false;
		File f = null;
		String path = modulo.getPath();
		String type = modulo.getType();
		
		//Comprobación de extensión coincida con extensión de la ruta.
		if(path != null && checkExt(path,type)) {
			f = getFile(2,path,type);
			ok = true;
		}

		//Escribir el archivo.
		if(ok && f != null){
		    try(FileWriter fw = new FileWriter(f)){	
		    	//Escribimos el texto en el fichero.
		    	fw.write(modulo.toString());
		    	//Si se ha guardado un proyecto establecer nuevo WD.
		    	if(type.equals(TypesFiles.PRJ)) {WorkingDirectory = f.getParent();}
		    	//Establecer datos del fichero en el módulo.
		    	modulo.setName(extractName(f.getName()));
		    	modulo.setDirectorio(f.getParent());
		    	modulo.setPath(f.getPath());
		    	modulo.setDate("" + f.lastModified());
		    } catch (IOException e1) {System.out.println("Error, file couldn't be saved: " + path);}
		}
		
		return ok;
	}
	
	
	/**
	 * Metodo para almacenar en un archivo un texto.
	 * Este método en caso de no tener asignado el nombre completo del fichero
	 * de destino con una extensión, le añade la extensión pasada por parámetro.
	 * En caso de no coincidir la extensión de la ruta completa con la extensión
	 * deseada, sustituye la primera por la segunda.
	 * @param datos los datos a guardar.
	 * @param ruta ruta completa con el nombre del fichero.
	 * @param name Nombre con el que se almacenará el fichero.
	 * @param ext extensión del archivo.
	 * @return Ruta en caso de operación realizada, Null en otro caso.
	 */
	public String saveFile2(String datos, String ruta, String name, String ext) {
		// TODO: MAl, está mal hecho y mal planteado. Reformar!!!
		String ruta2 = ruta;
		String ext2 = ext;
		//Comprobación de extensión coincida con extensión de la ruta.
		if(ruta != null && !checkExt(ruta,ext)) {								//Si son diferentes
			ext2 = ruta.substring(ruta.length() -3).toLowerCase();
			//Comprobación de que es una extensión registrada
			if (!TypesFiles.hasType(ext2)) {									//Sino esta registrada, tomar como no añadida.
				ruta2 = ruta + ext;												//Añade la extensión.
			}else {																//En otro caso remover la que tiene por la nueva.
				ruta2 = ruta.substring(0, ruta.length() -3);					//Eliminar 3 últimos carácteres.
				ruta2 = ruta2 + ext;
			}
		}
		
		//Escribir el archivo.
		File f = getFile(2,ruta2,ext2);
		if(f != null){
		    try(FileWriter fw = new FileWriter(f)){	 
		    	ruta2 = f.getPath();
		    	if(ext.equals(TypesFiles.PRJ)) WorkingDirectory = f.getParent();
		    	fw.write(datos);													//Escribimos el texto en el fichero.
		    	String name2 = f.getName();
		    	ruta2 = name2.substring(name2.length() -3).toLowerCase();		//Ahora devolvemos el nombre del fichero.
		    	
		    	System.out.println("IO > grabarArchivo > ruta2 (f.name): " + ruta2);
		    } catch (IOException e1) {System.out.println("Error, file couldn't be open.");}
		}
		return ruta2;
	}
	
	/**
	 * <p>Description: Obtiene un enlace a un archivo en disco dentro de una instancia File.</p> 
	 * @param sel Tipo de dialogo a mostrar: 1 Abrir archivo, 2 Guardar archivo.
	 * @param path ruta al fichero en disco.
	 * @param ext Tipo de fichero a abrir.
	 * @return Instancia File apuntando al fichero en disco. Null en otro caso.
	 */
	public static File getFile(int sel, String path,String ext) {
		File f = null;
		if(path != null) f = new File(path);
		return f;
	}
	
	/**
	 * Selecciona un archivo del disco, o establece su nombre. Puede recibir un
	 * por parámetro un filtro para aplicar a los ficheros de extensión válidos
	 * para selección.
	 * @param sel Selecciona el tipo de dialogo 1: Leer, 2: Grabar.
	 * @param ext Extensión del archivo.
	 * @return fileAttr Un array de Strings con los atributos de ubicación del fichero o null en otro caso:
	 * 		[ruta,directorio,nombre].
	 * 		La ruta contiene todo el nombre, incluyendo la extensión.
	 */
	public static String[] selFile(int sel, String ext) {
		String path  = null;
		//fileAttr [ruta,directorio,nombre,ext]
		String[] fileAttr = {null,null,null,null};
		FileNameExtensionFilter filtro = null;
		//Comprobación de filtro para imagenes soportadas u otros tipos.
		if(!ext.equals(TypesFiles.IMG)) {
			filtro = new FileNameExtensionFilter(TypesFiles.get(ext),ext);
		}else {
			filtro = new FileNameExtensionFilter(TypesFiles.get(ext), TypesFiles.PNG, TypesFiles.JPG, TypesFiles.JPEG, TypesFiles.GIF);
		}
		
		JFileChooser sf = new JFileChooser(".");								//Directorio local.
		sf.setFileSelectionMode(JFileChooser.FILES_ONLY);						//Selección de ficheros unicamente.
		sf.setFileFilter(filtro);												//Establecimiento del filtro
		sf.setDialogTitle(TypesFiles.get(ext));
		int seleccion;
		//Apertura del dialogo correspondiente a la selección indicada.
		if(sel == 1) seleccion = sf.showOpenDialog(null);
		else seleccion = sf.showSaveDialog(null);		
		//Elección del archivo.
	    if(seleccion == JFileChooser.APPROVE_OPTION) {							//En caso de haber elegido archivo.
	        File f = sf.getSelectedFile();										//Obtenemos el archivo.
			path = f.getPath();													//Obtención de la ruta del archivo.
			//Comprobación de elección de archivo correcta.
			if(!checkExt(path,ext)) {
				path = path + "." + ext;										//En caso de omisión de la extensión o discordancia, se le añade la indicada.
				System.out.println("IO > selFile: Añadida extensión: " + ext +
						" al archivo con path: " + f.getPath() +
						"\nResultando: " + path);
			}
			//Otorgar los valores.
			fileAttr[0] = path;
			fileAttr[1] = f.getParent();
			fileAttr[2] = extractName(f.getName());
	    }
		return fileAttr;
	}
	
	/**
	 * Función que retira la extensión de un fichero del resto del nombre.
	 *  Un nombre puede tener varios separadores tipo punto '.'. Esta función lo tiene en cuenta
	 *   y extrae unicamente la extensión (ultimos tres carácteres y el punto) del resto del nombre
	 *    del archivo (cadena de texto de entrada). También tiene en cuenta que puede no tenerla.
	 * @param nameIn Nombre del fichero con extensión (puede no contenerla).
	 * @return Nombre del fichero sin extensión.
	 */
	private static String extractName(String nameIn) {
		String nameOut = null;
		//Name puede ser: algo.algomas.ext   o    algo.ext    o  algo   (sin extensión)
		String[] cadena = nameIn.split("\\.");
		//coger la penúltima parte en caso de que pueda tener extensión.
		switch(cadena.length) {
		case(1):
		case(2): nameOut = cadena[0]; break;
		default: //En caso de ser un nombre complejo (varios separadores punto), retira los últimos.
			System.out.println("IO > extractName > Default: Complex name: " + nameIn);
			nameOut = nameIn.substring(0, nameIn.length() - 3);
			System.out.println("IO > extractName > Default: > nameOut" + nameOut);
		}
		return nameOut;
	}
	
	/**
	 * Extrae de una cadena de texto la extensión del fichero (si tiene). No 
	 *  acepta valores nulos de entrada.
	 * @param nameIn Cadena de texto con el nombre del fichero y su extensión (si la tiene).
	 * @return Extensión del fichero, Null en otro caso.
	 */
	private static String getExt(String nameIn) {
		String ext = null;
		//Dividir cadena con separador "." por extensión.
		String[] cadena = nameIn.split("\\.");
		//coger última parte en caso de que pueda tener extensión.
		if(cadena.length > 0) {ext = cadena[cadena.length -1];}
		return ext;
	}
	
	
	/**
	 * <p>Comprobación de la extensión sea de los tipos aceptados
	 * y además para los tipos de archivos imagen, comprobar los formatos
	 * admitidos.</p>
	 * Esta función no sólo comprueba que la ruta contiene una extensión, además
	 *  la compara con otra extensión y comprueba si coinciden.
	 * @param ruta Nombre del archivo con su ruta completa.
	 * @param ext Extensión con la que comparar.
	 * @return True si las extensiones coinciden, False en otro caso.
	 */
	private static boolean checkExt(String ruta, String ext) {
		boolean ok = true;
		String ext2 = getExt(ruta);
		if(ext2 != null) {
			ok = TypesFiles.hasType(ext2);
		} else {ok = false;}
		// Comprobar que la extensión pueda ser JPG, JPEG, PNG o GIF
		if((ok != false) && ext.equals(TypesFiles.IMG) &&  !(ext2.equals(TypesFiles.JPG) || ext2.equals(TypesFiles.PNG) || ext2.equals(TypesFiles.JPEG) || ext2.equals(TypesFiles.GIF))) {
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
		Image img = null;
		try {
			img = new ImageIcon(IO.class.getResource(ruta)).getImage();
			if(escalado) img = img.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
		}catch(Exception e) {
			return img;
		}
		return img;
	}
	

	/**
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