/**
 * 
 */
package modelo;

import controlador.ControladorMapa;
import vista.Principal;


/**
 * @author Silverio Manuel Rosales Santana
 * @date
 * @version 1.0
 *
 */
public class Main {

	
	/**
	 * Método main de la clase.
	 * @param args no usado.
	 */
	public static void main(String[] args) {
		Principal ventana = new Principal();
		ventana.setVisible(true);	
		//Mapa mapa = new Mapa();
		ControladorMapa cmapa = new ControladorMapa();
	}

}
