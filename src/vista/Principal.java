/**
 * 
 */
package vista;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import controlador.ControladorDatosIO;
import controlador.ControladorMapa;
import modelo.DCVS;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.Font;
import modelo.FondoPanel;
import modelo.IO;
import java.awt.BorderLayout;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * Clase de la vista principal donde se modelan las tablas
 * que sean necesarias.
 * @author Silverio Manuel Rosales Santana.
 * @date 2021/04/10
 * @version 1.5
 *
 */
public class Principal extends JFrame {

	private static final long serialVersionUID = -1830456885294124447L;
	private JButton btnAbout,btnAbrirArchivo,btnGuardarArchivo,btnBorrarTabla,btnImprimir;
	private JButton btnAddRow,btnAddCol,btnBorrarFila,btnBorrarColumna;
	private ControladorDatosIO cio;
	private JScrollPane scrollPane;
	private JTable tabla;
	private Archivos archivos;
	private About about;

	private DefaultTableModel modelo;
	private final FondoPanel fondo = new FondoPanel("/vista/imagenes/imagen4.jpg");
	private final Panel panel = new Panel();
	private JLabel lblAsignarTablaA;
	private JComboBox<String> comboBox;
	private JButton btnAplicarTabla;
	private ControladorMapa cMap;
	private JLayeredPane layeredPane;
	private JLayeredPane boxAsignacion;
	private final int panelCentralW = 665;
	private final int panelCentralH = 456;

	/**
	 * Crea la aplicación.
	 */
	public Principal() {
		setResizable(false);
		cio = new ControladorDatosIO();
		cMap = new ControladorMapa(panelCentralW,panelCentralH);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		about = new About();
		archivos = new Archivos(cio);
		setTitle("Simulador de Pandemias");	
		getContentPane().setBackground(Color.GRAY);
		this.setContentPane(fondo);
		setVisible(true);	
		initialize();
		this.setLocationRelativeTo(null);
	}

