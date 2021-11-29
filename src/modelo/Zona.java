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
 * el C100K de contagio de la poligono.
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
	/** Número de recuperados iniciales.*/
	private double R0;
	/** Número de susceptibles iniciales.*/
	private double S0;
	/** Número de incidentes (infectados) iniciales.*/
	private double I0;
	/** Prevalencia.*/
	private double P;
	/** C100K Nivel de contagio de una poligono [0-9] */
	private int C100K;
	
	
	// Lista de puntos que componen el poligono.
	private ArrayList<Point> puntosPoligono;
	private final String SN = "Nivel";
	/** Indica el valor del eje X para su representación de cada C100K en el eje Y. */
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
	 * @param s Número de susceptibles iniciales.
	 * @param i Número de incidentes iniciales (infectados).
	 * @param r Número de recuperados iniciales.
	 * @param p Prevalencia (sin unidad).
	 * @param C100K Nivel inicial de contagio, equivale al número de casos por cada 100 mil habitantes.
	 * @param poligono Poligono cerrado representación gráfica de la zona.
	 */
	public Zona(int ID, String name, int nhabitantes, int superficie,
			double s, double i, double r, double p, int C100K, Polygon poligono) {
		puntosPoligono = new ArrayList<Point>();
		this.name = name;
		this.ID = ID;
		this.poblacion = nhabitantes;
		this.superficie = superficie;
		//
		this.S0 = s;
		this.I0 = i;
		this.R0 = r;
		this.P = p;
		this.C100K = C100K;
		//
		this.poligono = poligono;
		//
		this.chart = new GraficasChart("Tiempo (días)",
				"Nivel",
				"Zona: " + name,
				"Evolución pandemica." + " ID: " + ID);
		this.contadorN = 0;														//Serie de niveles.
		chart.addSerie(SN);
		//Añadir los valores iniciales a la gráfica. Por tanto se añaden después de crearla.
		setNivel(C100K);
		setSIR(s,i,r);
	}


	/**
	 * @return La listas de puntos que componen el poligono que representa la zona.
	 */
	public ArrayList<Point> getListaPuntos() {return puntosPoligono;}

	/**
	 * @param listaPuntos El/la puntosPoligono a establecer
	 */
	public void setListaPuntos(ArrayList<Point> listaPuntos) {this.puntosPoligono = listaPuntos;}
	
	@Override
	public String toString() {	
		String txt = getID() + "," + getName() + "," + getPoblacion() + "," + getSuperficie() +
				"," + this.S0 + "," + this.I0 +  "," + this.R0 + "," + this.P + "," + this.C100K;
		//Si contiene un poligono obtenemos sus coordenadas:
		if(poligono != null) {
			int npuntos = poligono.npoints;
			int[] Px = poligono.xpoints;
			int[] Py = poligono.ypoints;
			for(int i = 0; i<npuntos; i++) {
				txt += "," + Px[i] + ";" + Py[i];
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
		Labels labels = new Labels();
		addNivel(labels.getWord(Labels.S),0,s);
		addNivel(labels.getWord(Labels.I),0,i);
		addNivel(labels.getWord(Labels.R),0,r);
	}
	
	/**
	 * <p>Title: addNivel</p>  
	 * <p>Description: Añade un nuevo C100K a su hisstorico y establece el 
	 * C100K añadido como C100K actual.</p>
	 * @param name Nombre de la serie de datos al que añadir el nuevo C100K..
	 * @param t Variable tiempo, indica el valor en el eje X de tiempo al que corresponderá el valor de la función.
	 * @param valor Valor o C100K a añadir a dicha serie.
	 */
	public void addNivel(String name, int t, double valor) {
		if(name.equals(SN)) {
			chart.addPunto(name, contadorN, valor);
			setNivel((int) valor);
			contadorN++;			
		}else chart.addPunto(name, t, valor);
	}
	
	
	/* Funciones get y set de los atributos del grupo de población o zona. */ 
	

	/**
	 * Devuelve la representación gráfica de los datos dados.
	 * @return El/la grafica
	 */
	public GraficasChart getGrafica() {return this.chart;	}
	
	/**
	 * @return devuelve C100K desde 0 hasta 9.
	 */
	public int getNivel() {	return C100K;}

	/**
	 * Establecimiento del C100K actual de la poligono. Su valor debe estar entre 0 y 9,
	 *  ambos inclusive. 
	 * @param C100K Nivel a establecer.
	 */
	public void setNivel(int C100K) {this.C100K = C100K;}

	/**
	 * @return El número de recuperados iniciales.
	 */
	public double getR() {	return R0;}

	/**
	 * @param r Establece el número de recuperados iniciales.
	 */
	public void setR(double r) {this.R0 = r;	}

	/**
	 * @return El número de susceptibles iniciales.
	 */
	public double getS() {	return S0;}

	/**
	 * @param d Establece el número de suceptibles.
	 */
	public void setS(double d) {this.S0 = d;	}

	/**
	 * @return La prevalencia inicial.
	 */
	public double getP() {	return P;}

	/**
	 * @param p Establece la prevalencia inicial.
	 */
	public void setP(double p) {this.P = p;	}

	/**
	 * @return El número de incidentes (infectados) iniciales.
	 */
	public double getI() {	return I0;}

	/**
	 * @param i Establece el número de Incidentes (infectados) iniciales.
	 */
	public void setI(double i) {this.I0 = i;}

	/**
	 * @return devuelve el nombre de la poligono.
	 */
	public String getName() {return name;}

	/**
	 * @param name Nuevo nombre para el grupo de población.
	 */
	public void setName(String name) {this.name = name;}
	
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
	public Polygon getPoligono() {return poligono;}

	/**
	 * <p>Title: setPoligono</p>  
	 * <p>Description: Establece un Poligono como poligono representativa gráfica</p> 
	 * @param poligono Figura geometrica.
	 */
	public void setPoligono(Polygon poligono) {this.poligono = poligono;}

}
