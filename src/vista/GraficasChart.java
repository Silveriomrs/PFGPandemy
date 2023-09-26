/**  
* Representa gráficamente la progresión una cadena de valores.   
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 17 Sept. 2021  
* @version 2.0  
*/  
package vista;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import controlador.IO;
import modelo.Labels;
import modelo.Labels_GUI;

import java.awt.BorderLayout;
import javax.swing.JCheckBox;

/**
 * <p>Representa la progresión en modo de gráfica de un dato a lo
 * largo del tiempo, las series representadas deben ser añadidas a través de los
 * métodos correspondientes, por tanto creados con antelación.</p>
 * Las dimensiones de la ventana también deben ser indicadas a través de el método
 * correspondiente.
 * @author Silverio Manuel Rosales Santana
 * @date 17 sept. 2021
 * @version versión 2.2
 */
public class GraficasChart{
	private String etiquetaX;
	private String etiquetaY;
	private String titulo;
	private String tituloF;
	private HashMap<String,XYSeries> seriesMap;
	private XYSeriesCollection dataset;
	private ChartFrame cframe;
	private JFreeChart chart;
	private JMenuBar menuBar;
	private JMenu mnVer;
	private JMenu mnCasos;
	private JMenu mnTasas;
	private final String background = "/vista/imagenes/degradado.png";
	
	/**
	 * Constructor del chart gráfico de línea.  
	 * @param etqX Paleta de la barra horizontal X.
	 * @param etqY Paleta de la barra vertical Y.
	 * @param titulo Título del gráfico.
	 * @param tituloFrame Titulo a mostrar en el marco de la ventana.
	 */
	public GraficasChart(String etqX, String etqY, String titulo, String tituloFrame) {
		this.etiquetaX = etqX;
		this.etiquetaY = etqY;
		this.titulo = titulo;
		this.tituloF = tituloFrame;
		this.seriesMap = new HashMap<String,XYSeries>();
		this.dataset = new XYSeriesCollection();
		this.menuBar = new JMenuBar();
		iniciarChart();
		iniciarFrame();
		iniciarMenu();
	}
	
	/**
	 * Renombra el título de la gráfica de línea.
	 * @param newTitle Nuevo título para la gráfica.
	 */
	public void changeTitle(String newTitle) {
		this.titulo = newTitle;
		chart.setTitle(this.titulo);
	}
	
	/**
	 * Inicia los parámetros internos del menú adjunto a la gráfica.
	 */
	private void iniciarMenu() {	
		mnVer = new JMenu(Labels_GUI.M_VER);
		mnCasos = new JMenu(Labels_GUI.M_CASOS);
		mnTasas = new JMenu(Labels_GUI.M_TASAS);
		menuBar.add(mnVer);
		menuBar.add(mnCasos);
		menuBar.add(mnTasas);
		cframe.getContentPane().add(menuBar, BorderLayout.NORTH);
	}
	
	/**
	 * Añade una opción del menú a la barra de menú, facilitando el añadir opciones
	 *  a la barra.
	 * @param padre Menú padre del que colgará.
	 * @param nombre Nombre con el que aparecerá en el menú desplegable.
	 * @param selected Indica si incialmente debe estar seleccionado para mostrarse. True lo activa por defecto. False en otro caso.
	 * @param rutaIcon Icono de imagen que se desea asociar a dicha opción del menú.
	 */
	private void addJMenuItem(JMenu padre, String nombre, boolean selected, String rutaIcon) {
		JCheckBox JCBox = new JCheckBox(new VerMenuListener(nombre));
		JCBox.setSelected(selected);
		if(rutaIcon != null)  JCBox.setIcon(IO.getIcon(rutaIcon,20,20));
		padre.add(JCBox);
	}
	
