/**
 * Controlador de la parte visual encargada de la representación gráfica de las
 * zonas de influencias. En modo mapa.
 */
package controlador;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import modelo.DCVS;
import modelo.DCVSFactory;
import modelo.Labels;
import modelo.Labels_GUI;
import modelo.ModuleType;
import modelo.OperationsType;
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
 * Clase principal del módulo controlador, coordina todas las acciones entre
 *  las vistas, el módelo y la gestión del flujo de datos.
 * @author Silverio Manuel Rosales Santana
 * @date 13/07/2021
 * @version 6.2
 */
public class ControladorModulos {
 
	@SuppressWarnings("unused")
	private Labels labels;														//Necesario iniciarlo al menos una vez en el proyecto.
	private Paleta paleta;
	private ControladorDatosIO cio;
	//Vistas
	private About about;
	private Principal principal;
	private Player player;
	private Mapa mapa;
	private Archivos archivos;
	private VistaSIR vistaSIR;
	private TablaEditor tableEditor;
	private ParametrosGrupos pgrupos;
	private ParametrosProyecto pproyecto;
	private Pizarra pizarra;
	private HashMap<String,DCVS> modulos;										//Conexión con la parte del modelo. Almacena todos los datos de cada modelo.
	private HashMap<Integer,Zona> zonas;
	//
	private ParserPoly parserPoly;
	//Obtener dimensiones de la pantalla para controlar donde aparecen los módulos.
	Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();

	/** FrameDim Dimensión preferible del marco para todo módulo de la aplicación*/  
	public final static Dimension FrameDim = new Dimension(1024, 768);
	/** PanelCentralDim Dimensión del panel central para estandarizar el aspecto de los diferentes paneles de toda la aplicación */  
	public final static Dimension PanelCentralDim = new Dimension(1024, 768);
	/** MinDim damaño mínimo para todo marco o panel de la aplicación*/  
	public final static Dimension MinDim = new Dimension(800, 600);

	private final String separador = System.getProperty("file.separator");
	
	private int w = 1024;
	private int h = 768;
	private OperationsType panelActivo = null;
	
	
	/**
	 * Constructor de la clase controladora.
	 */
	public ControladorModulos() {
		labels = new Labels();
		//Inicio del mapa de módulos.
		modulos = new HashMap<String, DCVS>();
		zonas = new HashMap<Integer,Zona>();
		@SuppressWarnings("unused")
		TypesFiles typesFiles = new TypesFiles();					//Necesario para inicializar las funciones correctamente de la clase TypesFiles.
		//Inicio de los controladores
		cio = new ControladorDatosIO();
		//Inicio de las vistas
		archivos = new Archivos(this);
		tableEditor = new TablaEditor(this);
		pgrupos = new ParametrosGrupos(this);
		pproyecto = new ParametrosProyecto(this,archivos);
		about = new About();
		paleta = new Paleta(this,170, 215);
		mapa = new Mapa(w,h, this);
		principal = new Principal(this);
		player = new Player(this,mapa);
		vistaSIR = new VistaSIR(this);
		pizarra = new Pizarra(this);
		//Inicio de los parsers.
		parserPoly = new ParserPoly();
		parserPoly.setEscala(1);
		agregarPaneles();
		newProject();
		refresh();
	}
	
	/**
	 * <p>Genera los módulos básicos para operar con el sistema</p>
	 * Estos son los del proyecto, paleta de colores y los parámetros de la enfermedad.
	 *  Borra todos los datos anteriores que estén en cargados.
	 */
	private void newProject() {
		DCVS proyecto = DCVSFactory.newModule(TypesFiles.PRJ);
		//Establecer último directorio de trabajo.
		String wd = IO.WorkingDirectory + separador;
		proyecto.setDirectorio(wd);
		//Borrar todo lo anterior.
		clearProject();
		//Procesarlo.
		//Añadir este nuevo módulo al sistema para que el resto ded módulos puedo usar sus atributos de fichero.
		modulos.put(TypesFiles.PRJ, proyecto);
		//Módulo de parámetros SIR.
		generarModuloDEF();
		//Módulo de paleta de colores.
		generarModuloPAL();
		
	}
	
	/**
	 * <p>Genera la tabla del módulo correspondiente a la definición de
	 *  propiedades intrínsicas de la enfermedad.</p>
	 * Esta función además integra dicha tabla en el resto de la aplicación.
	 */
	private void generarModuloDEF() {
		DCVS pSIR = DCVSFactory.newModule(TypesFiles.DEF);
		establecerDatos(pSIR);
	}
	
	/**
	 * <p>Genera una tabla (módulo) de la representación en grados de color
	 *  de los niveles de contagio on las etiquetas propias.</p>
	 * <p>Además de establecerle los atributos propios que pudieran estar definidos en el proyecto y
	 *  procesará el establecimiento del módulo generado dentro de la aplicación.
	 */
	private void generarModuloPAL() {
		DCVS pal = DCVSFactory.newModule(TypesFiles.PAL);
		establecerDatos(pal);
	}
	
	/**
	 * <p>Genera el módulo de grupos de población o zonas.</p>
	 * El módulo es añadido al conjunto de módulos sin datos de los propios
	 *  grupos de población añadidos.
	 */
	private void generarModuloMAP() {
		DCVS moduloZonas = DCVSFactory.newModule(TypesFiles.MAP);
		establecerDatos(moduloZonas);
	}
	
	/**
	 * <p>Genera una tabla (módulo) de relaciones o matriz de contactos
	 *  con las etiquetas propias, además de establecerle los atributos propios que
	 *   pudieran estar definidos en el proyecto.</p>
	 * Esta función también procesará el establecimiento del módulo generado dentro
	 *  de la aplicación.
	 */
	private void generarModuloREL(){
		//Añadimos nueva matriz de contactos.
		DCVS relaciones = DCVSFactory.newREL(zonas);
		establecerDatos(relaciones);
	}
			
