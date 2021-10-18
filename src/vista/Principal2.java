/**
 * 
 */
package vista;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import controlador.ControladorDatosIO;
import controlador.ControladorMapa;
import modelo.DCVS;
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
public class Principal2 extends JFrame {

	private static final long serialVersionUID = -1830456885294124447L;

	private Archivos archivos;
	private ControladorDatosIO cio;
	private TablaEditor tablaEditor;
	private Pizarra pizarra;

	private About about;
	private FondoPanel fondo = new FondoPanel("/vista/imagenes/imagen4.jpg");
	private JPanel panelCentral;
	private ControladorMapa cMap;
	//
	private JMenuBar menuBar;
	private final int panelCentralW = 665;
	private final int panelCentralH = 456;

	/**
	 * Crea la aplicación.
	 */
	public Principal2() {
		cio = new ControladorDatosIO();
		cMap = new ControladorMapa(panelCentralW,panelCentralH);
		archivos = new Archivos(cio,cMap);
		tablaEditor = new TablaEditor(cio,cMap);
		pizarra = new Pizarra(cMap);
		about = new About();
		this.setTitle("Simulador de Pandemias");	
		this.getContentPane().setBackground(Color.GRAY);
		this.setContentPane(fondo);	
		this.setBounds(0, 0, 934, 662);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
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
		
		//Ocultar JPanels no primarios y mostrar el panel por defecto.
		mostrarPanel("Archivos");
		//Añadir paneles de los módulos.
		panelCentral.add(tablaEditor, null);
		panelCentral.add(archivos, null);
		panelCentral.add(cMap.getJMapa(), null);
		//Añadir elementos al JPanel principal.
		fondo.add(menuBar, BorderLayout.NORTH);
		fondo.add(panelCentral, BorderLayout.CENTER);
	}
	
	/**
	 * <p>Title: iniciarMenuBar</p>  
	 * <p>Description: Genera una barra de herramientas y la añade
	 * al JPanel principal, localización Norte.</p> 
	 */
	private void iniciarMenuBar() {
		//Barra de menus.
		menuBar = new JMenuBar();
		getContentPane().add(menuBar, BorderLayout.NORTH);

		//Menu Archivo
		JMenu mnArchivo = new JMenu("Archivo");
		addJMenuItem(mnArchivo, "Nuevo Proyecto","/vista/imagenes/Iconos/portapapeles_64px.png" );
		addJMenuItem(mnArchivo, "Abrir Proyecto","/vista/imagenes/Iconos/carpeta_64px.png" );
		addJMenuItem(mnArchivo, "Guardar Proyecto","/vista/imagenes/Iconos/disquete_64px.png" );
		
		//Menu Ver
		JMenu mnVer = new JMenu("Ver");
		addJMenuItem(mnVer, "Mapa","/vista/imagenes/Iconos/region_64px.png" );
		addJMenuItem(mnVer, "Tabla","/vista/imagenes/Iconos/hoja-de-calculo_64px.png" );
		addJMenuItem(mnVer, "Archivos","/vista/imagenes/Iconos/archivo_64px.png" );
		
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
	}

	private void addJMenuItem(JMenu padre, String nombre, String rutaIcon) {
		JMenuItem item = new JMenuItem(new VerMenuListener(nombre));
		if(rutaIcon != null)  item.setIcon(IO.getIcon(rutaIcon,20,20));
		padre.add(item);
	}
	
	 /**
	 * <p>Title: BtnAplicarTablaMouseListener</p>  
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
			System.out.println(name);
			//En cualquier caso actualizar.
			cMap.setModulos(archivos.getMapaModulos());
			switch(name){
				case "Reproductor":
					//Pasar mapa de módulos al controlar de mapa.
					if(cMap.play()) mostrarPanel("Mapa");
					break;
				case "Editor Gráfico":				
				//	pizarra.setZonas(cMap.getZonas());
					cMap.getMapa().setVisible(true);
					pizarra.testModulo(null);
					break;
				case "Paleta":
					Leyenda paleta = cMap.getPaleta();
					paleta.setVisible(true);
					break;
				case "Mapa":
				case "Tabla":
				case "Archivos":
					mostrarPanel(name);
					break;
				case "Abrir Proyecto":
					DCVS prj = cio.abrirArchivo(null,IO.PRJ);
					if(prj != null) {
						archivos.establecerDatos(prj);				
					}
					break;
				case "Nuevo Proyecto":
					
					break;
				case "Acerca de...":
					about.toggleVisible();
					break;
				default:
					System.out.println(name + ", tipo no reconocido");
			}	
		}
	}
	
	private void mostrarPanel(String nombre) {
		
		switch(nombre){
		case "Reproductor":

			break;
		case "Editor Gráfico":				

			break;
		case "Paleta":
			cMap.getPaleta().setVisible(true);
			break;
		case "Mapa":
			archivos.setVisible(false);
			tablaEditor.setVisible(false);
			cMap.getMapa().setVisible(true);
			break;
		case "Tabla":
			cMap.getMapa().setVisible(false);
			archivos.setVisible(false);
			tablaEditor.setVisible(true);
			break;
		case "Archivos":
			cMap.getMapa().setVisible(false);
			tablaEditor.setVisible(false);
			archivos.setVisible(true);			
			break;
		}
		panelCentral.updateUI();
		fondo.updateUI();
	}
	
	/**
	 * Método main de la clase.
	 * @param args no usado.
	 */
	public static void main(String[] args) {
		Principal2 ventana = new Principal2();
		ventana.setVisible(true);	
	}
}

