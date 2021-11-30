/**  
* <p>Title: TablaEditor.java</p>  
* <p>Description: </p>    
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
import modelo.DCVS;
import modelo.IO;
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
	private JButton btnAbrirArchivo,btnGuardarArchivo,btnGuardarCambios,btnBorrarTabla,btnImprimir;
	private JButton btnAddRow,btnAddCol,btnNuevaTabla,btnBorrarFila,btnBorrarColumna;
	private JScrollPane scrollPane;
	private JTable tabla;

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
	 * <p>Title: TablaEditor</p>  
	 * <p>Description: Constructor principal</p> 
	 * @param cm Controlador de mapa.
	 */
	public TablaEditor(ControladorModulos cm) {
		this.cm = cm;
		this.editable = true;
		setName("panel_tabla");
		setMaximumSize(new Dimension(1024, 768));
		setLayout(new BorderLayout(0, 0));
		setBorder(null);
		setOpaque(false);
		this.cio = new ControladorDatosIO();
		this.modificado = false;
		initialize();
	}
	
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
	 * <p>Title: abrirFrame</p>  
	 * <p>Description: Abre el módulo en un frame particular</p> 
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
	 * <p>Title: reset</p>  
	 * <p>Description: Reinicia todos los datos del módulo.</p> 
	 */
	public void reset() {nuevaTabla();}

	/**
	 * <p>Title: setUpComboBox</p>  
	 * <p>Description: Configuración del JComboBox con las opciones posibles</p>
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
		setBounds(0, 0, 914, 610);	
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
		jtoolBar.setOrientation(JToolBar.VERTICAL);

		//Añadir los botones
		btnNuevaTabla = addBotonToolBar(btnNuevaTabla, "Crea una nueva tabla desde una plantilla","/vista/imagenes/Iconos/nuevaTabla_64px.png",new BtnNuevaTablaMouseListener(),Color.GREEN);
		btnAddRow= addBotonToolBar(btnAddRow, "Crear fila nueva","/vista/imagenes/Iconos/nuevaFila_64px.png",new BtnAddRowMouseListener(),null);
		btnAddCol = addBotonToolBar(btnAddCol, "Crear columna nueva","/vista/imagenes/Iconos/nuevaColumna_64px.png",new BtnAddColMouseListener(),null);
		jtoolBar.addSeparator();
		btnBorrarFila = addBotonToolBar(btnBorrarFila, "Elimina las filas marcadas","/vista/imagenes/Iconos/eliminarFila_64px.png",new BtnBorrarFilaMouseListener(),Color.ORANGE);
		btnBorrarColumna = addBotonToolBar(btnBorrarColumna, "Elimina las columnas indicas","/vista/imagenes/Iconos/eliminarCol_64px.png",new BtnBorrarColumnaMouseListener(),Color.ORANGE);
		btnBorrarTabla = addBotonToolBar(btnBorrarTabla, "Borrar tabla","/vista/imagenes/Iconos/borrarTabla_64px.png",new BtnBorrarTablaMouseListener(),Color.ORANGE);
		jtoolBar.add(Box.createHorizontalGlue());
		jtoolBar.addSeparator();
		btnGuardarCambios = addBotonToolBar(btnGuardarCambios, "Guardar cambios","/vista/imagenes/Iconos/guardarCambios_64px.png",new BtnGuardarCambiosMouseListener(),null);
		btnGuardarArchivo = addBotonToolBar(btnGuardarArchivo, "Guardar tabla","/vista/imagenes/Iconos/disquete_64px.png",new BtnGuardarArchivoMouseListener(),null);
		btnAbrirArchivo = addBotonToolBar(btnAbrirArchivo, "Cargar tabla","/vista/imagenes/Iconos/carpeta_64px.png",new BtnAbrirArchivoMouseListener(),null);
		jtoolBar.addSeparator();
		btnImprimir = addBotonToolBar(btnImprimir, "Imprimir","/vista/imagenes/Iconos/impresora_64px.png",new BtnImprimirMouseListener(),null);

		//BoxAsignación (JPanel)
		boxAsignacion = new JPanel();
		boxAsignacion.setOpaque(false);
		boxAsignacion.setBorder(null);
		boxAsignacion.setBackground(Color.ORANGE);
		
		setUpComboBox();
		
		btnAsignarTabla = new JButton("Aplicar tipo");
		btnAsignarTabla.setHorizontalAlignment(SwingConstants.LEFT);
		btnAsignarTabla.addMouseListener(new BtnAplicarTablaMouseListener());
		//jtoolBar.add(btnAplicarTabla);
		
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
		tabla.setCellSelectionEnabled(false);
		tabla.setRowSelectionAllowed(true);	
		tabla.setColumnSelectionAllowed(false);
		tabla.setBackground(new Color(224, 255, 255));
		tabla.getTableHeader().setReorderingAllowed(false);
		scrollPane.getViewport().setOpaque(false);

		//Genera el cuerpo de datos y establace.
		nuevaTabla();
		scrollPane.setViewportView(tabla);

		add(boxAsignacion, BorderLayout.SOUTH);
		add(jtoolBar, BorderLayout.WEST);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private void estadoBotones() {
		boolean tieneColumna = dcvs.getColumnCount() > 0;
		boolean tieneFila = dcvs.getRowCount() > 0;
		//Botones de guardado.
		btnGuardarArchivo.setEnabled(modificado);
		btnGuardarCambios.setEnabled(modificado && dcvs.getRuta() != null && dcvs.getTipo() != null);
		if(editable) {
			//Botones nueva tabla, fila, borrarTabla	
			btnBorrarTabla.setEnabled(tieneColumna);							//Botón borrar columna => debe tener alguna columna.
			btnNuevaTabla.setEnabled(tieneColumna);
			btnAddRow.setEnabled(tieneColumna);
			btnBorrarColumna.setEnabled(tieneColumna);
			//Botón borrar fila => debe tener alguna fila.		
			btnBorrarFila.setEnabled(tieneFila);	
			//Controles especiales.
			btnAsignarTabla.setEnabled(tieneColumna && tieneFila);
		}
	}

	/**
	 * <p>Title: addBotonToolBar</p>  
	 * <p>Description: Añade los controles la barra de herramietnas que los contendrá</p>
	 * Los añade con el formato correcto y una separación costante, además,
	 * permite que los botones puedan tener colores personalizados.
	 * @param btn Botón a ser añadido.
	 * @param tooltip Texto de ayuda al botón.
	 * @param ruta Ruta completa hasta el archivo de imagen.
	 * @param ml El observador o listener adjunto al botón.
	 * @param c Color para el botón, null en otro caso.
	 * @return JButton modificado con las propiedades requeridas.
	 */
	private JButton addBotonToolBar(JButton btn, String tooltip,String ruta, MouseAdapter ml, Color c) {
		int hi,wi;
		hi = wi =  25;
		btn = new JButton();
		//btn.setSize(new Dimension(102, 25));
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
	 * <p>Title: addIconB</p>  
	 * <p>Description: Añade un icono a una botón</p>
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
	 * <p>Title: getIcon</p>  
	 * <p>Description: Devuelve un icono escalado a las medidas obtenidas
	 * por parámetros, de la imagen fuente..</p> 
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
	 * <p>Title: setModelo</p>  
	 * <p>Description: Establece un modelo concreto en la tabla.</p> 
	 * El tipo de módulo debe estar definido en: {@link ModuleType Tipos de módulos}.
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
		tabla.setModel(dcvs);
		estadoBotones();
	}
	
	/**
	 * <p>Title: nuevaTabla</p>  
	 * <p>Description: Crea una nueva tabla sin columnas ni filas..</p>
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
		
		this.modificado = false;
		estadoBotones();
	}
	
	/**
	 * Función auxiliar. Muestra cuadros de mensajes. Los cuadros de mensajes
	 * no están enlazados con un hilo padre (null). Un número no definido se 
	 * mostrará como información.
	 * 
	 * El tipo 4 es usado para el botón "Acerca de...", re-escrito para mostrar un 
	 * mensaje tipo 1. 
	 * @param mensaje Texto a mostrar.
	 * @param tipo Es el tipo de cuadro de mensaje. Siendo:
	 *  <p>0 JOptionPane.ERROR_MESSAGE</p>
	 *  <p>1 JOptionPane.INFORMATION_MESSAGE</p>
	 *  <p>2 JOptionPane.WARNING_MESSAGE</p>
	 *  <p>3 JOptionPane.QUESTION_MESSAGE</p>
	 *  <p>4 JOptionPane.PLAIN_MESSAGE</p>
	 *  <p>10 JOptionPane.showConfirmDialog OK_CANCEL_OPTION</p>
	 * @return Devuelve el valor de la opción elegida para ShowConfirmDialog, 0 en otro caso.
	 * @see JOptionPane
	 */
	private int mostrar(String mensaje, int tipo ) {
		String titulo = "";
		int eleccion = 0;
		switch(tipo) {
		case 0: titulo = "Error";break;
		case 1: titulo = "Información"; break;
		case 2: titulo = "¡Antención!"; break;
		case 3: titulo = "Consulta"; break;
		case 4: titulo = "Acerca de..."; tipo = 1; break;
		case 10: titulo = "Atención";break;
		default:
		}
		
		if(tipo == 10) {
			eleccion = JOptionPane.showConfirmDialog(null, mensaje, titulo, JOptionPane.OK_CANCEL_OPTION);
		}else {
			JOptionPane.showMessageDialog(null, mensaje, titulo, tipo);
		}
		
		return eleccion;
	}
	
	private class BtnImprimirMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				try {tabla.print();}
				catch (PrinterException e1) {e1.printStackTrace();}
			}
		}
	}
	
	private class BtnAddColMouseListener extends MouseAdapter {
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
	
	private class BtnAddRowMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				int ncols = dcvs.getColumnCount();							//Obtención del número de columnas.
				if(ncols > 0) {													//Comprobación de que existe al menos una columna.
					Object[] row = new Object[ncols];							//Generar una fila con tantos campos como columnas tiene la tabla.
					dcvs.addFila(row);											//Se añade.
					modificado = true;
				}else {
					mostrar("Debe añadir alguna columna.", 0);
				}
				estadoBotones();
			}
		}
	}
	
	private class BtnAbrirArchivoMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				if(dcvs.getTipo() == null) dcvs.setTipo(TypesFiles.CSV);
				DCVS dcvs2 = cio.abrirArchivo(null,dcvs.getTipo());			
				if(dcvs2 != null) {
					dcvs = dcvs2;
					modificado = false;
					tabla.setModel(dcvs);										//Estabece el nuevo modelo en el scroll tabla.
					mostrar("Archivo Cargado", 1);
					estadoBotones();
				}
			}
		}
	}
	
	private class BtnGuardarCambiosMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled() && modificado) {
				if(dcvs.getTipo() == null) dcvs.setTipo(TypesFiles.CSV);
//				dcvs.setModelo(tabla.getModel());
//				* Ver si se realizan los cambios en dvs sin necesidad de establecer sobre si mismo el modelo en dcvs
				if(dcvs.getRowCount() >0) {
					String rutaF = cio.guardarArchivo(dcvs);
					if(rutaF != null) {
						mostrar("Archivo guardado", 1);
						dcvs.setRuta(rutaF);
						modificado = false;
						estadoBotones();
					}
				} else mostrar("No hay datos que guardar",0);
			}
		}
	}
	
	private class BtnGuardarArchivoMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				String rutaF;
				//Sino tiene de un tipo se le asigna el tipo general.
				if(dcvs.getTipo() == null) dcvs.setTipo(TypesFiles.CSV);	
				if(dcvs.getRowCount() >0) {
					rutaF = cio.guardarArchivo(dcvs);
					if(rutaF != null) {
						mostrar("Archivo guardado", 1);
						dcvs.setRuta(rutaF);
						modificado = false;
						estadoBotones();
					}					
				} else mostrar("No hay datos que guardar",0);

			}
		}
	}
	
	private class BtnBorrarFilaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				dcvs.delFilas(tabla.getSelectedRows());
				modificado = true;
				estadoBotones();
			}
		}
	}
	
	private class BtnBorrarColumnaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				tabla.setModel( dcvs.delColumnas(tabla.getSelectedColumns()));											//Establecemos el nuevo modelo en la tabla.
				modificado = true;
				estadoBotones();
			}
		}
	}
	
	/**
	 * <p>Title: BtnAplicarTablaMouseListener</p>  
	 * <p>Description: Clase dedicada al establecimiento de los datos en los
	 * apartados o módulos oportunos.</p>  
	 * @author Silverio Manuel Rosales Santana
	 * @date 10 ago. 2021
	 * @version versión
	 */
	private class BtnAplicarTablaMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				boolean actuar = true;
				int respuesta = 0;
				//Obtener selección
				ModuleType seleccion = (ModuleType) comboBox.getSelectedItem();
				
				if(seleccion != ModuleType.CSV) {
					respuesta = mostrar("Atención, esta acción sobreescribe los datos existentes en el módulo " + seleccion, 10);
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
					mostrar("Datos aplicados al módulo: " + seleccion, 1);
					estadoBotones();
				}	
			}
		}
		
		/**
		 * <p>Title: setTipo</p>  
		 * <p>Description: Realiza una conversión de tipos de archivos definidos
		 *  en la clase {@link TypesFiles} a tipos enumerados de {@link ModuleType}.</p> 
		 * @param seleccion Tipo enumerado de la clase {@link ModuleType}
		 * @return Valor correspondiente a los tipos de la clase {@link TypesFiles}.
		 * @see ModuleType
		 */
		private String setTipo(ModuleType seleccion) {
			String tipo = "CVS";
			switch(seleccion){
			case MAP:
				tipo = TypesFiles.MAP;
				break;
			case HST: 
				cm.setHistorico(dcvs);
				tipo = TypesFiles.HST;
				break;
			case PAL:
				//Cambiado, esto recibía el modelo de la tabla.
				cm.setPaleta(dcvs);
				mostrar("Nueva paleta asignada", 1);
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
				break;
			}
			
			return tipo;
		}
	}
	
	private class BtnBorrarTablaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				//Mostrar dialogo de confirmación
				int opt = JOptionPane.showConfirmDialog(null, "¿Desea eliminar la tabla actual con sus datos?", "Aviso", JOptionPane.YES_NO_OPTION);
				//Caso afirmativo borrar el modelo y crear uno nuevo.
				if(opt == JOptionPane.YES_OPTION) {	nuevaTabla(); }
			}
		}
	}


	private class BtnNuevaTablaMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if(((JButton) e.getSource()).isEnabled()) {
				//Mostrar dialogo de confirmación
				int opt = JOptionPane.showConfirmDialog(null, "Eliminará la tabla actual con sus datos ¿Desea continuar?", "Aviso", JOptionPane.YES_NO_OPTION);
				//Caso afirmativo borrar el modelo y crear uno nuevo.
				if(opt == JOptionPane.YES_OPTION) {	nuevaTabla(); }
			}
		}
	}
	
	private class TableUpdateListener implements TableModelListener {
		
        @Override
        public void tableChanged(TableModelEvent tme) {
            if (tme.getType() == TableModelEvent.UPDATE) {
                modificado = true;
            }
        }
    }
	
	
	@SuppressWarnings("javadoc")
	public static void main(String[] args) {
		TablaEditor te = new TablaEditor(new ControladorModulos());
		te.abrirFrame("Testing");
	}

}
