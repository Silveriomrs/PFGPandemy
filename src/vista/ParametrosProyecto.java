/**  
* <p>Title: ParametrosProyecto.java</p>  
* <p>Description: Clase dependiente de la clase ParametrosGrupos.</p>    
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

import javax.swing.JFrame;
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
import modelo.IO;
import modelo.Labels;
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
 * <p>Title: ParametrosProyecto</p>  
 * <p>Description: Panel de las propiedades del proyecto </p>  
 * @author Silverio Manuel Rosales Santana
 * @date 19 oct. 2021
 * @version versión 2.3
 */
public class ParametrosProyecto extends JPanel {
	
	/** serialVersionUID*/  
	private static final long serialVersionUID = -73292561581668096L;
	private Image imagen;
	private String ruta = "/vista/imagenes/agua_800px.png";
	private ControladorModulos cm;
	private JFormattedTextField fTFNombre;
	private JFormattedTextField fTFAutor;
	private JFormattedTextField fTFNGrupos;
	private JFormattedTextField fTFVersion;
	private JDateChooser dateChooser0;
	private JDateChooser dateChooser1;
	private JTextArea textArea;

	
		
	/**
	 * <p>Title: Constructor de la clase</p>  
	 * <p>Description: Inicialización de las propiedades y datos principales
	 * del proyecto.</p>
	 * @param cm Controlador de los módulos.
	 * @param archivos Módulo encargado de la gestión de los archivos del proyecto.
	 */
	public ParametrosProyecto(ControladorModulos cm,Archivos archivos) {
		super();
		this.cm = cm;
		setAutoscrolls(true);
		setSize(new Dimension(1024, 768));
		setName("Propiedades_Proyecto");
		archivos.setBounds(275, 323, 640, 347);
		add(archivos);	
		configurar();
	}
	
	
	@Override
	public void paint(Graphics g) {
		if(ruta != null) {
			imagen = new ImageIcon(getClass().getResource(ruta)).getImage();
			g.drawImage(imagen,0,0,getWidth(),getHeight(),this);
			setOpaque(true);
			super.paint(g);
		}
	}
	

	/* Clases privadas */
	
	
	private class BotonL extends MouseAdapter {
		private HashMap<String,String> datos;
		private int NG;
		private String name;
		
		public BotonL() {
			datos = new HashMap<String,String>();
		}
		
		@Override
		public void mouseClicked(MouseEvent evt) {
			boolean continuar = true;
			String aviso = "El siguiente paso eliminará parcial o toltamente los datos almacenados previamente ¿desea continuar?";
			//Primer valor obligatorio.
			try{NG = Integer.parseInt(fTFNGrupos.getText());}
			catch(Exception e) {
				String s = "Número de grupos de estudio incorrecto: " + fTFNGrupos.getText();
				cm.showMessage(s, 0);
				fTFNGrupos.setText(null);										//Le borramos texto introducido.
				continuar = false;
			}
			
			//Siguiente valor obligatorio.
			if(continuar) name = fTFNombre.getText();

			//Comprobación de valores obligatorios y emisión si procede de mensaje de error.
			if(continuar && NG == 0) {
				cm.showMessage("El número de grupos debe ser mayor de cero.", 0);
				continuar = false;
			//Comprobación de nombre válido.
			}else if(continuar && name == null || name.equals("")) {
				cm.showMessage("El campo nombre no puede estar vacio.", 0);
				continuar = false;
			//Si no hubieron errores y hay confirmación -> proceder con la actualización de los datos.
			}else if(continuar && cm.showMessage(aviso,3) == JOptionPane.OK_OPTION ){
				proceed();
				cm.doActionPProyecto(datos);
			}
		}
		
		/**
		 * <p>Title: proceed</p>  
		 * <p>Description: Recolecta los datos de los campos dentro del HashMap</p>
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
			else datos.put(Labels.VERSION,"1.0");
			//Fechas, si no hay datos, coloca fecha actual.
			Date d = dateChooser0.getDate();
			if(d != null) datos.put(Labels.DATE0, dateToString( d));
			else datos.put(Labels.DATE0, dateToString( new Date()));
			d =  dateChooser1.getDate();
			if(d != null) datos.put(Labels.DATE1, dateToString(d));
			else datos.put(Labels.DATE1, dateToString( new Date()));
		}
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
		fTFNGrupos.setText("" + cm.getNumberZonas());
		dateChooser0.setDate(new Date());
		dateChooser0.setDate(new Date());
	}
	
	/**
	 * <p>Title: refresh</p>  
	 * <p>Description: Actualiza los controles pertinentes de la vista.</p> 
	 */
	public void refresh() {
		String tipo = TypesFiles.PRJ;
		if(cm.hasModule(tipo)) {
			fTFNombre.setText(cm.getValueFromLabel(tipo,Labels.NAME));		
			fTFAutor.setText(cm.getValueFromLabel(tipo,Labels.AUTHOR));
			textArea.setText(cm.getValueFromLabel(tipo,Labels.DESCRIPTION));
			fTFVersion.setText(cm.getValueFromLabel(tipo,Labels.VERSION));
			dateChooser0.setDate(stringToDate(cm.getValueFromLabel(tipo,Labels.DATE0)));
			dateChooser0.setDate(stringToDate(cm.getValueFromLabel(tipo,Labels.DATE0)));
		}
		fTFNGrupos.setText("" + cm.getNumberZonas());
	}


