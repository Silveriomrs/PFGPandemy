/**
 * 
 */
package modelo;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
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
	/** ID de la zona*/  
	private int ID;
	/** Población de la zona */
	private int poblacion;
	/** Superficie de la zona en Kilometros cuadrados*/
	private int superficie;
	/** zona Poligono que representa gráficamente una zona*/
	private Polygon zona;
	// Lista de puntos que componen el poligono.
	private ArrayList<Point> listaPuntos;
	/** nivel Nivel de contagio de una zona [0-9] */  
	private int nivel;
	private final String sn = "Nivel";
	/** Indica el valor del eje X para su representación de cada nivel en el eje Y. */
	private int contadorN;														
	
	private GraficasChart chart;
	
	/**
	 * Constructor de las instancias de zonas.
	 * Recibe todos sus paramétros al instanciarse, ID (normalmente una abreviatura
	 * del nombre, nombre de la zona completo y el poligono que la representa.
	 * @param ID nombre corto unico de la zona representada.
	 * @param name nombre completo de la zona representada.
	 * @param nhabitantes Número de habitantes que componen la población del grupo/zona.
	 * @param superficie Superficie de la zona en kilometros cuadrados.
	 * @param zona poligono cerrado que contiene la representación gráfica de la zona.
	 */
	public Zona(int ID, String name, int nhabitantes, int superficie, Polygon zona) {
		setNivel(0);
		listaPuntos = new ArrayList<Point>();
		this.name = name;
		this.ID = ID;
		this.poblacion = nhabitantes;
		this.superficie = superficie;
		this.zona = zona;
		this.chart = new GraficasChart("Tiempo (días)",
				"Nivel",
				"Zona: " + name,
				"Evolución pandemica." + " ID: " + ID);
		this.contadorN = 0;														//Serie de niveles.
		chart.addSerie(sn);
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
	 * @return El número de habitantes que componen la población de la zona.
	 */
	public int getPoblacion() {	return poblacion;}
	
	/**
	 * @param poblacion El número de habitantes a establecer
	 */
	public void setPoblacion(int poblacion) {this.poblacion = poblacion;}

	/**
	 * @return La superficie de la zona en kilometros cuadrados.
	 */
	public int getSuperficie() {return superficie;}

	
	/**
	 * @param superficie Los kilometros cuadrados de superficie a establecer
	 */
	public void setSuperficie(int superficie) {	this.superficie = superficie;}
	
	/**
	 * Devuelve el Poligono que representa la zona.
	 * @return devuelve el poligono que representa la zona
	 */
	public Polygon getZona() {return zona;}

	/**
	 * <p>Title: setPoligono</p>  
	 * <p>Description: Establece un Poligono como zona representativa gráfica</p> 
	 * @param poligono Figura geometrica.
	 */
	public void setPoligono(Polygon poligono) {this.zona = poligono;}

	/**
	 * @return La listaPuntos que componen el poligono representación de la zona.
	 */
	public ArrayList<Point> getListaPuntos() {return listaPuntos;}

	/**
	 * @param listaPuntos El/la listaPuntos a establecer
	 */
	public void setListaPuntos(ArrayList<Point> listaPuntos) {this.listaPuntos = listaPuntos;}

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
		String txt = getID() + ", " + getName() + ", " + getPoblacion() + ", " + getSuperficie() +"\n";
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
	 * @param name Nombre de la serie de datos al que añadir el nuevo nivel..
	 * @param valor Valor o nivel a añadir a dicha serie.
	 */
	public void addNivel(String name, int valor) {
		chart.addPunto(name, contadorN, valor);
		contadorN++;
		setNivel(valor);
	}

	/**
	 * Devuelve la representación gráfica de los datos dados.
	 * @return El/la grafica
	 */
	public GraficasChart getGrafica() {return this.chart;	}

}
