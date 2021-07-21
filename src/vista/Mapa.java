/**
 * 
 */
package vista;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

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
	private HashMap<String,Polygon> zonas;
	private HashMap<String,Color> zonasColor;
	
	
	/**
	 * Creación del panel de dimensiones dadas (heigth, width).
	 * @param height ancho del mapa.
	 * @param width altura del mapa.
	 */
	public Mapa(int width, int height) {
		super();
		setBorder(new LineBorder(new Color(0, 0, 0)));
		zonas = new HashMap<String, Polygon>();
		zonasColor = new HashMap<String, Color>();
		setBackground(Color.LIGHT_GRAY);
		setLayout(new FlowLayout());
		frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setTitle("Mapa");
		frame.setSize(width,height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		zonas.forEach((n,p) -> {
			g.setColor(zonasColor.get(n));
			//g.fillPolygon(p);
			g.drawPolygon(p);
		});
	}
	
	/**
	 * Devuelve el poligono que representa una zona del mapa.
	 * @param nombre de la zona.
	 * @return devuelve el poligono que representa la zona.
	 */
	public Polygon getZona(String nombre) {return zonas.get(nombre);}

	/**
	 * Añade una nueva zona al mapa. En caso de que exista una zona con el mismo
	 * nombre, no añadirá la última.
	 * @param nombre De la zona que representa el poligono.
	 * @param p Poligono de la zona.
	 */
	public void addZona(String nombre, Polygon p) {zonas.putIfAbsent(nombre, p);}

	/**
	 * Devuelve el color de una zona o null si esta no existe.
	 * @param z Nombre de la zona.
	 * @return Color asignado a una zona, null si dicha zona no existe.
	 */
	public Color getZonaColor(String z) {return zonasColor.get(z);}

	/**
	 * Establece el color de una zona almacenandola.
	 * @param z Nombre de la zona.
	 * @param c Color de asignación.
	 */
	public void setZonaColor(String z, Color c) {zonasColor.put(z, c);}
	

	/**
	 * Devuelve el número de zonas que contiene el mapa.
	 * @return número de zonas que contiene el mapa.
	 */
	public int getNumZones() {return zonas.size();}
	
	
}
