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
import vista.Player;

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
	/** Player para la reproducción de una simulación */
	private Player player;
	private DefaultTableModel historico;
	
	/**
	 * Constructor de la clase.
	 */
	public ControladorMapa() {
		leyenda = new Leyenda(100, 205, false);
		mapa = new Mapa(850,600, leyenda);
		player = new Player(400, 400, true);
		new ParserSVG();
		parserPoly = new ParserPoly();
	}
	

	/**
	 * <p>Title: setPoligonos</p>  
	 * <p>Description: Establece la representación gráfica de cada zona. Una fila
	 * por zona.</p> 
	 * @param modelo con los datos especificados de cada zona.
	 */
	public void setPoligonos(DefaultTableModel modelo) {
		datos = new DCVS(modelo);
		int filas = datos.getRowCount();										//Número de poligonos a procesar.	
		//Llamar función parser.
		for(int i = 0; i<filas;i++) {parser(datos.getFila(i));}
	}
	
	/**
	 * <p>Title: verMapa</p>  
	 * <p>Description: Activa o desactiva la visualización del mapa</p> 
	 * @param ver True para activarla, false en otro caso.
	 */
	public void verMapa(boolean ver) {mapa.setVisible(ver);}
	
	/**
	 * <p>Title: play</p>  
	 * <p>Description: Ejecuta la reproducción de un historico mostrando gráficamente
	 * la evolución grabada. </p>
	 * La representación gráfica corresponde a las zonas creadas. Para poder
	 * usarse esta función deben haber sido almacezandos previamente los datos
	 * de los poligonos y los de la leyenda.
	 * 
	 *  @also setPoligonos.
	 *  @also setPaleta.
	 */
	public void play() {
		if(mapa.getNumZones() > 0) {
			player.setPlay(mapa,leyenda,new DCVS(historico));
			player.setVisible(true);
		}else {System.out.println("No hay cargadas zonas en la representación");}
	}
	
	/**
	 * <p>Title: setHistorico</p>  
	 * <p>Description: Actualiza el historico de un cálculo realizado, permitiendo
	 * su reproducción.</p> 
	 * @param historico historico con los datos de la evolución calculada.
	 */
	public void setHistorico(DefaultTableModel historico) {	this.historico = historico;	}
	
	
	/**
	 * Establece el nivel de contagio de una zona.
	 * @param id Nombre de la zona.
	 * @param n nivel de color para asignación.
	 */
	public void setNivel(int id, int n) {mapa.setZonaNivel(id, n);}
	
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
		int id = Integer.parseInt(puntos[0]);
		mapa.addZona(new Zona(id,nombre,parserPoly.parser(puntos)));
	}
	
}
