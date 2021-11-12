/**
* <p>Title: Pizarra.java</p>
* <p>Description: Módulo para el dibujado de los poligonos que represetan
* las zonas dentro de un mapa de la simulación.</p>
* <p>Aplication: UNED</p>
* @author Silverio Manuel Rosales Santana
* @date 22 sept. 2021
* @version 3.0
*/
package vista;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import java.awt.event.MouseAdapter;

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
	private int dimX, dimY;														//Dimensiones para la zona de dibujado.
	private Polygon poligono;
	private Polygon marco;
	// listaPuntos almacena los puntos, esta manera permite implementar a posteriori funciones tipo "undo". "restore",etc.
	private ArrayList<Point> listaPuntos;										
	private JPanel panelCanvas;
	private JComboBox<String> comboBoxAsignar;
	private JComboBox<String> comboBoxAsignados;
	private HashMap<Integer,Zona> zonas;
	private java.awt.Image fondo;

	/**
	 * <p>Title: Pizarra de dibujo</p>
	 * <p>Description: Pizarra donde poder crear los poligonos que representarán
	 * a cada zona</p>
	 * En caso de obtener un valor núlo o cuyo número de zonas no sea mayor a
	 * cero, no se iniciará el módulo.
	 * @param zonas Conjunto de zonas.
	 */
	public Pizarra(HashMap<Integer,Zona> zonas) {
		this.dimX = 1024;
		this.dimY = 768;
		this.listaPuntos = new ArrayList<Point>();
		//Si el conjunto de zonas es correcto y mayor a 0 -> iniciar módulo.
		if(zonas != null && zonas.size() > 0) {
			this.zonas = zonas;
			this.setPreferredSize(new Dimension(dimX, dimY));
			this.setSize(new Dimension(dimX, dimY));
		    this.setBackground(Color.white);
		    this.setLayout(new BorderLayout());
		    this.setOpaque(false);
		    configura();
		}
	}

	/**
	 * <p>Title: setZonas</p>
	 * <p>Description: Establece el conjunto de zonas contenidas dentro de un HashMAp.</p>
	 * En caso de que no contenga elementos, no actuará.
	 * @param zonas HashMap de zonas a establecer.
	 */
	public void setZonas(HashMap<Integer,Zona> zonas) {
	   this.zonas = zonas;
       zonasToCombo();
	   reinicioBotones();
	}

	/**
	 * <p>Title: getZonas</p>
	 * <p>Description:Devuelve el HashMap de las zonas </p>
	 * @return Conjunto de las zonas.
	 */
	public HashMap<Integer,Zona> getZonas() {return this.zonas;}

	/**
	 * <p>Title: abrirFrame</p>
	 * <p>Description: Visualiza los datos del módulo dentro de su propio marco</p>
	 */
	public void abrirFrame() {
	    JFrame frame = new JFrame("Diseño de figuras");
	    Dimension m = getPreferredSize();
	    int y = (int)m.getHeight() + 15;										//Una altura extra para no ocultar el canvas.
	    frame.setPreferredSize(new Dimension(dimX, y));
	    frame.setResizable(false);
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.getContentPane().add(this);
		frame.pack();
        frame.setVisible(true);
	}

	private void creaMarco() {
		listaPuntos.add(new Point(5,5));
		listaPuntos.add(new Point(c.getWidth() - 5,5));
		listaPuntos.add(new Point(c.getWidth() - 5,c.getHeight() - 5));
		listaPuntos.add(new Point(5,c.getHeight() - 5));	
		this.marco = generaPoligono();
		listaPuntos.clear();
	}
	
	private void configura() {
		//Inicialización de los componentes en estricto orden de dependencia.
		iniciarPizarra();
		creaMarco();
		iniciarBotones();
		iniciarCombos();
        iniciarToolBar();
        reinicioBotones();

	    panelCanvas = new JPanel();
	    panelCanvas.setOpaque(false);
	    panelCanvas.setLayout(null);
	    panelCanvas.add(c);
	    add(panelCanvas, BorderLayout.CENTER);

	    //Añadir Observadores.
	    bLimpiar.addActionListener(new LimpiarListener());
	    bCerrarPoligono.addActionListener(new ComponerListener());
	    bBoxAplicar.addActionListener(new AsignarBoxListener() );
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
    	Polygon pol = new Polygon();
		//Volcado de coordenadas desde la lista de puntos.
		for(int i = 0; i<contador; i++) {
			Point p = listaPuntos.get(i);
			pol.addPoint((int)p.getX(),(int)p.getY());
		}
		return pol;
    }

    private boolean isPrimero() {return 0 == listaPuntos.size();}

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

	private void iniciarBotones() {
		//Configuración de los botones.
	    bLimpiar = new JButton("Limpiar");
	    bLimpiar.setIcon(IO.getIcon("/vista/imagenes/Iconos/limpiar_64px.png",25,25));
	    bLimpiar.setToolTipText("Limpiar la pizarra y redibujar las zonas.");
	    bAImagen = new JButton("Abrir");
	    bAImagen.addMouseListener(new AbrirListener());
	    bAImagen.setIcon(IO.getIcon("/vista/imagenes/Iconos/carpeta_64px.png",25,25));
	    bAImagen.setToolTipText("Abre una imagen para establecer de papel de fondo.");
	    bGuardar = new JButton("Guardar");
	    bGuardar.setIcon(IO.getIcon("/vista/imagenes/Iconos/disquete_64px.png",25,25));
	    bGuardar.setToolTipText("Guardar los cambios realizados.");
        bCerrarPoligono = new JButton("Componer");
        bCerrarPoligono.setIcon(IO.getIcon("/vista/imagenes/Iconos/nodos_64px.png",25,25));
        bCerrarPoligono.setToolTipText("Cierra la figura y crea el poligono.\nUna vez creado debe asignarse a una zona.");
        bBoxAplicar = new JButton("");
	    bBoxAplicar.setIcon(IO.getIcon("/vista/imagenes/Iconos/aplicando_64px.png",25,25));
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
        listaPuntos.clear();													//Borrado de la lista de puntos almacenada.

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
	}
	
	private void iniciarPizarra() {
		//Configuración de la pizarra
		c = new MiCanvas();
		c.setBounds(0, 0, dimX, dimY -25);
		c.setName("pizarra");
		c.setBackground(Color.LIGHT_GRAY);
		c.addMouseListener(new SelectPointListener());
	}

	private void dibujaPoligono(Polygon poligono) {
		if (poligono != null) {
			Graphics g = c.getGraphics();
			if (g == null) {
				System.out.println("G es nulo");
				iniciarPizarra();
				g = c.getGraphics();
			} else {
				/* El que queda dibujado es el último impreso */
				g.setColor(Color.BLUE);
				g.drawPolygon(poligono);
			}
		} else {System.out.println("Poligono nulo");}
	}
	
	/**
	 * <p>Title: dibujaLinea</p>
	 * <p>Description: Dibuja una línea entre dos puntos.</p>
	 * Considerar el origen de coordenadas (0.0) está en la
	 * esquina superior izquierda del objeto sobre el que se dibuja.
	 * @param pa Punto inicial de la recta a dibujar.
	 * @param pb Punto final de la recta a dibujar.
	 */
	private void dibujaLinea(Point pa, Point pb) {
		Graphics g = c.getGraphics();
    	g.drawLine(															// Dibujado de la línea entre punto anterior y actual.
    			(int)pa.getX(),
    			(int)pa.getY(),
    			(int)pb.getX(),
    			(int)pb.getY()	
    	);
	}

	/**
	 * <p>Title: marcaPunto</p>
	 * <p>Description: Dibuja una marca de cruz en las coordenadas indicadas</p>
	 * Considerar el origen de coordenadas (0.0) está en la
	 * esquina superior izquierda del objeto sobre el que se dibuja.
	 * @param p Punto sobre el que dibujar la cruz.
	 */
	private void marcaPunto(Point p) {
		Graphics g = c.getGraphics();
		int posX = (int) p.getX();
		int posY = (int) p.getY();
        g.fillOval(posX -2,posY -2,4,4);									// Punto central
        g.drawLine(posX -5,posY, posX +5, posY);							// Línea horizontal
        g.drawLine(posX, posY -5, posX, posY +5);							// Línea vertical
	}

	private void dibujarZonas() {
		Graphics g = c.getGraphics();
		
		//Redibujado del fondo.
		if (fondo != null)	g.drawImage(fondo, 5, 5, c.getWidth() -10, c.getHeight() -10, null);
		else g.clearRect(0,0,c.getWidth(),c.getHeight());
		
		//Dibujado del marco.
		g.setColor(Color.black);
		dibujaPoligono(marco);
		
		//Lectura de las zonas y dibujado si procede.
		zonas.forEach((k, z) -> {
			if (z.getZona() != null) {dibujaPoligono(z.getZona());}
		});
		
		//En caso de haber un poligono pendiente de asignación, dibujarlo.
		if(poligono != null) {
			 g.setColor(Color.GREEN);											// Diferenciar el polígono en color verde.
	         g.fillPolygon(poligono);
		}
		
		//En caso de haber puntos y líneas sin terminar de conformar, dibujarlas.
		if(listaPuntos.size() > 0) {
			Point pAux = listaPuntos.get(0);
			marcaPunto(pAux);													// Dibuja una cruz en el punto inicial.
			for(int i = 1; i< listaPuntos.size(); i++) {						// Recorre el resto de puntos y los dibuja.
				Point p = listaPuntos.get(i);
				marcaPunto(p);
				dibujaLinea(pAux,p);											// Dibuja línea de unión entre el punto anterior y el actual.
				pAux = p;														// Punto actual pasa a ser el nuevo punto anterior.
			}
			
		}
	}

	/* Clases internas */
	
	private class MiCanvas extends Canvas {
		/** serialVersionUID */
		private static final long serialVersionUID = 5398261596293519343L;

		@Override
		public void paint(Graphics g) {
			dibujarZonas();														//Redibujado de las zonas.
		}
	}

    private class ComponerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//Creación del poligono.
            poligono = generaPoligono();
            //Primero dibujado de las figuras (por si se desactiva Canvas en reinicio botones).
            dibujarZonas();
            //Reconfiguramos los estados de los botones para el nuevo contexto.
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
			dibujarZonas();
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
			    if(item.equals(nombre)) {										// Si el item coincide con el nombre del Box ->
					combo1.removeItemAt(index);									// Borrado del item excepto si es el primero (0)
					combo2.addItem(item);										// Añadir Item al otro comboBox
					zonas.get(ID).setPoligono(p);			  					// Asignar Poligono a la zona correspondiente al item.
	//				cMap.setZonas(zonas); 										
					poligono = null;											// Iniciado del poligono a null.
					encontrado = true;
			    }
			}
		}
    }

    private class LimpiarListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
            poligono = null;
            dibujarZonas();
            reinicioBotones();
            c.update(c.getGraphics());
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
            //Si el punto esta dentro del marco -> dibujarlo.
            if(marco.contains(p)) {
	            marcaPunto(p);													//Dibujar punto         
	            if(!isPrimero()) { dibujaLinea(a,p); }							//Si no es el primer punto dibujado, unir con una línea al anterior punto.
	            else {contador = 0;}
	            a = p;															// Establece el punto actual como el anterior para la siguiente línea.
	            listaPuntos.add(p);												// Guardar punto en la lista.
	            contador++;
	            if(contador > 2) bCerrarPoligono.setEnabled(true);				// Habilitar botón de cerrar poligono cuando se disponga de al menos 3 puntos.
            }
        }
    }

    private class AbrirListener extends MouseAdapter {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		//Selección de imagen de fondo.
    		String ruta = IO.selFile(1, IO.IMG);
    		//En caso de tener una ruta correcta se procede a la carga.
    		if(ruta != null && ruta != "") {
    			fondo = new ImageIcon(ruta).getImage();
    			dibujarZonas();
    		}
    	}
    }


    /* Funciones para pruebas */

	/**
	 * <p>Title: testModulo</p>
	 * <p>Description: Funcion cuyo proposito es realizar pruebas de funcionamiento
	 * propio del módulo.</p>
	 * zonas internas por esta propia función a efecto de pruebas.
	 */
	public void testModulo() {
    	abrirFrame();
    }

	/**
	 * <p>Title: main</p>
	 * <p>Description: Metodo para pruebas</p>
	 * @param args argumentos
	 */
	public static void main(String[] args) {
		HashMap<Integer, Zona> zonas;
		zonas = new HashMap<Integer, Zona>();
    	Zona z1,z2,z3;
    	z1 = new Zona(1, "Zona 1",0,0, null);
    	z2 = new Zona(2, "Zona 2",0,0, null);
    	z3 = new Zona(3, "Zona 3",0,0, null);
    	zonas.put(z1.getID(), z1);
    	zonas.put(z2.getID(), z2);
    	zonas.put(z3.getID(), z3);
		
		Pizarra pizarra = new Pizarra(zonas);
		pizarra.testModulo();
	}
}