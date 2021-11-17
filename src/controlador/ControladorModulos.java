/**
 * Controlador de la parte visual encargada de la representación gráfica de las
 * zonas de influencias. En modo mapa.
 */
package controlador;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import modelo.DCVS;
import modelo.IO;
import modelo.ParserHistoricoVS;
import modelo.ParserPoly;
import modelo.ParserProyectoVS;
import modelo.Zona;
import vista.About;
import vista.Archivos;
import vista.Leyenda;
import vista.Mapa;
import vista.ParametrosGrupos;
import vista.ParametrosProyecto;
import vista.Pizarra;
import vista.Player;
import vista.Principal;
import vista.TablaEditor;

/**
 * @author Silverio Manuel Rosales Santana
 * @date 13/07/2021
 * @version 1.0
 *
 */
public class ControladorModulos {

	
	private Leyenda leyenda;
	private ControladorDatosIO cio;
	//Vistas
	private About about;
	private Principal principal;
	private Player player;
	private Mapa mapa;
	private Archivos archivos;
	private TablaEditor tablaEditor;
	private ParametrosGrupos pgrupos;
	private ParametrosProyecto pproyecto;
	
	//
	private DefaultTableModel historico;
	private ParserPoly parserPoly;
	
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
	
	private int w = 1024;
	private int h = 768;
	private String panelActivo = "None";
	
	
	/**
	 * Constructor de la clase.
	 */
	public ControladorModulos() {
		cio = new ControladorDatosIO();
		archivos = new Archivos(this);
		tablaEditor = new TablaEditor(this);
		pgrupos = new ParametrosGrupos(10);
		pproyecto = new ParametrosProyecto(archivos);
		about = new About();
		leyenda = new Leyenda(100, 205, false);
		mapa = new Mapa(w,h, leyenda);
		principal = new Principal(this);
		player = new Player(400, 400, true);
		parserPoly = new ParserPoly();
		parserPoly.setEscala(2.18);
		agregarPaneles();
	}
	
	private void agregarPaneles() {
		//Añadir paneles de los módulos.
		principal.addPanelToView(tablaEditor);
		principal.addPanelToView(mapa);
		principal.addPanelToView(pgrupos);
		principal.addPanelToView(pproyecto);
		mostrarPanel(panelActivo);
	}
	

	/**
	 * <p>Title: setPoligonos</p>  
	 * <p>Description: Establece la representación gráfica de cada zona. Una fila
	 * por zona.</p> 
	 * @param datos DCVS con los datos especificados de cada zona.
	 */
	public void setPoligonos(DCVS datos) {
		int filas = datos.getRowCount();										//Número de poligonos a procesar.
		for(int i = 0; i<filas;i++) {
			Zona z = parser(datos.getFila(i));									//Llamar función parser.
			if(z != null) mapa.addZona(z);										//Añadido al resto de zonas del mapa.
		}
	}
	
