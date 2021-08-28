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
	
	private double escala = 3.8;
	
	/**
	 * Crear un poligono cerrado a partir de los datos que lo conforman.
	 * Dichos datos estarán organizados en un formato tal que:"
	 * Donde cada tupla separada por comas ',', representa las coordenadas de los
	 * puntos que contienen al poligono. (Xi;Yi).
	 * @param zona Arreglo de Strings con cada uno de los puntos que conforman el poligono
	 * en formato Px;y donde el separador es el simbolo punto y coma ';'.
	 * @return poligono creado a partir de los puntos dados.
	 */
	public Polygon parser(String[] zona) {
		int size = zona.length -2;
		int[] polX = new int[size];
		int[] polY = new int[size];
		/* Problema de size hay muchos campos vacios que luego se quedan almacenados como puntos blancos en el poligono */
		//Lectura y almacenamiento de coordenadas.
		int contador = 0;
		for(int i=0; i<size;i++) {												
			//Separación coordenadas.
			String Pxy = zona[i+2];												//Saltamos las posiciones de ID y nombre.
			if(!Pxy.equals("")) {												//Si la posición no está vacia, leemos.
				contador++;														//Contador de puntos válidos.
				String[] coordenadas = Pxy.split(";");							//Aplicamos separador y obtención de coordenadas.
				//Obtener coordenadas convertidas a int.
				polX[i] = (int) (Double.valueOf(coordenadas[0])/escala);				//Conversión coordenada X
				polY[i] = (int) (Double.valueOf(coordenadas[1])/escala);				//Conversión coordenada Y
			}
		}
		
		//Eliminación de las posiciones vacias.
		//Creación de los arreglos de coordenadas finales.
		int[] polX2 = new int[contador];
		int[] polY2 = new int[contador];
		//Copiado de coordenadas válidas.
		for(int i = 0; i<contador; i++) {
			polX2[i] = polX[i];
			polY2[i] = polY[i];
		}
		
		return new Polygon(polX2, polY2, contador);
	}

	/**
	 * @return El valor de la escala
	 */
	public double getEscala() {	return escala; }

	/**
	 * @param escala El valor de la escala a establecer
	 */
	public void setEscala(double escala) {this.escala = escala;	}

}
