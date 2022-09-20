/**
* Módulo para el dibujado de los polígonos que representan
* las zonas dentro de un mapa de la simulación.
* <p>Esta clase encargada de servir editor gráfico de la aplicación, 
*  permitiendo la creación, edición, eliminación  y asignación de polígonos a 
*   los grupos de población.</p>
* Permite la carga de una imagen de fondo para facilitar el dibujado de las figuras,
* así mismo el guardado de los resultados en un fichero externo.
* <p>Aplication: UNED</p>
* @author Silverio Manuel Rosales Santana
* @date 22 sept. 2021
* @version 3.6
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

import controlador.ControladorModulos;
import controlador.IO;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import java.awt.event.MouseAdapter;

import modelo.Labels_GUI;
import modelo.OperationsType;
import modelo.TypesFiles;
import modelo.Zona;

/**
 * <p>Clase encargada de servir editor gráfico de la aplicación, 
 *  permitiendo la creación, edición, eliminación  y asignación de polígonos a 
 *   los grupos de población.</p>
 * Permite la carga de una imagen de fondo para facilitar el dibujado de las figuras,
 * así mismo el guardado de los resultados en un fichero externo.
 * @author Silverio Manuel Rosales Santana
 * @date 23 sept. 2021
 * @version versión 3.6
 */
public class Pizarra extends JFrame {
	/** serialVersionUID*/
	private static final long serialVersionUID = 6537461228067301333L;
	private ControladorModulos cm;
	//Controles internos.
	private JButton bLimpiar,bGuardar,bAImagen,bCerrarPoligono,bBoxAplicar;
	private Canvas c;
	private JToolBar toolBar;
	private JComboBox<String> comboBoxAsignar;
	private JComboBox<String> comboBoxAsignados;
	//Dimensiones para la zona de dibujado.
	private final int dimX;
	private final int dimY;														
	private final Dimension dim;
	//Datos internos
	private Polygon poligono;
	private Polygon marco;
	private boolean modificado;
	// listaPuntos almacena los puntos, esta manera permite implementar a posteriori funciones tipo "undo". "restore",etc.
	private ArrayList<Point> listaPuntos;
	private JPanel panelCentral, panelCanvas;

	private java.awt.Image fondo;

	/**
	 * <p>Pizarra donde poder crear los poligonos que representarán
	 * a cada zona</p>
	 * En caso de obtener un valor núlo o cuyo número de zonas no sea mayor a
	 * cero, no se iniciará el módulo.
	 * @param cm Controlador de módulos, requisito indispensable para comunicar
	 *  con el resto de módulos y tener actualizados los datos.
	 */
	public Pizarra(ControladorModulos cm) {
		this.cm = cm;
		this.dimX = 1024;
		this.dimY = 768;
		this.dim = new Dimension(dimX, dimY);
		this.modificado = false;
		this.listaPuntos = new ArrayList<Point>();
		this.panelCentral = new JPanel();
		this.panelCanvas = new JPanel();
		this.c = new MiCanvas();
		configura();
		//Sus propiedades dependen del canvas, por tanto debe
        // ejecutarse después de esta.
		configuraFrame();
	}
	
	/**
	 * <p>Reinicia la vista de este módulo.</p> 
	 *  Elimina los datos almacenados en el mismo y restaura el valor por defecto
	 *   de los controles.
	 */
	public void reset() {
		//Boorado de todos los items.
		comboBoxAsignar.removeAllItems();
		comboBoxAsignados.removeAllItems();
		//Puesta de Items 0, seleccionar.
		comboBoxAsignados.addItem(Labels_GUI.L_DELETE);
		comboBoxAsignar.addItem(Labels_GUI.L_ADD_TO);
		//Limpieza de datos temporales.
		listaPuntos.clear();
		poligono = null;
		modificado = false;
		fondo = null;
		//Primero actualizar los elementos de cada combo.
		zonasToCombo();
		//Luego los controles, pues estos depende de los elementos de los comboboxes.
		updateControls();
		//Redibujado de las zonas.
		dibujarZonas();
		//Refrescar pantallas.
		c.update(c.getGraphics());
	}
	
	/**
	 * Configura las propiedades del marco contenedor, altura
	 *  ancho, desactivación de redimensionabilidad y propiedad de ocultar en vez
	 *   de cierre.
	 */
	public void configuraFrame() {
	    Dimension m = panelCentral.getPreferredSize();
	    int y = (int) m.getHeight() + 15;										//Una altura extra para no ocultar el canvas.
	    int x = (int) m.getWidth();
	    setPreferredSize(new Dimension(x, y));
	    setResizable(false);
	    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    pack();
	}
	
