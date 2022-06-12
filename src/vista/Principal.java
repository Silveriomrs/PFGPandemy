/**
 * 
 */
package vista;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import controlador.ControladorModulos;
import controlador.IO;
import modelo.TypesFiles;

import java.awt.BorderLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Clase de la vista principal donde se cargan los menús y los elementos que siver de
 * soporte al resto de vistas de la aplicación.
 * @author Silverio Manuel Rosales Santana.
 * @date 2021/04/10
 * @version 2.9
 *
 */
public class Principal extends JFrame {

	private static final long serialVersionUID = -1830456885294124447L;
	/** FRAME Configuración del tamaño en pixels de la aplicación con el marco.*/  
	public static Dimension FRAME = new Dimension(1024,768);
	
	private HashMap<String, JMenuItem> jmitems;
	private FondoPanel fondo = new FondoPanel("/vista/imagenes/agua_800px.png");
	private JPanel panelCentral;
	private ControladorModulos cm;
	//
	private JMenuBar mBar;

	/**
	 * Crea el módulo principal de la aplicación.
	 * @param cm Controlador de las vistas y módulos.
	 */
	public Principal(ControladorModulos cm) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/vista/imagenes/LogoUNED.jpg")));
		int w = 1024;
		int h = 768;
		this.cm = cm;
		//Configurar frame.
		this.setTitle("Simulación de enfermedades transmisibles en varios grupos de población");
		this.getContentPane().setBackground(Color.GRAY);
		this.setContentPane(fondo);	
		this.setBounds(0, 0, w, h + 45);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		configurar();
		this.setVisible(true);
		//Clase privada para salir controlando guardar o no los cambios realizados.
		this.addWindowListener(new WindowListener());
		//Desactivar el cierre automático para relizar un cierre controlado.
	    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * Inicialización de los contenidos del frame.
	 */
	private void configurar() {		
		fondo.setLayout(new BorderLayout(0, 0));	
		iniciarMenuBar();
		
		panelCentral = new JPanel();
		panelCentral.setOpaque(false);
		panelCentral.setMaximumSize(new Dimension(2767, 2767));
		panelCentral.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);	
		panelCentral.setLayout(new BorderLayout(0, 0));

