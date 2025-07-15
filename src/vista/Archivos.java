/**  
* Esta clase esta destinada a la carga de los diferentes
*  módulos de la aplicación, mostrando en la vista aquellos módulos cargados
*   además presentae una serie de controles comunes (guardar, guardar como, abrir)
*    y unas opciones particulares (editar y borrar).
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 26 ago. 2021  
* @version 2.6  
*/  
package vista;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import controlador.ControladorModulos;
import controlador.IO;
import modelo.ImagesList;
import modelo.Labels_GUI;
import modelo.OperationsType;
import modelo.TypesFiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * Vista de los archivos asociados a cada módulo del proyecto. 
 * @author Silverio Manuel Rosales Santana
 * @date 17 nov. 2023
 * @version 2.8
 */
public class Archivos extends JPanel {

	/** serialVersionUID*/  
	private static final long serialVersionUID = 5320403899683788073L;	
	private TitledBorder tb;
	private Image imagen;
	private String fondo = ImagesList.BCKGND_FILES;
	//
	private final int hi;														//Altura de los elementos (principalmente iconos).
	private final int wi;														//Anchura de los iconos.
	private final String iconAbrir = ImagesList.FOLDER;
	private final String iconGuardarComo = ImagesList.DISK_1;
	private final String iconGuardarCambios = ImagesList.DISK_2;
	private final String iconEditar = ImagesList.EDIT_TABLE;
	private final String iconBorrar = ImagesList.DELETE;
	private final int lineaBase = 100;											//Primera línea a partir de la cual se dibujan todos los elementos.
	private int contador = 0;													//Contador de elementos que se han adjuntado de un grupo.
	private JPanel panelCentral;
	private ControladorModulos cm;												//Controlador de módulos.
	private HashMap<String,JButton> mBtnAbrir;
	private HashMap<String,JButton> mBtnGuardarCambios;
	private HashMap<String,JButton> mBotones;
	private HashMap<String,JTextField> mapaFields;
	

	/**
	 * Create the panelCentral.
	 * @param cm Controlador datos de mapeados.
	 */
	public Archivos(ControladorModulos cm) {
		if(cm != null) this.cm = cm;
		mBtnAbrir = new HashMap<String,JButton>();
		mBtnGuardarCambios = new HashMap<String,JButton>();
		mBotones = new HashMap<String,JButton>();
		mapaFields = new HashMap<String,JTextField>();
		panelCentral = new JPanel();
		panelCentral.setOpaque(false);
		hi = wi =  20;
		configuracion();
	}
	
	@Override
	public void paint(Graphics g) {
		if(fondo != null) {
			imagen = new ImageIcon(getClass().getResource(fondo)).getImage();
			g.drawImage(imagen,0,0,getWidth(),getHeight(),this);
			setOpaque(false);
			super.paint(g);
		}
	}
	
	
	/* Métodos principales para añadir nuevos elementos y sus controles */
	
	/**
	 * Agrega los elementos JTextField al grupo mapaFields.
	 * Todo elemento que se desea agregar se añade aquí, basado en los elementos
	 * añadidos en este punto se generarán tantos los botones como los controles
	 * que sean necerios para el funcionamiento de este módulo.
	 * @see #generateControls
	 */
	private void createFieldsInMap() {
		// Generación de sus nombres e iconos particulares.
		//Las etiquetas son más generales que los botones => 
		//Formato (tipo, nombre, tooltip, icono).
		iniciarLabels(TypesFiles.PRJ,Labels_GUI.MDL, Labels_GUI.TT_L_PRJ, ImagesList.CLIPBOARD);
		iniciarLabels(TypesFiles.MAP,Labels_GUI.GRP,Labels_GUI.W_GRP_TITLE, ImagesList.FILES_ICON_MAP);
		iniciarLabels(TypesFiles.DEF,Labels_GUI.DEF,Labels_GUI.TT_L_DEF, ImagesList.FILES_ICON_DEF);
		iniciarLabels(TypesFiles.REL,Labels_GUI.REL,Labels_GUI.TT_L_REL, ImagesList.NODES);
		iniciarLabels(TypesFiles.PAL,Labels_GUI.PAL,Labels_GUI.TT_L_PAL, ImagesList.PAL);
		iniciarLabels(TypesFiles.HST,Labels_GUI.HST,Labels_GUI.TT_L_HST, ImagesList.PLAYER);
		contador = 0;															// Reiniciar el contardor de líneas a 0.
	}
	
