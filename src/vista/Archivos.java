/**  
* <p>Title: Archivos.java</p>  
* <p>Description: </p>    
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
import modelo.IO;
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
 * <p>Title: Archivos</p>  
 * <p>Description: Vista de los archivos asociados a cada módulo del proyecto</p>  
 * @author Silverio Manuel Rosales Santana
 * @date 26 ago. 2021
 * @version 2.5
 */
public class Archivos extends JPanel {

	/** serialVersionUID*/  
	private static final long serialVersionUID = 5320403899683788073L;	
	private TitledBorder tb;
	private Image imagen;
	private String ruta = "/vista/imagenes/lineasAzules.png";
	//
	private final int hi;														//Altura de los elementos (principalmente iconos).
	private final int wi;														//Anchura de los iconos.
	private final String iconAbrir = "/vista/imagenes/Iconos/carpeta_64px.png";
	private final String iconGuardarComo = "/vista/imagenes/Iconos/disquete_64px.png";
	private final String iconGuardarCambios = "/vista/imagenes/Iconos/guardarCambios_64px.png";
	private final String iconEditar = "/vista/imagenes/Iconos/editarTabla_64px.png";
	private final String iconBorrar = "/vista/imagenes/Iconos/borrar_64px.png";
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
		if(ruta != null) {
			imagen = new ImageIcon(getClass().getResource(ruta)).getImage();
			g.drawImage(imagen,0,0,getWidth(),getHeight(),this);
			setOpaque(false);
			super.paint(g);
		}
	}
	
	
	/* Métodos principales para añadir nuevos elementos y sus controles */
	
	/**
	 * <p>Title: createFieldsInMap</p>  
	 * <p>Description: Agrega los elementos JTextField al grupo mapaFields</p>
	 * Todo elemento que se desea agregar se añade aquí, basado en los elementos
	 * añadidos en este punto se generarán tantos los botones como los controles
	 * que sean necerios para el funcionamiento de este módulo.
	 * <p>Para crear los controles ver {@link #generateControls(String ext, int posX)} .</p>
	 * @see #generateControls(String ext, int posX)
	 */
	private void createFieldsInMap() {
		// Generación de sus nombres e iconos particulares.
		iniciarLabels(TypesFiles.PRJ,"Modelo","Archivo con los parámetros del modelo.","/vista/imagenes/Iconos/portapapeles_64px.png");
		iniciarLabels(TypesFiles.MAP,"Grupos","Grupos de población.","/vista/imagenes/Iconos/spain_128px.png");
		iniciarLabels(TypesFiles.DEF,"Enfermedad","Archivo con la definición propia de la enfermedad.","/vista/imagenes/Iconos/adn_64px.png");
		iniciarLabels(TypesFiles.REL,"Relaciones","Matriz de contactos. Refleja las relaciones entre los grupos","/vista/imagenes/Iconos/nodos_64px.png");
		iniciarLabels(TypesFiles.PAL,"Paleta","Paleta de colores personalizada.","/vista/imagenes/Iconos/circulo-de-color_64px.png");
		iniciarLabels(TypesFiles.HST,"Histórico","Histórico de evolución con los datos de la simulación.","/vista/imagenes/Iconos/animar_128px.png");
		contador = 0;															// Reiniciar el contardor de líneas a 0.
	}

	
	/* Métodos públicos */
	
	
	/**
	 * @return El mapaFields donde se referencian todas las etiquetas.
	 */
	public HashMap<String, JTextField> getMapaFields() {return mapaFields;}
	
	/**
	 * <p>Title: abrirFrame</p>  
	 * <p>Description: Visualiza los datos del módulo dentro de su propio marco</p> 
	 */
	public void abrirFrame() {
	    JFrame frame = new JFrame("Módulo de Archivos");
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
	 * <p>Title: setLabel</p>  
	 * <p>Description: Establece el texto al campo que se le indique.</p> 
	 * @param label Etiqueta o campo a escribir.
	 * @param text Texto a escribir en el campo.
	 */
	public void setLabel(String label, String text) {
		if(mapaFields.containsKey(label)) {
			mapaFields.get(label).setText(text);
		}
	}
	
	/**
	 * <p>Title: disableAllSavers</p>  
	 * <p>Description: Desactiva todos los botones de guardado</p> 
	 */
	public void disableAllSavers() {
		//Desactivar todos los botones de guardado.
		//Para evitar que se activen al cargar algún módulo del proyecto, se desactivan al final.
		mBtnGuardarCambios.forEach((tipo,elemento) -> {elemento.setEnabled(false);});
	}	
	
	/**
	 * <p>Title: reset</p>  
	 * <p>Description: Limpia los textos mostrados en cada etiqueta, sustituyéndolos
	 * por cadenas vacias y posteriormente realiza una lectura de los módulos
	 *  cargados en el sistema actualizando los campos correspondientes.</p> 
	 */
	public void reset() {
		//En esta vista, no se almacenan datos => actualización de componentes.
		//Principalmente: Campos y controles.
		refresh();
	}	
	
	/* Métodos privados */
	
	private void configuracion() {
		//Configuración del borde.
		tb = BorderFactory.createTitledBorder("Ficheros");
		tb.setTitleColor(Color.BLUE);
		panelCentral.setBorder(tb);
		//Configuración básica del panelCentral superior
		setToolTipText("Visulación de los ficheros asignados a cada módulo.");
		setSize(new Dimension(650, 400));
		setName("Vista de módulos cargados");
		setMinimumSize(new Dimension(650, 500));
		setMaximumSize(new Dimension(1024, 768));
		setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.GRAY));
		setAutoscrolls(true);
		setLayout(new BorderLayout(0, 0));	
		
		panelCentral.setToolTipText("Selección archivos asignados a los módulos.");
		panelCentral.setLayout(null);
		add(panelCentral, BorderLayout.CENTER);	

		//Muy importante iniciar este método antes de proseguir.
		createFieldsInMap();

		//Establecer el icono representación del módulo Archivos.
		JLabel labelLogo = new JLabel("");
		labelLogo.setIcon(IO.getIcon("/vista/imagenes/Iconos/archivo_64px.png",70,75));
		labelLogo.setBounds(12, 12, 70, 75);
		panelCentral.add(labelLogo);
	}
			
	/**
	 * <p>Title: addIconL</p>  
	 * <p>Description: Añade un icono a una etiqueta</p>
	 * Los valores de dimensión de ancho y largo se establecen en función de los 
	 * datos pasados por parámetro. 
	 * @param componente Etiqueta a la que adjuntar el icono
	 * @param ruta Nombre del archivo y su ruta.
	 * @param w Ancho a escalar de la imagen original.
	 * @param h Alto a escalar de la imagen original.
	 */
	private void addIconL(JLabel componente, String ruta, int w, int h) {
		componente.setIconTextGap(5);
		componente.setIcon(IO.getIcon(ruta,w,h));	
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
		componente.setIcon(IO.getIcon(ruta,w,h));	
	}
		
	/**
	 * <p>Title: iniciarLabels</p>  
	 * <p>Description: Establece las facetas de las etiquetas descripticas</p> 
	 * @param ext Tipo de archivos que controla. Ver {@link TypesFiles Tipos de archivo}.
	 * @param nombre Nombre en la etiqueta.
	 * @param tt Tooltip mensaje emergente.
	 * @param ruta Ruta al icono de la etiqueta.
	 */
	private void iniciarLabels(String ext, String nombre, String tt, String ruta) {
		JLabel jl = new JLabel(nombre);
		int posY = lineaBase + 30*contador;
		generateControls(ext,0);
		contador++;
		int w = 110;
		int posX = 12;
		jl.setToolTipText(tt);
		addIconL(jl,ruta,wi,hi);
		jl.setBounds(posX, posY, w, hi);
		panelCentral.add(jl);
	}
		

	/**
	 * <p>Title: iniciarBoton</p>  
	 * <p>Description: Inicia un botón con los parámetros generales que se requiere
	 * para múltiples instancias que deben tener facetas comunes.</p> 
	 * La función devuelve la propia instancia del botón con el fin de facilitar
	 * el evitar perdidas de referencias.
	 * @param nombre Nombre del botón.
	 * @param ext Tipo de archivos o módulo particular.
	 * @param ruta Ruta al archivo de imagen o icono que se adjunta en el botón.
	 * @param posX Posición en la coordenada X del panelCentral.
	 * @param posY Posición en la coordenada Y del panelCentral.
	 * @param activado Habilita o desabilita incialmente el botón, TRUE activado, False en otro caso.
	 */
	private void iniciarBoton(String nombre, String ext, String ruta, int posX, int posY, boolean activado) {
		JButton boton = new JButton();
		String tt = nombre + " .";
		boton.setActionCommand(nombre);
		boton.addMouseListener(new ArchivoML());
		boton.setToolTipText(tt + ext);
		addIconB(boton,ruta,wi,hi);
		boton.setBounds(posX, posY, 27, hi);
		boton.setEnabled(activado);
		boton.setBorder(null);
		boton.setBorderPainted(false);
		boton.setContentAreaFilled(false);
		addBotonHM(boton,ext);
		panelCentral.add(boton);
	}
	
	/**
	 * <p>Title: generateControls</p>  
	 * <p>Description: Genera todos los JTextFields necesarios para cubrir
	 * tantos tipos de módulos como se añadan al grupo de elementos.</p>
	 * Los tipos de elementos están almacenados inicialmente en el mapaFields interno.
	 * <p>Para crear los controles ver {@link #createFieldsInMap} </p>
	 * @param ext Tipo de datos al que generar el control.
	 * @param posX Posición en las coordenadas X donde colocar el control.
	 * @see #createFieldsInMap()
	 */
	private void generateControls(String ext, int posX){
		JTextField jtf =  new JTextField();
		int posY = lineaBase + 30*contador;
		jtf.setEditable(false);
		jtf.setEnabled(true);
		jtf.setBounds(140,posY,300,hi);
		jtf.setColumns(10);
		jtf.setToolTipText("Archivo seleccionado");
		jtf.setHorizontalAlignment(SwingConstants.LEFT);
		panelCentral.add(jtf);
		mapaFields.put(ext,jtf);
		
		int xpos = 480;
		int gap = 30;
		iniciarBoton("Abrir",ext, iconAbrir,xpos,posY,true);
		iniciarBoton("Guardar como",ext,iconGuardarComo,xpos + gap,posY,false);
		iniciarBoton("Guardar cambios",ext,iconGuardarCambios,xpos + 2*gap,posY,false);		
		iniciarBoton("Editar",ext,iconEditar,xpos + 3*gap,posY,false);
		iniciarBoton("Borrar",ext,iconBorrar,xpos + 4*gap,posY,false);
	}
		
	/**
	 * <p>Title: inicializarMapas</p>  
	 * <p>Description: Inicializa los mapas de los elementos característicos</p>
	 * @param btn JButton a introducir en el grupo correspondiente.
	 * @param ext Tipo de archivos a los que hace referencia dicho boton.
	 */
	private void addBotonHM(JButton btn, String ext) {
		String tipo = btn.getActionCommand();
		switch(tipo) {
		case ("Abrir"):
			mBtnAbrir.put(ext, btn);
			break;
		case ("Guardar cambios"):
			mBtnGuardarCambios.put(ext, btn);
			break;
		case ("Guardar como"):
		case ("Editar"):
		case ("Borrar"):
			//Estos botones ya tienen configurado su Command, se aprovecha para añadir distincción.
			mBotones.put(btn.getActionCommand() + " " + ext, btn);
			break;
		}	
	}
	
	/**
	 * <p>Title: isFieldEmpty</p>  
	 * <p>Description: Particularación de la función isEmpty de los componentes</p>
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
	 * <p>Title: enableBotonesGuardado</p>  
	 * <p>Description: Configura los botones de guardado correspondientes
	 * a la etiqueta indicada.</p>
	 * Particularmente los botones de guardado rápido (Guardar cambios) no estarán
	 *  activados mientras no haya definida una ruta previamente en el correspondiente
	 *  campo.
	 * @param ext Etiqueta correspondiente con la extensión de tipo de archivos 
	 * o módulo al que hace referencia.
	 * @param activar TRUE si se desea habilitar, FALSE en otro caso.
	 */
	public void enableBotonesGuardado(String ext, boolean activar) {
		mBtnGuardarCambios.get(ext).setEnabled(activar && !isFieldEmpty(ext));
	}
	
	/**
	 * <p>Title: refreshEditarBorrar</p>  
	 * <p>Description: Actualiza el estado de los botones Editar y Borrar de cada
	 *  tipo de fichero.</p> 
	 */
	private void refreshEditarBorrar() {
		mBotones.forEach((key,btn) -> {
			//Extrar su extensión.
			String ext = key.substring(key.length() -3,key.length());
			btn.setEnabled(cm.hasModule(ext));
//			System.out.println("Archivos > refreshEditarBorar: " + ext + " = " + cm.hasModulo(ext));
		});
	}
	
	/**
	 * <p>Title: refreshFields</p>  
	 * <p>Description: Actualiza el estado de los campos de cada
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
			if(hasModule) nombre = cm.getModule(tipo).getNombre();
			//En caso de que no tenga nombre definido mostrar texto al respecto.
			hasName = nombre != null;
			if(hasModule && !hasName) {
				nombre = "Datos sin destino definido.";
				elemento.setBackground(Color.LIGHT_GRAY);
			}else if(hasModule && hasName) elemento.setBackground(elemento.getSelectionColor());
			else elemento.setBackground(elemento.getCaretColor());
			//Asigna el nombre al campo correspondiente.
			elemento.setText(nombre);
			
		});
	}
	
	/**
	 * <p>Title: refresh</p>  
	 * <p>Description: Actualiza el estado de todos los componentes de la vista </p>
	 * La actualización de los componentes se basa en los datos relacionados con
	 *  el controlador. No elimina datos. 
	 */
	public void refresh() {
		refreshFields();														//Primero actualizar los campos.
		refreshEditarBorrar();													//Actualizar estados de botones.
		updateUI();
	}
	
	/* Clases privadas */
	
	private class ArchivoML extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			boolean realizado = false;
			JButton btn = (JButton) e.getSource();								//Identificar botón.
			String tt = btn.getToolTipText();
			//Los valores de tipos van en minúscula.
			String ext = tt.substring(tt.length() -3, tt.length()).toLowerCase();
			//Identificador de operación (abrir o guardar).
			String op = btn.getActionCommand();		
			//Opciones de Carga de módulo, NO módulo PRJ.
			if(op.equals("Abrir") ) {realizado = cm.doActionArchivos(op, ext);}
			//Opciones de Guarga de módulo.
			else if(btn.isEnabled()){											//Comprobación de guardado activado.		
				//Optención del módulo correspondiente
				realizado = cm.doActionArchivos(op, ext);
				//Desactivar sus botones.
				if(realizado) enableBotonesGuardado(ext,false);
			}
			
			refresh();
			if(op.equals("Abrir") && realizado) enableBotonesGuardado(TypesFiles.PRJ, true);
		}
	}
	
	
	/**
	 * <p>Title: main</p>  
	 * <p>Description: Función a efecto de pruebas</p> 
	 * @param args Nada.
	 */
	public static void main(String[] args) {
		Archivos archivos = new Archivos(new ControladorModulos());
		archivos.abrirFrame();	
	}
	
}
