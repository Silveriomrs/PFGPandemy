/**  
* Motor para el cálculo de los diferentes valores evolutivos de
* una enfermedad. Dichos cálculos están basados en el Módelo SIR.
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 13 nov. 2021  
* @version 1.0  
*/  
package controlador;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import modelo.DCVS;
import modelo.DCVSFactory;
import modelo.Labels;
import modelo.Zona;

/**
 * Motor para el cálculo de los diferentes valores evolutivos de una enfermedad.
 * Dichos cálculos están basados en el Módelo SIR. 
 * @author Silverio Manuel Rosales Santana
 * @date 13 nov. 2021
 * @version versión 1.0
 */
public class MotorSIR {
	
	private double PTE;													//Probabilidad de transmisión de la enfermedad (sin unidades).
	private double TVS;													//Tasa vuelta a la susceptibilidad.
	private double TR;													//Tasa de recuperación o curación.
	private double DME;													//Duración media de la enfermedad (en días).
	private boolean IP;													//Inmunidad Permanente.
	private double DMI;													//Duración media de la inmunidad permanente.
	private int NG;														//Número de grupos de población.
	private DCVS matrizC;												//Matriz de contactos (relaciones).
	private HashMap<Integer,Zona> zonas;								//Zonas o grupos de población.
	private DCVS mHST;													//Módulo histórico a obtener.
	private int FT;
	private int IT;

	/**
	 * Constructor que requiere los parámetros obligados para la realización de los 
	 *  diferentes cálculos de propagación de la enfermedad bajo este modelo.  
	 * @param mDEF Módulo con la definición de las propiedades de la enfermedad.
	 * @param zonas Conjunto de zonas representantes de los grupos de estudio.
	 * @param matrizC Matriz de contactos de la simulación.
	 */
	public MotorSIR(DCVS mDEF, HashMap <Integer,Zona> zonas, DCVS matrizC) {
		new Labels();
		if(mDEF != null) {
			this.PTE = Double.parseDouble( (String) mDEF.getDataFromRowLabel(Labels.PTE));	
			this.DME = Double.parseDouble( (String) mDEF.getDataFromRowLabel(Labels.DME));

			this.TR = 1/DME;
			this.IP = Boolean.parseBoolean( (String) mDEF.getDataFromRowLabel(Labels.IP));
			//En caso de haber Inmunidad permanente, se ignora el parámetro DMI.
			if(IP) {DMI = 1;}
			else this.DMI = Double.parseDouble( (String) mDEF.getDataFromRowLabel(Labels.DMI));
			this.IT = Integer.parseInt( (String) mDEF.getDataFromRowLabel(Labels.IT));
		    this.FT = Integer.parseInt( (String) mDEF.getDataFromRowLabel(Labels.FT));
			this.TVS = 1/DMI;
		}
		this.zonas = zonas;
		this.matrizC = matrizC;
		this.NG = zonas.size();
		start();
	}	

	/**
	 * Realiza lectura inicial de los valores SIR y almacena los resultados en la tabla.
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
	 * Añade las etiquetas indicadas para cada grupo de población.
	 * @param label Etiqueta
	 * @see Labels 
	 */
	private void addLabels(String label) {
		for(int i = 1; i<=NG; i++) {
			mHST.addFila(new String[]{label + " " + zonas.get(i).getName(),null});
		}
	}