	/**
	 * <p>Añade los tres parámetros básicos del proyecto al módulo</p>
	 * Estos son: Nombre unificado, directorio de trabajo y ruta absoluta.
	 *  Debe existir previamente un módulo de proyecto creado y con dichos parámetros.
	 *  <P>En caso de que el módulo posea nombre este se respetará. Los demás serán
	 *   adaptados a la ubicación del proyecto.</P>
	 * @param module Módulo DCVS al que añadir los parámetros.
	 */
	private void setProjectParameters(DCVS module) {
		//Obteción de datos generales del módulo.
		String name = module.getName();
		String pName = getModule(TypesFiles.PRJ).getName();
		String path = module.getPath();
		//Establecer directorio de trabajo.
		String wd = IO.WorkingDirectory;
		module.setDirectorio(wd);
		
		//Nombre con el nombre del proyecto sino posee propio. si pName es null es porque no se ha establecido todavía.
		if((name == null || name.equals("")) && (pName != null) ) {
			name = modulos.get(TypesFiles.PRJ).getName();
			module.setName(name);
		}
		//Ruta absoluta. Si wd es null es porque no se ha indicado un directorio de trabajo todavía.
		if((path == null || path.equals("")) && (wd != null)) {
			path = wd +	separador + name + "." + module.getType();
			module.setPath(path);
		}
	}
	
	/**
	 * Llama a cada una de las vistas para que actualicen sus datos.
	 */
	private void refresh() {
		pproyecto.refresh();
		archivos.refresh();
		principal.refresh();
		paleta.refresh();
		vistaSIR.refresh();
		pgrupos.refresh();
		//Comprobar si el reproductor debe estar cerrado y ocultarlo en tal caso.
		if(!isPlayable() && player.isVisible()) {
			player.setVisible(isPlayable());
		}
	}
	
	/**
	 * <p>Establece el conjunto de zonas o grupos de población.</p>
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
	
	/**
	 * Aquí se situan todos los paneles (módulos) que se quieren
	 * añadir al a vista principal en su panel central.
	 */
 	private void agregarPaneles() {
		//Añadir paneles de los módulos.
		addDefaultBorder(tableEditor,Labels_GUI.L_BORDER_PANEL_TE);
		addDefaultBorder(mapa,Labels_GUI.L_BORDER_PANEL_MAP);
		addDefaultBorder(pgrupos,Labels_GUI.L_BORDER_PANEL_GRP);
		addDefaultBorder(pproyecto,Labels_GUI.L_BORDER_PANEL_PRJ);
		addDefaultBorder(vistaSIR,Labels_GUI.L_BORDER_PANEL_DEF);
		mostrarPanel(panelActivo);
	}
	
	/**
	 * <p>Añade un JPanel (módulo) a la vista central del módulo principal 
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
	 * Indica si hay zonas cargadas.
	 * @return TRUE si el sistema tiene zonas definidas, FALSE en otro caso.
	 */
	public boolean hasZonas() {return !zonas.isEmpty();}
	
	/**
	 * Devuelve el número de zonas actuales.
	 * @return Número de grupos de población o zonas actuales en el sistema.
	 */
	public int getNumberZonas() {
		int nz = 0;
		if(hasZonas()) nz = zonas.size();
		return nz;
	}
	
	/**
	 * Devuelve el color correspondiente a un nivel de contagio. 
	 * @param n Nivel de contagio del 0 al 9. Siendo el 0 el más bajo y el 9 el más alto.
	 * @return Color representante de dicho nivel.
	 */
	public Color getLevelColor(int n) {
		Color c = Color.GRAY;
		if(hasModule(TypesFiles.PAL)) {
			String label = Labels.LEVEL + String.valueOf(n);
			//Obtener línea
			int index = getModule(TypesFiles.PAL).getFilaItem(label);
			if(index > -1) {
				//Obtener colores
				int r = Integer.parseInt((String) getModule(TypesFiles.PAL).getValueAt(index, 1));
				int g = Integer.parseInt((String) getModule(TypesFiles.PAL).getValueAt(index, 2));
				int b = Integer.parseInt((String) getModule(TypesFiles.PAL).getValueAt(index, 3));
				c = new Color(r,g,b);
			}
		}
		return c;		
	}
	
	/**
	 * Establece la representación gráfica de cada zona. Una fila por zona.
	 * @param datos DCVS con los datos especificados de cada zona.
	 */
	private void setPoligonos(DCVS datos) {
		if(!zonas.isEmpty()) {
			mapa.reset();
			pgrupos.reset();
		}
		
		int filas = datos.getRowCount();										//Número de poligonos a procesar.
		for(int i = 0; i<filas;i++) {
			Zona z = parser(datos.getRow(i));									//Llamar función parser.
			if(z != null) zonas.put(z.getID(), z);
		}
	}
	
	/**
	 * Función para facilitar el acceso al conjuto de zonas desde otros módulos. 
	 * @return Conjunto de zonas actualmente almacenados.
	 */
	public HashMap<Integer,Zona> getZonas() { return this.zonas;}
			
	/**
	 * Determina si un dato es convertible a un número tipo Integer. 
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
	 * @return Devuelve la zona creada para dicho poligono. Null en otro caso.
	 */
	private Zona parser(String[] puntos) {
		Zona z = null;
		try {
			int id = Integer.parseInt(puntos[0]);
			String nombre = puntos[1];
			int habitantes =  Integer.parseInt(puntos[2]);
			int superficie =  Integer.parseInt(puntos[3]);
			double s0 =  Double.parseDouble(puntos[4]);
			double i0 =  Double.parseDouble(puntos[5]);
			double r0 =  Double.parseDouble(puntos[6]);
			double p0 = Double.parseDouble(puntos[7]);
			int c100k =  Integer.parseInt(puntos[8]);
			z = new Zona(id,nombre,habitantes,superficie,s0,i0,r0,p0,c100k,parserPoly.parser(puntos,9));
		}catch(Exception e) {
			return null;
		}
		
		return z;
	}

