/**  
* Clase diseñada para mostrar información acerca de esta aplicación.
* @author Silverio Manuel Rosales Santana
* @date 6 ago. 2021  
* @version 1.0  
*/  
package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import controlador.IO;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Muestra la información acerca del proyeccto.
 * @author Silverio Manuel Rosales Santana
 * @date 6 ago. 2021
 * @version versión 1.2
 */
public class About extends JPanel {
	private JFrame frame;
	private JLabel etiqueta;
	private JButton btnCerrar;
	private JPanel panel;
	private final String rutaImagen = "/vista/imagenes/Charlestoon.gif";

	/** serialVersionUID*/  
	private static final long serialVersionUID = -4660754389327191558L;

	/**
	 * Create the panel.
	 */
	public About() {
		frame = new JFrame();
		frame.setResizable(false);
		panel = new JPanel();
		panel.setBounds(12, 12, 447, 343);
		etiqueta = new JLabel("");
		configuracion();
	}
	
	private void configuracion() {
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		etiqueta.setBounds(62, 25, 345, 271);
		etiqueta.setBorder(new LineBorder(new Color(0, 0, 128), 2, true));
		etiqueta.setVerticalAlignment(SwingConstants.TOP);
		etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
		etiqueta.setIcon(IO.getIcon(rutaImagen, etiqueta.getWidth(), etiqueta.getHeight()));

		
		panel.add(etiqueta);
		
		btnCerrar = new JButton("Cerrar");
		btnCerrar.setBounds(191, 515, 79, 25);
		frame.getContentPane().add(btnCerrar);
		btnCerrar.setVerticalAlignment(SwingConstants.BOTTOM);
		btnCerrar.addMouseListener(new BotonListener());
	
		frame.setTitle("Acerca de...");
		frame.setSize(475,583);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	
	/**
	 * Observador de acción de pulsar con el ratón sobre el boton para cerrar la ventana (JFrame).
	 * @author Silverio Manuel Rosales Santana
	 * @date 7 ago. 2021
	 * @version versión
	 */
	private class BotonListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {frame.dispose();}
	}
	
	/**
	 * Cambia la propiedad de visibilidad del frame.
	 * Esta función actua como un interruptor de visibilidad, cambiando de visible
	 * a oculto cada vez que se le llama. 
	 */
	public void toggleVisible() {
		frame.setVisible(!frame.isVisible());									//Creamos una función toggle de visibilidad.
	}
	
	/**
	 * Para realización de pruebas.
	 * @param args argumentos.
	 */
	public static void main(String[] args) {
		About about = new About();
		about.toggleVisible();
	}
}
