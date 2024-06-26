/**
 * Clase controladora para realizar los encapsulados de datos, así como extraer
 * los datos del tipo de datos usado, en los flujos de datos entre disco
 * e interfáz gráfica.
 */
package controlador;

import java.awt.Desktop;
import java.io.IOException;

import modelo.DCVS;
import modelo.TypesFiles;

/**
 * @author Silverio Manuel Rosales Santana
 * @date	20210413
 * @version 1.1
 *
 */
public class ControladorDatosIO {
	/** io Instancia de entrada y salida de datos hacia un dispositivo de almacenamiento*/  
	private IO io;
	/** separador Contiene el separador usado por el sistema operativo anfitrión */  
	public final String separador = System.getProperty("file.separator");

	/**
	 * Constructor principal de la clase.
	 */
	public ControladorDatosIO() {
		super();
		this.io = new IO();
	}
	
	/**
	 * Ejecuta la acción de abrir un archivo de extensión CVS.
	 * @param ruta Ruta y nombre completo del archivo.
	 * @param ext Extensión del archivo.
	 * @return DefaultTableModel modelo con los datos del archivo abierto.
	 */
	public DCVS abrirArchivo(String ruta, String ext) {
		DCVS dcvs = null;
		try {dcvs = io.abrirArchivo(ruta,ext);}
		catch (IOException e) {return null;}
		return dcvs;
	}
	
	/**
	 * Guarda los datos correspondientes en un archivo del disco duro.
	 * @param modulo Modulo a guardar en el disco.
	 * @return La ruta al archivo guardado. Null en otro caso.
	 */
	public String guardarArchivo(DCVS modulo) {
		return io.grabarArchivo(modulo.toString(),modulo.getRuta(),modulo.getTipo());
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
		return IO.selFile(sel, ext);
	}
	
	/**
	 * Abre un fichero PDF del sistema.
	 * Función realizada para la parte de ayuda de la barra de menús, abrir manuales.
	 * @param man Tipo de manual para abrir.
	 */
	public void openPDF(String man) {
		//Composición de la ruta al manual con el separador por defecto del OS anfitrión.
		String pathPDF = "Manuales" + separador + man + "." + TypesFiles.PDF;
		//Apertura del PDF desde la propia aplicación del sistema.
		try {
			String r2 = IO.getFile(1, pathPDF, TypesFiles.PDF).getPath();			/* FUNCIONA */
			pathPDF = r2;
			Desktop.getDesktop().open(IO.getFile(1, pathPDF, TypesFiles.PDF));
		}catch (Exception ex) {
			System.out.println("ControladorDatosIO > openPDF > Error, No encontrado fichero: " + pathPDF);
		}
	}

}
