/**
 * 
 */
package vista;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.TextArea;
import java.awt.Color;
import java.awt.ComponentOrientation;
import javax.swing.JTabbedPane;

import controlador.ControladorDatosIO;
import modelo.IO;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * @author Silverio Manuel Rosales Santana
 * @date
 * @version 1.5
 *
 */
public class Principal extends JFrame implements ActionListener{

	private static final long serialVersionUID = -1830456885294124447L;
	private JButton btnTest,btnAbrirArchivo,btnGuardarArchivo;
	private TextArea textArea;
	private JTabbedPane tabbedPane;
	private JPanel panel_ayuda;
	private JPanel panel_archivo;
	private ControladorDatosIO cio;
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
		setBounds(100, 100, 614, 459);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 5, 5);
		flowLayout.setAlignOnBaseline(true);
		getContentPane().setLayout(flowLayout);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setAlignmentY(Component.TOP_ALIGNMENT);
		tabbedPane.setOpaque(true);
		tabbedPane.setName("Ayuda");
		getContentPane().add(tabbedPane);
		
		panel_archivo = new JPanel();
		panel_archivo.setBackground(new Color(0, 0, 255));
		tabbedPane.addTab("Archivo", null, panel_archivo, null);
		tabbedPane.setEnabledAt(0, true);
		
		panel_ayuda = new JPanel();
		panel_ayuda.setBackground(new Color(0, 255, 255));
		tabbedPane.addTab("Ayuda", null, panel_ayuda, null);
		tabbedPane.setForegroundAt(1, new Color(0, 100, 0));
		tabbedPane.setEnabledAt(1, true);
		btnGuardarArchivo = new JButton("Guardar archivo");
		btnGuardarArchivo.addActionListener(this);
		getContentPane().add(btnGuardarArchivo);
		btnTest = new JButton("Test");
		btnTest.addActionListener(this);
		
		getContentPane().add(btnTest);
		btnAbrirArchivo = new JButton("Abrir archivo");
		btnAbrirArchivo.addActionListener(this);	
		getContentPane().add(btnAbrirArchivo);
		
		textArea = new TextArea();
		textArea.setBackground(new Color(245, 255, 250));
		textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		textArea.setName("VentanaTexto");
		textArea.setForeground(Color.BLACK);
		getContentPane().add(textArea);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnTest) {JOptionPane.showMessageDialog(null, "Test OK");}
		else if(e.getSource()==btnAbrirArchivo) {cio.abrirArchivo(textArea);}
		else if( (e.getSource()==btnGuardarArchivo) && (cio.guardarArchivo(textArea.getText())) ) {			
			JOptionPane.showMessageDialog(null, "Archivo guardado");
		}
	}

}