	/**
	 * Inicialización de los contenidos del frame.
	 */
	private void initialize() {
		setBounds(0, 0, 914, 610);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		btnAbout = new JButton("");
		btnAbout.addMouseListener(new BtnAboutMouseListener());
		btnAbout.setForeground(Color.GRAY);
		btnAbout.setBackground(Color.GRAY);
		btnAbout.setIcon(new ImageIcon(Principal.class.getResource("/vista/imagenes/LogoUNED.jpg")));
		
		scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setName("scrollTabla");
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		layeredPane = new JLayeredPane();
		
		boxAsignacion = new JLayeredPane();
		boxAsignacion.setBorder(new LineBorder(new Color(0, 0, 0)));
		boxAsignacion.setBackground(Color.WHITE);
		
		GroupLayout gl_fondo = new GroupLayout(getContentPane());
		gl_fondo.setHorizontalGroup(
			gl_fondo.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_fondo.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_fondo.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_fondo.createSequentialGroup()
							.addGap(22)
							.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
							.addGap(33)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, panelCentralW, Short.MAX_VALUE))
						.addGroup(gl_fondo.createSequentialGroup()
							.addComponent(boxAsignacion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(76)
							.addComponent(btnAbout, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)))
					.addGap(23))
		);
		gl_fondo.setVerticalGroup(
			gl_fondo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_fondo.createSequentialGroup()
					.addGroup(gl_fondo.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_fondo.createSequentialGroup()
							.addGap(24)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, panelCentralH, Short.MAX_VALUE))
						.addGroup(gl_fondo.createSequentialGroup()
							.addGap(37)
							.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_fondo.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnAbout)
						.addComponent(boxAsignacion, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
					.addGap(23))
		);
		
		addBotonLayerPane(btnAddRow, "Crear Fila",new BtnAddRowMouseListener(),null,10);
		addBotonLayerPane(btnAddCol, "Crear Columna",new BtnAddColMouseListener(),null,35);
		addBotonLayerPane(btnBorrarFila, "Del fila/s",new BtnBorrarFilaMouseListener(),Color.ORANGE,70);
		addBotonLayerPane(btnBorrarColumna, "Del columna/s",new BtnBorrarColumnaMouseListener(),Color.ORANGE,95);
		addBotonLayerPane(btnGuardarArchivo, "Guardar tabla",new BtnGuardarArchivoMouseListener(),null,130);
		addBotonLayerPane(btnAbrirArchivo, "Cargar tabla",new BtnAbrirArchivoMouseListener(),null,155);
		addBotonLayerPane(btnBorrarTabla, "Borrar tabla",new BtnBorrarTablaMouseListener(),Color.ORANGE,198);
		addBotonLayerPane(btnImprimir, "Imprimir",new BtnImprimirMouseListener(),null,236);
		
		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Sin asignar", "Mapas", "Historico", "Ver Tabla", 
				"Ver Mapa", "Ver Archivos","Abrir Reproductor"}));
		
		btnAplicarTabla = new JButton("Aplicar tabla");
		btnAplicarTabla.addMouseListener(new BtnAplicarTablaMouseListener());
		
		lblAsignarTablaA = new JLabel("Asignar la tabla al módulo");
		lblAsignarTablaA.setForeground(UIManager.getColor("Button.highlight"));
		lblAsignarTablaA.setFont(new Font("Fira Code Retina", Font.BOLD, 15));
		lblAsignarTablaA.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lblAsignarTablaA.setBackground(Color.WHITE);
		
		GroupLayout gl_boxAsignacion = new GroupLayout(boxAsignacion);
		gl_boxAsignacion.setHorizontalGroup(
			gl_boxAsignacion.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_boxAsignacion.createSequentialGroup()
					.addGroup(gl_boxAsignacion.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_boxAsignacion.createSequentialGroup()
							.addGap(6)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnAplicarTabla))
						.addGroup(gl_boxAsignacion.createSequentialGroup()
							.addGap(58)
							.addComponent(lblAsignarTablaA, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_boxAsignacion.setVerticalGroup(
			gl_boxAsignacion.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_boxAsignacion.createSequentialGroup()
					.addGap(6)
					.addComponent(lblAsignarTablaA)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_boxAsignacion.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAplicarTabla))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		boxAsignacion.setLayout(gl_boxAsignacion);
		
		gl_fondo.setAutoCreateContainerGaps(true);
		gl_fondo.setAutoCreateGaps(true);
		//Creación tabla principal.
		tabla = new JTable();
		tabla.setAutoCreateRowSorter(true);
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabla.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
		tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tabla.setCellSelectionEnabled(false);
		tabla.setRowSelectionAllowed(true);
		tabla.setColumnSelectionAllowed(false);
		tabla.setBackground(new Color(224, 255, 255));
		tabla.getTableHeader().setReorderingAllowed(false);
		//Genera el cuerpo de datos y establace.
		nuevaTabla();
		scrollPane.setViewportView(tabla);
		
		//Barra de menus.
		JMenuBar menuBar = new JMenuBar();
		this.add(menuBar, BorderLayout.NORTH);
		
//		fondo.add(scrollPane, BorderLayout.EAST);
//		fondo.add(layeredPane, BorderLayout.WEST);
//		fondo.add(lblAsignarTablaA,BorderLayout.SOUTH);
//		fondo.add(btnAbout,BorderLayout.SOUTH);

		JMenu mnArchivo = new JMenu("Archivo");
		JMenu mnVer = new JMenu("Ver");

		JMenuItem mntmAbrirProyecto = new JMenuItem("Abrir Proyecto");
		JMenuItem mntmGuardarProj = new JMenuItem("Guardar Proyecto");
		JMenuItem mntmtest = new JMenuItem("test");

		mnArchivo.add(mntmGuardarProj);
		mnArchivo.add(mntmAbrirProyecto);
		mnVer.add(mntmtest);
		menuBar.add(mnArchivo);
		menuBar.add(mnVer);
		
		fondo.setLayout(gl_fondo);
	
	}
	
	/**
	 * <p>Title: addBotonLayerPane</p>  
	 * <p>Description: Añade los controles al LayerPane que los contendrá</p>
	 * Los añade con el formato correcto y una separación costante, además,
	 * permite que los botones puedan tener colores personalizados.
	 * @param btn Botón a ser añadido.
	 * @param nombre Texto del botón.
	 * @param ml El observador o listener adjunto al botón.
	 * @param c Color para el botón, null en otro caso.
	 * @param yPos Posición vertical dentro del LayerPane
	 */
	private void addBotonLayerPane(JButton btn, String nombre, MouseAdapter ml, Color c, int yPos) {
		btn = new JButton(nombre);
		btn.addMouseListener(ml);
		if(c != null) btn.setBackground(c);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		int yPos2  = layeredPane.getComponentCount()*30;
		btn.setBounds(0, yPos2, 140, 25);
		layeredPane.add(btn);
	}
	
	/**
	 * <p>Title: nuevaTabla</p>  
	 * <p>Description: Crea una nueva tabla sin columnas ni filas..</p>
	 */
	private void nuevaTabla() {
		//Cuerpo de datos
		Object[][] datos = new Object[][]{};	
		//Cabecera de datos.		
		String[] cabecera = new String[] {};
		modelo = new DefaultTableModel(datos,cabecera){
			private static final long serialVersionUID = 5615251971828569240L;
	//		@SuppressWarnings("rawtypes")
	//		Class[] columnTypes = new Class[] {Object.class, Object.class, Object.class, Object.class, Object.class};
	//		public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}  //Si deseo fijar el número de columnas.
		};
		tabla.setModel(modelo);
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
	 *  0 JOptionPane.ERROR_MESSAGE
	 *  1 JOptionPane.INFORMATION_MESSAGE
	 *  2 JOptionPane.WARNING_MESSAGE
	 *  3 JOptionPane.QUESTION_MESSAGE  
	 *  4 JOptionPane.PLAIN_MESSAGE
	 */
	private void mostrar(String txt, int tipo ) {
		String titulo = "";
		switch(tipo) {
		case 0: titulo = "Error";break;
		case 1: titulo = "Información"; break;
		case 2: titulo = "¡Antención!"; break;
		case 3: titulo = "Consulta"; break;
		case 4: titulo = "Acerca de..."; tipo = 1; break;
		default:
			titulo = "";
		}
		JOptionPane.showMessageDialog(null, txt, titulo, tipo);
	}
	
	private class BtnImprimirMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			try {tabla.print();}
			catch (PrinterException e1) {e1.printStackTrace();}
		}
	}
	
	private class BtnAddColMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
		//	JOptionPane.showMessageDialog(null, txt, titulo, tipo);
			String txt = JOptionPane.showInputDialog("¿Nombre de la nueva columna?");
				if(txt != "" || txt != null){
				modelo.addColumn(txt);
				tabla.setModel(modelo);
			}
		}
	}
	
	private class BtnAddRowMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			int ncols = modelo.getColumnCount();								//Obtención del número de columnas.
			if(ncols > 0) {														//Comprobación de que existe al menos una columna.
				Object[] o = new Object[ncols];
				modelo.addRow(o);
				tabla.setModel(modelo);
			}
		}
	}
	
	private class BtnAbrirArchivoMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			DefaultTableModel m = cio.abrirArchivo(null,IO.CSV).getModelo();			
			if(m != null) {
				modelo = m;
				tabla.setModel(modelo);
				mostrar("Archivo Cargado", 1);
			}
		}
	}
	
	private class BtnAboutMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			about.setVisible();
		}
	}
	
	private class BtnGuardarArchivoMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			DCVS bd = new DCVS( (DefaultTableModel)tabla.getModel());
			bd.setTipo(IO.CSV);
			if(!cio.guardarArchivo(bd).equals(null)) {
				mostrar("Archivo guardado", 1);
			}
		}
	}
	
	private class BtnBorrarFilaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			DCVS dcvs = new DCVS(modelo);
			dcvs.delFilas(tabla.getSelectedRows());
			tabla.setModel(modelo);
		}
	}
	
	private class BtnBorrarColumnaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {	
			int[] cols = tabla.getSelectedColumns();							//Obtención de las columnas a eliminar.
			modelo = new DCVS(modelo).delColumnas(cols);						//Creación del modelo nuevo sin las columnas indicadas
			tabla.setModel(modelo);												//Establecemos el nuevo modelo en la tabla.
		}
	}
	
	/**
	 * <p>Title: BtnAplicarTablaMouseListener</p>  
	 * <p>Description: Clase dedicada al establecimiento de los datos en los
	 * apartados o módulos oportunos.</p>  
	 * @author Silverio Manuel Rosales Santana
	 * @date 10 ago. 2021
	 * @version versión
	 */
	private class BtnAplicarTablaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			//Obtener selección
			String seleccion = (String) comboBox.getSelectedItem();
			//Si se ha seleccionado módulo ->
			System.out.println(seleccion);
			
			switch(seleccion){
				case "Mapas": 
					cMap.setPoligonos(modelo);
					mostrar("Mapa cargado en el módulo", 1);
					nuevaTabla();				
					break;
				case "Historico": 
					cMap.setHistorico(modelo);
					nuevaTabla();
					break;
				case "Leyenda":
					cMap.setPaleta(modelo);
					mostrar("Nueva paleta asignada", 1);
					nuevaTabla();
				case "Abrir Reproductor":
					//Pasar mapa de módulos al controlar de mapa.
					cMap.setModulos(archivos.getMapaModulos());
					cMap.play();
					break;
				case "Ver Mapa":
					scrollPane.setViewportView(cMap.getMapa());
					break;
				case "Ver Tabla":
					scrollPane.setViewportView(tabla);
					break;
				case "Ver Archivos":
					scrollPane.setViewportView(archivos.getPanel());
			}	
		}
	}
	
	private class BtnBorrarTablaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			//Mostrar dialogo de confirmación
			int opt = JOptionPane.showConfirmDialog(null, "¿Desea eliminar la tabla y sus datos?", "Aviso", JOptionPane.YES_NO_OPTION);
			//Caso afirmativo borrar el modelo y crear uno nuevo.
			if(opt == JOptionPane.YES_OPTION) {	nuevaTabla(); }
		}
	}
}

