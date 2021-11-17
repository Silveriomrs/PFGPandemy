/**
 * 
 */
package vista;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import controlador.ControladorModulos;
import modelo.FondoPanel;
import modelo.IO;

import java.awt.BorderLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

/**
 * Clase de la vista principal donde se modelan las tablas
 * que sean necesarias.
 * @author Silverio Manuel Rosales Santana.
 * @date 2021/04/10
 * @version 2.8
 *
 */
public class Principal extends JFrame {

	private static final long serialVersionUID = -1830456885294124447L;
	
	private HashMap<String, JMenuItem> jmitems;
	private FondoPanel fondo = new FondoPanel("/vista/imagenes/imagen4.jpg");
	private JPanel panelCentral;
	private ControladorModulos cm;
	//
	private JMenuBar menuBar;

	/**
	 * Crea el módulo principal de la aplicación.
	 * @param cm Controlador de las vistas y módulos.
	 */
	public Principal(ControladorModulos cm) {
		int w = 1024;
		int h = 768;
		this.cm = cm;	
		this.setTitle("Simulador de Pandemias");
		this.getContentPane().setBackground(Color.GRAY);
		this.setContentPane(fondo);	
		this.setBounds(0, 0, w + 25, h + 15);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		configurar();
		this.setVisible(true);
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
		fondo.add(menuBar, BorderLayout.NORTH);
		fondo.add(panelCentral, BorderLayout.CENTER);
	}
	
	/**
	 * <p>Title: addPanelToView</p>  
	 * <p>Description: Agrega un panel a la vista central.</p> 
	 * @param panel JPanel o vista para agregar.
	 */
	public void addPanelToView(JPanel panel) {
		panel.setVisible(false);
		panelCentral.add(panel,BorderLayout.CENTER);
	}
	
	/**
	 * <p>Title: iniciarMenuBar</p>  
	 * <p>Description: Genera una barra de herramientas y la añade
	 * al JPanel principal, localización Norte.</p> 
	 */
	private void iniciarMenuBar() {
		//Inicializar el HashMap
		jmitems = new HashMap<String,JMenuItem>();
		//Barra de menus.
		menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.NORTH);

		//Menu Archivo
		JMenu mnArchivo = new JMenu("Archivo");
		addJMenuItem(mnArchivo, "Nuevo Proyecto","/vista/imagenes/Iconos/portapapeles_64px.png" );
		mnArchivo.addSeparator();
		addJMenuItem(mnArchivo, "Abrir Proyecto","/vista/imagenes/Iconos/carpeta_64px.png" );
		//SubMenu VenSIM
		addJMenuItem(mnArchivo, "Importar Proyecto Vensim","/vista/imagenes/Iconos/portapapeles_64px.png" );
		addJMenuItem(mnArchivo, "Importar Histórico Vensim","/vista/imagenes/Iconos/portapapeles_64px.png" );
		mnArchivo.addSeparator();
		//
		addJMenuItem(mnArchivo, "Guardar Proyecto","/vista/imagenes/Iconos/disquete_64px.png" );
		mnArchivo.addSeparator();
		addJMenuItem(mnArchivo, "Salir","/vista/imagenes/Iconos/salir_64px.png" );
		
		//Menu Ver
		JMenu mnVer = new JMenu("Ver");
		addJMenuItem(mnVer, "Mapa","/vista/imagenes/Iconos/region_64px.png" );
		addJMenuItem(mnVer, "Tabla","/vista/imagenes/Iconos/hoja-de-calculo_64px.png" );
//		addJMenuItem(mnVer, "Archivos","/vista/imagenes/Iconos/archivo_64px.png" );
		addJMenuItem(mnVer, "Grupos","/vista/imagenes/Iconos/portapapeles_64px.png" );
		addJMenuItem(mnVer, "Proyecto","/vista/imagenes/Iconos/portapapeles_64px.png" );
		
		//Menu Ejecutar
		JMenu mnEjecutar = new JMenu("Ejecutar Modulo");
		addJMenuItem(mnEjecutar, "Reproductor","/vista/imagenes/Iconos/animar_128px.png" );
		addJMenuItem(mnEjecutar, "Editor Gráfico","/vista/imagenes/Iconos/editorGrafico_128px.png" );
		addJMenuItem(mnEjecutar, "Paleta","/vista/imagenes/Iconos/circulo-de-color_64px.png" );
		
		//Menu Ejecutar
		JMenu mnAyuda = new JMenu("Ayuda");
		addJMenuItem(mnAyuda, "Acerca de...","/vista/imagenes/LogoUNED.jpg" );
		
		//Añadir sub-menus a la barra de menus.
		menuBar.add(mnArchivo);
		menuBar.add(mnVer);
		menuBar.add(mnEjecutar);
		menuBar.add(mnAyuda);
		
		//Configurar estados de cada JMenuItem y/o sus menús según el contexto.
		actualizarJMItems();
	}
	
	/**
	 * <p>Title: actualizarJMItems</p>  
	 * <p>Description: Actualiza los JMenuItems en función del contexto de la aplicación</p>
	 * Actua leyendo el estado de la aplicación y activando o desactivado las funciones
	 * de los menús en base a los datos cargados y el estado previo de los diferentes módulos.
	 */
	private void actualizarJMItems() {
		//En caso de no zonas:
		boolean nozonas = cm.getZonas().size() > 0;
		//Desactivar vista mapa.
		jmitems.get("Mapa").setEnabled(nozonas);
		//Desactivar editor de zonas gráfico.
		jmitems.get("Editor Gráfico").setEnabled(nozonas);
		//Desactivar vistas de grupos.
		jmitems.get("Grupos").setEnabled(nozonas);

//		jmitems.get("Reproductor").setEnabled(cm.isPlayable());
		jmitems.get("Reproductor").setEnabled(nozonas);
		//En caso de no tener abierto proyecto (y luego no cambios)
		//Desactivar Guardar proyecto.
		jmitems.get("Guardar Proyecto").setEnabled(nozonas);
	}

	private void addJMenuItem(JMenu padre, String nombre, String rutaIcon) {
		JMenuItem item = new JMenuItem(new VerMenuListener(nombre));
		if(rutaIcon != null)  item.setIcon(IO.getIcon(rutaIcon,20,20));
		jmitems.put(nombre, item);
		padre.add(item);
	}
	
	 /**
	 * <p>Title: VerMenuListener</p>  
	 * <p>Description: Clase dedicada al establecimiento de los datos en los
	 * apartados o módulos oportunos.</p>  
	 * @author Silverio Manuel Rosales Santana
	 * @date 10 ago. 2021
	 * @version versión 1.1
	 */
	private class VerMenuListener extends AbstractAction {
		/** serialVersionUID*/  
		private static final long serialVersionUID = -5103462996882781094L;
		private String name;
		
		public VerMenuListener(String name) {
			super(name);
			this.name = name;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//Si se ha seleccionado módulo ->
			System.out.println("Principal AL: " + name);
			cm.doAction(name);
			actualizarJMItems();
		}
	}
	
	/* Funciones para pruebas */
	
	/**
	 * Método main de la clase.
	 * @param args no usado.
	 */
	public static void main(String[] args) {
		Principal ventana = new Principal(new ControladorModulos());
		ventana.setVisible(true);
	}
}

