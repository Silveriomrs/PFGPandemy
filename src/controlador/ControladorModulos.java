/**
 * Controlador de la parte visual encargada de la representación gráfica de las
 * zonas de influencias. En modo mapa.
 */
package controlador;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import modelo.DCVS;
import modelo.Labels;
import modelo.ParserHistoricoVS;
import modelo.ParserPoly;
import modelo.ParserProyectoVS;
import modelo.Types;
import modelo.Zona;
import vista.About;
import vista.Archivos;
import vista.Paleta;
import vista.Mapa;
import vista.ParametrosGrupos;
import vista.ParametrosProyecto;
import vista.Pizarra;
import vista.Player;
import vista.Principal;
import vista.TablaEditor;
import vista.VistaSIR;

/**
 * @author Silverio Manuel Rosales Santana
 * @date 13/07/2021
 * @version 2.4
 *
 */
public class ControladorModulos {

	
	private Paleta paleta;
	private ControladorDatosIO cio;
	//Vistas
	private About about;
	private Principal principal;
	private Player player;
	private Mapa mapa;
	private Archivos archivos;
	private VistaSIR vistaSIR;
	private TablaEditor tablaEditor;
	private ParametrosGrupos pgrupos;
	private ParametrosProyecto pproyecto;
	private Pizarra pizarra;
	private HashMap<String,DCVS> modulos;										//Conexión con la parte del modelo. Almacena todos los datos de cada modelo.
	private HashMap<Integer,Zona> zonas;
	//
	private DefaultTableModel historico;
	private ParserPoly parserPoly;
	//Obtener dimensiones de la pantalla para controlar donde aparecen los módulos.
	Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();

	
	/** LEYENDA referencia una paleta de colores o paleta*/  
	public final static String LEYENDA = "paleta";
	/** MAPA referencia a el módulo de mapa*/  
	public final static String MAPA = "mapa";
	/** REPRODUCTOR referencia al player o módulo del reproductor*/  
	public final static String REPRODUCTOR = "player";
	//"reproductor", "paleta", "mapa"
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
		//Inicio del mapa de módulos.
		modulos = new HashMap<String, DCVS>();
		zonas = new HashMap<Integer,Zona>();
		@SuppressWarnings("unused")
		Types types = new Types();												//Necesario para inicializar las funciones correctamente de la clase Types.
		//Inicio de los controladores
		cio = new ControladorDatosIO();
		//Inicio de las vistas
		archivos = new Archivos(this);
		tablaEditor = new TablaEditor(this);
		pgrupos = new ParametrosGrupos(this);
		pproyecto = new ParametrosProyecto(this,archivos);
		about = new About();
		paleta = new Paleta(100, 205, false);
		mapa = new Mapa(w,h, this);
		principal = new Principal(this);
		player = new Player(400, 400, true);
		vistaSIR = new VistaSIR(this);
		pizarra = new Pizarra(this);
		//Inicio de los parsers.
		parserPoly = new ParserPoly();
		parserPoly.setEscala(2.18);
		agregarPaneles();
	}
	
	/**
	 * <p>Title: agregarPaneles</p>  
	 * <p>Description: Aquí se situan todos los paneles (módulos) que se quieren
	 * añadir al a vista principal en su panel central.</p>
	 */
	private void agregarPaneles() {
		//Añadir paneles de los módulos.
		addDefaultBorder(tablaEditor,"Editor General");
		addDefaultBorder(mapa,"Visor de Mapa");
		addDefaultBorder(pgrupos,"Visor Grupos de Poblaci\u00F3n");
		addDefaultBorder(pproyecto,"Parámetros del Modelo");
		addDefaultBorder(vistaSIR,"Configuraci\u00F3n SIR");
		mostrarPanel(panelActivo);
	}
	
	/**
	 * <p>Title: addDefaultBorder</p>  
	 * <p>Description: Añade un JPanel (módulo) a la vista central del módulo principal 
	 * dotándole además de un border personalizado para la aplicación a todos los paneles.</p>
	 * Como añadido coloca un título a dicho panel, permitiendo identificarlo con 
	 * un nombre personalizado. 
	 * @param jp JPanel para añadir a la vista principal.
	 * @param title Título a colocar en el borde que rodea el panel.
	 */
	private void addDefaultBorder(JPanel jp,String title) {
		jp.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED,
				new Color(255, 0, 0),
				new Color(255, 175, 175),
				new Color(0, 255, 255),
				new Color(0, 0, 255)),
				title,
				TitledBorder.LEFT,
				TitledBorder.TOP,
				null,
				new Color(51, 51, 51)));
		principal.addPanelToView(jp);
	}
		
	/**
	 * <p>Title: hasZonas</p>  
	 * <p>Description: Indica si hay zonas cargadas.</p> 
	 * @return TRUE si el sistema tiene zonas definidas, FALSE en otro caso.
	 */
	public boolean hasZonas() {return !zonas.isEmpty();}
	
	/**
	 * <p>Title: getNumberZonas</p>  
	 * <p>Description: Devuelve el número de zonas actuales.</p> 
	 * @return Número de grupos de población o zonas actuales en el sistema.
	 */
	public int getNumberZonas() {
		int nz = 0;
		if(hasZonas()) nz = zonas.size();
		return nz;
	}
	
	/**
	 * <p>Title: getLevelColor</p>  
	 * <p>Description: Devuelve el color correspondiente a un nivel de contagio.</p> 
	 * @param n Nivel de contagio del 0 al 9. Siendo el 0 el más bajo y el 9 el más alto.
	 * @return Color representante de dicho nivel.
	 */
	public Color getLevelColor(int n) {return this.paleta.getColor(n);}
	
	/**
	 * <p>Title: setPoligonos</p>  
	 * <p>Description: Establece la representación gráfica de cada zona. Una fila
	 * por zona.</p> 
	 * @param datos DCVS con los datos especificados de cada zona.
	 */
	private void setPoligonos(DCVS datos) {
		int filas = datos.getRowCount();										//Número de poligonos a procesar.
		for(int i = 0; i<filas;i++) {
			Zona z = parser(datos.getFila(i));									//Llamar función parser.
			if(z != null) zonas.put(z.getID(), z);
		}
	
		if(!zonas.isEmpty()) {
			mapa.reset();
			pgrupos.reset();
		}
	}
	
	/**
	 * <p>Title: getZonas</p>  
	 * <p>Description: Función para facilitar el acceso al conjuto de 
	 * zonas desde otros módulos.</p> 
	 * @return Conjunto de zonas actualmente almacenados.
	 */
	public HashMap<Integer,Zona> getZonas() { return this.zonas;}
	
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
		int id = Integer.parseInt(puntos[0]);
		String nombre = puntos[1];
		int habitantes =  Integer.parseInt(puntos[2]);
		int superficie =  Integer.parseInt(puntos[3]);
		int s =  Integer.parseInt(puntos[4]);
		int i =  Integer.parseInt(puntos[5]);
		int r =  Integer.parseInt(puntos[6]);
		int p =  Integer.parseInt(puntos[7]);
		int nivel =  Integer.parseInt(puntos[8]);
		
		return new Zona(id,nombre,habitantes,superficie,s,i,r,p,nivel,parserPoly.parser(puntos,9));
	}

	/*  Métodos relacionados con la reproducción  */
	
	/**
	 * <p>Title: situarVentana</p>  
	 * <p>Description: Posiciona el marco (frame) del módulo incado en la nueva
	 *  posición de la pantalla, este método no hace por si solo visible el marco. </p>
	 * Los valores que puede tomar son: "REPRODUCTOR", "LEYENDA", "MAPA". Los cuales
	 * son los nombres de los módulos externos a la aplicación.
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
			paleta.setPosicion(posX, posY);
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
			OK = getNumberZonas() > 0;
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
		//paleta.setPaleta(paleta.getPaleta());
		paleta.setPaleta(colores);
	}
		
	
	/* Progreso de implementación control Principal */
	
	/**
	 * <p>Title: doAction</p>  
	 * <p>Description: Realiza la acción concreta indicada desde la vista
	 * que ha realizado la llamada.</p> 
	 * @param nombre Nombre de la acción.
	 */
	public void doActionPrincipal(String nombre) {
		//
		switch(nombre){
			case "Reproductor":
				situarVentana(ControladorModulos.REPRODUCTOR, principal.getX() - 350, principal.getY() + h/3);
				play();
				mostrarPanel("Mapa");
				break;
			case "Editor Gráfico":
				this.pizarra.reset();
				this.pizarra.toogleVisible();
				break;
			case "Paleta":
				situarVentana(LEYENDA, principal.getX() + w + 10, principal.getY());
				paleta.toggleFrameVisible();
				break;
			case "Mapa":
			case "Grupos":
			case "Tabla":
			case "Parámetros SIR":
			case "Proyecto":
				mostrarPanel(nombre);
				break;
			case "Abrir Proyecto":
				DCVS prj = cio.abrirArchivo(null,Types.PRJ);
				if(prj != null) {abrirProyecto(prj);}
				break;
			case "Importar Modelo A":
				DCVS pVS = cio.abrirArchivo(null,Types.CSV);
				//Si se ha abierto el archivo, procesarlo.
				if(pVS != null && pVS.getValueAt(0,0).equals("0")) importarProyectoVS(pVS);
				else if(pVS != null) showMessage("Archivo seleccionado no reconocido.",0);
				break;
			case "Importar Modelo B":
				//Hay que conocer la extensión que usa VenSim en sus proyectos. Temporalmente usar CSV
				DCVS hVS = cio.abrirArchivo(null,Types.CSV);
				//Si se ha abierto el archivo, procesarlo..
				if(hVS != null && hVS.getColumnName(1).equals("0")) importarHistoricoVS(hVS);
				else if(hVS != null) showMessage("Archivo seleccionado no reconocido.",0);
				break;		
			case "Nuevo Proyecto":
				clearProject();
				break;
			case "Salir":
				if(showMessage("¿Desea salir del programa?",3) == JOptionPane.YES_OPTION) System.exit(0);
				break;
			case "Acerca de...":
				about.toggleVisible();
				break;
			default:
				System.out.println("Controlador Modulos > doPrincipal: " + nombre + ", tipo no reconocido");
		}
	}
	
	
	/**
	 * <p>Title: mostrarPanel</p>  
	 * <p>Description: Establece la vista que debe ser visible.</p> 
	 * @param nombre Nombre de la vista a hacer visible.
	 */
	private void mostrarPanel(String nombre) {		
		//Mostrar panel correspondiente y ocultación del resto.
		mapa.setVisible(nombre.equals("Mapa"));					
		tablaEditor.setVisible(nombre.equals("Tabla"));
		pgrupos.setVisible(nombre.equals("Grupos"));
		pproyecto.setVisible(nombre.equals("Proyecto"));
		vistaSIR.setVisible(nombre.equals("Parámetros SIR"));
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
	private Integer showMessage(String txt, int tipo ) {
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
	
	/* En progreso de implementación control ARCHIVOS */
	
	/**
	 * <p>Title: guardarModulo</p>  
	 * <p>Description: Guardar los datos del módulo en el dispositivo indicado
	 * u establecido en el propio modelo</p> 
	 * @param dcvs Módulo DCVS a exportar a dispositivo de almacenamiento.
	 */
	public void guardarModulo(DCVS dcvs) {cio.guardarArchivo(dcvs);}
	
	/**
	 * <p>Title: getModulo</p>  
	 * <p>Description: Devuelve el módulo con la información que contenga. </p>
	 * El tipo de módulo es acorde a las extensiones aceptadas.
	 * @param tipo Tipo de módulo a devolver. Ej. MAP, HST, etc.
	 * @return El módulo solicitado.
	 */
	public DCVS getModulo(String tipo) {
		DCVS dcvs = null;
		if(modulos.containsKey(tipo)) dcvs = modulos.get(tipo);
		return dcvs;
	}
	
	/**
	 * <p>Title: hasModulo</p>  
	 * <p>Description: Indica si existe un módulo cargado en el sistema.</p> 
	 * @param tipo Tipo de módulo {@link Types Tipo de archivos y módulos}.
	 * @return TRUE si el sistema contiene dicho módulo, FALSE en otro caso.
	 */
	public boolean hasModulo(String tipo) {	return modulos.containsKey(tipo);}
	
	/**
	 * <p>Title: guardarProyecto</p>  
	 * <p>Description: Guarda la configuración del proyecto</p> En caso de que
	 * el módulo recibido sea null, se crea un módulo nuevo con los valores de
	 * ruta de los demás módulos.
	 * @param dcvsIn Módulo DCVS de entrada con configuración PRJ o null si hay
	 * que crear uno nuevo
	 */
	public void guardarProyecto(DCVS dcvsIn) {
		DCVS dcvs = dcvsIn;
		//Si se llama con null a la función crear nuevo módulo proyecto.	
//		if(dcvs == null) dcvs = creaModuloProyecto();
		
		//Guardado del fichero:
		String ruta = cio.guardarArchivo(dcvs);
		if(dcvs != null && ruta != null) {
			//Configuración de la ruta
			dcvs.setRuta(ruta);
			//Mostrar ruta en Field.
			archivos.setLabel(Types.PRJ,ruta);
//			mapaFields.get(IO.PRJ).setText(ruta);
			modulos.put(Types.PRJ, dcvs);
		}

	}
	
	/**
	 * <p>Title: clearProject</p>  
	 * <p>Description: Elimina los datos del proyecto y reinicia las vistas. </p>
	 */
	private void clearProject() {
		//Borrado de todos los módulos.
		modulos.clear();
		//Borrado de las zonas cargadas.
		zonas.clear();
		//Limpieza de las etiquetas de las vistas de diferentes módulos.
		archivos.reset();
		vistaSIR.reset();
		pproyecto.reset();
		tablaEditor.reset();
		mapa.reset();
		pgrupos.reset();
		pizarra.reset();
		principal.reset();
	}
	
	/**
	 * <p>Title: abrirProyecto</p>  
	 * <p>Description: Carga un proyecto con sus ficheros en los módulos
	 * correspondientes.</p>
	 * @param dcvs Archivo de proyecto con los datos del resto de módulos.
	 */
	public void abrirProyecto(DCVS dcvs) {
		int nm = dcvs.getRowCount();											//Número de datos (módulos) especificados.
		//Reiniciar todas las vistas y borrar datos anteriores.
		clearProject();	
		//Añadir ruta del proyecto a la etiqueta correspondiente.
		archivos.setLabel(dcvs.getTipo(), dcvs.getNombre());

		//Lectura de los módulos a cargar
		for(int i= 0;  i < nm; i++) {
			String[] m = dcvs.getFila(i);
			String dato = m[1];
			String etiq = m[0];
			//Si la etiqueta es de un módulo cargar el módulo correspondiente.
			if(!etiq.equals(Types.PRJ) && archivos.getMapaFields().containsKey(etiq)) {			//Nos asegurarmos que no cargue por error un PRJ.
				DCVS mAux = cio.abrirArchivo(dato,etiq);						//Carga el módulo desde el sistema de archivos.
				establecerDatos(mAux);											//Establecer el módulo.
			}else if(etiq.equals(Labels.NG)){									//Crea tantas zonas iniciales como indiqué el proyecto.
				pproyecto.setField(etiq, dato);
			}else pproyecto.setField(etiq, dato);								//Etiquetas de la vista -> mostrar en la vista.
		}
		
		//Desactivar los botones de guardado -> se han reiniciado todos, no hay nada que guardar.
		archivos.disableAllSavers();
		//Añadir este nuevo módulo al conjunto después de añadir el resto para evitar redundancias.
		modulos.put(dcvs.getTipo(), dcvs);
		//Forzar a todas las vistas a actualizar sus datos. La función setZonas se encarga.
		setZonas(zonas);
	}
	
	
	/**
	 * <p>Title: establecerDatos</p>  
	 * <p>Description: Establece el contenido del módulo cargado de acuerdo
	 * con su tipo, actualiza los elementos del JPanel correspondientes</p> 
	 * @param datos Conjunto de datos y cabecera encapsulados.
	 */
	public void establecerDatos(DCVS datos) {
		String tipo = datos.getTipo();
		//Actualizar etiqueta correspondiente con la ruta del archivo.
		archivos.setLabel(tipo,datos.getNombre());
		
		//Añadir la ruta del módulo al módulo del proyecto.
		//Los módulos PRJ están filtrados desde abrirProyecto y el ActionListener.
		if(modulos.containsKey(Types.PRJ)) {
			String[] nuevaEntrada = {tipo,datos.getRuta()};
			int linea = modulos.get(Types.PRJ).getFilaItem(tipo);
			boolean lineaDuplicada = linea > -1;
			//Si hay un módulo ya cargado, hay que sustituirlo.
			if(lineaDuplicada) {modulos.get(Types.PRJ).delFila(linea);}			//Eliminar entrada duplicada.
			modulos.get(Types.PRJ).addFila(nuevaEntrada);						//Añadir nueva entrada.
		}
		
		//Guardar los datos del módulo en su conjunto.
		modulos.put(tipo, datos);
		System.out.println("CM > establecerDatos > add modulo: " + hasModulo(tipo));
		//Operaciones extras según tipo de módulo.
		switch(tipo) {
			case (Types.MAP):
				setPoligonos(datos);
				break;
			case (Types.HST):
				setHistorico(datos);
				break;
			case (Types.DEF):
				vistaSIR.reset();
				break;
			default:			
		}
		
		//Actualización de las opciones de Principal.
		principal.reset();
		//Actualización de la vista archivos.
		archivos.refresh();
	}
	
	/* En progreso de implementación acciones Pizarra*/

	/**
	 * <p>Title: doActionPizarra</p>  
	 * <p>Description: Realizas las acciones oportundas pertenecientes a esta vista</p>
	 * La vista de pizarra puede comunicar dos operaciones: 1. aplicar cambios 
	 *  de las representaciones gráficas de los grupos de población o zonas.
	 *   2. Guardar los cambios efectuados en un nuevo fichero de tipo Mapa.
	 * <p>Dichos cambios son efectuados en las referencias de las zonas por tanto 
	 * quedan establecidas directamente en el grupo en el que están almacenadas.
	 * Si bien, es necesario detectar tales cambios a efectos de actualizar los
	 *  controles.</p>
	 * @param op Tipo de operación a realizar. (Guardar o Aplicar cambios de poligonos).
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionPizarra(String op) {
		boolean done = true;
		switch(op) {
		case("Cambios"):
			//Actualiza el mapa.
			mapa.reset();
			//Actualizar Grupos.
			pgrupos.reset();
			break;
		case("Guardar"):
			System.out.println("CM > doActionPizarra: Guardar, implementación en curso...");
			break;
		}

		return done;
	}
	
	
	/* En progreso de implementación acciones Archivos */
	
	/**
	 * <p>Title: doActionArchivos</p>  
	 * <p>Description: Función dedicada a la realización de las funciones de la
	 * la vista correspondiente.</p>
	 * Recibiendo dos operadores por parámetros realiza las diferentes actividades
	 * correspondientes al módulo correspondiente (entrada salida de archivos).
	 * Finalmente devolverá el resultado de la carga o guardado de la operación
	 * desde la ruta especifícada.
	 * @param op Operación a realizar "Abrir", "Guardar", "Guardar como".
	 * @param ext Extensión del tipo de datos, equivalente a los disponibles en
	 * la clase IO.
	 * @return TRUE si la operación ha concluido correctamente, FALSE en otro caso.
	 */
	public boolean doActionArchivos(String op, String ext) {
		DCVS dcvs = null;
		boolean ok = true;														//Indica si la operación ha tenido exito o no.
		//Opciones de Carga de módulo, NO módulo PRJ.
		if(op.equals("Abrir")) {
			dcvs = cio.abrirArchivo(null,ext);
			ok = dcvs != null;
			if(ok && !ext.equals(Types.PRJ)) {establecerDatos(dcvs);}
			else if(ok) {abrirProyecto(dcvs);}
		
		}else if(op.equals("Borrar") && modulos.containsKey(ext)){
			modulos.remove(ext);
		}else if(op.equals("Editar")) {
			TablaEditor te = new TablaEditor(this);
			te.abrirFrame();
			te.setModelo(modulos.get(ext));
		//Opciones de Guarga de módulo.	
		}else{
			//Optención del módulo correspondiente
			dcvs = getModulo(ext);
			ok = dcvs != null;
			if(ok) {
				//Si es guardar como, poner ruta a null. En otro caso guardará con la ruta que contiene.
				if(op.equals("Guardar como")) { dcvs.setRuta(null); }
				//
				if(ext.equals(Types.PRJ)) guardarProyecto(dcvs);
				else guardarModulo(dcvs);
			}
		}	
		return ok;
	}
	
	/* En progreso de implementación TableEditor */
	
	/**
	 * <p>Title: doActionTable</p>  
	 * <p>Description: Realizas las acciones oportundas pertenecientes al módulo
	 *  editor de tablas.</p>
	 * @param op Tipo de datos que esperan ser guardados tras modificación.
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionTableEditor(String op) {
		//Solo es la vista de las zonas => solo puede haber ocurrido cambios a guardar.
		//tablaEditor.enableBotonesGuardado(Types.MAP, true);
		System.out.println("doActionTableEditor > selección: " + op);
		
		
		
		
		
		return true;
	}

	/* En progreso de implementación VistasZonas y Grupos */
	
	/**
	 * <p>Title: doActionVistaZona</p>  
	 * <p>Description: Realizas las acciones oportundas pertenecientes a la vista
	 * de Zonas</p>
	 * La vista de zonas solo ejerce una opción, aplicar cambios de los campos.
	 * Dichos cambios son efectuados en las referencias de las zonas por tanto 
	 * quedan establecidas directamente en el grupo en el que están almacenadas.
	 * Si bien, es necesario detectar tales cambios a efectos de activar los botones
	 * correspondientes en la visa de Archivos.
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionVistaZona() {
		//Solo es la vista de las zonas => solo puede haber ocurrido cambios a guardar.
		archivos.enableBotonesGuardado(Types.MAP, true);
		return true;
	}
	
	/* En progreso de implementación acciones VistaSIR */
	
	/**
	 * <p>Title: doActionVistaSIR</p>  
	 * <p>Description: Realizas las acciones oportundas pertenecientes a la vista
	 * de parámetros SIR</p>
	 * La vista solo ejerce una opción, aplicar cambios de los campos.
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionVistaSIR() {
		//Solo es la vista de las zonas => solo puede haber ocurrido cambios a guardar.
		archivos.enableBotonesGuardado(Types.DEF, true);
		return true;
	}
	
	/* En progreso de implementación acciones ParametrosProyecto */
	
	/**
	 * <p>Title: doPProyecto</p>  
	 * <p>Description: Realizas las acciones oportundas pertenecientes a la vista
	 * de parámetros del modelo</p>
	 * La vista solo ejerce una opción, aplicar cambios de los campos.
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doPProyecto() {
		//Solo es la vista de las zonas => solo puede haber ocurrido cambios a guardar.
		System.out.println("CM > doPProyecto: ok");
		return true;
	}
		
	
	/* En progreso de implamentación PARSERS archivos de pruebas*/
	
	/**
	 * <p>Title: setZonas</p>  
	 * <p>Description: Establece el conjunto de zonas o grupos de población.</p>
	 * Actualiza las diferentes vistas vinculadas con los grupos de población obtenidos. 
	 * @param zonas Mapa cuya clave es la ID de cada zona y valor la zona.
	 */
	private void setZonas(HashMap<Integer,Zona> zonas) {
		this.zonas = zonas;
		//Actualiza el mapa.
		mapa.reset();
		//Actualizar Grupos.
		pgrupos.reset();
		//Actualizar en vista del proyecto en valor de NG.
		pproyecto.setField(Labels.NG,"" + zonas.size());
		//Actualización de la vista principal.
		principal.reset();
		//Actualización de la pizarra.
		pizarra.reset();
		//Actualización de la vista archivos.
		archivos.refresh();
	}
	
	private void setDatosProyecto(DCVS dcvs, int NG) {
		dcvs.addFila(new String[] {Labels.NG,"" + NG});
		//Cambiamos la etiqueta de tipo de módulo a PRJ para poder procesarla correctamente.
		dcvs.setTipo(Types.PRJ);
		//Llamamos a la función correspondiente.
		abrirProyecto(dcvs);
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
		//Limpiar todos los datos previos. No debe hacerse en otra parte pues
		// impediría modularidad e independencia de módulos.
		ParserProyectoVS parser = new ParserProyectoVS(prjV);
		//Establecer los datos del proyecto primero (provoca clear). 
//		abrirProyecto(parser.getmProyecto());
//		modulos.put(Types.PRJ, parser.getmProyecto());
//		setDatosProyecto(prjV,parser.getNG());
		//Establecer las zonas en las vistas correspondientes.
		setZonas(parser.getZonas());
		//Establecer el historico de niveles.
		establecerDatos(parser.getHistorico());
		//Establecer datos propios de la enfermedad.
		establecerDatos(parser.getmDefENF());
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
		//Limpiar todos los datos previos. No debe hacerse en otra parte pues 
		// impediría modularidad e independencia de módulos.
		ParserHistoricoVS parser = new ParserHistoricoVS(prjV);
		//Establecer los datos del proyecto primero (provoca clear). 
		setDatosProyecto(prjV,parser.getNG());
		//Establecer las zonas en las vistas correspondientes.
		setZonas(zonas = parser.getZonas());
		//Establecer el historico de niveles.

		//Establecer datos propios de la enfermedad.
		establecerDatos(parser.getmDefENF());
		//Establecer matriz de relaciones.
		tablaEditor.setModelo(parser.getMContactos());
		//Establecer los parámetros de la enfermedad.

	}
	
	/* Funciones para pruebas */
	/**
	 * Método main de la clase.
	 * @param args no usado.
	 */
	public static void main(String[] args) {
		ControladorModulos cm = new ControladorModulos();
		cm.isPlayable();
	}
}
