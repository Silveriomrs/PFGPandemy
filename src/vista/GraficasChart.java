/**  
* <p>Title: GraficasChart.java</p>  
* <p>Description: Representa gráficamente la progresión una cadena de valores.</p>    
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

import modelo.IO;

import java.awt.BorderLayout;
import javax.swing.JCheckBox;

/**
 * <p>Title: GraficasChart</p>  
 * <p>Description: Representa la progresión en modo de gráfica de un dato a lo
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
	private final String background = "/vista/imagenes/histograma1.jpg";
	
	/**
	 * <p>Title: </p>  
	 * <p>Description: Constructor del chart gráfico.</p>  
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
	
	private void iniciarMenu() {
		cframe.getContentPane().add(menuBar, BorderLayout.NORTH);
		mnVer = new JMenu("Ver");
		mnCasos = new JMenu("Casos");
		mnTasas = new JMenu("Tasas");
		menuBar.add(mnVer);
		menuBar.add(mnCasos);
		menuBar.add(mnTasas);
	}
	
	private void addJMenuItem(JMenu padre, String nombre, String rutaIcon) {
		JCheckBox JCBox = new JCheckBox(new VerMenuListener(nombre));
		JCBox.setSelected(false);
		if(rutaIcon != null)  JCBox.setIcon(IO.getIcon(rutaIcon,20,20));
		padre.add(JCBox);
	}
	
	 /**
	 * <p>Title: VerMenuListener</p>  
	 * <p>Description: Clase dedicada al establecimiento de los datos en los
	 * apartados o módulos oportunos.</p>  
	 * @author Silverio Manuel Rosales Santana
	 * @date 10 ago. 2021
	 * @version versión
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
	 * <p>Title: iniciarChart</p>  
	 * <p>Description: Inicia las propiedades básicas del Chart</p>
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
	 * <p>Title: getJPanel</p>  
	 * <p>Description: Devuelve este submódulo encapsulado dentro de un
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
	 * <p>Title: iniciarFrame</p>  
	 * <p>Description: Inicia las propiedades básicas del frame.</p>
	 */
	private void iniciarFrame() {
	    cframe = new ChartFrame(tituloF, chart);
		cframe.setPreferredSize(new Dimension(400,400));
	    cframe.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);
	    cframe.getContentPane().setLayout(new BorderLayout(0, 0));
	    cframe.pack();
	}
	
	/**
	 * <p>Title: addSerie</p>  
	 * <p>Description: Añade una nueva serie a la lista de series a representar</p>
	 * El nombre de la serie no debe existir, no se añadirá una nueva serie, 
	 * permaneciendo la existente.
	 * @param nombre Nombre de la nueva serie.
	 */
	public void addSerie(String nombre) {
		if(!seriesMap.containsKey(nombre)) {
			//Añadir una nueva serie al mapa de series.
			seriesMap.put(nombre, new XYSeries(nombre));
			//Nueva serie -> nueva opción de visualizar en el menú.
			if(nombre.startsWith("Casos")) addJMenuItem(mnCasos,nombre,null);
			else if(nombre.startsWith("Tasa")) addJMenuItem(mnTasas,nombre,null);
			else addJMenuItem(mnVer,nombre,null);
		}
	}
	
	/**
	 * <p>Title: addPunto</p>  
	 * <p>Description: Añade un punto nuevo a la serie indicada. Cada punto de una serie
	 * es representado en una gráfica</p> 
	 * @param serie Nombre de la de serie a la que añadir el dato.
	 * @param x Valor del punto en el eje horizontal.
	 * @param y Valor del punto en el eje vertical.
	 */
	public void addPunto(String serie, double x, double y) {
		//Comprobación previa si existe dicha serie, en caso contrario la crea.
		if(!seriesMap.containsKey(serie)) {	addSerie(serie);}
		//Añade el valor a la serie.
		seriesMap.get(serie).add(x,y);
	}
	
	
	/**
	 * <p>Title: getYValue</p>  
	 * <p>Description: Devuelve el valor del eje Y en la posición indicada por
	 * parámetro</p>
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
				e.printStackTrace();
			}
		}
		return y;
	}
	
	
	/**
	 * <p>Title: printSerie</p>  
	 * <p>Description: Imprime por consola los valores X e Y pertenecientes a la serie.</p> 
	 * @param serie Nombre de la serie.
	 */
	public void printSerie(String serie) {
		if(seriesMap.containsKey(serie)) {
			double tmp[][] = seriesMap.get(serie).toArray();
			for(int j = 0; j<tmp[0].length ; j++) {
				double x = tmp[0][j];
				double y = tmp[1][j];
				System.out.println("(" + x +  "," + y + ")");
			}	
		}
	}

	/**
	 * <p>Title: setVisible</p>  
	 * <p>Description: Establece si el cframe será visible o estará oculto.</p> 
	 * @param visible True para hacerlo Visible, False para tenerlo oculto.
	 */
	public void setVisible(boolean visible) {cframe.setVisible(visible); }
	
	/**
	 * <p>Title: addImagen</p>  
	 * <p>Description: Añade una imagen de fondo al gráfico</p> 
	 * @param ruta Ruta de la imagen a establecer de fondo.
	 */
	private void addImagen(String ruta) {
		Image img = IO.getImagen(ruta, false, 0, 0);
		this.chart.setBackgroundImage(img);										//Establecimiento del fondo.
	}

	/**
	 * <p>Title: genera</p>  
	 * <p>Description: Genera la gráfica con los datos contenidos y establecidos</p> 
	 */
	public void genera() {	
		//Si cframe no está inicializado crear.
	    if(cframe == null) {
		    System.out.println("Graficas Chart - Genera > frame era nulo y se reinicia");
	    	iniciarFrame();
	    }else if(cframe != null && !cframe.isVisible()) {						// En caso de estar oculto, mostrar.
	    	cframe.setVisible(true);
	    }
	}

	/* FUNCIONES SOLO PARA PRUEBAS */
	
	/**
	 * <p>Title: addPuntos</p>  
	 * <p>Description: A efectos de pruebas</p> 
	 */
	public void addPuntos() {
	    // Introduccion de datos
	    addPunto("serie1",1.0, 1.8);
	    addPunto("serie1",2.3, 6.65);
	    addPunto("serie1",3.1, 3);
	    addPunto("serie1",3.2, 10);
	    // Datos serie 2
	    addPunto("serie2",1, 2);
	    addPunto("serie2",2, 6);
	    addPunto("serie2",3, 8);
	    addPunto("serie2",4, 12);
	}
	
	/**
	 * <p>Title: main</p>  
	 * <p>Description: </p> 
	 * @param args Argumentos si fuera necesario.
	 */
	public static void main(String[] args) {
		GraficasChart chart = new GraficasChart("Tiempo (días)","Nivel","Título","Ejemplo Grafica Lineal");
		chart.addSerie("serie1");
		chart.addSerie("serie2");
		chart.addPuntos();
		chart.setVisible(true);
		//Pruebas
		System.out.println(chart.getYValue("serie1", 0));		
    }
}