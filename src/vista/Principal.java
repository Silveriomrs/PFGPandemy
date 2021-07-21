/**
 * 
 */
package vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import controlador.ControladorDatosIO;
import modelo.DCVS;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;

/**
 * Clase de la vista principal donde se modelan las tablas
 * que sean necesarias.
 * @author Silverio Manuel Rosales Santana.
 * @date 2021/04/10
 * @version 1.5
 *
 */
public class Principal extends JFrame implements ActionListener{

	private static final long serialVersionUID = -1830456885294124447L;
	private JButton btnAbout,btnAbrirArchivo,btnGuardarArchivo,btnImprimir;
	private JButton btnAddRow,btnAddCol,btnBorrarFila,btnBorrarColumna;
	private ControladorDatosIO cio;
	private JScrollPane scrollPane;
	private JTable tabla;

	private DefaultTableModel modelo;
	private FondoPanel fondo = new FondoPanel();
	private final Panel panel = new Panel();
	private JLayeredPane layeredPane;
	private JLabel lblAsignarTablaA;
	private JComboBox<String> comboBox;
	private JButton btnAplicarTabla;
	private JLayeredPane boxAsignacion;

	/**
	 * Crea la aplicación.
	 */
	public Principal() {
		cio = new ControladorDatosIO();
		panel.setLayout(new GridLayout(1, 0, 0, 0));	
		setResizable(false);
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
		
		//Cuerpo de datos
		Object[][] datos = new Object[][]{
				{"AN", 1, null, null, null},
				{"AR", 2, null, null, null},
				{"PA", 3, null, null, null},
				{"BA", 4, null, null, null},
				{"CN", 5, null, null, null},
				{"CB", 6, null, null, null},
				{"CM", 7, null, null, null},
				{"CL", 8, null, null, null},
				{"CA", 9, null, null, null},
				{"EX", 10, null, null, null},
				{"GA", 11, null, null, null},
				{"RJ", 12, null, null, null},
				{"MD", 13, null, null, null},
				{"MU", 14, null, null, null},
				{"NV", 15, null, null, null},
				{"PV", 16, null, null, null},
				{"VL", 17, null, null, null},};
		
		//Cabecera de datos.		
		String[] cabecera = new String[] {"CCAA", "Valor1", "valor2", "valor3", "valor4"};

		btnAbout = new JButton("");
		btnAbout.addMouseListener(new BtnAboutMouseListener());
		btnAbout.setForeground(Color.GRAY);
		btnAbout.setBackground(Color.GRAY);
		btnAbout.setIcon(new ImageIcon(Principal.class.getResource("/vista/imagenes/LogoUNED.jpg")));
		btnAbout.addActionListener(this);
		
		scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		//scrollPane.setBounds(5, 10, 350, 150);
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
					.addGroup(gl_fondo.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_fondo.createSequentialGroup()
							.addGap(31)
							.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE))
						.addGroup(gl_fondo.createSequentialGroup()
							.addContainerGap(433, Short.MAX_VALUE)
							.addComponent(boxAsignacion, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
							.addGap(87)
							.addComponent(btnAbout, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)))
					.addGap(23))
		);
		gl_fondo.setVerticalGroup(
			gl_fondo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_fondo.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_fondo.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_fondo.createSequentialGroup()
							.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_fondo.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_fondo.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnAbout)
								.addComponent(boxAsignacion, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
							.addGap(23))))
		);
		
		comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Sin asignar", "Mapas", "Cálculo", "Diversidad", "Otro"}));
		
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
					.addGroup(gl_boxAsignacion.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(Alignment.LEADING, gl_boxAsignacion.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblAsignarTablaA, 0, 0, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_boxAsignacion.createSequentialGroup()
							.addGap(6)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnAplicarTabla)))
					.addContainerGap(6, Short.MAX_VALUE))
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
		
		btnAddRow = new JButton("Nueva fila");
		btnAddRow.setBounds(0, 12, 104, 25);
		btnAddRow.addMouseListener(new BtnAddRowMouseListener());
		btnAddRow.addActionListener(this);
		
		btnBorrarFila = new JButton("Borrar fila/s");
		btnBorrarFila.setBounds(0, 74, 120, 25);
		btnBorrarFila.addMouseListener(new BtnBorrarFilaMouseListener());
		btnBorrarFila.setBackground(Color.ORANGE);
		layeredPane.setLayout(null);
		layeredPane.add(btnAddRow);
		btnAddCol = new JButton("Añadir columna");
		btnAddCol.setBounds(0, 37, 143, 25);
		btnAddCol.addMouseListener(new BtnAddColMouseListener());
		btnAddCol.addActionListener(this);
		layeredPane.add(btnAddCol);
		layeredPane.add(btnBorrarFila);
		
		btnImprimir = new JButton("Imprimir");
		btnImprimir.setBounds(0, 236, 91, 25);
		btnImprimir.setVerticalAlignment(SwingConstants.BOTTOM);
		btnImprimir.setHorizontalAlignment(SwingConstants.LEFT);
		btnImprimir.addMouseListener(new BtnImprimirMouseListener());
		btnImprimir.addActionListener(this);
		
		btnBorrarColumna = new JButton("Borrar columna/s");
		btnBorrarColumna.setHorizontalTextPosition(SwingConstants.LEFT);
		btnBorrarColumna.setHorizontalAlignment(SwingConstants.LEFT);
		btnBorrarColumna.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnBorrarColumna.setBounds(0, 100, 160, 25);
		btnBorrarColumna.addMouseListener(new BtnBorrarColumnaMouseListener());
		btnBorrarColumna.setBackground(Color.ORANGE);
		layeredPane.add(btnBorrarColumna);
		
		btnGuardarArchivo = new JButton("Guardar tabla");
		btnGuardarArchivo.setBounds(0, 148, 140, 25);
		layeredPane.add(btnGuardarArchivo);
		btnGuardarArchivo.addMouseListener(new BtnGuardarArchivoMouseListener());
		btnGuardarArchivo.addActionListener(this);
		btnAbrirArchivo = new JButton("Cargar tabla");
		btnAbrirArchivo.setBounds(0, 175, 123, 25);
		layeredPane.add(btnAbrirArchivo);
		btnAbrirArchivo.addMouseListener(new BtnAbrirArchivoMouseListener());
		btnAbrirArchivo.addActionListener(this);
		layeredPane.add(btnImprimir);
		gl_fondo.setAutoCreateContainerGaps(true);
		gl_fondo.setAutoCreateGaps(true);
		
		tabla = new JTable();
		tabla.setAutoCreateRowSorter(true);
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabla.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
		tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tabla.setCellSelectionEnabled(false);
		tabla.setRowSelectionAllowed(true);
		tabla.setColumnSelectionAllowed(false);
		tabla.setBackground(new Color(224, 255, 255));
		modelo = new DefaultTableModel(datos,cabecera){
			private static final long serialVersionUID = 5615251971828569240L;
	//		@SuppressWarnings("rawtypes")
	//		Class[] columnTypes = new Class[] {Object.class, Object.class, Object.class, Object.class, Object.class};
	//		public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}  //Si deseo fijar el número de columnas.
		};
		
		tabla.setModel(new DefaultTableModel(datos,cabecera));
		tabla.getTableHeader().setReorderingAllowed(false);
	//	tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
	//	tabla.getColumnModel().getColumn(3).setMinWidth(105);
		scrollPane.setViewportView(tabla);
		fondo.setLayout(gl_fondo);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//if(e.getSource()==btnGuardarArchivo) {
			//
		//}
	}
	
	/**
	 * Función auxiliar. Muestra cuadros de mensajes. Los cuadros de mensajes
	 * no están enlazados con un hilo padre (null). Un número no definido se 
	 * mostrará como información.
	 * 
	 * El tipo 4 es usado para el botón "Acerca de...", re-escrito para mostrar un 
	 * mensaje tipo 1. 
	 * @param txt Texto a mostrar.
	 * @param titulo Texto a mostrar en la barra del cuadro de mensaje.
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
		//	JOptionPane.showMessageDialog(null, txt, titulo, tipo);ç
			String txt = JOptionPane.showInputDialog("¿Nombre de la nueva columna?");
			modelo.addColumn(txt);
			tabla.setModel(modelo);
		}
	}
	
	private class BtnAddRowMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			Object[] o = new Object[modelo.getColumnCount()];
			modelo.addRow(o);
			tabla.setModel(modelo);
		}
	}
	
	private class BtnAbrirArchivoMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			DefaultTableModel m = cio.abrirArchivo();			
			if(m != null) {
				modelo = m;
				tabla.setModel(modelo);
				mostrar("Archivo Cargado", 1);
			}else {mostrar("Nombre de archivo inválido", 0);}
		}
	}
	
	private class BtnAboutMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			mostrar("Este proyecto ha sido...", 4);
		}
	}
	
	private class BtnGuardarArchivoMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(cio.guardarArchivo(tabla.getModel())) {
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
	
	private class BtnAplicarTablaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			//Obtener selección
			String seleccion = (String) comboBox.getSelectedItem();
			//Si se ha seleccionado módulo ->
			System.out.println(seleccion);
			//enviar modelo al módulo elegido.
			
		}
	}
}



/**
 * Esta subclase establece una imagen de fondo al panel.
 * @author Silverio Manuel Rosales Santana
 * @date 10/05/2021
 * @version 1.0
 *
 */
class FondoPanel extends JPanel{
	/**
	 * Serialización de la clase.
	 */
	private static final long serialVersionUID = -6096941937803410903L;
	private Image imagen;
	
	@Override
	public void paint(Graphics g) {
		imagen = new ImageIcon(getClass().getResource("/vista/imagenes/imagen4.jpg")).getImage();
		g.drawImage(imagen,0,0,getWidth(),getHeight(),this);
		setOpaque(false);
		super.paint(g);
	}
}

