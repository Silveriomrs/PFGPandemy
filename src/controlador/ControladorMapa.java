/**
 * Controlador de la parte visual encargada de la representación gráfica de las
 * zonas de influencias. En modo mapa.
 */
package controlador;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import modelo.DCVS;
import modelo.IO;
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
	 * @param w Ancho del mapa.
	 * @param h Alto del mapa.
	 */
	public ControladorMapa(int w, int h) {
		leyenda = new Leyenda(100, 205, false);
		mapa = new Mapa(w,h, leyenda);
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
		if(historico != null && historico.getRowCount()>0) {
			if(mapa.getNumZones() > 0) {
				player.setPlay(mapa,leyenda,new DCVS(historico));
				player.setVisible(true);
			}else {System.out.println("No hay cargadas zonas en la representación");}
		}else {System.out.println("No hay cargado un historico");}
	}
	
	/**
	 * <p>Title: setHistorico</p>  
	 * <p>Description: Actualiza el historico de un cálculo realizado, permitiendo
	 * su reproducción.</p> 
	 * @param historico historico con los datos de la evolución calculada.
	 */
	public void setHistorico(DefaultTableModel historico) {	this.historico = historico;	}
	
			
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

	/**
	 * <p>Title: crearPaleta</p>  
	 * <p>Description: </p> 
	 * @param modelo Modelo con los datos de la nueva paleta.
	 */
	public void setPaleta(DefaultTableModel modelo) {
		int filas = modelo.getRowCount();
		ArrayList<Color> colores = new ArrayList<Color>();
		//Leer colores.
		for(int i = 0; i<filas; i++) {
			//Lectura de valores.
			int r = Integer.parseInt((String) modelo.getValueAt(i, 0));
			int g = Integer.parseInt((String) modelo.getValueAt(i, 1));
			int b = Integer.parseInt((String) modelo.getValueAt(i, 2));
			Color c = new Color(r,g,b);
			//establecimiento de la paleta	
			colores.add(c);
			//leyenda.setColor(i, c);
		}
		//leyenda.setPaleta(leyenda.getPaleta());
		leyenda.setPaleta(colores);
	}
	
	/**
	 * <p>Title: getMapa</p>  
	 * <p>Description: Obtención de panel contendor del mapa</p> 
	 * @return Panel contenedor del mapa.
	 */
	public JPanel getMapa() {return mapa.getPanel();}


	/**
	 * <p>Title: setModulos</p>  
	 * <p>Description: </p> 
	 * @param mapaModulos
	 */
	public void setModulos(HashMap<String, DCVS> mapaModulos) {
		// TODO Auto-generated method stub
		mapaModulos.forEach((tipo,modulo) -> {
			if(tipo != IO.PRJ) {												//Evita introducir la ruta del propio archivo de proyecto.
				switch(tipo) {
				case(IO.HST):
					setHistorico( modulo.getModelo());
					break;
				case(IO.PAL):
					setPaleta(modulo.getModelo());
					break;
				case(IO.MAP):
					setPoligonos(modulo.getModelo());
					break;
				}
				
			}
		});
	}
	
	/**
	 * Establece una nueva palera de colores a partir de los componentes RGB.
	 *  Debe contener al menos 10 elementos.
	 * @param dcvs Nueva paleta de colores encapsulada en tipo de datos DCVS..
	 */
	private void setPaletaRGB(DCVS dcvs) {
		ArrayList<Color> paleta = new ArrayList<Color>();
		Color c = null;
		int r,g,b;
		r = g = b = 0;
		int nc = dcvs.getRowCount();
		
		if(nc == 10) {
			for(int i = 0; i<10; i++) {
				for(String linea : dcvs.getFila(i)) {
					String[] rgb = linea.split(",");
					r = Integer.parseInt(rgb[0]);
					g = Integer.parseInt(rgb[1]);
					b = Integer.parseInt(rgb[2]);
					c = new Color(r,g,b);
					paleta.add(c);
				}
			}	
		}
		
		leyenda.setPaleta(paleta);
	}
	
}
