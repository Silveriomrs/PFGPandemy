/**
 * 
 */
package modelo;

import java.awt.Polygon;

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
	private String ID;
	/** zona Poligono que representa gráficamente una zona*/  
	private Polygon zona;
	/** nivel Nivel de contagio de una zona [0-9]*/  
	private int nivel;	
	
	/**
	 * Constructor de las instancias de zonas.
	 * Recibe todos sus paramétros al instanciarse, ID (normalmente una abreviatura
	 * del nombre, nombre de la zona completo y el poligono que la representa.
	 * @param ID nombre corto unico de la zona representada.
	 * @param name nombre completo de la zona representada.
	 * @param zona poligono cerrado que contiene la representación gráfica de la zona.
	 */
	public Zona(String ID, String name, Polygon zona) {
		setNivel(0);
		this.name = name;
		this.ID = ID;
		this.zona = zona;
	}

	/**
	 * @return devuelve el nombre de la zona.
	 */
	public String getName() {return name;}


	/**
	 * @return devuelve ID de la zona
	 */
	public String getID() {return ID;}


	/**
	 * @return devuelve el poligono que representa la zona
	 */
	public Polygon getZona() {return zona;}


	/**
	 * @return devuelve nivel desde 0 hasta 9.
	 */
	public int getNivel() {	return nivel;}


	/**
	 * Establecimiento del nivel de la zona. Su valor debe estar entre 0 y 9,
	 *  ambos inclusive. 
	 * @param nivel Nivel a establecer.
	 */
	public void setNivel(int nivel) {this.nivel = nivel;}
	
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
}
