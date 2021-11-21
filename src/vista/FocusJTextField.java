/**  
* <p>Title: FocusJTextField.java</p>  
* <p>Description: Clase particular que simplifica el proceso de dotar a los 
* formularios de la característica visual de remarcar un campo de texto que 
* obtiene el foco (está seleccionado, de cuando no lo está.</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 19 nov. 2021  
* @version 1.0  
*/  
package vista;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

/**
 * <p>Title: FocusJTextField</p>  
 * <p>Description: Clase que hereda de la clase FocusAdapter</p> 
 * Su función es remarcar aquellos JTextFields de forma diferenciada cuando
 * tienen el foco a cuando no lo tienen.
 * @author Silverio Manuel Rosales Santana
 * @date 19 nov. 2021
 * @version versión 1.0
 */
public class FocusJTextField extends FocusAdapter {
		private JTextField jtf;
		
		/**
		 * <p>Title: </p>  
		 * <p>Description: El constructor necesita una referencia al propio 
		 * elemento sobre el que va a realizar el seguimiento.</p>  
		 * @param jtf JTextField sobre el que debe operar el listener.
		 */
		public FocusJTextField(JTextField jtf) {
			this.jtf = jtf;
		}

		@Override
        public void focusGained(FocusEvent e) {
            jtf.setBackground(new Color(204, 255, 204));
            jtf.setBorder(new LineBorder(Color.BLACK, 2, true));
        }

        @Override
        public void focusLost(FocusEvent e) {
            jtf.setBackground(UIManager.getColor("TextField.background"));
            jtf.setBorder(new LineBorder(Color.BLACK, 1, true));
        }
	}