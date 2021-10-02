/**
* <p>Title: Pizarra.java</p>
* <p>Description: </p>
* <p>Aplication: UNED</p>
* @author Silverio Manuel Rosales Santana
* @date 22 sept. 2021
* @version 1.0
*/
package vista;

/**
 * <p>Title: Pizarra</p>
 * <p>Description: </p>
 * @author Silverio Manuel Rosales Santana
 * @date 22 sept. 2021
 * @version versión 2.0
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import controlador.ControladorMapa;

import java.awt.event.MouseAdapter;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import modelo.IO;
import modelo.Zona;

/**
 * <p>Title: Pizarra</p>
 * <p>Description: </p>
 * @author Silverio Manuel Rosales Santana
 * @date 23 sept. 2021
 * @version versión 1.0
 */
public class Pizarra extends JPanel {
	/** serialVersionUID*/
	private static final long serialVersionUID = 6537461228067301333L;
	private JButton bLimpiar,bGuardar,bAImagen,bCerrarPoligono,bBoxAplicar;
	private Canvas c;
	private JToolBar toolBar;
	private int dimX, dimY;
	private Polygon poligono;
	private ArrayList<Point> listaPuntos;
	private JPanel panelCanvas;
	private JComboBox<String> comboBoxAsignar;
	private JComboBox<String> comboBoxAsignados;
	private HashMap<Integer,Zona> zonas;
	private ControladorMapa cMap;
	
	/**
	 * <p>Title: Pizarra de dibujo</p>
	 * <p>Description: Pizarra donde poder crear los poligonos que representarán
	 * a cada zona</p>
	 * @param cMap Módulo controlador del mapa.
	 */
	public Pizarra(ControladorMapa cMap) {
		setOpaque(false);
		this.dimX = 700;
		this.dimY = 600;
		this.cMap = cMap;
		if(cMap.getZonas() != null) {this.zonas = cMap.getZonas();}
		else this.zonas = new HashMap<Integer,Zona>();
		this.listaPuntos = new ArrayList<Point>();
		setPreferredSize(new Dimension(700, 600));
	    setBackground(Color.white);
	    setLayout(new BorderLayout());
	    configura();
	}
	
	public void setZonas(HashMap<Integer,Zona> zonas) {
		this.zonas = zonas;
    	zonasToCombo();
        reinicioBotones();
	}
	
	public HashMap<Integer,Zona> getZonas() {return this.zonas;}
	
	public void abrirFrame() {
	    JFrame frame = new JFrame("Diseño de figuras");
	    Dimension m = getPreferredSize();
	    int x = (int)m.getWidth();
	    int y = (int)m.getHeight()+15;
	    frame.setPreferredSize(new Dimension(x, y));
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.getContentPane().add(this);
		frame.pack();
        frame.setVisible(true);
	}
	
	private void dibujaPoligono(Polygon poligono) {
		if(poligono != null) {
			Graphics g = c.getGraphics();
			if(g == null) {
				System.out.println("G es nulo");
				iniciarPizarra();
				g = c.getGraphics();
			}
			g.setColor(Color.BLUE);
			g.drawPolygon(poligono);
		}else {System.out.println("Poligono nulo");}
	}
	
	private void dibujarZonas() {
		//Lectura de las zonas y dibujado si procede.
		zonas.forEach((k,z)->{
			if(z.getZona() != null) {
				System.out.println("Zona ID: " + z.getID());
				dibujaPoligono(z.getZona());		
			}
		});
		//this.updateUI();
	}	

	private void configura() {      
        iniciarBotones();
        iniciarPizarra();
		iniciarCombos();
        iniciarToolBar();
        reinicioBotones();

	    panelCanvas = new JPanel();
	    panelCanvas.setToolTipText("");
	    panelCanvas.setBackground(Color.PINK);
	    panelCanvas.setLayout(null);
	    panelCanvas.add(c);
	    add(panelCanvas, BorderLayout.CENTER);
	    
	    //Añadir Observadores.
	    bLimpiar.addActionListener(new LimpiarListener());
	    bCerrarPoligono.addActionListener(new CierreListener());
	    bBoxAplicar.addActionListener(new AsignarBoxListener() );
	}

    @Override
	public Dimension getPreferredSize() {
    	//return new Dimension(this.getWidth(), this.getHeight() - 40);
    	return new Dimension(dimX, dimY);
    }

    /**
     * <p>Title: getPanel</p>
     * <p>Description: Devuelve el JPanel que contiene los botones y la
     * pizarra</p>
     * @return El JPanel configurado.
     */
    public JPanel getPanel() {return this;}

