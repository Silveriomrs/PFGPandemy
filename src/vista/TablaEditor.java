/**  
* <p>Title: TablaEditor.java</p>  
* <p>Description: Modulo independiente cuyo objetivo es el tratamiento de los datos
*  en formato de tabla, permite importar datos en formato CVS así como exportarlo, 
*   convertir a formatos adecuados a un tipo de módulo interno.</p>
* Este módulo, puede ejecutarse tanto integrado en el resto de la aplicación, como de manera
*  externa (con su ventana independiente).
* <p>>El módulo permite también imprimir los resultados (si el sistema dispone de impresora,
*  crear nuevas tablas, editar una tabla existente, añadir y eliminar filas o columnas, 
*   borrar una tabla, guardar los cambios o la tabla nueva y aplicarla a otro módulo.</p>
* Por último, este módulo disponen de una barra de herramientas flotante, configurable en caliente
*  para facilitar el trabajo con dicha tabla.
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 30 sept. 2021
* @version 2.2
*/
package vista;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.awt.*;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import controlador.ControladorDatosIO;
import controlador.ControladorModulos;
import controlador.IO;
import modelo.DCVS;
import modelo.DCVSFactory;
import modelo.ModuleType;
import modelo.TypesFiles;


/**
 * <p>Title: TablaEditor</p>  
 * <p>Description: Clase editora de los datos de entrada en formato CSV</p>
 * Permite la edición de los datos principales de cada módulo perteneciente
 * al programa.  
 * @author Silverio Manuel Rosales Santana
 * @date 30 sept. 2021
 * @version versión 1.0
 */
public class TablaEditor extends JPanel{

	/** serialVersionUID*/  
	private static final long serialVersionUID = -3765555595448024955L;
	//
	private final String IVentana = "/vista/imagenes/Iconos/editorGrafico_128px.png";
	//
	private JButton btnGuardarArchivo,btnGuardarCambios,btnBorrarTabla;
	private JButton btnAddRow,btnAddCol,btnNuevaTabla,btnBorrarFila,btnBorrarColumna;
	private JButton btnPrintTable;
	private JScrollPane scrollPane;
	private JTable tabla;
	private CellRenderTableEditor cellRender;

	private DCVS dcvs;
	private JLabel lblAsignarTablaA;
	private JComboBox<ModuleType> comboBox;
	private JButton btnAsignarTabla;
	private ControladorDatosIO cio;
	private ControladorModulos cm;
	private JToolBar jtoolBar;
	private JPanel boxAsignacion;
	private JFrame frame;
	private Image imagen;
	private String ruta = "/vista/imagenes/degradado.png";
	
	private boolean modificado;
	private boolean editable; 													//Habilita la edición del número de columnas y filas.

	
	/**
	 * Constructor principal, establece los atributos iniciales de la vista, como si 
	 *  es editable, el editor de celdas, y establece que no ha sido modificado.
	 * @param cm Controlador de mapa.
	 */
	public TablaEditor(ControladorModulos cm) {
		this.cm = cm;
		this.editable = true;
		this.cellRender = new CellRenderTableEditor(editable);
		setName("panel_tabla");
		setMaximumSize(new Dimension(1024, 768));
		setLayout(new BorderLayout(0, 0));
		setBorder(null);
		setOpaque(false);
		this.cio = new ControladorDatosIO();
		this.modificado = false;
		initialize();
	}
	
	/**
	 * Sobrescritura del método para colocar una imagen de fondo con transparencias.
	 */
	@Override
	public void paint(Graphics g) {
		if(ruta != null) {
			imagen = new ImageIcon(getClass().getResource(ruta)).getImage();
			g.drawImage(imagen,0,0,getWidth(),getHeight(),this);
			setOpaque(false);
			super.paint(g);
		}
	}