	/**
	 * Description: Cambia de un estado de visibilidad a uno de oculto y
	 * viceversa.
	 */
	public void toogleVisible() { this.setVisible(!isVisible());}

	/**
	 * Configura los parámetros iniciales, datos y controles 
	 * 	generales de este módulo.
	 */
	private void configura() {
		//Inicialización de los componentes en estricto orden de dependencia.
		iniciarCanvas();
		creaMarco();
		crearBotones();
		crearCombos();
        iniciarToolBar();
        updateControls();       
        
        panelCentral.setPreferredSize(dim);
		panelCentral.setSize(dim);
	    panelCentral.setBackground(Color.white);
	    panelCentral.setLayout(new BorderLayout());
	    panelCentral.setOpaque(false);
	    
	    panelCanvas.setOpaque(false);
	    panelCanvas.setLayout(null);
	    
	    //Añadir Observadores.
	    bLimpiar.addActionListener(new LimpiarListener());
	    bCerrarPoligono.addActionListener(new ComponerListener());
	    bBoxAplicar.addActionListener(new AsignarBoxListener() );
	        
	    //Conectar las partes.
	    panelCanvas.add(c);
	    //Añadimos al panel central y este al frame
	    panelCentral.add(panelCanvas, BorderLayout.CENTER);
	    this.add(panelCentral);  
	}


    /* Métodos privados */

	/**
	 * Description: Genera un poligono desde una lista de puntos almacenados
	 *  en un ArrayList. 
	 * @param listaPuntos ArrayList con la lista de puntos que compondrán el Poligono.
	 * @return Poligono generado.
	 */
    private Polygon generaPoligono(ArrayList<Point> listaPuntos) {
    	int contador = listaPuntos.size();
    	Polygon pol = new Polygon();
		//Volcado de coordenadas desde la lista de puntos.
		for(int i = 0; i<contador; i++) {
			Point p = listaPuntos.get(i);
			pol.addPoint((int)p.getX(),(int)p.getY());
		}
		return pol;
    }

    /**
     * Description: Indica si el punto actual definido es el primer punto de la
     *  lista que contiene el módulo. 
     * @return True si es el primer punto, False en otro caso.
     */
    private boolean isPrimero() {return 0 == listaPuntos.size();}

    /**
	 * Dibuja un marco alrededor de la zona de pintado.
	 */
	private void creaMarco() {
		ArrayList<Point> listaP = new ArrayList<Point>();
		listaP.add(new Point(5,5));
		listaP.add(new Point(c.getWidth() - 5,5));
		listaP.add(new Point(c.getWidth() - 5,c.getHeight() - 5));
		listaP.add(new Point(5,c.getHeight() - 5));	
		this.marco = generaPoligono(listaP);
	}
    
	/**
	 * Crea la barra de herramientas y añade los todos los 
	 *  controles (una vez creados e inciados) a la misma.
	 */
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

	/**
	 * <p>Crear los elementos (botones) de la barra de herramientas.</p>
	 * Crea los controles (botones) a mostrar en la barra de herramientas, además
	 *  les asigna su icono.
	 */
	private void crearBotones() {
		//Configuración de los botones.
	    bLimpiar = new JButton(Labels_GUI.BTN_CLEAR);
	    bLimpiar.setIcon(IO.getIcon("/vista/imagenes/Iconos/limpiar_64px.png",25,25));
	    bLimpiar.setToolTipText(Labels_GUI.TT_CLEAR);
	    bAImagen = new JButton(OperationsType.OPEN.toString());
	    bAImagen.addMouseListener(new AbrirListener());
	    bAImagen.setIcon(IO.getIcon("/vista/imagenes/Iconos/carpeta_64px.png",25,25));
	    bAImagen.setToolTipText(Labels_GUI.TT_OPEN);
	    bGuardar = new JButton(OperationsType.SAVE.toString());
	    bGuardar.setIcon(IO.getIcon("/vista/imagenes/Iconos/disquete_64px.png",25,25));
	    bGuardar.setToolTipText(Labels_GUI.TT_SAVE);
	    bGuardar.addMouseListener(new GuardarListener());
        bCerrarPoligono = new JButton(Labels_GUI.BTN_COMPOSE);
        bCerrarPoligono.setIcon(IO.getIcon("/vista/imagenes/Iconos/nodos_64px.png",25,25));
        bCerrarPoligono.setToolTipText(Labels_GUI.TT_COMPOSE);
        bBoxAplicar = new JButton("");
	    bBoxAplicar.setIcon(IO.getIcon("/vista/imagenes/Iconos/aplicando_64px.png",25,25));
	    bBoxAplicar.setToolTipText(Labels_GUI.TT_APPLY);
	}

