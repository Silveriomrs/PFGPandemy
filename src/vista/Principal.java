/**
 * 
 */
package vista;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import controlador.ControladorDatosIO;
import controlador.ControladorMapa;
import modelo.DCVS;
import modelo.FondoPanel;
import modelo.IO;

import java.awt.BorderLayout;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

	private Archivos archivos;
	private ControladorDatosIO cio;
	private TablaEditor tablaEditor;
	private HashMap<String, JMenuItem> jmitems;
	private Pizarra pizarra;
	private Parametros pparametros;
	private JPanel mapa;

	private About about;
	private FondoPanel fondo = new FondoPanel("/vista/imagenes/imagen4.jpg");
	private JPanel panelCentral;
	private ControladorMapa cMap;
	private String panelActivo;
	//
	private JMenuBar menuBar;
	private final int w = 1024;
	private final int h = 768;

	/**
	 * Crea el módulo principal de la aplicación.
	 */
	public Principal() {
		cio = new ControladorDatosIO();
		cMap = new ControladorMapa(w,h);
		//cMap = new ControladorMapa(panelCentralW,panelCentralH);
		archivos = new Archivos(cMap);
		tablaEditor = new TablaEditor(cMap);
		pizarra = new Pizarra(cMap.getZonas());
		pparametros = new Parametros("Test",4);
		about = new About();
		panelActivo = "Mapa";
		this.setTitle("Simulador de Pandemias");
		this.getContentPane().setBackground(Color.GRAY);
		this.setContentPane(fondo);	
		this.setBounds(0, 0, w + 25, h + 15);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		initialize();
		this.setVisible(true);
	}

	/**
	 * Inicialización de los contenidos del frame.
	 */
	private void initialize() {		
		fondo.setLayout(new BorderLayout(0, 0));	
		iniciarMenuBar();
		
		panelCentral = new JPanel();
		panelCentral.setOpaque(false);
		panelCentral.setMaximumSize(new Dimension(2767, 2767));
		panelCentral.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);	
		panelCentral.setLayout(new BorderLayout(0, 0));
		
		//Añadir paneles de los módulos.
		mapa = cMap.getMapa();
		panelCentral.add(tablaEditor, BorderLayout.CENTER);
		panelCentral.add(archivos, BorderLayout.CENTER);
		panelCentral.add(mapa,BorderLayout.CENTER);
		panelCentral.add(pparametros,BorderLayout.CENTER);

		//Añadir elementos al JPanel principal.
		fondo.add(menuBar, BorderLayout.NORTH);
		fondo.add(panelCentral, BorderLayout.CENTER);
		
		//Ocultar JPanels no primarios y mostrar el panel por defecto.
		mostrarPanel(panelActivo);
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
		addJMenuItem(mnArchivo, "Importar Proyecto Vensim","/vista/imagenes/Iconos/portapapeles_64px.png" );
		mnArchivo.addSeparator();
		addJMenuItem(mnArchivo, "Guardar Proyecto","/vista/imagenes/Iconos/disquete_64px.png" );
		mnArchivo.addSeparator();
		addJMenuItem(mnArchivo, "Salir","/vista/imagenes/Iconos/salir_64px.png" );
		
		//Menu Ver
		JMenu mnVer = new JMenu("Ver");
		addJMenuItem(mnVer, "Mapa","/vista/imagenes/Iconos/region_64px.png" );
		addJMenuItem(mnVer, "Tabla","/vista/imagenes/Iconos/hoja-de-calculo_64px.png" );
		addJMenuItem(mnVer, "Archivos","/vista/imagenes/Iconos/archivo_64px.png" );
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
		boolean nozonas = cMap.getZonas().size() > 0;
		//Desactivar vista mapa.
		jmitems.get("Mapa").setEnabled(nozonas);
		//Desactivar editor de zonas gráfico.
		jmitems.get("Editor Gráfico").setEnabled(nozonas);
		//Desactivar reproductor.
	//	jmitems.get("Reproductor").setEnabled(cMap.isPlayable());
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
	
	private int getPosX() {	return (int) this.getBounds().getX();}
	
	private int getPosY() {	return (int) this.getBounds().getY();}
	
	 /**
	 * <p>Title: VerMenuListener</p>  
	 * <p>Description: Clase dedicada al establecimiento de los datos en los
	 * apartados o módulos oportunos.</p>  
	 * @author Silverio Manuel Rosales Santana
	 * @date 10 ago. 2021
	 * @version versión
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
			//
			switch(name){
				case "Reproductor":
					//Pasar mapa de módulos al controlar de mapa.
					if(cMap.isPlayable()) {
						cMap.play();
						mostrarPanel("Reproductor");
					}
					break;
				case "Editor Gráfico":				
					pizarra = new Pizarra(cMap.getZonas());
					pizarra.abrirFrame();
					break;
				case "Mapa":
					cMap.setMapaVisible(true);
				//	cMap.getMapa().verFrame(true);
					mostrarPanel(name);
					break;
				case "Paleta":
				case "Tabla":
				case "Archivos":
				case "Proyecto":
					mostrarPanel(name);
					break;
				case "Abrir Proyecto":
					DCVS prj = cio.abrirArchivo(null,IO.PRJ);
					if(prj != null) archivos.abrirProyecto(prj);
					break;
				case "Importar Proyecto Vensim":
					//Hay que conocer la extensión que usa VenSim en sus proyectos.
//					DCVS prjV = cio.abrirArchivo(null,IO.PRJ);
					//Requiere nuevo parser completo.
//					if(prjV != null) archivos.abrirProyecto(prjV);
					break;
				case "Nuevo Proyecto":
					Parametros.main(null);
					break;
				case "Salir":
					if(mostrar("¿Desea salir del programa?",3) == JOptionPane.YES_OPTION) System.exit(0);
					break;
				case "Acerca de...":
					about.toggleVisible();
					break;
				default:
					System.out.println(name + ", tipo no reconocido");
			}
			actualizarJMItems();
		}
	}
	
	private void mostrarPanel(String nombre) {
		boolean traza = false;
		
		switch(nombre){
		case "Reproductor":
			cMap.situarVentana(ControladorMapa.REPRODUCTOR,getPosX() - 350, getPosY() + h/3);
			mostrarPanel("Mapa");
			break;
		case "Editor Gráfico":
			break;
		case "Paleta":
			cMap.situarVentana(ControladorMapa.LEYENDA,getPosX() + w + 10, getPosY());
			cMap.getPaleta().toggleVisible();
			break;
		case "Mapa":
		case "Tabla":
		case "Archivos":
		case "Proyecto":	
			cMap.setMapaVisible(nombre.equals("Mapa"));							//Mostrar panel correspondiente y ocultación del resto.
			archivos.setVisible(nombre.equals("Archivos"));
			tablaEditor.setVisible(nombre.equals("Tabla"));
			pparametros.setVisible(nombre.equals("Proyecto"));
			break;
		}
		
		if(traza) System.out.println("Principal - Mostrar Panel > " + nombre);
		
//		fondo.updateUI();
//		panelCentral.updateUI();
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

	
	/**
	 * Método main de la clase.
	 * @param args no usado.
	 */
	public static void main(String[] args) {
		Principal ventana = new Principal();
		ventana.setVisible(true);	
	}
}