	/**
	 * Abre el módulo en un frame particular. 
	 * @param nombre Nombre a mostrar en el marco. El nombre del módulo.
	 */
	public void abrirFrame(String nombre) {
		frame = new JFrame("Editor de tablas");
		frame.setTitle(nombre);
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setBounds(40, 50, 914, 610);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(this);
		frame.setIconImage(IO.getImagen(IVentana,false,0,0));
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Reinicia todos los datos del módulo.
	 */
	public void reset() {nuevaTabla();}

	/**
	 * Configuración del JComboBox con las opciones posibles.
	 */
	private void setUpComboBox() {
		comboBox = new JComboBox<ModuleType>();
		comboBox.setMaximumSize(new Dimension(102, 25));
		comboBox.setMinimumSize(new Dimension(102, 25));
		comboBox.setName("Asignar tabla");
		comboBox.setToolTipText("Seleccione el tipo de tabla.");
		comboBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		comboBox.setModel(new DefaultComboBoxModel<ModuleType>(ModuleType.values()));
	}
	
	/**
	 * Inicialización de los contenidos del frame.
	 */
	private void initialize() {
		setBounds(40, 30, 934, 680);											//Dimensiones y posicióno del panel que contiene la tabla.	
		//Configuración del panel con Scroll de la tabla.
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setBackground(Color.GRAY);
		scrollPane.setOpaque(false);
		scrollPane.setAutoscrolls(true);
		scrollPane.setViewportBorder(null);
		scrollPane.setName("scrollTabla");
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		//ToolBar
		jtoolBar = new JToolBar(JToolBar.VERTICAL);
		jtoolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		jtoolBar.setForeground(Color.BLACK);
		jtoolBar.setName("Barra Herramientas");
		jtoolBar.setOpaque(false);

		//Añadir los botones
		btnNuevaTabla = addBotonToolBar("Crea una nueva tabla desde una plantilla","/vista/imagenes/Iconos/nuevaTabla_64px.png",new BtnNuevaTablaML(),Color.GREEN);
		btnAddRow= addBotonToolBar("Crear fila nueva","/vista/imagenes/Iconos/nuevaFila_64px.png",new BtnAddRowML(),null);
		btnAddCol = addBotonToolBar("Crear columna nueva","/vista/imagenes/Iconos/nuevaColumna_64px.png",new BtnAddColML(),null);
		jtoolBar.addSeparator();
		btnBorrarFila = addBotonToolBar("Elimina las filas marcadas","/vista/imagenes/Iconos/eliminarFila_64px.png",new BtnBorrarFilaML(),Color.ORANGE);
		btnBorrarColumna = addBotonToolBar("Elimina las columnas indicas","/vista/imagenes/Iconos/eliminarCol_64px.png",new BtnBorrarColumnaML(),Color.ORANGE);
		btnBorrarTabla = addBotonToolBar("Borrar tabla","/vista/imagenes/Iconos/borrarTabla_64px.png",new BtnBorrarTablaML(),Color.ORANGE);
		jtoolBar.add(Box.createHorizontalGlue());
		jtoolBar.addSeparator();
		btnGuardarCambios = addBotonToolBar("Guardar cambios","/vista/imagenes/Iconos/guardarCambios_64px.png",new BtnGuardarCambiosML(),null);
		btnGuardarArchivo = addBotonToolBar("Guardar tabla","/vista/imagenes/Iconos/disquete_64px.png",new BtnGuardarArchivoML(),null);
		addBotonToolBar("Cargar tabla","/vista/imagenes/Iconos/carpeta_64px.png",new BtnAbrirArchivoML(),null);
		jtoolBar.addSeparator();
		btnPrintTable = addBotonToolBar("Imprimir","/vista/imagenes/Iconos/impresora_64px.png",new BtnImprimirML(),null);

		//BoxAsignación (JPanel)
		boxAsignacion = new JPanel();
		boxAsignacion.setOpaque(false);
		boxAsignacion.setBorder(null);
		boxAsignacion.setBackground(Color.ORANGE);
		
		setUpComboBox();
		
		btnAsignarTabla = new JButton("Aplicar tipo");
		btnAsignarTabla.setHorizontalAlignment(SwingConstants.LEFT);
		btnAsignarTabla.addMouseListener(new BtnAplicarTablaML());
		
		lblAsignarTablaA = new JLabel("Asignar a modulo:");
		lblAsignarTablaA.setForeground(UIManager.getColor("Button.highlight"));
		lblAsignarTablaA.setFont(new Font("Fira Code Retina", Font.BOLD, 15));
		lblAsignarTablaA.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lblAsignarTablaA.setBackground(Color.BLUE);

		boxAsignacion.add(lblAsignarTablaA);
		boxAsignacion.add(comboBox);
		boxAsignacion.add(btnAsignarTabla);
		
		//Creación tabla principal.
		tabla = new JTable();
		tabla.setOpaque(false);
		tabla.setAutoCreateRowSorter(true);
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabla.setBorder(null);
		tabla.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tabla.setCellSelectionEnabled(true);
		//
		tabla.setShowHorizontalLines(true);
		tabla.setRowSelectionAllowed(true);
		tabla.setColumnSelectionAllowed(false);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.setDefaultRenderer(Object.class,cellRender);
		scrollPane.getViewport().setOpaque(false);

		//Genera el cuerpo de datos y establace.
		nuevaTabla();
		scrollPane.setViewportView(tabla);

		add(boxAsignacion, BorderLayout.SOUTH);
		add(jtoolBar, BorderLayout.WEST);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * Configura el estado de los botones en función del contexto.
	 * Tiene en cuenta el número de columnas y filas de la tabla en curso,
	 *  si ha sido realizado algún cambio y la opción establecida en el control del
	 *   selector.
	 */
	private void estadoBotones() {
		boolean tieneColumna = dcvs.getColumnCount() > 0;
		boolean tieneFila = dcvs.getRowCount() > 0;
		//Botones de guardado.
		btnGuardarArchivo.setEnabled(modificado);
		btnGuardarCambios.setEnabled(modificado && dcvs.getRuta() != null && dcvs.getTipo() != null);
		//Por ser primer boton en ejecutarse por defecto aparece activo,
		//Colocado fuera del condicional permite que se desactive al primer cambio
		//en caso de no estar habilitada la opción de edición.
		btnAddCol.setEnabled(editable);
		//Botón de imprimir según tenga o no datos para imprimir.
		btnPrintTable.setEnabled( tieneColumna && tieneFila);
		if(editable) {
			//Botones nueva tabla, fila, borrarTabla	
			btnBorrarTabla.setEnabled(tieneColumna);							//Botón borrar columna => debe tener alguna columna.
			btnNuevaTabla.setEnabled(true);
			btnAddRow.setEnabled(tieneColumna);
			btnBorrarColumna.setEnabled(tieneColumna);
			//Botón borrar fila => debe tener alguna fila.		
			btnBorrarFila.setEnabled(tieneFila);	
			//Controles especiales.
			btnAsignarTabla.setEnabled(tieneColumna && tieneFila);
		}else {
			btnAsignarTabla.setEnabled(true);
			btnAsignarTabla.setText("Actualizar");
		}
	}

	/**
	 * <p>Añade los controles la barra de herramietnas que los contendrá</p>
	 * Los añade con el formato correcto y una separación costante, además,
	 * permite que los botones puedan tener colores personalizados.
	 * @param tooltip Texto de ayuda al botón.
	 * @param ruta Ruta completa hasta el archivo de imagen.
	 * @param ml El observador o listener adjunto al botón.
	 * @param c Color para el botón, null en otro caso.
	 * @return JButton modificado con las propiedades requeridas.
	 */
	private JButton addBotonToolBar(String tooltip,String ruta, MouseAdapter ml, Color c) {
		int hi,wi;
		hi = wi =  25;
		JButton btn = new JButton();
		btn.setHorizontalTextPosition(SwingConstants.RIGHT);
		btn.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.addMouseListener(ml);
		btn.setToolTipText(tooltip);
		if(c != null) btn.setBackground(c);
		addIconB(btn,ruta,wi,hi);
		jtoolBar.add(btn);
		return btn;
	}
	
	/**
	 * <p>Añade un icono a una botón</p>
	 * Los valores de dimensión de ancho y largo se establecen en función de los 
	 * datos pasados por parámetro. 
	 * @param componente Etiqueta a la que adjuntar el icono
	 * @param ruta Nombre del archivo y su ruta.
	 * @param w Ancho a escalar de la imagen original.
	 * @param h Alto a escalar de la imagen original.
	 */
	private void addIconB(JButton componente, String ruta, int w, int h) {
		componente.setIconTextGap(0);
		componente.setIcon(getIcon(ruta,w,h));	
	}
	
	/**
	 * <p>Devuelve un icono escalado a las medidas obtenidas por parámetros, de la imagen fuente.</p> 
	 * @param ruta Ruta del archivo de imagen.
	 * @param w Ancho del escalado de la imagen.
	 * @param h Alto del escalado de la imagen.
	 * @return Icono escalado de la imagen, null en otro caso.
	 */
	private ImageIcon getIcon(String ruta, int w, int h) {
		ImageIcon icon = IO.getIcon(ruta, w, h);
		return icon;
	}
	
	/**
	 * <p>Establece un modelo concreto en la tabla.</p> 
	 * El tipo de módulo debe estar definido en: \ref modelo#ModuleType
	 * @param dcvsIn JTableModel o modelo que se quiere establecer.
	 * @param editable TRUE si se quiere permitir la edición del número de columnas y filas.
	 *  FALSE en otro caso.
	 */
	public void setModelo(DCVS dcvsIn, boolean editable) {
		this.editable = editable;
		this.dcvs = dcvsIn;
		//Obtener valor Enúmerado.
		String tipo = dcvs.getTipo();
		ModuleType mt = ModuleType.valueOf(tipo.toUpperCase());
		//Establecer en el selector el tipo.
		comboBox.setSelectedItem(mt);
		//Si el tipo no es General (CVS) bloquearlo para impedir inconsistencias por error de asignación.
		comboBox.setEnabled(tipo.equals(TypesFiles.CSV));
		//Añadir listener para los controles.
		dcvs.addTableModelListener(new TableUpdateListener());
		tabla.setDefaultRenderer(Object.class,new CellRenderTableEditor(editable));
		tabla.setModel(dcvs);
		estadoBotones();
	}
	
	/**
	 * Crea una nueva tabla sin columnas ni filas.
	 */
	private void nuevaTabla() {
		//Cuerpo de datos
		Object[][] datos = new Object[][]{};
		//Cabecera de datos.		
		String[] cabecera = new String[] {};
		dcvs = new DCVS();
		dcvs.setTipo(TypesFiles.CSV);
		dcvs.setModelo(new DefaultTableModel(datos,cabecera){
			private static final long serialVersionUID = 5615251971828569240L;
		});
		//Listener que avisará de cambios en la tabla -> activa botón de guardado.
		dcvs.addTableModelListener(new TableUpdateListener());
		tabla.setModel(dcvs);
		//Al ser tabla nueva en principio es completamente editable.
		this.editable = true;
		this.modificado = false;
		estadoBotones();
	}
	
	/**
	 * Realiza una conversión de tipos de archivos definidos
	 *  en la clase \ref modelo#TypesFiles a tipos enumerados de \ref modelo#ModuleType . 
	 * @param seleccion Tipo enumerado de la clase \ref modelo#ModuleType .
	 * @return Valor correspondiente a los tipos de la clase \ref modelo#TypesFiles .
	 * @see ModuleType
	 */
	private String setTipo(ModuleType seleccion) {
		String tipo;
		switch(seleccion){
		case MAP:
			tipo = TypesFiles.MAP;
			break;
		case HST: 
			tipo = TypesFiles.HST;
			break;
		case PAL:
			tipo = TypesFiles.PAL;
			break;
		case REL:
			tipo = TypesFiles.REL;
			break;
		case DEF:
			tipo = TypesFiles.DEF;
			break;
		case PRJ:
			tipo = TypesFiles.PRJ;
			break;
		case CSV:
			tipo = TypesFiles.CSV;
			break;
		case GRP:
			tipo = TypesFiles.GRP;
			break;
		default:
			tipo = TypesFiles.CSV;
			break;
		}
		
		return tipo;
	}
	
	/**
	 * <p>Clase dedicada al establecimiento de los datos en los apartados o módulos oportunos, mediante la
	 *  comunicación con el módulo controlador.</p> 
	 * Esta clase además está implicada en el mantenimiento de la coherencia respecto al contexto de la aplicación
	 *  de los diferentes controles implicados en esta vista.
	 * @author Silverio Manuel Rosales Santana
	 * @date 10 ago. 2021
	 * @version versión 1.2
	 */
	private class BtnAplicarTablaML extends MouseAdapter {

		/**
		 * Sobrescritura del método que detecta la pulsación del ratón sobre el elemento asociado.
		 * Esta función tiene asignada la asignación de la tabla al módulo que esté seleccionado en el
		 *  combobox.
		 *  <p>Antes de aplicar la selección avisa de la sobrescritura de los dantos anteriores y en caso
		 *   de aceptar, ejecuta la acción mediante el paso de la tabla al controlador a través de su
		 *    doAction.</p>
		 *  Esta función fuerza la revisión del estado de los botones con el nuevo contexto de la aplicación.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			boolean activo = ((JButton) e.getSource()).isEnabled();
			if(activo) {
				boolean actuar = true;
				int respuesta = 0;
				//Obtener selección
				ModuleType seleccion = (ModuleType) comboBox.getSelectedItem();
				
				if(seleccion != ModuleType.CSV) {
					respuesta = cm.showMessage("Atención, esta acción sobreescribe los datos existentes en el módulo " + seleccion, 3);
				}
				
				System.out.println(seleccion);
				
				// Si se acepta ...
				if(respuesta == JOptionPane.OK_OPTION) {
					// Establecer tipo.
					dcvs.setTipo(setTipo(seleccion));
					// Mandarlo a procesar al CM.	
					actuar = cm.doActionTableEditor(dcvs);			
				}
				//En caso de haber aceptado, mensaje al usuario y actualizar estado de los controles.
				if(actuar) {
					cm.showMessage("Datos aplicados al módulo: " + seleccion, 1);
					estadoBotones();
				}	
			} else {cm.doActionTableEditor(null);}
		}
	}
	
	/**
	 * Gestiona la acción de imprimir la tabla actual al pulsar
	 *  sobre el control que tenga asignado.
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BtnImprimirML extends MouseAdapter {
		
		/**
		 * Sobrescritura de pulsación de botón del ratón sobre el control.
		 * <p>Imprime la tabla actual en el dispositivo de impresión que tenga establecido
		 *  el sistema operativo.</p>
		 * Como precondición requiere tener una impresora instalada y configurada en el sistema operativo.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				try {tabla.print();}
				catch (PrinterException e1) {System.out.println("Error dealing with the printer.");}
			}
		}
	}
	
	/**
	 * Gestiona la acción de añadir una nueva columna a la tabla actual
	 * 	al pulsar sobre el control asignado.
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BtnAddColML extends MouseAdapter {
		
		/**
		 * Sobrescritura de pulsación de botón del ratón sobre el control.
		 * <p>Añade una nueva columna a la tabla actual.</p>
		 *  Como precondición, antes de añadir la columna solicita el nombre de la columna, el que será
		 *   añadido a la tabla. No debe ser nulo o cadena vacía.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				String txt = JOptionPane.showInputDialog("¿Nombre de la nueva columna?");
				//Si no se ha cancelado o no se ha introducido texto -> procede
				if(txt != null && !txt.equals("")){									
					dcvs.addColumna(txt);										//Añade la columna.
					modificado = true;
					estadoBotones();
				}
			}
		}
	}
	
	/**
	 * Gestiona la acción de añadir una fila a la tabla actual
	 *  al pulsar sobre el control asignado. 
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BtnAddRowML extends MouseAdapter {
		
		/**
		 * Sobrescritura de pulsación de botón del ratón sobre el control.
		 * <p>Añade una nueva fila a la tabla actual.</p>
		 * Como precondición requiere que exista al menos una columna en la tabla.
		 *  En caso de cumplir la precondición, añade una nueva fila con tantas celdas
		 *   como columnas contiene la tabla.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				int ncols = dcvs.getColumnCount();								//Obtención del número de columnas.
				if(ncols > 0) {													//Comprobación de que existe al menos una columna.
					Object[] row = new Object[ncols];							//Generar una fila con tantos campos como columnas tiene la tabla.
					dcvs.addFila(row);											//Se añade.
					modificado = true;											//Activa la bandera de modificación.
				}else {
					cm.showMessage("Debe añadir alguna columna.", 0);
				}
				estadoBotones();												//Actualización de los estados de los botones.
			}
		}
	}
	
	/**
	 *  Gestiona la carga de un fichero desde la unidad de almacenamiento
	 *  al pulsar sobre el control asignado.  
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BtnAbrirArchivoML extends MouseAdapter {
		
		/**
		 * Sobrescritura de pulsación de botón del ratón sobre el control.
		 * <p>Abre una tabla desde la unidad de almacenamiento. En caso de una tabla
		 *  no válida no realiza acción.</p>
		 * Una vez cargado, actualiza la ruta en el propio módulo, emite avisos al usuario,
		 *  desactiva bandera de modificación y actualiza el estado de los botones.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				if(dcvs.getTipo() == null) dcvs.setTipo(TypesFiles.CSV);
				DCVS dcvs2 = cio.abrirArchivo(null,dcvs.getTipo());				//Abre la nueva tabla en una variable temporal.
				//En caso de ser una tabla no nula...
				if(dcvs2 != null) {
					dcvs = dcvs2;												//Establecer la nueva tabla como tabla activa.
					modificado = false;											//Desactivar el registro de modificado.
					tabla.setModel(dcvs);										//Estabece el nuevo modelo en el scroll tabla.
					cm.showMessage("Archivo Cargado", 1);
					estadoBotones();											//Actualiza el estado de los botones.
				}
			}
		}
	}
	
	/**
	 * Gestiona el guardado de los cambios realizados en la tabla actual en el fichero asignado
	 *  y en unidad de almacenamiento al activar el control.
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BtnGuardarCambiosML extends MouseAdapter {
		
		/**
		 *  Sobrescritura de pulsación de botón del ratón sobre el control.
		 *  <p>Utiliza la ruta de archivo almacenada en el propio módulo para guardar
		 *   los datos de la tabla actual. Cuando no tiene un tipo establecido,
		 *    el tipo por defecto es CVS.</p>
		 *  Como precondición requiere que al menos exista una fila en la tabla,
		 *   además de que haya sido guardado previamente, es decir, tenga una ruta
		 *    prestablecida previamente.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled() && modificado) {
				if(dcvs.getRowCount() >0) {
					// Sistema que era identico al de la clase BtnGuardarArchivoML.
					cio.guardarArchivo(dcvs);
					cm.showMessage("Archivo guardado", 1);
					modificado = false;
					estadoBotones();
				} else cm.showMessage("No hay datos que guardar",0);
			}
		}
	}
	
	/**
	 * Realiza el guardado de la tabla actual en el destino que se indique al pulsar sobre
	 *  el control al que esté asignado. 
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BtnGuardarArchivoML extends MouseAdapter {
		
		/**
		 * Sobrescritura de pulsación de botón del ratón sobre el control.
		 * <p>Guarda la tabla actual en la unidad de almacenamiento si el número de filas
		 *  es superior a 0.</p>
		 * Una vez guardado, actualiza la ruta en el propio módulo, emite avisos al usuario,
		 *  activa bandera de modificación y actualiza el estado de los botones.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				//Sino tiene de un tipo se le asigna el tipo general.
				if(dcvs.getTipo() == null) dcvs.setTipo(TypesFiles.CSV);	
				if(dcvs.getRowCount() >0) {										//Comprobación de número de filas mínimo.
					String rutaF = cio.guardarArchivo(dcvs);							//Obtención de la ruta de guardado.
					//En caso de guardado correcto, proceder...
					if(rutaF != null) {
						cm.showMessage("Archivo guardado", 1);
						dcvs.setRuta(rutaF);									//Establecimiento de la nueva ruta al módulo.
						modificado = false;										//Activación flag modificado.
						estadoBotones();										//Actualización del estado de los botones.
					}					
				} else cm.showMessage("No hay datos que guardar",0);
			}
		}
	}
	
	/**
	 * Elimina la fila/s indicada/s al activar el control al que esté asignada. 
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BtnBorrarFilaML extends MouseAdapter {
		
		/**
		 * Sobrescritura de pulsación de botón del ratón sobre el control con el objetivo
		 *  de eliminar las filas seleccionadas de la tabla, modifica la bandera de mofidicado
		 *   y actualiza el estado de los botones.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				dcvs.delFilas(tabla.getSelectedRows());
				modificado = true;
				estadoBotones();
			}
		}
	}
	
	/**
	 * Elimina la/s columna/s seleccionadas al activar el control 
	 *  al que este asignada.
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BtnBorrarColumnaML extends MouseAdapter {
		
		/**
		 * Sobrescritura de pulsación de botón del ratón sobre el control.
		 *  Procede a la eliminación de las columnas seleccionadas, activa la
		 *   bandera de modificación y actualiza el estado de los botones.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				tabla.setModel( dcvs.delColumnas(tabla.getSelectedColumns()));	//Establecemos el nuevo modelo en la tabla.
				modificado = true;
				estadoBotones();
			}
		}
	}
		
	/**
	 * <p>Elimina la tabla actual con su contenido inclusive al activar el control
	 *  al que se asigne.</p>
	 * Previamente muestra un mensaje de aviso solicitando confirmación. 
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BtnBorrarTablaML extends MouseAdapter {
		
		/**
		 * Sobrescrita la función de detección de pulsación de la tecla del ratón.
		 * <p>Emite mensaje previo con solicitud de confirmación de acción, 
		 *  aceptada la acción elimina la tabla actual y genera una nueva vacía.</p>
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				//Mostrar dialogo de confirmación
				int opt = cm.showMessage("¿Desea eliminar la tabla actual con sus datos?",3);
				//Caso afirmativo borrar el modelo y crear uno nuevo.
				if(opt == JOptionPane.YES_OPTION) {	
					//Obtener atributos previos de la tabla anterior.
					DCVS temp = dcvs;
					nuevaTabla();
					dcvs.setDirectorio(temp.getDirectorio());
					dcvs.setTipo(temp.getTipo());
					dcvs.setName(temp.getNombre());
					dcvs.setDate(temp.getDate());
					dcvs.setRuta(temp.getRuta());
				}
			}
		}
	}

	/**
	 * Crea una nueva tabla eliminando la tabla anterior al activarse
	 *  el control al que se asigne.
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class BtnNuevaTablaML extends MouseAdapter {
		
		/**
		 * Sobrescritura del método heredado de la pulsación de un botón del ratón,
		 *  Ofrece una lista de opciones entre las que elegir el tipo de plantilla a cargar.
		 * <p>emite un mensaje de confirmación antes de proceder a la eliminación de la tabla
		 *   existente y el mensaje de selección de la nueva tabla a crear.</p>
		 * En caso de cierre de alguno de los mensajes emitidos, no se realiza modificación.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				//Mostrar dialogo de confirmación
				int opt = cm.showMessage("Eliminará la tabla actual con sus datos ¿Desea continuar?", 3);
				//Caso afirmativo borrar el modelo y crear uno nuevo.
				if(opt == JOptionPane.YES_OPTION) {
					ModuleType[] options = new ModuleType[] {ModuleType.CSV,ModuleType.PRJ,ModuleType.DEF,ModuleType.PAL,ModuleType.MAP};
					ModuleType opt2 = (ModuleType) JOptionPane.showInputDialog(getParent(),
							"Escoja una plantilla",								//Mensaje de solicitud
							"Selección de plantilla",							//Mensaje de ventana
							JOptionPane.QUESTION_MESSAGE,						//Tipo de mensaje (pregunta selección)
							null,												//Icono personalizado
							options,											//Array de opciones.
							options[0]);										//Opción por defecto, la primera.
					if(opt2 == ModuleType.CSV) nuevaTabla();					//Caso rápido de tabla general.
					else if(opt2 != null) {										//Para otros casos correctos procesar.
						dcvs = DCVSFactory.newModule(setTipo(opt2));			//Obtención de la tabla y asignación.
						tabla.setModel(dcvs);									//Establecimiento en el módelo actual.
					}
				}
			}
		}
	}
	
	/**
	 * Esta clase es una clase suscriptora apoyada en un patrón
	 *  Observer. Su finalidad es detectar cambios en la tabla actual para notificar al resto
	 *   de suscriptores, permitiendo tener actualizados el resto de controles.
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 1.0
	 */
	private class TableUpdateListener implements TableModelListener {
		
		/**
		 * Sobrescritura del método permitiendo adaptarlo para poder activar el 
		 *  registro de cambio en las propiedades o elementos de la tabla.
		 */
        @Override
        public void tableChanged(TableModelEvent tme) {
            if (tme.getType() == TableModelEvent.UPDATE) {						//Comprobación de que se ha actualizado un atributo de la tabla.
                modificado = true;												//Activa la bandera de modificado.
            }
        }
    }

}