	/**
	 * <p>Establece las facetas de las etiquetas descripticas</p> 
	 * @param tipo Tipo de archivos que controla. Ver \ref modelo#TypesFiles .
	 * @param nombre Nombre en la etiqueta.
	 * @param tt Tooltip mensaje emergente.
	 * @param ruta Ruta al icono de la etiqueta.
	 */
	private void iniciarLabels(String tipo, String nombre, String tt, String ruta) {
		JLabel jl = new JLabel(nombre);
		int posY = lineaBase + 30*contador;
		generateControls(nombre.toLowerCase(),tipo,0);
		contador++;
		int w = 120;
		int posX = 12;
		jl.setToolTipText(tt);
		addIconL(jl,ruta,wi,hi);
		jl.setBounds(posX, posY, w, hi);
		panelCentral.add(jl);
	}
	
	/**
	 * Genera todos los JTextFields necesarios para cubrir
	 * tantos tipos de módulos como se añadan al grupo de elementos.
	 *  Los tipos de elementos están almacenados inicialmente en el mapaFields interno.
	 * <p>Para crear los controles ver {@link #createFieldsInMap} </p>
	 * @param name Nombre del módulo sobre el que se realiza la acción.
	 * @param type Tipo de datos al que generar el control.
	 * @param posX Posición en las coordenadas X donde colocar el control.
	 * @see #createFieldsInMap()
	 */
	private void generateControls(String name, String type, int posX){
		JTextField jtf =  new JTextField();
		int posY = lineaBase + 30*contador;
		jtf.setEditable(false);
		jtf.setEnabled(true);
		jtf.setBounds(150,posY,300,hi);
		jtf.setColumns(10);
		jtf.setToolTipText(Labels_GUI.TT_FILE_SELECTED + " " + name);
		jtf.setHorizontalAlignment(SwingConstants.LEFT);
		panelCentral.add(jtf);
		mapaFields.put(type,jtf);
		
		int xpos = 480;
		int gap = 30;
		iniciarBoton(OperationsType.OPEN, name, type, iconAbrir,xpos,posY,true);
		iniciarBoton(OperationsType.SAVE_AS,name, type,iconGuardarComo,xpos + gap,posY,false);
		iniciarBoton(OperationsType.SAVE, name, type,iconGuardarCambios,xpos + 2*gap,posY,false);		
		iniciarBoton(OperationsType.EDIT, name, type,iconEditar,xpos + 3*gap,posY,false);
		//Para los módulos esenciales de cualquier proyecto, no incluir botones de borrado.
		if(!type.equals(TypesFiles.PRJ) && !type.equals(TypesFiles.DEF) && !type.equals(TypesFiles.MAP)) {
			iniciarBoton(OperationsType.DELETE, name, type,iconBorrar,xpos + 4*gap,posY,false);
		}
	}

	/**
	 * <p>Inicia un botón con los parámetros generales que se requiere
	 * para múltiples instancias que deben tener facetas comunes.</p> 
	 * La función devuelve la propia instancia del botón con el fin de facilitar
	 * el evitar perdidas de referencias.
	 * @param op Nombre de la acción.
	 * @param name Nombre del módulo al que se asocia el botón.
	 * @param type Tipo de archivos o módulo particular.
	 * @param ruta Ruta al archivo de imagen o icono que se adjunta en el botón.
	 * @param posX Posición en la coordenada X del panelCentral.
	 * @param posY Posición en la coordenada Y del panelCentral.
	 * @param activado Habilita o desabilita incialmente el botón, TRUE activado, False en otro caso.
	 */
	private void iniciarBoton(OperationsType op, String name, String type, String ruta, int posX, int posY, boolean activado) {
		//Creación y configuración del botón.
		JButton boton = new JButton();
		String tt = op.toString() + " " + name;
		boton.setActionCommand(op.toString());
		boton.addMouseListener(new ArchivoML(op, type));
		boton.setToolTipText(tt);
		addIconB(boton,ruta,wi,hi);
		boton.setBounds(posX, posY, 27, hi);
		boton.setEnabled(activado);
		boton.setBorder(null);
		boton.setBorderPainted(false);
		boton.setContentAreaFilled(false);
		//Guardado del botón en el hashmap.
		switch(op) {
		case OPEN:
			mBtnAbrir.put(type, boton);
			break;
		case SAVE:
			mBtnGuardarCambios.put(type, boton);
			break;
		case SAVE_AS:
		case EDIT:
		case DELETE:
			//Estos botones ya tienen configurado su Command, se aprovecha para añadir distincción.
			mBotones.put(boton.getActionCommand() + " " + type, boton);
			break;
		default:
			break;
		}
		//Añadirlo al panel
		panelCentral.add(boton);
	}

	
	/* Métodos públicos */
	
	
	/**
	 * @return El mapaFields donde se referencian todas las etiquetas.
	 */
	public HashMap<String, JTextField> getMapaFields() {return mapaFields;}
	