	/**
	 * <p>Realiza una comprobación para averiguar el nombre del item
	 *  más largo del desplegable, ajustando hasta un límite el ancho mínimo del mismo.</p>
	 *  Busca el compromiso entre el texto más largo y las dimensiones máximas de la ventana.
	 *   como lóngitud mínima a comparar toma 8 carácteres.
	 * @return La cadena de carácteres más larga que identifica uno de los items. Nombre de Zona.
	 */
	private String getMaxItem() {
		int maxLargo = 0;
		String item = Labels_GUI.SAMPLE_TEXT;									//Longitud mínima a comparar
		//Selección de la clave más larga.
		for (Zona zona:cm.getZonas().values()) {
			int l = zona.getName().length();
			if(maxLargo < l) {
				maxLargo = l;
				item = zona.getName();
			}
		}
		return item;
	}

	/**
	 * <p>Establece los elementos a colocar en cada ComboBox.</p>
	 * Recorre todos los grupos de población (zonas) y situa los nombres de cada
	 *  grupo en un combobox u otro, en función de si ya disponen de un poligono
	 *   como representación gráfica.
	 */
	private void zonasToCombo() {
		if(cm.hasZonas()) {
			cm.getZonas().forEach((k,v)->{
				if(v.getPoligono() == null) {comboBoxAsignar.addItem(v.getName());}
				else { comboBoxAsignados.addItem(v.getName());}
			});
		}
	}

	/**
	 * <p>Crea los controles de los ComboBox</p>
	 * Esta función no añade items a los ComboBox, para tal próposito ver
	 *  el método {@link #zonasToCombo() zonasToCombo}.
	 */
	private void crearCombos() {
		//ComboBox zonas sin asignación.
		comboBoxAsignar = new JComboBox<String>();
		comboBoxAsignar.setModel(new DefaultComboBoxModel<String>());
		comboBoxAsignar.setToolTipText(Labels_GUI.TT_SELECT_ZONE);
		//ComboBox de asignaciones dadas.
		comboBoxAsignados = new JComboBox<String>();
		comboBoxAsignados.setModel(new DefaultComboBoxModel<String>());
		comboBoxAsignados.setToolTipText(Labels_GUI.TT_REMOVE_ZONE);
		//Puesta de Item 0, seleccionar.
		comboBoxAsignados.addItem(Labels_GUI.L_DELETE);
		comboBoxAsignar.addItem(Labels_GUI.L_ADD_TO);
		// Ajustar largo máximo
		String itemMax = getMaxItem();
		comboBoxAsignar.setPrototypeDisplayValue(itemMax);
		comboBoxAsignar.setPrototypeDisplayValue(itemMax);
		//Lectura de las zonas y asignación a cada comboBox.
		zonasToCombo();
	}

	/**
	 * <p>Actualiza los controles propios del módulo</p>
	 * Detecta el estado en que se encuentra la aplicación y configura la activación
	 *  o desactivación de los controles en función de dicho estado inicial.
	 */
	private void updateControls() {
        boolean hayPoligono = poligono != null;
        boolean hasItemC1 = comboBoxAsignar.getItemCount() > 1;
        boolean hasItemC2 = comboBoxAsignados.getItemCount() > 1;
        listaPuntos.clear();													//Borrado de la lista de puntos almacenada.

        //Botones, configuración de activación según contexto.
        comboBoxAsignar.setEnabled(hasItemC1 && hayPoligono);
       	comboBoxAsignados.setEnabled(hasItemC2);
       	bCerrarPoligono.setEnabled(listaPuntos.size() > 0);
        bBoxAplicar.setEnabled(hayPoligono || hasItemC2);
        bGuardar.setEnabled(modificado);
        bCerrarPoligono.setEnabled(listaPuntos.size() > 2);

        //Establecer valor por defecto de los comboBox
  		comboBoxAsignados.setSelectedIndex(0);
  		comboBoxAsignar.setSelectedIndex(0);

        // Desactivar cuando no hay poligonos o ya no hay más zonas sin asignar.
        c.setEnabled(!hayPoligono && hasItemC1);
	}
	
	/**
	 * Inicializa el Canvas o zona de dibujo.
	 */
	private void iniciarCanvas() {
		c.setBounds(0, 0, dimX, dimY -25);
		c.setName(Labels_GUI.NAME_CANVAS);
		c.setBackground(Color.LIGHT_GRAY);
		c.addMouseListener(new SelectPointListener());
	}

