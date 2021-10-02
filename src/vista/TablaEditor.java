/**  
* <p>Title: TablaEditor.java</p>  
* <p>Description: </p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 30 sept. 2021  
* @version 1.0  
*/  
package vista;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.awt.*;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import controlador.ControladorDatosIO;
import controlador.ControladorMapa;
import modelo.DCVS;
import modelo.IO;


/**
 * <p>Title: TablaEditor</p>  
 * <p>Description: Clase editora de los datos de entrada en formato CSV</p>
 * Permite la edición de los datos principales de cada módulo perteneciente
 * al programa.  
 * @author Silverio Manuel Rosales Santana
 * @date 30 sept. 2021
 * @version versión 1.0
 */
public class TablaEditor extends JPanel{

	/** serialVersionUID*/  
	private static final long serialVersionUID = -3765555595448024955L;

	//
	private final String IAbrir = "/vista/imagenes/Iconos/carpeta_64px.png";
	private final String IGuardarA = "/vista/imagenes/Iconos/disquete_64px.png";
	private final String IVentana = "/vista/imagenes/Iconos/editorGrafico_128px.png";
	//
	private JButton btnAbrirArchivo,btnGuardarArchivo,btnBorrarTabla,btnImprimir;
	private JButton btnAddRow,btnAddCol,btnNuevaTabla,btnBorrarFila,btnBorrarColumna;
	private JScrollPane scrollPane;
	private JTable tabla;

	private DefaultTableModel modelo;
	private JLabel lblAsignarTablaA;
	private JComboBox<String> comboBox;
	private JButton btnAplicarTabla;
	private ControladorMapa cMap;
	private ControladorDatosIO cio;
	private JToolBar jtoolBar;
	private JPanel boxAsignacion;
	private JFrame frame;

	
	/**
	 * <p>Title: TablaEditor</p>  
	 * <p>Description: Constructor principal</p> 
	 * @param cio Controlador de entrada y salida de datos al disco.
	 * @param cMap Controlador de los datos relacionados con el mapa.
	 */
	public TablaEditor(ControladorDatosIO cio, ControladorMapa cMap ) {
		setName("panel_tabla");
		setMaximumSize(new Dimension(1024, 768));
		setBorder(null);
		setOpaque(false);
		this.cio = cio;
		this.cMap = cMap;
		initialize();
	}

	/**
	 * <p>Title: abrirFrame</p>  
	 * <p>Description: Abre el módulo en un frame particular</p> 
	 */
	public void abrirFrame() {
		frame = new JFrame("Simulador de Pandemias");
		frame.setTitle("Editor CSV");
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setBounds(0, 0, 914, 610);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(this);
		//getIcon(IVentana,128,128)
		frame.setIconImage(getImage(IVentana));
		frame.pack();
		frame.setVisible(true);
	}

	
	/**
	 * <p>Title: getPanel</p>  
	 * <p>Description: Retorna el panel configurado</p> 
	 * @return Este panel.
	 */
	public JPanel getPanel() {return this;}
	
