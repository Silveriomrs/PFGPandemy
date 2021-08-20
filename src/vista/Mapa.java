/**
 * 
 */
package vista;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import modelo.FondoPanel;
import modelo.Zona;
import java.awt.CardLayout;
import javax.swing.BoxLayout;

/**
 * Clase para la representación en modo de mapa de los datos de un núcleo
 * poblacional (provincia o comunidad autónoma).
 * @author Silverio Manuel Rosales Santana
 * @date 10/07/2021
 * @version 1.0
 *
 */
public class Mapa extends JPanel{

	private static final long serialVersionUID = 6251836932416777274L;
	/** frame Marco para dibujado del mapa en modo flotante*/  
	private JFrame frame;
	/** fondo Imagen de fondo establecida*/  
	private FondoPanel fondo;
	/** zonas Conjunto con las zonas que contiene el mapa.*/  
	private HashMap<Integer,Zona> zonas;
	/** leyenda Leyenda del mapa.*/  
	private Leyenda leyenda;
	
	/**
	 * Creación del panel de dimensiones dadas (heigth, width).
	 * @param height ancho del mapa.
	 * @param width altura del mapa.
	 * @param leyenda Leyenda con los colores y sus valores.
	 */
	public Mapa(int width, int height, Leyenda leyenda) {
		super();
		this.leyenda = leyenda;
		zonas = new HashMap<Integer, Zona>();
		
		setBorder(new LineBorder(new Color(0, 0, 0)));	
		setBackground(Color.LIGHT_GRAY);
		setOpaque(false);
	
	//	setFondo("/vista/imagenes/mapa-mudo-CCAA.jpg");
		configuracion();	
		
	}
	
	/**
	 * <p>Title: configuracion</p>  
	 * <p>Description: Aunar códigos de las propiedades del frame.</p>
	 */
	private void configuracion(){
		frame = new JFrame();
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		frame.getContentPane().add(this);
		setLayout(new CardLayout(0, 0));
		frame.setTitle("Mapa");
		frame.setSize(825,590);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	@Override
	public void setVisible(boolean ver) {frame.setVisible(ver);}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		zonas.forEach((n,z) -> {
			g.setColor(leyenda.getColor(z.getNivel()));
			g.fillPolygon(z.getZona());
		});
		this.updateUI();														//Redibujado y actualización del panel.
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
	public Zona getZona(int id) {return zonas.get(id);}

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
	public int getZonaNivel(int id) {return zonas.get(id).getNivel();}
	
	/**
	 * Establece el grado de contagio de una zona.
	 * @param id ID de la zona.
	 * @param d Fecha asociada al nivel almacenado.
	 * @param n Nivel de asignación.
	 */
	public void addZonaNivel(int id, Date d, int n) {
		if(zonas.containsKey(id)) {												//Comprobación de que existe.
			zonas.get(id).addNivel(d, n);
		}
	}

	/**
	 * Devuelve el número de zonas que contiene el mapa.
	 * @return número de zonas que contiene el mapa.
	 */
	public int getNumZones() {return zonas.size();}
}
