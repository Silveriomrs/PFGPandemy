/**
 * 
 */
package vista;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.ComponentOrientation;
import controlador.ControladorDatosIO;
import modelo.DCVS;

import javax.swing.JScrollPane;

import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
	private JButton btnTest,btnAbrirArchivo,btnGuardarArchivo;
	private ControladorDatosIO cio;
	private JScrollPane scrollPane;
	private JTable tabla;
	private JButton btnImprimir;
	private DefaultTableModel modelo;

	/**
	 * Crea la aplicación.
	 */
	public Principal() {
		cio = new ControladorDatosIO();
		getContentPane().setBackground(Color.GRAY);
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
		String[] cabecera = new String[]{"CCAA", "Valor1", "valor2", "valor3", "valor4"};
		
		
		btnTest = new JButton("Test");
		btnTest.addActionListener(this);
		
		btnGuardarArchivo = new JButton("Guardar archivo");
		btnGuardarArchivo.addActionListener(this);
		btnAbrirArchivo = new JButton("Abrir archivo");
		btnAbrirArchivo.addActionListener(this);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
		scrollPane.setName("Tabla 2");
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		btnImprimir = new JButton("Imprimir");
		btnImprimir.addActionListener(this);
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnTest)
							.addPreferredGap(ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 384, GroupLayout.PREFERRED_SIZE)
							.addGap(89))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnAbrirArchivo)
							.addContainerGap(559, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnGuardarArchivo)
							.addContainerGap(535, Short.MAX_VALUE))))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(66)
					.addComponent(btnImprimir)
					.addContainerGap(511, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(272)
							.addComponent(btnAbrirArchivo)
							.addGap(14)
							.addComponent(btnGuardarArchivo)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnTest))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(97)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
					.addComponent(btnImprimir)
					.addGap(33))
		);
		
		tabla = new JTable();
		tabla.setModel(new DefaultTableModel(datos,cabecera) {
			private static final long serialVersionUID = 5615251971828569240L;
			Class[] columnTypes = new Class[] {Object.class, Object.class, Object.class, Object.class, Object.class};
			public Class<?> getColumnClass(int columnIndex) {return columnTypes[columnIndex];}
		});
		//Establezco un ancho especial para la columna 0.
		tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
		scrollPane.setViewportView(tabla);
		getContentPane().setLayout(groupLayout);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==btnTest) {JOptionPane.showMessageDialog(null, "Test OK");}
		else if(e.getSource()==btnAbrirArchivo) {
			this.tabla = cio.abrirArchivo();
			scrollPane.setViewportView(tabla);
			JOptionPane.showMessageDialog(null, "Archivo Cargado");
		}
		else if(e.getSource()==btnGuardarArchivo) {
			cio.guardarArchivo(this.tabla.getModel());
			JOptionPane.showMessageDialog(null, "Archivo guardado");
		}else if(e.getSource()==btnImprimir) {
			DCVS d = new DCVS();
			d.setModelo(modelo);
			System.out.println(d.toString());
			try {tabla.print();}
			catch (PrinterException e1) {e1.printStackTrace();}
		}
	}

	/**
	 * @return devuelve modelo
	 */
	public DefaultTableModel getModelo() {return modelo;}

	/**
	 * @param modelo el modelo a establecer
	 */
	public void setModelo(DefaultTableModel modelo) {this.modelo = modelo;}
}
