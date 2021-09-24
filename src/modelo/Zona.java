/**
 * 
 */
package modelo;

import java.awt.Polygon;
import java.util.Date;
import java.util.HashMap;

import vista.GraficasChart;

/**
 * Clase inicial que almacena los datos significativos de una zona representada
 * por el poligono en cuestión, como su identificador, nombre y figura. También
 * el nivel de contagio de la zona.
 * @author Silverio Manuel Rosales Santana
 * @date 23707/2021
 * @version 1.0
 *
 */
public class Zona {
	/** name Nombre de la zona*/  
	private String name;
	/** ID ID de la zona*/  
	private int ID;
	/** zona Poligono que representa gráficamente una zona*/  
	private Polygon zona;
	/** nivel Nivel de contagio de una zona [0-9]*/  
	private int nivel;
	private final String sn = "Nivel";
	private int contadorN;
	/** historico de la zona */
	private HashMap<Date,Integer> historico;
	private GraficasChart chart;
	
	/**
	 * Constructor de las instancias de zonas.
	 * Recibe todos sus paramétros al instanciarse, ID (normalmente una abreviatura
	 * del nombre, nombre de la zona completo y el poligono que la representa.
	 * @param ID nombre corto unico de la zona representada.
	 * @param name nombre completo de la zona representada.
	 * @param zona poligono cerrado que contiene la representación gráfica de la zona.
	 */
	public Zona(int ID, String name, Polygon zona) {
		setNivel(0);
		historico = new HashMap<Date,Integer>();
		this.name = name;
		this.ID = ID;
		this.zona = zona;
		this.chart = new GraficasChart("Tiempo (días)",
				"Nivel","Zona: " + name,
				"Evolución pandemica." + " ID: " + ID);
		this.contadorN = 0;														//Serie de niveles.
		chart.nuevaSerie(sn);
	}

	/**
	 * @return devuelve el nombre de la zona.
	 */
	public String getName() {return name;}


	/**
	 * @return devuelve ID de la zona
	 */
	public int getID() {return ID;}


	/**
	 * Devuelve el Poligono que representa la zona.
	 * @return devuelve el poligono que representa la zona
	 */
	public Polygon getZona() {return zona;}


	/**
	 * @return devuelve nivel desde 0 hasta 9.
	 */
	public int getNivel() {	return nivel;}


	/**
	 * Establecimiento del nivel actual de la zona. Su valor debe estar entre 0 y 9,
	 *  ambos inclusive. 
	 * @param nivel Nivel a establecer.
	 */
	private void setNivel(int nivel) {this.nivel = nivel;}
	
	@Override
	public String toString() {	
		int npuntos = zona.npoints;
		String txt = getID() + ", " + getName() + ", Nº Puntos: " + npuntos + "\n";
		int[] Px = zona.xpoints;
		int[] Py = zona.ypoints;
		for(int i = 0; i<npuntos; i++) {
			txt += "(" + Px[i] + "," + Py[i] + ")";
		}
		return txt;
	}
	
	/**
	 * <p>Title: addNivel</p>  
	 * <p>Description: Añade un nuevo nivel a su hisstorico y establece el 
	 * nivel añadido como nivel actual.</p>
	 * Cuando el parámetro de Date es null, unicamente establece el nivel indicado
	 * como actual. 
	 * @param d Fecha del nivel (Date).
	 * @param n Nivel a añadir debe estar entre 0 y 9.
	 */
	public void addNivel(Date d, int n) {
		if(d != null && !historico.containsKey(d)) { historico.put(d,n); }
		chart.addPunto(sn, contadorN, n);
		contadorN++;
		setNivel(n);
	}

	/**
	 * Devuelve el historico de la pandemia en esa zona.
	 * @return El historico
	 */
	public HashMap<Date,Integer> getHistorico() {return historico;}

	/**
	 * Devuelve la representación gráfica de los datos dados hasta la fecha
	 * indicada. En caso de un valor null, devuelve la gráfica con todos los
	 * valores almacenados.
	 * @param d Fecha (Date) hasta la cual se debe representar el histograma.
	 * @return El/la grafica
	 */
	public GraficasChart getGrafica(Date d) {
		//Obtener datos ordenados por fecha.
		
		//Ir añadiendo datos a la serie.
		
		//Retorno
		return this.chart;
	}

	/**
	 * <p>Title: setPoligono</p>  
	 * <p>Description: Establece un Poligono como zona representativa gráfica</p> 
	 * @param poligono Figura geometrica.
	 */
	public void setPoligono(Polygon poligono) {this.zona = poligono;}
}