	/**
	 * Inicialización de los contenidos del frame.
	 */
	private void initialize() {
		setBounds(0, 0, 914, 610);	
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setBackground(Color.GRAY);
		scrollPane.setOpaque(false);
		scrollPane.setAutoscrolls(true);
		scrollPane.setViewportBorder(null);
		scrollPane.setName("scrollTabla");
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		//ToolBar
		jtoolBar = new JToolBar(JToolBar.VERTICAL);
		jtoolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		jtoolBar.setForeground(Color.BLACK);
		jtoolBar.setName("Barra Herramientas");
		jtoolBar.setOpaque(false);
		jtoolBar.setOrientation(JToolBar.VERTICAL);

		//Añadir los botones
		addBotonToolBar(btnNuevaTabla, "Crea una nueva tabla desde una plantilla","/vista/imagenes/Iconos/nuevaTabla_64px.png",new BtnNuevaTablaMouseListener(),Color.GREEN);
		addBotonToolBar(btnAddRow, "Crear fila nueva","/vista/imagenes/Iconos/nuevaFila_64px.png",new BtnAddRowMouseListener(),null);
		addBotonToolBar(btnAddCol, "Crear columna nueva","/vista/imagenes/Iconos/nuevaColumna_64px.png",new BtnAddColMouseListener(),null);
		jtoolBar.addSeparator();
		addBotonToolBar(btnBorrarFila, "Elimina las filas marcadas","/vista/imagenes/Iconos/eliminarFila_64px.png",new BtnBorrarFilaMouseListener(),Color.ORANGE);
		addBotonToolBar(btnBorrarColumna, "Elimina las columnas indicas","/vista/imagenes/Iconos/eliminarCol_64px.png",new BtnBorrarColumnaMouseListener(),Color.ORANGE);
		addBotonToolBar(btnBorrarTabla, "Borrar tabla","/vista/imagenes/Iconos/borrarTabla_64px.png",new BtnBorrarTablaMouseListener(),Color.ORANGE);
		jtoolBar.add(Box.createHorizontalGlue());
		jtoolBar.addSeparator();
		addBotonToolBar(btnGuardarArchivo, "Guardar tabla",IGuardarA,new BtnGuardarArchivoMouseListener(),null);
		addBotonToolBar(btnAbrirArchivo, "Cargar tabla",IAbrir,new BtnAbrirArchivoMouseListener(),null);
		jtoolBar.addSeparator();
		addBotonToolBar(btnImprimir, "Imprimir","/vista/imagenes/Iconos/impresora_64px.png",new BtnImprimirMouseListener(),null);

		//BoxAsignación (JPanel)
		boxAsignacion = new JPanel();
		boxAsignacion.setOpaque(false);
		boxAsignacion.setBorder(null);
		boxAsignacion.setBackground(Color.ORANGE);
		
		comboBox = new JComboBox<String>();
		comboBox.setMaximumSize(new Dimension(102, 25));
		comboBox.setMinimumSize(new Dimension(102, 25));
		comboBox.setName("Asignar tabla");
		comboBox.setToolTipText("Seleccione el módulo que desea asignar esta tabla.");
		comboBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Sin asignar", "Mapas", "Historico", "Leyenda", "Relaciones"}));
		//jtoolBar.add(comboBox);
		
		btnAplicarTabla = new JButton("Aplicar tabla");
		btnAplicarTabla.setHorizontalAlignment(SwingConstants.LEFT);
		btnAplicarTabla.addMouseListener(new BtnAplicarTablaMouseListener());
		//jtoolBar.add(btnAplicarTabla);
		
		lblAsignarTablaA = new JLabel("Asignar la tabla al módulo");
		lblAsignarTablaA.setForeground(UIManager.getColor("Button.highlight"));
		lblAsignarTablaA.setFont(new Font("Fira Code Retina", Font.BOLD, 15));
		lblAsignarTablaA.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lblAsignarTablaA.setBackground(Color.WHITE);

		boxAsignacion.add(lblAsignarTablaA);
		boxAsignacion.add(comboBox);
		boxAsignacion.add(btnAplicarTabla);
		
		//Creación tabla principal.
		tabla = new JTable();
		tabla.setOpaque(false);
		tabla.setAutoCreateRowSorter(true);
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabla.setBorder(null);
		tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tabla.setCellSelectionEnabled(false);
		tabla.setRowSelectionAllowed(true);	
		tabla.setColumnSelectionAllowed(false);
		tabla.setBackground(new Color(224, 255, 255));
		tabla.getTableHeader().setReorderingAllowed(false);
		scrollPane.getViewport().setOpaque(false);

		//Genera el cuerpo de datos y establace.
		nuevaTabla();
		scrollPane.setViewportView(tabla);
		//Colocación del listener para adaptar la tabla según ancho y con ancho mínimo
//		tabla.getParent().addComponentListener(new ComponentAdapter() {
//		    @Override
//		    public void componentResized(final ComponentEvent e) {
//		        if (tabla.getPreferredSize().width < tabla.getParent().getWidth()) {
//		            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//		        } else {
//		            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		        }
//		    }
//		});
		setLayout(new BorderLayout(0, 0));

		add(boxAsignacion, BorderLayout.SOUTH);
		add(jtoolBar, BorderLayout.WEST);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * <p>Title: addBotonToolBar</p>  
	 * <p>Description: Añade los controles la barra de herramietnas que los contendrá</p>
	 * Los añade con el formato correcto y una separación costante, además,
	 * permite que los botones puedan tener colores personalizados.
	 * @param btn Botón a ser añadido.
	 * @param tooltip Texto de ayuda al botón.
	 * @param ruta Ruta completa hasta el archivo de imagen.
	 * @param ml El observador o listener adjunto al botón.
	 * @param c Color para el botón, null en otro caso.
	 */
	private void addBotonToolBar(JButton btn, String tooltip,String ruta, MouseAdapter ml, Color c) {
		int hi,wi;
		hi = wi =  25;
		btn = new JButton();
		//btn.setSize(new Dimension(102, 25));
		btn.setHorizontalTextPosition(SwingConstants.RIGHT);
		btn.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.addMouseListener(ml);
		btn.setToolTipText(tooltip);
		if(c != null) btn.setBackground(c);
		addIconB(btn,ruta,wi,hi);
		jtoolBar.add(btn);
		
		
	}
	
	/**
	 * <p>Title: addIconB</p>  
	 * <p>Description: Añade un icono a una botón</p>
	 * Los valores de dimensión de ancho y largo se establecen en función de los 
	 * datos pasados por parámetro. 
	 * @param componente Etiqueta a la que adjuntar el icono
	 * @param ruta Nombre del archivo y su ruta.
	 * @param w Ancho a escalar de la imagen original.
	 * @param h Alto a escalar de la imagen original.
	 */
	private void addIconB(JButton componente, String ruta, int w, int h) {
		componente.setIconTextGap(0);
		componente.setIcon(getIcon(ruta,w,h));	
	}
	
	/**
	 * <p>Title: getImage</p>  
	 * <p>Description: Abre una imagen desde la ruta especificada.</p> 
	 * @param ruta Ruta hasta el archivo de imagen.
	 * @return Imagen especificada. Null en otro caso.
	 */
	private Image getImage(String ruta) {
		Image img = null;
		try {
			img = new ImageIcon(Archivos.class.getResource(ruta)).getImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return img;
		}
		return img;
	}
	
	/**
	 * <p>Title: getIcon</p>  
	 * <p>Description: Devuelve un icono escalado a las medidas obtenidas
	 * por parámetros, de la imagen fuente..</p> 
	 * @param ruta Ruta del archivo de imagen.
	 * @param w Ancho del escalado de la imagen.
	 * @param h Alto del escalado de la imagen.
	 * @return Icono escalado de la imagen, null en otro caso.
	 */
	private ImageIcon getIcon(String ruta, int w, int h) {
		ImageIcon icon = null;
		Image img = getImage(ruta);
		if(img != null) icon = new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH));
		return icon;
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
			DCVS dcvs = cio.abrirArchivo(null,IO.CSV);			
			if(dcvs != null) {
				DefaultTableModel modelo = dcvs.getModelo();
				tabla.setModel(modelo);
				mostrar("Archivo Cargado", 1);
			}
		}
	}
	
	
	private class BtnGuardarArchivoMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			DCVS bd = new DCVS( (DefaultTableModel)tabla.getModel());
			bd.setTipo(IO.CSV);
			if(cio.guardarArchivo(bd) != null) {
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
					if(modelo != null && modelo.getRowCount() > 0) {
						cMap.setPoligonos(modelo);
						mostrar("Mapa cargado en el módulo", 1);
						nuevaTabla();
					}
					break;
				case "Historico": 
					cMap.setHistorico(modelo);
					nuevaTabla();
					break;
				case "Leyenda":
					cMap.setPaleta(modelo);
					mostrar("Nueva paleta asignada", 1);
					nuevaTabla();
					break;
				case "Relaciones":
					break;
			}	
		}
	}
	
	private class BtnBorrarTablaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			//Mostrar dialogo de confirmación
			int opt = JOptionPane.showConfirmDialog(null, "¿Desea eliminar la tabla actual con sus datos?", "Aviso", JOptionPane.YES_NO_OPTION);
			//Caso afirmativo borrar el modelo y crear uno nuevo.
			if(opt == JOptionPane.YES_OPTION) {	nuevaTabla(); }
		}
	}


	private class BtnNuevaTablaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			//Mostrar dialogo de confirmación
			int opt = JOptionPane.showConfirmDialog(null, "Eliminará la tabla actual con sus datos ¿Desea continuar?", "Aviso", JOptionPane.YES_NO_OPTION);
			//Caso afirmativo borrar el modelo y crear uno nuevo.
			if(opt == JOptionPane.YES_OPTION) {	nuevaTabla(); }
		}
	}


	
	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		TablaEditor te = new TablaEditor(new ControladorDatosIO(), new ControladorMapa(665,456));
		te.abrirFrame();
	}

}
