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
	private TablaEditor tablaEditor;
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
	private String panelActivo = "None";
	
	
	/**
	 * Constructor de la clase controladora.
	 */
	public ControladorModulos() {
		labels = new Labels();
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
		generarModulosBasicos();
		refresh();
	}
	
	/**
	 * <p>Genera los módulos básicos para operar con el sistema</p>
	 * Estos son los del proyecto y los parámetros de la enfermedad.
	 */
	private void generarModulosBasicos() {
		DCVS proyecto = DCVSFactory.newModule(TypesFiles.PRJ);
		//Procesarlo.
		abrirProyecto(proyecto);
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
		//Procesarlo.
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
		//Añadir datos para el guardado  e identifación.
		setProjectParameters(moduloZonas);
		//añadir el módulo al conjunto de módulos.
		modulos.put(moduloZonas.getTipo(),moduloZonas);
		//Procesarlo.
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
		setProjectParameters(relaciones);
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
		//Directorio de trabajo
		String wd = modulos.get(TypesFiles.PRJ).getDirectorio();
		if(wd != null && !wd.equals("")) {
			//Tipo
			String type = module.getTipo();
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
					separador + module.getNombre());
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
		addDefaultBorder(tablaEditor,Labels_GUI.L_BORDER_PANEL_TE);
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
	 * <p>Guarda la configuración del proyecto.</p> En caso de que
	 * el módulo recibido sea null, no realizará operación alguna. Cuando dicho
	 *  módulo no tiene establecido un directorio de trabajo, lo obtendrá del 
	 *   resultado de la operación.
	 * <P>Cuando se guarda los módulos existentes serán guardados en el mismo
	 * mismo directorio de trabajo, además de variar sus propiedades internas para
	 *  conservar dichos cambios.</P>
	 * Aquellos módulos creados pero sin nombre asignado obtendrán el nombre del
	 *  proyecto con la extensión propia de cada módulo.
	 * @param dcvsIn Módulo DCVS de entrada con configuración PRJ.
	 * @return TRUE si la operación se ha realizado correctamente. False en otro caso.
	 */
	private boolean saveProjectAs(DCVS dcvsIn) {
		boolean done = true;
		DCVS dcvs = dcvsIn;
		//Guardado del fichero: En name se almacena el nombre elegido con su extensión.
		String name = cio.guardarArchivo(dcvs);
		//Si no es nulo el módulo y no se ha cancelado el guardado -> procede.
		if(dcvs != null && name != null) {
			//Obtener nuevo nombre y directorio de trabajo usado en este guardado.
			String wd = IO.WorkingDirectory;
			//Configuración de la ruta
			dcvs.setRuta(wd + separador + name);
			dcvs.setName(name);
			dcvs.setDirectorio(wd);
			//Guardar todos los módulos en el mismo directorio de trabajo.												//Guardar proyecto
			saveAllTogether();													//Guardar resto	
			//Mostrar rutas en Field.
			archivos.refresh();
		}else done = false;
		
		return done;
	}
	
	/**
	 * Almacena los módulos actuales del sistema en el mismo
	 *  directorio de trabajo que el módulo principal (proyecto).
	 */
	private void saveAllTogether() {
		modulos.forEach((type,module)->{
			if(!type.equals(TypesFiles.PRJ)) {
				setProjectParameters(module);;
				//Re-integrar el módulo en el entorno.
				establecerDatos(module);
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
		tablaEditor.reset();
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
		//Reiniciar todas las vistas y borrar datos anteriores.
		clearProject();	

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
				else showMessage(Labels_GUI.ERR_MSG_1_CM + dato + "." + etiq,0);
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
		//Refrescar vista de Proyecto
		refresh();
	}
	
	/**
	 * <p>Introduce los datos (tipo y nombre del archivo del módulo)
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
	 * Establece el contenido del módulo cargado de acuerdo
	 * con su tipo, actualiza los elementos del JPanel correspondientes.
	 * @param datos Conjunto de datos y cabecera encapsulados.
	 */
	private void establecerDatos(DCVS datos) {
		String tipo = datos.getTipo();
		
		//Añadir la ruta del módulo al módulo del proyecto.
		//Los módulos PRJ están filtrados desde abrirProyecto y el ActionListener.
		insertInProjectModule(datos);
		
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
	 * @param nombre Nombre de la acción.
	 */
	public void doActionPrincipal(String nombre) {
		//
		switch(nombre){
			case Labels_GUI.W_PLAYER_TITLE:
				situarVentana(ModuleType.PLAYER, principal.getX() - 350, principal.getY() + h/3);
				play();
				mostrarPanel(Labels_GUI.W_MAP_TITLE);
				break;
			case Labels_GUI.W_GE_TITLE:
				this.pizarra.reset();
				this.pizarra.toogleVisible();
				break;
			case Labels_GUI.W_PE_TITLE:
				situarVentana(ModuleType.PAL, principal.getX() + w + 10, principal.getY());
				paleta.setEditable(true);
				paleta.setFrameVisible(true);
				break;
			case Labels_GUI.W_PAL_TITLE:
				situarVentana(ModuleType.PAL, principal.getX() + w + 10, principal.getY());
				paleta.setEditable(false);
				paleta.toggleFrameVisible();
				break;
			case Labels_GUI.W_MAP_TITLE:
			case Labels_GUI.W_GRP_TITLE:
			case Labels_GUI.W_TE_TITLE:
			case Labels_GUI.W_DEF_TITLE:
			case Labels_GUI.MVER_PRJ:
				mostrarPanel(nombre);
				break;
			case Labels_GUI.W_REL_TITLE: editModule(TypesFiles.REL); break;					//Abrir el editor de tablas con la matriz de contactos.
			case Labels_GUI.M_OPEN_PRJ:
				DCVS prj = cio.abrirArchivo(null,TypesFiles.PRJ);
				if(prj != null) {
					clearProject();
					abrirProyecto(prj);
				}
				break;
			case Labels_GUI.M_IMPORT_PA:
				DCVS pVS = cio.abrirArchivo(null,TypesFiles.CSV);
				//Si se ha abierto el archivo, procesarlo.
				if(pVS != null && pVS.getValueAt(0,0).equals("0")) {
					clearProject();
					importarModelo(pVS,TypesFiles.MODEL_A);
				}
				else if(pVS != null) showMessage(Labels_GUI.ERR_FILE_UNKNOWN,0);
				break;
			case Labels_GUI.M_IMPORT_PB:	
				//Formato de importación CSV
				DCVS hVS = cio.abrirArchivo(null,TypesFiles.CSV);
				//Si se ha abierto el archivo, procesarlo..
				if(hVS != null && hVS.getColumnName(1).equals("0")) {
					clearProject();
					importarModelo(hVS,TypesFiles.MODEL_B);
				}
				else if(hVS != null) showMessage(Labels_GUI.ERR_FILE_UNKNOWN,0);
				break;		
			case Labels_GUI.M_NEW_PRJ:
				generarModulosBasicos();
				break;
			case Labels_GUI.M_SAVE_PRJ:
				saveProjectAs(modulos.get(TypesFiles.PRJ));
				break;
			case Labels_GUI.M_EXIT:
				if(showMessage(Labels_GUI.WARNING_1_DATA_LOSS + "\n" + Labels_GUI.REQUEST_EXIT_CONFIRM,3) == JOptionPane.YES_OPTION) System.exit(0);
				break;
			case Labels_GUI.MHELP_TABLES:
				cio.openPDF("mTablas");
				break;
			case Labels_GUI.MHELP_USER_GUIDE:
				cio.openPDF("mUsuario");
				break;
			case Labels_GUI.MHELP_ABOUT:
				about.toggleVisible();
				break;
			default:
				System.out.println("CM > doPrincipal: " + nombre + ", opción no reconocida");
		}
	}
	
	/**
	 * Establece la vista que debe ser visible.
	 * @param nombre Nombre de la vista a hacer visible.
	 */
	private void mostrarPanel(String nombre) {
		//Mostrar panel correspondiente y ocultación del resto.
		mapa.setVisible(nombre.equals(Labels_GUI.W_MAP_TITLE));					
		tablaEditor.setVisible(nombre.equals(Labels_GUI.W_TE_TITLE));
		pgrupos.setVisible(nombre.equals(Labels_GUI.W_GRP_TITLE));
		pproyecto.setVisible(nombre.equals(Labels_GUI.MVER_PRJ));
		vistaSIR.setVisible(nombre.equals(Labels_GUI.W_DEF_TITLE));
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
	 * @param op Tipo de operación a realizar. (Guardar o Aplicar cambios de poligonos).
	 * @return TRUE si la operación ha tenido exito, FALSE en otro caso.
	 */
	public boolean doActionPizarra(String op) {
		boolean done = true;
		OperationsType opr = OperationsType.getNum(op);
		switch(opr) {
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
			done = saveModule(TypesFiles.MAP,modulos.get(TypesFiles.MAP).getNombre() == null);
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
		DCVS dcvs = cio.abrirArchivo(null,ext);
		boolean ok = dcvs != null;
		if(ok && !ext.equals(TypesFiles.PRJ)) {
			//En caso de mapa o de relaciones, el módulo debe tener definidos tantos grupos
			// como haiga definido en el proyecto.
			int NG = getNumberZonas();
			
			if((ext.equals(TypesFiles.REL) || ext.equals(TypesFiles.MAP))
					&& dcvs.getRowCount() != NG ) {
				showMessage(Labels_GUI.ERR_LOAD_MODULE + dcvs.getRuta() + "\n" + Labels_GUI.ERR_MODULE_LESS_NG,0);
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
		//Optención del módulo correspondiente
		DCVS dcvs = getModule(ext);
		boolean done = dcvs != null;
		if(done && !ext.equals(TypesFiles.PRJ)) {
			//Ruta a null. En otro caso guardará con la ruta definida en dicho módulo.
			if(as) dcvs.setRuta(null);
			//
			cio.guardarArchivo(dcvs);
		}else {
			done = saveProjectAs(dcvs);
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
	 * @param op Operación a realizar "Abrir", "Guardar", "Guardar como".
	 * @param ext Extensión del tipo de datos, equivalente a los disponibles en
	 * la clase IO.
	 * @return TRUE si la operación ha concluido correctamente, FALSE en otro caso.
	 */
	public boolean doActionArchivos(String op, String ext) {
		boolean ok = true;														//Indica si la operación ha tenido exito o no.
		OperationsType opr = OperationsType.getNum(op);
		//Opciones de Carga de módulo, NO módulo PRJ.
		switch(opr) {
		case OPEN:  ok = openModule(ext); break;
		case DELETE: ok = removeModule(ext); break;
		case EDIT: 
			editModule(ext);
			ok = false;															//Evitar desabilitar botón de guardar como.
		break;
		default:
			boolean as = opr == OperationsType.SAVE_AS;
			ok = saveModule(ext,as);
		}
		
		refresh();
		return ok;
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
	public boolean doActionTableEditor(DCVS modulo) {
		boolean done = true;
		if(modulo != null) {
			establecerDatos(modulo);
			//indicar al módulo de archivos que modulo se ha modificado para activar 
			//El botón de guardar correspondiente.
			archivos.enableBotonesGuardado(modulo.getTipo(), true);
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
	 * Asigna un valor a una etiqueta en el módulo específicado.
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
	 * Asigna un valor a una etiqueta. Si la etiqueta no existe, la crea.
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
		
		if(op == OperationsType.UPDATE) done = updateMSIR();
		else {
			done = updateMSIR();
			//Checar si valores OK
			// Si valores OK. Ejecutar motorSIR
			// runSIR(double pte, double dme, boolean ip, double dmi) 
			runSIR();
		}
		
		//Solo es la vista de las zonas => solo puede haber ocurrido cambios a guardar.
		archivos.enableBotonesGuardado(TypesFiles.DEF, true);
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
		int NG  = Integer.parseInt(datos.get(Labels.NG));
		
		//Actualizar datos de las etiquetas del módulo del proyecto.
		datos.forEach((label,data) ->{
			setValueAtLabel(TypesFiles.PRJ ,label ,data);
		});
		
		//En caso de no haber un módulo de grupos de población, generar y agregarlo.
		if(!modulos.containsKey(TypesFiles.MAP)) {
			generarModuloMAP();
			generarModuloREL();
		}
		
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