	/**
	 * Visualiza los datos del módulo dentro de su propio marco.
	 */
	public void abrirFrame() {
	    JFrame frame = new JFrame(Labels_GUI.W_FILES_TITLE);
	    Dimension m = getPreferredSize();
	    int x = (int)m.getWidth();
	    int y = (int)m.getHeight()+15;
	    frame.setPreferredSize(new Dimension(x, y));
	    frame.setMaximumSize(new Dimension(2767, 2767));
		frame.setMinimumSize(new Dimension(650, 400));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.getContentPane().add(this);
		frame.pack();
        frame.setVisible(true);
	}
	
	/**
	 * Desactiva todos los botones de guardado.
	 */
	public void disableAllSavers() {
		//Desactivar todos los botones de guardado.
		//Para evitar que se activen al cargar algún módulo del proyecto, se desactivan al final.
		mBtnGuardarCambios.forEach((tipo,elemento) -> {elemento.setEnabled(false);});
	}	
	
	/**
	 * Limpia los textos mostrados en cada etiqueta, sustituyéndolos
	 * por cadenas vacias y posteriormente realiza una lectura de los módulos
	 *  cargados en el sistema actualizando los campos correspondientes.
	 */
	public void reset() {
		//En esta vista, no se almacenan datos => actualización de componentes.
		//Principalmente: Campos y controles.
		refresh();
	}	
	
	/* Métodos privados */
	