	/**
	 * <p>Posiciona el marco (frame) del módulo incado en la nueva
	 *  posición de la pantalla, este método no hace por si solo visible el marco.</p>
	 * Los valores que puede tomar son: "REPRODUCTOR", "LEYENDA", "MAPA". Los cuales
	 * son los nombres de los módulos externos a la aplicación.
	 * @param modulo identificador del módulo a posicionar. @see ModuleType
	 * @param posX Posición X a establacer.
	 * @param posY Posición Y a establecer.
	 */
	private void situarVentana(ModuleType modulo, int posX, int posY) {
		switch(modulo) {
		case PLAYER:
			player.setPosicion(posX, posY);
			break;
		case PAL:
			paleta.setPosicion(posX, posY);
			break;
		case MAP:
			mapa.setPosicion(posX, posY);
			break;
		default:
			System.out.println("CM > situarVentana > Valor en el controlador de mapa no establecido: " + modulo);
		}
	}
	
	/*  Métodos relacionados con la reproducción  */
	
	/**
	 * <p>Evalua si el reproductor puede ser ejecutado en función
	 *  de disponer de un historico condatos y un conjunto de zonas representables</p>
	 *  Para facilitar su seguimiento, en caso de no cumplirse la idoniedad, mostrará
	 *  el mensaje adecuado de información por consola.
	 * @return True si el reproductor puede ejecutarse, False en otro caso.
	 */
	public boolean isPlayable() {
		return hasModule(TypesFiles.HST) &&
				getModule(TypesFiles.HST).getRowCount()>0 &&
				getNumberZonas() > 0;
	}
	
	/**
	 * <p>Ejecuta la reproducción de un historico mostrando gráficamente
	 * la evolución grabada. </p>
	 * La representación gráfica corresponde a las zonas creadas. Para poder
	 * usarse esta función deben haber sido almacezandos previamente los datos
	 * de las zonas y el historico a reproducir. Para más detalles consultar:
	 * - \ref setPoligonos()
	 */
	private void play() {
		if(isPlayable()) {
			player.setPlay();
			player.setVisible(true);
		}
	}

	
	/* Control ARCHIVOS */
	
	/**
	 * <p>Devuelve el módulo con la información que contenga.</p>
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
	 * Indica si existe un módulo cargado en el sistema.
	 * @param tipo Tipo de módulo
	 * @return TRUE si el sistema contiene dicho módulo, FALSE en otro caso.
	 * \see TypesFiles
	 */
	public boolean hasModule(String tipo) {	return modulos.containsKey(tipo);}

	/**
	 * Almacena los módulos actuales (excepto PRJ) del sistema en el mismo
	 *  directorio de trabajo que el módulo principal (proyecto).
	 * <P>Este método requiere por tanto que el proyecto haya sido grabado
	 *  previamente.</P>
	 */
	private void saveAllTogether() {
		modulos.forEach((type,module)->{
			if(!type.equals(TypesFiles.PRJ)) {
				//Si el módulo no tiene un nombre o ruta darles los que tenga el proyecto establecidos.
				setProjectParameters(module);
				//Añadir la ruta del módulo al módulo del proyecto.
				//Los módulos PRJ están filtrados desde abrirProyecto y el ActionListener.
				insertInProjectModule(module);
				//Guardar en el directorio de trabajo.
				saveModule(type,false);
			}
		});
	}
	
	/**
	 * Elimina los datos del proyecto y reinicia las vistas.
	 */
	private void clearProject() {
		//Borrado de todos los módulos.
		modulos.clear();
		//Borrado de las zonas cargadas.
		zonas.clear();
		//Limpieza de las etiquetas de las vistas de diferentes módulos.
		tableEditor.reset();
		archivos.reset();
		vistaSIR.reset();
		pproyecto.reset();
		player.clear();
		mapa.reset();
		pgrupos.reset();
		principal.reset();
		mostrarPanel(panelActivo);
		paleta.reset();
		pizarra.reset();
	}
	
	/**
	 * Carga un proyecto con sus ficheros en los módulos correspondientes.
	 * @param dcvs Archivo de proyecto con los datos del resto de módulos.
	 */
	public void abrirProyecto(DCVS dcvs) {
		int NG = 0;																//Número de zonas que tiene definido el proyecto.
		int nm = dcvs.getRowCount();											//Número de datos (módulos) especificados.
		String wd = IO.WorkingDirectory + separador;
		
		//Añadir este nuevo módulo al sistema para que el resto ded módulos puedo usar sus atributos de fichero.
		modulos.put(TypesFiles.PRJ, dcvs);
		
		//Lectura de los módulos a cargar
		for(int i= 0;  i < nm; i++) {
			String[] m = dcvs.getRow(i);
			String etiq = m[0];
			String dato = m[1];
			
			//Eliminar espacios en blanco al inicio y final.
			if(dato != null) dato = dato.strip();
			
			//Si la etiqueta es de un módulo cargar el módulo correspondiente.
			//Filtro al corte teniendo en cuenta el valor del dato.
			if(!etiq.equals(TypesFiles.PRJ) && archivos.getMapaFields().containsKey(etiq) &&
					dato != null && !dato.equals("")) {
				//Composiciónm de la ruta para cargar desde el DD el fichero.
				String path = wd + dato + "." + etiq;
				//Carga el módulo desde el sistema de archivos.
				DCVS mAux = cio.abrirArchivo(path,etiq);	
				//Establecer el módulo.
				if(mAux != null) establecerDatos(mAux);							
				else showMessage(Labels_GUI.ERR_MSG_1_CM + dato + "." + etiq,0);
			}else if(etiq.equals(Labels.NG)){									
				//Guardar el número de zonas que debe contener el proyecto.
				NG = Integer.parseInt(dato);
			}
			
		}
		
		//Desactivar los botones de guardado -> se han reiniciado todos, no hay nada que guardar.
		archivos.disableAllSavers();
		//Ahora hay que comprobar que el número de zonas coincide con el cargado en el sub-modulo mapas.
		//Sino coinciden el número de zonas, re-ajustar.
		if(NG != getNumberZonas()) resizeZonas(NG);
		//Si ha faltado  algún módulo básico por cargar, genera uno nuevo sin datos.
		checkBasicModule();
		//Refrescar vista de Proyecto
		refresh();
	}
	