    /* Métodos privados */
    
    private Polygon generaPoligono() {
    	int contador = listaPuntos.size();
    	//Creación de los arreglos de coordenadas.
		int[] polX = new int[contador];
		int[] polY = new int[contador];
		//Volcado de coordenadas desde la lista de puntos.
		for(int i = 0; i<contador; i++) {
			Point p = listaPuntos.get(i);
			polX[i] = (int)p.getX();
			polY[i] = (int)p.getY();
		}
		poligono = new Polygon(polX, polY, contador);							// Guardado en el campo para posterior asignación.
		return poligono;
    }

    private boolean isPrimero() {return 0 == listaPuntos.size();}
    
	/**
	 * <p>Title: getIcon</p>  
	 * <p>Description: Devuelve un icono escalado a las medidas obtenidas
	 * por parámetros, de la imagen fuente..</p> 
	 * @param ruta Ruta del archivo de imagen.
	 * @param w Ancho del escalado de la imagen.
	 * @param h Alto del escalado de la imagen.
	 * @return Icono escalado de la imagen.
	 */
	private ImageIcon getIcon(String ruta, int w, int h) {
		ImageIcon icon = null;
		IO io = new IO();
		Image img = io.abrirImagen(ruta);
		icon = new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
		return icon;
	}

	private void iniciarToolBar() {
	    toolBar = new JToolBar();
	    toolBar.add(bLimpiar);
	    toolBar.add(bAImagen);
	    toolBar.add(bGuardar);
	    toolBar.add(bCerrarPoligono);
	    toolBar.add(comboBoxAsignar);
	    toolBar.add(bBoxAplicar);
	    toolBar.add(comboBoxAsignados);
	    add(toolBar, BorderLayout.NORTH);
	}
	
	private void iniciarPizarra() {
	    //Configuración de la pizarra
	    c = new Canvas() {
            /** serialVersionUID*/
	    	private static final long serialVersionUID = 5398261596293519343L;
	    	@Override
	    	public Dimension getPreferredSize() { return new Dimension(320,240); }
            @Override
	    	public void paint(Graphics g) {
                g.setColor(Color.black);
                g.drawRect(5,5,getWidth()-10,getHeight()-10);
            }
        };
	    c.setBounds(0, 0, 665, 542);
	    c.setName("pizarra");
	    c.setBackground(Color.yellow);
	    c.addMouseListener(new SelectPointListener());
	}
	
	private void iniciarBotones() {
		//Configuración de los botones.
	    bLimpiar = new JButton("Limpiar");
	    bLimpiar.setIcon(getIcon("/vista/imagenes/Iconos/limpiar_64px.png",25,25));
	    bLimpiar.setToolTipText("Limpiar la pizarra y redibujar las zonas.");
	    bAImagen = new JButton("Abrir");
	    bAImagen.addMouseListener(new AbrirListener());
	    bAImagen.setIcon(getIcon("/vista/imagenes/Iconos/carpeta_64px.png",25,25));
	    bAImagen.setToolTipText("Abre una imagen para establecer de papel de fondo.");
	    bGuardar = new JButton("Guardar");
	    bGuardar.setIcon(getIcon("/vista/imagenes/Iconos/disquete_64px.png",25,25));
	    bGuardar.setToolTipText("Guardar los cambios realizados.");
        bCerrarPoligono = new JButton("Componer");
        bCerrarPoligono.setIcon(getIcon("/vista/imagenes/Iconos/nodos_64px.png",25,25));
        bCerrarPoligono.setToolTipText("Cierra la figura y crea el poligono.\nUna vez creado debe asignarse a una zona.");   
        bBoxAplicar = new JButton("");
	    bBoxAplicar.setIcon(getIcon("/vista/imagenes/Iconos/aplicando_64px.png",25,25));    
	    bBoxAplicar.setToolTipText("Aplica las asignaciones seleccionadas.");
	}
	
	private String getMaxItem() {
		int maxLargo = 0;
		String item = "Zona: XX";												//Longitud mínima a comparar
		//Selección de la clave más larga.
		for (Zona zona:zonas.values()) {
			int l = zona.getName().length();
			if(maxLargo < l) {
				maxLargo = l;
				item = zona.getName();
			}
		}
		return item;
	}
	
	private void zonasToCombo() {
		zonas.forEach((k,v)->{
			if(v.getZona() == null) {comboBoxAsignar.addItem(v.getName());}
			else { comboBoxAsignados.addItem(v.getName());}
		});
	}
	
