/**
 * Controlador de la parte visual encargada de la representación gráfica de las
 * zonas de influencias. En modo mapa.
 */
package controlador;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import modelo.DCVS;
import modelo.IO;
import modelo.ParserPoly;
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
	
	/** LEYENDA referencia una paleta de colores o leyenda*/  
	public final static String LEYENDA = "paleta";
	/** MAPA referencia a el módulo de mapa*/  
	public final static String MAPA = "mapa";
	/** REPRODUCTOR referencia al player o módulo del reproductor*/  
	public final static String REPRODUCTOR = "player";
	//"reproductor", "leyenda", "mapa"
	/** FrameDim Dimensión preferible del marco para todo módulo de la aplicación*/  
	public final static Dimension FrameDim = new Dimension(1024, 768);
	/** PanelCentralDim Dimensión del panel central para estandarizar el aspecto de los diferentes paneles de toda la aplicación */  
	public final static Dimension PanelCentralDim = new Dimension(1024, 768);
	/** MinDim damaño mínimo para todo marco o panel de la aplicación*/  
	public final static Dimension MinDim = new Dimension(800, 600);
	
	/**
	 * Constructor de la clase.
	 * @param w Ancho del mapa.
	 * @param h Alto del mapa.
	 */
	public ControladorMapa(int w, int h) {
		leyenda = new Leyenda(100, 205, false);
		mapa = new Mapa(w,h, leyenda);
		player = new Player(400, 400, true);
		//new ParserSVG();
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
		
		for(int i = 0; i<filas;i++) {
			Zona z= parser(datos.getFila(i));									//Llamar función parser.
			mapa.addZona(z);													//Añadido al resto de zonas del mapa.
		}
	}
	
	/**
	 * <p>Title: setVisibleMapa</p>  
	 * <p>Description: Activa o desactiva la visualización del mapa</p> 
	 * @param ver True para activarla, false en otro caso.
	 */
	public void setVisibleMapa(boolean ver) {mapa.setVisible(ver);}
	
	
	/**
	 * <p>Title: situarVentana</p>  
	 * <p>Description: Posiciona el marco (frame) del módulo incado en la nueva
	 *  posición de la pantalla, este método no hace por si solo visible el marco. </p>
	 * Los valores que puede tomar son: "REPRODUCTOR", "LEYENDA", "MAPA".
	 * @param nombre Nombre identificador del módulo a posicionar.
	 * @param posX Posición X a establacer.
	 * @param posY Posición Y a establecer.
	 */
	public void situarVentana(String nombre, int posX, int posY) {
		switch(nombre) {
		case REPRODUCTOR:
			player.setPosicion(posX, posY);
			break;
		case LEYENDA:
			leyenda.setPosicion(posX, posY);
			break;
		case MAPA:
			mapa.setPosicion(posX, posY);
			break;
		default:
			System.out.println("Valor en el controlador de mapa no establecido: " + nombre);
		}
	}
	
	/**
	 * <p>Title: isPlayable</p>  
	 * <p>Description: Evalua si el reproductor puede ser ejecutado en función
	 *  de disponer de un historico condatos y un conjunto de zonas representables</p>
	 *  Para facilitar su seguimiento, en caso de no cumplirse la idoniedad, mostrará
	 *  el mensaje adecuado de información por consola.
	 * @return True si el reproductor puede ejecutarse, False en otro caso.
	 */
	public boolean isPlayable() {
		boolean OK = historico != null && historico.getRowCount()>0;
		if(OK) {
			OK = mapa.getNumZones() > 0;
			if(OK) {
			}else {System.out.println("No hay cargadas zonas en la representación");}
		}else {System.out.println("No hay cargado un historico");}
		
		return OK;
	}
	
	/**
	 * <p>Title: play</p>  
	 * <p>Description: Ejecuta la reproducción de un historico mostrando gráficamente
	 * la evolución grabada. </p>
	 * La representación gráfica corresponde a las zonas creadas. Para poder
	 * usarse esta función deben haber sido almacezandos previamente los datos
	 * de las zonas y el historico a reproducir.
	 * @also setPoligonos.
	 * @also setPaleta.
	 */
	public void play() {
		if(isPlayable()) {
			player.setPlay(mapa,new DCVS(historico));
			player.setVisible(true);
		}
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
	 * @return Devuelve la zona creada para dicho poligono.
	 */
	private Zona parser(String[] puntos) {
		String nombre = puntos[1];
		int id = Integer.parseInt(puntos[0]);
		int habitantes =  Integer.parseInt(puntos[2]);
		int superficie =  Integer.parseInt(puntos[3]);
		parserPoly.setEscala(2.18);
		return new Zona(id,nombre,habitantes,superficie,parserPoly.parser(puntos));
	}

	/**
	 * <p>Title: crearPaleta</p>  
	 * <p>Description: Lee los datos pasados por un TableModel y los almacena en
	 * la lista de colores en formato Color.</p> 
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
		}
		//leyenda.setPaleta(leyenda.getPaleta());
		leyenda.setPaleta(colores);
	}
	
	/**
	 * <p>Title: getPaleta</p>  
	 * <p>Description: Devuelve la paleta de colores que este configurada 
	 * en el entorno actualmente</p> 
	 * @return JPanel con la paleta de colores.
	 */
	public Leyenda getPaleta() {return leyenda;}
	
	/**
	 * <p>Title: getJMapa</p>  
	 * <p>Description: Obtención de panel contendor del mapa</p> 
	 * @return Panel contenedor del mapa.
	 */
	public JPanel getJMapa() {return mapa.getPanel();}
	
	/**
	 * <p>Title: getMapa</p>  
	 * <p>Description: Devuelve una instacia de la clase Mapa con la configuración
	 * con la que está almacenada</p> 
	 * @return Mapa 
	 */
	public Mapa getMapa() {return mapa;}
	
	/**
	 * <p>Title: getZonas</p>  
	 * <p>Description: Devuelve las instancias de zonas en un HashMap cuya 
	 * clave es el ID (Integer) y el valor es una instancia de la clase Zona</p> 
	 * @return El conjunto de zonas.
	 */
	public HashMap<Integer,Zona> getZonas() {return mapa.getZonas();}

	/**
	 * <p>Title: setZonas</p>  
	 * <p>Description: Establece el conjunto de zonas</p> 
	 * @param zonas Mapa cuya clave es la ID de cada zona y valor la zona.
	 */
	public void setZonas(HashMap<Integer,Zona> zonas) {mapa.setZonas(zonas);}
	
	/**
	 * <p>Title: setModulos</p>  
	 * <p>Description: Establece los módulos que conforman un proyecto.</p> 
	 * @param mapaModulos HasMap con los módulos del proyecto.
	 */
	public void setModulos(HashMap<String, DCVS> mapaModulos) {
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
	
}
