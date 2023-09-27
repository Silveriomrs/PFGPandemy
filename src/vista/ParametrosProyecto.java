/**  
* Clase dependiente de la clase ParametrosGrupos, además contiene la vista del módulo de Archivos. 
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 19 oct. 2021  
* @version 2.3  
*/  
package vista;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.ComponentOrientation;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import controlador.ControladorModulos;
import controlador.IO;
import modelo.ImagesList;
import modelo.Labels;
import modelo.Labels_GUI;
import modelo.TypesFiles;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.border.LineBorder;
import javax.swing.JSeparator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.util.Locale;

/**
 * Panel de las propiedades del proyecto.
 * @author Silverio Manuel Rosales Santana
 * @date 19 oct. 2021
 * @version versión 2.3
 */
public class ParametrosProyecto extends JPanel {
	
	/** serialVersionUID*/  
	private static final long serialVersionUID = -73292561581668096L;
	private Image imagen;
	private String ruta = ImagesList.BCKGND_PARAMETERS;
	private ControladorModulos cm;
	private JFormattedTextField fTFNombre;
	private JFormattedTextField fTFAutor;
	private JFormattedTextField fTFNGrupos;
	private JFormattedTextField fTFVersion;
	private JDateChooser dateChooser0;
	private JDateChooser dateChooser1;
	private JTextArea textArea;

	
		
	/**
	 * Inicialización de las propiedades y datos principales del proyecto.
	 * @param cm Controlador de los módulos.
	 * @param archivos Módulo encargado de la gestión de los archivos del proyecto.
	 */
	public ParametrosProyecto(ControladorModulos cm,Archivos archivos) {
		super();
		this.cm = cm;
		setAutoscrolls(true);
		setSize(new Dimension(1024, 768));
		setName(Labels_GUI.PROJECT_NAME);
		archivos.setBounds(275, 323, 640, 347);
		add(archivos);	
		configurar();
	}
	
	/**
	 * Sobrescritura del método que permite un redibujado de los elementos gráficos
	 *  acordes a la necesidades de esta clase. En este caso, una imagen de fondo, iconos,
	 *   y otros atributos particulares de esta vista.
	 */
	@Override
	public void paint(Graphics g) {
		if(ruta != null) {
			imagen = new ImageIcon(getClass().getResource(ruta)).getImage();
			g.drawImage(imagen,0,0,getWidth(),getHeight(),this);
			setOpaque(true);
			super.paint(g);
		}
	}
	
	
	/**
	 * <p>Reinicia la vista de este módulo.</p> 
	 *  Limpia los textos mostrados en cada etiqueta, sustituyéndolos
	 * por cadenas vacias y reinicia el resto de valores.
	 */
	public void reset() {
		fTFNombre.setText(null);		
		fTFAutor.setText(null);
		textArea.setText(null);
		fTFVersion.setText(null);
		fTFNGrupos.setText("" + cm.getNumberZonas());
		dateChooser0.setDate(new Date());
		dateChooser0.setDate(new Date());
	}
	
	/**
	 * Actualiza los controles pertinentes de la vista.
	 */
	public void refresh() {
		String tipo = TypesFiles.PRJ;
		if(cm.hasModule(tipo)) {
			fTFNombre.setText(cm.getValueFromLabel(tipo,Labels.NAME));		
			fTFAutor.setText(cm.getValueFromLabel(tipo,Labels.AUTHOR));
			textArea.setText(cm.getValueFromLabel(tipo,Labels.DESCRIPTION));
			fTFVersion.setText(cm.getValueFromLabel(tipo,Labels.VERSION));
			dateChooser0.setDate(stringToDate(cm.getValueFromLabel(tipo,Labels.DATE0)));
			dateChooser1.setDate(stringToDate(cm.getValueFromLabel(tipo,Labels.DATE1)));
		}
		fTFNGrupos.setText("" + cm.getNumberZonas());
	}

