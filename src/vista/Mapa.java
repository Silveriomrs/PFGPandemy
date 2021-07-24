/**
 * 
 */
package vista;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import modelo.FondoPanel;
import modelo.Zona;

/**
 * Clase para la representación en modo de mapa de los datos de un núcleo
 * poblacional (provincia o comunidad autónoma).
 * @author Silverio Manuel Rosales Santana
 * @date 10/07/2021
 * @version 1.0
 *
 */
public class Mapa extends JPanel {

	private static final long serialVersionUID = 6251836932416777274L;
	private JFrame frame;
	private FondoPanel fondo;
	private HashMap<String,Zona> zonas;
	private ArrayList<Color> paleta;
	
	/**
	 * Creación del panel de dimensiones dadas (heigth, width).
	 * @param height ancho del mapa.
	 * @param width altura del mapa.
	 * @param paleta Colores representantes de los grados de contagio.
	 */
	public Mapa(int width, int height, ArrayList<Color> paleta) {
		super();
		this.paleta = paleta;
		setBorder(new LineBorder(new Color(0, 0, 0)));
		zonas = new HashMap<String, Zona>();
		setBackground(Color.LIGHT_GRAY);
		setLayout(new FlowLayout());
		
		frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setTitle("Mapa");
		frame.setSize(825,590);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
        repaint();
	//	setFondo("/vista/imagenes/mapa-mudo-CCAA.jpg");	
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		zonas.forEach((n,z) -> {
			g.setColor(paleta.get(z.getNivel()));
			g.fillPolygon(z.getZona());
	//		g.drawPolygon(z.getZona());
		});
	}
	
	/**
	 * Carga una imagen de fondo para el panel.
	 * @param nombre nombre de la imagen y su ruta.
	 */
	public void setFondo(String nombre) {
		fondo = new FondoPanel(nombre);
		frame.setContentPane(fondo);	
	}
	
	/**
	 * Devuelve el poligono que representa una zona del mapa.
	 * @param id Identificador de la zona.
	 * @return devuelve zona del mapa.
	 */
	public Zona getZona(String id) {return zonas.get(id);}

	/**
	 * Añade una nueva zona al mapa. En caso de que exista una zona con el mismo
	 * nombre, no añadirá la última.
	 * @param z Zona que representa el poligono.
	 */
	public void addZona(Zona z) {zonas.putIfAbsent(z.getID(), z);}

	/**
	 * Devuelve el grado de contagio de una zona o null si esta no existe.
	 * @param id ID de la zona.
	 * @return Nivel asignado a una zona, null si dicha zona no existe.
	 */
	public int getZonaNivel(String id) {return zonas.get(id).getNivel();}

	/**
	 * Establece el grado de contagio de una zona.
	 * @param id ID de la zona.
	 * @param n Nivel de asignación.
	 */
	public void setZonaNivel(String id, int n) {
		if(zonas.containsKey(id)) {												//Comprobación de que existe.
			zonas.get(id).setNivel(n);
		}
	}
	

	/**
	 * Devuelve el número de zonas que contiene el mapa.
	 * @return número de zonas que contiene el mapa.
	 */
	public int getNumZones() {return zonas.size();}
	
	
}
