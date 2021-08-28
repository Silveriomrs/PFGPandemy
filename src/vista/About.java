/**  
* <p>Title: About.java</p>  
* <p>Description: Clase diseñada para mostrar información acerca de esta
* aplicación.</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 6 ago. 2021  
* @version 1.0  
*/  
package vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * <p>Title: About</p>  
 * <p>Description: Muestra la información acerca del proyeccto</p>  
 * @author Silverio Manuel Rosales Santana
 * @date 6 ago. 2021
 * @version versión 1.2
 */
public class About extends JPanel {
	private JFrame frame;
	private JLabel etiqueta;
	private JButton btnCerrar;
	private ImageIcon imagen;
	private JPanel panel;

	/** serialVersionUID*/  
	private static final long serialVersionUID = -4660754389327191558L;

	/**
	 * Create the panel.
	 */
	public About() {
		frame = new JFrame();
		frame.setResizable(false);
		panel = new JPanel();
		panel.setBounds(0, 5, 447, 343);
		etiqueta = new JLabel("");
		configuracion();
		setImagenLbl(etiqueta,"/vista/imagenes/Charlestoon.gif");
	}
	
	private void configuracion() {
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		etiqueta.setBounds(62, 25, 345, 271);
		etiqueta.setBorder(new LineBorder(new Color(0, 0, 128), 2, true));
		etiqueta.setVerticalAlignment(SwingConstants.TOP);
		etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
		etiqueta.setBackground(new Color(0, 0, 0));
		
		panel.add(etiqueta);
		
		btnCerrar = new JButton("Cerrar");
		btnCerrar.setBounds(191, 515, 79, 25);
		frame.getContentPane().add(btnCerrar);
		btnCerrar.setVerticalAlignment(SwingConstants.BOTTOM);
		btnCerrar.addMouseListener(new BotonListener());
	
		frame.setTitle("Acerca de...");
		frame.setSize(459,579);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	/**
	 * <p>Title: setImagenLbl</p>  
	 * <p>Description: Establece una imagen como relleno a una etiqueta</p>
	 * La imagen tiene propiedades de escalado automático, ajustandose a las 
	 * dimensiones de la etiqueta.
	 * @param etiqueta Estiqueta donde se establecerá la imagen.
	 * @param ruta Ruta en un dispositivo físico.
	 */
	private void setImagenLbl(JLabel etiqueta, String ruta) {
		this.imagen = new ImageIcon(ruta);
		Icon icon;
		int w = etiqueta.getWidth();
		int h = etiqueta.getHeight();
		icon = new ImageIcon(
				this.imagen.getImage().getScaledInstance(w, h, Image.SCALE_DEFAULT)
		);
//		icon = new ImageIcon(About.class.getResource(ruta));
		
		this.etiqueta.setIcon(icon);
		System.out.println("w: " + w + ", h: " + h);							//Check del tamaño actual del label.
		this.repaint();
	}
	
	/**
	 * <p>Title: BotonListener</p>  
	 * <p>Description: Observador de acción de pulsar con el ratón sobre el
	 * boton para cerrar la ventana (JFrame)</p>  
	 * @author Silverio Manuel Rosales Santana
	 * @date 7 ago. 2021
	 * @version versión
	 */
	private class BotonListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {frame.dispose();}
	}
	
	/**
	 * <p>Title: setVisible</p>  
	 * <p>Description: Cambia la propiedad de visibilidad del frame</p>
	 * Esta función actua como un interruptor de visibilidad, cambiando de visible
	 * a oculto cada vez que se le llama. 
	 */
	public void setVisible() {
		frame.setVisible(!frame.isVisible());									//Creamos una función toggle de visibilidad.
	}
}