	/**
	 * Función auxiliar, genera aquellos módulos básicos que por alguna razón no estén generados o cargados.
	 * En algunas ocasiones, sucede que no existe al cargar un proyecto la definición de un módulo básico. Esto
	 * generalmente se debe a un error del usuario. En estos casos, la solución es generar un módulo autoomático
	 *  sin datos generados.
	 */
	private void checkBasicModule() {
		//Comprobación un a uno de que existen los módulos básicos (Exepto PRJ).
		//En caso de que no exista uno, genera y almacena sin atributos de ningún tipo.
		if(!hasModule(TypesFiles.DEF)) generarModuloDEF(); 
		if(!hasModule(TypesFiles.PAL)) generarModuloPAL();
		// Los siguientes módulos están comentados porque su generación implica conocer el valor de NG, y puede producir errores
		//if(!hasModule(TypesFiles.MAP)) generarModuloMAP();
		//if(!hasModule(TypesFiles.REL)) generarModuloREL();

	}

	/**
	 * <p>Introduce los datos (tipo y nombre del archivo del módulo)
	 *  en el módulo del proyecto. Su finalidad es la carga de este módulo al abrir
	 *   el proyecto.</p>
	 * No realiza acción alguna sino hay un módulo de proyecto previamente en el sistema.
	 * @param datos Módulo a referenciar dentro del módulo del proyecto.
	 */
	private void insertInProjectModule(DCVS datos) {
		String tipo = datos.getType();
		if(modulos.containsKey(TypesFiles.PRJ)) {
			//Formato y datos de la línea a insertar [Etiqueta (type), nombre]
			String[] nuevaEntrada = {tipo,datos.getName()};
			//Buscar pos de la etiqueta
			int linea = modulos.get(TypesFiles.PRJ).getFilaItem(tipo);
			//Comprobar si existe la entrada.
			boolean lineaDuplicada = linea > -1;
			//Si hay un módulo ya cargado, hay que sustituirlo.
			//Eliminar entrada duplicada.
			if(lineaDuplicada) {modulos.get(TypesFiles.PRJ).delFila(linea);}	
			//Añadir nueva entrada.
			modulos.get(TypesFiles.PRJ).addFila(nuevaEntrada);					
		}
	}
		
	
	/**
	 * Establece el contenido del módulo cargado de acuerdo
	 * con su tipo, actualiza los elementos del JPanel correspondientes.
	 * @param datos Conjunto de datos y cabecera encapsulados.
	 */
	private void establecerDatos(DCVS datos) {
		String tipo = datos.getType();
		
		//Guardar los datos del módulo en su conjunto.
		modulos.put(tipo, datos);
		
		//Operaciones extras según tipo de módulo.
		switch(tipo) {
		case (TypesFiles.REL):		
			break;
		case (TypesFiles.MAP):	
			setPoligonos(datos);
			//Forzar a todas las vistas a actualizar sus datos. La función setZonas se encarga.
			setZonas(zonas);
			break;
		case (TypesFiles.HST):
			break;
		case (TypesFiles.DEF):
			break;
		case (TypesFiles.PAL):
			break;
		default:
		}
		
		refresh();
	}
	
	/* Acciones Principal */
	
	/**
	 *  Realiza la acción concreta indicada desde la vista que ha realizado la llamada.
	 * @param op Nombre de la acción.
	 */
	public void doActionPrincipal(OperationsType op) {
		//
		switch(op){
			case PLAY:
				situarVentana(ModuleType.PLAYER, principal.getX() - 350, principal.getY() + h/3);
				play();
				mostrarPanel(op);
				break;
			case EDIT_GRAPHIC:
				this.pizarra.reset();
				this.pizarra.toogleVisible();
				break;
			case EDIT_PAL:
				situarVentana(ModuleType.PAL, principal.getX() + w + 10, principal.getY());
				paleta.setEditable(true);
				paleta.setFrameVisible(true);
				break;
			case VIEW_PAL:
				situarVentana(ModuleType.PAL, principal.getX() + w + 10, principal.getY());
				paleta.setEditable(false);
				paleta.toggleFrameVisible();
				break;
			case VIEW_MAP:
			case VIEW_GRP:
			case EDIT:
			case VIEW_DEF:
			case VIEW_PRJ:
				mostrarPanel(op);
				break;
			case VIEW_REL: editModule(TypesFiles.REL); break;					//Abrir el editor de tablas con la matriz de contactos.
			case OPEN:
				String path = cio.selFile(1, TypesFiles.PRJ)[0];
				DCVS prj = cio.abrirArchivo(path,TypesFiles.PRJ);
				if(prj != null) {
					clearProject();
					abrirProyecto(prj);
				}
				break;
			case IMPORT_A:
				DCVS pVS = cio.abrirArchivo(null,TypesFiles.CSV);
				//Si se ha abierto el archivo, procesarlo.
				if(pVS != null && pVS.getValueAt(0,0).equals("0")) {
					clearProject();
					importarModelo(pVS,TypesFiles.MODEL_A);
				}
				else if(pVS != null) showMessage(Labels_GUI.ERR_FILE_UNKNOWN,0);
				break;
			case IMPORT_B:	
				//Formato de importación CSV
				DCVS hVS = cio.abrirArchivo(null,TypesFiles.CSV);
				//Si se ha abierto el archivo, procesarlo..
				if(hVS != null && hVS.getColumnName(1).equals("0")) {
					clearProject();
					importarModelo(hVS,TypesFiles.MODEL_B);
				}
				else if(hVS != null) showMessage(Labels_GUI.ERR_FILE_UNKNOWN,0);
				break;		
			case NEW:
				newProject();
				break;
			case SAVE:
				saveModule(TypesFiles.PRJ,true);
				break;
			case EXIT:
				if(showMessage(Labels_GUI.WARNING_1_DATA_LOSS + "\n" + Labels_GUI.REQUEST_EXIT_CONFIRM,3) == JOptionPane.YES_OPTION) System.exit(0);
				break;
			case HELP_TABLES:
				cio.openPDF("mTablas");
				break;
			case HELP_USER_GUIDE:
				cio.openPDF("mUsuario");
				break;
			case HELP_ABOUT:
				about.toggleVisible();
				break;
			default:
				System.out.println("CM > doPrincipal: " + op + ", opción no reconocida");
		}
	}
	