	private void iniciarCombos() {		
		//ComboBox zonas sin asignación.
		comboBoxAsignar = new JComboBox<String>();
		comboBoxAsignar.setModel(new DefaultComboBoxModel<String>());	
		comboBoxAsignar.setToolTipText("Seleccionar una zona para asignar la figura.");		
		//ComboBox de asignaciones dadas.
		comboBoxAsignados = new JComboBox<String>();
		comboBoxAsignados.setModel(new DefaultComboBoxModel<String>());
		comboBoxAsignados.setToolTipText("Seleccionar una zona a reasignar.");
		//Puesta de Item 0, seleccionar.
		comboBoxAsignados.addItem("Eliminar");
		comboBoxAsignar.addItem("Asignar a");
		// Ajustar largo máximo
		String itemMax = getMaxItem();
		comboBoxAsignar.setPrototypeDisplayValue(itemMax);
		comboBoxAsignar.setPrototypeDisplayValue(itemMax);
		//Lectura de las zonas y asignación a cada comboBox.
		zonasToCombo();
	}
	
	private void reinicioBotones() {  
        boolean hayPoligono = poligono != null;
        boolean hasItemC1 = comboBoxAsignar.getItemCount() > 1;
        boolean hasItemC2 = comboBoxAsignados.getItemCount() > 1;
        listaPuntos.clear();
        
        //Botones, configuración de activación según contexto.
        comboBoxAsignar.setEnabled(hasItemC1 && hayPoligono);					       
       	comboBoxAsignados.setEnabled(hasItemC2);        
       	bCerrarPoligono.setEnabled(listaPuntos.size() > 0);
        bBoxAplicar.setEnabled(hayPoligono || hasItemC2);
        bGuardar.setEnabled(false);
        bCerrarPoligono.setEnabled(listaPuntos.size() > 2);
        
        //Establecer valor por defecto de los comboBox
  		comboBoxAsignados.setSelectedIndex(0);
  		comboBoxAsignar.setSelectedIndex(0); 
        
        // Desactivar cuando no hay poligonos o ya no hay más zonas sin asignar.
        c.setEnabled(!hayPoligono && hasItemC1);
        //Dibujar zonas con representación gráfica.
        dibujarZonas();
	}
	
	/* Zona clases internas */
	
    private class CierreListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
            poligono = generaPoligono();
            Graphics g = c.getGraphics();
            //Dibujar última línea de cierre.
            g.drawLine(														// Dibujado de la línea entre punto anterior y actual.
        			(int)listaPuntos.get(0).getX(),
        			(int)listaPuntos.get(0).getY(),
        			(int)listaPuntos.get(listaPuntos.size()-1).getX(),
        			(int)listaPuntos.get(listaPuntos.size()-1).getY()
        	);