	private void configurar() {
		fTFNombre = setUpTextField(null,"Nombre del modelo, se usará para dar nombre a los archivos que lo componen.", SwingConstants.LEFT);
		fTFAutor = setUpTextField(null,"Autor del modelo.",SwingConstants.LEFT);
		fTFVersion = setUpTextField("1.0","Número de versión del modelo.",SwingConstants.CENTER);	
		fTFNGrupos = setUpTextField(null,"Número de grupos de población que componen el modelo.",SwingConstants.CENTER);

		//
		fTFNombre.setBounds(275, 83, 235, 19);
		fTFAutor.setBounds(275, 110, 235, 19);
		fTFVersion.setBounds(715, 155, 63, 19);
		fTFNGrupos.setBounds(275, 277, 63, 19);

		//
		setName("Módulos");
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setBackground(Color.LIGHT_GRAY);
		setAutoscrolls(true);
		setLayout(null);
		
		add(fTFNombre);
		add(fTFAutor);
		add(fTFVersion);	
		add(fTFNGrupos);
		

		JLabel lblAutor = new JLabel("Autor/a:");
		lblAutor.setBounds(53, 112, 57, 15);
		add(lblAutor);
		JLabel lblFechaDeltima = new JLabel("Fecha modificación:");
		lblFechaDeltima.setBounds(556, 112, 147, 15);
		add(lblFechaDeltima);
		//
		JLabel label_obligatorio_1 = new JLabel("");
		label_obligatorio_1.setToolTipText("Campo obligatorio");
		label_obligatorio_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_obligatorio_1.setBounds(new Rectangle(28, 277, 15, 15));
		label_obligatorio_1.setIcon(IO.getIcon("/vista/imagenes/Iconos/obligatorio2_64px.png",20,20));
		add(label_obligatorio_1);
		
		JLabel label_obligatorio = new JLabel("");
		label_obligatorio.setToolTipText("Campo obligatorio");
		label_obligatorio.setHorizontalAlignment(SwingConstants.CENTER);
		label_obligatorio.setBounds(new Rectangle(28, 83, 15, 15));

		label_obligatorio.setIcon(IO.getIcon("/vista/imagenes/Iconos/obligatorio2_64px.png",20,20));
		add(label_obligatorio);
		
		
		textArea = new JTextArea();
		textArea.setLocale(new Locale("es", "ES"));
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setBounds(275, 141, 235, 88);
		textArea.setMinimumSize(new Dimension(200, 100));
		textArea.setMaximumSize(new Dimension(2147, 2147));
		textArea.setToolTipText("Introduzca cualquier texto descriptivo que crea necesario acerca del modelo.");
		textArea.setBorder(new LineBorder(Color.BLACK, 1, true));
		textArea.setBackground(Color.WHITE);

		textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		add(textArea);
		
		JLabel lblNombreDelProyecto = new JLabel("Título del Proyecto:");
		lblNombreDelProyecto.setBounds(53, 85, 153, 15);
		add(lblNombreDelProyecto);		
		
		dateChooser1 = new JDateChooser();
		dateChooser1.setBounds(721, 108, 194, 19);
		dateChooser1.setToolTipText("Indica la última fecha de modificación.");
		dateChooser1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		//Desactivar la edición de fechas de forma manual.
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser1.getDateEditor();
		editor.setEditable(false);
		add(dateChooser1);
		
		dateChooser0 = new JDateChooser();
		dateChooser0.setToolTipText("Indica la fecha de creación.");
		dateChooser0.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		dateChooser0.setBounds(724, 83, 191, 19);
		//Desactivar la edición de fechas de forma manual.
		editor = (JTextFieldDateEditor) dateChooser0.getDateEditor();
		editor.setEditable(false);
		add(dateChooser0);
		
		
		JLabel lblDescripcin = new JLabel("Descripción del modelo:");
		lblDescripcin.setBounds(53, 137, 180, 15);
		add(lblDescripcin);
		JLabel lblNmeroDeGrupos = new JLabel("Número de Grupos de estudio:");
		lblNmeroDeGrupos.setBounds(53, 279, 217, 15);
		add(lblNmeroDeGrupos);
		JLabel lblArchivosDelProyecto = new JLabel("Archivos del Proyecto:");
		lblArchivosDelProyecto.setBounds(53, 336, 157, 15);
		add(lblArchivosDelProyecto);
		JLabel txtVersion = new JLabel();
		txtVersion.setText("Versión:");
		txtVersion.setBounds(556, 155, 137, 19);
		add(txtVersion);
		
		JLabel lblFechaDeCreacin = new JLabel("Fecha de creación: ");
		lblFechaDeCreacin.setBounds(556, 85, 137, 15);
		add(lblFechaDeCreacin);

		
		JSeparator separator = new JSeparator();
		separator.setBackground(Color.BLUE);
		separator.setBounds(28, 305, 903, 19);
		add(separator);
		
		
		JButton btnAplicar = new JButton("Aplicar Cambios");
		btnAplicar.addMouseListener(new BotonL());
		btnAplicar.setIcon(IO.getIcon("/vista/imagenes/Iconos/ok_64px.png",64,64));
		btnAplicar.setToolTipText("Guarda los cambios efectuados.");
		btnAplicar.setBounds(682, 201, 233, 74);
		add(btnAplicar);
		

	}
	
	
	/**
	 * <p>Title: stringToDate</p>  
	 * <p>Description: Convierte una cadena de texto que contiene una fecha
	 * en un objeto Date</p> 
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
	
	private String dateToString(Date date) {
		String fecha = null;
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");	//Formato de la fecha.
		fecha = formato.format(date);
		return fecha;
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
		ParametrosProyecto pp = new ParametrosProyecto(new ControladorModulos(),new Archivos(null));
		pp.abrirFrame();
	}
}
