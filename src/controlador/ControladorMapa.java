/**
 * Controlador de la parte visual encargada de la representación gráfica de las
 * zonas de influencias. En modo mapa.
 */
package controlador;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import modelo.DCVS;
import modelo.ParserPoly;
import modelo.ParserSVG;
import modelo.Zona;
import vista.Leyenda;
import vista.Mapa;

/**
 * @author Silverio Manuel Rosales Santana
 * @date 13/07/2021
 * @version 1.0
 *
 */
public class ControladorMapa {

	/** mapa Instancia de un mapa*/  
	private Mapa mapa;
	/** parserPoly Parser para datos en formato coordenadas de poligonos*/  
	private ParserPoly parserPoly;
	/** datos Almacen de datos*/  
	private DCVS datos;
	/** leyenda Leyenda del mapa.*/  
	private Leyenda leyenda;
	
	/**
	 * Constructor de la clase.
	 * @param modelo base con los datos de las figuras.
	 */
	public ControladorMapa(DefaultTableModel modelo) {
		datos = new DCVS(modelo);
		leyenda = new Leyenda(100, 185, false);
		mapa = new Mapa(850,600, leyenda);
		
		new ParserSVG();
		parserPoly = new ParserPoly();
		pinta();
	}
	
	/**
	 * Clase privada con los datos de una configuración de mapa básica por
	 * CCAA de España.
	 */
	private void pinta() {	
		int filas = datos.getRowCount();										//Número de poligonos a procesar.	
		//Llamar función parser.
		for(int i = 0; i<filas;i++) {			
			parser(datos.getFila(i));
		}
		//Prueba de color.
		mapa.setZonaNivel("10", 0);	//Green
		mapa.setZonaNivel("1", 1);	//Green
		mapa.setZonaNivel("2", 2);	//Grey
		mapa.setZonaNivel("3", 3);	//Dark gray
		mapa.setZonaNivel("4", 4);	//Cyan
		mapa.setZonaNivel("5", 5);	//Yellow
		mapa.setZonaNivel("6", 6);	//Pink
		mapa.setZonaNivel("7", 7);	//Orange
		mapa.setZonaNivel("8", 8);	//Red
		mapa.setZonaNivel("9", 9);  //Black
	}
	
	/**
	 * Establece el nivel de contagio de una zona.
	 * @param id Nombre de la zona.
	 * @param n nivel de color para asignación.
	 */
	public void setNivel(String id, int n) {mapa.setZonaNivel(id, n);}
	
	/**
	 * Establece una nueva paleta de colores representativos de los grados de
	 * contagio. Hasta 10 niveles desde 0 a 9.
	 * @param paleta Colores representativos en formato sRGB(r,g,b).
	 */
	public void setPaleta(ArrayList<Color> paleta) {
		if(paleta != null && paleta.size()==10) {
			leyenda.setPaleta(paleta);
		}
	}
			
	/**
	 * Realiza la conversión de datos de texto a un poligono cerrado. En el array
	 * de datos debe estar conforme a ID, Nombre, Px;y... donde Px;y son las
	 * coordenadas (X;Y) de cada punto, con separador ';'.
	 * @param puntos String con las coordenadas de los puntos componentes del
	 * debe estar conforme al formato ID,Nombre,P0x;y,P1x;y... del poligono. Es
	 * decir, la posición 0 reservada al ID, la posición 1 reservada al nombre,
	 * las siguientes posiciones representan cada uno de los puntos que conforman
	 * el poligono.
	 */
	private void parser(String[] puntos) {	
		String nombre = puntos[1];
		String id = puntos[0];
		mapa.addZona(new Zona(id,nombre,parserPoly.parser(puntos)));
	}
	
}
