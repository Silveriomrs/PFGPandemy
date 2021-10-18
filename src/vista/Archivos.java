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
import controlador.ControladorMapa;
import modelo.DCVS;
import modelo.IO;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.ImageIcon;

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
	//Campos de texto que indicarán la ruta del fichero cargado.
	private JTextField textFieldMapa,txtFieldProyecto,textFieldHistorico,textFieldLeyenda,textFieldRelaciones;
	private TitledBorder tb;
	// Etiquetas
	private JLabel lblMapa, lblProyecto,lblRelaciones, lblPaleta, lblHistorico;
	// Botones de abrir archivo
	private JButton btnAbrirPRJ, btnAbrirMapa, btnAbrirRelaciones, btnAbrirPaleta, btnAbrirHistorico;
	// Botones de guardar archivos/cambio.
	private JButton btnGuardarPRJ, btnGuardarMapa, btnGuardarRelaciones, btnGuardarPaleta, btnGuardarHistorico;
	//
	private int hi;																//Altura de los elementos (principalmente iconos).
	private int wi;																//Anchura de los iconos.
	private final String IAbrir = "/vista/imagenes/Iconos/carpeta_64px.png";
	private final String IGuardarA = "/vista/imagenes/Iconos/disquete_64px.png";
	private int lineaBase = 100;												//Primera línea a partir de la cual se dibujan todos los elementos.
	private int contador = 0;													//Contador de elementos que se han adjuntado de un grupo.
	private JPanel panel;
	private ControladorDatosIO cIO;
	private ControladorMapa cMap;