	/**
	 * <p>Title: setVisibleMapa</p>  
	 * <p>Description: Activa o desactiva la visualización del mapa</p> 
	 * @param ver True para activarla, false en otro caso.
	 */
	private void setMapaVisible(boolean ver) {mapa.setVisible(ver);}
	
	
	/**
	 * <p>Title: situarVentana</p>  
	 * <p>Description: Posiciona el marco (frame) del módulo incado en la nueva
	 *  posición de la pantalla, este método no hace por si solo visible el marco. </p>
	 * Los valores que puede tomar son: "REPRODUCTOR", "LEYENDA", "MAPA".
	 * @param nombre Nombre identificador del módulo a posicionar.
	 * @param posX Posición X a establacer.
	 * @param posY Posición Y a establecer.
	 */
	private void situarVentana(String nombre, int posX, int posY) {
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
			if(!OK) {System.out.println("No hay cargadas zonas en la representación");}
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
	private void play() {
		if(isPlayable()) {
			DCVS h = new DCVS();
			h.setModelo(historico);
			player.setPlay(mapa,h);
			player.setVisible(true);
		}
	}
	
	/**
	 * <p>Title: setHistorico</p>  
	 * <p>Description: Actualiza el historico de un cálculo realizado, permitiendo
	 * su reproducción.</p> 
	 * @param historico Historico con los datos de la evolución calculada.
	 */
	public void setHistorico(DCVS historico) {	this.historico = historico.getModelo();	}
	
			
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
	 * <p>Description: Establece el conjunto de zonas o grupos de población.</p>
	 * Actualiza las diferentes vistas vinculadas con los grupos de población obtenidos. 
	 * @param zonas Mapa cuya clave es la ID de cada zona y valor la zona.
	 */
	public void setZonas(HashMap<Integer,Zona> zonas) {
		//Actualiza el mapa.
		mapa.setZonas(zonas);
		//Actualizar Grupos.
		pgrupos.setZonas(zonas);
		
	}
	
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
					setHistorico(modulo);
					break;
				case(IO.PAL):
					setPaleta(modulo.getModelo());
					break;
				case(IO.MAP):
					setPoligonos(modulo);
					break;
				}
			}
		});
	}
	
	/* Control de los módulos */
	
	/**
	 * <p>Title: doAction</p>  
	 * <p>Description: Realiza la acción concreta indicada desde la vista
	 * que ha realizado la llamada.</p> 
	 * @param nombre Nombre de la acción.
	 */
	public void doAction(String nombre) {
		//
		switch(nombre){
			case "Reproductor":
				//Pasar mapa de módulos al controlar de mapa.
				if(isPlayable()) {
					play();
					situarVentana(ControladorModulos.REPRODUCTOR, principal.getX() - 350, principal.getY() + h/3);
					mostrarPanel("Mapa");
				}
				break;
			case "Editor Gráfico":		
			new Pizarra(mapa.getZonas());
				//pizarra.toogleVisible();
				break;
			case "Mapa":
				//getMapa().verFrame(true);
				mostrarPanel(nombre);
				break;
			case "Paleta":
				situarVentana(LEYENDA, principal.getX() + w + 10, principal.getY());
				getPaleta().toggleVisible();
				break;
			case "Grupos":
				pgrupos.setZonas(getZonas());
			case "Tabla":
//			case "Archivos":
			case "Proyecto":
				mostrarPanel(nombre);
				break;
			case "Abrir Proyecto":
				DCVS prj = cio.abrirArchivo(null,IO.PRJ);
				if(prj != null) {
					archivos.abrirProyecto(prj);
					pproyecto.setDCVS(prj);		
				}					
				break;
			case "Importar Proyecto Vensim":
				DCVS pVS = cio.abrirArchivo(null,IO.CSV);
				//Requiere nuevo parser completo.
				if(pVS != null && pVS.getValueAt(0,0).equals("0")) importarProyectoVS(pVS);
				else if(pVS != null) mostrar("Archivo seleccionado no reconocido.",0);
				break;
			case "Importar Histórico Vensim":
				//Hay que conocer la extensión que usa VenSim en sus proyectos. Temporalmente usar CSV
				DCVS hVS = cio.abrirArchivo(null,IO.CSV);
				//Requiere nuevo parser completo.
				if(hVS != null && hVS.getColumnName(1).equals("0")) importarHistoricoVS(hVS);
				else if(hVS != null) mostrar("Archivo seleccionado no reconocido.",0);
				break;		
			case "Nuevo Proyecto":
				ParametrosProyecto.main(null);
				break;
			case "Salir":
				if(mostrar("¿Desea salir del programa?",3) == JOptionPane.YES_OPTION) System.exit(0);
				break;
			case "Acerca de...":
				about.toggleVisible();
				break;
			default:
				System.out.println(nombre + ", tipo no reconocido");
		}
		
		mostrarPanel(nombre);
	}
	
	private void mostrarPanel(String nombre) {		
		switch(nombre){
		case "Grupos":
			pgrupos.setZonas(getZonas());
		case "Mapa":
		case "Tabla":
//		case "Archivos":
		case "Proyecto":
		case "NONE":
			//Mostrar panel correspondiente y ocultación del resto.
			setMapaVisible(nombre.equals("Mapa"));							
			tablaEditor.setVisible(nombre.equals("Tabla"));
			pgrupos.setVisible(nombre.equals("Grupos"));
			pproyecto.setVisible(nombre.equals("Proyecto"));
		}
	}
	
	/**
	 * Función auxiliar. Muestra cuadros de mensajes. Los cuadros de mensajes
	 * no están enlazados con un hilo padre (null). Un número no definido se
	 * mostrará como información.
	 *
	 * El tipo 4 es usado para el botón "Acerca de...", re-escrito para mostrar un
	 * mensaje tipo 1.
	 * @param txt Texto a mostrar.
	 * @param tipo Es el tipo de cuadro de mensaje. Siendo:
	 *  0 showMessageDialog ERROR_MESSAGE
	 *  1 showMessageDialog INFORMATION_MESSAGE
	 *  2 showMessageDialog WARNING_MESSAGE
	 *  3 showConfirmDialog YES_NO_OPTION
	 *  4 showMessageDialog PLAIN_MESSAGE
	 * @return En caso de un mensaje de confirmación devuelve el valor Integer
	 * de la opción escogida.
	 */
	private Integer mostrar(String txt, int tipo ) {
		String titulo = "";
		Integer opcion = null;
		
		switch(tipo) {
		case 0: titulo = "Error"; break;
		case 1: titulo = "Información"; break;
		case 2: titulo = "¡Antención!"; break;
		case 3: titulo = "Consulta";
			opcion = JOptionPane.showConfirmDialog(null, txt, titulo, JOptionPane.YES_NO_OPTION);
		break;
		case 4: titulo = "Acerca de..."; tipo = 1; break;
		default:
			titulo = "";
		}
		
		if(tipo != 3) JOptionPane.showMessageDialog(null, txt, titulo, tipo);
		
		return opcion;
	}
	

	
	/* En progreso de implementación */
	
	private void setDatosProyecto(DCVS dcvs, int NG) {
		dcvs.addFila(new String[] {"NG","" + NG});
		pproyecto.setDCVS(dcvs);
		
	}
	
	/**
	 * <p>Title: importarProyectoVS</p>  
	 * <p>Description: Importa los datos de un archivo de proyecto generado con VenSim.</p>
	 * Adquiere por tanto todos los valores de dicho proyecto disponibles, tales como la
	 * matriz de contactos, R,S,I, tasas, etcetera.
	 * Esta opción elimina el resto de datos actuales de los módulos implicados.
	 * @param prjV Conjunto de datos del archivo de salida Vensim.
	 */
	private void importarProyectoVS(DCVS prjV) {
		ParserProyectoVS parser = new ParserProyectoVS(prjV);
		//Establecer las zonas en las vistas correspondientes.
		setZonas(parser.getZonas());
		//Establecer los datos del proyecto en la vista.
		setDatosProyecto(prjV,parser.getNG());
		//Establecer el historico de niveles.
		
		//Establecer matriz de relaciones.
		tablaEditor.setModelo(parser.getMContactos());
	}
	
	/**
	 * <p>Title: importarHistoricoVS</p>  
	 * <p>Description: Importa los datos de un archivo de salida (histórico) generado con VenSim.</p>
	 * Adquiere por tanto todos los valores de dicho proyecto disponibles, R,S,I, tasas, etcetera,
	 * siempre que estén contenidos.
	 * Esta opción elimina el resto de datos actuales de los módulos implicados.
	 * @param prjV Conjunto de datos del archivo de salida Vensim.
	 */
	private void importarHistoricoVS(DCVS prjV) {
		ParserHistoricoVS parser = new ParserHistoricoVS(prjV);
		//Establecer las zonas en las vistas correspondientes.
		setZonas(parser.getZonas());
		//Establecer los datos del proyecto en la vista.
		setDatosProyecto(prjV,parser.getNG());
		//Establecer el historico de niveles.
		
		//Establecer matriz de relaciones.
		tablaEditor.setModelo(parser.getMContactos());
		
	}
	
	/* Funciones para pruebas */
	/**
	 * Método main de la clase.
	 * @param args no usado.
	 */
	public static void main(String[] args) {
		ControladorModulos cm = new ControladorModulos();
	}
}
