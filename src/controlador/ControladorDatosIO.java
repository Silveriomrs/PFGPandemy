/**
 * Clase controladora para realizar los encapsulados de datos, así como extraer
 * los datos del tipo de datos usado, en los flujos de datos entre disco
 * e interfáz gráfica.
 */
package controlador;

import java.awt.TextArea;

import modelo.DCVS;
import modelo.IO;

/**
 * @author Silverio Manuel Rosales Santana
 * @date	20210413
 * @version 1.0
 *
 */
public class ControladorDatosIO {
	private IO io;

	/**
	 * 
	 */
	public ControladorDatosIO() {
		this.io = new IO();
	}

	
	/**
	 * Ejecuta la acción de abrir un archivo de extensión CVS.
	 * @param textArea 
	 */
	public void abrirArchivo(TextArea textArea) {
		DCVS bd = io.abrirArchivo("cvs,CVS");
		textArea.setText(bd.toString());
	}
	
	/**
	 * Guarda los datos correspondientes en un archivo del disco duro.
	 * @param datos para ser almacenados.
	 * @return True si la operación ha tenido éxito, FALSE en otro caso.
	 */
	public boolean guardarArchivo(String datos) {return io.grabarArchivo(datos,"cvs,CVS");}
	
}
