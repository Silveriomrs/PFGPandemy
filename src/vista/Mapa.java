/**
 *
 */
package vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import controlador.ControladorModulos;
import modelo.Zona;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;

/**
 * Clase para la representación en modo de mapa de los datos de un núcleo
 * poblacional (provincia o comunidad autónoma).
 * @author Silverio Manuel Rosales Santana
 * @date 10/07/2021
 * @version 1.4
 *
 */
public class Mapa extends JPanel{

	private static final long serialVersionUID = 6251836932416777274L;
	/** frame Marco para dibujado del mapa en modo flotante*/
	private JFrame frame;
	/** zonas Conjunto con las zonas que contiene el mapa.*/
	private HashMap<Integer,Zona> zonas;
	/** leyenda Leyenda del mapa.*/
	private Leyenda leyenda;

	/**
	 * Creación del panel de dimensiones dadas (heigth, width).
	 * @param width ancho del mapa.
	 * @param height altura del mapa.
	 * @param leyenda Leyenda con los colores y sus valores.
	 */
	public Mapa(int width, int height, Leyenda leyenda) {
		super();
		this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		this.setPreferredSize(ControladorModulos.PanelCentralDim);
		this.setMinimumSize(ControladorModulos.MinDim);
		this.leyenda = leyenda;
		this.zonas = new HashMap<Integer, Zona>();
		this.addMouseListener(new SelectorPoligono());
		this.setBorder(new LineBorder(new Color(0, 0, 0)));
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(new BorderLayout(0, 0));
		iniciarFrame(width,height);
	}

	/**
	 * <p>Title: verFrame</p>
	 * <p>Description: Aunar códigos de las propiedades del frame.</p>
	 * @param w Ancho del marco.
	 * @param h Alto del marco.
	 */
	public void iniciarFrame(int w, int h){
		frame = new JFrame();
		frame.setTitle("Mapa");
		frame.setSize(w,h);
	    frame.setMaximumSize(new Dimension(2767, 2767));
		frame.setMinimumSize(ControladorModulos.MinDim);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
	}

	/**
	 * <p>Title: verFrame</p>
	 * <p>Description: Activa o desactiva la visibilidad del frame.</p>
	 * @param activado True para hacerlo visible, False en otro caso.
	 */
	public void verFrame(boolean activado) {frame.setVisible(activado);}

	/**
	 * <p>Title: toogleVisible</p>
	 * <p>Description: Permite cambiar el modo de visibilidad del panel entre oculto y visible</p>
	 * Para tal efecto consulta el estado previo y configura el estado actual en
	 * función del anterior.
	 */
	public void toogleVisible() {this.setVisible(!this.isVisible());}

	 /** <p>Title: setPosicion</p>
	 * <p>Description: Establece la posición para el frame</p>
	 * @param xPos Posición X relativa a la pantalla.
	 * @param yPos Posición Y relativa a la pantalla.
	 */
	public void setPosicion(int xPos, int yPos) {frame.setLocation(xPos,yPos);}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

		zonas.forEach((n,z) -> {
			if(z.getZona() != null) {
				g2.setPaint(leyenda.getColor(z.getNivel()));
				g2.fill(z.getZona());
				g2.setPaint(Color.BLACK);
				g2.draw(z.getZona());
			}
		});
		System.out.println("Mapa - paintComponent > refrescando");
//		this.updateUI();														//Redibujado y actualización del panel.
	}

	/**
	 * <p>Title: getPanel</p>
	 * <p>Description: Obtiene el JPanel contenedor del mapa </p>
	 * @return Mapa embebido dentro de un JPanel.
	 */
	public JPanel getPanel() {return this;}

	/**
	 * Devuelve el poligono que representa una zona del mapa.
	 * @param id Identificador de la zona.
	 * @return devuelve zona del mapa. Null en caso de no existir.
	 */
	public Zona getZona(int id) {
		Zona z = null;
		if(zonas.containsKey(id)) z = zonas.get(id);
		return z;
	}

	/**
	 * Añade una nueva zona al mapa. En caso de que exista una zona con el mismo
	 * nombre, sobreescribirá la anterior.
	 * @param z Zona que representa el poligono.
	 */
	public void addZona(Zona z) {if(z != null) zonas.put(z.getID(), z);}

	/**
	 * Devuelve el grado de contagio de una zona o null si esta no existe.
	 * @param id ID de la zona.
	 * @return Nivel asignado a una zona, null si dicha zona no existe.
	 */
	public int getZonaNivel(int id) {return zonas.get(id).getNivel();}

	/**
	 * Establece el grado de contagio de una zona.
	 * @param id ID de la zona.
	 * @param serie Nombre de la serie asociada al nivel almacenado.
	 * @param n Nivel de asignación.
	 */
	public void addZonaNivel(int id, String serie, int n) {
		if(zonas.containsKey(id)) {												//Comprobación de que existe.
			zonas.get(id).addNivel(serie,0, n);									//Añadir valor a la serie que corresponda.
		}
	}

	/**
	 * Devuelve el número de zonas que contiene el mapa.
	 * @return número de zonas que contiene el mapa.
	 */
	public int getNumZones() {return zonas.size();}

	/**
	 * <p>Title: getZonas</p>
	 * <p>Description: Devuelve las instancias de zonas en un HashMap cuya
	 * clave es el ID (Integer) y el valor es una instancia de la clase Zona</p>
	 * @return El conjunto de zonas.
	 */
	public HashMap<Integer,Zona> getZonas(){return this.zonas;}

	/**
	 * <p>Title: setZonas</p>
	 * <p>Description: Establece el conjunto de zonas</p>
	 * @param zonas2 Mapa cuya clave es la ID de cada zona y valor la zona.
	 */
	public void setZonas(HashMap<Integer, Zona> zonas2) { this.zonas = zonas2;}

	private class SelectorPoligono extends MouseAdapter	{
	    @Override
		public void mousePressed(MouseEvent e)  {
	    	//Obtención del punto del mapa que ha recibido la pulsación.
	        Point p = e.getPoint();
	        Zona z = getZona(p);
	        if(z != null) {
	        	GraficasChart chart = z.getGrafica();
        		chart.genera();
        		chart.setVisible(true);
	        }
	    }

	    private Zona getZona(Point p) {
	    	boolean encontrado = false;
	    	Zona zona = null;
	    	Zona zAux = null;
	        //Busqueda del poligono que contiene dicha coordenada.
	        Iterator<Map.Entry<Integer, Zona>> it = zonas.entrySet().iterator();
	        while (it.hasNext() && !encontrado) {
	            zAux = it.next().getValue();									//Obtención del nuevo set sin el valor anterior.
	            //Si se ha encontrado se termina la búsqueda.
	            if(zAux.getZona() != null && zAux.getZona().contains(p)) {		//Evita evaluar valores nulos
	        		zona = zAux;												//Valor final a devolver.
	        		encontrado = true;											//Detenemos la búsqueda para ahorrar recursos.
	            }
	        }
	    	return zona;
	    }
	}

	/* Funciones para pruebas */


	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		Mapa mapa = new Mapa(500, 500, new Leyenda(100, 205, false));
		mapa.iniciarFrame(520, 520);
		mapa.verFrame(true);
	}
}