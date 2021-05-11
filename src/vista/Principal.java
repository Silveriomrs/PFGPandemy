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

/**
 * @author Silverio Manuel Rosales Santana
 * @date
 * @version 1.5
 *
 */
public class Principal extends JFrame implements ActionListener{

	private static final long serialVersionUID = -1830456885294124447L;
	private JButton btnAbout,btnAbrirArchivo,btnGuardarArchivo;
	private ControladorDatosIO cio;
	private JScrollPane scrollPane;
	private JTable tabla;
	private JButton btnImprimir;
	private DefaultTableModel modelo;
	private FondoPanel fondo = new FondoPanel();
	private JButton btnAddRow;
	private JButton btnAddCol;
	private JButton btnBorrarFila;
	private JButton btnBorrarColumna;

	/**
	 * Crea la aplicación.
	 */
	public Principal() {
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
		setBounds(100, 100, 694, 543);
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
		
		btnAbout = new JButton("Acerca...");
		btnAbout.addActionListener(this);
		
		btnGuardarArchivo = new JButton("Guardar tabla");
		btnGuardarArchivo.addActionListener(this);
		btnAbrirArchivo = new JButton("Cargar tabla");
		btnAbrirArchivo.addActionListener(this);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setName("scrollTabla");
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		btnImprimir = new JButton("Imprimir");
		btnImprimir.addActionListener(this);
		
		btnAddRow = new JButton("Nueva fila");
		btnAddRow.addActionListener(this);
		btnAddCol = new JButton("Añadir columna");
		btnAddCol.addActionListener(this);
		
		btnBorrarFila = new JButton("Borrar fila");
		btnBorrarFila.setBackground(Color.ORANGE);
		
		btnBorrarColumna = new JButton("Borrar columna");
		btnBorrarColumna.setBackground(Color.ORANGE);
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(24)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnAbout)
							.addPreferredGap(ComponentPlacement.RELATED, 179, Short.MAX_VALUE)
							.addComponent(btnGuardarArchivo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAbrirArchivo)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnImprimir))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnAddRow)
								.addComponent(btnBorrarFila)
								.addComponent(btnBorrarColumna)
								.addComponent(btnAddCol))
							.addPreferredGap(ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 444, GroupLayout.PREFERRED_SIZE)))
					.addGap(30))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(48)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnAddRow)
									.addGap(1)
									.addComponent(btnAddCol)
									.addGap(32)
									.addComponent(btnBorrarFila)
									.addGap(1)
									.addComponent(btnBorrarColumna)))
							.addContainerGap(129, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(431)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAbout)
								.addComponent(btnImprimir)
								.addComponent(btnAbrirArchivo)
								.addComponent(btnGuardarArchivo))
							.addContainerGap())))
		);
		
		tabla = new JTable();
		tabla.setColumnSelectionAllowed(true);
		tabla.setBackground(new Color(224, 255, 255));
		modelo = new DefaultTableModel(datos,cabecera){
			private static final long serialVersionUID = 5615251971828569240L;
		//	public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}  //Si deseo fijar el número de columnas.
		};
		
		tabla.setModel(modelo);
		tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
		scrollPane.setViewportView(tabla);
		getContentPane().setLayout(groupLayout);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * String opcion,bt, ba, bg, bi; opcion = e.getSource().toString(); bt =
		 * btnTest.toString(); ba = btnAbrirArchivo.toString(); bg =
		 * btnGuardarArchivo.toString(); bi = btnImprimir.toString(); switch(opcion){
		 * case (bt): break; }
		 */
	
		if(e.getSource()==btnAbout) {mostrar("Acerca de...");}
		else if(e.getSource()==btnAbrirArchivo) {
			modelo = cio.abrirArchivo();			
			if(modelo != null) {
				tabla.setModel(modelo);
				mostrar("Archivo Cargado");
			}
		}
		else if(e.getSource()==btnGuardarArchivo) {
			if(cio.guardarArchivo(this.tabla.getModel())) {mostrar("Archivo guardado");}
		}else if(e.getSource()==btnImprimir) {
			try {tabla.print();}
			catch (PrinterException e1) {e1.printStackTrace();}
		}else if(e.getSource()== btnAddRow) {
			Object[] o = new Object[modelo.getColumnCount()];
			modelo.addRow(o);
			tabla.setModel(modelo);		
		}else if(e.getSource()== btnAddCol) {
			modelo.addColumn("test");
			modelo.setColumnCount(modelo.getColumnCount() + 1);
			tabla.setModel(modelo);	
	
		}
	}
	
	/**
	 * Función auxiliara para mostrar cuadros de mensajes.
	 * @param txt texto a mostrar.
	 */
	private void mostrar(String txt) {
		JOptionPane.showMessageDialog(null, txt);
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

