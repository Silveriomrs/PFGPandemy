/**  
* <p>Title: Parametros_Proyecto.java</p>  
* <p>Description: Clase dependiente de la clase Parametros.</p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 19 oct. 2021  
* @version 1.0  
*/  
package vista;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import com.toedter.calendar.JDateChooser;

import modelo.IO;

import java.awt.Rectangle;
import javax.swing.border.LineBorder;
import javax.swing.JSeparator;

/**
 * <p>Title: Parametros_Proyecto</p>  
 * <p>Description: Panel de las propiedades del proyecto </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 19 oct. 2021
 * @version versión 1.0
 */
public class Parametros_Proyecto extends JPanel {
	
	
	/**
	 * <p>Title: Constructor de la clase</p>  
	 * <p>Description: Inicialización de las propiedades y datos principales
	 * del proyecto.</p>  
	 */
	public Parametros_Proyecto() {
		setBackground(Color.ORANGE);
		setAutoscrolls(true);
		setSize(new Dimension(1024, 768));
		setName("Propiedades_Proyecto");
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel_central = new JPanel();
		panel_central.setName("panel_central");
		panel_central.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel_central.setBackground(Color.LIGHT_GRAY);
		panel_central.setAutoscrolls(true);
		add(panel_central, BorderLayout.CENTER);
		GridBagLayout gbl_panel_central = new GridBagLayout();
		gbl_panel_central.columnWidths = new int[]{32, 241, 243, 212, 143, 0};
		gbl_panel_central.rowHeights = new int[]{15, 30, 0, 114, 10, 0, 0, 0, 0, 220, 54, 0};
		gbl_panel_central.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_central.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_central.setLayout(gbl_panel_central);
		
		JLabel label_obligatorio = new JLabel("");
		label_obligatorio.setToolTipText("Campo obligatorio");
		label_obligatorio.setHorizontalAlignment(SwingConstants.CENTER);
		label_obligatorio.setMaximumSize(new Dimension(25, 25));
		label_obligatorio.setIcon(IO.getIcon("/vista/imagenes/Iconos/obligatorio2_64px.png",20,20));
		GridBagConstraints gbc_label_obligatorio = new GridBagConstraints();
		gbc_label_obligatorio.insets = new Insets(0, 0, 5, 5);
		gbc_label_obligatorio.gridx = 0;
		gbc_label_obligatorio.gridy = 1;
		panel_central.add(label_obligatorio, gbc_label_obligatorio);
		
		JLabel lblNombreDelProyecto = new JLabel("Nombre del Proyecto:");
		lblNombreDelProyecto.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNombreDelProyecto = new GridBagConstraints();
		gbc_lblNombreDelProyecto.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombreDelProyecto.anchor = GridBagConstraints.WEST;
		gbc_lblNombreDelProyecto.gridx = 1;
		gbc_lblNombreDelProyecto.gridy = 1;
		panel_central.add(lblNombreDelProyecto, gbc_lblNombreDelProyecto);
		
		textField = new JTextField();
		textField.setBackground(new Color(255, 255, 255));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		panel_central.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JLabel lblAutor = new JLabel("Autor/a:");
		lblAutor.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblAutor = new GridBagConstraints();
		gbc_lblAutor.anchor = GridBagConstraints.WEST;
		gbc_lblAutor.insets = new Insets(0, 0, 5, 5);
		gbc_lblAutor.gridx = 1;
		gbc_lblAutor.gridy = 2;
		panel_central.add(lblAutor, gbc_lblAutor);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 2;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 2;
		panel_central.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JLabel lblDescripcin = new JLabel("Descripción del proyecto:");
		GridBagConstraints gbc_lblDescripcin = new GridBagConstraints();
		gbc_lblDescripcin.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblDescripcin.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescripcin.gridx = 1;
		gbc_lblDescripcin.gridy = 3;
		panel_central.add(lblDescripcin, gbc_lblDescripcin);
		
		JTextArea textArea = new JTextArea();
		textArea.setBorder(new LineBorder(Color.BLACK, 2, true));
		textArea.setBackground(Color.WHITE);
		textArea.setColumns(10);
		textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.gridx = 2;
		gbc_textArea.gridy = 3;
		panel_central.add(textArea, gbc_textArea);
		
		JLabel lblFechaDeltima = new JLabel("Fecha de última modificación:");
		GridBagConstraints gbc_lblFechaDeltima = new GridBagConstraints();
		gbc_lblFechaDeltima.anchor = GridBagConstraints.WEST;
		gbc_lblFechaDeltima.insets = new Insets(0, 0, 5, 5);
		gbc_lblFechaDeltima.gridx = 1;
		gbc_lblFechaDeltima.gridy = 4;
		panel_central.add(lblFechaDeltima, gbc_lblFechaDeltima);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		GridBagConstraints gbc_dateChooser = new GridBagConstraints();
		gbc_dateChooser.fill = GridBagConstraints.HORIZONTAL;
		gbc_dateChooser.insets = new Insets(0, 0, 5, 5);
		gbc_dateChooser.gridx = 2;
		gbc_dateChooser.gridy = 4;
		panel_central.add(dateChooser, gbc_dateChooser);
		
		txtVersin = new JLabel();
		txtVersin.setText("Versión:");
		GridBagConstraints gbc_txtVersin = new GridBagConstraints();
		gbc_txtVersin.anchor = GridBagConstraints.WEST;
		gbc_txtVersin.insets = new Insets(0, 0, 5, 5);
		gbc_txtVersin.gridx = 1;
		gbc_txtVersin.gridy = 5;
		panel_central.add(txtVersin, gbc_txtVersin);
		
		JFormattedTextField formattedTextField_1 = new JFormattedTextField();
		formattedTextField_1.setHorizontalAlignment(SwingConstants.RIGHT);
		formattedTextField_1.setText("1.0");
		GridBagConstraints gbc_formattedTextField_1 = new GridBagConstraints();
		gbc_formattedTextField_1.insets = new Insets(0, 0, 5, 5);
		gbc_formattedTextField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_formattedTextField_1.gridx = 2;
		gbc_formattedTextField_1.gridy = 5;
		panel_central.add(formattedTextField_1, gbc_formattedTextField_1);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.gridwidth = 4;
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 6;
		panel_central.add(separator, gbc_separator);
		
		JLabel label_obligatorio_1 = new JLabel("");
		label_obligatorio_1.setToolTipText("Campo obligatorio");
		label_obligatorio_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_obligatorio_1.setBounds(new Rectangle(0, 0, 15, 15));
		label_obligatorio_1.setIcon(IO.getIcon("/vista/imagenes/Iconos/obligatorio2_64px.png",20,20));
		GridBagConstraints gbc_label_obligatorio_1 = new GridBagConstraints();
		gbc_label_obligatorio_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_obligatorio_1.gridx = 0;
		gbc_label_obligatorio_1.gridy = 7;
		panel_central.add(label_obligatorio_1, gbc_label_obligatorio_1);
		
		JLabel lblNmeroDeGrupos = new JLabel("Número de Grupos de estudio:");
		GridBagConstraints gbc_lblNmeroDeGrupos = new GridBagConstraints();
		gbc_lblNmeroDeGrupos.anchor = GridBagConstraints.WEST;
		gbc_lblNmeroDeGrupos.insets = new Insets(0, 0, 5, 5);
		gbc_lblNmeroDeGrupos.gridx = 1;
		gbc_lblNmeroDeGrupos.gridy = 7;
		panel_central.add(lblNmeroDeGrupos, gbc_lblNmeroDeGrupos);
		
		JFormattedTextField formattedTextField = new JFormattedTextField();
		formattedTextField.setColumns(10);
		GridBagConstraints gbc_formattedTextField = new GridBagConstraints();
		gbc_formattedTextField.insets = new Insets(0, 0, 5, 5);
		gbc_formattedTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_formattedTextField.gridx = 2;
		gbc_formattedTextField.gridy = 7;
		panel_central.add(formattedTextField, gbc_formattedTextField);
		
		JLabel lblArchivosDelProyecto = new JLabel("Archivos del Proyecto:");
		GridBagConstraints gbc_lblArchivosDelProyecto = new GridBagConstraints();
		gbc_lblArchivosDelProyecto.anchor = GridBagConstraints.WEST;
		gbc_lblArchivosDelProyecto.insets = new Insets(0, 0, 5, 5);
		gbc_lblArchivosDelProyecto.gridx = 1;
		gbc_lblArchivosDelProyecto.gridy = 9;
		panel_central.add(lblArchivosDelProyecto, gbc_lblArchivosDelProyecto);
		
		JPanel panel_archivos = new Archivos(null);
		GridBagConstraints gbc_panel_archivos = new GridBagConstraints();
		gbc_panel_archivos.gridwidth = 3;
		gbc_panel_archivos.gridheight = 2;
		gbc_panel_archivos.fill = GridBagConstraints.BOTH;
		gbc_panel_archivos.gridx = 2;
		gbc_panel_archivos.gridy = 9;
		panel_central.add(panel_archivos, gbc_panel_archivos);
	}

	
	
	/**
	 * <p>Title: abrirFrame</p>  
	 * <p>Description: Visualiza los datos del módulo dentro de su propio marco</p> 
	 */
	public void abrirFrame() {
	    JFrame frame = new JFrame("Paneles de configuración");
	    Dimension m = getPreferredSize();
	    int x = (int)m.getWidth() +25;
	    int y = (int)m.getHeight()+15;
	    frame.setPreferredSize(new Dimension(x, y));
	    frame.setSize(new Dimension(x, y));
	    frame.setMaximumSize(new Dimension(2767, 2767));
		frame.setMinimumSize(new Dimension(800, 600));
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.getContentPane().add(this);
		frame.pack();
        frame.setVisible(true);
	}
	
	
	/** serialVersionUID*/  
	private static final long serialVersionUID = -73292561581668096L;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel txtVersin;

	/**
	 * <p>Title: main</p>  
	 * <p>Description: Método para pruebas.</p> 
	 * @param args Nada
	 */
	public static void main(String[] args) {
		Parametros_Proyecto pp = new Parametros_Proyecto();
		pp.abrirFrame();

	}

}
