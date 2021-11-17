/**  
* <p>Title: Archivos.java</p>  
* <p>Description: </p>    
* <p>Aplication: UNED</p>  
* @author Silverio Manuel Rosales Santana
* @date 26 ago. 2021  
* @version 1.0  
*/  
package vista;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import controlador.ControladorDatosIO;
import controlador.ControladorModulos;
import modelo.DCVS;
import modelo.IO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
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
 * @version 1.0
 */
public class Archivos extends JPanel {

	/** serialVersionUID*/  
	private static final long serialVersionUID = 5320403899683788073L;
	private final int ELEMENTOS = 5;
	
	//Campos de texto que indicarán la ruta del fichero cargado.
	private JTextField TFMapa,TFProyecto,TFHistorico,TFLeyenda,TFRelaciones;
	private TitledBorder tb;
	// Etiquetas
	private JLabel lblMapa, lblProyecto,lblRelaciones, lblPaleta, lblHistorico;
	// Botones de abrir archivo
	private JButton btnAbrirPRJ, btnAbrirMapa, btnAbrirRelaciones, btnAbrirPaleta, btnAbrirHistorico;
	// Botones de guardar archivos como...
	private JButton btnGuardarPRJ, btnGuardarMapa, btnGuardarRelaciones, btnGuardarPaleta, btnGuardarHistorico;
	// Botonoes de guardar cambios.
	private JButton btnGCPRJ, btnGCMAP, btnGCREL, btnGCPAL, btnGCHST;
	//
	private int hi;																//Altura de los elementos (principalmente iconos).
	private int wi;																//Anchura de los iconos.
	private final String IAbrir = "/vista/imagenes/Iconos/carpeta_64px.png";
	private final String IGuardarA = "/vista/imagenes/Iconos/disquete_64px.png";
	private final String IGuardarC = "/vista/imagenes/Iconos/guardarCambios_64px.png";
	private int lineaBase = 100;												//Primera línea a partir de la cual se dibujan todos los elementos.
	private int contador = 0;													//Contador de elementos que se han adjuntado de un grupo.
//	private boolean modificado = false;
	private JPanel panelCentral;
	private ControladorDatosIO cIO;
	private ControladorModulos cMap;
	private HashMap<String,JButton> mBtnAbrir;
	private HashMap<String,JButton> mBtnGuardar;
	private HashMap<String,JButton> mBtnGuardarCambios;
	private HashMap<String,JTextField> mapaFields;
	private HashMap<String,DCVS> mapaModulos;
	
	
	/**
	 * Create the panelCentral.
	 * @param cMap Controlador datos de mapeados.
	 */
	public Archivos(ControladorModulos cMap) {
		this.cIO = new ControladorDatosIO();
		if(cMap != null) this.cMap = cMap;
		mBtnAbrir = new HashMap<String,JButton>();
		mBtnGuardar = new HashMap<String,JButton>();
		mBtnGuardarCambios = new HashMap<String,JButton>();
		mapaFields = new HashMap<String,JTextField>();
		mapaModulos = new HashMap<String,DCVS>();
		panelCentral = new JPanel();
		hi = wi =  20;
		configuracion();
	}
	
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
	 * @return El mapa de los módulos cargadoss
	 */
	public HashMap<String, DCVS> getMapaModulos() {	return mapaModulos;	}
	

	/**
	 * @param mapaModulos El mapa con los Modulos a establecer
	 */
	public void setMapaModulos(HashMap<String, DCVS> mapaModulos) {	this.mapaModulos = mapaModulos;	}
	
	/**
	 * <p>Title: getPanel</p>  
	 * <p>Description: Devuelve el panelCentral con su estado actual.</p> 
	 * @return Panel actual.
	 */
	public JPanel getPanel() {return this;}	

