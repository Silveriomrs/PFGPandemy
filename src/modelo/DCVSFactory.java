/**  
* Clase generadora de módulos configurados.
* Usando los conceptos de los patrones Factory Method y Builder, esta clase
*  tiene el objetivo de facilitar la obtención de una instancia de los módulos 
*   DCVS configurados con las etiquetas propias de cada módulo. Facilitando su
*    reutilización y mejorando la legibilidad del código general.  
* <p>Aplication: UNED</p> 
* @author Silverio Manuel Rosales Santana
* @date 2 dic. 2021  
* @version 1.0
*/  
package modelo;

import java.util.HashMap;

/**
* Clase generadora de módulos configurados.
* Usando los conceptos de los patrones Factory Method y Builder, esta clase
*  tiene el objetivo de facilitar la obtención de una instancia de los módulos 
*   DCVS configurados con las etiquetas propias de cada módulo. Facilitando su
*    reutilización y mejorando la legibilidad del código general.
* <p>Para ver los tipos soportados ver \ref modelo#ModuleType </p>
*  @see ModuleType   
 * @author Silverio Manuel Rosales Santana
 * @date 2 dic. 2021
 * @version versión 1.1
 */
public class DCVSFactory {
	
		
	/**
	 * Devuelve una nueva instancia con los atributos del tipo
	 *  de módulo específicado.
	 *  Crea las etiquetas correspondientes a dicho módulo y los atributos característicos.
	 * @param type Tipo de módulo a generar.
	 * @return Una instancia nueva del módulo específicado.
	 * @see TypesFiles
	 */
	public static DCVS newModule(String type) {
		DCVS module = new DCVS();
		module.setType(type);
		setType(module);
		module.setEditLabels(false);
		return module;
	}
	
	/**
	 * @param module Realiza la configuración general de atributos particularizando
	 *  a para el módulo que se especifique.
	 */
	private static void setType(DCVS module) {
		switch(module.getType()) {
		case(TypesFiles.PRJ): setPRJ(module); break;
		case(TypesFiles.MAP): setMAP(module); break;
		case(TypesFiles.DEF): setDEF(module); break;
		case(TypesFiles.PAL): setPAL(module); break;
		default:
			System.out.println("DCVSFactory > type not implemented yet: " + module.getType());
		}
	}
	
	/**
	 * Configura el módulo con las etiquetas y valores como módulo
	 *  de proyecto. 
	 * @param module Modulo a configurar como módulo de proyecto.
	 */
	private static void setPRJ(DCVS module) {
		//Crear cabecera		
		String[] cabecera = {"Tipo","Dato"};
		//Añadir cabecera.
		module.addCabecera(cabecera);
		//Añadir etiquetas generales.
		module.addFila(new String[]{Labels.NAME, null});
		module.addFila(new String[]{Labels.AUTHOR, null});
		module.addFila(new String[]{Labels.DESCRIPTION,null});
		module.addFila(new String[]{Labels.DATE0,null});
		module.addFila(new String[]{Labels.DATE1,null});
		module.addFila(new String[]{Labels.VERSION,"1.0"});
		module.addFila(new String[]{Labels.NG,"0"});
		//Desactivar edición de columna de etiquetas.
	}
	
	/**
	 * Configura el módulo con las etiquetas y valores como módulo
	 *  de de mapa. 
	 * @param module Modulo a configurar como módulo de mapas.
	 */
	private static void setMAP(DCVS module) {
		//Crear cabecera.
		String[] cabecera = {Labels.ID,Labels.NAME,Labels.PEOPLE,
				Labels.AREA,Labels.S,Labels.I,Labels.R,Labels.P,Labels.C100K};
		//Añadir cabecera.
		module.addCabecera(cabecera);
	}
	
	/**
	 * Establece las propiedades para configurar el módulo como un
	 *  módulo de Definición de enfermedad transmisible. 
	 * @param module Módulo al que establecer los atributos.
	 */
	private static void setDEF(DCVS module) {
		//Crear cabecera		
		String[] cabecera = {"Tipo","Dato"};
		//Añadir cabecera.
		module.addCabecera(cabecera);
		//Añadir etiquetas generales.
		module.addFila(new String[]{Labels.PTE, null});
		module.addFila(new String[]{Labels.DME, null});
		module.addFila(new String[]{Labels.DMI, null});
		module.addFila(new String[]{Labels.IP, null});
		module.addFila(new String[]{Labels.IT,null});
		module.addFila(new String[]{Labels.FT,null});
	}
	
