/**  
* <p>Title: ParametrosProyecto.java</p>  
* <p>Description: Clase dependiente de la clase ParametrosGrupos.</p>    
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
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import modelo.IO;
import modelo.Labels;
import java.awt.Rectangle;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.border.LineBorder;
import javax.swing.JSeparator;

/**
 * <p>Title: ParametrosProyecto</p>  
 * <p>Description: Panel de las propiedades del proyecto </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 19 oct. 2021
 * @version versión 1.0
 */
public class ParametrosProyecto extends JPanel {
	
	/** serialVersionUID*/  
	private static final long serialVersionUID = -73292561581668096L;
	private JFormattedTextField fTFNombre;
	private JFormattedTextField fTFAutor;
	private JFormattedTextField fTFNGrupos;
	private JFormattedTextField fTFVersion;
	private JDateChooser dateChooser;	
	private JTextArea textArea;
	
	private JPanel panel_archivos;
	private Archivos archivos;
		
	/**
	 * <p>Title: Constructor de la clase</p>  
	 * <p>Description: Inicialización de las propiedades y datos principales
	 * del proyecto.</p>  
	 * @param archivos Módulo encargado de la gestión de los archivos del proyecto.
	 */
	public ParametrosProyecto(Archivos archivos) {
		super();
		this.archivos = archivos;
		setAutoscrolls(true);
		setSize(new Dimension(1024, 768));
		setName("Propiedades_Proyecto");
		setLayout(new BorderLayout(0, 0));
		panel_archivos = this.archivos.getPanel();
		configurar();
	}
	
	
	/**
	 * <p>Title: reset</p>  
	 * <p>Description: Reinicia la vista de este módulo.</p> 
	 *  Limpia los textos mostrados en cada etiqueta, sustituyéndolos
	 * por cadenas vacias y reinicia el resto de valores.
	 */
	public void reset() {
		fTFNombre.setText(null);		
		fTFAutor.setText(null);
		textArea.setText(null);
		fTFVersion.setText(null);
		fTFNGrupos.setText(null);
		textArea.setText(null);
		dateChooser.setDate(new Date());
	}
		
	/**
	 * <p>Title: setField</p>  
	 * <p>Description: Modifica el contenido mostrado por un campo</p>
	 * @see Labels
	 * @param fieldName Nombre de la etiqueta que representa el campo.
	 * @param txt Texto a introducir en el campo.
	 */
	public void setField(String fieldName, String txt) {
		switch(fieldName) {
		case(Labels.NAME): fTFNombre.setText(txt);
			break;
		case(Labels.AUTHOR): fTFAutor.setText(txt);
			break;
		case(Labels.DESCRIPTION): textArea.setText(txt);
			break;
		case(Labels.VERSION): fTFVersion.setText(txt);
			break;
		case(Labels.NG): fTFNGrupos.setText(txt);
			break;
		case(Labels.DATE): dateChooser.setDate(stringToDate(txt));
			break;
		default:
//			System.out.println("ParametrosProyecto > setField, campo no reconocido: " + fieldName);
		}
	}

	
	/**
	 * <p>Title: stringToDate</p>  
	 * <p>Description: Convierte una cadena de texto que contiene una fecha
	 * en un objeto Date</p> 
	 * @param fecha Grupo fecha/hora en formato: "dd/MM/yyyy hh:mm"
	 * @return Date con los valores leidos almacenados.
	 */
	@SuppressWarnings("finally")
	private Date stringToDate(String fecha){
		 System.out.println("ParametrosProyecto > stringToDate: " + fecha);
		 SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");			//Formato de la fecha.
		 Date date = null;
		 try { date = formato.parse(fecha);	}									//Conversión tipo de datos.
		 catch (ParseException ex) { ex.printStackTrace(); }
		 finally {return date;} 
	}
	
	/**
	 * <p>Title: setUpTextField</p>  
	 * <p>Description: Establece una configuración general para los campos de texto</p> 
	 * Dicha configuración consta de tooltip, un borde personalizado, el ancho, alineación,
	 *  y el texto inicial. Además de un Listener para el resalte de los campos.
	 * @param texto Texto inicial a mostrar en el campo. Acepta null como parámetro.
	 * @param tt Tooltip o información extra que se mostrará al pasar el cursor por encima.
	 * @param align Alineación del texto. @see ComponentOrientation.
	 * @return El propio campo formateado con las opciones por defecto y las indicadas
	 *  por parámetro.
	 */
	private JFormattedTextField setUpTextField(String texto,String tt, int align) {
		JFormattedTextField jtf = new JFormattedTextField(texto);
		jtf.addFocusListener(new FocusJTextField(jtf));
		jtf.setHorizontalAlignment(align);
		jtf.setBorder(new LineBorder(Color.BLACK, 1, true));
		jtf.setToolTipText(tt);
		jtf.setColumns(10);
		return jtf;
	}

