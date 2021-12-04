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
import javax.swing.border.LineBorder;

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
	private int posXL = 15;
	private int gap = 20;

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
		addLabel("Nivel de contagio:",null,posXL,9*gap,175,15);
		
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
		tf_ID = iniciarTextField(tf_ID, "ID",posX, posY, 114, alto);
		tf_NAME = iniciarTextField(tf_NAME,"Nombre",posX, posY + gap*1, ancho, alto);
		tf_PEOPLE = iniciarTextField(tf_PEOPLE,"Población",posX, posY + gap*2, ancho, alto);
		tf_AREA = iniciarTextField(tf_AREA,"Superficie",posX, posY + gap*3, ancho, alto);
		tf_S = iniciarTextField(tf_S,"Susceptibles",posX, posY + gap*4, ancho, alto);
		tf_I = iniciarTextField(tf_I,"Infectados",posX, posY + gap*5, ancho, alto);
		tf_R = iniciarTextField(tf_R,"Recuperados",posX, posY + gap*6, ancho, alto);
		tf_P = iniciarTextField(tf_P,"Prevalencia",posX, posY + gap*7, ancho, alto);
		tf_C100K = iniciarTextField(tf_C100K,"Nivel",152, posY + gap*8, 114, 19);	
		
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
	 * <p>Title: refresh</p>  
	 * <p>Description: Recarga los campos de los TextFields con los datos del modelo</p>
	 * Realiza una nueva lectura de cada campo del modelo (zona) e introduciendo
	 *  sus valores en los correspondientes JTextFields. 
	 */
	public void refresh() {
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
	 * <p>Title: abrirFrame</p>  
	 * <p>Description: Visualiza los datos del módulo dentro de su propio marco</p> 
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
	 * @param zona La zona a establecer
	 */
	public void setZona(Zona zona) {
		refresh();
	}
	
	/* Clases privadas */
	
	private class BotonL extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent evt) {
			
			//
			try{
				zona.setName(tf_NAME.getText());
				zona.setPoblacion(Integer.parseInt(tf_PEOPLE.getText()));
				zona.setSuperficie(Integer.parseInt(tf_AREA.getText()));
				zona.setS(Double.parseDouble(tf_S.getText()));
				zona.setI(Double.parseDouble(tf_I.getText()));
				zona.setR(Double.parseDouble(tf_R.getText()));
				zona.setP(Double.parseDouble(tf_P.getText()));
				zona.setNivel(Integer.parseInt(tf_C100K.getText()));
				
			}catch(Exception e) {System.out.println("Valor incorrecto");}
			cm.doActionVistaZona(zona.getID());
		}
	}
	
	private class PanelPoligono extends JPanel{
		/** serialVersionUID*/  
		private static final long serialVersionUID = 1575177244442912505L;
		private JLabel lblSinPoligono;
		private final int x = posXL;
		private final int y = 12*gap;
		private final int h = 250;
		private final int w = 225;

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
		
		this.zona = new Zona(0, "1_Test",2 , 3 ,4,5,6,7,8, p);
		System.out.println( this.zona.toString() + "\nNivel: " + zona.getNivel());
		refresh();
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