	/**
	 * Configura todas las propiedades de la vista y los controles adjuntos, tal como
	 *  posiciones, dimensiones, iconos, textos emergentes, etc.
	 */
	private void configurar() {
		fTFNombre = setUpTextField(null,Labels_GUI.TT_MODEL_NAME, SwingConstants.LEFT);
		fTFAutor = setUpTextField(null,Labels_GUI.TT_AUTHOR,SwingConstants.LEFT);
		fTFVersion = setUpTextField(Labels_GUI.VERSION_NUMBER,Labels_GUI.TT_VERSION_MODEL,SwingConstants.CENTER);	
		fTFNGrupos = setUpTextField(null,Labels_GUI.TT_NG,SwingConstants.CENTER);

		//
		fTFNombre.setBounds(275, 83, 235, 19);
		fTFAutor.setBounds(275, 110, 235, 19);
		fTFVersion.setBounds(715, 155, 63, 19);
		fTFNGrupos.setBounds(275, 277, 63, 19);

		//
		setName(Labels_GUI.NAME_MODULES);
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setBackground(Color.LIGHT_GRAY);
		setAutoscrolls(true);
		setLayout(null);
		
		add(fTFNombre);
		add(fTFAutor);
		add(fTFVersion);	
		add(fTFNGrupos);
		

		JLabel lblAutor = new JLabel(Labels_GUI.L_AUTHOR);
		lblAutor.setBounds(53, 112, 57, 15);
		add(lblAutor);
		JLabel lblFechaDeltima = new JLabel(Labels_GUI.L_DATE_MODIFIED);
		lblFechaDeltima.setBounds(556, 112, 147, 15);
		add(lblFechaDeltima);
		//
		JLabel label_obligatorio_1 = new JLabel("");
		label_obligatorio_1.setToolTipText(Labels_GUI.L_MANDATORY_FIELD);
		label_obligatorio_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_obligatorio_1.setBounds(new Rectangle(28, 277, 15, 15));
		label_obligatorio_1.setIcon(IO.getIcon(ImagesList.MANDATORY,20,20));
		add(label_obligatorio_1);
		
		JLabel label_obligatorio = new JLabel("");
		label_obligatorio.setToolTipText(Labels_GUI.L_MANDATORY_FIELD);
		label_obligatorio.setHorizontalAlignment(SwingConstants.CENTER);
		label_obligatorio.setBounds(new Rectangle(28, 83, 15, 15));

		label_obligatorio.setIcon(IO.getIcon(ImagesList.MANDATORY,20,20));
		add(label_obligatorio);
		
		
		textArea = new JTextArea();
		textArea.setLocale(new Locale("es", "ES"));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setBounds(275, 141, 235, 88);
		textArea.setMinimumSize(new Dimension(200, 100));
		textArea.setMaximumSize(new Dimension(2147, 2147));
		textArea.setToolTipText(Labels_GUI.TT_DESCRIPTION);
		textArea.setBorder(new LineBorder(Color.BLACK, 1, true));
		textArea.setBackground(Color.WHITE);

		textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		add(textArea);
		
		JLabel lblNombreDelProyecto = new JLabel(Labels_GUI.PRJ_TITLE);
		lblNombreDelProyecto.setBounds(53, 85, 153, 15);
		add(lblNombreDelProyecto);		
		
		dateChooser1 = new JDateChooser();
		dateChooser1.setBounds(721, 108, 194, 19);
		dateChooser1.setToolTipText(Labels_GUI.TT_DATE_MODIFIED);
		dateChooser1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		//Desactivar la edición de fechas de forma manual.
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser1.getDateEditor();
		editor.setEditable(false);
		add(dateChooser1);
		
		dateChooser0 = new JDateChooser();
		dateChooser0.setToolTipText(Labels_GUI.TT_DATE_CREATION);
		dateChooser0.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		dateChooser0.setBounds(724, 83, 191, 19);
		//Desactivar la edición de fechas de forma manual.
		editor = (JTextFieldDateEditor) dateChooser0.getDateEditor();
		editor.setEditable(false);
		add(dateChooser0);
		
		
		JLabel lblDescripcin = new JLabel(Labels_GUI.L_DESCRIPTION_MODEL);
		lblDescripcin.setBounds(53, 137, 180, 15);
		add(lblDescripcin);
		JLabel lblNmeroDeGrupos = new JLabel(Labels_GUI.L_NG);
		lblNmeroDeGrupos.setBounds(53, 279, 217, 15);
		add(lblNmeroDeGrupos);
		JLabel lblArchivosDelProyecto = new JLabel(Labels_GUI.L_FILES);
		lblArchivosDelProyecto.setBounds(53, 336, 157, 15);
		add(lblArchivosDelProyecto);
		JLabel txtVersion = new JLabel();
		txtVersion.setText(Labels_GUI.VERSION_TITLE);
		txtVersion.setBounds(556, 155, 137, 19);
		add(txtVersion);
		
		JLabel lblFechaDeCreacin = new JLabel(Labels_GUI.L_DATE_CREATION);
		lblFechaDeCreacin.setBounds(556, 85, 137, 15);
		add(lblFechaDeCreacin);

		
		JSeparator separator = new JSeparator();
		separator.setBackground(Color.BLUE);
		separator.setBounds(28, 305, 903, 19);
		add(separator);
		
		
		JButton btnAplicar = new JButton(Labels_GUI.BTN_APPLY);
		btnAplicar.addMouseListener(new BotonL());
		btnAplicar.setIcon(IO.getIcon( ImagesList.OK,64,64));
		btnAplicar.setToolTipText(Labels_GUI.TT_BTN_APPLY);
		btnAplicar.setBounds(682, 201, 233, 74);
		add(btnAplicar);
		

	}
	
	
	/**
	 * Convierte una cadena de texto que contiene una fecha en un objeto Date.
	 * @param fecha Grupo fecha/hora en formato: "dd/MM/yyyy hh:mm"
	 * @return Date con los valores leidos almacenados.
	 */
	private Date stringToDate(String fecha){
		 SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");	//Formato de la fecha.
		 Date date = new Date();
		 if(fecha != null) {
			 try { date = formato.parse(fecha);	}									//Conversión tipo de datos.
			 catch (ParseException ex) { 
				 System.out.println("ParametrosProyecto > Error de parser de fechas: " + fecha);
				 return date;
			 }
		 }
		 return date;
	}
	
