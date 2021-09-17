/**  
* <p>Title: GraficasChart.java</p>  
* <p>Description: Representa gráficamente la progresión una cadena de valores.</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 17 Sept. 2021  
* @version 2.0  
*/  
package modelo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * <p>Title: GraficasChart</p>  
 * <p>Description: Representa la progresión en modo de gráfica de un dato a lo
 * largo del tiempo</p>  
 * @author Silverio Manuel Rosales Santana
 * @date 17 sept. 2021
 * @version versión 2.0
 */
public class GraficasChart {
	private String etiquetaX;
	private String etiquetaY;
	private String titulo;
	private XYSeries series;
	
	
	/**
	 * <p>Title: </p>  
	 * <p>Description: Constructor del chart gráfico.</p>  
	 * @param etqX Leyenda de la barra horizontal X.
	 * @param etqY Leyenda de la barra vertical Y.
	 * @param titulo Título del gráfico.
	 */
	public GraficasChart(String etqX, String etqY, String titulo) {
		this.etiquetaX = etqX;
		this.etiquetaY = etqY;
		this.titulo = titulo;
		series = new XYSeries("Lo que representa");
		genera();
	}
	
	/**
	 * <p>Title: addPunto</p>  
	 * <p>Description: Añade un punto nuevo a la gráfica</p> 
	 * @param x Coordenada X del punto.
	 * @param y Coordenada Y del punto.
	 */
	public void addPunto(int x, int y) {series.add(x,y);}

	private void genera() {
        // Introduccion de datos
        addPunto(1, 1);
        addPunto(2, 6);
        addPunto(3, 3);
        addPunto(4, 10);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                titulo, // Título
                etiquetaX, // Etiqueta Coordenada X
                etiquetaY, // Etiqueta Coordenada Y
                dataset, // Datos
                PlotOrientation.VERTICAL,
                true, // Muestra la leyenda de los productos (Producto A)
                false,
                false
        );

        // Mostramos la grafica en pantalla a traves de un frame específico.
        ChartFrame frame = new ChartFrame("Ejemplo Grafica Lineal", chart);
        frame.pack();
        frame.setVisible(true);
	}

	public static void main(String[] args) {
		new GraficasChart("Tiempo (días)","Nivel","Título");
    }
}