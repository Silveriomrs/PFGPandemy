package vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JColorChooser;


/**
 * <p>Title: Paleta</p>  
 * <p>Description: Clase para mostrar la leyenda de un mapa. </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 29 jul. 2021
 * @version versión 1.2
 */
public class Paleta extends JPanel implements ActionListener{

	/**
	 * Frame de la leyenda para soportar modo de visualización flotante.
	 */
	private JFrame frame;
	/**
	 * Paleta de colores con equivalencia a los diferentes niveles de contagio.
	 */
	private ArrayList<Color> paleta;
	private HashMap<String,JButton> mapaBotones;
	private static final long serialVersionUID = 3521309276542156368L;
	private int width;
	private int height;
	private boolean editable;
	
	/**
	 * Creación de una leyenda con la representación de los valores y sus grados
	 * de color. 
	 * @param width Ancho del panel de leyenda.
	 * @param height Alto del panel de leyenda.
	 * @param editable Indica si los colores son editables, true si lo son,
	 * false en otro caso.
	 */
	public Paleta(int width, int height, boolean editable) {
		super();
		this.width = width;
		this.height = height;
		this.editable = editable;
		this.mapaBotones = new HashMap<String, JButton>();
		
		this.setBorder(new LineBorder(new Color(0, 0, 0)));
		this.setBackground(Color.LIGHT_GRAY);
		TitledBorder tb = BorderFactory.createTitledBorder("Paleta");
		this.setBorder(tb);
		this.setLayout(null);
		
		paletaBase();															//Carga la paleta base.
		creaEtiquetas(50,20,50,20);												//Crea y dibuja las etiquetas.
		creaBotones(0,20,50,20,editable);										//Crea y dibuja los botones.
		iniciarFrame();
        repaint();
	}
	
