/**
 * 
 */
package vista;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Image;

import controlador.ControladorDatosIO;
import javax.swing.JScrollPane;

import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.SwingConstants;

/**
 * @author Silverio Manuel Rosales Santana
 * @date
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

	/**
	 * Crea la aplicación.
	 */
	public Principal() {
		setResizable(false);
		setTitle("Simulador de Pandemias");
		cio = new ControladorDatosIO();
		getContentPane().setBackground(Color.GRAY);
		this.setContentPane(fondo);
		setVisible(true);	
		initialize();
	}

	/**
	 * Inicialización de los contenidos del frame.
	 */
	private void initialize() {
		setBounds(100, 100, 900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Cuerpo de datos
		Object[][] datos = new Object[][]{
				{"AN", null, null, null, null},
				{"AR", null, null, null, null},
				{"PA", null, null, null, null},
				{"BA", null, null, null, null},
				{"CN", null, null, null, null},
				{"CB", null, null, null, null},
				{"CM", null, null, null, null},
				{"CL", null, null, null, null},
				{"CA", null, null, null, null},
				{"EX", null, null, null, null},
				{"GA", null, null, null, null},
				{"RJ", null, null, null, null},
				{"MD", null, null, null, null},
				{"MU", null, null, null, null},
				{"NV", null, null, null, null},
				{"PV", null, null, null, null},
				{"VL", null, null, null, null},};
		
		//Cabecera de datos.		
		String[] cabecera = new String[] {"CCAA", "Valor1", "valor2", "valor3", "valor4"};
		//Tipos de columna.
		@SuppressWarnings("rawtypes")
		Class[] columnTypes = new Class[] {Object.class, Object.class, Object.class, Object.class, Object.class};
		
		btnAbout = new JButton("");
		btnAbout.addMouseListener(new BtnAboutMouseListener());
		btnAbout.setForeground(Color.GRAY);
		btnAbout.setBackground(Color.GRAY);
		btnAbout.setIcon(new ImageIcon(Principal.class.getResource("/vista/imagenes/LogoUNED.jpg")));
		btnAbout.addActionListener(this);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setName("scrollTabla");
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		btnAddRow = new JButton("Nueva fila");
		btnAddRow.addMouseListener(new BtnAddRowMouseListener());
		btnAddRow.addActionListener(this);
		btnAddCol = new JButton("Añadir columna");
		btnAddCol.addMouseListener(new BtnAddColMouseListener());
		btnAddCol.addActionListener(this);
		
		btnBorrarFila = new JButton("Borrar fila");
		btnBorrarFila.addMouseListener(new BtnBorrarFilaMouseListener());
		btnBorrarFila.setBackground(Color.ORANGE);
		
		btnBorrarColumna = new JButton("Borrar columna");
		btnBorrarColumna.addMouseListener(new BtnBorrarColumnaMouseListener());
		btnBorrarColumna.setBackground(Color.ORANGE);
		btnAbrirArchivo = new JButton("Cargar tabla");
		btnAbrirArchivo.addMouseListener(new BtnAbrirArchivoMouseListener());
		btnAbrirArchivo.addActionListener(this);
		
		btnImprimir = new JButton("Imprimir");
		btnImprimir.setVerticalAlignment(SwingConstants.BOTTOM);
		btnImprimir.setHorizontalAlignment(SwingConstants.LEFT);
		btnImprimir.addMouseListener(new BtnImprimirMouseListener());
		btnImprimir.addActionListener(this);
		
		btnGuardarArchivo = new JButton("Guardar tabla");
		btnGuardarArchivo.addMouseListener(new BtnGuardarArchivoMouseListener());
		btnGuardarArchivo.addActionListener(this);
		
		GroupLayout gl_fondo = new GroupLayout(getContentPane());
		gl_fondo.setHorizontalGroup(
			gl_fondo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_fondo.createSequentialGroup()
					.addGap(46)
					.addGroup(gl_fondo.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAddCol)
						.addComponent(btnBorrarFila)
						.addComponent(btnBorrarColumna)
						.addComponent(btnAbrirArchivo)
						.addComponent(btnGuardarArchivo)
						.addComponent(btnAddRow)
						.addComponent(btnImprimir))
					.addGap(43)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 596, GroupLayout.PREFERRED_SIZE)
					.addGap(72))
				.addGroup(Alignment.TRAILING, gl_fondo.createSequentialGroup()
					.addContainerGap(747, Short.MAX_VALUE)
					.addComponent(btnAbout, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
					.addGap(39))
		);
		gl_fondo.setVerticalGroup(
			gl_fondo.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_fondo.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
					.addComponent(btnAbout)
					.addGap(24))
				.addGroup(gl_fondo.createSequentialGroup()
					.addGap(68)
					.addComponent(btnAddRow)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAddCol)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnBorrarFila)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnBorrarColumna)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnAbrirArchivo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnGuardarArchivo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnImprimir)
					.addContainerGap(282, Short.MAX_VALUE))
		);
		
		tabla = new JTable();
		//tabla.setBorder(UIManager.getBorder("Table.scrollPaneBorder"));
		tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tabla.setCellSelectionEnabled(true);
		tabla.setColumnSelectionAllowed(true);
		tabla.setBackground(new Color(224, 255, 255));
		modelo = new DefaultTableModel(datos,cabecera){
			private static final long serialVersionUID = 5615251971828569240L;
		//	public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}  //Si deseo fijar el número de columnas.
		};
		
		tabla.setModel(modelo);
		tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
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
	 * Función auxiliara para mostrar cuadros de mensajes.
	 * @param txt texto a mostrar.
	 */
	private void mostrar(String txt) {
		JOptionPane.showMessageDialog(null, txt);
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
			modelo.addColumn("test");
			modelo.setColumnCount(modelo.getColumnCount() + 1);
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
			modelo = cio.abrirArchivo();			
			if(modelo != null) {
				tabla.setModel(modelo);
				mostrar("Archivo Cargado");
			}
		}
	}
	private class BtnAboutMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			mostrar("Acerca de...");
		}
	}
	
	private class BtnGuardarArchivoMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(cio.guardarArchivo(tabla.getModel())) {mostrar("Archivo guardado");}
		}
	}
	private class BtnBorrarFilaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("Pulsado borrar fila");
		}
	}
	private class BtnBorrarColumnaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("Pulsado borrar columna");
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

