/**
 * 
 */
package modelo;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import vista.GraficasChart;

/**
 * Clase inicial que almacena los datos significativos de una poligono representada
 * por el poligono en cuestión, como su identificador, nombre y figura. También
 * el nivel de contagio de la poligono.
 * @author Silverio Manuel Rosales Santana
 * @date 23707/2021
 * @version 1.0
 *
 */
public class Zona {
	/** name Nombre de la poligono*/  
	private String name;
	/** ID de la poligono*/  
	private int ID;
	/** Población de la poligono */
	private int poblacion;
	/** Superficie de la poligono en Kilometros cuadrados*/
	private int superficie;
	/** poligono Poligono que representa gráficamente una poligono*/
	private Polygon poligono;
	// Lista de puntos que componen el poligono.
	private ArrayList<Point> puntosPoligono;
	/** nivel Nivel de contagio de una poligono [0-9] */
	private int nivel;
	private final String SN = "Nivel";
	/** Indica el valor del eje X para su representación de cada nivel en el eje Y. */
	private int contadorN;
	
	private GraficasChart chart;
	
	/**
	 * Constructor de las instancias de zonas.
	 * Recibe todos sus paramétros al instanciarse, ID (normalmente una abreviatura
	 * del nombre, nombre de la poligono completo y el poligono que la representa.
	 * @param ID Número identificador unico de la población representada.
	 * @param name Nombre del grupo de población representado.
	 * @param nhabitantes Número de habitantes que componen la población del grupo.
	 * @param superficie Superficie del poligono en kilometros cuadrados.
	 * @param poligono Poligono cerrado representación gráfica de la zona.
	 */
	public Zona(int ID, String name, int nhabitantes, int superficie, Polygon poligono) {
		setNivel(0);
		puntosPoligono = new ArrayList<Point>();
		this.name = name;
		this.ID = ID;
		this.poblacion = nhabitantes;
		this.superficie = superficie;
		this.poligono = poligono;
		this.chart = new GraficasChart("Tiempo (días)",
				"Nivel",
				"Zona: " + name,
				"Evolución pandemica." + " ID: " + ID);
		this.contadorN = 0;														//Serie de niveles.
		chart.addSerie(SN);
	}

	/**
	 * @return devuelve el nombre de la poligono.
	 */
	public String getName() {return name;}

	/**
	 * @return devuelve ID de la poligono
	 */
	public int getID() {return ID;}

	/**
	 * @return El número de habitantes que componen la población de la poligono.
	 */
	public int getPoblacion() {	return poblacion;}
	
	/**
	 * @param poblacion El número de habitantes a establecer
	 */
	public void setPoblacion(int poblacion) {this.poblacion = poblacion;}

	/**
	 * @return La superficie de la poligono en kilometros cuadrados.
	 */
	public int getSuperficie() {return superficie;}

	
	/**
	 * @param superficie Los kilometros cuadrados de superficie a establecer
	 */
	public void setSuperficie(int superficie) {	this.superficie = superficie;}
	
	/**
	 * Devuelve el Poligono que representa la poligono.
	 * @return devuelve el poligono que representa la poligono
	 */
	public Polygon getZona() {return poligono;}

	/**
	 * <p>Title: setPoligono</p>  
	 * <p>Description: Establece un Poligono como poligono representativa gráfica</p> 
	 * @param poligono Figura geometrica.
	 */
	public void setPoligono(Polygon poligono) {this.poligono = poligono;}

	/**
	 * @return La listas de puntos que componen el poligono que representa la zona.
	 */
	public ArrayList<Point> getListaPuntos() {return puntosPoligono;}

	/**
	 * @param listaPuntos El/la puntosPoligono a establecer
	 */
	public void setListaPuntos(ArrayList<Point> listaPuntos) {this.puntosPoligono = listaPuntos;}

	/**
	 * @return devuelve nivel desde 0 hasta 9.
	 */
	public int getNivel() {	return nivel;}

	/**
	 * Establecimiento del nivel actual de la poligono. Su valor debe estar entre 0 y 9,
	 *  ambos inclusive. 
	 * @param nivel Nivel a establecer.
	 */
	private void setNivel(int nivel) {this.nivel = nivel;}
	
	@Override
	public String toString() {	
		String txt = getID() + ", " + getName() + ", " + getPoblacion() + ", " + getSuperficie();
		//Si contiene un poligono obtenemos sus coordenadas:
		if(poligono != null) {
			int npuntos = poligono.npoints;
			int[] Px = poligono.xpoints;
			int[] Py = poligono.ypoints;
			for(int i = 0; i<npuntos; i++) {
				txt += "(" + Px[i] + ";" + Py[i] + ")";
			}
		}
		return txt;
	}
	
	
	/**
	 * <p>Title: setSIR</p>  
	 * <p>Description: Establece los valores iniciales Susceptibles, Recuperados
	 * e Incidentes del grupo de población representado.</p> 
	 * @param s Número de susceptibles.
	 * @param i Número de incidentes o infectados.
	 * @param r Número de recuperados o curados.
	 */
	public void setSIR(double s, double i, double r) {
		addNivel("Susceptibles",0,s);
		addNivel("Incidencias",0,i);
		addNivel("Recuperados",0,r);
	}
	
	/**
	 * <p>Title: addNivel</p>  
	 * <p>Description: Añade un nuevo nivel a su hisstorico y establece el 
	 * nivel añadido como nivel actual.</p>
	 * @param name Nombre de la serie de datos al que añadir el nuevo nivel..
	 * @param t Variable tiempo, indica el valor en el eje X de tiempo al que corresponderá el valor de la función.
	 * @param valor Valor o nivel a añadir a dicha serie.
	 */
	public void addNivel(String name, int t, double valor) {
		if(name.equals(SN)) {
			chart.addPunto(name, contadorN, valor);
			contadorN++;			
		}else chart.addPunto(name, t, valor);
		
		setNivel((int) valor);
	}

	/**
	 * Devuelve la representación gráfica de los datos dados.
	 * @return El/la grafica
	 */
	public GraficasChart getGrafica() {return this.chart;	}

}
