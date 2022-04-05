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
import modelo.DCVSFactory;
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
	private final double DMI;													//Duración media de la inmunidad permanente.
	private final int NG;														//Número de grupos de población.
	private DCVS matrizC;														//Matriz de contactos (relaciones).
	private HashMap<Integer,Zona> zonas;										//Zonas o grupos de población.
	private DCVS mHST;															//Módulo histórico a obtener.
	private int FT;
	private int IT;

	/**
	 * <p>Title: </p>  
	 * <p>Description: Constructor que requiere los parámetros obligados
	 * para la realización de los diferentes cálculos de propagación de la enfermedad
	 * bajo este modelo. </p>  
	 * @param pte Probabilidad de transmisión de la enfermedad (sin unidades).
	 * @param dme Duración media de la enfermedad (en días).
	 * @param ip Inmunidad Permanente.
	 * @param dmi Duración media de la inmunidad permanente (en días).
	 * @param zonas Conjunto de zonas representantes de los grupos de estudio.
	 * @param matrizC Matriz de contactos de la simulación.
	 */
	public MotorSIR(Double pte, double dme, boolean ip, double dmi, HashMap <Integer,Zona> zonas, DCVS matrizC) {
		new Labels();
		this.PTE = pte;	
		this.DME = dme;
		this.TR = 1/dme;
		this.IP = ip;
		//En caso de haber Inmunidad permanente, se ignora el parámetro DMI.
		if(IP) {this.DMI = 1;}
		else this.DMI = dmi;
		
		this.TVS = 1/this.DMI;
		this.zonas = zonas;
		this.matrizC = matrizC;
		this.NG = zonas.size();
	}
	
	
	/**
	 * <p>Title: readXs</p>  
	 * <p>Description: Realiza lectura inicial de los valores SIR</p>
	 * Almacena resultados en la tabla.
	 * @param label Etiqueta S,R o I.
	 */
	private void readXs(String label) {
		for(int i = 1; i<= zonas.size(); i++) {
			Zona z = zonas.get(i);
			String[] fila = {label + " " + z.getName(),null};
			//Obtención de los datos SIR particulares para el instante de tiempo.
			switch(label) {
			case(Labels.S): fila[1] = "" + z.getS(); break;
			case(Labels.I): fila[1] = "" + z.getI(); break;
			case(Labels.R): fila[1] = "" + z.getR(); break;
			}
			mHST.addFila(fila);
		}
	}
	
	/**
	 * <p>Title: addLabels</p>  
	 * <p>Description: Añade las etiquetas indicadas para cada grupo de población.</p> 
	 * @param label Etiqueta
	 * @see Labels 
	 */
	private void addLabels(String label) {
		for(int i = 1; i<=NG; i++) {
			mHST.addFila(new String[]{label + " " + zonas.get(i).getName(),null});
		}
	}

	
	private void setUpHST() {
		this.mHST = DCVSFactory.newHST(FT +1);
		//Leer datos iniciales.
		readXs(Labels.S);
		readXs(Labels.I);
		readXs(Labels.R);
		//
		addLabels(Labels.P);
		addLabels(Labels.TC);
		addLabels(Labels.TCONTAGIO);
		addLabels(Labels.CVS);
		addLabels(Labels.CC);
		addLabels(Labels.CI);
		addLabels(Labels.C100K);
	}

	
	/**
	 * <p>Title: start</p>  
	 * <p>Description: Inicia la realización de los cálculos para cada uno de los
	 * parámetros del modelo.</p> 
	 * @param IT Tiempo inicial de la simulación.
	 * @param FT Tiempo final de la simulación.
	 */
	public void start(int IT, int FT) {
		//Introducir datos de partida en la tabla.
		this.IT = IT;
		this.FT = FT;
		setUpHST();
		
		for(int time = 1; time <= FT; time++) {									//Bucle para todas las líneas de tiempo.
			//Cálculo de las prevalencias. Requiere cálculo antes que el resto.
			//Cálculo P
			calcP(time);
			
			for(int j = 1; j <= NG; j++) {										//Bucle para cada una de las zonas presentes.
				Zona z = zonas.get(j);
				String name = z.getName();	
				
				//Obtención de los datos SIR particulares para el instante de tiempo.
				//valor Sj.
				int index = mHST.getFilaItem(Labels.S + " " + name);
				double vs = Double.parseDouble((String) mHST.getValueAt(index, time));
				//valor Ij.
				index = mHST.getFilaItem(Labels.I + " " + name);
				double vi = Double.parseDouble((String) mHST.getValueAt(index, time));
				//valor Rj.
				index = mHST.getFilaItem(Labels.R + " " + name);
				double vr = Double.parseDouble((String) mHST.getValueAt(index, time));
				//Prevalencias cargadas antes de entrar a este bucle.
				
				//Cálculo de TC particular.
				double TC = getTC(z,time);
				int row = mHST.getFilaItem(Labels.TC + " " + name);
				mHST.setValueAt("" + TC, row, time);
				
				//Cálculo TContagio
				double TContagio = TC*PTE;
				index = mHST.getFilaItem(Labels.TCONTAGIO + " " + name);
				mHST.setValueAt("" + TContagio, index, time);
				
				//Cálculo CVS
				double CVS = TVS*vr;
				index = mHST.getFilaItem(Labels.CVS + " " + name);
				mHST.setValueAt("" + CVS, index, time);
	
				//Cálculo CC
				double CC = vi*TR;
				index = mHST.getFilaItem(Labels.CC + " " + name);
				mHST.setValueAt("" + CC, index, time);	
				
				//Cálculo CI
				double CI = TContagio*vs;
				index = mHST.getFilaItem(Labels.CI + " " + name);
				mHST.setValueAt("" + CI, index, time);
				
				//Cálculo del nivel (casos por cada 100 mil habitantes)
				double C100K = getCI100K(CI, vs, vi, vr);
				index = mHST.getFilaItem(Labels.C100K + " " + name);
				mHST.setValueAt("" + C100K, index, time);
				
				//Cálculo de los siguientes SIR (t+1).
				//valor Sj.
				if(time < FT) {
					index = mHST.getFilaItem(Labels.S + " " + name);
					vs = vs + CVS - CI;
					mHST.setValueAt("" + vs, index, time + 1);
					//valor Ij.
					index = mHST.getFilaItem(Labels.I + " " + name);
					vi = vi + CI - CC;
					mHST.setValueAt("" + vi, index, time + 1);
					//valor Rj.
					index = mHST.getFilaItem(Labels.R + " " + name);
					vr = vr +CC - CVS;
					mHST.setValueAt("" + vr, index, time + 1);
				}
			}
		}
		
		System.out.println("\nConfigurado para obtener Histórico de 10 posiciones\n=======================================\n");
		System.out.println( mHST.toString() );
		System.out.println( "\n===============================================\n");
	}
	
	/**
	 * <p>Title: calcP</p>  
	 * <p>Description: Cálcula la prevalencia para un tiempo determinado.</p> 
	 * @param time Slot de tiempo a usar para el cálculo.
	 */
	private void calcP(int time) {
		for(int j = 1; j<= NG; j++) {
			String name = zonas.get(j).getName();
			//valor Sj.
			int index = mHST.getFilaItem(Labels.S + " " + name);
			double vs = Double.parseDouble((String) mHST.getValueAt(index, time));
			//valor Ij.
			index = mHST.getFilaItem(Labels.I + " " + name);
			double vi = Double.parseDouble((String) mHST.getValueAt(index, time));
			//valor Rj.
			index = mHST.getFilaItem(Labels.R + " " + name);
			double vr = Double.parseDouble((String) mHST.getValueAt(index, time));
			
			//Guardar
			index = mHST.getFilaItem(Labels.P + " " + name);
			String valor = "" + getPrevalencia(vs,vi,vr);
			mHST.setValueAt(valor, index, time);
		}
	}
	
	/**
	 * <p>Title: getTC</p>  
	 * <p>Description: Cálcula la Tasa de Contactos de una zona y la almacena
	 * en sus registros correspondientes.</p> 
	 * @param z Zona o grupo de población de estudio.
	 * @param time Indice de tiempo del cálculo de donde extraer los datos para
	 * el cálculo presente.
	 * @return El Tasa de contagios.
	 */
	private double getTC(Zona z, int time) {
		double sumTC = 0.0;
		int fila = z.getID() -1;												//Coincide con el indice de la matrizC
		
		for(int j = 1; j<= NG ;j++) {											//Indice de las columnas.																			
			String name2 = zonas.get(j).getName();
			//Obtener prevalencia actual de cada elemento..
			int index = mHST.getFilaItem(Labels.P + " " + name2);
			double p = Double.parseDouble((String) mHST.getValueAt(index, time));
			//Ya cálculada añadir al sumatorio.
			sumTC += Double.parseDouble((String)matrizC.getValueAt(fila, j)) * p;
		}

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


	/**
	 * <p>Title: getHST</p>  
	 * <p>Description: Devuelve el histórico calculado con los parámetros de entrada</p> 
	 * @return Módulo histórico en formato de tabla.
	 */
	public DCVS getHST() {return mHST;}



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
		for(int i = 1; i<=NG; i++) System.out.println(zonas.get(i).toString());
		//Imprimir parámetros de la enfermedad:
		System.out.println("\nParámetros de la enfermedad:");
		System.out.println("PTE: " + PTE);
		System.out.println("DME: " + DME);
		System.out.println("IP: " + IP);
		System.out.println("DMI: " + DMI);
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
		matrizC.addCabecera(new String[] {"Grupos","G1","G2","G3","G4"});
		matrizC.addFila(new String[] {"G1","4","1","1","1"});
		matrizC.addFila(new String[] {"G2","1","4","2","2"});
		matrizC.addFila(new String[] {"G3","1","2","4","3"});
		matrizC.addFila(new String[] {"G4","1","2","3","4"});
		
		HashMap<Integer,Zona> zonas = new HashMap<Integer,Zona>();
		//Creación de 4 zonas con las características de la prueba.
		zonas.put(1,new Zona(1,"G1" , 1000,0 ,999,1,0,0,0, null));
		zonas.put(2,new Zona(2,"G2" , 500,0 ,500,0,0,0,0, null));
		zonas.put(3,new Zona(3,"G3" , 200,0 ,200,0,0,0,0, null));
		zonas.put(4,new Zona(4,"G4" , 100,0 ,100,0,0,0,0, null));
		
		//PTE,DME,IP,DMI
		MotorSIR msir = new MotorSIR(0.05, 8, false, 50, zonas, matrizC);
//		msir.imprimirDatos();
		msir.start(0, 10);
		System.exit(0);
	}
	
}