	/**
	 * Dibuja un polígono en la zona de la pizarra (canvas). 
	 * @param poligono Figura que debe ser dibujada.
	 */
	private void dibujaPoligono(Polygon poligono) {
		if (poligono != null) {
			Graphics g = c.getGraphics();
			if (g == null) {
				System.out.println("Pizarra > G es nulo");
				iniciarCanvas();
				g = c.getGraphics();
			} else {
				//queda dibujado es el último impreso
				g.setColor(Color.BLUE);
				g.drawPolygon(poligono);
			}
		} else {System.out.println("Pizarra > Poligono nulo");}
	}
	
	/**
	 * <p>Dibuja una línea entre dos puntos.</p>
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
	 * <p>Dibuja una marca de cruz en las coordenadas indicadas</p>
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

	/**
	 * <p>Realiza una lectura de las figuras gráficas y las dibuja</p>
	 * Dibuja los poligonos almacenados, las líneas y puntos actuales inclusive, 
	 *  así como el marco.
	 *  <p>En caso de haber una imagen de fondo cargada, también la redibuja.</p>
	 */
	private void dibujarZonas() {
		Graphics g = c.getGraphics();
		
		//Redibujado del fondo.
		if (fondo != null)	g.drawImage(fondo, 5, 5, c.getWidth() -10, c.getHeight() -10, null);
		else g.clearRect(0,0,c.getWidth(),c.getHeight());
		
		//Dibujado del marco.
		g.setColor(Color.black);
		dibujaPoligono(marco);
		
		//Lectura de las zonas y dibujado si procede.
		if(cm.hasZonas()) {
			cm.getZonas().forEach((k, z) -> {
				if (z.getPoligono() != null) {dibujaPoligono(z.getPoligono());}
			});
		}
		
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
	
	/**
	 * <p>Extiende la clase Canvas para permitir la sobrescritura
	 *  de su método paint(), permitiendo hacer llamadas a la función dibujarZonas()
	 *   de la clase Pizarra.</p>
	 * Dicho definición es necesaria para garantizar el correcto dibujado de los elementos
	 *  dentro del canvas cada vez que se añade cualquier nuevo elemento.  
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class MiCanvas extends Canvas {
		/** serialVersionUID */
		private static final long serialVersionUID = 5398261596293519343L;

		@Override
		public void paint(Graphics g) {
			dibujarZonas();														//Redibujado de las zonas.
		}
	}

