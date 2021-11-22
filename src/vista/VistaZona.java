/**  
* <p>Title: VistaZona.java</p>  
* <p>Description: Representa una vista con los datos organizados de forma legible.</p>
* Su fuente de datos (modelo) es la clase Zona, que almacena los datos propios
* de la zona o grupo de población que representa.    
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

import modelo.IO;
import modelo.Zona;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import controlador.ControladorModulos;

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
	private JPanel panel_poligono;
	//
	private JTextField tf_Nombre;
	private JTextField tf_Poblacion;
	private JTextField tf_Superficie;
	private JTextField tf_ID;
	private JTextField tf_Nivel;
	//
	

	/**
	 * Creación del panel con la vista de una zona.
	 * @param zona Zona con los datos propios del grupo de estudio. Null en otro caso.
	 * @param cm Controlador de módulos, necesario para permitir el flujo de datos
	 * entre las vistas, el módelo y el controlador.
	 */
	public VistaZona(Zona zona, ControladorModulos cm) {
		this.cm = cm;
		if(zona == null) this.zona = new Zona(0, "No asignado", 0, 0, null);	
		else this.zona =zona;
		panelCentral = new JPanel();
		panel_poligono = new JPanel();
		configurar();
	}
	
	private void configurar() {
		setLayout(new BorderLayout(0, 0));	
//		setBackground(Color.RED);
	
		add(panelCentral);
		panelCentral.setLayout(null);
		
		int gap = 20;
		addLabel("ID:", null, 12,gap,19,15);
		addLabel("Nombre:",null,12,2*gap,172,15);
		addLabel("Población:",null,12,3*gap,81,15);
		addLabel("Superficie:",null,12,4*gap,81,15);
		addLabel("Nivel de contagio:",null,12,5*gap,175,15);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(0, 0, 139));
		separator.setBackground(new Color(0, 0, 128));
		separator.setBounds(12, 119, 426, 13);
		panelCentral.add(separator);
		
		JLabel lblRepresentacingrfica = new JLabel("Representación Gráfica");
		lblRepresentacingrfica.setHorizontalAlignment(SwingConstants.LEFT);
		lblRepresentacingrfica.setBounds(12, 136, 166, 15);
		panelCentral.add(lblRepresentacingrfica);
		
		//
		PanelPoligono pp = new PanelPoligono();
		panelCentral.add(pp);
		
		//Iniciar los JTextFields
		int posX = 100;
		int posY = 18;
		int alto = 19;
		tf_ID = iniciarTextField(tf_ID, "ID",posX, posY, 114, alto);
		tf_Nombre = iniciarTextField(tf_Nombre,"Nombre",posX, posY + gap*1, 166, alto);
		tf_Poblacion = iniciarTextField(tf_Poblacion,"Población",posX, posY + gap*2, 166, alto);
		tf_Superficie = iniciarTextField(tf_Superficie,"Superficie",posX, posY + gap*3, 166, alto);
		tf_Nivel = iniciarTextField(tf_Nivel,"Nivel",152, posY + gap*4, 114, 19);
		
		JButton btnAplicar = new JButton("Aplicar");
		btnAplicar.addMouseListener(new BotonL());
		btnAplicar.setIcon(IO.getIcon("/vista/imagenes/Iconos/ok_64px.png",64,64));
		btnAplicar.setToolTipText("Guarda los cambios efectuados.");
		btnAplicar.setBounds(288, 20, 150, 87);
		panelCentral.add(btnAplicar);
		
		recargarDatos();
	}
	
	/**
	 * <p>Title: recargarDatos</p>  
	 * <p>Description: Recarga los campos de los TextFields con los datos del modelo</p>
	 * Realiza una nueva lectura de cada campo del modelo (zona) e introduciendo
	 *  sus valores en los correspondientes JTextFields. 
	 */
	private void recargarDatos() {
		//Si no es nulo actúa.
		if(zona != null) {
			tf_ID.setText("" + zona.getID());									//ID
			tf_ID.setEditable(false);											//Desactivar edición del ID
			tf_ID.setBorder(null);												//Quitamos el border a su campo.
			tf_Nombre.setText(zona.getName());									//Establecer el campo nombre.
			tf_Poblacion.setText("" + zona.getPoblacion());
			tf_Superficie.setText("" + zona.getSuperficie());
			tf_Nivel.setText("" + zona.getNivel());								//Nivel inicial de contagio.
		}
	}
	
	
	/**
	 * <p>Title: abrirFrame</p>  
	 * <p>Description: Visualiza los datos del módulo dentro de su propio marco</p> 
	 */
	public void abrirFrame() {
	    JFrame frame = new JFrame("Módulo de Archivos");
	    Dimension m = getPreferredSize();
	    int x = (int)m.getWidth();
	    int y = (int)m.getHeight()+15;
	    frame.setPreferredSize(new Dimension(x, y));
	    frame.setMaximumSize(new Dimension(2767, 2767));
		frame.setMinimumSize(new Dimension(650, 400));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.getContentPane().add(this);
		frame.pack();
        frame.setVisible(true);
	}
	
	/**
	 * <p>Title: addIconL</p>  
	 * <p>Description: Añade un icono a una etiqueta</p>
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
	
	private JTextField iniciarTextField(JTextField jtf, String tt, int posX, int posY, int wi, int hi){
		jtf = new JTextField();
		jtf.setEnabled(true);
		jtf.setBounds(posX,posY,wi,hi);
		jtf.setColumns(10);
		if(tt != null) jtf.setToolTipText(tt);
		jtf.setHorizontalAlignment(SwingConstants.LEFT);
		panelCentral.add(jtf);
		return jtf;
	}
	
	/**
	 * <p>Title: iniciarLabel</p>  
	 * <p>Description: Establece las facetas de las etiquetas descripticas</p> 
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
	 * <p>Title: getPanel</p>  
	 * <p>Description: Devuelve el panel con su configuración actual</p> 
	 * @return JPanel con toda la información que contiene.
	 */
	public JPanel getPanel() {return this;}
	
	/**
	 * @return La zona con sus atributos.
	 */
	public Zona getZona() {	return zona;}

	/**
	 * @param zona La zona a establecer
	 */
	public void setZona(Zona zona) {
		this.zona = zona;
		recargarDatos();
	}
	
	/* Clases privadas */
	
	private class BotonL extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent evt) {
			zona.setName(tf_Nombre.getText());
			//
			try{
				zona.setPoblacion(Integer.parseInt(tf_Poblacion.getText()));
				zona.setSuperficie(Integer.parseInt(tf_Superficie.getText()));
				zona.setNivel(Integer.parseInt(tf_Nivel.getText()));}
			catch(Exception e) {System.out.println("Valor incorrecto");}
			cm.doActionVistaZona();
			
		}
	}
	
	private class PanelPoligono extends JPanel{
		/** serialVersionUID*/  
		private static final long serialVersionUID = 1575177244442912505L;
		private JLabel lblSinPoligono;
		private final int w = 290;
		private final int h = 190;
		private final int x = 60;
		private final int y = 160;

		public PanelPoligono() {
			setBounds(x, y, w, h);
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
	        
	        if(zona.getZona() == null) {
	        	lblSinPoligono.setVisible(true);
	        }else {
	        	lblSinPoligono.setVisible(false);
	        	Shape p = fitPolygon();											//Ajustamos la forma.
	  			g2.setPaint(cm.getLevelColor(zona.getNivel()));
	            g2.fill(p);														//Rellenar el poligono de verde.
	            g2.setPaint(Color.RED);
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
			Polygon pz = zona.getZona();
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
	
	/* Funciones de pruebas */
	
	/**
	 * <p>Title: generaTest</p>  
	 * <p>Description: Genera unos datos básicos de prueba</p>
	 * Función para comprobar el correcto funcionamiento. Genera unos datos de prueba
	 * que será mostrados en la vista.
	 */
	public void generaTest() {
		Polygon p = new Polygon();
		p.addPoint( 65, 45 );
		p.addPoint( 95, 150 );
		p.addPoint( 170, 100 );
		
		this.zona = new Zona(1, "Zona_Test", 10 , 400 , p);
		recargarDatos();
		panel_poligono.setVisible(true);
	}
	
	/**
	 * <p>Title: main</p>  
	 * <p>Description: Función a modo de prueba</p> 
	 * @param args ninguno.
	 */
	public static void main(String[] args) {
		VistaZona vz = new VistaZona(null,new ControladorModulos());
		vz.generaTest();
		vz.abrirFrame();
	}
}
