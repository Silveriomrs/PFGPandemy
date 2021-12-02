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
import modelo.IO;
import modelo.Labels;
import modelo.ParserHistoricoVS;
import modelo.ParserPoly;
import modelo.ParserProyectoVS;
import modelo.TypesFiles;
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
		TypesFiles typesFiles = new TypesFiles();												//Necesario para inicializar las funciones correctamente de la clase TypesFiles.
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
		parserPoly.setEscala(1);
		agregarPaneles();
		generarModulosBasicos();
		refreshViews();
	}
	
	/**
	 * <p>Title: generarModulosBasicos</p>  
	 * <p>Description: Genera los módulos básicos para operar con el sistema</p>
	 * Estos son los del proyecto y los parámetros de la enfermedad.
	 */
	private void generarModulosBasicos() {
		DCVS proyecto = new DCVS();
		proyecto.setTipo(TypesFiles.PRJ);
		//Crear cabecera		
		String[] cabecera = {"Tipo","Dato"};
		//Añadir cabecera.
		proyecto.addCabecera(cabecera);
		//Añadir etiquetas generales.
		proyecto.addFila(new String[]{Labels.NAME, null});
		proyecto.addFila(new String[]{Labels.AUTHOR, null});
		proyecto.addFila(new String[]{Labels.DESCRIPTION,null});
		proyecto.addFila(new String[]{Labels.DATE0,null});
		proyecto.addFila(new String[]{Labels.DATE1,null});
		proyecto.addFila(new String[]{Labels.VERSION,"1.0"});
		proyecto.addFila(new String[]{Labels.NG,"0"});
		//Desactivar edición de columna de etiquetas.
		proyecto.setEditLabels(false);
		//Procesarlo.
		abrirProyecto(proyecto);
		
		//Módulo de parámetros SIR.
		DCVS pSIR = new DCVS();
		pSIR.setTipo(TypesFiles.DEF);
		pSIR.addCabecera(cabecera);
		pSIR.addFila(new String[]{Labels.PTE, null});
		pSIR.addFila(new String[]{Labels.DME, null});
		pSIR.addFila(new String[]{Labels.DMI, null});
		pSIR.addFila(new String[]{Labels.IP, null});
		pSIR.addFila(new String[]{Labels.IT,null});
		pSIR.addFila(new String[]{Labels.FT,null});
		pSIR.setEditLabels(false);
		//Procesarlo.
		establecerDatos(pSIR);
	
	}
	
	/**
	 * <p>Title: generarModuloZonass</p>  
	 * <p>Description: Genera el módulo de grupos de población o zonas.</p>
	 * El módulo es añadido al conjunto de módulos sin datos de los propios
	 *  grupos de población añadidos.
	 */
	private void generarModuloZonas() {
		DCVS moduloZonas = new DCVS();
		//Añadir datos para el guardado  e identifación.
		setProjectParameters(TypesFiles.MAP,moduloZonas);

		//Crear cabecera.
		String[] cabecera = {Labels.ID,Labels.NAME,Labels.PEOPLE,
				Labels.AREA,Labels.S,Labels.I,Labels.R,Labels.P,Labels.C100K};
		//Añadir cabecera.
		moduloZonas.addCabecera(cabecera);
		moduloZonas.setEditLabels(false);
		//añadir el módulo al conjunto de módulos.
		modulos.put(moduloZonas.getTipo(),moduloZonas);
		//Procesarlo.
		establecerDatos(moduloZonas);
	}
	
	
	
	
	
	/**
	 * <p>Title: setProjectParameters</p>  
	 * <p>Description: Añade los tres parámetros básicos del proyecto al módulo</p>
	 * Estos son: Nombre unificado, directorio de trabajo y ruta absoluta.
	 *  Debe existir previamente un módulo de proyecto creado y con dichos parámetros.
	 *  <P>En caso de que el módulo posea nombre este se respetará. Los demás serán
	 *   adaptados a la ubicación del proyecto.</P>
	 * @param type Tipo de módulo a específicar.
	 * @param module Módulo DCVS al que añadir los parámetros.
	 */
	private void setProjectParameters(String type, DCVS module) {
		//Tipo
		module.setTipo(type);
		//Directorio de trabajo
		String wd = modulos.get(TypesFiles.PRJ).getDirectorio();
		module.setDirectorio(wd);
		//Nombre con el nombre del proyecto sino posee propio.
		if(module.getNombre() == null || module.getNombre().equals("") ) {
			String name = modulos.get(TypesFiles.PRJ).getNombre();
			//Quitar extensión
			int size = name.length();
			name = name.substring(0,size -4);
			//Añadir nueva extensión.
			name = name + "." + type;
			module.setName(name);
		}

		//Ruta absoluta.
		module.setRuta(module.getDirectorio() +
				System.getProperty("file.separator") + module.getNombre());	
	}
	

	/**
	 * <p>Title: refreshViews</p>  
	 * <p>Description: Llama a cada una de las vistas para que actualicen sus datos</p>
	 */
	private void refreshViews() {
		principal.refresh();
		vistaSIR.reset();
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
			Zona z = parser(datos.getRow(i));									//Llamar función parser.
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
	 * <p>Title: isNumeric</p>  
	 * <p>Description: Determina si un dato es convertible a un número tipo Integer</p> 
	 * @param cadena Cadena de texto a evaluar.
	 * @return TRUE si es convertible a Integer, FALSE en otro caso.
	 */
	public boolean isNumeric(String cadena) {
        boolean resultado = false;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
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
	 * @return Devuelve la zona creada para dicho poligono.
	 */
	private Zona parser(String[] puntos) {
		int id = Integer.parseInt(puntos[0]);
		String nombre = puntos[1];
		int habitantes =  Integer.parseInt(puntos[2]);
		int superficie =  Integer.parseInt(puntos[3]);
		double s0 =  Double.parseDouble(puntos[4]);
		double i0 =  Double.parseDouble(puntos[5]);
		double r0 =  Double.parseDouble(puntos[6]);
		double p0 = Double.parseDouble(puntos[7]);
		int c100k =  Integer.parseInt(puntos[8]);
		
		return new Zona(id,nombre,habitantes,superficie,s0,i0,r0,p0,c100k,parserPoly.parser(puntos,9));
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
		return historico != null &&
				historico.getRowCount()>0 &&
				getNumberZonas() > 0;
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
	public void setPaleta(DCVS modelo) {
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
				DCVS prj = cio.abrirArchivo(null,TypesFiles.PRJ);
				if(prj != null) {abrirProyecto(prj);}
				break;
			case "Importar Modelo A":
				DCVS pVS = cio.abrirArchivo(null,TypesFiles.CSV);
				//Si se ha abierto el archivo, procesarlo.
				if(pVS != null && pVS.getValueAt(0,0).equals("0")) importarProyectoVS(pVS);
				else if(pVS != null) showMessage("Archivo seleccionado no reconocido.",0);
				break;
			case "Importar Modelo B":
				//Hay que conocer la extensión que usa VenSim en sus proyectos. Temporalmente usar CSV
				DCVS hVS = cio.abrirArchivo(null,TypesFiles.CSV);
				//Si se ha abierto el archivo, procesarlo..
				if(hVS != null && hVS.getColumnName(1).equals("0")) importarHistoricoVS(hVS);
				else if(hVS != null) showMessage("Archivo seleccionado no reconocido.",0);
				break;		
			case "Nuevo Proyecto":
				generarModulosBasicos();
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
	 *  <p>0 showMessageDialog ERROR_MESSAGE</p>
	 *  <p>1 showMessageDialog INFORMATION_MESSAGE</p>
	 *  <p>2 showMessageDialog WARNING_MESSAGE</p>
	 *  <p>3 showConfirmDialog YES_NO_OPTION</p>
	 *  <p>4 showMessageDialog PLAIN_MESSAGE</p>
	 * @return En caso de un mensaje de confirmación devuelve el valor Integer
	 * de la opción escogida.
	 */
	public Integer showMessage(String txt, int tipo ) {
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
	 * <p>Title: getModule</p>  
	 * <p>Description: Devuelve el módulo con la información que contenga. </p>
	 * El tipo de módulo es acorde a las extensiones aceptadas.
	 * @param tipo Tipo de módulo a devolver. Ej. MAP, HST, etc.
	 * @return El módulo solicitado.
	 */
	public DCVS getModule(String tipo) {
		DCVS dcvs = null;
		if(modulos.containsKey(tipo)) dcvs = modulos.get(tipo);
		return dcvs;
	}
	
	/**
	 * <p>Title: hasModule</p>  
	 * <p>Description: Indica si existe un módulo cargado en el sistema.</p> 
	 * @param tipo Tipo de módulo {@link TypesFiles Tipo de archivos y módulos}.
	 * @return TRUE si el sistema contiene dicho módulo, FALSE en otro caso.
	 */
	public boolean hasModule(String tipo) {	return modulos.containsKey(tipo);}
	
	/**
	 * <p>Title: saveProjectAs</p>  
	 * <p>Description: Guarda la configuración del proyecto.</p> En caso de que
	 * el módulo recibido sea null, no realizará operación alguna. Cuando dicho
	 *  módulo no tiene establecido un directorio de trabajo, lo obtendrá del 
	 *   resultado de la operación.
	 * <P>Cuando se guarda los módulos existentes serán guardados en el mismo
	 * mismo directorio de trabajo, además de variar sus propiedades internas para
	 *  conservar dichos cambios.</P>
	 * Aquellos módulos creados pero sin nombre asignado obtendrán el nombre del
	 *  proyecto con la extensión propia de cada módulo.
	 * @param dcvsIn Módulo DCVS de entrada con configuración PRJ.
	 */
	private void saveProjectAs(DCVS dcvsIn) {
		DCVS dcvs = dcvsIn;
		//Guardado del fichero: En name se almacena el nombre elegido con su extensión.
		String name = cio.guardarArchivo(dcvs);
		//Si no es nulo el módulo y no se ha cancelado el guardado -> procede.
		if(dcvs != null && name != null) {
			//Obtener nuevo nombre y directorio de trabajo usado en este guardado.
			String wd = IO.WorkingDirectory;
			//Configuración de la ruta
			dcvs.setRuta(wd + System.getProperty("file.separator") + name);
			dcvs.setName(name);
			dcvs.setDirectorio(wd);
			//Guardar todos los módulos en el mismo directorio de trabajo.
//			saveModule(TypesFiles.PRJ,false);													//Guardar proyecto
			saveAllTogether();													//Guardar resto	
			//Mostrar rutas en Field.
			archivos.refresh();
		}
	}
	
	/**
	 * <p>Title: saveAllTogether</p>  
	 * <p>Description: Almacena los módulos actuales del sistema en el mismo
	 *  directorio de trabajo que el módulo principal (proyecto).</p>
	 */
	private void saveAllTogether() {
		modulos.forEach((type,module)->{
			if(!type.equals(TypesFiles.PRJ)) {
				setProjectParameters(type, module);;
				//Re-integrar el módulo en el entorno.
				establecerDatos(module);
				//Guardar en el directorio de trabajo.
				saveModule(type,false);
			}
		});
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
		int NG = 0;																//Número de zonas que tiene definido el proyecto.
		int nm = dcvs.getRowCount();											//Número de datos (módulos) especificados.
		String wd = IO.WorkingDirectory + System.getProperty("file.separator");
		//Reiniciar todas las vistas y borrar datos anteriores.
		clearProject();	
		//Añadir ruta del proyecto a la etiqueta correspondiente.
		archivos.setLabel(dcvs.getTipo(), dcvs.getNombre());

		//Lectura de los módulos a cargar
		for(int i= 0;  i < nm; i++) {
			String[] m = dcvs.getRow(i);
			String dato = m[1];
			String etiq = m[0];
			//Si la etiqueta es de un módulo cargar el módulo correspondiente.
			if(!etiq.equals(TypesFiles.PRJ) && archivos.getMapaFields().containsKey(etiq)) {			//Nos asegurarmos que no cargue por error un PRJ.
				//Como es un módulo, que esta dentro del proyecto, al nombre del archivo
				//Le añadimos al directorio de trabajo. Esa será la ruta donde debe
				//Estar el otro archivo.
				String ruta = wd + dato;
				DCVS mAux = cio.abrirArchivo(ruta,etiq);						//Carga el módulo desde el sistema de archivos.
				if(mAux != null) establecerDatos(mAux);							//Establecer el módulo.
				else showMessage("Archivo de proyecto incorrecto. \nReferencia un módulo que no está contenido en la misma carpeta:\n " + dato + "." + etiq,0);
			}else if(etiq.equals(Labels.NG)){									
				//Guardar el número de zonas que debe contener el proyecto.
				NG = Integer.parseInt(dato);
			}
		}
		
		//Desactivar los botones de guardado -> se han reiniciado todos, no hay nada que guardar.
		archivos.disableAllSavers();
		//Añadir este nuevo módulo al conjunto después de añadir el resto para evitar redundancias.
		modulos.put(dcvs.getTipo(), dcvs);
		//Ahora hay que comprobar que el número de zonas coincide con el cargado en el sub-modulo mapas.
		//Sino coinciden el número de zonas, re-ajustar.
		if(NG != getNumberZonas()) resizeZonas(NG);
		//Forzar a todas las vistas a actualizar sus datos. La función setZonas se encarga.
		setZonas(zonas);
		//Refrescar vista de Proyecto
		pproyecto.refresh();
	}
	
	/**
	 * <p>Title: insertInProjectModule</p>  
	 * <p>Description: Introduce los datos (tipo y nombre del archivo del módulo)
	 *  en el módulo del proyecto. Su finalidad es la carga de este módulo al abrir
	 *   el proyecto.</p>
	 * No realiza acción alguna sino hay un módulo de proyecto previamente en el sistema.
	 * @param datos Módulo a referenciar dentro del módulo del proyecto.
	 */
	private void insertInProjectModule(DCVS datos) {
		String tipo = datos.getTipo();
		if(modulos.containsKey(TypesFiles.PRJ)) {
			String[] nuevaEntrada = {tipo,datos.getNombre()};
			int linea = modulos.get(TypesFiles.PRJ).getFilaItem(tipo);
			boolean lineaDuplicada = linea > -1;
			//Si hay un módulo ya cargado, hay que sustituirlo.
			if(lineaDuplicada) {modulos.get(TypesFiles.PRJ).delFila(linea);}	//Eliminar entrada duplicada.
			modulos.get(TypesFiles.PRJ).addFila(nuevaEntrada);					//Añadir nueva entrada.
		}
	}
		
	/**
	 * <p>Title: establecerDatos</p>  
	 * <p>Description: Establece el contenido del módulo cargado de acuerdo
	 * con su tipo, actualiza los elementos del JPanel correspondientes</p> 
	 * @param datos Conjunto de datos y cabecera encapsulados.
	 * @return TRUE si la operación se ha realizado. FALSE en otro caso.
	 */
	private boolean establecerDatos(DCVS datos) {
		boolean isDone = true;
		String tipo = datos.getTipo();
		//Actualizar etiqueta correspondiente con la ruta del archivo.
//		archivos.setLabel(tipo,datos.getNombre());
		
		//Añadir la ruta del módulo al módulo del proyecto.
		//Los módulos PRJ están filtrados desde abrirProyecto y el ActionListener.
		insertInProjectModule(datos);
		
		//Guardar los datos del módulo en su conjunto.
		modulos.put(tipo, datos);
		//Operaciones extras según tipo de módulo.
		switch(tipo) {
			case (TypesFiles.MAP):
				setPoligonos(datos);
				break;
			case (TypesFiles.HST):
				setHistorico(datos);
				break;
			case (TypesFiles.DEF):
				vistaSIR.reset();
				break;
			default:			
		}
		
		//Actualización de las opciones de Principal.
		principal.refresh();
		//Actualización de la vista archivos.
		archivos.refresh();
		
		return isDone;
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
			//Guardar los cambios efectuados en disco.
			done = saveModule(TypesFiles.MAP,modulos.get(TypesFiles.MAP).getNombre() == null);
			System.out.println("doActionPizarra > guardado en disco? " + done);
			break;
		}
		return done;
	}
	
	
	
	/* En progreso de implementación acciones Archivos */
	
	/**
	 * <p>Title: openModule</p>  
	 * <p>Description: Abre un fichero (módulo) desde disco.</p> 
	 * @param ext Tipo de fichero/módulo a abrir.
	 * @return TRUE si la operación se ha realizado correctamente. FALSE en otro caso.
	 */
	private boolean openModule(String ext) {
		DCVS dcvs = cio.abrirArchivo(null,ext);
		boolean ok = dcvs != null;
		if(ok && !ext.equals(TypesFiles.PRJ)) {establecerDatos(dcvs);}
		else if(ok) {abrirProyecto(dcvs);}
		return ok;
	}
	
	/**
	 * <p>Title: removeModule</p>  
	 * <p>Description: Elimina un módulo del proyecto.</p>
	 * Elimina tanto la referencia dentro del módulo del proyecto como del conjunto
	 *  de módulos cargados. No elimina el fichero del disco. 
	 * @param ext Tipo de módulo a eliminar.
	 * @return TRUE si la operación se ha realizado correctamente. FALSE en otro caso.
	 */
	private boolean removeModule(String ext) {
		boolean done = modulos.containsKey(ext);
		if(done) {
			//Eliminar del grupo de módulos.
			modulos.remove(ext);
			//Eliminar si hay entrada, de la propiedades del proyecto.
			//Garantizar de que hay un módulo de proyecto antes de tratar de eliminar una entrada del mismo.
			if(hasModule(TypesFiles.PRJ)) {
				DCVS dcvs = modulos.get(TypesFiles.PRJ);
				int index = dcvs.getFilaItem(ext);
				//Si hay entrada en el módulo del proyecto, eliminar dicha entrada.
				done = index > -1;
				if(done) dcvs.delFila(index);
			}
		}
		return done;
	}
	
	/**
	 * <p>Title: saveModule</p>  
	 * <p>Description: Guarda los datos del módulo indicado en el disco.</p>
	 * El método puede guardar en la ruta indicada dentro del propio módulo o
	 *  realizar una consulta del lugar donde guardarlo. Este comportamiento es
	 *   indicado mediante el parámetro 'as'. Si está activado (TRUE) abrirá
	 *    cuadro de dialogo correspondiente. 
	 * @param ext Tipo de módulo a salvar datos en disco.
	 * @param as TRUE activara cuadro de destino del archivo, FALSE guardará en 
	 *  la ruta especifícada dentro del propio módulo, es decir, origen del archivo.
	 * @return TRUE si la operación se ha relizado correctamente. FALSE en otro caso.
	 */
	private boolean saveModule(String ext, boolean as) {
		//Optención del módulo correspondiente
		DCVS dcvs = getModule(ext);
		boolean done = dcvs != null;
		if(done) {
			//Ruta a null. En otro caso guardará con la ruta definida en dicho módulo.
			if(as) dcvs.setRuta(null);
			//
			cio.guardarArchivo(dcvs);
		}
		return done;
	}
	
	
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
		boolean ok = true;														//Indica si la operación ha tenido exito o no.
		//Opciones de Carga de módulo, NO módulo PRJ.
		switch(op) {
		case("Abrir"):  ok = openModule(ext); break;
		case("Borrar"): ok = removeModule(ext); break;
		case("Editar"): 
			editModule(ext);
			ok = false;															//Evitar desabilitar botón de guardar como.
		break;
		default:
			boolean as = op.equals("Guardar como");
			ok = saveModule(ext,as);
		}
		return ok;
	}
	
	/* En progreso de implementación TableEditor */
	
	/**
	 * <p>Title: editModule</p>  
	 * <p>Description: Abre el editor general de módulos con el módulo en cuestión.</p>
	 * @see modelo.ModuleType
	 * @param ext Tipo de módulo a editar.
	 */
	private void editModule(String ext) {
		TablaEditor te = new TablaEditor(this);
		te.abrirFrame("Editando modulo: " + TypesFiles.get(ext));
		te.setModelo(modulos.get(ext),false);
	}
	
	/**
	 * <p>Title: doActionTable</p>  
	 * <p>Description: Realizas las acciones oportundas pertenecientes al módulo
	 *  editor de tablas.</p>
	 * @param modulo Módulo con los datos que esperan ser guardados tras modificación.
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionTableEditor(DCVS modulo) {
		boolean as = modulo.getNombre() == null;
		return saveModule(modulo.getTipo(),as);
	}

	/* En progreso de implementación VistasZonas y Grupos */
	
	/**
	 * <p>Title: resizeDCVS</p>  
	 * <p>Description: Ajusta el número de columnas del módulo.</p>
	 * El método comprueba si el número de columnas existentes es menor que el 
	 *  nuevo número de columnas indicado, en tal caso genera nuevas columnas y 
	 *   las denomina por orden de indice poniendo como prefijo el texto pasado
	 *    por parámetro.
	 * @param type El módulo (su tipo) a editar.
	 * @param needNCols Número de columnas que debe tener el módulo.
	 * @param head Texto que será el prefijo para nombrar las nuevas columnas.
	 * @return TRUE si la operación se ha relizado (por tanto sido necesaria), FALSE en otro caso.
	 */
	private boolean resizeDCVS(String type, int needNCols, String head) {
		int nCols = modulos.get(type).getColumnCount();
		boolean done = nCols < needNCols;
		//Si el número de columnas actual es menor del necesario, proceder.
		if(done) {
			for(int i = nCols +1; i<=needNCols ; i++) {
				modulos.get(type).addColumna(head + (i -10));					//Se restan el número de etiquetas que conforman el grupo.
			}
		}
		return done;
	}
	
	private boolean upgradePolygonZone(int ID) {
		boolean done = true;
		//Obtener zona del grupo con su ID y separar datos usando el separador tipo ','
		String[] valores = zonas.get(ID).toString().split(",");
		int sizeV = valores.length;
		//Recalibrar número de columnas si es necesario (necesario por longitud del poligono)
		done = resizeDCVS(TypesFiles.MAP, sizeV, "Px;y");
		//Localizar la posición en el módulo DCVS
		int index = modulos.get(TypesFiles.MAP).getFilaItem("" + ID);
		//Limpiar los datos anteriores de poligono que hubiera en la fila.
		if(index > -1) {
			modulos.get(TypesFiles.MAP).clearRow(index);
		}else {
			//No debería entrar aquí, puesto que debería existir siempre la entrada.
			System.out.println("CM > upgradePolygonZone > Atención! incoherencia de datos.");
		}
		//Escribir datos.
		for(int i = 9; i<sizeV; i++) {
			modulos.get(TypesFiles.MAP).setValueAt(valores[i], index, i);
		}
		return done;
	}
	
	/**
	 * <p>Title: doActionVistaZona</p>  
	 * <p>Description: Realizas las acciones oportundas pertenecientes a la vista
	 * de Zonas</p>
	 * La vista de zonas solo ejerce una opción, aplicar cambios de los campos.
	 * Dichos cambios son efectuados en las referencias de las zonas por tanto 
	 * quedan establecidas directamente en el grupo en el que están almacenadas.
	 * Si bien, es necesario detectar tales cambios a efectos de activar los botones
	 * correspondientes en la visa de Archivos.
	 * @param ID Identificador uniquivoco del grupo de población o Zona del que actualizar datos.
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionVistaZona(int ID) {
		boolean done = true;
		//Solo es la vista de las zonas => solo puede haber ocurrido cambios a guardar.
		//Obtener zona del grupo con su ID y separar datos usando el separador tipo ','
		String[] valores = zonas.get(ID).toString().split(",");
		int sizeV = valores.length;
		//Recalibrar número de columnas si es necesario (necesario por longitud del poligono)
		done = resizeDCVS(TypesFiles.MAP, sizeV, "Px;y");
		//Localizar la posición en el módulo DCVS
		int index = modulos.get(TypesFiles.MAP).getFilaItem("" + ID);
		//Limpiar los datos anteriores que hubieran en la fila.
		if(index > -1) modulos.get(TypesFiles.MAP).clearRow(index);		
		else {
			//Sino existe tal entrada hay que crearla.
/* No debería ser necesario crearla. Debería estar ya creada aunque fuera la primera vez. */
			modulos.get(TypesFiles.MAP).addFila(null);
			index = modulos.get(TypesFiles.MAP).getRowCount();
			//Si el mensaje se dispara, es que se ha dado el caso de incoherencia de datos.
			System.out.println("CM > doActionVistaZona > Atención! incoherencia de datos.");
		}
		
		//Escribir datos.
		for(int i = 0; i<sizeV; i++) {
			modulos.get(TypesFiles.MAP).setValueAt(valores[i], index, i);
		}
		
		
		archivos.enableBotonesGuardado(TypesFiles.MAP, true);
		return done;
	}
	
	/* En progreso de implementación acciones VistaSIR */
	
	/**
	 * <p>Title: setDataToLabel</p>  
	 * <p>Description: Asigna un valor a una etiqueta en el módulo específicado.</p>
	 * @param ext Extensión (etiqueta identificadora) del módulo objetivo.
	 * @param label Etiqueta a la que modificar su valor asignado.
	 * @param data Dato a insertar como nuevo valor de la etiqueta.
	 * @return TRUE si la operación se ha realizado correctamente, FALSE en otro caso.
	 */
	private boolean setDataModule(String ext, String label, String data) {
		boolean done = false;
		if(hasModule(ext)) {
			done = modulos.get(ext).setDataToLabel(label, data);
		}
		return done;
	}
	
	/**
	 * <p>Title: getValueFromLabel</p>  
	 * <p>Description: Recupera un valor de una etiqueta. Si la etiqueta no existe, devuelve null.</p>
	 * @param ext Tipo de módulo (extensión) donde realizar la búsqueda.
	 * @param label Etiqueta a buscar dentro de dicho módulo.
	 * @return La cadena de texto cuyo valor está asociada a la etiqueta. NULL en otro caso.
	 * */
	public String getValueFromLabel(String ext, String label) {
		boolean done = hasModule(ext);
		String resultado = null;
		if(done) {
			int index = getModule(ext).getFilaItem(label);
			done = index > -1;
			if(done) resultado = (String) getModule(ext).getValueAt(index, 1);
		}
		return resultado;
	}
	
	/**
	 * <p>Title: setValueAtLabel</p>  
	 * <p>Description: Asigna un valor a una etiqueta. Si la etiqueta no existe, la crea.</p>
	 * 
	 * @param ext Extensión (etiqueta identificadora) del módulo a editar.
	 * @param label Etiqueta a buscar.
	 * @param data Dato a insertar en la tabla.
	 * @return TRUE si la operación se ha realizado correctamente, FALSE en otro caso.
	 * */
	private boolean setValueAtLabel(String ext, String label, String data) {
		boolean done = false;
		done = setDataModule(ext ,label ,data);
		if(!done) {
			getModule(ext).addFila(new String[] {label,data});
			done = true;
		}
		return done;
	}
	
	/**
	 * <p>Title: doActionVistaSIR</p>  
	 * <p>Description: Realizas las acciones oportundas pertenecientes a la vista
	 * de parámetros SIR</p>
	 * La vista solo ejerce una opción, aplicar cambios de los campos.
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionVistaSIR() {
		//extraer datos de los fields y actualizar con los mismos el módulo.
		boolean realizado = true;
		//PTE
		realizado = setValueAtLabel(TypesFiles.DEF ,Labels.PTE ,vistaSIR.getLabel(Labels.PTE));
		//DME
		realizado = setValueAtLabel(TypesFiles.DEF ,Labels.DME ,vistaSIR.getLabel(Labels.DME));
		//IP
		realizado = setValueAtLabel(TypesFiles.DEF ,Labels.IP ,vistaSIR.getLabel(Labels.IP));
		//DMIP
		realizado = setValueAtLabel(TypesFiles.DEF ,Labels.DMI ,vistaSIR.getLabel(Labels.DMI));
		//IT
		realizado = setValueAtLabel(TypesFiles.DEF ,Labels.IT ,vistaSIR.getLabel(Labels.IT));
		//FT
		realizado = setValueAtLabel(TypesFiles.DEF ,Labels.FT ,vistaSIR.getLabel(Labels.FT));
		
		//Guardar el módulo en el disco.
		saveModule(TypesFiles.DEF,false);
		//Solo es la vista de las zonas => solo puede haber ocurrido cambios a guardar.
		archivos.enableBotonesGuardado(TypesFiles.DEF, true);
		return realizado;
	}
	
	/* En progreso de implementación acciones ParametrosProyecto */
	
	/**
	 * <p>Title: addNewZones</p>  
	 * <p>Description: Añade nuevas zonas hasta cubrir el total indicado.</p> 
	 * @param index Indice de la última zona actual.
	 * @param nNew Número total de zonas que deben haber en el sistema.
	 */
	private void addNewZones(int index, int nNew) {
		for(int i = index; i < nNew; i++) {
			int superficie = 0;											
			int habitantes = 0;
			Zona z = new Zona(i+1, "Grupo " + (i+1), habitantes, superficie,0,0,0,0,0, null);
			zonas.put(i+1,z);
			//Añadir nueva entrada al módulo de grupos (zonas).
			String[] datos = z.toString().split(",");
			modulos.get(TypesFiles.MAP).addFila(datos);
		}
	}
	
	/**
	 * <p>Title: removeExtraZones</p>  
	 * <p>Description: Elimina las zonas extras. </p> 
	 * @param index Último número de zona del sistema
	 * @param nNew Nuevo número de zonas a establecer.
	 */
	private void removeExtraZones(int index,int nNew) {
		for(int i = index; i >= nNew; i--) {
			zonas.remove(i);
			//Si dicha zona estaba en el módulo de los grupos, eliminarla.
			if(index > -1) {modulos.get(TypesFiles.MAP).delFila(i);}
		}
	}
	
	/**
	 * <p>Title: resizeZonas</p>  
	 * <p>Description: Crea o elimina zonas del grupo de Zonas según sea necesario.</p>
	 * La modificación se realiza sobre el HashMap, no sobre el módulo (lo cual lo realizan
	 *  las otras funciones de apoyo a este método).
	 * @param newNG Nuevo número de zonas.
	 * @return TRUE si ha realizado o sido necesario efectuar operación. FALSE en otro caso.
	 */
	private boolean resizeZonas(int newNG) {
		boolean done = true;
		int NG = 0;
		if(hasZonas()) NG = getNumberZonas();
		
		if(NG < newNG) {														//Si hay menos zonas -> añadir.
			addNewZones(NG,newNG);
		}else if(NG > newNG) {													//Si hay más zonas -> eliminar restos.
			removeExtraZones(NG,newNG);
		}
		return done;
	}
	
	/**
	 * <p>Title: doPProyecto</p>  
	 * <p>Description: Realizas las acciones oportundas pertenecientes a la vista
	 * de parámetros del modelo</p>
	 * La vista solo ejerce una opción, aplicar cambios de los campos.
	 * @param datos Datos contenidos en un mapa con sus etiquetas y valores correspondientes.
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionPProyecto(HashMap<String,String> datos) {
		boolean done = true;
		int NG  = Integer.parseInt(datos.get(Labels.NG));
		
		//Actualizar datos de las etiquetas del módulo del proyecto.
		datos.forEach((label,data) ->{
			setValueAtLabel(TypesFiles.PRJ ,label ,data);
		});
		
		//Guardar el módulo en el disco.
		saveProjectAs(getModule(TypesFiles.PRJ));
		
		//En caso de no haber un módulo de grupos de población, generar y agregarlo.
		if(!modulos.containsKey(TypesFiles.MAP)) generarModuloZonas();
		
		//Ajustar el número de zonas o crearlas si es necesario.
		if(getNumberZonas() != NG) { 
			done = resizeZonas(NG);
			setZonas(zonas);
		}
		
		return done;
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
		pproyecto.refresh();
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
		dcvs.setTipo(TypesFiles.PRJ);
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
//		modulos.put(TypesFiles.PRJ, parser.getmProyecto());
//		setDatosProyecto(prjV,parser.getNG());
		//Establecer las zonas en las vistas correspondientes.
		setZonas(parser.getZonas());
		//Establecer el historico de niveles.
		establecerDatos(parser.getHistorico());
		//Establecer datos propios de la enfermedad.
		establecerDatos(parser.getmDefENF());
		//Establecer matriz de relaciones.
		tablaEditor.setModelo(parser.getMContactos(),false);
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
		tablaEditor.setModelo(parser.getMContactos(),false);
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
