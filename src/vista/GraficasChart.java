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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * <p>Title: GraficasChart</p>  
 * <p>Description: Representa la progresión en modo de gráfica de un dato a lo
 * largo del tiempo, las series representadas deben ser añadidas a través de los
 * métodos correspondientes, por tanto creados con antelación.</p>
 * Las dimensiones de la ventana también deben ser indicadas a través de el método
 * correspondiente.
 * @author Silverio Manuel Rosales Santana
 * @date 17 sept. 2021
 * @version versión 2.1
 */
public class GraficasChart {
	private String etiquetaX;
	private String etiquetaY;
	private String titulo;
	private String tituloF;
	private HashMap<String,XYSeries> seriesMap;
	private XYSeriesCollection dataset;
	private ChartFrame frame;
	private JFreeChart chart;
	
	
	/**
	 * <p>Title: </p>  
	 * <p>Description: Constructor del chart gráfico.</p>  
	 * @param etqX Leyenda de la barra horizontal X.
	 * @param etqY Leyenda de la barra vertical Y.
	 * @param titulo Título del gráfico.
	 * @param tituloFrame Titulo a mostrar en el marco de la ventana.
	 */
	public GraficasChart(String etqX, String etqY, String titulo, String tituloFrame) {
		this.etiquetaX = etqX;
		this.etiquetaY = etqY;
		this.titulo = titulo;
		this.tituloF = tituloFrame;
		seriesMap = new HashMap<String,XYSeries>();
		this.dataset = new XYSeriesCollection();
	}
	
	/**
	 * <p>Title: nuevaSerie</p>  
	 * <p>Description: Añade una nueva serie a la lista de series a representar</p>
	 * El nombre de la serie no debe existir, no se añadirá una nueva serie, 
	 * permaneciendo la existente.
	 * @param nombre Nombre de la nueva serie.
	 */
	public void nuevaSerie(String nombre) {
		if(!seriesMap.containsKey(nombre)) {
			seriesMap.put(nombre, new XYSeries(nombre));
		}
	}
	
	/**
	 * <p>Title: addPunto</p>  
	 * <p>Description: Añade un punto nuevo a la serie indicada. Cada punto de una serie
	 * es representado en una gráfica</p> 
	 * @param serie Nombre de la de serie a la que añadir el dato.
	 * @param x Coordenada X del punto.
	 * @param y Coordenada Y del punto.
	 */
	public void addPunto(String serie, int x, int y) {
		if(seriesMap.containsKey(serie)) {seriesMap.get(serie).add(x,y);}
	}

	/**
	 * <p>Title: setVisible</p>  
	 * <p>Description: Establece si el frame será visible o estará oculto.</p> 
	 * @param visible True para hacerlo Visible, False para tenerlo oculto.
	 */
	public void setVisible(boolean visible) {frame.setVisible(visible); }

	/**
	 * Establece las dimensiones para el marco de la gráfica.
	 * @param dimX La dimensión X de la ventana.
	 * @param dimY La dimensión Y de la ventana.
	 */
	public void setDimensionesFrame(int dimX, int dimY) {
		frame.setPreferredSize(new Dimension(dimX,dimY));
		frame.pack();
	}
	
	/**
	 * <p>Title: addImagen</p>  
	 * <p>Description: Añade una imagen de fondo al gráfico</p> 
	 * @param ruta Ruta de la imagen a establecer de fondo.
	 */
	private void addImagen(String ruta) {
		Image img = null;
		File imageFile = new File(ruta); 										// guarda la imagen en un archivo
		try {img = ImageIO.read(getClass().getResourceAsStream(imageFile.toString()));}
		catch (IOException e) {e.printStackTrace();} 
		chart.setBackgroundImage(img);											//Establecimiento del fondo.
	}

	/**
	 * <p>Title: genera</p>  
	 * <p>Description: Genera la gráfica con los datos contenidos y establecidos</p> 
	 */
	public void genera() {
		if(!seriesMap.isEmpty()) {seriesMap.forEach((k,v) -> dataset.addSeries(v));}	// Añadir las series al dataset.
	    chart = ChartFactory.createXYLineChart(
	            titulo, 														// Título de la gráfica (Embebida)
	            etiquetaX, 														// Etiqueta Coordenada X
	            etiquetaY, 														// Etiqueta Coordenada Y
	            dataset, 														// Datos de la representación.
	            PlotOrientation.VERTICAL,
	            true, 															// Muestra la leyenda de los productos (Producto A)
	            false, 															// Hablita tooltips si está configurado.
	            false															// Habilita urls si está configurado.
	    );  
	    
	    addImagen("/vista/imagenes/histograma1.jpg");							// Configuración imagen de fondo.    
	    frame = new ChartFrame(tituloF, chart);									// Mostramos la grafica en pantalla a traves de un frame específico.
	    setDimensionesFrame(400,400);
	    frame.setDefaultCloseOperation(ChartFrame.DISPOSE_ON_CLOSE);	
	}	

	/* FUNCIONES A BORRAR - SOLO PARA PRUEBAS */
	public void addPuntos() {
	    // Introduccion de datos
	    addPunto("serie1",1, 1);
	    addPunto("serie1",2, 6);
	    addPunto("serie1",3, 3);
	    addPunto("serie1",4, 10);
	    // Datos serie 2
	    addPunto("serie2",1, 2);
	    addPunto("serie2",2, 6);
	    addPunto("serie2",3, 8);
	    addPunto("serie2",4, 100);
	}
	
	/**
	 * <p>Title: main</p>  
	 * <p>Description: </p> 
	 * @param args Argumentos si fuera necesario.
	 */
	public static void main(String[] args) {
//		GraficasChart chart = new GraficasChart("Tiempo (días)","Nivel","Título","Ejemplo Grafica Lineal");
//		chart.nuevaSerie("serie1");
//		chart.nuevaSerie("serie2");
//		chart.addPuntos();
//		chart.genera();
//		chart.setVisible(true);
    }
}