	 /**
	 * Clase dedicada al establecimiento de los datos en los apartados o módulos oportunos.
	 * @author Silverio Manuel Rosales Santana
	 * @date 10 ago. 2021
	 * @version versión 2.2
	 */
	private class VerMenuListener extends AbstractAction {
		/** serialVersionUID*/  
		private static final long serialVersionUID = -5103462996882781094L;
		private final String name;
		
		public VerMenuListener(String name) {
			super(name);
			this.name = name;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//Si se ha seleccionado módulo ->	
		    JCheckBox cbLog = (JCheckBox) e.getSource();
		    //Si se ha seleccionado y no estaba la serie añadida -> añadirla
	        if (cbLog.isSelected() && !dataset.getSeries().contains(name)) {
	            dataset.addSeries(seriesMap.get(name));         				//Añade la serie.
	        //En otro caso eliminar la serie de la representación.
	        } else {
				dataset.removeSeries(seriesMap.get(name));						//Eliminar serie.
	        }
		}
	}
	
	/**
	 * <p>Inicia las propiedades básicas del Chart</p>
	 * Entre dichas propiedades está la imagen de fondo a mostrar.
	 */
	private void iniciarChart() {
		// Añadir las series al dataset.
	    chart = ChartFactory.createXYLineChart(
	            titulo, 														// Título de la gráfica (Embebida)
	            etiquetaX, 														// Etiqueta Coordenada X
	            etiquetaY, 														// Etiqueta Coordenada Y
	            dataset, 														// Datos de la representación.
	            PlotOrientation.VERTICAL,
	            true, 															// Muestra la leyenda de los productos (Producto A).
	            false, 															// Hablita tooltips si está configurado.
	            false															// Habilita urls si está configurado.
	    );
	    addImagen(background);													// Configuración imagen de fondo.
	}
	
	/**
	 * <p>Reinicia los datos de todas las series borrandolos</p>
	 * El objetivo es limpiar la gráfica para permitir que reproduzca desde el punto
	 *  inicial. 
	 */
	public void reset() {
//		dataset.removeAllSeries();
		seriesMap.forEach((k,v) ->{
			v.clear();
		});
	}
	
