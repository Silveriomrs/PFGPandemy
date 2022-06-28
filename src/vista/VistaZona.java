/**  
* <p>Title: VistaZona.java</p>  
* <p>Description: Representa una vista con los datos organizados de forma legible.</p>
* Su fuente de datos (modelo) es la clase Zona, que almacena los datos propios
* de la zona o grupo de población que representa. Además de garantizar que cada campo recibe
*  el tipo de datos correcto que le corresponda.
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 2 nov. 2021  
* @version 1.2
*/  
package vista;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import modelo.Zona;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import controlador.ControladorModulos;
import controlador.IO;

import javax.swing.JTextField;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * <p>Title: VistaZona</p>  
 * <p>Description: </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 2 nov. 2021
 * @version versión 1.2
 */
public class VistaZona extends JPanel {
	private Zona zona;
	private ControladorModulos cm;
	
	/** serialVersionUID*/  
	private static final long serialVersionUID = 2843655383959458235L;
	//
	private JPanel panelCentral;
	private JPanel panelChart;
	private PanelPoligono pp;
	//
	private JTextField tf_ID;
	private JTextField tf_NAME;
	private JTextField tf_PEOPLE;
	private JTextField tf_AREA;
	private JTextField tf_S;
	private JTextField tf_R;
	private JTextField tf_I;
	private JTextField tf_P;
	private JTextField tf_C100K;
	//
	private static final int posXL = 15;
	private static final int gap = 20;

	/**
	 * Creación del panel con la vista de una zona.
	 * @param zona Zona con los datos propios del grupo de estudio. Null en otro caso.
	 * @param cm Controlador de módulos, necesario para permitir el flujo de datos
	 * entre las vistas, el módelo y el controlador.
	 */
	public VistaZona(Zona zona, ControladorModulos cm) {
		this.cm = cm;
		if(zona == null) this.zona = new Zona(0, "No asignado", 0, 0,0,0,0,0,0, null);	
		else this.zona = zona;
		panelCentral = new JPanel();
		panelChart = new JPanel();
		pp = new PanelPoligono();
		configurar();
	}
	
	/**
	 * Configura los controles, etiquetas y campos de la vista.
	 * Además también configura los atributos propios de la vista.
	 */
	private void configurar() {
		setLayout(new BorderLayout(0, 0));	
	
		add(panelCentral);
		panelCentral.setLayout(null);	
		
		addLabel("ID:", null, posXL,gap,19,15);
		addLabel("Nombre:",null,posXL,2*gap,172,15);
		addLabel("Población:",null,posXL,3*gap,81,15);
		addLabel("Superficie:",null,posXL,4*gap,81,15);
		//
		addLabel("Susceptibles:",null,posXL,5*gap,100,15);
		addLabel("Incidentes:",null,posXL,6*gap,100,15);
		addLabel("Recuperados:",null,posXL,7*gap,100,15);
		addLabel("Prevalencia:",null,posXL,8*gap,100,15);
		addLabel("Nivel de incidencia:",null,posXL,9*gap,175,15);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(0, 0, 139));
		separator.setBackground(new Color(0, 0, 128));
		separator.setBounds(posXL, 10*gap, 262, 13);
		panelCentral.add(separator);
		
		JLabel lblRepresentacingrfica = new JLabel("Representación Gráfica");
		lblRepresentacingrfica.setHorizontalAlignment(SwingConstants.LEFT);
		lblRepresentacingrfica.setBounds(posXL, 11*gap, 166, 15);
		panelCentral.add(lblRepresentacingrfica);
		
		//
		panelCentral.add(pp);
		
		//Iniciar los JTextFields
		int posX = 120;
		int posY = 18;
		int alto = 19;
		int ancho = 150;
		tf_ID = iniciarTextField("ID",posX, posY, 114, alto);
		tf_NAME = iniciarTextField("Nombre",posX, posY + gap*1, ancho, alto);
		tf_PEOPLE = iniciarTextField("Población",posX, posY + gap*2, ancho, alto);
		tf_AREA = iniciarTextField("Superficie",posX, posY + gap*3, ancho, alto);
		tf_S = iniciarTextField("Susceptibles",posX, posY + gap*4, ancho, alto);
		tf_I = iniciarTextField("Infectados",posX, posY + gap*5, ancho, alto);
		tf_R = iniciarTextField("Recuperados",posX, posY + gap*6, ancho, alto);
		tf_P = iniciarTextField("Prevalencia",posX, posY + gap*7, ancho, alto);
		tf_C100K = iniciarTextField("Nivel",152, posY + gap*8, 114, 19);
		tf_P.setEditable(false);
		tf_C100K.setEditable(false);
		
