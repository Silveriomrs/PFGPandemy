/**
 * Clase controladora para realizar los encapsulados de datos, así como extraer
 * los datos del tipo de datos usado, en los flujos de datos entre disco
 * e interfáz gráfica.
 */
package controlador;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import modelo.DCVS;
import modelo.IO;

/**
 * @author Silverio Manuel Rosales Santana
 * @date	20210413
 * @version 1.1
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
	public DefaultTableModel abrirArchivo() {
		bd = io.abrirArchivo();													//Abrir archivo CVS
		if(bd != null) return bd.getModelo();
		else return null;
	}
	

	/**
	 * Guarda los datos correspondientes en un archivo del disco duro.
	 * @param tableModel para ser almacenados.
	 * @return True si la operación ha tenido éxito, FALSE en otro caso.
	 */
	public boolean guardarArchivo(TableModel tableModel) {
		bd = new DCVS();
		//Realizar conversión y extracción de datos.
		bd.setModelo(tableModel);
		return io.grabarArchivo(bd.toString());
	}

}
