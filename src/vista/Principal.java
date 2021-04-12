/**
 * 
 */
package vista;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import controlador.IO;

/**
 * @author Silverio Manuel Rosales Santana
 * @date
 * @version 1.5
 *
 */
public class Principal extends JFrame implements ActionListener{

	private static final long serialVersionUID = -1830456885294124447L;
	private JButton btnTest,btnAbrirArchivo;
	private JTable table;
	/**
	 * Crea la aplicación.
	 */
	public Principal() {
		initialize();
	}

	/**
	 * Inicialización de los contenidos del frame.
	 */
	private void initialize() {
		setBounds(100, 100, 868, 712);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		btnTest = new JButton("Test");
		btnTest.addActionListener(this);
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		table = new JTable();
		btnAbrirArchivo = new JButton("Abrir archivo");
		btnAbrirArchivo.addActionListener(this);
		
		getContentPane().add(btnAbrirArchivo);
		getContentPane().add(btnTest);
		getContentPane().add(table);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==btnTest) {
			JOptionPane.showMessageDialog(null, "Test OK");
		}else if(e.getSource()==btnAbrirArchivo) {
			IO io = new IO();
			io.abrirArchivo("cvs");			
		}
		
	}

}