	/**
	 * Convierte una fecha en cadena de texto con un formato "dd/MM/yyyy".
	 * @param date Fecha a convertir a texto.
	 * @return La cadena de texto resultante de la conversión.
	 */
	private String dateToString(Date date) {
		String fecha = null;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");	//Formato de la fecha.
		fecha = formato.format(date);
		return fecha;
	}
	
	
	/**
	 * <p>Establece una configuración general para los campos de texto</p> 
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
	
	/* Clases privadas */
	
	/**
	 * Clase privada que hereda de la clase MouseADapter para lograr aplicar un
	 * patrón observer con el objetivo de poder manejar los eventos que el usuario realiza
	 *  con el ratón sobre los controles de la vista.
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BotonL extends MouseAdapter {
		private HashMap<String,String> datos;
		private int NG;
		private String name;
		
		public BotonL() {
			datos = new HashMap<String,String>();
		}
		
		/**
		 * Método sobrescrito de captura de la pulsación del ratón sobre el control.
		 *  Antes de proceder con la acción muestra un aviso de las consecuencias de dicha acción.
		 *   Si es aprovada dicha acción, comprueba la corrección de los datos almacenados en los campos de la vista,
		 *    finalmente, procede con la ejecución de las acciones pertinentes.
		 */
		@Override
		public void mouseClicked(MouseEvent evt) {
			boolean continuar = true;
			String aviso = Labels_GUI.WARNING_1;
			//Primer valor obligatorio.
			try{NG = Integer.parseInt(fTFNGrupos.getText());}
			catch(Exception e) {
				String s = Labels_GUI.WARNING_2 + fTFNGrupos.getText();
				cm.showMessage(s, 0);
				fTFNGrupos.setText(null);										//Le borramos texto introducido.
				continuar = false;
			}
			
			//Siguiente valor obligatorio.
			if(continuar) name = fTFNombre.getText();

			//Comprobación de valores obligatorios y emisión si procede de mensaje de error.
			if(continuar && NG == 0) {
				cm.showMessage(Labels_GUI.WARNING_3, 0);
				continuar = false;
			//Comprobación de nombre válido.
			}else if(continuar && (name == null || name.equals(""))) {
				cm.showMessage(Labels_GUI.WARNING_EMPTY_FIELD, 0);
				continuar = false;
			//Si no hubieron errores y hay confirmación -> proceder con la actualización de los datos.
			}else if(continuar && cm.showMessage(aviso,3) == JOptionPane.OK_OPTION ){
				proceed();
				cm.doActionPProyecto(datos);
			}
		}
		
		/**
		 * Recolecta los datos de los campos y los introduce en el HashMap.
		 */
		private void proceed() {
			//Si se ha pulsado el botón de aplicar... recolectar datos.
			datos.clear();
			datos.put(Labels.NAME, name);
			datos.put(Labels.AUTHOR,"" + fTFAutor.getText());
			datos.put(Labels.NG, "" + NG);		
			datos.put(Labels.DESCRIPTION,"" + textArea.getText());
			//Versión, sino hay datos, coloca una versión inicial.
			String version = fTFVersion.getText();
			if(version != null) datos.put(Labels.VERSION,version);
			else datos.put(Labels.VERSION,Labels_GUI.VERSION_NUMBER);
			//Fechas, si no hay datos, coloca fecha actual.
			Date d = dateChooser0.getDate();
			if(d != null) datos.put(Labels.DATE0, dateToString( d));
			else datos.put(Labels.DATE0, dateToString( new Date()));
			d =  dateChooser1.getDate();
			if(d != null) datos.put(Labels.DATE1, dateToString(d));
			else datos.put(Labels.DATE1, dateToString( new Date()));
		}
	}
}