	/**
	 * Establece la vista que debe ser visible.
	 * @param nombre Nombre de la vista a hacer visible.
	 */
	private void mostrarPanel(OperationsType nombre) {
		//Mostrar panel correspondiente y ocultación del resto.
		mapa.setVisible(nombre == OperationsType.VIEW_MAP || nombre == OperationsType.PLAY);					
		tableEditor.setVisible(nombre == OperationsType.EDIT);
		pgrupos.setVisible(nombre == OperationsType.VIEW_GRP);
		pproyecto.setVisible(nombre == OperationsType.VIEW_PRJ);
		vistaSIR.setVisible(nombre == OperationsType.VIEW_DEF);
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
		case 0: titulo = Labels_GUI.ERR; break;
		case 1: titulo = Labels_GUI.INF; break;
		case 2: titulo = Labels_GUI.WARN; break;
		case 3: titulo = Labels_GUI.QST;
			opcion = JOptionPane.showConfirmDialog(null, txt, titulo, JOptionPane.YES_NO_OPTION);
		break;
		case 4: titulo = Labels_GUI.MHELP_ABOUT; tipo = 1; break;
		default:
			titulo = "";
		}
		
		if(tipo != 3) JOptionPane.showMessageDialog(null, txt, titulo, tipo);
		
		return opcion;
	}
		
	
	/* Acciones Pizarra*/

	/**
	 * <p>Realizas las acciones oportundas pertenecientes a esta vista</p>
	 * La vista de pizarra puede comunicar dos operaciones: 1. aplicar cambios 
	 *  de las representaciones gráficas de los grupos de población o zonas.
	 *   2. Guardar los cambios efectuados en un nuevo fichero de tipo Mapa.
	 * <p>Dichos cambios son efectuados en las referencias de las zonas por tanto 
	 * quedan establecidas directamente en el grupo en el que están almacenadas.
	 * Si bien, es necesario detectar tales cambios a efectos de actualizar los
	 *  controles.</p>
	 * @param op Tipo de operación a realizar. (Ej: Guardar o Aplicar cambios de poligonos).
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionPizarra(OperationsType op) {
		boolean done = true;
//TODO: Eliminar después de que funcione  OperationsType opr = OperationsType.getNum(op);
		switch(op) {
		case CHANGES:
			//Actualiza el mapa.
			mapa.reset();
			//Actualizar Grupos.
			pgrupos.reset();
			break;
		case SAVE:
			//Aplicar cambios a la tabla después de pedir confirmación.
			int option = showMessage(Labels_GUI.WARNING_2_OVERWRITE,3);	
			if(option == JOptionPane.YES_OPTION) {
				//Añadir o eliminar polígono de/a la tabla.
				zonas.forEach((k,v)->{
					addPolygonToMap(k);
				});
			}
			//Guardar los cambios efectuados en disco.
			done = saveModule(TypesFiles.MAP,modulos.get(TypesFiles.MAP).getName() == null);
			break;
		case OPEN: 
			//Apertura de una imagen para usar de fondo.
    		String ruta = cio.selFile(1, TypesFiles.IMG)[0];
    		// En caso de tener una ruta correcta se procede a la carga.
    		if(ruta != null && !ruta.equals("")) {
    			pizarra.setFondo(ruta);
    		}
    		break;
		default:
			break;
		}
		return done;
	}
		
	
	/* Acciones Archivos */
	
	/**
	 * Abre un fichero (módulo) desde disco.
	 * @param ext Tipo de fichero/módulo a abrir.
	 * @return TRUE si la operación se ha realizado correctamente. FALSE en otro caso.
	 */
	private boolean openModule(String ext) {
		//Selección de fichero.
		String path = cio.selFile(1, ext)[0];
		DCVS dcvs = cio.abrirArchivo(path,ext);
		boolean ok = dcvs != null;
		if(ok && !ext.equals(TypesFiles.PRJ)) {
			//En caso de mapa o de relaciones, el módulo debe tener definidos tantos grupos
			// como esten definidos en el proyecto.
			int NG = getNumberZonas();
			
			if((ext.equals(TypesFiles.REL) || ext.equals(TypesFiles.MAP)) && dcvs.getRowCount() != NG ) {
				showMessage(Labels_GUI.ERR_LOAD_MODULE + dcvs.getPath() + "\n" + Labels_GUI.ERR_MODULE_LESS_NG,0);
				ok = false;	
				System.out.println("CM > OpenModule > Tipo: " + ext + ", NG: " + NG + " / filas de datos: " + dcvs.getRowCount());
			}	
			
			establecerDatos(dcvs);	
		}else if(ok) {abrirProyecto(dcvs);}
		return ok;
	}
	
	/**
	 * <p>Recrea el módulo especificado con los parámetros básicos</p>
	 * Método diseñado para restablecer valroes por defecto cuando se elimina un 
	 *  módulo considerado básico.
	 *  <P>En función de las dependencias entre módulos, la función dispara la 
	 *   regeneración de otros módulos o su actualización dado el caso.</P> 
	 * @param type Tipo de módulo a volver a generar con valores por defecto.
	 */
	private void regenerarModuloBasico(String type) {
		switch(type) {
		case(TypesFiles.MAP): generarModuloMAP();								//Generar un mapa => generar Relaciones también.
		case(TypesFiles.REL): generarModuloREL(); break;
		case(TypesFiles.DEF): generarModuloDEF(); break;
		case(TypesFiles.PAL): generarModuloPAL(); break;						//Efecto de restablecer valores por defecto.
		}
	}
	
	/**
	 * <p>Elimina un módulo del proyecto.</p>
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
			if(!ext.equals(TypesFiles.PRJ) && hasModule(TypesFiles.PRJ)) {
				DCVS dcvs = modulos.get(TypesFiles.PRJ);
				int index = dcvs.getFilaItem(ext);
				//Si hay entrada en el módulo del proyecto, eliminar dicha entrada.
				done = index > -1;
				if(done) dcvs.delFila(index);
			}
			
			//Si es un módulo básico hay que generarlo de nuevo.
			regenerarModuloBasico(ext);
		}
		return done;
	}
	
	/**
	 * <p>Guarda los datos del módulo indicado en el disco.</p>
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
		//Obtención del módulo correspondiente
		DCVS dcvs = getModule(ext);
		String[] fileAttr = null;
		boolean done = dcvs != null;
		//Pedir nueva ubicación / nombre en caso as = true.
		if (as) {
			fileAttr = cio.selFile(2, ext);
			if(fileAttr[0] != null) {
				// [ruta,directorio trabajo (parent),nombre]
				dcvs.setPath(fileAttr[0]);
				dcvs.setDirectorio(fileAttr[1]);
				dcvs.setName(fileAttr[2]);		
			}
			else {done = false;}
		}

		//Si es un módulo particular
		if(done && !ext.equals(TypesFiles.PRJ)) {
			done = cio.guardarModulo(dcvs);
		//En otro caso es un PRJ.
		} else if (done){
			//Guardar todos los módulos en el mismo directorio de trabajo.
			saveAllTogether();
			//Guardar PRJ después del resto de módulos para garantizar el correcto guardado de PRJ.
			//Con sus tablas actualizadas después de establecer los parámetros de ficheros (path,wd,name) y tablas en PRJ.
			done = cio.guardarModulo(dcvs);
			//Mostrar rutas en Field.
			archivos.refresh();
		}

		return done;
	}	
	
	/**
	 * <p>Función dedicada a la realización de las funciones de la
	 * la vista correspondiente.</p>
	 * Recibiendo dos operadores por parámetros realiza las diferentes actividades
	 * correspondientes al módulo correspondiente (entrada salida de archivos).
	 * Finalmente devolverá el resultado de la carga o guardado de la operación
	 * desde la ruta especifícada.
	 * @param op Operación a realizar "Abrir", "Guardar", "Guardar como". @see modelo#OperationsType
	 * @param ext Extensión del tipo de datos, equivalente a los disponibles en
	 * la clase IO.
	 * @return TRUE si la operación ha concluido correctamente, FALSE en otro caso.
	 */
	public boolean doActionArchivos(OperationsType op, String ext) {
		boolean done = true;														//Indica si la operación ha tenido exito o no.
		//Opciones de Carga de módulo, NO módulo PRJ.
		switch(op) {
		case OPEN:  done = openModule(ext); break;
		case DELETE: done = removeModule(ext); break;
		case EDIT: 
			editModule(ext);
			done = false;															//Evitar desabilitar botón de guardar como.
		break;
		case SAVE_AS: done = saveModule(ext,true); break;
		case SAVE: done = saveModule(ext,false); break;
		default:
			System.out.println("CM > doActionArchivos > Unknown option: " + op);
		}
		
		refresh();
		return done;
	} 
	
	/* Acciones TableEditor */
	
	/**
	 * Abre el editor general de módulos con el módulo en cuestión.
	 * @see modelo.ModuleType
	 * @param ext Tipo de módulo a editar.
	 */
	private void editModule(String ext) {
		TablaEditor te = new TablaEditor(this);
		te.abrirFrame(Labels_GUI.TE_TITLE_EXTERNAL + TypesFiles.get(ext));
		te.setModelo(modulos.get(ext),false);
	}
	
	/**
	 * Realizas las acciones oportundas pertenecientes al módulo editor de tablas.
	 * @param modulo Módulo con los datos que esperan ser guardados tras modificación.
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionTableEditor(OperationsType op) {
		boolean done = true;
		
		switch(op) {
		case OPEN:
			//Selección de fichero.
			String[] file = cio.selFile(1, TypesFiles.ANY);
			String path = file[0];
			String ext = file[2];
			DCVS dcvs = cio.abrirArchivo(path,ext);
			done = dcvs != null;
			//En caso de ser una tabla no nula...
			if(done) {
				tableEditor.setModelo(dcvs, true);							//Establecer la nueva tabla como tabla activa.
			}
			
			break;
		case APPLY:
			DCVS module = tableEditor.getDCVS();
			if(module != null) {
				establecerDatos(module);
				//indicar al módulo de archivos que modulo se ha modificado para activar 
				//El botón de guardar correspondiente.
				archivos.enableBotonesGuardado(module.getType(), true);
			}
			break;
		default:
		}
		
		refresh();
		return done;
	}

	/* Acciones VistasZonas y Grupos */
	
	/**
	 * <p>Ajusta el número de columnas del módulo.</p>
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
	
	/**
	 *  Incluye el nombre de un grupo de población en la matriz de relaciones.
	 * @param ID Identificador del grupo de población.
	 */
	private void setNameMAPinREL(int ID) {
		String name = zonas.get(ID).getName();
		//Cambiar el nombre de la fila (su etiqueta)
		getModule(TypesFiles.REL).setValueAt(name, ID-1, 0);					//IDs empiezan por 1, las filas por 0.
		//Cambiar el nombre de la columnna.
		getModule(TypesFiles.REL).setColumnName(ID, name);
	}

	/**
	 * Añade un polígono correspondiente a un grupo de población a la tabla MAP.
	 * <p>Esta función no elimina las definiciones de punto sobrante, aunque estas
	 *  estén sin un valor asignado en tabla.</p>
	 * @param ID Identificador del grupo de población.
	 */
	private void addPolygonToMap(int ID) {
		//Obtener zona del grupo con su ID y separar datos usando el separador tipo ','
		String[] valores = zonas.get(ID).toString().split(",");
		int sizeV = valores.length;
		//Recalibrar número de columnas si es necesario (necesario por longitud del poligono)
		//Añadiendo tantas nuevas columnas de puntos como sea necesario.
		resizeDCVS(TypesFiles.MAP, sizeV, "Px;y");
		//Localizar la posición en el módulo DCVS
		int index = modulos.get(TypesFiles.MAP).getFilaItem("" + ID);
		//Limpiar los datos anteriores que hubieran en la fila.
		if(index > -1) modulos.get(TypesFiles.MAP).clearRow(index);
		else {
			//Sino existe tal entrada hay que crearla.
			//No debería ser necesario crearla. Debería estar ya creada aunque
			//fuera la primera vez. 
			modulos.get(TypesFiles.MAP).addFila(null);
			index = modulos.get(TypesFiles.MAP).getRowCount();
			//Si el mensaje se dispara, es que se ha dado el caso de incoherencia de datos.
			System.out.println("CM > doActionVistaZona > Atención! incoherencia de datos.");
		}
		
		//Escribir datos.
		for(int i = 0; i<sizeV; i++) {
			modulos.get(TypesFiles.MAP).setValueAt(valores[i], index, i);
		}
	}
	
	/**
	 * <p>Realizas las acciones oportundas pertenecientes a la vista
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
		//Añadir o eliminar polígono de/a la tabla.
		addPolygonToMap(ID);
		//Ajustar nombre de la zona con el de la relaciones por si se han realizado cambios.
		setNameMAPinREL(ID);
		
		//Actualizar nombre del panel. Se da por sentado que ha sido cambiado incluso cuando no lo ha sido.
		pgrupos.changeTabName(ID,zonas.get(ID).getName());
		
		archivos.enableBotonesGuardado(TypesFiles.MAP, true);
		refresh();
		return done;
	}
	
	/* Acciones VistaPaleta */
	
	/**
	 * <p>Realiza las acciones de actualización de datos de la vista
	 *  correspondiente a la paleta de colores o representación gráfica por colores 
	 *   de los niveles de contagio.</p>
	 *   El nivel de contagio es igual al número de casos por cada cien mil hábitantes. 
	 * @param level Nivel de contagio. Letra 'L' en mayúsculas seguida del nivel del [0 al 9],
	 *  ambos inclusive.
	 * @param r Valor del componente rojo.
	 * @param g Valor del componente verde.
	 * @param b Valor del componente azul.
	 * @return TRUE si la operación se ha realizado correctamente, false en otro caso.
	 */
	public boolean doActionPAL(String level, int r, int g, int b) {
		boolean done = true;
		int index = getModule(TypesFiles.PAL).getFilaItem(level);
		if(index > -1) {
			//Variamos sus valores.
			getModule(TypesFiles.PAL).setValueAt(String.valueOf(r), index, 1);
			getModule(TypesFiles.PAL).setValueAt(String.valueOf(g), index, 2);
			getModule(TypesFiles.PAL).setValueAt(String.valueOf(b), index, 3);	
		} else done = false;
		return done;
	}
	
	/* Acciones VistaSIR */
	
	/**
	 * Asigna un valor a una etiqueta en el módulo específicado. No crea campo nuevo en caso de no existir.
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
	 * Recupera un valor de una etiqueta. Si la etiqueta no existe, devuelve null.
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
	 * Asigna un valor a una etiqueta. Si la etiqueta no existe, la crea y asigna el valor indicado.
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
	 * Actualiza los datos de la tabla SIR con los datos que
	 *  esten establecidos en las etiquetas correspondientes de la Vista.
	 * @return TRUE si se han añadido los datos, FALSE en otro caso.
	 */
	private boolean updateMSIR() {
		boolean done = true;
		//PTE
		done = setValueAtLabel(TypesFiles.DEF ,Labels.PTE ,vistaSIR.getLabel(Labels.PTE));
		//DME
		done = setValueAtLabel(TypesFiles.DEF ,Labels.DME ,vistaSIR.getLabel(Labels.DME));
		//IP
		done = setValueAtLabel(TypesFiles.DEF ,Labels.IP ,vistaSIR.getLabel(Labels.IP));
		//DMIP
		done = setValueAtLabel(TypesFiles.DEF ,Labels.DMI ,vistaSIR.getLabel(Labels.DMI));
		//IT
		done = setValueAtLabel(TypesFiles.DEF ,Labels.IT ,vistaSIR.getLabel(Labels.IT));
		//FT
		done = setValueAtLabel(TypesFiles.DEF ,Labels.FT ,vistaSIR.getLabel(Labels.FT));
		return done;
	}
	
	/**
	 * <p>Realizas las acciones oportundas pertenecientes a la vista
	 * de parámetros SIR</p>
	 * La vista puede indicar dos acciones:
	 * - 1. aplicar cambios de los campos.
	 * - 2. Iniciar el motor de cálculo del módelo.
	 * @param op Operación a realizar, UPDATE o EXECUTE
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionVistaSIR(OperationsType op) {
		//extraer datos de los fields y actualizar con los mismos el módulo.
		boolean done = true;	
		System.out.println(op);
		done = updateMSIR();
		switch(op) {
		case APPLY: 
			//Solo es la vista de las zonas => solo puede haber ocurrido cambios a guardar.
			archivos.enableBotonesGuardado(TypesFiles.DEF, true);
			break;
		case EXECUTE:
			//Comprobar si valores OK. Si valores OK. Ejecutar motorSIR
			// runSIR(double pte, double dme, boolean ip, double dmi) 
			if(done) runSIR();
			break;
		default:
		}
		
		return done;
	}
	
	
	/* Acciones ParametrosProyecto */
	
	/**
	 * Añade nuevas zonas hasta cubrir el total indicado.
	 * @param index Indice de la última zona actual.
	 * @param nNew Número total de zonas que deben haber en el sistema.
	 */
	private void addNewZones(int index, int nNew) {
		for(int i = index; i < nNew; i++) {
			int superficie = 0;											
			int habitantes = 0;
			Zona z = new Zona(i+1, Labels_GUI.DEFAULT_NAME_GRP + (i+1), habitantes, superficie,0,0,0,0,0, null);
			zonas.put(i+1,z);
			//Añadir nueva entrada al módulo de grupos (zonas).
			String[] datos = z.toString().split(",");
			if(hasModule(TypesFiles.MAP)) modulos.get(TypesFiles.MAP).addFila(datos);
		}
	}
	
	/**
	 * Elimina las zonas extras.
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
	 * <p>Crea o elimina zonas del grupo de Zonas según sea necesario.</p>
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
	 * <p>Realizas las acciones oportundas pertenecientes a la vista
	 * de parámetros del modelo</p>
	 * La vista solo ejerce una opción, aplicar cambios de los campos.
	 * @param datos Datos contenidos en un mapa con sus etiquetas y valores correspondientes.
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionPProyecto(HashMap<String,String> datos) {
		boolean done = true;
		//Número de Grupos de población, importante para dimensionar el proyecto.
		int NG  = Integer.parseInt(datos.get(Labels.NG));
		//Nombre del proyecto, immportante para asignar al resto de módulos.
		String name = datos.get(Labels.NAME);
		
		//Actualizar datos de las etiquetas del módulo del proyecto.
		datos.forEach((label,data) ->{
			setValueAtLabel(TypesFiles.PRJ ,label ,data);
		});
	
		//Establece por defecto el nombe del proyecto como nombre de archivo.
		getModule(TypesFiles.PRJ).setName(name);
		
		//En caso de no haber un módulo de grupos de población, generar y agregarlo.
		if(!modulos.containsKey(TypesFiles.MAP)) {
			generarModuloMAP();
			generarModuloREL();
		}
		
		//Establecer nombre del proyecto al módulo DEF
		establecerDatos(getModule(TypesFiles.DEF));
		
		//Ajustar el número de zonas o crearlas si es necesario.
		if(getNumberZonas() != NG) { 
			done = resizeZonas(NG);
			setZonas(zonas);
			//Añadimos nueva matriz de contactos.
			generarModuloREL();
		}

		return done;
	}
		
	
	/* PARSERS y generación Históricos */
	
	
	/**
	 * <p>Importa los datos de un archivo de proyecto generado con alguna herramienta externa.</p>
	 * Adquiere por tanto todos los valores de dicho proyecto disponibles, tales como la
	 * matriz de contactos, R,S,I, tasas, etcetera.
	 * Esta opción elimina el resto de datos actuales de los módulos implicados.
	 * @param prjV Conjunto de datos del archivo de entrada.
	 * @param tipoModelo Cadena de texto con el tipo de modelo a cargar, siendo:
	 *  <p>TypesFiles.MODEL_A tipo de modelo A (Etiquetado por columnas).</p>
	 * 	<p>TypesFiles.MODEL_B tipo de modelo B (Etiquetado por filas).</p>
	 */
	private void importarModelo(DCVS prjV, String tipoModelo) {
		ParserModelo parser = null;
		//Limpiar todos los datos previos. No debe hacerse en otra parte pues
		// impediría modularidad e independencia de módulos.
		switch(tipoModelo) {
		case(TypesFiles.MODEL_A): parser = new ParserModeloA(prjV); break;
		case(TypesFiles.MODEL_B): parser = new ParserModeloB(prjV); break;
		}
		
		//Establecer los datos del proyecto primero (provoca clear). 
		abrirProyecto(parser.getPRJ());
		//Establecer datos de los grupos (Mapa).
		establecerDatos(parser.getMAP());
		//Establecer las zonas en las vistas correspondientes.
		setZonas(parser.getZonas());
		//Establecer datos propios de la enfermedad.
		establecerDatos(parser.getDEF());
		//Establecer matriz de relaciones.
		establecerDatos(parser.getREL());
		//Establecer el historico de niveles.
		establecerDatos(parser.getHST());
		//Establecer paleta por defecto.
		establecerDatos(parser.getPAL());
		//Reiniciar vistas.
		refresh();
	}
	
	/**
	 * <p>Ejecuta el cálculo de demolo SIR</p>
	 * La función no realiza comprobación de los valores iniciales. Dicha comprobación
	 *  debe ser realizada previamente. 
	 */
	private void runSIR() {	
		MotorSIR motor = new MotorSIR(modulos.get(TypesFiles.DEF), zonas, modulos.get(TypesFiles.REL));
		motor.start();
		//Establecer el historico de niveles.
		establecerDatos(motor.getHST());
	}
	
	/**
	 * Método main de la clase responsable del inicio de la aplicación.
	 * @param args no usado.
	 */
	public static void main(String[] args) {
		ControladorModulos cm = new ControladorModulos();
		cm.isPlayable();
	}
}