            g.setColor(Color.GREEN);
            g.fillPolygon(poligono);
            listaPuntos.clear();
            reinicioBotones();
        }
    }

    private class AsignarBoxListener implements ActionListener{
	
		@Override
		public void actionPerformed(ActionEvent e) {
			String item1 = comboBoxAsignar.getSelectedItem().toString();
			String item2 = comboBoxAsignados.getSelectedItem().toString();	
			int index1 =  comboBoxAsignar.getSelectedIndex();
			int index2 =  comboBoxAsignados.getSelectedIndex();
			cambiarBox(item1,index1,comboBoxAsignar,comboBoxAsignados,poligono);
			cambiarBox(item2,index2,comboBoxAsignados,comboBoxAsignar,null);
			reinicioBotones();
		}
		
		private void cambiarBox(String item, int index, JComboBox<String> combo1, JComboBox<String> combo2, Polygon p) {
			boolean encontrado = false;											//Bandera para salir del bucle al encontrar lo buscado.
			combo1.setSelectedIndex(0);
			//Búsqueda de la zona con el nombre igual al item del checkBox.
			Iterator<Entry<Integer, Zona>> iterator = zonas.entrySet().iterator();
			while (!encontrado && iterator.hasNext()) {
			    Entry<Integer, Zona> entry = iterator.next();
			    String nombre = entry.getValue().getName();						// Obtención del nombre de la zona.
			    Integer ID = entry.getKey();
			    if(item.equals(nombre)) {
					combo1.removeItemAt(index);									// Borrado del item excepto si es el primero (0)			
					combo2.addItem(item);										// Añadir Item al otro comboBox
					zonas.get(ID).setPoligono(p);			  					// Asignar Poligono a la zona correspondiente al item.
					cMap.setZonas(zonas);
					poligono = null;											// Iniciado del poligono a null.
					encontrado = true;											
			    }
			}
		}
    }

    private class LimpiarListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
            Graphics g = c.getGraphics();
            g.clearRect(0,0,c.getWidth(),c.getHeight());						// Repintamos la pizarra con recuadro limpio.
            c.repaint();														// Redibujar Canvas.
            poligono = null;
            reinicioBotones();
		}
    }
   
    /**
     * <p>Title: SelectPointListener</p>
     * <p>Description: Realiza un seguimiento de los puntos que son seleccionados para
     * conformar el Poligono posteriormente</p>
     * Cada punto seleccionado se marca con una cruz y un punto en su centro, a
     * medida que se van añadiendo más puntos se van creando más líneas hasta
     * finalizar la operación.
     * @author Silverio Manuel Rosales Santana
     * @date 23 sept. 2021
     * @version versión
     */
    private class SelectPointListener extends MouseAdapter{
    	private Point a;														// Punto inicial y final de una recta a dibujar.
    	private int contador = 0;
    	private Graphics g;
    	/**
    	 * Dibuja las marcas de referencia de cada punto, las líneas de unión
    	 * cuando procede y añade cada punto nuevo a la lista correspondiente de
    	 * la clase padre.
    	 * @param e Evento del ratón.
    	 */
    	@Override
		public void mousePressed(MouseEvent e) {
    		//Comprobación de pulsación de borrado de pizarra.
            Point p =  e.getPoint();
            //Dibuja una cruz en las coordenadas (X,Y)
    		int posX = (int)p.getX();
            int posY = (int)p.getY();
            g = c.getGraphics();
            marcaPunto(posX,posY);
            //Si no es el primer punto dibujado, unir con una línea al anterior.
            if(!isPrimero()) { dibujaLinea(posX, posY); }
            else {contador = 0;}
            a = p;																// Establece el punto actual como el anterior para la siguiente línea.
            listaPuntos.add(p);
            contador++;
            if(contador > 2) bCerrarPoligono.setEnabled(true);
        }
    	
    	/**
    	 * <p>Title: dibujaLinea</p>  
    	 * <p>Description: Dibuja una línea entre el punto anterior y la
    	 * nueva posición.</p>
    	 * Considerar el origen de coordenadas (0.0) está en la 
    	 * esquina superior izquierda del objeto sobre el que se dibuja.
    	 * @param posX Coordenada X del punto final de la recta a dibujar.
    	 * @param posY Coordenada Y del punto final de la recta a dibujar.
    	 */
    	private void dibujaLinea(int posX, int posY) {   
            //Unión gráfica de dos puntos consecutivos.
        	g.drawLine(															// Dibujado de la línea entre punto anterior y actual.
        			(int)a.getX(),
        			(int)a.getY(), posX,posY);
    	}
    	
    	/**
    	 * <p>Title: marcaPunto</p>  
    	 * <p>Description: Dibuja una marca de cruz en las coordenadas indicadas</p>
    	 * Considerar el origen de coordenadas (0.0) está en la 
    	 * esquina superior izquierda del objeto sobre el que se dibuja.
    	 * @param posX Posición del punto en el eje de coordendas X.
    	 * @param posY POsición del punto en el eje de coordendas Y.
    	 */
    	private void marcaPunto(int posX, int posY) {
            g.fillOval(posX -2,posY -2,4,4);									// Punto central
            g.drawLine(posX -5,posY, posX +5, posY);							// Línea horizontal
            g.drawLine(posX, posY -5, posX, posY +5);							// Línea vertical
    	}
    }

    private class AbrirListener extends MouseAdapter {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		IO io = new IO();
    		String ruta = io.selFile(1, "png");
    		if(ruta != null && ruta != "") {
    		}    
    	}
    }

    
    /* Función única para pruebas */
    
	/**
	 * <p>Title: testModulo</p>  
	 * <p>Description: Funcion cuyo proposito es realizar pruebas de funcionamiento
	 * propio del módulo.</p> 
	 * @param zonas HashMap con las zonas como posible parámetro, null para 
	 * zonas creadas internas por esta propia función. 
	 */
	public void testModulo(HashMap<Integer, Zona> zonas) {
    	//Creación de mapa y zona de prueba.
		HashMap<Integer, Zona> zonas2;
		zonas2 = (zonas == null)? new HashMap<Integer, Zona>() : zonas;
    	Zona z1,z2,z3;
    	z1 = new Zona(1, "Zona 1", null);
    	z2 = new Zona(2, "Zona 2", null);
    	z3 = new Zona(3, "Zona 3", null);
    	zonas2.put(z1.getID(), z1);
    	zonas2.put(z2.getID(), z2);
    	zonas2.put(z3.getID(), z3);
    	setZonas(zonas2);
    	abrirFrame();
    }
}