/**
 * 
 */
package modelo;

import java.awt.Polygon;

/**
 * Clase dedicada a convertir los datos recibidos en formato String, en un 
 * poligono.
 * @author Silverio Manuel Rosales Santana
 * @date 16/07/2021
 * @version 1.0
 *
 */
public class ParserPoly {
	
	
	/**
	 * Crear un poligono cerrado a partir de los datos que lo conforman.
	 * Dichos datos estarán organizados en un formato tal que:
	 * 		"846,1435 890,1397 823,1413 846,1435 846,1424 846,1435"
	 * Donde cada tupla separada por espacios, representa las coordenadas de los
	 * puntos que contienen al poligono. (Xi,Yi). Donde en este ejemplo, el primer
	 * punto de este poligono es P(846,1435). 
	 * @param raw cadena de texto con cada uno de los puntos que conforman el poligono.
	 * @return poligono creado a partir de los puntos dados.
	 */
	public Polygon parser(String raw) {		
		String[] raw2= raw.split(" ");
		int size = raw2.length;
		int[] polX = new int[size];
		int[] polY = new int[size];		
		//Lectura y almacenamiento de coordenadas.
		int escala = 3;
		for(int i=0; i<size;i++) {
			//Separación coordenadas.
			String[] raw3= raw2[i].split(",");
			//Obtener coordenadas convertidas a int.
			polX[i] = Integer.valueOf(raw3[0])/escala;
			polY[i] = Integer.valueOf(raw3[1])/escala;
		}
		
		return new Polygon(polX, polY, size);
	}

}