	/**
	 * Configura las etiquetas iniciales de la tabla histórico y
	 *  realiza una lectura inicial de los valores SIR.
	 */
	private void setUpHST() {
		this.mHST = DCVSFactory.newHST(FT);
		generateTimeStamps();
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
	 * Genera tiempos en formato fecha, añadiendolos a la cabecera del histórico.
	 * Esta función usa la fecha actual.
	 */
 	private void generateTimeStamps() {
		int contador = IT;
		//Establecer el día de hoy como día inicial.
		Date hoy = new Date();
		//crear array con la secuencia de fechas en incrementos de 1 día.
		while(contador < FT) {
			//Añadimos el tiempo en el formato deseado.
			String date =  new SimpleDateFormat("dd/MM/yyyy hh:mm").format(hoy);
			//Renombrar la columna con la fecha.
			mHST.setColumnName(contador + 1, date);
			//Incrementar un día.
			hoy = addDay(hoy);
			//Siguiente línea.
			contador++;
		}
	}
	
	/**
	 * <p>Añade un día a la fecha en curso.</p>
	 * Usada para dar un formato de fecha a las unidades tiempo. 
	 * @param dt Fecha a la que agregar un día.
	 * @return Fecha actualizada.
	 */
	private Date addDay(Date dt) {
		 Calendar c = Calendar.getInstance();
	        c.setTime(dt);
	        c.add(Calendar.DATE, 1);
	        return c.getTime();
	}
	
	/**
	 * Inicia la realización de los cálculos para cada uno de los parámetros del modelo.
	 */
	public void start() {
		//Introducir datos de partida en la tabla.
		setUpHST();
		//Bucle para todas las líneas de tiempo.
		for(int time = 1; time <= FT; time++) {
			//Cálculo de las prevalencias. Requiere cálculo antes que el resto.
			//Cálculo P
			calcP(time);
			//Bucle para cada una de las zonas presentes.
			for(int j = 1; j <= NG; j++) {
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
				index = mHST.getFilaItem(Labels.TC + " " + name);
				mHST.setValueAt("" + TC, index, time);
				
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
					vs += CVS - CI;
					mHST.setValueAt("" + vs, index, time + 1);
					//valor Ij.
					index = mHST.getFilaItem(Labels.I + " " + name);
					vi += CI - CC;
					mHST.setValueAt("" + vi, index, time + 1);
					//valor Rj.
					index = mHST.getFilaItem(Labels.R + " " + name);
					vr += CC - CVS;
					mHST.setValueAt("" + vr, index, time + 1);
				}
			}
		}
	}
	
	/**
	 * Cálcula la prevalencia para un tiempo determinado. 
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
	 * Cálcula la Tasa de Contactos de una zona y la almacena en sus registros correspondientes. 
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
			//Obtener prevalencia actual de cada elemento.
			int index = mHST.getFilaItem(Labels.P + " " + name2);
			double p = Double.parseDouble((String) mHST.getValueAt(index, time));
			//Ya cálculada añadir al sumatorio.
			sumTC += (Double.parseDouble( (String) matrizC.getValueAt(fila, j))) * p;
		}

		return sumTC;
	}
		
	/**
	 * Calcula la prevalencia instantánea. 
	 * @param s Número de susceptibles.
	 * @param i Número de infectados o incidentes.
	 * @param r Número de recuperados.
	 * @return La prevalencia instantanea de la enfermedad.
	 */
	private double getPrevalencia( double s, double i, double r) { return i/(s+i+r);}
		
	/**
	 * Calcula los casos incidentes por 100 mil habitantes en el grupo. 
	 * @param ci Casos incidentes en el grupo.
	 * @param s Número de susceptibles.
	 * @param i Número de infectados o incidentes.
	 * @param r Número de recuperados.
	 * @return La prevalencia instantanea de la enfermedad.
	 */
	private double getCI100K(double ci, double s, double i, double r) {return 100000*(ci/(s+i+r));}

	/**
	 * Devuelve el histórico calculado con los parámetros de entrada. 
	 * @return Módulo histórico en formato de tabla.
	 */
	public DCVS getHST() {return mHST;}

	/**
	 * @return El conjunto de grupos de población.
	 */
	public HashMap<Integer,Zona> getZonas() {return zonas;}
	
	/**
	 * Devuelve una zona indicada por su identificador.
	 * @param ID Identificador de la zona o grupo de población.
	 * @return Zona o grupo de población. Null en otro caso.
	 */
	public Zona getZona(int ID) {
		Zona z = null;
		if(zonas.containsKey(ID)) z = zonas.get(ID);
		return z;
	}

}