	private void iniciarFrame(){
		frame = new JFrame();
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		frame.getContentPane().add(this);
		frame.setTitle("Paleta");												//Establecimiento del título.
		frame.setSize(width,height);											//Establecimiento de las dimensiones.
		frame.setResizable(false); 												//Dimesiones fijas.
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);					//Comportamiento al cerrar el frame.
		frame.setAlwaysOnTop(false);
	}
	
	/**
	 * <p>Title: getFrame</p>  
	 * <p>Description: Devuelve el frame contenedor para poder manipular sus
	 * propiedades.</p> 
	 * @return JFrame con la configuración actual.
	 */
	public JFrame getFrame() {return frame;}
	
	/**
	 * <p>Title: reset</p>  
	 * <p>Description: Reinicia la vista de este módulo.</p> 
	 *  Elimina los datos previamente almacenados en el mismo.
	 */
	public void reset() {
		paletaBase();
		updateUI();
	}

	/**
	 * <p>Title: setFrameVisible</p>  
	 * <p>Description: Establece el frame como visible o no.</p> 
	 * @param ver TRUE para hacerlo visible, FALSE en otro caso.
	 */
	public void setFrameVisible(boolean ver) {frame.setVisible(ver);}
	
	/**
	 * <p>Title: toggleVisible</p>  
	 * <p>Description: Cambia la propiedad de visibilidad del estado anterior</p>
	 * Si el estado actual es Visible pasa a estar oculto, de la misma manera
	 * cuando el estado actual es oculto pasa a estar visible.  
	 */
	public void toggleFrameVisible() {setFrameVisible(!frame.isVisible());}
	
	/**
	 * <p>Title: setPosicion</p>  
	 * <p>Description: Establece la posición para el frame</p> 
	 * @param xPos Posición X relativa a la pantalla.
	 * @param yPos Posición Y relativa a la pantalla.
	 */
	public void setPosicion(int xPos, int yPos) {frame.setLocation(xPos,yPos);}
	
	/**
	 * Dibuja las etiquetas en la leyenda en las coordenadas indicadas por 
	 * parámetros.
	 * @param x Coordenada x de la posición de alineación horizontal de los botones.
	 * @param y0 Coordenada y de la posición inicial del primer botón.
	 * @param h Altura de los botones.
	 * @param w Anchura de los botones
	 */
	private void creaEtiquetas(int x, int y0, int h, int w) {
		int separacion = 15;
		int escala = 100;
		String s = "<";
		int y = y0;
		for(int i=0; i<10; i++) {
			JLabel nivel = new JLabel(s + escala);
			nivel.setBounds(x, y, h, w);
			add(nivel);
			y += separacion;
			escala += 100;
		}
	}
	
	/**
	 * Crea los botones de la leyenda en las coordenadas y formas indicadas en
	 * los parámetros.
	 * @param x Coordenada x de la posición de alineación horizontal de los botones.
	 * @param y0 Coordenada y de la posición inicial del primer botón.
	 * @param h Altura de los botones.
	 * @param w Anchura de los botones
	 * @param editable Establece si los botones tienen caracter editable o no.
	 * 	true para permitir edición, false en otro caso.
	 */
	private void creaBotones(int x, int y0, int h, int w, boolean editable) {
		int separacion = 15;
		int y = y0;
		for(int i=0; i<10; i++) {
			JButton button = new JButton("");
			String name = String.valueOf(i);
			button.setBounds(x, y, h, w);
			button.setBackground(paleta.get(i));
			button.setName(name);
			button.setEnabled(editable);
			button.addActionListener(this);
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
	 * Establece la paleta por defecto de 10 colores correspondiente a cada nivel
	 * desde el 0 al 9 ambos inclusive.
	 */
	public void paletaBase() {
		paleta = new ArrayList<Color>();
		paleta.add(new Color( 82, 190, 128 ));									//Color Nivel 0.
		paleta.add(new Color( 40, 180, 99 ));									//Color Nivel 1.
		paleta.add(new Color( 174, 214, 241 ));									//Color Nivel 2.
		paleta.add(new Color( 46, 134, 193 ));									//Color Nivel 3.
		paleta.add(new Color( 247, 220, 111 ));									//Color Nivel 4.
		paleta.add(new Color( 243, 156, 18 ));									//Color Nivel 5.
		paleta.add(new Color( 210, 180, 222 ));									//Color Nivel 6.
		paleta.add(new Color( 118, 68, 138 ));									//Color Nivel 7.
		paleta.add(new Color( 230, 126, 34 ));									//Color Nivel 8.
		paleta.add(new Color( 231, 76, 60 ));									//Color Nivel 9.
		repaint();
	}
	
	/**
	 * Retorna el panel contenedor de la paleta de colores con su formato,
	 * etiquetas y botones.
	 * @return JPanel con la paleta de colores en formato vertical.
	 */
	public JPanel getPanelPaleta() {return this;}
	
	/**
	 * Devuelve el color correspondiente en la paleta de colores al nivel
	 * indicado por parámetro, en caso de un valor indice incorrecto, devuelve
	 * el valor del color en la primera posición.
	 * @param i Indice de la paleta de colores.
	 * @return El color correspondiente al indice. El color base 0 en otro caso.
	 */
	public Color getColor(int i) {
		Color c = paleta.get(0);
		if(i>=0 && i<10) {c = paleta.get(i);}
		return c;
	}
	
	
	/**
	 * Establecimiento de un color para una posición determinada.
	 * @param i Indice del color a sustituir [0,9].
	 * @param c Color a establecer.
	 */
	private void setColor(int i, Color c) {paleta.set(i,c);}
	
	/**
	 * Devuelve la paleta de colores actual, dentro de un ArrayList<Color>.
	 * @return Paleta de colores actual.
	 */
	public ArrayList<Color> getPaleta(){return paleta;}
	
	/**
	 * Establece una nueva palera de colores. Debe contener al menos
	 * 10 elementos.
	 * @param paleta Nueva paleta de colores.
	 */
	public void setPaleta(ArrayList<Color> paleta) {
		this.paleta = paleta;
		//Repintar los botones.
		for(int i=0; i<10; i++) {
			String name = String.valueOf(i);
			if(mapaBotones.containsKey(name)) {
				mapaBotones.get(name).setBackground(paleta.get(i));
			}
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(editable) {
			//Obtención del botón pulsado
			JButton boton = (JButton)e.getSource();
			//Obtención de su posición
			int pos = Integer.parseInt(boton.getName());
			//Obtención de un color desde el selector.
			Color color = JColorChooser.showDialog(null, "Seleccione nuevo color", null);
			//Establece color si no es nulo.
			if(color != null) {
				boton.setBackground(color);
				this.setVisible(true);
				//Añadir nuevo color a la paleta en sustitución del anterior.
				setColor(pos, color);
			}
		}
	}
	
	@Override
	public String toString() {
		String txt = "";
		String nivel = "Nivel";
		for(int i=0;i<10;i++) {
			txt += nivel + i + "," + paleta.get(i).getRed() + "," + paleta.get(i).getGreen() +
					"," +  paleta.get(i).getBlue();
			if(i<9) {txt += "\n";}
		}
		return txt;
	}
	
	/**
	 * <p>Title: main</p>  
	 * <p>Description: A efectos de pruebas</p> 
	 * @param args Nada.
	 */
	public static void main(String[] args) {
		Paleta paleta = new Paleta(100, 205, false);
		paleta.setPosicion(0, 0);
		paleta.setFrameVisible(true);
		System.out.println(paleta.toString());
	}

}