	/**
	 * Genera una tabla de Histórico con la cabecera adecuada
	 *  y ajustada al time slot definido por el parámetro FT (final time o tiempo
	 *   final). 
	 * @param FT Tiempo final del histórico. Número de slots (en días) que contendrá
	 *  el histórico.
	 * @return Módulo histórico configurado con las estiquetas (sin datos).
	 */
	public static DCVS newHST(int FT) {
		DCVS module = new DCVS();
		module.setType(TypesFiles.HST);
		//Crear cabecera		
		String[] cabecera = new String[FT + 1];
		cabecera[0] = "TIME";
		
		//Añadir resto de etiquetas de la cabecera (Times Slots en unidades).
		for(int i = 1; i <= FT;i++)	cabecera[i] = "" + (i -1);
		//Añadir cabecera.
		module.addCabecera(cabecera);
		return module;
	}
		
	/**
	 * Establece las propiedades para configurar el módulo como un
	 *  módulo de definición de relaciones, es decir, matriz de contactos. 
	 * @param grupos Grupos de población dentro de un HashMap.
	 * @return Modulo preconfigurado con los nombres de los grupos de población.
	 */
	public static DCVS newREL(HashMap<Integer,Zona> grupos) {
		DCVS module = new DCVS();
		module.setType(TypesFiles.REL);
		
		int NG = grupos.size();													//Restar etiqueta inicial.
		
		String[] fila = new String[NG+1];
		String[] cabecera = new String[NG+1];
		//Crear cabeceras.
		cabecera[0] = "Grupos";													//Primera columna reservada.
		//Crear resto columnas con nombres de grupos.
		for(int i=1; i<=NG;i++) {cabecera[i] = grupos.get(i).getName();}
		module.addCabecera(cabecera);
		
		//Añadir filas
		for(int i = 0; i<NG; i++) {
			fila[0] = cabecera[i+1];											//Dar nombre a la fila 0 del grupo que representa.
			for(int j = 1; j<=NG; j++) {										//Saltamos la columna de indentificadores verticales.
				int contadorCeros = i;											//Indica los ceros que hay que dejar al comienzo de cada fila.
				if(contadorCeros > (j-1))  fila[j] = "0";
				else {
					fila[j] = "" + 0;
				}
				contadorCeros++;
			}
			module.addFila(fila);
		}

		return module;
	}
	
	/**
	 * Establece las propiedades para configurar el módulo como un
	 *  módulo de leyenda o paleta de colores. 
	 * @param module Módulo al que establecer los atributos propios de la paleta de colores.
	 *  Además al ser la paleta por defecto, cargará los valores iniciales.
	 */
	private static void setPAL(DCVS module) {
		//Crear cabecera		
		String[] cabecera = {"Level","Red","Green","Blue"};
		//Añadir cabecera.
		module.addCabecera(cabecera);
		//Añadir etiquetas generales y en este caso los valores por defecto.
		module.addFila(new String[]{"L0","" + 82, "" + 190,"" + 128});
		module.addFila(new String[]{"L1","" + 40, "" + 180,"" + 99});
		module.addFila(new String[]{"L2","" + 174,"" + 214,"" + 241});
		module.addFila(new String[]{"L3","" + 46, "" + 134,"" + 193});
		module.addFila(new String[]{"L4","" + 247,"" + 220,"" + 111});
		module.addFila(new String[]{"L5","" + 243,"" + 156,"" + 18});
		module.addFila(new String[]{"L6","" + 210,"" + 180,"" + 222});
		module.addFila(new String[]{"L7","" + 118,"" + 68, "" + 138});
		module.addFila(new String[]{"L8","" + 230,"" + 126,"" + 34});
		module.addFila(new String[]{"L9","" + 231,"" + 76, "" + 60});
	}
	
}
