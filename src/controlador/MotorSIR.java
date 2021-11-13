/**  
* <p>Title: MotorSIR.java</p>  
* <p>Description: Motor para el cálculo de los diferentes valores evolutivos de
* una enfermedad. Dichos cálculos están basados en el Módelo SIR. </p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 13 nov. 2021  
* @version 1.0  
*/  
package controlador;

import java.util.HashMap;

import modelo.Zona;

/**
 * <p>Title: MotorSIR</p>  
 * <p>Description: Motor para el cálculo de los diferentes valores evolutivos de
 * una enfermedad. Dichos cálculos están basados en el Módelo SIR.</p>  
 * @author Silverio Manuel Rosales Santana
 * @date 13 nov. 2021
 * @version versión 1.0
 */
public class MotorSIR {
	
	private final double PTE;													//Probabilidad de transmisión de la enfermedad (sin unidades)
	private final double DME;													//Duración media de la enfermedad (en días).
	private final boolean IP;													//Inmunidad Permanente.
	private final double DMIP;													//Duración media de la inmunidad permanente.
	private HashMap<Integer,Zona> zonas;

	/**
	 * <p>Title: </p>  
	 * <p>Description: Constructor que requiere los parámetros obligados
	 * para la realización de los diferentes cálculos de propagación de la enfermedad
	 * bajo este modelo. </p>  
	 * @param pte Probabilidad de transmisión de la enfermedad (sin unidades).
	 * @param dme Duración media de la enfermedad (en días).
	 * @param ip Inmunidad Permanente.
	 * @param dmip Duración media de la inmunidad permanente (en días).
	 * @param zonas Conjunto de zonas representantes de los grupos de estudio.
	 */
	public MotorSIR(Double pte, double dme, boolean ip, double dmip, HashMap <Integer,Zona> zonas) {
		this.PTE = pte;
		this.DME = dme;
		this.IP = ip;
		this.DMIP = dmip;
		this.zonas = zonas;
	}
	
	
	/**
	 * <p>Title: getPrevalencia</p>  
	 * <p>Description: Calcula la prevalencia instantánea </p> 
	 * @param s Número de susceptibles.
	 * @param i Número de infectados o incidentes.
	 * @param r Número de recuperados.
	 * @return La prevalencia instantanea de la enfermedad.
	 */
	public double getPrevalencia( double s, double i, double r) { return i/(s+i+r);}
	
	
	/**
	 * <p>Title: getCI100K</p>  
	 * <p>Description: Calcula los casos incidentes por 100 mil habitantes en el grupo</p> 
	 * @param ci Casos incidentes en el grupo.
	 * @param s Número de susceptibles.
	 * @param i Número de infectados o incidentes.
	 * @param r Número de recuperados.
	 * @return La prevalencia instantanea de la enfermedad.
	 */
	public double getCI100K(double ci, double s, double i, double r) {return 100000*(ci/(s+i+r));}


	/**
	 * @return Probabilidad de transmisión de la enfermedad.
	 */
	public double getPTE() {return PTE;}


	/**
	 * @return Duración media de la enfermedad (en días).
	 */
	public double getDME() {return DME;	}


	/**
	 * Devuelve la existencia o no de inmunidad permanente para la enfermedad estudiada.
	 * @return TRUE si hay inmunidad permanente. False en otro caso.
	 */
	public boolean isIP() {	return IP;}


	/**
	 * @return Duración media de la inmunidad permanente en días.
	 */
	public double getDMIP() {return DMIP;}


	/**
	 * @return El conjunto de grupos de población.
	 */
	public HashMap<Integer,Zona> getZonas() {return zonas;}
	
	/**
	 * <p>Title: getZona</p>  
	 * <p>Description: Devuelve una zona indicada por su identificador.</p> 
	 * @param ID Identificador de la zona o grupo de población.
	 * @return Zona o grupo de población. Null en otro caso.
	 */
	public Zona getZona(int ID) {
		Zona z = null;
		if(zonas.containsKey(ID)) z = zonas.get(ID);
		return z;
	}

}
