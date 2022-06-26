package vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controlador.ControladorModulos;
import modelo.TypesFiles;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JColorChooser;


/**
 * <p>Clase para mostrar la leyenda de un mapa.</p>
 * Esta clase permite cambiar los valores de los colores de forma que se puede
 * seleccionar nuevas paletas de colores y exportar.
 * <p>Este módulo permite operar en dos modos, editable y no editable, con el primero
 * se habilita la posibilidad de modificar la paleta, con el segundo solo se accede en 
 *  modo lectura.</p>  
 * @author Silverio Manuel Rosales Santana
 * @date 29 jul. 2021
 * @version versión 1.2
 */
public class Paleta extends JPanel{

	/**
	 * Frame de la leyenda para soportar modo de visualización flotante.
	 */
	private JFrame frame;
	private HashMap<String,JButton> mapaBotones;
	private ControladorModulos cm;
	private static final long serialVersionUID = 3521309276542156368L;
	private int w;
	private int h;
	private boolean editable;
	
	/**
	 * Creación de una leyenda con la representación de los valores y sus grados
	 * de color. 
	 * @param cm Controlador de los módulos. Necesario para integrarse con el sistema.
	 * @param width Ancho del panel de leyenda.
	 * @param height Alto del panel de leyenda.
	 * false en otro caso.
	 */
	public Paleta(ControladorModulos cm,int width, int height) {
		super();
		this.cm = cm;
		this.w = width;
		this.h = height;
		this.editable = false;
		this.mapaBotones = new HashMap<String, JButton>();
		
		this.setBorder(new LineBorder(new Color(0, 0, 0)));
		this.setBackground(Color.LIGHT_GRAY);
		TitledBorder tb = BorderFactory.createTitledBorder("Paleta");
		this.setBorder(tb);
		this.setLayout(null);
		
		creaEtiquetas(65,20,100,20);												//Crea y dibuja las etiquetas.	
		creaBotones(10,20,50,20);												//Crea y dibuja los botones.
		iniciarFrame();
		refresh();
	}
	