	/**
	 * Configura los atributos generales de la vista, tales como
	 *  dimensiones, comportamiento, tipo de borde, imagen de fondo, etc.
	 *  También llama a las funciones encargada de crear los controles y su configuración.
	 */
	private void configuracion() {
		//Configuración del borde.
		tb = BorderFactory.createTitledBorder(Labels_GUI.W_MODULES_TITLE);
		tb.setTitleColor(Color.BLUE);
		panelCentral.setBorder(tb);
		//Configuración básica del panelCentral superior
		setToolTipText(Labels_GUI.TT_W_FILES);
		setSize(new Dimension(650, 400));
		setName(Labels_GUI.FILES_PANEL_NAME);
		setMinimumSize(new Dimension(650, 500));
		setMaximumSize(new Dimension(1024, 768));
		setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.GRAY));
		setAutoscrolls(true);
		setLayout(new BorderLayout(0, 0));	
		
		panelCentral.setToolTipText(Labels_GUI.TT_FILES_PANEL);
		panelCentral.setLayout(null);
		add(panelCentral, BorderLayout.CENTER);	

		//Muy importante iniciar este método antes de proseguir.
		createFieldsInMap();

		//Establecer el icono representación del módulo Archivos.
		JLabel labelLogo = new JLabel("");
		labelLogo.setIcon(IO.getIcon( ImagesList.BOOKS,70,75));
		labelLogo.setBounds(12, 12, 70, 75);
		panelCentral.add(labelLogo);
	}
			
	/**
	 * Añade un icono a una etiqueta
	 * <p>
	 * Los valores de dimensión de ancho y largo se establecen en función de los 
	 * datos pasados por parámetro.</p>
	 * @param componente Etiqueta a la que adjuntar el icono
	 * @param ruta Nombre del archivo y su fondo.
	 * @param w Ancho a escalar de la imagen original.
	 * @param h Alto a escalar de la imagen original.
	 */
	private void addIconL(JLabel componente, String ruta, int w, int h) {
		componente.setIconTextGap(5);
		componente.setIcon(IO.getIcon(ruta,w,h));	
	}	
	
	/**
	 * <p>Añade un icono a una botón</p>
	 * Los valores de dimensión de ancho y largo se establecen en función de los 
	 * datos pasados por parámetro. 
	 * @param componente Etiqueta a la que adjuntar el icono
	 * @param ruta Nombre del archivo y su fondo.
	 * @param w Ancho a escalar de la imagen original.
	 * @param h Alto a escalar de la imagen original.
	 */
	private void addIconB(JButton componente, String ruta, int w, int h) {
		componente.setIconTextGap(0);
		componente.setIcon(IO.getIcon(ruta,w,h));	
	}
		
	/**
	 * <p>Particularación de la función isEmpty de los componentes</p>
	 * La particularización estriba en que previamente comprueba que existe dicho
	 * campo mendiante la etiqueta (extensión) y obtiene el componente en caso 
	 *  de exitir de su campo.
	 * @param ext Extensión particular del campo.
	 * @return TRUE si dicho campo esta vacio o no esta contenido. FALSE en otro caso.
	 */
	private boolean isFieldEmpty(String ext) {
		boolean isEmpty = true;
		if(mapaFields.containsKey(ext)) {
			isEmpty =  mapaFields.get(ext).getText().isBlank();
		}
		return isEmpty;
	}
	
	/**
	 * <p>Configura los botones de guardado correspondientes
	 * a la etiqueta indicada.</p>
	 * Particularmente los botones de guardado rápido (Guardar cambios) no estarán
	 *  activados mientras no haya definida una fondo previamente en el correspondiente
	 *  campo.
	 * @param ext Etiqueta correspondiente con la extensión de tipo de archivos.
	 * o módulo al que hace referencia.
	 * @param activar TRUE si se desea habilitar, FALSE en otro caso.
	 */
	public void enableBotonesGuardado(String ext, boolean activar) {
		mBtnGuardarCambios.get(ext).setEnabled(activar && !isFieldEmpty(ext));
	}
	
	/**
	 * <p>Actualiza el estado de los botones Editar y Borrar de cada
	 *  tipo de fichero.</p> 
	 */
	private void refreshEditarBorrar() {
		mBotones.forEach((key,btn) -> {
			//Extraer su extensión.
			String ext = key.substring(key.length() -3,key.length());
			btn.setEnabled(cm.hasModule(ext));
		});
	}
	
	/**
	 * <p>Actualiza el estado de los campos de cada
	 *  tipo de fichero.</p> 
	 */
	private void refreshFields() {
		//Reasignación de cada campo de los módulos existentes en el Controlador.
		mapaFields.forEach((tipo,elemento) -> {
			//Sin nombre en principio.
			String nombre = null;
			boolean hasModule = cm.hasModule(tipo);
			boolean hasName = true;
			//Si existe el módulo, obtiene el nombre del mismo.
			if(hasModule) nombre = cm.getModule(tipo).getName();
			//En caso de que no tenga nombre definido mostrar texto al respecto.
			hasName = nombre != null;
			if(hasModule && !hasName) {
				nombre = Labels_GUI.PATH_NO_DEFINED;
				elemento.setBackground(Color.LIGHT_GRAY);
			}else if(hasModule && hasName) elemento.setBackground(elemento.getSelectionColor());
			else elemento.setBackground(elemento.getCaretColor());
			//Asigna el nombre al campo correspondiente.
			elemento.setText(nombre);
		});
	}
	
	/**
	 * <p>Actualiza el estado de todos los componentes de la vista </p>
	 * La actualización de los componentes se basa en los datos relacionados con
	 *  el controlador. No elimina datos. 
	 */
	public void refresh() {
		refreshFields();														//Primero actualizar los campos.
		refreshEditarBorrar();													//Actualizar estados de botones.
		updateUI();
	}
	
	/* Clases privadas */
	
	/**
	 * <p>Captura un evento de pulsación sobre un control (botón)
	 *  y procesa la acción correspondiente mediante la llamada a la clase controladora</p>
	 *  Para realizar dicha labor determina que botón ha sido pulsado mediante el análisis
	 *  del tooltiptext asociado al control, extrayendo su extensión asociada y el tipo de control
	 *   asociado al nombre, ext y op. Si el botón está habilitado procede con la operación. En otro 
	 *    caso ingnora la acción.
	 *  <p>Una vez realizada la acción procede a la actualización de los controles en función del
	 *   nuevo estado de la aplicación.</p>
	 * @author Silverio Manuel Rosales Santana
	 * @version versión 2.1
	 */
	private class ArchivoML extends MouseAdapter {
		private String tipo;
		private OperationsType op;
		
		/**
		 * Constructor de la clase que gestiona la acción del control al que se asocia.
		 * @param op Tipo de operación que gestiona el botón.
		 * @param type Tipo de módulo sobre el que se gestiona la acción.
		 */
		public ArchivoML(OperationsType op, String type) {
			this.tipo = type;
			this.op = op;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			boolean realizado = false;
			//Identificar botón.
			JButton btn = (JButton) e.getSource();
			if(btn.isEnabled()){	
				realizado = cm.doActionArchivos(op, tipo);
			}
					
			if(op == OperationsType.OPEN && realizado) enableBotonesGuardado(TypesFiles.PRJ, realizado);
			else if(realizado) enableBotonesGuardado(tipo,false);
			refresh();
		}
	}
	
}
