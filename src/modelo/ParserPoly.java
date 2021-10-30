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
	
	private double escala = 2.4;
	
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
		int inicio = 4;															//Saltamos las posiciones del resto de atributos del grupo/zona.
		Polygon pol = new Polygon();
		int size = zona.length - inicio;
		//Lectura y almacenamiento de coordenadas.
		for(int i=inicio; i<size;i++) {												
			//Separación coordenadas.
			String Pxy = zona[i];
			if(!Pxy.equals("")) {												//Si la posición no está vacia, leemos.
				String[] coordenadas = Pxy.split(";");							//Aplicamos separador y obtención de coordenadas.
				//Obtener coordenadas convertidas a int.
				int x = (int) (Double.valueOf(coordenadas[0])/escala);			//Conversión coordenada X
				int y = (int) (Double.valueOf(coordenadas[1])/escala);			//Conversión coordenada Y
				pol.addPoint(x,y);
			}
		}
		return pol;
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