		JButton btnAplicar = new JButton("Aplicar Cambios");
		btnAplicar.addMouseListener(new BotonL());
		btnAplicar.setIcon(IO.getIcon("/vista/imagenes/Iconos/ok_64px.png",64,64));
		btnAplicar.setToolTipText("Guarda los cambios efectuados.");
		btnAplicar.setBounds(374, 393, 233, 74);
		panelCentral.add(btnAplicar);
		//
		panelChart = zona.getGrafica().getJPanel();
		panelChart.setBounds(296, 20, 413, 340);
		panelChart.setBorder(new  LineBorder(Color.BLACK)  );
		panelCentral.add(panelChart,BorderLayout.CENTER);

		refresh();
	}
	
	/**
	 * <p>Recarga los campos de los TextFields con los datos del modelo</p>
	 * Realiza una nueva lectura de cada campo del modelo (zona) e introduciendo
	 *  sus valores en los correspondientes JTextFields. 
	 */
	public void refresh() {
		if(cm.getNumberZonas() > 0) zona = cm.getZonas().get(zona.getID());
		
		//Si no es nulo actúa.
		if(zona != null) {
			tf_ID.setText("" + zona.getID());									//ID
			tf_ID.setEditable(false);											//Desactivar edición del ID
			tf_ID.setBorder(null);												//Quitamos el border a su campo.
			tf_NAME.setText(zona.getName());									//Establecer el campo nombre.
			tf_PEOPLE.setText("" + zona.getPoblacion());
			tf_AREA.setText("" + zona.getSuperficie());
			tf_S.setText("" + zona.getS());
			tf_R.setText("" + zona.getR());
			tf_I.setText("" + zona.getI());
			tf_P.setText("" + zona.getP());
			tf_C100K.setText("" + zona.getNivel());								//Nivel inicial de contagio.
		}
	}	
	
	/**
	 * <p>Visualiza los datos del módulo dentro de su propio marco.</p>
	 * La ventaja de esta función es que abre la vista en modo de ficha flotante,
	 * mejorando mucho la personalización de la aplicación.
	 */
	public void abrirFrame() {
	    JFrame frame = new JFrame("Módulo de Archivos");
	    Dimension m = getPreferredSize();
	    int x = (int)m.getWidth();
	    int y = (int)m.getHeight()+15;
	    frame.setPreferredSize(new Dimension(x, y));
	    frame.setMaximumSize(new Dimension(1024, 768));
		frame.setMinimumSize(new Dimension(720, 550));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.getContentPane().add(this);
		frame.pack();
        frame.setVisible(true);
	}
	
	/**
	 * <p>Añade un icono a una etiqueta</p>
	 * Los valores de dimensión de ancho y largo se establecen en función de los 
	 * datos pasados por parámetro. 
	 * @param componente Etiqueta a la que adjuntar el icono
	 * @param ruta Nombre del archivo y su ruta.
	 * @param w Ancho a escalar de la imagen original.
	 * @param h Alto a escalar de la imagen original.
	 */
	private void addIconL(JLabel componente, String ruta, int w, int h) {
		componente.setIconTextGap(5);
		componente.setIcon(IO.getIcon(ruta,w,h));	
	}
	
	/**
	 * Inicializa un campo de texto con los atributos necesarios para su correcta
	 *  visualización y funcionamiento.
	 * @param tt Texto emergente a establecer.
	 * @param posX Posición en el eje horizontal donde colocar la etiqueta.
	 * @param posY Posición en el eje vertical donde colocar la etiqueta.
	 * @param wi Ancho de la etiqueta.
	 * @param hi Alto de la etiqueta,
	 * @return Devuelve el campo de texto configurado con los atributos comunes.
	 */
	private JTextField iniciarTextField(String tt, int posX, int posY, int wi, int hi){
		JTextField jtf = new JTextField();
		jtf.setEnabled(true);													//Inicialmente el campo está habilitado.
		jtf.setBounds(posX,posY,wi,hi);											//Establecer posición.
		jtf.setColumns(10);														//Número de columnas.
		if(tt != null) jtf.setToolTipText(tt);									//Establecer mensaje emergente.
		jtf.setHorizontalAlignment(SwingConstants.LEFT);						//Alineación a la izquierda.
		panelCentral.add(jtf);													//Añadir al panel central
		return jtf;
	}
	
	/**
	 * Establece las facetas de las etiquetas descripticas
	 * @param nombre Nombre en la etiqueta.
	 * @param ruta Ruta al icono de la etiqueta. Null en otro caso.
	 * @param posX Posición donde colocarlo en el eje X.
	 * @param posY Posición donde colocarlo en el eje Y.
	 * @param wi Ancho de la etiqueta.
	 * @param hi Alto de la etiqueta.
	 */
	private void addLabel(String nombre, String ruta, int posX, int posY, int wi, int hi) {
		JLabel jl = new JLabel(nombre);
		if(ruta != null) addIconL(jl,ruta,wi,hi);
		jl.setBounds(posX, posY, wi, hi);
		panelCentral.add(jl);
	}

	/**
	 * @param zona La zona a establecer
	 */
	public void setZona(Zona zona) {
		this.zona = zona;
		refresh();
	}
	
	/* Clases privadas */
	
	/**
	 * <p>Procesa el formulario asignando a cada uno de los atributos del grupo de población
	 *  los datos introducidos en los campos del formulario.</p>
	 *  Para garantizar la validez de dichos datos, se realiza una conversión de formato, en caso de existir
	 *   un valor incorrecto, saltará una expceción que es capturada mostrando el mensaje de error correspondiente.  
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BotonL extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent evt) {
			//
			try{
				Double s,i,r;
				zona.setName(tf_NAME.getText());
				zona.setPoblacion(Integer.parseInt(tf_PEOPLE.getText()));
				zona.setSuperficie(Integer.parseInt(tf_AREA.getText()));
				s = Double.parseDouble(tf_S.getText());
				i = Double.parseDouble(tf_I.getText());
				r = Double.parseDouble(tf_R.getText());
				zona.setS(s);
				zona.setI(i);
				zona.setR(r);
//				zona.setP(Double.parseDouble(tf_P.getText()));
				zona.setP(i/(s+i+r)); /* Modificación para calcular la prevalencia inicial en vez de tomarla por constructor */
				zona.setNivel(Integer.parseInt(tf_C100K.getText()));
				
			}catch(Exception e) {System.out.println("Valor incorrecto");}
			cm.doActionVistaZona(zona.getID());
		}
	}
	
	/**
	 * <p>Clase particular encargada de redimensionar un polígono representación
	 *  de un grupo de población, de manera que quede escalado dentro del marco contenedor, respetando
	 *   el factor de forma y demás atributos propios. No modifica el original.</p> 
	 *   Por último desplaza dicho polígono de forma que que quede centrado en su marco contenedor.
	 *    Esta clase esta vinculada al nivel de contagio del grupo de población con el fin de adquirir
	 *     el color que le corresponda.
	 * @author Silverio Manuel Rosales Santana
	 * @date 26 mar. 2022
	 * @version versión 1.2
	 */
	private class PanelPoligono extends JPanel{
		/** serialVersionUID*/  
		private static final long serialVersionUID = 1575177244442912505L;
		private JLabel lblSinPoligono;
		private final int h = 250;
		private final int w = 225;

		public PanelPoligono() {
			setBounds(posXL, 12*gap, w, h);
			setLayout(null);	
			//Etiqueta para mostrar en caso de falta de poligono.
			lblSinPoligono = new JLabel();
			lblSinPoligono.setBounds(new Rectangle(0, 0, w, h));
			lblSinPoligono.setIcon(IO.getIcon("/vista/imagenes/Iconos/sinImg_256px.png",getWidth(),getHeight()));
			add(lblSinPoligono);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
	        Graphics2D g2 = (Graphics2D)g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                            RenderingHints.VALUE_ANTIALIAS_ON);
	        
	        if(zona.getPoligono() == null) {
	        	lblSinPoligono.setVisible(true);
	        }else {
	        	lblSinPoligono.setVisible(false);
	        	Shape p = fitPolygon();											//Ajustamos la forma.
	  			g2.setPaint(cm.getLevelColor(zona.getNivel()));
	            g2.fill(p);														//Rellenar el poligono de verde.
	            g2.setPaint(new Color(0, 0, 153));
	  			g2.draw(p);														//Dibuja sus bordes.
	            g2.setPaint(Color.BLACK);
	            g2.draw(p.getBounds());											//Dibuja un marco encima del poligono.
	            updateUI();
	        }
		}
		
		/**
		 * <p>Title: fitPolygon</p>  
		 * <p>Description: Función de escalado y posicionamiento</p>
		 * Permite situar la figura en el origen de coordenadas y posteriormente
		 * la escala ajustandola el lado mayor (y manteniendo la proporción del 
		 * lado menor) a la caja que lo va a contener. 
		 * @return Figura transformada para encanjar en el panel.
		 */
		private Shape fitPolygon() {
			Polygon pz = zona.getPoligono();
			Polygon p2 = new Polygon(pz.xpoints, pz.ypoints, pz.npoints);
			Rectangle rp = p2.getBounds();
			int xp = rp.x;
			int yp = rp.y;
			//Primeramente transformamos para llevar al punto de origen de coordenadas.
			p2.translate(-xp, -yp);
			//Ahora escalar usando el factor menor (es decir el lado más corto)
			AffineTransform tx = new AffineTransform();
            Rectangle2D r2d = pz.getBounds2D();
            /*Cálculo del mejor factor de escalada. 
             * El menor => evitar desbordamiento, por tanto ese será el mejor.*/
            double factorX = w/(r2d.getWidth() + 3);							// Suma de pixels extra para evitar solapamiento de líneas.
            double factorY = h/(r2d.getHeight() + 3);
            double factor = (factorX >= factorY)? factorY:factorX;
            tx.scale(factor ,factor);											// Establece la misma escala para ambas dimensiones.
            return tx.createTransformedShape(p2);
		}
	}

}