//	private final FondoPanel fondo = new FondoPanel("/vista/imagenes/histograma2.png");
	private HashMap<String,JButton> mapaBotonesAbrir;
	private HashMap<String,JButton> mapaBotonesGuardar;
	private HashMap<String,JTextField> mapaFields;
	private HashMap<String,DCVS> mapaModulos;
	
	
	/**
	 * Create the panel.
	 * @param cIO Controlador de Entrada y salidas de datos.
	 * @param cMap Controlador datos de mapeados.
	 */
	public Archivos(ControladorDatosIO cIO, ControladorMapa cMap) {
		if(cIO != null) this.cIO = cIO;
		if(cMap != null) this.cMap = cMap;
		mapaBotonesAbrir = new HashMap<String,JButton>();
		mapaBotonesGuardar = new HashMap<String,JButton>();
		mapaFields = new HashMap<String,JTextField>();
		mapaModulos = new HashMap<String,DCVS>();
		panel = new JPanel();
		
		hi = wi =  20;
		configuracion();
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
	 * <p>Description: Devuelve el panel con su estado actual.</p> 
	 * @return Panel actual.
	 */
	public JPanel getPanel() { return this;}
	

	private void configuracion() {
		//Configuración del borde.
		tb = BorderFactory.createTitledBorder("Ficheros");
		tb.setTitleColor(Color.BLUE);
		panel.setBorder(tb);
		//Configuración básica del panel superior
		setToolTipText("Visulación de los ficheros asignados a cada módulo.");
		setSize(new Dimension(665, 456));
		setName("Vista de módulos cargados");
		setMinimumSize(new Dimension(500, 300));
		setMaximumSize(new Dimension(665, 456));
		setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.GRAY));
		setAutoscrolls(true);
		setLayout(new BorderLayout(0, 0));	
		
		panel.setToolTipText("Selección archivos asignados a los módulos.");
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		iniciarLabels(lblProyecto,"Proyecto","/vista/imagenes/Iconos/portapapeles_64px.png");
		iniciarLabels(lblMapa,"Mapa","/vista/imagenes/Iconos/region_64px.png");
		iniciarLabels(lblRelaciones,"Relaciones","/vista/imagenes/Iconos/nodos_64px.png");
		iniciarLabels(lblPaleta,"Leyenda","/vista/imagenes/Iconos/circulo-de-color_64px.png");
		iniciarLabels(lblHistorico,"Histórico","/vista/imagenes/Iconos/animar_128px.png");		
		//
		txtFieldProyecto = iniciarTextField(txtFieldProyecto);
		textFieldMapa= iniciarTextField(textFieldMapa);
		textFieldRelaciones = iniciarTextField(textFieldRelaciones);
		textFieldLeyenda = iniciarTextField(textFieldLeyenda);
		textFieldHistorico = iniciarTextField(textFieldHistorico);
		//
		btnAbrirPRJ = iniciarBoton(btnAbrirPRJ,"","Abrir PRJ",IAbrir,480,true);
		btnAbrirMapa = iniciarBoton(btnAbrirMapa,"","Abrir MAP",IAbrir,480,true);
		btnAbrirRelaciones = iniciarBoton(btnAbrirRelaciones,"","Abrir REL",IAbrir,480,true);
		btnAbrirPaleta = iniciarBoton(btnAbrirPaleta,"","Abrir PAL",IAbrir,480,true);
		btnAbrirHistorico = iniciarBoton(btnAbrirHistorico,"","Abrir HST",IAbrir,480,true);
		//
		btnGuardarPRJ = iniciarBoton(btnGuardarPRJ,"","Guardar PRJ",IGuardarA,520,false);	
		btnGuardarMapa = iniciarBoton(btnGuardarMapa,"","Guardar MAP",IGuardarA,520,false);
		btnGuardarRelaciones = iniciarBoton(btnGuardarRelaciones,"","Guardar REL",IGuardarA,520,false);
		btnGuardarPaleta = iniciarBoton(btnGuardarPaleta,"","Guardar PAL",IGuardarA,520,false);
		btnGuardarHistorico = iniciarBoton(btnGuardarHistorico,"","Guardar HST",IGuardarA,520,false);
	
		inicializarMapas();
		
		JLabel labelLogo = new JLabel("");
		labelLogo.setIcon(new ImageIcon(Archivos.class.getResource("/vista/imagenes/Iconos/archivo_64px.png")));
		labelLogo.setBounds(12, 12, 70, 75);
		panel.add(labelLogo);
	}
	
	/**
	 * <p>Title: abrirProyecto</p>  
	 * <p>Description: Carga un proyecto con sus ficheros en los módulos
	 * correspondientes.</p>
	 * @param mp Archivo de proyecto con los datos del resto de módulos.
	 */
	private void abrirProyecto(DCVS mp) {
		DCVS mPrj = null;
		if(mp != null) mPrj = mp;
		else mPrj = mapaModulos.get(IO.PRJ);
		int nm = mPrj.getRowCount();	
		
		for(int i= 0;  i < nm; i++) {
			String[] m = mPrj.getFila(i);
			String ruta = m[1];
			String tipo = m[0];
			if(!tipo.equals(IO.PRJ)) {											//Nos asegurarmos que no cargue por error un PRJ.
				DCVS mAux = cIO.abrirArchivo(ruta,tipo);						//Carga el módulo desde el sistema de archivos.
				establecerDatos(mAux);											//Establecer el módulo.
			}
		}
		
		mapaBotonesGuardar.get(IO.PRJ).setEnabled(false);
	}
	
	
	private void guardarModulo(DCVS dcvs) {
		String tipo = dcvs.getTipo();
		mapaBotonesGuardar.get(tipo).setEnabled(false);							//Desactivar su botón.
		cIO.guardarArchivo(dcvs);												//Guardar los datos en el disco.
	}
	
	/**
	 * <p>Title: guardarProyecto</p>  
	 * <p>Description: Guarda la configuración del proyecto</p>
	 */
	private void guardarProyecto() {	
		DCVS dcvs = new DCVS();
		dcvs.nuevoModelo();
		dcvs.setTipo(IO.PRJ);
		dcvs.addCabecera(new Object[] {"Tipo","Ruta"});
		//Bucle para cada módulo
		mapaModulos.forEach((tipo,modulo) -> {
			if(tipo != IO.PRJ) {												//Evita introducir la ruta del propio archivo de proyecto.
				dcvs.addFila(new Object[] {tipo, modulo.getRuta()});			
//				guardarModulo(tipo);											//Guardar cada módulo.
//				mapaBotonesGuardar.get(tipo).setEnabled(false);
			}
		});
		
		//Desactivación del botón de guardado de proyecto.
		mapaBotonesGuardar.get(IO.PRJ).setEnabled(false);
		//Guardado del fichero:
		String ruta = cIO.guardarArchivo(dcvs);
		if(ruta != null) {
			//Configuración de la ruta
			dcvs.setRuta(ruta);
			//Mostrar ruta en Field.
			mapaFields.get(IO.PRJ).setText(ruta);
			mapaModulos.put(IO.PRJ, dcvs);
		}

	}
	
	/**
	 * <p>Title: establecerDatos</p>  
	 * <p>Description: Establece el contenido del módulo cargado de acuerdo
	 * con su tipo, actualiza los elementos del JPanel correspondientes</p> 
	 * @param datos Conjunto de datos y cabecera encapsulados.
	 */
	public void establecerDatos(DCVS datos) {
		//Asociar a su textfield.
		String tipo = datos.getTipo();
		JTextField tf = null;
		//Actualizar el campo con la ruta del archivo.
		if(mapaFields.containsKey(tipo)) {
			tf = mapaFields.get(tipo);
			tf.setText(datos.getRuta());
		}
		
		//Guardar los datos del módulo.
		mapaModulos.put(tipo, datos);

		switch(tipo) {
			case (IO.PRJ):
				abrirProyecto(null);
				break;
			case (IO.MAP):
				cMap.setPoligonos(datos.getModelo());
				break;
			default:
				mapaBotonesGuardar.get(IO.PRJ).setEnabled(true);
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
		if(contador == 5) contador = 0;
		int posY = lineaBase + 30*contador;
		contador++;
		int w = 105;
		int posX = 12;
		jl = new JLabel(nombre);
		addIconL(jl,ruta,wi,hi);
		jl.setBounds(posX, posY, w, hi);
		panel.add(jl);
	}
	
	private JTextField iniciarTextField(JTextField jtf){
		if(contador == 5) contador = 0;
		int posY = lineaBase + 30*contador;
		contador++;
		jtf = new JTextField();
		jtf.setEditable(false);
		jtf.setEnabled(true);
		jtf.setBounds(129,posY,338,hi);
		jtf.setColumns(10);
		jtf.setToolTipText("Archivo seleccionado");
		jtf.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(jtf);
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
	 * @param posX Posición en la coordenada X del panel.
	 * @param activado Habilita o desabilita incialmente el botón, TRUE activado, False en otro caso.
	 * @return la propia instancia del botón.
	 */
	private JButton iniciarBoton(JButton boton, String nombre,String tt,  String ruta, int posX, boolean activado) {
		if(contador == 5) contador = 0;
		int posY = lineaBase + 30*contador;
		contador++;
		boton = new JButton(nombre);
		boton.addMouseListener(new ArchivoML());
		boton.setToolTipText(tt);
		addIconB(boton,ruta,wi,hi);
		boton.setBounds(posX, posY, 27, hi);
		boton.setEnabled(activado);
		boton.setBorder(null);
		panel.add(boton);
		return boton;
	}

	/**
	 * <p>Title: inicializarMapas</p>  
	 * <p>Description: Inicializa los mapas de los elementos característicos</p>
	 */
	private void inicializarMapas() {
		mapaBotonesAbrir.put(IO.PRJ, btnAbrirPRJ);
		mapaBotonesAbrir.put(IO.MAP, btnAbrirMapa);
		mapaBotonesAbrir.put(IO.REL, btnAbrirRelaciones);
		mapaBotonesAbrir.put(IO.PAL, btnAbrirPaleta);
		mapaBotonesAbrir.put(IO.HST, btnAbrirHistorico);
		
		mapaBotonesGuardar.put(IO.PRJ, btnGuardarPRJ);
		mapaBotonesGuardar.put(IO.MAP, btnGuardarMapa);
		mapaBotonesGuardar.put(IO.REL, btnGuardarRelaciones);
		mapaBotonesGuardar.put(IO.PAL, btnGuardarPaleta);
		mapaBotonesGuardar.put(IO.HST, btnGuardarHistorico);
		
		mapaFields.put(IO.PRJ, txtFieldProyecto);
		mapaFields.put(IO.MAP, textFieldMapa);
		mapaFields.put(IO.REL, textFieldRelaciones);
		mapaFields.put(IO.PAL, textFieldLeyenda);
		mapaFields.put(IO.HST, textFieldHistorico);
	}
		
	/* Clases privadas */
	
	private class ArchivoML extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			DCVS dcvs = null;			
			JButton btn = (JButton) e.getSource();								//Identificar botón.			
			String op = btn.getToolTipText().split(" ")[0];						//Identificar lectura o guardado.
			String ext = btn.getToolTipText().split(" ")[1].toLowerCase();		//Los valores de tipos van en minúscula.
			
			//Carga de módulo.
			if(op.equals("Abrir")) {											
				dcvs = cIO.abrirArchivo(null,ext);
				if(dcvs != null) {establecerDatos(dcvs);}
			//Guarga de módulo.
			}else if(btn.isEnabled()){											//Comprobación de guardado activado.		
				switch(ext) {
				case (IO.PRJ):
					guardarProyecto();
					break;
				default:
					guardarModulo(mapaModulos.get(ext));
				}
			}
		}
	}
	
}