		//Añadir elementos al JPanel principal.
		fondo.add(mBar,BorderLayout.NORTH);
		fondo.add(panelCentral, BorderLayout.CENTER);
	}
	
	/**
	 * Agrega un panel a la vista central. 
	 * @param panel JPanel o vista para agregar.
	 */
	public void addPanelToView(JPanel panel) {
		panel.setVisible(false);
		panelCentral.add(panel,BorderLayout.CENTER);
	}
	
	/**
	 * Genera una barra de herramientas y la añade al JPanel principal, localización Norte. 
	 */
	private void iniciarMenuBar() {
		//Inicializar el HashMap
		this.jmitems = new HashMap<String,JMenuItem>();
		this.mBar = new JMenuBar();
		//Menu Archivo
		JMenu mnArchivo = new JMenu("Archivo");
		addJMenuItem(mnArchivo, "Nuevo Proyecto","/vista/imagenes/Iconos/portapapeles_64px.png" );
		mnArchivo.addSeparator();
		addJMenuItem(mnArchivo, "Abrir Proyecto","/vista/imagenes/Iconos/carpeta_64px.png" );
		//SubMenu VenSIM
		addJMenuItem(mnArchivo, "Importar Modelo A","/vista/imagenes/Iconos/portapapeles_64px.png" );
		addJMenuItem(mnArchivo, "Importar Modelo B","/vista/imagenes/Iconos/portapapeles_64px.png" );
		mnArchivo.addSeparator();
		//
		addJMenuItem(mnArchivo, "Guardar Proyecto","/vista/imagenes/Iconos/disquete_64px.png" );
		mnArchivo.addSeparator();
		addJMenuItem(mnArchivo, "Salir","/vista/imagenes/Iconos/salir_64px.png" );
		
		//Menu Ver
		JMenu mnVer = new JMenu("Ver");
		addJMenuItem(mnVer, "Proyecto","/vista/imagenes/Iconos/portapapeles_64px.png" );	
		addJMenuItem(mnVer, "Parámetros SIR","/vista/imagenes/Iconos/portapapeles_64px.png" );
		addJMenuItem(mnVer, "Grupos","/vista/imagenes/Iconos/portapapeles_64px.png" );
		mnVer.addSeparator();
		addJMenuItem(mnVer, "Mapa","/vista/imagenes/Iconos/region_64px.png" );
		addJMenuItem(mnVer, "Reproductor","/vista/imagenes/Iconos/animar_128px.png" );
		addJMenuItem(mnVer, "Paleta","/vista/imagenes/Iconos/circulo-de-color_64px.png" );
			
		
		//Menu Herramientas
		JMenu mnHerramientas = new JMenu("Herramientas");
		addJMenuItem(mnHerramientas, "Tabla","/vista/imagenes/Iconos/hoja-de-calculo_64px.png" );
		mnHerramientas.addSeparator();
		addJMenuItem(mnHerramientas, "Editor Gráfico","/vista/imagenes/Iconos/editorGrafico_128px.png" );
		addJMenuItem(mnHerramientas, "Editor Paleta","/vista/imagenes/Iconos/circulo-de-color_64px.png" );
		
		//Menu Ayuda
		JMenu mnAyuda = new JMenu("Ayuda");
		addJMenuItem(mnAyuda, "Acerca de...","/vista/imagenes/LogoUNED.jpg" );
		
		//Añadir sub-menus a la barra de menus.
		mBar.add(mnArchivo);
		mBar.add(mnVer);
		mBar.add(mnHerramientas);
		mBar.add(mnAyuda);
		
		//Configurar estados de cada JMenuItem y/o sus menús según el contexto.
		actualizarJMItems();
	}
	
	/**
	 * <p>Reinia la vista.</p>
	 * Provoca una lectura de los datos requeridos para que la cosistencia de
	 *  la vista sea adecuada. 
	 */
	public void reset() {
		actualizarJMItems();
	}
	
	/**
	 * Refresca la vista con los datos del módulo. 
	 */
	public void refresh() {
		actualizarJMItems();
	}
	
	/**
	 * <p>Actualiza los JMenuItems en función del contexto de la aplicación.</p>
	 * Actua leyendo el estado de la aplicación y activando o desactivado las funciones
	 * de los menús en base a los datos cargados y el estado previo de los diferentes módulos.
	 */
	private void actualizarJMItems() {
		//En caso de no zonas:
		boolean hasZonas = cm.hasZonas();
		//Desactivar vista mapa.
		jmitems.get("Mapa").setEnabled(hasZonas);
		//Desactivar editor de zonas gráfico.
		jmitems.get("Editor Gráfico").setEnabled(hasZonas);
		//Desactivar vistas de grupos.
		jmitems.get("Grupos").setEnabled(hasZonas);
		//Desactivar vista de Parámetros de la enfermedad.
		jmitems.get("Parámetros SIR").setEnabled(cm.hasModule(TypesFiles.DEF));
//		jmitems.get("Reproductor").setEnabled(cm.isPlayable());
		jmitems.get("Reproductor").setEnabled(hasZonas);
		//En caso de no tener abierto proyecto (y luego no cambios)
		//Desactivar Guardar proyecto.
		jmitems.get("Guardar Proyecto").setEnabled(hasZonas);
	}

	/**
	 * Añade un elemento a la barra de menú.
	 * @param padre Elemento padre del que colgará la opción de menú.
	 * @param nombre Nombre con el que aparecerá en el menú.
	 * @param rutaIcon Ruta a un icono que será añadido a la opción.
	 */
	private void addJMenuItem(JMenu padre, String nombre, String rutaIcon) {
		JMenuItem item = new JMenuItem(new VerMenuListener(nombre));
		if(rutaIcon != null)  item.setIcon(IO.getIcon(rutaIcon,20,20));
		jmitems.put(nombre, item);
		padre.add(item);
	}
	
	 /**
	 * Clase dedicada al establecimiento de los datos en los apartados o módulos oportunos.  
	 * @author Silverio Manuel Rosales Santana
	 * @date 10 ago. 2021
	 * @version versión 1.1
	 */
	private class VerMenuListener extends AbstractAction {
		/** serialVersionUID*/  
		private static final long serialVersionUID = -5103462996882781094L;
		private String name;
		
		/**
		 * Esstablece como propiedad el nombre de la clase con el que se
		 *  identificará ante el controlador de módulos y así identificar la acción
		 *   requerida.  
		 * @param name Nombre del control al que se asocia.
		 */
		public VerMenuListener(String name) {
			super(name);
			this.name = name;
		}
		
		/**
		 * Sobrescritura del método heredado, le pasa el nombre del control asociado
		 *  al controlador de módulos y actualiza los menús de la barra en función
		 *   del contexto actual.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//Si se ha seleccionado módulo ->
			cm.doActionPrincipal(name);
			actualizarJMItems();
		}
	}
	
	/**
	 * <p>Clase privada para controlar el cierre de la aplicación.</p>
	 * El objetivo principal es permitir a la clase controladora interrogar sobre
	 * la operación, así como realizar el guardado de los datos antes de salir
	 * de manera abrupta o incontrolada.  
	 * @author Silverio Manuel Rosales Santana
	 * @date 18 nov. 2021
	 * @version versión 1.0
	 */
	private class WindowListener extends WindowAdapter {
		
			/**
			 * Al sobrescribir este método se fuerza que la aplicación fuerce al
			 *  usuario a confirmar la acción, de esta manera se da seguridad extra
			 *   al eviar la perdida accidental de datos.
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				cm.doActionPrincipal("Salir");
		    }
	}
}

