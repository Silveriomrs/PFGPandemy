package vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JColorChooser;


/**
 * <p>Title: Leyenda</p>  
 * <p>Description: Clase para mostrar la leyenda de un mapa. </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 29 jul. 2021
 * @version versión
 */
public class Leyenda extends JPanel implements ActionListener{

	/**
	 * Frame de la leyenda para soportar modo de visualización flotante.
	 */
	private JFrame frmLeyenda;
	/**
	 * Paleta de colores con equivalencia a los diferentes niveles de contagio.
	 */
	private ArrayList<Color> paleta;
	private static final long serialVersionUID = 3521309276542156368L;

	/**
	 * Creación de una leyenda con la representación de los valores y sus grados
	 * de color. 
	 * @param width Ancho del panel de leyenda.
	 * @param height Alto del panel de leyenda.
	 * @param editable Indica si los colores son editables, true si lo son,
	 * false en otro caso.
	 */
	public Leyenda(int width, int height, boolean editable) {
		super();
		paleta = new ArrayList<Color>();
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBackground(Color.LIGHT_GRAY);
		
		frmLeyenda = new JFrame();
		frmLeyenda.getContentPane().setLayout(new BoxLayout(frmLeyenda.getContentPane(), BoxLayout.X_AXIS));
		frmLeyenda.getContentPane().add(this);
		setLayout(null);
		
		paletaBase();
		creaEtiquetas(50,0,50,20);
		creaBotones(0,0,50,20,editable);
			
		frmLeyenda.setTitle("Leyenda");
		frmLeyenda.setSize(width,height);
		frmLeyenda.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmLeyenda.setVisible(true);
        repaint();
	//	setFondo("/vista/imagenes/mapa-mudo-CCAA.jpg");	
	}
	
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
			button.setBounds(x, y, h, w);
			button.setBackground(paleta.get(i));
			button.setName(""+i);
			button.setEnabled(editable);
			button.setEnabled(true);
			button.addActionListener(this);
			this.add(button);
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
	private void paletaBase() {
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
	public void setColor(int i, Color c) {paleta.set(i,c);}
	
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
	public void setPaleta(ArrayList<Color> paleta) {this.paleta = paleta;}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Obtención del botón pulsado
		JButton boton = (JButton)e.getSource();									
		//Obtención de un color desde el selector.
		Color color = JColorChooser.showDialog(null, "Seleccione nuevo color", null);
		//Establece color si no es nulo.
		if(color != null) {boton.setBackground(color);}							
	}
	
}
