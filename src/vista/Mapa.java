/**
 * 	Módulo independiente cuya función es la representación gráfica de los polígonos
 *  que representan los grupos de población o zonas geográficas. 
 *  <p>Este módulo permite además seleccionar una zona representada y mostrar información adicional
 *   sobre la misma. Puede ser utilizado de forma integrada en la aplicación principal
 *    o en una ventana independiente.</p>
 *  El mapa es capaz de realizar representaciones dinámicas de las zonas, es decir,
 *   debe permitir la modificación en tiempo de ejecución de los colores que
 *    representan los niveles de contagio con respecto al tiempo.
 *  @author Silverio Manuel Rosales Santana
 *  @version 1.6
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
 * poblacional (provincia o comunidad autónoma), además permite su coloreado.
 * @author Silverio Manuel Rosales Santana
 * @date 10/07/2021
 * @version 1.6
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
	 * <p>Reinicia la vista de este módulo.</p> 
	 *  Elimina los datos almacenados en el mismo.
	 */
	public void reset() {updateUI();}
	
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
	 * Activa o desactiva la visibilidad del frame.
	 * @param activado True para hacerlo visible, False en otro caso.
	 */
	public void verFrame(boolean activado) {frame.setVisible(activado);}

	/**
	 * <p>Permite cambiar el modo de visibilidad del panel entre oculto y visible</p>
	 * Para tal efecto consulta el estado previo y configura el estado actual en
	 * función del anterior.
	 */
	public void toogleVisible() {this.setVisible(!this.isVisible());}

	 /**
	 * Establece la posición para el frame.
	 * @param xPos Posición X relativa a la pantalla.
	 * @param yPos Posición Y relativa a la pantalla.
	 */
	public void setPosicion(int xPos, int yPos) {frame.setLocation(xPos,yPos);}

	/**
	 * Sobrescibe la función para lograr el dibujado de los poligonos en tiempo real.
	 * Obtiene tanto el poligono como el color mediante solicitudes al controlador.
	 */
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
	 * Establece el grado de contagio de una zona.
	 * @param id ID de la zona.
	 * @param serie Nombre de la serie asociada al nivel almacenado.
	 * @param time Instante de tiempo en el que encuadrar dicho valor. Tiene equivalencia
	 *  con la posición en el eje de coordenadas X de la gráfica.
	 * @param n Nivel de asignación.
	 */
	public void addZonaNivel(int id, String serie,int time, double n) {
		if(cm.getZonas().containsKey(id)) {										//Comprobación de que existe.
			cm.getZonas().get(id).addNivel(serie,time, n);						//Añadir valor a la serie que corresponda.
		}
	}

	/**
	 * <p>Clase usada para la detección de la selección de un grupo de
	 *  población.</p>
	 *  Basa su funcionamiento en las coordenadas de la pulsación en pantalla, recorriendo
	 *   cada uno de los polígonos de las diferentes zonas y comprobando cual contiene dicho
	 *    punto dentro de sus límintes.
	 * <p>Una vez encontrada muestra la información deseada, en este caso el gráfico de línea
	 *  asociado a dicha zona.</p>  
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.2
	 */
	private class SelectorPoligono extends MouseAdapter	{
	    @Override
		public void mousePressed(MouseEvent e)  {
	    	//Obtención del punto del mapa que ha recibido la pulsación.
	        Point p = e.getPoint();
	        Zona z = getZona(p);
	        if(z != null) z.getGrafica().setVisible(true);
	    }

	    /**
	     * Devuelve la zona cuyo polígono de representación gráfica
	     *  contenga el punto pásado por parámetro.
	     * @param p Punto de coordenadas.
	     * @return Zona que contiene dicho punto de coordenadas dentro de su representación.
	     */
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

}