	/**
	 * Inicia los parámetros de la ventana que contendrá la vista de la paleta,
	 *  ajustándolo a los atributos deseados en la aplicación, entre ellos, la posición
	 *   y las dimensiones.
	 */
	private void iniciarFrame(){
		frame = new JFrame();
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		frame.getContentPane().add(this);
		frame.setTitle("Paleta");												//Establecimiento del título.
		frame.setSize(w,h);														//Establecimiento de las dimensiones.
		frame.setResizable(false); 												//Dimesiones fijas.
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);					//Comportamiento al cerrar el frame.
		frame.setAlwaysOnTop(false);
	}
	
	/**
	 * <p>Reinicia la vista de este módulo.</p> 
	 *  Elimina los datos previamente almacenados en el mismo.
	 */
	public void reset() {
		updateUI();
	}

	/**
	 *  Establece el frame como visible o no.
	 * @param ver TRUE para hacerlo visible, FALSE en otro caso.
	 */
	public void setFrameVisible(boolean ver) {frame.setVisible(ver);}
	
	/**
	 * <p>Cambia la propiedad de visibilidad del estado anterior</p>
	 * Si el estado actual es Visible pasa a estar oculto, de la misma manera
	 * cuando el estado actual es oculto pasa a estar visible.  
	 */
	public void toggleFrameVisible() {setFrameVisible(!frame.isVisible());}
	
	/**
	 * Establece la posición para el frame.
	 * @param xPos Posición X relativa a la pantalla.
	 * @param yPos Posición Y relativa a la pantalla.
	 */
	public void setPosicion(int xPos, int yPos) {frame.setLocation(xPos,yPos);}
		
	
	/**
	 * <p>Establece como editable o no la paleta de colores.</p> 
	 * Cuando cambia a editable permite que se pueda modificar la paleta de colores.
	 * @param edit TRUE para permitir edición. FALSE en caso contrario.
	 */
	public void setEditable(boolean edit) {
		this.editable = edit;
		mapaBotones.forEach((key,boton) -> {
			boton.setEnabled(edit);
		});
	}
	
	/**
	 * Dibuja las etiquetas en la leyenda en las coordenadas indicadas por 
	 * parámetros.
	 * @param x Coordenada x de la posición de alineación horizontal de los botones.
	 * @param y0 Coordenada y de la posición inicial del primer botón.
	 * @param w Altura de los botones.
	 * @param h Anchura de los botones.
	 */
	private void creaEtiquetas(int x, int y0, int w, int h) {
		int separacion = 15;
		int escala = 100;
		int y = y0;
		for(int i=0; i<10; i++) {
			JLabel nivel = null;
			if(i == 0) nivel = new JLabel("< " + escala);
			else if(i == 9) nivel = new JLabel(">= " + escala);
			else {
				nivel = new JLabel(">= " + escala + " < " + (escala + 100));
				escala += 100;
			}
			nivel.setBounds(x, y, w, h);
			add(nivel);
			y += separacion;
		}
	}
	
	/**
	 * Crea los botones de la leyenda en las coordenadas y formas indicadas en
	 * los parámetros.
	 * @param x Coordenada x de la posición de alineación horizontal de los botones.
	 * @param y0 Coordenada y de la posición inicial del primer botón.
	 * @param h Altura de los botones.
	 * @param w Anchura de los botones
	 * 	true para permitir edición, false en otro caso.
	 */
	private void creaBotones(int x, int y0, int h, int w) {
		int separacion = 15;
		int y = y0;
		for(int i=0; i<10; i++) {
			JButton button = new JButton("");
			String name = "L" + String.valueOf(i);
			button.setBounds(x, y, h, w);
			button.setName(name);
			button.setEnabled(true);
//			button.addActionListener(this);
			button.addActionListener(new BotonL(button));
			mapaBotones.put(name, button);
			this.add(mapaBotones.get(name));
			y += separacion;
		}
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
	
	/**
	 * Actualiza los colores de los botones con los valores
	 *  que consten en el controlador de módulos.
	 */
	public void refresh() {
		if(cm.hasModule(TypesFiles.PAL)) {
			for(int i=0; i<10; i++) {
				String label = "L" + String.valueOf(i);
				//Obtener línea
				int index = cm.getModule(TypesFiles.PAL).getFilaItem(label);
				//Obtener colores
				int r = Integer.parseInt((String) cm.getModule(TypesFiles.PAL).getValueAt(index, 1));
				int g = Integer.parseInt((String) cm.getModule(TypesFiles.PAL).getValueAt(index, 2));
				int b = Integer.parseInt((String) cm.getModule(TypesFiles.PAL).getValueAt(index, 3));
				Color c = new Color(r,g,b);
				//Añadir al mapa de botones.							
				if(mapaBotones.containsKey(label)) {
					mapaBotones.get(label).setBackground(c);
				}
			}	
		}
		repaint();
	}
	
	/**
	 * Genera el observador que actuará en función del estado de cada botón que 
	 *  representa a cada uno de los colores de la paleta. Actúa cuando el atributo
	 *   "editable" de la paleta está activo.
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BotonL implements ActionListener{
		JButton boton;
		
		public BotonL(JButton boton) {
			this.boton = boton;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(editable) {
				//Obtención de su posición
				String level = boton.getName();
				//Obtención de un color desde el selector.
				Color color = JColorChooser.showDialog(null, "Seleccione nuevo color", null);
				//Establece color si no es nulo.
				if(color != null) {
					boton.setBackground(color);
					setVisible(true);
					//Añadir nuevo color a la paleta en sustitución del anterior en el módulo.
					int r = color.getRed();
					int g = color.getGreen();
					int b = color.getBlue();
					cm.doActionPAL(level,r,g,b);
				}
			}
		}
	}
	
}
