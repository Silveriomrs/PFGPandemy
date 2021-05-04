/**
 * Clase controladora para realizar los encapsulados de datos, así como extraer
 * los datos del tipo de datos usado, en los flujos de datos entre disco
 * e interfáz gráfica.
 */
package controlador;

import javax.swing.JTable;
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
	private DCVS bd;

	/**
	 * Constructor principal de la clase.
	 */
	public ControladorDatosIO() {
		super();
		this.io = new IO();
		this.bd = new DCVS();
	}

	
	/**
	 * Ejecuta la acción de abrir un archivo de extensión CVS.
	 * @return Jtable tabla con los datos del archivo abierto.
	 */
	public JTable abrirArchivo() {
		bd = new DCVS();
		bd = io.abrirArchivo("cvs");											//Abrir archivo CVS
		/* No está implementado en DCVS la buena generación del modelo */
		return new JTable(bd.getModelo());
	}
	

	/**
	 * Guarda los datos correspondientes en un archivo del disco duro.
	 * @param tabla para ser almacenados.
	 * @return True si la operación ha tenido éxito, FALSE en otro caso.
	 */
	public boolean guardarArchivo(JTable tabla) {
		bd = new DCVS();
		//Realizar conversión y extracción de datos.
		
		//Falta que me guarde la tabla.Esta pasando un String con la tabla.
		
		return io.grabarArchivo(bd.toString(),"cvs");
	}

}