	/**
	 * <p>Devuelve este submódulo encapsulado dentro de un
	 *  JPanel, de esta forma, facilita su inclusión dentro de otras vistas. </p>
	 *  Su distribución interna es de BorderLayot e incluye su barra de menus.
	 *   Hay que establecer sus dimensiones y posición en la clase receptora.
	 * @return La vista del Chart en un JPanel.
	 */
	public JPanel getJPanel() {
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout(0, 0));
		jp.add(new ChartPanel(this.chart),BorderLayout.CENTER);
		jp.add(this.menuBar,BorderLayout.NORTH);
		return jp;
	}
	
	/**
	 * Inicia las propiedades básicas del frame.
	 */
	private void iniciarFrame() {
	    cframe = new ChartFrame(tituloF, chart);
		cframe.setPreferredSize(new Dimension(400,400));
	    cframe.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
	    cframe.getContentPane().setLayout(new BorderLayout(0, 0));
	    cframe.pack();
	}
	
	/**
	 * <p>Añade una nueva serie a la lista de series a representar</p>
	 * El nombre de la serie no debe existir, no se añadirá una nueva serie, 
	 * permaneciendo la existente.
	 * @param nombre Nombre de la nueva serie.
	 */
	public void addSerie(String nombre) {
		if(!seriesMap.containsKey(nombre)) {
			//Añadir una nueva serie al mapa de series.
			seriesMap.put(nombre, new XYSeries(nombre));
			//Nueva serie -> nueva opción de visualizar en el menú.
			if(nombre.startsWith(Labels_GUI.M_CASOS)) addJMenuItem(mnCasos,nombre,false,null);
			else if(nombre.startsWith(Labels_GUI.M_TASA)) addJMenuItem(mnTasas,nombre,false,null);
			else if(nombre.equals(Labels.getWord( Labels.C100K ) )) {
				//Añadir serie al menú
				addJMenuItem(mnVer,nombre,true,null);
				//Añadir nueva serie al dataset desde el hashmap seriesMap.
				dataset.addSeries(seriesMap.get(nombre));
			}else{
				addJMenuItem(mnVer,nombre,false,null);
			}
		}
	}
	
	/**
	 * Añade un punto nuevo a la serie indicada. Cada punto de una serie
	 * es representado en una gráfica
	 * @param serie Nombre de la de serie a la que añadir el dato.
	 * @param x Valor del punto en el eje horizontal.
	 * @param y Valor del punto en el eje vertical.
	 */
	public void addPunto(String serie, double x, double y) {
		//Comprobación previa si existe dicha serie, en caso contrario la crea.
		if(serie != null && !seriesMap.containsKey(serie)) {addSerie(serie);}
		//Añade el valor a la serie o actualiza el valor si no existe.
		seriesMap.get(serie).addOrUpdate(x, y);
	}
		
	/**
	 * <p>Devuelve el valor del eje Y en la posición indicada por
	 * parámetro.</p>
	 * En este proyecto el eje X indica el tiempo en días y son unidades enteras. 
	 * @param serie Nombre de la serie cuyo valor se quiere obtener.
	 * @param x Posición del valor a obtener, equivalente al día.
	 * @return Valor del punto en la serie cuya posición es la indicada. -1.0 en otro caso.
	 */
	public double getYValue(String serie, int x) {
		double y = -1.0;
		if(seriesMap.containsKey(serie)) {
			double tmp[][] = seriesMap.get(serie).toArray();
			int size = tmp[0].length;
			// [0 = X / 1 = Y][xPos]
			try{y = tmp[1][x];}
			catch(Exception e) {
				System.out.println("GraciasChart > getYValue : Valores incorrectos: " + serie + " size: " + size + " X: "+ x);
			}
		}
		return y;
	}
	
	/**
	 * <p>Devuelve los valores almanceados de una serie dentro de un Array de Strings.</p> 
	 * Cada posición se corresponde con el tiempo o valor en el eje X.
	 * @param nombre Nombre de la serie.
	 * @return Cadena de valores almancenados como textos.
	 */
	public String[] getSerie(String nombre) {
		String[] serie = null;
		
		if(seriesMap.containsKey(nombre)) {
			double tmp[][] = seriesMap.get(nombre).toArray();
			serie = new String[tmp[0].length +1];								//Primera posición para el nombre de la serie.
			serie[0] = nombre;
			for(int j = 0; j<tmp[0].length ; j++) {
//				double x = tmp[0][j];
				double y = tmp[1][j];
				serie[j+1] = "" + y;											//Sumar una posción más respecto a la primera columna (nombres)
			}	
		}	
		return serie;
	}
	
	/**
	 * Devuelve en un array bidimensional todas las series.
	 * @param nRegistros El indice del último registro a incluir en la matriz.
	 * @return Matriz bidimensional (array) con las series y sus datos hasta el índice indicado.
	 */
	public String[][] getAllSeries(int nRegistros){
		int nSeries = seriesMap.size();
		int contador = 0;
		String[][] todo = new String[nSeries][nRegistros];
		
		for (String clave:seriesMap.keySet()) {
			String[] fila = getSerie(clave);
			todo[contador] = fila;
			contador++;
		}

		return todo;
	}

	/**
	 * Establece si el cframe será visible o estará oculto.
	 * @param visible True para hacerlo Visible, False para tenerlo oculto.
	 */
	public void setVisible(boolean visible) {cframe.setVisible(visible); }
	
	/**
	 * Añade una imagen de fondo al gráfico.
	 * @param ruta Ruta de la imagen a establecer de fondo.
	 */
	private void addImagen(String ruta) {
		Image img = IO.getImagen(ruta, false, 0, 0);
		this.chart.setBackgroundImage(img);										//Establecimiento del fondo.
	}

}