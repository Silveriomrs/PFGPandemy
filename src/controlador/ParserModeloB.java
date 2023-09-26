/**  
* <p>Title: ParserModeloA.java</p>  
* <p>Description: Parser para importar datos generados con una herramineta externa
*  cuya distribución de etiquetas sea por filas, esto es, una distribución vertical.</p>   
*  Hereda de la clase \ref ParserModelo 
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 11 nov. 2021  
* @version 2.0  
*/  
package controlador;

import modelo.DCVS;
import modelo.Zona;

/**
 * <p>Description: Parser para importar datos cuyo etiquetado sea vertical</p> 
 * La clase obtiene mediante un sistema de etiquetas todos los datos necesarios
 * de un proyecto y configura los módulos correspondientes con esos datos.
 * Esta clase es usada para lectura de datos, no guarda datos.
 * @author Silverio Manuel Rosales Santana
 * @date 11 nov. 2021
 * @version versión 2.2
 */
public class ParserModeloB extends ParserModelo{


	/**
	 * <p>Description: Inicializar el parser con los datos obtenidos por parámetro.</p>  
	 * @param prjV Conjunto de datos del proyecto VenSim
	 */
	public ParserModeloB(DCVS prjV) {super(prjV);}
		
	/**
	 * <p>Description: Devuelve el valor asociado a una etiqueta dentro de un módulo</p>
	 *  Esta función desacopla la implementación por columnas o filas.
	 *  Particularmente en esta clase la búsqueda es por filas.
	 * @param label Etiqueta a buscar.
	 * @return El valor asociado a la etiqueta, Null en otro caso.
	 */
	@Override
	protected Object getLabelValue(String label) {return dcvs.getDataFromRowLabel(label);}
	
	/**
	 * <p>Description: Función de búsqueda de una etiqueta dentro del módulo.</p>
	 *  Esta función desacopla la función de búsqueda de la implementación por columnas o filas.
	 *  Particularmente en esta clase la búsqueda es por filas.
	 * @param label Etiqueta a buscar.
	 * @return El índice de la posición de dicha etiqueta, -1 en otro caso.
	 */
	@Override
	protected int getIndexLabel(String label) {return dcvs.getFilaItem(label);}
		
	/**
	 * <p>Description: Agrega la información de una serie de datos a la gráfica
	 * correspondiente a una zona.</p> 
	 * @param et Etiqueta u operador correspondiente de la columna.
	 * @param pos Número de fila en la que está situada dicha etiqueta.
	 * @param z Zona a la que añadir los valores correspondientes a la serie indicada en la etiqueta.
	 */
	@Override
	protected void addSerieXs(String et, int pos, Zona z) {
		double valor;															//Almacena cada valor de la serie en lectura.
		boolean correcto = true;												//Finaliza la lectura si hay un valor no válido.
		int contador = IT;
		//Componer la fila que se añadirá a la tabla al final de la adquisición.
		String[] fila = new String[FT + 1];
		fila[0] = et + " " + z.getName();
		// Se recorre para cada tipo de dato todos los registros.
		while(correcto && (contador < FT)) {
			String s = (String) dcvs.getValueAt(pos, contador +1);
			String op = et.split(" ")[0];
			
			if(s != null && !s.equals("") && op != null) {
				try{valor =  Double.parseDouble(s.replace(",", "."));
				z.addNivel(op, contador,valor);
				fila[contador +1] = "" + valor;
				}catch(Exception e) {System.out.println("ParserModeloB > addSerieXs - Error en valor: " + s);}
			}else { correcto = false; }											//Si un valor leído no es válido no continuar con la lectura.
			//Siguiente línea.
			contador++;
		}
		//Añadir el array de valores leídos al módulo.
		mHST.addFila(fila);
	}	
	
	/**
	 * <p>Description: Cuenta el número de filas o columnas dedicados a los intervalos
	 *  de tiempo y devuelve el resultado.</p>
	 * Esta función no comprueba que el formato sea válido.
	 * @return El número de intervalos de tiempo contenidos en el histórico.
	 */
	@Override
	protected int getTimeSlots() {return dcvs.getColumnCount() - 1;}
			
	/**
	 * <p>Description: Devuelve la etiqueta en la posición indicada.</p>
	 * Permite abstraer la búsqueda de etiquetas de la distribución de las etiquetas.
	 * @param index Posición de la etiqueta a obtener.
	 * @return El contenido de la etiqueta en la posición indicada.
	 */
	@Override
	protected String getLabel(int index) {return (String) dcvs.getValueAt(index,0);}
	
	@Override
	protected void setNLabels() {nLabels = dcvs.getRowCount();}
	
}