	/**
	 * <p>Clase específica para el botón componer</p>
	 * Esta clase compone un poligono con los datos actuales, lo dibuja y actualiza 
	 *  los controles.  
	 * @author Silverio Manuel Rosales Santana
	 * @date 22 nov. 2021
	 * @version versión 1.2
	 */
    private class ComponerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//Creación del poligono.
            poligono = generaPoligono(listaPuntos);
            //Primero dibujado de las figuras (por si se desactiva Canvas en reinicio botones).
            dibujarZonas();
            //Reconfiguramos los estados de los botones para el nuevo contexto.
            updateControls();
        }
    }

    /**
     * <p>Efectua los cambios de cada combo box cuando es activado
     *  el control correspondiente.</p>
     *  Implica que eliminará o añadirá los poligonos a los grupos de población
     *   o zonas correspondientes, activa el botón de guardado y actualiza el
     *    estado de los controles.  
     * @author Silverio Manuel Rosales Santana
     * @date 22 nov. 2021
     * @version versión 1.4
     */
    private class AsignarBoxListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//Activamos el botón de guardado en cuanto ha habido un cambio.
			String op = OperationsType.CHANGES.toString();
			modificado = true;
			//Obtener los nombres de los items seleccionados en los ComboBox
			String item1 = comboBoxAsignar.getSelectedItem().toString();
			String item2 = comboBoxAsignados.getSelectedItem().toString();
			//Obtener los indices de los items seleccionados en los ComboBox
			int index1 =  comboBoxAsignar.getSelectedIndex();		
			int index2 =  comboBoxAsignados.getSelectedIndex();
			//Realización de los intercambios de comboboxes pertinentes.
			cambiarBox(item1,index1,comboBoxAsignar,comboBoxAsignados,poligono);
			cambiarBox(item2,index2,comboBoxAsignados,comboBoxAsignar,null);
			//Redibujar zonas (en caso de borrados o de nuevos asignados).
			dibujarZonas();
			//Actualización de los controles en función del nuevo contexto.
			updateControls();
			//Avisar al controlador de que se ha producido un cambio.
			cm.doActionPizarra(op);
		}

		/**
		 * <p>Realiza un cambio de item entre dos JComboBox.</p>
		 * Además al cambiar de un grupo al otro, el poligono que tiene una zona asociado cambiará
		 *  a un nuevo poligono o a un elemento nulo según corresponda.
		 * @param item Nombre del item que cambia de JComboBox
		 * @param index Posición del item.
		 * @param combo1 JComboBox de origen.
		 * @param combo2 JComboBox destino.
		 * @param p Poligono que se asignara al item (Zona). Null para eliminar y un poligono en otro caso.
		 */
		private void cambiarBox(String item, int index, JComboBox<String> combo1, JComboBox<String> combo2, Polygon p) {
			boolean encontrado = false;											//Bandera para salir del bucle al encontrar lo buscado.
			combo1.setSelectedIndex(0);
			//Búsqueda de la zona con el nombre igual al item del checkBox.
			Iterator<Entry<Integer, Zona>> iterator = cm.getZonas().entrySet().iterator();
			while (!encontrado && iterator.hasNext()) {
			    Entry<Integer, Zona> entry = iterator.next();
			    String nombre = entry.getValue().getName();						// Obtención del nombre de la zona.
			    Integer ID = entry.getKey();
			    if(item.equals(nombre)) {										// Si el item coincide con el nombre del Box ->
					combo1.removeItemAt(index);									// Borrado del item excepto si es el primero (0)
					combo2.addItem(item);										// Añadir Item al otro comboBox
					cm.getZonas().get(ID).setPoligono(p);			  			// Asignar Poligono a la zona correspondiente al item.
					poligono = null;											// Iniciado del poligono a null.
					encontrado = true;
			    }
			}
		}
    }

    /**
     * <p>Limpia la pizarra.</p>
     * Los datos de poligonos o líneas pendientes serán borrados. Los datos de
     *  los poligonos asignados permanecerán.  
     * @author Silverio Manuel Rosales Santana
     * @date 22 nov. 2021
     * @version versión 1.2
     */
    private class LimpiarListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
            poligono = null;
            dibujarZonas();
            updateControls();
            c.update(c.getGraphics());
		}
    }

    /**
     * <p>Realiza un seguimiento de los puntos que son seleccionados para
     * conformar el Poligono posteriormente</p>
     * Cada punto seleccionado se marca con una cruz y un punto en su centro, a
     * medida que se van añadiendo más puntos se van creando más líneas hasta
     * finalizar la operación.
     * @author Silverio Manuel Rosales Santana
     * @date 23 sept. 2021
     * @version versión 1.3
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

    /**
     * <p>Realiza la acción de cargar una imagen de fondo que sirva
     *  como guía para el dibujado de los poligonos.</p> 
     * @author Silverio Manuel Rosales Santana
     * @date 22 nov. 2021
     * @version versión 1.2
     */
    private class AbrirListener extends MouseAdapter {
    	
    	/**
    	 * La sobrescritura de este método permite controlar las acciones para
    	 *  abrir un fichero al activar el control mediante la pulsación con el ratón.
    	 * <p>Para alcanzar su objetivo hace uso de las funciones de la clase IO.</p>
    	 * @see controlador#IO 
    	 */
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		// Selección de imagen de fondo.
    		@SuppressWarnings("unused")
			IO io = new IO();													//Sin esta ininicialización previa, no es posible cargar una imagen de fondo.
    		String ruta = IO.selFile(1, TypesFiles.IMG);
    		// En caso de tener una ruta correcta se procede a la carga.
    		if(ruta != null && !ruta.equals("")) {
    			fondo = new ImageIcon(ruta).getImage();
//    			fondo = IO.getImagen(ruta,false, 0, 0);
    			dibujarZonas();
    		}
    	}
    }
    
    /**
     * Realiza la acción de guardado de las figuras modificadas.  
     * @author Silverio Manuel Rosales Santana
     * @date 22 nov. 2021
     * @version versión 1.2
     */
    private class GuardarListener extends MouseAdapter {
    	
    	/**
    	 * Sobrescribe el método para lograr la activación del guardado cuando
    	 *  se pulsa sobre el control correspondiente.
    	 * <p>Esta función hace uso de la llamada al método doAction de la clase
    	 *  controladora correspondiente.</p>
    	 */
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		// Llamada al controlador para efectuar la acción pertinente.
    		boolean resultado = cm.doActionPizarra(OperationsType.SAVE.toString());
    		if(resultado) {
    			//Si se ha guardado, desactivar modificado => desactivar botón guardado.
    			modificado = !resultado;
        		updateControls();
    		}		
    	}
    }
    
}