	private void configuracion() {
		//Configuración del borde.
		tb = BorderFactory.createTitledBorder("Ficheros");
		tb.setTitleColor(Color.BLUE);
		panelCentral.setBorder(tb);
		//Configuración básica del panelCentral superior
		setToolTipText("Visulación de los ficheros asignados a cada módulo.");
		setSize(new Dimension(650, 400));
		setName("Vista de módulos cargados");
		setMinimumSize(new Dimension(650, 400));
		setMaximumSize(new Dimension(1024, 768));
		setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.GRAY));
		setAutoscrolls(true);
		setLayout(new BorderLayout(0, 0));	
		
		panelCentral.setToolTipText("Selección archivos asignados a los módulos.");
		panelCentral.setLayout(null);
		add(panelCentral, BorderLayout.CENTER);
		
		iniciarLabels(lblProyecto,"Proyecto","/vista/imagenes/Iconos/portapapeles_64px.png");
		iniciarLabels(lblMapa,"Mapa","/vista/imagenes/Iconos/grupos_64px.png");
		iniciarLabels(lblRelaciones,"Relaciones","/vista/imagenes/Iconos/nodos_64px.png");
		iniciarLabels(lblPaleta,"Leyenda","/vista/imagenes/Iconos/circulo-de-color_64px.png");
		iniciarLabels(lblHistorico,"Histórico","/vista/imagenes/Iconos/animar_128px.png");		
		//
		TFProyecto = iniciarTextField(TFProyecto);
		TFMapa= iniciarTextField(TFMapa);
		TFRelaciones = iniciarTextField(TFRelaciones);
		TFLeyenda = iniciarTextField(TFLeyenda);
		TFHistorico = iniciarTextField(TFHistorico);	
		//
		int gap = 30;
		int x0 = 480;
		btnAbrirPRJ = iniciarBoton(btnAbrirPRJ,"","Abrir proyecto.PRJ",IAbrir,x0,true);
		btnAbrirMapa = iniciarBoton(btnAbrirMapa,"","Abrir mapa.MAP",IAbrir,x0,true);
		btnAbrirRelaciones = iniciarBoton(btnAbrirRelaciones,"","Abrir relaciones.REL",IAbrir,x0,true);
		btnAbrirPaleta = iniciarBoton(btnAbrirPaleta,"","Abrir paleta.PAL",IAbrir,x0,true);
		btnAbrirHistorico = iniciarBoton(btnAbrirHistorico,"","Abrir historico.HST",IAbrir,x0,true);
		//
		x0 = x0 + gap;
		btnGuardarPRJ = iniciarBoton(btnGuardarPRJ,"","Guardar como.PRJ",IGuardarA,x0,false);
		btnGuardarMapa = iniciarBoton(btnGuardarMapa,"","Guardar como.MAP",IGuardarA,x0,false);
		btnGuardarRelaciones = iniciarBoton(btnGuardarRelaciones,"","Guardar como.REL",IGuardarA,x0,false);
		btnGuardarPaleta = iniciarBoton(btnGuardarPaleta,"","Guardar como.PAL",IGuardarA,x0,false);
		btnGuardarHistorico = iniciarBoton(btnGuardarHistorico,"","Guardar como.HST",IGuardarA,x0,false);
		//
		x0 = x0 + gap;
		btnGCPRJ = iniciarBoton(btnGCPRJ,"","Guardar cambios.PRJ",IGuardarC,x0,false);
		btnGCMAP = iniciarBoton(btnGCMAP,"","Guardar cambios.MAP",IGuardarC,x0,false);
		btnGCREL = iniciarBoton(btnGCREL,"","Guardar cambios.REL",IGuardarC,x0,false);
		btnGCPAL = iniciarBoton(btnGCPAL,"","Guardar cambios.PAL",IGuardarC,x0,false);
		btnGCHST = iniciarBoton(btnGCHST,"","Guardar cambios.HST",IGuardarC,x0,false);
			
		inicializarMapas();
		
		JLabel labelLogo = new JLabel("");
		labelLogo.setIcon(IO.getIcon("/vista/imagenes/Iconos/archivo_64px.png",70,75));
		labelLogo.setBounds(12, 12, 70, 75);
		panelCentral.add(labelLogo);
	}
	
	/**
	 * <p>Title: abrirProyecto</p>  
	 * <p>Description: Carga un proyecto con sus ficheros en los módulos
	 * correspondientes.</p>
	 * @param dcvs Archivo de proyecto con los datos del resto de módulos.
	 */
	public void abrirProyecto(DCVS dcvs) {
		boolean traza = false;						 							/* Bandera de registro */
		int nm = dcvs.getRowCount();											//Número de datos (módulos) especificados.
		//NO hace falta comparar a nulo, pues esa comprobación ya está hecha desde el Action Listener.
		//Borrado etiquetas actuales.
		mapaFields.forEach((tipo,elemento) -> {	elemento.setText("");});
		//Borrado de todos los módulos.
		mapaModulos.clear();
		//Añadir ruta del proyecto a la etiqueta correspondiente.
		mapaFields.get(dcvs.getTipo()).setText(dcvs.getRuta());
		
		//Lectura de los módulos a cargar
		for(int i= 0;  i < nm; i++) {
			String[] m = dcvs.getFila(i);
			String dato = m[1];
			String etiq = m[0];
			//Si la etiqueta es de un módulo cargar el módulo correspondiente.
			if(!etiq.equals(IO.PRJ) && mapaFields.containsKey(etiq)) {			//Nos asegurarmos que no cargue por error un PRJ.
				DCVS mAux = cIO.abrirArchivo(dato,etiq);						//Carga el módulo desde el sistema de archivos.
				establecerDatos(mAux);											//Establecer el módulo.
				if(traza) System.out.println("\nArchivos - Abrir Proyecto: Modulo a cargar: " + etiq + " - " + dato);
			}else {
				//En otro caso es una etiqueta con información del proyecto.
//				establecerCampo(dcvs);
			}
		}
		
		//Desactivar todos los botones de guardado.
		//Para evitar que se activen al cargar algún módulo del proyecto, se desactivan al final.
		mBtnGuardar.forEach((tipo,elemento) -> {elemento.setEnabled(false);	});
		mBtnGuardarCambios.forEach((tipo,elemento) -> {elemento.setEnabled(false);});
		//Añadir este nuevo módulo al conjunto después de añadir el resto para evitar redundancias.
		mapaModulos.put(dcvs.getTipo(), dcvs);
	}
	
	/**
	 * <p>Title: getModulo</p>  
	 * <p>Description: Devuelve el módulo con la información que contenga. </p>
	 * El tipo de módulo es acorde a las extensiones aceptadas.
	 * @param tipo Tipo de módulo a devolver. Ej. MAP, HST, etc.
	 * @return El módulo solicitado.
	 */
	public DCVS getModulo(String tipo) {
		DCVS dcvs = null;
		if(mapaModulos.containsKey(tipo)) dcvs = mapaModulos.get(tipo);
		return dcvs;
	}
	
	
	/**
	 * <p>Title: guardarModulo</p>  
	 * <p>Description: Guardar los datos del módulo en el dispositivo indicado
	 * u establecido en el propio modelo</p> 
	 * @param dcvs Módulo DCVS a exportar a dispositivo de almacenamiento.
	 */
	private void guardarModulo(DCVS dcvs) {
		String tipo = dcvs.getTipo();
		//Desactivar sus botones.
		mBtnGuardar.get(tipo).setEnabled(false);
		mBtnGuardarCambios.get(tipo).setEnabled(false);
		//Guardar los datos en el disco.
		cIO.guardarArchivo(dcvs);												
	}
	
