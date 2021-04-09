/**
 * 
 */
package vista;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Silverio Manuel Rosales Santana
 * @date
 * @version 1.0
 *
 */
public class Principal extends JFrame implements ActionListener{

	private static final long serialVersionUID = -1830456885294124447L;
	private JButton btnNewButton;
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
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		btnNewButton = new JButton("Test");
		btnNewButton.addActionListener(this);
		getContentPane().add(btnNewButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==btnNewButton) {
			JOptionPane.showMessageDialog(null, "Este es un mensaje prueba");
		}
		
	}

}
