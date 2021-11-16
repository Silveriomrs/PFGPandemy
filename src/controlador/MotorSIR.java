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

import modelo.DCVS;
import modelo.Labels;
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
	
	private final double PTE;													//Probabilidad de transmisión de la enfermedad (sin unidades).
	private final double TVS;													//Tasa vuelta a la susceptibilidad.
	private final double TR;													//Tasa de recuperación o curación.
	private final double DME;													//Duración media de la enfermedad (en días).
	private final boolean IP;													//Inmunidad Permanente.
	private final double DMIP;													//Duración media de la inmunidad permanente.
	private final int NG;														//Número de grupos de población.
	private DCVS matrizC;														//Matriz de contactos (relaciones).
	private HashMap<Integer,Zona> zonas;										//Zonas o grupos de población.
	private Labels labels;

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
	 * @param matrizC Matriz de contactos de la simulación.
	 */
	public MotorSIR(Double pte, double dme, boolean ip, double dmip, HashMap <Integer,Zona> zonas, DCVS matrizC) {
		this.PTE = pte;	
		this.DME = dme;
		this.TR = 1/dme;
		this.IP = ip;
		this.DMIP = dmip;
		this.TVS = 1/dmip;
		this.zonas = zonas;
		this.matrizC = matrizC;
		this.NG = zonas.size();
		this.labels = new Labels();
	}
	
	
	/**
	 * <p>Title: start</p>  
	 * <p>Description: Inicia la realización de los cálculos para cada uno de los
	 * parámetros del modelo.</p> 
	 * @param IT Tiempo inicial de la simulación.
	 * @param FT Tiempo final de la simulación.
	 */
	public void start(int IT, int FT) {
		/* Los dos bucles anidados están en esta colocación en prevención de cálculos
		 * relacionados entre diferentes zonas, puesto que una zona no debe calcular
		 * toda la evolución sin tener en cuenta las demás de las que depende. */
		for(int time = IT; time< FT; time++) {									//Bucle para todas las líneas de tiempo.
			//Cálculo de las prevalencias. Requiere cálculo antes que el resto.
			calcP(time);
			for(int j = 1; j <= NG; j++) {										//Bucle para cada una de las zonas presentes.
				Zona z = zonas.get(j);
				//Obtención de los datos SIR particulares para el instante de tiempo.
				double s = z.getGrafica().getYValue(labels.getWord("S"), time);
				double i = z.getGrafica().getYValue(labels.getWord("I"), time);
				double r = z.getGrafica().getYValue(labels.getWord("R"), time);
				//Cálculo de TC particular.
				double TC = addTCyP(z,time);
				//Cálculo TContagio
				double TContagio = getTContagio(TC,PTE);
				z.addNivel(labels.getWord("TCONTAGIO"), time, TContagio);
				//Cálculo CVS
				double CVS = getCVS(TVS,r);
				z.addNivel(labels.getWord("CVS"), time, CVS);
				//Cálculo CC
				double CC = getCVS(TR,i);
				z.addNivel(labels.getWord("CC"), time, CC);
				//Cálculo CI
				double CI = getCC(TContagio,s);
				z.addNivel(labels.getWord("CI"), time, CI);
				//Cálculo de los siguientes SIR (t+1).
				z.addNivel(labels.getWord("S"), time+1 , CVS - CI );
				z.addNivel(labels.getWord("I"), time+1 , CI - CC );
				z.addNivel(labels.getWord("R"), time+1 , CC - CVS );	
			}
		}
	}
	
	private void calcP(int time) {
		for(int j = 1; j<= NG ;j++) {											//Indice de las columnas.
			Zona z = zonas.get(j);
			double s = z.getGrafica().getYValue(labels.getWord("S"), time);
			double i = z.getGrafica().getYValue(labels.getWord("I"), time);
			double r = z.getGrafica().getYValue(labels.getWord("R"), time);
			double p = getPrevalencia(s,i,r);
			//Almacenar la prevalencia particular calculada.
			z.addNivel(labels.getWord("P"), time, p);
			System.out.println("Prevalencia " + z.getName() + " : "+ p);
		}
	}
	
	private double getCC(double tcontagio, double i) {return tcontagio*i;}
	
	private double getCVS(double tvs, double r) { return tvs*r;} 
	
	private double getTContagio(double tc, double pte) {return tc*pte;}
	
	/**
	 * <p>Title: getTCyP</p>  
	 * <p>Description: Cálcula la Tasa de Contactos de una zona y la almacena
	 * en sus registros correspondientes.</p> 
	 * @param z Zona o grupo de población de estudio.
	 * @param ti Indice de tiempo del cálculo de donde extraer los datos para
	 * el cálculo presente.
	 * @return El Tasa de contagios.
	 */
	private double addTCyP(Zona z, int ti) {
		double sumTC = 0.0;
		int id = z.getID(); 													//Coincide con el indice de la matrizC
		for(int j = 0; j< NG ;j++) {											//Indice de las columnas.
			Zona zAux = zonas.get(j+1);
			/* Estos datos para la prevalencia ¿deben ser del tiempo en cálculo
			 * o los datos anteriores (t-1)? */
			double p = zAux.getGrafica().getYValue(labels.getWord("P"), ti);
			//Almacenar la prevalencia particular calculada.
			zAux.addNivel(labels.getWord("P"), ti, p);
			//Ya cálculada se guarda 
			sumTC += Double.parseDouble((String)matrizC.getValueAt(id-1, j)) * p;
		}
		//Guardar TC.
		z.addNivel(labels.getWord("TC"), ti, sumTC);
		System.out.println("TC " + z.getName() + " : " + sumTC);
		return sumTC;
	}
	
	
	/**
	 * <p>Title: getPrevalencia</p>  
	 * <p>Description: Calcula la prevalencia instantánea </p> 
	 * @param s Número de susceptibles.
	 * @param i Número de infectados o incidentes.
	 * @param r Número de recuperados.
	 * @return La prevalencia instantanea de la enfermedad.
	 */
	private double getPrevalencia( double s, double i, double r) { return i/(s+i+r);}
	
	
	/**
	 * <p>Title: getCI100K</p>  
	 * <p>Description: Calcula los casos incidentes por 100 mil habitantes en el grupo</p> 
	 * @param ci Casos incidentes en el grupo.
	 * @param s Número de susceptibles.
	 * @param i Número de infectados o incidentes.
	 * @param r Número de recuperados.
	 * @return La prevalencia instantanea de la enfermedad.
	 */
	private double getCI100K(double ci, double s, double i, double r) {return 100000*(ci/(s+i+r));}


	
	//Constantes.
	
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

	/* Funciones a modo de pruebas.*/
	
	
	/**
	 * <p>Title: imprimirDatos</p>  
	 * <p>Description: Imprimir los datos iniciales de las pruebas</p> 
	 */
	public void imprimirDatos() {
		//Ver Matriz de contactos.
		System.out.println("Matriz de contactos:\n" + matrizC.toString());
		//ver grupos de población (zonas)
		System.out.println("\nGrupos (" + NG + "):");
		for(int i = 1; i<=NG; i++) {
			System.out.println(zonas.get(i).toString());
		}
		//Imprimir parámetros de la enfermedad:
		System.out.println("\nParámetros de la enfermedad:");
		System.out.println("PTE: " + PTE);
		System.out.println("DME: " + DME);
		System.out.println("IP: " + IP);
		System.out.println("DMIP: " + DMIP);		
		
	}
	
	public void abrirGraficas() {
		zonas.forEach((k,z) -> {
			z.getGrafica().setVisible(true);
		});
	}
	
	/**
	 * <p>Title: main</p>  
	 * <p>Description: Función para las pruebas de implementación e integración. </p>
	 * Inicialmente las pruebas estarán acotadas a 4 grupos de población.
	 * @param args ninguno.
	 */
	public static void main(String[] args) {
		DCVS matrizC = new DCVS();
		//Creación matriz de contactos.
		matrizC.addCabecera(new String[] {"G1","G2","G3","G4"});				//Cabecera
		matrizC.addFila(new String[] {"4","1","1","1"});
		matrizC.addFila(new String[] {"0","4","2","2"});
		matrizC.addFila(new String[] {"0","0","4","3"});
		matrizC.addFila(new String[] {"0","0","0","4"});
		
		HashMap<Integer,Zona> zonas = new HashMap<Integer,Zona>();
		//Creación de 4 zonas con las características de la prueba.
		zonas.put(1,new Zona(1,"G1" , 1000,0 , null));
		zonas.put(2,new Zona(2,"G2" , 500,0 , null));
		zonas.put(3,new Zona(3,"G3" , 200,0 , null));
		zonas.put(4,new Zona(4,"G4" , 100,0 , null));
		//Configurar valores SIR iniciales.
		zonas.get(1).setSIR(999,1,0);
		zonas.get(2).setSIR(500,0,0);
		zonas.get(3).setSIR(200,0,0);
		zonas.get(4).setSIR(100,0,0);
		//PTE,DME,IP,DMIP
		MotorSIR msir = new MotorSIR(0.05, 8, false, 50, zonas, matrizC);
		msir.imprimirDatos();
		msir.start(0, 2);
//		msir.abrirGraficas();
		System.exit(0);
	}
	
}