//	/* Esta función debe usarse para crear un módulo PRJ Básico para cuando no hay 
//	 * ninguno creado porque no se han cargado o no se ha generado.*/
//	private DCVS creaModuloProyecto() {
//		DCVS dcvs = new DCVS();
//		dcvs.nuevoModelo();
//		dcvs.setTipo(IO.PRJ);
//		dcvs.addCabecera(new Object[] {"Tipo","Ruta"});
//		//Bucle para añadir cada módulo al proyecto.
//		mapaModulos.forEach((tipo,modulo) -> {
//			if(tipo != IO.PRJ) {												//Evita introducir la ruta del propio archivo de proyecto.
//				dcvs.addFila(new Object[] {tipo, modulo.getRuta()});
//			}
//		});
//		dcvs.toString();
//		return dcvs;
//	}
//	
///* Trabajando aquí para que cree una lista con
// *  todos los tipos y rutas de módulos cargados ¿Para qué? quizás para facilitar
// *  una actualización, reinicio de datos o guardado de los datos. */
//	
//	/* Función no probada. */
//	private String[][] getRutasModulos(){
//		int size = mapaModulos.size();
//		if(mapaModulos.containsKey(IO.PRJ)) size--;								//Si contiene un módulo PRJ -> descontarlo.
//		String[][] lista = new String[size][2];
//		
//		mapaModulos.forEach((tipo,modulo) -> {
//			int fil = 0;
//			if(tipo != IO.PRJ) {												//Evita introducir la ruta del propio archivo de proyecto.
//				lista[fil][0] = tipo;
//				lista[fil][1] = modulo.getRuta();
//				fil++;
//			}
//		});
//		
//		return lista;
//	}
	
	/**
	 * <p>Title: guardarProyecto</p>  
	 * <p>Description: Guarda la configuración del proyecto</p> En caso de que
	 * el módulo recibido sea null, se crea un módulo nuevo con los valores de
	 * ruta de los demás módulos.
	 * @param dcvsIn Módulo DCVS de entrada con configuración PRJ o null si hay
	 * que crear uno nuevo
	 */
	private void guardarProyecto(DCVS dcvsIn) {
		DCVS dcvs = dcvsIn;
		//Si se llama con null a la función crear nuevo módulo proyecto.	
//		if(dcvs == null) dcvs = creaModuloProyecto();
		
		//Guardado del fichero:
		String ruta = cIO.guardarArchivo(dcvs);
		if(dcvs != null && ruta != null) {
			//Configuración de la ruta
			dcvs.setRuta(ruta);
			//Mostrar ruta en Field.
			mapaFields.get(IO.PRJ).setText(ruta);
			mapaModulos.put(IO.PRJ, dcvs);
			//Desactivación del botón de guardado de proyecto.
			mBtnGuardar.get(IO.PRJ).setEnabled(false);
			mBtnGuardarCambios.get(IO.PRJ).setEnabled(false);
		}

	}
	
	/**
	 * <p>Title: establecerDatos</p>  
	 * <p>Description: Establece el contenido del módulo cargado de acuerdo
	 * con su tipo, actualiza los elementos del JPanel correspondientes</p> 
	 * @param datos Conjunto de datos y cabecera encapsulados.
	 */
	public void establecerDatos(DCVS datos) {
		String tipo = datos.getTipo();
		//Actualizar etiqueta correspondiente con la ruta del archivo.
		if(mapaFields.containsKey(tipo)) {
			mapaFields.get(tipo).setText(datos.getRuta());
		}
		
		//Añadir nuevo módulo al módulo del proyecto.
		//Los módulos PRJ están filtrados desde abrirProyecto y el ActionListener.
		if(mapaModulos.containsKey(IO.PRJ)) {
			String[] nuevaEntrada = {tipo,datos.getRuta()};
			int linea = mapaModulos.get(IO.PRJ).getFilaItem(tipo);
			boolean lineaDuplicada = linea > -1;
			//Si hay un módulo ya cargado, hay que sustituirlo.
			if(lineaDuplicada) {mapaModulos.get(IO.PRJ).delFila(linea);}		//Eliminar entrada duplicada.
			mapaModulos.get(IO.PRJ).addFila(nuevaEntrada);						//Añadir nueva entrada.
		}
		
		//Guardar los datos del módulo en su conjunto.
		mapaModulos.put(tipo, datos);

		//Operaciones extras según tipo de módulo.
		switch(tipo) {
			case (IO.MAP):
				cMap.setPoligonos(datos);
				break;
			case (IO.HST):
				cMap.setHistorico(datos);
				break;
			default:
				mBtnGuardar.get(IO.PRJ).setEnabled(true);
				mBtnGuardarCambios.get(IO.PRJ).setEnabled(true);
		}
	}
	
	/* Métodos privados */
	
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
	 * @param jl Etiqueta a tratar.
	 * @param nombre Nombre en la etiqueta.
	 * @param ruta Ruta al icono de la etiqueta.
	 */
	private void iniciarLabels(JLabel jl, String nombre, String ruta) {
		if(contador == ELEMENTOS) contador = 0;
		int posY = lineaBase + 30*contador;
		contador++;
		int w = 105;
		int posX = 12;
		jl = new JLabel(nombre);
		addIconL(jl,ruta,wi,hi);
		jl.setBounds(posX, posY, w, hi);
		panelCentral.add(jl);
	}
	
	
	private JTextField iniciarTextField(JTextField jtf){
		if(contador == ELEMENTOS) contador = 0;
		int posY = lineaBase + 30*contador;
		contador++;
		jtf = new JTextField();
		jtf.setEditable(false);
		jtf.setEnabled(true);
		jtf.setBounds(129,posY,338,hi);
		jtf.setColumns(10);
		jtf.setToolTipText("Archivo seleccionado");
		jtf.setHorizontalAlignment(SwingConstants.LEFT);
		panelCentral.add(jtf);
		return jtf;
	}
	
	
	/**
	 * <p>Title: iniciarBoton</p>  
	 * <p>Description: Inicia un botón con los parámetros generales que se requiere
	 * para múltiples instancias que deben tener facetas comunes.</p> 
	 * La función devuelve la propia instancia del botón con el fin de facilitar
	 * el evitar perdidas de referencias.
	 * @param boton Botón al que configurar las propiedades.
	 * @param nombre Nombre del botón.
	 * @param tt ToolTipText, texto añadido para cuando se pasa el ratón por encima.
	 * @param ruta Ruta al archivo de imagen o icono que se adjunta en el botón.
	 * @param posX Posición en la coordenada X del panelCentral.
	 * @param activado Habilita o desabilita incialmente el botón, TRUE activado, False en otro caso.
	 * @return la propia instancia del botón.
	 */
	private JButton iniciarBoton(JButton boton, String nombre,String tt,  String ruta, int posX, boolean activado) {
		if(contador == ELEMENTOS) contador = 0;
		int posY = lineaBase + 30*contador;
		contador++;
		boton = new JButton(nombre);
		boton.addMouseListener(new ArchivoML());
		boton.setToolTipText(tt);
		addIconB(boton,ruta,wi,hi);
		boton.setBounds(posX, posY, 27, hi);
		boton.setEnabled(activado);
		boton.setBorder(null);
		panelCentral.add(boton);
		return boton;
	}

	
	/**
	 * <p>Title: inicializarMapas</p>  
	 * <p>Description: Inicializa los mapas de los elementos característicos</p>
	 */
	private void inicializarMapas() {
		mBtnAbrir.put(IO.PRJ, btnAbrirPRJ);
		mBtnAbrir.put(IO.MAP, btnAbrirMapa);
		mBtnAbrir.put(IO.REL, btnAbrirRelaciones);
		mBtnAbrir.put(IO.PAL, btnAbrirPaleta);
		mBtnAbrir.put(IO.HST, btnAbrirHistorico);
		
		mBtnGuardar.put(IO.PRJ, btnGuardarPRJ);
		mBtnGuardar.put(IO.MAP, btnGuardarMapa);
		mBtnGuardar.put(IO.REL, btnGuardarRelaciones);
		mBtnGuardar.put(IO.PAL, btnGuardarPaleta);
		mBtnGuardar.put(IO.HST, btnGuardarHistorico);
		
		mBtnGuardarCambios.put(IO.PRJ, btnGCPRJ);
		mBtnGuardarCambios.put(IO.MAP, btnGCMAP);
		mBtnGuardarCambios.put(IO.REL, btnGCREL);
		mBtnGuardarCambios.put(IO.PAL, btnGCPAL);
		mBtnGuardarCambios.put(IO.HST, btnGCHST);
		
		mapaFields.put(IO.PRJ, TFProyecto);
		mapaFields.put(IO.MAP, TFMapa);
		mapaFields.put(IO.REL, TFRelaciones);
		mapaFields.put(IO.PAL, TFLeyenda);
		mapaFields.put(IO.HST, TFHistorico);
	}
		
	
	/* Clases privadas */
	
	private class ArchivoML extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			DCVS dcvs = null;			
			JButton btn = (JButton) e.getSource();								//Identificar botón.
			String tt = btn.getToolTipText();
			//Los valores de tipos van en minúscula.
			String ext = tt.substring(tt.length() -3, tt.length()).toLowerCase();
			//Identificador de operación (abrir o guardar).
			String op = tt.split(" ")[0];
			String aux = tt.split(" ")[1];
			int size = aux.length() -4;
			String op2 = aux.substring(0, size);								//Identificar guardado rápido o guardar como.
			
			//Opciones de Carga de módulo, NO módulo PRJ.
			if(op.equals("Abrir") && !ext.equals(IO.PRJ)) {
				dcvs = cIO.abrirArchivo(null,ext);
				if(dcvs != null) {
					establecerDatos(dcvs);
				}
			//Opciones de carga de módulo PRJ
			}else if(op.equals("Abrir") && ext.equals(IO.PRJ)) {
				dcvs = cIO.abrirArchivo(null,ext);
				if(dcvs != null) {
					abrirProyecto(dcvs);
				}
			//Opciones de Guarga de módulo.
			}else if(btn.isEnabled()){											//Comprobación de guardado activado.		
				//Optención del módulo correspondiente
				if(mapaModulos.containsKey(ext)) {
					dcvs = mapaModulos.get(ext);
					//Si es guardar como, poner ruta a null. En otro caso guardará con la ruta que contiene.
					if(op2.equals("como")) { dcvs.setRuta(null); }
				}
				//
				if(ext.equals(IO.PRJ)) guardarProyecto(dcvs);
				else guardarModulo(dcvs);
			}
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
