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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import controlador.IO;
import modelo.ImagesList;
import modelo.Labels_GUI;
import modelo.OperationsType;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.UIManager;

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
	private JPanel panel_animado;
	private final String rutaImagen = ImagesList.ABOUT_IMG_1;
	private final String rutaFondo = ImagesList.BCKGND_ABOUT;

	/** serialVersionUID*/  
	private static final long serialVersionUID = -4660754389327191558L;

	/**
	 * Crea el panel con la configuración de los atributos generales.
	 */
	public About() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 255, 224));
		frame.setResizable(false);
		panel_animado = new JPanel();
		panel_animado.setOpaque(false);
		panel_animado.setBounds(35, 12, 398, 316);
		etiqueta = new JLabel("");
		setOpaque(false);
		setLayout(null);
		configuracion();
	}
	
	/**
	 * Sobrescritura del método heredado de dibujado con el objetivo de permitir
	 *  colocar una imagen de fondo centrada y con efecto transparente.
	 */
	@Override
	public void paint(Graphics g) {
		if(rutaFondo != null) {
			Image imagen = new ImageIcon(getClass().getResource(rutaFondo)).getImage();
			g.drawImage(imagen,getX(),getY(),getWidth(),getHeight(),this);
			super.paint(g);
		}
	}
	
	/**
	 * Configura todos los aspectos particulares de la vista a mostrar al usuario.
	 */
	private void configuracion() {
		setLayout(null);
		add(panel_animado);
		panel_animado.setLayout(null);
		
		etiqueta.setBounds(24, 12, 345, 271);
		etiqueta.setBorder(new LineBorder(new Color(0, 0, 128), 2, true));
		etiqueta.setVerticalAlignment(SwingConstants.TOP);
		etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
		etiqueta.setIcon(IO.getIcon(rutaImagen, etiqueta.getWidth(), etiqueta.getHeight()));
		panel_animado.add(etiqueta);
		
		btnCerrar = new JButton(OperationsType.CLOSE.toString());
		btnCerrar.setBackground(UIManager.getColor("Button.background"));
		btnCerrar.setBounds(191, 515, 79, 25);
		add(btnCerrar);
		btnCerrar.setVerticalAlignment(SwingConstants.BOTTOM);
		
		JPanel panel_datos = new JPanel();
		panel_datos.setOpaque(false);
		panel_datos.setBorder(new TitledBorder(new CompoundBorder(new LineBorder(new Color(138, 43, 226), 2, true), new LineBorder(new Color(0, 128, 0), 1, true)), "Versi\u00F3n 1.0", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 128)));
		panel_datos.setBounds(12, 338, 447, 150);
		add(panel_datos);
		panel_datos.setLayout(null);
		
		String texto = Labels_GUI.TXT_1_ABOUT;
		texto += Labels_GUI.TXT_2_ABOUT;
		texto += Labels_GUI.TXT_3_ABOUT;
		JLabel lblAplicacinDesarrolladaComo = new JLabel(texto);
		lblAplicacinDesarrolladaComo.setHorizontalAlignment(SwingConstants.CENTER);
		lblAplicacinDesarrolladaComo.setFont(new Font("DejaVu Sans Mono", Font.ITALIC, 13));
		lblAplicacinDesarrolladaComo.setBounds(23, 12, 412, 51);
		panel_datos.add(lblAplicacinDesarrolladaComo);
		
		JLabel lblDirectorDrFernando = new JLabel(Labels_GUI.NAME_DIRECTOR_INITIAL);
		lblDirectorDrFernando.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		lblDirectorDrFernando.setBounds(111, 75, 204, 15);
		panel_datos.add(lblDirectorDrFernando);
		
		JLabel lblNewLabel = new JLabel(Labels_GUI.NAME_SILVERIO);
		lblNewLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		lblNewLabel.setBounds(111, 102, 108, 15);
		panel_datos.add(lblNewLabel);
		
		JLabel lblDireccin = new JLabel(Labels_GUI.TXT_5_ABOUT);
		lblDireccin.setBounds(29, 76, 70, 15);
		panel_datos.add(lblDireccin);
		
		JLabel lblAutor = new JLabel(Labels_GUI.L_AUTHOR);
		lblAutor.setBounds(29, 103, 70, 15);
		panel_datos.add(lblAutor);
		btnCerrar.addMouseListener(new BotonListener());
	
		frame.setTitle(Labels_GUI.TXT_6_ABOUT);
		frame.setSize(475,583);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().add(this);
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
	public void toggleVisible() {frame.setVisible(!frame.isVisible());}			//Creamos una función toggle de visibilidad.
	
	/**
	 * Para realización de pruebas.
	 * @param args argumentos.
	 */
	public static void main(String[] args) {
		About about = new About();
		about.toggleVisible();
	}
}