	private void configurar() {
		//Parámetros repetitivos -> factorizar para mejorar lejibilidad general.
		Insets insets = new Insets(0, 0, 5, 5);
		//TFIelds para factorizar:
		fTFNombre = setUpTextField(null,"Nombre del proyecto, se usará para dar nombre a los archivos que lo componen.", SwingConstants.LEFT);
		fTFAutor = setUpTextField(null,"Autor del proyecto.",SwingConstants.LEFT);
		fTFVersion = setUpTextField("1.0","Número de versión del proyecto.",SwingConstants.CENTER);	
		fTFNGrupos = setUpTextField(null,"Número de grupos de población que componen el proyecto.",SwingConstants.CENTER);
		//
		setName("panel");
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setBackground(Color.LIGHT_GRAY);
		setAutoscrolls(true);
		
		//GRIDS
		
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{32, 241, 243, 212, 143, 0};
		gbl_panel.rowHeights = new int[]{15, 30, 0, 114, 10, 0, 0, 0, 0, 220, 54, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_panel);
		
		JLabel label_obligatorio = new JLabel("");
		label_obligatorio.setToolTipText("Campo obligatorio");
		label_obligatorio.setHorizontalAlignment(SwingConstants.CENTER);
		label_obligatorio.setMaximumSize(new Dimension(25, 25));
		label_obligatorio.setIcon(IO.getIcon("/vista/imagenes/Iconos/obligatorio2_64px.png",20,20));
		GridBagConstraints gbc_label_obligatorio = new GridBagConstraints();
		gbc_label_obligatorio.insets = insets;
		gbc_label_obligatorio.gridx = 0;
		gbc_label_obligatorio.gridy = 1;
		add(label_obligatorio, gbc_label_obligatorio);
		
		JLabel lblNombreDelProyecto = new JLabel("Nombre del Proyecto:");
		lblNombreDelProyecto.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNombreDelProyecto = new GridBagConstraints();
		gbc_lblNombreDelProyecto.insets = insets;
		gbc_lblNombreDelProyecto.anchor = GridBagConstraints.WEST;
		gbc_lblNombreDelProyecto.gridx = 1;
		gbc_lblNombreDelProyecto.gridy = 1;
		add(lblNombreDelProyecto, gbc_lblNombreDelProyecto);
		
		GridBagConstraints gbc_tfNombre = new GridBagConstraints();
		gbc_tfNombre.insets = insets;
		gbc_tfNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfNombre.gridx = 2;
		gbc_tfNombre.gridy = 1;
		add(fTFNombre, gbc_tfNombre);
		
		JLabel lblAutor = new JLabel("Autor/a:");
		lblAutor.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblAutor = new GridBagConstraints();
		gbc_lblAutor.anchor = GridBagConstraints.WEST;
		gbc_lblAutor.insets = insets;
		gbc_lblAutor.gridx = 1;
		gbc_lblAutor.gridy = 2;
		add(lblAutor, gbc_lblAutor);
		

		GridBagConstraints gbc_tfAutor = new GridBagConstraints();
		gbc_tfAutor.insets = insets;
		gbc_tfAutor.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfAutor.gridx = 2;
		gbc_tfAutor.gridy = 2;
		add(fTFAutor, gbc_tfAutor);
		
		
		JLabel lblDescripcin = new JLabel("Descripción del proyecto:");
		GridBagConstraints gbc_lblDescripcin = new GridBagConstraints();
		gbc_lblDescripcin.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblDescripcin.insets = insets;
		gbc_lblDescripcin.gridx = 1;
		gbc_lblDescripcin.gridy = 3;
		add(lblDescripcin, gbc_lblDescripcin);
		
		textArea = new JTextArea();
		textArea.setMinimumSize(new Dimension(200, 100));
		textArea.setMaximumSize(new Dimension(2147, 2147));
		textArea.setToolTipText("Introduzca cualquier texto descriptivo que crea necesario acerca del proyecto.");
		textArea.setBorder(new LineBorder(Color.BLACK, 1, true));
		textArea.setBackground(Color.WHITE);
		textArea.setColumns(10);
		textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.insets = insets;
		gbc_textArea.gridx = 2;
		gbc_textArea.gridy = 3;
		add(textArea, gbc_textArea);
		
		JLabel lblFechaDeltima = new JLabel("Fecha de última modificación:");
		GridBagConstraints gbc_lblFechaDeltima = new GridBagConstraints();
		gbc_lblFechaDeltima.anchor = GridBagConstraints.WEST;
		gbc_lblFechaDeltima.insets = insets;
		gbc_lblFechaDeltima.gridx = 1;
		gbc_lblFechaDeltima.gridy = 4;
		add(lblFechaDeltima, gbc_lblFechaDeltima);
		
		dateChooser = new JDateChooser();
		dateChooser.setToolTipText("Puede dejar indicado la última fecha de modificación o creación.");
		dateChooser.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		//Desactivar la edición de fechas de forma manual.
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
		editor.setEditable(false);
		
		GridBagConstraints gbc_dateChooser = new GridBagConstraints();
		gbc_dateChooser.fill = GridBagConstraints.HORIZONTAL;
		gbc_dateChooser.insets = insets;
		gbc_dateChooser.gridx = 2;
		gbc_dateChooser.gridy = 4;
		add(dateChooser, gbc_dateChooser);
		
		JLabel txtVersion = new JLabel();
		txtVersion.setText("Versión:");
		GridBagConstraints gbc_txtVersion = new GridBagConstraints();
		gbc_txtVersion.anchor = GridBagConstraints.WEST;
		gbc_txtVersion.insets = insets;
		gbc_txtVersion.gridx = 1;
		gbc_txtVersion.gridy = 5;
		add(txtVersion, gbc_txtVersion);
		
		GridBagConstraints gbc_fTFVersion = new GridBagConstraints();
		gbc_fTFVersion.insets = insets;
		gbc_fTFVersion.fill = GridBagConstraints.HORIZONTAL;
		gbc_fTFVersion.gridx = 2;
		gbc_fTFVersion.gridy = 5;
		add(fTFVersion, gbc_fTFVersion);
		
		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.gridwidth = 4;
		gbc_separator.insets = insets;
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 6;
		add(separator, gbc_separator);
		
		JLabel label_obligatorio_1 = new JLabel("");
		label_obligatorio_1.setToolTipText("Campo obligatorio");
		label_obligatorio_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_obligatorio_1.setBounds(new Rectangle(0, 0, 15, 15));
		label_obligatorio_1.setIcon(IO.getIcon("/vista/imagenes/Iconos/obligatorio2_64px.png",20,20));
		GridBagConstraints gbc_label_obligatorio_1 = new GridBagConstraints();
		gbc_label_obligatorio_1.insets = insets;
		gbc_label_obligatorio_1.gridx = 0;
		gbc_label_obligatorio_1.gridy = 7;
		add(label_obligatorio_1, gbc_label_obligatorio_1);
		
		JLabel lblNmeroDeGrupos = new JLabel("Número de Grupos de estudio:");
		GridBagConstraints gbc_lblNmeroDeGrupos = new GridBagConstraints();
		gbc_lblNmeroDeGrupos.anchor = GridBagConstraints.WEST;
		gbc_lblNmeroDeGrupos.insets = insets;
		gbc_lblNmeroDeGrupos.gridx = 1;
		gbc_lblNmeroDeGrupos.gridy = 7;
		add(lblNmeroDeGrupos, gbc_lblNmeroDeGrupos);
		

		GridBagConstraints gbc_fTFNGrupos = new GridBagConstraints();
		gbc_fTFNGrupos.insets = insets;
		gbc_fTFNGrupos.fill = GridBagConstraints.HORIZONTAL;
		gbc_fTFNGrupos.gridx = 2;
		gbc_fTFNGrupos.gridy = 7;
		add(fTFNGrupos, gbc_fTFNGrupos);
		
		JLabel lblArchivosDelProyecto = new JLabel("Archivos del Proyecto:");
		GridBagConstraints gbc_lblArchivosDelProyecto = new GridBagConstraints();
		gbc_lblArchivosDelProyecto.anchor = GridBagConstraints.WEST;
		gbc_lblArchivosDelProyecto.insets = insets;
		gbc_lblArchivosDelProyecto.gridx = 1;
		gbc_lblArchivosDelProyecto.gridy = 9;
		add(lblArchivosDelProyecto, gbc_lblArchivosDelProyecto);
		
		
		GridBagConstraints gbc_panel_archivos = new GridBagConstraints();
		gbc_panel_archivos.gridwidth = 3;
		gbc_panel_archivos.gridheight = 2;
		gbc_panel_archivos.fill = GridBagConstraints.BOTH;
		gbc_panel_archivos.gridx = 2;
		gbc_panel_archivos.gridy = 9;
		add(panel_archivos, gbc_panel_archivos);
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
	

	/**
	 * <p>Title: main</p>  
	 * <p>Description: Método para pruebas.</p> 
	 * @param args Nada
	 */
	public static void main(String[] args) {
		ParametrosProyecto pp = new ParametrosProyecto(new Archivos(null));
		pp.abrirFrame();
	}

}
