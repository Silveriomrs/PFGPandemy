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
 * @version 1.6
 *
 */
public class Mapa extends JPanel{

	private static final long serialVersionUID = 6251836932416777274L;
	/** frame Marco para dibujado del mapa en modo flotante*/
	private JFrame frame;
	private ControladorModulos cm;

	/**
	 * Creación del panel de dimensiones dadas (heigth, width).
	 * @param width ancho del mapa.
	 * @param height altura del mapa.
	 * @param cm Controlador de módulos. Necesario para trabajar con la parte controladora.
	 */
	public Mapa(int width, int height,ControladorModulos cm) {
		super();
		this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		this.setPreferredSize(ControladorModulos.PanelCentralDim);
		this.setMinimumSize(ControladorModulos.MinDim);
		this.cm = cm;
		this.addMouseListener(new SelectorPoligono());
		this.setBorder(new LineBorder(new Color(0, 0, 0)));
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(new BorderLayout(0, 0));
		iniciarFrame(width,height);
	}

	/**
	 * <p>Title: reset</p>  
	 * <p>Description: Reinicia la vista de este módulo.</p> 
	 *  Elimina los datos almacenados en el mismo.
	 */
	public void reset() {
		updateUI();
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

		cm.getZonas().forEach((n,z) -> {
			if(z.getPoligono() != null) {
				g2.setPaint(cm.getLevelColor(z.getNivel()));
				g2.fill(z.getPoligono());
				g2.setPaint(Color.BLACK);
				g2.draw(z.getPoligono());
			}
		});
	}

	/**
	 * Añade una nueva zona al mapa. En caso de que exista una zona con el mismo
	 * nombre, sobreescribirá la anterior.
	 * @param z Zona que representa el poligono.
	 */
	public void addZona(Zona z) {if(z != null) cm.getZonas().put(z.getID(), z);}


	/**
	 * Establece el grado de contagio de una zona.
	 * @param id ID de la zona.
	 * @param serie Nombre de la serie asociada al nivel almacenado.
	 * @param n Nivel de asignación.
	 */
	public void addZonaNivel(int id, String serie, int n) {
		if(cm.getZonas().containsKey(id)) {												//Comprobación de que existe.
			cm.getZonas().get(id).addNivel(serie,0, n);									//Añadir valor a la serie que corresponda.
		}
	}


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
	        Iterator<Map.Entry<Integer, Zona>> it = cm.getZonas().entrySet().iterator();
	        while (it.hasNext() && !encontrado) {
	            zAux = it.next().getValue();									//Obtención del nuevo set sin el valor anterior.
	            //Si se ha encontrado se termina la búsqueda.
	            if(zAux.getPoligono() != null && zAux.getPoligono().contains(p)) {		//Evita evaluar valores nulos
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
		Mapa mapa = new Mapa(500, 500, new ControladorModulos());
		mapa.iniciarFrame(520, 520);
		mapa.verFrame(true);
	}
}